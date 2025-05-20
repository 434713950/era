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


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESEncrypt {

    private static final String DEFAULT_CHARSET = "UTF-8";
    private static final String KEY_AES = "AES";
    private static final String CBC_CIPHER_NOPADDING = "AES/CBC/PKCS5Padding";
    final private static String IV = "MLSNKNVZW9HDPRDC";

    /**
     * 加密方法
     * @param data  要加密的数据
     * @param key 加密key
     * @param iv 加密iv
     * @return 加密的结果
     * @throws Exception
     */
    public static String encrypt(String data, String key, String iv) throws Exception {
        Cipher cipher = Cipher.getInstance(CBC_CIPHER_NOPADDING);
        byte[] dataBytes = data.getBytes(DEFAULT_CHARSET);
        SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), KEY_AES);
        IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
        byte[] encrypted = cipher.doFinal(dataBytes);
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * 解密方法
     * @param data 要解密的数据
     * @param key  解密key
     * @param iv 解密iv
     * @return 解密的结果
     * @throws Exception
     */
    private static String desEncrypt(String data, String key, String iv) throws Exception {
        byte[] encrypted = Base64.getDecoder().decode(data);
        Cipher cipher = Cipher.getInstance(CBC_CIPHER_NOPADDING);
        SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), KEY_AES);
        IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
        byte[] original = cipher.doFinal(encrypted);
        return new String(original, DEFAULT_CHARSET);
    }

    /**
     * 使用默认的key和iv加密
     * @param data
     * @return
     * @throws Exception
     */
    public static String AESEncode(String key, String data) throws Exception {
        return encrypt(data, key, IV);
    }

    /**
     * 使用默认的key和iv解密
     * @param data
     * @return
     * @throws Exception
     */
    public static String AESDecode(String key, String data) throws Exception {
        return desEncrypt(data, key, IV);
    }
}
