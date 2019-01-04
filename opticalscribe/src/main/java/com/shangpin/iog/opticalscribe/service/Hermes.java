package com.shangpin.iog.opticalscribe.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.opticalscribe.dto.RequestFailProductDTO;
import com.shangpin.iog.opticalscribe.dto.TemplateProductDTO;
import com.shangpin.iog.opticalscribe.tools.CSVUtilsSolveRepeatData;
import com.shangpin.iog.pavinGroup.util.HttpResponse;
import com.shangpin.iog.pavinGroup.util.HttpUtils;
import com.shangpin.iog.service.EventProductService;
import com.shangpin.iog.service.ProductFetchService;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.Header;
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
 * @Author wanner
 * @Date Create in 17:46 2018/7/27
 * @Description:

 conf配置如下：
# hermes 意大利

uri-it=https://www.hermes.com
path-it=D://hermes/hermes.csv
destFilePath-it=D://hermes/hermes-solved.csv
flag-it=false
sleepsecond=5
sizeCategoryNames-it=,Sandali,Sneakers,Calzature basse,Mocassini &Car Shoe,Stivaletti e Stivali,Décolleté,Calzature stringate & Calzature con fibbia,Tessili&altro,

#women
hermes-it-genderAndNameAndCategoryUri1=women&&Scarves and silk accessories&&/it/en/women/scarves-and-silk-accessories
hermes-it-genderAndNameAndCategoryUri2=women&&Belts&&/it/en/women/belts
hermes-it-genderAndNameAndCategoryUri3=women&&Bags and clutches&&/it/en/women/bags-and-small-leather-goods/bags-and-clutches/
hermes-it-genderAndNameAndCategoryUri4=women&&Small leather goods&&/it/en/women/bags-and-small-leather-goods/small-leather-goods/
hermes-it-genderAndNameAndCategoryUri5=women&&Luggage&&/it/en/women/bags-and-small-leather-goods/luggage/
hermes-it-genderAndNameAndCategoryUri6=women&&Leather accessories&&/it/en/women/bags-and-small-leather-goods/leather-accessories/
hermes-it-genderAndNameAndCategoryUri7=women&&Accessories&&/it/en/women/accessories/
hermes-it-genderAndNameAndCategoryUri8=women&&女士皮革手镯&&https://www.hermes.com/it/en/women/jewelry-and-fashion-jewelry/leather-jewelry/#||Category
hermes-it-genderAndNameAndCategoryUri9=women&&女士手镯饰品&&https://www.hermes.com/it/en/women/jewelry-and-fashion-jewelry/enamel-jewelry/#||Category


#men
hermes-it-genderAndNameAndCategoryUri11=men&&Ties&&/it/en/men/ties/
hermes-it-genderAndNameAndCategoryUri12=men&&Belts&&/it/en/men/belts/
hermes-it-genderAndNameAndCategoryUri13=men&&Bags&&/it/en/men/bags-and-small-leather-goods/bags/
hermes-it-genderAndNameAndCategoryUri14=men&&Small leather goods&&/it/en/men/bags-and-small-leather-goods/small-leather-goods/
hermes-it-genderAndNameAndCategoryUri15=men&&Luggage&&/it/en/men/bags-and-small-leather-goods/luggage/
hermes-it-genderAndNameAndCategoryUri16=men&&Leather accessories&&/it/en/men/bags-and-small-leather-goods/leather-accessories/
hermes-it-genderAndNameAndCategoryUri17=men&&Accessories&&/it/en/men/accessories/

 */

//@Component("hermes")
public class Hermes {

    //有库存
    private static final String IN_STOCK = "1";
    //无库存
    private static final String NO_STOCK = "0";

    private static String supplierId="2018070502014";
    private static String supplierNo="S0000963";

    private Integer limit = 36;

    // 商品barCodeMap  key:barCode value:null
    private static HashMap<String, String> barCodeMap= null;

    //商品失败请求信息
    private static List<RequestFailProductDTO> failList = null;

