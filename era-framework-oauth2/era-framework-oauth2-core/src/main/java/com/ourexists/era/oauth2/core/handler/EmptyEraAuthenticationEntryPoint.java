package com.ourexists.era.oauth2.core.handler;

import com.ourexists.era.framework.core.constants.ResultMsgEnum;
import com.ourexists.era.framework.core.utils.EraStandardUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

public class EmptyEraAuthenticationEntryPoint implements EraAuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        EraStandardUtils.exceptionView(response, ResultMsgEnum.SC_UNAUTHORIZED, authException.getMessage());
    }
}
