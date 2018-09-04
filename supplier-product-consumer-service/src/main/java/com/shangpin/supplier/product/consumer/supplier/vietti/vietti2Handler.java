package com.shangpin.supplier.product.consumer.supplier.vietti;

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
import com.shangpin.supplier.product.consumer.supplier.vietti.dto.CsvDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Title:StefaniaHandler.java </p>
 * <p>Description: stefania供货商原始数据处理器</p>
 * <p>Company: www.shangpin.com</p>
 * @author lubaijang
 * @date 2016年12月8日 上午11:36:22
 */
@Component("vietti2Handler")
@Slf4j
public class vietti2Handler implements ISupplierHandler{

	@Autowired
	private SupplierProductSaveAndSendToPending supplierProductSaveAndSendToPending;
	@Autowired
	private PictureHandler pictureHandler;
	@Autowired
	private SupplierProductMongoService mongoService;
	@Override
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
		try {
			if(!StringUtils.isEmpty(message.getData())){
				CsvDTO jsonObject = JsonUtil.deserialize(message.getData(), CsvDTO.class);
				String supplierId = message.getSupplierId();
				HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
				List<Image> images = converImage(supplierId,jsonObject);
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
	 * @param
	 * @return
	 */
	private List<Image> converImage(String supplierId, CsvDTO jsonObject){
//		String supplierSpuNo =jsonObject.getId().substring(0,jsonObject.getId().lastIndexOf("-"));
//		Map<String,String> existPics = pictureHandler.checkPicExistsOfSpu(supplierId, supplierSpuNo);
		String picture0 = jsonObject.getMain_image();

		Map<String,String> urlMap = new HashMap<>();
		List<Image> imagesList = new ArrayList<>();
		if(org.apache.commons.lang.StringUtils.isNotBlank(picture0)){
			log.info("monnalisa pic : "+picture0+" 将推送");
			Image image = new Image();
			image.setUrl(picture0);
			urlMap.put(picture0,"");
			imagesList.add(image);
		}else{

		}

		if(null!=jsonObject.getMain_image()&&org.apache.commons.lang.StringUtils.isNotBlank(jsonObject.getMain_image())){

			String[] picUrlArray = jsonObject.getMain_image().split(",");
            if(null!=picUrlArray){
            	for(int i= 0 ;i<picUrlArray.length;i++){
            		if(!urlMap.containsKey(picUrlArray[i])){
						log.info("monnalisa pic:"+picUrlArray[i]+" 将推送");
						Image image = new Image();
						image.setUrl(picUrlArray[i]);
						imagesList.add(image);
					}
				}
			}

		}

		return imagesList;
	}

	/**
	 * 将stefania原始数据转换成hub spu
	 * @param supplierId 供应商门户编号

	 * @return
	 */
	public boolean convertSpu(String supplierId, CsvDTO ob, HubSupplierSpuDto hubSpu, String data){
		if(null != ob && ob != null){
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(ob.getSku().substring(0,ob.getSku().lastIndexOf("-")));
			hubSpu.setSupplierSpuModel(ob.getConfigurable_sku().substring(0,ob.getConfigurable_sku().lastIndexOf("-")));
			hubSpu.setSupplierSpuName(ob.getItem_code());
			hubSpu.setSupplierSpuColor(ob.getColor());
			hubSpu.setSupplierGender("");
			hubSpu.setSupplierCategoryname("");
			hubSpu.setSupplierCategoryname("");



			return true;
        }
		else{
        return false;
    }
	}
	public String SubCategory(String s){
		String[] sp = s.split(">");
		String k="";
		StringBuffer buffer = new StringBuffer();
		ArrayList<Object> list = new ArrayList<>();
		for (int i = 0; i < sp.length; i++) {
			if (sp[i].contains("Summer")|| sp[i].contains("Winter")){
				String string = sp[i].toString();
				sp[i].replace(string,"");
			}else {
				k=buffer.append(sp[i]).toString();
			}
		}
		String sgirl="";
		if (k.contains("Girl")){
			String s1 = k.replaceAll("Girl", "").replace(" ","");
			sgirl= "Girl" + " " + s1;
			return sgirl;
		}
		String  sboy="";
		if (k.contains("Boy")){
			String s2 = k.replaceAll("Boy", "").replace(" ","");
			sboy="Boy"+" "+s2;
			return sboy;
		}
		return  k;
	}
	/**
	 * 将stefania原始数据转换成hub sku
	 * @param supplierId
	 * @param supplierSpuId
	 * @param
	 * @param hubSku
	 * @return
	 */
	public boolean convertSku(String supplierId, Long supplierSpuId, CsvDTO ob, HubSupplierSkuDto hubSku){
		if(null != ob){
			hubSku.setSupplierSpuId(supplierSpuId);
			hubSku.setSupplierId(supplierId);
			hubSku.setSupplierSkuNo(ob.getSku());
			hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(ob.getPrice())));
			hubSku.setSalesPrice(new BigDecimal(StringUtil.verifyPrice(ob.getSpecial_price())));
			hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(ob.getPrice())));
			hubSku.setSupplierBarcode(ob.getBarcode());
			if(!StringUtils.isEmpty(ob.getSize())){
				hubSku.setSupplierSkuSize(ob.getSize());
			}

			hubSku.setStock(ob.getStock());
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
