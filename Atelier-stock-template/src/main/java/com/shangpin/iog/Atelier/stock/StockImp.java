package com.shangpin.iog.Atelier.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.Atelier.dto.AtelierSku;
import com.shangpin.iog.Atelier.schedule.AppContext;
import com.shangpin.iog.common.utils.logger.LoggerUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lubaijiang on 2015/9/14.
 */
@Component("Atelier-templatestock")
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
    private static String api_url = "";
	private static String sku_interface = "";
	private static String userName = "";
	private static String password = "";
	
	private static String savePath = "";
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        
        api_url = bdl.getString("api_url");
		sku_interface = bdl.getString("sku_interface");
		userName = bdl.getString("userName");
		password = bdl.getString("password");
		savePath = bdl.getString("savePath");
    }
    @Override
    public Map<String,String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
    	
    	//获取库存元数据
    	Map<String, String> skustock = new HashMap<String, String>();
		Map<String,String> stockMap = new HashMap<String, String>();
        
        try{
        	//业务实现
        	List<AtelierSku> skuLists =  handleSkuData();
        	for(AtelierSku atelierSku : skuLists){
        		try {
					stockMap.put(atelierSku.getSpuId()+"-"+atelierSku.getBarcode(), atelierSku.getStock()); 
				} catch (Exception e) {
					e.printStackTrace(); 
				}        		
        	}
        
        }catch(Exception ex){
        	logError.error(ex);
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
    
    /**
	 * 处理sku数据，返回AtelierSku集合
	 * @return
	 */
	private List<AtelierSku> handleSkuData(){
		String skuData = getInterfaceData(sku_interface,new OutTimeConfig(1000*60*60,1000*60*5,1000*60*60));
		if(HttpUtil45.errorResult.equals(skuData)){
			return null;
		}else{
			List<AtelierSku> skuLists = new ArrayList<AtelierSku>();
			String[] skuStrings = skuData.split("\\r\\n");
			for (int i = 1; i < skuStrings.length; i++) {
				try {
					if (StringUtils.isNotBlank(skuStrings[i])) {
						String data = "";
						if (i==1) {
						  data =  skuStrings[i].split("\\n")[1];
						}else {
						  data = skuStrings[i];
						}
						String[] skuArr = data.replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;","").split(";");
						AtelierSku atelierSku = new AtelierSku();
						atelierSku.setSpuId(skuArr[0]);
						atelierSku.setSize(skuArr[1]);
						atelierSku.setStock(skuArr[2]);
						atelierSku.setBarcode(skuArr[5]); 
						skuLists.add(atelierSku);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
			logger.info("sku的总数是======="+skuLists.size()); 
			return skuLists;
		}
	}
	
	/**
	 * 下载接口文件并返回数据
	 * @param api_interface
	 * @param outTimeConfig
	 * @return
	 */
	private String getInterfaceData(String api_interface,OutTimeConfig outTimeConfig) {
		logger.info("开始拉取"+api_interface+"的数据......"); 
		String data = HttpUtil45.postAuth(api_url+api_interface,null,outTimeConfig,userName,password);		
		int i=0;
		while((StringUtils.isBlank(data) || HttpUtil45.errorResult.equals(data)) && i<10){ 
			try {
				Thread.sleep(1000*3);
				data = HttpUtil45.postAuth(api_url+api_interface,null,outTimeConfig,userName,password);				
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				i++;
			}		        	
		}
		logger.info("拉取"+api_interface+"用了=="+i+"次"); 
		saveData(api_interface+".txt",data);
		return data;
	}	
	
	/**
	 * 保存数据
	 * @param name 文件名
	 * @param data 要保存的数据
	 */
	private void saveData(String name,String data){
    	try {
    		File file = new File(savePath+File.separator+name);
    		if (!file.exists()) {
    			try {
    				file.getParentFile().mkdirs();
    				file.createNewFile();
    				
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    		FileWriter fwriter = null;
    		try {
    			fwriter = new FileWriter(savePath+File.separator+name);
    			fwriter.write(data);
    		} catch (IOException ex) {
    			ex.printStackTrace();
    		} finally {
    			try {
    				fwriter.flush();
    				fwriter.close();
    			} catch (IOException ex) {
    				ex.printStackTrace();
    			}
    		}
		} catch (Exception e) {
			e.printStackTrace();			
		}
    	
    }

    public static void main(String[] args) {
    	//加载spring
        loadSpringContext();       
    }
}
