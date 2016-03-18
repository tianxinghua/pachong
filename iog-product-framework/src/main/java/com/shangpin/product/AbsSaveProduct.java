package com.shangpin.product;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.dto.NewPriceDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.iog.service.SkuPriceService;

/**
 * 
 * @author Administrator
 *
 */
public abstract class AbsSaveProduct {
	private static org.apache.log4j.Logger loggerInfo = org.apache.log4j.Logger.getLogger("info");
	private static org.apache.log4j.Logger loggerError = org.apache.log4j.Logger.getLogger("error");
	@Autowired
	ProductFetchService productFetchService;
	@Autowired
	ProductSearchService productSearchService;
	@Autowired
	SkuPriceService skuPriceService;

	@SuppressWarnings("unchecked")
	public void handleData(String flag,String supplierId,int day) {

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
		saveImage(imageMap,flag,supplierId);

	}
	//检查price是否改变
	private void isSkuChanged(String supplierId,SkuDTO skuDTO){
		NewPriceDTO newPriceDTO = null;
		boolean change = false;
		try {
			change = false;
			newPriceDTO = skuPriceService.getNewPriceDTO(supplierId, skuDTO.getSkuId());
			if (!skuDTO.getMarketPrice().equals(newPriceDTO.getNewMarketPrice())) {
				change = true;
			}else if(!skuDTO.getSupplierPrice().equals(newPriceDTO.getNewSupplierPrice())){
				change = true;
			}else if(!skuDTO.getSalePrice().equals(newPriceDTO.getNewSalePrice())){
				change = true;
			}
			if (change) {
				//更新数据库
				skuDTO.setLastTime(new Date());
				skuDTO.setMemo("价格发生变化");
				productFetchService.updateSkuMemoAndTime(skuDTO);
			}
		} catch (ServiceException e) {
			loggerError.error("查询价格出错"+e.toString());
			e.printStackTrace();
		}
	}
	//检查spu的  pic ， made_in ,material ,season 是否变化
	private void isSpuChanged(){
		
	}
	//检查图片是否变化
	
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
						productFetchService.updatePriceAndStock(skuDTO);
						isSkuChanged(supplierId,skuDTO);
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
					productFetchService.updateMaterial(spuDTO);
				} catch (ServiceException e1) {
					loggerError.info("spu更新材质信息失败");
				}
			}
		}
	}
	/**
	 * 保存图片,处理图片变化
	 * @param imageMap
	 * @param flag
	 * @param supplierId
	 */
	private void saveImage(Map<String,List<String>> imageMap,String flag,String supplierId) {
		for (Entry<String, List<String>> entry : imageMap.entrySet()) {
			if (flag.equals("0")) {
				productFetchService.savePicture(supplierId, null, entry.getKey(), entry.getValue());
			}else {
				productFetchService.savePicture(supplierId, entry.getKey(),null, entry.getValue());
			}
		}
	}

	/**
	 * 子类处理原始数据
	 * @param flag=0 表示图片使用skuid保存 ，
	 * @return sku:List<SkuDTO> spu:List<SpuDTO> image:Map<id,List<picUrl>
	 */
	public abstract Map<String, Object> fetchProductAndSave() throws Exception;

}
