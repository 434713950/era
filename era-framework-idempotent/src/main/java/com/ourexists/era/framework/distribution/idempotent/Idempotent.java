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

package com.ourexists.era.framework.distribution.idempotent;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 幂等性注解
 * 可用于接口幂等性、消费者幂等性处理
 * @author pengcheng
 * @date 2022/5/12 9:46
 * @since 1.0.0
 */
@Target({ ElementType.METHOD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Idempotent {

    /**
     * 传参中用于标志唯一id的属性名
     * @return
     */
    @AliasFor("field")
    String value() default "idempotentId";

    /**
     * 传参中用于标志唯一id的属性名
     * @return
     */
    @AliasFor("value")
    String field() default "idempotentId";

    /**
     * 传参的位置
     * @return
     */
    int position() default 0;

    /**
     * 用于做幂等性处理的空间前缀标志
     * @return
     */
    String space() default "era:idempotent";
}
