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

package com.ourexists.era.framework.core.constants;

/**
 * 常用正则常量
 * @author PengCheng
 * @date 2018/5/9
 */
public interface RegexConstants {

    /**
     * 密码(以字母开头，长度在8~16之间，只能包含字母、数字)
     */
    String PASSWORD = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$";

    /**
     * 强密码(必须包含大小写字母、数字、特殊字符的组合，长度在8-16之间)
     */
    String STORAGE_PASSWORD = "^(?=.*\\d)(?=.*[a-zA-Z])(?=.*[~!@#$%^&*.])[\\da-zA-Z~!@#$%^&*.]{8,16}$";

    /**
     * e-mail
     */
    String EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

    /**
     * 身份证
     */
    String ID_CARD = "^\\d{15}|\\d{18}$";

    /**
     * 手机号码
     */
    String MOBILE = "^(13[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\\d{8}$";

    /**
     *  电话号码("XXX-XXXXXXX"、"XXXX-XXXXXXXX"、"XXX-XXXXXXX"、"XXX-XXXXXXXX"、"XXXXXXX"和"XXXXXXXX)
     */
    String PHONE = "^(\\(\\d{3,4}-)|\\d{3.4}-)?\\d{7,8}$";

    /**
     * 日期格式
     */
    String DATE = "^\\d{4}-\\d{1,2}-\\d{1,2}";

    /**
     * ip地址
     */
    String IP = "\\d+\\.\\d+\\.\\d+\\.\\d+";
}
