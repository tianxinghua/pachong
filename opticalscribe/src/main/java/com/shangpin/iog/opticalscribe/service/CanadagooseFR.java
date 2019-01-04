package com.shangpin.iog.opticalscribe.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
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

@Component("canadagooseFR")
public class CanadagooseFR {
    private String deltailCookies="__cfduid=de965a367d12a3dd7e82436ea5d5f56fe1545188792 ;__cq_dnt=1 ;cqcid=bdbJKTvJYpTae4L6xpnzeIVi1C ;dw_dnt=1 ;dwac_76570e40711a415c9b160ab389=z6kR5JovYgXoUiLmRaB4AtL6kYJHd6k-P6E%3D|dw-only|||CNY|false|Etc%2FGMT%2B8|true ;dwac_bdbM6iaaioAiEaaaddLOa2751H=z6kR5JovYgXoUiLmRaB4AtL6kYJHd6k-P6E%3D|dw-only|||CAD|false|Canada%2FEastern|true ;dwanonymous_27b803890ff7266f5c658c198ca9e17b=bdbJKTvJYpTae4L6xpnzeIVi1C ;dwanonymous_4b678b2f3ddcd887e7cd4635d93160c7=abbi8e09kra3ZTiO9jtj4b5AJf ;dwsecuretoken_27b803890ff7266f5c658c198ca9e17b=5khB1Z9pQcJaKS9RI9xtdUq2GfPoRinQOQ== ;dwsecuretoken_4b678b2f3ddcd887e7cd4635d93160c7=Bgm9EoNwSI6GO2XQ4LwIa0drYdgMr8luLw== ;dwsid=Y3UsjGPk0lrCE06Kp2WcWn4mrOq-vBlhFeB2E5ZZzn5OL1zrAjIXD_nT3_pwnLFAVTfXcN7B1-9CCrTwIj8ZUw== ;language=zh ;sid=z6kR5JovYgXoUiLmRaB4AtL6kYJHd6k-P6E";
    private String listCookies="__cfduid=df7118f5f6bb50efdf8565f4e2c141d4b1545189246 ;__cq_dnt=1 ;cqcid=bdY0KdBLpdwYBeUJWSWbKxLWa4 ;dw_dnt=1 ;dwac_76570e40711a415c9b160ab389=UJkEJuQnrJUOcu81OVWv1issTbLoaPULjlM%3D|dw-only|||CNY|false|Etc%2FGMT%2B8|true ;dwac_bdbM6iaaioAiEaaaddLOa2751H=UJkEJuQnrJUOcu81OVWv1issTbLoaPULjlM%3D|dw-only|||CAD|false|Canada%2FEastern|true ;dwanonymous_27b803890ff7266f5c658c198ca9e17b=bdY0KdBLpdwYBeUJWSWbKxLWa4 ;dwanonymous_4b678b2f3ddcd887e7cd4635d93160c7=ab0dUFKUCbNBLqYbrKHFvExUsY ;dwsecuretoken_27b803890ff7266f5c658c198ca9e17b=2nO1YvmrwjHGRoEJ9jv6PeZaAmghEwdJ1w== ;dwsecuretoken_4b678b2f3ddcd887e7cd4635d93160c7=m_e6FKvKkma9ZPbEHbyGSm-ioSO5IJAEUg== ;dwsid=C4DG3TpZzOJa4YG6KOBrEHA1j0k2FpV82sBP756gp3qeEghHDCE2ay8sdHNknNT3MuyUDcAH4p_qDm8ULCw4Nw== ;language=zh ;sid=UJkEJuQnrJUOcu81OVWv1issTbLoaPULjlM";
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
    private static String baseUrl="https://www.canadagoose.com";
    private static OutputStreamWriter out= null;
    static String splitSign = ",";
    /**
     * 特殊处理 商品品类名称 字符 如： ,高跟鞋,凉鞋,
     */
    private static String sizeCategoryNames = "";


