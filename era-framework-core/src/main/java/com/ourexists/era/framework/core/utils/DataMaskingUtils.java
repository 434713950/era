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

package com.ourexists.era.framework.core.utils;

import java.util.Arrays;

/**
 * @author pengcheng
 * @date 2021/7/1 14:30
 * @since
 */
public class DataMaskingUtils {

    private DataMaskingUtils() {
    }

    public static String maskIdCard(String idNo) {
        return mask(idNo, 6, 4);
    }

    public static String maskMobile(String idNo) {
        return mask(idNo, 3, 4);
    }


    public static String mask(String str, int retainHeadLength, int retainTailLength) {
        int length = str.length();
        int remainderLength = length - retainHeadLength - retainTailLength;
        if (remainderLength <= 0) {
            return str;
        }
        String s = str.substring(0, retainHeadLength);
        String e = str.substring(length - retainTailLength);
        byte[] mb = new byte[remainderLength];
        Arrays.fill(mb, (byte) '*');
        return s + new String(mb) + e;
    }
}
