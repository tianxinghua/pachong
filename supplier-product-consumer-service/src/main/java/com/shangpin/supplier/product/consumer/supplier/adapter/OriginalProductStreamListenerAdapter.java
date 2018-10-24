package com.shangpin.supplier.product.consumer.supplier.adapter;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.shangpin.ephub.client.message.original.body.SupplierProduct;
import com.shangpin.ephub.client.message.original.body.SupplierStock;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;
import com.shangpin.supplier.product.consumer.supplier.allstock.StockHandler;
import com.shangpin.supplier.product.consumer.supplier.common.atelier.IAtelierHandler;
import com.shangpin.supplier.product.consumer.supplier.common.spinnaker.ISpinnakerHandler;

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
	@Qualifier("fratinardi")
	private ISupplierHandler fratinardiHandler;	
	@Autowired
	@Qualifier("forzieri")
	private ISupplierHandler forzieriHandler;	
	
	@Autowired
	@Qualifier("stefaniaHandler")
	private ISupplierHandler stefaniaHandler;	
	
	@Autowired
	private IAtelierHandler atelierCommonHandler;
	@Autowired
	private ISpinnakerHandler spinnakerCommonHandler;	
	@Autowired
	private ISupplierHandler edsCommonHandler;
	
	@Autowired
	@Qualifier("biondiniHandler")
	private ISupplierHandler biondiniHandler;
	@Autowired
	@Qualifier("tonyHandler")
	private ISupplierHandler tonyHandler;
	@Autowired
	@Qualifier("coltortiHandler")
	private ISupplierHandler coltortiHandler;
	@Autowired
	@Qualifier("monnierHandler")
	private ISupplierHandler monnierfreresHandler;
	@Autowired
	@Qualifier("giglioHandler")
	private ISupplierHandler giglioHandler;
	@Autowired
	@Qualifier("eleonoraHandler")
	private ISupplierHandler eleonoraHandler;
	@Autowired
	@Qualifier("studio69Handler")
	private ISupplierHandler studio69Handler;
	@Autowired
	@Qualifier("alducadaostaHandler") 
	private ISupplierHandler alducadaostaHandler;
	@Autowired
	@Qualifier("delibertiHandler2")
	private ISupplierHandler delibertiHandler;
    @Autowired
    @Qualifier("parisiHandler") 
    private ISupplierHandler parisiHandler;
	@Autowired
	@Qualifier("theclutcherHandler") 
	private ISupplierHandler theclutcherHandler;
	@Autowired
	@Qualifier("raffaellonetworkHandler") 
	private ISupplierHandler raffaellonetworkHandler;
	@Qualifier("dellogliostoreHandler")
	private ISupplierHandler dellogliostoreHandler;
	@Autowired
	@Qualifier("pritelliHandler")
	private ISupplierHandler pritelliHandler;
	@Autowired
	@Qualifier("piccadillyHandler")
	private ISupplierHandler piccadillyHandler;
	@Autowired
	@Qualifier("massHandler")
	private ISupplierHandler massHandler;
	@Autowired
	@Qualifier("frmodaHandler")
	private ISupplierHandler frmodaHandler;
