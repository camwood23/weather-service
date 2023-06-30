package com.disney.app.infrastructure;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class WeatherClient {

    private static final String FORECAST_HOST = "https://api.weather.gov"; // TODO: Add these to config file
    private static final String FORECAST_PATH = "/gridpoints/MLB/33,70/forecast";

    private final WebClient client;

    public WeatherClient(WebClient.Builder builder) {
        this.client = builder.baseUrl(FORECAST_HOST).build();
    }

    public Mono<ForecastResponse> getForecast() {
        return this.client.get().uri(FORECAST_PATH).accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(ForecastResponse.class); // TODO Investigate using flux object instead of mono
    }

}
