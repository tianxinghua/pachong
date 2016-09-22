package com.shangpin.iog.mq.amqp;

public enum Queue {
    /**
     * 产品
     */



    QUEUE_PRODUCT_PRICE_QUEUE("QUEUE_EP_PRODUCT_PRICE",1),
    QUEUE_PRODUCT_PRICE_ROUTE("QUEUE_EP_PRODUCT_PRICE_ROUTE",2),
    QUEUE_PRODUCT_PRICE_EXCHANGE("QUEUE_EP_PRODUCT_PRICE_EXCHANGE",3),

    QUEUE_PRODUCT_SUPPLIER_PRICE_QUEUE("QUEUE_EP_PRODUCT_SUPPLIER_PRICE",4),
    QUEUE_PRODUCT_SUPPLIER_PRICE_ROUTE("QUEUE_EP_PRODUCT_SUPPLIER_PRICE_ROUTE",5),
    QUEUE_PRODUCT_SUPPLIER_PRICE_EXCHANGE("QUEUE_EP_PRODUCT_SUPPLIER_PRICE_EXCHANGE",6);

    /**
     * 消息队列名称
     */
    private String messageName;

    private Integer type;

    public String getMessageName() {
        return messageName;
    }

    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    private Queue(String messageName,Integer type) {
        this.messageName = messageName;
        this.type = type;
    }
}
