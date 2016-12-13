package com.shangpin.pending.product.consumer.conf.stream.sink.channel;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;
/**
 * <p>Title:PendingProductSource.java </p>
 * <p>Description: 待处理商品数据流通道组件配置</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月5日 下午7:34:44
 */
public interface PendingProductSink {
	
	public String SPINNAKER = "spinnakerPendingProduct";
	
	public String OSTORE = "ostorePendingProduct";
	
	public String BRUNAROSSO = "brunarossoPendingProduct";
	
	public String STEFANIA = "stefaniaPendingProduct";
	
	public String GEB = "gebPendingProduct";
	
	public String COLTORTI = "coltortiPendingProduct";
	
	public String TONY = "tonyPendingProduct";
	
	public String BIONDIONI = "biondioniPendingProduct";
	
	/**
	 * 供货商SPINNAKER通道组件配置
	 * @return 供货商SPINNAKER通道组件
	 */
	@Input(value = PendingProductSink.SPINNAKER)
    public SubscribableChannel spinnakerPendingProduct();
	/**
	 * 供货商OSTORE通道组件配置
	 * @return 供货商OSTORE通道组件
	 */
	@Input(value = PendingProductSink.OSTORE)
    public SubscribableChannel ostorePendingProduct();
	/**
	 * 供货商BRUNAROSSO通道组件配置
	 * @return 供货商BRUNAROSSO通道组件
	 */
	@Input(value = PendingProductSink.BRUNAROSSO)
    public SubscribableChannel brunarossoPendingProduct();
	/**
	 * 供货商STEFANIA通道组件配置
	 * @return 供货商STEFANIA通道组件
	 */
	@Input(value = PendingProductSink.STEFANIA)
    public SubscribableChannel stefaniaPendingProduct();
	/**
	 * 供货商GEB通道组件配置
	 * @return 供货商GEB通道组件
	 */
	@Input(value = PendingProductSink.GEB)
    public SubscribableChannel gebPendingProduct();
	/**
	 * 供货商COLTORTI通道组件配置
	 * @return 供货商COLTORTI通道组件
	 */
	@Input(value = PendingProductSink.COLTORTI)
    public SubscribableChannel coltortiPendingProduct();
	/**
	 * 供货商TONY通道组件配置
	 * @return 供货商TONY通道组件
	 */
	@Input(value = PendingProductSink.TONY)
    public SubscribableChannel tonyPendingProduct();
	/**
	 * 供货商BIONDIONI通道组件配置
	 * @return 供货商BIONDIONI通道组件
	 */
	@Input(value = PendingProductSink.BIONDIONI)
    public SubscribableChannel biondioniPendingProduct();
}
