package com.shangpin.iog.mclabels.service;

/**
 * Created by wang on 2015/9/21.
 */

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mongodb.util.JSON;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.mclabels.dao.AttributeInfo;
import com.shangpin.iog.mclabels.dao.DistributionCenterInfoSubmit;
import com.shangpin.iog.mclabels.dao.ImageInfoSubmit;
import com.shangpin.iog.mclabels.dao.Item;
import com.shangpin.iog.service.EventProductService;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.product.AbsSaveProduct;

/**
 * Created by 赵根春 on 2015/9/25.
 */
@Component("efashion")
public class FetchProduct extends AbsSaveProduct{

	@Autowired
	ProductFetchService productFetchService;
	@Autowired
	private ProductSearchService productSearchService;
	@Autowired
	EventProductService eventProductService;
	private static Logger logger = Logger.getLogger("info");
	private static String supplierId = "2016111101957";
	public static int day = 90;
	public static int max;
	public static Gson gson = null;
	static {
		gson = new Gson();
	}
	static int i=0;
	private List<Item> allItemList = new ArrayList<Item>();
	public void save(){
		handleData("spu", supplierId, day, null);
	}
	public  List<Item>   getProductList(){
		String url = "http://nodo.azurewebsites.net/api/channel/export/?channelName=Shangpin&token=bearer%20eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJlbWFpbCI6InNlcnZpY2VzQHNoYW5ncGluLmNvbSIsInBhc3N3b3JkIjoiJDJhJDA4JEdrcDZENmdYVkQxVmtOYWdGandrak9yRGRQekcvdjBuN3ZTUzFScEhKYnNyNHZXY0g2OTNPIiwiaWQiOjEzLCJpYXQiOjE0ODA2OTgxMTF9.7kRekC3N7xe0TjuFGbbpK93Kv4ry7eyrS897qmPvBIc";
		String json = HttpUtil45
				.get(url,new OutTimeConfig(1000 * 60 *2, 1000 * 60*2, 1000 * 60*2),null);
		List<Item> obj = null;
		try{
			obj = gson.fromJson(json,new TypeToken<List<Item>>(){}.getType());
			if(obj!=null){
				fetchProductAndSave();
			}
		}catch(Exception e){
			i++;
		}
		return obj;
	}
	public  Map<String, Object> fetchProductAndSave() {

		logger.info("总的商品数量：" + allItemList.size());
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<SkuDTO> skuList = new ArrayList<SkuDTO>();
		List<SpuDTO> spuList = new ArrayList<SpuDTO>();
		Map<String, List<String>> imageMap = new HashMap<String, List<String>>();
		List<Item> list = getProductList();
		for (Item item : list) {
			if("true".equals(item.getVariationInfo().getIsParent())){
				SpuDTO spu = new SpuDTO();
				spu.setId(UUIDGenerator.getUUID());
				spu.setSupplierId(supplierId);
				spu.setSpuId(item.getSku());
				spu.setBrandName(item.getBrand());
				List<AttributeInfo> attributeInfolist = item.getAttributeList().getAttributeInfo();
				String season = null;
				String gender = null;
				String material = null;
				for(AttributeInfo attr:attributeInfolist){
					if("season".equals(attr.getName())){
						season = attr.getValue();
					}
					if("Gender".equals(attr.getName())){
						gender = attr.getValue();
					}
					if("Materials_node".equals(attr.getName())){
						material = attr.getValue();
					}
				}
				spu.setSpuName(item.getTitle());
				spu.setCategoryName(item.getClassification());
				spu.setMaterial(material);
//				spu.setProductOrigin(item.getMade_in());
				spu.setSeasonName(season);
				spu.setCategoryGender(gender);
				spuList.add(spu);
				List<ImageInfoSubmit> listImages = item.getImageList().getImageInfoSubmit();
				List<String> listImag = new ArrayList<String>();
				if(listImages!=null){
					for(ImageInfoSubmit imageInfoSubmit:listImages){
						listImag.add(imageInfoSubmit.getFilenameOrUrl());
					}
				}
				imageMap.put(spu.getSpuId()+";"+spu.getSpuId(),listImag);
			}else{
				SkuDTO sku = new SkuDTO();
				sku.setId(UUIDGenerator.getUUID());
				sku.setSupplierId(supplierId);
				sku.setSpuId(item.getVariationInfo().getParentSku());
				sku.setSkuId(item.getSku());
				sku.setBarcode(item.getEAN());
				List<AttributeInfo> attributeInfolist = item.getAttributeList().getAttributeInfo();
				String size = null;
				String color = null;
				for(AttributeInfo attr:attributeInfolist){
					if("size".equals(attr.getName())){
						size = attr.getValue();
					}
					if("Color".equals(attr.getName())){
						color = attr.getValue();
					}
				
				}
				DistributionCenterInfoSubmit distributionCenterInfoSubmit = item.getDistributionCenterList().getDistributionCenterInfoSubmit();
				sku.setProductSize(size);
				sku.setStock(distributionCenterInfoSubmit.getQuantity());
				sku.setProductCode(item.getVariationInfo().getParentSku());
				sku.setMarketPrice(item.getPriceInfo().getRetailPrice());
				sku.setColor(color);
				sku.setProductName(item.getTitle());
				sku.setProductDescription(item.getDescription());
//				sku.setSaleCurrency(item.getCurrency());
//				sku.setSalePrice("");
//				sku.setSupplierPrice(item.getPrice());
				skuList.add(sku);

			}
		}
		returnMap.put("sku", skuList);
		returnMap.put("spu", spuList);
		returnMap.put("image", imageMap);
		return returnMap;
	}
}
