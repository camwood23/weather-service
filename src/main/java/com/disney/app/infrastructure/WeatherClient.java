package com.disney.app.infrastructure;

import com.disney.app.infrastructure.model.ForecastResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class WeatherClient {

    private static final Logger logger = LoggerFactory.getLogger(WeatherClient.class);

    private static final String FORECAST_HOST = "https://api.weather.gov";
    private static final String FORECAST_PATH = "/gridpoints/MLB/33,70/forecast";

    private final WebClient client;

    public WeatherClient(WebClient.Builder builder) {
        this.client = builder
                .baseUrl(FORECAST_HOST)
                .build();
    }

    public Mono<ForecastResponse> getForecast() {
        return this.client.get().uri(FORECAST_PATH)
                .retrieve()
                .bodyToMono(ForecastResponse.class)
                .doOnNext(response -> logger.info(response.toString()));
    }

}
