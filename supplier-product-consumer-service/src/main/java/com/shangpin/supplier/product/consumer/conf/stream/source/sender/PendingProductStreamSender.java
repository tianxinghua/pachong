package com.shangpin.supplier.product.consumer.conf.stream.source.sender;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.shangpin.ephub.client.message.pending.body.PendingProduct;
import com.shangpin.supplier.product.consumer.conf.stream.source.channel.PendingProductSource;
/**
 * <p>Title:OrderStreamSender.java </p>
 * <p>Description: 待处理商品数据流发送器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月23日 下午3:28:09
 */
@EnableBinding({PendingProductSource.class})
public class PendingProductStreamSender {
	
	@Autowired
	private PendingProductSource pendingProductSource;
	
	/**
	 * 发送供应商fashionTamers商品流数据
	 * @param pendingProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
	public boolean fashionTamersPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
		return pendingProductSource.fashionTamersPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
	}
	/**
	 * 发送供应商angeloMinetti商品流数据
	 * @param pendingProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
	public boolean angeloMinettiPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
		return pendingProductSource.angeloMinettiPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
	}
	/**
	 * 发送供应商cocoroseLondon商品流数据
	 * @param pendingProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
	public boolean cocoroseLondonPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
		return pendingProductSource.cocoroseLondonPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
	}
	/**
	 * 发送供应商dolciTrame商品流数据
	 * @param pendingProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
	public boolean dolciTramePendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
		return pendingProductSource.dolciTramePendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
	}
	/**
	 * 发送供应商julianFashion商品流数据
	 * @param pendingProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
	public boolean julianFashionPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
		return pendingProductSource.julianFashionPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
	}
	
	/**
	 * 发送供应商portofino商品流数据
	 * @param pendingProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
	public boolean portofinoPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
		return pendingProductSource.portofinoPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
	}
	/**
	 * 发送供应商coccolebimbi商品流数据
	 * @param pendingProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
	public boolean coccolebimbiPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
		return pendingProductSource.coccolebimbiPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
	}
	/**
	 * 发送供应商fratinardi商品流数据
	 * @param pendingProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
	public boolean fratinardiPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
		return pendingProductSource.fratinardiPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
	}
	/**
	 * 发送供应商forzieri商品流数据
	 * @param pendingProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
	public boolean forzieriPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
		return pendingProductSource.forzieriPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
	}
	/**
	 * 发送供应商biondioni商品流数据
	 * @param pendingProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean biondioniPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.biondioniPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
	/**
	 * 发送供应商brunarosso商品流数据
	 * @param pendingProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean brunarossoPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.brunarossoPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
	/**
	 * 发送供应商coltorti商品流数据
	 * @param pendingProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean coltortiPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.coltortiPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
	/**
	 * 发送供应商ostore商品流数据
	 * @param pendingProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean ostorePendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.ostorePendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
	/**
	 * 发送供应商MENGOTTISNC商品流数据
	 * @param pendingProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
	public boolean mengottiSncPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
		return pendingProductSource.mengottiSncPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
	}

	/**
	 * 发送供应商spinnaker商品流数据
	 * @param pendingProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean spinnakerPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.spinnakerPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
	/**
	 * 发送供应商stefania商品流数据
	 * @param pendingProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean stefaniaPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.stefaniaPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
	/**
	 * 发送供应商tony商品流数据
	 * @param pendingProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean tonyPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.tonyPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商geb商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean gebPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.gebPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean pozzileiPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.pozzileiPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean pozzileiartePendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.pozzileiartePendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean pozzileifortePendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.pozzileifortePendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean carofiglioPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.carofiglioPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean genteromaPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.genteromaPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean danielloPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.danielloPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean italianiPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.italianiPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean tufanoPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.tufanoPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean monnierfreresPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.monnierfreresPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean eleonorabonucciPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.eleonorabonucciPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean russocapriPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.russocapriPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean giglioPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.giglioPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean divoPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.divoPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean biondiniPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.biondiniPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean dellogliostorePendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.dellogliostorePendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean francescomassaPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.francescomassaPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean tessabitPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.tessabitPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean alducadaostaPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.alducadaostaPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean sanremoPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.sanremoPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean paolofiorilloPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.paolofiorilloPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean viettiPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.viettiPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }

    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean vietti2PendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
        return pendingProductSource.vietti2PendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean anielloPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.anielloPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean lindelepalaisPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.lindelepalaisPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean pritelliPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.pritelliPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean piccadillyPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.piccadillyPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean velaPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.velaPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean montiPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.montiPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean creative99PendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.creative99PendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean leamPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.leamPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean bagheeraPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.bagheeraPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean papiniPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.papiniPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean zitafabianiPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.zitafabianiPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean wisePendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.wisePendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean basebluPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.basebluPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean raffaellonetworkPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.raffaellonetworkPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean frmodaPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.frmodaPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean studio69PendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.studio69PendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean delibertiPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.delibertiPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean parisiPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.parisiPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean smetsPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.smetsPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean sarenzaPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.sarenzaPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean soleplacenewPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.soleplacenewPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean honestdousaPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.honestdousaPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean opticalPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.opticalPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean tizianafaustiPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.tizianafaustiPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean theclutcherPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.theclutcherPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean antonacciPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers) {
    	return pendingProductSource.antonacciPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @param headers 消息头
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean lungolivignoPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers){
    	return pendingProductSource.lungolivignoPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct
     * @param headers
     * @return
     */
    public boolean filippoPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers){
    	return pendingProductSource.filippoPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @param headers 消息头
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean dellaMartiraPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers){
    	return pendingProductSource.dellaMartiraPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @param headers 消息头
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean rosiSerliPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers){
    	return pendingProductSource.rosiSerliPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @param headers 消息头
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean mclablesPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers){
    	return pendingProductSource.mclablesPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @param headers 消息头
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean emontiPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers){
    	return pendingProductSource.emontiPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @param headers 消息头
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean dlrboutiquePendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers){
    	return pendingProductSource.dlrboutiquePendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @param headers 消息头
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean reebonzPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers){
    	return pendingProductSource.reebonzPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @param headers 消息头
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean gaudenziPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers){
    	return pendingProductSource.gaudenziPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @param headers 消息头
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean monnalisaPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers){
    	return pendingProductSource.monnalisaPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
    
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @param headers 消息头
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean starPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers){
    	return pendingProductSource.starPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }


	/**
	 * 发送供应商商品流数据
	 * @param pendingProduct 消息体
	 * @param headers 消息头
	 * @return 如果发送成功返回true,否则返回false
	 */
	public boolean marinoPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers){
		return pendingProductSource.marinoPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
	}
    
    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @param headers 消息头
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean theStyleSidePendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers){
    	return pendingProductSource.theStyleSidePendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }

	/**
	 * 发送供应商商品流数据
	 * @param pendingProduct 消息体
	 * @param headers 消息头
	 * @return 如果发送成功返回true,否则返回false
	 */
	public boolean rediPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers){
		return pendingProductSource.rediPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
	}

	/**
	 * 发送供应商商品流数据
	 * @param pendingProduct 消息体
	 * @param headers 消息头
	 * @return 如果发送成功返回true,否则返回false
	 */
	public boolean obluPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers){
		return pendingProductSource.obluPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
	}
	
	/**
	 * 发送供应商商品流数据
	 * @param pendingProduct 消息体
	 * @param headers 消息头
	 * @return 如果发送成功返回true,否则返回false
	 */
	public boolean zhicaiPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers){
		return pendingProductSource.zhicaiPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
	}

	public boolean ylatiPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers){
		return pendingProductSource.ylatiPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
	}
	/**
	 * 发送供应商商品流数据
	 * @param pendingProduct 消息体
	 * @param headers 消息头
	 * @return 如果发送成功返回true,否则返回false
	 */
	public boolean max1980PendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers){
		return pendingProductSource.max1980PendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
	}

	/**
	 * 发送供应商商品流数据
	 * @param pendingProduct 消息体
	 * @param headers 消息头
	 * @return 如果发送成功返回true,否则返回false
	 */
	public boolean gebnegozioPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers){
		return pendingProductSource.gebnegozioPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());

	}

	/**
	 * 发送供应商商品流数据
	 * @param pendingProduct 消息体
	 * @param headers 消息头
	 * @return 如果发送成功返回true,否则返回false
	 */
	public boolean vipgroupPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers){
		return pendingProductSource.vipgroupPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());

	}

    /**
     * 发送供应商商品流数据
     * @param pendingProduct 消息体
     * @param headers 消息头
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean ilcuccioloPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers){
        return pendingProductSource.ilcuccioloPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());

    }

	/**
	 * 发送供应商eraldo商品流数据
	 * @param pendingProduct 消息体
	 * @param headers 消息头
	 * @return 如果发送成功返回true,否则返回false
	 */
	public boolean eraldoPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers){
		return pendingProductSource.eraldoPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());

	}

	/**
	 * 发送供应商tricot商品流数据
	 * @param pendingProduct 消息体
	 * @param headers 消息头
	 * @return 如果发送成功返回true,否则返回false
	 */
	public boolean tricotPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers){
		return pendingProductSource.tricotPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());

	}



    public boolean suitnegoziPendingProductStream(PendingProduct pendingProduct, Map<String, ?> headers){
        return pendingProductSource.suitnegoziPendingProduct().send(MessageBuilder.withPayload(pendingProduct).copyHeaders(headers).build());
    }
}
