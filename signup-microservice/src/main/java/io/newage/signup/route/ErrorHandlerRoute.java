package io.newage.signup.route;

import io.newage.signup.model.ErrorInfo;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ValidationException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import static io.newage.signup.model.ErrorConstant.ERR_UNKNOWN;
import static io.newage.signup.model.ErrorConstant.ERR_VALIDATION;

@Component
public class ErrorHandlerRoute extends RouteBuilder {

    static final String ERROR_HANDLER_URI = "direct:errorHandler";

    @Override
    public void configure() throws Exception {

        from(ERROR_HANDLER_URI).routeId(ErrorHandlerRoute.class.getSimpleName())
                .log("${messageHistory}")
                .log("${exception.stacktrace}")
                .log(LoggingLevel.ERROR, "Route error [${exception.message}]")
                .choice()
                .when(exchange -> exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class) instanceof ValidationException)
                .setBody().constant(ERR_VALIDATION)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
                .otherwise()
                .setBody().constant(ERR_UNKNOWN)
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
                .end()
                .convertBodyTo(ErrorInfo.class)
                .marshal().json(JsonLibrary.Jackson)
                .log(LoggingLevel.WARN, "Error[${body}] was generated.");
    }
}
