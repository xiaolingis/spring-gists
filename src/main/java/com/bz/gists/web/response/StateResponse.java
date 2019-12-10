package com.bz.gists.web.response;

/**
 * Created on 2019/1/19
 *
 * 响应实体，所有接口都必须返回该实体类或其子类
 *
 * @author zhongyongbin
 */
public class StateResponse {

    private String state = State.SUCCESS.name();

    private String message;

    private long timestamp = System.currentTimeMillis();

    public static StateResponse ofSuccess() {
        return new StateResponse().withState(State.SUCCESS);
    }

    public StateResponse withState(State state) {
        this.state = state.name();
        return this;
    }

    public static StateResponse ofFail() {
        return new StateResponse().withState(State.FAIL);
    }

    public static StateResponse ofInvalid() {
        return new StateResponse().withState(State.INVALID);
    }

    public StateResponse withMessage(String message) {
        this.message = message;
        return this;
    }

    public String getState() {
        return state;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
