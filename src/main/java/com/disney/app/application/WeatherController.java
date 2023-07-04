package com.disney.app.application;

import com.disney.app.application.model.WeatherResponse;
import com.disney.app.domain.ForecastMapper;
import com.disney.app.infrastructure.WeatherClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/weather")
public class WeatherController {
    private final ForecastMapper forecastMapper;
    private final WeatherClient weatherClient;

    @Autowired
    public WeatherController(ForecastMapper forecastMapper, WeatherClient weatherClient) {
        this.forecastMapper = forecastMapper;
        this.weatherClient = weatherClient;
    }

    @GetMapping("/current")
    public Mono<WeatherResponse> getCurrentWeather() {
        return forecastMapper.map(weatherClient.getForecast());
    }
}
