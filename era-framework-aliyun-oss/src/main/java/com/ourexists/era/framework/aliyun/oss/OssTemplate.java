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

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.List;

@Slf4j
public abstract class OssTemplate implements DisposableBean {

    protected final OssProperties ossProperties;

    private final static String HTTP_VALUE = "http://";
    private final static String HTTPS_VALUE = "https://";
    private final static String PIC_AS = ",";

    public OssTemplate(OssProperties ossProperties) {
        this.ossProperties = ossProperties;
    }


    /**
     * 从上传路径中剪裁图片路径。
     * 运营端都是web进行直接上传的
     *
     * @param picUrl 图片路径
     * @return 剪裁后的图片路径
     */
    public String cutPicUrl(String picUrl) {
        String fileName = picUrl;
        //去除前缀
        if (fileName.contains(HTTP_VALUE)) {
            fileName = picUrl.replace(HTTP_VALUE + ossProperties.getBucketName() + "." + ossProperties.getUploadEndpoint() + "/", "");
        }
        if (fileName.contains(HTTPS_VALUE)) {
            fileName = picUrl.replace(HTTPS_VALUE + ossProperties.getBucketName() + "." + ossProperties.getUploadEndpoint() + "/", "");
        }
        //去除后缀
        if (fileName.contains("?")) {
            fileName = fileName.split("\\?")[0];
        }
        return fileName;
    }

    /**
     * 从访问路径中提取图片剪裁路径
     *
     * @param pic 图片访问路径
     * @return 图片剪裁路径
     */
    public String extractPicUrl(String pic) {
        String fileName = pic;
        if (fileName.contains(HTTP_VALUE)) {
            fileName = pic.replace(HTTP_VALUE + ossProperties.getAccessEndpoint().replace(HTTP_VALUE, "").replace(HTTPS_VALUE, "") + "/", "");
        }
        if (fileName.contains(HTTPS_VALUE)) {
            fileName = pic.replace(HTTPS_VALUE + ossProperties.getAccessEndpoint().replace(HTTP_VALUE, "").replace(HTTPS_VALUE, "") + "/", "");
        }
        //去除后缀
        if (fileName.contains("?")) {
            fileName = fileName.split("\\?")[0];
        }
        return fileName;
    }

    /**
     * 拼接oss url地址
     *
     * @param pic
     * @return
     */
    public String assemblePicUrls(String pic) {
        return assemblePicUrls(pic, null);
    }

    /**
     * 生成可访问的https全路径地址
     *
     * @param pic          数据库存储地址
     * @param folderPrefix 文件夹前缀，前后均不包含"/"
     * @return
     */
    public String assemblePicUrls(String pic, String folderPrefix) {
        if (StringUtils.isEmpty(pic) || pic.contains(HTTPS_VALUE)) {
            return pic;
        }
        if (pic.contains(PIC_AS)) {
            String[] splits = pic.split(PIC_AS);
            if (splits.length > 1) {
                StringBuilder pics = new StringBuilder();
                for (int i = 0; i < splits.length; i++) {
                    if (i > 0) {
                        pics.append(",");
                    }
                    pics.append(assemblePicUrl(splits[i], folderPrefix));
                }
                return pics.toString();
            }
        }
        return assemblePicUrl(pic, folderPrefix);
    }

    public String assemblePicUrl(String pic) {
        return assemblePicUrl(pic, null);
    }

