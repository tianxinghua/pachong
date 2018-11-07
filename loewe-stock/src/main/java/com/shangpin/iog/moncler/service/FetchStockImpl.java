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
    System.out.println("============拉取loewe库存信息开始 " + startDateTime + "=========================");
    logger.info("==============拉取loewe库存信息开始" + startDateTime + "=========================");
    
    failedSpSkuNoList = new ArrayList();
    
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    String todayStr = simpleDateFormat.format(new Date());
    
    String temFilePath = filePath + "loewe-qty-" + todayStr + ".csv";
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
    
    ShangPinPageContent monclerPageContent = getShangPinPageContentByParam(supplierId, "", "www.loewe.com", Integer.valueOf(1), Integer.valueOf(Integer.parseInt(pageSize)));
    
    productDTOAllList.addAll(monclerPageContent.getZhiCaiResultList());
    if (monclerPageContent == null) {
      return;
    }
    Integer total = monclerPageContent.getTotal();
    Integer pageNumber = getPageNumber(total, Integer.valueOf(20));
    for (int i = 2; i <= pageNumber.intValue(); i++)
    {
      ShangPinPageContent temmonclerPageContent = getShangPinPageContentByParam(supplierId, "", "www.loewe.com", Integer.valueOf(i), Integer.valueOf(Integer.parseInt(pageSize)));
      List<ProductDTO> zhiCaiResultList = temmonclerPageContent.getZhiCaiResultList();
      ProductDTO localProductDTO;
      for (Iterator localIterator = zhiCaiResultList.iterator(); localIterator.hasNext(); localProductDTO = (ProductDTO)localIterator.next()) {}
      if (temmonclerPageContent != null)
      {
        productDTOAllList.addAll(temmonclerPageContent.getZhiCaiResultList());
      }
      else
      {
        temmonclerPageContent = getShangPinPageContentByParam(supplierId, "", "www.loewe.com", Integer.valueOf(i), Integer.valueOf(Integer.parseInt(pageSize)));
        if (temmonclerPageContent != null) {
          productDTOAllList.addAll(temmonclerPageContent.getZhiCaiResultList());
        }
      }
    }
    logger.info("=====需要更新loewe spProduct Size:" + productDTOAllList.size());
    System.out.println("=====需要更新loewe spProduct Size:" + productDTOAllList.size());
    
    exportQtyInfoForProductList(productDTOAllList);
    
    int failedSpSkuNoSize = failedSpSkuNoList.size();
    for (int i = 0; i < failedSpSkuNoSize; i++) {
      repeatSolveFailedSpSkuNo((SpSkuNoDTO)failedSpSkuNoList.get(i));
    }
    String endtDateTime = format.format(new Date());
    logger.info("===================拉取loewe库存信息结束 " + endtDateTime + "=========================");
    System.out.println("=================拉取loewe库存信息结束 " + endtDateTime + "=========================");
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
    if (!productDTO.getProductUrl().contains("www.loewe.com")) {
      return true;
    }
    System.out.println(productUrl);
    List<SkuDTO> zhiCaiSkuResultList = productDTO.getZhiCaiSkuResultList();
    int zhiCaiSkuResultListSize = zhiCaiSkuResultList.size();
    try
    {
      Document doc = Jsoup.connect(productUrl).timeout(500000).cookie("cookie","__cfduid=d7a39850d7daa4fe392e99c6c26133efd1540268870; dwac_dbe0a10617e57472113ac114ca=lvGHT-DaqeiKzkQp0oCXKtZd3SEo6K1AlB8%3D|dw-only|||EUR|false|Europe%2FMadrid|true; sid=lvGHT-DaqeiKzkQp0oCXKtZd3SEo6K1AlB8; dispatcherRedirect=Search-Show|cgid=w_womenswear&iscroll=1; dwanonymous_09ca7ed58839320229f1a601b6c9e62b=ab9hg5wx0UXIpZMhX9SpwCnU0N; dwsecuretoken_09ca7ed58839320229f1a601b6c9e62b=57iOAaerxBrHLJz4p5kzKjZhLWxEmndKRw==; dwsid=c3seLqFHKRcKSUjDNCAtJDPXREBpdS2_FJj4izCrIkIk6IDWYoSZ2AmPWZNUIg-4t4jMiSZaTMbusmRZaBnnBg==; dwac_1709df91f68b756bdadec56b11=lvGHT-DaqeiKzkQp0oCXKtZd3SEo6K1AlB8%3D|dw-only|||CNY|false|Europe%2FMadrid|true; dwanonymous_e26fcb3658db420d19f2288ae971792b=ac9w5y716gDkcpKR4yHbCaktix; dwsecuretoken_e26fcb3658db420d19f2288ae971792b=VxWvEiXlxmVxCHBroYjQFESUIQh5iGQy7w==; __cq_dnt=0; dw_dnt=0; _gcl_au=1.1.1455961738.1540268877; __55=%7B%22userId%22%3Anull%2C%22ms%22%3A%22non-member%22%2C%22st%22%3A%22regular%22%2C%22vF0%22%3A1540268877237%2C%22vF%22%3A%22new%22%7D; __gaLoewe=GA1.2.164809046.1540268877; __gaLoewe_gid=GA1.2.426483622.1540268878; __cq_uuid=b59d1240-b4cf-11e8-ac82-173fd260c816; dispatcherSite=FR-en-LOE_EUR; cqcid=ab9hg5wx0UXIpZMhX9SpwCnU0N; ABTastySession=referrer%3D__landingPage%3Dhttps%3A//www.loewe.com/eur/en/women/ready-to-wear%3Fiscroll%3D4__referrerSent%3Dtrue; cookiePopin=0; ABTasty=uid%3D18102312275672186%26fst%3D1540268876222%26pst%3D1540268876222%26cst%3D1540271175427%26ns%3D2%26pvt%3D29%26pvis%3D21%26th%3D334754.0.25.19.2.0.1540268889959.1540272012286.1_346712.452322.4.2.2.1.1540268907448.1540272024470.1_347481.0.31.21.2.1.1540268876269.1540272023658.1_347647.453439.4.2.2.1.1540268907460.1540272024478.1; OptanonConsent=landingPath=NotLandingPage&datestamp=Tue+Oct+23+2018+13%3A20%3A26+GMT%2B0800+(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)&version=3.6.19&groups=0_116493%3A1%2C1%3A1%2C104%3A1%2C0_116494%3A1%2C0_122812%3A1%2C0_122896%3A1%2C2%3A1%2C101%3A1%2C0_125933%3A1%2C3%3A1%2C0_116495%3A1%2C102%3A1%2C0_116452%3A1%2C0_122813%3A1%2C4%3A1%2C0_125932%3A1&AwaitingReconsent=false; __cq_bc=%7B%22bbpc-LOE_EUR%22%3A%5B%7B%22id%22%3A%22%22%2C%22alt_id%22%3A%22D1282060ST-1100%22%2C%22type%22%3A%22raw_sku%22%7D%2C%7B%22id%22%3A%22D1282060ST%22%2C%22type%22%3A%22vgroup%22%2C%22alt_id%22%3A%22D1282060ST-1100%22%7D%2C%7B%22id%22%3A%22D1283980TO%22%2C%22type%22%3A%22vgroup%22%2C%22alt_id%22%3A%22D1283980TO-2556%22%7D%5D%7D; __cq_seg=0~0.21!1~0.02!2~-0.21!3~0.21!4~0.05!5~0.17!6~-0.34!7~-0.19!8~0.65!9~-0.51!f0~15~5").get();

     // HttpResponse response = HttpUtils.get(productUrl);
      if (doc!=null)
      {

        String price = "";

        String priceElements = doc.select("div#quickview-middle").select("span.price-sales").first().text();
        String price3 = doc.select("div#quickview-middle").select("span.price-sales").first().text();
        if (!(StringUtils.isEmpty(price3)))
        {
          price = price3.replace(".", "").replace(" ", "").replace("€","");
        }else {
          price = priceElements.replace(",", "").replace(" ", "").replace("€","");
        }
        byte[] bytes = { -62, -96 };
        String UTFSpace = new String(bytes, "utf-8");
        price = price.replaceAll(UTFSpace, "&nbsp;").replaceAll("&nbsp;", "");
        Elements temSizeElements = doc.select("div.price-and-size-wrapper").select("div.value").select("option");
        String a = "Add to Bag";
        if (temSizeElements.size() == 0)
        {
          String temQty = "";
          String s = doc.select("div.product-add-to-cart").select("button#add-to-cart").text();
          if (s.equals(a)) {
            temQty = IN_STOCK;
          } else {
            temQty = NO_STOCK;
          }
          SkuDTO skuDTO = (SkuDTO)zhiCaiSkuResultList.get(0);
          String spSizeName = skuDTO.getSize();
          String marketPrice = skuDTO.getMarketPrice();
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
              if (!text.contains("Size"))
              {
                Element sizeElement = (Element)temSizeElements.get(i);
                String temElementSizeName = "";
                if (sizeElement.text().contains("notify me")) {
                  temElementSizeName = sizeElement.text().replace("notify me","");
                }
                String spSizeName = skuDTO.getSize();
                String s=sizeElement.text().replace("notify me"," ").trim();
                String s1=sizeElement.text();

                if (s.equals(spSizeName))
                {
                  String temQty = "";
                  if (s1.contains("notify me")) {
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
    productDTO.setProductUrl("https://www.loewe.com/eur/en/women/ready-to-wear/long-sleeve-dress/S2286020FA-1100.html?cgid=w_womenswear");
    List<SkuDTO> zhiCaiSkuResultList = new ArrayList();
    SkuDTO skuDTO = new SkuDTO();
    skuDTO.setSpSkuNo("30968589002");
    skuDTO.setSize("32");
    skuDTO.setSupplierSkuNo("493117 X3I31 9169-U");
    skuDTO.setMarketPrice("350.0");
    zhiCaiSkuResultList.add(skuDTO);
    productDTO.setZhiCaiSkuResultList(zhiCaiSkuResultList);
    solveProductQty(productDTO);
  }
}