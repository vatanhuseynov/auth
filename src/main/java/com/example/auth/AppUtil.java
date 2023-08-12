package com.example.auth;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class AppUtil {

    private static AppUtil instance = null;

    private final ObjectMapper objectMapper;

    private AppUtil() {
        ObjectMapper o = new ObjectMapper();
        o.registerModule(new JavaTimeModule());
        o.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        o.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        this.objectMapper = o;
    }

    public static AppUtil get() {
        if (instance == null) {
            instance = new AppUtil();
        }
        return instance;
    }

    public ObjectMapper objectMapper() {
        return objectMapper;
    }
}
