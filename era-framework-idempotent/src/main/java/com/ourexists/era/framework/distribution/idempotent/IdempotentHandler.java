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

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author pengcheng
 * @date 2022/5/12 11:57
 * @since 1.0.0
 */
@Aspect
public class IdempotentHandler {

    private final RedisTemplate redisTemplate;

    public IdempotentHandler(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Pointcut(value = "@annotation(com.ourexists.era.framework.distribution.idempotent.Idempotent)")
    public void pointcut() {
    }

    @Around(value = "pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature sign = (MethodSignature) joinPoint.getSignature();
        Method method = sign.getMethod();
        Idempotent idempotent = method.getAnnotation(Idempotent.class);

        try {
            Object[] args = joinPoint.getArgs();
            Object message = args[idempotent.position()];
            Field field = message.getClass().getField(idempotent.value());
            field.setAccessible(true);
            Object idempotentIdObj = field.get(message);
            //未设置幂等id则不执行幂等操作
            if (idempotentIdObj == null) {
                return joinPoint.proceed();
            }
            String idempotentId = idempotentIdObj.toString();
            String key = idempotent.space() + ":" + idempotentId;
            ValueOperations<String, Boolean> valueOperations = redisTemplate.opsForValue();
            if (valueOperations.get(key) != null) {
                return null;
            }
            valueOperations.set(key, true, 1, TimeUnit.DAYS);
            return joinPoint.proceed();
        } catch (IndexOutOfBoundsException e) {
            throw new RuntimeException("@Idempotent can not get parameters for the location[" + idempotent.position() + "]");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("@Idempotent can not grab field [" + idempotent.value() + "]");
        }


    }
}
