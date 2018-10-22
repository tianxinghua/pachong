package com.shangpin.iog.balenciga.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.balenciga.dto.*;
import com.shangpin.iog.spider.BalenciagaProcessor;
import com.shangpin.iog.spider.GoodsEntity;
import com.shangpin.iog.spider.SetGoods;
import com.shangpin.iog.utils.HttpUtil45;
import com.shangpin.openapi.api.sdk.client.OutTimeConfig;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wanner on 2018/8/8
 * 爬虫 balenciga 库存拉取（保存到csv文件） ，价格变动推送
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

    //有库存
    private static final String IN_STOCK = "1";
    //无库存
    private static final String NO_STOCK = "0";

    /**
     * 请求失败的尚品 ProductDTO 集合
     */
    private static List<ProductDTO> failedProductDTOList = null;

    /**
     * 请求失败的 商品集合
     */
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

    }

    private static OutTimeConfig timeConfig = new OutTimeConfig(1000*60*30,1000*60*30,1000*60*30);

    /**
     * 拉取 hub 需要更新 库存和价格 的hermes商品库存数据
     */
    public void fetchItlyProductStock(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startDateTime = format.format(new Date());
        System.out.println("============拉取库存数据开始 "+startDateTime+"=========================");
        logger.info("==============拉取库存数据开始 "+startDateTime+"=========================");

        failedProductDTOList = new ArrayList<>();
        failedSpSkuNoList = new ArrayList<>();
        failedUpdateItemPriceList = new ArrayList<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = simpleDateFormat.format(new Date());

        String temFilePath = filePath + "balenciga-qty-"+todayStr+"-1.csv";
        File fristFile = new File(temFilePath);

        if(fristFile.exists()){
            temFilePath = filePath + "balenciga-qty-"+todayStr+"-2.csv";
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
            for (int i = 17; i <= pageNumber; i++) {
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

        logger.info("=====需要更新 balencigaPageContent spProduct Size:"+productDTOAllList.size());
        System.out.println("=====需要更新 balencigaPageContent spProduct Size:"+productDTOAllList.size());
        //导出尚品库存数据
        exportQtyInfoForProductList(productDTOAllList);

        // 处理 请求失败的 尚品 productDTO 信息
        int failedProductDTOSize = failedProductDTOList.size();
        for (int i = 0; i < failedProductDTOSize; i++) {
            repeatSolveFailedProductDTOList(failedProductDTOList.get(i));
        }

        // 处理 请求失败的 尚品 skuNo 信息
        int failedSpSkuNoSize = failedSpSkuNoList.size();
        for (int i = 0; i < failedSpSkuNoSize; i++) {
            repeatSolveFailedSpSkuNo(failedSpSkuNoList.get(i));
        }

        // 处理 推送失败的 尚品 市场价格 信息
        int failedUpdateItemPriceListSize = failedUpdateItemPriceList.size();
        for (int i = 0; i <failedUpdateItemPriceListSize ; i++) {
            repeatSolveFailedSpItemPrice(failedUpdateItemPriceList.get(i));
        }

        String endtDateTime = format.format(new Date());
        logger.info("===================拉取库存数据结束 "+endtDateTime+"=========================");
        System.out.println("=================拉取库存数据结束 "+endtDateTime+"=========================");
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
        //jsonObject.put("brandName",brandName);
        jsonObject.put("pageIndex",pageIndex);
        jsonObject.put("pageSize",pageSize);
        jsonObject.put("channel","www.balenciaga.com");

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
            solveProductQtyAndItemPrice(productDTO);
        }
    }


    /**
     * 处理单个商品库存信息
     * @param productDTO 商品信息
     */
    private  void solveProductQtyAndItemPrice(ProductDTO productDTO) {

        if(productDTO==null||productDTO.getZhiCaiSkuResultList()==null){
            return;
        }
        String productUrl = productDTO.getProductUrl();
        List<SkuDTO> skuDTOs = productDTO.getZhiCaiSkuResultList();
        try {
            BalenciagaProcessor balenciga = new BalenciagaProcessor();
            List<GoodsEntity> goodsList = balenciga.updateInventorySpider(productUrl);
            for (int i = 0; i < skuDTOs.size(); i++) {
                SkuDTO skuDTO = skuDTOs.get(i);
                String size = skuDTO.getSize();
                double marketPrice = Double.parseDouble(skuDTO.getMarketPrice());
                for (int j = 0; j < goodsList.size(); j++) {
                    double foreignPrice = Double.parseDouble(goodsList.get(j).getForeignPrice());
                    //判断尺码一致
                    if (size.equals(goodsList.get(j).getSize())) {
                        if (marketPrice != foreignPrice) {
                            //更新价格和库存
                            updateSpSkuMarketPrice(skuDTO.getSupplierSkuNo(),String.valueOf(foreignPrice));
                            logger.info("推送 价格成功："+ skuDTO.getSupplierSkuNo()+" 原价："+marketPrice+" 新价:"+foreignPrice);
                            System.out.println("推送 价格成功："+ skuDTO.getSupplierSkuNo()+" 原价："+marketPrice+" 新价:"+foreignPrice);
                        }
                        String qty = goodsList.get(j).getQty();
                        if (qty != null) {
                            exportSpSkunoAndQty(skuDTO.getSpSkuNo(),qty);
                        }
                    }
                }
            }

        }catch (Exception e){
            loggerError.error("--地址出错 productrDetailUrl"+productUrl+ "--------"+e.getMessage());
            System.out.println("--地址出错 productrDetailUrl"+productUrl);
            e.printStackTrace();
            failedProductDTOList.add(productDTO);
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
        String qtyUrl = "https://www.balenciaga.com/apps/ecom/stock";

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
                    resultMap.put("qty",IN_STOCK);
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
        jsonObject.put("channel","www.balenciaga.com");
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
    private  void exportSpSkunoAndQty(String spSkuNo, String qty) {
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


    /**
     * 重复处理 productDTO qty 以及价格 信息
     * @param productDTO
     */
    public  void repeatSolveFailedProductDTOList(ProductDTO productDTO){
        int count = 0;

        while(count<4){
            count++;
            try {
                solveProductQtyAndItemPrice(productDTO);
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //重复请求 失败 做商品 库存置零处理
        List<SkuDTO> zhiCaiSkuResultList = productDTO.getZhiCaiSkuResultList();
        for (SkuDTO skuDTO: zhiCaiSkuResultList) {
            exportSpSkunoAndQty(skuDTO.getSpSkuNo(),NO_STOCK);
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
                String supplierSkuNo = skuDTO.getSupplierSkuNo();
                Map<String, String> productQty = getProductQty(supplierSkuNo);
                String qty  = productQty.get("qty");
                exportSpSkunoAndQty(skuDTO.getSpSkuNo(),qty);
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
