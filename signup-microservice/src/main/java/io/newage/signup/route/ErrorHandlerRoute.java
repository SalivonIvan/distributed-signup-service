package io.newage.signup.route;

import io.newage.signup.model.ErrorInfo;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.ValidationException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import static io.newage.signup.model.ErrorConstant.ERR_VALIDATION;

@Component
public class ErrorHandlerRoute extends RouteBuilder {

    public static final String ERROR_HANDLER_URI = "direct:errorHandler";

    @Override
    public void configure() throws Exception {

        onException(Exception.class).handled(true)
                .process(exchange -> {
                    exchange.getOut().setBody(ErrorInfo.builder()
                            .code(ERR_VALIDATION.getCode())
                            .errorId(ERR_VALIDATION.getErrorId())
                            .message(ERR_VALIDATION.getMessage())
                            .variable(exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class).getMessage())
                            .build());
                })
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
                .marshal().json(JsonLibrary.Jackson);

        from(ERROR_HANDLER_URI)
                .choice()
                .when(exchange -> exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class) instanceof ValidationException)
                .to("direct:validationExceptionHandle")
                .otherwise()
                .to("direct:exceptionHandle")
                .endChoice()
                .end()
                .log(LoggingLevel.ERROR, "Error[${body}] was process.");

        from("direct:validationExceptionHandle")
                .process(exchange -> {
                    exchange.getOut().setBody(ErrorInfo.builder()
                            .code(ERR_VALIDATION.getCode())
                            .errorId(ERR_VALIDATION.getErrorId())
                            .message(ERR_VALIDATION.getMessage())
                            .variable(exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class).getMessage())
                            .build());
                })
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
                .marshal().json(JsonLibrary.Jackson);

    }
}
