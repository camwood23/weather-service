package com.disney.app.application.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class WeatherResponse {
    List<Daily> daily;

    @Builder
    @Getter
    public static class Daily {
        String day_name;
        Double temp_high_celsius;
        String forecast_blurp;
    }
}
