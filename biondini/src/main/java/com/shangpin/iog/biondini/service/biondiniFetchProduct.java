package com.shangpin.iog.biondini.service;

/**
 * Created by wang on 2015/9/21.
 */

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.app.AppContext;
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
import com.shangpin.product.AbsSaveProduct;

/**
 * Created by 赵根春 on 2015/12/25.
 */
@Component("biondini")
public class biondiniFetchProduct extends AbsSaveProduct {

	@Autowired
	ProductFetchService productFetchService;
	private static String picpath;
	public static int day;
	public static int max;
	
	@Autowired
	EventProductService eventProductService;
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		
		day = Integer.valueOf(bdl.getString("day"));
		max = Integer.valueOf(bdl.getString("max"));
		picpath = bdl.getString("picpath");
	}

	/**
	 * message mapping and save into DB
	 */
	public Map<String, Object> fetchProductAndSave() {

		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<SkuDTO> skuList = new ArrayList<SkuDTO>();
		List<SpuDTO> spuList = new ArrayList<SpuDTO>();
		Map<String, List<String>> imageMap = new HashMap<String, List<String>>();
		// 获取商品list
		List<Modele> array = SoapUtil.getProductList();
		SoapUtil.getTableModeleAndArticle();
		// Mode下的属性代码表
		List<IdTable> listIdTable = SoapUtil.getTableModele();
		// 获取artcile下的代码表
		List<IdTable> listTableArtcile = SoapUtil.getTableArtcile();
		// 获取商品库存
		Map<String, String> map = SoapUtil.getProductStockList();
		if (array != null) {
			for (Modele item : array) {
				String numMdle = null;
				numMdle = item.getNumMdle();
				List<Article> artList = item.getArticleList();
				for (Article art : artList) {
					String prductName = null, numArti = null, spuId = null;
					numArti = art.getNumArti();
					spuId = numMdle + numArti;
					SpuDTO spu = new SpuDTO();
					spu.setId(UUIDGenerator.getUUID());
					spu.setSupplierId(supplierId);
					spu.setSpuId(spuId);
					spu.setBrandName(item.getNomFour());
					if (item.getNomFour() != null) {
						prductName = item.getNomFour();
					}
					for (IdTable id : listIdTable) {
						boolean flag = false;
						if ("Rayon".equals(id.getNomTable())) {
							List<TableMdle> tbList = id.getDescription()
									.getTableMdle();
							for (TableMdle tb : tbList) {
								if (item.getRayon().equals(tb.getCode())) {
									spu.setCategoryGender(tb.getLibelle());
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
									spu.setCategoryName(tb.getLibelle());
									prductName += "," + tb.getLibelle();
									flag1 = true;
									break;
								}
							}
						}
						if (flag1) {
							break;
						}
					}
					// listIdTable.get(0);
					// spu.setSpuName(item.getProductName());
					for (IdTable id : listTableArtcile) {
						boolean fl = false;
						if ("Matière".equals(id.getNomTable())) {
							List<TableArti> tbList = id.getDescription()
									.getTableArti();
							for (TableArti tb : tbList) {
								if (art.getMatière().equals(tb.getCode())) {
									spu.setMaterial(tb.getLibelle());
									fl = true;
									break;
								}
							}
						}
						if (fl) {
							break;
						}
					}
					spu.setSeasonId(art.getSaisonArticle());
					spuList.add(spu);

					List<QtTaille> ta = art.getTarifMagInternet().getList();
					for (QtTaille qt : ta) {
						SkuDTO sku = new SkuDTO();

						sku.setId(UUIDGenerator.getUUID());
						sku.setSupplierId(supplierId);
						sku.setSpuId(spuId);
						String size = qt.getTaille();
						if (size != null) {
							if (size.indexOf("½") > 0) {
								size = size.replace("½", "+");
							}
						} else {
							size = "A";
						}
						String color = art.getCouleurPrincipale();
						for (IdTable id : listTableArtcile) {
							boolean fla = false;
							if ("Couleur-Principale".equals(id.getNomTable())) {
								List<TableArti> tbArtList = id.getDescription()
										.getTableArti();
								for (TableArti tb : tbArtList) {
									if (color.equals(tb.getCode())) {
										sku.setColor(tb.getLibelle());
										fla = true;
										break;
									}
								}
							}
							if (fla) {
								break;
							}
						}
						sku.setSkuId(spuId + "|" + size);
						sku.setBarcode(item.getCodMdle());
						sku.setProductSize(size);
						if (map.get(sku.getSkuId()) == null) {
							sku.setStock("0");
						} else {
							sku.setStock(map.get(sku.getSkuId()));
						}
						sku.setProductCode(spuId);
						sku.setSalePrice(qt.getPrixVente());
						sku.setProductName(prductName);
						// sku.setSaleCurrency(item.getSaleCurrency());
						sku.setBarcode(art.getCodArti());
						skuList.add(sku);
					}
				}
			}
		}
		returnMap.put("sku", skuList);
		returnMap.put("spu", spuList);
		returnMap.put("image", imageMap);
		return returnMap;

	}
	private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
	public static void main(String[] args) throws Exception {
	  	//加载spring
        loadSpringContext();
        biondiniFetchProduct stockImp =(biondiniFetchProduct)factory.getBean("biondini");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		stockImp.handleData("spu", supplierId, day, picpath);
	}
}
