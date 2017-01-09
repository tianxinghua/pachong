package com.shangpin.pending.product.consumer.conf.stream.sink.listener;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import com.shangpin.ephub.client.message.pending.body.PendingProduct;
import com.shangpin.pending.product.consumer.conf.stream.sink.channel.PendingProductSink;
import com.shangpin.pending.product.consumer.supplier.adapter.PendingProductStreamListenerAdapter;

/**
 * <p>Title:StreamConf.java </p>
 * <p>Description: 待处理商品数据流监听器配置</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月9日 下午6:07:52
 */
@EnableBinding(PendingProductSink.class)
public class PendingProductStreamListener {
	
	@Autowired
	private PendingProductStreamListenerAdapter adapter;
	/**
	 * 供应商BIONDIONI待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.BIONDIONI)
    public void biondioniPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.biondioniPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商BRUNAROSSO待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.BRUNAROSSO)
    public void brunarossoPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.brunarossoPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商COLTORTI待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.COLTORTI)
    public void coltortiPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.coltortiPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商GEB待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.GEB)
    public void gebPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.gebPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商OSTORE待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.OSTORE)
    public void ostorePendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.ostorePendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商SPINNAKER待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.SPINNAKER)
    public void spinnakerPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.spinnakerPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商STEFANIA待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.STEFANIA)
    public void stefaniaPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.stefaniaPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商TONY待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.TONY)
    public void tonyPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.tonyPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商POZZILEI待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.POZZILEI)
    public void pozzileiPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.pozzileiPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商POZZILEIARTE待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.POZZILEIARTE)
    public void pozzileiartePendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.pozzileiartePendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商POZZILEIFORTE待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.POZZILEIFORTE)
    public void pozzileifortePendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.pozzileifortePendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商CAROFIGLIO待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.CAROFIGLIO)
    public void carofiglioPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.carofiglioPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商GENTEROMA待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.GENTEROMA)
    public void genteromaPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.genteromaPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商DANIELLO待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.DANIELLO)
    public void danielloPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.danielloPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商ITALIANI待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.ITALIANI)
    public void italianiPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.italianiPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商TUFANO待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.TUFANO)
    public void tufanoPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.tufanoPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商MONNIERFRERES待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.MONNIERFRERES)
    public void monnierfreresPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.monnierfreresPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商ELEONORABONUCCI待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.ELEONORABONUCCI)
    public void eleonorabonucciPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.eleonorabonucciPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商RUSSOCAPRI待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.RUSSOCAPRI)
    public void russocapriPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.russocapriPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商GIGLIO待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.GIGLIO)
    public void giglioPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.giglioPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商DIVO待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.DIVO)
    public void divoPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.divoPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商BIONDINI待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.BIONDINI)
    public void biondiniPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.biondiniPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商DELLOGLIOSTORE待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.DELLOGLIOSTORE)
    public void dellogliostorePendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.dellogliostorePendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商FRANCESCOMASSA待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.FRANCESCOMASSA)
    public void francescomassaPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.francescomassaPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商TESSABIT待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.TESSABIT)
    public void tessabitPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.tessabitPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商ALDUCADAOSTA待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.ALDUCADAOSTA)
    public void alducadaostaPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.alducadaostaPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商SANREMO待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.SANREMO)
    public void sanremoPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.sanremoPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商PAOLOFIORILLO待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.PAOLOFIORILLO)
    public void paolofiorilloPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.paolofiorilloPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商VIETTI待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.VIETTI)
    public void viettiPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.viettiPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商ANIELLO待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.ANIELLO)
    public void anielloPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.anielloPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商LINDELEPALAIS待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.LINDELEPALAIS)
    public void lindelepalaisPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.lindelepalaisPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商PRITELLI待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.PRITELLI)
    public void pritelliPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.pritelliPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商PICCADILLY待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.PICCADILLY)
    public void piccadillyPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.piccadillyPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商VELA待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.VELA)
    public void velaPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.velaPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商MONTI待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.MONTI)
    public void montiPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.montiPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商CREATIVE99待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.CREATIVE99)
    public void creative99PendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.creative99PendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商LEAM待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.LEAM)
    public void leamPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.leamPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商BAGHEERA待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.BAGHEERA)
    public void bagheeraPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.bagheeraPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商PAPINI待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.PAPINI)
    public void papiniPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.papiniPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商ZITAFABIANI待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.ZITAFABIANI)
    public void zitafabianiPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.zitafabianiPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商WISE待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.WISE)
    public void wisePendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.wisePendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商BASEBLU待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.BASEBLU)
    public void basebluPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.basebluPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商RAFFAELLONETWORK待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.RAFFAELLONETWORK)
    public void raffaellonetworkPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.raffaellonetworkPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商FRMODA待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.FRMODA)
    public void frmodaPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.frmodaPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商STUDIO69待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.STUDIO69)
    public void studio69PendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.studio69PendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商DELIBERTI待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.DELIBERTI)
    public void delibertiPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.delibertiPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商SMETS待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.SMETS)
    public void smetsPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.smetsPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商SARENZA待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.SARENZA)
    public void sarenzaPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.sarenzaPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商SOLEPLACENEW待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.SOLEPLACENEW)
    public void soleplacenewPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.soleplacenewPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商HONESTDOUSA待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.HONESTDOUSA)
    public void honestdousaPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.honestdousaPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商OPTICAL待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.OPTICAL)
    public void opticalPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.opticalPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商TIZIANAFAUSTI待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.TIZIANAFAUSTI)
    public void tizianafaustiPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.tizianafaustiPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商THECLUTCHER待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.THECLUTCHER)
    public void theclutcherPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.theclutcherPendingProductStreamListen(message,headers);
    }
}
