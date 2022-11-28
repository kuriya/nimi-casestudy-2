package com.nimi.guildbank.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class is responsible to configure swagger
 */
@Configuration
public class SwaggerConfig {

    private static final String GROUP_NAME= "guild-bank";
    private static final String PACKAGE_NAME= "com.nimi.guildbank.controller";
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group(GROUP_NAME)
                .packagesToScan(PACKAGE_NAME)
                .pathsToMatch("/**")
                .build();
    }


}