package com.disney.app.application.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Daily {
    String day_name;
    int temp_high_celsius;
    String forecast_blurp;
}