    private static OutTimeConfig timeConfig = new OutTimeConfig(1000*60,1000*60,1000*60);

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

    /**
     * 拉取商品 睡眠时间
     */
    private static Integer sleepSecond = 10;

    static {
        if (null == bdl)
            bdl = ResourceBundle.getBundle("conf");
        uri = bdl.getString("uri-it");
        path = bdl.getString("path-it");
        destFilePath = bdl.getString("destFilePath-it");
        flag = bdl.getString("flag-it");

        sleepSecond = Integer.parseInt(bdl.getString("sleepsecond"));

        Enumeration<String> keys = bdl.getKeys();
        while(keys.hasMoreElements()){
            String key = keys.nextElement();
            if(key.startsWith("hermes-it-genderAndNameAndCategoryUri")){
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
            barCodeMap = new HashMap<>();
            failList = new ArrayList<>();

            int productModeNumber = 0;

            //校验配置信息 是否符合格式要求
            int size = genderAndNameAndCategoryUris.size();
            for (int i = 0; i < size; i++) {

                String[] sexAnduriAndName = genderAndNameAndCategoryUris.get(i).split("&&");
                if(sexAnduriAndName.length!=3) {
                    logger.info(" hermes-it--genderAndCategoryUri 不符合格式 ：" + genderAndNameAndCategoryUris.get(i));
                    System.out.println(" hermes-it-genderAndCategoryUri 不符合格式 ：" + genderAndNameAndCategoryUris.get(i));
                    throw new RuntimeException("配置文件 genderAndCategoryUri 不符合格式："+genderAndNameAndCategoryUris.get(i));
                }
            }

            for (int i = 0; i < size; i++) {
                if(i>0){
                    String[] temsexAnduriAndName = genderAndNameAndCategoryUris.get(i-1).split("&&");
                    System.out.println("hermes-it-拉取品类结束 gender:"+temsexAnduriAndName[0]+" categoryName:"+temsexAnduriAndName[1]+" uri:"+temsexAnduriAndName[2]);
                    System.out.println("共拉取 ："+(barCodeMap.size()-productModeNumber)+"款商品数据");
                    System.out.println("=====================================================================");

                    logger.info("hermes-it-拉取品类结束 gender:"+temsexAnduriAndName[0]+" categoryName:"+temsexAnduriAndName[1]+" uri:"+temsexAnduriAndName[2]);
                    logger.info("共拉取 ："+(barCodeMap.size()-productModeNumber)+"款商品数据");
                    logger.info("=====================================================================");
                    productModeNumber = barCodeMap.size();
                }

                String[] sexAnduriAndName = genderAndNameAndCategoryUris.get(i).split("&&");
                //拉取 品类信息
                String temCategoryUrl = uri + sexAnduriAndName[2];
                System.out.println("hermes-it--开始拉取："+sexAnduriAndName[0]+":"+sexAnduriAndName[1]+":"+temCategoryUrl);
                logger.info("hermes-it--开始拉取："+sexAnduriAndName[0]+":"+sexAnduriAndName[1]+":"+temCategoryUrl);
                grapProductListByCategoryUrl(sexAnduriAndName[0],sexAnduriAndName[1],temCategoryUrl);
            }
            String[] temsexAnduriAndName = genderAndNameAndCategoryUris.get(size-1).split("&&");
            System.out.println();
            System.out.println("hermes-it-拉取品类结束 gender:"+temsexAnduriAndName[0]+" categoryName:"+temsexAnduriAndName[1]+" uri:"+temsexAnduriAndName[2]);
            System.out.println("共拉取 ："+(barCodeMap.size()-productModeNumber)+"款商品数据");
            System.out.println("所有品类拉取结束共:"+barCodeMap.size()+"款商品数据");
            System.out.println("=====================================================================");

            logger.info("hermes-it-拉取品类结束 gender:"+temsexAnduriAndName[0]+" categoryName:"+temsexAnduriAndName[1]+" uri:"+temsexAnduriAndName[2]);
            logger.info("共拉取 ："+(barCodeMap.size()-productModeNumber)+"款商品数据");
            logger.info("所有品类拉取结束共:"+barCodeMap.size()+"款商品数据");
            logger.info("=====================================================================");

            System.out.println("======hermes-it-重新请求失败的商品及品类信息===================================================");
            logger.error("=======hermes-it-重新请求失败的商品及品类信息===================================================");
            int failSize = failList.size();
            for (int i = 0; i <failSize ; i++) {
                RequestFailProductDTO failProduct = failList.get(i);
                String flag = failProduct.getFlag();
                if("0".equals(flag)){
                    grapProductListByCategoryUrl(failProduct.getGender(),failProduct.getCategoryName(),failProduct.getUrl());
                }else if("1".equals(flag)){
                    //不校验 productModel 是否存在
                    grapProductDetail(failProduct.getGender(),failProduct.getCategoryName(),failProduct.getUrl());
                }
            }
            System.out.println("======hermes-it-请求失败的商品及品类信息 结束===================================================");
            logger.info("=======hermes-it-请求失败的商品及品类信息 结束===================================================");
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
    public void grapProductListByCategoryUrl(String sex,String categoryName,String categoryUrl) {

        //请求分页的参数时 ni
        if(categoryUrl==null||"".equals(categoryUrl)){
            return;
        }
        try {
            HttpResponse response = HttpUtils.get(categoryUrl);
            if (response.getStatus() == 200) {
                String htmlContent = response.getResponse();
                Document doc = Jsoup.parse(htmlContent);
                //获取商品列表页数据
                //Elements productElemets = doc.select("ul[data-track-list=product grid]").select("li");
                //处理当前页的数据
//                int productSize = productElemets.size();
//                for (int i = 0; i < productSize; i++) {
//                    Element prductAElement = productElemets.get(i).select("article").select("a").first();
//                    String productDetailUrl = uri + prductAElement.attr("href");
//                    //处理商品 分页 信息
//                    grapProductDetail(sex,categoryName,productDetailUrl);
//                }
                /**
                 *  拉取分页数据
                 */
                //获取商品总数量
                List<String> parents = new ArrayList<>();
                String locale = "";
                String url_locale = "";
                String categroyName = "";
                Elements scripts = doc.select("script");
                for(Element script : scripts) {
                    String str = script.html();
                    if (str.contains("jQuery.extend(Drupal.settings,")) { //注意这里一定是html(), 而不是text()
                        String jsonStr = str.replace("jQuery.extend(Drupal.settings,", "").replace(");", "").trim();
                        JsonElement je = new JsonParser().parse(jsonStr);
                        JsonObject asJsonObject = je.getAsJsonObject();
                        locale = asJsonObject.get("hermes_locale").toString().replace("\"","");
                        url_locale = asJsonObject.get("hermes_url_locale").toString().replace("\"","");
                        JsonObject hermes_category = asJsonObject.get("hermes_category").getAsJsonObject();
                        categroyName =hermes_category.get("data").toString().replace("\"","");
                        JsonArray parentsFilter = hermes_category.get("parents").getAsJsonArray();
                        for (int i = 0; i < parentsFilter.size() ; i++) {
                            parents.add(parentsFilter.get(i).toString().replace("\"",""));
                        }
                        break;
                    }

                }
                boolean pageEndFlag = true;
                //定义分页数据起始页 第一页数据已经处理 从第二页开始请求数据
                int pageNum = 1 ;
                String sort = "relevance";
                while (pageEndFlag){
                    logger.info("开始拉取"+sex+":"+categoryName+"第"+pageNum+"页数据:"+categoryUrl);
                    System.out.println("开始拉取"+sex+":"+categoryName+"第"+pageNum+"页数据:"+categoryUrl);
                    Integer offset = (pageNum-1)*limit;
                    List<String> productDetailsUris = getOtherPageProductDetailsUri(locale,parents,url_locale,sort,categroyName,limit,offset);
                    Integer productNum = productDetailsUris.size();
                    for (int i = 0; i < productNum ; i++) {
                        String productDetailUri = productDetailsUris.get(i);
                        String productDetailUrl = ""+productDetailUri;
                        grapProductDetail(sex,categoryName,productDetailUrl.replace("\"",""));

                        /**
                         * 获取每一个尺码都休息 sleepSecond s
                         */
                        Thread.sleep(sleepSecond*1000);
                    }
                    if(productNum<limit){
                        System.out.println("拉取"+sex+":"+categoryName+"结束共: "+((pageNum-1)*limit+productNum)+" 款商品"+categoryUrl);
                        break;
                    }else{
                        pageNum++;
                    }
                }
            }else{
                //添加到 失败 请求中
                failList.add(new RequestFailProductDTO(sex, categoryName, categoryUrl, "0"));
                System.err.println("it-请求商品品类 地址出错  "+categoryUrl);
                logger.error("it-请求商品品类 地址出错categoryUrl {}  "+categoryUrl);
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
            //添加到 失败 请求中
            failList.add(new RequestFailProductDTO(sex, categoryName, categoryUrl, "0"));
            System.out.println("it-请求商品品类 地址出错categoryUrl{}"+categoryUrl+e.getMessage());
            loggerError.error("it-请求商品品类 地址出错categoryUrl{}"+categoryUrl+e.getMessage());
            return;
        }
    }

    /**
     * 获取商品分页数据
     * @param locale
     * @param parents
     * @param url_locale
     * @param sort
     * @param categroyName 类别名称
     * @param limit  查询条数
     * @param offset  数据库查询起始行 limit*(pageNum -1)
     * @return
     */
    private List<String> getOtherPageProductDetailsUri(String locale, List<String> parents, String url_locale, String sort, String categroyName, Integer limit, Integer offset) {
        //https://www.hermes.com/apps/cde/personalize/grid/WOMEN0ACCESSORIES
        List<String> productDetailUrls = new ArrayList<>();
        //请求头
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36)");
        headerMap.put("Cookie", "_ga=GA1.2.1410775761.1531446626; cookiebanner=1; _gid=GA1.2.1246508835.1532678167; has_js=1; locale-country-data={\"country\":\"it\",\"locale\":\"en-he\",\"language\":\"en\"}; ECOM_SESS=3259abe9fab28706f7a9414a72c268c8; _gat_UA-72839523-2=1; _gat_UA-64545050-1=1");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("locale",locale);
        jsonObject.put("parents",parents);
        jsonObject.put("url_locale",url_locale);
        jsonObject.put("sort",sort);
        jsonObject.put("limit",limit);
        jsonObject.put("offset",offset);
        String jsonStr = jsonObject.toString();

        String pageProductUrl = "https://www.hermes.com/apps/cde/personalize/grid/"+categroyName;

        try {

            String resultJsonStr = HttpUtil45.operateData("post", "json", pageProductUrl, timeConfig, null, jsonStr,headerMap, null, null);

            if(resultJsonStr!=null&&!"".equals(resultJsonStr)){
                JsonElement je = new JsonParser().parse(resultJsonStr);
                JsonElement products = je.getAsJsonObject().get("products");
                if(products!=null){
                    JsonElement items = products.getAsJsonObject().get("items");
                    if(items!=null){
                        JsonArray productArray = items.getAsJsonArray();
                        int productSize = productArray.size();
                        for (int i = 0; i <productSize ; i++) {
                            JsonObject productObject = productArray.get(i).getAsJsonObject();
                            String productUri = productObject.get("url").toString();
                            String productDetailUrl = uri+"/it/en" +productUri;
                            productDetailUrls.add(productDetailUrl);
                        }
                    }
                }
            }else{

            }
        }catch(Exception e){
            System.out.println("请求库存数据接口失败");
        }
        return productDetailUrls;
    }


    /**
     * 拉取 品类 下的 商品列表信息
     *
     * @param sex 性别
     * @param categoryName  品类名称
     * @param productrDetailUrl  商品详情 url
     */
    public void grapProductDetail(String sex,String categoryName,String productrDetailUrl) {
        try {
            Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36)");
            Header[] headers = new Header[1];
            headers[0] = header;
            HttpResponse response = HttpUtils.get(productrDetailUrl,headers);
            if (response.getStatus() == 200) {
                String htmlContent = response.getResponse();
                Document doc = Jsoup.parse(htmlContent);
                Elements scripts = doc.select("script");
                for(Element script : scripts) {
                    String str = script.html();
                    if (str.contains("jQuery.extend(Drupal.settings,")) { //注意这里一定是html(), 而不是text()
                        String jsonStr = str.replace("jQuery.extend(Drupal.settings,","").replace(");", "").trim();
                        JsonElement je = new JsonParser().parse(jsonStr);
                        JsonObject asJsonObject = je.getAsJsonObject();
                        JsonArray productArray = asJsonObject.get("hermes_products").getAsJsonObject().get("data").getAsJsonObject().get("products").getAsJsonArray();
                        int valueSize = productArray.size();
                        for (int i = 0; i < valueSize ; i++) {
                            JsonObject productJsonObject = productArray.get(i).getAsJsonObject();
                            String sku = productJsonObject.get("sku").toString().replace("\"","");
                            if(barCodeMap.containsKey(sku)){
                                continue;
                            }
                            TemplateProductDTO productDto = new TemplateProductDTO();
                            String size = "";
                            String colorName = "";
                            String currentProductDetail = "";

                            String currentProductUri = productJsonObject.get("url").toString().replace("\"", "");
                            if(null==currentProductUri||"".equals(currentProductUri)){
                                currentProductDetail = productrDetailUrl;
                            }else{
                                currentProductDetail = uri +"/it/en/product/"+ currentProductUri;
                            }

                            JsonElement colorElement = productJsonObject.get("attributes").getAsJsonObject().get("field_color_hermes");
                            if(colorElement==null){
                                /**
                                 * TODO  没有颜色信息
                                 */
                                System.out.println(" 没有颜色信息："+currentProductDetail);
                                loggerError.error(" 没有颜色信息："+currentProductDetail);
                                continue;
                            }
                            JsonElement sizeElement = productJsonObject.get("attributes").getAsJsonObject().get("field_ref_size");
                            if(sizeElement==null||"".equals(sizeElement.toString())){ //只有颜色信息 没有尺寸信息
                                size = "U";
                            }else{
                                size = sizeElement.getAsJsonObject().get("attr_display").toString().replace(",",".").replace("\"","");
                            }
                            JsonObject colorObject = colorElement.getAsJsonObject();
                            JsonElement colorNameElement = colorObject.get("attr_display");
                            if(colorElement!=null){
                                colorName = colorNameElement.toString().replace(",",".").replace("\"","");
                            }
                            String season = "2018春夏";

                            String material = "";
                            StringBuffer descript = new StringBuffer();
                            JsonArray description = productJsonObject.get("description").getAsJsonArray();
                            for (int j = 0; j < description.size(); j++) {
                                if(description.get(j).toString().contains("<br/>")){
                                    System.out.println(description.get(j).toString());
                                }
                                String temDesc = description.get(j).toString().replace(",",".").replace("\"","").replace("<br/>"," ");
                                if(temDesc.contains("%")||temDesc.contains("eather")||temDesc.contains("kin")||temDesc.contains("wool")||temDesc.contains("Wool")){  //抽取材质信息
                                    material = material + " "+temDesc;
                                }
                                descript.append(temDesc).append(" ");
                            }

                            String productName = productJsonObject.get("display_name").toString().replace("\"","").replace(",",".");
                            String itemPrice = productJsonObject.get("price").toString().replace("€","").replace(".","").replace(",",".").replace("\"","").trim();
                            /**
                             *  获取库存 产地 信息
                             */
                            Map<String, String> productQty = getProductQty(sku);
                            String qty  = productQty.get("qty");
                            String qtyDesc = productQty.get("qtyDesc");


                            String made = "";

                            StringBuffer picUrls = new StringBuffer();
                            JsonArray imageArr = productJsonObject.get("images").getAsJsonArray();
                            for (int j = 0; j < imageArr.size(); j++) {
                                String imgUri = imageArr.get(j).getAsJsonObject().get("uri").toString().replace("\"","");
                                String imgUrl = "https:" + imgUri + "-1200-1200.jpg";
                                imgUrl = imgUrl.replace("\"","");
                                if(j==0){
                                    picUrls.append(imgUrl);
                                }else{
                                    picUrls.append("|").append(imgUrl);
                                }
                            }

                            StringBuffer sizeDesc = new StringBuffer();
                            JsonArray measurementArr = productJsonObject.get("measurements").getAsJsonArray();
                            for (int j = 0; j < measurementArr.size() ; j++) {
                                String temMeasurement = measurementArr.get(j).toString().replace(",",".").replace("\"","");
                                sizeDesc.append(" "+temMeasurement);
                            }

                            productDto.setSex(sex);
                            productDto.setBrand("HERMES");
                            productDto.setCategory(categoryName);
                            productDto.setSpuNo(sku);
                            productDto.setProductModelCode(sku);
                            productDto.setSeason(season);
                            productDto.setMaterial(material);
                            productDto.setColorName(colorName);
                            productDto.setSize(size);
                            productDto.setProductName(productName);
                            productDto.setItemprice(itemPrice);
                            productDto.setItemsaleprice(itemPrice);
                            productDto.setQty(qty);
                            productDto.setQtyDesc(qtyDesc);
                            productDto.setMade(made);
                            productDto.setDescript(descript.toString());
                            productDto.setPicUrls(picUrls.toString());
                            productDto.setUrl(currentProductDetail);
                            productDto.setSizeDesc(sizeDesc.toString());
                            exportExcel(productDto);
                            barCodeMap.put(sku,"");
                            System.out.print(barCodeMap.size()+":"+sku+" ");
                        }
                        //找到就放行
                        break;
                    }
                }
            }else{
                //添加到 失败 请求中
                failList.add(new RequestFailProductDTO(sex, categoryName, productrDetailUrl, "1"));
                System.out.println("cn-地址出错 productrDetailUrl"+productrDetailUrl);
                loggerError.error("cn-地址出错 productrDetailUrl"+productrDetailUrl);
                return;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 获取商品库存信息
     * @param skuNo
     * @throws Exception
     */
    public Map<String,String> getProductQty(String skuNo) throws Exception{

        List<String> skuList = new ArrayList();
        skuList.add(skuNo);
        //定义结果集
        Map<String,String> resultMap = new HashMap<>();
        //请求头
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36)");
        headerMap.put("Cookie", "_ga=GA1.2.1410775761.1531446626; cookiebanner=1; _gid=GA1.2.1246508835.1532678167; has_js=1; locale-country-data={\"country\":\"it\",\"locale\":\"en-he\",\"language\":\"en\"}; ECOM_SESS=3259abe9fab28706f7a9414a72c268c8; _gat_UA-72839523-2=1; _gat_UA-64545050-1=1");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("locale","it_en");
        jsonObject.put("skus",skuList);

        String jsonStr = jsonObject.toString();
        String qtyUrl = "https://www.hermes.com/apps/ecom/stock";

        try {
            //operateData(String operatorType,String transParaType ,String url,OutTimeConfig outTimeConf,Map<String,String> param,String jsonValue ,Map<String,String> headerMap,String username,String password)
            String resultJsonStr = HttpUtil45.operateData("post", "json", qtyUrl, timeConfig, null, jsonStr,headerMap, null, null);
            /**
             {
                 "H071002Z 01350": {
                 "in_stock": false
                 },
                 "H071002Z 01370": {
                 "in_stock": true
                 }
             }
             */
            //resultJsonStr = resultJsonStr.replace("\n","").trim();
            if(resultJsonStr!=null&&!"".equals(resultJsonStr)){
                JsonElement je = new JsonParser().parse(resultJsonStr);
                JsonObject skuObject = je.getAsJsonObject().get(skuNo).getAsJsonObject();
                if(skuObject!=null){
                    String inStockStr = skuObject.get("in_stock").toString();
                    if("true".equals(inStockStr)){  //有货
                        resultMap.put("qty","1");
                        resultMap.put("qtyDesc","有货");
                    }else{  //售罄
                        resultMap.put("qty","0");
                        resultMap.put("qtyDesc","售罄");
                    }
                }
            }else{
                resultMap.put("qty","0");
                resultMap.put("qtyDesc","售罄");
            }
        }catch(Exception e){
            System.out.println("请求库存数据接口失败");
            resultMap.put("qty","0");
            resultMap.put("qtyDesc","请求库存数据接口失败");
        }
        return resultMap;
    }


    /**
     * 导出单个商品信息到csv 文件（追加）
     * @param dto 商品信息DTO
     */
    private static void exportExcel(TemplateProductDTO dto) {
        if(dto==null||dto.getProductName()==null||"".equals(dto.getProductName())||dto.getSpuNo()==null||"".equals(dto.getSpuNo())){
            return;
        }

        //继续追加
        StringBuffer buffer = new StringBuffer();
        try {

            String supplierSpuNo = dto.getSpuNo();
            dto.setSupplierSkuNo(supplierSpuNo);
            String spuNo = solveSpu(supplierSpuNo);
            /**
             * TODO 处理商品货号数据
             *
             *
             */
            buffer.append(dto.getSex()).append(splitSign);
            buffer.append(dto.getBrand()).append(splitSign);
            buffer.append(dto.getCategory()).append(splitSign);

            buffer.append(spuNo).append(splitSign);
            buffer.append(spuNo).append(splitSign);
            buffer.append((dto.getSeason()==null||"".equals(dto.getSeason()))?"2018春夏":dto.getSeason()).append(splitSign);

            buffer.append(dto.getMaterial()).append(splitSign);
            buffer.append(dto.getColorName()).append(splitSign);
            buffer.append(dto.getSize()).append(splitSign);

            buffer.append(dto.getProductName()).append(splitSign);
            buffer.append(dto.getItemprice()).append(splitSign);
            buffer.append(dto.getItemprice()).append(splitSign);

            buffer.append(dto.getQty()).append(splitSign);
            buffer.append(dto.getQtyDesc()).append(splitSign);
            buffer.append(dto.getMade()).append(splitSign);

            buffer.append(dto.getSizeDesc()+" "+dto.getDescript()).append(splitSign);
            buffer.append(dto.getPicUrls()).append(splitSign);
            buffer.append(dto.getUrl()).append(splitSign);

            buffer.append(dto.getSizeDesc()).append(splitSign);
            buffer.append(supplierId).append(splitSign);
            buffer.append(supplierNo).append(splitSign);
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


    /**
     * 截取 hermes 原始barcode 11位有效字符 空格无效
     * @param barCode
     * @return
     */
    private static String solveSpu(String barCode){
        String[] split = barCode.split("");
        if(split.length==11||split.length<11){
            return barCode;
        }

        int count = 0;
        StringBuffer temBarCode = new StringBuffer();

        for (int i = 0; i <split.length ; i++) {
            temBarCode.append(split[i]);
            count++;
            if(count==11){
                return temBarCode.toString();
            }
        }
        return temBarCode.toString();

    }

    public static void main(String[] args) throws Exception {

        String barCode = "H074760 KAA";
        String s = solveSpu(barCode);
        System.out.println(s);

    }

}