    static {
        if (null == bdl)
            bdl = ResourceBundle.getBundle("confCanadagoose");
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
            Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
            Header header1 =new Header("Cookie",listCookies);
            Header[] headers = new Header[2];
            headers[0] = header;
            headers[1] = header1;
            HttpResponse response = HttpUtils.get(categoryUrl,headers);
            if (response.getStatus() == 200) {
                String htmlContent = response.getResponse();
                Document doc = Jsoup.parse(htmlContent);
                //获取第一页商品数据
                //第二步：拿到品类列表的DOM
                Elements itemElements = doc.select(".search-result-content").select("div.grid-tile");
                String countStr = doc.select(".result").text();
                countStr= countStr.replace("结果", "").replace("个","").trim();
                System.out.println(countStr);
                int productCountNumberStr=Integer.valueOf(countStr);
                int pageSize =8;
                int page=(productCountNumberStr-1)/pageSize+1;
                int pagecount=8;
                if (page>1){
                    for (int i=1;i<=page;i++){
                        String url=categoryUrl+"?start="+(i-1)*8+"&sz="+i*8+"&format=page-element";
                        grapProductListByCategoryUrlOrDoc(sex,categoryName,url,i+1,category);
                    }
                }
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
        //System.out.println(categoryUrl);

        try {
            if(doc==null&&categoryUrl!=null){
                Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
                Header header1 =new Header("Cookie",listCookies);
                Header[] headers = new Header[2];
                headers[0] = header;
                headers[1] = header1;
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
            Elements elements=doc.select("body").select("div.grid-tile");
            System.out.println("列表上的详情页数量：" + elements.size());
            int productSize = elements.size();
            for (int i = 0; i < productSize; i++) {
                String elementhref=elements.get(i).select("a.thumb-link").attr("href");
                //String elementhref = elements.get(i).select("div.product-info>div.details>span.name").select("a").attr("href");
                if (elementhref==""){continue;}
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
            Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
            Header header1 =new Header("Cookie",deltailCookies);
            Header[] headers = new Header[2];
            headers[0] = header;
            headers[1] = header1;
            HttpResponse response = HttpUtils.get(productDetailUrl,headers);

            if (response.getStatus() == 200) {
                String htmlContent = response.getResponse();
                Document doc = Jsoup.parse(htmlContent);
               /* String text = doc.select(".col-lg-7").select("div.price-part").select("div.product-price").select("span[itemprop=\"price\"]").text();
                System.out.println(text);*/
                //产地
                String made="UK";
                //li.colors ul.color li a
                Elements colorElements=doc.select("li.colors").select("ul.color").select("li").select("a");
                if (colorElements.size()!=0){
                    for (int i=0;i<colorElements.size();i++){
                        String colorUrl=colorElements.get(i).attr("href");
                        String newColoUrl="";
                        if (colorUrl.contains("_Size")){
                            String[] colorUrls=colorUrl.split("&");
                            for (int o=0;o<colorUrls.length;o++){
                                if (!(colorUrls[o].contains("Size="))){
                                    newColoUrl=newColoUrl+"&"+colorUrls[o];
                                }
                            }
                            newColoUrl=newColoUrl.substring(1);
                        }
                        String colorStr=colorElements.get(i).attr("title");
                         getProductColorAndSize(sex,categoryName,newColoUrl,made,colorStr,category);
                     }
                }else {
                    System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!#@#@!#@!");
                    getProductColorAndSize(sex,categoryName,productDetailUrl,made,flag,category);
                }




            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void getProductColorAndSize(String sex,String categoryName,String colorUrl,String made,String colorStr,String category){
        System.out.println(colorUrl);
        try {
            Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
            Header header1 =new Header("Cookie",deltailCookies);
            Header[] headers = new Header[2];
            headers[0] = header;
            headers[1] = header1;
            HttpResponse response = HttpUtils.get(colorUrl,headers);
            if (response.getStatus() == 200) {
                String htmlContent = response.getResponse();
                Document doc = Jsoup.parse(htmlContent);
                String pricestr = doc.select(".col-lg-7").select("div.price-part").select("div.product-price").select("span[itemprop=\"price\"]").text();
                String matrerial=doc.select(".material").select(".container").text().replace(",","").trim();
                String matrerialUrl = baseUrl+doc.select("input[name=\"TEIFabricContent\"]").attr("value");
                HttpResponse matrerialRes = HttpUtils.get(matrerialUrl, headers);
                Document matrerialDoc = Jsoup.parse(matrerialRes.getResponse());
                matrerial= matrerialDoc.select(".material-head").select(".col-lg-2").text().replace("了解更多","");
                made=matrerialDoc.select("div.features-head").select(".col-lg-6").text().replace("产地","");
                //商品图片
                String colorId=colorUrl.split("&tmpcolor=")[1];
                String SPU=doc.select(".col-lg-7").select(".product-detail").select(".product-number").select("span").attr("data-gtm_pid")+colorUrl.split("tmpcolor=")[1];

                String pics=getPics(doc,colorId,SPU).replace(",","@");

                //商品描述
                String productDesc=doc.select(".product-description").text();
                        //测量信息
                String measurement=doc.select(".product-item__detail-row-large-4-4").select(".product-item__detail-info-fit-size-wrapper").select("ul").text().replace(",",".");
                String salePrice="";
                salePrice=pricestr.replace("£","").replace("$","").replace(" ","").replace(",","").trim();
                //String color=doc.select(".product-item__detail-sticky-wrapper").select(".hide-element").select("span[itemprop=\"color\"]").text();

                String color=colorStr;
                System.out.println("color="+color);
                //Elements elements=doc.select("div.gallery").select("li");
                //String SPU=doc.select(".product-show").select("span[itemprop=\"productId\"]").text().trim()+colorUrl.split("tmpcolor=")[1];
                 String proName=doc.select(".col-lg-7").select(".product-detail").select(".product-name").text().trim();
                if (StringUtils.isEmpty(color)){
                    color=proName;
                }
                Elements sizeElements = doc.select("ul.size-list").select("li");
                if (sizeElements.size()==0){
                    throw new RuntimeException("没有获取尺码信息！");
                }

                //System.out.println(sizeElements);
                for (int j=0;j<sizeElements.size();j++){
                    String qty=IN_STOCK;
                    String aClass = sizeElements.get(j).attr("class");
                    if (aClass.contains("sizeout")||aClass.contains("disabled")){
                        qty=NO_STOCK;
                    }
                    String sizeValue=sizeElements.get(j).select("a").attr("data-sizeVal");
                    System.out.println("尺码为："+sizeValue);
                  /*  if (sizeText.equalsIgnoreCase("SELECT SIZE")){
                        continue;
                    }
*/
                    /*if (sizeText.contains("Out")||sizeText.contains("Sold out")){
                        String[] strs;
                        strs=sizeText.split("    ");
                        sizeText=strs[0];
                        sizeStock=strs[1];
                    }
                    System.out.println(sizeText);
                    sizeText=sizeText.replace(" ","").trim();
                    String tempty="";*/
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
                        product.setSize(sizeValue);
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
                        product.setSupplierId("");
                        product.setSupplierNo("");
                        product.setSupplierSkuNo("");
                        product.setChannel("www.canadagoose.com");
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


    /*public static void main(String[] args) {
       *//* CanadagooseFR canadagooseFR = new CanadagooseFR();
        canadagooseFR.getProductColorAndSize("","","https://www.canadagoose.com/on/demandware.store/Sites-CanadaGooseCA-Site/zh_CA/Product-Variation?pid=3100M&dwvar_3100M_Color=392&tmpcolor=392","","","");
     *//*   Document doc=null;
        String categoryUrl="https://www.fendi.cn/api/rest/products?page=1&category_id=727&pn=0&with_filter=1&limit=999";
        //grapProductDetailMaterials(sex,categoryName,elementhref,"");
        try {
            CanadagooseFR object=new CanadagooseFR();

            //object.grapProductDetailMaterials("women","acnestudios",categoryUrl,"","","knitwear");
            if(doc==null&&categoryUrl!=null) {
                Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
                Header[] headers = new Header[1];
                headers[0] = header;
                HttpResponse response = HttpUtils.get(categoryUrl, headers);
                if (response.getStatus() == 200) {
                    String htmlContent = response.getResponse();
                    JsonObject parse = (JsonObject)new JsonParser().parse(htmlContent);
                    JsonObject data = parse.get("data").getAsJsonObject();
                    JsonArray products = data.get("products").getAsJsonArray();
                    for (int i=0;i<products.size();i++){
                        JsonObject product = products.get(i).getAsJsonObject();
                        String url = product.get("url").getAsString();
                        System.out.println(url);
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/

    public static void main(String[] args) {
        String url="https://www.valentino.cn/zh-cn/skirts_cod7789028784816257.html#dept=ROW_Apparel_W";
        Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
        //Header header1=new Header("cookie","RESOURCEINFO=DEVICE=desktop&ORIGINALDEVICE=desktop; ytos-session-VALENTINO=928607862a7643c7b9cea6c331ac6cc8hLOihK3d7c4cD7lO0Y6Grw; UI-PERSISTENT=abtest=&abtestperc=opcabtest&abtesth=uKypHGatIkBdcbzqlSK4TQ&country=cn; UI=abtest=&abtestperc=opcabtest&abtesth=uKypHGatIkBdcbzqlSK4TQ&cacheversion=f31-v755-valentino-w&device=desktop&version=2018-12-18-ab602b7&lang=; YEDGESESSION=7914d23c684d00001de2195c270200003f8e1000; rxVisitor=1545200157676I2RQTTG08CVBJCMH6RHE5GI5V2Q352JU; VISIT=NAZIONE=CHINA&NAZIONE_ISO=CN&LANGID=20&SITE_CODE=VALENTINO_CN; JSESSIONID=0000LQlAQs_NTvuS7ygEJs27k4V:-1; AWSELB=0B594BE514263D4021D7EC907B53B4CC775F526C3CD4B0D36029AFFBCBF7DE2F9644064DA9D272FD79167B8DE0A11689C0CD61E449B4FFB76A49EB7CF921953CFCF3D567BD3BB9B8B491B6FF440AEFA006581441BC; ak_bmsc=ECD505FF0E71ED6EA86A74FAA7051DDA3CD21479684D00001DE2195C8F234944~pl8v0ia7Pq5ajyjM+qnGuYA7s9cBp7gXGMD/ZmWDVtbjj4Toaa1XWp9Vb+ReSWQNFDsCUAVb2O4f72doYZdMKyV5C56mclT4c69Gc69Hga/U4XccYkcpgeBClRyd5tjbSLi11M7Y79y/haNWpk7omELPoGo1rzoe12yxumPPT2dWT4BEpHH7OdwSXB9j2udMJo1073veUCXSWAV4iBzt5oT9yGRXokTYB+PUGYtRd3inzxtciC1/+Evy/wIssGvsS0; Y-Country-Language=en-wy; FIRSTACCESS=1; AMCVS_49DBA42E58DE4C560A495C19%40AdobeOrg=1; _ga=GA1.2.1992327929.1545200162; _gid=GA1.2.654498324.1545200162; AMCV_49DBA42E58DE4C560A495C19%40AdobeOrg=2096510701%7CMCIDTS%7C17885%7CMCMID%7C42729139406631858743395311237969089804%7CMCAAMLH-1545804961%7C11%7CMCAAMB-1545804961%7CRKhpRz8krg2tLO6pguXWp5olkAcUniQYPHaMWWgdJ3xzPWQmdj0y%7CMCOPTOUT-1545207361s%7CNONE%7CMCAID%7CNONE%7CvVersion%7C2.0.0; s_cc=true; AKA_A2=A; yOrbRD=guid%3D7a61acc4-ce36-49c3-a95e-7bd489f6569f; TS01cad097=0122c051bcb41f1604972649facc2510de49e0d1d4c86bf4d60718b6bb0f23d3cd16d376ab72678d1d8b6253bf9c8fe554c0271e82ae26aa9891b4e0f22a8d27213109d39b; dtSa=-; s_sq=%5B%5BB%5D%5D; TS01d2253f=0122c051bcc2599bc299f287d967fa4e0954a656f05a3389e2c95dcee657bbd00c3bedfacdcd818682c63079968575a683fce1b11b; dtLatC=17; RT=\"dm=valentino.cn&si=01341ff0-9d69-4bc3-a376-c3940e5e80d1&ss=1545206968261&sl=0&tt=0&obo=0&sh=&bcn=%2F%2F0dfab5ec.akstat.io%2F\"; SetNewsletter_0=HbyCCYHP9CCt_YYy9XInXJJMlXP1GN-Hu-E3zR2mkVajglR180tr4OVDiQw4JCL_bW0yBNQ61F8f0jgY38XKRCKFTvI1; TS01cd3807=0122c051bc11d36ef1e1ca779c7b4d9db2ad6baaebc86bf4d60718b6bb0f23d3cd16d376ab8cbba54473134a820c14a99fa2204da1fc25964158004d362c2042ef5c057b537a8e6acd3c01ed68fde8c2f894d47b2d1441efff8ae4b0b49b760ddd576a1950; newRepeat=1545206971407-Repeat; daysSinceLastVisit=1545206971411; daysSinceLastVisit_s=Less%20than%201%20day; previousPageview=V%3ACN%3A%E5%A5%B3%E5%A3%AB%3A%E6%88%90%E8%A1%A3; TCSESSION=2018123169316662351693; dtPC=2$206968203_23h-vFOAELDLAGCCBDJBDNMKEHOFCMBBGDHDC; bm_sv=99024DB18FF20243D3DDA9A27331C850~qkXL0zLHYW8nSh763kxnubAsvsShPD4GbFYSKsjCIPiolb9yXdT2cXsvycDol45gY6U9vhDnzuJOX5H3gi9OBAMYlMcr7ORRCFkuVnB7lUsz3LB+z+MLh02z4RlsFcwPE64rO80Kpso5AYu2s34hnEwbJ4u4C7bbsmO09jTI2Zw=; s_ppvl=V%253ACN%253Aitem%253A%25u8FDE%25u8863%25u88D9_cod9057334113917898%2C43%2C43%2C946%2C1909%2C946%2C1920%2C1080%2C1%2CP; s_ppv=V%253ACN%253A%25u5973%25u58EB%253A%25u6210%25u8863%2C4%2C4%2C1385%2C1909%2C285%2C1920%2C1080%2C1%2CP; dtCookie=2$4C41A3E6426808478FB21D5652037212|store.valentino.com|1; rxvt=1545208779074|1545206620495");
        Header[] headers = new Header[1];
        headers[0] = header;
        //headers[1] = header1;
        try {
            HttpResponse response = HttpUtils.get(url, headers);
            System.out.println(response);
            Document parse = Jsoup.parse(response.getResponse());
            Elements select = parse.select("ul.products").select(".wrapper-shelf").select("li");
            System.out.println(select.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private String getPics(Document doc,String colorId,String SPU) {
        String splitStr=SPU.replace(colorId,"");
        String picStr="";
        Elements piclis = doc.select(".pdp-image").select("ul").select("li");
        for (int i=0;i<piclis.size();i++){
            String hrefUrl = piclis.get(i).select("a").attr("href");
            if (hrefUrl.contains(colorId)){
                picStr=picStr+"|"+hrefUrl;
            }
        }
        if (StringUtils.isNotBlank(picStr)){
            picStr = picStr.substring(1);
        }else {
            picStr=doc.select(".pdp-image").select("ul").select("li").select("a").first().attr("href").split(splitStr)[0]+splitStr+"_"+colorId+".jpg";
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
