package com.shangpin.iog.bernardellistores;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.bernardellistores.service.FetchProduct;

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
		brandMap.put("WOMAN-SHOES", new HashSet<String>());
		brandMap.get("WOMAN-SHOES").add("ASH");
		brandMap.get("WOMAN-SHOES").add("DR. MARTENS");
		brandMap.get("WOMAN-SHOES").add("MICHAEL MICHAEL KORS");
		brandMap.get("WOMAN-SHOES").add("GIUSEPPE ZANOTTI DESIGN");
		brandMap.get("WOMAN-SHOES").add("HUNTER");
		brandMap.get("WOMAN-SHOES").add("DSQUARED2");
		brandMap.get("WOMAN-SHOES").add("GOLDEN GOOSE DELUXE");
		brandMap.get("WOMAN-SHOES").add("PHILIPPE MODEL");
		brandMap.get("WOMAN-SHOES").add("Y-3 YOHJI YAMAMOTO ADIDAS");
		brandMap.get("WOMAN-SHOES").add("JIL SANDER");
		brandMap.get("WOMAN-SHOES").add("STUART WEITZMAN");
		brandMap.get("WOMAN-SHOES").add("TORY BURCH");
		brandMap.get("WOMAN-SHOES").add("3.1 PHILLIP LIM");
		brandMap.get("WOMAN-SHOES").add("REPETTO");
		brandMap.put("MAN-SHOES", new HashSet<String>());
		brandMap.get("MAN-SHOES").add("GOLDEN GOOSE DELUXE BRAND");
		brandMap.get("MAN-SHOES").add("POLO RALPH LAUREN");
		brandMap.get("MAN-SHOES").add("PHILIPPE MODEL");
		brandMap.put("MAN-CLOTHING", new HashSet<String>());
		brandMap.get("MAN-CLOTHING").add("PAUL SMITH");
		brandMap.get("MAN-CLOTHING").add("BURBERRY");
		brandMap.get("MAN-CLOTHING").add("POLO RALPH LAUREN");
		brandMap.get("MAN-CLOTHING").add("LOVE MOSCHINO");
		brandMap.get("MAN-CLOTHING").add("MONCLER");
		brandMap.put("WOMAN-BAGS", new HashSet<String>());
		brandMap.get("WOMAN-BAGS").add("CELINE");
		brandMap.get("WOMAN-BAGS").add("BURBERRY");
		brandMap.get("WOMAN-BAGS").add("DSQUARED2");
		brandMap.get("WOMAN-BAGS").add("MICHAEL MICHAEL KORS");
		brandMap.get("WOMAN-BAGS").add("MOSCHINO");
		brandMap.get("WOMAN-BAGS").add("3.1 PHILLIP LIM");
		brandMap.get("WOMAN-BAGS").add("STELLA JEAN");
		brandMap.get("WOMAN-BAGS").add("LOVE MOSCHINO");
		brandMap.get("WOMAN-BAGS").add("ALEXANDER WANG");
		brandMap.get("WOMAN-BAGS").add("ARMANI COLLEZIONI");
		brandMap.get("WOMAN-BAGS").add("ISSEY MIYAKE");
		brandMap.get("WOMAN-BAGS").add("VERSACE COLLECTION");
		brandMap.put("MAN-BAGS", new HashSet<String>());
		brandMap.put("MAN-BAGS", new HashSet<String>());
		brandMap.get("MAN-BAGS").add("CELINE");
		brandMap.get("MAN-BAGS").add("BURBERRY");
		brandMap.get("MAN-BAGS").add("DSQUARED2");
		brandMap.get("MAN-BAGS").add("MICHAEL KORS");
		brandMap.get("MAN-BAGS").add("MOSCHINO");
		brandMap.get("MAN-BAGS").add("SAINT LAURENT");
		brandMap.get("MAN-BAGS").add("STELLA JEAN");
		brandMap.get("MAN-BAGS").add("LOVE MOSCHINO");
	}

	private static void loadSpringContext() {
		factory = new AnnotationConfigApplicationContext(AppContext.class);
	}

	public static void main(String[] args) {

		// 加载spring
		log.info("----拉取bernardellistores数据开始----");
		loadSpringContext();
		log.info("----初始SPRING成功----");
		// 拉取数据
		FetchProduct fetchProduct = (FetchProduct) factory.getBean("bernardellistores");
		fetchProduct.fetchProductAndSave(supplierId, brandMap);

		log.info("----拉取bernardellistores数据完成----");

		System.out.println("-------fetch end---------");
		System.exit(0);
	}

}
