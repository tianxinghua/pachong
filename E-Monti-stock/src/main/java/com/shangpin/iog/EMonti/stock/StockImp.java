package com.shangpin.iog.EMonti.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.EMonti.schedule.AppContext;
import com.shangpin.iog.EMonti.util.MagentoService;
import com.shangpin.iog.EMonti.util.MagentoServiceLocator;
import com.shangpin.iog.EMonti.util.PortType;
import com.shangpin.iog.common.utils.logger.LoggerUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by lubaijiang on 2015/9/14.
 */
@Component("E-Montistock")
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
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
    }
    @Override
    public Map<String,String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
    	
    	//获取库存元数据
    	Map<String, String> skustock = new HashMap<String, String>();
		Map<String,String> stockMap = new HashMap<String, String>();
        
        try{
        	//业务实现
        	MagentoService magentoService = new MagentoServiceLocator();		
			PortType portType =magentoService.getPort();
			String login = portType.login("soapws", "soap22WS!@#");
			logger.info("===========开始获取产品信息================");
			Map<String,Object> paramatt = new HashMap<String,Object>();
			paramatt.put("setId", "4");				
			HashMap[] product_attribute = (HashMap[])portType.call(login, "product_attribute.list",paramatt);
			List filters = new ArrayList<>();
			for(HashMap map : product_attribute){
				filters.add(map.get("attribute_id"));
			}							
			@SuppressWarnings("rawtypes")
			HashMap[] objects = (HashMap[])portType.call(login, "catalog_product.list",null);
			System.out.println(objects.length); 
			for(Map map : objects){	
				try {
					System.out.println("product_id==="+map.get("product_id").toString()); 
					Map<String,Object> param = new HashMap<String,Object>();
					param.put("product",map.get("product_id").toString());
					param.put("storeView", "");
					param.put("attributes", filters.toArray());
					param.put("productIdentifierType", "ID");
					HashMap product = (HashMap)portType.call(login, "catalog_product.info", param);
					
					//获取价格和库存
					String skuStock = "";
					Map stockMapParam = new HashMap();
					String[] aaa = new String[1];
					aaa[0] = map.get("sku").toString();
					stockMapParam.put("productIds", aaa);
					HashMap[] oo = (HashMap[])portType.call(login, "cataloginventory_stock_item.list", stockMapParam);
					for(HashMap stock : oo){
						stockMap.put(map.get("sku").toString(),stock.get("qty").toString());
//						System.out.println(stockMap.toString());
					}
				}catch(Exception e){
					e.printStackTrace();
				}				
			}
			logger.info("获取的供货商的map大小是========="+stockMap.size());
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
        logger.info("返回的map的大小是======="+skustock.size()); 
        return skustock;
    }

    public static void main(String[] args) throws Exception {
//    	StockImp s = new StockImp();
//    	s.grabStock(null);
    	//加载spring
        loadSpringContext();       
    }
}
