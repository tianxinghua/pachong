package com.shangpin.iog.luisaworld.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.luisaworld.dto.Item;
import com.shangpin.iog.luisaworld.util.CVSUtil;
import com.shangpin.iog.luisaworld.util.FTPUtils;
import com.shangpin.sop.AbsUpdateProductStock;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lubaijiang on 2016/3/14.
 */
@Component("luisaworldstock")
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
    
    private static String userName = null;
    private static String password = null;
    private static String ip = null;
    private static String port = null;
    private static String remotePath = null;
    private static String remoteFileName = null;
    private static String localPath = null;
    
    private static String host = null;
    private static String app_key = null;
    private static String app_secret= null;
    
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        port = bdl.getString("port");
        userName = bdl.getString("userName");
        password = bdl.getString("password");
        ip = bdl.getString("ip");
        remotePath = bdl.getString("remotePath");
        remoteFileName = bdl.getString("remoteFileName");
        localPath = bdl.getString("localPath");
        host = bdl.getString("HOST");
        app_key = bdl.getString("APP_KEY");
        app_secret= bdl.getString("APP_SECRET");
    }
    @Override
    public Map<String,Integer> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
    	
    	//获取库存元数据
    	Map<String,String> stockMap = new HashMap<String,String>();
        Map<String,Integer> skustock = new HashMap<String,Integer>();
        FTPUtils ftp = null;
        try{
        	
        	ftp = new FTPUtils(userName, password, ip, Integer.parseInt(port));
        	if(null != ftp){
        		ftp.downFile(remotePath, remoteFileName, localPath);
        		File file = new File(localPath+File.separator+remoteFileName);
        		List<Item> list = CVSUtil.readCSV(file, Item.class, ',');
        		for(Item item:list){
        			stockMap.put(item.getBarcode(), item.getQty());
        		}
        	}else{
        		//log
        		logError.error("ftp为空，下载文件时异常！！！！");
        		return skustock;
        	}        	
        	
        }catch(Exception ex){
        	ex.printStackTrace();
        	//log
        	logError.error(ex.getMessage());
        	return skustock;
        }
        for (String skuno : skuNo) {
            if(stockMap.containsKey(skuno)){
                skustock.put(skuno, Integer.parseInt(stockMap.get(skuno)));
            } else{
                skustock.put(skuno, 0);
            }
        }
        return skustock;
    }

    public static void main(String[] args) throws Exception {
    	//加载spring
    	loadSpringContext();
		
        if(StringUtils.isBlank(host)||StringUtils.isBlank(app_key)||StringUtils.isBlank(app_secret)){
            logger.error("参数错误，无法执行更新库存");
        }

        StockImp luisaworld = (StockImp)factory.getBean("luisaworldstock");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		logger.info("luisaworldstock-stock更新数据库开始");
		try{
			
			luisaworld.updateProductStock(host,app_key,app_secret,"2015-01-01 00:00",format.format(new Date()));
			
		}catch(Exception ex){
			logger.error(ex);
			ex.printStackTrace();
		}
		
		logger.info("luisaworldstock更新数据库结束");
		System.exit(0);
//    	StockImp s = new StockImp();
//    	s.grabStock(null);
    }
}
