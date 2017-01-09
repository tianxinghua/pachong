package com.shangpin.pending.product.consumer.supplier.adapter;

import java.util.Map;

import com.shangpin.pending.product.consumer.supplier.common.PendingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ephub.client.message.pending.body.PendingProduct;

/**
 * <p>Title:PendingProductStreamListenerAdapter.java </p>
 * <p>Description: 待处理商品数据流监听适配器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年12月12日 下午4:30:09
 */
@Component
@Slf4j
public class PendingProductStreamListenerAdapter {

	@Autowired
	PendingHandler pendingHandler;


	private void messageHandle(PendingProduct message, Map<String, Object> headers){
		try {
			pendingHandler.receiveMsg(message,headers);
		} catch (Exception e) {
			log.error(" exception message = message boday : "  + message.toString()
					+ "  message header :" + headers.toString() + " exception reason :" + e.getMessage(),e);
			e.printStackTrace();
		}
	}

	/**
	 * 供应商biondioni待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void biondioniPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		this.messageHandle(message, headers);

	}
	/**
	 * 供应商ostore待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void ostorePendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		this.messageHandle(message, headers);
		
	}
	/**
	 * 供应商spinnaker待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void spinnakerPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		this.messageHandle(message, headers);
		
	}
	/**
	 * 供应商stefania待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void stefaniaPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		this.messageHandle(message, headers);
		
	}
	/**
	 * 供应商tony待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void tonyPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		this.messageHandle(message, headers);
		
	}
	/**
	 * 供应商geb待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void gebPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		this.messageHandle(message, headers);
		
	}
	/**
	 * 供应商coltorti待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void coltortiPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		this.messageHandle(message, headers);
		
	}
	/**
	 * 供应商brunarosso待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void brunarossoPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		this.messageHandle(message, headers);
		
	}
	/**
	 * 供应商pozzilei待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void pozzileiPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商pozzileiarte待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void pozzileiartePendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商pozzileiforte待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void pozzileifortePendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商carofiglio待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void carofiglioPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商genteroma待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void genteromaPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商daniello待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void danielloPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商italiani待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void italianiPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商tufano待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void tufanoPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商monnierfreres待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void monnierfreresPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商eleonorabonucci待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void eleonorabonucciPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商russocapri待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void russocapriPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商giglio待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void giglioPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商divo待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void divoPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商biondini待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void biondiniPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商dellogliostore待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void dellogliostorePendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商francescomassa待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void francescomassaPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商tessabit待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void tessabitPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商alducadaosta待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void alducadaostaPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商sanremo待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void sanremoPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商paolofiorillo待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void paolofiorilloPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商vietti待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void viettiPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商aniello待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void anielloPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商lindelepalais待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void lindelepalaisPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商pritelli待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void pritelliPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商piccadilly待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void piccadillyPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商vela待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void velaPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商monti待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void montiPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商creative99待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void creative99PendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商leam待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void leamPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商bagheera待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void bagheeraPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商papini待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void papiniPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商zitafabiani待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void zitafabianiPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商wise待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void wisePendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商baseblu待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void basebluPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商raffaellonetwork待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void raffaellonetworkPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商frmoda待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void frmodaPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商studio69待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void studio69PendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商deliberti待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void delibertiPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商smets待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void smetsPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商sarenza待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void sarenzaPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商soleplacenew待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void soleplacenewPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商honestdousa待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void honestdousaPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商optical待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void opticalPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商tizianafausti待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void tizianafaustiPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}
	/**
	 * 供应商theclutcher待处理商品数据流监听
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void theclutcherPendingProductStreamListen(PendingProduct message, Map<String, Object> headers) {
		// TODO Auto-generated method stub
		
	}

}
