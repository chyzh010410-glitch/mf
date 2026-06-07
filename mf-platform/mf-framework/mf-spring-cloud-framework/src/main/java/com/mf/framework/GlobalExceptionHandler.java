package com.mf.framework;

import com.mf.common.base.ResultVO;
import com.mf.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * 全局异常处理 —— 统一 ResultVO 返回格式。
 * 所有微服务共用，保证错误响应格式一致。
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResultVO<?> handleBusiness(BusinessException e) {
        return ResultVO.fail(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultVO<?> handleBind(BindException e) {
        String msg = e.getBindingResult().getFieldErrors().stream()
                .map(f -> f.getField() + ": " + f.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return ResultVO.fail(400, msg);
    }

    @ExceptionHandler(Exception.class)
    public ResultVO<?> handleException(Exception e) {
        log.error("系统异常", e);
        return ResultVO.fail(500, "系统繁忙，请稍后重试");
    }
}
