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

import java.io.IOException;
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
    private static String clientId;
    private static String clientsecret;
    private static String accessToken;
    private static String refreshToken;
    private static String tokenurl;
    private static String producturl;
    static {
        if (null == bdl)
            bdl = ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        clientId = bdl.getString("clientId");
        clientsecret = bdl.getString("clientsecret");
        tokenurl = bdl.getString("tokenurl");
        producturl = bdl.getString("producturl");
    }
    @Autowired
    TokenService tokenService;

    @Override
    public Map<String, String> grabStock(Collection<String> skuNo)
            throws ServiceException, Exception {
        TokenDTO tokenDTO = tokenService.findToken(supplierId);
        accessToken = tokenDTO.getAccessToken();
        refreshToken = tokenDTO.getRefreshToken();
        logger.info("accessToken的值是"+accessToken);
        logger.info("refreshToken的值是"+refreshToken);
        HttpClient httpClient = new HttpClient();
        Gson gson = new Gson();
        Map<String, String> skustock = new HashMap<>(skuNo.size());
        Iterator<String> it = skuNo.iterator();
        while (it.hasNext()) {
            String skuId = it.next();
            // 用access_token 和 skuid获取实时数据 ，得到stock
//			GetMethod getMethod = new GetMethod("https://api.forzieri.com/test/products/"+skuId);//测试
            GetMethod getMethod = new GetMethod(producturl+"/"+skuId);
            TokenDTO tokenDTO2 = tokenService.findToken(supplierId);
            String Token2 =tokenDTO2.getAccessToken();
            getMethod.setRequestHeader("Authorization", "Bearer "+Token2);

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
              /*  PostMethod ethod = new PostMethod(tokenurl);
                tokenMethod.addParameter("grant_type", "refresh_token");
                tokenMethod.addParameter("client_id", clientId);
                tokenMethod.addParameter("client_secret", clientsecret);
                tokenMethod.addParameter("refresh_token", refreshToken);*/
                TokenDTO tokenDTO3 = tokenService.findToken(supplierId);
                String Token3 =tokenDTO3.getRefreshToken();
                String   tokenurl="https://api.forzieri.com/v2/oauth/token?grant_type=refresh_token&client_id=NTY0MjBmOWZiZjI3OTc5&client_secret=9470b9341606430e3b36871541732865e0f51979&refresh_token="+Token3+"";
                System.out.println("tokenurl的值是=================="+tokenurl);
                GetMethod tokenMethod = new GetMethod(tokenurl);
                int tokenCode = httpClient.executeMethod(tokenMethod);
                logger.info("executeMethod的值是"+tokenCode);
                if (tokenCode==200) {

                    NewAccessToken newAccessToken = gson.fromJson(tokenMethod.getResponseBodyAsString(), NewAccessToken.class);
                    String accessTokenNew = newAccessToken.getAccess_token();
                    String refreshTokenNew = newAccessToken.getRefresh_token();
                    tokenDTO.setAccessToken(accessTokenNew);
                    tokenDTO.setRefreshToken(refreshTokenNew);
                    tokenDTO.setCreateDate(new Date());
                    tokenDTO.setExpireTime(newAccessToken.getExpires_in());
                    tokenService.refreshToken(tokenDTO);
                    TokenDTO tokenDTO1= tokenService.findToken(supplierId);
                    String httpToken=tokenDTO1.getAccessToken();
                    getMethod.setRequestHeader("Authorization", "Bearer "+httpToken);
                    httpCode = httpClient.executeMethod(getMethod);
                    logger.info("httpCode的值是"+httpCode);
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
                        loggerError.error(skuId+"服务器错误1111111111111111111111");
                        System.out.println(skuId+"服务器错误"+httpCode);
                    }
                }else{
                    loggerError.error(skuId+"刷新token错误"+getMethod.getResponseBodyAsString());
                    System.out.println(skuId+"刷新token错误"+tokenMethod+getMethod.getResponseBodyAsString());
                }
            }else{
                //服务器错误
                loggerError.error(skuId+"服务器错误2222222222222222222222");
                System.out.println(skuId+"服务器错误"+httpCode);
            }
        }
        return skustock;
    }

/*
    public static void main(String[] args) {
        //加载spring
        loadSpringContext();
        HttpClient httpClient = new HttpClient();
        String   tokenurl="https://api.forzieri.com/v2/oauth/token?grant_type=refresh_token&client_id=NTY0MjBmOWZiZjI3OTc5&client_secret=9470b9341606430e3b36871541732865e0f51979&refresh_token=1483b4e19ba480fdb47f62dbc712cfa045f806cc";
        GetMethod tokenMethod = new GetMethod(tokenurl);
        try {
            int tokenCode = httpClient.executeMethod(tokenMethod);
            System.out.println(tokenCode+"===================================");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/
    	public static void main(String[] args) throws Exception {
    	//加载spring
        loadSpringContext();
        //拉取数据
        StockClientImp impl =(StockClientImp)factory.getBean("forzieristock");
      List<String> skuNo = new ArrayList<>();
      skuNo.add("dq090318-003-00");
            skuNo.add("dp300018-008-01");
            skuNo.add("dq430318-013-02");
            skuNo.add("dq430318-013-03");
      //skuNo.add("fz580415-005-00");
      skuNo.add("dq090318-003-01");
      Map returnMap = impl.grabStock(skuNo);
      System.out.println("test return size is "+returnMap.keySet().size());
      for(Object key: returnMap.keySet()) {
          System.out.println(key+" test return value is " + returnMap.get(key));
      }
	}
}
