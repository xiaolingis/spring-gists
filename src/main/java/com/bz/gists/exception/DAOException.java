package com.bz.gists.exception;

/**
 * Created on 2019/4/26
 *
 * 用于包装数据库操作时的异常进行事务回滚
 *
 * @author zhongyongbin
 */
public class DAOException extends RuntimeException {

    public DAOException(final String message, Throwable ex) {
        super(message, ex);
    }

}
