package io.newage.signup.route;

import io.newage.signup.model.ErrorInfo;
import io.newage.signup.model.SignupRequest;
import org.apache.camel.Exchange;
import org.apache.camel.ValidationException;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.restlet.data.MediaType;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static io.newage.signup.model.ErrorConstant.ERR_VALIDATION;
import static io.newage.signup.route.Path.Service.SIGNUP;
import static io.newage.signup.route.Path.Web.USER;

@Component
public class SignupRoute extends RouteBuilder {

    private static final String KAFKA_URI = "kafka:signup";
    private static final String PROCESS_SIGNUP_URI = "direct:process-signup";
    private static final String BEAN_VALIDATOR_URI = "bean-validator://signup";

    @Override
    public void configure() throws Exception {

        onException(Exception.class).handled(true).to(ErrorHandlerRoute.ERROR_HANDLER_URI);
//        onException(ValidationException.class).handled(true)
//                .process(exchange -> {
//                    exchange.getOut().setBody(ErrorInfo.builder()
//                            .code(ERR_VALIDATION.getCode())
//                            .errorId(ERR_VALIDATION.getErrorId())
//                            .message(ERR_VALIDATION.getMessage())
//                            .variable(exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class).getMessage())
//                            .build());
//                })
//                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
//                .marshal().json(JsonLibrary.Jackson);

        rest(USER + SIGNUP).description("Signup REST service")
                .consumes(MediaType.APPLICATION_JSON.getName())
                .produces(MediaType.APPLICATION_JSON.getName())

                .post().description("signup users").type(SignupRequest.class)
                .responseMessage().code(200).message("Signup successfully.").endResponseMessage()
                .to(PROCESS_SIGNUP_URI);

        from(PROCESS_SIGNUP_URI)
                .to(BEAN_VALIDATOR_URI)
                .log("REST request to process the signup message[${body}]")
                .process(exchange -> {
                    SignupRequest signupRequest = exchange.getIn().getBody(SignupRequest.class);
                    signupRequest.setUuid(UUID.randomUUID().toString());
                })
                .marshal().json(JsonLibrary.Jackson)
//                .multicast((oldExchange, newExchange) -> oldExchange).end()
                .setHeader(KafkaConstants.KEY, constant("Signup"))
                .to(KAFKA_URI)
                .unmarshal().json(JsonLibrary.Jackson, SignupRequest.class)
                .log("The signup message[${body}] was successfully sent to kafka")
                .setBody(simple("${body.uuid}"));
    }
}
