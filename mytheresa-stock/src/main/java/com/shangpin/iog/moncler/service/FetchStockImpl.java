package com.shangpin.iog.moncler.service;


import com.shangpin.iog.moncler.dto.*;
import com.shangpin.iog.utils.HttpResponse;
import com.shangpin.iog.utils.HttpUtil45;
import com.shangpin.iog.utils.HttpUtils;
import com.shangpin.openapi.api.sdk.client.OutTimeConfig;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.Header;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component("fetchStockImpl")
public class FetchStockImpl
{
  private static Logger logger = Logger.getLogger("info");
  private static Logger loggerError = Logger.getLogger("error");
  private static ResourceBundle bdl = null;
  private static OutputStreamWriter out = null;
  static String splitSign = ",";
  private static List<SpSkuNoDTO> failedSpSkuNoList = null;
  
  static
  {
    if (null == bdl) {
      bdl = ResourceBundle.getBundle("conf");
    }
  }
  
  private static String supplierId = bdl.getString("supplierId");
  private static String supplierNo = bdl.getString("supplierNo");
  private static String fetchSpProductInfosUrl = bdl.getString("fetchSpProductInfosUrl");
  private static String updateSpMarketPriceUrl = bdl.getString("updateSpMarketPriceUrl");
  private static String pageSize = bdl.getString("pageSize");
  private static String filePath = bdl.getString("csvFilePath");
  private static String uri = bdl.getString("uri");
  private static OutTimeConfig timeConfig = new OutTimeConfig(1800000, 1800000, 1800000);
  private static String IN_STOCK=bdl.getString("IN_STOCK") ;
  private static String NO_STOCK =bdl.getString("NO_STOCK");
  
