package com.shangpin.iog.Helme.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.Helme.dto.Product;
import com.shangpin.iog.Helme.schedule.AppContext;
import com.shangpin.iog.Helme.util.CVSUtil;
import com.shangpin.iog.Helme.util.FTPUtils;
import com.shangpin.iog.common.utils.logger.LoggerUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lubaijiang on 2015/9/14.
 */
@Component("Helmestock")
public class StockImp  extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    private static LoggerUtil logError = LoggerUtil.getLogger("error");
    
    private static ResourceBundle bdl=null;
    private static String supplierId;
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
    
    private static String localFile = null;
    private static String remoteFile = null;
    private static String host = null;
    private static String port = null;
    private static String user = null;
    private static String password = null;
    
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        localFile = bdl.getString("localFile");
        remoteFile = bdl.getString("remoteFile");
        host = bdl.getString("host");
        port = bdl.getString("port");
        user = bdl.getString("user");
        password = bdl.getString("password");
    }
    @Override
    public Map<String,String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
    	
    	//获取库存元数据
    	Map<String, String> skustock = new HashMap<String, String>();
		Map<String,String> stockMap = new HashMap<String, String>();
        List<Product> items = null;
        try{
        	//业务实现
        	FTPUtils ftp =new FTPUtils(user, password, host, 21);
			ftp.downFile("", remoteFile, localFile);
			File file = new File(localFile+File.separator+remoteFile);
			items = CVSUtil.readCSV(file, Product.class, ';');
			for(Product item:items){
				try {
					String skuId = item.getBarcode().replaceAll("[*]", "").trim();
					stockMap.put(skuId, item.getGiacenza());
				} catch (Exception e) {
					e.printStackTrace(); 
				}
				
			}
			try {
				ftp.logout();
			} catch (Exception e) {
				logError.error(e); 
			}
        
        }catch(Exception ex){
        	logError.error(ex);
        	ex.printStackTrace(); 
        	return skustock;
        }
        
        for (String skuno : skuNo) {
            if(stockMap.containsKey(skuno)){
                skustock.put(skuno, stockMap.get(skuno));
            } else{
                skustock.put(skuno, "0");
            }
        }
        return skustock;
    }

    public static void main(String[] args) throws Exception {
    	//加载spring
//        loadSpringContext();     
    	StockImp grabStock = new StockImp();
    	grabStock.grabStock(null);
    }
}
