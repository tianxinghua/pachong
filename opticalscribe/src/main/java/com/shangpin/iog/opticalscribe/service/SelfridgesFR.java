package com.shangpin.iog.opticalscribe.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shangpin.iog.opticalscribe.dto.RequestFailProductDTO;
import com.shangpin.iog.opticalscribe.dto.SelfridgesDTO;
import com.shangpin.iog.opticalscribe.tools.CSVUtilsSolveRepeatData;
import com.shangpin.iog.pavinGroup.util.HttpResponse;
import com.shangpin.iog.pavinGroup.util.HttpUtils;
import com.shangpin.iog.service.EventProductService;
import com.shangpin.iog.service.ProductFetchService;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.apache.commons.httpclient.Header;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.ws.Response;
import java.io.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Company: www.shangpin.com
 * @Author txh
 * @Date Create in 9:27 2018/9/04
 * @Description: Selfridges 英国官网 商品拉取


# selfridges England
uri-fr=http://www.selfridges.com
#拉取数据存放路径
path-fr=D://SELFRIDGES/selfridges-fr.csv
#去重数据存放路径
destFilePath-fr=D://SELFRIDGES/selfridges-fr-solved.csv

storeLang-fr=fra-fr

sizeCategoryNames-fr=,Ceintures,
女上衣
fr-genderAndNameAndCategoryUrl1=women&&MAX MARA&&http://www.selfridges.com/GB/en/cat/max-mara/womens/clothing/?llc=sn
fr-genderAndNameAndCategoryUrl2=women&&S MAX MARA&&http://www.selfridges.com/GB/en/cat/s-max-mara/womens/clothing/?llc=sn

fr-genderAndNameAndCategoryUrl3=women&&VLGARI&&http://www.selfridges.com/GB/en/cat/bvlgari/jewellery-watches/fine-jewellery!!!!
戒指
http://www.selfridges.com/GB/en/cat/bvlgari/jewellery-watches/fine-jewellery/rings/?llc=sn
手链
http://www.selfridges.com/GB/en/cat/bvlgari/jewellery-watches/fine-jewellery/bracelets/?llc=sn
项链
http://www.selfridges.com/GB/en/cat/bvlgari/jewellery-watches/fine-jewellery/necklaces/?llc=sn
耳环
http://www.selfridges.com/GB/en/cat/bvlgari/jewellery-watches/fine-jewellery/earrings/?llc=sn
男士
http://www.selfridges.com/GB/en/cat/bvlgari/jewellery-watches/fine-jewellery/mens/?llc=sn
挂坠
http://www.selfridges.com/GB/en/cat/bvlgari/jewellery-watches/fine-jewellery/pendants/?llc=sn
fr-genderAndNameAndCategoryUrl4=women&&BVLGARI&&http://www.selfridges.com/GB/en/cat/bvlgari/bags/?llc=sn


 */

@Component("selfridgesFR")
public class SelfridgesFR {
    private int urlCount=0;
    //有库存
    private static final String IN_STOCK = "1";
    //无库存
    private static final String NO_STOCK = "0";
    private static String supplierId="2018090410264";
    private static String supplierNo1="S000096974";
    private static String supplierNo2="S000096976";
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

                        "qty" + splitSign +

                        "made" + splitSign +

                        "desc" + splitSign +
                        "pics" + splitSign +

