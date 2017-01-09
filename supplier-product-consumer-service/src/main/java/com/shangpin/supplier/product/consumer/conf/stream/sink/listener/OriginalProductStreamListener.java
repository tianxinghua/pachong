package com.shangpin.supplier.product.consumer.conf.stream.sink.listener;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import com.shangpin.ephub.client.message.original.body.SupplierProduct;
import com.shangpin.supplier.product.consumer.conf.stream.sink.channel.OriginalProductSink;
import com.shangpin.supplier.product.consumer.supplier.adapter.OriginalProductStreamListenerAdapter;

/**
 * 订单流处理配置
 * <p>Title:StreamConf.java </p>
 * <p>Description: 订单流数据处理</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月9日 下午6:07:52
 */
@EnableBinding({OriginalProductSink.class})
public class OriginalProductStreamListener {
	
	@Autowired
	private OriginalProductStreamListenerAdapter adapter;
	/**
	 * 供应商BIONDIONI原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.BIONDIONI)
    public void biondioniStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) {
		adapter.biondioniStreamListen(message,headers);
    }
	/**
	 * 供应商BRUNAROSSO原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.BRUNAROSSO)
    public void brunarossoStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) {
		adapter.brunarossoStreamListen(message,headers);
    }
	/**
	 * 供应商COLTORTI原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.COLTORTI)
    public void coltortiStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) {
		adapter.coltortiStreamListen(message,headers);
    }
	/**
	 * 供应商GEB原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.GEB)
    public void gebStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) {
		adapter.gebStreamListen(message,headers);
    }
	/**
	 * 供应商OSTORE原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.OSTORE)
    public void ostoreStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) {
		adapter.ostoreStreamListen(message,headers);
    }
	/**
	 * 供应商SPINNAKER原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.SPINNAKER)
    public void spinnakerStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) {
		adapter.spinnakerStreamListen(message,headers);
    }
	/**
	 * 供应商STEFANIA原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.STEFANIA)
    public void stefaniaStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) {
		adapter.stefaniaStreamListen(message,headers);
    }
	/**
	 * 供应商TONY原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.TONY)
    public void tonyStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) {
		adapter.tonyStreamListen(message,headers);
    }
	/**
	 * 供应商POZZILEI原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.POZZILEI)
    public void pozzileiStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) {
		adapter.pozzileiStreamListen(message,headers);
    }
	/**
	 * 供应商POZZILEIARTE原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.POZZILEIARTE)
    public void pozzileiarteStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) {
		adapter.pozzileiarteStreamListen(message,headers);
    }
	/**
	 * 供应商POZZILEIFORTE原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.POZZILEIFORTE)
    public void pozzileiforteStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) {
		adapter.pozzileiforteStreamListen(message,headers);
    }
	/**
	 * 供应商CAROFIGLIO原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.CAROFIGLIO)
    public void carofiglioStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.carofiglioStreamListen(message,headers);
    }
	/**
	 * 供应商GENTEROMA原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.GENTEROMA)
    public void genteromaStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.genteromaStreamListen(message,headers);
    }
	/**
	 * 供应商DANIELLO原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.DANIELLO)
    public void danielloStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.danielloStreamListen(message,headers);
    }
	/**
	 * 供应商ITALIANI原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.ITALIANI)
    public void italianiStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.italianiStreamListen(message,headers);
    }
	/**
	 * 供应商TUFANO原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.TUFANO)
    public void tufanoStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.tufanoStreamListen(message,headers);
    }
	/**
	 * 供应商MONNIERFRERES原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.MONNIERFRERES)
    public void monnierfreresStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.monnierfreresStreamListen(message,headers);
    }
	/**
	 * 供应商ELEONORABONUCCI原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.ELEONORABONUCCI)
    public void eleonorabonucciStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.eleonorabonucciStreamListen(message,headers);
    }
	/**
	 * 供应商RUSSOCAPRI原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.RUSSOCAPRI)
    public void russocapriStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.russocapriStreamListen(message,headers);
    }
	/**
	 * 供应商GIGLIO原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.GIGLIO)
    public void giglioStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.giglioStreamListen(message,headers);
    }
	/**
	 * 供应商DIVO原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.DIVO)
    public void divoStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.divoStreamListen(message,headers);
    }
	/**
	 * 供应商BIONDINI原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.BIONDINI)
    public void biondiniStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.biondiniStreamListen(message,headers);
    }
	/**
	 * 供应商DELLOGLIOSTORE原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.DELLOGLIOSTORE)
    public void dellogliostoreStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.dellogliostoreStreamListen(message,headers);
    }
	/**
	 * 供应商FRANCESCOMASSA原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.FRANCESCOMASSA)
    public void francescomassaStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.francescomassaStreamListen(message,headers);
    }
	/**
	 * 供应商TESSABIT原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.TESSABIT)
    public void tessabitStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.tessabitStreamListen(message,headers);
    }
	/**
	 * 供应商ALDUCADAOSTA原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.ALDUCADAOSTA)
    public void alducadaostaStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.alducadaostaStreamListen(message,headers);
    }
	/**
	 * 供应商SANREMO原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.SANREMO)
    public void sanremoStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.sanremoStreamListen(message,headers);
    }
	/**
	 * 供应商PAOLOFIORILLO原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.PAOLOFIORILLO)
    public void paolofiorilloStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.paolofiorilloStreamListen(message,headers);
    }
	/**
	 * 供应商VIETTI原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.VIETTI)
    public void viettiStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.viettiStreamListen(message,headers);
    }
	/**
	 * 供应商ANIELLO原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.ANIELLO)
    public void anielloStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.anielloStreamListen(message,headers);
    }
	/**
	 * 供应商LINDELEPALAIS原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.LINDELEPALAIS)
    public void lindelepalaisStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.lindelepalaisStreamListen(message,headers);
    }
	/**
	 * 供应商PRITELLI原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.PRITELLI)
    public void pritelliStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.pritelliStreamListen(message,headers);
    }
	/**
	 * 供应商PICCADILLY原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.PICCADILLY)
    public void piccadillyStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.piccadillyStreamListen(message,headers);
    }
	/**
	 * 供应商VELA原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.VELA)
    public void velaStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.velaStreamListen(message,headers);
    }
	/**
	 * 供应商MONTI原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.MONTI)
    public void montiStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.montiStreamListen(message,headers);
    }
	/**
	 * 供应商CREATIVE99原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.CREATIVE99)
    public void creative99StreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.creative99StreamListen(message,headers);
    }
	/**
	 * 供应商LEAM原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.LEAM)
    public void leamStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.leamStreamListen(message,headers);
    }
	/**
	 * 供应商BAGHEERA原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.BAGHEERA)
    public void bagheeraStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.bagheeraStreamListen(message,headers);
    }
	/**
	 * 供应商PAPINI原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.PAPINI)
    public void papiniStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.papiniStreamListen(message,headers);
    }
	/**
	 * 供应商ZITAFABIANI原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.ZITAFABIANI)
    public void zitafabianiStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.zitafabianiStreamListen(message,headers);
    }
	/**
	 * 供应商WISE原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.WISE)
    public void wiseStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.wiseStreamListen(message,headers);
    }
	/**
	 * 供应商BASEBLU原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.BASEBLU)
    public void basebluStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.basebluStreamListen(message,headers);
    }
	/**
	 * 供应商RAFFAELLONETWORK原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.RAFFAELLONETWORK)
    public void raffaellonetworkStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.raffaellonetworkStreamListen(message,headers);
    }
	/**
	 * 供应商FRMODA原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.FRMODA)
    public void frmodaStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.frmodaStreamListen(message,headers);
    }
	/**
	 * 供应商STUDIO69原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.STUDIO69)
    public void studio69StreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.studio69StreamListen(message,headers);
    }
	/**
	 * 供应商DELIBERTI原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.DELIBERTI)
    public void delibertiStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.delibertiStreamListen(message,headers);
    }
	/**
	 * 供应商SMETS原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.SMETS)
    public void smetsStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.smetsStreamListen(message,headers);
    }
	/**
	 * 供应商SARENZA原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.SARENZA)
    public void sarenzaStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.sarenzaStreamListen(message,headers);
    }
	/**
	 * 供应商SOLEPLACENEW原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.SOLEPLACENEW)
    public void soleplacenewStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.soleplacenewStreamListen(message,headers);
    }
	/**
	 * 供应商HONESTDOUSA原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.HONESTDOUSA)
    public void honestdousaStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.honestdousaStreamListen(message,headers);
    }
	/**
	 * 供应商OPTICAL原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.OPTICAL)
    public void opticalStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.opticalStreamListen(message,headers);
    }
	/**
	 * 供应商TIZIANAFAUSTI原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.TIZIANAFAUSTI)
    public void tizianafaustiStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.tizianafaustiStreamListen(message,headers);
    }
	/**
	 * 供应商THECLUTCHER原始商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(OriginalProductSink.THECLUTCHER)
    public void theclutcherStreamListen(@Payload SupplierProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.theclutcherStreamListen(message,headers);
    }
	}
