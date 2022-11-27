package com.nimi.guildbank.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class is responsible to configure swagger
 */
@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("guild-bank")
                .packagesToScan("com.nimi.guildbank.controller")
                .pathsToMatch("/**")
                .build();
    }


}