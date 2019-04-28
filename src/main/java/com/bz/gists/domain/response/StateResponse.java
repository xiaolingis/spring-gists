package com.bz.gists.domain.response;

/**
 * Created on 2019/1/19
 *
 * @author zhongyongbin
 */
public class StateResponse {

    private String state;

    private String message;

    public static StateResponse ofSuccess() {
        return StateResponse.of(State.SUCCESS);
    }

    public static StateResponse of(State state) {
        return new StateResponse().withState(state);
    }

    public StateResponse withState(State state) {
        this.state = state.name();
        return this;
    }

    public static StateResponse ofFail() {
        return StateResponse.of(State.FAIL);
    }

    public String getState() {
        return state;
    }

    public String getMessage() {
        return message;
    }

    public StateResponse withMessage(String message) {
        this.message = message;
        return this;
    }
}
