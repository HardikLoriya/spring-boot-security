package com.spring.jwt.refresh.token.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String jwtScheme = "hardik JWT";
        final String basicScheme = "hardik Basic";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement()
                        .addList(jwtScheme)
                        .addList(basicScheme)
                )
                .components(
                        new Components()
                                .addSecuritySchemes(jwtScheme,
                                        new SecurityScheme()
                                                .name(jwtScheme)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                                .addSecuritySchemes(basicScheme,
                                        new SecurityScheme()
                                                .name(basicScheme)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("basic")
                                )
                )
                .info(new Info()
                        .title("Springboot Security JWT")
                        .version("1.0"));
    }

}
