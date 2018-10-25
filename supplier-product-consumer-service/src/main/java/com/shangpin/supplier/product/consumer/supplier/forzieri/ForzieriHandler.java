package com.shangpin.supplier.product.consumer.supplier.forzieri;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.message.original.body.SupplierProduct;
import com.shangpin.ephub.client.message.picture.body.SupplierPicture;
import com.shangpin.ephub.client.message.picture.image.Image;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.supplier.product.consumer.service.SupplierProductMongoService;
import com.shangpin.supplier.product.consumer.service.SupplierProductSaveAndSendToPending;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;
import com.shangpin.supplier.product.consumer.supplier.common.picture.PictureHandler;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;
import com.shangpin.supplier.product.consumer.supplier.forzieri.dto.CategoryMap;
import com.shangpin.supplier.product.consumer.supplier.forzieri.dto.CsvDTO;

import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.SchemaOutputResolver;

/**
 * <p>Title:StefaniaHandler.java </p>
 * <p>Description: stefania供货商原始数据处理器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijang
 * @date 2016年12月8日 上午11:36:22
 */
@Component("forzieri")
@Slf4j
public class ForzieriHandler implements ISupplierHandler{
	
	@Autowired
	private SupplierProductSaveAndSendToPending supplierProductSaveAndSendToPending;
	@Autowired
	private PictureHandler pictureHandler;
	@Autowired
	private SupplierProductMongoService mongoService;
	Map<String, String> categoryMap = new CategoryMap(new HashMap<String,String>()).getCategoryMap();
	@Override
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
		try {
			if(!StringUtils.isEmpty(message.getData())){
				CsvDTO jsonObject = JsonUtil.deserialize(message.getData(), CsvDTO.class);
				String supplierId = message.getSupplierId();
				HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
				List<Image> images = null;
                Image image1 = new Image();
                Image image2 = new Image();
                Image image3 = new Image();
                Image image4 = new Image();
                Image image5 = new Image();
                Image image6 = new Image();
                String picture0 = jsonObject.getVistaImagel0();
                String picture1 = jsonObject.getVistaImagel1();
                String picture2 = jsonObject.getVistaImagel2();
                String picture3 = jsonObject.getVistaImagel3();
                String picture4 = jsonObject.getVistaImagel4();
                String picture5 = jsonObject.getVistaImagel5();
                if(org.apache.commons.lang.StringUtils.isNotBlank(picture0)) {
                    image1.setUrl(picture0);
                    images.add(image1);
                }
                if(org.apache.commons.lang.StringUtils.isNotBlank(picture1)) {
                    image2.setUrl(picture1);
                    images.add(image2);
                }
                if(org.apache.commons.lang.StringUtils.isNotBlank(picture2)) {
                    image3.setUrl(picture2);
                    images.add(image3);
                }
                if(org.apache.commons.lang.StringUtils.isNotBlank(picture3)) {
                    image4.setUrl(picture3);
                    images.add(image4);
                }
                if(org.apache.commons.lang.StringUtils.isNotBlank(picture4)) {
                    image5.setUrl(picture4);
                    images.add(image5);
                }
                if(org.apache.commons.lang.StringUtils.isNotBlank(picture5)) {
                    image6.setUrl(picture5);
                    images.add(image6);
                }


                System.out.println("images:"+images);
//				if(null == images){
//					hubSpu.setIsexistpic(Isexistpic.NO.getIndex());
//				}else{
//					hubSpu.setIsexistpic(Isexistpic.YES.getIndex()); 
//				}
				boolean success = convertSpu(supplierId, jsonObject, hubSpu,message.getData());
				
				mongoService.save(supplierId, hubSpu.getSupplierSpuNo(), jsonObject);
				
				List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
				HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
				boolean skuSucc = convertSku(supplierId, hubSpu.getSupplierSpuId(), jsonObject, hubSku);
				if(skuSucc){
					hubSkus.add(hubSku);
				}
				//处理图片
				SupplierPicture supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu, images);
                log.info("supplierPicture对象的值为==================================================："+supplierPicture);
                System.out.println("supplierPicture对象的值为==================================================："+supplierPicture );
				if(success){
					supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(),supplierId, message.getSupplierName(), hubSpu, hubSkus,supplierPicture);
				}
			}	
		} catch (Exception e) {
			log.error("stefania异常："+e.getMessage(),e); 
		}		
		
	}
	
	/**
	 * stefania处理图片
	/// * @param stefPicture
	 * @return
	 */
	private List<Image> converImage(String supplierId,CsvDTO jsonObject){
		String supplierSpuNo =jsonObject.getProduct_id();
        System.out.println();
		Map<String,String> existPics = pictureHandler.checkPicExistsOfSpu(supplierId, supplierSpuNo);
		String picture0 = jsonObject.getVistaImagel0();
		String picture1 = jsonObject.getVistaImagel1();
		String picture2 = jsonObject.getVistaImagel2();
		String picture3 = jsonObject.getVistaImagel3();
		String picture4 = jsonObject.getVistaImagel4();
		String picture5 = jsonObject.getVistaImagel5();
		List<Image> images = new ArrayList<Image>();
		if(org.apache.commons.lang.StringUtils.isNotBlank(picture0)&&!existPics.containsKey(picture0)){
			log.info("forzieri "+supplierSpuNo +"======" +picture0+" 将推送");
            System.out.println("");
			Image image = new Image();
			image.setUrl(picture0);
			images.add(image);
		}else{
			log.info("XXXXXXXXX forzieri "+supplierSpuNo+"====="+picture0+" 已存在XXXXXXXXXXXX");
		}
		if(org.apache.commons.lang.StringUtils.isNotBlank(picture1)&&!existPics.containsKey(picture1)){
			log.info("forzieri "+ supplierSpuNo+"====="+picture1+" 将推送");
			Image image = new Image();
			image.setUrl(picture1);
			images.add(image);
		}else{
			log.info("XXXXXXXXX forzieri "+supplierSpuNo+"====="+picture1+" 已存在XXXXXXXXXXXX");
		}
		if(org.apache.commons.lang.StringUtils.isNotBlank(picture2)&&!existPics.containsKey(picture2)){
			log.info("forzieri "+supplierSpuNo+"====="+picture2+" 将推送");
			Image image = new Image();
			image.setUrl(picture2);
			images.add(image);
		}else{
			log.info("XXXXXXXXX forzieri "+supplierSpuNo+"====="+picture2+" 已存在XXXXXXXXXXXX");
		}
		if(org.apache.commons.lang.StringUtils.isNotBlank(picture3)&&!existPics.containsKey(picture3)){
			log.info("forzieri "+supplierSpuNo+"====="+picture3+" 将推送");
			Image image = new Image();
			image.setUrl(picture3);
			images.add(image);
		}else{
			log.info("XXXXXXXXX forzieri "+supplierSpuNo+"====="+picture3+" 已存在XXXXXXXXXXXX");
		}
		if(org.apache.commons.lang.StringUtils.isNotBlank(picture4)&&!existPics.containsKey(picture4)){
			log.info("forzieri "+picture4+" 将推送");
			Image image = new Image();
			image.setUrl(picture4);
			images.add(image);
		}else{
			log.info("XXXXXXXXX forzieri "+supplierSpuNo+"====="+picture4+" 已存在XXXXXXXXXXXX");
		}
		if(org.apache.commons.lang.StringUtils.isNotBlank(picture5)&&!existPics.containsKey(picture5)){
			log.info("forzieri "+supplierSpuNo+"====="+picture5+" 将推送");
			Image image = new Image();
			image.setUrl(picture5);
			images.add(image);
		}else{
			log.info("XXXXXXXXX forzieri "+supplierSpuNo+"====="+picture5+" 已存在XXXXXXXXXXXX");
		}
		return images;
	}

	/**
	 * 将stefania原始数据转换成hub spu
	 * @param supplierId 供应商门户编号
	 //* @param  stefProduct stef 原始dto
	 //* @param  stefItem stef 原始dto
	 * @param hubSpu hub spu
	 * @return
	 */
	public boolean convertSpu(String supplierId,CsvDTO ob,HubSupplierSpuDto hubSpu,String data){
		if(null != ob && ob != null){
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(ob.getProduct_id());
			hubSpu.setSupplierSpuModel(ob.getManufacturer_id());
			hubSpu.setSupplierSpuName(ob.getProduct_name());
			hubSpu.setSupplierSpuColor(ob.getColor());
			hubSpu.setSupplierGender(ob.getGender());
			String categoryid = ob.getCategory_ids().split("\\|")[0];
			hubSpu.setSupplierCategoryname(categoryMap.get(categoryid));
			hubSpu.setSupplierBrandname(ob.getBrand_name());
			hubSpu.setSupplierSeasonname(ob.getCollection());
			hubSpu.setSupplierMaterial(ob.getMaterial());
			String desc = ob.getDescription();
			String madeIn = null;
			if(desc!=null&&(desc.contains("Made in")||desc.contains("制造"))){
				String [] arr = null;
				if(desc.contains("。")){
					arr = desc.split("\\。");
				}else if(desc.contains(".")){
					arr = desc.split("\\.");			
				}
				if(arr!=null){
					for(String origin:arr){
						if(origin.contains("Made in")||origin.contains("制造")){
							madeIn = origin.replace("制造","").replace("Made in","").trim();
							break;
						}
					}	
				}
			}
			hubSpu.setSupplierOrigin(madeIn);
			hubSpu.setSupplierSpuDesc(desc);
			return true;
		}else{
			return false;
		}
	}
	/**
	 * 将stefania原始数据转换成hub sku
	 * @param supplierId
	 * @param supplierSpuId
	// * @param stefItem
	 * @param hubSku
	 * @return
	 */
	public boolean convertSku(String supplierId,Long supplierSpuId,CsvDTO ob,HubSupplierSkuDto hubSku){
		if(null != ob){
			hubSku.setSupplierSpuId(supplierSpuId);
			hubSku.setSupplierId(supplierId);
			hubSku.setSupplierSkuNo(ob.getSku());
			hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(ob.getList_price().trim())));
			hubSku.setSalesPrice(new BigDecimal(StringUtil.verifyPrice(ob.getSelling_price().trim())));
			hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(ob.getCost_price().trim())));
			hubSku.setSupplierBarcode(ob.getSku());
			/*if(!StringUtils.isEmpty(ob.getSize())){
				String size = ob.getSize();
				if(size.contains("(")){
					size = size.substring(0,size.indexOf("(")).trim();
				}
				hubSku.setSupplierSkuSize(ob.getSize());
			}else{*/
                hubSku.setSupplierSkuSize(ob.getSize());
            //}
			
			hubSku.setStock(StringUtil.verifyStock(ob.getQuantity()));
//			String stock = ob.getString("availability");
//			Pattern pattern = Pattern.compile("[0-9]*"); 
//		    Matcher isNum = pattern.matcher(stock);
//		    if(isNum.matches() ){
//		    	hubSku.setStock(StringUtil.verifyStock(stock));
//		    } 
			return true;
		}else{
			return false;
		}
	}
}
