package com.ourexists.era.framework.core.utils;

import com.alibaba.fastjson2.JSONObject;
import com.ourexists.era.framework.core.constants.CommonConstant;
import com.ourexists.era.framework.core.constants.ResultMsgEnum;
import com.ourexists.era.framework.core.model.vo.JsonResponseEntity;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * era规范工具包
 *
 * @author pengcheng
 * {@code @date 2025/05/14}
 */
@Slf4j
public class EraStandardUtils {

    /**
     * 响应视图输出
     * @param response
     * @param resultMsgEnum
     */
    public static void exceptionView(HttpServletResponse response, ResultMsgEnum resultMsgEnum) {
        exceptionView(response, resultMsgEnum, null);
    }

    /**
     * 响应视图输出
     * @param response
     * @param resultMsgEnum
     */
    public static void exceptionView(HttpServletResponse response, ResultMsgEnum resultMsgEnum, String message) {
        if (!StringUtils.hasText(message)) {
            message = resultMsgEnum.getResultMsg();
        }
        JsonResponseEntity<?> jo = new JsonResponseEntity<>(
                resultMsgEnum.getResultCode(), message);
        String json = JSONObject.toJSONString(jo);
        response.setContentType(CommonConstant.CONTENT_TYPE);
        try (PrintWriter out = response.getWriter()) {
            out.write(json);
            out.flush();
        } catch (IOException e) {
            log.error("response output error", e);
        }
    }
}
