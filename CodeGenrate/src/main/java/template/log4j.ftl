#root
log4j.rootLogger=WARN, file
log4j.appender.file=org.apache.log4j.RollingFileAppender
log4j.appender.file.Encoding=UTF-8
log4j.appender.file.File=log/${supplierName}${stock}-logs.log
log4j.appender.file.Append=true
log4j.appender.file.MaxFileSize=5MB
log4j.appender.file.MaxBackupIndex=0
log4j.appender.file.layout=org.apache.log4j.PatternLayout
log4j.appender.file.layout.ConversionPattern=[%-5p] %d{yyyy-MM-dd HH\:mm\:ss,SSS} method\:%l%n%m%n

#private static Logger logger = Logger.getLogger("info");
#private static Logger logger = Logger.getLogger("error");

#right information
log4j.logger.info=INFO, info
#daily log's format
log4j.appender.info=org.apache.log4j.DailyRollingFileAppender
log4j.appender.info.Encoding=UTF-8
log4j.appender.info.File=log/${supplierName}${stock}-info.log
log4j.appender.info.Append=true
log4j.appender.info.DatePattern=_yyyy-MM-dd'.log'
#log4j.appender.info.MaxFileSize=1MB
#log4j.appender.info.MaxBackupIndex=2
log4j.appender.info.layout=org.apache.log4j.PatternLayout
log4j.appender.info.layout.ConversionPattern=\r\n--------Start----------\r\n[%-5p] %d{yyyy-MM-dd HH\:mm\:ss,SSS} method\:%l%n%m%n\r\n----------End---------\r\n
#not out to log4j.rootLogger
log4j.additivity.info=false

#error log
log4j.logger.error=ERROR, error
#daily log's format
log4j.appender.error=org.apache.log4j.DailyRollingFileAppender
log4j.appender.error.Encoding=UTF-8
log4j.appender.error.File=log/${supplierName}${stock}-error.log
log4j.appender.error.Append=true
log4j.appender.error.DatePattern=_yyyy-MM-dd'.log'
#log4j.appender.error.MaxFileSize=1MB
#log4j.appender.error.MaxBackupIndex=2
log4j.appender.error.layout=org.apache.log4j.PatternLayout
<#if stock=='-stock'>
log4j.appender.error.layout.ConversionPattern=%m%n
<#else>
log4j.appender.error.layout.ConversionPattern=\r\n--------Start----------\r\n[%-5p] %d{yyyy-MM-dd HH\:mm\:ss,SSS} method\:%l%n%m%n\r\n----------End---------\r\n
</#if>

#not out to log4j.rootLogger
log4j.additivity.error=false

#log4j for mongodb
log4j.logger.mongodb=INFO, MongoDB
#org.log4mongo.MongoDbAppender
log4j.appender.MongoDB=com.shangpin.iog.common.utils.log.mongodb.MyMongoDbAppender
#mongodb's name
log4j.appender.MongoDB.databaseName=iog
#mongodb's table name
log4j.appender.MongoDB.collectionName=operate_log
log4j.appender.MongoDB.hostname=@db.MongoDB.hostname@
log4j.appender.MongoDB.port=27017
#not out to log4j.rootLogger
log4j.additivity.mongodb=false