package com.shangpin.iog.mongobase;

import com.mongodb.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by lizhongren on 2017/1/11.
 */
public class Test {
    public static  void main(String[] args){
        try {
            ServerAddress sa = new ServerAddress("47.90.79.139", 27017);
            List<MongoCredential> mongoCredentialList = new ArrayList<MongoCredential>();
            mongoCredentialList.add(MongoCredential.createMongoCRCredential("writer", "iog", "wt@sp520".toCharArray()));

            MongoCredential credential = MongoCredential.createCredential("writer", "iog", "wt@sp520".toCharArray());
            MongoClient mongo = new MongoClient(sa, Arrays.asList(credential));

        //    MongoClient  mongo  = new  MongoClient(sa, mongoCredentialList, MongoClientOptions.builder().serverSelectionTimeout(1000*60).build());
//            mongo.getDB("iog").command("ping");
            System.out.print(mongo.getDB("iog").command("ping"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
