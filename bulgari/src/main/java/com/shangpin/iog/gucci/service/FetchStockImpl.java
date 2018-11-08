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
        System.out.println("============拉取prada库存数据开始 " + startDateTime + "=========================");
        logger.info("==============拉取prada库存数据开始 " + startDateTime + "=========================");

        //1. 请求需要更新库存商品 信息接口
        failedSpSkuNoList = new ArrayList<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = simpleDateFormat.format(new Date());

        String temFilePath = filePath + "prada-qty-" + todayStr + ".csv";
        String priceFilePath = filePath + "prada-price-"+todayStr+".csv";
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
        ShangPinPageContent gucciPageContent = getShangPinPageContentByParam(supplierId, "prada", 1, Integer.parseInt(pageSize), channel);

        //ShangPinPageContent gucciPageContent = getShangPinPageContentByParam("2018090402046","The Kooples",1,20,"www.thekooples.com");
        productDTOAllList.addAll(gucciPageContent.getZhiCaiResultList());

        if (gucciPageContent == null) return;
        //总记录数
        Integer total = gucciPageContent.getTotal();
        Integer pageNumber = getPageNumber(total, 20);
        for (int i = 2; i <= pageNumber; i++) {
            ShangPinPageContent temgucciPageContent = getShangPinPageContentByParam(supplierId, "prada", i, Integer.parseInt(pageSize), channel);
            if (temgucciPageContent != null) {
                productDTOAllList.addAll(temgucciPageContent.getZhiCaiResultList());
            } else { //请求失败重新 再次请求
                temgucciPageContent = getShangPinPageContentByParam(supplierId, "prada", i, Integer.parseInt(pageSize), channel);
                if (temgucciPageContent != null) {
                    productDTOAllList.addAll(temgucciPageContent.getZhiCaiResultList());
                }
            }
        }

        logger.info("=====需要更新prada spProduct Size:" + productDTOAllList.size());
        System.out.println("=====需要更新prada spProduct Size:" + productDTOAllList.size());
        //导出尚品库存数据
        exportQtyInfoForProductList(productDTOAllList);

        // 处理 请求失败的 尚品 skuNo 信息
        ProductDTO productDTO = new ProductDTO();
        int failedSpSkuNoSize = failedSpSkuNoList.size();
        for (int i = 0; i < failedSpSkuNoSize; i++) {
            repeatSolveFailedSpSkuNo(failedSpSkuNoList.get(i), productDTO);
        }

        String endtDateTime = format.format(new Date());
        logger.info("===================拉取prada库存数据结束 " + endtDateTime + "=========================");
        System.out.println("=================拉取prada库存数据结束 " + endtDateTime + "=========================");

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
     * 处理 品类 下的 商品列表分页
     *

     */
    public void getProductList(){
        for (int i = 0; i < genderAndNameAndCategoryUris.size(); i++) {
            grapProductListByCategoryUrl(genderAndNameAndCategoryUris.get(i),uri);
        }
    }
    /**
     * 处理 品类 下的 商品列表分页
     * @param categoryUrl  品类url
     */
    public static void grapProductListByCategoryUrl(String categoryUrl, String url) {

        try {


            org.apache.commons.httpclient.Header[] headers = new org.apache.commons.httpclient.Header[2];
            headers[0] = new org.apache.commons.httpclient.Header("cookie","_qVisit_cookie=true; detected-locale=en-gb; wwsessionid=C19CA0F6EBCEFF33B6CC; _gcl_au=1.1.1082993648.1541382657; _ga=GA1.2.1755560366.1541382658; _gid=GA1.2.218454290.1541382658; cartQty=0; _hjIncludedInSample=1; _scid=bb679953-5e2a-4fe9-8605-1042da995084; CACHED_FRONT_FORM_KEY=Z2LTO22yeJtvwFRv; customer_privacy_policy=1; liveagent_oref=https://www.bulgari.com/en-gb/products.html?root_level=315&product_detail_one=218; liveagent_sid=90959dfb-0713-415c-836f-6a84e11fb68e; liveagent_vc=2; liveagent_ptid=90959dfb-0713-415c-836f-6a84e11fb68e; VIEWED_PRODUCT_IDS=66911%2C63832%2C68044%2C64278%2C46817; LAST_CATEGORY=530; CATEGORY_INFO=%7B%22product_detail_one%22%3A%22228%5Cuff01%22%2C%22root_level%22%3A%22315%22%7D; frontend=tig808ega7o9c92aljlfqluek4; ak_bmsc=B8B7E64381A8D7D90B4AE7273188B0BE170DDBEA2614000080DCDF5B98CC5366~plGaNrP5IqkduZq2dhiHwF/juxvX7PFbXAh2vlTUDO6vpKOxWnBlnzaIqmx14teGGzNByO2Wt376pu4gcf6kdL5cZIHHGl2xrONDq78kKFhigYSwX+lEhI4hBlwji7KSXXqphCOWA/WA2ZPJvZM5pwAdvuv+t09c6kg8HXYUF44eWdg37s9LT1Wyk0I8E88zRp8a3WCtDNPPoYKZTThAwP1o3CHIRlYaErXQhaoIzNFIE=; frontend_cid=86mAiHsYPKfj3Jh7; _gat_UA-65941188-1=1; bm_mi=AC3C6448122127D9793E4714D28CE9E9~PmG0ro8pOBbBrLFzA+PyRXef18QLQgNCONYetLDx/XEd1+ltM7Jv0Pda13fizMFAymmEy533IsmQc9EVaeMfj8h1ApEpbvFaOBoxP28XR04jxUS0i99lTtc6pE0rLMSld4Pf1Gl3Fi6njKgsG65O0o0WOuIO20RQECZaGqnpJ4NKoI70xYoiclPAcGU5xnwOwlhBFWpfxZZVhLs2QjYnGBT2DevyqV3OkGBSupaiSL/BQIXa7gYvtDyHe0hKqgJdKL57+fIkuscpE994XeWbSFgH7rWcSyPk+y0fSRbPOigows4G5+0lsYzvA/L4coE1; bm_sv=32D9C2B4439774BD3EC4F25E7BE081F3~gxM67SwGi5mvnaIPy3BxbJOHCfLkAMH6/ZlOVu9nAubEtU2ruDWaWD5rn6jGqWD+lEzW6a7IhnAsr4aHYBOstZiaufdNQf0k7Ud+wNPRz8JEYcY6diySf7eUi+h+4hvKbGX6319c/FfDyMhb3CF/Dcfk+gbGaBH4CLZhG8mdtcc=");
            headers[1] = new org.apache.commons.httpclient.Header("user-agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36");
            HttpResponse response = HttpUtils.get(categoryUrl,headers);
            System.out.println(response.getStatus());
            if (response.getStatus() == 200) {
                String htmlContent = response.getResponse();
                Document doc = Jsoup.parse(htmlContent);
                Elements select = doc.select(" div.filter-option-mobile>div.filter-accordeon>div");
                // System.out.println("商品链接select："+select);
                if(select!=null&&!"".equals(select)){
                    for (int i = 0; i < select.size(); i++) {
                        Element element = select.get(i);
                        String dataName = element.attr("data-name");
                        //System.out.println("dataName标签："+dataName);
                        if("aesthetic_line".equals(dataName)){
                            String dataValue = element.attr("data-value");
                            //System.out.println("dataValue："+dataValue);
                           // String productJsonUrl="https://www.bulgari.com/en-gb/alpencatalog/category/getjson/?group_by=aesthetic_line&product_detail_one="+categoryName+"&rid=1&root_level=315&from=0&limit=100&aesthetic_line="+dataValue;
                         /*   String productJsonUrl = categoryUrl.replace("products.html","alpencatalog/category/getjson/")+
                                   "&group_by=aesthetic_line&rid=155&from=0&limit=500&aesthetic_line="+dataValue;*/
                            String productJsonUrl = categoryUrl.replace("products.html","alpencatalog/category/getjson/")+
                                    "&group_by=aesthetic_line&rid=1&from=0&limit=500&aesthetic_line="+dataValue;
                            System.out.println("product的json集合的第"+i+"个url："+productJsonUrl);
                            getProductUrl(productJsonUrl,uri);
                        }
                    }
                }
            }else{
                //添加到 失败 请求中
                //failList.add(new RequestFailProductDTO(sex, categoryName, categoryUrl, "0"));
                System.err.println("gb-请求商品品类 地址出错  "+categoryUrl);
                logger.error("gb-请求商品品类 地址出错categoryUrl {}  "+categoryUrl);
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
            //添加到 失败 请求中
            // failList.add(new RequestFailProductDTO(sex, categoryName, categoryUrl, "0"));
            System.out.println("gb-请求商品品类 地址出错categoryUrl{}"+categoryUrl+e.getMessage());
            loggerError.error("gb-请求商品品类 地址出错categoryUrl{}"+categoryUrl+e.getMessage());
            return;
        }

    }

    /**
     * 处理 品类 下的 商品列表信息
     *
     * @param categoryUrl  品类url
     */

    public static void getProductUrl(String categoryUrl,String uri){

        try {

            org.apache.commons.httpclient.Header[] headers = new org.apache.commons.httpclient.Header[2];
            headers[0] = new org.apache.commons.httpclient.Header("cookie","_qVisit_cookie=true; detected-locale=en-gb; wwsessionid=C19CA0F6EBCEFF33B6CC; _gcl_au=1.1.1082993648.1541382657; _ga=GA1.2.1755560366.1541382658; _gid=GA1.2.218454290.1541382658; cartQty=0; _hjIncludedInSample=1; _scid=bb679953-5e2a-4fe9-8605-1042da995084; CACHED_FRONT_FORM_KEY=Z2LTO22yeJtvwFRv; customer_privacy_policy=1; liveagent_oref=https://www.bulgari.com/en-gb/products.html?root_level=315&product_detail_one=218; liveagent_sid=90959dfb-0713-415c-836f-6a84e11fb68e; liveagent_vc=2; liveagent_ptid=90959dfb-0713-415c-836f-6a84e11fb68e; VIEWED_PRODUCT_IDS=66911%2C63832%2C68044%2C64278%2C46817; LAST_CATEGORY=530; CATEGORY_INFO=%7B%22product_detail_one%22%3A%22228%5Cuff01%22%2C%22root_level%22%3A%22315%22%7D; frontend=tig808ega7o9c92aljlfqluek4; ak_bmsc=B8B7E64381A8D7D90B4AE7273188B0BE170DDBEA2614000080DCDF5B98CC5366~plGaNrP5IqkduZq2dhiHwF/juxvX7PFbXAh2vlTUDO6vpKOxWnBlnzaIqmx14teGGzNByO2Wt376pu4gcf6kdL5cZIHHGl2xrONDq78kKFhigYSwX+lEhI4hBlwji7KSXXqphCOWA/WA2ZPJvZM5pwAdvuv+t09c6kg8HXYUF44eWdg37s9LT1Wyk0I8E88zRp8a3WCtDNPPoYKZTThAwP1o3CHIRlYaErXQhaoIzNFIE=; frontend_cid=86mAiHsYPKfj3Jh7; _gat_UA-65941188-1=1; bm_mi=AC3C6448122127D9793E4714D28CE9E9~PmG0ro8pOBbBrLFzA+PyRXef18QLQgNCONYetLDx/XEd1+ltM7Jv0Pda13fizMFAymmEy533IsmQc9EVaeMfj8h1ApEpbvFaOBoxP28XR04jxUS0i99lTtc6pE0rLMSld4Pf1Gl3Fi6njKgsG65O0o0WOuIO20RQECZaGqnpJ4NKoI70xYoiclPAcGU5xnwOwlhBFWpfxZZVhLs2QjYnGBT2DevyqV3OkGBSupaiSL/BQIXa7gYvtDyHe0hKqgJdKL57+fIkuscpE994XeWbSFgH7rWcSyPk+y0fSRbPOigows4G5+0lsYzvA/L4coE1; bm_sv=32D9C2B4439774BD3EC4F25E7BE081F3~gxM67SwGi5mvnaIPy3BxbJOHCfLkAMH6/ZlOVu9nAubEtU2ruDWaWD5rn6jGqWD+lEzW6a7IhnAsr4aHYBOstZiaufdNQf0k7Ud+wNPRz8JEYcY6diySf7eUi+h+4hvKbGX6319c/FfDyMhb3CF/Dcfk+gbGaBH4CLZhG8mdtcc=");
            headers[1] = new Header("user-agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36");
            HttpResponse response = HttpUtils.get(categoryUrl,headers);
            if (response.getStatus() == 200) {
                String htmlContent = response.getResponse();
                String price = "";
                String qty = "";
                String size = "";
                String productModel = "";
                Gson gson = new Gson();
                Map<String,String> sizeMap =  new HashMap<>();
                //Map<String,String> productMap =  new HashMap<>();
                StringBuffer productBuffer = new StringBuffer();
                //System.out.println(htmlContent);
                JsonElement je = new JsonParser().parse(htmlContent);
                JsonObject asJsonObject = je.getAsJsonObject();
                JsonObject group = asJsonObject.get("group").getAsJsonObject();
                JsonObject type = group.get("type").getAsJsonObject();
                JsonObject g0 = type.get("g0").getAsJsonObject();
                JsonObject rows = g0.get("rows").getAsJsonObject();
                JsonObject asJsonObject1 =g0;
                for (int count = 0; count < 1000; count++) {
                    String rowsString = rows.get(count + "").toString();
                    if (rowsString != null && !"".equals(rowsString)) {
                        asJsonObject1 = rows.get(count + "").getAsJsonObject();
                        //System.out.println(asJsonObject1);
                        if (asJsonObject1.isJsonObject()) {
                            //System.out.println(asJsonObject1.toString());
                            //productName = asJsonObject1.get("seo_text").toString().replaceAll("\"","").replaceAll(",","");
                            JsonObject data = asJsonObject1.getAsJsonObject("data");
                            if (data.has("sku")) {

                                productModel = data.get("sku").toString().replaceAll("\"", "");
                                if (productModel == null && "".equals(productModel)) {
                                    continue;
                                }
                                System.out.println("产品编号：" + productModel);
                                String price_string = data.get("price_string").toString();
                                //System.out.println(price_string);
                                //英国价格
                                price = price_string.substring(price_string.indexOf("£") + 1, price_string.length()).replaceAll(",", "").replaceAll("\"", "");
                                //法国价格
                                //price = price_string.substring(price_string.indexOf("€") + 1, price_string.length()).replaceAll(",", "").replaceAll("\"", "")
                                 //       .replaceAll("<br/>Price in France<br/>Including taxes", "");
                                System.out.println("产品价格：" + price);
                                String childrens1 = data.get("childrens").toString().replaceAll("\"", "");
                                if (childrens1.contains("{")) {
                                    JsonObject childrens = data.getAsJsonObject("childrens");
                                    //System.out.println(childrens);
                                    Map<String, Object> map = gson.fromJson(childrens.toString(), Map.class);
                                    for (Map.Entry<String, Object> entry : map.entrySet()) {
                                        //System.out.println(entry.getValue());
                                        String[] split = entry.getValue().toString().split(",");
                                        for (int i = 0; i < split.length; i++) {

                                            if (split[i].contains("size=")) {
                                                size = split[i].replaceAll("size=", "");
                                                //System.out.println("产品尺寸："+size);
                                            }
                                            if (split[i].contains("is_in_stock=")) {
                                                //String flag = split[i].replaceAll("is_in_stock=","");
                                                if (split[i].contains("is_in_stock=false")) {
                                                    qty = NO_STOCK;
                                                } else if (split[i].contains("is_in_stock=true")) {
                                                    qty = IN_STOCK;
                                                } else {
                                                    qty = NO_STOCK;
                                                }
                                                //System.out.println("对应尺寸的产品库存:"+qty);
                                                if (size != null && !"".equals(size)) {
                                                    sizeMap.put(size, qty);
                                                }
                                                if(price!=null&&!"".equals(price)) {
                                                    productBuffer.append(size).append(",").append(qty).append(",").append(price);
                                                    productMap.put(productModel + "," + size, productBuffer.toString());
                                                    productBuffer.delete(0, productBuffer.length());
                                                }
                                            }
                                        }

                                    }

                                } else {
                                    String is_in_stock = data.get("is_in_stock").toString();
                                    if (is_in_stock.contains("false")) {
                                        qty = NO_STOCK;
                                    } else if (is_in_stock.contains("true")) {
                                        qty = IN_STOCK;
                                    } else {
                                        qty = NO_STOCK;
                                    }
                                    System.out.println("没有尺寸的库存：" + qty);
                                    if(price!=null&&!"".equals(price)) {
                                        productBuffer.append("U").append(",").append(qty).append(",").append(price);
                                        productMap.put(productModel + ",U", productBuffer.toString());
                                        productBuffer.delete(0, productBuffer.length());
                                    }
                                }


                                System.out.println("尺寸以及对应库存的集合：" + sizeMap.toString());

                                //count++;
                            } else {
                                continue;
                            }
                        }
                    }
                }


                }else{
                //添加到 失败 请求中
                //failList.add(new RequestFailProductDTO(sex, categoryName, categoryUrl, "0"));
                System.err.println("gb-请求商品品类 地址出错  "+categoryUrl);
                logger.error("gb-请求商品品类 地址出错categoryUrl {}  "+categoryUrl);
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
        for (int i = 0; i < genderAndNameAndCategoryUris.size(); i++) {
            grapProductListByCategoryUrl(genderAndNameAndCategoryUris.get(i),uri);
        }
        for (Map.Entry<String,String> entry:productMap.entrySet()){
            System.out.println("编号："+entry.getKey()+",产品信息："+entry.getValue());
        }

        System.out.println("产品信息集合:"+productMap.size());
        String productUrl = productDTO.getProductUrl();
        List<SkuDTO> zhiCaiSkuResultList = productDTO.getZhiCaiSkuResultList();
        int zhiCaiSkuResultListSize = zhiCaiSkuResultList.size();
        logger.info("货号：" + productDTO.getSupplierSpuModel() + "；sku的数量：" + zhiCaiSkuResultListSize);
        try {


            HttpResponse response = HttpUtils.get(productUrl);
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

                    String size = skuDTO.getSize();
                    final String supplierSkuNo = skuDTO.getSupplierSkuNo();
                    for (Map.Entry<String,String> entry:productMap.entrySet()) {
                        String[] productModel = entry.getKey().split(",");
                        if(supplierSkuNo.contains(productModel[0])){
                            String[] split = entry.getValue().split(",");
                            if(size == split[0]){
                                exportSpSkunoAndQty(spSkuNo, split[1]);
                            }
                            String onLinePrice=split[2];
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
        }else{
            logger.error("================请求商品地址失败===========================================");
            logger.error(productDTO.toString());
            logger.error("================请求商品地址失败===========================================");
            return false;
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
            DataSource dataSource = new FileDataSource(bdl.getString("csvFilePath")+"prada-price-"+todayStr+".csv");
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
        String fileName=bdl.getString("csvFilePath")+"prada-price-"+todayDateStr+".csv";
        File file=new File(bdl.getString("csvFilePath")+"prada-price-"+todayDateStr+".csv");
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


       ProductDTO productDTO = new ProductDTO();
        productDTO.setProductUrl("https://www.prada.com/fr/en/women/shoes/products.patent_leather_sandals.1X109L_93W_F0J7K_F_D110.html");
        List<SkuDTO> zhiCaiSkuResultList = new ArrayList<>();
        SkuDTO skuDTO = new SkuDTO();
        skuDTO.setSpSkuNo("30968589002");
        skuDTO.setSize("U");
        skuDTO.setSupplierSkuNo("343471-E-U");
        skuDTO.setMarketPrice("350.0");
        zhiCaiSkuResultList.add(skuDTO);
        productDTO.setZhiCaiSkuResultList(zhiCaiSkuResultList);
        solveProductQty(productDTO);

        //updateSpSkuMarketPrice("454070 A7M0T 5909-U","550");
      // FetchStockImpl fetchStock = new FetchStockImpl();
       // fetchStock.getShangPinPageContentByParam("2018090402046","moncler ",1,20,"www.prada.com");
        //getShangPinPageContentByParam(String supplierId, String brandName, Integer pageIndex, Integer pageSize, String channel) {

       /* FetchStockImpl fetchStock = new FetchStockImpl();
        fetchStock.getFileToEmail();*/
    }

}
