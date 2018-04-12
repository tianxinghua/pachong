package com.shangpin.supplier.product.consumer.supplier.biondioni;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.shangpin.ephub.client.message.picture.body.SupplierPicture;
import com.shangpin.ephub.client.message.picture.image.Image;
import com.shangpin.supplier.product.consumer.supplier.coccolebimbi.dto.Item;
import com.shangpin.supplier.product.consumer.supplier.common.picture.PictureHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.message.original.body.SupplierProduct;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.supplier.product.consumer.exception.EpHubSupplierProductConsumerRuntimeException;
import com.shangpin.supplier.product.consumer.service.SupplierProductMongoService;
import com.shangpin.supplier.product.consumer.service.SupplierProductSaveAndSendToPending;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;
import com.shangpin.supplier.product.consumer.supplier.biondioni.dto.Article;
import com.shangpin.supplier.product.consumer.supplier.biondioni.dto.Modele;
import com.shangpin.supplier.product.consumer.supplier.biondioni.dto.QtTaille;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>Title:BiondioniHandler </p>
 * <p>Description: 供应商biondioni数据处理器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月12日 下午2:14:06
 *
 */
@Component("biondiniHandler")
@Slf4j
public class BiondioniHandler implements ISupplierHandler {
	
	@Autowired
	private SupplierProductSaveAndSendToPending supplierProductSaveAndSendToPending;
	@Autowired
	private SupplierProductMongoService mongoService;

	@Autowired
	private PictureHandler pictureHandler;

	@Override
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
		try {
			if(!StringUtils.isEmpty(message.getData())){
				Modele modele = JsonUtil.deserialize(message.getData(),Modele.class);
				String supplierId = message.getSupplierId();
				
				mongoService.save(supplierId, modele.getNumMdle(), modele);
				
				List<Article> artList = modele.getArticleList();
				for(Article article : artList){
					HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
					boolean success = convertSpu(supplierId, modele, article, hubSpu,message.getData());
					List<QtTaille> qtys = article.getTarifMagInternet().getList();
					List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
					for(QtTaille qty : qtys){
						HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
						boolean skuSucc = convertSku(supplierId, hubSpu.getSupplierSpuId(), modele, article, qty, hubSku);
						if(skuSucc){
							hubSkus.add(hubSku);
						}
					}
					SupplierPicture supplierPicture =  pictureHandler.initSupplierPicture(message, hubSpu, converImage(article.getImageUrl()));
					if(success){
						supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(),supplierId, message.getSupplierName(), hubSpu, hubSkus,supplierPicture);
					}
				}
				
			}
		} catch (Exception e) {
			log.error("biondioni异常："+e.getMessage(), e); 
		}
	}
	
	/**
	 * 将供应商原始dto转换成hub spu
	 * @param supplierId 供应商门户编号
	 * @param modele 供应商原始dto
	 * @param article 供应商原始dto
	 * @param hubSpu
	 * @return
	 */
	public boolean convertSpu(String supplierId,Modele modele, Article article, HubSupplierSpuDto hubSpu,String data) throws EpHubSupplierProductConsumerRuntimeException{
		if(modele != null && article != null){
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(modele.getNumMdle()+article.getNumArti());
			hubSpu.setSupplierSpuModel(StringUtils.isEmpty(article.getSpuModel())?modele.getNumMdle()+article.getNumArti():article.getSpuModel());
			hubSpu.setSupplierSpuName(modele.getNomFour());
			hubSpu.setSupplierSpuColor(article.getCouleurPrincipale());
			hubSpu.setSupplierGender(modele.getRayon());
			hubSpu.setSupplierCategoryname(modele.getFamille());
			hubSpu.setSupplierBrandname(modele.getNomFour());
			hubSpu.setSupplierSeasonname(article.getSaisonArticle());
			hubSpu.setSupplierMaterial(article.getMatière());
//			hubSpu.setSupplierOrigin(supplierOrigin);			
			return true;
		}else{
			
			return false;
		}
	}
	
	/**
	 * 将供应商原始dto转换成hub sku
	 * @param supplierId
	 * @param supplierSpuId
	 * @param modele
	 * @param article
	 * @param qty
	 * @param hubSku
	 * @return
	 */
	public boolean convertSku(String supplierId, Long supplierSpuId, Modele modele, Article article,QtTaille qty,HubSupplierSkuDto hubSku) throws EpHubSupplierProductConsumerRuntimeException{
		if(modele != null && article != null){
			hubSku.setSupplierId(supplierId);
			hubSku.setSupplierSpuId(supplierSpuId);
			String size = qty.getTaille();
			if (size != null) {
				if (size.indexOf("½") > 0) {
					size = size.replace("½", "+");
				}
			} else {
				size = "A";
			}
			hubSku.setSupplierSkuNo(modele.getNumMdle()+article.getNumArti()+ "|" + size);
			hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(qty.getPrixVente())));
//			hubSku.setSalesPrice(new BigDecimal(StringUtil.verifyPrice(qty.getPrixVente())));
			hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(qty.getPrixVenteSolde())));
			hubSku.setSupplierSkuSize(size);
			hubSku.setSupplierBarcode(qty.getCodeBarre());
			hubSku.setStock(StringUtil.verifyStock(qty.getQty()));
			return true;
		}else{
			
			return false;
		}
	}

	private List<Image> converImage(String  imageUrl) {
		List<Image> images = new ArrayList<Image>();
		if(!StringUtils.isEmpty(imageUrl)){

			String[] urlArray = imageUrl.split("\\|");
			if(null!=urlArray){
				for(String url :urlArray){
					Image image = new Image();
					image.setUrl(url);
					images.add(image);
				}
			}
		}
		return images;
	}

}
