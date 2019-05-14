package com.bz.gists.domain.response;

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

    public static StateResponse ofSuccess() {
        return new StateResponse().withState(State.SUCCESS.name());
    }

    public StateResponse withState(String state) {
        this.state = state;
        return this;
    }

    public static StateResponse ofFail() {
        return new StateResponse().withState(State.FAIL.name());
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
}
