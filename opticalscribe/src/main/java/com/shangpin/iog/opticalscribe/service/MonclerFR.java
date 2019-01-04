package com.shangpin.iog.opticalscribe.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shangpin.iog.opticalscribe.dto.LouisvuittonDTO;
import com.shangpin.iog.opticalscribe.dto.RequestFailProductDTO;
import com.shangpin.iog.opticalscribe.dto.TemplateProductDTO;
import com.shangpin.iog.opticalscribe.tools.CSVUtilsSolveRepeatData;
import com.shangpin.iog.pavinGroup.util.HttpResponse;
import com.shangpin.iog.pavinGroup.util.HttpUtils;
import com.shangpin.iog.service.EventProductService;
import com.shangpin.iog.service.ProductFetchService;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
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
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Company: www.shangpin.com
 * @Author myw
 * @Date Create in 18:31 2018/8/28
 * @Description: LV 法国官网 商品拉取


# lv france
uri-fr=https://fr.louisvuitton.com
#拉取数据存放路径
path-fr=D://GUCCI/gucci-fr.csv
#去重数据存放路径
destFilePath-fr=D://GUCCI/gucci-fr-solved.csv
# false:拉取数据  true:处理拉取完的数据（去重）
flag-fr=false

storeLang-fr=fra-fr

sizeCategoryNames-fr=,Ceintures,

#箱包
fr-genderAndNameAndCategoryUrl1=women&&Nouveautés&&https://fr.louisvuitton.com/fra-fr/femme/sacs-a-main/nouveautes/_/N-1vd2kyh
fr-genderAndNameAndCategoryUrl2=women&&Sacs Monogram&&https://fr.louisvuitton.com/fra-fr/femme/sacs-a-main/sacs-monogram/_/N-9xxkpv
fr-genderAndNameAndCategoryUrl3=women&&Sacs Iconiques&&https://fr.louisvuitton.com/fra-fr/femme/sacs-a-main/icones/_/N-15h16e2
fr-genderAndNameAndCategoryUrl4=women&&Sacs Défilés&&https://fr.louisvuitton.com/fra-fr/femme/sacs-a-main/sacs-defiles/_/N-14yok9x
fr-genderAndNameAndCategoryUrl5=women&&Sacs porté main&&https://fr.louisvuitton.com/fra-fr/femme/sacs-a-main/sacs-porte-main/_/N-1oj8k2e
fr-genderAndNameAndCategoryUrl6=women&&Sacs porté épaule & Cabas&&https://fr.louisvuitton.com/fra-fr/femme/sacs-a-main/sacs-porte-epaule-cabas/_/N-18inxg5
fr-genderAndNameAndCategoryUrl7=women&&Sacs porté croisé&&https://fr.louisvuitton.com/fra-fr/femme/sacs-a-main/sac-porte-croise/_/N-f35uui
fr-genderAndNameAndCategoryUrl8=women&&Pochettes & Soir&&https://fr.louisvuitton.com/fra-fr/femme/sacs-a-main/pochettes/_/N-1tpr79u
fr-genderAndNameAndCategoryUrl9=women&&Sacs en Cuir Exotique&&https://fr.louisvuitton.com/fra-fr/femme/sacs-a-main/sacs-en-cuir-exotique/_/N-5nt2gh


 */
//@Component("monclerFR")
public class MonclerFR {

    //有库存
    private static final String IN_STOCK = "5";
    //无库存
    private static final String NO_STOCK = "0";

