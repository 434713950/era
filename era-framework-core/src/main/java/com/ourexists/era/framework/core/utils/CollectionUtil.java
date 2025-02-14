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

import java.util.Collection;
import java.util.Iterator;

/**
 * @Author: PengCheng
 * @Description:    容器工具包
 * @Date: 2018/9/12
 */
public class CollectionUtil {

    private CollectionUtil(){}

    /**
     * 判断集合是否为空
     * @param collection
     * @return
     */
    public static boolean isBlank(Collection<?> collection){
        return collection == null || collection.isEmpty();
    }

    /**
     * 判断集合不为空
     * @param collection
     * @return
     */
    public static boolean isNotBlank(Collection<?> collection){
        return !isBlank(collection);
    }

    /**
     * 判断两集合间是否有并集
     * @param collection1
     * @param collection2
     * @return
     */
    public static boolean containAny(Collection<?> collection1,Collection<?> collection2){
        if (isBlank(collection1) || isBlank(collection2)){
            return false;
        }
        if (collection1.size() < collection2.size()) {
            for (Object object : collection1) {
                if (collection2.contains(object)) {
                    return true;
                }
            }
        } else {
            for (Object object : collection2) {
                if (collection1.contains(object)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 以 conjunction 为分隔符将集合转换为字符串<br>
     * 如果集合元素为数组、{@link Iterable}或{@link Iterator}，则递归组合其为字符串
     *
     * @param <T> 集合元素类型
     * @param iterator 集合
     * @param conjunction 分隔符
     * @return 连接后的字符串
     */
    public static <T> String join(Iterator<T> iterator, CharSequence conjunction) {
        if (null == iterator) {
            return null;
        }

        final StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        T item;
        while (iterator.hasNext()) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(conjunction);
            }

            item = iterator.next();
            if (ArrayUtil.isArray(item)) {
                sb.append(ArrayUtil.join(ArrayUtil.wrap(item), conjunction));
            } else if (item instanceof Iterable<?>) {
                sb.append(join((Iterable<?>) item, conjunction));
            } else if (item instanceof Iterator<?>) {
                sb.append(CollectionUtil.join((Iterator<?>) item, conjunction));
            } else {
                sb.append(item);
            }
        }
        return sb.toString();
    }

    /**
     * 以 conjunction 为分隔符将集合转换为字符串
     * @param <T> 集合元素类型
     * @param iterable {@link Iterable}
     * @param conjunction 分隔符
     * @return 连接后的字符串
     */
    public static <T> String join(Iterable<T> iterable, CharSequence conjunction) {
        if (null == iterable) {
            return null;
        }
        return join(iterable.iterator(), conjunction);
    }

}
