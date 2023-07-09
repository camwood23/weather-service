package com.disney.app.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfigs {
    @Getter
    @Setter
    @ConfigurationProperties(prefix = "clients.weather")
    public static class WeatherClientConfigs {
        private String host;
        private String path;
    }
}
