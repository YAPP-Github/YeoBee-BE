package com.example.yeobee.common.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

public class SwaggerConfig {

    private final Optional<String> serverUrl;

    private final Environment env;

    public SwaggerConfig(@Value("${yeobee.server-url:}") String serverUrl, Environment env) {
        this.serverUrl = Optional.ofNullable(serverUrl).filter(s -> !s.isEmpty());
        this.env = env;
    }

    @ConditionalOnProperty(prefix = "yeobee", name = "server-url")
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .servers(List.of(
                new Server().url(
                    serverUrl.orElse(getDefaultUrl())
                )
            ))
            .info(new Info().title("여비 API").version("1.0.0"))
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
            .components(new Components().addSecuritySchemes("bearerAuth",
                                                            new SecurityScheme().name("bearerAuth")
                                                                .type(SecurityScheme.Type.HTTP)
                                                                .scheme("bearer")
                                                                .bearerFormat("JWT")));
    }

    private String getDefaultUrl() {
        String port = env.getProperty("server.port", "8080");
        String contextPath = env.getProperty("server.servlet.context-path", "");
        return "http://localhost:" + port + contextPath;
    }
}
