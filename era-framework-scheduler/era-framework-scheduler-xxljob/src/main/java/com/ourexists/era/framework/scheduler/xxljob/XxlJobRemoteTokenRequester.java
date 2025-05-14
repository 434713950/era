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

import com.ourexists.era.framework.core.utils.CollectionUtil;
import com.ourexists.era.framework.scheduler.core.accesstoken.JobRemoteTokenRequester;
import com.ourexists.era.framework.scheduler.core.accesstoken.execption.JobException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author pengcheng
 * @version 1.1.0
 * @date 2023/2/24 15:44
 * @since 1.1.0
 */
public class XxlJobRemoteTokenRequester implements JobRemoteTokenRequester {

    private final RestTemplate restTemplate;

    public XxlJobRemoteTokenRequester(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public String gainToken(String appId, String appSecret, String uri) throws JobException {
        String path = XxlJobConstants.PATH_LOGIN + "?userName=" + appId + "&password=" + appSecret;
        HttpHeaders requestHeaders = new HttpHeaders();
        HttpEntity<String> requestEntity = new HttpEntity<>(null, requestHeaders);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(uri + path, HttpMethod.POST, requestEntity, String.class);
        HttpHeaders httpHeaders = responseEntity.getHeaders();
        List<String> cookies = httpHeaders.get("Set-Cookie");
        if (CollectionUtils.isEmpty(cookies)) {
            cookies = httpHeaders.get("Cookie");
            if (CollectionUtils.isEmpty(cookies)) {
                throw new JobException("【xxl-job remote】login fail");
            }
        }
        return CollectionUtil.join(cookies, XxlJobConstants.COOKIE_SPLIT);
    }
}
