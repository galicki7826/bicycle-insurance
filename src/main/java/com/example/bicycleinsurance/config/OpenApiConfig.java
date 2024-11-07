package com.example.bicycleinsurance.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.*;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Bicycle Insurance API")
                        .version("1.0")
                        .description("API for calculating premiums for bicycle insurance policies"));
    }
}