  public void fetchItlyProductStock()
  {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String startDateTime = format.format(new Date());
    System.out.println("============拉取mytheresa库存信息开始 " + startDateTime + "=========================");
    logger.info("==============拉取mytheresa库存信息开始" + startDateTime + "=========================");
    
    failedSpSkuNoList = new ArrayList();
    
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String todayStr = simpleDateFormat.format(new Date());
    
    String temFilePath = filePath + "mytheresa-qty-" + todayStr + ".csv";
    System.out.println("文件保存目录" + temFilePath);
    logger.info("文件保存目录" + temFilePath);
    try
    {
      out = new OutputStreamWriter(new FileOutputStream(temFilePath, true), "gb2312");
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    StringBuffer buffer = new StringBuffer("spSkuNO" + splitSign + "qty" + splitSign).append("\r\n");
    try
    {
      out.write(buffer.toString());
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    List<ProductDTO> productDTOAllList = new LinkedList();
    
    ShangPinPageContent monclerPageContent = getShangPinPageContentByParam(supplierId, "", "www.mytheresa.com", Integer.valueOf(1), Integer.valueOf(Integer.parseInt(pageSize)));
    
    productDTOAllList.addAll(monclerPageContent.getZhiCaiResultList());
    if (monclerPageContent == null) {
      return;
    }
    Integer total = monclerPageContent.getTotal();
    Integer pageNumber = getPageNumber(total, Integer.valueOf(20));
    for (int i = 2; i <= pageNumber.intValue(); i++)
    {
      ShangPinPageContent temmonclerPageContent = getShangPinPageContentByParam(supplierId, "", "www.mytheresa.com", Integer.valueOf(i), Integer.valueOf(Integer.parseInt(pageSize)));
      List<ProductDTO> zhiCaiResultList = temmonclerPageContent.getZhiCaiResultList();
      ProductDTO localProductDTO;
      for (Iterator localIterator = zhiCaiResultList.iterator(); localIterator.hasNext(); localProductDTO = (ProductDTO)localIterator.next()) {}
      if (temmonclerPageContent != null)
      {
        productDTOAllList.addAll(temmonclerPageContent.getZhiCaiResultList());
      }
      else
      {
        temmonclerPageContent = getShangPinPageContentByParam(supplierId, "", "www.mytheresa.com", Integer.valueOf(i), Integer.valueOf(Integer.parseInt(pageSize)));
        if (temmonclerPageContent != null) {
          productDTOAllList.addAll(temmonclerPageContent.getZhiCaiResultList());
        }
      }
    }
    logger.info("=====需要更新mytheresa spProduct Size:" + productDTOAllList.size());
    System.out.println("=====需要更新mytheresa spProduct Size:" + productDTOAllList.size());
    
    exportQtyInfoForProductList(productDTOAllList);
    
    int failedSpSkuNoSize = failedSpSkuNoList.size();
    for (int i = 0; i < failedSpSkuNoSize; i++) {
      repeatSolveFailedSpSkuNo((SpSkuNoDTO)failedSpSkuNoList.get(i));
    }
    String endtDateTime = format.format(new Date());
    logger.info("===================拉取mytheresa库存信息结束 " + endtDateTime + "=========================");
    System.out.println("=================拉取mytheresa库存信息结束 " + endtDateTime + "=========================");
  }
  
  public static Integer getPageNumber(Integer total, Integer pageSize)
  {
    Integer pageNumner = Integer.valueOf(total.intValue() / pageSize.intValue());
    Integer localInteger1;
    if (total.intValue() % pageSize.intValue() > 0)
    {
      localInteger1 = pageNumner;Integer localInteger2 = pageNumner = Integer.valueOf(pageNumner.intValue() + 1);
    }
    return pageNumner;
  }
  
  public static ShangPinPageContent getShangPinPageContentByParam(String supplierId, String brandName, String channel, Integer pageIndex, Integer pageSize)
  {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("supplierId", supplierId);
    jsonObject.put("brandName", brandName);
    jsonObject.put("pageIndex", pageIndex);
    jsonObject.put("pageSize", pageSize);
    jsonObject.put("channel", channel);
    String jsonStr = jsonObject.toString();
    
    ShangPinPageContent shangPinPageContent = null;
    try
    {
      String resultJsonStr = HttpUtil45.operateData("post", "json", fetchSpProductInfosUrl, timeConfig, null, jsonStr, null, null);
      
      JSONObject resultJsonObject = JSONObject.fromObject(resultJsonStr);
      Map<String, Class> keyMapConfig = new HashMap();
      keyMapConfig.put("zhiCaiResultList", ProductDTO.class);
      keyMapConfig.put("zhiCaiSkuResultList", SkuDTO.class);
      keyMapConfig.put("content", ShangPinPageContent.class);
      ApiResponseBody apiResponseBody = (ApiResponseBody)JSONObject.toBean(resultJsonObject, ApiResponseBody.class, keyMapConfig);
      if (apiResponseBody != null) {
        shangPinPageContent = (ShangPinPageContent)apiResponseBody.getContent();
      }
      System.out.println();
      System.out.println("获取第 " + pageIndex + "页成功:" + resultJsonStr);
      logger.info("获取第 " + pageIndex + "页成功:" + resultJsonStr);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return shangPinPageContent;
  }
  
  public static void exportQtyInfoForProductList(List<ProductDTO> productDTOAllList)
  {
    for (ProductDTO productDTO : productDTOAllList)
    {
      boolean flag = solveProductQty(productDTO);
      if (!flag) {
        repeatSolveFailProductQty(productDTO);
      }
    }
  }
  
  public static void repeatSolveFailedSpSkuNo(SpSkuNoDTO spSkuNoDTO)
  {
    int count = 0;
    while (count > 4)
    {
      count++;
      Map<String, String> checkProductInfo = getProductQtyInfo(spSkuNoDTO.getSpSkuNo(), spSkuNoDTO.getPcode());
      String temQty = (String)checkProductInfo.get("qty");
      if (temQty != null)
      {
        exportSpSkunoAndQty(spSkuNoDTO.getSpSkuNo(), temQty);
        return;
      }
    }
    exportSpSkunoAndQty(spSkuNoDTO.getSpSkuNo(), "0");
  }
  
  private static void repeatSolveFailProductQty(ProductDTO productDTO)
  {
    int count = 0;
    while (count < 4)
    {
      count++;
      boolean temFlag = solveProductQty(productDTO);
      if (temFlag) {
        return;
      }
    }
    setQtyZeroForProduct(productDTO);
  }
  
  private static void setQtyZeroForProduct(ProductDTO productDTO)
  {
    if (productDTO != null)
    {
      List<SkuDTO> zhiCaiSkuResultList = productDTO.getZhiCaiSkuResultList();
      if ((zhiCaiSkuResultList != null) && (zhiCaiSkuResultList.size() > 0))
      {
        int size = zhiCaiSkuResultList.size();
        for (int i = 0; i < size; i++)
        {
          SkuDTO skuDTO = (SkuDTO)zhiCaiSkuResultList.get(i);
          String spSkuNo = skuDTO.getSpSkuNo();
          if (spSkuNo != null) {
            exportSpSkunoAndQty(spSkuNo, "0");
          }
        }
      }
    }
  }
  
  private static boolean solveProductQty(ProductDTO productDTO)
  {
    String productUrl = productDTO.getProductUrl();
    if (!productDTO.getProductUrl().contains("www.mytheresa.com")) {
      return true;
    }
    System.out.println(productUrl);
    List<SkuDTO> zhiCaiSkuResultList = productDTO.getZhiCaiSkuResultList();
    int zhiCaiSkuResultListSize = zhiCaiSkuResultList.size();
    try
    {
      HttpResponse response = HttpUtils.get(productUrl);
      if (response.getStatus() == 200)
      {
        String htmlContent = response.getResponse();
        Document doc = Jsoup.parse(htmlContent);
        
        String price = "";

        String priceElements = doc.select("div.product-shop").select("span.price").text();
        String price3 = doc.select(".price-info").select("div.price-box").select("p.special-price").text();
        if (!(StringUtils.isEmpty(price3)))
        {
          price = price3.replace(",", "").replace(" ", "").replace("£","");
        }else {
          price = priceElements.replace(",", "").replace(" ", "").replace("£","");
        }
        byte[] bytes = { -62, -96 };
        String UTFSpace = new String(bytes, "utf-8");
        price = price.replaceAll(UTFSpace, "&nbsp;").replaceAll("&nbsp;", "");

        Elements temSizeElements = doc.select("div.product-options").select("div.size-chooser").select("li");
        String a = "Add to Shopping Bag";
        if (temSizeElements.size() == 0)
        {
          String temQty = "";
          String s = doc.select("div.product-shop").select("div.add-to-cart").text();
          if (s.equals(a)) {
            temQty = IN_STOCK;
          } else {
            temQty = NO_STOCK;
          }
          SkuDTO skuDTO = (SkuDTO)zhiCaiSkuResultList.get(0);
          String spSizeName = skuDTO.getSize();
          String marketPrice = skuDTO.getMarketPrice();
          if (StringUtils.isBlank(price)){
            price=marketPrice;
          }
          if (marketPrice != null)
          {
            float temElementPrice = Float.parseFloat(price);
            float spMarketPrice = Float.parseFloat(marketPrice);
            if (temElementPrice != spMarketPrice)
            {
              updateSpSkuMarketPrice(skuDTO.getSupplierSkuNo(), price);
              logger.info("推送 价格成功" + skuDTO.getSupplierSkuNo() + " 原价" + marketPrice + " 新价:" + price);
              System.out.println("推送 价格成功" + skuDTO.getSupplierSkuNo() + " 原价" + marketPrice + " 新价:" + price);
            }
          }
          else
          {
            loggerError.error("getMarketPrice 为空 ProductDTO:" + productDTO.toString());
          }
          exportSpSkunoAndQty(skuDTO.getSpSkuNo(), temQty);
        }
        else if ((temSizeElements != null) && (temSizeElements.size() > 0))
        {
          int spSkuSize = zhiCaiSkuResultList.size();
          int pageSize = temSizeElements.size();
          for (int j = 0; j < spSkuSize; j++)
          {
            SkuDTO skuDTO = (SkuDTO)zhiCaiSkuResultList.get(j);
            for (int i = 0; i < pageSize; i++)
            {
              String text = ((Element)temSizeElements.get(i)).text();
              if (!text.contains("size"))
              {
                Element sizeElement = (Element)temSizeElements.get(i);
                
                String temElementSizeName = "";
                if (sizeElement.text().contains("DT")) {
                  temElementSizeName = sizeElement.text().split("/")[0];
                } else {
                  String sizeValue;
                  if (sizeElement.text().contains("-")&& sizeElement.text().contains("/")){
                    sizeValue = sizeElement.text().split("-")[0].replace(" ","");
                    sizeValue=sizeElement.text().split("/")[0].replace(" ","");
                    temElementSizeName=sizeValue;
                  }else if (sizeElement.text().contains("-")){
                    sizeValue = sizeElement.text().split("-")[0].replace(" ","");
                    temElementSizeName=sizeValue;
                  }else if (sizeElement.text().contains("/")){
                    sizeValue = sizeElement.text().split("/")[0].replace(" ","");
                    temElementSizeName=sizeValue;
                  }
                  else {
                    temElementSizeName=sizeElement.text();
                  }

                }
                String spSizeName = skuDTO.getSize();
                String s=temElementSizeName.trim();
                if (temElementSizeName.contains("DT")) {
                  String replace = temElementSizeName.replace(" ", "");
                  String[] dts = replace.split("DT");
                   s = "DT"+dts[0] + " " + dts[1];
                }
                if (temElementSizeName.contains("IT")) {
                  String replace = temElementSizeName.replace(" ", "");
                  String[] dts = replace.split("IT");
                  s = "IT"+dts[0] + " " + dts[1];
                }
                if (temElementSizeName.contains("FR")) {
                  String replace = temElementSizeName.replace(" ", "");
                  String[] dts = replace.split("FR");
                  s = dts[1];
                }
                if (temElementSizeName.contains("EU")) {
                  String replace = temElementSizeName.replace(" ", "");
                  String[] dts = replace.split("EU");
                  s = dts[1];
                }
                if (s.equals(spSizeName))
                {
                  String temQty = "";
                  if (text.contains(" - Add to wishlist")) {
                    temQty =NO_STOCK;
                  } else {
                    temQty =IN_STOCK;
                  }
                  String marketPrice = skuDTO.getMarketPrice();
                  if (marketPrice != null)
                  {
                    float temElementPrice = Float.parseFloat(price);
                    float spMarketPrice = Float.parseFloat(marketPrice);
                    if (temElementPrice != spMarketPrice)
                    {
                      updateSpSkuMarketPrice(skuDTO.getSupplierSkuNo(), price);
                      logger.info("推送 价格成功" + skuDTO.getSupplierSkuNo() + " 原价" + marketPrice + " 新价" + price);
                      System.out.println("推送 价格成功" + skuDTO.getSupplierSkuNo() + " 原价" + marketPrice + " 新价" + price);
                    }
                  }
                  else
                  {
                    loggerError.error("getMarketPrice ���� ProductDTO:" + productDTO.toString());
                  }
                  exportSpSkunoAndQty(skuDTO.getSpSkuNo(), temQty);
                  break;
                }
              }
            }
          }
        }
        else
        {
          logger.error("===请求商品地址解析 商品尺码失败===========================================");
          logger.error(productDTO.toString());
          logger.error("===请求商品地址解析 商品尺码失败===========================================");
          
          return false;
        }
      }
      else
      {
        logger.error("================请求商品地址失败===========================================");
        logger.error(productDTO.toString());
        logger.error("================请求商品地址失败===========================================");
        return false;
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return true;
  }
  
  private static void updateSpSkuMarketPrice(String supplierSkuNo, String marketPrice)
  {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("supplierId", supplierId);
    jsonObject.put("supplierNo", supplierNo);
    jsonObject.put("supplierSkuNo", supplierSkuNo);
    jsonObject.put("marketPrice", marketPrice);
    String jsonStr = jsonObject.toString();
    System.out.println(" 推送价格入参json:" + jsonStr);
    logger.info(" 推送价格入参json:" + jsonStr);
    try
    {
      String resultJsonStr = HttpUtil45.operateData("post", "json", updateSpMarketPriceUrl, timeConfig, null, jsonStr, null, null);
      JSONObject resultJsonObject = JSONObject.fromObject(resultJsonStr);
      Map<String, Class> keyMapConfig = new HashMap();
      keyMapConfig.put("content", String.class);
      ApiResponseBody apiResponseBody = (ApiResponseBody)JSONObject.toBean(resultJsonObject, ApiResponseBody.class, keyMapConfig);
      String code = apiResponseBody.getCode();
      if ("0".equals(code))
      {
        logger.info("==更新updateSpSkuMarketPrice成功:" + supplierId + ":" + supplierSkuNo + ":" + marketPrice + "===============");
        System.out.println("更新updateSpSkuMarketPrice成功:" + supplierId + ":" + supplierSkuNo + ":" + marketPrice + "===============");
      }
      else
      {
        loggerError.error("==更新updateSpSkuMarketPrice 失败==" + jsonStr);
        System.err.println("==更新updateSpSkuMarketPrice 失败==" + jsonStr);
      }
      System.out.println("更新updateSpMarketPrice resultJsonStr:" + resultJsonStr);
      logger.info("更新updateSpMarketPrice resultJsonStr:" + resultJsonStr);
    }
    catch (Exception e)
    {
      loggerError.error("更新updateSpMarketPrice 失败   supplierSkuNo:" + supplierSkuNo + "marketPrice:" + marketPrice);
      System.out.println("更新updateSpMarketPrice 失败   supplierSkuNo:" + supplierSkuNo + "marketPrice:" + marketPrice);
      e.printStackTrace();
    }
  }
  
  public static Map<String, String> getProductQtyInfo(String spSkuNO, String pcode)
  {
    HashMap<String, String> mapDate = new HashMap();
    
    String checkProductQtyUrl = uri + "/it/it/p/ajax/product-detail-shipping.ajax?pcode=" + pcode;
    try
    {
      getProductQtyUnit(checkProductQtyUrl, mapDate);
    }
    catch (Exception e)
    {
      logger.info("=========尝试再次获取=====");
      try
      {
        getProductQtyUnit(checkProductQtyUrl, mapDate);
      }
      catch (Exception ee)
      {
        logger.info("=========第二次：获取库存失败 ===spSkuNO: " + spSkuNO + "pcode:" + pcode);
        ee.printStackTrace();
        System.out.println();
        System.err.println("=========第二次：获取库存失败 ===spSkuNO: " + spSkuNO + "pcode:" + pcode);
      }
    }
    return mapDate;
  }
  
  public static void getProductQtyUnit(String checkProductUrl, Map<String, String> mapDate)
    throws Exception
  {
    Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36)");
    Header[] headers = new Header[1];
    headers[0] = header;
    HttpResponse checkResponse = HttpUtils.get(checkProductUrl, headers);
    if (checkResponse.getStatus() == 200)
    {
      String responseHtml = checkResponse.getResponse();
      if ((responseHtml == null) || ("".equals(responseHtml)))
      {
        mapDate.put("qty", NO_STOCK);
        mapDate.put("qtyDesc", "售罄");
        return;
      }
      Document doc = Jsoup.parse(responseHtml);
      
      String qtyStr = doc.select("p.title").first().text();
      if (qtyStr != null)
      {
        if (qtyStr.contains("PRE-ORDINE"))
        {
          mapDate.put("qty", NO_STOCK);
          mapDate.put("qtyDesc", "预售");
        }
        else if (qtyStr.contains("DISPONIBILE"))
        {
          mapDate.put("qty", IN_STOCK);
          mapDate.put("qtyDesc", "有货");
        }
      }
      else
      {
        mapDate.put("qty", NO_STOCK);
        mapDate.put("qtyDesc", "售罄");
      }
    }
    else
    {
      mapDate.put("qty", NO_STOCK);
      mapDate.put("qtyDesc", "售罄");
    }
  }
  
  private static void exportSpSkunoAndQty(String spSkuNo, String qty)
  {
    StringBuffer buffer = new StringBuffer();
    try
    {
      buffer.append(spSkuNo).append(splitSign);
      buffer.append(qty).append(splitSign);
      buffer.append("\r\n");
      out.write(buffer.toString());
      System.out.println("spSkuNo:" + spSkuNo + " qty:" + qty + "|");
      logger.info("spSkuNo:" + spSkuNo + " qty:" + qty + "|");
      logger.info(buffer.toString());
      out.flush();
    }
    catch (Exception localException) {}
  }
  
  public static void main(String[] args)
  {
    ProductDTO productDTO = new ProductDTO();
    productDTO.setProductUrl("https://www.mytheresa.com/en-gb/gucci-dionysus-gg-supreme-small-coated-canvas-shoulder-bag-867476.html");
    List<SkuDTO> zhiCaiSkuResultList = new ArrayList();
    SkuDTO skuDTO = new SkuDTO();
    skuDTO.setSpSkuNo("30968589002");
    skuDTO.setSize("34");
    skuDTO.setSupplierSkuNo("493117 X3I31 9169-U");
    skuDTO.setMarketPrice("350.0");
    zhiCaiSkuResultList.add(skuDTO);
    productDTO.setZhiCaiSkuResultList(zhiCaiSkuResultList);
    solveProductQty(productDTO);
  }
}