package com.shangpin.iog.gucci.service;

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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wanner on 2018/6/27
 */
@Component("fetchStockImpl")
public class FetchStockImpl  {

    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");

    private static ResourceBundle bdl = null;
    private static String supplierId = "",supplierNo = "",fetchSpProductInfosUrl ="",updateSpMarketPriceUrl="",pageSize="";

    private static OutputStreamWriter  out= null;
    static String splitSign = ",";
    //库存csv 文件存放目录
    private static String filePath="";

    //GUCCI意大利官网地址
    private static String uri="";

    //有库存
    private static final String IN_STOCK = "1";
    //无库存
    private static final String NO_STOCK = "0";

    // 请求失败的尚品 skuNo 集合
    private static List<SpSkuNoDTO> failedSpSkuNoList = null;

    static {
        if (null == bdl){
            bdl = ResourceBundle.getBundle("conf");
        }
        supplierId = bdl.getString("supplierId");

        supplierNo = bdl.getString("supplierNo");

        fetchSpProductInfosUrl = bdl.getString("fetchSpProductInfosUrl");

        updateSpMarketPriceUrl = bdl.getString("updateSpMarketPriceUrl");

        pageSize = bdl.getString("pageSize");

        filePath = bdl.getString("csvFilePath");

        uri = bdl.getString("uri");

    }

    private static OutTimeConfig timeConfig = new OutTimeConfig(1000*60*30,1000*60*30,1000*60*30);

