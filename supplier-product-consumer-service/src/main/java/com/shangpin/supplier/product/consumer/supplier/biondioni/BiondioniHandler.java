package com.shangpin.supplier.product.consumer.supplier.biondioni;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.google.gson.Gson;
import com.shangpin.supplier.product.consumer.conf.client.mysql.sku.bean.HubSupplierSku;
import com.shangpin.supplier.product.consumer.conf.client.mysql.spu.bean.HubSupplierSpu;
import com.shangpin.supplier.product.consumer.conf.stream.sink.message.SupplierProduct;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;
import com.shangpin.supplier.product.consumer.supplier.biondioni.dto.Article;
import com.shangpin.supplier.product.consumer.supplier.biondioni.dto.Modele;
import com.shangpin.supplier.product.consumer.supplier.biondioni.dto.QtTaille;
/**
 * <p>Title:BiondioniHandler </p>
 * <p>Description: 供应商biondioni数据处理器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月12日 下午2:14:06
 *
 */
@Component("biondioniHandler")
public class BiondioniHandler implements ISupplierHandler {

	@Override
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
		if(!StringUtils.isEmpty(message.getData())){
			Modele modele = new Gson().fromJson(message.getData(),Modele.class);
			List<Article> artList = modele.getArticleList();
			for(Article article : artList){
				HubSupplierSpu hubSpu = new HubSupplierSpu();
				boolean success = convertSpu(message.getSupplierId(), modele, article, hubSpu);
				if(success){
					//TODO save hubSpu
				}
				List<QtTaille> qtys = article.getTarifMagInternet().getList();
				for(QtTaille qty : qtys){
					HubSupplierSku hubSku = new HubSupplierSku();
					boolean skuSucc = convertSku(message.getSupplierId(), hubSpu.getSupplierSpuId(), modele, article, qty, hubSku);
					if(skuSucc){
						//TODO save hubSku
					}
				}				
			}
			
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
	public boolean convertSpu(String supplierId,Modele modele, Article article, HubSupplierSpu hubSpu){
		if(modele != null && article != null){
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(modele.getNumMdle()+article.getNumArti());
			hubSpu.setSupplierSpuModel(modele.getNumMdle()+article.getNumArti());
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
	public boolean convertSku(String supplierId, Long supplierSpuId, Modele modele, Article article,QtTaille qty,HubSupplierSku hubSku){
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
			hubSku.setSalesPrice(new BigDecimal(qty.getPrixVente()));
			hubSku.setSupplierSkuSize(size);
			hubSku.setStock(Integer.parseInt(qty.getQty()));
			return true;
		}else{
			
			return false;
		}
	}

}
