package com.disney.app.infrastructure.model;

import com.disney.app.config.OffsetDateTimeDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForecastResponse {
    @NonNull
    private Properties properties;

    @NoArgsConstructor
    @Data
    public static class Properties {
        @NonNull
        private List<Period> periods;
    }

    @NoArgsConstructor
    @Data
    public static class Period {
        private int number;
        @NonNull
        private String name;
        @NonNull
        private Temperature temperature;
        @NonNull
        private String shortForecast;
        @NonNull
        @JsonDeserialize(using = OffsetDateTimeDeserializer.class)
        private OffsetDateTime startTime;

        public Double getTemperatureValue() {
            return temperature.getValue();
        }

        public TemperatureUnit getTemperatureUnitCode() {
            return temperature.getUnitCode();
        }
    }

    @NoArgsConstructor
    @Data
    public static class Temperature {
        @NonNull
        private Double value;
        @NonNull
        private TemperatureUnit unitCode;
    }

    public enum TemperatureUnit {
        CELSIUS("wmoUnit:degC");

        private final String unit;

        TemperatureUnit(String unit) {
            this.unit = unit;
        }

        @JsonValue
        public String getUnit() {
            return unit;
        }
    }
}
