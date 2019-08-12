package io.newage.signup.route;

import io.newage.signup.domain.SignupRequest;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.restlet.data.MediaType;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SignupRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        restConfiguration("restlet")
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true")
                .enableCORS(true)
                .contextPath("/api")
                // turn on swagger api-doc
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "User API")
                .apiProperty("api.version", "1.0.0");

        rest("/signup").description("Signup REST service")
                .consumes(MediaType.APPLICATION_JSON.getName())
                .produces(MediaType.APPLICATION_JSON.getName())

                .post().description("signup users").type(SignupRequest.class)
                .responseMessage().code(200).message("Signup successfully.").endResponseMessage()
                .to("direct:process-signup");

        from("direct:process-signup")
                .log("REST request to process the signup message[${body}]")
                .process(exchange -> {
                    SignupRequest signupRequest = exchange.getIn().getBody(SignupRequest.class);
                    signupRequest.setUuid(UUID.randomUUID().toString());
                })
                .marshal().json(JsonLibrary.Jackson)
                .setHeader(KafkaConstants.KEY, constant("Signup"))
                .to("kafka:signup")
                .unmarshal().json(JsonLibrary.Jackson, SignupRequest.class)
                .log("The signup message[${body}] was successfully sent to kafka")
                .setBody(simple("${body.uuid}"));
    }
}
