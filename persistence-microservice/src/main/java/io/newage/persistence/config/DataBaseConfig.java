package io.newage.persistence.config;

import com.mongodb.MongoClient;
import org.apache.camel.PropertyInject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataBaseConfig {

    @PropertyInject(value = "mongo.port", defaultValue = "27017")
    private int port;
    @PropertyInject(value = "mongo.host", defaultValue = "localhost")
    private String host;

    @Bean(name = "mongoClient")
    public MongoClient getMongoClient() {
        MongoClient mongoClient = new MongoClient(host,port);
        return mongoClient;
    }
}
