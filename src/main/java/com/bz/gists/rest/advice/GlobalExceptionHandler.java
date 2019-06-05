package com.bz.gists.rest.advice;

import com.google.common.base.Joiner;

import com.bz.gists.domain.response.StateResponse;
import com.bz.gists.exception.ForbiddenException;
import com.bz.gists.exception.InvalidParameterException;
import com.bz.gists.exception.NotFoundException;
import com.bz.gists.exception.UnexpectedException;
import com.bz.gists.util.ObjectMapperUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ValidationException;

/**
 * 全局异常处理器
 *
 * @author zhongyongbin
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = ForbiddenException.class)
    public ResponseEntity<Object> forbiddenExceptionHandler(Exception ex) {
        LOGGER.debug("forbidden exception!", ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(StateResponse.ofFail().withMessage(ex.getMessage()));
    }

    @ExceptionHandler(value = {
            InvalidParameterException.class,
            ValidationException.class
    })
    public ResponseEntity<Object> invalidParameterExceptionHandler(Exception ex) {
        LOGGER.debug("invalid parameter exception!", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(StateResponse.ofFail().withMessage(ex.getMessage()));
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<Object> notFoundExceptionExceptionHandler(Exception ex) {
        LOGGER.debug("not found exception!", ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(StateResponse.ofFail().withMessage(ex.getMessage()));
    }

    /**
     * 如果没有被相应的 ExceptionHandler 处理的异常一般为发生了意料之外的错误所导致的，统一捕获处理，并返回 500
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<Object> unexpectedExceptionHandler(Throwable ex, HttpServletRequest request) {
        LOGGER.error("unexpected exception occur, uri:[{}], params:[{}], request body:[{}], stacktrace:",
                request.getRequestURI(),
                ObjectMapperUtil.toJson(request.getParameterMap()),
                request.getAttribute(RequestBodyAttributeAdvice.REQUEST_BODY_ATTRIBUTE_NAME),
                ex);
        ResponseEntity<Object> responseEntity;
        if (ex instanceof UnexpectedException) {
            responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(StateResponse.ofFail().withMessage(ex.getMessage()));
        } else {
            responseEntity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(StateResponse.ofFail().withMessage(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()));
        }

        return responseEntity;
    }

    /**
     * 参数验证错误处理
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BindingResult bindingResult = ex.getBindingResult();
        String message = Joiner.on(",").join(
                bindingResult.getAllErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList())
        );
        return handleExceptionInternal(ex, StateResponse.ofFail().withMessage(message), headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        body = Optional.ofNullable(body).orElse(StateResponse.ofFail().withMessage(status.getReasonPhrase()));
        return super.handleExceptionInternal(ex, body, headers, status, request);
    }
}
