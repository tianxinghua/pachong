package com.shangpin.iog.gucci.service;


import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.*;
import com.shangpin.iog.gucci.dto.*;
import com.shangpin.iog.utils.HttpResponse;
import com.shangpin.iog.utils.HttpUtil45;
import com.shangpin.iog.utils.HttpUtils;
import com.shangpin.openapi.api.sdk.client.OutTimeConfig;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.Header;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wanner on 2018/6/27
 */
@Component("fetchStockImpl")
public class FetchStockImpl {

    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");

    private static ResourceBundle bdl = null;
    private static String supplierId = "", supplierNo = "", fetchSpProductInfosUrl = "", updateSpMarketPriceUrl = "", pageSize = "";

    private static OutputStreamWriter out = null, priceOut= null;;
    static String splitSign = ",";
    //库存csv 文件存放目录
    private static String filePath = "";

    //theKooples官网地址
    private static String uri = "";

    //有库存
    private static String IN_STOCK ;
    //无库存
    private static String NO_STOCK ;

    // 请求失败的尚品 skuNo 集合
    private static List<SpSkuNoDTO> failedSpSkuNoList = null;

    //  // 所有品类相对路径以及名称
    private static List<String> genderAndNameAndCategoryUris = new ArrayList<>();
    //所有商品的信息
    private static  Map<String,String> productMap =  new HashMap<>();
    //渠道
    private static String channel = "";

