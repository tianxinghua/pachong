package com.shangpin.iog.mongobase;

/**
 * Created by loyalty on 15/4/10.
 */

import com.mongodb.Mongo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Controller;

import java.util.ResourceBundle;

@Configurable

@EnableMongoRepositories(basePackages= "com.shangpin.iog.mongodao")
public class MongodbConfiguration extends AbstractMongoConfiguration {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private static ResourceBundle bdl=null;
    private static String mongodbAddress;
    private static  int port;

    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("mongodb");
        mongodbAddress = bdl.getString("Mongodb.Address");
        port = Integer.valueOf(bdl.getString("MongoDB.Port"));
    }



    @Override
    protected String getDatabaseName() {
        return "iog";
    }

    @Override
    public Mongo mongo() throws Exception {
        return new Mongo(mongodbAddress,port);   //192.168.20.112     49.213.13.167
    }




    @Override
    protected String getMappingBasePackage() {
        return "com.shangpin.iog.mongodomain";
    }
}
