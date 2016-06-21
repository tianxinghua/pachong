package com.shangpin.iog.productmanager;

import java.util.ArrayList;
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
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.ImageUtils;
import com.shangpin.iog.dto.NewPriceDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.generater.dto.ProductDTO;
import com.shangpin.iog.generater.strategy.sepStrategy.ISepStrategy;
import com.shangpin.iog.generater.strategy.sepStrategy.SepStrategyContext;
import com.shangpin.iog.generater.util.Csv2DTO;
import com.shangpin.iog.generater.util.DataListToMap;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.iog.service.SkuPriceService;
@Component("abssaveproduct")
public class AbsSaveProductImpl {

	private static org.apache.log4j.Logger loggerInfo = org.apache.log4j.Logger
			.getLogger("info");
	private static org.apache.log4j.Logger loggerError = org.apache.log4j.Logger
			.getLogger("error");
	@Autowired
	ProductFetchService productFetchService;
	@Autowired
	ProductSearchService productSearchService;
	@Autowired
	SkuPriceService skuPriceService;

	/**
	 * 处理数据
	 * 
	 * @param flag
	 *            如果图片使用skuid存储，设置sku。否则设置spu
	 * @param supplierId
	 * @param day
	 *            90
	 * @param picpath
	 *            如果为"",表示不下载图片
	 */
	public void handleData(final String flag, final String supplierId,
			final int day, final String picpath, String condition, String url,
			String filePath, String sep, String[] needColsNo,String[] sepStrategys) {
		System.out.println("处理数据");
		Map<String, Object> totalMap = null;

		try {
			totalMap = fetchProductAndSave(supplierId,condition,url,filePath,sep,needColsNo,sepStrategys);
		} catch (Exception e) {
			loggerError.error("处理原始数据出错" + e.getMessage());
			return;
		}

		final List<SkuDTO> skuList = (List<SkuDTO>) totalMap.get("sku");
		Thread t = new Thread(new Runnable() {
			public void run() {
				saveSKU(skuList, supplierId, day);
			}
		});
		t.start();
		final List<SpuDTO> spuList = (List<SpuDTO>) totalMap.get("spu");
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				saveSPU(spuList, supplierId);
			}
		});
		t1.start();

		final Map<String, List<String>> imageMap = (Map<String, List<String>>) totalMap.get("image");
		Thread t2 = new Thread(new Runnable() {
			public void run() {
				saveImage(imageMap, flag, supplierId, picpath);
			}
		});
		t2.start();

	}

	// 检查price是否改变
	private void updateSkuChange(String supplierId, List<SkuDTO> skuList) {
		// isSkuChanged
		// 查询库中所有的sku价格，对比skuList，如果未变化从查询出的集合中删除，如果有变化添加备注到这个sku，最后保存结果集
		List<NewPriceDTO> newSkuPriceList = skuPriceService
				.getNewSkuPriceList(supplierId);
		Map<String, NewPriceDTO> priceMap = new HashMap<String, NewPriceDTO>();
		for (NewPriceDTO newPriceDTO : newSkuPriceList) {
			try {
				priceMap.put(newPriceDTO.getSkuId(), newPriceDTO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		boolean change = false;
		NewPriceDTO n = null;
		List<String> idList = new ArrayList<String>();
		for (SkuDTO sku : skuList) {
			try {
				if (priceMap.containsKey(sku.getSkuId())) {

					change = false;
					n = priceMap.get(sku.getSkuId());
					if (!sku.getMarketPrice().equals(
							n.getNewMarketPrice() == null ? "" : n
									.getNewMarketPrice())) {
						change = true;
					} else if (!sku.getSupplierPrice().equals(
							n.getNewSupplierPrice() == null ? "" : n
									.getNewSupplierPrice())) {
						change = true;
					} else if (!sku.getSalePrice().equals(
							n.getNewSalePrice() == null ? "" : n
									.getNewSalePrice())) {
						change = true;
					}

					if (change) {
						// 更新数据库
						idList.add(sku.getSkuId());
					}

				}
			} catch (Exception e) {

			}

		}
		// 更新list memo
		if (idList.size() > 0 && idList != null) {
			try {
				productFetchService.updateSkuListMemo(supplierId, idList);
			} catch (Exception e) {
				loggerError.error(e);
				e.printStackTrace();
			}

		}
	}

	// 检查spu的 made_in ,material ,season 是否变化
	private String conpareSpu(SpuDTO spuDTO, SpuDTO nespuDTO) {
		StringBuffer memo = new StringBuffer();
		String localeString = new Date().toLocaleString();
		if (StringUtils.isNotBlank(spuDTO.getSeasonName())) {
			if (!spuDTO.getSeasonName().equals(nespuDTO.getSeasonName())) {
				memo.append(localeString + "季节改变").append(";");
			}
		}
		if (StringUtils.isNotBlank(spuDTO.getMaterial())) {
			if (!spuDTO.getMaterial().equals(nespuDTO.getMaterial())) {
				memo.append(localeString + "材质改变").append(";");
			}
		}
		if (StringUtils.isNotBlank(spuDTO.getProductOrigin())) {
			if (!spuDTO.getProductOrigin().equals(nespuDTO.getProductOrigin())) {
				memo.append(localeString + "产地改变").append(";");
			}
		}
		return memo.toString();
	}

	private void updateSPUMemo(String supplierId, List<SpuDTO> spuList) {
		List<SpuDTO> updateSpu = productSearchService
				.findpartSpuListBySupplier(supplierId);
		loggerInfo.info("获取到更新spu数" + updateSpu.size());
		Map<String, SpuDTO> spuMap = new HashMap<String, SpuDTO>();

		List<SpuDTO> reSpuList = new ArrayList<SpuDTO>();
		for (SpuDTO spu : updateSpu) {
			try {
				spuMap.put(spu.getSpuId(), spu);
			} catch (Exception e) {
				loggerError.error(e);
			}
		}
		String memo = "";
		for (SpuDTO spuDTO : spuList) {
			try {
				if (spuMap.containsKey(spuDTO.getSpuId())) {
					memo = conpareSpu(spuDTO, spuMap.get(spuDTO.getSpuId()));
					if (StringUtils.isNotEmpty(memo)) {
						spuDTO.setMemo(memo);
						reSpuList.add(spuDTO);
					}
				}
			} catch (Exception e) {
				loggerError.error(e);
			}

		}
		if (reSpuList.size() > 0 && reSpuList != null) {
			productFetchService.updateSpuListMemo(reSpuList);
		}
	}

	/**
	 * 保存sku
	 * 
	 * @param skuList
	 * @param supplierId
	 * @param day
	 */
	private void saveSKU(List<SkuDTO> skuList, String supplierId, int day) {
		loggerInfo.info("开始保存sku");
		Date startDate, endDate = new Date();
		startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate, day
				* -1, "D");
		// 获取原有的SKU 仅仅包含价格和库存
		Map<String, SkuDTO> skuDTOMap = new HashedMap();
		try {
			skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(
					supplierId, startDate, endDate);
		} catch (ServiceException e) {
			loggerError.error("获取原有sku失败" + e.getMessage());
			e.printStackTrace();
		}

		updateSkuChange(supplierId, skuList);

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
						// isSkuChanged(skuDTO);//取消单个查询对比
						productFetchService.updatePriceAndStock(skuDTO);
					} catch (ServiceException e1) {
						loggerError.error("更新价格库存失败");
					}
				} else {
					loggerError.error("数据插入更新失败");
				}
			}
		}
		// 更新网站不再给信息的老数据
		for (Iterator<Map.Entry<String, SkuDTO>> itor = skuDTOMap.entrySet()
				.iterator(); itor.hasNext();) {
			Map.Entry<String, SkuDTO> entry = itor.next();
			if (!"0".equals(entry.getValue().getStock())) {// 更新不为0的数据 使其库存为0
				entry.getValue().setStock("0");
				try {
					productFetchService.updatePriceAndStock(entry.getValue());
				} catch (ServiceException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("=================保存sku结束=================");
		loggerInfo.info("=================保存sku结束=================");

	}

	/**
	 * 保存spu
	 * 
	 * @param spuList
	 */
	private void saveSPU(List<SpuDTO> spuList, String supplierId) {
		loggerInfo.info("开始保存spu");
		updateSPUMemo(supplierId, spuList);
		for (SpuDTO spuDTO : spuList) {
			try {
				productFetchService.saveSPU(spuDTO);
			} catch (ServiceException e) {
				try {
					// isSpuChanged(spuDTO);//取消单个查询
					productFetchService.updateMaterial(spuDTO);
				} catch (ServiceException e1) {
					loggerError.info("spu更新材质信息失败");
				}
			}
		}
		System.out.println("+++++++++++++保存spu结束+++++++++++++");
		loggerInfo.info("保存spu结束");
	}

	/**
	 * 保存图片,检查并处理图片变化
	 * 
	 * @param imageMap
	 * @param flag
	 *            sku,spu
	 * @param supplierId
	 * @param picpath
	 *            如果为空就不下载图片
	 */
	private void saveImage(Map<String, List<String>> imageMap, String flag,
			String supplierId, String picpath) {
		loggerInfo.info("开始处理图片");
		List<String> list = null;
		String imagePath = "";
		String memo = "";
		String id = "";
		String imgname = "";
		String result = "";
		Map<String, List<String>> downMap = new HashMap<String, List<String>>();
		for (Entry<String, List<String>> entry : imageMap.entrySet()) {
			try {
				id = entry.getKey().split(";")[0];
				// 正常使用
				list = productFetchService.saveAndCheckPicture(supplierId, id,
						entry.getValue(), flag);
				// 仅仅stefaniamode采取
				// list =
				// productFetchService.saveAndCheckPictureForSteFaniamode(supplierId,id,
				// entry.getValue(), flag);
				loggerInfo.info("id" + id + "新增图片数" + list.size());
				if (list.size() > 0) {
					productFetchService.updateSpuOrSkuMemoAndTime(supplierId,
							id, new Date().toLocaleString() + "图片变化", flag);
					// 存新增的的图片到map
					imgname = entry.getKey().split(";")[1];
					downMap.put(id + ";" + imgname, list);
				}
			} catch (Exception e) {
				loggerError.error(e);
			}

		}

		if (StringUtils.isNotBlank(picpath) && downMap.size() > 0) {
			loggerInfo.info("开始保存图片" + downMap.size());
			for (Entry<String, List<String>> e : downMap.entrySet()) {
				try {
					int n = 1;
					for (String url : e.getValue()) {
						try {
							if (StringUtils.isNotEmpty(url)) {
								id = e.getKey().split(";")[0];
								imgname = e.getKey().split(";")[1] + " (" + n++
										+ ").jpg";
								imagePath = picpath + imgname;
								imagePath = ImageUtils.downImage(url, picpath,
										imgname);
								result = ImageUtils.checkImageSize(imagePath);
								if (!result.equals("")) {
									if (memo.contains(result)) {
										memo = memo.replace(result, result
												+ "1");
									} else {
										memo += " " + result;
									}
								}
							}
						} catch (Exception e2) {
							loggerError.error(e2);
						}

					}
					if (StringUtils.isNotBlank(memo)) {
						productFetchService.updateSpuOrSkuMemoAndTime(
								supplierId, id, new Date().toLocaleString()
										+ memo, flag);
					}
					memo = "";
				} catch (Exception e2) {
					loggerError.error(e2);
				}

			}
		}
		System.out.println("保存图片结束");
		loggerInfo.info("保存图片结束");
	}

	/**
	 * 子类处理原始数据
	 * 
	 * @param flag
	 *            =0 表示图片使用skuid保存 ，
	 * @return sku:List(skuDTO) spu:List(spuDTO) image:
	 *         Map(id;picName,List<picUrl>)
	 */
	private Map<String, Object> fetchProductAndSave(String supplierId,
			String condition, String url, String filePath, String sep,
			String[] needColsNo, String[] sepStrategys) {
		ISepStrategy[] iSepStrategies = new SepStrategyContext()
				.operate(sepStrategys);

		List<ProductDTO> productList = new Csv2DTO().toDTO(url, filePath, sep,
				needColsNo, iSepStrategies, ProductDTO.class);

		Map<String, Object> map = DataListToMap.toMap(condition, productList,
				supplierId, sepStrategys);

		return map;
	}

}