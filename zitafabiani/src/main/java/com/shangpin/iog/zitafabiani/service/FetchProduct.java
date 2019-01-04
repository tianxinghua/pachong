package com.shangpin.iog.zitafabiani.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.iog.zitafabiani.dto.Item;
import com.shangpin.iog.zitafabiani.dto.ZitaSkuDto;
import com.shangpin.iog.zitafabiani.dto.ZitaSpuDto;
import com.shangpin.iog.zitafabiani.utils.CVSUtil;
import com.shangpin.iog.zitafabiani.utils.DownLoad;
import com.shangpin.product.AbsSaveProduct;

@Component("zitafabiani")
public class FetchProduct extends AbsSaveProduct{

	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	public static int day;
	private static String url = null;
	private static String local = null;
	
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		day = Integer.valueOf(bdl.getString("day"));
		url = bdl.getString("url");
		local = bdl.getString("local");
	}
	private static Gson gson = new Gson();

	public Map<String, Object> fetchProductAndSave() {
		
		try {
			logger.info("=======开始下载文件=======");
			DownLoad.downFromNet(url, local);
			logger.info("=======下载结束=======");
			File file = new File(local);
			List<Item> lists = CVSUtil.readCSV(file, Item.class, ';');
			logger.info("======转化对象成功========");
			for(Item item:lists){
				ZitaSkuDto sku = new ZitaSkuDto();
                String skuId = item.getSupplier_sku_no()+"-"+item.getProduct_size();
                sku.setSkuId(skuId);
                sku.setSpuId(item.getSupplier_sku_no());
                sku.setMarketPrice(item.getMarker_price());
                sku.setSupplierPrice(item.getSupplier_price());
                sku.setProductDescription(item.getProduct_description());
                sku.setSaleCurrency(item.getCurrency());
                sku.setProductSize(item.getProduct_size());
                sku.setStock(item.getStock());
              //保存图片
                List<String> list = new ArrayList<String>();
                list.add(item.getProduct_url1());
                list.add(item.getProduct_url2());
                list.add(item.getProduct_url3());
              //保存SPU
                ZitaSpuDto spu = new ZitaSpuDto();
                spu.setSpuId(item.getSupplier_sku_no());
                spu.setCategoryGender(item.getGender());
                spu.setCategoryName(item.getCategory_name());
                spu.setBrandName(item.getBrand_name());
                spu.setSeasonName(item.getSeason());
                spu.setMaterial(item.getMaterial());
                spu.setProductOrigin(item.getProduct_origin());
                spu.setColor(item.getProduct_color());
                spu.setProductModel(item.getProduct_model());
                spu.setSpuName(item.getProduct_name());
                spu.setPictures(list);
                List<ZitaSkuDto> skus = new ArrayList<>();
                skus.add(sku);
                spu.setSkus(skus);
                supp.setData(gson.toJson(spu));  
				pushMessage(null);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		logger.info("抓取结束");
		return null;
	}
}

