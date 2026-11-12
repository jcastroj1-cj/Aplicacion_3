package com.unidad.inventario.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI inventarioOpenApi() {
        return new OpenAPI().info(new Info()
                .title("API de Inventario de Computadores")
                .version("1.0.0")
                .description("API REST para gestionar usuarios y sus computadores. "
                        + "Arquitectura hexagonal con Spring Boot y PostgreSQL."));
    }
}