    /**
     * 拉取 意大利官网 商品库存数据
     */
    public void fetchItlyProductStock(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startDateTime = format.format(new Date());
        System.out.println("============拉取GUCCI库存数据开始 "+startDateTime+"=========================");
        logger.info("==============拉取GUCCI库存数据开始 "+startDateTime+"=========================");

        //1. 请求需要更新库存商品 信息接口
        failedSpSkuNoList = new ArrayList<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = simpleDateFormat.format(new Date());

        String temFilePath = filePath + "gucci-qty-"+todayStr+".csv";
        System.out.println("文件保存目录："+temFilePath);
        logger.info("文件保存目录："+temFilePath);
        try {
            out = new OutputStreamWriter(new FileOutputStream(temFilePath, true),"gb2312");
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuffer buffer = new StringBuffer(
                "spSkuNO" + splitSign +
                        "qty" + splitSign
        ).append("\r\n");
        try {
            out.write(buffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //定义所有需要的更新库存商品信息List
        List<ProductDTO> productDTOAllList =  new LinkedList<>();

        //获取第一页商品数据
        ShangPinPageContent gucciPageContent = getShangPinPageContentByParam(supplierId,"GUCCI", 1, Integer.parseInt(pageSize));
        productDTOAllList.addAll(gucciPageContent.getZhiCaiResultList());

        if(gucciPageContent == null) return;
        //总记录数
        Integer total = gucciPageContent.getTotal();
        Integer pageNumber = getPageNumber(total, 20);
        for (int i = 2; i <= pageNumber; i++) {
            ShangPinPageContent temgucciPageContent = getShangPinPageContentByParam(supplierId,"GUCCI", i, Integer.parseInt(pageSize));
            if(temgucciPageContent!=null){
                productDTOAllList.addAll(temgucciPageContent.getZhiCaiResultList());
            }else{ //请求失败重新 再次请求
                temgucciPageContent = getShangPinPageContentByParam(supplierId,"GUCCI", i, Integer.parseInt(pageSize));
                if(temgucciPageContent!=null){
                    productDTOAllList.addAll(temgucciPageContent.getZhiCaiResultList());
                }
            }
        }

        logger.info("=====需要更新GUCCI spProduct Size:"+productDTOAllList.size());
        System.out.println("=====需要更新GUCCI spProduct Size:"+productDTOAllList.size());
        //导出尚品库存数据
        exportQtyInfoForProductList(productDTOAllList);

        // 处理 请求失败的 尚品 skuNo 信息
        int failedSpSkuNoSize = failedSpSkuNoList.size();
        for (int i = 0; i < failedSpSkuNoSize; i++) {
            repeatSolveFailedSpSkuNo(failedSpSkuNoList.get(i));
        }

        String endtDateTime = format.format(new Date());
        logger.info("===================拉取GUCCI库存数据结束 "+endtDateTime+"=========================");
        System.out.println("=================拉取GUCCI库存数据结束 "+endtDateTime+"=========================");

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
     * 获取需要更新库存商品 分页数据
     * @param brandName 供应商名称
     * @param pageIndex 页码
     * @param pageSize 分页条数
     * @return
     */
    public static ShangPinPageContent getShangPinPageContentByParam(String supplierId,String brandName,Integer pageIndex,Integer pageSize){
        //String fetchSpProductInfosUrl = "http://192.168.20.176:8003/supplier-sku/get-product";
        //1. 请求需要更新库存商品 信息接口
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("supplierId",supplierId);
        jsonObject.put("brandName",brandName);
        jsonObject.put("pageIndex",pageIndex);
        jsonObject.put("pageSize",pageSize);

        String jsonStr = jsonObject.toString();

        ShangPinPageContent shangPinPageContent = null;
        try {
            String resultJsonStr = HttpUtil45.operateData("post","json",fetchSpProductInfosUrl,timeConfig,null,jsonStr,null,null);
            //System.out.println("=======resultJsonStr:"+resultJsonStr);
            //logger.info("=======resultJsonStr:"+resultJsonStr);
            JSONObject resultJsonObject = JSONObject.fromObject(resultJsonStr);
            Map<String,Class> keyMapConfig= new HashMap<>();
            keyMapConfig.put("zhiCaiResultList",ProductDTO.class);
            keyMapConfig.put("zhiCaiSkuResultList",SkuDTO.class);
            keyMapConfig.put("content",ShangPinPageContent.class);
            ApiResponseBody apiResponseBody = (ApiResponseBody) JSONObject.toBean(resultJsonObject, ApiResponseBody.class, keyMapConfig);

            if(apiResponseBody!=null){
                shangPinPageContent = (ShangPinPageContent) apiResponseBody.getContent();
            }
            System.out.println();
            System.out.println("获取第 "+pageIndex+"页成功 :"+resultJsonStr);
            logger.info("获取第 "+pageIndex+"页成功 :"+resultJsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shangPinPageContent;
    }


    /**
     * 循环遍历 拉取商品库存信息
     * @param productDTOAllList 商品信息list
     */
    public static void exportQtyInfoForProductList(List<ProductDTO> productDTOAllList){
        for (ProductDTO productDTO:productDTOAllList) {
            boolean flag = solveProductQty(productDTO);
            if(!flag){
                repeatSolveFailProductQty(productDTO);
            }
        }
    }

    /**
     * 重复处理 spSkuNO qty 信息
     * @param spSkuNoDTO
     */
    public static void repeatSolveFailedSpSkuNo(SpSkuNoDTO spSkuNoDTO){
        int count = 0;
        while(count>4){
            count++;
            Map<String, String> checkProductInfo = getProductQtyInfo(spSkuNoDTO.getSpSkuNo(),spSkuNoDTO.getPcode());
            String temQty = checkProductInfo.get("qty");
            if (temQty != null) {
                exportSpSkunoAndQty(spSkuNoDTO.getSpSkuNo(),temQty);
                return;
            }
        }
        //重复请求 失败 做商品 库存置零处理
        exportSpSkunoAndQty(spSkuNoDTO.getSpSkuNo(),NO_STOCK);
    }

    /**
     * 重复请求 处理 商品库存信息 ，最后没有处理成功 直接将 库存置0 处理
     * @param productDTO
     */
    private static void repeatSolveFailProductQty(ProductDTO productDTO) {
        int count = 0;
        while(count<4){
            count++;
            boolean temFlag = solveProductQty(productDTO);
            if(temFlag){
                return;
            }
        }
        //重复请求 失败 做商品 库存置零处理
        setQtyZeroForProduct(productDTO);
    }

    /**
     * 将商品所有 spSkuNO 置零 并导出到 csv
     * @param productDTO
     */
    private static void setQtyZeroForProduct(ProductDTO productDTO) {
        if(productDTO!=null){
            List<SkuDTO> zhiCaiSkuResultList = productDTO.getZhiCaiSkuResultList();
            if(zhiCaiSkuResultList!=null&&zhiCaiSkuResultList.size()>0){
                int size = zhiCaiSkuResultList.size();
                for (int i = 0; i < size; i++) {
                    SkuDTO skuDTO = zhiCaiSkuResultList.get(i);
                    String spSkuNo = skuDTO.getSpSkuNo();
                    if(spSkuNo!=null){
                        exportSpSkunoAndQty(spSkuNo,NO_STOCK);
                    }
                }
            }
        }

    }

    /**
     * 处理单个商品库存信息
     * @param productDTO 商品信息
     */
    private static boolean solveProductQty(ProductDTO productDTO) {
        String productUrl = productDTO.getProductUrl();
        List<SkuDTO> zhiCaiSkuResultList = productDTO.getZhiCaiSkuResultList();
        int zhiCaiSkuResultListSize = zhiCaiSkuResultList.size();
        try {
            Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36)");
            Header[] headers = new Header[1];
            headers[0] = header;

            HttpResponse response = HttpUtils.get(productUrl,headers);

            if (response.getStatus()==200) {
                String htmlContent = response.getResponse();
                Document doc = Jsoup.parse(htmlContent);

                /**
                 *   处理商品 的尺码 以及 库存信息
                 <form action="/it/it/cart/ajax/add" class="add-to-shopping-bag-form" id="product-detail-add-to-shopping-bag-form">
                 <input name="sku" type="hidden" value="747804" />
                 <input class="personalized" name="personalized" type="hidden" value="false" />
                 <input class="monogramStyle" name="monogramStyle" type="hidden" value="" />
                 <input class="monogram" name="monogram" type="hidden" value="" />
                 <input name="position" type="hidden" value="1" />
                 <input name="listName" type="hidden" value="PGEU4Cols" />
                 <input name="categoryPath" type="hidden" value="Women/Womens-Ready-to-Wear/Coats" />
                 <input class="CSRFToken" name="CSRFToken" type="hidden" value="fca2c19f-3364-4fe3-ba86-c32d8015b53f" />

                 <div class="sizes">
                 <div class="size-dropdown " data-module="productSizes" >
                 <div class="content-select basic-content-select">
                 <select name="size" class="custom size-select _disable-mobile "   aria-label="Taglia">
                 <option value="-1" selected="selected">Taglia</option>
                 <option value="808313659" data-copy="36" data-copy-device="36&nbsp;" data-available="true" class="" >
                 36&nbsp;
                 </option>
                 <option value="808305674" data-copy="38" data-copy-device="38&nbsp;" data-available="true" class="" >
                 38&nbsp;
                 </option>
                 <option value="808225409" data-copy="40" data-copy-device="40&nbsp;" data-available="true" class="" >
                 40&nbsp;
                 </option>
                 <option value="808305927" data-copy="42" data-copy-device="42&nbsp;" data-available="true" class="" >
                 42&nbsp;
                 </option>
                 <option value="808305928" data-copy="44" data-copy-device="44&nbsp;" data-available="true" class="" >
                 44&nbsp;
                 </option>
                 <option value="808310044" data-copy="46" data-copy-device="46&nbsp;" data-available="true" class="" >
                 46&nbsp;
                 </option>
                 <option value="808313400" data-copy="48" data-copy-device="48&nbsp;Ricerca nei negozi" data-available="false" class="sizes-item-value-disabled" >
                 48&nbsp;
                 </option>

                 // 均码
                 <option value="808252057" data-copy="U" data-copy-device="U&nbsp;" data-available="true" class="" selected="selected">U</option>

                 </select>

                 */
                //判断当前包页面有没有尺码信息  有分成多个 product 没有 尺码为均码
                Elements temSizeElements = doc.select("select[name=size]");
                if(temSizeElements!=null&&temSizeElements.size()>0){
                    Elements sizeElements =temSizeElements.first().select("option");
                    /** 商品价格
                     * <input type="hidden" class="gucciProductPrice" value="1690.0">
                     */
                    //获取商品市场价
                    String itemPrice = "";
                    Element itemPriceElement  = doc.select("input.gucciProductPrice").first();
                    if(itemPriceElement!=null){
                        itemPrice = itemPriceElement.attr("value"); //款式sku
                    }else{
                        logger.error("获取商品价格异常=============productUrl:"+productUrl+"==============================");
                    }

                    sizeElement:  for (Element sizeElement:sizeElements ) {
                        //处理尺码信息
                        String pcode = sizeElement.attr("value");
                        String classValue = sizeElement.attr("class");
                        String sizeContent = sizeElement.attr("data-copy");
                        if (sizeContent != null && !"".equals(sizeContent)) {
                            if (pcode == null || "-1".equals(pcode) || "Taglia".equals(sizeContent)) {  //跳过尺码提示语选项
                                continue;
                            }
                            //处理特殊字符均码
                            if (sizeContent.startsWith("U") && sizeContent.length() == 2) {
                                sizeContent = "U";
                            }
                        } else {
                            continue;
                        }
                        // 校对 商品尺码信息
                        zhiCai:for (int i = 0; i < zhiCaiSkuResultListSize; i++) {
                            SkuDTO skuDTO = zhiCaiSkuResultList.get(i);
                            String spSkuNo = skuDTO.getSpSkuNo();
                            if(spSkuNo == null || "".equals(spSkuNo)){
                                continue zhiCai;
                            }
                            /**
                             * 校验价格信息
                             */
                            String marketPrice = skuDTO.getMarketPrice();
                            if(marketPrice!=null){
                                if(!marketPrice.equals(itemPrice)){ //价格发生改变
                                    updateSpSkuMarketPrice(skuDTO.getSupplierSkuNo(),itemPrice);
                                    logger.info("推送 价格成功："+ skuDTO.getSupplierSkuNo()+" 原价："+marketPrice+" 新价:"+itemPrice);
                                    System.out.println("推送 价格成功："+ skuDTO.getSupplierSkuNo()+" 原价："+marketPrice+" 新价:"+itemPrice);
                                }
                            }else{
                                loggerError.error("getMarketPrice 为空 ProductDTO:"+productDTO.toString());
                            }

                            String size = skuDTO.getSize();
                            if(sizeContent.equals(size)){
                                //如果<option> 标签上含有 class="sizes-item-value-disabled" 为无库存
                                if ("sizes-item-value-disabled".equals(classValue)) {
                                    exportSpSkunoAndQty(skuDTO.getSpSkuNo(),NO_STOCK);
                                } else {
                                    //获取库存数据
                                    //获取 pcode 参数value
                                    Map<String, String> checkProductInfo = getProductQtyInfo(skuDTO.getSpSkuNo(),pcode);
                                    String temQty = checkProductInfo.get("qty");
                                    if (temQty != null) {
                                        exportSpSkunoAndQty(skuDTO.getSpSkuNo(),temQty);
                                    } else {
                                        //获取库存失败 将商品 spSkuNO pcode 保存起来 处理
                                        exportSpSkunoAndQty(skuDTO.getSpSkuNo(),NO_STOCK);
                                        //加入到失败库存信息中
                                        failedSpSkuNoList.add(new SpSkuNoDTO(skuDTO.getSpSkuNo(),pcode));
                                    }
                                    /**
                                     * 获取需要请求的都休息 0.5s
                                     */
                                    //Thread.sleep(500);
                                }
                            }
                        }
                    }
                }else{
                    logger.error("===请求商品地址解析 商品尺码失败===========================================");
                    logger.error(productDTO.toString());
                    logger.error("===请求商品地址解析 商品尺码失败===========================================");
                    // 商品页面中没有获取到尺码信息重新请求
                    return false;
                }
            }else{
                logger.error("================请求商品地址失败===========================================");
                logger.error(productDTO.toString());
                logger.error("================请求商品地址失败===========================================");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
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
     * @param pcode 商品尺码 pcode 请求参数
     * @return
     */
    public static  Map<String,String> getProductQtyInfo(String spSkuNO, String pcode){
        //库存结果集
        HashMap<String, String> mapDate = new HashMap<>();
        //  /it/it/p/ajax/product-detail-shipping.ajax?pcode=808354516
        String checkProductQtyUrl = uri + "/it/it/p/ajax/product-detail-shipping.ajax?pcode="+pcode;

        try {
            getProductQtyUnit(checkProductQtyUrl,mapDate);
        } catch (Exception e) {
            logger.info("=========尝试再次获取=====");
            try {
                getProductQtyUnit(checkProductQtyUrl,mapDate);
            } catch (Exception ee) {
                logger.info("=========第二次：获取库存失败 ===spSkuNO: "+spSkuNO +"pcode:"+pcode);
                ee.printStackTrace();
                System.out.println();
                System.err.println("=========第二次：获取库存失败 ===spSkuNO: "+spSkuNO +"pcode:"+pcode);
            }
        }
        return mapDate;
    }

    /**
     * 获取商品库存信息
     * @param checkProductUrl
     * @throws Exception
     */
    public static void getProductQtyUnit(String checkProductUrl,Map<String,String> mapDate) throws Exception{
        Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36)");
        Header[] headers = new Header[1];
        headers[0] = header;
        HttpResponse checkResponse = HttpUtils.get(checkProductUrl,headers);
        if (checkResponse.getStatus()==200) {
            /**
             <p class="title">DISPONIBILE	</p>
             <div class="subtitle">Il prodotto è disponibile per la spedizione. La consegna è stimata in 1-2 giorni lavorativi.</div>
             */
            String responseHtml = checkResponse.getResponse();
            if(responseHtml==null||"".equals(responseHtml)){
                //没有库存
                mapDate.put("qty",NO_STOCK);
                mapDate.put("qtyDesc","售罄");
                return;
            }
            Document doc = Jsoup.parse(responseHtml);

            String qtyStr = doc.select("p.title").first().text();
            // DISPONIBILE SOLO PER PRE-ORDINE | DISPONIBILE  | DISPONIBILE IN 1-2 SETTIMANE

            if(qtyStr!=null){
                if(qtyStr.contains("PRE-ORDINE")){ //预售
                    mapDate.put("qty",NO_STOCK);
                    mapDate.put("qtyDesc","预售");
                }else if(qtyStr.contains("DISPONIBILE")){
                    mapDate.put("qty",IN_STOCK);
                    mapDate.put("qtyDesc","有货");
                }
            }else{
                // 售罄
                mapDate.put("qty",NO_STOCK);
                mapDate.put("qtyDesc","售罄");
            }
        }else{
            mapDate.put("qty",NO_STOCK);
            mapDate.put("qtyDesc","售罄");
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


    public static void main(String[] args) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductUrl("https://www.gucci.com/it/it/pr/men/mens-ready-to-wear/mens-t-shirts-polos/oversize-t-shirt-with-amour-eye-p-493117X3I319169?position=14&listName=VariationOverlay");
        List<SkuDTO> zhiCaiSkuResultList = new ArrayList<>();
        SkuDTO skuDTO = new SkuDTO();
        skuDTO.setSpSkuNo("30968589002");
        skuDTO.setSize("U");
        skuDTO.setSupplierSkuNo("493117 X3I31 9169-U");
        skuDTO.setMarketPrice("350.0");
        zhiCaiSkuResultList.add(skuDTO);
        productDTO.setZhiCaiSkuResultList(zhiCaiSkuResultList);
        solveProductQty(productDTO);

        //updateSpSkuMarketPrice("454070 A7M0T 5909-U","550");
    }

}
