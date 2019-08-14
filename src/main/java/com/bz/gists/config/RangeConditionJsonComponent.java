package com.bz.gists.config;

import com.bz.gists.domain.extend.RangeCondition;
import com.bz.gists.helper.RangeConditionHelper;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

/**
 * Created on 2019/8/14
 *
 * @author zhongyongbin
 */
@JsonComponent
public class RangeConditionJsonComponent {

    public static class Serializer extends JsonSerializer<RangeCondition> {

        @Override
        public void serialize(RangeCondition value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(RangeConditionHelper.conditionToString(value));
        }
    }

    public static class Deserializer extends JsonDeserializer<RangeCondition> {

        @Override
        public RangeCondition deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return RangeConditionHelper.stringToCondition(p.getValueAsString());
        }
    }
}
