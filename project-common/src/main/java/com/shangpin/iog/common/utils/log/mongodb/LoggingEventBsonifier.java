package com.shangpin.iog.common.utils.log.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.apache.log4j.spi.LoggingEvent;
import org.log4mongo.LoggingEventBsonifierImpl;

import java.util.Date;
import java.util.Map;

/**
 * Created by Administrator on 2015/3/26.
 */
public class LoggingEventBsonifier extends LoggingEventBsonifierImpl {

    public DBObject bsonify(LoggingEvent loggingEvent) {
        BasicDBObject result = null;
        if (loggingEvent != null) {
            result = new BasicDBObject();
            result.put("timestamp", new Date(loggingEvent.getTimeStamp()));
            this.nullSafePut(result, "level", loggingEvent.getLevel().toString());
            this.nullSafePut(result, "thread", loggingEvent.getThreadName());

            if (loggingEvent.getMessage() instanceof Map) {
                Map message = (Map) loggingEvent.getMessage();
                for (Object key : message.keySet()) {
                    if (result.containsKey(key)) continue;
                    this.nullSafePut(result, (String) key, message.get(key) != null ? message.get(key).toString() : "");
                }
            } else {
                this.nullSafePut(result, "message", loggingEvent.getMessage().toString());
            }

            /*this.nullSafePut(result, "loggerName", this.bsonifyClassName(loggingEvent.getLoggerName()));
            this.addMDCInformation(result, loggingEvent.getProperties());
            this.addLocationInformation(result, loggingEvent.getLocationInformation());
            this.addThrowableInformation(result, loggingEvent.getThrowableInformation());
            this.addHostnameInformation(result);*/
        }
        return result;
    }
}
