package com.bz.gists.rest.response;

/**
 * Created on 2019/1/19
 *
 * @author zhongyongbin
 */
public class StateResponse {

    private String state = State.SUCCESS.name();

    private String message;

    private Object data;

    public String getState() {
        return state;
    }

    public StateResponse setState(String state) {
        this.state = state;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public StateResponse setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public StateResponse setData(Object data) {
        this.data = data;
        return this;
    }

    public static StateResponse ofSuccess() {
        return new StateResponse().setState(State.SUCCESS.name());
    }

    public static StateResponse ofFail() {
        return new StateResponse().setState(State.FAIL.name());
    }

    enum State {
        /**
         * 成功
         */
        SUCCESS,

        /**
         * 失败
         */
        FAIL;
    }
}
