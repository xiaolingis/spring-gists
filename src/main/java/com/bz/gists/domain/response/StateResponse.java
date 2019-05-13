package com.bz.gists.domain.response;

/**
 * Created on 2019/1/19
 *
 * @author zhongyongbin
 */
public class StateResponse<T> {

    private State state;

    private String message;

    private T data;

    private StateResponse(State state) {
        this.state = state;
    }

    public static <T> StateResponse<T> ofSuccess() {
        return new StateResponse<>(State.SUCCESS);
    }

    public static <T> StateResponse<T> ofFail() {
        return new StateResponse<>(State.FAIL);
    }

    public static <T> StateResponse<T> of(State state) {
        return new StateResponse<>(state);
    }

    public StateResponse<T> withMessage(String message) {
        this.message = message;
        return this;
    }

    public StateResponse<T> withData(T data) {
        this.data = data;
        return this;
    }

    public State getState() {
        return state;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
