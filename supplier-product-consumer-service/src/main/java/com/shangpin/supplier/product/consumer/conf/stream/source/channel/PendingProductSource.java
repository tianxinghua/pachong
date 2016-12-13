package com.shangpin.supplier.product.consumer.conf.stream.source.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
/**
 * <p>Title:PendingProductSource.java </p>
 * <p>Description: 待处理商品数据流通道组件配置</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月5日 下午7:34:44
 */
public interface PendingProductSource {
	
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
	@Output(value = PendingProductSource.SPINNAKER)
    public MessageChannel spinnakerPendingProduct();
	/**
	 * 供货商OSTORE通道组件配置
	 * @return 供货商OSTORE通道组件
	 */
	@Output(value = PendingProductSource.OSTORE)
    public MessageChannel ostorePendingProduct();
	/**
	 * 供货商BRUNAROSSO通道组件配置
	 * @return 供货商BRUNAROSSO通道组件
	 */
	@Output(value = PendingProductSource.BRUNAROSSO)
    public MessageChannel brunarossoPendingProduct();
	/**
	 * 供货商STEFANIA通道组件配置
	 * @return 供货商STEFANIA通道组件
	 */
	@Output(value = PendingProductSource.STEFANIA)
    public MessageChannel stefaniaPendingProduct();
	/**
	 * 供货商GEB通道组件配置
	 * @return 供货商GEB通道组件
	 */
	@Output(value = PendingProductSource.GEB)
    public MessageChannel gebPendingProduct();
	/**
	 * 供货商COLTORTI通道组件配置
	 * @return 供货商COLTORTI通道组件
	 */
	@Output(value = PendingProductSource.COLTORTI)
    public MessageChannel coltortiPendingProduct();
	/**
	 * 供货商TONY通道组件配置
	 * @return 供货商TONY通道组件
	 */
	@Output(value = PendingProductSource.TONY)
    public MessageChannel tonyPendingProduct();
	/**
	 * 供货商BIONDIONI通道组件配置
	 * @return 供货商BIONDIONI通道组件
	 */
	@Output(value = PendingProductSource.BIONDIONI)
    public MessageChannel biondioniPendingProduct();
}
