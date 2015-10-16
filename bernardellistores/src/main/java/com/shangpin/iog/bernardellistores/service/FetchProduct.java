package com.shangpin.iog.bernardellistores.service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.util.DateUtil;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.bernardellistores.utils.HttpResponse;
import com.shangpin.iog.bernardellistores.utils.HttpUtils;
import com.shangpin.iog.bernardellistores.utils.queue.DelayTask;
import com.shangpin.iog.bernardellistores.utils.queue.ITask;
import com.shangpin.iog.bernardellistores.utils.queue.QueueManager;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;

/**
 * Created by Jerry on 2015/10/05.
 */
@Component("bernardellistores")
public class FetchProduct {
	final Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	ProductFetchService productFetchService;

	public static QueueManager listManager = new QueueManager("list_queue", 3, 3);

	public static QueueManager infoManager = new QueueManager("info_queue", 3, 3);

	public static Set<String> doneUrlSet = new HashSet<String>();

	public static Map<String, SpuDTO> spuMap = new HashMap<String, SpuDTO>();

	public static Map<String, SkuDTO> skuMap = new HashMap<String, SkuDTO>();

	public static Map<String, Set<String>> imageMap = new HashMap<String, Set<String>>();

	public void fetchProductAndSave(String supplierId, Map<String, Set<String>> brandMap) {
		// 男士包
		ListSPUTASK task = new ListSPUTASK("MAN-BAGS", "http://shop.bernardellistores.com/en/man/-/bag-1/?f_p=", 1, productFetchService);
		FetchProduct.listManager.delayTask(new DelayTask(task, new Date()));
		// // 女鞋
		task = new ListSPUTASK("WOMAN-SHOES", "http://shop.bernardellistores.com/en/woman/-/shoes/?f_p=", 1, productFetchService);
		FetchProduct.listManager.delayTask(new DelayTask(task, new Date(System.currentTimeMillis() + 60000L)));
		// 男鞋
		task = new ListSPUTASK("MAN-SHOES", "http://shop.bernardellistores.com/en/man/-/shoes/?f_p=", 1, productFetchService);
		FetchProduct.listManager.delayTask(new DelayTask(task, new Date(System.currentTimeMillis() + 120000L)));
		// // 男装
		task = new ListSPUTASK("MAN-CLOTHING", "http://shop.bernardellistores.com/en/man/-/n79/?f_p=", 1, productFetchService);
		FetchProduct.listManager.delayTask(new DelayTask(task, new Date(System.currentTimeMillis() + 240000L)));
		// // 女包
		task = new ListSPUTASK("WOMAN-BAGS", "http://shop.bernardellistores.com/en/woman/-/bag-1/?f_p=", 1, productFetchService);
		FetchProduct.listManager.delayTask(new DelayTask(task, new Date(System.currentTimeMillis() + 360000L)));
		// 女装
		task = new ListSPUTASK("WOMAN-CLOTHING", "http://shop.bernardellistores.com/en/woman/-/n79/?f_p=", 1, productFetchService);
		FetchProduct.listManager.delayTask(new DelayTask(task, new Date(System.currentTimeMillis() + 420000L)));
		try {
			Thread.sleep(60000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while (listManager.getDelayQueueLoad() != 0 || infoManager.getDelayQueueLoad() != 0) {
			System.out.println("at=" + DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss") + " list_queue_load="
					+ listManager.getDelayQueueLoad() + " info_queue_load=" + infoManager.getDelayQueueLoad());
			try {
				Thread.sleep(60000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Set<String> spuIdSet = new HashSet<String>();
		Set<String> skuIdSet = new HashSet<String>();
		for (SpuDTO spu : spuMap.values()) {
			spu.setSupplierId(supplierId);
			String key = spu.getCategoryGender() + "-" + spu.getCategoryName();
			if (brandMap.containsKey(key) && brandMap.get(key).contains(spu.getBrandName())) {
				spuIdSet.add(spu.getSpuId());
				try {
					productFetchService.saveSPU(spu);
				} catch (Exception e) {
				}
			}
		}
		for (SkuDTO sku : skuMap.values()) {
			sku.setSupplierId(supplierId);
			if (!spuIdSet.contains(sku.getSpuId())) {
				continue;
			}
			skuIdSet.add(sku.getSkuId());
			try {
				productFetchService.saveSKU(sku);
			} catch (ServiceException e) {
				try {
					if (e.getMessage().equals("数据插入失败键重复")) {
						// 更新价格和库存
						productFetchService.updatePriceAndStock(sku);
					} else {
						e.printStackTrace();
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}

		for (Map.Entry<String, Set<String>> entry : imageMap.entrySet()) {
			String skuId = entry.getKey();
			if (!skuIdSet.contains(skuId)) {
				continue;
			}

			for (String imageUrl : entry.getValue()) {
				ProductPictureDTO pictureDTO = new ProductPictureDTO();
				pictureDTO.setPicUrl(imageUrl);
				pictureDTO.setSupplierId(supplierId);
				pictureDTO.setId(UUIDGenerator.getUUID());
				pictureDTO.setSkuId(skuId);
				try {
					productFetchService.savePicture(pictureDTO);
					productFetchService.savePictureForMongo(pictureDTO);
				} catch (ServiceException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 
	 * @author Administrator
	 *
	 */
	class ListSPUTASK implements ITask {

		private int reTryCount = 0;

		private int index;

		private String url;

		private String gender;

		private ProductFetchService productFetchService;

		public ListSPUTASK(String gender, String url, int index, ProductFetchService productFetchService) {
			this.gender = gender;
			this.url = url;
			this.index = index;
			this.productFetchService = productFetchService;
		}

		private void fetchData(String targetUrl) {
			String listUrl = targetUrl + index;
			try {
				HttpResponse resp = HttpUtils.get(listUrl);
				if (resp.getStatus() == 200) {
					String htmlContent = resp.getResponse();
					Document doc = Jsoup.parse(htmlContent);
					Elements pList = doc.select("#shopProducts");
					Elements aList = pList.select(".productName");
					for (int i = 0; i < aList.size(); i++) {
						String detailUrl = aList.get(i).select("a").get(0).attr("href");
						AppendSKUInfoTask task = new AppendSKUInfoTask(detailUrl, gender, productFetchService);
						FetchProduct.infoManager.delayTask(new DelayTask(task, new Date(System.currentTimeMillis() + 1000L * i)));
					}
					if (aList.size() == 15) {
						ListSPUTASK task = new ListSPUTASK(gender, url, index + 1, productFetchService);
						FetchProduct.listManager.delayTask(new DelayTask(task, new Date(System.currentTimeMillis() + 10000L)));
					}
				}
			} catch (IOException e) {
				ListSPUTASK task = new ListSPUTASK(gender, url, index, productFetchService);
				task.setReTryCount(reTryCount + 1);
				if (task.getReTryCount() <= 3) {
					FetchProduct.listManager.delayTask(new DelayTask(task, new Date(System.currentTimeMillis() + 300000L)));
				} else {
					// 超过重试次数处理；
					System.out.println("ERROR on fetch  spu at=" + DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss") + "  url=" + url
							+ " index=" + index);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void doTask(long startTime) {
			fetchData(url);
		}

		public int getReTryCount() {
			return reTryCount;
		}

		public void setReTryCount(int reTryCount) {
			this.reTryCount = reTryCount;
		}
	}

	/**
	 * 
	 * @author Jerry Wang. 补全商品信息；
	 */
	class AppendSKUInfoTask implements ITask {

		private final static String baseUrl = "http://shop.bernardellistores.com";

		private int reTryCount = 0;

		private String url;

		private String gender;

		private ProductFetchService productFetchService;

		public AppendSKUInfoTask(String url, String gender, ProductFetchService productFetchService) {
			this.url = url;
			this.gender = gender;
			this.productFetchService = productFetchService;
		}

		@Override
		public void doTask(long startTime) {

			if (FetchProduct.doneUrlSet.contains(url)) {
				return;
			}
			String targetUrl = baseUrl + url;
			System.out.println("sku reTryCount=" + reTryCount + " at=" + DateUtil.formatDate(new Date(startTime), "yyyy-MM-dd HH:mm:ss")
					+ "  url=" + targetUrl + " tid=" + Thread.currentThread().getId());
			try {
				HttpResponse resp = HttpUtils.get(targetUrl);
				if (resp.getStatus() == 200) {
					String htmlContent = resp.getResponse();
					Document doc = Jsoup.parse(htmlContent);
					Elements compositionElement = doc.select("#composition-content");
					if (compositionElement.size() == 0) {
						return;
					}
					SpuDTO spu = new SpuDTO();
					String topCategory = doc.select(".breadcrumbs").get(0).select("a").get(3).text();
					Element detailDoc = doc.select(".details").get(0);
					String brandName = detailDoc.select("h1").get(0).select("a").get(0).text().trim();
					String description = detailDoc.select("h2").get(0).text().trim();
					String spuId = detailDoc.select("h3").get(0).text().split(":")[1].trim();
					String price = detailDoc.select(".price").get(0).text().trim().replace("€", "").trim().replaceAll(",", "");
					String marketPrice = null;
					if (detailDoc.select(".discounted").size() != 0) {
						marketPrice = detailDoc.select(".discounted").get(0).text().trim().replace("€", "").trim().replaceAll(",", "");
						price = detailDoc.select(".price").get(1).text().trim().replace("€", "").trim().replaceAll(",", "");
					} else {
						marketPrice = price;
					}
					String composition = compositionElement.get(0).text().trim();
					String productOrigin = "待确认";
					Set<String> sizes = new HashSet<String>();
					Elements sizeElement = detailDoc.select("#sizes_colour_1");
					if (sizeElement.size() == 0) {
						return;
					}
					Elements aList = sizeElement.get(0).select("a");
					for (int i = 0; i < aList.size(); i++) {
						sizes.add(aList.get(i).text().replace("½", ".5"));
					}
					Element imgDoc = doc.select(".thumbnails").get(0);
					Elements aLinkList = imgDoc.select("a");
					Set<String> imageList = new HashSet<String>();
					for (int i = 0; i < aLinkList.size(); i++) {
						Element aLink = aLinkList.get(i);
						String imageUrl = aLink.attr("href");
						imageList.add(imageUrl);
					}
					String color = "待人工确认";
					Elements colorElement = detailDoc.select("#color-content");
					if (colorElement.size() > 0) {
						color = colorElement.get(0).text().trim();
					}
					spu.setId(UUIDGenerator.getUUID());
					spu.setSpuId(spuId);
					spu.setBrandName(brandName);
					spu.setCategoryName(gender.split("-")[1]);
					spu.setSubCategoryName(topCategory);
					spu.setSpuName(description);
					spu.setMaterial(composition);
					// 商品所属性别字段；
					spu.setCategoryGender(gender.split("-")[0]);
					spu.setProductOrigin(productOrigin);

					FetchProduct.doneUrlSet.add(url);
					FetchProduct.spuMap.put(spu.getSpuId(), spu);

					for (String size : sizes) {
						size = size.replace("One size", "U");
						SkuDTO sku = new SkuDTO();
						sku.setId(UUIDGenerator.getUUID());
						sku.setSpuId(spuId);
						sku.setSkuId(spuId + "_" + size);
						sku.setProductSize(size);
						sku.setMarketPrice(marketPrice);
						sku.setSalePrice(price);
						sku.setColor(color);
						sku.setProductDescription(description);
						sku.setStock("1");
						sku.setProductCode(spuId);
						sku.setSaleCurrency("EUR");
						sku.setProductName(description);
						FetchProduct.skuMap.put(sku.getSkuId(), sku);
						FetchProduct.imageMap.put(sku.getSkuId(), imageList);
					}
				}
			} catch (IOException e) {
				AppendSKUInfoTask task = new AppendSKUInfoTask(url, gender, productFetchService);
				task.setReTryCount(reTryCount + 1);
				if (task.getReTryCount() <= 3) {
					FetchProduct.infoManager.delayTask(new DelayTask(task, new Date(System.currentTimeMillis() + 300000L)));
				} else {
					// 超过重试次数处理；
					System.out.println("Error on fetch sku at=" + DateUtil.formatDate(new Date(startTime), "yyyy-MM-dd HH:mm:ss")
							+ "  url=" + targetUrl + " tid=" + Thread.currentThread().getId());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public int getReTryCount() {
			return reTryCount;
		}

		public void setReTryCount(int reTryCount) {
			this.reTryCount = reTryCount;
		}
	}
}
