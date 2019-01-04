package com.shangpin.iog.opticalscribe.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shangpin.iog.opticalscribe.dto.LKBennettDTO;
import com.shangpin.iog.opticalscribe.dto.RequestFailProductDTO;
import com.shangpin.iog.opticalscribe.tools.CSVUtilsSolveRepeatData;
import com.shangpin.iog.pavinGroup.util.HttpResponse;
import com.shangpin.iog.pavinGroup.util.HttpUtils;
import com.shangpin.iog.service.EventProductService;
import com.shangpin.iog.service.ProductFetchService;
import org.apache.commons.httpclient.Header;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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

@Component("acneStudiosFR")
public class AcneStudiosFR {
    private int urlCount=0;
    //有库存
    private static final String IN_STOCK = "10";
    //无库存
    private static final String NO_STOCK = "0";
    private static String supplierId="2018082702041";
    private static String supplierNo="S0000976";
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
    // 所有品类相对路径以及名称 如： women&&新作：RE(BELLE)&&/zh/ca/women/handbags/new,women&&手提包&波士顿包&&/zh/ca/women/handbags/top-handles
    private static List<String> genderAndNameAndCategoryUris = new ArrayList<>();
    private static String baseUrl="https://www.acnestudios.com";
    private static OutputStreamWriter out= null;
    static String splitSign = ",";
    /**
     * 特殊处理 商品品类名称 字符 如： ,高跟鞋,凉鞋,
     */
    private static String sizeCategoryNames = "";

