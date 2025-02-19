package com.ourexists.era.framework.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.handler.WriteHandler;
import com.ourexists.era.framework.oss.OssTemplate;
import com.ourexists.era.framework.oss.UploadPartR;
import com.ourexists.era.framework.core.exceptions.EraCommonException;
import com.ourexists.era.framework.core.utils.CollectionUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public abstract class AbstractExportExecutor<T, R> implements ExportExecutor<T> {

    protected OssTemplate ossTemplate;


    public AbstractExportExecutor(OssTemplate ossTemplate) {
        this.ossTemplate = ossTemplate;
    }

    /**
     * 导出
     *
     * @param exportId  导出id
     * @param condition 导出条件
     * @param fileName  文件名
     * @param excelType 导出的文件类型
     */
    @Override
    public void export(String exportId, T condition, String fileName, ExcelTypeEnum excelType) {
        beforeExport(exportId, condition, fileName, excelType);
        log.info("导出工具：资源id-【{}】，线程名称【{}】", exportId, Thread.currentThread().getName());
        try {
            exportOssMultipart(condition, exportDataClass(), fileName, excelType);
            completeHandle(exportId, condition, fileName, excelType);
        } catch (Exception e) {
            log.error("文件上传异常【{}】", exportId, e);
            failedHandle(exportId, condition, fileName, excelType, e);
        }
    }

    /**
     * @return 文件路径名
     */
    protected abstract String filePath();


    /**
     * 样式处理器，不对csv生效
     *
     * @param condition 条件对象
     * @return
     */
    protected List<WriteHandler> loadWriteHandlers(T condition) {
        return null;
    }

    /**
     * 分片数据查询
     *
     * @param condition            查询条件
     * @param exportPartDataResult 分片结果集合，分页页码使用该实体的partNum字段,最后一页时需要更新isEnd为true
     * @return 查询出的数据
     */
    protected abstract List<R> exportData(T condition, ExportPartDataResult exportPartDataResult) throws EraCommonException;

    /**
     * 导出的数据类型
     *
     * @return
     */
    protected abstract Class<R> exportDataClass();


    /**
     * 导出列，配合easy-excel的标签使用
     *
     * @param condition 条件
     * @return
     */
    protected Collection<Integer> includeColumnIndexes(T condition) {
        return null;
    }


    protected void exportOssMultipart(T condition, Class<R> dataClass, String fileName, ExcelTypeEnum excelType) throws EraCommonException {
        String uploadId = ossTemplate.initiateMultipartUpload(fileName);
        ExportPartDataResult exportPartDataResult = new ExportPartDataResult()
                .setUploadId(uploadId)
                .setPartNum(1)
                .setIsEnd(false)
                .setIncludeColumnIndexes(includeColumnIndexes(condition))
                .setFileName(fileName);
        List<WriteHandler> writeHandlers = loadWriteHandlers(condition);
        List<UploadPartR> uploadPartRS = new ArrayList<>();
        while (!exportPartDataResult.getIsEnd()) {
            beforePartHandle(condition, dataClass, excelType, exportPartDataResult, writeHandlers);
            uploadPartExportData(condition, dataClass, excelType, exportPartDataResult, writeHandlers);
            if (exportPartDataResult.getUploadPartR() != null) {
                uploadPartRS.add(exportPartDataResult.getUploadPartR());
            }
            Integer nextPartNum = exportPartDataResult.getPartNum() + 1;
            exportPartDataResult.setPartNum(nextPartNum);
            afterPartHandle(condition, dataClass, excelType, exportPartDataResult, writeHandlers);
        }
        ossTemplate.completeMultipartUpload(fileName, uploadId, uploadPartRS);
    }

    /**
     * 上传分片导出数据到oss
     *
     * @param condition            条件
     * @param dataClass            数据类
     * @param excelType            excel类型
     * @param exportPartDataResult 导出分片数据集
     * @param writeHandlers        样式
     * @throws EraCommonException
     */
    protected void uploadPartExportData(T condition, Class<R> dataClass, ExcelTypeEnum excelType, ExportPartDataResult exportPartDataResult, List<WriteHandler> writeHandlers) throws EraCommonException {
        List<R> ossList = exportData(condition, exportPartDataResult);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        //导出文件输出流
        ExcelWriterSheetBuilder excelWriterSheetBuilder = EasyExcel
                .write(out, dataClass)
                .relativeHeadRowIndex(1)
                .includeColumnIndexes(exportPartDataResult.getIncludeColumnIndexes())
                .orderByIncludeColumn(true)
                .excelType(excelType)
                .sheet(0);
        //csv不参与样式控件
        if (!excelType.equals(ExcelTypeEnum.CSV) && CollectionUtil.isNotBlank(writeHandlers)) {
            for (WriteHandler writeHandler : writeHandlers) {
                excelWriterSheetBuilder.registerWriteHandler(writeHandler);
            }
        }
        excelWriterSheetBuilder.doWrite(ossList);
        //转换输入流
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(out.toByteArray());
        UploadPartR uploadPartR = ossTemplate.uploadPart(byteArrayInputStream, exportPartDataResult.getFileName(), exportPartDataResult.getUploadId(), exportPartDataResult.getPartNum());
        exportPartDataResult.setUploadPartR(uploadPartR);
    }


    /**
     * 完成后处理
     *
     * @param exportId  导出id
     * @param condition 导出条件
     * @param fileName  文件名
     * @param excelType 导出的文件类型
     */
    protected void completeHandle(String exportId, T condition, String fileName, ExcelTypeEnum excelType) {
    }

    /**
     * 失败处理
     *
     * @param exportId  出id
     * @param condition 导出条件
     * @param fileName  文件名
     * @param excelType 导出的文件类型
     * @param e         失败异常
     */
    protected void failedHandle(String exportId, T condition, String fileName, ExcelTypeEnum excelType, Exception e) {
    }

    /**
     * 导出前处理
     */
    protected void beforeExport(String exportId, T condition, String fileName, ExcelTypeEnum excelType) {
    }

    /**
     * 单次分片前处理
     *
     * @param condition            导出条件
     * @param dataClass            数据类
     * @param excelType            导出的文件类型
     * @param exportPartDataResult 分片数据处理集
     * @param writeHandlers        样式处理器
     */
    protected void beforePartHandle(T condition, Class<R> dataClass, ExcelTypeEnum excelType, ExportPartDataResult exportPartDataResult, List<WriteHandler> writeHandlers) {
    }

    /**
     * 单次分片后处理
     *
     * @param condition            导出条件
     * @param dataClass            数据类
     * @param excelType            导出的文件类型
     * @param exportPartDataResult 分片数据处理集
     * @param writeHandlers        样式处理器
     */
    protected void afterPartHandle(T condition, Class<R> dataClass, ExcelTypeEnum excelType, ExportPartDataResult exportPartDataResult, List<WriteHandler> writeHandlers) {
    }
}
