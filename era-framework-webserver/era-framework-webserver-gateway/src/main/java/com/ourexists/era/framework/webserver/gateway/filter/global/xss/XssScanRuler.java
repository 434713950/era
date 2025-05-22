/*
 * Copyright (c) 2025. created by ourexists.https://gitee.com/ourexists
 */

package com.ourexists.era.framework.webserver.gateway.filter.global.xss;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @Author: zhengcan
 * @Date: 2022/3/20
 * @Description: 自定义的XSS扫描工具类
 * @Version: 1.0.0 创建
 */
@Slf4j
public class XssScanRuler {

    /**
     * XSS过滤规则
     */
    private final static Pattern[] scriptPatterns = {
            Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("src[\r\n]*=[\r\n]*\\'(.*?)\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
            Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
            Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
            Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
            Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
    };

    /**
     * 非法的sql输入规则
     */
    private static final String badSqlStrReg = "\\b(and|or)\\b.{1,6}?(=|>|<|\\bin\\b|\\blike\\b)|\\/\\*.+?\\*\\/|<\\s*script\\b|\\bEXEC\\b|UNION.+?SELECT|UPDATE.+?SET|INSERT\\s+INTO.+?VALUES|(SELECT|DELETE).+?FROM|(CREATE|ALTER|DROP|TRUNCATE)\\s+(TABLE|DATABASE)";

    private static final Pattern sqlPattern = Pattern.compile(badSqlStrReg, Pattern.CASE_INSENSITIVE);

    /**
     * 校验Get请求是否通过安全扫描
     * @param value
     * @return
     */
    public static boolean passGetRequestScan(String value) {
        if (StringUtils.isEmpty(value)) {
            return true;
        }
        boolean isMatch = false;
        try {
            // 先进行一次转义,处理特殊字符转义的问题
            String decode = URLDecoder.decode(value, "UTF-8");
            //获取到请求中所有参数值-取每个key=value组合第一个等号后面的值
            isMatch = Stream.of(decode.split("\\&"))
                    .map(kp -> kp.substring(kp.indexOf("=") + 1))
                    .parallel()
                    .anyMatch(param -> processScan(param));
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        }
        return !isMatch;
    }

    /**
     * 校验Post请求是否通过安全扫描
     * @param value
     * @return
     */
    public static boolean passPostRequestScan(String value) {
        if (StringUtils.isEmpty(value)) {
            return true;
        }
        boolean isMatch = false;
        Object jsonObj = JSON.parse(value);
        if (jsonObj instanceof JSONObject) {
            JSONObject jo = (JSONObject) jsonObj;
            // 对Map类型的post请求参数值进行安全扫描
            isMatch = jo.entrySet().stream().parallel().anyMatch(entry -> {
                //这里需要将参数转换为小写来处理
                String lowerValue = Optional.ofNullable(entry.getValue())
                        .map(Object::toString)
                        .map(String::toLowerCase)
                        .orElse("");
                return processScan(lowerValue);
            });
        } else if (jsonObj instanceof JSONArray) {
            JSONArray json = (JSONArray) jsonObj;
            if (json.isEmpty()) {
                return true;
            }
            // 对List类型的post请求参数值进行安全扫描
            isMatch = json.stream().parallel().anyMatch(obj -> {
                //这里需要将参数转换为小写来处理
                String lowerValue = Optional.ofNullable(obj)
                        .map(Object::toString)
                        .map(String::toLowerCase)
                        .orElse("");
                return processScan(lowerValue);
            });
        }
        return !isMatch;
    }

    /**
     * 执行安全扫描流程
     * @param value 待校验值
     * @return
     */
    private static boolean processScan(String value) {
        if (StringUtils.isEmpty(value)) {
            return false;
        }
        String oldValue = value;
        // 执行xss扫描
        value = value.replaceAll("\n|\r", "");
        for (Pattern pattern : scriptPatterns) {
            if (pattern.matcher(value).find()) {
                log.warn("参数中包含非法的跨站攻击关键词，param={}", oldValue);
                return true;
            }
        }
        // 执行sql安全校验
        if (sqlPattern.matcher(value.toLowerCase()).find()) {
            log.warn("参数中包含不允许的sql关键词, param={}", oldValue);
            return true;
        }
        return false;
    }

}
