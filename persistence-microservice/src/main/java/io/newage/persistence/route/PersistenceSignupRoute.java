package io.newage.persistence.route;

import com.mongodb.DBObject;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class PersistenceSignupRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {


        from("kafka:signup?groupId=persistence")
                .log("Message received from Kafka : ${body}")
                .log("    on the topic ${headers[kafka.TOPIC]}")
                .log("    on the partition ${headers[kafka.PARTITION]}")
                .log("    with the offset ${headers[kafka.OFFSET]}")
                .log("    with the key ${headers[kafka.KEY]}")
                .convertBodyTo(DBObject.class)
                .to("mongodb:mongoClient?database=persistence&collection=profile&operation=save");
    }
}
