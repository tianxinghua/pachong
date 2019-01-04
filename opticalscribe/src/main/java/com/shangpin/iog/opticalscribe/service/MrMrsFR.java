package com.shangpin.iog.opticalscribe.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shangpin.iog.opticalscribe.dto.LKBennettDTO;
import com.shangpin.iog.opticalscribe.dto.RequestFailProductDTO;
import com.shangpin.iog.opticalscribe.tools.CSVUtilsSolveRepeatData;
import com.shangpin.iog.pavinGroup.util.HttpResponse;
import com.shangpin.iog.pavinGroup.util.HttpUtils;
import com.shangpin.iog.pavinGroup.util.XinghuaClickTest;
import com.shangpin.iog.service.EventProductService;
import com.shangpin.iog.service.ProductFetchService;
import org.apache.commons.httpclient.Header;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Company: www.shangpin.com
 * @Author txh
 * @Date Create in 9:27 2018/9/13
 * @Description: L.K.BennettA 英国官网 商品拉取


#女装:
fr-genderAndNameAndCategoryUrl1=women&&L.K.Bennett&&https://www.lkbennett.com/Clothing
#女鞋：
#fr-genderAndNameAndCategoryUrl2=women&&L.K.Bennett&&https://www.lkbennett.com/Shoes


 */

@Component("mrMrsFR")
public class MrMrsFR {
    private int urlCount=0;
    //有库存
    private static final String IN_STOCK = "10";
    //无库存
    private static final String NO_STOCK = "0";
    private static String supplierId="2018070502014";
    private static String supplierNo="S0000963";
    // 商品barCodeMap  key:barCode value:null
    private static HashMap<String, Object> barCodeMap= null;

    //商品失败请求信息
    private static List<RequestFailProductDTO> failList = null;

    @Autowired
    ProductFetchService productFetchService;

    @Autowired
    EventProductService eventProductService;

    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");

    private static ResourceBundle bdl = null;
    private static String uri;
    private static String path;
    private static String destFilePath;
    private static String flag;
    private static String storeLang;
    private static String cookies;
    // 所有品类相对路径以及名称 如： women&&新作：RE(BELLE)&&/zh/ca/women/handbags/new,women&&手提包&波士顿包&&/zh/ca/women/handbags/top-handles
    private static List<String> genderAndNameAndCategoryUris = new ArrayList<>();
    private static OutputStreamWriter out= null;
    static String splitSign = ",";
    /**
     * 特殊处理 商品品类名称 字符 如： ,高跟鞋,凉鞋,
     */
    private static String sizeCategoryNames = "";

