package com.shangpin.iog.biondini.service;

/**
 * Created by wang on 2015/9/21.
 */

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.biondini.dao.Article;
import com.shangpin.iog.biondini.dao.IdTable;
import com.shangpin.iog.biondini.dao.Modele;
import com.shangpin.iog.biondini.dao.QtTaille;
import com.shangpin.iog.biondini.dao.TableArti;
import com.shangpin.iog.biondini.dao.TableMdle;
import com.shangpin.iog.biondini.util.SoapUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.EventProductService;
import com.shangpin.iog.service.ProductFetchService;

/**
 * Created by 赵根春 on 2015/12/25.
 */
@Component("biondini")
public class FetchProduct {

	@Autowired
	ProductFetchService productFetchService;

	@Autowired
	EventProductService eventProductService;
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
	}
	
	/**
	 * message mapping and save into DB
	 */
	public void messMappingAndSave() {
		
		//Mode下的属性代码表
		List<IdTable> listIdTable = SoapUtil.getTableModele();
		//
		List<IdTable> listTableArtcile = SoapUtil.getTableArtcile();
		
		Map map = SoapUtil.getProductStockList(null);
		
		//获取商品list
		List<Modele> array = SoapUtil.getProductList();
		
		if (array != null) {
			for (Modele item : array) {
				
				String numMdle,numArti,spuId = null;
				numMdle = item.getNumMdle();
				List<Article> artList = item.getArticleList();
				
				for(Article art :artList){
					
					numArti = art.getNumArti();
					spuId= numMdle + numArti;
					SpuDTO spu = new SpuDTO();
					try {
						spu.setId(UUIDGenerator.getUUID());
						spu.setSupplierId(supplierId);
						spu.setSpuId(spuId);
						spu.setBrandName(item.getNomFour());
						
						for(IdTable id :listIdTable){
							boolean flag = false;
							if("Rayon".equals(id.getNomTable())){
								List<TableMdle> tbList = id.getDescription().getTableMdle();
								for(TableMdle tb : tbList){
									if(item.getRayon().equals(tb.getCode())){
										spu.setCategoryGender(tb.getLibelle());
										flag=true;
										break;
									}
								}
							}
							if(flag){
								break;	
							}
						}
						for(IdTable id :listIdTable){
							boolean flag1 = false;
							if("Famille".equals(id.getNomTable())){
								List<TableMdle> tbList = id.getDescription().getTableMdle();
								for(TableMdle tb : tbList){
									if(item.getFamille().equals(tb.getCode())){
										spu.setCategoryName(tb.getLibelle());
										flag1=true;
										break;
									}
								}
							}
							if(flag1){
								break;	
							}
						}
//						listIdTable.get(0);
//						spu.setSpuName(item.getProductName());
						for(IdTable id :listTableArtcile){
							boolean fl = false;
							if("Matière".equals(id.getNomTable())){
								List<TableArti> tbList = id.getDescription().getTableArti();
								for(TableArti tb : tbList){
									if(art.getMatière().equals(tb.getCode())){
										spu.setMaterial(tb.getLibelle());
										fl = true;
										break;
									}
								}
							}
							if(fl){
								break;
							}
						}
						spu.setSeasonId(art.getSaisonArticle());
						productFetchService.saveSPU(spu);
					} catch (Exception e) {
						try {
							productFetchService.updateMaterial(spu);
						} catch (ServiceException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					List<QtTaille> ta = art.getTarifMagInternet().getList();
					for(QtTaille qt : ta){
						SkuDTO sku = new SkuDTO();
						try {
							

							
							
							sku.setId(UUIDGenerator.getUUID());
							sku.setSupplierId(supplierId);
							sku.setSpuId(spuId);
							String size = qt.getTaille();
							if(size!=null){
								if(size.indexOf("½")>0){
									size = size.replace("½","+");	
								}
							}else{
								size = "A";
							}
							String color = art.getCouleurPrincipale();
							for(IdTable id :listTableArtcile){
								boolean fla = false;
								if("Couleur-Principale".equals(id.getNomTable())){
									List<TableArti> tbArtList = id.getDescription().getTableArti();
									for(TableArti tb : tbArtList){
										if(color.equals(tb.getCode())){
											sku.setColor(tb.getLibelle());
											fla = true;
											break;
										}
									}
								}
								if(fla){
									break;
								}
							}
							sku.setSkuId(spuId+"|"+size);
							sku.setBarcode(item.getCodMdle());
							sku.setProductSize(size);
							if(map.get(sku.getSkuId())==null){
								sku.setStock("0");
							}else{
								sku.setStock(sku.getSkuId());
							}
							sku.setProductCode(spuId);
//							sku.setSalePrice(item.getSalePrice());
							sku.setMarketPrice(qt.getPrixVente());
//							sku.setSupplierPrice(item.getSupplierPrice());
//							sku.setProductName(item.getProductName());
//							sku.setProductDescription(item.getProductDescription());
//							sku.setSaleCurrency(item.getSaleCurrency());
							sku.setBarcode(art.getCodArti());
							productFetchService.saveSKU(sku);
						} catch (ServiceException e) {
							if (e.getMessage().equals("数据插入失败键重复")) {
								try {
									productFetchService.updatePriceAndStock(sku);
								} catch (ServiceException e1) {
									e1.printStackTrace();
								}
							} else {
								e.printStackTrace();
							}

						}
					}
					
				}
					
				}
			}
		}
	}
