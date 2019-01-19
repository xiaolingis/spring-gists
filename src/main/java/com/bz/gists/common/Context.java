package com.bz.gists.common;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * Created on 2019/1/19
 *
 * 使用 TransmittableThreadLocal 保存全局上下文数据，在跨线程时亦能保证获取到数据
 *
 * @author zhongyongbin
 */
public final class Context {

    private Context(){
    }

    private static final Context INSTANCE = new Context();

    private final TransmittableThreadLocal<String> userIdThreadLocal = new TransmittableThreadLocal<>();

    public static String getUserId() {
        return INSTANCE.userIdThreadLocal.get();
    }

    public static void setUserId(String userId) {
        INSTANCE.userIdThreadLocal.set(userId);
    }

    public static void clear() {
        INSTANCE.userIdThreadLocal.remove();
    }

}
