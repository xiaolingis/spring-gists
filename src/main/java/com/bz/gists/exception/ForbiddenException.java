package com.bz.gists.exception;

/**
 * 禁止访问异常，HTTP 响应 403
 */
public class ForbiddenException extends RuntimeException {

    public ForbiddenException() {
        super();
    }

    public ForbiddenException(String message) {
        super(message);
    }

}