    static {
        if (null == bdl)
            bdl = ResourceBundle.getBundle("conf");
        uri = bdl.getString("uri-fr");
        path = bdl.getString("path-fr");
        destFilePath = bdl.getString("destFilePath-fr");
        //flag = bdl.getString("flag-fr");
        storeLang = bdl.getString("storeLang-fr");

        sizeCategoryNames = bdl.getString("sizeCategoryNames-fr");

        Enumeration<String> keys = bdl.getKeys();
        while(keys.hasMoreElements()){
            String key = keys.nextElement();
            if(key.startsWith("fr-genderAndNameAndCategoryUrl")){
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

    ExecutorService exe = new ThreadPoolExecutor(10,10, 500, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<Runnable>(100),new ThreadPoolExecutor.CallerRunsPolicy());
    /**
     * 拉取商品入口
     * @throws Exception
     */
    public  void getUrlList() throws Exception {
        //根据flag 判断是处理数据还是 拉取数据
        if("true".equals(flag)){  //处理拉取的重复商品数据
            System.out.println("=============fr-处理重复数据开始===============");
            logger.info("=============fr-处理重复数据开始===============");
            CSVUtilsSolveRepeatData.solveFinalProductData(path,destFilePath);
            return;
        }
        System.out.println("要下载的url："+uri);
        System.out.println("文件保存目录："+path);
        try {
            out = new OutputStreamWriter(new FileOutputStream(path, true),"gb2312");
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuffer buffer = new StringBuffer(
                "gender" + splitSign +
                        "brand" + splitSign +

                        "category" + splitSign +
                        "SPU" + splitSign +

                        "productModel" + splitSign +
                        "season" + splitSign +

                        "material"+ splitSign +
                        "color"+ splitSign +

                        "size" + splitSign +
                        "proName" + splitSign +

                        "国外市场价" + splitSign +
                        "国内市场价" + splitSign +
                        "salePrice" + splitSign +

                        "qty" + splitSign +

                        "made" + splitSign +

                        "desc" + splitSign +
                        "pics" + splitSign +

                        "detailLink" + splitSign +
                        "measurement" + splitSign+
                        "supplierId" + splitSign+
                        "supplierNo" + splitSign+
                        "supplierSkuNo" + splitSign+
                        "channel" + splitSign
        ).append("\r\n");
        out.write(buffer.toString());
        //cookies=XinghuaClickTest.getCookies();
        try {

            //每次调用都初始化 barCodeMap 以及 failList
            barCodeMap = new HashMap<>();
            failList = new ArrayList<>();

            int productModeNumber = 0;

            //校验配置信息 是否符合格式要求
            int size = genderAndNameAndCategoryUris.size();
            for (int i = 0; i < size; i++) {

                String[] sexAnduriAndName = genderAndNameAndCategoryUris.get(i).split("&&");
                if(sexAnduriAndName.length!=3) {
                    logger.info(" lv-fr-genderAndCategoryUri 不符合格式 ：" + genderAndNameAndCategoryUris.get(i));
                    System.out.println(" lv-fr-genderAndCategoryUri 不符合格式 ：" + genderAndNameAndCategoryUris.get(i));
                    throw new RuntimeException("配置文件 genderAndCategoryUri 不符合格式："+genderAndNameAndCategoryUris.get(i));
                }
            }

            for (int i = 0; i < size; i++) {
                if(i>0){
                    String[] temsexAnduriAndName = genderAndNameAndCategoryUris.get(i-1).split("&&");

                    System.out.println("lv-fr-拉取品类结束 gender:"+temsexAnduriAndName[0]+" categoryName:"+temsexAnduriAndName[1]+" uri:"+temsexAnduriAndName[2]);
                    System.out.println("共拉取 ："+(barCodeMap.size()-productModeNumber)+"款商品数据");
                    System.out.println("=====================================================================");

                    logger.info("lv-fr-拉取品类结束 gender:"+temsexAnduriAndName[0]+" categoryName:"+temsexAnduriAndName[1]+" uri:"+temsexAnduriAndName[2]);
                    logger.info("共拉取 ："+(barCodeMap.size()-productModeNumber)+"款商品数据");
                    logger.info("=====================================================================");
                    productModeNumber = barCodeMap.size();
                }

                String[] sexAnduriAndName = genderAndNameAndCategoryUris.get(i).trim().split("&&");
                //拉取 品类信息
                System.out.println("lv-fr-开始拉取："+sexAnduriAndName[0]+":"+sexAnduriAndName[1]+":"+sexAnduriAndName[2]);
                logger.info("lv-fr-开始拉取："+sexAnduriAndName[0]+":"+sexAnduriAndName[1]+":"+sexAnduriAndName[2]);
                getAllProductsCategroyUrl(sexAnduriAndName[0],sexAnduriAndName[1],sexAnduriAndName[2]);

            }
            String[] temsexAnduriAndName = genderAndNameAndCategoryUris.get(size-1).split("&&");
            System.out.println();
            System.out.println("lv-fr-拉取品类结束 gender:"+temsexAnduriAndName[0]+" categoryName:"+temsexAnduriAndName[1]+" uri:"+temsexAnduriAndName[2]);
            System.out.println("共拉取 ："+(barCodeMap.size()-productModeNumber)+"款商品数据");
            System.out.println("所有品类拉取结束共:"+barCodeMap.size()+"款商品数据");
            System.out.println("=====================================================================");


            logger.info("lv-fr-拉取品类结束 gender:"+temsexAnduriAndName[0]+" categoryName:"+temsexAnduriAndName[1]+" uri:"+temsexAnduriAndName[2]);
            logger.info("共拉取 ："+(barCodeMap.size()-productModeNumber)+"款商品数据");
            logger.info("所有品类拉取结束共:"+barCodeMap.size()+"款商品数据");
            logger.info("=====================================================================");

           System.out.println("==========fr重新请求失败的商品及品类信息===================================================");
            logger.error("==========fr重新请求失败的商品及品类信息===================================================");
            int failSize = failList.size();
            for (int i = 0; i <failSize ; i++) {
                RequestFailProductDTO failProduct = failList.get(i);
                String flag = failProduct.getFlag();
                if("2".equals(flag)){
                    getAllProductsCategroyUrl(failProduct.getGender(),failProduct.getCategoryName(),failProduct.getUrl());
                }else if("1".equals(flag)){
                    //不校验 productModel 是否存在
                    //grapProductDetailMaterials(failProduct.getGender(),failProduct.getCategoryName(),failProduct.getUrl());
                }else if("0".equals(flag)){
                    //不校验 productModel 是否存在
                   // grapProductDetailMaterials(failProduct.getGender(),failProduct.getCategoryName(),failProduct.getUrl());
                }
            }
            System.out.println("==========fr请求失败的商品及品类信息 结束===================================================");
            logger.info("==========fr请求失败的商品及品类信息 结束===================================================");
            exe.shutdown();
            out.close();
            barCodeMap = null;
            failList = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 处理 品类 下的 商品列表信息
     *
     * @param sex 性别
     * @param categoryName  品类名称
     * @param categoryUrl  品类url
     */
    /**
     * 处理 品类 下的 商品列表信息
     *
     * @param sex 性别
     * @param categoryName  品类名称
     * @param categoryUrl  品类url
     */
    public void getAllProductsCategroyUrl(String sex,String categoryName,String categoryUrl) {
        //请求分页的参数时 ni
        if (categoryUrl == null || "".equals(categoryUrl)) {
            return;
        }
        try {
            Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
            Header header1=new Header("cookie",cookies);
            Header[] headers = new Header[2];
            headers[0] = header;
            headers[1]=header1;
            HttpResponse response = HttpUtils.get(categoryUrl,headers);
            if (response.getStatus() == 200) {
                String htmlContent = response.getResponse();
                Document doc = Jsoup.parse(htmlContent);
                //获取第一页商品数据
                //第二步：拿到品类列表的DOM
                grapProductListByCategoryUrlOrDoc(sex,categoryName,categoryUrl,1);
                Elements countNumberElements =null;

                    String countStr = doc.select("div.pager").select("p.amount").text();
                    countStr=countStr.split("共")[1];
                    countStr=countStr.replace("个","").trim();
                int productCountNumberStr=Integer.valueOf(countStr);

                int pageSize =12;
                int page=(productCountNumberStr-1)/pageSize+1;
                //System.out.println(page);
                if (page>1){
                    for (int i=1;i<page;i++){
                       /* if (i==1){*/
                            String url=categoryUrl+"?p="+(i+1);
                            //System.out.println(url);
                            grapProductListByCategoryUrlOrDoc(sex,categoryName,url,i+1);
                        /*}else {
                            String url=categoryUrl+"?browsing_country=GB&currency=GBP&language=zh"+"&pn="+(i+1);
                            String url=categoryUrl;
                        }*/

                    }
                }
            } else{
                //添加到 失败 请求中
                failList.add(new RequestFailProductDTO(sex, categoryName, categoryUrl, "1"));
                System.err.println("fr-请求商品品类 地址出错  "+categoryUrl);
                logger.error("fr-请求商品品类 地址出错categoryUrl {}  "+categoryUrl);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 拉取 品类 下的 商品列表信息   主要获取到每个商品的url为了跳转到详情页
     *
     * @param sex 性别
     * @param categoryName  品类名称
     * @param categoryUrl  品类url
     */
    public void grapProductListByCategoryUrlOrDoc(String sex,String categoryName,String categoryUrl,Integer page) {
        Document doc=null;

        try {
            if(doc==null&&categoryUrl!=null){
                Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
                Header header1=new Header("cookie",cookies);
                Header[] headers = new Header[2];
                headers[0] = header;
                headers[1]=header1;
                HttpResponse response = HttpUtils.get(categoryUrl,headers);
                if (response.getStatus() == 200) {
                    String htmlContent = response.getResponse();
                    doc = Jsoup.parse(htmlContent);
                }else{
                    //添加到 失败 请求中
                    failList.add(new RequestFailProductDTO(sex, categoryName, categoryUrl, "1"));
                    System.out.println("fr-请求商品品类 地址出错categoryUrl{}"+categoryUrl);
                    loggerError.error("fr-请求商品品类 地址出错categoryUrl{}"+categoryUrl);
                    return;
                }
            }
            Elements elements = doc.select(".category-products ul li");
            //获取商品列表页数据
            /*String AjaxUrl="";
            String flag="";
            if (categoryUrl.contains("lothing")){
                AjaxUrl="https://fsm.attraqt.com/zones-js.aspx?version=18.3.3&siteId=6f55b353-a747-4be6-bf94-6da365951803" +
                        "&UID=33ea2f66-7315-6e9e-991a-b8f202e1e1fd&SID=02166fc6-ec82-67fe-b382-5ca4eb2a5dde&referrer=&sitereferrer=" +
                        "&pageurl=https%3A%2F%2Fwww.lkbennett.com%2FClothing%23esp_pg%3D"+page+"&zone0=category&zone1=advert1&zone2=advert2" +
                        "&zone3=advert3&zone4=advert4&zone5=advert5&facetmode=data&mergehash=true&currency=GBP&language=en-GB" +
                        "&config_categorytree=collections%2Fclothing%2F&config_category=clothing&config_fsm_sid=02166fc6-ec82-67fe-b382-5ca4eb2a5dde" +
                        "&config_fsm_returnuser=0&config_fsm_currentvisit=13%2F09%2F2018&config_fsm_visitcount=1&bucket_sys_price=";
                flag="clothing";
            }else {
                AjaxUrl="https://fsm.attraqt.com/zones-js.aspx?version=18.3.3&siteId=6f55b353-a747-4be6-bf94-6da365951803" +
                        "&UID=33ea2f66-7315-6e9e-991a-b8f202e1e1fd&SID=986c63c7-26be-34b0-644a-da431f0e5f40&referrer=&sitereferrer=" +
                        "&pageurl=https%3A%2F%2Fwww.lkbennett.com%2FShoes%23esp_pg%3D"+page+"&zone0=category&zone1=advert1&zone2=advert2" +
                        "&zone3=advert3&zone4=advert4&zone5=advert5&facetmode=data&mergehash=true&currency=GBP&language=en-GB" +
                        "&config_categorytree=collections%2Fshoes%2F&config_category=shoes&config_fsm_sid=986c63c7-26be-34b0-644a-da431f0e5f40" +
                        "&config_fsm_returnuser=1&config_fsm_currentvisit=15%2F09%2F2018&config_fsm_visitcount=3" +
                        "&config_fsm_lastvisit=14%2F09%2F2018&bucket_sys_price==";
                flag="shoes";
            }


            HttpResponse liebiaoResponse=HttpUtils.get(AjaxUrl,headers);*/
           // Elements select = doc.select(".regular-price>.price");
            System.out.println("列表上的详情页数量：" + elements.size());
                int productSize = elements.size();
                for (int i = 0; i < productSize; i++) {
                    String elementhref = elements.get(i).select("div.item-area").select("a").first().attr("href");
                    System.out.println("连接为："+elementhref);
                    urlCount++;
                    //第三步：跳转到处理商品详情
                    grapProductDetailMaterials(sex,categoryName,elementhref,"",flag);
                }
                System.out.println("urlCount:" + urlCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        String categoryUrl="https://www.arket.com/en_gbp/women/tops/product.crew-neck-t-shirt-pink.0630665006.html";
        try {
            Document document = Jsoup.connect(categoryUrl).get();
            System.out.println(document);
            Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
            Header[] headers = new Header[1];
            headers[0] = header;
            try {
                HttpResponse response = HttpUtils.get(categoryUrl,headers);
                System.out.println(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        MrMrsFR mrMrsFR = new MrMrsFR();
        mrMrsFR.grapProductDetailMaterials("","","https://www.mmi.it/cn/embroidery-downjacket-superlight-nylonvb-finnraccoon-fur-ov131e","","");

    }
    /**
     * 获取该商品的材质信息
     * @param productDetailUrl 商品详情url
     */
    /*int 材质Count=0;
    int measurementCount=0;*/

    private void grapProductDetailMaterials(String sex,String categoryName,String productDetailUrl,String season,String flag){


        try {



            Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36");
            //Header header1=new Header("cookie","_gcl_au=1.1.791908118.1541382439; _ga=GA1.2.837078211.1541382439; _gid=GA1.2.1378732055.1541382439; __auc=4444e96d166e18d80856c6f4cab; cookieAlertClosedFlag=1; frontend=jr9pdrpqc4lt2gj3j4kageej96; frontend_cid=3SF7koQ6rtNNqK8f; __asc=3fe09610166e843a8ab872c82c2; external_no_cache=1; _chutm={}; _fbp=fb.1.1541495042400.269682048; mmi_change_quote_checkout=0; experiment=0; mmi_change_quote=0; tss_country_code=IT; mmi_change_quote_store_id=22; tss_mage_run_code=eu_cn; _dc_gtm_UA-28597932-1=1; _ch={%22sid%22:%228341d54f-10f0-4138-acf8-8c510abe4c10%22%2C%22workspaceId%22:%2289ae6ca0-db42-4f7a-a5b1-17be490fb467%22%2C%22nodeId%22:%22cba96d3b-ddb1-4a22-8469-7ec1425104db%22%2C%22token%22:%22a13fbe46bb1241ceaf6afe75efe99db56994d7557dc4499f80946999ed3c5422%22%2C%22context%22:%22ECOMMERCE%22%2C%22contextInfo%22:{%22store%22:{%22id%22:%223%22%2C%22name%22:%22Chinese%22%2C%22country%22:%22IT%22%2C%22website%22:%22https://www.mmi.it/cn/%22%2C%22type%22:%22ECOMMERCE%22}}}; __atuvc=48%7C45; __atuvs=5be15904072374ed005");
            Header header1=new Header("cookie",cookies);
            //Header header1 = new Header("accept-language", "zh-CN,zh;q=0.9");

            Header[] headers = new Header[2];
            headers[0] = header;
            headers[1]=header1;
            HttpResponse response = HttpUtils.get(productDetailUrl,headers);
            if (response.getStatus() == 200) {
                String htmlContent = response.getResponse();
                Document doc = Jsoup.parse(htmlContent);
                //商品描述
                String productDesc=doc.select(".short-description").text().replace(",","");
                String matrerial="";
                //商品材质
                String completeDescription = doc.select(".short-description").select("#completeDescription").toString().replace("</span>","").replace("<span id=\"completeDescription\">","");
                System.out.println(completeDescription);
                String[] descs = completeDescription.split("<br>");
                for (String everydesc:descs){
                    if (everydesc.contains("%")){
                        matrerial+=everydesc;
                    }
                }
                matrerial=matrerial.replace(",",".");
                String productName=doc.select("div.product-name").text().replace(",","").trim();
                //产地
                String made="Italy";
                //测量信息
                String measurement="";
                /*Elements select = doc.select("span.regular-price>span.price");
                String text = doc.select("span.price").text();*/
                String price=doc.select(".regular-price>.price").text().replace("￥","").replace("€","").replace(",","").trim();
                byte bytes[] = {(byte) 0xC2,(byte) 0xA0};
                String UTFSpace = new String(bytes,"utf-8");
                price = price.replaceAll(UTFSpace, "&nbsp;").replaceAll("&nbsp;","");
                String oldprice="";
                String saleprice="";
                if (StringUtils.isBlank(price)){
                    oldprice=doc.select("span.price").first().text().replace("￥","").replace("€","").replace(",","").trim();
                    oldprice=oldprice.replaceAll(UTFSpace, "&nbsp;").replaceAll("&nbsp;","");
                    saleprice=doc.select("span.price").last().text().replace("￥","").replace("€","").replace(",","").trim();
                    saleprice=saleprice.replaceAll(UTFSpace, "&nbsp;").replaceAll("&nbsp;","");
                }else {
                   oldprice=price;
                   saleprice=price;
                }

                //Elements colorElements=doc.select(".last").select(".variant-selector").first().select("ul").select("li");
                Element spuElement = doc.select("script[type=\"application/ld+json\"]").last();
                //String price=doc.select(".productDesc").select(".prices").select("span[itemprop=\"price\"]").text();
                String spuText=spuElement.toString().split("json\">\n" )[1].split("</script>")[0];
                JsonObject spuJson=(JsonObject)new JsonParser().parse(spuText);
                //System.out.println(spuJson);
                String SPU=spuJson.get("productID").getAsString();
                //颜色
                Elements colorElements=doc.select(".product-options").select("script");
                Element colorAndSizeElement=null;
                Element imageElement=null;
                for (Element e:colorElements){
                    if (e.toString().contains("<script type=\"text/javascript\">\n" +
                            "    var spConfig = new Product.Config")){
                        colorAndSizeElement=e;
                    }else if (e.toString().contains("var amConfAutoSelectAttribute = 1;")){
                        imageElement=e;
                    }
                    if (colorAndSizeElement!=null&&imageElement!=null){
                        break;
                    }
                }
                String information=colorAndSizeElement.toString().split("Config\\(")[1].split("\\);")[0];

                //information=information.substring(0,information.length()-2);
                JsonObject jsonObject = (JsonObject) new JsonParser().parse(information);
                JsonObject jsonAttributes =jsonObject.getAsJsonObject("attributes");
                JsonObject jsonColors= jsonAttributes.getAsJsonObject("92");
                JsonObject jsonSizes= jsonAttributes.getAsJsonObject("144");
                JsonArray jsonColoroptions = jsonColors.get("options").getAsJsonArray();
                //int colorSingerOrMoreFlag=0;
                for (int i=0;i<jsonColoroptions.size();i++){
                    String color = jsonColoroptions.get(i).getAsJsonObject().get("label").getAsString();
                    String colorId=jsonColoroptions.get(i).getAsJsonObject().get("id").getAsString();
                    JsonArray products = jsonColoroptions.get(i).getAsJsonObject().get("products").getAsJsonArray();
                  /*  System.out.println(color);
                    System.out.println(products);*/
                    SPU=SPU+"_"+colorId;
                    //String image=spuJson.get("image").getAsString();

                        String pics = getImages(doc, color, colorId, imageElement);

                    /*if (jsonColoroptions.size()>1){
                        colorSingerOrMoreFlag=1;
                    }*/
                    //made
                    getSize(sex,categoryName,color,products,jsonSizes,productDesc,made,matrerial,measurement,productName,oldprice,saleprice,productDetailUrl,SPU,pics);
            }
                System.out.println(jsonColoroptions);
                System.out.println(jsonSizes);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getImages(Document doc,String color,String colorId,Element imageElement) {
        //JsonArray images = json.get("image").getAsJsonArray();
        String pics="";
        String text = imageElement.text();
        String s = imageElement.toString().split("AmConfigurableData\\(")[1].split("\\);")[0];
        JsonObject jsonObject = (JsonObject) new JsonParser().parse(s);
        JsonObject asJsonObject = jsonObject.getAsJsonObject(colorId);
        String imageUrl=asJsonObject.get("media_url").getAsString();
        Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36");
        Header[] headers = new Header[1];
        headers[0] = header;
        HttpResponse imagesResponse= null;
        try {
            imagesResponse = HttpUtils.get(imageUrl,headers);
        } catch (Exception e) {
            System.out.println("发送图片请求失败");
            e.printStackTrace();
        }
        String response = imagesResponse.getResponse();
        Document imagesDoc = Jsoup.parse(response);
            Elements hrefElements = imagesDoc.select("ul").select("li");
            for (int i=0;i<hrefElements.size();i++){
                String imageStr=hrefElements.get(i).select("a").attr("href").toString();
                if (i==0){
                    pics=imageStr;
                }else {
                    pics=pics+"|"+imageStr;
                }
            }

        /*//boolean flag=false;
            for(int i=0;i<images.size();i++){
                if (i==0){
                    pics=images.get(i).toString();
                }else {
                    pics=pics+"|"+images.get(i).toString();
                }
            }
            Elements select = doc.select("input[name=\"product\"]");
            String value = select.attr("value");
            System.out.println(value);
            System.out.println(select);*/

        return pics;
    }

    private void getSize(String sex, String categoryName, String color, JsonArray products, JsonObject jsonSizes, String productDesc, String made, String matrerial, String measurement,String productName,String oldprice,String saleprice,String productDetailUrl,String SPU,String pics) {
        try {
            JsonArray jsonSizesOptions = jsonSizes.get("options").getAsJsonArray();
            System.out.println(jsonSizesOptions);
            Map<String,String>map=new HashMap();
            for (int i=0;i<jsonSizesOptions.size();i++){
                String sizeValue=jsonSizesOptions.get(i).getAsJsonObject().get("label").getAsString();
                JsonArray productsSize = jsonSizesOptions.get(i).getAsJsonObject().get("products").getAsJsonArray();
                for (int j=0;j<productsSize.size();j++){
                    map.put(productsSize.get(j).getAsString(),sizeValue);
                }

            }
            System.out.println(map);
            for (int i=0;i<products.size();i++){
                String sizeValue=products.get(i).getAsString();
                String qty=map.get(sizeValue);
                if (StringUtils.isBlank(qty)){
                    qty=NO_STOCK;
                }else {
                    qty=IN_STOCK;
                }
                LKBennettDTO product=new LKBennettDTO();
                product.setGender(sex);
                product.setBrand(categoryName);
                product.setCategory("coat");
                product.setSPU(SPU);
                product.setProductModel(SPU);
                product.setSeason("");
                product.setMaterial(matrerial);
                product.setColor(color);
                product.setSize(map.get(sizeValue));
                product.setQty(qty);
                product.setProName(productName);
                product.setForeignPrice(oldprice);
                product.setDomesticPrice(oldprice);
                product.setSalePrice(saleprice);
                product.setPics(pics);
                product.setMade(made);
                product.setDesc(productDesc);
                product.setDetailLink(productDetailUrl);
                product.setMeasurement(measurement);
                product.setSupplierId("");
                product.setSupplierNo(supplierNo);
                product.setSupplierSkuNo("");
                product.setChannel("www.mmi.it");
                System.out.println(product);
                exportExcel(product);
                barCodeMap.put(SPU,product);
            }
        }catch (Exception e){
            System.out.println("尺码或封装有问题");
            e.printStackTrace();
        }

    }





    /*public static void main(String[] args) {
        Document doc=null;
        String categoryUrl="https://www.lkbennett.com/product/SCFERNHAIRCALFNaturalNatural%20Leopard~Fern-Animal-Print-Calf-Hair-Closed-Courts-Leopard%20print";
        //grapProductDetailMaterials(sex,categoryName,elementhref,"");
        try {
            LKBennettFR object=new LKBennettFR();
            object.grapProductDetailMaterials("women","lkbennett",categoryUrl,"","shoes");
            if(doc==null&&categoryUrl!=null) {
                Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
                Header[] headers = new Header[1];
                headers[0] = header;
                HttpResponse response = HttpUtils.get(categoryUrl, headers);
                if (response.getStatus() == 200) {
                    String htmlContent = response.getResponse();
                    doc = Jsoup.parse(htmlContent);
                    System.out.println(doc);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/
    private String getPics(Document doc,String colorUrl) {
        Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
        Header[] headers = new Header[1];
        headers[0] = header;
        String picStr=doc.select("meta").select("meta[name=\"image\"]").attr("content");
        String sign=colorUrl.split("~")[1].split("-")[0];
        picStr=picStr.split("/"+sign)[0];
        picStr=picStr.substring(0,picStr.length()-1);
        String pics="";
        List<String> picList=new ArrayList();
        picList.add(picStr+"a");
        picList.add(picStr+"b");
        picList.add(picStr+"c");
        picList.add(picStr+"d");
        picList.add(picStr+"e");
        picList.add(picStr+"f");
        picList.add(picStr+"g");
        Boolean flag=true;
        try {
            for(String everyPic:picList){
                HttpResponse picResponse=HttpUtils.get(everyPic,headers);
                if(picResponse.getStatus()!=200){
                    System.out.println(picResponse.getStatus());
                    continue;
                }else {

                    if (flag==true){
                        pics=everyPic;
                        flag=false;
                    }else {
                        pics=pics+"|"+everyPic;
                    }
                }
            }
            return pics;
        } catch (Exception e) {
            e.printStackTrace();
        }
            return "";
    }

    /**
     * 导出单个商品信息到csv 文件（追加）
     * @param dto 商品信息DTO
     */
    private static void exportExcel(LKBennettDTO dto) {
        if(dto==null||dto.getProName()==null||"".equals(dto.getProName())){
            return;
        }
        //继续追加
        StringBuffer buffer = new StringBuffer();
        try {
            buffer.append(dto.getGender()).append(splitSign);
            buffer.append((dto.getBrand()==null||"".equals(dto.getBrand()))?"":dto.getBrand()).append(splitSign);

            buffer.append(dto.getCategory()).append(splitSign);
            buffer.append(dto.getSPU()).append(splitSign);

            buffer.append(dto.getProductModel()).append(splitSign);
            buffer.append((dto.getSeason()==null||"".equals(dto.getSeason()))?"四季":dto.getSeason()).append(splitSign);

            buffer.append(dto.getMaterial()).append(splitSign);
            buffer.append((dto.getColor()==null||"".equals(dto.getColor()))?"":dto.getColor()).append(splitSign);

            buffer.append((dto.getSize()==null||"".equals(dto.getSize()))?"":dto.getSize()).append(splitSign);
            buffer.append((dto.getProName()==null||"".equals(dto.getProName()))?"":dto.getProName()).append(splitSign);

            buffer.append(dto.getForeignPrice()).append(splitSign);
            buffer.append(dto.getDomesticPrice()).append(splitSign);
            buffer.append(dto.getSalePrice()).append(splitSign);
            buffer.append(dto.getQty()).append(splitSign);

            buffer.append(dto.getMade()).append(splitSign);

            buffer.append(dto.getDesc()).append(splitSign);
            buffer.append(dto.getPics()).append(splitSign);

            buffer.append(dto.getDetailLink()).append(splitSign);
            //测量信息替代描述
            buffer.append(dto.getMeasurement()).append(splitSign); //尺寸描述

            buffer.append(dto.getSupplierId()).append(splitSign);
            buffer.append(dto.getSupplierNo()).append(splitSign);
            buffer.append(dto.getSupplierSkuNo()).append(splitSign);
            buffer.append(dto.getChannel()).append(splitSign);
            buffer.append("\r\n");
            System.out.println(buffer.toString());
            out.write(buffer.toString());
            logger.info(buffer.toString());
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            loggerError.error(e.getMessage());
        }
    }
}
