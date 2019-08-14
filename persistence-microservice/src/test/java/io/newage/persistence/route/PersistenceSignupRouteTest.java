package io.newage.persistence.route;

import io.newage.persistence.PersistenceApp;
import io.newage.persistence.constant.Topic;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PersistenceApp.class)
@DirtiesContext
public class PersistenceSignupRouteTest {

    @ClassRule
    public static final EmbeddedKafkaRule embeddedKafka =
            new EmbeddedKafkaRule(1, true, Topic.SIGNUP);

    @Autowired
    private CamelContext camelContext;

    @Before
    public void init() throws Exception {
        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:sendToSignupTopic")
                        .setHeader(KafkaConstants.KEY, constant("SIGNUP"))
                        .to("kafka:" + Topic.SIGNUP);
            }
        });
    }

    @Test
    public void testSendMessageToSignTopic() throws Exception {
        camelContext.createProducerTemplate().sendBody("direct:sendToSignupTopic"
                , "{\"_id\":\"123\",\"email\":\"test@test.com\",\"password\":\"12345678\"}");
    }

}