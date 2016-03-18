package com.shangpin.product;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.ImageUtils;
import com.shangpin.iog.dto.NewPriceDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.iog.service.SkuPriceService;

public abstract class AbsSaveProduct {
	
	private static org.apache.log4j.Logger loggerInfo = org.apache.log4j.Logger.getLogger("info");
	private static org.apache.log4j.Logger loggerError = org.apache.log4j.Logger.getLogger("error");
	@Autowired
	ProductFetchService productFetchService;
	@Autowired
	ProductSearchService productSearchService;
	@Autowired
	SkuPriceService skuPriceService;
	/**
	 * 处理数据
	 * @param flag 如果图片使用skuid存储，设置sku。否则设置spu
	 * @param supplierId
	 * @param day 90
	 * @param picpath 如果为"",表示不下载图片
	 */
	@SuppressWarnings("unchecked")
	public void handleData(String flag,String supplierId,int day,String picpath) {
//		SpuDTO spuDTO = new SpuDTO();
//		spuDTO.setSupplierId("2015101501608");
//		spuDTO.setSpuId("00101007622");
//		spuDTO.setSeasonName("春天");
//		isSpuChanged(spuDTO );
//		SkuDTO skuDTO = new SkuDTO();
//		skuDTO.setSupplierId("2015101501608");
//		skuDTO.setSkuId("00101007622_456-TU");
//		skuDTO.setMarketPrice("250");
//		isSkuChanged(skuDTO );
		
		Map<String, Object> totalMap = null;

		try {
			totalMap = fetchProductAndSave();
		} catch (Exception e) {
			loggerError.error("处理原始数据出错" + e.getMessage());
			return;
		}

		List<SkuDTO> skuList = (List<SkuDTO>)totalMap.get("sku");
		saveSKU(skuList,supplierId,day);

		List<SpuDTO> spuList =(List<SpuDTO>)totalMap.get("spu");
		saveSPU(spuList);

		Map<String,List<String>> imageMap = (Map<String,List<String>>)totalMap.get("image");
		saveImage(imageMap,flag,supplierId,picpath);

	}
	//检查price是否改变
	private void isSkuChanged(SkuDTO skuDTO){
		NewPriceDTO newPriceDTO = null;
		boolean change = false;
		try {
			change = false;
			newPriceDTO = skuPriceService.getNewPriceDTO(skuDTO.getSupplierId(), skuDTO.getSkuId());
			if (!skuDTO.getMarketPrice().equals(newPriceDTO.getNewMarketPrice())) {
				change = true;
			}else if(!skuDTO.getSupplierPrice().equals(newPriceDTO.getNewSupplierPrice())){
				change = true;
			}else if(!skuDTO.getSalePrice().equals(newPriceDTO.getNewSalePrice())){
				change = true;
			}
			if (change) {
				//更新数据库
				productFetchService.updateSkuMemoAndTime(skuDTO.getSupplierId(),skuDTO.getSkuId(), new Date().toLocaleString()+"价格发生变化");
			}
		} catch (ServiceException e) {
			loggerError.error("查询价格出错"+e.toString());
			e.printStackTrace();
		}
	}
	//检查spu的   made_in ,material ,season 是否变化
	private void isSpuChanged(SpuDTO spuDTO){
		StringBuffer memo = new StringBuffer();
		String localeString = new Date().toLocaleString();
		SpuDTO spuData = productSearchService.findPartSpuData(spuDTO.getSupplierId(), spuDTO.getSpuId());
		if (StringUtils.isNotBlank(spuDTO.getSeasonName())) {
			if (!spuDTO.getSeasonName().equals(spuData.getSeasonName())) {
				memo.append(localeString+"季节改变").append(";");
			}
		}
		if (StringUtils.isNotBlank(spuDTO.getMaterial())) {
			if (!spuDTO.getMaterial().equals(spuData.getMaterial())) {
				memo.append(localeString+"材质改变").append(";");
			}
		}
		if (StringUtils.isNotBlank(spuDTO.getProductOrigin())) {
			if (!spuDTO.getProductOrigin().equals(spuData.getProductOrigin())) {
				memo.append(localeString+"产地改变").append(";");
			}
		}
		if (memo.length()>0) {
			productFetchService.updateSpuMemoAndTime(spuDTO.getSupplierId(),spuDTO.getSpuId(),memo.toString());
		}
	}
	/**
	 * 保存sku
	 * @param skuList
	 * @param supplierId
	 * @param day
	 */
	private void saveSKU(List<SkuDTO> skuList,String supplierId,int day) {
		Date startDate, endDate = new Date();
		startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate, day* -1, "D");
		// 获取原有的SKU 仅仅包含价格和库存
		Map<String, SkuDTO> skuDTOMap = new HashedMap();
		try {
			skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(supplierId, startDate, endDate);
		} catch (ServiceException e) {
			loggerError.error("获取原有sku失败" + e.getMessage());
			e.printStackTrace();
		}
		for (SkuDTO skuDTO : skuList) {
			if (skuDTOMap.containsKey(skuDTO.getSkuId())) {
				skuDTOMap.remove(skuDTO.getSkuId());
			}
			try {
				productFetchService.saveSKU(skuDTO);
			} catch (ServiceException e) {
				if (e.getMessage().equals("数据插入失败键重复")) {
					// 更新价格和库存
					try {
						isSkuChanged(skuDTO);
						productFetchService.updatePriceAndStock(skuDTO);
					} catch (ServiceException e1) {
						loggerError.error("更新价格库存失败");
					}
				} else {
					loggerError.error("数据插入更新失败");
				}
			}
		}
		//更新网站不再给信息的老数据
		for(Iterator<Map.Entry<String,SkuDTO>> itor = skuDTOMap.entrySet().iterator();itor.hasNext(); ){
			 Map.Entry<String,SkuDTO> entry =  itor.next();
			if(!"0".equals(entry.getValue().getStock())){//更新不为0的数据 使其库存为0
				entry.getValue().setStock("0");
				try {
					productFetchService.updatePriceAndStock(entry.getValue());
				} catch (ServiceException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 保存spu
	 * @param spuList
	 */
	private void saveSPU(List<SpuDTO> spuList) {
		for (SpuDTO spuDTO : spuList) {
			try {
				productFetchService.saveSPU(spuDTO);
			} catch (ServiceException e) {
				try {
					isSpuChanged(spuDTO);
					productFetchService.updateMaterial(spuDTO);
				} catch (ServiceException e1) {
					loggerError.info("spu更新材质信息失败");
				}
			}
		}
	}
	/**
	 * 保存图片,检查并处理图片变化
	 * @param imageMap
	 * @param flag sku,spu
	 * @param supplierId
	 * @param picpath 如果为空就不下载图片
	 */
	private void saveImage(Map<String,List<String>> imageMap,String flag,String supplierId, String picpath) {
		List<String> list = null;
		String imagePath = "";
		String memo = "";
		for (Entry<String, List<String>> entry : imageMap.entrySet()) {
			if (flag.equals("sku")) {
				list = productFetchService.saveAndCheckPicture(supplierId, null, entry.getKey().split(";")[0], entry.getValue());
				if (list.size()>0) {
					productFetchService.updateSkuMemoAndTime(supplierId, entry.getKey().split(";")[0], new Date().toLocaleString()+"图片变化");
				}
			}else {
				list =productFetchService.saveAndCheckPicture(supplierId, entry.getKey().split(";")[0], null, entry.getValue());
				if (list.size()>0) {
					productFetchService.updateSpuMemoAndTime(supplierId, entry.getKey().split(";")[0], new Date().toLocaleString()+"图片变化");
				}
			}
			if (!picpath.equals("")&&list.size()>0) {
				for (String url : list) {
					imagePath = ImageUtils.downImage(url, picpath, entry.getKey().split(";")[1]);
					memo = ImageUtils.checkImageSize(imagePath);
					if (!memo.equals("")) {
						if (flag.equals("sku")) {
							productFetchService.updateSkuMemoAndTime(supplierId, entry.getKey().split(";")[0], new Date().toLocaleString()+memo);
						}else{
							productFetchService.updateSpuMemoAndTime(supplierId, entry.getKey().split(";")[0], new Date().toLocaleString()+memo);
						}
					}
				}
			}
		}
		
	}

	/**
	 * 子类处理原始数据
	 * @param flag=0 表示图片使用skuid保存 ，
	 * @return sku:List<SkuDTO> spu:List<SpuDTO> image:Map<id;picName,List<picUrl>
	 */
	public abstract Map<String, Object> fetchProductAndSave();
	
}
