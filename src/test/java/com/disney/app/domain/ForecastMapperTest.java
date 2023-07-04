package com.disney.app.domain;

import com.disney.app.application.model.WeatherResponse;
import com.disney.app.infrastructure.ForecastResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ForecastMapperTest {
    @Autowired
    private ForecastMapper forecastMapper;

    @Test
    public void testMap() {
        ForecastResponse response = new ForecastResponse();
        response.setProperties(new ForecastResponse.Properties());
        response.getProperties().setPeriods(new ArrayList<>());
        ForecastResponse.Period period1 = new ForecastResponse.Period();
        period1.setName("Today");
        period1.setTemperature(80);
        period1.setShortForecast("Sunny day");
        ForecastResponse.Period period2 = new ForecastResponse.Period();
        period2.setName("Tomorrow");
        period2.setTemperature(90);
        response.getProperties().getPeriods().add(period1);
        response.getProperties().getPeriods().add(period2);


        Mono<WeatherResponse> weatherResponseMono = forecastMapper.map(Mono.just(response));
        StepVerifier.create(weatherResponseMono)
                .expectNextMatches(weatherResponse ->
                        weatherResponse.getDaily().size() == 1 &&
                                weatherResponse.getDaily().get(0).getDay_name().equals("Today") &&
                                weatherResponse.getDaily().get(0).getTemp_high_celsius() == 80 &&
                                weatherResponse.getDaily().get(0).getForecast_blurp().equals("Sunny day")
                )
                .expectComplete()
                .verify();
    }
}
