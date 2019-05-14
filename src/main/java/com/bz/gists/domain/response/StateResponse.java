package com.bz.gists.domain.response;

/**
 * Created on 2019/1/19
 *
 * @author zhongyongbin
 */
public class StateResponse {

    private State state;

    private String message;

    private AbstractResponseEntity data;

    private StateResponse(State state) {
        this.state = state;
    }

    public static StateResponse ofSuccess() {
        return of(State.SUCCESS);
    }

    public static StateResponse of(State state) {
        return new StateResponse(state);
    }

    public static StateResponse ofFail() {
        return of(State.FAIL);
    }

    public StateResponse withMessage(String message) {
        this.message = message;
        return this;
    }

    public StateResponse withData(AbstractResponseEntity data) {
        this.data = data;
        return this;
    }

    public State getState() {
        return state;
    }

    public String getMessage() {
        return message;
    }

    public AbstractResponseEntity getData() {
        return data;
    }
}
