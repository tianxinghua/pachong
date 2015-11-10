package com.shangpin.iog.railSoAtelier.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.onsite.base.common.HTTPClient;
import com.shangpin.iog.onsite.base.constance.Constant;
import com.shangpin.iog.railSoAtelier.dto.Attribute;
import com.shangpin.iog.railSoAtelier.dto.Attributes;
import com.shangpin.iog.railSoAtelier.dto.Category;
import com.shangpin.iog.railSoAtelier.dto.Image;
import com.shangpin.iog.railSoAtelier.dto.Product;
import com.shangpin.iog.railSoAtelier.dto.Products;
import com.shangpin.iog.railSoAtelier.dto.Stock;
import com.shangpin.iog.railSoAtelier.dto.Stocks;
import com.shangpin.iog.service.ProductFetchService;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/9/17.
 */
@Component("railSoAtelier")
public class FetchProduct {
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static String productUrl;
	private static String stockUrl;
	private static String url;
    @Autowired
    private ProductFetchService productFetchService;

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		url = bdl.getString("url");
		stockUrl = bdl.getString("stockUrl");
		
	}
	 private static void readLine(String content){
	    	File file = new File("C://product.json");
	    	FileWriter fwriter = null;
	    	   try {
	    	    fwriter = new FileWriter(file);
	    	    fwriter.write(content);
	    	   } catch (Exception ex) {
	    	    ex.printStackTrace();
	    	   } finally {
	    	    try {
	    	     fwriter.flush();
	    	     fwriter.close();
	    	    } catch (Exception ex) {
	    	     ex.printStackTrace();
	    	    }
	    	   }
	    }
	private static String getJson(String fileName) {

		String fullFileName = "C:/" + fileName + ".json";
		File file = new File(fullFileName);
		Scanner scanner = null;
		StringBuilder buffer = new StringBuilder();
		try {
			scanner = new Scanner(file, "utf-8");
			while (scanner.hasNextLine()) {
				buffer.append(scanner.nextLine());
			}
		} catch (Exception e) {

		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
		System.out.println(buffer.toString());
		return buffer.toString();
	}
	 private static Map<String,Attributes> getStockMap() {
		 
		 Map<String,String> map = new HashMap<String,String>();
	    	map.put("id","4");
	    	map.put("password","JwqDZDF-5jk%YRH=");
	    	map.put("affiliate","shangpin");
	    	String json = HttpUtil45.post("http://www.railso.com/xml_feeds.php", map,new OutTimeConfig(1000*60*10,10*1000*60,10*1000*60));// new HTTPClient(Constant.URL_MARYLOU).fetchProductJson();
//	    	String json = getJson("stock");
	    	System.out.println("库存"+json);
	    	logger.info(json);
//	    	readLine(json);
	    	Pattern patt =Pattern.compile("</attributes-\\d*>");
			Pattern patt1 =Pattern.compile("<attributes-\\d*>");
			Stocks stocks = null;
			try {
				String replaceAll = patt.matcher(json).replaceAll("</item>");
				String replaceAll1 = patt1.matcher(replaceAll).replaceAll("<item>");
				stocks = ObjectXMLUtil.xml2Obj(Stocks.class, replaceAll1.toString());
			} catch (JAXBException e) {
				e.printStackTrace();
			}
			List<Stock> list = stocks.getStocks();
		    Map<String,Attributes> returnMap = new HashMap<String,Attributes>();
			for(Stock stock:list){
				Attributes att = stock.getAttributes();
				String productId = stock.getProduct_id().trim();
				returnMap.put(productId, att);
			}
			return returnMap;
		}
	 private static Products getProductList() {
		 Map<String,String> map = new HashMap<String,String>();
    	map.put("id","3");
    	map.put("password","=eFf`khmbN:3Dfc");
    	map.put("affiliate","shangpin");
    	String json = HttpUtil45.post("http://www.railso.com/xml_feeds.php", map,new OutTimeConfig(1000*60*10,10*1000*60,10*1000*60));// new HTTPClient(Constant.URL_MARYLOU).fetchProductJson();
//    	String json = getJson("product");
//    	readLine(json);
    	System.out.println("商品"+json);
    	logger.info(json);
		Products products = null;
		try {
			products = ObjectXMLUtil.xml2Obj(Products.class, json);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return products;
	}
    /**
     * 得到产品信息并储存
     */
    public void fetchProductAndSave(){
    	Map<String,Attributes> returnMap = getStockMap();
    	Products products = getProductList();
     
        //映射数据并保存
        logger.info("save product into DB begin");
        messMappingAndSave(returnMap,products);
        logger.info("save product into DB success");
 
        //System.out.println(json);
    }
    /**
     * 映射数据并保存
     */
    private void messMappingAndSave(Map<String,Attributes> returnMap,Products products) {
        List<Product> productList = products.getProducts();
        for (Product product : productList) {
        	
            SpuDTO spu = new SpuDTO();
        	  try {
                  spu.setId(UUIDGenerator.getUUID());
                  spu.setSupplierId(supplierId);
                  spu.setSpuId(product.getProduct_id());
                  spu.setBrandName(product.getManufacturer_name());
                  Category c = product.getDefault_category();
                  if(c!=null){
                	  spu.setCategoryName(c.getCategory_default_name());
                  }
                  spu.setSpuName(product.getName());
                  String tree = product.getProduct_category_tree();
                  String sex[] = tree.split(">");
                  spu.setCategoryGender(sex[0]);
                  productFetchService.saveSPU(spu);
              } catch (ServiceException e) {
                  e.printStackTrace();
              }
        
        	  if(returnMap.get(product.getProduct_id())!=null){
        		  Attributes att = returnMap.get(product.getProduct_id());
        		  if(att!=null){
        			  List<Attribute> list = att.getAttributes();

            		  String skuId = "";
                      for (Attribute item : list) {
                          SkuDTO sku = new SkuDTO();
                          try {
                              sku.setId(UUIDGenerator.getUUID());
                              sku.setSupplierId(supplierId);
                              sku.setSpuId(product.getProduct_id());
                              sku.setProductDescription(product.getDescription_short());
                              sku.setMarketPrice(product.getPrice());
                              String itemSize = item.getAttribute_name().trim();
                              sku.setProductName(product.getName());
                              if("Unique".equals(itemSize)){
                            	  itemSize="A"; 
                            	  skuId = product.getProduct_id()+"|"+itemSize;
                              }
                              skuId = product.getProduct_id()+"|"+itemSize;
                              sku.setSkuId(skuId);
                              sku.setSaleCurrency("EUR");
                              sku.setMarketPrice(product.getShangpin());
                              sku.setStock(item.getQuantity());
                              productFetchService.saveSKU(sku);
                              Image image = product.getImages();
                              String picture = null;
                              if(image!=null){
                            	  picture = image.getLarge_default();
                            	  if (StringUtils.isNotBlank(image.getLarge_default())) {
                                      ProductPictureDTO dto = new ProductPictureDTO();
                                      dto.setPicUrl(picture);
                                      dto.setSupplierId(supplierId);
                                      dto.setId(UUIDGenerator.getUUID());
                                      dto.setSpuId(product.getProduct_id());
                                      try {
                                          productFetchService.savePictureForMongo(dto);
                                      } catch (ServiceException e) {
                                          e.printStackTrace();
                                      }
                                  }
                              }
                          } catch (ServiceException e) {
                              try {
                                  if (e.getMessage().equals("数据插入失败键重复")) {
                                      //更新价格和库存
                                      productFetchService.updatePriceAndStock(sku);
                                  } else {
                                      e.printStackTrace();
                                  }
                              } catch (ServiceException e1) {
                                  e1.printStackTrace();
                              }
                          }
                      }
        		  }
        	  }
        }
    }

}
