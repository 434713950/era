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

package com.ourexists.era.framework.aliyun.oss;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author pengcheng
 * @date 2021/9/1 15:47
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "era.aliyun.file")
public class OssProperties {

    private String type;

    private String uploadEndpoint;

    private String accessEndpoint;

    private String bucketName;

    private String accesskeyId;

    private String accesskeySecret;

    private Long partSize = 102400L;

    private Boolean privateAccess = true;

    private Long accessExpire = 3600000L;

    public Long getPartSize() {
        return partSize;
    }

    public void setPartSize(Long partSize) {
        this.partSize = partSize;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUploadEndpoint() {
        return uploadEndpoint;
    }

    public void setUploadEndpoint(String uploadEndpoint) {
        this.uploadEndpoint = uploadEndpoint;
    }

    public String getAccessEndpoint() {
        return accessEndpoint;
    }

    public void setAccessEndpoint(String accessEndpoint) {
        this.accessEndpoint = accessEndpoint;
    }

    public String getAccesskeyId() {
        return accesskeyId;
    }

    public void setAccesskeyId(String accesskeyId) {
        this.accesskeyId = accesskeyId;
    }

    public String getAccesskeySecret() {
        return accesskeySecret;
    }

    public void setAccesskeySecret(String accesskeySecret) {
        this.accesskeySecret = accesskeySecret;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public Boolean getPrivateAccess() {
        return privateAccess;
    }

    public void setPrivateAccess(Boolean privateAccess) {
        this.privateAccess = privateAccess;
    }

    public Long getAccessExpire() {
        return accessExpire;
    }

    public void setAccessExpire(Long accessExpire) {
        this.accessExpire = accessExpire;
    }
}
