package com.shangpin.iog.mq.service.producer;


import com.fasterxml.jackson.databind.ObjectMapper;

import com.shangpin.iog.mq.amqp.Queue;
import com.shangpin.iog.mq.dto.ProductDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * Created by lizhongren on 2016/5/4.
 */
@Component
public class MessageSend {

    private static org.apache.log4j.Logger loggerInfo = org.apache.log4j.Logger
            .getLogger("info");
    private static org.apache.log4j.Logger loggerError = org.apache.log4j.Logger
            .getLogger("error");

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 根据类型 通送数据到不同的消息队列
     * @param dto
     * @param type   1:市场价  2：供价
     * @return
     */
    public Boolean  sendMessage(ProductDTO dto,String type)  {
        String body ="";
        try {
//            ObjectMapper mapper = new ObjectMapper();
//            mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
//            body = mapper.writeValueAsString(dto);
            if("1".equals(type)){
                rabbitTemplate.convertAndSend(Queue.QUEUE_PRODUCT_PRICE_EXCHANGE.getMessageName(),
                        Queue.QUEUE_PRODUCT_PRICE_ROUTE.getMessageName(),dto);
            }else if("2".equals(type)){
                rabbitTemplate.convertAndSend(Queue.QUEUE_PRODUCT_PRICE_EXCHANGE.getMessageName(),
                        Queue.QUEUE_PRODUCT_PRICE_ROUTE.getMessageName(),dto);
            }


             return true;
        } catch (Exception e) {
            loggerError.error(dto.toString() + " 发送消息队列失败");
        }
        return false;

    }

}
