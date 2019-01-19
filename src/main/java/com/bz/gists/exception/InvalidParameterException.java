package com.bz.gists.exception;

import org.springframework.core.NestedRuntimeException;

/**
 * 不合法的参数异常，HTTP 响应 400
 */
public class InvalidParameterException extends NestedRuntimeException {

    public InvalidParameterException(final String message) {
        super(message);
    }

}
