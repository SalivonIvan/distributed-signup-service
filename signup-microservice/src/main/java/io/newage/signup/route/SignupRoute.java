package io.newage.signup.route;

import io.newage.signup.aggregate.AfterKafkaAggregate;
import io.newage.signup.model.SignupRequest;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.restlet.data.MediaType;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static io.newage.signup.route.Path.Service.SIGNUP;
import static io.newage.signup.route.Path.Web.USER;

@Component
public class SignupRoute extends RouteBuilder {

    private static final String KAFKA_URI = "kafka:signup";
    private static final String PROCESS_SIGNUP_URI = "direct:process-signup";
    private static final String BEAN_VALIDATOR_URI = "bean-validator://signup";
    private static final String PRODUCER_TO_KAFKA_URI = "direct:producerToKafka";

    @Override
    public void configure() throws Exception {

        onException(Exception.class).handled(true).to(ErrorHandlerRoute.ERROR_HANDLER_URI);

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
                .enrich(PRODUCER_TO_KAFKA_URI, new AfterKafkaAggregate())
                .log("The signup message[${body}] was successfully sent to kafka")
                .setBody(simple("${body.uuid}"));

        from(PRODUCER_TO_KAFKA_URI)
                .marshal().json(JsonLibrary.Jackson)
                .setHeader(KafkaConstants.KEY, constant("Signup"))
                .to(KAFKA_URI);
    }
}
