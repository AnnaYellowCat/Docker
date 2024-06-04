package ru.gudkova.mongoproject.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig {
    @Bean
    public MongoClient mongoLogin() {
        return MongoClients.create("mongodb://login:login@mongodb/shop?authSource=admin&authMechanism=SCRAM-SHA-1");
    }
    @Bean
    public MongoTemplate mongoLoginTemplate() {
        return new MongoTemplate(mongoLogin(), "shop");
    }

    @Bean
    public MongoClient mongoUser() {
        return MongoClients.create("mongodb://client:client@mongodb/shop?authSource=admin&authMechanism=SCRAM-SHA-1");
    }
    @Bean
    public MongoTemplate mongoUserTemplate() {
        return new MongoTemplate(mongoUser(), "shop");
    }

    @Bean
    public MongoClient mongoManager() {
        return MongoClients.create("mongodb://manager:manager@mongodb/shop?authSource=admin&authMechanism=SCRAM-SHA-1");
    }
    @Bean
    public MongoTemplate mongoManagerTemplate() {
        return new MongoTemplate(mongoManager(), "shop");
    }

    @Bean
    public MongoClient mongoDirector() {
        return MongoClients.create("mongodb://director:director@mongodb/shop?authSource=admin&authMechanism=SCRAM-SHA-1");
    }
    @Bean
    public MongoTemplate mongoDirectorTemplate() {
        return new MongoTemplate(mongoDirector(), "shop");
    }
}
