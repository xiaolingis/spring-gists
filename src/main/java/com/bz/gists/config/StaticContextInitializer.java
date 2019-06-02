package com.bz.gists.config;

import com.bz.gists.util.JsonUtil;
import com.bz.gists.util.ValidatorUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.validation.Validator;


/**
 * Created on 2019/6/2
 *
 * @author zhongyongbin
 */
@Component
public class StaticContextInitializer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Validator validator;

    @PostConstruct
    public void init() {
        JsonUtil.setObjectMapper(objectMapper);
        ValidatorUtil.setValidator(validator);
    }
}
