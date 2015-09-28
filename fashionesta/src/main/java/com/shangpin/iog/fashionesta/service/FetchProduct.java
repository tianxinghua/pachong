package com.shangpin.iog.fashionesta.service;

import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.fashionesta.dto.FashionestaDTO;
import com.shangpin.iog.fashionesta.utils.DownloadAndReadCSV;
import com.shangpin.iog.service.ProductFetchService;

@Component("fashionesta")
public class FetchProduct {
	 	ResourceBundle bundle = ResourceBundle.getBundle("param");
		final Logger logger = Logger.getLogger(this.getClass());
	    private static Logger logMongo = Logger.getLogger("mongodb");
	   
	    @Autowired
	    private ProductFetchService productFetchService;
	    
	    String supplierId = bundle.getString("supplier");
	    public void fetchProductAndSave(){
	    	logMongo.info("save product starting....");
	    	//TODO 更改状态存储，不要忘了填币种
	    	try {
				List<FashionestaDTO> list = DownloadAndReadCSV.readLocalCSV();
				for (FashionestaDTO dto : list) {
					SkuDTO sku = new SkuDTO();
					SpuDTO spu = new SpuDTO();
				    ProductPictureDTO picture = new ProductPictureDTO();
	                String size = dto.getSIZE();
	                if(size.indexOf("½")>0){
	                    size=size.replace("½","+");
	                }
	                sku.setId(UUIDGenerator.getUUID());
	                sku.setSupplierId(supplierId);
	                sku.setSkuId(dto.getSUPPLIER_CODE()+"-"+size);
	                sku.setSpuId(dto.getSUPPLIER_CODE());
	                sku.setProductDescription(dto.getDESCRIPTION());
	                sku.setColor(dto.getCOLOR());
	                sku.setProductSize(size);
	                sku.setSaleCurrency(dto.getCURRENCY());
	                sku.setStock(dto.getSTOCK());
	                spu.setId(UUIDGenerator.getUUID());
	                spu.setSpuId(dto.getSUPPLIER_CODE());
	                spu.setSupplierId(supplierId);
	                spu.setProductOrigin(dto.getMADE());
	                picture.setSupplierId(supplierId);
	                picture.setId(UUIDGenerator.getUUID());
	                picture.setSkuId(dto.getSUPPLIER_CODE()+"-"+dto.getSIZE());
	                picture.setPicUrl(dto.getIMAGE_URL());
	                try {
	                    productFetchService.saveSPU(spu);
	                } catch (ServiceException e) {
	                    e.printStackTrace();
	                }
	                try {
	                    productFetchService.saveSKU(sku);
	                    productFetchService.savePictureForMongo(picture);
	                } catch (ServiceException e) {
	                    e.printStackTrace();
	                }
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	    	logMongo.info("save product over");
	    }
}
