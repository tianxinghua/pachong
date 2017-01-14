package com.shangpin.supplier.product.consumer.supplier.adapter;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.shangpin.ephub.client.message.original.body.SupplierProduct;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;

/**
 * <p>Title:OriginalProductStreamListenerAdapter.java </p>
 * <p>Description: 消息流适配器,此适配器后期将会针对具体的供应商做相应处理，这就是分开处理的原因，后期还有很大的扩张性！</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月8日 上午11:29:45
 */
@Component
public class OriginalProductStreamListenerAdapter {
	
	@Autowired
	@Qualifier("stefaniaHandler")
	private ISupplierHandler stefaniaHandler;	
	@Autowired
	@Qualifier("ostoreHandler")
	private ISupplierHandler ostoreHandler;
	@Autowired
	@Qualifier("brunarossoHandler")
	private ISupplierHandler brunarossoHandler;
	@Autowired
	@Qualifier("spinnakerHandler")
	private ISupplierHandler spinnakerHandler;	
	@Autowired
	@Qualifier("gebHandler")
	private ISupplierHandler gebHandler;
	@Autowired
	@Qualifier("biondioniHandler")
	private ISupplierHandler biondioniHandler;
	@Autowired
	@Qualifier("tonyHandler")
	private ISupplierHandler tonyHandler;
	@Autowired
	@Qualifier("coltortiHandler")
	private ISupplierHandler coltortiHandler;
	
	/**
	 * biondioni供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void biondioniStreamListen(SupplierProduct message, Map<String, Object> headers) {
		biondioniHandler.handleOriginalProduct(message, headers); 
	}
	/**
	 * brunarosso供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void brunarossoStreamListen(SupplierProduct message, Map<String, Object> headers) {
		brunarossoHandler.handleOriginalProduct(message, headers);		
	}
	/**
	 * coltorti供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void coltortiStreamListen(SupplierProduct message, Map<String, Object> headers) {
		coltortiHandler.handleOriginalProduct(message, headers); 
	}
	/**
	 * geb供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void gebStreamListen(SupplierProduct message, Map<String, Object> headers) {
		gebHandler.handleOriginalProduct(message, headers); 
		
	}
	/**
	 * ostore供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void ostoreStreamListen(SupplierProduct message, Map<String, Object> headers) {
		ostoreHandler.handleOriginalProduct(message, headers);		
	}
	/**
	 * spinnaker供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void spinnakerStreamListen(SupplierProduct message, Map<String, Object> headers) {
		spinnakerHandler.handleOriginalProduct(message, headers); 
	}
	/**
	 * stefania供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void stefaniaStreamListen(SupplierProduct message, Map<String, Object> headers) {
		stefaniaHandler.handleOriginalProduct(message,headers);
	}
	/**
	 * tony供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void tonyStreamListen(SupplierProduct message, Map<String, Object> headers) {
		tonyHandler.handleOriginalProduct(message, headers); 
	}
	/**
	 * pozzilei供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void pozzileiStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * pozzileiarte供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void pozzileiarteStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * pozzileiforte供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void pozzileiforteStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * carofiglio供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void carofiglioStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * genteroma供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void genteromaStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * daniello供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void danielloStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * italiani供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void italianiStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * tufano供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void tufanoStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * monnierfreres供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void monnierfreresStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * eleonorabonucci供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void eleonorabonucciStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * russocapri供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void russocapriStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * giglio供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void giglioStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * divo供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void divoStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * biondini供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void biondiniStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * dellogliostore供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void dellogliostoreStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * francescomassa供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void francescomassaStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * tessabit供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void tessabitStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * alducadaosta供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void alducadaostaStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * sanremo供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void sanremoStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * paolofiorillo供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void paolofiorilloStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * vietti供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void viettiStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * aniello供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void anielloStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * lindelepalais供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void lindelepalaisStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * pritelli供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void pritelliStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * piccadilly供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void piccadillyStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * vela供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void velaStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * monti供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void montiStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * creative99供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void creative99StreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * leam供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void leamStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * bagheera供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void bagheeraStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * papini供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void papiniStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * zitafabiani供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void zitafabianiStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * wise供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void wiseStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * baseblu供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void basebluStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * raffaellonetwork供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void raffaellonetworkStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * frmoda供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void frmodaStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * studio69供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void studio69StreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * deliberti供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void delibertiStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * smets供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void smetsStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * sarenza供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void sarenzaStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * soleplacenew供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void soleplacenewStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * honestdousa供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void honestdousaStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * optical供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void opticalStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * tizianafausti供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void tizianafaustiStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * theclutcher供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void theclutcherStreamListen(SupplierProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
}
