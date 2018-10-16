package com.shangpin.iog.hermes.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.hermes.dto.*;
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
import org.springframework.stereotype.Component;

import java.io.File;
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
    private static String supplierId = "",supplierNo = "",supplierName = "",fetchSpProductInfosUrl ="",updateSpMarketPriceUrl="",pageSize="";

    private static OutputStreamWriter  out= null;
    static String splitSign = ",";
    //库存csv 文件存放目录
    private static String filePath="";

    //LV意大利官网地址
    private static String uri="";

    //有库存
    private static  String IN_STOCK = "10";
    //无库存
    private static final String NO_STOCK = "0";

    // 请求失败的尚品 skuNo 集合
    private static List<SkuDTO> failedSpSkuNoList = null;

    /**
     * 更新市场价格失败 集合
     */
    private static List<SkuDTO> failedUpdateItemPriceList = null;

    static {
        if (null == bdl){
            bdl = ResourceBundle.getBundle("conf");
        }
        supplierId = bdl.getString("supplierId");

        supplierNo = bdl.getString("supplierNo");

        supplierName = bdl.getString("supplierName");

        fetchSpProductInfosUrl = bdl.getString("fetchSpProductInfosUrl");

        updateSpMarketPriceUrl = bdl.getString("updateSpMarketPriceUrl");

        pageSize = bdl.getString("pageSize");

        filePath = bdl.getString("csvFilePath");

        uri = bdl.getString("uri");

        IN_STOCK = bdl.getString("IN_STOCK");

    }

    private static OutTimeConfig timeConfig = new OutTimeConfig(1000*60*30,1000*60*30,1000*60*30);

    /**
     * 拉取 意大利官网 商品库存数据
     */
    public void fetchItlyProductStock(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startDateTime = format.format(new Date());
        System.out.println("============拉取 farfetch 库存数据开始 "+startDateTime+"=========================");
        logger.info("==============拉取 farfetch 库存数据开始 "+startDateTime+"=========================");


        failedSpSkuNoList = new ArrayList<>();
        failedUpdateItemPriceList = new ArrayList<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = simpleDateFormat.format(new Date());

        String temFilePath = filePath + "lv-qty-"+todayStr+"-1.csv";

        File fristFile = new File(temFilePath);
        if(fristFile.exists()){
            temFilePath = filePath + "lv-qty-"+todayStr+"-2.csv";
        }


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

        //1. 请求需要更新库存商品 信息接口
        //定义所有需要的更新库存商品信息List
        List<ProductDTO> productDTOAllList =  new LinkedList<>();

        //获取第一页商品数据
        ShangPinPageContent prpductPageContent = getShangPinPageContentByParam(supplierId,supplierName, 1, Integer.parseInt(pageSize));
        if(prpductPageContent == null) return;

        productDTOAllList.addAll(prpductPageContent.getZhiCaiResultList());

        //总记录数
        Integer total = prpductPageContent.getTotal();
        //当商品数据
        if(total>Integer.parseInt(pageSize)){
            Integer pageNumber = getPageNumber(total, 20);
            for (int i = 2; i <= pageNumber; i++) {
                ShangPinPageContent temprpductPageContent = getShangPinPageContentByParam(supplierId,supplierName, i, Integer.parseInt(pageSize));
                if(temprpductPageContent!=null){
                    productDTOAllList.addAll(temprpductPageContent.getZhiCaiResultList());
                }else{ //请求失败重新 再次请求
                    temprpductPageContent = getShangPinPageContentByParam(supplierId,supplierName, i, Integer.parseInt(pageSize));
                    if(temprpductPageContent!=null){
                        productDTOAllList.addAll(temprpductPageContent.getZhiCaiResultList());
                    }
                }
            }
        }

        logger.info("=====需要更新 lvPageContent spProduct Size:"+productDTOAllList.size());
        System.out.println("=====需要更新 lvPageContent spProduct Size:"+productDTOAllList.size());
        //导出尚品库存数据
        exportQtyInfoForProductList(productDTOAllList);

        // 处理 请求失败的 尚品 skuNo 信息
        int failedSpSkuNoSize = failedSpSkuNoList.size();
        for (int i = 0; i < failedSpSkuNoSize; i++) {
            repeatSolveFailedSpSkuNo(failedSpSkuNoList.get(i));
        }
        int failedUpdateItemPriceListSize = failedUpdateItemPriceList.size();
        for (int i = 0; i <failedUpdateItemPriceListSize ; i++) {
            repeatSolveFailedSpItemPrice(failedUpdateItemPriceList.get(i));
        }

        String endtDateTime = format.format(new Date());
        logger.info("===================拉取LV库存数据结束 "+endtDateTime+"=========================");
        System.out.println("=================拉取LV库存数据结束 "+endtDateTime+"=========================");
    }

    /**
     * 获取商品页数
     * @param total 总记录数
     * @param pageSize 每页显示数
     * @return 页数
     */
    public  Integer getPageNumber(Integer total,Integer pageSize){
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
    public  ShangPinPageContent getShangPinPageContentByParam(String supplierId,String brandName,Integer pageIndex,Integer pageSize){
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
            System.out.println("=======resultJsonStr:"+resultJsonStr);
            logger.info("=======resultJsonStr:"+resultJsonStr);
            JSONObject resultJsonObject = JSONObject.fromObject(resultJsonStr);
            Map<String,Class> keyMapConfig= new HashMap<>();
            keyMapConfig.put("zhiCaiResultList",ProductDTO.class);
            keyMapConfig.put("zhiCaiSkuResultList",SkuDTO.class);
            keyMapConfig.put("content",ShangPinPageContent.class);
            ApiResponseBody apiResponseBody = (ApiResponseBody) JSONObject.toBean(resultJsonObject, ApiResponseBody.class, keyMapConfig);

            if(apiResponseBody!=null){
                shangPinPageContent = (ShangPinPageContent) apiResponseBody.getContent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shangPinPageContent;
    }

    /**
     * 循环遍历 拉取商品库存信息
     * @param productDTOAllList 商品信息list
     */
    public  void exportQtyInfoForProductList(List<ProductDTO> productDTOAllList){
        for (ProductDTO productDTO:productDTOAllList) {
            solveProductQty(productDTO);
        }
    }


    /**
     * 处理单个商品库存信息
     * @param productDTO 商品信息
     */
    private  void solveProductQty(ProductDTO productDTO) {
        List<SkuDTO> zhiCaiSkuResultList = productDTO.getZhiCaiSkuResultList();
        int zhiCaiSkuResultListSize = zhiCaiSkuResultList.size();

        String productUrl = productDTO.getProductUrl();
        // 校对 商品尺码信息
        for (int i = 0; i < zhiCaiSkuResultListSize; i++) {
            SkuDTO skuDTO = zhiCaiSkuResultList.get(i);
            try {
                skuDTO.setProductUrl(productUrl);
                soloveQtyAndItemPriceBySkuDTO(skuDTO);
            } catch (Exception e1) {
                e1.printStackTrace();
                loggerError.error(e1.getMessage());
                // 加入到失败库存信息中 获取库存失败 将商品 spSkuNO 供应商skuNo 保存起来 重新请求处理
                skuDTO.setProductUrl(productUrl);
                failedSpSkuNoList.add(skuDTO);
            }
        }

        //每一款商品休息5s
        try {
            //Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 重复处理 spSkuNO qty 信息
     * @param skuDTO
     */
    public  void repeatSolveFailedSpSkuNo(SkuDTO skuDTO){
        int count = 0;

        while(count<4){
            count++;
            try {
                soloveQtyAndItemPriceBySkuDTO(skuDTO);
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //重复请求 失败 做商品 库存置零处理
        exportSpSkunoAndQty(skuDTO.getSpSkuNo(),NO_STOCK);
    }


    /**
     * 重复处理 spSkuNO qty 信息
     * @param skuDTO
     */
    public void repeatSolveFailedSpItemPrice(SkuDTO skuDTO){
        int count = 0;
        while(count<4){
            count++;
            try {
                updateSpSkuMarketPrice(skuDTO.getSupplierSkuNo(),skuDTO.getNewMarketPrice());
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //重复请求 失败 做商品 库存置零处理
        exportSpSkunoAndQty(skuDTO.getSpSkuNo(),NO_STOCK);
    }


    /**
     * 处理商品sku
     * @param skuDTO
     */
    private void soloveQtyAndItemPriceBySkuDTO(SkuDTO skuDTO) throws Exception {
        String productUrl = skuDTO.getProductUrl();
        if(skuDTO==null||skuDTO.getSupplierSkuNo()==null||"".equals(skuDTO.getSupplierSkuNo())){
            return;
        }

        String supplierSkuNo = skuDTO.getSupplierSkuNo();

        String supplierOrginalSkuNo = "";
        if(supplierSkuNo.contains("-")){
            supplierOrginalSkuNo = supplierSkuNo.split("-")[0];
        }else{
            supplierOrginalSkuNo = supplierSkuNo;
        }

        //获取该 sku 的价格 库存信息
        Map<String, String> productInfoMap = getProductSkuPrivateInfo(supplierOrginalSkuNo);
        String itemPrice = productInfoMap.get("itemPrice");
        /**
         * 校验价格信息 推送价格不影响 库存数据的处理
         */
        String marketPrice = skuDTO.getMarketPrice();
        if(marketPrice!=null){
            float itemPriceFloat = Float.parseFloat(itemPrice);
            float marketPriceFloat = Float.parseFloat(marketPrice);

            if(itemPriceFloat!=marketPriceFloat){ //价格发生改变
                try {
                    logger.info("开始推送价格："+ skuDTO.getSupplierSkuNo()+" 原价："+marketPrice+" 新价:"+itemPrice);
                    System.out.println("开始推送价格："+ skuDTO.getSupplierSkuNo()+" 原价："+marketPrice+" 新价:"+itemPrice);
                    updateSpSkuMarketPrice(skuDTO.getSupplierSkuNo(),itemPrice);
                    logger.info("推送 价格成功："+ skuDTO.getSupplierSkuNo()+" 原价："+marketPrice+" 新价:"+itemPrice);
                    System.out.println("推送 价格成功："+ skuDTO.getSupplierSkuNo()+" 原价："+marketPrice+" 新价:"+itemPrice);
                } catch (Exception e) {
                    loggerError.error("推送 价格失败："+ skuDTO.getSupplierSkuNo()+" 原价："+marketPrice+" 新价:"+itemPrice+" url:"+productUrl);
                    System.out.println("推送 价格失败："+ skuDTO.getSupplierSkuNo()+" 原价："+marketPrice+" 新价:"+itemPrice +" url:"+productUrl);

                    skuDTO.setNewMarketPrice(marketPrice);
                    failedUpdateItemPriceList.add(skuDTO);
                }
            }
        }else{
            loggerError.error("【获取 hub 市场价失败 skuDTO:"+skuDTO.toString()+"】");
        }
        /**
         *  推送库存数据
         */
        String temQty = productInfoMap.get("qty");
        if (temQty != null) {
            exportSpSkunoAndQty(skuDTO.getSpSkuNo(),temQty);
        }
    }


    /**
     * 获取某一尺码 skuNO 的 库存 库存描述 价格 图片
     * @param skuNo
     * @throws Exception
     */
    public Map<String,String> getProductSkuPrivateInfo(String skuNo) throws Exception{
        Map<String,String> resultMap = new HashMap<>();
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
            //获取价格数据
            /**
             商品市场价格
             */
            String itemPrice  = "";
            Element itemPriceElement  = doc.select("#infoProductBlockTop").select("table.priceButton").select("td.priceValue").first();
            if(itemPriceElement!=null){
                itemPrice = itemPriceElement.text().replace("€","").replace(".","").replace(",",".").trim();
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
            resultMap.put("itemPrice",itemPrice);
        }
        return resultMap;
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
                    resultMap.put("qty",NO_STOCK); // 加入到失败 商品skuNo
                    resultMap.put("qtyDesc","售罄");
                }
            }else{
                resultMap.put("qty",NO_STOCK); // 加入到失败 商品skuNo
                resultMap.put("qtyDesc","售罄");
            }
        }else{
            System.out.println("请求库存数据接口失败"); // 加入到失败 商品skuNo
            resultMap.put("qty",NO_STOCK);
            resultMap.put("qtyDesc","售罄");
        }
        return resultMap;
    }


    /**
     * 更新尚品 spSkuMarketPrice
     * @param supplierSkuNo
     * @param marketPrice
     */
    private static void updateSpSkuMarketPrice(String supplierSkuNo, String marketPrice) throws ServiceException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("supplierId",supplierId);
        jsonObject.put("supplierNo",supplierNo);
        jsonObject.put("supplierSkuNo",supplierSkuNo);
        jsonObject.put("marketPrice",marketPrice);
        String jsonStr = jsonObject.toString();

        String resultJsonStr = HttpUtil45.operateData("post","json",updateSpMarketPriceUrl,timeConfig,null,jsonStr,null,null);
        JSONObject resultJsonObject = JSONObject.fromObject(resultJsonStr);
        Map<String,Class> keyMapConfig= new HashMap<>();
        keyMapConfig.put("content",String.class);
        ApiResponseBody apiResponseBody = (ApiResponseBody) JSONObject.toBean(resultJsonObject, ApiResponseBody.class, keyMapConfig);
        String code = apiResponseBody.getCode();
        System.out.println("更新updateSpMarketPrice resultJsonStr:"+resultJsonStr);
        logger.info("更新updateSpMarketPrice resultJsonStr:"+resultJsonStr);
        if("0".equals(code)){
            logger.info("=============更新updateSpSkuMarketPrice成功===============");
            System.out.println("=============更新updateSpSkuMarketPrice成功===============");
        }else{
            loggerError.error("=============更新updateSpSkuMarketPrice 失败===============");
            System.err.println("=============更新updateSpSkuMarketPrice成功===============");
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
            System.out.println("spSkuNo:"+spSkuNo+" qty:"+qty+"|");
            logger.info("spSkuNo:"+spSkuNo+" qty:"+qty+"|");
            //logger.info(buffer.toString());
            out.flush();
        } catch (Exception e) {
        }
    }


    public static void main(String[] args) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductUrl("https://www.gucci.com/it/it/pr/gifts/gifts-for-women/rhyton-glitter-gucci-leather-sneaker-p-524990DRW009022?position=1&listName=VariationOverlay");
        List<SkuDTO> zhiCaiSkuResultList = new ArrayList<>();
        SkuDTO skuDTO = new SkuDTO();
        skuDTO.setSpSkuNo("30970081005");
        skuDTO.setSize("37");
        skuDTO.setSupplierSkuNo("37");
        zhiCaiSkuResultList.add(skuDTO);
        productDTO.setZhiCaiSkuResultList(zhiCaiSkuResultList);

    }

}
