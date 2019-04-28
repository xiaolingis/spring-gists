package com.bz.gists.domain.response;

/**
 * Created on 2019/1/19
 *
 * @author zhongyongbin
 */
public class StateResponse {

    private String state = State.SUCCESS.name();

    private String message;

    public static StateResponse ofSuccess() {
        return new StateResponse().setState(State.SUCCESS.name());
    }

    public static StateResponse ofFail() {
        return new StateResponse().setState(State.FAIL.name());
    }

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
}
