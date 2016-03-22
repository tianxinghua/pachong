package com.shangpin.product;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
	private static ExecutorService executor = new ThreadPoolExecutor(2, 5, 300, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(3),new ThreadPoolExecutor.CallerRunsPolicy());
	
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
	public void handleData(final String flag,final String supplierId,final int day,final String picpath) {
		
		Map<String, Object> totalMap = null;

		try {
			totalMap = fetchProductAndSave();
		} catch (Exception e) {
			loggerError.error("处理原始数据出错" + e.getMessage());
			return;
		}

		final List<SkuDTO> skuList = (List<SkuDTO>)totalMap.get("sku");
		Thread t = new Thread(new Runnable() {
			public void run() {
				saveSKU(skuList,supplierId,day);
			}
		});
		t.start();
		final List<SpuDTO> spuList =(List<SpuDTO>)totalMap.get("spu");
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				saveSPU(spuList);
			}
		});
		t1.start();

		final Map<String,List<String>> imageMap = (Map<String,List<String>>)totalMap.get("image");
		Thread t2 = new Thread(new Runnable() {
			public void run() {
				saveImage(imageMap,flag,supplierId,picpath);
			}
		});
		t2.start();
		
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
				productFetchService.updateSpuOrSkuMemoAndTime(skuDTO.getSupplierId(),skuDTO.getSkuId(), new Date().toLocaleString()+"价格发生变化","sku");
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
			productFetchService.updateSpuOrSkuMemoAndTime(spuDTO.getSupplierId(), spuDTO.getSpuId(),memo.toString(), "spu");
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
		System.out.println("=================保存sku结束=================");
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
		System.out.println("+++++++++++++保存spu结束+++++++++++++");
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
		String id = "";
		String imgname = "";
		String result = "";
		Map<String, List<String>> downMap = new HashMap<String, List<String>>();
		for (Entry<String, List<String>> entry : imageMap.entrySet()) {
			id = entry.getKey().split(";")[0];
			list = productFetchService.saveAndCheckPicture(supplierId,id, entry.getValue(), flag);
			if (list.size()>0) {
				productFetchService.updateSpuOrSkuMemoAndTime(supplierId, id,  new Date().toLocaleString()+"图片变化", flag);
				//存新增的的图片到map
				imgname = entry.getKey().split(";")[1];
				downMap.put(imgname, list);
			}
		}
		if (StringUtils.isNotBlank(picpath)) {
			if (downMap.size()>0) {
				for (Entry<String, List<String>> e : downMap.entrySet()) {
					int n = 1;
					for (String url : e.getValue()) {
						if (StringUtils.isEmpty(url)) {
							continue;
						}
						imgname = e.getKey()+"_"+n+++".jpg";
						imagePath = picpath+imgname;
						imagePath = ImageUtils.downImage(url, picpath,imgname);
						result = ImageUtils.checkImageSize(imagePath);
						if (!result.equals("")) {
							if (memo.contains(result)) {
								memo = memo.replace(result, result+"1");
							}else{
								memo +=" "+result;
							}
						}
					}
					if (StringUtils.isNotBlank(memo)) {
						productFetchService.updateSpuOrSkuMemoAndTime(supplierId, id,  new Date().toLocaleString()+memo, flag);
					}
					memo = "";
				}
			}
		}
	}
	
	

	/**
	 * 子类处理原始数据
	 * @param flag=0 表示图片使用skuid保存 ，
	 * @return sku:List(skuDTO) spu:List(spuDTO) image: Map(id;picName,List<picUrl>) 
	 */
	public abstract Map<String, Object> fetchProductAndSave();
	
}
