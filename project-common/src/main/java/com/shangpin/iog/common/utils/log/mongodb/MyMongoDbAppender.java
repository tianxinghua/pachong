package com.shangpin.iog.common.utils.log.mongodb;

import com.mongodb.DBObject;
import org.apache.log4j.spi.LoggingEvent;
import org.log4mongo.MongoDbAppender;

/**
 * Created by Administrator on 2015/3/26.
 */
public class MyMongoDbAppender extends org.log4mongo.MongoDbAppender {

    private org.log4mongo.LoggingEventBsonifier bsonifier = new LoggingEventBsonifier();

    @Override
    protected void append(LoggingEvent loggingEvent) {
        DBObject bson = this.bsonifier.bsonify(loggingEvent);
        this.append(bson);
    }
//    import org.apache.log4j.Logger;
//    static Logger logger_mongo = Logger.getLogger("mongodb");
//    Map<String,String> mongodb_log_base = new HashMap<>();
//    mongodb_log_base.put("messageid",messageid);
//    //通过ice接口获取供应商商品sku
//    mongodb_log_base.put("supplierid",SupplierID);
//    Map<String,String> mongodb_log_1 = new HashMap<>();
//    mongodb_log_1.putAll(mongodb_log_base);
//    mongodb_log_1.put("message","startting job");
//    logger_mongo.debug(mongodb_log_1);
}