    public String assemblePicUrl(String pic, String folderPrefix) {
        String fileName = pic;
        //处理存储不对,提取文件原名
        fileName = cutPicUrl(fileName);
        fileName = extractPicUrl(fileName);
        //处理之后路径还是不会不进行下一步处理
        if (fileName.contains(HTTP_VALUE) || fileName.contains(HTTPS_VALUE)) {
            return fileName;
        }
        if (StringUtils.isNotEmpty(folderPrefix)) {
            fileName = folderPrefix + "/" + pic;
        }
        if (ossProperties.getPrivateAccess()) {
            URL url = generatePresignedUrl(fileName, new Date(new Date().getTime() + this.ossProperties.getAccessExpire()));
            fileName = url.toString();
            if (fileName != null) {
                //去除前缀
                if (fileName.contains(HTTP_VALUE)) {
                    fileName = fileName.replace(HTTP_VALUE + ossProperties.getBucketName() + "." + ossProperties.getUploadEndpoint() + "/", "");
                }
                if (fileName.contains(HTTPS_VALUE)) {
                    fileName = fileName.replace(HTTPS_VALUE + ossProperties.getBucketName() + "." + ossProperties.getUploadEndpoint() + "/", "");
                }
            }
        }
        //未私密访问
        StringBuilder url = new StringBuilder();
        if (ossProperties.getAccessEndpoint().startsWith(HTTP_VALUE)
                || ossProperties.getAccessEndpoint().startsWith(HTTPS_VALUE)) {
            url.append(ossProperties.getAccessEndpoint()).append("/");
        } else {
            url.append(HTTPS_VALUE).append(ossProperties.getAccessEndpoint()).append("/");
        }
        url.append(fileName);
        return url.toString();
    }

    /**
     * 上传文件
     *
     * @param file
     * @param url
     * @return
     */
    public String upload(MultipartFile file, String url) throws UploadException {
        return upload(file.getName(), file, url);
    }

    public String upload(String fileName, MultipartFile file, String url) throws UploadException {
        // 设置文件上传到oss路径和名称
        try {
            return upload(file.getBytes(), fileName, url + "/");
        } catch (IOException e) {
            log.info("【oss上传】上传失败!", e);
            throw new UploadException("文件上传失败!");
        }
    }

    public String upload(byte[] bytes, String uploadFileName, String dir) throws UploadException {
        File tmpFile = null;
        FileInputStream fis = null;
        dir = dir.startsWith("/") ? dir.substring(1) : dir;
        String path = dir + uploadFileName;
        try {
            tmpFile = new File(uploadFileName);
            FileCopyUtils.copy(bytes, tmpFile);
            fis = new FileInputStream(tmpFile);
            return upload(fis, path);
        } catch (IOException e) {
            log.error("【oss上传】字节码上传错误!", e);
            throw new UploadException("【oss上传】上传失败!");
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    //nothing
                }
            }
            if (tmpFile != null && tmpFile.exists()) {
                if (!tmpFile.delete()) {
                    log.error("【oss上传】文件[{}]清理失败!", tmpFile.getAbsoluteFile());
                }
            }
            log.info("【oss上传】结束,删除临时文件");
        }
    }

    public String upload(InputStream is, String uploadFileName) throws UploadException {
        upload_0(is, uploadFileName);
        return assemblePicUrl(uploadFileName);
    }

    protected abstract void upload_0(InputStream is, String uploadFileName) throws UploadException;

    public String upload(Workbook workbook, String fileName) throws UploadException {
        ByteArrayOutputStream fos = null;
        ByteArrayInputStream fis = null;
        try {
            fos = new ByteArrayOutputStream();
            workbook.write(fos);
            fis = new ByteArrayInputStream(fos.toByteArray());
            return upload(fis, fileName);
        } catch (IOException e) {
            throw new UploadException("【excel工具】导出失败!");
        } finally {
            IOUtils.closeQuietly(fis);
            IOUtils.closeQuietly(fos);
            try {
                workbook.close();
            } catch (IOException e) {
                //nothing
            }
        }
    }

    public abstract void delete(String fileName) throws UploadException;


    public abstract InputStream getFileStream(String fileName) throws UploadException;

    /**
     * 生成签名路径
     *
     * @param objectName 文件名
     * @param expiration 过期时间
     * @return
     */
    public URL generatePresignedUrl(String objectName, Date expiration) {
        return null;
    }


    /**
     * 初始化分片
     * @param fileName 文件名
     * @return 分片id
     */
    public abstract String initiateMultipartUpload(String fileName);


    /**
     * 上传分片
     * @param is            文件流
     * @param fileName      文件名
     * @param uploadId      上传id
     * @param partNum       分片编号
     * @return
     */
    public abstract UploadPartR uploadPart(InputStream is, String fileName, String uploadId, Integer partNum);

    /**
     * 完成分片上传
     * @param fileName      文件名
     * @param uploadId      上传id
     * @param uploadPartRS  分片结果集合
     */
    public abstract  void completeMultipartUpload(String fileName, String uploadId, List<UploadPartR> uploadPartRS);
}
