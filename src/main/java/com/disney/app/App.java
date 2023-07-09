package com.disney.app;

import com.disney.app.config.ApplicationConfigs;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
@EnableConfigurationProperties({ApplicationConfigs.WeatherClientConfigs.class})
public class App
{
    public static void main( String[] args )
    {
        SpringApplication.run(App.class, args);
    }
}
