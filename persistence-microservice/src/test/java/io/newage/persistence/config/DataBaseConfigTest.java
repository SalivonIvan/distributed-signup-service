package io.newage.persistence.config;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@TestConfiguration
public class DataBaseConfigTest {

    @Value("${mongo.host}")
    private String host;
    @Autowired
    private Environment environment;


    @Bean(name = "mongoClient")
    public MongoClient getMongoClient() {
        MongoClient mongoClient = new MongoClient(host, environment.getProperty("local.mongo.port", Integer.class));
        return mongoClient;
    }
}
