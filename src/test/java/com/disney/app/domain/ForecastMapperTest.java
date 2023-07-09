package com.disney.app.domain;

import com.disney.app.application.model.WeatherResponse;
import com.disney.app.infrastructure.model.ForecastResponse;
import com.disney.app.infrastructure.TimeClient;
import com.disney.app.infrastructure.model.TimeResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.OffsetDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ForecastMapperTest {
    private static final OffsetDateTime NOW = OffsetDateTime.now();
    private static final int TEMP_IN_CELSIUS = 26;
    private static final int TEMP_IN_FAHRENHEIT = 80;
    private static final String FORECAST_BLURP = "Sunny day";
    private static final String FAHRENHEIT_SYMBOL = "F";

    @Mock
    private TimeClient timeClient;

    @InjectMocks
    private ForecastMapper forecastMapper;

    public void timeClientReturnsNow() {
        when(timeClient.getCurrentTime()).thenReturn(Mono.just(new TimeResponse(NOW)));
    }

    @Test
    public void testMapOnlyToday() {
        ForecastResponse response = new ForecastResponse();
        response.setProperties(new ForecastResponse.Properties());
        response.getProperties().setPeriods(new ArrayList<>());
        ForecastResponse.Period period1 = new ForecastResponse.Period();
        period1.setNumber(1);
        period1.setName("Today");
        period1.setTemperature(TEMP_IN_FAHRENHEIT);
        period1.setShortForecast(FORECAST_BLURP);
        period1.setTemperatureUnit(FAHRENHEIT_SYMBOL);
        ForecastResponse.Period period2 = new ForecastResponse.Period();
        period2.setNumber(2);
        period2.setName("Tomorrow");
        period2.setTemperature(90);
        response.getProperties().getPeriods().add(period1);
        response.getProperties().getPeriods().add(period2);

        timeClientReturnsNow();

        Mono<WeatherResponse> weatherResponseMono = forecastMapper.map(Mono.just(response));
        StepVerifier.create(weatherResponseMono)
                .expectNextMatches(weatherResponse ->
                        weatherResponse.getDaily().size() == 1 &&
                                weatherResponse.getDaily().get(0).getDay_name().equals(
                                        NOW.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.US)) &&
                                weatherResponse.getDaily().get(0).getTemp_high_celsius() == TEMP_IN_CELSIUS &&
                                weatherResponse.getDaily().get(0).getForecast_blurp().equals(FORECAST_BLURP)
                )
                .expectComplete()
                .verify();
    }

    @Test
    public void testMapWithNullTempUnit() {
        ForecastResponse response = new ForecastResponse();
        response.setProperties(new ForecastResponse.Properties());
        response.getProperties().setPeriods(new ArrayList<>());
        ForecastResponse.Period period1 = new ForecastResponse.Period();
        period1.setNumber(1);
        period1.setName("Today");
        period1.setTemperature(TEMP_IN_CELSIUS);
        period1.setShortForecast(FORECAST_BLURP);
        response.getProperties().getPeriods().add(period1);

        timeClientReturnsNow();

        Mono<WeatherResponse> weatherResponseMono = forecastMapper.map(Mono.just(response));
        StepVerifier.create(weatherResponseMono)
                .expectNextMatches(weatherResponse ->
                                weatherResponse.getDaily().get(0).getTemp_high_celsius() == TEMP_IN_CELSIUS // If no unit is specified, assume Celsius
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
        period1.setTemperature(TEMP_IN_CELSIUS);
        period1.setShortForecast(FORECAST_BLURP);
        response.getProperties().getPeriods().add(period1);

        Mono<WeatherResponse> weatherResponseMono = forecastMapper.map(Mono.just(response));
        StepVerifier.create(weatherResponseMono)
                .expectError()
                .verify();
    }
}
