package com.shangpin.iog.opticalscribe.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shangpin.iog.opticalscribe.dto.LouisvuittonDTO;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Company: www.shangpin.com
 * @Author wanner
 * @Date Create in 10:08 2018/7/20
 * @Description: LV 意大利网 商品拉取


# lv 意大利
#uri-it=https://it.louisvuitton.com
#path-it=D://LV/lv-it.csv
#destFilePath-it=D://LV/lv-it-solved.csv
#flag-it=false
#sizeCategoryNames-it=,Sandali,Sneakers,Calzature basse,Mocassini &Car Shoe,Stivaletti e Stivali,Décolleté,Calzature stringate & Calzature con fibbia,Tessili&altro,Cinture,


#women
#腰带
lv-it-genderAndNameAndCategoryUrl1=women&&Cinture&&https://it.louisvuitton.com/ita-it/donna/accessori/cinture/_/N-1jmkkt9
#小皮具
#lv-it-genderAndNameAndCategoryUrl2=women&&Portafogli&&https://it.louisvuitton.com/ita-it/donna/portafogli/_/N-ij6jql
#lv-it-genderAndNameAndCategoryUrl3=women&&Portafogli con catenella&&https://it.louisvuitton.com/ita-it/donna/piccola-pelletteria/portafogli-con-catenella/_/N-1w39475
#lv-it-genderAndNameAndCategoryUrl4=women&&Cover Smartphone&&https://it.louisvuitton.com/ita-it/donna/piccola-pelletteria/cover-smartphone/_/N-6gjmix
#lv-it-genderAndNameAndCategoryUrl5=women&&Portachiavi&Porta carte&&https://it.louisvuitton.com/ita-it/donna/portachiavi-porta-carte/_/N-1jm6gjb
##围巾披肩
#lv-it-genderAndNameAndCategoryUrl6=women&&Tessili&altro&&https://it.louisvuitton.com/ita-it/donna/accessori/tessili-altro/_/N-101b7n2
##鞋
#lv-it-genderAndNameAndCategoryUrl7=women&&Stivaletti e Stivali&&https://it.louisvuitton.com/ita-it/donna/calzature/stivaletti-e-stivali/_/N-64fxn5
#lv-it-genderAndNameAndCategoryUrl8=women&&Décolleté&&https://it.louisvuitton.com/ita-it/donna/calzature/decollete/_/N-sr1k15
#lv-it-genderAndNameAndCategoryUrl9=women&&Calzature basse&&https://it.louisvuitton.com/ita-it/donna/calzature/calzature-basse/_/N-1tdqh9r
#lv-it-genderAndNameAndCategoryUrl10=women&&Sneakers&&https://it.louisvuitton.com/ita-it/donna/calzature/sneakers-/_/N-1yqsd7v
#lv-it-genderAndNameAndCategoryUrl11=women&&Sandali&&https://it.louisvuitton.com/ita-it/donna/calzature/sandali/_/N-qr86vf


#men
#腰带
lv-it-genderAndNameAndCategoryUrl12=men&&Cinture&&https://it.louisvuitton.com/ita-it/uomo/accessori/cinture/_/N-1mrbnsa
##小皮具
#lv-it-genderAndNameAndCategoryUrl13=men&&Portafogli&&https://it.louisvuitton.com/ita-it/uomo/piccola-pelletteria/portafogli/_/N-1cxd6kl
#lv-it-genderAndNameAndCategoryUrl14=men&&Portafogli Compatti&&https://it.louisvuitton.com/ita-it/uomo/piccola-pelletteria/portafogli-compatti/_/N-nod9vu
#lv-it-genderAndNameAndCategoryUrl15=men&&Portachiavi&Porta carte&&https://it.louisvuitton.com/ita-it/uomo/piccola-pelletteria/portachiavi-porta-carte/_/N-1plukgk
#lv-it-genderAndNameAndCategoryUrl16=men&&Cover Smartphone&&https://it.louisvuitton.com/ita-it/uomo/piccola-pelletteria/cover-smartphone/_/N-gf1ru0
##围巾披肩
#lv-it-genderAndNameAndCategoryUrl17=men&&Tessili & cravatte & altro&&https://it.louisvuitton.com/ita-it/uomo/accessori/tessili-cravatte-altro/_/N-lopaf7
##鞋
#lv-it-genderAndNameAndCategoryUrl18=men&&Stivaletti e Stivali&&https://it.louisvuitton.com/ita-it/uomo/calzature/stivaletti-e-stivali/_/N-jt1253
#lv-it-genderAndNameAndCategoryUrl19=men&&Mocassini &Car Shoe&&https://it.louisvuitton.com/ita-it/uomo/calzature/mocassini-car-shoe/_/N-ywhxp0
#lv-it-genderAndNameAndCategoryUrl20=men&&Calzature stringate & Calzature con fibbia&&https://it.louisvuitton.com/ita-it/uomo/calzature/calzature-stringate-calzature-con-fibbia/_/N-3vkbi2
#lv-it-genderAndNameAndCategoryUrl21=men&&Sneakers&&https://it.louisvuitton.com/ita-it/uomo/calzature/sneakers/_/N-h0aqhr
#lv-it-genderAndNameAndCategoryUrl22=men&&Sandali&&https://it.louisvuitton.com/ita-it/uomo/calzature/sandali/_/N-zan4w2


 */
//@Component("louisvuittonIT")
public class LouisvuittonIT {

    //有库存
    private static final String IN_STOCK = "1";
    //无库存
    private static final String NO_STOCK = "0";

    private static String supplierId="2018070502014";
    private static String supplierNo="S0000963";

    // 商品barCodeMap  key:barCode value:null
    private static HashMap<String, String> barCodeMap= null;

    //商品失败请求信息
    private static List<RequestFailProductDTO> failList = null;

    /**
     * 特殊处理 商品品类名称 字符 如： ,高跟鞋,凉鞋,腰带
     */
    private static String sizeCategoryNames = "";


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
    // 所有品类相对路径以及名称 如： women&&新作：RE(BELLE)&&/zh/ca/women/handbags/new,women&&手提包&波士顿包&&/zh/ca/women/handbags/top-handles
    private static List<String> genderAndNameAndCategoryUris = new ArrayList<>();
    private static OutputStreamWriter out= null;
    static String splitSign = ",";


