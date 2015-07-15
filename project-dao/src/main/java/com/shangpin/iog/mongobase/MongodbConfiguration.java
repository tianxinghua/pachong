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

@Configurable

@EnableMongoRepositories(basePackages= "com.shangpin.iog.mongodao")
public class MongodbConfiguration extends AbstractMongoConfiguration {
    Logger logger = LoggerFactory.getLogger(this.getClass());




    @Override
    protected String getDatabaseName() {
        return "iog";
    }

    @Override
    public Mongo mongo() throws Exception {
        return new Mongo("192.168.20.82",27017);
    }




    @Override
    protected String getMappingBasePackage() {
        return "com.shangpin.iog.mongodomain";
    }
}
