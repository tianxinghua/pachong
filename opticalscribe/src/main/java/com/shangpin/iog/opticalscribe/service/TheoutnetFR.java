package com.shangpin.iog.opticalscribe.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shangpin.iog.opticalscribe.dto.LKBennettDTO;
import com.shangpin.iog.opticalscribe.dto.RequestFailProductDTO;
import com.shangpin.iog.opticalscribe.tools.CSVUtilsSolveRepeatData;
import com.shangpin.iog.pavinGroup.util.HttpResponse;
import com.shangpin.iog.pavinGroup.util.HttpUtils;
import com.shangpin.iog.service.EventProductService;
import com.shangpin.iog.service.ProductFetchService;
import net.sf.json.JSONObject;
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

@Component("theoutnetFR")
public class TheoutnetFR {
    private int urlCount=0;
    //有库存
    private static final String IN_STOCK = "1";
    //无库存
    private static final String NO_STOCK = "0";
    private static String supplierId="2018082702041";
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
            Header[] headers = new Header[1];
            headers[0] = header;
            HttpResponse response = HttpUtils.get(categoryUrl,headers);
            if (response.getStatus() == 200) {
                String htmlContent = response.getResponse();
                Document doc = Jsoup.parse(htmlContent);
                //获取第一页商品数据
                //第二步：拿到品类列表的DOM
                String category="clothing";
                int categorySet=categoryUrl.split("/").length-1;
                category=categoryUrl.split("/")[categorySet];
                grapProductListByCategoryUrlOrDoc(sex,categoryName,categoryUrl,1,category);
                Elements countNumberElements =null;

                    String countStr = doc.select("#mainContent").select(".sr-nav-items").select("span.totalResultsCount").text();
                int productCountNumberValue=Integer.valueOf(countStr);

                int pageSize =96;
                int page=(productCountNumberValue-1)/pageSize+1;
                //System.out.println(page);
                if (page>1){
                    for (int i=1;i<page;i++){
                       /* if (i==1){*/
                            String url=categoryUrl+"?page="+(i+1);
                            //System.out.println(url);
                            grapProductListByCategoryUrlOrDoc(sex,categoryName,url,i+1,category);
                        /*}else {
                            String url=categoryUrl+"?browsing_country=GB&currency=GBP&language=zh"+"&pn="+(i+1);
                            String url=categoryUrl;
                        }*/

                    }
                    /*for (int i=1;i<page;i++){
                        *//* if (i==1){*//*
                        String url=categoryUrl+"?page="+(i+1);
                        //System.out.println(url);
                        grapProductListByCategoryUrlOrDoc(sex,categoryName,url,i+1);
                        *//*}else {
                            String url=categoryUrl+"?browsing_country=GB&currency=GBP&language=zh"+"&pn="+(i+1);
                            String url=categoryUrl;
                        }*//*

                    }*/
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

            //获取商品列表页数据
            //.sr-items .products >ul>li.item
             Elements elements=doc.select(".sr-items").select(".products").select("ul").select("li.item");


                System.out.println("列表上的详情页数量：" + elements.size());
                int productSize = elements.size();
                for (int i = 0; i < productSize; i++) {
                    String elementhref = elements.get(i).attr("data-product-link");
                    System.out.println("连接为："+elementhref);
                    urlCount++;
                    //第三步：跳转到处理商品详情
                    grapProductDetailMaterials(sex,categoryName,elementhref,"2018春夏",category);
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
    private void grapProductDetailMaterials(String sex,String categoryName,String productDetailUrl,String season,String category){


        try {

            Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
            Header[] headers = new Header[1];
            headers[0] = header;
            HttpResponse response = HttpUtils.get(productDetailUrl,headers);

            if (response.getStatus() == 200) {
                String htmlContent = response.getResponse();
                Document doc = Jsoup.parse(htmlContent);
               /* //商品描述
                String productDesc=doc.select(".panel").select(".panel-collapse").select("div.panel-description").select("p").text().replace(",","");
                String matrerial=doc.select(".panel").select("div#collapse2").select(".panel-body-custom").select("p").text();
                //商品材质
                matrerial=matrerial.split("Wash Method")[0].replace(",",".");
                System.out.println(matrerial);

                //产地
                String made="UK";
                //测量信息
                String measurement=doc.select(",panel").select("div#collapse4").select("p").text().replace(",",".");
                System.out.println(measurement);*/
                //.item-content__actions  .colors-list>div.color-selection
                Elements colorElements=doc.select(".item-content__actions").select(".colors-list").select("div.color-selection");

                System.out.println("颜色数量为："+colorElements.size());
                if (colorElements.size()==0){
                    String colorUrl=productDetailUrl;
                    System.out.println("只有一个颜色："+colorUrl);
                    getProductColorAndSize(sex,categoryName,colorUrl,season,category);
                }
                for (int i=0;i<colorElements.size();i++){
                    if (colorElements.size()==1){
                        String colorUrl=productDetailUrl;
                        System.out.println("只有一个颜色："+colorUrl);
                        getProductColorAndSize(sex,categoryName,colorUrl,season,category);

                    }else {
                        String colorUrl=colorElements.get(i).select("div.inner").select("a").attr("href");
                        if (StringUtils.isEmpty(colorUrl)){
                            colorUrl=productDetailUrl;
                        }
                        System.out.println(colorUrl+"第"+(i+1)+"颜色");
                        getProductColorAndSize(sex,categoryName,colorUrl,season,category);


                    }



                }




            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void getProductColorAndSize(String sex,String categoryName,String colorUrl,String season,String category){
        try {
            Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
            Header[] headers = new Header[1];
            headers[0] = header;
            HttpResponse response = HttpUtils.get(colorUrl,headers);
            if (response.getStatus() == 200) {
                String htmlContent = response.getResponse();
                Document doc = Jsoup.parse(htmlContent);
                //商品描述.item-info>ul>li>.item-content__shipping  .selected .item-content__shipping
                String productDesc=doc.select(".accordion__block").select(".item-content__shipping").first().text().replace(",",".");
                //商品材质 .item-info .compositionInfo
                String matrerial=doc.select(".item-info").select(".compositionInfo").text().replace(",",".");
                //商品SPU .item-info .accordion__info
                String SPU=doc.select(".item-info").select(".accordion__info").select("div:not(.need-help)").text().replace(",",".");
                SPU=SPU.split("code:")[1].trim().replace(" ","");
                System.out.println("SPU:"+SPU);
                //测量信息 .item-info ul li.sizeFitTab
                String measurement = doc.select("div.item-content__accordion").select("ul.accordion__content").text().replace(",","");
                if (StringUtils.isEmpty(measurement)){
                    measurement=doc.select("div.item-content__accordion").select(".size-and-fit").text();
                }
                //String  measurement = elements.select("div.item-content__accordion").select("ul.accordion__content").text();
                String made="";
                //产地 .item-info>ul>li.selected ul>li
                Elements madeElements=doc.select(".item-info").select("ul").select("li.selected").select("ul").select("li");
                for (int i=0;i<madeElements.size();i++) {
                    String text=madeElements.get(i).text();
                    if (text.contains("Made in")){
                        made=text.replace(",",".");
                    }
                }
                //商品图片
                String pics=getPics(doc,colorUrl);
                System.out.println(pics);
                //商品价格

                //String price=doc.select(".product-details").select(".price").text().replace("£","");
                //price = price.replace(",","").trim().replace(",","");
                //.price span.full.price
                String price="";
                //.price span.discounted.price
                String salePrice="";
                //
                price=doc.select(".price").select("span").select(".full.price").select("span.value").text().trim().replace(",","");
                salePrice=doc.select(".price").select("span").select(".discounted.price").select("span.value").text().trim().replace(",","");
                if (StringUtils.isEmpty(salePrice)){
                    salePrice=price;
                }
                if (StringUtils.isEmpty(price)){
                    price=salePrice;
                }
                if (StringUtils.isEmpty(price)&&StringUtils.isEmpty(salePrice)){
                    price=doc.select(".item-content__price").select(".price").select("span.value").first().text().trim().replace(",","");
                    salePrice=price;
                }
                //颜色 .item-content__selected-color span.selectionLabel
                String color=doc.select(".item-content__selected-color").select("span.selectionLabel").text();
                //商品名称
                String proName=doc.select(".item-content__title").text().trim().replace(",","");
                System.out.println("商品名称："+proName);
                Elements sizeElements=doc.select(".item-content__sizes").select("ul").select("li");
                    for (int j=0;j<sizeElements.size();j++){
                        try {
                    String sizeText=sizeElements.get(j).text();
                    String qtyDesc=sizeElements.get(j).attr("class");
                    String jsonStr= sizeElements.get(j).attr("data-ytos-size-model");
                    //JsonObject jsonObject = (JsonObject) new JsonParser().parse(jsonStr);
                    //Boolean qtyDesc=jsonObject.get("IsLastAvailableForSelectedColor").getAsBoolean();
                            String sizeValue="";
                            String qty="";
                            if (sizeElements.get(j).select(".sizeLabel").text().equals("--")){
                                sizeValue="U";
                            }
                    else if (sizeText.contains("-")){
                        sizeValue=sizeText.split("-")[0].trim();
                        qty=sizeText.split("-")[1];
                    }else {
                        sizeValue=sizeText.trim();

                    }
                    if (sizeValue.equalsIgnoreCase("ONESIZE")){
                        continue;
                    }
                    //String qty=sizeElements.get(j).select("span.availabilityLabel").text();
                        System.out.println(sizeValue+":"+qty);
                    String tempty=IN_STOCK;
                    if (qty.contains("Sold out")||qtyDesc.contains("is-disabled")||qtyDesc.contains("is-soldOut")){
                        tempty=NO_STOCK;
                    }
                        LKBennettDTO product=new LKBennettDTO();
                        product.setGender(sex);
                        product.setBrand(categoryName);
                        product.setCategory(category);
                        product.setSPU(SPU+"\t");
                        product.setProductModel(SPU+"\t");
                        product.setSeason(season);
                        product.setMaterial(matrerial);
                        product.setColor(color);
                        product.setSize(sizeValue);
                        product.setProName(proName);
                        product.setForeignPrice(price);
                        product.setDomesticPrice(price);
                        product.setSalePrice(salePrice);
                        product.setPics(pics);
                        product.setQty(tempty);

                       product.setMade(made);
                        product.setDesc(productDesc);
                        product.setDetailLink(colorUrl);
                        product.setMeasurement(measurement);
                        product.setSupplierId("");
                        product.setSupplierNo("S0000976");
                        product.setSupplierSkuNo(SPU+"-"+sizeValue);
                        product.setChannel("www.theoutnet.com");
                        System.out.println(product);
                        exportExcel(product);
                        barCodeMap.put(SPU,product);
                        System.out.println("-------------------已拉取了："+barCodeMap.size()+"个SPU");
                    } catch (Exception e) {
                            e.printStackTrace();
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
        Header header = new Header("user-agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
        Header[] headers = new Header[1];
        headers[0] = header;
       /* try {
            Document doc=Jsoup.connect("https://www.loewe.com/eur/en/women/ready-to-wear?sz=15&start=15&format=page-element").header("user-agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36")
                    .cookie("cookie","__cfduid=db13f4e1362205ed46882dece0c2118521540274011; dwac_dbe0a10617e57472113ac114ca=ZpaWUR2TvUjfWTvrkOEaBM3vCjLtE1-OB8E%3D|dw-only|||EUR|false|Europe%2FMadrid|true; sid=ZpaWUR2TvUjfWTvrkOEaBM3vCjLtE1-OB8E; dispatcherRedirect=Search-Show|cgid=w_womenswear&format=page-element&start=60&sz=60; dwanonymous_09ca7ed58839320229f1a601b6c9e62b=deR6bSIKL1mMjEnzgjkLlcIaBW; dwsecuretoken_09ca7ed58839320229f1a601b6c9e62b=IiDs_3dDQnJKrKZd7aBEgNIePFRH9slWbg==; dwsid=irVcwfZWm0n5wOqTYJ1qYCHMwKIG1nmPIViNqY-AIwZVqHbQhNAtNCcu8UoLx3SKGi5c29KKUXnKDA08egJ3-Q==; dwac_1709df91f68b756bdadec56b11=ZpaWUR2TvUjfWTvrkOEaBM3vCjLtE1-OB8E%3D|dw-only|||CNY|false|Europe%2FMadrid|true; dwanonymous_e26fcb3658db420d19f2288ae971792b=abt7XbzqZfPMBpWj94CJxkRHo3; dwsecuretoken_e26fcb3658db420d19f2288ae971792b=MzHTso-3dkjuV6f1Cny9sYI1SpEvxOeVEg==; __cq_dnt=0; dw_dnt=0; ABTastySession=referrer%3D__landingPage%3Dhttps%3A//www.loewe.com/chn/zh_CN/arrival__referrerSent%3Dtrue; _gcl_au=1.1.499047460.1540274015; __55=%7B%22userId%22%3Anull%2C%22ms%22%3A%22non-member%22%2C%22st%22%3A%22regular%22%2C%22vF0%22%3A1540274015046%2C%22vF%22%3A%22new%22%7D; __gaLoewe=GA1.2.1900429995.1540274015; __gaLoewe_gid=GA1.2.1073806115.1540274015; __cq_uuid=aa22f320-bb38-11e8-a1cf-8b10d9af18bf; __cq_bc=%7B%22bbpc-LOE_CHN%22%3A%5B%7B%22id%22%3A%22%22%2C%22alt_id%22%3A%22D1283980TO-2556%22%2C%22type%22%3A%22raw_sku%22%7D%2C%7B%22id%22%3A%22D1283980TO%22%2C%22type%22%3A%22vgroup%22%2C%22alt_id%22%3A%22D1283980TO-2556%22%7D%5D%7D; dwac_d8194eb0b957c04cf044cee055=ZpaWUR2TvUjfWTvrkOEaBM3vCjLtE1-OB8E%3D|dw-only|||USD|false|Europe%2FMadrid|true; dwsecuretoken_735c2848305b2cebbb106b40180c8c92=lCM6ARYOmrvoiYkQKBaz1yXBqAXIG9sDlg==; dispatcherSite=AE-en_CA-LOE_INT; dwanonymous_735c2848305b2cebbb106b40180c8c92=acmxCC2TIiGPL6NvbvBraP9SNv; __cq_seg=0~0.00!1~0.00!2~0.00!3~0.00!4~0.00!5~0.00!6~0.00!7~0.00!8~0.00!9~0.00; ABTasty=uid%3D18102313533320910%26fst%3D1540274013689%26pst%3Dnull%26cst%3D1540274013689%26ns%3D1%26pvt%3D6%26pvis%3D6%26th%3D346712.452322.3.3.1.0.1540274025732.1540274240695.1_347481.453235.6.6.1.1.1540274013724.1540275013917.1_347647.453439.3.3.1.0.1540274025756.1540274240711.1; OptanonConsent=landingPath=NotLandingPage&datestamp=Tue+Oct+23+2018+14%3A10%3A14+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&version=3.6.19&AwaitingReconsent=false&groups=0_116493%3A1%2C1%3A1%2C104%3A1%2C0_116494%3A1%2C0_122812%3A1%2C0_122896%3A1%2C2%3A1%2C101%3A1%2C0_125933%3A1%2C3%3A1%2C0_116495%3A1%2C102%3A1%2C0_116452%3A1%2C0_122813%3A1%2C4%3A1%2C0_125932%3A1; cookiePopin=3; cqcid=deR6bSIKL1mMjEnzgjkLlcIaBW")
                    .get();
            System.out.println(doc);
            Elements select = doc.select("#search-result-items");
            System.out.println(select);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        TheoutnetFR object=new TheoutnetFR();
        String categoryUrl="https://www.loewe.com/eur/en/women/ready-to-wear?sz=15&start=15&format=page-element";
        HttpResponse response = null;
        try {
            response = HttpUtils.get(categoryUrl,headers);

        if (response.getStatus() == 200) {
            String htmlContent = response.getResponse();
            Document doc1 = Jsoup.parse(htmlContent);
            Elements elements=doc1.select("#search-result-items");
            System.out.println(elements);
        }
        //object.grapProductDetailMaterials("","",categoryUrl,"","");

            object.getProductColorAndSize("women","lkbennett",categoryUrl,"","");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private String getPics(Document doc,String colorUrl) {
        Elements imageElements=doc.select(".item-content__images").select("ul").first().select("li");
        String pics="";
        Boolean flag=true;
        try{
            for (int i=0;i<imageElements.size();i++){
                String attr = imageElements.get(i).select("img").attr("src");
                attr=attr.replace("_11","_14");
                if (flag==true){
                    pics=attr;
                    flag=false;
                }
                else {
                    pics = pics+ "|"+attr;
                }
            }
            return pics;
        }catch (Exception e){
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