    static {
        if (null == bdl)
            bdl = ResourceBundle.getBundle("confAcneStudios");
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

        try {

            //每次调用都初始化 barCodeMap 以及 failList
            barCodeMap = new HashMap<>();
            failList = new ArrayList<>();

            int productModeNumber = 0;

            //校验配置信息 是否符合格式要求
            int size = genderAndNameAndCategoryUris.size();
            for (int i = 0; i < size; i++) {

                String[] sexAnduriAndName = genderAndNameAndCategoryUris.get(i).split("&&");
                if(sexAnduriAndName.length!=4) {
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
                getAllProductsCategroyUrl(sexAnduriAndName[0],sexAnduriAndName[1],sexAnduriAndName[2],sexAnduriAndName[3]);

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
                    //getAllProductsCategroyUrl(failProduct.getGender(),failProduct.getCategoryName(),failProduct.getUrl(),failProduct);
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
    public void getAllProductsCategroyUrl(String sex,String categoryName,String categoryUrl,String category) {
        //请求分页的参数时 ni
        if (categoryUrl == null || "".equals(categoryUrl)) {
            return;
        }
        try {
            Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
            Header[] headers = new Header[1];
            headers[0] = header;
            HttpResponse response = HttpUtils.get(categoryUrl,headers);
            if (response.getStatus() == 200) {
                String htmlContent = response.getResponse();
                Document doc = Jsoup.parse(htmlContent);
                //获取第一页商品数据
                //第二步：拿到品类列表的DOM
                grapProductListByCategoryUrlOrDoc(sex,categoryName,categoryUrl,1,category);
                /*Elements countNumberElements =null;

                    String countStr = doc.select(".row").select("div.viewAll").text();
                    countStr=countStr.split("\\(")[1];
                    countStr=countStr.replace(")","");
                int productCountNumberStr=Integer.valueOf(countStr);

                int pageSize =62;
                int page=(productCountNumberStr-1)/pageSize+1;*/
                //System.out.println(page);
               /* if (page>1){
                    for (int i=1;i<page;i++){
                       *//* if (i==1){*//*
                            String url=categoryUrl+"#esp_pg="+(i+1);
                            //System.out.println(url);
                            grapProductListByCategoryUrlOrDoc(sex,categoryName,url,i+1);
                        *//*}else {
                            String url=categoryUrl+"?browsing_country=GB&currency=GBP&language=zh"+"&pn="+(i+1);
                            String url=categoryUrl;
                        }*//*

                    }
                }*/
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
    public void grapProductListByCategoryUrlOrDoc(String sex,String categoryName,String categoryUrl,Integer page,String category) {
        Document doc=null;

        try {
            if(doc==null&&categoryUrl!=null){
                Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
                Header[] headers = new Header[1];
                headers[0] = header;
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
            Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
            Header[] headers = new Header[1];
            headers[0] = header;
            Elements elements=doc.select("body").select(".product-list__items-grid-wrapper").select("ul").select("li.product-list__item-tile");
            System.out.println("列表上的详情页数量：" + elements.size());
            int productSize = elements.size();
            for (int i = 0; i < productSize; i++) {
                String elementhref=elements.get(i).select(".product-list__item-tile.product-tile").attr("data-gtag");
                //String elementhref = elements.get(i).select("div.product-info>div.details>span.name").select("a").attr("href");
                if (elementhref==""){continue;}
                JsonObject jsonObject = (JsonObject) new JsonParser().parse(elementhref);
                elementhref=jsonObject.get("url").getAsString();
                elementhref=baseUrl+elementhref;
                System.out.println("连接为："+elementhref);
                urlCount++;
                //第三步：跳转到处理商品详情
                grapProductDetailMaterials(sex,categoryName,elementhref,"",flag,category);
            }
            System.out.println("urlCount:" + urlCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取该商品的材质信息
     * @param productDetailUrl 商品详情url
     */
    /*int 材质Count=0;
    int measurementCount=0;*/
    private void grapProductDetailMaterials(String sex,String categoryName,String productDetailUrl,String season,String flag,String category){


        try {

            Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
            Header[] headers = new Header[1];
            headers[0] = header;
            HttpResponse response = HttpUtils.get(productDetailUrl,headers);

            if (response.getStatus() == 200) {
                String htmlContent = response.getResponse();
                Document doc = Jsoup.parse(htmlContent);
                //产地
                String made="UK";
                Elements colorElements=doc.select(".product-item__core-information").select(".product-item__detail-row").select("ul.color").select("li");
                if (colorElements.size()!=0){
                    for (int i=0;i<colorElements.size();i++){
                        String colorUrl=baseUrl+colorElements.get(i).select("a").attr("href");
                         getProductColorAndSize(sex,categoryName,colorUrl,made,flag,category);
                     }
                }else {
                    getProductColorAndSize(sex,categoryName,productDetailUrl,made,flag,category);
                }




            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void getProductColorAndSize(String sex,String categoryName,String colorUrl,String made,String flag,String category){
        try {
            Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
            Header[] headers = new Header[1];
            headers[0] = header;
            HttpResponse response = HttpUtils.get(colorUrl,headers);
            if (response.getStatus() == 200) {
                String htmlContent = response.getResponse();
                Document doc = Jsoup.parse(htmlContent);
                String matrerial=doc.select(".product-item__asset>.product-item__detail-row >.product-item__additional-item-border>.first>ul").text().replace(",","").trim();

                //商品图片
                String pics=getPics(doc,colorUrl);
                //商品描述
                String productDesc=doc.select(".product-item__detail-row>div.product-item__detail-info-description").text().replace(",","").trim();
                //测量信息
                String measurement=doc.select(".product-item__detail-row-large-4-4").select(".product-item__detail-info-fit-size-wrapper").select("ul").text().replace(",",".");
                String salePrice="";
                salePrice=doc.select(".product-item__detail").select(".product-item__detail-sticky-wrapper")
                        .select(".product-price").select("span.price-sales").text().replace("£","").replace(" ","").trim();
                String color=doc.select(".product-item__detail-sticky-wrapper").select(".hide-element").select("span[itemprop=\"color\"]").text();
                System.out.println("color="+color);
                Elements elements=doc.select("div.gallery").select("li");
                String SPU=doc.select(".product-item__detail-sticky-wrapper").select(".hide-element").select("span[itemprop=\"sku\"]").text();
                String proName=doc.select(".product-item__detail-name").select(".product-name").text().replace(",",".");
                if (StringUtils.isEmpty(color)){
                    color=proName;
                }
                Elements sizeElements = doc.select(".valuet").select(".row-select").select("option");
                if (sizeElements.size()==0){
                    String size="U";
                    String qty=NO_STOCK;
                    String qtyMessage=doc.select(".product-info-wrapper").select(".product-item__detail-add-to-cart").select(".product-add-to-cart")
                            .text();
                    if (qtyMessage.contains("Add to cart")){
                        qty=IN_STOCK;
                    }
                        LKBennettDTO product=new LKBennettDTO();
                        product.setGender(sex);
                        product.setBrand(categoryName);
                        product.setCategory(category);
                        product.setSPU(SPU);
                        product.setProductModel(SPU);
                        product.setSeason("");
                        product.setMaterial(matrerial);
                        product.setColor(color);
                        product.setSize(size);
                        product.setProName(proName);
                        product.setForeignPrice(salePrice);
                        product.setDomesticPrice(salePrice);
                        product.setSalePrice(salePrice);
                        product.setPics(pics);
                        product.setQty(qty);
                        product.setMade(made);
                        product.setDesc(productDesc);
                        product.setDetailLink(colorUrl);
                        product.setMeasurement(measurement);
                        product.setSupplierId(supplierId+ "\t");
                        product.setSupplierNo(supplierNo);
                        product.setSupplierSkuNo("");
                        product.setChannel("www.acnestudios.com");
                        System.out.println(product);
                        exportExcel(product);
                        barCodeMap.put(SPU,product);
                        System.out.println("-------------------已拉取了："+barCodeMap.size()+"个SPU");
                }

                //System.out.println(sizeElements);
                for (int j=0;j<sizeElements.size();j++){

                    String sizeText=sizeElements.get(j).text();
                    System.out.println("尺码库存："+sizeText);
                    String sizeStock="";
                    if (sizeText.equalsIgnoreCase("SELECT SIZE")){
                        continue;
                    }

                    if (sizeText.contains("Out")||sizeText.contains("Sold out")){
                        String[] strs;
                        strs=sizeText.split("    ");
                        sizeText=strs[0];
                        sizeStock=strs[1];
                    }
                    System.out.println(sizeText);
                    sizeText=sizeText.replace(" ","").trim();
                    String tempty="";
                    try {

                        /*if (flag.equals("clothing")){
                            sizeValue=sizeText.split(",")[0];
                        }else {
                            if ((sizeText.split(",")[0]).contains("|")){
                                sizeValue=sizeText.split(",")[0].split("\\|")[1].trim().replace(" ","");
                            }else {
                                sizeValue=sizeText.split(",")[0].trim().replace(" ","");
                            }

                        }*/
                        //tempty=sizeText.split(",")[1].replace("  ","").trim();
                        LKBennettDTO product=new LKBennettDTO();
                        product.setGender(sex);
                        product.setBrand(categoryName);
                        product.setCategory(category);
                        product.setSPU(SPU);
                        product.setProductModel(SPU);
                        product.setSeason("");
                        product.setMaterial(matrerial);
                        product.setColor(color);
                        product.setSize(sizeText);
                        product.setProName(proName);
                        product.setForeignPrice(salePrice);
                        product.setDomesticPrice(salePrice);
                        product.setSalePrice(salePrice);
                        product.setPics(pics);
                        if (sizeStock.equalsIgnoreCase("Sold Out")){
                            product.setQty(NO_STOCK);
                        }else {
                            product.setQty(IN_STOCK);
                        }

                        product.setMade(made);
                        product.setDesc(productDesc);
                        product.setDetailLink(colorUrl);
                        product.setMeasurement(measurement);
                        product.setSupplierId(supplierId+ "\t");
                        product.setSupplierNo(supplierNo);
                        product.setSupplierSkuNo("");
                        product.setChannel("www.acnestudios.com");
                        System.out.println(product);
                        exportExcel(product);
                        barCodeMap.put(SPU,product);
                        System.out.println("-------------------已拉取了："+barCodeMap.size()+"个SPU");
                    } catch (Exception e) {
                        System.out.println("尺码批策略不匹配");
                    }
                    /*System.out.println(sizeValue);
                    System.out.println(tempty);*/

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        Document doc=null;
        String categoryUrl="https://www.acnestudios.com/uk/en/kelow-dye-poncho/CA0003-AEV.html";
        //grapProductDetailMaterials(sex,categoryName,elementhref,"");
        try {
            AcneStudiosFR object=new AcneStudiosFR();

            object.grapProductDetailMaterials("women","acnestudios",categoryUrl,"","","knitwear");
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

    }
    private String getPics(Document doc,String colorUrl) {
        Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
        Header[] headers = new Header[1];
        headers[0] = header;
        Elements picElements=doc.select(".pdp-item-wrapper").select(".product-item__gallery-container").select(".image-container");
        String picStr="";
        for (int i=0;i<picElements.size();i++){
            if (i==0){
                picStr=baseUrl+picElements.get(i).select("div.product-item__gallery-item-image").select("a").select("img").attr("data-zoom-src");

            }else {
                picStr=picStr+"|"+baseUrl+picElements.get(i).select("div.product-item__gallery-item-image").select("a").select("img").attr("data-zoom-src");

            }

        }
        return picStr;
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
