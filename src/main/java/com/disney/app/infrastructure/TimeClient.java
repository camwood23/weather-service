package com.disney.app.infrastructure;

import com.disney.app.infrastructure.model.TimeResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;

@Component
@Slf4j
public class TimeClient {
    private static final Logger logger = LoggerFactory.getLogger(TimeClient.class);

    private static final String TIME_HOST = "http://worldtimeapi.org";
    private static final String TIME_PATH = "/api/timezone/America/New_York";

    private final WebClient client;

    public TimeClient(WebClient.Builder builder) {
        this.client = builder
                .baseUrl(TIME_HOST)
                .build();
    }

    public Mono<TimeResponse> getCurrentTime() {
        return this.client.get().uri(TIME_PATH)
                .retrieve()
                .bodyToMono(TimeResponse.class)
                .doOnNext(response -> logger.info(response.toString()))
                .onErrorResume(throwable -> Mono.just(new TimeResponse(OffsetDateTime.now())));
    }
}
