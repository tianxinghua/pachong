package com.shangpin.iog.brunarosso20.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.sop.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.brunarosso20.schedule.AppContext;
import com.shangpin.iog.brunarosso20.util.FTPUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by usr on 2015/9/14.
 */
@Component("brunarosso20stock")
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
    
    private static String ip = null;
    private static int port = 21;
    private static String usr = null;
    private static String password = null;
    private static String localPath = null;
    private static String startDate = null;
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        ip = bdl.getString("ip");
        port = Integer.parseInt(bdl.getString("port"));
        usr = bdl.getString("usr");
        password = bdl.getString("password");
        localPath = bdl.getString("localPath");
        startDate = bdl.getString("startDate");
    }
    @Override
    public Map<String, Integer> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
    	
    	//获取库存元数据
		Map<String,Integer> stockMap = new HashMap<String, Integer>();
        
        try{
        	//业务实现
        	logger.info("开始下载库存文件=========");
        	
        	//只更新最新的文件
        	if(StringUtils.isBlank(startDate)){
        		
        		String fileName = FTPUtils.downFile(ip, port, usr, password, "/public/stockftp", localPath);
            	logger.info("下载的库存文件名称是======"+fileName); 
            	if(StringUtils.isNotBlank(fileName)){
            		handleFile(stockMap, fileName);
            	}else{
            		logger.error("文件下载失败"); 
            		return new HashMap<String, Integer>();
            	}
        	}else{
        		
        		//更新从指定的时间之后的所有文件
        		List<String> files = FTPUtils.bulkDownload(ip, port, usr, password, "/public/stockftp", localPath, startDate);
        		if(null != files && files.size()>0){
        			for(String fileName : files){
        				try {
        					handleFile(stockMap, fileName);
						} catch (Exception e) {
							e.printStackTrace();
						}
        			} 
        		}else{
        			logger.error("批量下载文件失败"); 
        			return new HashMap<String, Integer>();
        		}
        	}
        	
        
        }catch(Exception ex){
        	ex.printStackTrace();
        	logError.error(ex);
        }
      
        return stockMap;
    }

    /**
     * 处理文件，获取供货商skuId-库存map
     * @param stockMap
     * @param fileName
     * @throws JDOMException
     * @throws IOException
     */
	private void handleFile(Map<String, Integer> stockMap, String fileName)
			throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(new File(localPath+File.separator+fileName));
		Element foo =doc.getRootElement();                
		for (Element element:foo.getChildren()){                    
			try {
				String size =  element.getChildText("MM_TAGLIA").replaceAll("½", "+");
				stockMap.put(element.getChildText("ID_ARTICOLO")+"-"+size, Integer.parseInt(element.getChildText("ESI")));
			} catch (Exception e) {
				e.printStackTrace();
				logError.error(e.getMessage()); 
			}
			
		}
	}

    public static void main(String[] args) throws Exception {
    	//加载spring
        loadSpringContext();    
//    	StockImp s = new StockImp();
//    	s.grabStock(null);
    }
}
