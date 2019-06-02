package com.bz.gists.util;

import com.google.common.base.Joiner;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Callable;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

/**
 * 校验工具类
 *
 * @author zhongyongbin
 */
public final class ValidatorUtil {

    private static final String SEPARATOR = ",";

    private static Validator validator;

    /**
     * 校验参数，校验不通过将抛出校验错误异常
     *
     * @param target 校验对象
     * @param <T>    校验对象类型
     */
    public static <T> void validateAndCheck(T target) {
        validate(target).checkValidateResult();
    }

    /**
     * 校验参数
     *
     * @param target 校验对象
     * @param <T>    校验对象类型
     */
    public static <T> ValidateResult<Void> validate(T target) {
        ValidateResult<Void> result = new ValidateResult<>();
        generateViolationException(target).ifPresent(ex -> result.violationException = ex);

        return result;
    }

    private static <T> Optional<ConstraintViolationException> generateViolationException(T target) {
        ConstraintViolationException ex = null;
        Set<ConstraintViolation<T>> violations = getValidator().validate(target);
        if (!violations.isEmpty()) {
            List<String> violationMessageList = violations.stream().collect(ArrayList::new,
                    (list, violation) -> list.add(violation.getMessage()),
                    ArrayList::addAll);
            String violationMessage = Joiner.on(SEPARATOR).join(violationMessageList);
            ex = new ConstraintViolationException(violationMessage, violations);
        }

        return Optional.ofNullable(ex);
    }

    public static Validator getValidator() {
        return Objects.requireNonNull(validator, "validator uninitialized");
    }

    public static void setValidator(Validator validator) {
        ValidatorUtil.validator = validator;
    }

    /**
     * 校验参数，校验通过后执行指定任务
     *
     * @param target 校验对象
     * @param task   校验通过后执行的任务
     * @param <T>    校验对象类型
     * @return 通常调用 {@link ValidateResult#checkValidateResult()} 进行处理校验异常
     */
    public static <T> ValidateResult<Void> validate(T target, Runnable task) {
        ValidateResult<Void> result = new ValidateResult<>();
        Optional<ConstraintViolationException> exceptionOptional = generateViolationException(target);
        if (exceptionOptional.isPresent()) {
            result.violationException = exceptionOptional.get();
        } else {
            task.run();
        }

        return result;
    }

    /**
     * 校验参数，校验通过后执行指定任务，任务有返回值
     *
     * @param target 校验对象
     * @param task   校验通过后执行的任务
     * @param <T>    校验对象类型
     * @param <R>    校验通过后执行 task 返回的对象类型
     * @return 通常调用 {@link ValidateResult#checkValidateResult()} 进行处理校验异常
     * @throws Exception task 可能抛出的异常
     */
    public static <T, R> ValidateResult<R> validate(T target, Callable<R> task) throws Exception {
        ValidateResult<R> result = new ValidateResult<>();
        Optional<ConstraintViolationException> exceptionOptional = generateViolationException(target);
        if (exceptionOptional.isPresent()) {
            result.violationException = exceptionOptional.get();
        } else {
            result.returnValue = task.call();
        }

        return result;
    }

    public static class ValidateResult<T> {
        private ConstraintViolationException violationException;

        private T returnValue;

        public Optional<ConstraintViolationException> getViolationException() {
            return Optional.ofNullable(violationException);
        }

        public Optional<T> getReturnValue() {
            return Optional.ofNullable(returnValue);
        }

        public ValidateResult checkValidateResult() {
            if (Objects.nonNull(violationException)) {
                throw violationException;
            }

            return this;
        }
    }
}
