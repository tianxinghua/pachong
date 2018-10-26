package com.shangpin.supplier.product.message.original.conf.sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.shangpin.ephub.client.message.original.body.SupplierProduct;
import com.shangpin.ephub.client.message.original.body.SupplierStock;
import com.shangpin.supplier.product.message.original.conf.channel.OriginalProductSource;
/**
 * <p>Title:OrderStreamSender.java </p>
 * <p>Description: 订单流发送器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月23日 下午3:28:09
 */
@EnableBinding({OriginalProductSource.class})
public class OriginalProductStreamSender {
	
	@Autowired
	private OriginalProductSource originalProductSource;


	/**
	 * 发送供应商fashionTamers商品流数据
	 * @param supplierProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
	public boolean fashionTamersStream(SupplierProduct supplierProduct) {
		return originalProductSource.fashionTamers().send(MessageBuilder.withPayload(supplierProduct).build());
	}

	/**
	 * 发送供应商angeloMinetti商品流数据
	 * @param supplierProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
	public boolean angeloMinettiStream(SupplierProduct supplierProduct) {
		return originalProductSource.angeloMinetti().send(MessageBuilder.withPayload(supplierProduct).build());
	}
	/**
	 * 发送供应商cocoroseLondon商品流数据
	 * @param supplierProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
	public boolean cocoroseLondonStream(SupplierProduct supplierProduct) {
		return originalProductSource.cocoroseLondon().send(MessageBuilder.withPayload(supplierProduct).build());
	}
	/**
	 * 发送供应商dolciTrame商品流数据
	 * @param supplierProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
	public boolean dolciTrameStream(SupplierProduct supplierProduct) {
		return originalProductSource.dolciTrame().send(MessageBuilder.withPayload(supplierProduct).build());
	}
	/**
	 * 发送供应商julianFashion商品流数据
	 * @param supplierProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
	public boolean julianFashionStream(SupplierProduct supplierProduct) {
		return originalProductSource.julianFashion().send(MessageBuilder.withPayload(supplierProduct).build());
	}
	
	/**
	 * 发送供应商coccolebimbi商品流数据
	 * @param supplierProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
	public boolean coccolebimbiStream(SupplierProduct supplierProduct) {
		return originalProductSource.coccolebimbi().send(MessageBuilder.withPayload(supplierProduct).build());
	}
	/**
	 * 发送供应商portofino商品流数据
	 * @param supplierProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
	public boolean portofinoStream(SupplierProduct supplierProduct) {
		return originalProductSource.portofino().send(MessageBuilder.withPayload(supplierProduct).build());
	}
	/**
	 * 发送供应商biondioni商品流数据
	 * @param supplierProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean biondioniStream(SupplierProduct supplierProduct) {
    	return originalProductSource.biondioni().send(MessageBuilder.withPayload(supplierProduct).build());
    }
	/**
	 * 发送供应商brunarosso商品流数据
	 * @param supplierProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean brunarossoStream(SupplierProduct supplierProduct) {
    	return originalProductSource.brunarosso().send(MessageBuilder.withPayload(supplierProduct).build());
    }
	/**
	 * 发送供应商coltorti商品流数据
	 * @param supplierProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean coltortiStream(SupplierProduct supplierProduct) {
    	return originalProductSource.coltorti().send(MessageBuilder.withPayload(supplierProduct).build());
    }
	/**
	 * 发送供应商ostore商品流数据
	 * @param supplierProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean ostoreStream(SupplierProduct supplierProduct) {
    	return originalProductSource.ostore().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商fratinardi商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean fratinardiStream(SupplierProduct supplierProduct) {
    	return originalProductSource.fratinardi().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商forzieri商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean forzieriStream(SupplierProduct supplierProduct) {
    	return originalProductSource.forzieri().send(MessageBuilder.withPayload(supplierProduct).build());
    }
	/**
	 * 发送供应商spinnaker商品流数据
	 * @param supplierProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean spinnakerStream(SupplierProduct supplierProduct) {
    	return originalProductSource.spinnaker().send(MessageBuilder.withPayload(supplierProduct).build());
    }
	/**
	 * 发送供应商stefania商品流数据
	 * @param supplierProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean stefaniaStream(SupplierProduct supplierProduct) {
    	return originalProductSource.stefania().send(MessageBuilder.withPayload(supplierProduct).build());
    }
	/**
	 * 发送供应商tony商品流数据
	 * @param supplierProduct 消息体
	 * @return 如果发送成功返回true,否则返回false
	 */
    public boolean tonyStream(SupplierProduct supplierProduct) {
    	return originalProductSource.tony().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商geb商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean gebStream(SupplierProduct supplierProduct) {
    	return originalProductSource.geb().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商paloma商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean palomaStream(SupplierProduct supplierProduct) {
    	return originalProductSource.paloma().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商lamborghini商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean lamborghiniStream(SupplierProduct supplierProduct) {
    	return originalProductSource.lamborghini().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商pozzilei商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean pozzileiStream(SupplierProduct supplierProduct) {
    	return originalProductSource.pozzilei().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商pozzileiarte商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean pozzileiarteStream(SupplierProduct supplierProduct) {
    	return originalProductSource.pozzileiarte().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商pozzileiforte商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean pozzileiforteStream(SupplierProduct supplierProduct) {
    	return originalProductSource.pozzileiforte().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商carofiglio商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean carofiglioStream(SupplierProduct supplierProduct) {
    	return originalProductSource.carofiglio().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商genteroma商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean genteromaStream(SupplierProduct supplierProduct) {
    	return originalProductSource.genteroma().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商daniello商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean danielloStream(SupplierProduct supplierProduct) {
    	return originalProductSource.daniello().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商italiani商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean italianiStream(SupplierProduct supplierProduct) {
    	return originalProductSource.italiani().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商tufano商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean tufanoStream(SupplierProduct supplierProduct) {
    	return originalProductSource.tufano().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商monnierfreres商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean monnierfreresStream(SupplierProduct supplierProduct) {
    	return originalProductSource.monnierfreres().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商eleonorabonucci商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean eleonorabonucciStream(SupplierProduct supplierProduct) {
    	return originalProductSource.eleonorabonucci().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商russocapri商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean russocapriStream(SupplierProduct supplierProduct) {
    	return originalProductSource.russocapri().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商giglio商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean giglioStream(SupplierProduct supplierProduct) {
    	return originalProductSource.giglio().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商divo商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean divoStream(SupplierProduct supplierProduct) {
    	return originalProductSource.divo().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商mengottiSnc商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean mengottiSncStream(SupplierProduct supplierProduct) {
        return originalProductSource.mengottiSnc().send(MessageBuilder.withPayload(supplierProduct).build());
    }

    /**
     * 发送供应商biondini商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean biondiniStream(SupplierProduct supplierProduct) {
    	return originalProductSource.biondini().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商dellogliostore商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean dellogliostoreStream(SupplierProduct supplierProduct) {
    	return originalProductSource.dellogliostore().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商francescomassa商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean francescomassaStream(SupplierProduct supplierProduct) {
    	return originalProductSource.francescomassa().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商tessabit商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean tessabitStream(SupplierProduct supplierProduct) {
    	return originalProductSource.tessabit().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商alducadaosta商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean alducadaostaStream(SupplierProduct supplierProduct) {
    	return originalProductSource.alducadaosta().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商sanremo商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean sanremoStream(SupplierProduct supplierProduct) {
    	return originalProductSource.sanremo().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商paolofiorillo商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean paolofiorilloStream(SupplierProduct supplierProduct) {
    	return originalProductSource.paolofiorillo().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商vietti商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean viettiStream(SupplierProduct supplierProduct) {
    	return originalProductSource.vietti().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商aniello商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean anielloStream(SupplierProduct supplierProduct) {
    	return originalProductSource.aniello().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商lindelepalais商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean lindelepalaisStream(SupplierProduct supplierProduct) {
    	return originalProductSource.lindelepalais().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商pritelli商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean pritelliStream(SupplierProduct supplierProduct) {
    	return originalProductSource.pritelli().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商piccadilly商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean piccadillyStream(SupplierProduct supplierProduct) {
    	return originalProductSource.piccadilly().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商vela商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean velaStream(SupplierProduct supplierProduct) {
    	return originalProductSource.vela().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商monti商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean montiStream(SupplierProduct supplierProduct) {
    	return originalProductSource.monti().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商creative99商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean creative99Stream(SupplierProduct supplierProduct) {
    	return originalProductSource.creative99().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商leam商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean leamStream(SupplierProduct supplierProduct) {
    	return originalProductSource.leam().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商bagheera商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean bagheeraStream(SupplierProduct supplierProduct) {
    	return originalProductSource.bagheera().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商papini商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean papiniStream(SupplierProduct supplierProduct) {
    	return originalProductSource.papini().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商zitafabiani商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean zitafabianiStream(SupplierProduct supplierProduct) {
    	return originalProductSource.zitafabiani().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商wise商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean wiseStream(SupplierProduct supplierProduct) {
    	return originalProductSource.wise().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商baseblu商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean basebluStream(SupplierProduct supplierProduct) {
    	return originalProductSource.baseblu().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商raffaellonetwork商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean raffaellonetworkStream(SupplierProduct supplierProduct) {
    	return originalProductSource.raffaellonetwork().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商frmoda商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean frmodaStream(SupplierProduct supplierProduct) {
    	return originalProductSource.frmoda().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商studio69商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean studio69Stream(SupplierProduct supplierProduct) {
    	return originalProductSource.studio69().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商deliberti商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean delibertiStream(SupplierProduct supplierProduct) {
    	return originalProductSource.deliberti().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商parisi商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean parisiStream(SupplierProduct supplierProduct) {
    	return originalProductSource.parisi().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商smets商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean smetsStream(SupplierProduct supplierProduct) {
    	return originalProductSource.smets().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商sarenza商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean sarenzaStream(SupplierProduct supplierProduct) {
    	return originalProductSource.sarenza().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商soleplacenew商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean soleplacenewStream(SupplierProduct supplierProduct) {
    	return originalProductSource.soleplacenew().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商honestdousa商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean honestdousaStream(SupplierProduct supplierProduct) {
    	return originalProductSource.honestdousa().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商optical商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean opticalStream(SupplierProduct supplierProduct) {
    	return originalProductSource.optical().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商tizianafausti商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean tizianafaustiStream(SupplierProduct supplierProduct) {
    	return originalProductSource.tizianafausti().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商theclutcher商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean theclutcherStream(SupplierProduct supplierProduct) {
    	return originalProductSource.theclutcher().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商antonacci商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean antonacciStream(SupplierProduct supplierProduct) {
    	return originalProductSource.antonacci().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商lungolivigno商品流数据
     * @param supplierProduct
     * @return
     */
    public boolean lungolivignoStream(SupplierProduct supplierProduct){
    	return originalProductSource.lungolivigno().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商filippo商品流数据
     * @param supplierProduct
     * @return
     */
    public boolean filippoStream(SupplierProduct supplierProduct){
    	return originalProductSource.filippo().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商dellaMartira商品流数据
     * @param supplierProduct
     * @return
     */
    public boolean dellaMartiraStream(SupplierProduct supplierProduct){
    	return originalProductSource.dellaMartira().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商rosiSerli商品流数据
     * @param supplierProduct
     * @return
     */
    public boolean rosiSerliStream(SupplierProduct supplierProduct){
    	return originalProductSource.rosiSerli().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商mclables商品流数据
     * @param supplierProduct
     * @return
     */
    public boolean mclablesStream(SupplierProduct supplierProduct){
    	return originalProductSource.mclables().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商emonti商品流数据
     * @param supplierProduct
     * @return
     */
    public boolean emontiStream(SupplierProduct supplierProduct){
    	return originalProductSource.emonti().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商dlrboutique商品流数据
     * @param supplierProduct
     * @return
     */
    public boolean dlrboutiqueStream(SupplierProduct supplierProduct){
    	return originalProductSource.dlrboutique().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 所有供应商更新库存队列
     * @param supplierStock
     * @return
     */
    public boolean allProductStockStream(SupplierStock supplierStock){
    	return originalProductSource.allProductStock().send(MessageBuilder.withPayload(supplierStock).build());
    }


    /**
     * 发送供应商antonacci商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean reebonzStream(SupplierProduct supplierProduct) {
        return originalProductSource.reebonz().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    
    /**
     * 发送供应商gaudenzi商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean gaudenziStream(SupplierProduct supplierProduct) {
        return originalProductSource.gaudenzi().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    
    /**
     * 发送供应商monnalisa商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean monnalisaStream(SupplierProduct supplierProduct) {
        return originalProductSource.monnalisa().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    
    /**
     * 发送供应商srl商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean starStream(SupplierProduct supplierProduct) {
        return originalProductSource.star().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    
    /**
     * 发送供应商theStyleSide商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean theStyleSideStream(SupplierProduct supplierProduct) {
        return originalProductSource.theStyleSide().send(MessageBuilder.withPayload(supplierProduct).build());
    }

    /**
     * 供货商marino消息发送
     * @param supplierProduct
     * @return
     */
    public boolean marinoStream(SupplierProduct supplierProduct) {
        return originalProductSource.marino().send(MessageBuilder.withPayload(supplierProduct).build());
    }


    public boolean rediStream(SupplierProduct supplierProduct) {
        return originalProductSource.redi().send(MessageBuilder.withPayload(supplierProduct).build());
    }


    public boolean obluStream(SupplierProduct supplierProduct) {
        return originalProductSource.oblu().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    
    public boolean zhicaiStream(SupplierProduct supplierProduct) {
        return originalProductSource.zhicai().send(MessageBuilder.withPayload(supplierProduct).build());
    }

    public boolean ylatiStream(SupplierProduct supplierProduct) {
        return originalProductSource.ylati().send(MessageBuilder.withPayload(supplierProduct).build());
    }

    public boolean max1980Stream(SupplierProduct supplierProduct) {
        return originalProductSource.max1980().send(MessageBuilder.withPayload(supplierProduct).build());
    }

    /**
     * 发送供应商vietti商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean vietti2Stream(SupplierProduct supplierProduct) {
        return originalProductSource.vietti2().send(MessageBuilder.withPayload(supplierProduct).build());
    }

    /**
     * 发送供应商ilcucciolo商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean ilcuccioloStream(SupplierProduct supplierProduct) {
        return originalProductSource.ilcucciolo().send(MessageBuilder.withPayload(supplierProduct).build());
    }
    /**
     * 发送供应商gebnegozio商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean gebnegozioStream(SupplierProduct supplierProduct){
        return originalProductSource.gebnegozio().send(MessageBuilder.withPayload(supplierProduct).build());
    }

    /** 发送供应商eraldo商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean eraldoStream(SupplierProduct supplierProduct) {
        return originalProductSource.eraldo().send(MessageBuilder.withPayload(supplierProduct).build());
    }


    /**
     * 发送供应商vipgroup商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean vipgroupStream(SupplierProduct supplierProduct){
        return originalProductSource.vipgroup().send(MessageBuilder.withPayload(supplierProduct).build());
    }



    public boolean suitnegoziStream(SupplierProduct supplierProduct) {
        return originalProductSource.suitnegozi().send(MessageBuilder.withPayload(supplierProduct).build());
    }

    /**
     * 发送供应商tricot商品流数据
     * @param supplierProduct 消息体
     * @return 如果发送成功返回true,否则返回false
     */
    public boolean tricotStream(SupplierProduct supplierProduct){
        return originalProductSource.tricot().send(MessageBuilder.withPayload(supplierProduct).build());
    }
}
