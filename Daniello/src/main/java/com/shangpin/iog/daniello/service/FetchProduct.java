package com.shangpin.iog.daniello.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.csvreader.CsvReader;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.daniello.dto.Item;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

@Component("daniello")
public class FetchProduct
{
  final Logger logger = Logger.getLogger("info");
  private static ResourceBundle bdl = null;
  
  static
  {
    if (null == bdl) {
      bdl = ResourceBundle.getBundle("conf");
    }
    supplierId = bdl.getString("supplierId");
    url = bdl.getString("url");
    day = Integer.valueOf(bdl.getString("day")).intValue();
  }
  
  private static String user = bdl.getString("user");
  private static String password = bdl.getString("password");
  private static String supplierId;
  private static String url;
  public static int day;
  @Autowired
  private ProductFetchService productFetchService;
  @Autowired
  ProductSearchService productSearchService;
  
  public void fetchProductAndSave()
  {
    Map<String, Item> spuMap = new HashMap();
    Map<String, String> imgMap = new HashMap();
    Map<String, String> priceMap = new HashMap();
    
    this.logger.info("get product starting....");
    System.out.println("获取数据");
    OutTimeConfig outTimeConfig = new OutTimeConfig(18000000, 54000000, 54000000);
    Map<String, String> map = new HashMap();
    user = "shangpin";
    password = "Daniello0203";
    String skuData = HttpUtil45.postAuth(url + "GetAllAvailabilityMarketplace", map, outTimeConfig, user, password);
    save("anielloSKU.txt", skuData);
    String imageData = HttpUtil45.postAuth(url + "GetAllImageMarketplace", map, outTimeConfig, user, password);
    save("anielloImage.txt", imageData);
    String priceData = HttpUtil45.postAuth(url + "GetAllPricelistMarketplace", map, outTimeConfig, user, password);
    save("anielloPrice.txt", priceData);
    
    CsvReader cs = null;
    try
    {
      cs = new CsvReader("/usr/local/app/daniello/GetAllItemsMarketplace.xml");
    }
    catch (FileNotFoundException e2)
    {
      e2.printStackTrace();
    }
    Date endDate = new Date();
    Date startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate, day * -1, "D");
    
    Map<String, SkuDTO> skuDTOMap = new HashedMap();
    try
    {
      skuDTOMap = this.productSearchService.findStockAndPriceOfSkuObjectMap(supplierId, startDate, endDate);
    }
    catch (ServiceException e)
    {
      e.printStackTrace();
    }
    this.logger.info("get product over");
    
    this.logger.info("save product into DB begin");
    String data = "";
    
