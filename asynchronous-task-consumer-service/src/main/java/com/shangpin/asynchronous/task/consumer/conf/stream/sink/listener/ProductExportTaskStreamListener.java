package com.shangpin.asynchronous.task.consumer.conf.stream.sink.listener;


import com.shangpin.asynchronous.task.consumer.conf.stream.sink.channel.ProductExportTaskSink;

import com.shangpin.asynchronous.task.consumer.conf.stream.sink.channel.ProductImportTaskSink;
import com.shangpin.asynchronous.task.consumer.productexport.adapter.ProductExportHandler;
import com.shangpin.ephub.client.message.task.product.body.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import java.util.Map;

@EnableBinding({ProductExportTaskSink.class})
public class ProductExportTaskStreamListener {


    @Autowired
    private ProductExportHandler productExportHandler;

    /**
     * 待处理商品导入任务数据流发送
     * @param message 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    @StreamListener(ProductExportTaskSink.PRODUCT_EXPORT)
    public void productStreamListen(@Payload Task message, @Headers Map<String,Object> headers) throws Exception  {
        productExportHandler.productExportTask(message,headers);
    }

}
