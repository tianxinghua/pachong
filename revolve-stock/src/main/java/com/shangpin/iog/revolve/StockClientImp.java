package com.shangpin.iog.revolve;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.revolve.stock.dto.Item;
import com.shangpin.iog.revolve.stock.dto.ProductDTO;
import com.shangpin.iog.revolve.stock.schedule.AppContext;
import com.shangpin.iog.revolve.stock.sepStrategy.ISepStrategy;
import com.shangpin.iog.revolve.stock.sepStrategy.SepStrategyContext;
import com.shangpin.iog.revolve.stock.util.CVSUtil;
import com.shangpin.iog.revolve.stock.util.Csv2DTO;

/**
 * Created by sunny on 2015/9/10.
 */
@Component("revolveStock")
public class StockClientImp extends AbsUpdateProductStock{
    private static Logger logger = Logger.getLogger("info");    
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

    private static ResourceBundle bdl=null;
    private static String supplierId;
    private static String url;
    private static String filepath;
//    private static String savePath = null;

    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        url = bdl.getString("url");
        filepath = bdl.getString("filepath");
    }
    

    @Override
    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
    	String skuId ="";    	
        Map<String, String> skustock = new HashMap<>();
        Map<String, String> skuData = new HashMap<>();        
        
//		Csv2DTO csv2 = new Csv2DTO();
//		//第一个为size and stock
//		String[] needColsNo = new String[]{"","","2","","","","","","","","","","","","","","","","20","",""};
//		//策略组
//		String[] strategys = new String[]{"","","","","","","","","","","","","","","","","","","","",""};
//		ISepStrategy[] iSepStrategies = new SepStrategyContext().operate(strategys);
//		List<ProductDTO> list = csv2.toDTO(url, filepath, sep, needColsNo, iSepStrategies, ProductDTO.class);
//        
//        for (ProductDTO dto : list) {
//        	skuData.put(dto.getSkuId(), dto.getStock());
//		}
        logger.info("===============开始下载库存文件==============");
        OutTimeConfig outTimeConf = new OutTimeConfig(1000*60*10, 1000*60*60, 1000*60*60);
		String data = HttpUtil45.get(url, outTimeConf, null);
		int loop = 0;
		while(HttpUtil45.errorResult.equals(data) && loop <100){
			logger.info("==============="+loop+"==============");
			Thread.sleep(1000*3); 
			data = HttpUtil45.get(url, outTimeConf, null);
			loop ++;
		}
		
		String path = save("products.txt",data);
		
		List<ProductDTO> items = CVSUtil.readCSV(path, ProductDTO.class, '\t');
		logger.info("csv转换items.size=========="+items.size());
		for(ProductDTO item :items){
			try {
				skuData.put(item.getId(),item.getSellableqty());
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}        
        Iterator<String> it = skuNo.iterator(); 
        
        while (it.hasNext()) {
            skuId = it.next();
            if (skuData.containsKey(skuId)) {
            	skustock.put(skuId, skuData.get(skuId));
			}else {
				skustock.put(skuId, "0");
			}
        }
        return skustock;
    }
    
    public String save(String name,String data){
    	String path = filepath+File.separator+name;
    	try {
    		File file = new File(path);
//        	File file = new File("E://"+name);
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
    			fwriter = new FileWriter(filepath+File.separator+name);
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
    	return path;
    	
    }

    public static void main(String[] args) throws Exception {
    	Collection<String> skuNo = null;
		new StockClientImp().grabStock(skuNo );
    	//加载spring
//        loadSpringContext();
//        StockClientImp stockImp = (StockClientImp)factory.getBean("bagheeraStock");
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//        logger.info("bagheera更新数据库开始");
//        try {
//        	stockImp.updateProductStock(supplierId,"2015-01-01 00:00",format.format(new Date()));
//		} catch (Exception e) {
//			logger.info("bagheera更新数据库出错"+e.toString());
//		}
//        logger.info("bagheera更新数据库结束");
//        System.exit(0);

    }

}
