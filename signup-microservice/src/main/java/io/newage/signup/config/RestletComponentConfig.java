package io.newage.signup.config;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestletComponentConfig extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        restConfiguration("restlet")
                .dataFormatProperty("prettyPrint", "true")
                .enableCORS(true)
                .contextPath("{{camel.component.restlet.context-path}}")
                .apiContextPath("{{camel.component.restlet.api-context-path}}")
                .apiProperty("api.title", "{{camel.component.restlet.api-title}}")
                .apiProperty("api.version", "{{camel.component.restlet.api-version}}");
    }
}
