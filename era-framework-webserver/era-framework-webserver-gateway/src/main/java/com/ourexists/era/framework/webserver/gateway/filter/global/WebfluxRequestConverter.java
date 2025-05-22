/*
 * Copyright (c) 2025. created by ourexists.https://gitee.com/ourexists
 */

package com.ourexists.era.framework.webserver.gateway.filter.global;

import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import reactor.core.publisher.Flux;

import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

public class WebfluxRequestConverter {

    private WebfluxRequestConverter() {
    }

    /**
     * 从Flux<DataBuffer>中获取字符串的方法
     * @return 请求体字符串
     */
    public static String resolveBodyFromRequest(ServerHttpRequest serverHttpRequest) {
        //获取请求体
        Flux<DataBuffer> body = serverHttpRequest.getBody();
        AtomicReference<String> bodyRef = new AtomicReference<>();
        body.subscribe(buffer -> {
            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
            /**
             * 踩坑记录：
             *   此处不能释放buffer，因为在我们创建ByteBuf对象后，它的引用计数是1，
             *   当你每次调用DataBufferUtils.release之后会释放引用计数对象时，它的引用计数减1，
             *   如果引用计数为0，这个引用计数对象会被释放（deallocate）,并返回对象池。
             *   当尝试访问引用计数为0的引用计数对象会抛出IllegalReferenceCountException异常。
             */
//            DataBufferUtils.release(buffer);
            bodyRef.set(charBuffer.toString());
        });
        return bodyRef.get();
    }


}
