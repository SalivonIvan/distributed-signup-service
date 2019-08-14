package io.newage.persistence;

import io.newage.persistence.config.DataBaseConfigTest;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.rule.EmbeddedKafkaRule;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PersistenceApp.class, DataBaseConfigTest.class})
@ActiveProfiles("test")
@DirtiesContext
public class PersistenceAppTest {

    @ClassRule
    public static final EmbeddedKafkaRule embeddedKafka =
            new EmbeddedKafkaRule(1, true, "signup");

    @Test
    public void testRunApp() throws Exception {
    }

}