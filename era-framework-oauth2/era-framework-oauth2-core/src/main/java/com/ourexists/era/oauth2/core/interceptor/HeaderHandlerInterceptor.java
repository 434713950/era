package com.ourexists.era.oauth2.core.interceptor;

import com.ourexists.era.framework.core.EraSystemHeader;
import com.ourexists.era.framework.core.user.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class HeaderHandlerInterceptor implements HandlerInterceptor {

    public HeaderHandlerInterceptor() {
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            Map<String, String> headers = new HashMap<>();
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String values = request.getHeader(name);
                headers.put(name, values);
            }
            UserContext.setRequestHeader(headers);
        }
        //先默认获取请求头的租户信息用于白名单路径
        UserContext.setPlatform(EraSystemHeader.extractPlatform(request));
        UserContext.setTenant(EraSystemHeader.extractTenant(request));
        UserContext.setUser(EraSystemHeader.extractUserInfo(request));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserContext.remove();
    }
}
