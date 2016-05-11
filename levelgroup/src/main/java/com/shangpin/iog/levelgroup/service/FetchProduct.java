package com.shangpin.iog.levelgroup.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.levelgroup.dto.Product;
import com.shangpin.iog.levelgroup.util.MyTxtUtil;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.product.AbsSaveProduct;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by wangyuzhi on 2015/11/13.
 */
@Component("levelgroup")
public class FetchProduct extends AbsSaveProduct{
    private static Logger logMongo = Logger.getLogger("mongodb");
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static String supplierId;
//    private static int day;
    @Autowired
    ProductFetchService productFetchService;
    @Autowired
	ProductSearchService productSearchService;

    private static ResourceBundle bdl=null;
    static {
        if(null==bdl)
            bdl= ResourceBundle.getBundle("conf");
            supplierId = bdl.getString("supplierId");
//            day = Integer.valueOf(bdl.getString("day"));
    }
    /**
     * 主处理
     */
    public Map<String, Object> fetchProductAndSave(){
    	
    	Map<String, Object> returnMap = new HashMap<String, Object>();
		List<SkuDTO> skuList = new ArrayList<SkuDTO>();
		List<SpuDTO> spuList = new ArrayList<SpuDTO>();
		Map<String,List<String>> imageMap = new HashMap<String, List<String>>();
    	
        //download
        try {
            MyTxtUtil.txtDownload();
        } catch (MalformedURLException e) {
            loggerError.error("拉取数据失败！");
            e.printStackTrace();
        }
        //read .csv file
        List<Product> list = null;
        try {
            list = MyTxtUtil.readTXTFile();
        } catch (Exception e) {
            loggerError.error("解析文件失败！");
            e.printStackTrace();
        }
        logger.info("解析数据条数："+list.size());
        messMappingAndSave(list,skuList,spuList,imageMap);
        
        returnMap.put("sku", skuList);
		returnMap.put("spu", spuList);
		returnMap.put("image", imageMap);
		return returnMap;
    }
    /**
     * 映射数据并保存
     */
    private void messMappingAndSave(List<Product> list,List<SkuDTO> skuList,List<SpuDTO> spuList,Map<String,List<String>> imageMap) {
//    	Date startDate,endDate= new Date();
//		startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate,day*-1,"D");
//		//获取原有的SKU 仅仅包含价格和库存
//		Map<String,SkuDTO> skuDTOMap = new HashedMap();
//		try {
//			skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(supplierId,startDate,endDate);
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		}
        for(Product product:list) {
            ///////////////////////////////保存SKU//////////////////////////////////
            SkuDTO sku = new SkuDTO();
//            String spuId = product.getVARIANT_SKU().substring(0, 10);
            String spuId = product.getMASTER_SKU();
            try {
                sku.setId(UUIDGenerator.getUUID());
                sku.setSupplierId(supplierId);
                sku.setSpuId(spuId);
                sku.setSkuId(product.getVARIANT_SKU());
                sku.setProductSize(product.getSIZE());
                sku.setMarketPrice(product.getPRICE());
                sku.setSalePrice(product.getSALEPRICE());
//                sku.setSupplierPrice(product.getPRICE());
                sku.setColor(product.getCOLOR());
                sku.setProductDescription(product.getDESCRIPTION());
                sku.setStock(product.getSTOCK_LEVEL());
                sku.setProductCode(spuId);
                sku.setSaleCurrency("euro");
                sku.setBarcode("");
                
                skuList.add(sku);
                
//                if(skuDTOMap.containsKey(sku.getSkuId())){
//    				skuDTOMap.remove(sku.getSkuId());
//    			}
//                productFetchService.saveSKU(sku);

                ///////////////////////////////////保存图片///////////////////////////////////
//            for(int i = 0;i<2;i++){
//                ProductPictureDTO dto  = new ProductPictureDTO();
//                String picUrl = product.getIMAGEURL();
//                dto.setPicUrl(picUrl);
//                dto.setSupplierId(supplierId);
//                dto.setId(UUIDGenerator.getUUID());
//                dto.setSkuId(product.getVARIANT_SKU());
//                try {
//                    productFetchService.savePictureForMongo(dto);
//                } catch (ServiceException e) {
//                    System.out.println("savePictureForMongo异常");
//                    e.printStackTrace();
//                }
                if(StringUtils.isNotBlank(product.getIMAGEURL())){
                    String[] picArray = product.getIMAGEURL().split("\\|");
//                    productFetchService.savePicture(supplierId, null, product.getVARIANT_SKU(), Arrays.asList(picArray));
                    imageMap.put(sku.getSkuId()+";"+sku.getProductCode(), Arrays.asList(picArray));
                }
//            }
                ///////////////////////////////////保存SPU/////////////////////////////////

            } catch (Exception e) {
//                try {
//                    if (e.getMessage().equals("数据插入失败键重复")) {
//                        System.out.println("saveSKU数据插入失败键重复");
//                        //更新价格和库存
//                        productFetchService.updatePriceAndStock(sku);
//                    } else {
//                        System.out.println("saveSKU异常");
//                        e.printStackTrace();
//                    }
//                } catch (ServiceException e1) {
//                    System.out.println("updatePriceAndStock异常");
//                    e1.printStackTrace();
//                }
            	loggerError.error(e); 
            }



            SpuDTO spu = new SpuDTO();
            try {

                spu.setId(UUIDGenerator.getUUID());
                spu.setSupplierId(supplierId);
                spu.setSpuId(spuId);
                spu.setSpuName(product.getNAME());
                spu.setBrandName(product.getMANUFACTURER());
                spu.setCategoryName(product.getADVERTISERCATEGORY());
                //spu.setSeasonId(skuDto.getSKU());
                spu.setSeasonName(product.getSEASON());
                spu.setMaterial(product.getMATERIAL());
                spu.setCategoryGender(product.getGENDER());
                spu.setProductOrigin("Spain");
//                productFetchService.saveSPU(spu);
                
                spuList.add(spu);
                
            } catch (Exception e) {
//                System.out.println("saveSPU异常");
//                e.printStackTrace();
            	loggerError.error(e); 
            }
        }
    }
}
