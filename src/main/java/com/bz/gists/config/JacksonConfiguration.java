package com.bz.gists.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.util.Date;

/**
 * Created at 2018/12/27
 *
 * @author zhongyongbin
 */
@JsonComponent
public class JacksonConfiguration {

    public static class DateToLongSerializer extends JsonSerializer<Date> {
        @Override
        public void serialize(Date date, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeNumber(date.getTime());
        }

    }

    public static class LongToDateDeserializer extends JsonDeserializer<Date> {

        @Override
        public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            return new Date(p.getValueAsLong());
        }
    }
}
