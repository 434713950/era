/*
 * Copyright (C) 2025  ChengPeng
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as published
 *     by the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.ourexists.era.framework.scheduler.xxljob;

import com.ourexists.era.framework.scheduler.core.accesstoken.Job;
import com.ourexists.era.framework.scheduler.core.accesstoken.JobProperties;
import com.ourexists.era.framework.scheduler.core.accesstoken.execption.JobException;
import com.ourexists.era.framework.scheduler.core.accesstoken.manager.JobAccessTokenManger;
import com.ourexists.era.framework.scheduler.core.accesstoken.template.AbstractJobOperateTemplate;
import com.ourexists.era.framework.scheduler.xxljob.support.JobGroupPageReq;
import com.ourexists.era.framework.scheduler.xxljob.support.JobGroupPageResp;
import com.ourexists.era.framework.scheduler.xxljob.support.ResponseParam;
import com.ourexists.era.framework.scheduler.xxljob.support.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pengcheng
 * @version 1.1.0
 * @date 2023/2/24 15:51
 * @since 1.1.0
 */
@Slf4j
public class XxlJobOperateTemplate extends AbstractJobOperateTemplate {

    private final RestTemplate restTemplate;

    public XxlJobOperateTemplate(RestTemplate restTemplate,
                                 JobAccessTokenManger jobAccessTokenManger,
                                 JobProperties jobProperties) {
        super(jobAccessTokenManger, jobProperties);
        this.restTemplate = restTemplate;
    }


    @Override
    public Integer addJob(Job job) throws JobException {
        Map<String, String> formDatas = new HashMap<>(16);
        XxlJob xxlJob = XxlJob.warp(job);
        Field[] fields = xxlJob.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(xxlJob);
                if (value != null) {
                    formDatas.put(field.getName(), value.toString());
                }
                field.setAccessible(false);
            }
        } catch (IllegalAccessException e) {
            throw new JobException("【xxl-job remote】params spell error");
        }
        ResponseParam result = post(XxlJobConstants.PATH_ADD, formDatas, null, ResponseParam.class);
        if (result == null) {
            throw new JobException("【xxl-job remote】add task fail");
        }
        if (result.getCode() != HttpStatus.OK.value()) {
            throw new JobException("xxl-job remote】add task fail.[" + result.getMsg() + "]");
        }
        log.info("【xxl-job remote】new task build success.[{}]", result);
        return Integer.parseInt(result.getContent());
    }

    @Override
    public boolean deleteJob(String taskId) throws JobException {
        ResponseParam result = post(XxlJobConstants.PATH_REMOVE + "?id=" + taskId, null,
                null, ResponseParam.class);
        return result != null && result.getCode() == HttpStatus.OK.value();
    }

    @Override
    public boolean startJob(int taskId) throws JobException {
        ResponseParam result = post(XxlJobConstants.PATH_START + "?id=" + taskId, null, null, ResponseParam.class);
        return result != null && result.getCode() == HttpStatus.OK.value();
    }

    @Override
    public boolean stopJob(int taskId) throws JobException {
        ResponseParam result = post(XxlJobConstants.PATH_STOP + "?id=" + taskId, null, null, ResponseParam.class);
        return result != null && result.getCode() == HttpStatus.OK.value();
    }

    public JobGroupPageResp selectJobInGroupByPage(JobGroupPageReq req) throws JobException {
        Map<String, String> formdata = new HashMap<>(16);
        if (req.getJobGroup() == null) {
            throw new JobException("【xxl-job remote】请传入jobGroup id");
        }
        try {
            Field[] fields = req.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object fieldVal = field.get(req);
                if (!ObjectUtils.isEmpty(fieldVal)) {
                    formdata.put(field.getName(), fieldVal.toString());
                }
                field.setAccessible(false);
            }
        } catch (IllegalAccessException e) {
            //nothing
        }
        return post(XxlJobConstants.PATH_PAGE, formdata, null, JobGroupPageResp.class);
    }


    public <T, V> V post(String path, Map<String, String> formData, T body, Class<V> respClass) {
        if (!CollectionUtils.isEmpty(formData)) {
            StringBuilder pathBuilder = new StringBuilder(path + "?");
            for (Map.Entry<String, String> entry : formData.entrySet()) {
                pathBuilder.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
            path = pathBuilder.toString();
            path = path.substring(0, path.length() - 1);
        }
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.put("Cookie", cookies());
        HttpEntity<T> requestEntity = new HttpEntity<>(body, requestHeaders);
        return restTemplate.exchange(uri() + path, HttpMethod.POST, requestEntity, respClass).getBody();
    }

    private List<String> cookies() {
        String accessToken = token().getAccessToken();
        return Arrays.asList(accessToken.split(XxlJobConstants.COOKIE_SPLIT));
    }
}
