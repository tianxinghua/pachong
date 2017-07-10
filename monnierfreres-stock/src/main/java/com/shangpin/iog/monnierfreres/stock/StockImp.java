package com.shangpin.iog.monnierfreres.stock;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.monnierfreres.schedule.AppContext;
import com.shangpin.iog.monnierfreres.utils.DownloadAndReadCSV;

/**
 * Created by 赵根春 on 2015/10/12.
 */
@Component("monnierfreres")
public class StockImp extends AbsUpdateProductStock {

    private static Logger logger = Logger.getLogger("info");

    @Override
    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        //定义三方
    	Map<String,String> stockMap = new HashMap<String,String>();
    	Map<String,String> map = DownloadAndReadCSV.readLocalCSV();
    	logger.info("抓取供应商数据并转换完成，map大小是======="+map.size()); 
    	for(String skuId:skuNo){
    		if(map.containsKey(skuId)){
                try {
                    stockMap.put(skuId,map.get(skuId));
                } catch (NumberFormatException e) {
                    stockMap.put(skuId, "0");
                }
            }else{
                stockMap.put(skuId,"0");
            }
    	}
        return stockMap;
    }

	public static String getPath(String realpath) {
		Date dt = new Date();
		SimpleDateFormat matter1 = new SimpleDateFormat("yyyy-MM-ddHH");
		String date = matter1.format(dt).replaceAll("-", "").trim();
		realpath = realpath + "_" + date + ".csv";
		return realpath;
	}

    @SuppressWarnings("unused")
	private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

    public static void main(String[] args) {
    	//加载spring
        loadSpringContext();
    }

}
