package com.shangpin.iog.opticalscribe.service;

/**
 * Created by zhaogenchun on 2015/11/26.
 */

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.opticalscribe.dto.GucciProductDTO;
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
 * Created by wanner on 2018/06/28

 conf.properties 配置信息如下：

 # gucci 中国
 uri-cn=https://www.gucci.cn
 #拉取数据存放路径
 path-cn=D://GUCCI/gucci-cn.csv
 #去重数据存放路径
 destFilePath-cn=D://GUCCI/gucci-cn-solved.csv
 # false:拉取数据  true:处理拉取完的数据（去重）
 flag-cn=false
 # women
 cn-genderAndNameAndCategoryUri1=women&&新作：RE(BELLE)&&/zh/ca/women/handbags/new
 cn-genderAndNameAndCategoryUri0=women&&手提包&波士顿包&&/zh/ca/women/handbags/top-handles
 cn-genderAndNameAndCategoryUri2=women&&托特包&&/zh/ca/women/handbags/totes
 cn-genderAndNameAndCategoryUri3=women&&链条包&肩背包&&/zh/ca/women/handbags/chain-shoulder-bags
 cn-genderAndNameAndCategoryUri4=women&&斜挎包&&/zh/ca/women/handbags/crossbody-bags
 cn-genderAndNameAndCategoryUri5=women&&腰包&&/zh/ca/women/handbags/belt-bags
 cn-genderAndNameAndCategoryUri6=women&&双肩背包&&/zh/ca/women/handbags/backpacks
 cn-genderAndNameAndCategoryUri7=women&&迷你包&&/zh/ca/women/handbags/mini-bags
 cn-genderAndNameAndCategoryUri8=women&&旅行箱包&&/zh/ca/women/accessories/luggage-lifestyle-bags
 cn-genderAndNameAndCategoryUri9=women&&卫衣&T恤&&/zh/ca/women/readytowear/sweatshirts-t-shirts
 cn-genderAndNameAndCategoryUri10=women&&莫卡辛鞋&乐福鞋&&/zh/ca/women/shoes/moccasins-loafers
 cn-genderAndNameAndCategoryUri11=women&&芭蕾舞鞋&&/zh/ca/women/shoes/ballerinas
 cn-genderAndNameAndCategoryUri12=women&&时装拖鞋&穆勒鞋&&/zh/ca/women/shoes/slippers-mules
 cn-genderAndNameAndCategoryUri13=women&&钱包&小皮件&&/zh/ca/women/accessories/wallets-small-accessories
 cn-genderAndNameAndCategoryUri14=women&&腰带&&/zh/ca/women/accessories/belts

 #men
 cn-genderAndNameAndCategoryUri21=men&&新作：Gucci Print&&/zh/ca/men/bags/new
 cn-genderAndNameAndCategoryUri22=men&&邮差包&&/zh/ca/men/bags/messenger
 cn-genderAndNameAndCategoryUri23=men&&背包&&/zh/ca/men/bags/backpacks
 cn-genderAndNameAndCategoryUri24=men&&腰包&&/zh/ca/men/bags/belt-bags
 cn-genderAndNameAndCategoryUri25=men&&托特包&&/zh/ca/men/bags/totes
 cn-genderAndNameAndCategoryUri26=men&&手拿包&&/zh/ca/men/bags/pouches-bags
 cn-genderAndNameAndCategoryUri27=men&&公文包&&/zh/ca/men/bags/briefcases
 cn-genderAndNameAndCategoryUri28=men&&行李袋&&/zh/ca/men/bags/suitcases-duffle-bags
 cn-genderAndNameAndCategoryUri29=men&&衬衫&&/zh/ca/men/readytowear/shirts
 cn-genderAndNameAndCategoryUri30=men&&卫衣&&/zh/ca/men/readytowear/sweatshirts
 cn-genderAndNameAndCategoryUri31=men&&T恤&Polo衫&&/zh/ca/men/readytowear/t-shirts-polos
 cn-genderAndNameAndCategoryUri32=men&&莫卡辛鞋&乐福鞋&&/zh/ca/men/shoes/moccasins-loafers
 cn-genderAndNameAndCategoryUri33=men&&系带鞋&&/zh/ca/men/shoes/lace-ups
 cn-genderAndNameAndCategoryUri34=men&&凉鞋&平底凉拖&&/zh/ca/men/shoes/sandals-slides
 cn-genderAndNameAndCategoryUri35=men&&钱包&小皮件&&/zh/ca/men/accessories/wallets-small-accessories
 cn-genderAndNameAndCategoryUri36=men&&腰带&&/zh/ca/men/accessories/belts

 */
//@Component("gucciProductCN")
public class GucciProductCN {

    //有库存
    private static final String IN_STOCK = "1";
    //无库存
    private static final String NO_STOCK = "0";

    private static String supplierId="2018070502014";
    private static String supplierNo="S0000963";

    // 商品barCodeMap  key:barCodeMap value:null
    private static HashMap<String, String> barCodeMap= null;

    //商品失败请求信息
    private static List<RequestFailProductDTO> failList = null;

    @Autowired
    ProductFetchService productFetchService;

