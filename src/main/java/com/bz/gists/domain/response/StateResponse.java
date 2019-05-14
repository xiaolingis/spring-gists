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
