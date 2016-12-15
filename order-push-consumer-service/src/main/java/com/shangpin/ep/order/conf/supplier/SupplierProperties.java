package com.shangpin.ep.order.conf.supplier;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/**
 * <p>Title:SupplierProperties.java </p>
 * <p>Description: 所有供应商订单对接配置信息</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 下午3:39:07
 */
/**
 * Configuration properties for DataSource.
 *
 * @author yanxiaobin
 */
@Getter
@Setter
@ConfigurationProperties(prefix = SupplierProperties.SUPPLIER_PREFIX)
@Component
public class SupplierProperties {
	
	public static final String SUPPLIER_PREFIX = "shangpin.hub.Supplier";

	private Aladuca aladuca;
	
	private Brunarosso brunarosso;
	
	private Coltorti coltorti;
	
	private Creative99 creative99;
	
	private Daniello daniello;
	
	private Deliberti deliberti;
	
	private Della della;
	
	private Divo divo;
	
	private EfashionConf efashionConf;
	
	private Genteroma genteroma;
	
	private InviqaConf inviqaConf;
	
	private Leam leam;
	
	private Linde linde;
	
	private MontiParam montiParam;
	
	private Ostore ostore;
	
	private PapiniConf papiniConf;
	
	private Paolo paolo;
	
	private PozzileiArte pozzileiArte;
	
	private PozzileiForte pozzileiForte;
	
	private PozzileiParam pozzileiParam;
	
	private SanremoParam sanremoParam;
	
	private SpinnakerParam spinnakerParam;
	
	private Stefania stefania;
	
	private Studio69 studio69;
	
	private TonyConf tonyConf;
	
	private TonySub tonySub;
	
	private VelaParam velaParam;
	
	private Wise wise;
	
	private Woolrich woolrich;

	private Supplier supplier;
	
	private RussoCapri russoCapri;
	
	private Clutcher clutcher;
	private Kix kix;
	
	private Lamborghini lamborghiniConf;
	private PalomaBarcelo palomaBarceloConf;
	private Carofiglio carofiglio;
	private Tufano tufano;
	private Lungolivigno lungolivigno;
	
}
