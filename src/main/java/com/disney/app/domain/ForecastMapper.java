package com.disney.app.domain;

import com.disney.app.application.model.Daily;
import com.disney.app.application.model.WeatherResponse;
import com.disney.app.infrastructure.ForecastResponse;
import com.disney.app.infrastructure.TimeClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Component
public class ForecastMapper {
    private final TimeClient timeClient;

    @Autowired
    public ForecastMapper(TimeClient timeClient) {
        this.timeClient = timeClient;
    }

    public Mono<WeatherResponse> map(Mono<ForecastResponse> forecastResponse) {
        return forecastResponse.map(forecast -> forecast.getProperties().getPeriods().get(0))
                .map(period ->
                    Daily.builder()
                        .temp_high_celsius(convertPeriodToCelsius(period))
                        .forecast_blurp(period.getShortForecast()))
                .flatMap(dailyBuilder -> timeClient.getCurrentTime().map(timeResponse ->
                        dailyBuilder.day_name(timeResponse.getUtc_datetime().getDayOfWeek().getDisplayName(
                                TextStyle.FULL, Locale.US)).build()))
                .map(daily -> WeatherResponse.builder().daily(List.of(daily)).build());
    }

    private int convertPeriodToCelsius(ForecastResponse.Period period) {
        return period.getTemperatureUnit().equals("F") ? ((period.getTemperature()-32)*5)/9 : period.getTemperature();
    }
}
