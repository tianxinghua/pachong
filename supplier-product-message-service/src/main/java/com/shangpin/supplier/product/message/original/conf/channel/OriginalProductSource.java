package com.shangpin.supplier.product.message.original.conf.channel;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
/**
 * <p>Title:OriginalProductSource.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月5日 下午7:34:44
 */
public interface OriginalProductSource {
	
	public String SPINNAKER = "spinnakerOriginalProduct";
	
	public String OSTORE = "ostoreOriginalProduct";
	
	public String BRUNAROSSO = "brunarossoOriginalProduct";
	
	public String STEFANIA = "stefaniaOriginalProduct";
	
	public String GEB = "gebOriginalProduct";
	
	public String COLTORTI = "coltortiOriginalProduct";
	
	public String TONY = "tonyOriginalProduct";
	
	public String BIONDIONI = "biondioniOriginalProduct";
	
	/**
	 * 供货商SPINNAKER通道组件配置
	 * @return 供货商SPINNAKER通道组件
	 */
	@Output(value = OriginalProductSource.SPINNAKER)
    public MessageChannel spinnaker();
	/**
	 * 供货商OSTORE通道组件配置
	 * @return 供货商OSTORE通道组件
	 */
	@Output(value = OriginalProductSource.OSTORE)
    public MessageChannel ostore();
	/**
	 * 供货商BRUNAROSSO通道组件配置
	 * @return 供货商BRUNAROSSO通道组件
	 */
	@Output(value = OriginalProductSource.BRUNAROSSO)
    public MessageChannel brunarosso();
	/**
	 * 供货商STEFANIA通道组件配置
	 * @return 供货商STEFANIA通道组件
	 */
	@Output(value = OriginalProductSource.STEFANIA)
    public MessageChannel stefania();
	/**
	 * 供货商GEB通道组件配置
	 * @return 供货商GEB通道组件
	 */
	@Output(value = OriginalProductSource.GEB)
    public MessageChannel geb();
	/**
	 * 供货商COLTORTI通道组件配置
	 * @return 供货商COLTORTI通道组件
	 */
	@Output(value = OriginalProductSource.COLTORTI)
    public MessageChannel coltorti();
	/**
	 * 供货商TONY通道组件配置
	 * @return 供货商TONY通道组件
	 */
	@Output(value = OriginalProductSource.TONY)
    public MessageChannel tony();
	/**
	 * 供货商BIONDIONI通道组件配置
	 * @return 供货商BIONDIONI通道组件
	 */
	@Output(value = OriginalProductSource.BIONDIONI)
    public MessageChannel biondioni();
}
