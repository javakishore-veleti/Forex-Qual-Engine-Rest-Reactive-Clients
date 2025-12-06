package com.jk.labs.fx.qual_engine.integrations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FxQualClientFactory {

    @Qualifier("restClientStrategy")
    private final FxQualClient restClientStrategy;

    @Qualifier("restTemplateStrategy")
    private final FxQualClient restTemplateStrategy;

    @Qualifier("webClientStrategy")
    private final FxQualClient webClientStrategy;

    public FxQualClient resolve(String type) {
        log.info("resolve: Starting Determining Client Type based on  client type {}", type);

        FxQualClient fxQualClient = switch (type.toLowerCase()) {
            case "rest_template" -> restTemplateStrategy;
            case "rest_client" -> restClientStrategy;
            case "web_client" -> webClientStrategy;
            default -> restClientStrategy;
        };

        log.info("resolve: Exiting Determining Client Type based on  client type {} " +
                "clientImplName {}", type, fxQualClient.clientImplName());
        return fxQualClient;
    }
}