    @Autowired
    EventProductService eventProductService;
    private static Logger logger = Logger.getLogger("info");
    private static ResourceBundle bdl = null;
    // .csv 文件分隔符
    private static String splitSign = ",";
    private static String uri="",path="",flag="",destFilePath="";
    // 所有品类相对路径以及名称 如： women&&新作：RE(BELLE)&&/zh/ca/women/handbags/new,women&&手提包&波士顿包&&/zh/ca/women/handbags/top-handles
    private static List<String> genderAndNameAndCategoryUris = new ArrayList<>();

    private static OutputStreamWriter  out= null;

    static {
        if (null == bdl)
            bdl = ResourceBundle.getBundle("conf");
        uri = bdl.getString("uri-cn");
        path = bdl.getString("path-cn");
        flag = bdl.getString("flag-cn");
        destFilePath = bdl.getString("destFilePath-cn");
        Enumeration<String> keys = bdl.getKeys();
        while(keys.hasMoreElements()){
            String key = keys.nextElement();
            if(key.startsWith("cn-genderAndNameAndCategoryUri")){
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

    public  void getUrlList() throws Exception {
        //根据flag 判断是处理数据还是 拉取数据
        if("true".equals(flag)){  //处理拉取的重复商品数据
            System.out.println("=============cn-处理重复数据开始===============");
            logger.info("=============cn-处理重复数据开始===============");
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

            barCodeMap = new HashMap<>();
            failList = new ArrayList<>();
            int productModeNumber = 0;

            //校验配置信息 是否符合格式要求
            int size = genderAndNameAndCategoryUris.size();
            for (int i = 0; i < size; i++) {
                String[] sexAnduriAndName = genderAndNameAndCategoryUris.get(i).split("&&");
                if(sexAnduriAndName.length!=3) {
                    logger.info("cn-genderAndCategoryUri 不符合格式 ：" + genderAndNameAndCategoryUris.get(i));
                    System.out.println(" cn-genderAndCategoryUri 不符合格式 ：" + genderAndNameAndCategoryUris.get(i));
                    throw new RuntimeException("配置文件 genderAndCategoryUri 不符合格式："+genderAndNameAndCategoryUris.get(i));
                }
            }

            for (int i = 0; i < size; i++) {

                if(i>0){
                    String[] temsexAnduriAndName = genderAndNameAndCategoryUris.get(i-1).split("&&");

                    System.out.println("cn拉取品类结束 gender:"+temsexAnduriAndName[0]+" categoryName:"+temsexAnduriAndName[1]+" uri:"+temsexAnduriAndName[2]);
                    System.out.println("共拉取 ："+(barCodeMap.size()-productModeNumber)+"款商品数据");
                    System.out.println("=====================================================================");

                    logger.info("cn拉取品类结束 gender:"+temsexAnduriAndName[0]+" categoryName:"+temsexAnduriAndName[1]+" uri:"+temsexAnduriAndName[2]);
                    logger.info("共拉取 ："+(barCodeMap.size()-productModeNumber)+"款商品数据");
                    logger.info("=====================================================================");
                    productModeNumber = barCodeMap.size();
                }
                //拉取 品类信息
                String[] sexAnduriAndName = genderAndNameAndCategoryUris.get(i).split("&&");
                String temCategoryUrl =  uri+sexAnduriAndName[2];
                System.out.println("============cn开始拉取："+sexAnduriAndName[0]+":"+sexAnduriAndName[1]+":"+temCategoryUrl+"=========================================================");
                logger.info("============cn开始拉取："+sexAnduriAndName[0]+":"+sexAnduriAndName[1]+":"+temCategoryUrl+"=========================================================");
                solveCategoryProjectEelements(sexAnduriAndName[0],sexAnduriAndName[1],temCategoryUrl);
            }
            String[] temsexAnduriAndName = genderAndNameAndCategoryUris.get(size-1).split("&&");
            System.out.println();
            System.out.println("cn拉取品类结束 gender:"+temsexAnduriAndName[0]+" categoryName:"+temsexAnduriAndName[1]+" uri:"+temsexAnduriAndName[2]);
            System.out.println("共拉取 ："+(barCodeMap.size()-productModeNumber)+"款商品数据");
            System.out.println("=====================================================================");


            logger.info("cn拉取品类结束 gender:"+temsexAnduriAndName[0]+" categoryName:"+temsexAnduriAndName[1]+" uri:"+temsexAnduriAndName[2]);
            logger.info("共拉取 ："+(barCodeMap.size()-productModeNumber)+"款商品数据");
            logger.info("所有品类 ："+barCodeMap.size()+"款商品数据");
            logger.info("=====================================================================");


            logger.error("=====================================================================================");
            logger.error("==========重新请求失败的商品及品类信息===================================================");
            int failSize = failList.size();
            for (int i = 0; i <failSize ; i++) {
                RequestFailProductDTO failProduct = failList.get(i);
                String flag = failProduct.getFlag();
                if("1".equals(flag)){
                    solveCategoryProjectEelements(failProduct.getGender(),failProduct.getCategoryName(),failProduct.getUrl());
                }else if("0".equals(flag)){
                    //校验 productModel 是否存在
                    savePre(failProduct.getGender(),failProduct.getCategoryName(),failProduct.getUrl());
                }
            }
            logger.error("=====================================================================================");
            logger.error("==========请求失败的商品及品类信息 结束===================================================");

            barCodeMap = null;
            failList = null;
            out.close();
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
    public void solveCategoryProjectEelements(String sex,String categoryName,String categoryUrl) {

        //请求分页的参数时 ni
        if(categoryUrl==null||"".equals(categoryUrl)){
            return;
        }
        try {
            HttpResponse response = HttpUtils.get(categoryUrl);
            if (response.getStatus() == 200) {
                String htmlContent = response.getResponse();

                Document doc = Jsoup.parse(htmlContent);

                //获取当前页的 商品数据
                Elements pp = doc.select("#pdlist");
                Elements pages = pp.select("li");
                //处理当前页的数据
                fetchProducts(sex,categoryName,pages);

                //加载更多下面还有 加载更多或者加载30个商品 li小于30不在加载 加载全部
                Elements addPage = doc.select("#addlist");
                String ni = doc.select("#filterNid").attr("value");
                Elements totalPage1 = addPage.select("a");
                String getMoreText = totalPage1.first().text();
                if(getMoreText == null||!getMoreText.contains("加载更多")){
                    return;
                }
                if (addPage != null) { //不为空 说明可以加载更多
                    /**
                     * 注意
                     * pn  可以从页面的    <a data-pn="2" class="spice-btn spice-btn-golden">加载更多</a>  data-pn属性 获取
                     * ni=12 可以从页面的 <input type="hidden" id="filterNid" value="27"/> 获取
                     *
                     * &_=1529405469032 为随意拼接的参数
                     */
                    //获取中间加载更多 默认最多加载30 条商品数据

                    if (totalPage1 != null) {
                        String pn = totalPage1.get(0).attr("data-pn");
                        if (pn != null&&ni !=null) {
                            htmlContent = HttpUtil45.get("https://www.gucci.cn/zh/itemList?pn=" + pn + "&ni="+ni+"&_=1529405469032", new OutTimeConfig(), null);
                            doc = Jsoup.parse(htmlContent);
                            //获取异步请求到的部分 商品信息 li
                            Elements elementsMore = doc.select("li");
                            fetchProducts(sex,categoryName,elementsMore);
                            if(elementsMore.size()>=30){
                                htmlContent = HttpUtil45.get("https://www.gucci.cn/zh/itemList?pn=3&ni="+ni+"&_=1529405469032", new OutTimeConfig(), null);
                                doc = Jsoup.parse(htmlContent);
                                //获取异步请求到的部分 商品信息 li
                                Elements elementsAll = doc.select("li");
                                fetchProducts(sex,categoryName,elementsAll);
                            }
                        }
                    }
                }
            }else{
                //添加到 失败 请求中
                failList.add(new RequestFailProductDTO(sex, categoryName, categoryUrl, "1"));
                System.err.println("cn-请求商品品类 地址出错  "+categoryUrl);
                logger.error("cn-请求商品品类 地址出错categoryUrl {}  "+categoryUrl);
                return;
            }
        }catch (Exception e){

        }
    }

    /**
     * 根据商品列表 elements 获取保存商品数据
     * @param sex 性别
     * @param categoryName 列表名称
     * @param elements 商品列表
     */
    public void fetchProducts(String sex,String categoryName,Elements elements){
        if(elements.size()>0){
            for (Element element:elements) {
                Element productDetailElement = element.select("a").first();
                String proUrl =productDetailElement.attr("href");
                if(StringUtils.isBlank(proUrl)){
                    return;
                }
                proUrl = uri + proUrl;
                savePre(sex,categoryName,proUrl);
            }
        }
    }



    /**
     * 获取某一 style 商品信息（单个或多个size）
     * @param url
     * @return
     */
    private  List<GucciProductDTO> getProductUrlForProject(String sex, String categoryName, String url) {
        List<GucciProductDTO> products = new ArrayList<>();

        GucciProductDTO product = new GucciProductDTO();
        //材质
        String  material = "";
        //商品描述
        String  desc= "";
        //商品名称
        String productName="";
        //供应商 spuNo
        String spuNo = "";
        //货号
        String  barCode= "";
        //品牌
        String brand = "GUCCI";
        //颜色 名称
        String colorCode = "";
        //产地
        String made = "意大利制造";
        //市场价
        String itemPrice="";
        //销售价
        String salePrice="";
        //库存
        String qty="";
        //库存描述
        String qtyDesc = "";
        //包类 尺寸大小
        StringBuffer sizeDesc = new StringBuffer();

        //图片信息
        StringBuffer picSrcs = new StringBuffer();  //多张图片 链接Str  用|分割
        String season = "2018春夏";


        try {
            Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36)");
            Header[] headers = new Header[1];
            headers[0] = header;

            HttpResponse response = HttpUtils.get(url,headers);

            if (response.getStatus()==200) {

                String htmlContent = response.getResponse();
                Document doc = Jsoup.parse(htmlContent);

                //商品名称  spice-product-name
                Element proNameElement  = doc.select("h1.spice-product-name").first();
                if(proNameElement!=null){
                    productName = proNameElement.text().replaceAll("\r|\n", "");
                }

                //商品货号 class="spice-style-number-title"
                Element barCodeElement  = doc.select("div.spice-style-number-title").first().select("span").first();
                if(barCodeElement!=null){
                    barCode = barCodeElement.text(); //款式sku
                    spuNo = barCode;
                }

                //获取商品市场价
                itemPrice = doc.select("div.spice-product-price").first().text();
                itemPrice = itemPrice.replace("￥","");
                itemPrice = itemPrice.replace(",","");
                Pattern pattern = Pattern.compile("[^0-9]");
                Matcher listMatcher = pattern.matcher(itemPrice);
                itemPrice = listMatcher.replaceAll("");

                //商品售价
                salePrice = itemPrice;

                //处理商品图片信息
                Elements imgsElements = doc.select("div.spice-standard-image").select("img");
                int i = 0;
                for(Element img : imgsElements) {
                    String imgUrlJsonStr = img.attr("spice-data-image-src");
                    try {
                        JsonElement je = new JsonParser().parse(imgUrlJsonStr);
                        JsonObject asJsonObject = je.getAsJsonObject();
                        JsonElement standardRetina = asJsonObject.get("standardRetina");
                        String imgUrl = standardRetina.toString();
                        if(i==0){
                            picSrcs.append(imgUrl);
                        }else{
                            picSrcs.append("|").append(imgUrl);
                        }
                        i++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                //商品描述 spice-product-detail
                Element descriptElement  = doc.select("div.spice-product-detail").select("p").first();
                if(descriptElement!=null){
                    desc = descriptElement.text();
                    desc = desc.replace(",",".").replaceAll("\r|\n", "");
                }

                //季节
                if(desc.contains("早春")){
                    season = "早春";
                }else if(desc.contains("早夏")){
                    season = "早夏";
                }else if(desc.contains("早秋")){
                    season = "早秋";
                }else if(desc.contains("早冬")){
                    season = "早冬";
                }else if(desc.contains("春")){
                    season = "春季";
                }else if(desc.contains("夏")){
                    season = "夏季";
                }else if(desc.contains("秋")){
                    season = "秋季";
                }else if(desc.contains("冬")){
                    season = "冬季";
                }

                //商品其他信息 如 颜色、产地、省份、尺码大小
                Elements otherInfoElements  = doc.select("div.spice-product-detail").select("ul").select("li");
                //StringBuffer otherInfos = new StringBuffer();
                for (Element otherInfoElement:otherInfoElements) {
                    /**
                     * 处理其他信息规则 含有 % 的为材质 含有 色 的为颜色+材质   （对于材质而言 % 优先级高 ）
                     */
                    String text = otherInfoElement.text();
                    if(text!=null){
                        text = text.replace(",",".");
                        if(text.contains("%")){
                            //34%人造丝，33%棉，12%亚麻，8%腈纶，7%尼龙，5%聚氨酯，1%涤纶
                            material = text;
                        }else if(text.contains("色")){
                            if(colorCode==null||"".equals(colorCode)){ //只取第一个颜色信息
                                colorCode = text;
                            }
                        }
                        //设置 包的尺寸信息
                        if(text.contains("宽）")||text.contains("高）")||text.contains("深）")||text.contains("厘米")){
                            sizeDesc.append(" "+text) ;
                        }
                    }
                }
                if(colorCode!=null){
                    colorCode.replace(",",".");
                    if(colorCode.contains("色")){
                        String[] split = colorCode.split("色");
                        if("".equals(material)){ //判断材质是否为空
                            material = split[split.length-1];
                        }
                        colorCode =split[0]+"色";
                    }
                }

                if(colorCode==null || "".equals(colorCode)){
                    colorCode = otherInfoElements.first().text();
                }
                if(material==null || "".equals(material)){
                    material = otherInfoElements.first().text();
                }
                product.setUrl(url);
                product.setMaterial(material);
                product.setProductname(productName);
                product.setPrice(itemPrice);
                product.setBrand(brand);
                product.setProductCode("");
                product.setDescript(desc);
                product.setBarCode(barCode);
                product.setMade(made);
                product.setSex(sex);
                product.setSeason(season);
                product.setColorCode(colorCode);
                product.setSizeDesc(sizeDesc.toString());
                product.setItemprice(itemPrice);
                product.setItemsaleprice(salePrice);
                product.setSpuNo(spuNo);
                product.setCategory(categoryName);
                product.setPicUrls(picSrcs.toString().replace("\"",""));

                //product.setOtherInfos(otherInfos.toString());

                /**
                 *   处理商品 的尺码 以及 库存信息
                 */
                //判断当前包页面有没有尺码信息  有分成多个 product 没有 尺码为均码
                //   .spice-dropdown-pdp-size-box
                Elements sizeElements = doc.select("div.spice-dropdown-pdp-size-box").select("ul").select("li");
                if(sizeElements!=null&&sizeElements.size()>0){
                    for (Element sizeElement:sizeElements ) {
                        String size = "";
                        String sizeContent = sizeElement.text();
                        if(sizeContent==null||"".equals(sizeContent)||sizeContent.contains("选择尺码")){
                            continue;
                        }

                        if(sizeContent!=null&&sizeContent.contains("after")){
                            String[] split = sizeContent.split("::");
                            size = split[0].replace("\"","");
                        }else {
                            size = sizeContent.replace("\"", "");
                        }
                        product.setSize(size);
                        //获取skuId 获取库存 qty
                        String skuId = sizeElement.attr("spice-data-value");
                        String className = sizeElement.select("span").first().attr("class");
                        if(className!=null&&"spice-size-no".equals(className)){  //页面显示没有库存
                            qty = NO_STOCK;
                            qtyDesc = "售罄";
                        } else{
                            Map<String, String> checkProductInfo = getCheckProductInfo(response, skuId,url);
                            qty = checkProductInfo.get("qty");
                            qtyDesc = checkProductInfo.get("qtyDesc");
                        }
                        product.setQty(qty);
                        product.setQtyDesc(qtyDesc);
                        products.add(GucciProductDTO.copyProductData(product));

                        /**
                         * 获取每一个尺码都休息 0.5s
                         */
                        Thread.sleep(500);
                    }
                }else{
                    /**
                     *  获取sessionId  以及 X-CSRF-TOKEN
                     */
                    Map<String, String> checkProductInfo = getCheckProductInfo(response, null,url);
                    qty = checkProductInfo.get("qty");
                    qtyDesc = checkProductInfo.get("qtyDesc");
                    //处理 详情其他信息
                    product.setQty(qty);
                    product.setQtyDesc(qtyDesc);
                    product.setSize("U");
                    products.add(product);

                }
            }else{
                failList.add(new RequestFailProductDTO(sex,categoryName,url,"0"));
                System.err.println("cn-getProductUrlForProject请求商品url 失败"+sex +":"+categoryName+":"+url);
                logger.error("cn-getProductUrlForProject请求商品url 失败"+sex +":"+categoryName+":"+url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    /**
     * 访问商品详情页信息获取所有包款式 style url
     * @param sex 性别
     * @param categoryName 分类名称
     * @param proUrl 商品详情url
     */
    private void savePre(String sex,String categoryName,String proUrl){

        try {
            HttpResponse response = HttpUtils.get(proUrl);
            if (response.getStatus()==200) {

                String htmlContent = response.getResponse();
                Document doc = Jsoup.parse(htmlContent);
                /**
                 * 判断当前商品是否含有多个 款式
                 */
                Elements styleElements = doc.select("div.spice-product-style-selector").select("div.spice-style-color-material").select("span.spice-material-num");
                if(styleElements!=null&&styleElements.size()>0){
                    String styleText = styleElements.text();
                    if(styleText!=null&&styleText.contains("款式")){
                        Elements elements = loadingSameStyle(response, proUrl);
                        /**
                         * elements 每个款式 element 结构如下
                         * <li data-color-material="Ophidia系列高级人造帆布小号腰包" data-price='￥9,600' data-code="51707696I3B8745">
                         *     <div class="spice-background-image">
                         *          <a data-href="/zh/pr/51707696I3B8745">
                         *              <img src="/images/autoReplaceImageSrc/lazy-medium.png" spice-data-image-src='{
                         * 					"standard": "https://res.gucci.cn/resources/2018/2/13/15185131081479788_w_170X250.jpg",
                         * 					"standardRetina": "https://res.gucci.cn/resources/2018/2/13/15185131081479788_w_340X500.jpg",
                         * 				    "medium": "https://res.gucci.cn/resources/2018/2/13/15185131081479788_w_180X320.jpg",
                         * 					"mediumRetina": "https://res.gucci.cn/resources/2018/2/13/15185131081479788_w_360X640.jpg",
                         * 					"small": "https://res.gucci.cn/resources/2018/2/13/15185131081479788_w_245X365.jpg",
                         * 					"smallRetina": "https://res.gucci.cn/resources/2018/2/13/15185131081479788_w_490X730.jpg"
                         *                                                        }' alt="Ophidia系列高级人造帆布小号腰包"/>
                         *          </a>
                         *     </div>
                         * </li>
                         */
                        /**
                         * 遍历多款数据
                         */
                        for (Element element: elements) {
                            //商品detail uri
                            String productDetailLink = element.select("a").first().attr("data-href");
                            //保存该款商品数据
                            productDetailLink = uri +productDetailLink;
                            saveAndSolveRepeatSpu(sex,categoryName,productDetailLink);
                        }
                    }
                }else{ //该商品只有一款
                    //保存该款商品数据
                    saveAndSolveRepeatSpu(sex,categoryName,proUrl);
                }
            }else{
                failList.add(new RequestFailProductDTO(sex,categoryName,proUrl,"0"));
                System.err.println("cn-savePre请求商品url 失败"+sex +":"+categoryName+":"+proUrl);
                logger.error("cn-savePre请求商品url 失败"+sex +":"+categoryName+":"+proUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存并处理重复商品数据 时机：获取具体某一style 商品数据 前（判断是否拉取过） 后（将处理过的barCode 存入barCodeMap ）
     * @param sex 性别
     * @param categoryName 品类名称
     * @param productDetailLink 商品详情url
     */
    public void saveAndSolveRepeatSpu(String sex,String categoryName,String productDetailLink){
        /**
         * 获取barCode
         */
        String barCode= "";
        if(productDetailLink.contains("?")){
            String[] split = productDetailLink.split("\\?");
            barCode = split[0].replace("/zh/pr/","");
        }else{
            String temUri[] = productDetailLink.split("/");
            barCode = temUri[temUri.length-1];
        }
        //校验之前是否已经存储过该SPU
        if(barCodeMap.containsKey(barCode)){
            return;
        }

        //获取商品详情页数据
        saveByProductDetailLink( sex, categoryName, productDetailLink);
        /**
         * 将商品的 barCode 加入到 barCodeMap 中
         */
        barCodeMap.put(barCode,null);
        System.out.print(" "+barCodeMap.size());
        logger.info(" "+barCodeMap.size());
    }

    /**
     * 访问商品 某一style 获取该 style 所有商品size ，并输出到csv文件
     * @param category
     */
    private void saveByElements(String sex,String categoryName,Element category){

        Element productDetailElement = category.select("a").first();
        String proUrl =productDetailElement.attr("href");
        if(StringUtils.isBlank(proUrl)){
            return;
        }
        saveAndSolveRepeatSpu(sex,categoryName,proUrl);
    }

    /**
     * 访问商品 某一style 获取该 style 所有 size ，并输出到csv文件
     * @param proUrl 商品详情页url
     */
    private void saveByProductDetailLink(String sex,String categoryName,String proUrl)  {

        //获取商品详情页数据，多个商品信息是同一个 SPU 只是size 不同
        List<GucciProductDTO> pros = getProductUrlForProject(sex,categoryName,proUrl);
        for (GucciProductDTO pro:pros) {
            exportExcel(pro);
        }
        /**
         * 拉取商品某一个style 就sleep 10s
         */
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据商品数据详情页response 获取页面内的隐藏数据 可用于获取库存以及不同款式的请求数据
     * @param response  商品数据详情页response
     * @param skuId  商品skuId  不为空以参数为准， 没有页面获取
     * @param url   商品详情页请求url
     * @return  Cookie  X-CSRF-TOKEN itemId itemCode style
     */
    public  static Map<String,String> getProductDetailRequestData(HttpResponse response,String skuId,String url){

        Map<String, String> mapDate = new HashMap<>();

        //获取 sessionId
        String sessionId = response.getSessionId();
        sessionId = "JSESSIONID="+sessionId;
        mapDate.put("Cookie",sessionId);
        //获取X-CSRF-TOKEN <meta name="_csrf" content="b19f5cb2-2e05-46f2-8989-b1e36ba4a389"/>
        String _csrf ="";
        String htmlContent = response.getResponse();
        Document doc = Jsoup.parse(htmlContent);
        Elements select = doc.select("meta[name=_csrf]");
        _csrf = select.first().attr("content");
        mapDate.put("X-CSRF-TOKEN",_csrf);

		/*itemId: 2466     <input type="hidden" id="itemId" value="2466"/>
		itemCode: 476541DVUXT9193
		skuId: 14107
			<script type="text/javascript">
				var currentItemCode = '476541DVUXT9193';
				var currentSkuId='14107';
				var style="476541";
			</script>
		*/
        //获取  skuId

        String itemCode = "";
        String style = "";
        String responseSkuId = "";
        Elements scriptElements = doc.select("script");
        for (Element scriptElement: scriptElements) {
            String scriptTtext = scriptElement.html();
            if(scriptTtext!=null&&scriptTtext.contains("currentItemCode")){
                String[] split = scriptTtext.split(";");
                for (String temStr:split) {
                    if(temStr.contains("currentItemCode")){
                        itemCode = temStr.split("=")[1].trim().replace("\'","").replace("\"","");
                    }else if(temStr.contains("currentSkuId")){
                        responseSkuId = temStr.split("=")[1].trim().replace("\'","").replace("\"","");
                    }else if(temStr.contains("style")){
                        style = temStr.split("=")[1].trim().replace("\'","").replace("\"","");
                    }
                }
            }
        }

        mapDate.put("style",style);
        String itemId = doc.select("#itemId").attr("value");
        mapDate.put("itemId",itemId);
        mapDate.put("itemCode",itemCode);
        if(skuId!=null&&!"".equals(skuId)){
            mapDate.put("skuId",skuId);
        }else{
            mapDate.put("skuId",responseSkuId);
        }
        return mapDate;
    }

    /**
     * 获取库存数据 qty
     * @param response 用户取出 token sessionId itemCode itemId skuId 等参数
     * @param skuId 当有多个尺码的时候 skuId 需要手动出入
     * @return
     */
    public  Map<String,String> getCheckProductInfo(HttpResponse response,String skuId,String url){

        /**
         *  异步请求商品 库存信息
         *  请求头：
         *  X-CSRF-TOKEN:e41f4410-3ebd-4701-8d81-3f032674c75c
         *  Cookie:JSESSIONID=813100536F60F5184E96B3693BC9B0FE-n2.n-1-2
         */
        Map<String, String> mapDate = getProductDetailRequestData(response, skuId, url);

        Header[] checkHeaders = getHeaders(mapDate);
        String checkProductUrl = uri + "/zh/pr/checkProduct";

        try {
            getProductQtyUnit(checkProductUrl, mapDate, checkHeaders);
        } catch (Exception e) {

            logger.info("=========第一次： 获取库存失败 itemId:"+mapDate.get("itemId")+"  itemCode:"+mapDate.get("itemCode") + "  skuId:"+mapDate.get("skuId")+" ===url: "+url);
            logger.info("=========尝试再次获取=====");
            try {
                //重新请求商品详情
                Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36)");
                Header[] headers = new Header[1];
                headers[0] = header;
                HttpResponse responseSecond = HttpUtils.get(url,headers);
                if (responseSecond.getStatus()==200) {
                    mapDate = getProductDetailRequestData(responseSecond, skuId, url);
                    checkHeaders = getHeaders(mapDate);
                    getProductQtyUnit(checkProductUrl, mapDate, checkHeaders);
                    logger.info("=====获取成功");
                }
            } catch (Exception ee) {
                //获取不到库存 默认为0
                mapDate.put("qty",NO_STOCK);
                mapDate.put("qtyDesc","售罄");
                logger.info("=========第二次：获取库存失败 itemId:"+mapDate.get("itemId")+"  itemCode:"+mapDate.get("itemCode") + "  skuId:"+mapDate.get("skuId")+" ===url: "+url);
                ee.printStackTrace();
                System.out.println();
                System.err.println("=========第二次：获取库存失败 itemId:"+mapDate.get("itemId")+"  itemCode:"+mapDate.get("itemCode") + "  skuId:"+mapDate.get("skuId")+" ===url: "+url);
                System.out.println();
            }
        }
        return mapDate;
    }

    /**
     * 获取商品库存信息
     * @param checkProductUrl
     * @param mapDate
     * @param checkHeaders
     * @throws Exception
     */
    public void getProductQtyUnit(String checkProductUrl,Map<String,String> mapDate,Header[] checkHeaders) throws Exception{
        HttpResponse checkResponse = HttpUtils.post(checkProductUrl,mapDate,checkHeaders);
        if (checkResponse.getStatus()==200) {
            String qtyJson = checkResponse.getResponse();
            JsonElement je = new JsonParser().parse(qtyJson);
            JsonObject asJsonObject = je.getAsJsonObject();
            JsonElement standardRetina = asJsonObject.get("sketch");
            String offOutStock = asJsonObject.get("offOutStock").toString();
            if("false".equals(offOutStock)){
                if(standardRetina!=null){
                    String  qty = standardRetina.toString().replace("\"","");
                    if("有货".equals(qty)){
                        mapDate.put("qty",IN_STOCK);
                        mapDate.put("qtyDesc","有货");
                    }else if("预售".equals(qty)){
                        mapDate.put("qty",NO_STOCK);
                        mapDate.put("qtyDesc","预售");
                    }else if("门店配货".equals(qty)){
                        mapDate.put("qty",IN_STOCK);
                        mapDate.put("qtyDesc","门店配货");
                    }else{
                        mapDate.put("qty",NO_STOCK);
                        mapDate.put("qtyDesc","售罄");
                    }
                }
            }else{
                // 售罄
                mapDate.put("qty",NO_STOCK);
                mapDate.put("qtyDesc","售罄");
            }
        }
    }


    /**
     * 根据 mapData 获取请求头信息
     * @param mapData
     * @return
     */
    public static Header[] getHeaders(Map<String,String> mapData){
        Header[] headers = new Header[3];
        Header cookieHeader = new Header("X-CSRF-TOKEN", mapData.get("X-CSRF-TOKEN"));
        Header tokenHeader = new Header("Cookie", mapData.get("Cookie"));
        headers[0] = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36)");
        headers[1] = cookieHeader;
        headers[2] = tokenHeader;
        return headers;
    }

    /**
     * 获取spu 下所有款式 数据elements
     * @param response 商品详情response 用于取出 token sessionId itemCode itemId skuId 等参数
     * @return
     */
    public  static Elements loadingSameStyle(HttpResponse response,String url){

        /**
         *  异步请求spu 其它款数据
         *  请求头：
         *  X-CSRF-TOKEN:e41f4410-3ebd-4701-8d81-3f032674c75c
         *  Cookie:JSESSIONID=813100536F60F5184E96B3693BC9B0FE-n2.n-1-2
         */
        Elements sameStyleElements =  null;

        Map<String, String> mapDate = getProductDetailRequestData(response, null, url);

        Header[] checkHeaders = getHeaders(mapDate);
        String checkProductUrl = uri + "/zh/pr/loadingSameStyle";

        try {
            HttpResponse checkResponse = HttpUtils.post(checkProductUrl,mapDate,checkHeaders);
            if (checkResponse.getStatus()==200) {
                String htmlContent = checkResponse.getResponse();
                /**
                 <div class="spice-color-material-dialog spice-material-dialog">
                 <div class="spice-scroll e-scroll-color">
                 <ul>
                 <li data-color-material="Ophidia系列小号腰包" data-price='￥10,200' data-code="5170760KCDB8670">
                 <div class="spice-background-image">
                 <a data-href="/zh/pr/5170760KCDB8670">
                 <img src="/images/autoReplaceImageSrc/lazy-medium.png" spice-data-image-src='{
                 "standard": "https://res.gucci.cn/resources/2018/2/13/15185130000562013_w_170X250.jpg",
                 "standardRetina": "https://res.gucci.cn/resources/2018/2/13/15185130000562013_w_340X500.jpg",
                 "medium": "https://res.gucci.cn/resources/2018/2/13/15185130000562013_w_180X320.jpg",
                 "mediumRetina": "https://res.gucci.cn/resources/2018/2/13/15185130000562013_w_360X640.jpg",
                 "small": "https://res.gucci.cn/resources/2018/2/13/15185130000562013_w_245X365.jpg",
                 "smallRetina": "https://res.gucci.cn/resources/2018/2/13/15185130000562013_w_490X730.jpg"
                 }' alt="Ophidia系列小号腰包"/>
                 </a>
                 </div>
                 </li>
                 <li data-color-material="Ophidia系列高级人造帆布小号腰包" data-price='￥9,600' data-code="51707696I3B8745">
                 <div class="spice-background-image">
                 <a data-href="/zh/pr/51707696I3B8745">
                 <img src="/images/autoReplaceImageSrc/lazy-medium.png" spice-data-image-src='{
                 "standard": "https://res.gucci.cn/resources/2018/2/13/15185131081479788_w_170X250.jpg",
                 "standardRetina": "https://res.gucci.cn/resources/2018/2/13/15185131081479788_w_340X500.jpg",
                 "medium": "https://res.gucci.cn/resources/2018/2/13/15185131081479788_w_180X320.jpg",
                 "mediumRetina": "https://res.gucci.cn/resources/2018/2/13/15185131081479788_w_360X640.jpg",
                 "small": "https://res.gucci.cn/resources/2018/2/13/15185131081479788_w_245X365.jpg",
                 "smallRetina": "https://res.gucci.cn/resources/2018/2/13/15185131081479788_w_490X730.jpg"
                 }' alt="Ophidia系列高级人造帆布小号腰包"/>
                 </a>
                 </div>
                 </li>
                 </ul>
                 </div>
                 <div class="spice-product-style-resume">
                 <div class="spice-product-quantity">
                 <span>2个款式</span>
                 </div>
                 <div class="spice-product-title">
                 <a class="spice-prev-style spice-none-xs spice-none">Prev</a>
                 <span>Ophidia系列小号腰包</span>
                 <a class="spice-next-style spice-none-xs">Next</a>
                 </div>
                 <div class="spice-product-price" data-code="5170760KCDB8670">
                 ￥10,200</div>
                 </div>
                 <div class="spice-dialog-btn-group">
                 <a class="spice-btn spice-btn-golden e-spice-dialog-submit">
                 <span>选择此款式</span>
                 </a>
                 </div>
                 </div>

                 */
                Document doc = Jsoup.parse(htmlContent);
                sameStyleElements = doc.select("div.spice-color-material-dialog").select("div.spice-scroll").select("ul").select("li");
            }
        } catch (Exception e) {
            logger.info("=========获取款式 styles 失败 itemId:"+mapDate.get("itemId")+"  itemCode:"+mapDate.get("itemCode") + "  skuId:"+mapDate.get("skuId")+" ===url: "+url);
            e.printStackTrace();
        }
        return sameStyleElements;
    }



    /**
     * 导出单个商品信息到csv 文件（追加）
     * @param dto 商品信息DTO
     */
    private static void exportExcel(GucciProductDTO dto) {
        //继续追加
        StringBuffer buffer = new StringBuffer();
        try {
            buffer.append(dto.getSex()).append(splitSign);
            buffer.append(dto.getBrand()).append(splitSign);

            buffer.append(dto.getCategory()).append(splitSign);
            buffer.append(dto.getSpuNo()).append(splitSign);

            buffer.append(dto.getBarCode()).append(splitSign);
            buffer.append((dto.getSeason()==null||"".equals(dto.getSeason()))?"2018春夏":dto.getSeason()).append(splitSign);

            buffer.append(dto.getMaterial()).append(splitSign);
            buffer.append(dto.getColorCode()).append(splitSign);

            buffer.append(dto.getSize()).append(splitSign);
            buffer.append(dto.getProductname()).append(splitSign);

            buffer.append(dto.getItemprice()).append(splitSign);
            buffer.append(dto.getItemsaleprice()).append(splitSign);

            buffer.append(dto.getQty()).append(splitSign);
            buffer.append(dto.getQtyDesc()).append(splitSign);


            buffer.append(dto.getMade()).append(splitSign);

            buffer.append(dto.getSizeDesc()+"。"+dto.getDescript()).append(splitSign);
            buffer.append(dto.getPicUrls()).append(splitSign);

            buffer.append(dto.getUrl()).append(splitSign);
            buffer.append(dto.getSizeDesc()).append(splitSign);

            buffer.append(supplierId).append(splitSign);
            buffer.append(supplierNo).append(splitSign);
            buffer.append(splitSign);

            buffer.append("\r\n");
            out.write(buffer.toString());
            //System.out.println(buffer.toString());
            logger.info(buffer.toString());
            out.flush();
        } catch (Exception e) {
        }
    }


}