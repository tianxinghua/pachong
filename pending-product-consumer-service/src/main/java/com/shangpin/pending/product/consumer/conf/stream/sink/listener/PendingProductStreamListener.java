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
	 * 供应商angeloMinetti待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.fashionTamers)
	public void fashionTamersPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.fashionTamersPendingProductStreamListen(message,headers);
	}
	/**
	 * 供应商angeloMinetti待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.angeloMinetti)
	public void angeloMinettiPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.angeloMinettiPendingProductStreamListen(message,headers);
	}
	/**
	 * 供应商julianFashion待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.cocoroseLondon)
	public void cocoroseLondonPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.cocoroseLondonPendingProductStreamListen(message,headers);
	}
	/**
	 * 供应商julianFashion待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.dolciTrame)
	public void dolciTramePendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.dolciTramePendingProductStreamListen(message,headers);
	}
	/**
	 * 供应商julianFashion待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.JULIANFASHION)
	public void julianFashionPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.julianFashionPendingProductStreamListen(message,headers);
	}
	
	/**
	 * 供应商coccolebimbi待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.coccolebimbi)
	public void coccolebimbiPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.coccolebimbiPendingProductStreamListen(message,headers);
	}
	/**
	 * 供应商fratinardi待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.fratinardi)
	public void fratinardiPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.fratinardiPendingProductStreamListen(message,headers);
	}
	/**
	 * 供应商portofino待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.portofino)
	public void portofinoPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.portofinoPendingProductStreamListen(message,headers);
	}
	/**
	 * 供应商FORZIERI待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.FORZIERI)
	public void forzieriPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.forzieriPendingProductStreamListen(message,headers);
	}
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
	@StreamListener(PendingProductSink.paloma)
    public void palomaPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.gebPendingProductStreamListen(message,headers);
    }
	/**
	 * 供应商GEB待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.lamborghini)
	public void lamborghiniPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.gebPendingProductStreamListen(message,headers);
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
	 * 供应商MENGOTTTSNC待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.MENGOTTISNC)
	public void mengottiSncPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.mengottiSncPendingProductStreamListen(message,headers);
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
     * 供应商VIETTI待处理商品数据流通道监听者
     * @param message 消息
     * @param headers 消息头
     */
    @StreamListener(PendingProductSink.VIETTI2)
    public void vietti2PendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
        adapter.vietti2PendingProductStreamListen(message,headers);
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
	 * 供应商PARISI待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.PARISI)
    public void parisiPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.parisiPendingProductStreamListen(message,headers);
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
	/**
	 * 供应商antonacci待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.ANTONACCI)
	public void antonacciPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.antonacciPendingProductStreamListen(message,headers);
	}
	/**
	 * 供应商LUNGOLIVIGNO待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 * @throws Exception
	 */
	@StreamListener(PendingProductSink.LUNGOLIVIGNO)
	public void lungolivignoPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.lungolivignoPendingProductStreamListen(message, headers); 
	}
	/**
	 * 供应商FILIPPO待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 * @throws Exception
	 */
	@StreamListener(PendingProductSink.FILIPPO)
	public void filippoPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception {
		adapter.filippoPendingProductStreamListen(message,headers);
	}
	/**
	 * 供应商DELLAMARTIRA待处理商品数据流通道监听者
	 * @param message
	 * @param headers
	 * @throws Exception
	 */
	@StreamListener(PendingProductSink.DELLAMARTIRA)
	public void dellaMartiraPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception {
		adapter.dellaMartiraPendingProductStreamListen(message,headers);
	}
	/**
	 * 供应商ROSISERLI待处理商品数据流通道监听者
	 * @param message
	 * @param headers
	 * @throws Exception
	 */
	@StreamListener(PendingProductSink.ROSISERLI)
	public void rosiSerliPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception {
		adapter.rosiSerliPendingProductStreamListen(message,headers);
	}
	/**
	 * 供应商MCLABLES待处理商品数据流通道监听者
	 * @param message
	 * @param headers
	 * @throws Exception
	 */
	@StreamListener(PendingProductSink.MCLABLES)
	public void mclablesPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception {
		adapter.mclablesPendingProductStreamListen(message,headers);
	}
	/**
	 * 供应商EMONTI待处理商品数据流通道监听者
	 * @param message
	 * @param headers
	 * @throws Exception
	 */
	@StreamListener(PendingProductSink.EMONTI)
	public void emontiPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception {
		adapter.emontiPendingProductStreamListen(message,headers);
	}
	/**
	 * 供应商DLRBOUTIQUE待处理商品数据流通道监听者
	 * @param message
	 * @param headers
	 * @throws Exception
	 */
	@StreamListener(PendingProductSink.DLRBOUTIQUE)
	public void dlrboutiquePendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception {
		adapter.dlrboutiquePendingProductStreamListen(message,headers);
	}

	/**
	 * 供应商reebonz待处理商品数据流通道监听者
	 * @param message
	 * @param headers
	 * @throws Exception
	 */
	@StreamListener(PendingProductSink.REEBONZ)
	public void reebonzPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception {
		adapter.reebonzPendingProductStreamListen(message,headers);
	}
	
	/**
	 * 供应商GAUDENZI待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.GAUDENZI)
    public void gaudenziPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.gaudenziPendingProductStreamListen(message,headers);
    }
	
	/**
	 * 供应商GAUDENZI待处理商品数据流通道监听者
	 * @param message 消息
	 * @param headers 消息头
	 */
	@StreamListener(PendingProductSink.MONNALISA)
    public void monnalisaPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.monnalisaPendingProductStreamListen(message,headers);
    }
	
	@StreamListener(PendingProductSink.STAR)
    public void starPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.starPendingProductStreamListen(message,headers);
    }

	/**
	 * 供应商marino待处理商品数据流通道监听者
	 * @param message
	 * @param headers
	 * @throws Exception
	 */
	@StreamListener(PendingProductSink.MARINO)
	public void marinoPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.marinoPendingProductStreamListen(message,headers);
	}
	
	@StreamListener(PendingProductSink.THESTYLESIDE)
    public void theStyleSidePendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.theStyleSidePendingProductStreamListen(message,headers);
    }


	@StreamListener(PendingProductSink.REDI)
	public void rediPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.rediPendingProductStreamListen(message,headers);
	}

	@StreamListener(PendingProductSink.OBLU)
	public void obluPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.obluPendingProductStreamListen(message,headers);
	}

	@StreamListener(PendingProductSink.ZHICAI)
	public void zhicaiPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.zhicaiPendingProductStreamListen(message,headers);
	}

	@StreamListener(PendingProductSink.YLATI)
	public void ylatiPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.ylatiPendingProductStreamListen(message,headers);
	}

	@StreamListener(PendingProductSink.MAX1980)
	public void max1980PendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.max1980PendingProductStreamListen(message,headers);
	}
	/**
	* 供应商gebnegozio待处理商品数据流通道监听者
	 * @param message
	 * @param headers
	 * @throws Exception
	 */
	@StreamListener(PendingProductSink.GEBNEGOZIO)
	public void gebnegozioPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.gebnegozioPendingProductStreamListen(message,headers);
	}

	/**
	 * 供应商ilcucciolo待处理商品数据流通道监听者
	 * @param message
	 * @param headers
	 * @throws Exception
	 */
	@StreamListener(PendingProductSink.ILCUCCIOLO)
	public void ilcuccioloPendingProductStreamListen(@Payload PendingProduct message, @Headers Map<String,Object> headers) throws Exception  {
		adapter.ilcuccioloPendingProductStreamListen(message,headers);
	}

}
