package com.bz.gists.exception;

/**
 * 没有找到资源异常，HTTP 响应 404
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(final String message) {
        super(message);
    }

}
