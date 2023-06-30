package com.disney.app.application;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/weather")
public class WeatherResource {

    @GetMapping("/current")
    public Mono<String> getCurrentWeather() {
        // TODO: Don't forget to add logging
        return Mono.just("Hello World!"); // TODO: Need business logic that will map client call to response object
    }
}
