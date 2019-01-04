package com.shangpin.iog.zitafabiani.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.zitafabiani.dto.Item;
import com.shangpin.iog.zitafabiani.schedule.AppContext;
import com.shangpin.iog.zitafabiani.util.CVSUtil;
import com.shangpin.iog.zitafabiani.util.DownLoad;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lubaijiang on 2016/9/14.
 */
@Component("zitafabianistock")
public class StockImp  extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    private static ResourceBundle bdl=null;
    private static String supplierId;
    private static String url;
	private static String local = null;
    private static ApplicationContext factory;
    private static LoggerUtil logError = LoggerUtil.getLogger("error");
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        url = bdl.getString("url");
        local = bdl.getString("local");
    }
    @Override
    public Map<String,String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
    	
    	//获取库存元数据
    	Map<String,String> stockMap = new HashMap<String,String>();
        Map<String,String> skustock = new HashMap<String,String>();
        
        try {
			logger.info("=======开始下载文件=======");
			DownLoad.downFromNet(url, local);
			logger.info("=======下载结束=======");
			File file = new File(local);
			List<Item> lists = CVSUtil.readCSV(file, Item.class, ';');			
			logger.info("======转化对象成功========");
			for(Item item:lists){
				stockMap.put(item.getSupplier_sku_no()+"-"+item.getProduct_size(), item.getStock());
			}
			logger.info("======拉取over========"+stockMap.size());
        }catch(Exception e){
        	logError.error(e);
        }
        
        for (String skuno : skuNo) {
            if(stockMap.containsKey(skuno)){
                skustock.put(skuno, stockMap.get(skuno));
            } else{
                skustock.put(skuno, "0");
            }
        }
        logger.info("返回的skustock.size======"+skustock.size()); 
        return skustock;
    }

    public static void main(String[] args) throws Exception {
    	//加载spring
        loadSpringContext();
       
    }
}
