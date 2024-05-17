package com.d105.loanplease.global.config;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        SecurityScheme bearer = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("Authorization")
                .in(SecurityScheme.In.HEADER)
                .name(HttpHeaders.AUTHORIZATION);

        // Security 요청 설정
        SecurityRequirement addSecurityItem = new SecurityRequirement();
        addSecurityItem.addList("Authorization");

        Components components = new Components()
                .addSecuritySchemes("Authorization", bearer);

        return new OpenAPI()
                .components(components)
                .addSecurityItem(addSecurityItem)
                .addServersItem(new Server().url("https://loanplease.kr")
                        .description("Default Server URL"))
                .addServersItem(new Server().url("http://localhost:8080")
                        .description("Local Development Server"));
//                .info(info);
    }

}
