package io.newage.persistence.route;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ErrorHandlerRoute extends RouteBuilder {

    static final String ERROR_HANDLER_URI = "direct:errorHandler";

    @Override
    public void configure() throws Exception {

        from(ERROR_HANDLER_URI).routeId(ErrorHandlerRoute.class.getName())
                .log(LoggingLevel.ERROR, "Route error [${exception.message}]");
    }
}
