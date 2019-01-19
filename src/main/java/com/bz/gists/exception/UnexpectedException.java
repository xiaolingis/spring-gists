package com.bz.gists.exception;

/**
 * 预料之外的异常，通常用于封装异常并添加自定义的消息，HTTP 响应 500
 */
public class UnexpectedException extends RuntimeException {

    public UnexpectedException(final String message) {
        super(message);
    }

    public UnexpectedException(final String message, Throwable ex) {
        super(message, ex);
    }
}
