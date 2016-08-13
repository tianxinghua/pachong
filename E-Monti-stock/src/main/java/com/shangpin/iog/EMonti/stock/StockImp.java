package com.shangpin.iog.EMonti.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.EMonti.schedule.AppContext;
import com.shangpin.iog.EMonti.axisclient.axisclient20.MagentoService;
import com.shangpin.iog.EMonti.axisclient.axisclient20.MagentoServiceLocator;
import com.shangpin.iog.EMonti.axisclient.axisclient20.PortType;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

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
    
    private Map<String,String> stockMap = null;
    
    @Override
    public Map<String,String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
    	
    	//获取库存元数据
    	Map<String, String> skustock = new HashMap<String, String>();
    	stockMap = new HashMap<String, String>();
        
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
			int poolCnt=objects.length/50;
			ExecutorService exe=Executors.newFixedThreadPool(poolCnt/4+1);//相当于跑4遍
			final List<Collection<HashMap>> subSkuNos=subCollection(Arrays.asList(objects)); 
			for(Collection<HashMap> maps : subSkuNos){
				try {
					GrabStockThread t =  new GrabStockThread(portType,login,filters, maps);
					exe.submit(t);					
				} catch (Exception e) {
					logError.error(e.toString()); 
				}				
			}
			exe.shutdown();
			try {
				exe.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
			} catch (Exception e) {
				e.printStackTrace();
			}			
			
			logger.info("获取的供货商的map大小是========="+stockMap.size());
        }catch(Exception ex){
        	logError.error(ex);
        }
        
        try {
        	for (String skuno : skuNo) {
//        		logger.info(skuno+"--------------"); 
            	try {
            		if(stockMap.containsKey(skuno)){
                        skustock.put(skuno, ""+Double.valueOf(stockMap.get(skuno)).intValue());
                    } else{
                        skustock.put(skuno, "0");
                    }
    			} catch (Exception e) {
    				logError.error(e.toString());
    			}
                
            }
		} catch (Exception e) {
			logError.error(e.toString());
		}
        
        logger.info("返回的map的大小是======="+skustock.size()); 
        return skustock;
    }
    
    private List<Collection<HashMap>> subCollection(Collection<HashMap> skuNoSet) {
		int thcnt = 50;
		List<Collection<HashMap>> list=new ArrayList<>();
		int count=0;int currentSet=0;
		for (Iterator<HashMap> iterator = skuNoSet.iterator(); iterator
				.hasNext();) {
			HashMap skuNo = iterator.next();
			if(count==thcnt)
				count=0;
			if(count==0){
				Collection<HashMap> e = new ArrayList<>();
				list.add(e);
				currentSet++;
			}
			list.get(currentSet-1).add(skuNo);
			count++;
		}
		return list;
	}
    
    class GrabStockThread extends Thread{
    	private PortType portType;
    	private String login;
    	private List filters;    	
    	private Collection<HashMap> maps;
    	public GrabStockThread(PortType portType,String login, List filters, Collection<HashMap> maps){
    		this.portType = portType;
    		this.login = login;
    		this.filters = filters;
    		this.maps = maps;
    	}
    	@Override
    	public void run() {
    		for(HashMap map : maps){
    			try {
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
    					logger.info(map.get("sku").toString()+"==========="+stock.get("qty").toString());
    				}
    				//System.out.println("product_id======"+map.get("product_id").toString()); 
    				//logger.info("product_id======"+map.get("product_id").toString());
				} catch (Exception e) {
					logError.error(e.getMessage());
				}
    		}
    	}
    }

	private void run(Map<String, String> stockMap, PortType portType,
			String login, List filters, Map map) throws RemoteException {
		//System.out.println("product_id==="+map.get("product_id").toString()); 
		
		
	}

    public static void main(String[] args) throws Exception {
//    	StockImp s = new StockImp();
//    	s.grabStock(null);
    	//加载spring
        loadSpringContext();       
    }
}