                        "detailLink" + splitSign +
                        "measurement" + splitSign+
                        "supplierId" + splitSign+
                        "supplierNo" + splitSign+
                        "supplierSkuNo" + splitSign
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
            HttpResponse response = HttpUtils.get(categoryUrl);
            if (response.getStatus() == 200) {
                String htmlContent = response.getResponse();
                Document doc = Jsoup.parse(htmlContent);
                //获取第一页商品数据
                //第二步：拿到品类列表的DOM
                grapProductListByCategoryUrlOrDoc(sex,categoryName,categoryUrl);
                Elements countNumberElements =null;

                    countNumberElements = doc.select(".headingArea").select("p.numberOfResults").select("span");
                int productCountNumberStr=Integer.valueOf(countNumberElements.text());

                int pageSize =60;
                int page=(productCountNumberStr-1)/pageSize+1;
                //System.out.println(page);
                if (page>1){
                    for (int i=1;i<page;i++){
                       /* if (i==1){*/
                            String url=categoryUrl+"?browsing_country=GB&currency=GBP&language=zh"+"&pn="+(i+1);
                            //System.out.println(url);
                            grapProductListByCategoryUrlOrDoc(sex,categoryName,url);
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
    public void grapProductListByCategoryUrlOrDoc(String sex,String categoryName,String categoryUrl) {
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
            Elements productElements= doc.select(".products>.productsInner>.productContainer>a");
            System.out.println("列表上的详情页数量："+productElements.size());
            int productSize = productElements.size();
            for (int i=0;i<productSize;i++){
                Element element=productElements.get(i);

                String productDetailUrl=element.attr("href");
                //System.out.println(productDetailUrl);
                urlCount++;
                //第三步：跳转到处理商品详情
                String season="";
                String size="";
                if (categoryUrl.contains("http://www.selfridges.com/GB/zh/cat/bvlgari/jewellery-watches/fine-jewellery")){
                    grapProductDetailMaterialsForJewellery(sex,categoryName,productDetailUrl,season,size);
                }else{
                    grapProductDetailMaterials(sex,categoryName,productDetailUrl,season,size);
                }

            }
            System.out.println("urlCount:"+urlCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /*public static void main(String[] args) {
        String AjaxUrl="http://www.selfridges.com/GB/zh/webapp/wcs/stores/servlet/AjaxStockStatusView?attr=Colour&attrval=Cornflower%20blue&productId=6796657&quantityLimit=&wcid=168-82052654-VALDESEAW18&storeId=10052&langId=-1&orderId=.&catalogId=16151&catEntryId=&childItemId=&calculationUsageId=-1&shouldCachePage=false&check=**";
        SelfridgesFR fr=new SelfridgesFR();
        String sex="women";
        String categoryName="MAX MARA";
        //String productDetailUrl="http://www.selfridges.com/GB/zh/cat/max-mara-madame-double-breasted-wool-and-cashmere-blend-coat_168-82052654-MADAME/?previewAttribute=Navy";
       String productDetailUrl="http://www.selfridges.com/GB/zh/cat/bvlgari-b-zero1-18ct-white-gold-and-diamond-band-ring_709-10045-AN858378/?previewAttribute=";

       //fr.grapProductDetailMaterials(sex,categoryName,productDetailUrl,"","");//调非珠宝
       fr.grapProductDetailMaterialsForJewellery(sex,categoryName,productDetailUrl,"","");
        try {
            HttpResponse sizeResponse=HttpUtils.get(AjaxUrl);
            System.out.println(sizeResponse.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
    private void grapProductDetailMaterialsForJewellery(String sex, String categoryName, String productDetailUrl, String season, String size) {
        productDetailUrl=productDetailUrl.replace("’","");
        System.out.println(productDetailUrl);
        SelfridgesDTO product=new SelfridgesDTO();
        try {
            Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
            Header[] headers = new Header[1];
            headers[0] = header;
            HttpResponse response = HttpUtils.get(productDetailUrl,headers);
            if (response.getStatus() == 200) {
                String htmlContent = response.getResponse();
                Document doc = Jsoup.parse(htmlContent);
                Elements elements=doc.select(".productDetails").select(".productDetailsInner");
                String ss = elements.outerHtml();
                ss = ss.replace("<script type=\"text/template\" class=\"infoblockhtmlholder\">","<div class=\"infoblockhtmlholder\">");
                ss = ss.replace("</script>","</div>");
                Document docdesc= Jsoup.parse(ss);
                Elements elements2 = docdesc.select(".infoblockhtmlholder").select("ul").select("li");
                //商品的描述
                String productDesc=docdesc.select(".infoblockhtmlholder").select("ul").select("li").text().replace(","," ");
                //商品的名称
                String productName=doc.select(".productDesc").select(".description").text().trim().replace(","," ");
                //商品的价格
                //价格
                String price=doc.select(".productDesc").select(".prices").select("span[itemprop=\"price\"]").text();
                price = price.replace(",","").trim();
                //遍历描述中每个li的信息
                String materialName="";
                String measurement="";
                String made="";
                for(Element element:elements2){
                    String info=element.text();
                    //商品的材质
                    if (info.contains("%")||info.contains("％")){
                        materialName=info;
                        materialName=info.replace(",",".");
                       // System.out.println(materialName);
                        //材质Count++;
                        //商品的cm
                    }else if (info.contains("厘米")){
                        measurement=info.replace(",",".");
                        // measurementCount++;
                    }else if(info.contains("制造")){
                        made=info.replace(",",".");

                    }
                        /*System.out.println(made);
                        System.out.println(measurement);*/

                }

                Element elements1=doc.select("#main").select("ul[itemprop=\"breadcrumb\"]").select("li").get(2);
                String category="";
                category=elements1.text();
                String productColor="";
                String productSku="";
                String productQty="";
                //System.out.println(productDetailUrl);
                getproductColor(doc,productDetailUrl,sex,categoryName,category,productName,made,materialName,productDesc,measurement);
            }
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
    private void grapProductDetailMaterials(String sex,String categoryName,String productDetailUrl,String season,String size){
        System.out.println("非珠宝"+productDetailUrl);
        SelfridgesDTO product=new SelfridgesDTO();
        try {

            Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
            Header[] headers = new Header[1];
            headers[0] = header;
            HttpResponse response = HttpUtils.get(productDetailUrl,headers);

            if (response.getStatus() == 200) {
                String htmlContent = response.getResponse();
                Document doc = Jsoup.parse(htmlContent);
                Elements elements=doc.select(".productDetails").select(".productDetailsInner");
                String ss = elements.outerHtml();
                ss = ss.replace("<script type=\"text/template\" class=\"infoblockhtmlholder\">","<div class=\"infoblockhtmlholder\">");
                ss = ss.replace("</script>","</div>");
                Document docdesc= Jsoup.parse(ss);
                Elements elements2 = docdesc.select(".infoblockhtmlholder").select("ul").select("li");
                //商品的描述
                String productDesc=docdesc.select(".infoblockhtmlholder").select("ul").select("li").text().replace(","," ");
                //商品的名称
                String productName=doc.select(".productDesc").select(".description").text().trim().replace(","," ");
                //商品的价格
                //价格
                String price=doc.select(".productDesc").select(".prices").select("span[itemprop=\"price\"]").text();
                price = price.replace(",","").trim();
                //遍历描述中每个li的信息
                String materialName="";
                String measurement="";
                String made="";
                for(Element element:elements2){
                    String info=element.text();
                    //商品的材质
                    if (info.contains("%")||info.contains("％")){
                        materialName=info;
                        materialName=info.replace(",",".");
                        //materialName=info.replace("，",".");
                        System.out.println(materialName);
                        //材质Count++;
                        //商品的cm
                    }else if (info.contains("厘米")){
                        measurement=info.replace(",",".");
                        // measurementCount++;
                    }else if(info.contains("制造")){
                        made=info.replace(",",".");

                    }
                    /*System.out.println(made);
                    System.out.println(measurement);*/

                }

                Element elements1=doc.select("#main").select("ul[itemprop=\"breadcrumb\"]").select("li").get(2);
                String category="";
                category=elements1.text();
                String productColor="";
                String productSku="";
                String productQty="";
                System.out.println(productDetailUrl);
                //判断是否为特殊商品：正常类型或包首饰等
                if (productDetailUrl.contains("max-mara")||productDetailUrl.contains("s-max-mara")){

                    getproductColor(doc,productDetailUrl,sex,categoryName,category,productName,made,materialName,productDesc,measurement);
                }else{
                //包请求颜色
                String productSpu=doc.select(".productDesc").select("form").select("p").select(".val").text();
                //http://www.selfridges.com/GB/zh/webapp/wcs/stores/servlet/AjaxStockStatusView?productId=5735015&quantityLimit=
                // &wcid=709-10045-34563&storeId=10052&langId=-1&orderId=.&catalogId=16151&catEntryId=
                // &childItemId=3559144&calculationUsageId=-1&shouldCachePage=false&check=**
                // &defaultViewName=OrderItemDisplay&errorViewName=ProductDisplayErrorView&categoryId=
                // &productTitle=34563+serpenti+forever+shoulder+bag+m&productBrand=BVLGARI&quantity=1
                Elements inputElements=doc.select(".itemscope").select("form").select("input");
                Map map=new HashMap();
                for (Element element:inputElements){
                    map.put(element.attr("name"),element.attr("value"));
                }
                String AjaxUrl="http://www.selfridges.com/GB/zh/webapp/wcs/stores/servlet/AjaxStockStatusView?productId="+map.get("productId")
                        +"&quantityLimit="+map.get("quantityLimit")+"&wcid="+map.get("wcid")+"&storeId="+map.get("storeId")+"&langId="+map.get("langId")
                        +"&orderId="+map.get("orderId")+"&catalogId="+map.get("catalogId")+"&catEntryId="+map.get("catEntryId")+"&childItemId="
                        +map.get("childItemId")+"&calculationUsageId="+map.get("calculationUsageId")+"&shouldCachePage="+map.get("shouldCachePage")
                        +"&check="+map.get("check");
                HttpResponse ColorResponse=HttpUtils.get(AjaxUrl,headers);
                    //暂时备注
                if (ColorResponse.getStatus()==200) {
                    String colorRes = ColorResponse.getResponse();
                    Document colorDoc = Jsoup.parse(colorRes);
                    String colorAjax=colorDoc.select("body").first().text();
                    JsonObject jsonObject = (JsonObject) new JsonParser().parse(colorAjax);
                    JsonArray jsonArray=jsonObject.get("stocks").getAsJsonArray();
                    for (int i=0;i<jsonArray.size();i++){
                        String pics=getPics(doc);
                        productColor=jsonArray.getAsJsonArray().get(i).getAsJsonObject().get("value").getAsString();
                        productSku=jsonArray.getAsJsonArray().get(i).getAsJsonObject().get("sku").getAsString();
                        if (true==jsonArray.getAsJsonArray().get(i).getAsJsonObject().get("inStock").getAsBoolean()){
                            productQty="1";
                        }else {
                            productQty="0";
                        }
                        product.setGender(sex);
                        product.setBrand(categoryName);
                        product.setCategory("bags");
                        product.setSPU(productSpu);
                        product.setProductModel(productSpu);
                        product.setSeason(season);
                        product.setMaterial(materialName);
                        product.setColor(productColor);
                        product.setSize(size);
                        product.setProName(productName);
                        product.setForeignPrice(price);
                        product.setDomesticPrice(price);
                        product.setQty(productQty);
                        product.setMade(made);
                        product.setDesc(productDesc);
                        product.setPics(pics);
                        product.setDetailLink(productDetailUrl);
                        product.setMeasurement(measurement);
                        product.setSupplierId(supplierId);
                        if(product.getCategory().equals("bags")){
                            product.setSupplierNo(supplierNo2);
                        }else {
                            product.setSupplierNo(supplierNo1);
                        }
                        product.setSupplierSkuNo(productSku);
                        barCodeMap.put(productName,product);
                        System.out.println("-------------------已拉取了："+barCodeMap.size()+"个详情链接的商品");
                        //System.out.println(product);
                        //exportExcel(product);
                    }
                }

                    /*for ( Object i:map.keySet()){
                        System.out.println(i);
                        System.out.println(map.get(i));
                    }*/
                /*System.out.println("能获取到的商品的材质数量："+材质Count);
                System.out.println("能获取到的商品的cm数量："+measurementCount);*/



                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //获取商品详情信息 sku size等
    private void getproductColor(Document doc,String url,String sex,String categoryName,String category,String productName,String made,String
            materialName,String productDesc,String measurement){
        String color="";
        String productSpu;
        Elements colorElements=doc.select(".itemScope").select("form").select(".colour").select("ul").select("li");
        //珠宝特殊走if
        if (colorElements.size()<2){
            color=colorElements.select("label").select("input").attr("value");
            productSpu=doc.select(".productDesc").select("form").select("p").select(".val").text();
            getAjax(doc,url,sex,categoryName,category,productName,made,materialName,productDesc,color,measurement,productSpu);
        }else {
            for (int i=0;i<colorElements.size();i++){
                Map sizeMap=new HashMap();
                color=colorElements.get(i).select("label").select("input").attr("value");
                productSpu=doc.select(".productDesc").select("form").select("p").select(".val").text()+color;
                String newUrl=(url.split("previewAttribute=")[0]+"previewAttribute="+color).replace(" ","");
                String pics="";
                try {
                    HttpResponse response = HttpUtils.get(newUrl);
                    if (response.getStatus() == 200) {
                        String htmlContent = response.getResponse();
                        doc = Jsoup.parse(htmlContent);
                        pics=getPics(doc);
                    }
                    //new想法

                    Elements inputElements=doc.select(".itemscope").select("form").select("input");
                    Map map=new HashMap();
                    for (Element element:inputElements){
                        map.put(element.attr("name"),element.attr("value"));
                    }
                    String AjaxUrl="http://www.selfridges.com/GB/zh/webapp/wcs/stores/servlet/AjaxStockStatusView?attr=Colour&attrval="+color+"&productId="+map.get("productId")
                            +"&quantityLimit="+map.get("quantityLimit")+"&wcid="+map.get("wcid")+"&storeId="+map.get("storeId")+"&langId="+map.get("langId")
                            +"&orderId="+map.get("orderId")+"&catalogId="+map.get("catalogId")+"&catEntryId="+map.get("catEntryId")+"&childItemId="
                            +map.get("childItemId")+"&calculationUsageId="+map.get("calculationUsageId")+"&shouldCachePage="+map.get("shouldCachePage")
                            +"&check="+map.get("check");
                    AjaxUrl=AjaxUrl.replace(" ","%20");
                    HttpResponse sizeResponse=HttpUtils.get(AjaxUrl);
                    if (sizeResponse.getStatus()==200) {
                        String colorRes = sizeResponse.getResponse();
                        Document colorDoc = Jsoup.parse(colorRes);
                        String colorAjax=colorDoc.select("body").first().text();
                        JsonObject jsonObject = (JsonObject) new JsonParser().parse(colorAjax);
                        JsonArray jsonArray=jsonObject.get("stocks").getAsJsonArray();
                        for (int k=0;k<jsonArray.size();k++){
                            JsonObject asJsonObject = jsonArray.get(k).getAsJsonObject();
                            sizeMap.put(asJsonObject.get("value").getAsString(),asJsonObject.get("inStock").getAsBoolean());
                        }
                        System.out.println(sizeMap);
                    }
                    Elements sizeElements=doc.select(".itemscope").select("form").select(".size").select(".dk").select("option");
                    if (sizeElements.size()==0){
                        Elements  sizeSML=doc.select(".itemscope").select("form").select(".size").select("ul").select("li");
                        System.out.println(sizeSML);
                        for (int w=0;w<sizeSML.size();w++){
                            SelfridgesDTO product=new SelfridgesDTO();
                            String sizeValue=sizeSML.get(w).select("label").select("span").text();
                            //String sizeValue=size.split("寸")[1];
                            String price=doc.select(".productDesc").select(".prices").select("span[itemprop=\"price\"]").text();
                            price = price.replace(",","").trim();
                            product.setGender(sex);
                            product.setBrand(categoryName);
                            product.setCategory(category);
                            product.setSPU(productSpu);
                            product.setProductModel(productSpu);
                            product.setMaterial(materialName);
                            product.setColor(color);
                            product.setSize(sizeValue);
                            product.setProName(productName);
                            product.setForeignPrice(price);
                            product.setDomesticPrice(price);
                            Boolean  qtyhave= null;
                            try {
                                qtyhave = (Boolean)sizeMap.get(sizeValue);
                            } catch (Exception e) {
                                qtyhave=false;
                            }
                            if (qtyhave==null){
                                product.setQty(NO_STOCK);
                            }else if (qtyhave==true) {
                                product.setQty(IN_STOCK);
                            }else {
                                product.setQty(NO_STOCK);
                            }
                            product.setMade(made);
                            product.setDesc(productDesc);
                            product.setPics(pics);
                            product.setDetailLink(newUrl);
                            product.setSupplierId(supplierId);
                            product.setSupplierNo(supplierNo1);
                            product.setMeasurement("");
                            product.setSupplierSkuNo("");
                            System.out.println(product);
                            exportExcel(product);
                        }
                    }
                    System.out.println(sizeElements);
                    for (int w=1;w<sizeElements.size();w++){
                        SelfridgesDTO product=new SelfridgesDTO();
                        String size=sizeElements.get(w).text();
                        String sizeValue=size.split("寸")[1];
                        String price=doc.select(".productDesc").select(".prices").select("span[itemprop=\"price\"]").text();
                        price = price.replace(",","").trim();
                        product.setGender(sex);
                        product.setBrand(categoryName);
                        product.setCategory(category);
                        product.setSPU(productSpu);
                        product.setProductModel(productSpu);
                        product.setMaterial(materialName);
                        product.setColor(color);
                        product.setSize(size);
                        product.setProName(productName);
                        product.setForeignPrice(price);
                        product.setDomesticPrice(price);
                        Boolean  qtyhave= null;
                        try {
                            qtyhave = (Boolean)sizeMap.get(sizeValue);
                        } catch (Exception e) {
                            qtyhave=false;
                        }
                        if (qtyhave==null){
                            product.setQty(NO_STOCK);
                        }else if (qtyhave==true) {
                            product.setQty(IN_STOCK);
                        }else {
                            product.setQty(NO_STOCK);
                        }
                        product.setMade(made);
                        product.setDesc(productDesc);
                        product.setPics(pics);
                        product.setDetailLink(newUrl);
                        product.setSupplierId(supplierId);
                        product.setSupplierNo(supplierNo1);
                        product.setMeasurement("");
                        product.setSupplierSkuNo("");
                        System.out.println(product);
                        exportExcel(product);
                    }
                   /* Ajax请求size和库存
                   http://www.selfridges.com/GB/zh/webapp/wcs/stores/servlet/AjaxStockStatusView?attr=Colour&attrval=Pink&productId=6857365
                    &quantityLimit=&wcid=168-82052654-BIKINI&storeId=10052&langId=-1&orderId=.&catalogId=16151&catEntryId=&childItemId=
                    &calculationUsageId=-1&shouldCachePage=false&check=**&defaultViewName=OrderItemDisplay&errorViewName=ProductDisplayErrorView
                    &categoryId=&productTitle=Bikini+geometric-print+silk-twill+shirt&productBrand=S+MAX+MARA&quantity=1&Colour=Pink&Size=Select+Size

                    System.out.println(AjaxUrl);*/



                    /*Elements inputElements=doc.select(".itemscope").select("form").select("input");
                    Map map=new HashMap();
                    for (Element element:inputElements){
                        map.put(element.attr("name"),element.attr("value"));
                    }
                    String AjaxUrl="http://www.selfridges.com/GB/zh/webapp/wcs/stores/servlet/AjaxStockStatusView?attr=Colour&attrval="+color+"&productId="+map.get("productId")
                            +"&quantityLimit="+map.get("quantityLimit")+"&wcid="+map.get("wcid")+"&storeId="+map.get("storeId")+"&langId="+map.get("langId")
                            +"&orderId="+map.get("orderId")+"&catalogId="+map.get("catalogId")+"&catEntryId="+map.get("catEntryId")+"&childItemId="
                            +map.get("childItemId")+"&calculationUsageId="+map.get("calculationUsageId")+"&shouldCachePage="+map.get("shouldCachePage")
                            +"&check="+map.get("check");
                    AjaxUrl=AjaxUrl.replace(" ","%20");
                    HttpResponse sizeResponse=HttpUtils.get(AjaxUrl);
                    if (sizeResponse.getStatus()==200) {
                        String colorRes = sizeResponse.getResponse();
                        Document colorDoc = Jsoup.parse(colorRes);
                        String colorAjax=colorDoc.select("body").first().text();
                        JsonObject jsonObject = (JsonObject) new JsonParser().parse(colorAjax);
                        JsonArray jsonArray=jsonObject.get("stocks").getAsJsonArray();
                        for (int k=0;k<jsonArray.size();k++){
                            //价格
                            String price=doc.select(".productDesc").select(".prices").select("span[itemprop=\"price\"]").text();
                            price = price.replace(",","").trim();
                            SelfridgesDTO product=new SelfridgesDTO();
                            JsonObject asJsonObject = jsonArray.get(k).getAsJsonObject();
                            product.setGender(sex);
                            product.setBrand(categoryName);
                            product.setCategory(category);
                            product.setSPU(productSpu);
                            product.setProductModel(productSpu);
                            product.setMaterial(materialName);
                            product.setColor(color);
                            product.setSize(asJsonObject.get("value").getAsString());
                            product.setProName(productName);
                            product.setForeignPrice(price);
                            product.setDomesticPrice(price);
                            Boolean qtyhave=asJsonObject.get("inStock").getAsBoolean();

                            if (qtyhave==true) {
                                product.setQty(IN_STOCK);
                            }else {
                                product.setQty(NO_STOCK);
                            }
                            product.setMade(made);
                            product.setDesc(productDesc);
                            product.setPics(pics);
                            product.setDetailLink(newUrl);
                            product.setSupplierId(supplierId);
                            product.setSupplierNo(supplierNo1);
                            product.setSupplierSkuNo(asJsonObject.get("sku").getAsString());
                            product.setMeasurement("");
                            //System.out.println(product);
                            exportExcel(product);
                            barCodeMap.put(productName,product);
                            System.out.println("-------------------已拉取了："+barCodeMap.size()+"个详情链接的商品");
                        }
                    }*/
                } catch (Exception e) {
                    e.printStackTrace();
                }


            /*for (int j=0;j<sizeElements.size();j++){
                String s=sizeElements.get(j).text();
                System.out.println("size:"+s);
            }*/
            }
            //特殊珠宝的if
       }

    }

    private void getAjax(Document doc,String url,String sex,String categoryName,String category,String productName,String made,String materialName
    ,String productDesc,String color,String measurement,String productSpu) {
        Map sizeMap=new HashMap();
        Elements inputElements=doc.select(".itemscope").select("form").select("input");
        Map map=new HashMap();
        for (Element element:inputElements){
            map.put(element.attr("name"),element.attr("value"));
        }
        //attr=Colour&attrval="+color+"&
        String AjaxUrl="http://www.selfridges.com/GB/zh/webapp/wcs/stores/servlet/AjaxStockStatusView?productId="+map.get("productId")
                +"&quantityLimit="+map.get("quantityLimit")+"&wcid="+map.get("wcid")+"&storeId="+map.get("storeId")+"&langId="+map.get("langId")
                +"&orderId="+map.get("orderId")+"&catalogId="+map.get("catalogId")+"&catEntryId="+map.get("catEntryId")+"&childItemId="
                +map.get("childItemId")+"&calculationUsageId="+map.get("calculationUsageId")+"&shouldCachePage="+map.get("shouldCachePage")
                +"&check="+map.get("check");
        AjaxUrl=AjaxUrl.replace(" ","%20");
        try {
            HttpResponse sizeResponse=HttpUtils.get(AjaxUrl);
            if (sizeResponse.getStatus()==200) {
                String jsonResponse = sizeResponse.getResponse();
                Document jsonDoc = Jsoup.parse(jsonResponse);
                String jsonAjax=jsonDoc.select("body").first().text();
                JsonObject jsonObject = (JsonObject) new JsonParser().parse(jsonAjax);
                JsonArray jsonArray=jsonObject.get("stocks").getAsJsonArray();
                for (int k=0;k<jsonArray.size();k++){
                    JsonObject asJsonObject = jsonArray.get(k).getAsJsonObject();
                    sizeMap.put(asJsonObject.get("value").getAsString(),asJsonObject.get("inStock").getAsBoolean());
                }
                System.out.println(sizeMap);
                Elements sizeElements=doc.select(".itemscope").select("form").select(".size").select(".dk").select("option");
                if (sizeElements.size()==0){
                    Elements  sizeSML=doc.select(".itemscope").select("form").select(".size").select("ul").select("li");
                    System.out.println(sizeSML);
                    if (sizeSML.size()==0){//珠宝首饰特殊存在无尺寸现象
                        SelfridgesDTO product=new SelfridgesDTO();
                        String sizeValue="";
                        String price=doc.select(".productDesc").select(".prices").select("span[itemprop=\"price\"]").text();

                        price = price.replace(",","").trim();
                        if (StringUtils.isEmpty(sex)){
                            String strGender=doc.select("#main").select("ul[itemprop=\"breadcrumb\"]").select("li").get(1).text();
                            if (!(strGender.equals("女士"))){
                                sex="men";
                            }else {
                                sex="women";
                            }
                        }
                        product.setGender(sex);
                        product.setBrand(categoryName);
                        product.setCategory(category);
                        product.setSPU(productSpu);
                        product.setProductModel(productSpu);
                        product.setMaterial(materialName);
                        product.setColor(color);
                        product.setSize(sizeValue);
                        product.setProName(productName);
                        product.setForeignPrice(price);
                        product.setDomesticPrice(price);
                        Boolean  qtyhave= null;
                        try {
                            qtyhave = (Boolean)sizeMap.get(sizeValue);
                        } catch (Exception e) {
                            qtyhave=false;
                        }
                        if (qtyhave==null){
                            product.setQty(NO_STOCK);
                        }else if (qtyhave==true) {
                            product.setQty(IN_STOCK);
                        }else {
                            product.setQty(NO_STOCK);
                        }
                        product.setMade(made);
                        product.setDesc(productDesc);
                        product.setPics(getPics(doc));
                        product.setDetailLink(url);
                        product.setSupplierId(supplierId);
                        product.setSupplierNo(supplierNo1);
                        product.setMeasurement("");
                        product.setSupplierSkuNo("");
                        System.out.println(product);
                        exportExcel(product);
                    }
                    for (int w=0;w<sizeSML.size();w++){
                        SelfridgesDTO product=new SelfridgesDTO();
                        String sizeValue=sizeSML.get(w).select("label").select("span").text();
                        //String sizeValue=size.split("寸")[1];
                        String price=doc.select(".productDesc").select(".prices").select("span[itemprop=\"price\"]").text();
                        price = price.replace(",","").trim();
                        if (StringUtils.isEmpty(sex)){
                            String strGender=doc.select("#main").select("ul[itemprop=\"breadcrumb\"]").select("li").get(1).text();
                            if (!(strGender.equals("女士"))){
                                sex="men";
                            }else {
                                sex="women";
                            }
                        }
                        product.setGender(sex);
                        product.setBrand(categoryName);
                        product.setCategory(category);
                        product.setSPU(productSpu);
                        product.setProductModel(productSpu);
                        product.setMaterial(materialName);
                        product.setColor(color);
                        product.setSize(sizeValue);
                        product.setProName(productName);
                        product.setForeignPrice(price);
                        product.setDomesticPrice(price);
                        Boolean  qtyhave= null;
                        try {
                            qtyhave = (Boolean)sizeMap.get(sizeValue);
                        } catch (Exception e) {
                            qtyhave=false;
                        }
                        if (qtyhave==null){
                            product.setQty(NO_STOCK);
                        }else if (qtyhave==true) {
                            product.setQty(IN_STOCK);
                        }else {
                            product.setQty(NO_STOCK);
                        }
                        product.setMade(made);
                        product.setDesc(productDesc);
                        product.setPics(getPics(doc));
                        product.setDetailLink(url);
                        product.setSupplierId(supplierId);
                        product.setSupplierNo(supplierNo1);
                        product.setMeasurement("");
                        product.setSupplierSkuNo("");
                        System.out.println(product);
                        exportExcel(product);
                    }
                }
                System.out.println(sizeElements);
                for (int w=1;w<sizeElements.size();w++){
                    SelfridgesDTO product=new SelfridgesDTO();
                    String size=sizeElements.get(w).text();
                    String sizeValue="";
                    try {
                        sizeValue=size.split("寸：")[1].replace(" ","").replace("毫米","mm");
                    } catch (Exception e) {
                        sizeValue=size.split("寸")[1].replace(" ","").replace("毫米","mm");
                    }
                    String price=doc.select(".productDesc").select(".prices").select("span[itemprop=\"price\"]").text();
                    price = price.replace(",","").trim();
                    if (StringUtils.isEmpty(sex)){
                        String strGender=doc.select("#main").select("ul[itemprop=\"breadcrumb\"]").select("li").get(1).text();
                        if (!(strGender.equals("女士"))){
                            sex="men";
                        }else {
                            sex="women";
                        }
                    }
                    product.setGender(sex);
                    product.setBrand(categoryName);
                    product.setCategory(category);
                    product.setSPU(productSpu);
                    product.setProductModel(productSpu);
                    product.setMaterial(materialName);
                    product.setColor(color);
                    product.setSize(size.replace("：","").replace("尺寸","").replace("毫米","mm").replace(" ",""));
                    product.setProName(productName);
                    product.setForeignPrice(price);
                    product.setDomesticPrice(price);
                    Boolean  qtyhave= null;
                    try {
                        qtyhave = (Boolean)sizeMap.get(sizeValue);
                    } catch (Exception e) {
                        qtyhave=false;
                    }
                    if (qtyhave==null){
                        product.setQty(NO_STOCK);
                    }else if (qtyhave==true) {
                        product.setQty(IN_STOCK);
                    }else {
                        product.setQty(NO_STOCK);
                    }
                    product.setMade(made);
                    product.setDesc(productDesc);
                    product.setPics(getPics(doc));
                    product.setDetailLink(url);
                    product.setSupplierId(supplierId);
                    product.setSupplierNo(supplierNo1);
                    product.setMeasurement("");
                    product.setSupplierSkuNo("");
                    System.out.println(product);
                    exportExcel(product);
                }
                /*for (int k=0;k<jsonArray.size();k++){
                    SelfridgesDTO product=new SelfridgesDTO();
                    //价格
                    String price=doc.select(".productDesc").select(".prices").select("span[itemprop=\"price\"]").text();
                    price = price.replace(",","").trim();
                    JsonObject asJsonObject = jsonArray.get(k).getAsJsonObject();
                    String name=asJsonObject.get("name").getAsString();
                    if (StringUtils.isEmpty(sex)){
                        String strGender=doc.select("#main").select("ul[itemprop=\"breadcrumb\"]").select("li").get(1).text();
                        if (!(strGender.equals("女士"))){
                            sex="men";
                        }else {
                            sex="women";
                        }
                    }
                    product.setGender(sex);
                    product.setBrand(categoryName);
                    product.setCategory(category);
                    product.setSPU(productSpu);
                    product.setProductModel(productSpu);
                    product.setMaterial(materialName);
                    product.setColor(color);
                    if (name.equals("Size")){
                        product.setSize(asJsonObject.get("value").getAsString());
                    }
                    product.setProName(productName);
                    product.setForeignPrice(price);
                    product.setDomesticPrice(price);
                    Boolean qtyhave=asJsonObject.get("inStock").getAsBoolean();
                    if (qtyhave==true) {
                        product.setQty(IN_STOCK);
                    }else {
                        product.setQty(NO_STOCK);
                    }
                    product.setMade(made);
                    product.setDesc(productDesc);
                    product.setPics(getPics(doc));
                    product.setDetailLink(url);
                    product.setSupplierId(supplierId);
                    product.setSupplierNo(supplierNo1);
                    product.setSupplierSkuNo(asJsonObject.get("sku").getAsString());
                    product.setMeasurement(measurement);
                    exportExcel(product);
                    barCodeMap.put(productName,product);
                    System.out.println("-------------------已拉取了："+barCodeMap.size()+"个详情链接的商品");
                        *//*int width = asJsonObject.get("width").getAsInt();
                        int height = asJsonObject.get("height").getAsInt();
                        String job = asJsonObject.get("partNumbers").getAsJsonArray().get(0).getAsString();*//*

                }*/
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*public static void main(String[] args) {
        Document doc=null;
        String categoryUrl="http://www.selfridges.com/GB/zh/cat/max-mara/womens/clothing/?llc=sn&browsing_country=GB&currency=GBP&language=zh&pn=2";

        try {
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
    private String getPics(Document doc) {
       Elements elements= doc.select(".productImage").select(".pImgWrap").select("#largeImage");
       String firstpic=elements.attr("src");
        String picStr=firstpic;
        String[] picArr=firstpic.split("M\\?");
        for (int i=1;;i++){
            String newpic=picArr[0]+"ALT0"+i+"?"+picArr[1];
            Document elements1=Jsoup.parse(newpic);
            Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
            Header[] headers = new Header[1];
            headers[0] = header;
            try {
                HttpResponse ColorResponse=HttpUtils.get(newpic,headers);
                if(ColorResponse.getStatus()!=200){
                    //System.out.println(ColorResponse.getStatus());
                    return picStr;
                }else {
                    picStr=picStr+"|"+newpic;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return picStr;
            }
        }

            //return "";
    }

    /**
     * 导出单个商品信息到csv 文件（追加）
     * @param dto 商品信息DTO
     */
    private static void exportExcel(SelfridgesDTO dto) {
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