    static {
        if (null == bdl)
            bdl = ResourceBundle.getBundle("conf");
        uri = bdl.getString("uri-it");
        path = bdl.getString("path-it");
        destFilePath = bdl.getString("destFilePath-it");
        flag = bdl.getString("flag-it");
        sizeCategoryNames = bdl.getString("sizeCategoryNames-it");

        Enumeration<String> keys = bdl.getKeys();
        while(keys.hasMoreElements()){
            String key = keys.nextElement();
            if(key.startsWith("lv-it-genderAndNameAndCategoryUrl")){
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
            System.out.println("=============it-处理重复数据开始===============");
            logger.info("=============it-处理重复数据开始===============");
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
                        "qtyDesc" + splitSign +


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
            barCodeMap = new HashMap<String,String>();
            failList = new ArrayList<>();

            int productModeNumber = 0;

            //校验配置信息 是否符合格式要求
            int size = genderAndNameAndCategoryUris.size();
            for (int i = 0; i < size; i++) {

                String[] sexAnduriAndName = genderAndNameAndCategoryUris.get(i).split("&&");
                if(sexAnduriAndName.length!=3) {
                    logger.info(" lv-it-genderAndCategoryUri 不符合格式 ：" + genderAndNameAndCategoryUris.get(i));
                    System.out.println(" lv-it-genderAndCategoryUri 不符合格式 ：" + genderAndNameAndCategoryUris.get(i));
                    throw new RuntimeException("配置文件 genderAndCategoryUri 不符合格式："+genderAndNameAndCategoryUris.get(i));
                }
            }

            for (int i = 0; i < size; i++) {
                if(i>0){
                    String[] temsexAnduriAndName = genderAndNameAndCategoryUris.get(i-1).split("&&");

                    System.out.println("lv-it-拉取品类结束 gender:"+temsexAnduriAndName[0]+" categoryName:"+temsexAnduriAndName[1]+" uri:"+temsexAnduriAndName[2]);
                    System.out.println("共拉取 ："+(barCodeMap.size()-productModeNumber)+"款商品数据");
                    System.out.println("=====================================================================");

                    logger.info("lv-it-拉取品类结束 gender:"+temsexAnduriAndName[0]+" categoryName:"+temsexAnduriAndName[1]+" uri:"+temsexAnduriAndName[2]);
                    logger.info("共拉取 ："+(barCodeMap.size()-productModeNumber)+"款商品数据");
                    logger.info("=====================================================================");
                    productModeNumber = barCodeMap.size();
                }

                String[] sexAnduriAndName = genderAndNameAndCategoryUris.get(i).split("&&");
                //拉取 品类信息
                System.out.println("lv-it-开始拉取："+sexAnduriAndName[0]+":"+sexAnduriAndName[1]+":"+sexAnduriAndName[2]);
                logger.info("lv-it-开始拉取："+sexAnduriAndName[0]+":"+sexAnduriAndName[1]+":"+sexAnduriAndName[2]);
                getAllProductsCategroyUrl(sexAnduriAndName[0],sexAnduriAndName[1],sexAnduriAndName[2]);

            }
            String[] temsexAnduriAndName = genderAndNameAndCategoryUris.get(size-1).split("&&");
            System.out.println();
            System.out.println("lv-it-拉取品类结束 gender:"+temsexAnduriAndName[0]+" categoryName:"+temsexAnduriAndName[1]+" uri:"+temsexAnduriAndName[2]);
            System.out.println("共拉取 ："+(barCodeMap.size()-productModeNumber)+"款商品数据");
            System.out.println("所有品类拉取结束共:"+barCodeMap.size()+"款商品数据");
            System.out.println("=====================================================================");


            logger.info("lv-it-拉取品类结束 gender:"+temsexAnduriAndName[0]+" categoryName:"+temsexAnduriAndName[1]+" uri:"+temsexAnduriAndName[2]);
            logger.info("共拉取 ："+(barCodeMap.size()-productModeNumber)+"款商品数据");
            logger.info("所有品类拉取结束共:"+barCodeMap.size()+"款商品数据");
            logger.info("=====================================================================");

            System.out.println("==========IT重新请求失败的商品及品类信息===================================================");
            logger.error("==========IT重新请求失败的商品及品类信息===================================================");
            int failSize = failList.size();
            for (int i = 0; i <failSize ; i++) {
                RequestFailProductDTO failProduct = failList.get(i);
                String flag = failProduct.getFlag();
                if("2".equals(flag)){
                    getAllProductsCategroyUrl(failProduct.getGender(),failProduct.getCategoryName(),failProduct.getUrl());
                }else if("1".equals(flag)){
                    //不校验 productModel 是否存在
                    grapProductDetailMaterials(failProduct.getGender(),failProduct.getCategoryName(),failProduct.getUrl());
                }else if("0".equals(flag)){
                    //不校验 productModel 是否存在
                    grapProductDetailMaterials(failProduct.getGender(),failProduct.getCategoryName(),failProduct.getUrl());
                }
            }
            System.out.println("==========IT请求失败的商品及品类信息 结束===================================================");
            logger.info("==========IT请求失败的商品及品类信息 结束===================================================");
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
    public void getAllProductsCategroyUrl(String sex,String categoryName,String categoryUrl) {

        //请求分页的参数时 ni
        if(categoryUrl==null||"".equals(categoryUrl)){
            return;
        }
        try {
            HttpResponse response = HttpUtils.get(categoryUrl);
            if (response.getStatus() == 200) {
                String htmlContent = response.getResponse();

                Document doc = Jsoup.parse(htmlContent);
                //获取第一页商品数据 18 条
                grapProductListByCategoryUrlOrDoc(sex,categoryName,null,doc);

                /**
                 * 获取商品总页数 处理该品类剩余所有商品数据
                 */
                Elements countNumberElements = doc.select("#filtersHeader").select("div.filters-counter").select("div.filters-counter-nb");
                if(countNumberElements!=null&&countNumberElements.size()>0){
                    String productCountNumberStr = countNumberElements.first().attr("data-products-count");
                    //获取商品的 每页显示数
                    String pageSizeStr = "";
                    Elements scriptElements = doc.select("#content").select("script");
                    int scriptSize = scriptElements.size();
                    for (int i = 0; i < scriptSize; i++) {
                        Element temScriptElement = scriptElements.get(i);
                        String html = temScriptElement.html().trim();
                        if(html.contains("var maxPageSize =")){
                            String[] split = html.split(";");
                            for (String temStr:split) {
                                if(temStr.contains("var maxPageSize =")){
                                    pageSizeStr = temStr.replace("var maxPageSize =","").replace(" ","");
                                }
                            }
                            break;
                        }
                    }
                    //计算出商品的总页数
                    Integer pageSize = Integer.parseInt(pageSizeStr);
                    int productCountNumber = Integer.parseInt(productCountNumberStr);
                    Integer pageNumber = getPageNumber(productCountNumber, pageSize);
                    if(pageNumber>1){
                        //拼接该类别 获取所有商品数据url
                        // /ajax/endeca/browse-frag/donna/calzature/tutte-le-collezioni/_/N-16qh15m?storeLang=ita-it&pageType=category&Nrpp=198&originalNrpp=18&No=18
                        String getAllCategoryUrl = "";
                        Integer nrpp = (pageNumber-1)*pageSize;
                        if(categoryUrl.contains("?")){
                            getAllCategoryUrl = categoryUrl.replace("ita-it","ajax/endeca/browse-frag") +"&storeLang=ita-it&pageType=category&Nrpp="+nrpp+"&originalNrpp="+pageSizeStr+"&No="+pageSizeStr;
                        }else{
                            getAllCategoryUrl = categoryUrl.replace("ita-it","ajax/endeca/browse-frag") +"?storeLang=ita-it&pageType=category&Nrpp="+nrpp+"&originalNrpp="+pageSizeStr+"&No="+pageSizeStr;
                        }

                        //爬取该类别首页 剩余所有商品数据
                        grapProductListByCategoryUrlOrDoc(sex,categoryName,getAllCategoryUrl,null);
                    }
                }else{ //获取商品总数失败 做重复请求 失败后添加到失败列表
                    System.out.println("获取商品类别商品总数目失败："+sex+":"+categoryName+":"+categoryUrl);
                    loggerError.error("获取商品类别商品总数目失败："+sex+":"+categoryName+":"+categoryUrl);
                }
            }else{
                //添加到 失败 请求中
                failList.add(new RequestFailProductDTO(sex, categoryName, categoryUrl, "1"));
                System.err.println("it-请求商品品类 地址出错  "+categoryUrl);
                logger.error("it-请求商品品类 地址出错categoryUrl {}  "+categoryUrl);
                return;
            }
        }catch (Exception e){
            //添加到 失败 请求中
            failList.add(new RequestFailProductDTO(sex, categoryName, categoryUrl, "2"));
            System.out.println("it-请求商品品类 地址出错categoryUrl{}"+categoryUrl+e.getMessage());
            loggerError.error("it-请求商品品类 地址出错categoryUrl{}"+categoryUrl+e.getMessage());
            return;
        }
    }


    /**
     * 拉取 品类 下的 商品列表信息
     *
     * @param sex 性别
     * @param categoryName  品类名称
     * @param categoryUrl  品类url
     */
    public void grapProductListByCategoryUrlOrDoc(String sex,String categoryName,String categoryUrl,Document doc) {

        try {
            if(doc==null&&categoryUrl!=null){
                Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36)");
                Header[] headers = new Header[1];
                headers[0] = header;
                HttpResponse response = HttpUtils.get(categoryUrl,headers);
                if (response.getStatus() == 200) {
                    String htmlContent = response.getResponse();
                    doc = Jsoup.parse(htmlContent);
                }else{
                    //添加到 失败 请求中
                    failList.add(new RequestFailProductDTO(sex, categoryName, categoryUrl, "1"));
                    System.out.println("it-请求商品品类 地址出错categoryUrl{}"+categoryUrl);
                    loggerError.error("it-请求商品品类 地址出错categoryUrl{}"+categoryUrl);
                    return;
                }
            }
            //获取商品列表页数据
            Elements productElemets = doc.select("#products-grid").select("div.pl-page").select("a.product-item");
            //处理当前页的数据
            int productSize = productElemets.size();


            for (int i = 0; i < productSize; i++) {
                Element prductAElement = productElemets.get(i);
                String productDetailUrl = uri + prductAElement.attr("href");
                //处理商品 分页 信息
                grapProductDetailMaterials(sex,categoryName,productDetailUrl);
            }
        }catch (Exception e){

        }
    }

    /**
     * 访问商品所有style url，并输出到csv文件
     * @param productDetailUrl 商品详情url
     */
    private void grapProductDetailMaterials(String sex,String categoryName,String productDetailUrl){
        if(StringUtils.isBlank(productDetailUrl)){
            return;
        }
        try {
            Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36)");
            Header[] headers = new Header[1];
            headers[0] = header;

            HttpResponse response = HttpUtils.get(productDetailUrl,headers);

            if (response.getStatus()==200) {
                String htmlContent = response.getResponse();
                Document doc = Jsoup.parse(htmlContent);

                Elements materialElements = doc.select("#attributePanelOpenedWrapper").select("ul.Material").select("li");
                //.select("class[aria-live=polite]").select("div.slick-track").select("div");
                //是否有材质信息区分
                if(materialElements!=null&&materialElements.size()>0){
                    int materialSize = materialElements.size();
                    for (int i = 0; i <materialSize ; i++) {
                        Element materialElement = materialElements.get(i);

                        Elements aElements = materialElement.select("a");
                        if(aElements==null||aElements.size() == 0){ //当前页就是该材质的url
                            //材质名称
                            String materialName = materialElement.select("img").first().attr("data-alt");
                            grapProductDetailColors( sex, categoryName,materialName, productDetailUrl, doc);
                            continue;
                        }
                        //材质名称
                        String materialName = materialElement.select("span").first().text();

                        //获取材质url
                        String productMaterialDetailUrl = aElements.first().attr("href");
                        grapProductDetailColors( sex, categoryName,materialName, productMaterialDetailUrl, null);
                    }
                }else{// 没有商品材质信息 将doc 获取商品 color 信息
                    grapProductDetailColors( sex, categoryName,"", productDetailUrl, doc);
                }

            }else{
                failList.add(new RequestFailProductDTO(sex,categoryName,productDetailUrl,"0"));
                System.err.println("it-savePre请求商品url 失败"+sex +":"+categoryName+":"+productDetailUrl);
                logger.error("it-savePre请求商品url 失败"+sex +":"+categoryName+":"+productDetailUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 拉取 某一材质下 所有的商品颜色sku
     * 该商品  1. 有颜色 的区分  直接获取所有的颜色skuNo
     *        2. 没有颜色区分 商品详情中获取所商品尺码 skuNo
     *
     * @param sex 性别
     * @param categoryName  品类名称
     * @param productMaterialDetailUrl  商品详情某一材质 url
     */
    public void grapProductDetailColors(String sex,String categoryName,String materialName ,String productMaterialDetailUrl,Document doc) {

        try {
            if(doc==null){
                Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36)");
                Header[] headers = new Header[1];
                headers[0] = header;
                HttpResponse response = HttpUtils.get(productMaterialDetailUrl,headers);
                if (response.getStatus() == 200) {
                    String htmlContent = response.getResponse();
                    doc = Jsoup.parse(htmlContent);
                }else{
                    //添加到 失败 请求中
                    failList.add(new RequestFailProductDTO(sex, categoryName, productMaterialDetailUrl, "1"));
                    System.out.println("it-地址出错 productMaterialDetailUrl"+productMaterialDetailUrl);
                    loggerError.error("it-地址出错 productMaterialDetailUrl"+productMaterialDetailUrl);
                    return;
                }
            }
            //获取商品颜色数据（可能没有）
            Elements productColorElemets = doc.select("#attributePanelOpenedWrapper").select("ul.Color").select("li");
            if(productColorElemets!=null&&productColorElemets.size()>0){
                int colorSize = productColorElemets.size();
                for (int i = 0; i < colorSize; i++) {
                    Element colorElement = productColorElemets.get(i);

                    Elements aElements = colorElement.select("a");
                    if(aElements==null||aElements.size() == 0){ //当前页就是该材质的url
                        //获取当前页的 颜色skuNO
                        Element barCodeElement  = doc.select("#infoProductBlockTop").select("div.sku").first();
                        String colorSkuNo = barCodeElement.text().trim();

                        //颜色名称
                        String colorName = colorElement.select("img").first().attr("data-alt");
                        grapProductDetailSizes( sex, categoryName,materialName,colorName,colorSkuNo, productMaterialDetailUrl, null);
                        continue;
                    }
                    //颜色名称
                    String colorName = "";
                    Elements colorNameElements = colorElement.select("span");
                    if(colorNameElements!=null&&colorNameElements.size()>0){
                        colorName = colorNameElements.first().text();
                    }
                    if(colorName==null||"".equals(colorName)){
                        colorName = colorElement.select("img").first().attr("alt");
                    }
                    //获取颜色skuNo
                    String colorSkuNo = aElements.first().attr("data-sku");
                    if(colorSkuNo==null ||"".equals(colorSkuNo)){
                        continue;
                    }
                    grapProductDetailSizes( sex, categoryName,materialName,colorName,colorSkuNo, productMaterialDetailUrl, null);
                }

            }else{ //没有颜色信息 开始获取商品的尺码信息
                grapProductDetailSizes( sex, categoryName, materialName, "", "", productMaterialDetailUrl, doc);
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("解析颜色skuNo信息失败---");
        }

        //每一款商品 休息 十秒钟
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    /**
     * 拉取 品类 下的 商品列表信息
     *
     * @param sex 性别
     * @param categoryName  品类名称
     * @param productColorDetailUrl  商品详情某一材质 url
     */
    public void grapProductDetailSizes(String sex,String categoryName,String materialName,String colorName, String colorSkuNo,String productColorDetailUrl,Document doc) {

        try {
            if(doc==null&&colorSkuNo!=null&&!"".equals(colorSkuNo)){  // doc 为 null 该商品有颜色 skuNo 区分 去请求单独 url 获取该颜色skuNO 下的所有尺码skuNo
                //1.请求特殊url 获取该颜色下的所有尺码 skuNo
                List<String> sizeSkuNoAndNames = getSizeSkuNoListByColorSkuNo(colorSkuNo);
                //2.总体获取该商品的基础信息
                LouisvuittonDTO product = grapProductDetail(sex, categoryName, materialName, colorName, "", productColorDetailUrl, null);
                /**
                 * TODO 判断 商品数据是否为空
                 */
                //3.遍历该商品尺码的skuNo 获取具体尺码下的价格 库存 图片信息
                if(sizeSkuNoAndNames.size()>0){ //该颜色有多个尺码
                    exportProductsByBaseProductInfoAndSkuNoNames(product,sizeSkuNoAndNames,productColorDetailUrl);
                }else{  //该颜色没有尺码信息
                    Map<String, String> productSkuPrivateInfo = getProductSkuPrivateInfo(colorSkuNo);
                    String itemPrice = productSkuPrivateInfo.get("itemPrice");
                    String pics = productSkuPrivateInfo.get("pics");
                    String qty = productSkuPrivateInfo.get("qty");
                    String qtyDesc = productSkuPrivateInfo.get("qtyDesc");
                    product.setSize("U");
                    product.setBarCode(colorSkuNo);
                    product.setUrl(productColorDetailUrl+"#"+colorSkuNo);
                    product.setItemprice(itemPrice);
                    product.setPicUrls(pics);
                    product.setQty(qty);
                    product.setQtyDesc(qtyDesc);
                    exportExcel(product);
                }

            }else{ //没有颜色skuNo 直接 从 doc中获取该商品的尺码 skuNo

                //1.获取商品基础信息
                LouisvuittonDTO baseProductInfo = grapProductDetail(sex, categoryName, materialName, colorName, "", productColorDetailUrl, doc);

                //获取商品图片、价格、 列表页数据
                //https://it.louisvuitton.com/ajax/product.jsp?storeLang=ita-it&pageType=product&id=1A4BO8
                // &requestingURL=https://it.louisvuitton.com/ita-it/prodotti/perfecto-con-interno-monogram-nvprod920154v
                Elements productSizeElemets = doc.select("#size").select("li[data-skuSubType=product]");
                /**
                 <li data-sku="1A3YXO" data-ona="Size/35.5" data-skuSubType="product"
                 value="https://it.louisvuitton.com/ita-it/prodotti/stivaletto-fireball-nvprod650059v"> 35.5
                 <a href="https://it.louisvuitton.com/ita-it/prodotti/stivaletto-fireball-nvprod650059v">
                 <span>35.5</span>
                 </a>
                 </li>
                 */
                if(productSizeElemets!=null&&productSizeElemets.size()>0){
                    //2.定义尺码skuNo:skuName 集合
                    List<String> sizeSkuNoAndNames =  new ArrayList<>();
                    int sizeNumber = productSizeElemets.size();
                    for (int i = 0; i < sizeNumber; i++) {
                        Element sizeElement = productSizeElemets.get(i);
                        String sizeSku = sizeElement.attr("data-sku");
                        String sizeName = sizeElement.attr("data-ona").replace("Size/","").replace(",",".");
                        sizeSkuNoAndNames.add(sizeSku+":"+sizeName);
                    }

                    //3.遍历该商品尺码的skuNo 获取具体尺码下的价格 库存 图片信息
                    exportProductsByBaseProductInfoAndSkuNoNames(baseProductInfo,sizeSkuNoAndNames,productColorDetailUrl);

                }else{ //该商品 没有尺码信息
                    baseProductInfo.setSize("U"); //设置商品为均码
                    //当该商品有颜色skuNo 信息的时候 需要单独获取该 skuno 的 信息
                    if(colorSkuNo!=null&&!"".equals(colorSkuNo)){
                        //获取该skuNo 的 私有信息 价格、图片、库存、库存描述
                        Map<String, String> productSkuPrivateInfo = getProductSkuPrivateInfo(colorSkuNo);
                        String itemPrice = productSkuPrivateInfo.get("itemPrice");
                        String pics = productSkuPrivateInfo.get("pics");
                        String qty = productSkuPrivateInfo.get("qty");
                        String qtyDesc = productSkuPrivateInfo.get("qtyDesc");
                        baseProductInfo.setBarCode(colorSkuNo);
                        baseProductInfo.setUrl(productColorDetailUrl+"#"+colorSkuNo);
                        baseProductInfo.setItemprice(itemPrice);
                        baseProductInfo.setPicUrls(pics);
                        baseProductInfo.setQty(qty);
                        baseProductInfo.setQtyDesc(qtyDesc);
                    }
                    exportExcel(baseProductInfo);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取商品详情信息
     * @param sex 性别
     * @param categoryName 品类名称
     * @param materialName 材质名称
     * @param colorName 颜色名称
     * @param sizeName 尺码名称
     * @param productMaterialDetailUrl 商品详情url
     * @param doc
     * @return
     */
    private  LouisvuittonDTO grapProductDetail(String sex,String categoryName,String materialName,String colorName,String sizeName, String productMaterialDetailUrl,Document doc){

        LouisvuittonDTO product = new LouisvuittonDTO();
        //商品描述
        String  desc= "";
        //商品名称
        String productName="";
        //供应商 spuNo
        String spuNo = "";
        //货号
        String barCode= "";
        //品牌
        String brand = "LOUIS VUITTON";
        //商品 尺寸大小描述
        StringBuffer sizeDesc=new StringBuffer();
        //产地
        String made = "";
        //市场价
        String itemPrice="";
        //库存
        String qty="";
        //库存描述信息
        String qtyDesc = "";
        //图片信息
        StringBuffer picSrcs = new StringBuffer();  //多张图片 链接Str  用|分割
        String season = "2018春夏";
        //模特测试信息
        String modelMeasurements = "";


        try {
            if(doc==null){
                Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36)");
                Header[] headers = new Header[1];
                headers[0] = header;
                HttpResponse response = HttpUtils.get(productMaterialDetailUrl,headers);
                if (response.getStatus() == 200) {
                    String htmlContent = response.getResponse();
                    doc = Jsoup.parse(htmlContent);
                }else{
                    //添加到 失败 请求中
                    failList.add(new RequestFailProductDTO(sex, categoryName, productMaterialDetailUrl, "1"));
                    System.out.println("it-地址出错 productMaterialDetailUrl"+productMaterialDetailUrl);
                    loggerError.error("it-地址出错 productMaterialDetailUrl"+productMaterialDetailUrl);

                }
            }

            /** 商品名称
             <div class="productName title" id="productName">
             <h1 itemprop="name">Stivaletto Fireball  </h1>
             </div>
             */
            Element proNameElement  = doc.select("#productName").select("h1").first();
            if(proNameElement!=null){
                productName = proNameElement.text().replace(",", ".").trim();
            }

            /**
             *  商品货号(实际是url里面  某一具体sku )
             */
            Element barCodeElement  = doc.select("#infoProductBlockTop").select("div.sku").first();
            if(barCodeElement!=null){
                barCode = barCodeElement.text().trim();
            }

            /**
             商品市场价格
             */
            Element itemPriceElement  = doc.select("#infoProductBlockTop").select("table.priceButton").select("td.priceValue").first();
            if(itemPriceElement!=null){
                itemPrice = itemPriceElement.text().replace("€","").replace(".","").replace(",",".").trim();
            }

            //处理商品图片信息
            Elements imgsElements = doc.select("#productSheetSlideshow").select("ul.bigs").select("li");
            int imageSize = imgsElements.size();
            for (int i = 0; i < imageSize ; i++) {
                /**
                 <img
                 data-src="https://it.louisvuitton.com/images/is/image/lv/1/PP_VP_L/louis-vuitton-stivaletto-fireball-calzature--AE8Q3IPC02_PM2_Front view.jpg?wid={IMG_WIDTH}&hei={IMG_HEIGHT}"
                 dataCategory="productMain" itemprop="image"
                 */

                String imgUrl = imgsElements.get(i).select("img").first().attr("data-src").replace("{IMG_WIDTH}","1200").replace("{IMG_HEIGHT}","1200")
                        .replace("\n","").replace("\r","").trim();
                if(i==0){
                    picSrcs.append(imgUrl);
                }else{
                    picSrcs.append("|").append(imgUrl);
                }
            }

            //商品描述 spice-product-detail
            Element descriptElement  = doc.select("div#productDescriptionSeeMore").first();
            if(descriptElement!=null){
                desc = descriptElement.text();
                desc = desc.replace(",",".").replace("\"", "");
            }

            /**
             *  TODO 季节
             */

            //获取商品尺寸描述信息
            Elements sizeDescElements  = doc.select("div#textClientInfo_productDescription").select("div.functional-text");
            if(sizeDescElements!=null&&sizeDescElements.size()>0){
                String sizeText = sizeDescElements.first().ownText().trim();
                System.out.println(sizeText);
                sizeDesc.append(sizeText.trim());
            }
            //商品其他信息 如 颜色、产地、省份、尺码大小   第一行含有 cm 就是商品的尺寸描述信息 否则为 材质信息 （如果材质原本不为空，不用赋值）
            Elements otherInfoElements  = doc.select("div#textClientInfo_productDescription").select("ul").select("li");
            int otherInfoSize = otherInfoElements.size();
            StringBuffer temMaterialInfo = new StringBuffer();
            for (int i = 0; i < otherInfoSize ; i++) {
                Element otherInfoElement = otherInfoElements.get(i);
                String text = otherInfoElement.text();
                String replace = text.replace(",", ".");
                //尺寸
                if(replace.contains("cm")|| replace.contains("mm")){  //商品尺寸信息 可能含有 模特信息
                    sizeDesc.append(replace);
                }else if(replace.contains("Made in")){  //产地
                    made = replace;
                }else if(replace.contains("%")||replace.contains("gram")||replace.contains("acchetta")||replace.contains("initure ")
                        ||replace.contains("etalliche")||replace.contains("fibra")||replace.contains("vitello")||replace.contains("ateriale")
                        ||replace.contains("oliestere")||replace.contains("odera")||replace.contains("cupro")
                        ||replace.contains("gram")||replace.contains("gram")||replace.contains("gram")){  //为材质信息 多数 第一个为材质信息
                    //判断材质信息是否为空 或者 空串
                    temMaterialInfo.append(replace.replace(",",".")+" ");
                }
                //模特信息(暂时无用)
                if(replace.contains("model")){
                    modelMeasurements = modelMeasurements+" "+ replace;
                }
            }
            //拼接材质信息
            materialName = materialName+" "+ temMaterialInfo.toString();

            colorName = colorName.replace(",",".");
            materialName = materialName.replace(",",".");

            /**
             *  获取商品 库存信息
             *  https://secure.louisvuitton.com/ajaxsecure/getStockLevel.jsp?storeLang=ita-it&pageType=product&skuIdList=1A47Z3,1A47Z4,1A47Z5,1A47Z6,1A47Z7,1A47Z8,1A47Z9,1A47ZA&null&_=1532426961111
             */
            Map<String,String> resultMap = getProductQty(barCode);
            qty = resultMap.get("qty");
            qtyDesc = resultMap.get("qtyDesc");

            product.setUrl(productMaterialDetailUrl+"#"+barCode);
            product.setMaterial(materialName);
            product.setProductName(productName);
            product.setBrand(brand);
            product.setDescript(desc);
            product.setBarCode(barCode);
            product.setMade(made);
            product.setSex(sex);
            product.setSeason(season);
            product.setColorName(colorName);
            product.setSizeDesc(sizeDesc.toString());
            product.setItemprice(itemPrice);

            product.setSpuNo(spuNo);
            product.setCategory(categoryName);
            product.setPicUrls(picSrcs.toString().replace("\"",""));
            product.setSize(sizeName);
            product.setQty(qty);
            product.setQtyDesc(qtyDesc);

            //判断当前包页面有没有尺码信息  有分成多个 product 没有 尺码为均码
            //   .spice-dropdown-pdp-size-box
            exportExcel(product);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return product;
    }


    /**
     * 根据商品基础信息 以及 skuNo 列表 导出商品的 信息
     * @param product 商品基础信息
     * @param sizeSkuNoAndNames sku编号名称 列表
     * @param productColorDetailUrl  商品原始url
     */
    private void exportProductsByBaseProductInfoAndSkuNoNames(LouisvuittonDTO product,List<String> sizeSkuNoAndNames,String productColorDetailUrl){

        //3.遍历该商品尺码的skuNo 获取具体尺码下的价格 库存 图片信息
        int size = sizeSkuNoAndNames.size();
        if(size>0){

        }else{

        }
        for (int i = 0; i < size; i++) {
            String sizeSkuNoAndName = sizeSkuNoAndNames.get(i);
            String[] split = sizeSkuNoAndName.split(":");  //
            String skuNo = split[0];
            String sizeName = split[1];
            if(split.length==3){
                System.out.println("尺码信息中含有 :  productColorDetailUrl -> "+productColorDetailUrl );
            }
            //获取该skuNo 的 私有信息 价格、图片、库存、库存描述
            Map<String, String> productSkuPrivateInfo = getProductSkuPrivateInfo(skuNo);
            String itemPrice = productSkuPrivateInfo.get("itemPrice");
            String pics = productSkuPrivateInfo.get("pics");
            String qty = productSkuPrivateInfo.get("qty");
            String qtyDesc = productSkuPrivateInfo.get("qtyDesc");

            product.setSize(sizeName);
            product.setBarCode(skuNo);
            product.setUrl(productColorDetailUrl+"#"+skuNo);
            product.setItemprice(itemPrice);
            product.setPicUrls(pics);
            product.setQty(qty);
            product.setQtyDesc(qtyDesc);

            exportExcel(product);
        }
    }

    /**
     * 获取商品库存信息
     * @param skuNo
     * @throws Exception
     */
    public Map<String,String> getProductQty(String skuNo) throws Exception{
        //定义结果集
        Map<String,String> resultMap = new HashMap<String,String>();
        Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36)");
        Header[] headers = new Header[1];
        headers[0] = header;
        /**
         *  获取商品 库存信息
         *  https://secure.louisvuitton.com/ajaxsecure/getStockLevel.jsp?storeLang=ita-it&pageType=product&skuIdList=1A47Z3,1A47Z4,1A47Z5,1A47Z6,1A47Z7,1A47Z8,1A47Z9,1A47ZA&null&_=1532426961111
         */
        String qtyUrl = "https://secure.louisvuitton.com/ajaxsecure/getStockLevel.jsp?storeLang=ita-it&pageType=product&skuIdList="+skuNo;
        HttpResponse checkResponse = HttpUtils.get(qtyUrl,headers);
        if (checkResponse.getStatus()==200) {
            String resultJson = checkResponse.getResponse();
            /**
             "1A45H3":{
             "inStock":true,    true:有库存  false:无库存
             "backOrder":true   true:预订产品   false:可直接购买
             },
             */
            resultJson = resultJson.replace("\n","").trim();
            if(resultJson!=null&&!"".equals(resultJson)){
                JsonElement je = new JsonParser().parse(resultJson);
                JsonObject skuObject = je.getAsJsonObject().get(skuNo).getAsJsonObject();
                if(skuObject!=null){
                    String inStockStr = skuObject.get("inStock").toString();
                    String backOrderStr = skuObject.get("backOrder").toString();

                    if("true".equals(backOrderStr)){ //预售
                        resultMap.put("qty",NO_STOCK);
                        resultMap.put("qtyDesc","预售");
                    }else{
                        if("true".equals(inStockStr)){  //有货
                            resultMap.put("qty",IN_STOCK);
                            resultMap.put("qtyDesc","有货");

                        }else{  //售罄
                            resultMap.put("qty",NO_STOCK);
                            resultMap.put("qtyDesc","售罄");
                        }
                    }
                }else{
                    resultMap.put("qty",NO_STOCK);
                    resultMap.put("qtyDesc","请求库存数据接口失败");
                }
            }else{
                resultMap.put("qty",NO_STOCK);
                resultMap.put("qtyDesc","请求库存数据接口失败");
            }
        }else{
            System.out.println("请求库存数据接口失败");
            resultMap.put("qty",NO_STOCK);
            resultMap.put("qtyDesc","请求库存数据接口失败");
        }
        return resultMap;
    }


    /**
     * 获取某一尺码 skuNO 的 库存 库存描述 价格 图片
     * @param skuNo
     * @throws Exception
     */
    public Map<String,String> getProductSkuPrivateInfo(String skuNo){
        Map<String,String> resultMap = new HashMap<>();
        Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36)");
        Header[] headers = new Header[1];
        headers[0] = header;
        /**
         https://it.louisvuitton.com/ajax/product.jsp?storeLang=ita-it&pageType=product&id=1A43EA
         */
        String qtyUrl = "https://it.louisvuitton.com/ajax/product.jsp?storeLang=ita-it&pageType=product&id="+skuNo;
        HttpResponse response = null;
        try {
            response = HttpUtils.get(qtyUrl,headers);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response.getStatus()==200) {
            String htmlContent = response.getResponse();
            Document doc = Jsoup.parse(htmlContent);
            //获取价格数据
            /**
             商品市场价格
             */
            String itemPrice  = "";
            Element itemPriceElement  = doc.select("#infoProductBlockTop").select("table.priceButton").select("td.priceValue").first();
            if(itemPriceElement!=null){
                itemPrice = itemPriceElement.text().replace("€","").replace(".","").replace(",",".").trim();
            }
            //获取图片数据
            //处理商品图片信息
            //图片信息
            StringBuffer picSrcs = new StringBuffer();  //多张图片 链接Str  用|分割
            Elements imgsElements = doc.select("#productSheetSlideshow").select("ul.bigs").select("li");
            int imageSize = imgsElements.size();
            for (int i = 0; i < imageSize ; i++) {
                /**
                 <img
                 data-src="https://it.louisvuitton.com/images/is/image/lv/1/PP_VP_L/louis-vuitton-stivaletto-fireball-calzature--AE8Q3IPC02_PM2_Front view.jpg?wid={IMG_WIDTH}&hei={IMG_HEIGHT}"
                 dataCategory="productMain" itemprop="image"
                 */
                String imgUrl = imgsElements.get(i).select("img").first().attr("data-src").replace("{IMG_WIDTH}","1200").replace("{IMG_HEIGHT}","1200")
                        .replace("\n","").replace("\r","").trim();

                if(i==0){
                    picSrcs.append(imgUrl);
                }else{
                    picSrcs.append("|").append(imgUrl);
                }
            }
            //获取库存数据
            Map<String,String> qtyMap = null;
            try {
                qtyMap = getProductQty(skuNo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(qtyMap!=null){
                resultMap.put("qty",qtyMap.get("qty"));
                resultMap.put("qtyDesc",qtyMap.get("qtyDesc"));
            }else{
                resultMap.put("qty","0");
                resultMap.put("qtyDesc","库存获取失败");
            }
            resultMap.put("pics",picSrcs.toString());
            resultMap.put("itemPrice",itemPrice);
        }else{
            /**
             * TODO 获取 skuNo私有数据失败
             */
            System.out.println("------------ 获取 skuNo私有数据失败--------------------");
        }
        return resultMap;
    }


    /**
     * 获取某一颜色skuNO 下的所有尺码skuNo 信息 每一个 stirng   skuNo:skuName
     * @param skuNo
     * @throws Exception
     */
    public List<String> getSizeSkuNoListByColorSkuNo(String skuNo) throws Exception{
        List<String> sizeSkuNos = new ArrayList<String>();
        Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36)");
        Header[] headers = new Header[1];
        headers[0] = header;
        /**
         https://it.louisvuitton.com/ajax/product.jsp?storeLang=ita-it&pageType=product&id=1A43EA
         */
        String qtyUrl = "https://it.louisvuitton.com/ajax/product.jsp?storeLang=ita-it&pageType=product&id="+skuNo;
        HttpResponse response = HttpUtils.get(qtyUrl,headers);
        if (response.getStatus()==200) {
            String htmlContent = response.getResponse();
            Document doc = Jsoup.parse(htmlContent);
            Elements productSizeElemets = doc.select("#size").select("li[data-skuSubType=product]");
            /**
             <li data-sku="1A3YXO" data-ona="Size/35.5" data-skuSubType="product"
             value="https://it.louisvuitton.com/ita-it/prodotti/stivaletto-fireball-nvprod650059v"> 35.5
             <a href="https://it.louisvuitton.com/ita-it/prodotti/stivaletto-fireball-nvprod650059v">
             <span>35.5</span>
             </a>
             </li>
             */
            int sizeNumber = productSizeElemets.size();
            for (int i = 0; i < sizeNumber; i++) {
                Element sizeElement = productSizeElemets.get(i);
                String sizeSku = sizeElement.attr("data-sku");
                String sizeName = sizeElement.attr("data-ona").replace("Size/","").replace(",",".");
                sizeSkuNos.add(sizeSku+":"+sizeName);
            }
        }
        return sizeSkuNos;
    }


    /**
     * 保存并处理重复商品数据 时机：获取具体某一style 商品数据 前（判断是否拉取过） 后（将处理过的spu 存入skuMap ）
     * @param sex 性别
     * @param categoryName 品类名称
     * @param productDetailLink 商品详情绝对路径
     * @param barCode 商品货号
     */
    public void saveAndSolveRepeatSpu(String sex,String categoryName,String productDetailLink,String barCode){

        //校验之前是否已经存储过该SPU
        if(barCodeMap.containsKey(barCode)){ //之前处理过
            return;
        }
        //获取商品详情页数据
        //saveByProductDetailLink( sex, categoryName, productDetailLink);
        /**
         * 将商品的 barCode 加入到 barCodeMap 中
         */
        barCodeMap.put(barCode,null);
        System.out.print(" "+barCodeMap.size());
    }

    /**
     * 导出单个商品信息到csv 文件（追加）
     * @param dto 商品信息DTO
     */
    private static void exportExcel(LouisvuittonDTO dto) {
        if(dto==null||dto.getProductName()==null||"null".equals(dto.getProductName())||dto.getSpuNo()==null||"null".equals(dto.getSpuNo())){
            return;
        }

        /**
         * TODO 判断当前商品barCode 是否特殊处理条件：  1.配置文件指定的品类（如：鞋、衣服） 2.非均码
         * barCode 生成规则如下：
         * 1、没有颜色：前五位+productId数字部分
         * 2、有颜色：前五位+颜色名称
         */
        String categoryName = dto.getCategory();
        String colorName = dto.getColorName();

        String sizeName = dto.getSize();
        String productMaterialDetailUrl = dto.getUrl();
        String temBarCode = dto.getBarCode();
        dto.setSupplierSkuNo(temBarCode);
        if(sizeCategoryNames.contains((","+categoryName+","))&&!"U".equals(sizeName)){  //当前商品符合 特殊处理要求
            if(colorName!=null&&!"".equals(colorName)){ //有颜色
                temBarCode = temBarCode.substring(0,5)+colorName;
                dto.setBarCode(temBarCode);
            }else{ //没有颜色
                //获取当前商品的productId

                String[] split = productMaterialDetailUrl.split("-");
                String productIdStr = split[split.length-1];

                Pattern pattern = Pattern.compile("[^0-9]");
                Matcher temlistMatcher = pattern.matcher(productIdStr);
                String productId = temlistMatcher.replaceAll("");
                temBarCode = temBarCode.substring(0,5)+productId;
                dto.setBarCode(temBarCode);
            }
        }
        //继续追加
        StringBuffer buffer = new StringBuffer();
        try {
            String size = dto.getSize().replace("cm","").replace("CM","").trim();
            String barCode = dto.getBarCode();

            buffer.append(dto.getSex()).append(splitSign);
            buffer.append(dto.getBrand()).append(splitSign);

            buffer.append(dto.getCategory()).append(splitSign);
            buffer.append(barCode).append(splitSign);

            buffer.append(barCode).append(splitSign);
            buffer.append((dto.getSeason()==null||"".equals(dto.getSeason()))?"2018春夏":dto.getSeason()).append(splitSign);

            buffer.append(dto.getMaterial()).append(splitSign);
            buffer.append(dto.getColorName()).append(splitSign);

            buffer.append(size).append(splitSign);
            buffer.append(dto.getProductName()).append(splitSign);

            buffer.append(dto.getItemprice()).append(splitSign);
            buffer.append(dto.getItemprice()).append(splitSign);

            buffer.append(dto.getQty()).append(splitSign);
            buffer.append(dto.getQtyDesc()).append(splitSign);


            buffer.append(dto.getMade()).append(splitSign);

            //buffer.append(dto.getDescript()).append(splitSign);
            //测量信息替代描述
            buffer.append(dto.getSizeDesc()).append(splitSign);

            buffer.append(dto.getPicUrls()).append(splitSign);

            buffer.append(dto.getUrl()).append(splitSign);

            //buffer.append(dto.getSizeDesc()).append(splitSign);
            buffer.append("").append(splitSign);

            buffer.append(supplierId).append(splitSign);
            buffer.append(supplierNo).append(splitSign);
            buffer.append(dto.getSupplierSkuNo()).append(splitSign);
            buffer.append("\r\n");
            System.out.println(buffer.toString());
            out.write(buffer.toString());
            logger.info(buffer.toString());
            out.flush();
        } catch (Exception e) {
        }
    }


    /**
     * 获取商品页数
     * @param total 总记录数
     * @param pageSize 每页显示数
     * @return 页数
     */
    public static Integer getPageNumber(Integer total,Integer pageSize){
        Integer pageNumner = total/pageSize;
        //当余数大于0 的时候 页数加一
        if(total%pageSize>0){
            pageNumner++;
        }
        return pageNumner;
    }


    public static void main(String[] args) {
        String barCode = "M9904V";
        String substring = barCode.substring(0,5);
        System.out.println(substring);
    }


}
