package com.shongon.identity_service.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomTime extends JsonSerializer<LocalDateTime> {
    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("HH:mm:ss | dd-MM-yyyy");

    @Override
    public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider provider)
            throws IOException {
        if (value != null) {
            gen.writeString(value.format(formatter));
        }
    }
}