    private static String supplierId="2018090402046";
    private static String supplierNo="S0000987";
    private static String channel="store.moncler.com";
    // 商品barCodeMap  key:barCode value:null
    private static HashMap<String, String> barCodeMap= null;

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
        flag = bdl.getString("flag-fr");
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
        try {  //-------------------------------------------myw为什么这一步要用流去写？？？？？？？？？？？？
            out = new OutputStreamWriter(new FileOutputStream(path, true),"gb2312");
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*StringBuffer buffer = new StringBuffer(
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
        ).append("\r\n");*/
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
                //第一步：拉取该品类
                getAllProductsCategroyUrl(sexAnduriAndName[0],sexAnduriAndName[1],sexAnduriAndName[2]); //-----------------------------myw1拉取品类的list列表信息

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
                    //grapProductDetailMaterials(failProduct.getGender(),failProduct.getCategoryName(),failProduct.getUrl(),failProduct);
                }else if("0".equals(flag)){
                    //不校验 productModel 是否存在
                    //grapProductDetailMaterials(failProduct.getGender(),failProduct.getCategoryName(),failProduct.getUrl());
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
                //获取第一页商品数据
                //第二步：拿到品类列表的DOM
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
                        // /ajax/endeca/browse-frag/donna/calzature/tutte-le-collezioni/_/N-16qh15m?storeLang=zhs-cn&pageType=category&Nrpp=198&originalNrpp=18&No=18
                        String getAllCategoryUrl = "";
                        Integer nrpp = (pageNumber-1)*pageSize;
                        if(categoryUrl.contains("?")){
                            getAllCategoryUrl = categoryUrl.replace(storeLang,"ajax/endeca/browse-frag") +"&storeLang="+storeLang+"&pageType=category&Nrpp="+nrpp+"&originalNrpp="+pageSizeStr+"&No="+pageSizeStr;
                        }else{
                            getAllCategoryUrl = categoryUrl.replace(storeLang,"ajax/endeca/browse-frag") +"?storeLang="+storeLang+"&pageType=category&Nrpp="+nrpp+"&originalNrpp="+pageSizeStr+"&No="+pageSizeStr;
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
                System.err.println("fr-请求商品品类 地址出错  "+categoryUrl);
                logger.error("fr-请求商品品类 地址出错categoryUrl {}  "+categoryUrl);
                return;
            }
        }catch (Exception e){
            //添加到 失败 请求中
            failList.add(new RequestFailProductDTO(sex, categoryName, categoryUrl, "2"));
            System.out.println("fr-请求商品品类 地址出错categoryUrl{}"+categoryUrl+e.getMessage());
            loggerError.error("fr-请求商品品类 地址出错categoryUrl{}"+categoryUrl+e.getMessage());
            return;
        }
    }


    /**
     * 拉取 品类 下的 商品列表信息   主要获取到每个商品的url为了跳转到详情页
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
                    System.out.println("fr-请求商品品类 地址出错categoryUrl{}"+categoryUrl);
                    loggerError.error("fr-请求商品品类 地址出错categoryUrl{}"+categoryUrl);
                    return;
                }
            }
            //获取商品列表页数据
            Elements productElemets = doc.select("#container").select("div.wrapper").select("div.description").select("div.itemColors").select("ul.colors-container").select("li");
            String season = doc.select("#search").select("h1.here").select("span.season").first().text();
            //处理当前页的数据
            int productSize = productElemets.size();
            for (int i = 0; i < productSize; i++) {
                Element prductAElement = productElemets.get(i);
                String productDetailUrl = prductAElement.attr("data-ytos-link");
                //第三步：跳转到处理商品详情
                grapProductDetailMaterials(sex,categoryName,productDetailUrl,season);
            }
        }catch (Exception e){

        }
    }

    /**
     * 获取该商品的材质信息
     * @param productDetailUrl 商品详情url
     */
    private void grapProductDetailMaterials(String sex,String categoryName,String productDetailUrl,String season){
        if(StringUtils.isBlank(productDetailUrl)){
            return;
        }
        try {
           /* Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36)");
            Header[] headers = new Header[1];
            headers[0] = header;*/

           // HttpResponse response = HttpUtils.get(productDetailUrl,headers);
            HttpResponse response = HttpUtils.get(productDetailUrl);
            if (response.getStatus()==200) {
                String htmlContent = response.getResponse();
                Document doc = Jsoup.parse(htmlContent);

                //获取材质名称
                String materialName = "";
                Elements materialNameElements = doc.select("#item").select("div.editorialComposition");
                if(materialNameElements!=null&&materialNameElements.size()>0){
                    materialName = materialNameElements.first().text();
                    materialName = materialName.replaceAll(",",".");
                }else{
                    //有几个网页描述标签比较特殊
                    String tep = doc.select("#item").select("div.compositionInfo").select("span.text").text();
                    if(tep!=null&&!"".equals(tep)){
                        materialName = tep.replaceAll(",",".");
                    }
                }

               //获取颜色名称
               // String colorName = doc.select("#container").select("li.is-selected").attr("data-ytos-color-identifier");
                String colorName ="";
                Elements colorNameElements = doc.select("#container").select("div.selectionInfo").select("span.selectedColorInfo");
                if(colorNameElements!=null&&colorNameElements.size()>0){
                    colorName = colorNameElements.first().text();
                }else{
                    return;
                }
               //价格
                String price = "";
                Elements priceElements = doc.select("#container").select("div.hidden").select("span.value");
                if(priceElements!=null&&priceElements.size()>0){
                    price = priceElements.first().text();
                    price = price.replace(",",".");
                }else{
                    return;
                }
                //解决特殊价格去不掉空格的问题，原因：浏览器和编码转换的问题 参考地址：https://www.cnblogs.com/renxiaoren/p/5442431.html
                byte bytes[] = {(byte) 0xC2,(byte) 0xA0};
                String UTFSpace = new String(bytes,"utf-8");
                price = price.replaceAll(UTFSpace, "&nbsp;").replaceAll("&nbsp;","");
                //季节
               // String season = doc.select("#container").select("div.seasonLabel").select("span.text").text();
               //商品名称
                String productName = "";
                Elements productNameElements = doc.select("#container").select("div.itemDescription_content").select("h1").select("span.modelName");
                if(productNameElements!=null&&productNameElements.size()>0){
                    productName = productNameElements.first().text();
                }
                //商品描述 ????
                String  productDes= "";
                Elements productDesElements = doc.select("#container").select("div.itemDescription_infos").select("p").select("span.value");
                if(productDesElements!=null&&productDesElements.size()>0){
                    productDes = productDesElements.first().text();
                    productDes = productDes.replace(",",".");
                }
                //品牌名称
                String brandName = doc.select("#container").select("div.brandName").text();
                //String brandName = "MONCLER";
                //获取productId
                String datas = doc.select("#container").select("div.HTMLListColorSelector").select("ul").select("li.is-selected").attr("data-ytos-color-model");
                JsonElement je = new JsonParser().parse(datas).getAsJsonObject().get("ProductColorPartNumber");
                String productId = je.getAsString();
                //获取availabilityType
                int availabilityType = 0;
                /*String avaiElements = doc.select("#container").select("div.image").first().attr("data-ytos-opt");;
                    if(avaiElements!=null&&avaiElements.contains("availabilityType")){
                        availabilityType = new JsonParser().parse(avaiElements).getAsJsonObject().get("availabilityType").getAsInt();
                    }*/

                //获取langId
                String langId ="";
                Elements scriptElements = doc.select("script");
                for (Element scriptElement: scriptElements) {
                    String scriptTtext = scriptElement.html();
                    if(scriptTtext!=null&&scriptTtext.contains("LangId")){
                        String[] split = scriptTtext.split("};");
                        for (String temStr:split) {
                            if(temStr.contains("LangId")){
                                 String temp = temStr.split("" +
                                         " =")[1]+"}";
                                 langId = new JsonParser().parse(temp).getAsJsonObject().get("LangId").getAsString();
                            }
                        }
                    }
                }

                //获取尺寸信息列表
                Elements sizeListElements = doc.select("#container").select("div.itemColorSize_size").select("div.HTMLSelectSizeSelector").select("select option");

                //.select("class[aria-live=polite]").select("div.slick-track").select("div");
                //尺寸列表
                if(sizeListElements!=null&&sizeListElements.size()>0){
                    int sizeList = sizeListElements.size();
                    for (int i = 1; i <sizeList ; i++) {
                        //第四步：导出具体信息    628行
                        TemplateProductDTO productDto = new TemplateProductDTO();
                        Element sizeElement = sizeListElements.get(i);
                         //获取具体的尺码信息  第一个为请选择尺码故不要
                        String sizeName = sizeElement.text();
                        if(sizeName.toLowerCase().contains("Sold out".toLowerCase()) || sizeName.toLowerCase().contains("One left".toLowerCase())){
                            productDto.setQty(NO_STOCK);//0无 1
                        }else{
                            productDto.setQty(IN_STOCK);
                        }
                        if(sizeName.contains("-")){
                            sizeName = sizeName.split("-")[0];
                        }


                        //填充数据
                        productDto.setSex(sex);
                        productDto.setBrand(brandName);
                        productDto.setCategory(categoryName);
                        /*if(sex.toLowerCase().equals("boy") || sex.toLowerCase().equals("girl")){
                            productDto.setSpuNo(productId+"_");
                            productDto.setProductModelCode(productId+"_");
                        }else {
                            productDto.setSpuNo(productId);
                            productDto.setProductModelCode(productId);
                        }*/
                        productDto.setSpuNo(productId+ "\t");
                        productDto.setProductModelCode(productId+ "\t");
                        productDto.setSeason(season);
                        productDto.setMaterial(materialName);
                        productDto.setColorName(colorName);
                        productDto.setSize(sizeName);
                        productDto.setProductName(productName);
                        productDto.setItemsaleprice(price);
                        productDto.setItemprice(price);

                        productDto.setMade("France");
                        productDto.setDescript(productDes);
                        String urls = getPicUrlsByDatas(productId,availabilityType,langId);
                        productDto.setPicUrls(urls);
                        productDto.setUrl(productDetailUrl);
                        productDto.setSizeDesc("");
                        productDto.setSupplierId(supplierId+"\t");
                        productDto.setSupplierNo(supplierNo);
                        productDto.setSupplierSkuNo("");//目前都是先为空


                        //导出数据
                        //3.遍历该商品尺码的skuNo 获取具体尺码下的价格 库存 图片信息
                        exportExcel(productDto);

                    }
                }else{// 没有商品材质信息 将doc
                    return ;
                }

            }else{
                failList.add(new RequestFailProductDTO(sex,categoryName,productDetailUrl,"0"));
                System.err.println("fr-savePre请求商品url 失败"+sex +":"+categoryName+":"+productDetailUrl);
                logger.error("fr-savePre请求商品url 失败"+sex +":"+categoryName+":"+productDetailUrl);
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
                    System.out.println("fr-地址出错 productMaterialDetailUrl"+productMaterialDetailUrl);
                    loggerError.error("fr-地址出错 productMaterialDetailUrl"+productMaterialDetailUrl);
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
            Thread.sleep(9000);
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
                    //exportExcel(product);
                }

            }else{ //没有颜色skuNo 直接 从 doc中获取该商品的尺码 skuNo

                //1.获取商品基础信息
                LouisvuittonDTO baseProductInfo = grapProductDetail(sex, categoryName, materialName, colorName, "", productColorDetailUrl, doc);

                //获取商品图片、价格、 列表页数据
                //https://www.louisvuitton.cn/ajax/product.jsp?storeLang=zhs-cn&pageType=product&id=1A4BO8
                // &requestingURL=https://www.louisvuitton.cn/zhs-cn/prodotti/perfecto-con-interno-monogram-nvprod920154v
                Elements productSizeElemets = doc.select("#size").select("li[data-skuSubType=product]");
                /**
                 <li data-sku="1A3YXO" data-ona="Size/35.5" data-skuSubType="product"
                 value="https://www.louisvuitton.cn/zhs-cn/prodotti/stivaletto-fireball-nvprod650059v"> 35.5
                 <a href="https://www.louisvuitton.cn/zhs-cn/prodotti/stivaletto-fireball-nvprod650059v">
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
                   // exportExcel(baseProductInfo);
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

        //商品
        String barCodePreNum = "";

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
        String sizeDesc = "";
        StringBuffer sizeDescBuffer=new StringBuffer();
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
                    System.out.println("fr-地址出错 productMaterialDetailUrl"+productMaterialDetailUrl);
                    loggerError.error("fr-地址出错 productMaterialDetailUrl"+productMaterialDetailUrl);

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
                itemPrice = itemPrice.trim().substring(0,itemPrice.length()-3);
                Pattern pattern = Pattern.compile("[^0-9]");
                Matcher listMatcher = pattern.matcher(itemPrice);
                itemPrice = listMatcher.replaceAll("");
                System.out.println(itemPrice);
            }

            //处理商品图片信息
            Elements imgsElements = doc.select("#productSheetSlideshow").select("ul.bigs").select("li");
            int imageSize = imgsElements.size();
            for (int i = 0; i < imageSize ; i++) {
                /**
                 <img
                 data-src="https://www.louisvuitton.cn/images/is/image/lv/1/PP_VP_L/louis-vuitton-stivaletto-fireball-calzature--AE8Q3IPC02_PM2_Front view.jpg?wid={IMG_WIDTH}&hei={IMG_HEIGHT}"
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
            //材质信息
            StringBuffer temMaterialInfo = new StringBuffer();
            //商品其他信息 如 颜色、产地、省份、尺码大小
            Elements otherInfoElements  = doc.select("div#textClientInfo_productDescription").select("ul").select("li");
            if(otherInfoElements == null||otherInfoElements.size()==0){
                String[] otherArr = doc.select("div#textClientInfo_productDescription").select("div.functional-text").text().split("- ");
                int length = otherArr.length;
                for (int i = 0; i < length; i++) {
                    String text = otherArr[i];
                    if(text.contains("长x 高 x 宽")){
                        continue;
                    }
                    String replace = text.replace(",", ".");
                    //尺寸
                    if(replace.contains("cm")|| replace.contains("mm")){  //商品尺寸信息 可能含有 模特信息
                        if(sizeDesc==null || "".equals(sizeDesc)){
                            sizeDescBuffer.append(replace.replace("&nbsp;"," "));
                            System.out.println(replace);
                        }
                    }else if(replace.contains("fabrication")){  //产地
                        made = replace;
                    }else if(replace.contains("blanc")||replace.contains("noir")||replace.contains("orangé")||replace.contains("bleu")
                            ||replace.contains("brun")||replace.contains("gris")||replace.contains("blond")||replace.contains("vert")
                            ||replace.contains("violet")||replace.contains("marron")||replace.contains("jaune")||replace.contains("rose")
                            ||replace.contains("rouge")||replace.contains("écru")||replace.contains("beige")){  //颜色
                        if(colorName==null||"".equals(colorName)){
                            colorName = replace;
                        }
                    }else if(replace.contains("métal")||replace.contains("cuir")||replace.contains("toile")){  //为材质信息 多数 第一个为材质信息
                        //判断材质信息是否为空 或者 空串
                        temMaterialInfo.append(replace.replace(",","."));
                    }
                    //模特信息(暂时无用)
                    if(replace.contains("model")){
                        modelMeasurements = modelMeasurements+" "+ replace;
                    }
                }
            }else{
                int otherInfoSize = otherInfoElements.size();
                for (int i = 0; i < otherInfoSize ; i++) {
                    Element otherInfoElement = otherInfoElements.get(i);
                    String text = otherInfoElement.text();
                    String replace = text.replace(",", ".").replace("&nbsp;"," ");
                    //尺寸
                    if(replace.contains("cm")|| replace.contains("mm")){  //商品尺寸信息 可能含有 模特信息
                        if(sizeDesc==null || "".equals(sizeDesc)){
                            sizeDescBuffer.append(replace.replace("&nbsp;"," "));
                            System.out.println(replace);
                        }
                    }else if(replace.contains("fabrication")){  //产地
                        made = replace;
                    }else if(replace.contains("Blanc")||replace.contains("Noir")||replace.contains("Orangé")||replace.contains("Bleu")
                            ||replace.contains("Brun")||replace.contains("Gris")||replace.contains("Blond")||replace.contains("Vert")
                            ||replace.contains("Violet")||replace.contains("Marron")||replace.contains("Jaune")||replace.contains("Rose")
                            ||replace.contains("Rouge")||replace.contains("Écru")||replace.contains("Beige")){  //颜色
                        if(colorName==null||"".equals(colorName)){
                            colorName = replace;
                        }
                    }else if(replace.contains("métal")||replace.contains("Métal")||replace.contains("cuir")||replace.contains("Cuir")||replace.contains("toile")||replace.contains("Toile")){  //为材质信息 多数 第一个为材质信息
                        //判断材质信息是否为空 或者 空串
                        temMaterialInfo.append(replace.replace(",","."));
                    }
                    //模特信息(暂时无用)
                    if(replace.contains("model")){
                        modelMeasurements = modelMeasurements+" "+ replace;
                    }
                }
            }


            sizeDesc = sizeDescBuffer.toString().replace(",",".");

            colorName = colorName.replace(",",".");
            materialName = temMaterialInfo.toString().replace(",",".");

            /**
             *  获取商品 库存信息
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
            product.setSizeDesc(sizeDesc);
            product.setItemprice(itemPrice);

            product.setSpuNo(spuNo);
            product.setCategory(categoryName);
            product.setPicUrls(picSrcs.toString().replace("\"",""));
            product.setSize(sizeName.replace(",","."));
            product.setQty(qty);
            product.setQtyDesc(qtyDesc);

            //判断当前包页面有没有尺码信息  有分成多个 product 没有 尺码为均码
            //   .spice-dropdown-pdp-size-box
            //exportExcel(product);
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

           // exportExcel(product);
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
         *  https://secure.louisvuitton.com/ajaxsecure/getStockLevel.jsp?storeLang=zhs-cn&pageType=product&skuIdList=1A47Z3,1A47Z4,1A47Z5,1A47Z6,1A47Z7,1A47Z8,1A47Z9,1A47ZA&null&_=1532426961111
         */
        String qtyUrl = "https://secure.louisvuitton.com/ajaxsecure/getStockLevel.jsp?storeLang="+storeLang+"&pageType=product&skuIdList="+skuNo;
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
                resultMap.put("qtyDesc","售罄");
            }
        }else{
            System.out.println("请求库存数据接口失败");
            resultMap.put("qty",NO_STOCK);
            resultMap.put("qtyDesc","售罄");
        }
        return resultMap;
    }


    /**
     * 获取某一尺码 skuNO 的 价格 图片
     * @param skuNo
     * @throws Exception
     */
    public Map<String,String> getProductSkuPrivateInfo(String skuNo){
        Map<String,String> resultMap = new HashMap<>();
        Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36)");
        Header[] headers = new Header[1];
        headers[0] = header;
        /**
         https://www.louisvuitton.cn/ajax/product.jsp?storeLang=zhs-cn&pageType=product&id=1A43EA
         */
        String qtyUrl = "https://www.louisvuitton.cn/ajax/product.jsp?storeLang="+storeLang+"&pageType=product&id="+skuNo;
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
                itemPrice = itemPrice.trim().substring(0,itemPrice.length()-3);
                Pattern pattern = Pattern.compile("[^0-9]");
                Matcher listMatcher = pattern.matcher(itemPrice);
                itemPrice = listMatcher.replaceAll("");
                System.out.println(itemPrice);

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
                 data-src="https://www.louisvuitton.cn/images/is/image/lv/1/PP_VP_L/louis-vuitton-stivaletto-fireball-calzature--AE8Q3IPC02_PM2_Front view.jpg?wid={IMG_WIDTH}&hei={IMG_HEIGHT}"
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
                resultMap.put("qty",NO_STOCK);
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
         https://www.louisvuitton.cn/ajax/product.jsp?storeLang=zhs-cn&pageType=product&id=1A43EA
         */
        String qtyUrl = "https://www.louisvuitton.cn/ajax/product.jsp?storeLang="+storeLang+"&pageType=product&id="+skuNo;
        HttpResponse response = HttpUtils.get(qtyUrl,headers);
        if (response.getStatus()==200) {
            String htmlContent = response.getResponse();
            Document doc = Jsoup.parse(htmlContent);
            Elements productSizeElemets = doc.select("#size").select("li[data-skuSubType=product]");
            /**
             <li data-sku="1A3YXO" data-ona="Size/35.5" data-skuSubType="product"
             value="https://www.louisvuitton.cn/zhs-cn/prodotti/stivaletto-fireball-nvprod650059v"> 35.5
             <a href="https://www.louisvuitton.cn/zhs-cn/prodotti/stivaletto-fireball-nvprod650059v">
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
    private static void exportExcel(TemplateProductDTO dto) {
        if(dto==null||dto.getProductName()==null||"".equals(dto.getProductName())){
            return;
        }
        //继续追加
        StringBuffer buffer = new StringBuffer();
        try {
            buffer.append(dto.getSex()).append(splitSign);
            buffer.append((dto.getBrand()==null||"".equals(dto.getBrand()))?"":dto.getBrand()).append(splitSign);

            buffer.append(dto.getCategory()).append(splitSign);
            buffer.append(dto.getSpuNo()).append(splitSign);

            buffer.append(dto.getProductModelCode()).append(splitSign);
            buffer.append((dto.getSeason()==null||"".equals(dto.getSeason()))?"四季":dto.getSeason()).append(splitSign);

            buffer.append(dto.getMaterial()).append(splitSign);
            buffer.append((dto.getColorName()==null||"".equals(dto.getColorName()))?"":dto.getColorName()).append(splitSign);

            buffer.append((dto.getSize()==null||"".equals(dto.getSize()))?"":dto.getSize()).append(splitSign);
            buffer.append((dto.getProductName()==null||"".equals(dto.getProductName()))?"":dto.getProductName()).append(splitSign);

            buffer.append(dto.getItemsaleprice()).append(splitSign);
            buffer.append(dto.getItemprice()).append(splitSign);
            buffer.append(dto.getItemsaleprice()).append(splitSign);

            buffer.append(dto.getQty()).append(splitSign);

            buffer.append(dto.getMade()).append(splitSign);

            buffer.append(dto.getDescript()).append(splitSign);
            buffer.append(dto.getPicUrls()).append(splitSign);

            buffer.append(dto.getUrl()).append(splitSign);
            //测量信息替代描述
            buffer.append(dto.getSizeDesc()).append(splitSign); //尺寸描述

            buffer.append(supplierId).append(splitSign);
            buffer.append(supplierNo).append(splitSign);
            buffer.append(dto.getSupplierSkuNo()).append(splitSign);
            buffer.append(channel).append(splitSign);
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

    /**
     * 获取详情中的所有图片
     * @param
     * @throws Exception
     */
    public String getPicUrlsByDatas(String productId,int availabilityType,String langId) throws Exception{
        List<String> sizeSkuNos = new ArrayList<String>();
        Header header = new Header("cookie", "ytos-session-MONCLER=10a9a1cbc162436f8c7e4e758b3f9dd03fVR0oC4V76TvDCtfukC4A; UI-PERSISTENT=abtest=&abtestperc=&abtesth=4SObxciWWbbS2MCyf8Sq6Q&country=fr; rxVisitor=15354471443734SJCCLQIEGCMN8J9970JU5E6NT2TIUEO; optimizelyEndUserId=oeu1535447145314r0.4251853875909217; _ga=GA1.2.1360979099.1535447147; _gid=GA1.2.1122550430.1535447147; cto_lwid=117b07c4-e406-42eb-a343-ff27f2ae719f; _ym_d=1535447151; _ym_uid=1535447151693674599; yOrbRD=guid%3D3fa123ba-aec8-4a65-9253-7d3b58508812; yTos.CookieLaw={\"ShowLayer\":false}; _ceg.s=pe627c; _ceg.u=pe627c; RESOURCEINFO=DEVICE=desktop&ORIGINALDEVICE=desktop; UI=abtest=&abtestperc=&abtesth=4SObxciWWbbS2MCyf8Sq6Q&cacheversion=f31-v751-moncler-w&device=desktop&version=2018-08-28-04351fc&lang=; JSESSIONID=0000ZjLxdNjetC1wrVD6USrxQhH:-1; AWSELB=9D77CF831C27CD9CAB801D7DEDFF840A49150DD2363849853E93BD16F9EFBA81D7E5CF8E7B428964B3243CBFD6D805D3BE1F64461422EDD535A937EC316DD4EF617704493FA8718E8ADC2AD22C358CD75BAAE44476; TS013509ec=0122c051bc9af767bdad01dbe00e458350cf2f428f61b3db4dad4cd115115ed4baa071b441161dd434be25d93603c1d48c697f136c2e98f48f3052f92f3a252eb6be0ea6b4; YEDGESESSION=b15732b896590000c19a885bc3000000787e0900; VISIT=NAZIONE=FRANCE&NAZIONE_ISO=FR&LANGID=5&SITE_CODE=MONCLER_FR; TS014254ab=0122c051bc67db4cb948c0494adf2f3eaec950c5c361b3db4dad4cd115115ed4baa071b441e42b8af4a6329dbc8a67d7aed3941553d6ea8c5c519d1439369d71ec0fe9ad714aa832e74971d246a93587c20dd7a3c700ca800869aa6f11fcfb2d0ca9d9a75d2b7735280486d99b32ec698bad7b650c60c8ecace4bdc4a6eb234599d5136c3a46e48bd8ad41d1c6e974c7de7f1020913612517edb23cf4c42d005d840d81dd0; font-loaded=1; _ym_isad=2; bm_mi=C98533D72EA07D15A43808735E5F7E5C~AH6t5DLddN1Vm+TqNadJcqkTNpBZZtZ3dwOz6DOjuEP6DH08UdibQoohGiOaudvJmS6olva80pWHjl3pMkEBhaegC/x8M3i5cOOTYFs4JR5VjFZHtS19o+OFdGXYtM0Y9b1IwcA2B8Fj0z9r7gWdE1iOO6vAxF7IKUdGgO1mN8Y4qj2Omt9MxBGstHMXzgpynhEBTPTLn+xbL7bQoftI3Fu5WOwElNKqBbdam3XUxo40qTPjXm+866nKclQlX55mWxnDrtUG4qIZnW/rNrReMPJwsr9ZOXvzMeRUt2XceylkV6zZg3Nzj3Em7XjI2XBbfJCHQ7rB0nPD8+PY2Bvgh+WA62mCO9S6RH5pcyiM8QQ=; ak_bmsc=F0CD4C331825515B7D105F84BB5DFC5FB83257B1965900004FD5885B87B01824~plmqcB4cTaC+wMHzAboqLgNhX4souiQQwHZ/TSyD5CmzpTbrPyTZyhCDhSgLBNoVJZ7NCKacis2ls62sKnehOfeSJMgPFPabGkqEz/70A1p5deTuLolOvI2wJKkfUhmFqLLRaJZVAUQPXJ7tx64PI/PpSCf249HbdmMGr2mWsbQm2YENBfHbUjWROIab4RhcJxxe/vXs82qDNudUIbrs1Sn1Ka9wClTyEnxcsUz62lq2pzmCthfFGVyiuLHtQvIvUP; _ym_visorc_46539561=w; AKA_A2=A; dtSa=-; _ceg.s=pebdr8; _ceg.u=pebdr8; dtLatC=29; dtPC=3$300062481_400h-vKDKIOQANMKKCLBBCEMONVIEOOCMMACIJ; rxvt=1535701932691|1535699812781; dtCookie=3$C69E8A95E8E5D0411B74EA6A95E937F0; TS0143c829=0122c051bcccd1465b1fcab8025b783dcf845bce0d46d812d11f102a7e9c8c192535ca13f92f3121a8fce4bcd9bf5bf8d5a6640f7f; bm_sv=84DC6DCD044718069CEDF367CF8763BF~bKohceb/ADh4eSBYN70YUcL1DeEkUcKUaUEsMd2vsGnostk71LfXBB90fNkCJ5Su8U1mgCkmWw5cUvn15cEFDwXzuAmXVdcSWxZGNTzIHO4sJfxRL5Ziy8bX6Sf+Iy3WD1+UR09aFadCENYVBlsaPDH7iDHKEiqx72ftv1L90C0=; Y-Country-Language=en-wy");
        Header[] headers = new Header[1];
        headers[0] = header;

        int langIdM = Integer.parseInt(langId);
        Long productIdM = Long.parseLong(productId);
        //int siteCodeM = Integer.parseInt(siteCode);
        /**
         https://store.moncler.com/yTos/api/Plugins/ItemPluginApi/ProductAvailability/?partNumber=3024088872736056&siteCode=MONCLER_FR&langId=5
         */
        String qtyUrl = "https://store.moncler.com/yTos/api/Plugins/CommonPluginApi/GetProductImagesAsync/?partNumber="+productIdM+"&availabilityType="+availabilityType+"&langId="+langIdM;
        qtyUrl = qtyUrl.replace("\"","");
        HttpResponse response = HttpUtils.get(qtyUrl,headers);
        if (response.getStatus()==200) {
            String htmlContent = response.getResponse();
            Document doc = Jsoup.parse(htmlContent);
             String aa  = doc.select("body").first().text();
            StringBuffer urlsBuffer = new StringBuffer();
            JsonElement je = new JsonParser().parse(aa);
            JsonArray skuArry = je.getAsJsonArray();
            int size = skuArry.size();
            for (int i = 0; i < size ;i++) {
                JsonObject asJsonObject = skuArry.get(i).getAsJsonObject();
                int width = asJsonObject.get("width").getAsInt();
                int height = asJsonObject.get("height").getAsInt();
                String job = asJsonObject.get("partNumbers").getAsJsonArray().get(0).getAsString();
               // int height = Integer.parseInt(job.get("height").toString());
               // Object oo = job.get("partNumbers").
               // JSONArray partArr = JSONArray.fromObject(job.get("partNumbers"));

                if(job.equals(productId) && (720 <= width) && (width <= 1200)){
                    urlsBuffer.append(asJsonObject.get("url").toString()).append("|");
                }
            }
            String tempa = urlsBuffer.toString();
            String cc =tempa.substring(0,tempa.length()-1);
            cc = cc.replaceAll("\"","");
            return  cc;
        }
        return "";
    }


   /* public static void main(String[] args) {
        try {
            HttpResponse response = HttpUtils.get("https://store.moncler.com/en-gb/outerwear_cod4230358016510707.html");
            if (response.getStatus() == 200) {
                String htmlContent = response.getResponse();
                Document doc = Jsoup.parse(htmlContent);
                String datas = doc.select("#container").select("div.HTMLListColorSelector").select("ul").select("li.is-selected").attr("data-ytos-color-model");
                JsonElement je = new JsonParser().parse(datas).getAsJsonObject().get("ProductColorPartNumber");
                String productId = je.getAsString();
                System.out.println(productId);
            }
            // System.out.println(substring);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }*/

}
