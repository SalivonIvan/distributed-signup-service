package io.newage.persistence.config;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("dev")
@Configuration
public class DataBaseConfig {

    @Value("${mongo.port}")
    private int port;
    @Value("${mongo.host}")
    private String host;

    @Bean(name = "mongoClient")
    public MongoClient getMongoClient() {
        return new MongoClient(host, port);
    }
}
