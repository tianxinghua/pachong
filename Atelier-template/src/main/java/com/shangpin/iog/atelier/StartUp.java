package com.shangpin.iog.atelier;

import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.atelier.priceService.CommonPriceService;
import com.shangpin.iog.atelier.priceService.ViettiPriceService;

/**
 * 
 * @author lubaijiang 2016/07/29
 *
 */
public class StartUp {
	private static Logger log = Logger.getLogger("info");
	private static ResourceBundle bdl=null;
    private static String supplierId = "";
    private static String picpath = "";
    private static int day;
    private static String identity = null;
    
    static {
        if(null==bdl)
         bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        day = Integer.valueOf(bdl.getString("day"));
        picpath = bdl.getString("picpath");
        identity = bdl.getString("identity");
        
    }
    private static ApplicationContext factory;
	private static void loadSpringContext()

	{

        factory = new AnnotationConfigApplicationContext(AppContext.class);
	}
	
	/**
	 * 抓取产品开始主方法，必须要传入1个参数
	 * 传入的参数代表启动那个service服务，如传入vi代表启动ViettiPriceService,可自行拓展<br>
	 * 参数可取值为: vi,leam,dan,it
	 * @param args
	 */
	public static void main(String[] args){	
		
		if(StringUtils.isBlank(identity)){
			System.out.println("请配置参数identity，指定运行的供应商service"); 
			log.info("请配置参数identity，指定运行的供应商service");
		}else{
			//加载spring
	        log.info("----拉取Atelier-template数据开始----");
			loadSpringContext();
	        log.info("----初始SPRING成功----");
	        //拉取数据
	        if("vi".equals(identity)){
	        	ViettiPriceService fetchProduct =(ViettiPriceService)factory.getBean("viettiPriceService");
		        fetchProduct.handleData("spu", supplierId, day, picpath);
	        }else if("leam".equals(identity)){
	        	//Leam采用通用处理 第3列作为供价  第4列作为市场价
	        	CommonPriceService fetchProduct = (CommonPriceService) factory.getBean("commonPriceService");
	        	fetchProduct.handleData("spu", supplierId, day, picpath);
	        }else if("dan".equals(identity)){
	        	//Daniello采用通用处理 第3列作为供价  第4列作为市场价
	        	CommonPriceService fetchProduct = (CommonPriceService) factory.getBean("commonPriceService");
	        	fetchProduct.handleData("spu", supplierId, day, picpath);
	        }else if("it".equals(identity)){
	        	//italiani采用通用处理 第3列作为供价  第4列作为市场价
	        	CommonPriceService fetchProduct = (CommonPriceService) factory.getBean("commonPriceService");
	        	fetchProduct.handleData("spu", supplierId, day, picpath);
	        }else if("wise".equals(identity)){
	        	//wise采用通用处理 第3列作为供价  第4列作为市场价
	        	CommonPriceService fetchProduct = (CommonPriceService) factory.getBean("commonPriceService");
	        	fetchProduct.handleData("spu", supplierId, day, picpath);
	        }else if("cr".equals(identity)){
	        	//createve99采用通用处理 第3列作为供价  第4列作为市场价
	        	CommonPriceService fetchProduct = (CommonPriceService) factory.getBean("commonPriceService");
	        	fetchProduct.handleData("spu", supplierId, day, picpath);
	        }else if("bagh".equals(identity)){
	        	//bagheera采用通用处理 第3列作为供价  第4列作为市场价
	        	CommonPriceService fetchProduct = (CommonPriceService) factory.getBean("commonPriceService");
	        	fetchProduct.handleData("spu", supplierId, day, picpath);
	        }else if("tessabit".equals(identity)){
	        	//tessabit采用通用处理 第3列作为供价  第4列作为市场价
	        	CommonPriceService fetchProduct = (CommonPriceService) factory.getBean("commonPriceService");
	        	fetchProduct.handleData("spu", supplierId, day, picpath);
	        }else if("genteroma".equals(identity)){
	        	//genteroma采用通用处理 第3列作为供价  第4列作为市场价
	        	CommonPriceService fetchProduct = (CommonPriceService) factory.getBean("commonPriceService");
	        	fetchProduct.handleData("spu", supplierId, day, picpath);
	        }else if("common".equals(identity)){
	        	//以后通用标识就是common，不用再写代码了。采用通用处理 第3列作为供价  第4列作为市场价.
	        	CommonPriceService fetchProduct = (CommonPriceService) factory.getBean("commonPriceService");
	        	fetchProduct.handleData("spu", supplierId, day, picpath);
	        }

	        log.info("----拉取Atelier-template数据完成----");
			System.out.println("-------fetch end---------");
		}
		
	}
}
