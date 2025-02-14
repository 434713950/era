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

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author pengcheng
 * @version 1.0.0
 * @date 2023/6/13 14:28
 * @since 1.0.0
 */
@Slf4j
public class AliOssTemplate extends OssTemplate {

    private final OSS oss;

    public AliOssTemplate(OssProperties ossProperties) {
        super(ossProperties);
        this.oss = new OSSClientBuilder()
                .build(ossProperties.getUploadEndpoint(), ossProperties.getAccesskeyId(), ossProperties.getAccesskeySecret());
    }

    @Override
    public void upload_0(InputStream is, String uploadFileName) {
        this.oss.putObject(ossProperties.getBucketName(), uploadFileName, is);
    }

    @Override
    public void delete(String fileName) {
        this.oss.deleteObject(ossProperties.getBucketName(), fileName);
    }

    @Override
    public InputStream getFileStream(String fileName) {
        return this.oss.getObject(ossProperties.getBucketName(), fileName).getObjectContent();
    }

    @Override
    public void destroy() throws Exception {
        if (this.oss != null) {
            this.oss.shutdown();
        }
    }

    @Override
    public URL generatePresignedUrl(String fileName, Date expiration) {
        return this.oss.generatePresignedUrl(ossProperties.getBucketName(), fileName, expiration);
    }

    @Override
    public String initiateMultipartUpload(String fileName) {
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(ossProperties.getBucketName(), fileName);
        InitiateMultipartUploadResult upresult = this.oss.initiateMultipartUpload(request);
        return upresult.getUploadId();
    }

    @Override
    public UploadPartR uploadPart(InputStream is, String fileName, String uploadId, Integer partNum) {
        UploadPartRequest uploadPartRequest = new UploadPartRequest();
        uploadPartRequest.setBucketName(ossProperties.getBucketName());
        uploadPartRequest.setKey(fileName);
        uploadPartRequest.setUploadId(uploadId);
        uploadPartRequest.setInputStream(is);
        uploadPartRequest.setPartNumber(partNum);
        UploadPartResult uploadPartR = this.oss.uploadPart(uploadPartRequest);
        UploadPartR r= new UploadPartR();
        BeanUtils.copyProperties(uploadPartR, r);
        return r;
    }


    @Override
    public void completeMultipartUpload(String fileName, String uploadId, List<UploadPartR> uploadPartRS) {
        List<PartETag> partETags = new ArrayList<>();
        for (UploadPartR uploadPartR : uploadPartRS) {
            PartETag partETag = new PartETag(uploadPartR.getPartNumber(), uploadPartR.getETag());
            partETags.add(partETag);
        }
        CompleteMultipartUploadRequest completeMultipartUploadRequest =
                new CompleteMultipartUploadRequest(ossProperties.getBucketName(), fileName, uploadId, partETags);
        this.oss.completeMultipartUpload(completeMultipartUploadRequest);
    }
}
