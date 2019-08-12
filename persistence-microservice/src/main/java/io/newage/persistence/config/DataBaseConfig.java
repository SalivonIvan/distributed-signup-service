package io.newage.persistence.config;

import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataBaseConfig {

    @Bean(name = "mongoClient")
    public MongoClient getMongoClient() {
        MongoClient mongoClient = new MongoClient();
        return mongoClient;
    }
}
