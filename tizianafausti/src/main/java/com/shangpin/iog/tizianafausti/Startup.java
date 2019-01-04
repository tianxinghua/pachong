package com.shangpin.iog.tizianafausti;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.tizianafausti.service.FetchProduct;

public class Startup {
	private static Logger log = Logger.getLogger("info");

	private static ApplicationContext factory;

	private static ResourceBundle bdl = null;

	public static String supplierId;

	private static Map<String, Set<String>> brandMap = new HashMap<String, Set<String>>();

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		// 爬取所有品类
		brandMap.put("ALL", new HashSet<String>());
		brandMap.get("ALL").add("MASSIMO ALBA");
		brandMap.get("ALL").add("LORO PIANA");
		brandMap.get("ALL").add("DRIES VAN NOTEN");
		brandMap.get("ALL").add("RICK OWENS");
		brandMap.get("ALL").add("DONNA KARAN");
		brandMap.get("ALL").add("TOMAS MAIER");
		brandMap.get("ALL").add("RALPH LAUREN");
		brandMap.get("ALL").add("STOULS");
		brandMap.get("ALL").add("PAUL ANDREW");
		brandMap.get("ALL").add("THE ROW");
		brandMap.get("ALL").add("CEDRIC CHARLIER");
		brandMap.get("ALL").add("ALEXANDER MCQUEEN");
		brandMap.get("ALL").add("KOLOR");
		brandMap.get("ALL").add("EMILIO PUCCI");
		brandMap.get("ALL").add("LAURENCE DACADE");
		brandMap.get("ALL").add("FORTE COUTURE");
		brandMap.get("ALL").add("BLANCHA");
		brandMap.get("ALL").add("MATE");
		brandMap.get("ALL").add("CAPUCCI");
		brandMap.get("ALL").add("3.1 PHILLIP LIM");
		brandMap.get("ALL").add("DKNY");
		brandMap.get("ALL").add("ALBINO TEODORO");
		brandMap.get("ALL").add("ISABEL MARANT");
		brandMap.get("ALL").add("ISAIA");
		brandMap.get("ALL").add("AU JOUR LE JOUR");
		brandMap.get("ALL").add("GIANCARLO PETRIGLIA");
		brandMap.get("ALL").add("JOSEPH");
		brandMap.get("ALL").add("WEBER HODEL FEDER");
		brandMap.get("ALL").add("NEW YORK INDUSTRIE");
		brandMap.get("ALL").add("MARCO de VINCENZO");
		brandMap.get("ALL").add("OAMC");
		brandMap.get("ALL").add("NICHOLAS KIRKWOOD");
		brandMap.get("ALL").add("MOSCHINO");
		// 测试用品牌
//		brandMap.get("ALL").add("DOLCE & GABBANA");
		
		// 爬取男子
		brandMap.put("MAN", new HashSet<String>());
		brandMap.get("MAN").add("VALENTINO");
		brandMap.get("MAN").add("Z ZEGNA");
		brandMap.get("MAN").add("GIUSEPPE ZANOTTI DESIGN");
		// 爬取女士
		brandMap.put("WOMAN", new HashSet<String>());
		brandMap.get("WOMAN").add("VERSACE");
		// 爬取女装
		brandMap.put("WOMAN-CLOTHING", new HashSet<String>());
		brandMap.get("WOMAN-CLOTHING").add("STELLA MCCARTNEY");
		// 爬取服装
		brandMap.put("CLOTHING", new HashSet<String>());
		brandMap.get("CLOTHING").add("GIVENCHY");
		brandMap.get("CLOTHING").add("SAINT LAURENT");
	}

	private static void loadSpringContext() {
		factory = new AnnotationConfigApplicationContext(AppContext.class);
	}

	public static void main(String[] args) {

		// 加载spring
		log.info("----拉取tizianafausti数据开始----");
		loadSpringContext();
		log.info("----初始SPRING成功----");
		// 拉取数据
		FetchProduct fetchProduct = (FetchProduct) factory.getBean("tizianafausti");
		fetchProduct.fetchProductAndSave(supplierId, brandMap);

		log.info("----拉取tizianafausti数据完成----");

		System.out.println("-------fetch end---------");
		System.exit(0);
	}

}
