package com.shangpin.iog.lungolivigno.stock;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.lungolivigno.dto.LoginDTO;
import com.shangpin.iog.lungolivigno.dto.Pagination;
import com.shangpin.iog.lungolivigno.dto.RequestStokDTO;
import com.shangpin.iog.lungolivigno.dto.ResponseDTO;
import com.shangpin.iog.lungolivigno.dto.Result;
import com.shangpin.iog.lungolivigno.dto.Sizes;
import com.shangpin.iog.lungolivigno.dto.User;
import com.shangpin.iog.lungolivigno.schedule.AppContext;
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
@Component("lungolivignostock")
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
    
    private static String url_login = null;
    private static String url_getStock = null;
    
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        url_getStock = bdl.getString("url_getStock");
        url_login = bdl.getString("url_login");
    }
    @Override
    public Map<String,String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
    	
    	//获取库存元数据
    	Map<String, String> skustock = new HashMap<String, String>();
		Map<String,String> stockMap = new HashMap<String, String>();
        
        try{
        	//业务实现
        	OutTimeConfig outTimeConf = new OutTimeConfig(1000*5, 1000*60 * 5, 1000*60 * 5);
			User user = new User();
			user.setUserName("ll_web");
			user.setPassword("lng.r45h");			
			String jsonValue = new Gson().toJson(user); 			
			String result = HttpUtil45.operateData("post", "json", url_login, outTimeConf, null, jsonValue, "", "");
			logger.info("login result==="+result);
			int i = 0;
			while(HttpUtil45.errorResult.equals(result) && i<100){
				result = HttpUtil45.operateData("post", "json", url_login, outTimeConf, null, jsonValue, "", "");
				i++;
			}
			if(!HttpUtil45.errorResult.equals(result)){
				logger.info("登录了第 "+i+"次登录成功");
				LoginDTO LoginDTO = new Gson().fromJson(result, LoginDTO.class);
				String sessionId = LoginDTO.getResult();
				String urlStock = url_getStock+sessionId;
				Set<String> set = new HashSet<String>();
				for(String skuid : skuNo){
					try {
						set.add(skuid.substring(0, skuid.indexOf("-")));
					} catch (Exception e) {
						
					}									
				}
				
				Pagination pagination = new Pagination();
				pagination.setCount(100000);
				pagination.setOffset(1);
				RequestStokDTO  requestStokDTO = new RequestStokDTO();
				requestStokDTO.setSku(set); 
				requestStokDTO.setWithDetail(true);
				requestStokDTO.setPagination(pagination); 
				String requestStokStr = new Gson().toJson(requestStokDTO);
				System.out.println(urlStock);
				String stockResult = HttpUtil45.operateData("post", "json", urlStock, outTimeConf, null, requestStokStr, "", "");
				ResponseDTO responseDTO = new Gson().fromJson(stockResult, ResponseDTO.class);
//				System.out.println(stockResult); 
				for(Result resultDto : responseDTO.getResult()){
					try {
						for(Sizes size :resultDto.getSizes()){
							try {
								if(size.getQty()>0){
									stockMap.put(resultDto.getSku()+"-"+size.getSizeIndex(), size.getQty()+"");									
									logger.info(resultDto.getSku()+"-"+size.getSizeIndex()+"--------------"+resultDto.getStoreCode()+" Qty:"+size.getQty());
								}
							} catch (Exception e) {
								// TODO: handle exception
							}
						}
					} catch (Exception e) {
						logError.error(e); 
					}
					
				}
				
			}else{
				logger.info("##################登录失败##################################");
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

    public static void main(String[] args) throws Exception {
    	//加载spring
        loadSpringContext();    
//    	StockImp s = new StockImp();
//    	List<String> ll = new ArrayList<String>();
//    	ll.add("708074982162038-1");
//    	s.grabStock(ll);
    }
}
