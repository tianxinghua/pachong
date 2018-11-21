package com.shangpin.iog.vipgroup.stock;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.vipgroup.stock.dto.Product;
import com.shangpin.iog.vipgroup.stock.dto.SupplierToken;
import com.shangpin.iog.vipgroup.stock.dto.TokenResp;
import com.shangpin.iog.vipgroup.stock.schedule.AppContext;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.util.*;

/**
 * Created by monkey on 2015/10/20.
 */
@Component("vipgroup-stock")
public class StockClientImp extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");

    private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

    private static ResourceBundle bdl = null;
    private static String supplierId = "",tokenUrl="",stockUrl="";
    Gson gson = new Gson();

    static {
        if (null == bdl){
            bdl = ResourceBundle.getBundle("conf");
        }
        supplierId = bdl.getString("supplierId");
        tokenUrl = bdl.getString("tokenUrl");
        stockUrl = bdl.getString("stockUrl");
    }
    @Override
    public Map<String, String> grabStock(Collection<String> skuNo) throws Exception {
        String token = "";
        List<Product> productList = new ArrayList<Product>();
        HttpClient httpClient = new HttpClient();
        Map<String, String> skustock = new HashMap<>(skuNo.size());
        Iterator<String> it = skuNo.iterator();
        while (it.hasNext()) {
            SupplierToken supplierToken = queryToken(supplierId);
            if (null != supplierToken){
                token = supplierToken.getAccessToken();
            }
            String skuId = it.next();
            skuId = URLEncoder.encode(skuId);
            GetMethod getMethod = new GetMethod(stockUrl+"?uid=087&productId="+skuId+"&token="+token+"&lang=0&pageSize=1&pageNum=1");
            int httpCode = httpClient.executeMethod(getMethod);
            String stockRespJson = getMethod.getResponseBodyAsString();
            if(null != stockRespJson && !stockRespJson.equals("")){
                productList = gson.fromJson(stockRespJson, new TypeToken<List<Product>>() {
                }.getType());
            }
            if (httpCode==200) {
                if( null != productList && productList.size() > 0 ){
                   skustock.put(skuId, productList.get(0).getStock());//集合定义是因为查询所有商品的库存时需要
                }
            }else{
                //服务器错误
                loggerError.error(skuId+"服务器错误");
                System.out.println(skuId+"服务器错误"+httpCode);
            }
        }
        return skustock;
    }

    /**
     *  根据 supplierId 查token
     * @param supplierId
     * @return
     */
    public SupplierToken queryToken(String supplierId){
        SupplierToken supplierTokenDTO = new SupplierToken();
        TokenResp tokenResp = new TokenResp();
        Map<String, String> param = new HashMap<String, String>();
        param.put("supplierId", supplierId);
        try {
            String result = HttpUtil45.operateData("get", "", tokenUrl, new OutTimeConfig(1000 * 60 * 3,
                    1000 * 60 * 30, 1000 * 60 * 30), param, "", "", "");
            System.out.println("根据 supplierId 查token：" + result);
            logger.info("根据 supplierId 查token：" + result);
            tokenResp = gson.fromJson( result, TokenResp.class);
            String data = tokenResp.getData();
            if (null != tokenResp && !tokenResp.equals("") && tokenResp.getCode().equals("200")){
                if( null != data && !data.equals("") ){
                    supplierTokenDTO = gson.fromJson(data,SupplierToken.class);
                    logger.info("数据库存在supplierId为 "+supplierId+" 的token" + data);
                    System.out.println("数据库存在supplierId为 "+supplierId+" 的token" + data);
                }else {
                    supplierTokenDTO = null;
                    logger.info("数据库不存在supplierId为 "+supplierId+" 的token" + data);
                    System.out.println("数据库不存在supplierId为 "+supplierId+" 的token" + data);
                }
            }else {
                supplierTokenDTO = null;
                logger.info("根据 supplierId 查token失败：" + tokenResp.getMessage());
                System.out.println("根据 supplierId 查token失败：" + tokenResp.getMessage());
            }
        } catch (ServiceException e) {
            logger.error("根据 supplierId 查token异常：" + e.getMessage());
            e.printStackTrace();
        }
        return supplierTokenDTO;
    }
    //测试
    public static void main(String[] args)  {
        //加载spring
        loadSpringContext();
        //拉取数据
        StockClientImp impl =new StockClientImp();

        List<String> skuNo = new ArrayList<>();
        skuNo.add("00-10550072521");
        skuNo.add("1002QZ(AC)");
        skuNo.add("02-02-1");

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
