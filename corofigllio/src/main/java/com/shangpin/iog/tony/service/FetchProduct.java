package com.shangpin.iog.tony.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.tony.dto.Data;
import com.shangpin.iog.tony.dto.Item;
import com.shangpin.iog.tony.dto.Items;
import com.shangpin.iog.tony.dto.Product;
import com.shangpin.product.AbsSaveProduct;

@Component("tony")
public class FetchProduct extends AbsSaveProduct{

    private static Logger log = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    
    private static ResourceBundle bdl = null;
	private static String supplierId = "";
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
	}
    
    /**
     * fetch product and save into db
     */
    public Map<String, Object> fetchProductAndSave() {
    	Map<String, Object> returnMap = new HashMap<String, Object>();
		List<SkuDTO> skuList = new ArrayList<SkuDTO>();
		List<SpuDTO> spuList = new ArrayList<SpuDTO>();
		Map<String,List<String>> imageMap = new HashMap<String, List<String>>();
        //获取数据
        String itemsJson =  HttpUtil45.get("http://net13server2.net/TheApartmentAPI/MyApi/Productslist/GetProducts?DBContext=Default&CategoryId=&BrandId=&SeasonCode=079&StartIndex=1&EndIndex=0&key=2Y2sQVaiXX",new OutTimeConfig(1000*60*20,1000*60*20,1000*60*60),null);
        Data data = new Gson().fromJson(itemsJson, Data.class);
        //保存数据
        messMappingAndSave(data,skuList,spuList,imageMap);
        returnMap.put("sku", skuList);
		returnMap.put("spu", spuList);
		returnMap.put("image", imageMap);
		return returnMap;		
    }


    /**
     * message mapping and save into DB
     */
    private void messMappingAndSave(Data data,List<SkuDTO> skuList, List<SpuDTO> spuList,Map<String,List<String>> imageMap) {
    	
		List<Product> list = data.getProduct();
        for(Product product:list){
        	String [] pic = null;
        	Items items = product.getItems();
        	if(items!=null){
        		List<Item> listItem = items.getItem();
        		if(listItem!=null&&!listItem.isEmpty()){
            		for(Item item : listItem){

                        SkuDTO sku  = new SkuDTO();
                        try {
                        	pic = item.getPictures();
                            sku.setId(UUIDGenerator.getUUID());
                            sku.setSupplierId(supplierId);
                            sku.setSpuId(product.getProduct_id());
                            sku.setSkuId(item.getItem_id());
                            sku.setProductSize(item.getItem_size());
                            sku.setMarketPrice("");
                            sku.setSalePrice("");
                            sku.setSupplierPrice(product.getSupply_price());
                            sku.setColor(item.getColor());
                            sku.setProductDescription(product.getProduct_Note());
                            sku.setStock(item.getStock());
                            sku.setBarcode(item.getBarcode());
                            sku.setProductCode(product.getProduct_name());
                            skuList.add(sku);
                            //t图片
                            imageMap.put(sku.getSkuId()+";"+sku.getProductCode(), Arrays.asList(pic)); 
                            
                        } catch(Exception e){
                            loggerError.error("tony 更新库存失败 " + e.getMessage());
                        }                        
            		}	
        		}
        		
                SpuDTO spu = new SpuDTO();
                try {
                    spu.setId(UUIDGenerator.getUUID());
                    spu.setSupplierId(supplierId);
                    spu.setSpuId(product.getProduct_id());
                    spu.setBrandName(product.getProducer_id());
                    spu.setCategoryName(product.getCategory());
                    spu.setSpuName(product.getDescription());
                    spu.setSeasonId(product.getSeason());
                    spu.setMaterial(product.getProduct_Material());
                    spu.setProductOrigin(product.getProduct_MadeIn());
                    spu.setCategoryGender(product.getType());
                    spuList.add(spu);
                } catch (Exception e) {
                	loggerError.error("spu保存失败");
                }
        	}
        }        
    }

}
