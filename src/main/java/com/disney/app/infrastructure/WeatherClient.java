package com.disney.app.infrastructure;

import com.disney.app.config.ApplicationConfigs;
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

    private final ApplicationConfigs.WeatherClientConfigs weatherClientConfigs;

    private final WebClient client;

    public WeatherClient(WebClient.Builder builder, ApplicationConfigs.WeatherClientConfigs weatherClientConfigs) {
        this.weatherClientConfigs = weatherClientConfigs;
        this.client = builder
                .baseUrl(weatherClientConfigs.getHost())
                .build();
    }

    public Mono<ForecastResponse> getForecast() {
        return this.client.get().uri(weatherClientConfigs.getPath())
                .retrieve()
                .bodyToMono(ForecastResponse.class)
                .doOnNext(response -> logger.info(response.toString()));
    }

}
