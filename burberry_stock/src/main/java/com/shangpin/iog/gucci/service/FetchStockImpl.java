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

    //BURBERRY官网地址
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
     * 拉取 BURBERRY网 商品库存数据
     */
    public void fetchItlyProductStock(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startDateTime = format.format(new Date());
        System.out.println("============拉取BURBERRY库存数据开始 "+startDateTime+"=========================");
        logger.info("==============拉取BURBERRY库存数据开始 "+startDateTime+"=========================");

        //1. 请求需要更新库存商品 信息接口
        failedSpSkuNoList = new ArrayList<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = simpleDateFormat.format(new Date());

        String temFilePath = filePath + "burberry-qty-"+todayStr+".csv";
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
        ShangPinPageContent gucciPageContent = getShangPinPageContentByParam(supplierId,"BURBERRY", 1, Integer.parseInt(pageSize));
        productDTOAllList.addAll(gucciPageContent.getZhiCaiResultList());

        if(gucciPageContent == null) return;
        //总记录数
        Integer total = gucciPageContent.getTotal();
        Integer pageNumber = getPageNumber(total, 20);
        for (int i = 2; i <= pageNumber; i++) {
            ShangPinPageContent temgucciPageContent = getShangPinPageContentByParam(supplierId,"BURBERRY", i, Integer.parseInt(pageSize));
            if(temgucciPageContent!=null){
                productDTOAllList.addAll(temgucciPageContent.getZhiCaiResultList());
            }else{ //请求失败重新 再次请求
                temgucciPageContent = getShangPinPageContentByParam(supplierId,"BURBERRY", i, Integer.parseInt(pageSize));
                if(temgucciPageContent!=null){
                    productDTOAllList.addAll(temgucciPageContent.getZhiCaiResultList());
                }
            }
        }

        logger.info("=====需要更新BURBERRY spProduct Size:"+productDTOAllList.size());
        System.out.println("=====需要更新BURBERRY spProduct Size:"+productDTOAllList.size());
        //导出尚品库存数据
        exportQtyInfoForProductList(productDTOAllList);

        // 处理 请求失败的 尚品 skuNo 信息
        ProductDTO productDTO = new ProductDTO();
        int failedSpSkuNoSize = failedSpSkuNoList.size();
        for (int i = 0; i < failedSpSkuNoSize; i++) {
            repeatSolveFailedSpSkuNo(failedSpSkuNoList.get(i),productDTO);
        }

        String endtDateTime = format.format(new Date());
        logger.info("===================拉取BURBERRY库存数据结束 "+endtDateTime+"=========================");
        System.out.println("=================拉取BURBERRY库存数据结束 "+endtDateTime+"=========================");

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
    public static void repeatSolveFailedSpSkuNo(SpSkuNoDTO spSkuNoDTO,ProductDTO productDTO){
        int count = 0;
        while(count>4){
            count++;
            Map<String, String> checkProductInfo = getProductQtyInfo(spSkuNoDTO.getSpSkuNo(),productDTO);
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
        logger.info("货号："+productDTO.getSupplierSpuModel()+"；sku的数量："+zhiCaiSkuResultListSize);
        /*SkuDTO skuDTO = new SkuDTO();
        Map<String,SkuDTO> skuMap = new HashMap<>();
        zhiCai:for (int i = 0; i < zhiCaiSkuResultListSize; i++) {
            skuDTO = zhiCaiSkuResultList.get(i);
            String spSkuNo = skuDTO.getSpSkuNo();
            if (spSkuNo == null || "".equals(spSkuNo)) {
                continue zhiCai;
            }
            skuMap.put(skuDTO.getSize(),skuDTO);

        }*/


        try {
            Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
            Header[] headers = new Header[1];
            headers[0] = header;

            HttpResponse response = HttpUtils.get(productUrl,headers);

            if (response.getStatus()==200) {
                String htmlContent = response.getResponse();
                Document doc = Jsoup.parse(htmlContent);


                SkuDTO skuDTO = new SkuDTO();

                zhiCai:for (int i = 0; i < zhiCaiSkuResultListSize; i++) {
                    skuDTO = zhiCaiSkuResultList.get(i);
                    String spSkuNo = skuDTO.getSpSkuNo();
                    if (spSkuNo == null || "".equals(spSkuNo)) {
                        continue zhiCai;
                    }

                    Elements temSizeElements = doc.select("#size-picker-content").select("input");
                    Elements temSizeElement2 = doc.select("#transactional-panel-collapsible-data-content").select("div.delivery-message").select("p");
                    if (temSizeElements != null && temSizeElements.size() > 0) {
                        System.out.println(temSizeElements);
                        for (Element sizeElement : temSizeElements) {
                            String sizeContent = sizeElement.attr("data-text-value");
                            String disabled = sizeElement.attr("disabled");
                            System.out.println("尺码：" + sizeContent + ";" + "库存标记：" + disabled);


                            String size = skuDTO.getSize();
                            System.out.println("本地库存尺码：" + size);
                            if (sizeContent.equals(size)) {
                                //如果<option> 标签上含有disabled" 为无库存
                                if ("disabled".equals(disabled)) {
                                    exportSpSkunoAndQty(skuDTO.getSpSkuNo(), NO_STOCK);
                                } else if ("".equals(disabled)) {
                                    //获取库存数据
                                    exportSpSkunoAndQty(skuDTO.getSpSkuNo(), IN_STOCK);
                                } else {

                                    Map<String, String> checkProductInfo = getProductQtyInfo(skuDTO.getSpSkuNo(), productDTO);
                                    String temQty = checkProductInfo.get("qty");
                                    if (temQty != null) {
                                        exportSpSkunoAndQty(skuDTO.getSpSkuNo(), temQty);
                                    } else {
                                        //获取库存失败 将商品 spSkuNO pcode 保存起来 处理
                                        exportSpSkunoAndQty(skuDTO.getSpSkuNo(), NO_STOCK);
                                        //加入到失败库存信息中
                                        failedSpSkuNoList.add(new SpSkuNoDTO(skuDTO.getSpSkuNo(), "-1"));
                                    }

                                }
                                //Thread.sleep(500);
                            }
                        }
                    } else if (temSizeElement2 != null && !"".equals(temSizeElement2)) {
                        String text = temSizeElement2.text();
                        System.out.println("库存描述：" + text);
                        if (text.contains("免费次日达") || text.contains("免费标准送货服务")) {
                            exportSpSkunoAndQty(skuDTO.getSpSkuNo(), IN_STOCK);
                        } else {
                            //获取 pcode 参数value
                            Map<String, String> checkProductInfo = getProductQtyInfo(skuDTO.getSpSkuNo(), productDTO);
                            String temQty = checkProductInfo.get("qty");
                            if (temQty != null) {
                                exportSpSkunoAndQty(skuDTO.getSpSkuNo(), temQty);
                            } else {
                                //获取库存失败 将商品 spSkuNO pcode 保存起来 处理
                                exportSpSkunoAndQty(skuDTO.getSpSkuNo(), NO_STOCK);
                                //加入到失败库存信息中
                                failedSpSkuNoList.add(new SpSkuNoDTO(skuDTO.getSpSkuNo(), "-1"));
                            }

                        }
                    }

                    Elements itemPriceElement = doc.select("#product-transactional-panel").select("div.price-container").select("span");
                    if (itemPriceElement != null) {
                        String price = itemPriceElement.attr("data-monogrammedTotal");
                        float priceF = Float.parseFloat(price);
                        System.out.println("官网价格" + price);
                        String marketPrice = skuDTO.getMarketPrice();
                        System.out.println("本地出售价格：" + marketPrice);
                        if (marketPrice != null) {
                            float marketPriceF = Float.parseFloat(marketPrice);

                            if (marketPriceF != priceF) { //价格发生改变
                                updateSpSkuMarketPrice(skuDTO.getSupplierSkuNo(), price);
                                logger.info("推送 价格成功：" + skuDTO.getSupplierSkuNo() + " 原价：" + marketPrice + " 新价:" + price);
                                System.out.println("推送 价格成功：" + skuDTO.getSupplierSkuNo() + " 原价：" + marketPrice + " 新价:" + price);
                            }
                        } else {
                            loggerError.error("getMarketPrice 为空 ProductDTO:" + productDTO.toString());
                        }

                    } else {
                        logger.error("获取商品价格异常=============productUrl:" + productUrl + "==============================");

                    }
                }
            }else{
                logger.error("================请求商品地址失败===========================================");
                logger.error(productDTO.toString());
                logger.error("================请求商品地址失败===========================================");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
     * @param productDTO 商品尺码 pcode 请求参数
     * @return
     */
    public static  Map<String,String> getProductQtyInfo(String spSkuNO,ProductDTO productDTO){
        //库存结果集
        HashMap<String, String> mapDate = new HashMap<>();
        //  /it/it/p/ajax/product-detail-shipping.ajax?pcode=808354516


        try {
            getProductQtyUnit(productDTO,mapDate);
        } catch (Exception e) {
            logger.info("=========尝试再次获取=====");
            try {
                getProductQtyUnit(productDTO,mapDate);
            } catch (Exception ee) {
                logger.info("=========第二次：获取库存失败 ===spSkuNO: "+spSkuNO );
                ee.printStackTrace();
                System.out.println();
                System.err.println("=========第二次：获取库存失败 ===spSkuNO: "+spSkuNO );
            }
        }
        return mapDate;
    }


    /**
     * 获取商品库存信息
     * @param productDTO
     * @throws Exception
     */
    public static void getProductQtyUnit(ProductDTO productDTO,Map<String,String> mapDate) throws Exception{

        List<SkuDTO> zhiCaiSkuResultList = productDTO.getZhiCaiSkuResultList();
        int zhiCaiSkuResultListSize = zhiCaiSkuResultList.size();
        Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
        Header[] headers = new Header[1];
        headers[0] = header;

        HttpResponse response = HttpUtils.get(productDTO.getProductUrl(),headers);

        if (response.getStatus()==200) {
            String htmlContent = response.getResponse();
            Document doc = Jsoup.parse(htmlContent);



            SkuDTO skuDTO = new SkuDTO();
            zhiCai:for (int i = 0; i < zhiCaiSkuResultListSize; i++) {
                skuDTO = zhiCaiSkuResultList.get(i);
                String spSkuNo = skuDTO.getSpSkuNo();
                if (spSkuNo == null || "".equals(spSkuNo)) {
                    continue zhiCai;
                }

            }

            Elements temSizeElements = doc.select("#size-picker-content").select("input");
            Elements temSizeElement2 = doc.select("#transactional-panel-collapsible-data-content").select("div.delivery-message").select("p");
            if(temSizeElements!=null&&temSizeElements.size()>0) {
                System.out.println(temSizeElements);
                for (Element sizeElement : temSizeElements) {
                    String sizeContent = sizeElement.attr("data-text-value");
                    String disabled = sizeElement.attr("disabled");
                    System.out.println("尺码：" + sizeContent + ";" + "库存标记：" + disabled);

                    String size = skuDTO.getSize();
                    if(sizeContent.equals(size)) {
                        //如果<option> 标签上含有disabled" 为无库存
                        if ("disabled".equals(disabled)) {
                            mapDate.put("qty",NO_STOCK);
                            mapDate.put("qtyDesc","售罄");
                            return;
                        }else{
                            mapDate.put("qty",IN_STOCK);
                            mapDate.put("qtyDesc","有货");
                        }
                        //Thread.sleep(500);
                    }
                }
            }else if(temSizeElement2!=null && !"".equals(temSizeElement2)){
                String text = temSizeElement2.text();
                if(text.contains("免费次日达")||text.contains("免费标准送货服务")){
                    mapDate.put("qty",IN_STOCK);
                    mapDate.put("qtyDesc","有货");
                }else{
                    mapDate.put("qty",NO_STOCK);
                    mapDate.put("qtyDesc","售罄");
                }
            }else{
                mapDate.put("qty",NO_STOCK);
                mapDate.put("qtyDesc","售罄");
            }

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
        productDTO.setProductUrl("https://uk.burberry.com/vintage-check-reversible-puffer-jacket-p80037841/?locale=zh_GB");
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
