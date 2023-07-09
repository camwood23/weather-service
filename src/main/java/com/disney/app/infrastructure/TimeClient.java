package com.disney.app.infrastructure;

import com.disney.app.config.ApplicationConfigs;
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

    private final ApplicationConfigs.TimeClientConfigs timeClientConfigs;

    private final WebClient client;

    public TimeClient(WebClient.Builder builder, ApplicationConfigs.TimeClientConfigs timeClientConfigs) {
        this.timeClientConfigs = timeClientConfigs;
        this.client = builder
                .baseUrl(timeClientConfigs.getHost())
                .build();
    }

    public Mono<TimeResponse> getCurrentTime() {
        return this.client.get().uri(timeClientConfigs.getPath())
                .retrieve()
                .bodyToMono(TimeResponse.class)
                .doOnNext(response -> logger.info(response.toString()))
                .onErrorResume(throwable -> Mono.just(new TimeResponse(OffsetDateTime.now())));
    }
}
