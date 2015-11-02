package com.shangpin.iog.forzieri.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.dto.TokenDTO;
import com.shangpin.iog.forzieri.stock.dto.CsvDTO;
import com.shangpin.iog.forzieri.utils.DownloadAndReadCSV;
import com.shangpin.iog.service.TokenService;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by monkey on 2015/10/20.
 */
public class StockClientImp extends AbsUpdateProductStock {
	private static Logger logger = Logger.getLogger("info");
	private static Logger loggerError = Logger.getLogger("error");

	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static String url;
	private static String clientId;
	private static String clientsecret;
	private static String username;
	private static String password;
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
		TokenDTO tokenDTO = tokenService.findToken(null, supplierId);
		String accessToken = tokenDTO.getAccessToken();
		String refreshToken = tokenDTO.getRefreshToken();
		
		HttpClient httpClient = new HttpClient();
		Map<String, String> skustock = new HashMap<>(skuNo.size());
		Iterator<String> it = skuNo.iterator();
		while (it.hasNext()) {
			String skuId = it.next();
			//TODO 用access_token 和 skuid获取实时数据 ，得到stock
			GetMethod getMethod = new GetMethod("https://api.forzieri.com/test/products/"+skuId);
			getMethod.setRequestHeader("Authorization", "Bearer "+accessToken);
			
			int httpCode = httpClient.executeMethod(getMethod);
			//判断httpCode，404商品未找到...401 accessToken过期
			if (httpCode==404||httpCode==401) {
				//刷新Token,更改刷新后的数据库
				
			}
			//TODO 存入map
		}
		return skustock;
	}
//	public static void main(String[] args) throws Exception {
//		AbsUpdateProductStock stockImp = new StockClientImp();
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//		logger.info("更新数据库开始");
//		stockImp.updateProductStock(supplierId, "2015-01-01 00:00", format.format(new Date()));
//		logger.info("更新数据库结束");
//		System.exit(0);
//	}
	
	public static void main(String[] args) throws Exception {
	}
}
