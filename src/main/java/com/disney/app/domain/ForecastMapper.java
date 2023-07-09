package com.disney.app.domain;

import com.disney.app.application.model.Daily;
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
    private static final String FAHRENHEIT_SYMBOL = "F";

    public Mono<WeatherResponse> map(Mono<ForecastResponse> forecastResponse) {
        return forecastResponse.map(forecast -> forecast.getProperties().getPeriods()
                        .stream().filter(period ->
                            period.getNumber() == 1 || (period.getStartTime().getHour() < 18 && period.getNumber() == 2)
                        )
                        .max(Comparator.comparingInt(ForecastResponse.Period::getTemperature))
                        .orElseThrow(() -> new NoPeriodFoundException("No temperature found for today")))
                .map(period ->
                    Daily.builder()
                        .temp_high_celsius(convertPeriodToCelsius(period))
                        .forecast_blurp(period.getShortForecast())
                        .day_name(period.getStartTime().getDayOfWeek().getDisplayName(
                                TextStyle.FULL, Locale.US))
                        .build())
                .map(daily -> WeatherResponse.builder().daily(List.of(daily)).build());
    }

    private int convertPeriodToCelsius(ForecastResponse.Period period) {
        return FAHRENHEIT_SYMBOL.equals(period.getTemperatureUnit()) ? ((period.getTemperature()-32)*5)/9 : period.getTemperature();
    }
}
