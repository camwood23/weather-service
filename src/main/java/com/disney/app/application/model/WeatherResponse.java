package com.disney.app.application.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class WeatherResponse {
    List<Daily> daily;
}
