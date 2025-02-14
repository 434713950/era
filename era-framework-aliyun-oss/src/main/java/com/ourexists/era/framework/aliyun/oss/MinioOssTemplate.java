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

import com.j256.simplemagic.ContentInfo;
import com.j256.simplemagic.ContentInfoUtil;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.util.List;

/**
 * @author pengcheng
 * @version 1.0.0
 * @date 2023/6/13 14:58
 * @since 1.0.0
 */
@Slf4j
public class MinioOssTemplate extends OssTemplate {

    private MinioClient client;

    public MinioOssTemplate(OssProperties ossProperties) {
        super(ossProperties);
        this.client = MinioClient.builder()
                .endpoint(ossProperties.getUploadEndpoint())
                .credentials(ossProperties.getAccesskeyId(), ossProperties.getAccesskeySecret())
                .build();
    }

    @Override
    public void upload_0(InputStream is, String uploadFileName) throws UploadException {
        ContentInfo info = ContentInfoUtil.findExtensionMatch(uploadFileName);
        PutObjectArgs args = PutObjectArgs
                .builder()
                .contentType(info.getMimeType())
                .bucket(this.ossProperties.getBucketName())
                .object(uploadFileName)
                .stream(is, -1, this.ossProperties.getPartSize())
                .build();
        try {
            this.client.putObject(args);
        } catch (Exception e) {
            log.error("minio上传失败!", e);
            throw new UploadException("文件上传失败!");
        }
    }

    @Override
    public void delete(String fileName) throws UploadException {
        try {
            this.client.removeObject(
                    RemoveObjectArgs
                            .builder()
                            .bucket(this.ossProperties.getBucketName())
                            .object(fileName)
                            .build());
        } catch (Exception e) {
            log.error("minio文件删除失败！", e);
            throw new UploadException("文件删除失败!");
        }
    }

    @Override
    public InputStream getFileStream(String fileName) throws UploadException {
        GetObjectArgs objectArgs = GetObjectArgs
                .builder()
                .bucket(this.ossProperties.getBucketName())
                .object(fileName)
                .build();
        try {
            return this.client.getObject(objectArgs);
        } catch (Exception e) {
            log.error("minio下载文件流失败！", e);
            throw new UploadException("文件下载失败!");
        }
    }

    @Override
    public String initiateMultipartUpload(String fileName) {
//        CreateMultipartUploadResponse response =
//                client.createMultipartUpload(ossProperties.getBucketName(), region, object, headers, extraQueryParams);
//        return response.result().uploadId();
        return null;
    }

    @Override
    public UploadPartR uploadPart(InputStream is, String fileName, String uploadId, Integer partNum) {
        return null;
    }

    @Override
    public void completeMultipartUpload(String fileName, String uploadId, List<UploadPartR> uploadPartRS) {

    }

    @Override
    public void destroy() throws Exception {
        if (this.client!=null) {
            this.client.traceOff();
        }
    }
}
