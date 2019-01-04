package com.shangpin.iog.atelier;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.atelier.service.FetchProductService;

/**
 * 
 * @author lubaijiang 2016/07/29
 *
 */
public class StartUp {
	private static Logger log = Logger.getLogger("info");
//	private static ResourceBundle bdl = null;
//	private static String identity = null;

//	static {
//		if (null == bdl)
//			bdl = ResourceBundle.getBundle("conf");
////		identity = bdl.getString("identity");
//
//	}
	private static ApplicationContext factory;

	private static void loadSpringContext()

	{

		factory = new AnnotationConfigApplicationContext(AppContext.class);
	}

	/**
	 * 抓取产品开始主方法，必须要传入1个参数
	 * 传入的参数代表启动那个service服务，如传入vi代表启动ViettiPriceService,可自行拓展<br>
	 * 参数可取值为: vi,leam,dan,it
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		log.info("----拉取Atelier-template数据开始----");
		loadSpringContext();
		// 拉取数据
		FetchProductService fetchProduct = (FetchProductService) factory
				.getBean("Atelier-template");
		fetchProduct.sendAndSaveProduct();
		log.info("----拉取Atelier-template数据完成----");
		System.out.println("-------fetch end---------");

	}
}
