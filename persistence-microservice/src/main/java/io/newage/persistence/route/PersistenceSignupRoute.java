package io.newage.persistence.route;

import com.mongodb.DBObject;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import static io.newage.persistence.constant.Topic.SIGNUP;

@Component
public class PersistenceSignupRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        onException(Exception.class).handled(true).to(ErrorHandlerRoute.ERROR_HANDLER_URI);

        from("kafka:" + SIGNUP + "?groupId={{camel.component.kafka.consumer-group-id}}")
                .routeId(this.getClass().getSimpleName())
                .log("Message received from Kafka")
                .log("    TOPIC: ${headers[kafka.TOPIC]}")
                .log("    PARTITION: ${headers[kafka.PARTITION]}")
                .log("    OFFSET: ${headers[kafka.OFFSET]}")
                .log("    KEY: ${headers[kafka.KEY]}")
                .convertBodyTo(DBObject.class)
                .to("mongodb:mongoClient?database=persistence&collection=profile&operation=save")
                .log("Profile[${headers[CamelMongoOid]}] was saving to BD");
    }
}
