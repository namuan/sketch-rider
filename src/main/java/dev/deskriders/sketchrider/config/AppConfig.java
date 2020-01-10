package dev.deskriders.sketchrider.config;

import io.micronaut.context.annotation.ConfigurationProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(value = "app")
public class AppConfig {
    private String dynamo;
}
