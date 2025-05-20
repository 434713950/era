package com.ourexists.era.oauth2.core.handler;

import com.ourexists.era.framework.core.constants.ResultMsgEnum;
import com.ourexists.era.framework.core.utils.EraStandardUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

@Slf4j
public class EmptyEraAuthenticationEntryPoint implements EraAuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.error(authException.getMessage(), authException);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        EraStandardUtils.exceptionView(response, ResultMsgEnum.SC_UNAUTHORIZED, authException.getMessage());
    }
}
