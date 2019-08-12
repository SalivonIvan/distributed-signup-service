package io.newage.signup;

import io.newage.signup.domain.SignupRequest;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.restlet.data.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SignupRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        // this can also be configured in application.properties
        restConfiguration("restlet")
                .bindingMode(RestBindingMode.json)
                .dataFormatProperty("prettyPrint", "true")
                .enableCORS(true)
                .host("localhost")
                .port("8080")
                .contextPath("/api")
                // turn on swagger api-doc
                .apiContextPath("/api-doc")
                .apiProperty("api.title", "User API")
                .apiProperty("api.version", "1.0.0");
        System.out.println("START");
        from("restlet:hello")
                .to("direct:hello");

        rest("/signup").description("Signup REST service")
                .consumes(MediaType.APPLICATION_JSON.getName())
                .produces(MediaType.APPLICATION_JSON.getName())

                .post().description("signup users").outType(SignupRequest.class)
                .responseMessage().code(200).message("Signup successfully.").endResponseMessage()
                .to("direct:hello");

        from("direct:hello")
                .process(exchange -> {
                    System.out.println("");
                })
                .log("HELLO");
    }
}
