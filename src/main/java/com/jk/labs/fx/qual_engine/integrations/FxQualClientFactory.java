package com.jk.labs.fx.qual_engine.integrations;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FxQualClientFactory {

    @Qualifier("restClientStrategy")
    private final FxQualClient restClientStrategy;

    @Qualifier("restTemplateStrategy")
    private final FxQualClient restTemplateStrategy;

    @Qualifier("webClientStrategy")
    private final FxQualClient webClientStrategy;

    public FxQualClient resolve(String type) {
        return switch (type.toLowerCase()) {
            case "rest_template" -> restTemplateStrategy;
            case "rest_client" -> restClientStrategy;
            case "web_client" -> webClientStrategy;
            default -> restClientStrategy;
        };
    }
}