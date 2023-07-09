package com.disney.app.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class OffsetDateTimeDeserializer extends JsonDeserializer<OffsetDateTime> {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @Override
    public OffsetDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String dateString = parser.getText();
        return OffsetDateTime.parse(dateString, FORMATTER);
    }
}
