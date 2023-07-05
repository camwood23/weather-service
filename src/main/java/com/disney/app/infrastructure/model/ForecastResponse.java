package com.disney.app.infrastructure.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

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
        private int temperature;
        @NonNull
        private String temperatureUnit;
        @NonNull
        private String shortForecast;
    }
}
