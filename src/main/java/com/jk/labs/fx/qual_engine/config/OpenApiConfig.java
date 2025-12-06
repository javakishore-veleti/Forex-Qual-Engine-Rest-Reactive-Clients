package com.jk.labs.fx.qual_engine.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI fxQualOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FX Qual Engine API")
                        .description("REST API for client qualification, promo validation, FX product eligibility, and market-rate lookup.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("JK Labs")
                                .email("support@jklabs.com"))
                        .license(new License().name("Apache 2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("FX Qual Engine Documentation")
                        .url("https://jklabs.com/docs/fxqual"));
    }
}