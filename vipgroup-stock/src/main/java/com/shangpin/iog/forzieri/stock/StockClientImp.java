package com.shangpin.iog.forzieri.stock;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.dto.TokenDTO;
import com.shangpin.iog.forzieri.stock.dto.Product;
import com.shangpin.iog.forzieri.stock.dto.ResponseToken;
import com.shangpin.iog.forzieri.stock.schedule.AppContext;
import com.shangpin.iog.service.TokenService;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.*;

/**
 * Created by monkey on 2015/10/20.
 */
@Component("aa")
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
    /*static {
        if (null == bdl)
            bdl = ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        clientId = bdl.getString("clientId");
        clientsecret = bdl.getString("clientsecret");
        tokenurl = bdl.getString("tokenurl");
        producturl = bdl.getString("producturl");
    }*/
    @Autowired
    TokenService tokenService;

    @Override
    public Map<String, String> grabStock(Collection<String> skuNo)
            throws ServiceException, Exception {
        TokenDTO tokenDTO = tokenService.findToken("111111");
        accessToken = tokenDTO.getAccessToken();
        //  refreshToken = tokenDTO.getRefreshToken();
        logger.info("accessToken的值是"+accessToken);
        logger.info("refreshToken的值是"+refreshToken);
        HttpClient httpClient = new HttpClient();
        Gson gson = new Gson();
        Gson gson1 = new Gson();
        Map<String, String> skustock = new HashMap<>(skuNo.size());
        Iterator<String> it = skuNo.iterator();
        while (it.hasNext()) {
            String skuId = it.next();
            TokenDTO tokenDTO1 = tokenService.findToken("111111");
            String accessToken1 = tokenDTO1.getAccessToken();
            GetMethod getMethod = new GetMethod("http://open.vipgroup.com.hk/api/stock?uid=087&token="+accessToken1+"&lang=1&productId="+skuId+"&cat=&pageSize=100&pageNum=1");
            int httpCode = httpClient.executeMethod(getMethod);
            String pro1 = getMethod.getResponseBodyAsString();
            //判断httpCode，404商品未找到...401 accessToken过期,200得到数据
            if (httpCode==200) {
                if(!pro1.equals("null")){
                    String pro=pro1.substring(1,pro1.length()-1);
                    Product product = gson.fromJson(pro, Product.class);
                    skustock.put(skuId, product.getStock());
                }
                else{
                    //access_token过期
                    //刷新Token,更改刷新后的数据库,
                    // 存入map
                    logger.info("accessToken过期");
                    String  response=GetToken.httpClientTest();
                    ResponseToken responseDTO = gson.fromJson(response, ResponseToken.class);
                    String tokenNew =responseDTO.getToken();
                    int tokenCode=  responseDTO.getErrorCode();
                    if (tokenCode==0) {
                        tokenDTO.setAccessToken(tokenNew);
                        tokenDTO.setCreateDate(new Date());
                        tokenService.refreshToken(tokenDTO);
                        getMethod = new GetMethod("http://open.vipgroup.com.hk/api/stock?uid=087&token="+tokenNew+"&lang=1&productId="+skuId+"&cat=&pageSize=100&pageNum=1");
                        httpCode = httpClient.executeMethod(getMethod);
                        logger.info("httpCode的值是"+httpCode);
                        if (httpCode==200) {
                            String realSku2 = getMethod.getResponseBodyAsString();
                            realSku2= realSku2.substring(1,realSku2.length()-1);
                            Product  product = gson.fromJson(realSku2, Product.class);
                            skustock.put(skuId, product.getStock());
                        }else if (httpCode==404){
                            // 产品未找到
                            logger.info(skuId+"产品未找到");
                            skustock.put(skuId, "0");
                        }else{
                            //服务器错误
                            loggerError.error(skuId+"服务器错误1111111111111111111111");
                            System.out.println(skuId+"服务器错误"+httpCode);
                        }
                    }else if(tokenCode==2){
                        tokenDTO.setAccessToken(tokenNew);
                        tokenDTO.setCreateDate(new Date());
                        tokenService.refreshToken(tokenDTO);
                        loggerError.error(skuId+"Error!You've already requested a token!Please try again after at least one minute."+getMethod.getResponseBodyAsString());
                        System.out.println("Error!You've already requested a token!Please try again after at least one minute.");
                        break;
                    }else{
                        loggerError.error(skuId+"刷新token错误"+getMethod.getResponseBodyAsString());
                        //System.out.println(skuId+"刷新token错误"+tokenMethod+getMethod.getResponseBodyAsString());
                    }
                }



            }
            else if (httpCode==404){
                // 产品未找到
                logger.info(skuId+"产品未找到");
                skustock.put(skuId, "0");

            }/*else if (pro==null) {
                //access_token过期
                //刷新Token,更改刷新后的数据库,
                // 存入map
                logger.info("accessToken过期");
                String  response=GetToken.httpClientTest();
                ResponseToken responseDTO = gson.fromJson(response, ResponseToken.class);
                String tokenNew =responseDTO.getToken();
                int tokenCode=  responseDTO.getErrorCode();
                if (tokenCode==0) {
                    tokenDTO.setAccessToken(tokenNew);
                    tokenDTO.setCreateDate(new Date());
                    tokenService.refreshToken(tokenDTO);
                    getMethod = new GetMethod("http://open.vipgroup.com.hk/api/stock?uid=087&token="+tokenNew+"&lang=1&productId="+skuId+"&cat=&pageSize=100&pageNum=1");
                    httpCode = httpClient.executeMethod(getMethod);
                    logger.info("httpCode的值是"+httpCode);
                    if (httpCode==200) {
                        String realSku2 = getMethod.getResponseBodyAsString();
                        Product  product = gson.fromJson(realSku2, Product.class);
                        skustock.put(skuId, product.getStock());
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
                    //System.out.println(skuId+"刷新token错误"+tokenMethod+getMethod.getResponseBodyAsString());
                }
            }*/else{
                //服务器错误
                loggerError.error(skuId+"服务器错误2222222222222222222222");
                System.out.println(skuId+"服务器错误"+httpCode);
            }
        }
        return skustock;
    }

    public static void main(String[] args)  {
        //加载spring
        loadSpringContext();
        HttpClient httpClient = new HttpClient();
        //拉取数据

       StockClientImp impl =(StockClientImp)factory.getBean("aa");
       // StockClientImp impl = new StockClientImp();
        List<String> skuNo = new ArrayList<>();
        skuNo.add("00-10550072521");
        skuNo.add("0010620053311");
        skuNo.add("02-02-1139-0");

     /* skuNo.add("443650-2111110404812");
      skuNo.add("443650-2004244214498");*/
        Map returnMap = null;
        try {
            returnMap = impl.grabStock(skuNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("test return size is "+returnMap.keySet().size());
        for(Object key: returnMap.keySet()) {
            System.out.println(key+" test return value is " + returnMap.get(key));
        }




    }


}