    static {
        if (null == bdl) {
            bdl = ResourceBundle.getBundle("conf");
        }
        supplierId = bdl.getString("supplierId");

        supplierNo = bdl.getString("supplierNo");

        fetchSpProductInfosUrl = bdl.getString("fetchSpProductInfosUrl");

        updateSpMarketPriceUrl = bdl.getString("updateSpMarketPriceUrl");

        pageSize = bdl.getString("pageSize");

        filePath = bdl.getString("csvFilePath");

        uri = bdl.getString("uri");

        channel = bdl.getString("channel");

        IN_STOCK = bdl.getString("IN_STOCK");

        NO_STOCK = bdl.getString("NO_STOCK");

        Enumeration<String> keys = bdl.getKeys();
        while(keys.hasMoreElements()){
            String key = keys.nextElement();
            if(key.startsWith("prada-gb-genderAndNameAndCategoryUri")){
                String keyValue = null;
                try {
                    String value = bdl.getString(key);
                    keyValue = new String(value.getBytes("ISO-8859-1"), "GBK");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                genderAndNameAndCategoryUris.add(keyValue);
            }
        }


    }

    private static OutTimeConfig timeConfig = new OutTimeConfig(1000 * 60 * 30, 1000 * 60 * 30, 1000 * 60 * 30);

    /**
     * 拉取 BURBERRY网 商品库存数据
     */
    public void fetchItlyProductStock() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startDateTime = format.format(new Date());
        System.out.println("============拉取poporcelain库存数据开始 " + startDateTime + "=========================");
        logger.info("==============拉取poporcelain库存数据开始 " + startDateTime + "=========================");

        //1. 请求需要更新库存商品 信息接口
        failedSpSkuNoList = new ArrayList<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = simpleDateFormat.format(new Date());

        String temFilePath = filePath + "poporcelain-qty-" + todayStr + ".csv";
        String priceFilePath = filePath + "poporcelain-price-"+todayStr+".csv";
        System.out.println("文件保存目录：" + temFilePath);
        logger.info("文件保存目录：" + temFilePath);
        try {
            out = new OutputStreamWriter(new FileOutputStream(temFilePath, true), "gb2312");
            priceOut = new OutputStreamWriter(new FileOutputStream(priceFilePath, true),"gb2312");
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuffer buffer = new StringBuffer(
                "spSkuNO" + splitSign +
                        "qty" + splitSign
        ).append("\r\n");
        StringBuffer priceBuffer = new StringBuffer(
                "supplier_id" + splitSign +
                        "sp_sku_no" + splitSign +
                        "new price" + splitSign +
                        "old price" + splitSign +
                        "product_url" + splitSign
        ).append("\r\n");
        try {
            out.write(buffer.toString());
            priceOut.write(priceBuffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //定义所有需要的更新库存商品信息List
        List<ProductDTO> productDTOAllList = new LinkedList<>();

        //获取第一页商品数据
        //"2018090402046","The Kooples",1,20,"www.thekooples.com"
        ShangPinPageContent gucciPageContent = getShangPinPageContentByParam(supplierId, "poporcelain", 1, Integer.parseInt(pageSize), channel);

        //ShangPinPageContent gucciPageContent = getShangPinPageContentByParam("2018090402046","The Kooples",1,20,"www.thekooples.com");
        productDTOAllList.addAll(gucciPageContent.getZhiCaiResultList());

        if (gucciPageContent == null) return;
        //总记录数
        Integer total = gucciPageContent.getTotal();
        Integer pageNumber = getPageNumber(total, 20);
        for (int i = 2; i <= pageNumber; i++) {
            ShangPinPageContent temgucciPageContent = getShangPinPageContentByParam(supplierId, "poporcelain", i, Integer.parseInt(pageSize), channel);
            if (temgucciPageContent != null) {
                productDTOAllList.addAll(temgucciPageContent.getZhiCaiResultList());
            } else { //请求失败重新 再次请求
                temgucciPageContent = getShangPinPageContentByParam(supplierId, "poporcelain", i, Integer.parseInt(pageSize), channel);
                if (temgucciPageContent != null) {
                    productDTOAllList.addAll(temgucciPageContent.getZhiCaiResultList());
                }
            }
        }

        logger.info("=====需要更新poporcelain spProduct Size:" + productDTOAllList.size());
        System.out.println("=====需要更新poporcelain spProduct Size:" + productDTOAllList.size());
        //导出尚品库存数据
        exportQtyInfoForProductList(productDTOAllList);

        // 处理 请求失败的 尚品 skuNo 信息
        ProductDTO productDTO = new ProductDTO();
        int failedSpSkuNoSize = failedSpSkuNoList.size();
        for (int i = 0; i < failedSpSkuNoSize; i++) {
            repeatSolveFailedSpSkuNo(failedSpSkuNoList.get(i), productDTO);
        }

        String endtDateTime = format.format(new Date());
        logger.info("===================拉取poporcelain库存数据结束 " + endtDateTime + "=========================");
        System.out.println("=================拉取poporcelain库存数据结束 " + endtDateTime + "=========================");

    }

    /**
     * 获取商品页数
     *
     * @param total    总记录数
     * @param pageSize 每页显示数
     * @return 页数
     */
    public static Integer getPageNumber(Integer total, Integer pageSize) {
        Integer pageNumner = total / pageSize;
        //当余数大于0 的时候 页数加一
        if (total % pageSize > 0) {
            pageNumner++;
        }
        return pageNumner;
    }

    /**
     * 获取需要更新库存商品 分页数据
     *
     * @param brandName 供应商名称
     * @param pageIndex 页码
     * @param pageSize  分页条数
     * @return
     */
    public static ShangPinPageContent getShangPinPageContentByParam(String supplierId, String brandName, Integer pageIndex, Integer pageSize, String channel) {
        //String fetchSpProductInfosUrl = "http://192.168.20.176:8003/supplier-sku/get-product";
        //1. 请求需要更新库存商品 信息接口
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("supplierId", supplierId);
        //jsonObject.put("brandName", brandName);
        jsonObject.put("pageIndex", pageIndex);
        jsonObject.put("pageSize", pageSize);
        jsonObject.put("channel", channel);

        String jsonStr = jsonObject.toString();

        ShangPinPageContent shangPinPageContent = null;
        try {
            String resultJsonStr = HttpUtil45.operateData("post", "json", fetchSpProductInfosUrl, timeConfig, null, jsonStr, null, null);
            //System.out.println("=======resultJsonStr:"+resultJsonStr);
            //logger.info("=======resultJsonStr:"+resultJsonStr);
            JSONObject resultJsonObject = JSONObject.fromObject(resultJsonStr);
            Map<String, Class> keyMapConfig = new HashMap<>();
            keyMapConfig.put("zhiCaiResultList", ProductDTO.class);
            keyMapConfig.put("zhiCaiSkuResultList", SkuDTO.class);
            keyMapConfig.put("content", ShangPinPageContent.class);
            ApiResponseBody apiResponseBody = (ApiResponseBody) JSONObject.toBean(resultJsonObject, ApiResponseBody.class, keyMapConfig);

            if (apiResponseBody != null) {
                shangPinPageContent = (ShangPinPageContent) apiResponseBody.getContent();
            }
            System.out.println();
            System.out.println("获取第 " + pageIndex + "页成功 :" + resultJsonStr);
            logger.info("获取第 " + pageIndex + "页成功 :" + resultJsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shangPinPageContent;
    }


    /**
     * 循环遍历 拉取商品库存信息
     *
     * @param productDTOAllList 商品信息list
     */
    public static void exportQtyInfoForProductList(List<ProductDTO> productDTOAllList) {
        for (ProductDTO productDTO : productDTOAllList) {
            boolean flag = solveProductQty(productDTO);
            if (!flag) {
                repeatSolveFailProductQty(productDTO);
            }
        }
    }

    /**
     * 重复处理 spSkuNO qty 信息
     *
     * @param spSkuNoDTO
     */
    public static void repeatSolveFailedSpSkuNo(SpSkuNoDTO spSkuNoDTO, ProductDTO productDTO) {
        int count = 0;
        while (count > 4) {
            count++;
            Map<String, String> checkProductInfo = getProductQtyInfo(spSkuNoDTO.getSpSkuNo(), productDTO);
            String temQty = checkProductInfo.get("qty");
            if (temQty != null) {
                exportSpSkunoAndQty(spSkuNoDTO.getSpSkuNo(), temQty);
                return;
            }
        }
        //重复请求 失败 做商品 库存置零处理
        exportSpSkunoAndQty(spSkuNoDTO.getSpSkuNo(), NO_STOCK);
    }

    /**
     * 重复请求 处理 商品库存信息 ，最后没有处理成功 直接将 库存置0 处理
     *
     * @param productDTO
     */
    private static void repeatSolveFailProductQty(ProductDTO productDTO) {
        int count = 0;
        while (count < 4) {
            count++;
            boolean temFlag = solveProductQty(productDTO);
            if (temFlag) {
                return;
            }
        }
        //重复请求 失败 做商品 库存置零处理
        setQtyZeroForProduct(productDTO);
    }

    /**
     * 将商品所有 spSkuNO 置零 并导出到 csv
     *
     * @param productDTO
     */
    private static void setQtyZeroForProduct(ProductDTO productDTO) {
        if (productDTO != null) {
            List<SkuDTO> zhiCaiSkuResultList = productDTO.getZhiCaiSkuResultList();
            if (zhiCaiSkuResultList != null && zhiCaiSkuResultList.size() > 0) {
                int size = zhiCaiSkuResultList.size();
                for (int i = 0; i < size; i++) {
                    SkuDTO skuDTO = zhiCaiSkuResultList.get(i);
                    String spSkuNo = skuDTO.getSpSkuNo();
                    if (spSkuNo != null) {
                        exportSpSkunoAndQty(spSkuNo, NO_STOCK);
                    }
                }
            }
        }

    }


    /**
     * 处理 品类 下的 商品列表信息
     *
     *
     */

    public static void getProductUrl(){



           /* org.apache.commons.httpclient.Header[] headers = new org.apache.commons.httpclient.Header[10];
            headers[0] = new org.apache.commons.httpclient.Header("cookie","XSRF-TOKEN=1542331013|gG-P-dtdKlwv");
            headers[1] = new Header("user-agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
            headers[2] = new Header("Cookie","XSRF-TOKEN=1542331013|gG-P-dtdKlwv");
            headers[3] = new Header("authorization","-rLnIIF-XHkQLQixFL0UvCDAXOmVO0J7UhXHLrwqggE.eyJpbnN0YW5jZUlkIjoiMjAzODAwYTMtZmY3Yy00YjllLTliMWYtMDFhYTc5MTNlNGY3IiwiYXBwRGVmSWQiOiIxMzgwYjcwMy1jZTgxLWZmMDUtZjExNS0zOTU3MWQ5NGRmY2QiLCJtZXRhU2l0ZUlkIjoiNDVkODEwZDctZTEyMS00ZWQ2LWE2MzEtZmFjMzE0Njc4NzE1Iiwic2lnbkRhdGUiOiIyMDE4LTExLTE2VDAxOjU2OjAyLjUwOFoiLCJ1aWQiOm51bGwsImlwQW5kUG9ydCI6IjEyMi4xMTIuMTE2LjIyMi8yODA1MiIsInZlbmRvclByb2R1Y3RJZCI6IlByZW1pdW0xIiwiZGVtb01vZGUiOmZhbHNlLCJvcmlnaW5JbnN0YW5jZUlkIjoiYmU4ZjM2YjktMmY2Ny00NzI5LTgwMTQtYmU4ZGFmYzJkZTUwIiwiYWlkIjoiZTYxZjc1MDktZjYxYS00ZGQ2LWEwZDAtZjA4MzhjOTA0ZDg2IiwiYmlUb2tlbiI6IjY1ZTAxMDc0LTFlNWQtMDU0OC0zZDJlLWZiNjk2ZDc0NjNlMiIsInNpdGVPd25lcklkIjoiNGFmNzRjMDgtMjIzYS00MDQ1LTgwOTAtOGJmZmNhY2ZmMzA5In0");
            headers[4] = new Header("X-XSRF-TOKEN","1542331013|gG-P-dtdKlwv");
            headers[5] = new Header("Accept-Encoding","gzip, deflate, br");
            headers[6] = new Header("Accept-Language","zh-CN,zh;q=0.9");
            headers[7] = new Header("Origin","https://ecom.wix.com");
            headers[8] = new Header("Content-Type","application/json, text/plain, *");
            headers[9] = new Header("X-ecom-instance","-rLnIIF-XHkQLQixFL0UvCDAXOmVO0J7UhXHLrwqggE.eyJpbnN0YW5jZUlkIjoiMjAzODAwYTMtZmY3Yy00YjllLTliMWYtMDFhYTc5MTNlNGY3IiwiYXBwRGVmSWQiOiIxMzgwYjcwMy1jZTgxLWZmMDUtZjExNS0zOTU3MWQ5NGRmY2QiLCJtZXRhU2l0ZUlkIjoiNDVkODEwZDctZTEyMS00ZWQ2LWE2MzEtZmFjMzE0Njc4NzE1Iiwic2lnbkRhdGUiOiIyMDE4LTExLTE2VDAxOjU2OjAyLjUwOFoiLCJ1aWQiOm51bGwsImlwQW5kUG9ydCI6IjEyMi4xMTIuMTE2LjIyMi8yODA1MiIsInZlbmRvclByb2R1Y3RJZCI6IlByZW1pdW0xIiwiZGVtb01vZGUiOmZhbHNlLCJvcmlnaW5JbnN0YW5jZUlkIjoiYmU4ZjM2YjktMmY2Ny00NzI5LTgwMTQtYmU4ZGFmYzJkZTUwIiwiYWlkIjoiZTYxZjc1MDktZjYxYS00ZGQ2LWEwZDAtZjA4MzhjOTA0ZDg2IiwiYmlUb2tlbiI6IjY1ZTAxMDc0LTFlNWQtMDU0OC0zZDJlLWZiNjk2ZDc0NjNlMiIsInNpdGVPd25lcklkIjoiNGFmNzRjMDgtMjIzYS00MDQ1LTgwOTAtOGJmZmNhY2ZmMzA5In0");
            */
            Map<String,String> headerMap = new HashMap<>();
            headerMap.put("cookie","XSRF-TOKEN=1542331013|gG-P-dtdKlwv");
            headerMap.put("user-agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.102 Safari/537.36");
            headerMap.put("authorization","-rLnIIF-XHkQLQixFL0UvCDAXOmVO0J7UhXHLrwqggE.eyJpbnN0YW5jZUlkIjoiMjAzODAwYTMtZmY3Yy00YjllLTliMWYtMDFhYTc5MTNlNGY3IiwiYXBwRGVmSWQiOiIxMzgwYjcwMy1jZTgxLWZmMDUtZjExNS0zOTU3MWQ5NGRmY2QiLCJtZXRhU2l0ZUlkIjoiNDVkODEwZDctZTEyMS00ZWQ2LWE2MzEtZmFjMzE0Njc4NzE1Iiwic2lnbkRhdGUiOiIyMDE4LTExLTE2VDAxOjU2OjAyLjUwOFoiLCJ1aWQiOm51bGwsImlwQW5kUG9ydCI6IjEyMi4xMTIuMTE2LjIyMi8yODA1MiIsInZlbmRvclByb2R1Y3RJZCI6IlByZW1pdW0xIiwiZGVtb01vZGUiOmZhbHNlLCJvcmlnaW5JbnN0YW5jZUlkIjoiYmU4ZjM2YjktMmY2Ny00NzI5LTgwMTQtYmU4ZGFmYzJkZTUwIiwiYWlkIjoiZTYxZjc1MDktZjYxYS00ZGQ2LWEwZDAtZjA4MzhjOTA0ZDg2IiwiYmlUb2tlbiI6IjY1ZTAxMDc0LTFlNWQtMDU0OC0zZDJlLWZiNjk2ZDc0NjNlMiIsInNpdGVPd25lcklkIjoiNGFmNzRjMDgtMjIzYS00MDQ1LTgwOTAtOGJmZmNhY2ZmMzA5In0");
            headerMap.put("X-XSRF-TOKEN","1542331013|gG-P-dtdKlwv");
            headerMap.put("Accept-Encoding","gzip, deflate, br");
            headerMap.put("Accept-Language","zh-CN,zh;q=0.9");
            headerMap.put("Origin","https://ecom.wix.com");
            headerMap.put("Content-Type","application/json, text/plain, */*");
            headerMap.put("X-ecom-instance","-rLnIIF-XHkQLQixFL0UvCDAXOmVO0J7UhXHLrwqggE.eyJpbnN0YW5jZUlkIjoiMjAzODAwYTMtZmY3Yy00YjllLTliMWYtMDFhYTc5MTNlNGY3IiwiYXBwRGVmSWQiOiIxMzgwYjcwMy1jZTgxLWZmMDUtZjExNS0zOTU3MWQ5NGRmY2QiLCJtZXRhU2l0ZUlkIjoiNDVkODEwZDctZTEyMS00ZWQ2LWE2MzEtZmFjMzE0Njc4NzE1Iiwic2lnbkRhdGUiOiIyMDE4LTExLTE2VDAxOjU2OjAyLjUwOFoiLCJ1aWQiOm51bGwsImlwQW5kUG9ydCI6IjEyMi4xMTIuMTE2LjIyMi8yODA1MiIsInZlbmRvclByb2R1Y3RJZCI6IlByZW1pdW0xIiwiZGVtb01vZGUiOmZhbHNlLCJvcmlnaW5JbnN0YW5jZUlkIjoiYmU4ZjM2YjktMmY2Ny00NzI5LTgwMTQtYmU4ZGFmYzJkZTUwIiwiYWlkIjoiZTYxZjc1MDktZjYxYS00ZGQ2LWEwZDAtZjA4MzhjOTA0ZDg2IiwiYmlUb2tlbiI6IjY1ZTAxMDc0LTFlNWQtMDU0OC0zZDJlLWZiNjk2ZDc0NjNlMiIsInNpdGVPd25lcklkIjoiNGFmNzRjMDgtMjIzYS00MDQ1LTgwOTAtOGJmZmNhY2ZmMzA5In0");



//"{"viewName":"StoreFrontFilteredCategoryProductsView","params":{"limit":{"from":0,"to":100},"filters":[{"field":"categoryId","op":"EQUALS","values":["00000000-000000-000000-000000000001"]}],"categories":{"mainCategory":"00000000-000000-000000-000000000001"}}}"

            String jsonStr = "{\"viewName\":\"StoreFrontFilteredCategoryProductsView\",\"params\":{\"limit\":{\"from\":0,\"to\":100},\"filters\":[{\"field\":\"categoryId\",\"op\":\"EQUALS\",\"values\":[\"00000000-000000-000000-000000000001\"]}],\"categories\":{\"mainCategory\":\"00000000-000000-000000-000000000001\"}}}";
            System.out.println(" 推送价格入参json:"+jsonStr);
            logger.info(" 推送价格入参json:"+jsonStr);
            try {
                String resultJsonStr = HttpUtil45.operateData("post","json","https://ecom.wix.com/catalog/view",timeConfig,null,jsonStr,headerMap,null,null);
                if(resultJsonStr!=null&&!"".equals(resultJsonStr)){
                    JSONObject resultJsonObject = JSONObject.fromObject(resultJsonStr);
                    System.out.println(resultJsonObject);
                    String price = "";
                    String qty = "";
                    String size = "";
                    String url = "";
                    Gson gson = new Gson();
                    Map<String,String> sizeMap =  new HashMap<>();
                    //Map<String,String> productMap =  new HashMap<>();
                    StringBuffer productBuffer = new StringBuffer();
                    JSONObject data = resultJsonObject.getJSONObject("data");
                    JSONArray products = data.getJSONArray("products");
                    for (int i = 0; i < products.size(); i++) {
                        JSONObject product = (JSONObject) products.get(i);
                        url = product.get("url").toString();
                        price = product.get("price").toString();
                        String qtyDesc = product.get("inventoryStatus").toString();
                        System.out.println("第"+(i+1)+"个商品，name:"+url+",price:"+price+",qtyDesc:"+qtyDesc);
                        if(qtyDesc.equals("in_stock")){
                            qty = IN_STOCK;
                        }else{
                            qty = NO_STOCK;
                        }
                        productMap.put(url,price+","+qty);
                    }
                    System.out.println(sizeMap.size()+"\n"+sizeMap.toString());

                }else{
                //添加到 失败 请求中
                //failList.add(new RequestFailProductDTO(sex, categoryName, categoryUrl, "0"));
                //System.err.println("gb-请求商品品类 地址出错  "+categoryUrl);
                //logger.error("gb-请求商品品类 地址出错categoryUrl {}  "+categoryUrl);
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
            return;
        }
    }
    /**
     * 处理单个商品库存信息
     *
     * @param productDTO 商品信息
     */
    private static boolean solveProductQty(ProductDTO productDTO) {

            //grapProductListByCategoryUrl(genderAndNameAndCategoryUris.get(i),uri);
        getProductUrl();

        for (Map.Entry<String,String> entry:productMap.entrySet()){
            System.out.println("编号："+entry.getKey()+",产品信息："+entry.getValue());
        }

        System.out.println("产品信息集合:"+productMap.size());
        String productUrl = productDTO.getProductUrl();
        List<SkuDTO> zhiCaiSkuResultList = productDTO.getZhiCaiSkuResultList();
        int zhiCaiSkuResultListSize = zhiCaiSkuResultList.size();
        logger.info("货号：" + productDTO.getSupplierSpuModel() + "；sku的数量：" + zhiCaiSkuResultListSize);
        try {
            String productUrlName = productUrl.replaceAll("https://www.poporcelain.com/product-page/", "");
            SkuDTO skuDTO = new SkuDTO();
                Map<String,SkuDTO> skuMap = new HashMap<>();
                zhiCai:for (int i = 0; i < zhiCaiSkuResultListSize; i++) {
                    skuDTO = zhiCaiSkuResultList.get(i);
                    String spSkuNo = skuDTO.getSpSkuNo();
                    if (spSkuNo == null || "".equals(spSkuNo)) {
                        continue zhiCai;
                    }
                    skuMap.put(skuDTO.getSize(),skuDTO);

                    String size = skuDTO.getSize();
                    final String supplierSkuNo = skuDTO.getSupplierSkuNo();
                    for (Map.Entry<String,String> entry:productMap.entrySet()) {
                        String urlName = entry.getKey();

                        if(productUrlName.equals(urlName)){
                            String[] split = entry.getValue().split(",");
                            exportSpSkunoAndQty(spSkuNo, split[1]);
                            String onLinePrice=split[0];
                            if(onLinePrice!=null){
                                String marketPrice = skuDTO.getMarketPrice();
                                System.out.println("本地价格："+marketPrice);
                                if(marketPrice!=null){
                                    float onLineP = Float.parseFloat(onLinePrice);
                                    float marketP = Float.parseFloat(marketPrice);
                                    if(onLineP!=marketP) {
                                        updateSpSkuMarketPrice(skuDTO.getSupplierSkuNo(), onLinePrice);
                                        exportSpSkunoAndPrice(supplierId, skuDTO.getSpSkuNo(), onLinePrice, marketPrice, productUrl);
                                        logger.info("推送 价格成功：" + skuDTO.getSupplierSkuNo() + " 原价：" + marketPrice + " 新价:" + onLinePrice);
                                        System.out.println("推送 价格成功：" + skuDTO.getSupplierSkuNo() + " 原价：" + marketPrice + " 新价:" + onLinePrice);
                                    }
                                }else {
                                    loggerError.error("getMarketPrice 为空 ProductDTO:" + productDTO.toString());
                                }

                            } else {
                                logger.error("获取商品价格异常=============productUrl:" + productUrl + "==============================");

                            }
                        }
                    }
                }

       }catch(Exception e){
        e.printStackTrace();
        return false;
    }


        //每一款商品休息2s
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return true;
    }

    /**
     * 根据sku获取库存(追加）
     * @param sku 商品的sku
     *
     */
    public static String getQty(String sku){
        String qty = NO_STOCK;
        String qtyUrl = "https://www.prada.com/fr/en/women/ready_to_wear/products.glb.getStoresWithBookAnAppointment.json?productId="+sku;
        try {
            HttpResponse response = HttpUtils.get(qtyUrl);
            if (response.getStatus() == 200) {
                String htmlContent = response.getResponse();

                if (htmlContent.contains("FindInStore")) {
                    qty = IN_STOCK;
                    return qty;
                } else if (!htmlContent.contains("FindInStore")) {
                    return qty;
                } else {
                    return qty;
                }
            }else{
                return qty;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return qty;
        }
    }
    /**
     * 更新尚品 spSkuMarketPrice
     * @param supplierSkuNo
     * @param marketPrice
     */
    private static void updateSpSkuMarketPrice(String supplierSkuNo, String marketPrice) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("supplierId",supplierId);
        jsonObject.put("supplierNo",supplierNo);
        jsonObject.put("supplierSkuNo",supplierSkuNo);
        jsonObject.put("marketPrice",marketPrice);
        String jsonStr = jsonObject.toString();
        System.out.println(" 推送价格入参json:"+jsonStr);
        logger.info(" 推送价格入参json:"+jsonStr);
        try {
            String resultJsonStr = HttpUtil45.operateData("post","json",updateSpMarketPriceUrl,timeConfig,null,jsonStr,null,null);
            JSONObject resultJsonObject = JSONObject.fromObject(resultJsonStr);
            Map<String,Class> keyMapConfig= new HashMap<>();
            keyMapConfig.put("content",String.class);
            ApiResponseBody apiResponseBody = (ApiResponseBody) JSONObject.toBean(resultJsonObject, ApiResponseBody.class, keyMapConfig);
            String code = apiResponseBody.getCode();
            if("0".equals(code)){
                logger.info("==更新updateSpSkuMarketPrice成功:"+supplierId+":"+supplierSkuNo+":"+marketPrice+"===============");
                System.out.println("==更新updateSpSkuMarketPrice成功:"+supplierId+":"+supplierSkuNo+":"+marketPrice+"===============");
            }else{
                loggerError.error("==更新updateSpSkuMarketPrice 失败=="+jsonStr);
                System.err.println("==更新updateSpSkuMarketPrice成功=="+jsonStr);
            }
            System.out.println("更新updateSpMarketPrice resultJsonStr:"+resultJsonStr);
            logger.info("更新updateSpMarketPrice resultJsonStr:"+resultJsonStr);
        } catch (Exception e) {
            loggerError.error("更新updateSpMarketPrice 失败   supplierSkuNo:"+supplierSkuNo+"marketPrice:"+marketPrice);
            System.out.println("更新updateSpMarketPrice 失败   supplierSkuNo:"+supplierSkuNo+"marketPrice:"+marketPrice);
            e.printStackTrace();
        }

    }

    /**
     * 获取尺码库存信息
     * @param spSkuNO 尚品尺码 skuNo
     * @param productDTO 商品尺码 pcode 请求参数
     * @return
     */
    public static  Map<String,String> getProductQtyInfo(String spSkuNO,ProductDTO productDTO){
        //库存结果集
        HashMap<String, String> mapDate = new HashMap<>();
        //  /it/it/p/ajax/product-detail-shipping.ajax?pcode=808354516


        try {
            getProductQtyUnit(productDTO,mapDate);
        } catch (Exception e) {
            logger.info("=========尝试再次获取=====");
            try {
                getProductQtyUnit(productDTO,mapDate);
            } catch (Exception ee) {
                logger.info("=========第二次：获取库存失败 ===spSkuNO: "+spSkuNO );
                ee.printStackTrace();
                System.out.println();
                System.err.println("=========第二次：获取库存失败 ===spSkuNO: "+spSkuNO );
            }
        }
        return mapDate;
    }


    /**
     * 获取商品库存信息
     * @param productDTO
     * @throws Exception
     */
    public static void getProductQtyUnit(ProductDTO productDTO,Map<String,String> mapDate) throws Exception{

        List<SkuDTO> zhiCaiSkuResultList = productDTO.getZhiCaiSkuResultList();
        int zhiCaiSkuResultListSize = zhiCaiSkuResultList.size();
        HttpResponse response = HttpUtils.get(productDTO.getProductUrl());
        if (response.getStatus() == 200) {
            String htmlContent = response.getResponse();
            Document doc = Jsoup.parse(htmlContent);
            SkuDTO skuDTO = new SkuDTO();
            Map<String,SkuDTO> skuMap = new HashMap<>();
            zhiCai:for (int i = 0; i < zhiCaiSkuResultListSize; i++) {
                skuDTO = zhiCaiSkuResultList.get(i);
                String spSkuNo = skuDTO.getSpSkuNo();
                if (spSkuNo == null || "".equals(spSkuNo)) {
                    continue zhiCai;
                }
                skuMap.put(skuDTO.getSize(),skuDTO);


                Elements scripts = doc.select("script");
                for(Element script : scripts) {
                    String str = script.html();
                    if (str.contains("var spConfig = new Product.Config(")) {
                        //String jsonStr = str.replace("var spConfig = new Product.Config(", "").replace(");", "").trim();
                        String jsonStr =  str.substring(str.indexOf("var spConfig = new Product.Config("),str.length());
                        jsonStr = jsonStr.replaceAll("var spConfig = new Product.Config","").replaceAll(";","");
                        jsonStr = jsonStr.substring(1,jsonStr.length()-1);
                        //System.out.println(jsonStr);
                        JsonElement je = new JsonParser().parse(jsonStr);
                        JsonElement attributes = je.getAsJsonObject().get("attributes");
                        JsonElement json257 = attributes.getAsJsonObject().get("257");

                        JsonElement optionsArr = json257.getAsJsonObject().get("options");
                        JsonArray asJsonArray = optionsArr.getAsJsonArray();
                        for (int j = 0; j < asJsonArray.size(); j++) {
                            JsonObject asJsonObject = asJsonArray.get(j).getAsJsonObject();
                            String label = asJsonObject.get("label").toString();
                            label = label.substring(1,label.length()-1);
                            String stock = asJsonObject.get("stock").toString();
                            stock = stock.trim().substring(1,stock.indexOf("."));

                            System.out.println("库存标志："+stock);
                            String qty = "";
                            if ("0".equals(stock.trim())) {
                                qty = NO_STOCK;
                            }else if(!"0".equals(stock.trim())){
                                qty = IN_STOCK;
                            }

                            System.out.println("尺寸：" + label + ";库存" + qty);
                            String size = skuDTO.getSize();
                            if (label.equals(size)) {
                                mapDate.put("qty",qty );

                                return;
                            }
                        }
                    }
                }
            }
        }
    }
    /**
     * 导出 商品skuNo 和 qty 信息
     * @param spSkuNo 尚品skuNo
     * @param oldPrice 库中价格
     *  @param newPrice doc价格
     */
    private static void exportSpSkunoAndPrice(String supplierId,String spSkuNo, String newPrice,String oldPrice,String productUrl) {
        //继续追加
        StringBuffer buffer = new StringBuffer();
        try {
            /*
            "supplier_id" + splitSign +
                "sp_sku_no" + splitSign +
                "new price" + splitSign +
                "old price" + splitSign +
                "product_url" + splitSign
             */
            buffer.append(supplierId).append(splitSign);
            buffer.append(spSkuNo).append(splitSign);
            buffer.append(newPrice).append(splitSign);
            buffer.append(oldPrice).append(splitSign);
            buffer.append(productUrl).append(splitSign);
            buffer.append("\r\n");
            priceOut.write(buffer.toString());
            System.out.println("spSkuNo:"+spSkuNo+" newPrice:"+newPrice+"|");
            logger.info("supplierId:"+supplierId+"spSkuNo:"+spSkuNo+" newPrice:"+newPrice+"oldPrice"+oldPrice+"productUrl"+productUrl+"|");
            logger.info(buffer.toString());
            priceOut.flush();
        } catch (Exception e) {
        }
    }
    /**
     * 导出 商品skuNo 和 qty 信息
     * @param spSkuNo 尚品skuNo
     * @param qty 库存信息
     */
    private static void exportSpSkunoAndQty(String spSkuNo, String qty) {
        //继续追加
        StringBuffer buffer = new StringBuffer();
        try {
            buffer.append(spSkuNo).append(splitSign);
            buffer.append(qty).append(splitSign);
            buffer.append("\r\n");
            out.write(buffer.toString());
            System.out.print("spSkuNo:"+spSkuNo+" qty:"+qty+"|");
            logger.info("spSkuNo:"+spSkuNo+" qty:"+qty+"|");
            logger.info(buffer.toString());
            out.flush();
        } catch (Exception e) {
        }
    }
    //价格修改后发送邮件
    public static void sendMail(){

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props);
        Message message = new MimeMessage(session);
        try
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String todayStr = simpleDateFormat.format(new Date());
           /* long dayTime = 1000*3600*24l;
            Date yesterDate = new Date(new Date().getTime() - dayTime);
            String yesterdayDateStr = simpleDateFormat.format(yesterDate)*/;
            message.setSubject(todayStr+"-"+bdl.getString("uri")+"修改了价格的商品信息");
            message.setFrom(new InternetAddress("weidong.han@shangpin.com"));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress("sophia.huo@shangpin.com"));
            //message.setRecipient(Message.RecipientType.TO, new InternetAddress("weidong.han@shangpin.com"));
            //设置抄送人
            message.setRecipient(Message.RecipientType.CC, new InternetAddress("weidong.han@shangpin.com"));
            Multipart multipart = new MimeMultipart();
            //实例化一个bodypart用于封装内容
            BodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent("<font color='red'>修改了价格的商品信息，见附件</font>","text/html;charset=utf8");
            //添加bodypart到multipart
            multipart.addBodyPart(bodyPart);
            //每一个部分实例化一个bodypart，故每个附件也需要实例化一个bodypart
            bodyPart = new MimeBodyPart();

            //实例化DataSource(来自jaf)，参数为文件的地址
            DataSource dataSource = new FileDataSource(bdl.getString("csvFilePath")+"poporcelain-price-"+todayStr+".csv");
            //使用datasource实例化datahandler
            DataHandler dataHandler = new DataHandler(dataSource);
            bodyPart.setDataHandler(dataHandler);

            //设置附件标题，使用MimeUtility进行名字转码，否则接收到的是乱码
            bodyPart.setFileName(javax.mail.internet.MimeUtility.encodeText(todayStr+"修改了价格的商品信息"+".csv"));
            multipart.addBodyPart(bodyPart);
            message.setContent(multipart);
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.shangpin.com","weidong.han@shangpin.com", "xiao092916");
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            logger.info("===================发送邮件成功 =========================");
            System.out.println("===================发送邮件成功 =========================");
        }catch(Exception e) {
            e.printStackTrace();
            loggerError.error(" ===================发送邮件成功失败=========================");
            System.out.println("===================发送邮件成功失败 =========================");
        }
    }
    //文件发送
    protected void getFileToEmail(){
       /* long dayTime = 1000*3600*24l;
        Date yesterDate = new Date(new Date().getTime() - dayTime);*/
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String todayDateStr = simpleDateFormat.format(new Date());
        FetchStockImpl o=new FetchStockImpl();
        String fileName=bdl.getString("csvFilePath")+"poporcelain-price-"+todayDateStr+".csv";
        File file=new File(bdl.getString("csvFilePath")+"poporcelain-price-"+todayDateStr+".csv");
        try {
            FileInputStream fis = new FileInputStream(file);
            //System.out.println("文件的大小是："+fis.available()+"\n");
            if (fis.available()>0){
                sendMail();
            }else {
                deleteFile(fileName);
                logger.info("===================没有价格改变的商品不需邮箱发送 =========================");
                System.out.println("===================没有价格改变的商品不需邮箱发送 =========================");
            }
        } catch (IOException e) {
            e.printStackTrace();
            loggerError.error(" ===================寻找csv文件失败=========================");
            System.out.println("===================寻找csv文件失败 =========================");
        }
    }


    //空文件是否被删除
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }


    public static void main(String[] args) {


       /*ProductDTO productDTO = new ProductDTO();
        productDTO.setProductUrl("https://www.prada.com/fr/en/women/shoes/products.patent_leather_sandals.1X109L_93W_F0J7K_F_D110.html");
        List<SkuDTO> zhiCaiSkuResultList = new ArrayList<>();
        SkuDTO skuDTO = new SkuDTO();
        skuDTO.setSpSkuNo("30968589002");
        skuDTO.setSize("U");
        skuDTO.setSupplierSkuNo("343471-E-U");
        skuDTO.setMarketPrice("350.0");
        zhiCaiSkuResultList.add(skuDTO);
        productDTO.setZhiCaiSkuResultList(zhiCaiSkuResultList);
        solveProductQty(productDTO);*/

        //updateSpSkuMarketPrice("454070 A7M0T 5909-U","550");
        FetchStockImpl fetchStock = new FetchStockImpl();
        fetchStock.getProductUrl();
        //fetchStock.getShangPinPageContentByParam("2018082702041","moncler ",1,20,"www.prada.com");
        //getShangPinPageContentByParam(String supplierId, String brandName, Integer pageIndex, Integer pageSize, String channel) {

       /* FetchStockImpl fetchStock = new FetchStockImpl();
        fetchStock.getFileToEmail();*/
    }

}
