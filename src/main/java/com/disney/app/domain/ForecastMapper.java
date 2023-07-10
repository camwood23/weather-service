package com.disney.app.domain;

import com.disney.app.application.model.WeatherResponse;
import com.disney.app.infrastructure.model.ForecastResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.format.TextStyle;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Component
public class ForecastMapper {

    public Mono<WeatherResponse> map(Mono<ForecastResponse> forecastResponse) {
        return forecastResponse.map(forecast -> forecast.getProperties().getPeriods()
                        .stream().filter(period ->
                            period.getNumber() == 1 || (period.getStartTime().getHour() >= 18 && period.getNumber() == 2)
                        )
                        .max(Comparator.comparingDouble(ForecastResponse.Period::getTemperatureValue))
                        .orElseThrow(() -> new NoPeriodFoundException("No temperature found for today")))
                .map(period ->
                    WeatherResponse.Daily.builder()
                        .temp_high_celsius(period.getTemperatureValue())
                        .forecast_blurp(period.getShortForecast())
                        .day_name(period.getStartTime().getDayOfWeek().getDisplayName(
                                TextStyle.FULL, Locale.US))
                        .build())
                .map(daily -> WeatherResponse.builder().daily(List.of(daily)).build());
    }
}
