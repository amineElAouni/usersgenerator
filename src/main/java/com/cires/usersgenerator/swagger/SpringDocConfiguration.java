package com.cires.usersgenerator.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.cires.usersgenerator.swagger.SwaggerConstant.*;

@Configuration
@SecurityScheme(
        name = BEARER_AUTHENTICATION,
        type = SecuritySchemeType.HTTP,
        bearerFormat = JWT,
        scheme = BEARER
)

@OpenAPIDefinition(servers = {@Server(url = "/", description = "Default Server URL")})
public class SpringDocConfiguration {

    @Bean
    public OpenAPI baseOpenApi() {
        return new OpenAPI().info(new Info().title("User Documentation").version("1.0.0").description("User Documentation"));
    }
}
