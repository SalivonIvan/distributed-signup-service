package io.newage.persistence.route;

import io.newage.persistence.PersistenceApp;
import io.newage.persistence.config.DataBaseConfigTest;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static io.newage.persistence.constant.Topic.SIGNUP;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PersistenceApp.class, DataBaseConfigTest.class})
@ActiveProfiles("test")
@DirtiesContext
public class PersistenceSignupRouteTest {

    @ClassRule
    public static final EmbeddedKafkaRule embeddedKafka =
            new EmbeddedKafkaRule(1, true, SIGNUP);

    @Autowired
    private CamelContext camelContext;

    @Before
    public void init() throws Exception {
        camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:sendToSignupTopic")
                        .setHeader(KafkaConstants.KEY, constant("SIGNUP"))
                        .to("kafka:" + SIGNUP);

                from("direct:findAll")
                        .to("mongodb:mongoClient?database=persistence&collection=profile&operation=findAll");
            }
        });
    }

    @Test
    public void testSendMessageToSignTopic() throws Exception {
        ProducerTemplate producerTemplate = camelContext.createProducerTemplate();
        producerTemplate.sendBody("direct:sendToSignupTopic"
                , "{\"_id\":\"123\",\"email\":\"test@test.com\",\"password\":\"12345678\"}");
        producerTemplate.requestBody("direct:findAll", null, String.class);
    }

}