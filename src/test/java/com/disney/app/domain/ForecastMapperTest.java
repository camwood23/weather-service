package com.disney.app.domain;

import com.disney.app.application.model.WeatherResponse;
import com.disney.app.infrastructure.model.ForecastResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.OffsetDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class ForecastMapperTest {
    private static final OffsetDateTime BEFORE_SIX = OffsetDateTime.parse("2023-07-09T17:00:00-04:00");
    private static final OffsetDateTime AFTER_SIX = OffsetDateTime.parse("2023-07-09T19:00:00-04:00");
    private static final String DAY_NAME = "Sunday";
    private static final Double TEMP_IN_CELSIUS = 26.0;
    private static final String FORECAST_BLURP = "Sunny day";

    private final ForecastMapper forecastMapper = new ForecastMapper();

    @Test
    public void testMapMaxOfTodayTonight() {
        ForecastResponse response = new ForecastResponse();
        response.setProperties(new ForecastResponse.Properties());
        response.getProperties().setPeriods(new ArrayList<>());
        ForecastResponse.Period period1 = new ForecastResponse.Period();
        period1.setNumber(1);
        period1.setName("Today");

        period1.setShortForecast(FORECAST_BLURP);
        period1.setTemperature(new ForecastResponse.Temperature());
        period1.getTemperature().setUnitCode(ForecastResponse.TemperatureUnit.CELSIUS);
        period1.getTemperature().setValue(TEMP_IN_CELSIUS);
        period1.setStartTime(BEFORE_SIX);

        ForecastResponse.Period period2 = new ForecastResponse.Period();
        period2.setNumber(2);
        period2.setName("Tonight");
        period2.setStartTime(AFTER_SIX);
        period2.setTemperature(new ForecastResponse.Temperature());
        period2.getTemperature().setUnitCode(ForecastResponse.TemperatureUnit.CELSIUS);
        period2.getTemperature().setValue(25.0);
        response.getProperties().getPeriods().add(period1);
        response.getProperties().getPeriods().add(period2);

        Mono<WeatherResponse> weatherResponseMono = forecastMapper.map(Mono.just(response));
        StepVerifier.create(weatherResponseMono)
                .expectNextMatches(weatherResponse ->
                        weatherResponse.getDaily().size() == 1 &&
                                weatherResponse.getDaily().get(0).getDay_name().equals(DAY_NAME) &&
                                Objects.equals(weatherResponse.getDaily().get(0).getTemp_high_celsius(), TEMP_IN_CELSIUS) &&
                                weatherResponse.getDaily().get(0).getForecast_blurp().equals(FORECAST_BLURP)
                )
                .expectComplete()
                .verify();
    }

    @Test
    public void testMapWithOnlyTonight() {
        ForecastResponse response = new ForecastResponse();
        response.setProperties(new ForecastResponse.Properties());
        response.getProperties().setPeriods(new ArrayList<>());
        ForecastResponse.Period period1 = new ForecastResponse.Period();
        period1.setNumber(1);
        period1.setName("Tonight");
        period1.setTemperature(new ForecastResponse.Temperature());
        period1.getTemperature().setUnitCode(ForecastResponse.TemperatureUnit.CELSIUS);
        period1.getTemperature().setValue(TEMP_IN_CELSIUS);
        period1.setShortForecast(FORECAST_BLURP);
        period1.setStartTime(AFTER_SIX);
        response.getProperties().getPeriods().add(period1);

        Mono<WeatherResponse> weatherResponseMono = forecastMapper.map(Mono.just(response));
        StepVerifier.create(weatherResponseMono)
                .expectNextMatches(weatherResponse ->
                        weatherResponse.getDaily().size() == 1 &&
                                weatherResponse.getDaily().get(0).getDay_name().equals(DAY_NAME) &&
                                Objects.equals(weatherResponse.getDaily().get(0).getTemp_high_celsius(), TEMP_IN_CELSIUS) &&
                                weatherResponse.getDaily().get(0).getForecast_blurp().equals(FORECAST_BLURP)
                )
                .expectComplete()
                .verify();
    }

    @Test
    public void testMapMaxOfTodayTonightWithHotterNight() {
        ForecastResponse response = new ForecastResponse();
        response.setProperties(new ForecastResponse.Properties());
        response.getProperties().setPeriods(new ArrayList<>());
        ForecastResponse.Period period1 = new ForecastResponse.Period();
        period1.setNumber(1);
        period1.setName("Today");
        period1.setTemperature(new ForecastResponse.Temperature());
        period1.getTemperature().setUnitCode(ForecastResponse.TemperatureUnit.CELSIUS);
        period1.getTemperature().setValue(25.0);
        period1.setStartTime(BEFORE_SIX);
        ForecastResponse.Period period2 = new ForecastResponse.Period();
        period2.setNumber(2);
        period2.setName("Tonight");
        period2.setTemperature(new ForecastResponse.Temperature());
        period2.getTemperature().setUnitCode(ForecastResponse.TemperatureUnit.CELSIUS);
        period2.getTemperature().setValue(TEMP_IN_CELSIUS);
        period2.setShortForecast(FORECAST_BLURP);
        period2.setStartTime(AFTER_SIX);
        response.getProperties().getPeriods().add(period1);
        response.getProperties().getPeriods().add(period2);

        Mono<WeatherResponse> weatherResponseMono = forecastMapper.map(Mono.just(response));
        StepVerifier.create(weatherResponseMono)
                .expectNextMatches(weatherResponse ->
                        weatherResponse.getDaily().size() == 1 &&
                                weatherResponse.getDaily().get(0).getDay_name().equals(DAY_NAME) &&
                                Objects.equals(weatherResponse.getDaily().get(0).getTemp_high_celsius(), TEMP_IN_CELSIUS) &&
                                weatherResponse.getDaily().get(0).getForecast_blurp().equals(FORECAST_BLURP)
                )
                .expectComplete()
                .verify();
    }

    @Test
    public void testMapWithNoNumberThrowsException() {
        ForecastResponse response = new ForecastResponse();
        response.setProperties(new ForecastResponse.Properties());
        response.getProperties().setPeriods(new ArrayList<>());
        ForecastResponse.Period period1 = new ForecastResponse.Period();
        period1.setName("Today");
        period1.setTemperature(new ForecastResponse.Temperature());
        period1.getTemperature().setUnitCode(ForecastResponse.TemperatureUnit.CELSIUS);
        period1.getTemperature().setValue(TEMP_IN_CELSIUS);
        period1.setShortForecast(FORECAST_BLURP);
        response.getProperties().getPeriods().add(period1);

        Mono<WeatherResponse> weatherResponseMono = forecastMapper.map(Mono.just(response));
        StepVerifier.create(weatherResponseMono)
                .expectError()
                .verify();
    }
}