//	@Autowired
//	@Qualifier("zitafabianiHandler")
//	private ISupplierHandler zitafabianiHandler;

	@Autowired
	@Qualifier("opticalHandler")
	private ISupplierHandler opticalHandler;

	@Autowired
	@Qualifier("antonacciHandler")
	private ISupplierHandler antonacciHandler;
	
	@Autowired
	@Qualifier("bagheeraHandler")
	private ISupplierHandler bagheeraHandler;
	
	@Autowired
	@Qualifier("lungolivignoHandler")
	private ISupplierHandler lungolivignoHandler;



	@Autowired
	@Qualifier("dellaHandler")
	private ISupplierHandler dellaHandler;


	@Autowired
	@Qualifier("emontiHandler")
	private ISupplierHandler emontiHandler;


    @Autowired
    @Qualifier("vietti2Handler")
    private ISupplierHandler vietti2Handler;

	@Autowired
	@Qualifier("mclablesHandler") 
	private ISupplierHandler mclablesHandler;
	
	@Autowired
	@Qualifier("smetsHandler")
	private ISupplierHandler smetsHandler;
	
	@Autowired
	@Qualifier("filippoHandler")
	private ISupplierHandler filippoHandler;
	
	@Autowired
	@Qualifier("monnalisaHandler")
	private ISupplierHandler monnalisaHandler;
	
	@Autowired
	@Qualifier("starHandler")
	private ISupplierHandler starHandler;
	
	@Autowired
	@Qualifier("theStyleSideHandler")
	private ISupplierHandler theStyleSideHandler;
	
	@Autowired
	@Qualifier("coccolebimbiCommonHandler")
	private ISupplierHandler coccolebimbiCommonHandler;
	
	@Autowired
	private StockHandler stockHandler;

	@Autowired
	@Qualifier("reebonzHandler")
	private ISupplierHandler reebonzHandler;

	@Autowired
	@Qualifier("rediHandler")
	private ISupplierHandler rediHandler;


	@Autowired
	@Qualifier("obluHandler")
	private ISupplierHandler obluHandler;

	@Autowired
	@Qualifier("zhicaiHandler")
	private ISupplierHandler zhicaiHandler;

	@Autowired
	@Qualifier("ylatiHandler")
	private ISupplierHandler ylatiHandler;

    @Autowired
    @Qualifier("suitnegoziHandler")
    private ISupplierHandler suitnegoziHandler;


    /**
	 * coccolebimbi供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void coccolebimbiStreamListen(SupplierProduct message, Map<String, Object> headers) {
		coccolebimbiCommonHandler.handleOriginalProduct(message, headers);
	}
	/**
	 * 此队列重名，未使用
	 * biondioni供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void biondioniStreamListen(SupplierProduct message, Map<String, Object> headers) {
		biondiniHandler.handleOriginalProduct(message, headers);
	}
	
	/**
	 * biondini供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void biondiniStreamListen(SupplierProduct message, Map<String, Object> headers) {
		biondiniHandler.handleOriginalProduct(message, headers); 
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
		edsCommonHandler.handleOriginalProduct(message, headers); 
	}
	
	/**
	 * fashionTamers供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void fashionTamersStreamListen(SupplierProduct message, Map<String, Object> headers) {
		edsCommonHandler.handleOriginalProduct(message, headers); 
	}
	
	/**
	 * fratinardiHandler供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void fratinardiStreamListen(SupplierProduct message, Map<String, Object> headers) {
		fratinardiHandler.handleOriginalProduct(message,headers);
	}
	/**
	 * forzieriHandler供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void forzieriStreamListen(SupplierProduct message, Map<String, Object> headers) {
		forzieriHandler.handleOriginalProduct(message,headers);
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
	
	
	/*
	 * 以下为spinnaker系统统一处理 ，共八家供应商
	 */
	/**
	 * dolciTrame供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void dolciTrameStreamListen(SupplierProduct message, Map<String, Object> headers) {
		spinnakerCommonHandler.handleOriginalProduct(message, headers); 
	}
	/**
	 * spinnaker供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void portofinoStreamListen(SupplierProduct message, Map<String, Object> headers) {
		spinnakerCommonHandler.handleOriginalProduct(message, headers); 
	}
	/**
	 * spinnaker供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void spinnakerStreamListen(SupplierProduct message, Map<String, Object> headers) {
		spinnakerCommonHandler.handleOriginalProduct(message, headers); 
	}
	/**
	 * pozzilei供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void pozzileiStreamListen(SupplierProduct message, Map<String, Object> headers) {
		spinnakerCommonHandler.handleOriginalProduct(message, headers);
	}
	/**
	 * pozzileiarte供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void pozzileiarteStreamListen(SupplierProduct message, Map<String, Object> headers) {
		spinnakerCommonHandler.handleOriginalProduct(message, headers);
	}
	/**
	 * pozzileiforte供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void pozzileiforteStreamListen(SupplierProduct message, Map<String, Object> headers) {
		spinnakerCommonHandler.handleOriginalProduct(message, headers);
	}
	/**
	 * vela供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void velaStreamListen(SupplierProduct message, Map<String, Object> headers) {
		spinnakerCommonHandler.handleOriginalProduct(message, headers);
	}
	/**
	 * sanremo供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void sanremoStreamListen(SupplierProduct message, Map<String, Object> headers) {
		spinnakerCommonHandler.handleOriginalProduct(message, headers);
	}
	/**
	 * monti供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void montiStreamListen(SupplierProduct message, Map<String, Object> headers) {
		spinnakerCommonHandler.handleOriginalProduct(message, headers);
	}
	/**
	 * papini供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void papiniStreamListen(SupplierProduct message, Map<String, Object> headers) {
		spinnakerCommonHandler.handleOriginalProduct(message, headers);
	}

	/*
	 * 以下为atelier系统统一处理 ，共17家
	 */
	/**
	 * angeloMinetti供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void angeloMinettiStreamListen(SupplierProduct message, Map<String, Object> headers) {
		atelierCommonHandler.handleOriginalProduct(message, headers);		
	}
	/**
	 * ostore供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void ostoreStreamListen(SupplierProduct message, Map<String, Object> headers) {
		atelierCommonHandler.handleOriginalProduct(message, headers);		
	}
	/**
	 * max1980供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void MAX1980StreamListen(SupplierProduct message, Map<String, Object> headers) {
		atelierCommonHandler.handleOriginalProduct(message, headers);
	}


	/**
	 * MENGOTTISNC供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void mengottiSncStreamListen(SupplierProduct message, Map<String, Object> headers) {
		atelierCommonHandler.handleOriginalProduct(message, headers);
	}
	/**
	 * brunarosso供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void brunarossoStreamListen(SupplierProduct message, Map<String, Object> headers) {
		atelierCommonHandler.handleOriginalProduct(message, headers);		
	}
	/**
	 * studio69供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void studio69StreamListen(SupplierProduct message, Map<String, Object> headers) {
		studio69Handler.handleOriginalProduct(message, headers);	
	}
	/**
	 * creative99供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void creative99StreamListen(SupplierProduct message, Map<String, Object> headers) {
		atelierCommonHandler.handleOriginalProduct(message, headers);	
	}
	/**
	 * leam供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void leamStreamListen(SupplierProduct message, Map<String, Object> headers) {
		atelierCommonHandler.handleOriginalProduct(message, headers);
	}
	/**
	 * carofiglio供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void carofiglioStreamListen(SupplierProduct message, Map<String, Object> headers) {
		atelierCommonHandler.handleOriginalProduct(message, headers);	
	}
	/**
	 * genteroma供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void genteromaStreamListen(SupplierProduct message, Map<String, Object> headers) {
		atelierCommonHandler.handleOriginalProduct(message, headers);	
	}
	/**
	 * daniello供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void danielloStreamListen(SupplierProduct message, Map<String, Object> headers) {
		atelierCommonHandler.handleOriginalProduct(message, headers);	
	}
	/**
	 * italiani供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void italianiStreamListen(SupplierProduct message, Map<String, Object> headers) {
		atelierCommonHandler.handleOriginalProduct(message, headers);	
	}
	/**
	 * tufano供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void tufanoStreamListen(SupplierProduct message, Map<String, Object> headers) {
		atelierCommonHandler.handleOriginalProduct(message, headers);	
	}
	/**
	 * wise供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void wiseStreamListen(SupplierProduct message, Map<String, Object> headers) {
		atelierCommonHandler.handleOriginalProduct(message, headers);	
	}
	/**
	 * lindelepalais供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void lindelepalaisStreamListen(SupplierProduct message, Map<String, Object> headers) {
		atelierCommonHandler.handleOriginalProduct(message, headers);	
	}
	/**
	 * tessabit供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void tessabitStreamListen(SupplierProduct message, Map<String, Object> headers) {
		atelierCommonHandler.handleOriginalProduct(message, headers);	
	}
	/**
	 * paolofiorillo供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void paolofiorilloStreamListen(SupplierProduct message, Map<String, Object> headers) {
		atelierCommonHandler.handleOriginalProduct(message, headers);	
	}
	/**
	 * vietti供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void viettiStreamListen(SupplierProduct message, Map<String, Object> headers) {
		atelierCommonHandler.handleOriginalProduct(message, headers);	
	}

    /**
     * vietti2供货商原始数据监听方法
     * @param message 消息体
     * @param headers 消息头
     */
    public void vietti2StreamListen(SupplierProduct message, Map<String, Object> headers) {
        vietti2Handler.handleOriginalProduct(message, headers);
    }
	/**
	 * russocapri供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void russocapriStreamListen(SupplierProduct message, Map<String, Object> headers) {
		atelierCommonHandler.handleOriginalProduct(message, headers);	
	}
	/**
	 * divo供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void divoStreamListen(SupplierProduct message, Map<String, Object> headers) {
 		atelierCommonHandler.handleOriginalProduct(message, headers);	
	}
	
	
	/**
	 * monnierfreres供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void monnierfreresStreamListen(SupplierProduct message, Map<String, Object> headers) {
		monnierfreresHandler.handleOriginalProduct(message, headers);
	}
	/**
	 * eleonorabonucci供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void eleonorabonucciStreamListen(SupplierProduct message, Map<String, Object> headers) {
		eleonoraHandler.handleOriginalProduct(message, headers);
		
	}
	
	/**
	 * giglio供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void giglioStreamListen(SupplierProduct message, Map<String, Object> headers) {
		giglioHandler.handleOriginalProduct(message, headers); 
		
	}

	/**
	 * dellogliostore供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void dellogliostoreStreamListen(SupplierProduct message, Map<String, Object> headers) {
		dellogliostoreHandler.handleOriginalProduct(message, headers);
		
	}
	/**
	 * francescomassa供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void francescomassaStreamListen(SupplierProduct message, Map<String, Object> headers) {
		massHandler.handleOriginalProduct(message,headers);
		
	}
	
	/**
	 * alducadaosta供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void alducadaostaStreamListen(SupplierProduct message, Map<String, Object> headers) {
		alducadaostaHandler.handleOriginalProduct(message, headers);
		
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
	 * pritelli供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void pritelliStreamListen(SupplierProduct message, Map<String, Object> headers) {
		pritelliHandler.handleOriginalProduct(message, headers); 
		
	}
	/**
	 * piccadilly供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void piccadillyStreamListen(SupplierProduct message, Map<String, Object> headers) {
		piccadillyHandler.handleOriginalProduct(message, headers);
		
	}
	
	/**
	 * bagheera供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void bagheeraStreamListen(SupplierProduct message, Map<String, Object> headers) {
		bagheeraHandler.handleOriginalProduct(message, headers); 
		
	}
	
	/**
	 * zitafabiani供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void zitafabianiStreamListen(SupplierProduct message, Map<String, Object> headers) {
		atelierCommonHandler.handleOriginalProduct(message, headers); 
		
	}
	
	/**
	 * baseblu供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void basebluStreamListen(SupplierProduct message, Map<String, Object> headers) {
		edsCommonHandler.handleOriginalProduct(message, headers);

	}
	/**
	 * raffaellonetwork供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void raffaellonetworkStreamListen(SupplierProduct message, Map<String, Object> headers) {
		raffaellonetworkHandler.handleOriginalProduct(message, headers); 
	}
	/**
	 * frmoda供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void frmodaStreamListen(SupplierProduct message, Map<String, Object> headers) {
		frmodaHandler.handleOriginalProduct(message, headers);
	}
	
	/**
	 * deliberti供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void delibertiStreamListen(SupplierProduct message, Map<String, Object> headers) {
		delibertiHandler.handleOriginalProduct(message, headers); 
		
	}
	/**
     * parisi供货商原始数据监听方法
     * @param message 消息体
     * @param headers 消息头
     */
    public void parisiStreamListen(SupplierProduct message, Map<String, Object> headers) {
        parisiHandler.handleOriginalProduct(message, headers); 
        
    }
	/**
	 * cocoroseLondon供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void cocoroseLondonStreamListen(SupplierProduct message, Map<String, Object> headers) {
		smetsHandler.handleOriginalProduct(message, headers); 
	}
	/**
	 * smets供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void smetsStreamListen(SupplierProduct message, Map<String, Object> headers) {
		smetsHandler.handleOriginalProduct(message, headers); 
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
		opticalHandler.handleOriginalProduct(message, headers);
		
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
		theclutcherHandler.handleOriginalProduct(message, headers); 
	}
	/**
	 * theclutcher供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void antonacciStreamListen(SupplierProduct message, Map<String, Object> headers) {
		antonacciHandler.handleOriginalProduct(message, headers); 
	}
	/**
	 * lungolivigno供应商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void lungolivignoStreamListen(SupplierProduct message, Map<String, Object> headers){
		lungolivignoHandler.handleOriginalProduct(message, headers); 
	}
	/**
	 * filippo供应商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void filippoStreamListen(SupplierProduct message, Map<String, Object> headers){
		filippoHandler.handleOriginalProduct(message, headers); 
	}
	/**
	 * dellaMartira供应商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void dellaMartiraStreamListen(SupplierProduct message, Map<String, Object> headers){
		dellaHandler.handleOriginalProduct(message, headers);
	}
	/**
	 * rosiSerli供应商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void rosiSerliStreamListen(SupplierProduct message, Map<String, Object> headers){
		//TOOD 
	}
	/**
	 * mclables供应商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void mclablesStreamListen(SupplierProduct message, Map<String, Object> headers){
		mclablesHandler.handleOriginalProduct(message, headers); 
	}
	/**
	 * emonti供应商原始数据监听方法
	 * @param message
	 * @param headers
	 */
	public void emontiStreamListen(SupplierProduct message, Map<String, Object> headers){
		emontiHandler.handleOriginalProduct(message, headers);
	}

	/**
	 * dlrboutique供应商原始数据监听方法
	 * @param message
	 * @param headers
	 */
	public void dlrboutiqueStreamListen(SupplierProduct message, Map<String, Object> headers) {
		atelierCommonHandler.handleOriginalProduct(message, headers);
	}

	/**
	 * reebonz供应商原始数据监听方法
	 * @param message
	 * @param headers
	 */
	public void reebonzStreamListen(SupplierProduct message, Map<String, Object> headers) {
		reebonzHandler.handleOriginalProduct(message, headers);
	}
	
	/**
	 * gaudenzi供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void gaudenziStreamListen(SupplierProduct message, Map<String, Object> headers) {
		atelierCommonHandler.handleOriginalProduct(message, headers);
	}
	
	/**
	 * monnalisa供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void monnalisaStreamListen(SupplierProduct message, Map<String, Object> headers) {
		monnalisaHandler.handleOriginalProduct(message, headers);
	}
	
	/**
	 * star供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void starStreamListen(SupplierProduct message, Map<String, Object> headers) {
		starHandler.handleOriginalProduct(message, headers);
	}
	
	/**
	 * theStyleSide供货商原始数据监听方法
	 * @param message 消息体
	 * @param headers 消息头
	 */
	public void theStyleSideStreamListen(SupplierProduct message, Map<String, Object> headers) {
		theStyleSideHandler.handleOriginalProduct(message, headers);
	}
	
	/**
	 * 所以供应商更新库存监听方法
	 * @param message
	 * @param headers
	 */
	public void allProductStockStreamListen(SupplierStock message, Map<String, Object> headers){
		stockHandler.handleAllProductStock(message, headers);
	}
	/**
	 * 供应商julianFashion处理原始数据
	 * @param message
	 * @param headers
	 */
	public void julianFashionStreamListen(SupplierProduct message, Map<String, Object> headers) {
		atelierCommonHandler.handleOriginalProduct(message, headers);
	}

	/**
	 * 供应商marino处理原始数据
	 * @param message
	 * @param headers
	 */
	public void marinoStreamListen(SupplierProduct message, Map<String, Object> headers) {
		atelierCommonHandler.handleOriginalProduct(message, headers);
	}


	/**
	 * 供应商redi处理原始数据
	 * @param message
	 * @param headers
	 */
	public void rediStreamListen(SupplierProduct message, Map<String, Object> headers) {
		rediHandler.handleOriginalProduct(message, headers);
	}

	/**
	 * 供应商oblu处理原始数据
	 * @param message
	 * @param headers
	 */
	public void obluStreamListen(SupplierProduct message, Map<String, Object> headers) {
		obluHandler.handleOriginalProduct(message, headers);
	}
	/**
	 * 供应商zhicai处理原始数据
	 * @param message
	 * @param headers
	 */
	public void zhicaiStreamListen(SupplierProduct message, Map<String, Object> headers) {
		zhicaiHandler.handleOriginalProduct(message, headers);
	}

	public void ylatiStreamListen(SupplierProduct message, Map<String, Object> headers) {
		ylatiHandler.handleOriginalProduct(message, headers);
	}

    public void suitnegoziStreamListen(SupplierProduct message, Map<String, Object> headers) {
        suitnegoziHandler.handleOriginalProduct(message, headers);
    }
}
