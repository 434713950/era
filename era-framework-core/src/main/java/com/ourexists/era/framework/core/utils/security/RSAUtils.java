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

package com.ourexists.era.framework.core.utils.security;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.codec.Base64Encoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * @author pengcheng
 * @date 2022/2/23 14:57
 * @since 1.0.0
 */
@Slf4j
public class RSAUtils {

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 获取密钥对
     *
     * @return java.security.KeyPair
     * @date 2021/6/7 15:32
     * @author ChenYongJia
     * @version 1.0
     */
    public static KeyPair getKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(SecurityConstants.RSA_ALGORITHM);
        generator.initialize(1024);
        return generator.generateKeyPair();
    }

    /**
     * 获取私钥
     *
     * @param privateKey 私钥字符串
     * @return java.security.PrivateKey
     * @date 2021/6/7 15:32
     * @author ChenYongJia
     * @version 1.0
     */
    public static PrivateKey getPrivateKey(String privateKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(SecurityConstants.RSA_ALGORITHM);
        byte[] decodedKey = Base64Decoder.decode(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 获取公钥
     *
     * @param publicKey 公钥字符串
     * @param publicKey
     * @return java.security.PublicKey
     * @date 2021/6/7 15:32
     * @author ChenYongJia
     * @version 1.0
     */
    public static PublicKey getPublicKey(String publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(SecurityConstants.RSA_ALGORITHM);
        byte[] decodedKey = Base64Decoder.decode(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * RSA加密
     * @param data         待加密数据
     * @param publicKey     公钥字符串
     * @return  加密后的数据
     * @throws Exception
     */
    public static String encrypt(String data, String publicKey) throws Exception {
        return encrypt(data, getPublicKey(publicKey));
    }

    /**
     * RSA加密
     * @param data      待加密数据
     * @param publicKey 公钥
     * @return 加密后的数据
     * @version 1.0
     */
    public static String encrypt(String data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(SecurityConstants.RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        int inputLen = data.getBytes().length;
        try(ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            int offset = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段加密
            while (inputLen - offset > 0) {
                if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                    cache = cipher.doFinal(data.getBytes(), offset, MAX_ENCRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(data.getBytes(), offset, inputLen - offset);
                }
                out.write(cache, 0, cache.length);
                i++;
                offset = i * MAX_ENCRYPT_BLOCK;
            }
            byte[] encryptedData = out.toByteArray();
            // 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
            // 加密后的字符串
            return Base64Encoder.encode(encryptedData);
        }
    }


    /**
     * RSA解密
     * @param data          待解密数据
     * @param privateKey    私钥
     * @return  java.lang.String
     * @throws Exception
     */
    public static String decrypt(String data, String privateKey) throws Exception{
        return decrypt(data, getPrivateKey(privateKey));
    }

    /**
     * RSA解密
     *
     * @param data       待解密数据
     * @param privateKey 私钥
     * @return java.lang.String
     */
    public static String decrypt(String data, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(SecurityConstants.RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] dataBytes = Base64.decodeBase64(data);
        int inputLen = dataBytes.length;
        try(ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            int offset = 0;
            byte[] cache;
            int i = 0;
            //对数据分段解密
            while (inputLen - offset > 0) {
                if (inputLen - offset > MAX_DECRYPT_BLOCK) {
                    cache = cipher.doFinal(dataBytes, offset, MAX_DECRYPT_BLOCK);
                } else {
                    cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
                }
                out.write(cache, 0, cache.length);
                i++;
                offset = i * MAX_DECRYPT_BLOCK;
            }
            byte[] decryptedData = out.toByteArray();
            return new String(decryptedData, StandardCharsets.UTF_8);
        }
    }

    /**
     * 签名
     *
     * @param data       待签名数据
     * @param privateKey 私钥
     * @return java.lang.String
     * @date 2021/6/7 15:33
     * @author ChenYongJia
     * @version 1.0
     */
//    public static String sign(String data, PrivateKey privateKey) throws Exception {
//        byte[] keyBytes = privateKey.getEncoded();
//        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
//        KeyFactory keyFactory = KeyFactory.getInstance(SecurityConstants.RSA_ALGORITHM);
//        PrivateKey key = keyFactory.generatePrivate(keySpec);
//        Signature signature = Signature.getInstance(Constant.MD5_RSA);
//        signature.initSign(key);
//        signature.update(data.getBytes());
//        return Base64Encoder.encode(signature.sign());
//    }

    /**
     * 验签
     *
     * @param srcData   原始字符串
     * @param publicKey 公钥
     * @param sign      签名
     * @return boolean
     * @date 2021/6/7 15:33
     * @author ChenYongJia
     * @version 1.0
     */
//    public static boolean verify(String srcData, PublicKey publicKey, String sign) throws Exception {
//        byte[] keyBytes = publicKey.getEncoded();
//        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
//        KeyFactory keyFactory = KeyFactory.getInstance(SecurityConstants.RSA_ALGORITHM);
//        PublicKey key = keyFactory.generatePublic(keySpec);
//        Signature signature = Signature.getInstance(Constant.MD5_RSA);
//        signature.initVerify(key);
//        signature.update(srcData.getBytes());
//        return signature.verify(Base64Decoder.decode(sign.getBytes()));
//    }

//    public static void main(String[] args) {
//        try {
//            // 生成密钥对
//            KeyPair keyPair = getKeyPair();
//            String privateKey = Base64Encoder.encode(keyPair.getPrivate().getEncoded());
//            String publicKey = Base64Encoder.encode(keyPair.getPublic().getEncoded());
//            log.info("私钥:" + privateKey);
//            log.info("公钥:" + publicKey);
//            // RSA加密
//            String data = "123456";
//            String encryptData = encrypt(data, getPublicKey(publicKey));
//            log.info("加密后内容:" + encryptData);
//            // RSA解密
//            String decryptData = decrypt(encryptData, getPrivateKey(privateKey));
//            log.info("解密后内容:" + decryptData);
//            // RSA签名
//            String sign = sign(data, getPrivateKey(privateKey));
//            // RSA验签
//            boolean result = verify(data, getPublicKey(publicKey), sign);
//            log.info("验签结果:" + result);
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error("RSA加解密异常");
//        }
//    }
}
