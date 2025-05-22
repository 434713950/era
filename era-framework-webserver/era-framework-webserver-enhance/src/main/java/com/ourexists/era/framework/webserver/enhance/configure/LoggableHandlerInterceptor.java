/*
 * Copyright (c) 2025. created by ourexists.https://gitee.com/ourexists
 */

package com.ourexists.era.framework.webserver.enhance.configure;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.BeansException;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.util.*;

/**
 * @author pengcheng
 * @date 2021/5/27 15:47
 * @since
 */
@Slf4j
public class LoggableHandlerInterceptor implements HandlerInterceptor {

    private final ObjectMapper mapper;

    private Set<String> healthCheckUris;

    private final AntPathMatcher antPathMatcher;

    public LoggableHandlerInterceptor(ApplicationContext applicationContext) {
        this.antPathMatcher = new AntPathMatcher();
        this.mapper = new ObjectMapper();
        try {
            WebEndpointProperties webEndpointProperties = applicationContext.getBean(WebEndpointProperties.class);
            WebEndpointProperties.Exposure exposure = webEndpointProperties.getExposure();
            if (exposure != null) {
                healthCheckUris = exposure.getInclude();
            }
        } catch (BeansException e) {
            //nothing
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        //跳过健康检测
        String uri = request.getRequestURI();
        if (!CollectionUtils.isEmpty(healthCheckUris)) {
            for (String checkUri : healthCheckUris) {
                if (!checkUri.startsWith("/")) {
                    checkUri = "/" + checkUri;
                }
                if (antPathMatcher.match(checkUri, uri)) {
                    return true;
                }
            }
        }
        MDC.put("requestId", UUID.randomUUID().toString().replace("-", ""));
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);

        //创建一个 json 对象，用来存放 http 日志信息
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("clientIp", requestWrapper.getRemoteAddr());
        rootNode.set("requestHeaders", mapper.valueToTree(getRequestHeaders(requestWrapper)));
        String method = request.getMethod();
        rootNode.put("method", method);
        String contentType = request.getContentType();
        if (contentType != null
                && !contentType.contains(MediaType.MULTIPART_FORM_DATA_VALUE)
                && !contentType.contains(MediaType.IMAGE_PNG_VALUE)
                && !contentType.contains(MediaType.IMAGE_JPEG_VALUE)
                && !contentType.contains(MediaType.IMAGE_GIF_VALUE)
                && !contentType.contains(MediaType.APPLICATION_PDF_VALUE)
                && !contentType.contains(MediaType.APPLICATION_OCTET_STREAM_VALUE)
                && !contentType.contains(MediaType.MULTIPART_MIXED_VALUE)
                && !contentType.contains(MediaType.MULTIPART_RELATED_VALUE)
                && !contentType.contains(MediaType.TEXT_EVENT_STREAM_VALUE)
        ) {
            if (method.equals(HttpMethod.GET.name())) {
                rootNode.set("request", mapper.valueToTree(requestWrapper.getParameterMap()));
            } else {
                JsonNode newNode = mapper.readTree(requestWrapper.getContentAsByteArray());
                rootNode.set("request", newNode);
            }
        }
        log.info("【[{}]请求】[{}]", requestWrapper.getRequestURI(), rootNode);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("status", responseWrapper.getStatus());
//        JsonNode newNode = mapper.readTree(responseWrapper.getContentAsByteArray());
//        rootNode.set("response", newNode);
//        responseWrapper.copyBodyToResponse();
        rootNode.set("responseHeaders", mapper.valueToTree(getResponseHeaders(responseWrapper)));
        log.info("【[{}]响应】[{}]", request.getRequestURI(), rootNode);
        MDC.remove("requestId");
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
    }

    private Map<String, Object> getRequestHeaders(HttpServletRequest request) {
        Map<String, Object> headers = new HashMap<>(16);
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.put(headerName, request.getHeader(headerName));
        }
        return headers;

    }

    private Map<String, Object> getResponseHeaders(ContentCachingResponseWrapper response) {
        Map<String, Object> headers = new HashMap<>(16);
        Collection<String> headerNames = response.getHeaderNames();
        for (String headerName : headerNames) {
            headers.put(headerName, response.getHeader(headerName));
        }
        return headers;
    }
}
