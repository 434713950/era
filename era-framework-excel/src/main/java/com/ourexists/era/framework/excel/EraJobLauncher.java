package com.ourexists.era.framework.excel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.metrics.BatchMetrics;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.util.Assert;

import java.time.Duration;

public class EraJobLauncher implements JobLauncher {

    protected static final Log logger = LogFactory.getLog(EraJobLauncher.class);

    private JobRepository jobRepository;

    private TaskExecutor taskExecutor;

    /**
     * Run the provided job with the given {@link JobParameters}. The
     * {@link JobParameters} will be used to determine if this is an execution
     * of an existing job instance, or if a new one should be created.
     *
     * @param job the job to be run.
     * @param jobParameters the {@link JobParameters} for this particular
     * execution.
     * @return the {@link JobExecution} if it returns synchronously. If the
     * implementation is asynchronous, the status might well be unknown.
     * @throws JobExecutionAlreadyRunningException if the JobInstance already
     * exists and has an execution already running.
     * @throws JobRestartException if the execution would be a re-start, but a
     * re-start is either not allowed or not needed.
     * @throws JobInstanceAlreadyCompleteException if this instance has already
     * completed successfully
     * @throws JobParametersInvalidException thrown if jobParameters is invalid.
     */
    @Override
    public JobExecution run(final Job job, final JobParameters jobParameters)
            throws JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException,
            JobParametersInvalidException {

        Assert.notNull(job, "The Job must not be null.");
        Assert.notNull(jobParameters, "The JobParameters must not be null.");

        final JobExecution jobExecution;
        JobExecution lastExecution = jobRepository.getLastJobExecution(job.getName(), jobParameters);
        if (lastExecution != null) {
            if (!job.isRestartable()) {
                throw new JobRestartException("JobInstance already exists and is not restartable");
            }
            /*
             * validate here if it has stepExecutions that are UNKNOWN, STARTING, STARTED and STOPPING
             * retrieve the previous execution and check
             */
            for (StepExecution execution : lastExecution.getStepExecutions()) {
                BatchStatus status = execution.getStatus();
                if (status.isRunning() || status == BatchStatus.STOPPING) {
                    throw new JobExecutionAlreadyRunningException("A job execution for this job is already running: "
                            + lastExecution);
                } else if (status == BatchStatus.UNKNOWN) {
                    throw new JobRestartException(
                            "Cannot restart step [" + execution.getStepName() + "] from UNKNOWN status. "
                                    + "The last execution ended with a failure that could not be rolled back, "
                                    + "so it may be dangerous to proceed. Manual intervention is probably necessary.");
                }
            }
        }

        // Check the validity of the parameters before doing creating anything
        // in the repository...
        job.getJobParametersValidator().validate(jobParameters);

        /*
         * There is a very small probability that a non-restartable job can be
         * restarted, but only if another process or thread manages to launch
         * <i>and</i> fail a job execution for this instance between the last
         * assertion and the next method returning successfully.
         */
        jobExecution = jobRepository.createJobExecution(job.getName(), jobParameters);

        try {
            taskExecutor.execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        if (logger.isInfoEnabled()) {
                            logger.info("Job: [" + job + "] launched with the following parameters: [" + jobParameters
                                    + "]");
                        }
                        job.execute(jobExecution);
                        if (logger.isInfoEnabled()) {
                            Duration jobExecutionDuration = BatchMetrics.calculateDuration(jobExecution.getStartTime(), jobExecution.getEndTime());
                            logger.info("Job: [" + job + "] completed with the following parameters: [" + jobParameters
                                    + "] and the following status: [" + jobExecution.getStatus() + "]"
                                    + (jobExecutionDuration == null ? "" : " in " + BatchMetrics.formatDuration(jobExecutionDuration)));
                        }
                    } catch (Throwable t) {
                        if (logger.isInfoEnabled()) {
                            logger.info("Job: [" + job
                                    + "] failed unexpectedly and fatally with the following parameters: [" + jobParameters
                                    + "]", t);
                        }
                        rethrow(t);
                    }
                }

                private void rethrow(Throwable t) {
                    if (t instanceof RuntimeException) {
                        throw (RuntimeException) t;
                    } else if (t instanceof Error) {
                        throw (Error) t;
                    }
                    throw new IllegalStateException(t);
                }
            });
        } catch (TaskRejectedException e) {
            jobExecution.upgradeStatus(BatchStatus.FAILED);
            if (jobExecution.getExitStatus().equals(ExitStatus.UNKNOWN)) {
                jobExecution.setExitStatus(ExitStatus.FAILED.addExitDescription(e));
            }
            jobRepository.update(jobExecution);
        }

        return jobExecution;
    }

    /**
     * Set the JobRepository.
     *
     * @param jobRepository instance of {@link JobRepository}.
     */
    public void setJobRepository(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    /**
     * Set the TaskExecutor. (Optional)
     *
     * @param taskExecutor instance of {@link TaskExecutor}.
     */
    public void setTaskExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

}
