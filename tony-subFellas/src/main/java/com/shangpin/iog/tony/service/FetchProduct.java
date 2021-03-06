package com.shangpin.iog.tony.service;

/**
 * Created by wang on 2015/9/21.
 */

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.iog.tony.common.Constant;
import com.shangpin.iog.tony.common.MyJsonClient;
import com.shangpin.iog.tony.common.StringUtil;
import com.shangpin.iog.tony.dto.Data;
import com.shangpin.iog.tony.dto.Items;
import com.shangpin.iog.tony.dto.ReturnObject;

import org.apache.commons.collections.functors.ExceptionClosure;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by wangyuzhi on 2015/9/10.
 */
@Component("tony")
public class FetchProduct {

    private static Logger log = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    @Autowired
    ProductFetchService productFetchService;
    private MyJsonClient jsonClient = new MyJsonClient();
    private String itemsJson;
    private String categoriesJson;
	@Autowired
	ProductSearchService productSearchService;
    private List<Items> itemsList= new ArrayList();
    /**
     * fetch product and save into db
     */
    public void fetchProductAndSave() {
        //获取数据
        itemsJson =       jsonClient.httpPostOfJson(Constant.ITEMS_INPUT, Constant.ITEMS_URL);
        categoriesJson = jsonClient.httpPostOfJson(Constant.CATEGORIES_INPUT, Constant.CATEGORIES_URL);
        //解析数据
//        Items[] array = new Gson().fromJson(new StringUtil().getItemsArray(itemsJson),
//                new TypeToken<Items[]>() {}.getType());
        this.getStock(itemsJson);
        //保存数据
        messMappingAndSave();
    }

    private  void getStock(String itemsJson){

        ReturnObject obj = new Gson().fromJson(itemsJson, ReturnObject.class);
        if(obj!=null){
            String next = obj.getNext_step();
            Data data = obj.getData();
            if(data!=null){
                Items[] array = data.getInventory();
                if(array!=null){
                    for(Items item:array){
                        itemsList.add(item);
                    }
                }
                if(next!=null){
                    getStock(jsonClient.httpPostOfJson("{\"merchantId\":\"56e95bb7f868647d13873d98\",\"token\":\"ec1ebaa88fbcb0e5fa02ad42bdcc5be4\",\"step\":\""+next+"\"}", Constant.ITEMS_URL));
                }
            }
        }
    }


    /**
     * message mapping and save into DB
     */
    private void messMappingAndSave() {
    	
    	Date startDate, endDate = new Date();
		startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate, Constant.day
				* -1, "D");
		// 获取原有的SKU 仅仅包含价格和库存
		Map<String, SkuDTO> skuDTOMap = new HashedMap();
		try {
			skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(
					Constant.SUPPLIER_ID, startDate, endDate);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
    	
        //get tony Category data
        String skuId = "";
        String spuId = "";
        String material = "";
        Map<String,String> map = new HashMap<>();

        Map<String,String> categoryMap = StringUtil.getCategoryMap(categoriesJson);

/*        for(Items item:array){
            skuId = item.get_id();
            map.put(StringUtil.getSkuID(skuId),"");
        }*/
        System.out.println(map.size());
        String categoryId ="";
        for(Items item:itemsList){
            skuId = item.getSku();
            spuId = StringUtil.getSpuId(skuId);
            if("522359926251_730-XXL".equals(skuId)){
            	System.out.println("");
            }
            //
            SkuDTO sku  = new SkuDTO();
            try {
                sku.setId(UUIDGenerator.getUUID());
                sku.setSupplierId(Constant.SUPPLIER_ID);
                sku.setSpuId(spuId);
                sku.setSkuId(skuId);
                sku.setProductSize(StringUtil.getProductSize(skuId));
                sku.setMarketPrice(item.getStock_price());
                //sku.setSalePrice(item.getPurchase_price());
                //sku.setSupplierPrice(item.getAge());
                sku.setColor(item.getColor());
                sku.setProductDescription(item.getDesc());
                sku.setStock(item.getQty());
                sku.setBarcode(item.getBarcode());
                sku.setProductCode(StringUtil.getProductCode(skuId));
                sku.setSaleCurrency(item.getCur());
                productFetchService.saveSKU(sku);

            

            } catch (ServiceException e) {
                try {
                    if(e.getMessage().equals("数据插入失败键重复")){
                        //update
                        productFetchService.updatePriceAndStock(sku);
                    } else{
                        e.printStackTrace();
                    }

                } catch (ServiceException e1) {
                    e1.printStackTrace();
                }
            } catch(Exception e){
                loggerError.error("tony 更新库存失败 " + e.getMessage());
            }
            
            
            if(null!=item.getImages()&& item.getImages().length>0&&StringUtils.isNotBlank(item.getImages()[0])){
                String[] picArray = item.getImages();
                productFetchService.savePicture(Constant.SUPPLIER_ID, null, skuId, Arrays.asList(picArray));
            }
            
            
/*            if (StringUtil.getMaterial(item.getDesc()) == null || "".equals(StringUtil.getMaterial(item.getDesc()))){
                System.out.println(skuId+"======================================================");
                continue;
            }*/
            //System.out.println(sku.getSupplierId()+"------------------------------------------");
            SpuDTO spu = new SpuDTO();
            try {
                spu.setId(UUIDGenerator.getUUID());
                spu.setSupplierId(Constant.SUPPLIER_ID);
                spu.setSpuId(spuId);
                spu.setBrandName(item.getBrand());
//                String categoryId = StringUtil.getCategoryID(item.getCat_id().toString());

                List<Map<String,String>> catList = (ArrayList)item.getCat_ids();
                List<String> categoryIdList = new ArrayList<>();
                if(null!=catList&&catList.size()>0){
//                    categoryId =catList.get(0).get("$id");
                    for(Map<String,String> tempCategoryMap:catList){
                        categoryIdList.add(tempCategoryMap.get("$id"));
                    }
                }else{
                    continue;
                }

                spu.setCategoryId(categoryIdList.get(0));

                spu.setCategoryName(StringUtil.getCategoryNameByID(categoryIdList, categoryMap));

                //spu.setSpuName(item.getTitle_en());
                spu.setSeasonId(item.getSeason());
                String desc = item.getDesc();
                material = StringUtil.getMaterial(desc);
                if ("".equals(material)){
                    material = StringUtil.getMaterial(item.getDesc_en());
                }
                
                String productOrigin = null;
            	int index = desc.lastIndexOf("Made in");
            	if(index!=-1){
            		desc = desc.substring(desc.lastIndexOf("Made in"));
            		if(desc.indexOf("<br>")!=-1){
            			productOrigin = desc.substring(0,desc.indexOf("<br>"));
            		}
            	}
                spu.setProductOrigin(productOrigin);
                spu.setMaterial(material);
                spu.setCategoryGender(item.getSex());
                //System.out.println(spuId+"======================================");
                productFetchService.saveSPU(spu);

            } catch (ServiceException e) {
            	try {
					productFetchService.updateMaterial(spu);
				} catch (ServiceException e1) {
					e1.printStackTrace();
				}
            }
        }
    }

}