    String[] priceStrings = priceData.split("\\r\\n");
    String[] priceArr = null;
    for (int i = 1; i < priceStrings.length; i++)
    {
      if (StringUtils.isNotBlank(priceStrings[i])) {
        if (i == 1) {
          data = priceStrings[i].split("\\n")[1];
        } else {
          data = priceStrings[i];
        }
      }
      priceArr = data.replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;", "").split(";");
      priceMap.put(priceArr[0], priceArr[3]);
    }
    String[] spuArr = null;
    try
    {
      cs.readRecord();
      cs.readRecord();
      while (cs.readRecord())
      {
        data = cs.getRawRecord();
        spuArr = data.replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;", "").split(";");
        SpuDTO spu = new SpuDTO();
        Item item = new Item();
        try
        {
          item.setColor(spuArr[10]);
          item.setSupplierPrice(spuArr[16]);
          item.setDescription(spuArr[15]);
          item.setSpuId(spuArr[0]);
          
          item.setStyleCode(spuArr[3]);
          item.setColorCode(spuArr[4]);
          
          spuMap.put(spuArr[0], item);
          
          spu.setId(UUIDGenerator.getUUID());
          spu.setSupplierId(supplierId);
          spu.setSpuId(spuArr[0]);
          spu.setBrandName(spuArr[2]);
          spu.setCategoryName(spuArr[8]);
          
          spu.setSeasonId(spuArr[1]);
          
          StringBuffer material = new StringBuffer();
          if (StringUtils.isNotBlank(spuArr[11])) {
            material.append(spuArr[11]).append(";");
          } else if (StringUtils.isNotBlank(spuArr[15])) {
            material.append(spuArr[15]).append(";");
          } else if (StringUtils.isNotBlank(spuArr[42])) {
            material.append(spuArr[42]);
          }
          spu.setMaterial(material.toString());
          spu.setCategoryGender(spuArr[5]);
          spu.setProductOrigin(spuArr[40]);
          this.productFetchService.saveSPU(spu);
        }
        catch (ServiceException e)
        {
          try
          {
            this.productFetchService.updateMaterial(spu);
          }
          catch (ServiceException e1)
          {
            e1.printStackTrace();
          }
        }
      }
    }
    catch (IOException e2)
    {
      e2.printStackTrace();
    }
    String[] imageStrings = imageData.split("\\r\\n");
    String[] imageArr = null;
    for (int j = 2; j < imageStrings.length; j++) {
      if (StringUtils.isNotBlank(imageStrings[j]))
      {
        data = imageStrings[j];
        imageArr = data.replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;", "").split(";");
        if (!imgMap.containsKey(imageArr[0])) {
          imgMap.put(imageArr[0], imageArr[1]);
        } else {
          imgMap.put(imageArr[0], (String)imgMap.get(imageArr[0]) + "," + imageArr[1]);
        }
      }
    }
    String size = "";
    String[] skuStrings = skuData.split("\\r\\n");
    String[] skuArr = null;
    for (int i = 1; i < skuStrings.length; i++) {
      if (StringUtils.isNotBlank(skuStrings[i]))
      {
        if (i == 1) {
          data = skuStrings[i].split("\\n")[1];
        } else {
          data = skuStrings[i];
        }
        skuArr = data.replaceAll("&lt;", "").replaceAll("&gt;", "").replaceAll("&amp;", "").split(";");
        if (spuMap.containsKey(skuArr[0]))
        {
          Item item = (Item)spuMap.get(skuArr[0]);
          SkuDTO sku = new SkuDTO();
          sku.setId(UUIDGenerator.getUUID());
          sku.setSupplierId(supplierId);
          sku.setSpuId(skuArr[0]);
          
          size = skuArr[1];
          if (size.indexOf("½") > 0) {
            size = size.replace("½", "+");
          }
          sku.setProductSize(size);
          

          sku.setMarketPrice((String)priceMap.get(item.getSpuId()));
          

          sku.setColor(item.getColor());
          sku.setProductDescription(item.getDescription());
          sku.setSaleCurrency("EURO");
          String stock = skuArr[2];
          String barCode = skuArr[5];
          sku.setStock(stock);
          
          sku.setSkuId(skuArr[0] + "-" + barCode);
          sku.setBarcode(barCode);
          sku.setProductCode(item.getStyleCode() + "-" + item.getColorCode());
          try
          {
            if (skuDTOMap.containsKey(sku.getSkuId())) {
              skuDTOMap.remove(sku.getSkuId());
            }
            this.productFetchService.saveSKU(sku);
          }
          catch (ServiceException e)
          {
            try
            {
              if (e.getMessage().equals("数据插入失败键重复")) {
                this.productFetchService.updatePriceAndStock(sku);
              } else {
                e.printStackTrace();
              }
            }
            catch (ServiceException e1)
            {
              e1.printStackTrace();
            }
          }
          String imgUrls = (String)imgMap.get(skuArr[0]);
          if (StringUtils.isNotBlank(imgUrls))
          {
            String[] imgUrlArr = imgUrls.split(",");
            this.productFetchService.savePicture(supplierId, null, skuArr[0] + "-" + barCode, Arrays.asList(imgUrlArr));
          }
        }
      }
    }
    for (Iterator<Map.Entry<String, SkuDTO>> itor = skuDTOMap.entrySet().iterator(); itor.hasNext();)
    {
      Map.Entry<String, SkuDTO> entry = (Map.Entry)itor.next();
      if (!"0".equals(((SkuDTO)entry.getValue()).getStock()))
      {
        ((SkuDTO)entry.getValue()).setStock("0");
        try
        {
          this.productFetchService.updatePriceAndStock((SkuDTO)entry.getValue());
        }
        catch (ServiceException e)
        {
          e.printStackTrace();
        }
      }
    }
    this.logger.info("save product into DB success");
  }
  
  public void save(String name, String data)
  {
    File file = new File("/usr/local/app/" + name);
    if (!file.exists()) {
      try
      {
        file.getParentFile().mkdirs();
        file.createNewFile();
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    FileWriter fwriter = null;
    try
    {
      fwriter = new FileWriter("/usr/local/app/" + name);
      fwriter.write(data); return;
    }
    catch (IOException ex)
    {
      ex.printStackTrace();
    }
    finally
    {
      try
      {
        fwriter.flush();
        fwriter.close();
      }
      catch (IOException ex)
      {
        ex.printStackTrace();
      }
    }
  }
  
  public static void main(String[] args)
  {
    new FetchProduct().fetchProductAndSave();
  }
}
