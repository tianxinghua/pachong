package com.shangpin.supplier.product.consumer.conf.stream.sink.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;
/**
 * <p>Title:OriginalProductSink.java </p>
 * <p>Description:  供货商原始商品数据流汇聚点配置接口</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月6日 下午4:06:53
 */
public interface OriginalProductSink {
	
	public static final String SPINNAKER = "spinnakerOriginalProduct";
	
	public static final String OSTORE = "ostoreOriginalProduct";
	
	public static final String BRUNAROSSO = "brunarossoOriginalProduct";
	
	public static final String STEFANIA = "stefaniaOriginalProduct";
	
	public static final String GEB = "gebOriginalProduct";
	
	public static final String COLTORTI = "coltortiOriginalProduct";
	
	public static final String TONY = "tonyOriginalProduct";
	
	public static final String BIONDIONI = "biondioniOriginalProduct";
	
	/**
	 * 供货商SPINNAKER通道组件配置
	 * @return SPINNAKER通道组件
	 */
	@Input(value = OriginalProductSink.SPINNAKER)
    public SubscribableChannel spinnakerOriginalProduct();
	/**
	 * 供货商OSTORE通道组件配置
	 * @return OSTORE通道组件
	 */
	@Input(value = OriginalProductSink.OSTORE)
    public SubscribableChannel ostoreOriginalProduct();
	/**
	 * 供货商BRUNAROSSO通道组件配置
	 * @return BRUNAROSSO通道组件
	 */
	@Input(value = OriginalProductSink.BRUNAROSSO)
    public SubscribableChannel brunarossoOriginalProduct();
	/**
	 * 供货商STEFANIA通道组件配置
	 * @return STEFANIA通道组件
	 */
	@Input(value = OriginalProductSink.STEFANIA)
    public SubscribableChannel stefaniaOriginalProduct();
	/**
	 * 供货商GEB通道组件配置
	 * @return GEB通道组件
	 */
	@Input(value = OriginalProductSink.GEB)
    public SubscribableChannel gebOriginalProduct();
	/**
	 * 供货商COLTORTI通道组件配置
	 * @return COLTORTI通道组件
	 */
	@Input(value = OriginalProductSink.COLTORTI)
    public SubscribableChannel coltortiOriginalProduct();
	/**
	 * 供货商TONY通道组件配置
	 * @return TONY通道组件
	 */
	@Input(value = OriginalProductSink.TONY)
    public SubscribableChannel tonyOriginalProduct();
	/**
	 * 供货商BIONDIONI通道组件配置
	 * @return BIONDIONI通道组件
	 */
	@Input(value = OriginalProductSink.BIONDIONI)
    public SubscribableChannel biondioniOriginalProduct();
}
