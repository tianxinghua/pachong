package com.shangpin.iog.mongobase;

/**
 * Created by loyalty on 15/4/10.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

@Configurable

@EnableMongoRepositories(basePackages= "com.shangpin.iog.mongodao")
public class MongodbConfiguration extends AbstractMongoConfiguration {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private static ResourceBundle bdl=null;
    private static String mongodbAddress;
    private static  int port;

    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("mongo");
        mongodbAddress = bdl.getString("Mongodb.Address");
        port = Integer.valueOf(bdl.getString("MongoDB.Port"));
    }



    @Override
    protected String getDatabaseName() {
        return "iog";
    }

    @Override
    public Mongo mongo() throws Exception {
        return mongoClient();   //192.168.20.112     49.213.13.167
    }

    public MongoClient mongoClient() throws Exception{
        String username="writer",database="iog",password="wt@sp520";

        ServerAddress sa = new ServerAddress(mongodbAddress, port);
        List<MongoCredential> mongoCredentialList = new ArrayList<MongoCredential>();
        mongoCredentialList.add(MongoCredential.createMongoCRCredential(username, database, password.toCharArray()));
        return new MongoClient(sa, mongoCredentialList);
//        return new MongoClient(mongodbAddress,port);

//        String username="writer",database="iog",password="wt@sp520";
//
//        ServerAddress sa = new ServerAddress(mongodbAddress, port);
//        MongoCredential credential = MongoCredential.createCredential(username, database, password.toCharArray());
//        return new MongoClient(sa, Arrays.asList(credential));

        // 本地调用
//         return new MongoClient(mongodbAddress,port);
    }

    public @Bean
    MongoTemplate mongoTemplate() throws Exception {
//        return new MongoTemplate(mongo(), getDatabaseName());
        return new MongoTemplate(mongoDbFactory());
    }

    public @Bean
    MongoDbFactory mongoDbFactory() throws Exception {
        return new SimpleMongoDbFactory(mongoClient(), getDatabaseName());
    }


    @Override
    protected String getMappingBasePackage() {
        return "com.shangpin.iog.mongodomain";
    }
}
