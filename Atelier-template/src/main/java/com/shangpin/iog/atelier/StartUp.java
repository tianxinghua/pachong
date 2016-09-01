package com.shangpin.iog.atelier;

import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.atelier.priceService.CommonPriceService;
import com.shangpin.iog.atelier.priceService.LeamPriceService;
import com.shangpin.iog.atelier.priceService.ViettiPriceService;
import com.shangpin.iog.atelier.service.FetchProduct;

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
    
    static {
        if(null==bdl)
         bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        day = Integer.valueOf(bdl.getString("day"));
        picpath = bdl.getString("picpath");
        
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
		
//		args = new String[1];
//		args[0] = "wise";
		
		if(args.length==0 || StringUtils.isBlank(args[0])){
			System.out.println("请传入参数，指定运行的供应商service,参数可取值为: vi,leam,dan,it,wise"); 
			log.info("请传入参数，指定运行的供应商service,参数可取值为: vi,leam,dan,it,wise");
		}else{
			//加载spring
	        log.info("----拉取Atelier-template数据开始----");
			loadSpringContext();
	        log.info("----初始SPRING成功----");
	        //拉取数据
	        if("vi".equals(args[0])){
	        	ViettiPriceService fetchProduct =(ViettiPriceService)factory.getBean("viettiPriceService");
		        fetchProduct.handleData("spu", supplierId, day, picpath);
	        }else if("leam".equals(args[0])){
	        	LeamPriceService fetchProduct = (LeamPriceService) factory.getBean("leamPriceService");
	        	fetchProduct.handleData("spu", supplierId, day, picpath);
	        }else if("dan".equals(args[0])){
	        	//Daniello采用通用处理 第3列作为供价  第4列作为市场价
	        	CommonPriceService fetchProduct = (CommonPriceService) factory.getBean("commonPriceService");
	        	fetchProduct.handleData("spu", supplierId, day, picpath);
	        }else if("it".equals(args[0])){
	        	//italiani采用通用处理 第3列作为供价  第4列作为市场价
	        	CommonPriceService fetchProduct = (CommonPriceService) factory.getBean("commonPriceService");
	        	fetchProduct.handleData("spu", supplierId, day, picpath);
	        }else if("wise".equals(args[0])){
	        	//wise采用通用处理 第3列作为供价  第4列作为市场价
	        	CommonPriceService fetchProduct = (CommonPriceService) factory.getBean("commonPriceService");
	        	fetchProduct.handleData("spu", supplierId, day, picpath);
	        }

	        log.info("----拉取Atelier-template数据完成----");
			System.out.println("-------fetch end---------");
		}
		
	}
}
