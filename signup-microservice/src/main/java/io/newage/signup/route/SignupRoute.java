package io.newage.signup.route;

import io.newage.signup.aggregate.AfterKafkaAggregate;
import io.newage.signup.constant.Topic;
import io.newage.signup.model.SignupRequest;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.restlet.data.MediaType;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static io.newage.signup.constant.Path.Service.SIGNUP;
import static io.newage.signup.constant.Path.Web.USER;

@Component
public class SignupRoute extends RouteBuilder {

    private static final String PROCESS_SIGNUP_URI = "direct:process-signup";
    private static final String BEAN_VALIDATOR_URI = "bean-validator://signup";
    private static final String PREPARE_MESSAGE_TO_KAFKA_URI = "direct:prepareMessageToKafka";
    private static final String KAFKA_SIGNUP_KEY = "SIGNUP";

    @Override
    public void configure() throws Exception {

        onException(Exception.class).handled(true).to(ErrorHandlerRoute.ERROR_HANDLER_URI);

        rest(USER + SIGNUP).description("Signup REST service")
                .consumes(MediaType.APPLICATION_JSON.getName())
                .produces(MediaType.APPLICATION_JSON.getName())

                .post().description("signup users").type(SignupRequest.class).outType(String.class)
                .responseMessage().code(200).message("Signup successfully.").endResponseMessage()
                .to(PROCESS_SIGNUP_URI);

        from(PROCESS_SIGNUP_URI)
                .routeId(this.getClass().getSimpleName())
                .unmarshal().json(JsonLibrary.Jackson, SignupRequest.class)
                .to(BEAN_VALIDATOR_URI)
                .log("REST request to process the signup message[${body}]")
                .process(exchange -> {
                    SignupRequest signupRequest = exchange.getIn().getBody(SignupRequest.class);
                    signupRequest.set_id(UUID.randomUUID().toString());
                })
                .enrich(PREPARE_MESSAGE_TO_KAFKA_URI, new AfterKafkaAggregate())
                .log("The signup message[${body}] was successfully sent to kafka")
                .setBody(simple("${body.get_id}"));

        from(PREPARE_MESSAGE_TO_KAFKA_URI)
                .marshal().json(JsonLibrary.Jackson)
                .setHeader(KafkaConstants.KEY, constant(KAFKA_SIGNUP_KEY))
                .to("kafka:" + Topic.SIGNUP);
    }
}
