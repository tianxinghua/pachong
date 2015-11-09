package com.shangpin.iog.forzieri.stock;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.dto.TokenDTO;
import com.shangpin.iog.forzieri.stock.dto.NewAccessToken;
import com.shangpin.iog.forzieri.stock.dto.RealStock;
import com.shangpin.iog.service.TokenService;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by monkey on 2015/10/20.
 */
@Component("forzieristock")
public class StockClientImp extends AbsUpdateProductStock {
	private static Logger logger = Logger.getLogger("info");
	private static Logger loggerError = Logger.getLogger("error");
	
	 private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static String url;
	private static String clientId;
	private static String clientsecret;
	private static String username;
	private static String password;
	private static String accessToken;
	private static String refreshToken;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("param");
		supplierId = bdl.getString("supplierId");
		url = bdl.getString("url");
		clientId = bdl.getString("clientId");
		clientsecret = bdl.getString("clientsecret");
		username = bdl.getString("username");
		password = bdl.getString("password");
	}
	@Autowired
	TokenService tokenService;
	
	@Override
	public Map<String, String> grabStock(Collection<String> skuNo)
			throws ServiceException, Exception {
		TokenDTO tokenDTO = tokenService.findToken(supplierId);
		 accessToken = tokenDTO.getAccessToken();
		 refreshToken = tokenDTO.getRefreshToken();
		
		HttpClient httpClient = new HttpClient();
		Gson gson = new Gson();
		Map<String, String> skustock = new HashMap<>(skuNo.size());
		Iterator<String> it = skuNo.iterator();
		while (it.hasNext()) {
			String skuId = it.next();
			// 用access_token 和 skuid获取实时数据 ，得到stock
//			GetMethod getMethod = new GetMethod("https://api.forzieri.com/test/products/"+skuId);//测试
			GetMethod getMethod = new GetMethod("https://api.forzieri.com/v2/products/"+skuId);
			getMethod.setRequestHeader("Authorization", "Bearer "+accessToken);
			
			int httpCode = httpClient.executeMethod(getMethod);
			//判断httpCode，404商品未找到...401 accessToken过期,200得到数据
			if (httpCode==200) {
				String realSku = getMethod.getResponseBodyAsString();
				RealStock realStock = gson.fromJson(realSku, RealStock.class);
				skustock.put(skuId, realStock.getData().getQty());
			}else if (httpCode==404){
			// 产品未找到	
				logger.info(skuId+"产品未找到");
				skustock.put(skuId, "0");
				
			}else if (httpCode==401) {
				//access_token过期
				//刷新Token,更改刷新后的数据库,
				// 存入map
				logger.info("accessToken过期");
//				PostMethod postMethod = new PostMethod("https://api.forzieri.com/test/oauth/token");//测试
				PostMethod postMethod = new PostMethod("https://api.forzieri.com/v2/oauth/token");
				postMethod.addParameter("grant_type", "refresh_token");
				postMethod.addParameter("client_id", clientId);
				postMethod.addParameter("client_secret", clientsecret);
				postMethod.addParameter("refresh_token", refreshToken);
				int executeMethod = httpClient.executeMethod(postMethod);
				if (executeMethod==200) {
					
					NewAccessToken newAccessToken = gson.fromJson(postMethod.getResponseBodyAsString(), NewAccessToken.class);
					accessToken = newAccessToken.getAccess_token();
					refreshToken = newAccessToken.getRefresh_token();
					tokenDTO.setAccessToken(accessToken);
					tokenDTO.setRefreshToken(refreshToken);
					tokenDTO.setCreateDate(new Date());
					tokenDTO.setExpireTime(newAccessToken.getExpires_in());
					tokenService.refreshToken(tokenDTO);
					
					getMethod.setRequestHeader("Authorization", "Bearer "+accessToken);
					httpCode = httpClient.executeMethod(getMethod);
					if (httpCode==200) {
						String realSku2 = getMethod.getResponseBodyAsString();
						RealStock realStock2 = gson.fromJson(realSku2, RealStock.class);
						skustock.put(skuId, realStock2.getData().getQty());
					}else if (httpCode==404){
					// 产品未找到	
						logger.info(skuId+"产品未找到");
						skustock.put(skuId, "0");
					}else{
						//服务器错误
						loggerError.error(skuId+"服务器错误");
						System.out.println(skuId+"服务器错误"+httpCode);
					}
				}else{
					loggerError.error(skuId+"刷新token错误"+getMethod.getResponseBodyAsString());
					System.out.println(skuId+"刷新token错误"+executeMethod+getMethod.getResponseBodyAsString());
				}
			}else{
				//服务器错误
				loggerError.error(skuId+"服务器错误");
				System.out.println(skuId+"服务器错误"+httpCode);
			}
		}
		return skustock;
	}
	
	public static void main(String[] args) throws Exception {
		AbsUpdateProductStock stockImp = new StockClientImp();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		logger.info("forzieri更新数据库开始");
		try {
			stockImp.updateProductStock(supplierId, "2015-01-01 00:00", format.format(new Date()));
		} catch (Exception e) {
			logger.info("forzieri更新数据库异常"+e.toString());
		}
		logger.info("forzieri更新数据库结束");
		System.exit(0);
	}
	
//	public static void main(String[] args) throws Exception {
//    	//加载spring
//        loadSpringContext();
//        //拉取数据
//        StockClientImp impl =(StockClientImp)factory.getBean("forzieristock");
//      List<String> skuNo = new ArrayList<>();
//      skuNo.add("ka340414-001-00");
//      skuNo.add("ka340414-002-00");
//      skuNo.add("443636-2100657478882");
//      skuNo.add("443650-2111110404812");
//      skuNo.add("443650-2004244214498");
//      Map returnMap = impl.grabStock(skuNo);
//      System.out.println("test return size is "+returnMap.keySet().size());
//      for(Object key: returnMap.keySet()) {
//          System.out.println(key+" test return value is " + returnMap.get(key));
//      }
//	}
}
