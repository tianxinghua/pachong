package com.shangpin.iog.biondini.service;

/**
 * Created by wang on 2015/9/21.
 */

import java.util.*;

import com.shangpin.iog.biondini.dao.*;
import com.shangpin.iog.biondini.util.DownloadAndReadCSV;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.biondini.util.SoapUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.EventProductService;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.product.AbsSaveProduct;

/**
 * Created by 赵根春 on 2015/12/25.
 */
@Component("biondini")
public class FetchProduct extends AbsSaveProduct {

	@Autowired
	ProductFetchService productFetchService;

	@Autowired
	EventProductService eventProductService;
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static int day;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));
	}

	public void sendAndSaveProduct(){
		handleData("spu", supplierId, day, null);
	}
	
	/**
	 * message mapping and save into DB
	 */
	public Map<String,Object> fetchProductAndSave() {
		
		Map<String,Object> returnMap = null;

		List<Product>  modelProductList = null ;
		try {
			modelProductList = DownloadAndReadCSV.readLocalCSV(Product.class,",");
		} catch (Exception e) {
			e.printStackTrace();
		}

		Map<String,Product> modelProductMap = new HashMap<>();
		if(null!=modelProductList&&modelProductList.size()>0){
			for(Product product:modelProductList ){

				modelProductMap.put(product.getSpuNo(),product);
			}
		}


		SoapUtil.getTableModeleAndArticle();
		// 获取商品list
		List<Modele> array = SoapUtil.getProductList();
		// Mode下的属性代码表
		List<IdTable> listIdTable = SoapUtil.getTableModele();
		//获取artcile下的代码表
		List<IdTable> listTableArtcile = SoapUtil.getTableArtcile();
		//获取商品库存
		Map<String,String> map = SoapUtil.getProductStockList();
		if (array != null) {
			logger.info("save start.......");
			for (Modele item : array) {
				List<Article> artList = item.getArticleList();
				String numMdle = item.getNumMdle();
				for (Article art : artList) {
					String numArti = null, spuId = null;
					numArti = art.getNumArti();
					spuId = numMdle + numArti;
					if(modelProductMap.containsKey(spuId)){
						art.setImageUrl(modelProductMap.get(spuId).getPicUrl());
						art.setSpuModel(modelProductMap.get(spuId).getSupplierSpuModel());
					}
					//取尺码
					List<QtTaille> ta = art.getTarifMagInternet().getList();
					for (QtTaille qt : ta) {
						SkuDTO sku = new SkuDTO();
						sku.setId(UUIDGenerator.getUUID());
						sku.setSupplierId(supplierId);
						sku.setSpuId(spuId);
						sku.setBarcode(qt.getCodeBarre());
						String size = qt.getTaille();
						if (size != null) {
							if (size.indexOf("½") > 0) {
								size = size.replace("½", "+");
							}
						} else {
							size = "A";
						}
						qt.setQty(map.get(spuId+"|"+size));
					}
					
					for (IdTable id : listIdTable) {
						boolean flag = false;
						if ("Rayon".equals(id.getNomTable())) {
							List<TableMdle> tbList = id.getDescription()
									.getTableMdle();
							for (TableMdle tb : tbList) {
								if (item.getRayon().equals(tb.getCode())) {
									item.setRayon(tb.getLibelle());
									flag = true;
									break;
								}
							}
						}
						if (flag) {
							break;
						}
					}
					for (IdTable id : listIdTable) {
						boolean flag1 = false;
						if ("Famille".equals(id.getNomTable())) {
							List<TableMdle> tbList = id.getDescription()
									.getTableMdle();
							for (TableMdle tb : tbList) {
								if (item.getFamille().equals(tb.getCode())) {
									item.setFamille(tb.getLibelle());
									flag1 = true;
									break;
								}
							}
						}
						if (flag1) {
							break;
						}
					}
					for (IdTable id : listTableArtcile) {
						boolean fl = false;
						if ("Matière".equals(id.getNomTable())) {
							List<TableArti> tbList = id.getDescription()
									.getTableArti();
							for (TableArti tb : tbList) {
								if (art.getMatière().equals(tb.getCode())) {
									art.setMatière(tb.getLibelle());
									fl = true;
									break;
								}
							}
						}
						if (fl) {
							break;
						}
					}
					String color = art.getCouleurPrincipale();
					for (IdTable id : listTableArtcile) {
						boolean fla = false;
						if ("Couleur-Principale".equals(id
								.getNomTable())) {
							List<TableArti> tbArtList = id
									.getDescription().getTableArti();
							for (TableArti tb : tbArtList) {
								if (color.equals(tb.getCode())) {
									art.setCouleurPrincipale(tb.getLibelle());
									fla = true;
									break;
								}
							}
						}
						if (fla) {
							break;
						}
					}
				}
				Gson gson = new Gson();
				//发送到队列
				supp.setData(gson.toJson(item));
				pushMessage(gson.toJson(supp));
			}
		}
		logger.info("save end.......");
		return returnMap;
	}


}
