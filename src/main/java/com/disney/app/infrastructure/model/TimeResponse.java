package com.disney.app.infrastructure.model;

import com.disney.app.config.OffsetDateTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TimeResponse {
    @NonNull
    @JsonProperty("datetime")
    @JsonDeserialize(using = OffsetDateTimeDeserializer.class)
    private OffsetDateTime datetime;
}
