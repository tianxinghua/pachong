package com.shangpin.iog.service;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dao.Item;
import com.shangpin.iog.dao.Rss;
import com.shangpin.iog.utils.XMLUtil;

@Component("UpdateStock")
public class UpdateStock extends AbsUpdateProductStock {

	private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");
    private static ResourceBundle bdl=null;
    private static String supplierId = "";
    private static String url = "";
    private static OutTimeConfig outTimeConf = new OutTimeConfig(1000*5, 1000*60 * 5, 1000*60 * 5);
    
    static {
        if(null==bdl)
         bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        url = bdl.getString("pullUrl");
    }
	
	@Override
	public Map<String, String> grabStock(Collection<String> skuNo)
			throws ServiceException, Exception {
		
		Map<String, String> skustock = new HashMap<>();
		Map<String,String> stockMap = new HashMap<>();
		
		if(StringUtils.isNotBlank(url)){
    		url = url + XMLUtil.random();
    		System.out.println("url=="+url);
			String result = HttpUtil45.get(url, outTimeConf,null);
			if(!result.equals(HttpUtil45.errorResult)){
				Rss rss = XMLUtil.gsonXml2Obj(Rss.class, result);
				if(null != rss && null != rss.getChannel()){					
					List<Item> items = rss.getChannel().getItem();
					for(Item item : items){
						//库存不为0，入库
						String stock = item.getQty();
						if(StringUtils.isNotBlank(stock) && !stock.equals("0")){
							stockMap.put(item.getSku(), stock);
//							System.out.println(stockMap.toString());
						}
					}
					
					for (String skuno : skuNo) {
			            if(stockMap.containsKey(skuno)){
			                skustock.put(skuno, stockMap.get(skuno));
			            } else{
			                skustock.put(skuno, "0");
			            }
			        }
					
				}else{
					loggerError.error("无rss 或 channel");
				}
			}else{
				loggerError.error("下载失败"+result);
			}
		}else{
			loggerError.error("url为空");
		}
		
		return skustock;
	}
	
	private static ApplicationContext factory;
    
	private static void loadSpringContext()

	{

        factory = new AnnotationConfigApplicationContext(AppContext.class);
	}
	public static void main(String[] args){
		
		loadSpringContext();
		System.out.println("初始化Spring成功");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		UpdateStock up = (UpdateStock)factory.getBean("UpdateStock");
		try{
			
			up.updateProductStock(supplierId, "2015-01-01 00:00",
					format.format(new Date()));
			
		}catch(Exception ex){
			loggerError.error(ex);
			ex.printStackTrace();
		}
		logger.info("-------cosentinoShop更新库存完成-------------");
		System.out.println("-------cosentinoShop更新库存完成-------------");
	}

}
