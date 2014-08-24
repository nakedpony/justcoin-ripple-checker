package com.shinydev.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class ObjectMapperFactory {

    private static SimpleModule testModule = new SimpleModule("CustomSerializers", Version.unknownVersion());
    private static ObjectMapper mapper = new ObjectMapper()
            .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
            .configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .registerModule(testModule);

    public static ObjectMapper getMapper() {
        return mapper;
    }
}
