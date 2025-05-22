/*
 * Copyright (c) 2025. created by ourexists.https://gitee.com/ourexists
 */

package com.ourexists.era.framework.webserver.enhance.handler;

import com.ourexists.era.framework.core.constants.ResultMsgEnum;
import com.ourexists.era.framework.core.exceptions.AuthorityException;
import com.ourexists.era.framework.core.exceptions.BusinessException;
import com.ourexists.era.framework.core.exceptions.InitialPasswordException;
import com.ourexists.era.framework.core.exceptions.LoginNotException;
import com.ourexists.era.framework.core.model.vo.JsonResponseEntity;
import com.ourexists.era.framework.webserver.enhance.I18nUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * 统一异常处理器
 * @author PengCheng
 * @date 2018/5/20
 */
@Slf4j
@RestControllerAdvice
public class ExceptionAnalysisHandler {

    @ExceptionHandler(BusinessException.class)
    public JsonResponseEntity handleErrorMsgException(BusinessException e){
        String msg = I18nUtil.i18nParser(e.getMsg(),e.getText());
        return new JsonResponseEntity(e.getCode(),msg);
    }

    @ExceptionHandler(LoginNotException.class)
    public JsonResponseEntity handleLoginNotException(LoginNotException e){
        return new JsonResponseEntity(ResultMsgEnum.UN_LOGIN);
    }

    @ExceptionHandler(InitialPasswordException.class)
    public JsonResponseEntity handleInitialPasswordException(InitialPasswordException e){
        return new JsonResponseEntity(ResultMsgEnum.INIT_PASS);
    }

    @ExceptionHandler(AuthorityException.class)
    public JsonResponseEntity handleAuthorityException(AuthorityException e){
        log.error("Unable to authenticate!", e);
        return new JsonResponseEntity(ResultMsgEnum.PERMISSION_DENIED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public JsonResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        List<ObjectError> errors =e.getBindingResult().getAllErrors();
        StringBuffer errorMsg=new StringBuffer();
        errors.forEach(x -> errorMsg.append(I18nUtil.i18nParser(x.getDefaultMessage())).append("\r\n"));
        return new JsonResponseEntity(ResultMsgEnum.VALIDATION_ERROR.getResultCode(),errorMsg.toString());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public JsonResponseEntity handleException(Exception e){
        log.error("system error!", e);
        return JsonResponseEntity.systemError();
    }

}
