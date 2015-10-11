package com.shangpin.iog.julian_fashion.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.julian_fashion.utils.HttpResponse;
import com.shangpin.iog.julian_fashion.utils.HttpUtils;
import com.shangpin.iog.julian_fashion.utils.queue.DelayTask;
import com.shangpin.iog.julian_fashion.utils.queue.ITask;
import com.shangpin.iog.julian_fashion.utils.queue.QueueManager;
import com.shangpin.iog.service.ProductFetchService;

/**
 * Created by Jerry Wang on 2015/09/23.
 */
@Component("vinicco")
public class FetchProduct {
	final Logger logger = Logger.getLogger(this.getClass());
	@Autowired
	ProductFetchService productFetchService;

	public static QueueManager listManager = new QueueManager("list_queue", 3, 3);

	public static QueueManager infoManager = new QueueManager("info_queue", 3, 5);

	public static Set<String> doneUrlSet = new HashSet<String>();

	public static Map<String, SpuDTO> spuMap = new HashMap<String, SpuDTO>();

	public static Map<String, SkuDTO> skuMap = new HashMap<String, SkuDTO>();

	public static Map<String, Set<String>> imageMap = new HashMap<String, Set<String>>();

	public void persist2DB(String supplierId, String[] brandArray) {
		Set<String> brandSet = new HashSet<String>();
		Set<String> spuIdSet = new HashSet<String>();
		Set<String> skuIdSet = new HashSet<String>();
		for (String name : brandArray) {
			brandSet.add(name);
		}
		String folder = System.getProperty("java.io.tmpdir") + "/julian_fashion";

		FileInputStream inputStream = null;
		ObjectInputStream objectIntputStream = null;
		String spuFilePath = folder + "/spu.bin";
		File spuFile = new File(spuFilePath);
		if (spuFile.exists()) {
			try {
				inputStream = new FileInputStream(spuFile);
				objectIntputStream = new ObjectInputStream(inputStream);
				Map<String, SpuDTO> spuMap = (Map<String, SpuDTO>) objectIntputStream.readObject();
				for (SpuDTO dto : spuMap.values()) {
					dto.setSupplierId(supplierId);
					if (brandSet.contains(dto.getBrandName().toUpperCase())
							&& (dto.getCategoryName().equalsIgnoreCase("BAGS") || dto.getCategoryName().equalsIgnoreCase("ACCESSORIES"))) {
						dto.setSpuName(dto.getSpuName().split("\r")[0]);
						dto.setSpuId(dto.getSpuId().trim());
						spuIdSet.add(dto.getSpuId());
						try {
							productFetchService.saveSPU(dto);
						} catch (Exception e) {
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (objectIntputStream != null) {
					try {
						objectIntputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		String skuFilePath = folder + "/sku.bin";
		File skuFile = new File(skuFilePath);
		if (skuFile.exists()) {
			try {
				inputStream = new FileInputStream(skuFile);
				objectIntputStream = new ObjectInputStream(inputStream);
				Map<String, SkuDTO> skuMap = (Map<String, SkuDTO>) objectIntputStream.readObject();
				for (SkuDTO dto : skuMap.values()) {
					dto.setSupplierId(supplierId);
					if (!spuIdSet.contains(dto.getSpuId())) {
						continue;
					}
					dto.setProductName(dto.getProductName().split("\r")[0]);
					dto.setSpuId(dto.getSpuId().trim());
					dto.setSkuId(dto.getSkuId().trim());
					dto.setSalePrice(dto.getSalePrice().replaceAll("\\.", "").replaceAll(",", "\\."));
					dto.setMarketPrice(dto.getMarketPrice().replaceAll("\\.", "").replaceAll(",", "\\."));
					skuIdSet.add(dto.getSkuId());
					try {
						productFetchService.saveSKU(dto);
					} catch (ServiceException e) {
						try {
							if (e.getMessage().equals("数据插入失败键重复")) {
								// 更新价格和库存
								productFetchService.updatePriceAndStock(dto);
							} else {
								e.printStackTrace();
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (objectIntputStream != null) {
					try {
						objectIntputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			String imgFilePath = folder + "/img.bin";
			File imgFile = new File(imgFilePath);
			if (skuFile.exists()) {
				try {
					inputStream = new FileInputStream(imgFile);
					objectIntputStream = new ObjectInputStream(inputStream);
					Map<String, Set<String>> imgMap = (Map<String, Set<String>>) objectIntputStream.readObject();
					for (Map.Entry<String, Set<String>> entry : imgMap.entrySet()) {
						String skuId = entry.getKey().trim();
						if (!skuIdSet.contains(skuId)) {
							continue;
						}

						for (String imageUrl : entry.getValue()) {
							ProductPictureDTO dto = new ProductPictureDTO();
							dto.setPicUrl(imageUrl);
							dto.setSupplierId(supplierId);
							dto.setId(UUIDGenerator.getUUID());
							dto.setSkuId(skuId);
							try {
								productFetchService.savePicture(dto);
								productFetchService.savePictureForMongo(dto);
							} catch (ServiceException e) {
								e.printStackTrace();
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					if (objectIntputStream != null) {
						try {
							objectIntputStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (inputStream != null) {
						try {
							inputStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	public void fetchProductAndSave(String supplierId, String[] brandArray) {
		String url = "http://www.julian-fashion.com/eu/woman?sortby=newIn&page=";
		// url =
		// "http://www.julian-fashion.com/eu/woman/bags/?sortby=newIn&page=";
		ListSPUTASK task = new ListSPUTASK("woman", url, 1, productFetchService);
		FetchProduct.listManager.delayTask(new DelayTask(task, new Date()));

		task = new ListSPUTASK("man", "http://www.julian-fashion.com/eu/man?sortby=newIn&page=", 1, productFetchService);
		FetchProduct.listManager.delayTask(new DelayTask(task, new Date(System.currentTimeMillis() + 120000L)));
		try {
			Thread.sleep(300000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while (listManager.getDelayQueueLoad() != 0 || infoManager.getDelayQueueLoad() != 0) {
			System.out.println("at=" + DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss") + " list_queue_load="
					+ listManager.getDelayQueueLoad() + " info_queue_load=" + infoManager.getDelayQueueLoad());
			try {
				Thread.sleep(300000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// 数据存入文件
		String folder = System.getProperty("java.io.tmpdir") + "/julian_fashion";
		File parentFolder = new File(folder);
		if (!parentFolder.exists()) {
			parentFolder.mkdirs();
		}
		String skuFilePath = folder + "/sku.bin";

		FileOutputStream outStream = null;
		ObjectOutputStream objectOutputStream = null;
		try {
			outStream = new FileOutputStream(skuFilePath);
			objectOutputStream = new ObjectOutputStream(outStream);
			objectOutputStream.writeObject(skuMap);
			objectOutputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (objectOutputStream != null) {
				try {
					objectOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outStream != null) {
				try {
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		String spuFilePath = folder + "/spu.bin";
		try {
			outStream = new FileOutputStream(spuFilePath);
			objectOutputStream = new ObjectOutputStream(outStream);
			objectOutputStream.writeObject(spuMap);
			objectOutputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (objectOutputStream != null) {
				try {
					objectOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outStream != null) {
				try {
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		String imgFilePath = folder + "/img.bin";
		try {
			outStream = new FileOutputStream(imgFilePath);
			objectOutputStream = new ObjectOutputStream(outStream);
			objectOutputStream.writeObject(imageMap);
			objectOutputStream.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (objectOutputStream != null) {
				try {
					objectOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outStream != null) {
				try {
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		System.out.println("julian fashion 数据保存完毕；");
		// 存入数据库
		Set<String> brandSet = new HashSet<String>();
		for (String name : brandArray) {
			brandSet.add(name);
		}
		Set<String> spuIdSet = new HashSet<String>();
		Set<String> skuIdSet = new HashSet<String>();
		for (SpuDTO spu : spuMap.values()) {
			spu.setSupplierId(supplierId);
			if (brandSet.contains(spu.getBrandName().toUpperCase())) {
				if (true) {
					spu.setSpuName(spu.getSpuName().split("\r")[0]);
					spu.setSpuId(spu.getSpuId().trim());
					spuIdSet.add(spu.getSpuId());
					try {
						productFetchService.saveSPU(spu);
					} catch (Exception e) {
					}
				}
			}
		}
		for (SkuDTO sku : skuMap.values()) {
			sku.setSupplierId(supplierId);
			if (!spuIdSet.contains(sku.getSpuId())) {
				continue;
			}
			sku.setProductName(sku.getProductName().split("\r")[0]);
			sku.setSpuId(sku.getSpuId().trim());
			sku.setSkuId(sku.getSkuId().trim());
			sku.setSalePrice(sku.getSalePrice().replaceAll("\\.", "").replaceAll(",", "\\."));
			sku.setMarketPrice(sku.getMarketPrice().replaceAll("\\.", "").replaceAll(",", "\\."));
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
			String skuId = entry.getKey().trim();
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
		System.out.println("julian fashion 数据入库完毕；");
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
				Elements pList = doc.select("#list1");
				Elements aList = pList.select("a");
				for (int i = 0; i < aList.size(); i++) {
					String detailUrl = aList.get(i).attr("href");
					AppendSKUInfoTask task = new AppendSKUInfoTask(detailUrl, gender, productFetchService);
					FetchProduct.infoManager.delayTask(new DelayTask(task, new Date(System.currentTimeMillis() + 1000L * i)));
				}
				if (aList.size() == 21) {
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

	private final static String baseUrl = "http://www.julian-fashion.com";

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
				String topCategory = "";
				Elements menu = doc.select(".menu");
				Elements activeMenu = menu.select(".active");
				if (activeMenu.size() > 0) {
					topCategory = activeMenu.get(0).select("span").get(0).text();
				}
				SpuDTO spu = new SpuDTO();
				Elements detailDoc = doc.select("#prod-desc");
				String description = detailDoc.select("strong").get(0).text().trim();
				String spuId = detailDoc.select(".left").get(0).text().trim();
				spuId = spuId.substring(4, spuId.length() - description.length()).trim();
				String brandName = detailDoc.select("h1").get(0).text().trim();
				String price = detailDoc.select("#price").get(0).text().trim().substring(2);
				String category = "";
				try {
					category = detailDoc.select("#zoomWindowContentsDesc").get(0).text().trim();
				} catch (Exception e) {

				}
				String composition = "";
				Elements liList = detailDoc.select(".lst").get(0).select("li");
				for (int i = 0; i < liList.size(); i++) {
					Element li = liList.get(i);
					composition += li.text() + " ";
				}
				String productOrigin = detailDoc.select(".lst").get(1).select("li").get(0).text();
				Elements dimensionElemet = detailDoc.select(".dimension");
				if (dimensionElemet.size() != 0) {
					productOrigin = detailDoc.select(".lst").get(2).select("li").get(0).text();
					for (Element e : detailDoc.select(".lst").get(1).select("li")) {
						description += "\r\n" + e.text();
					}
				}
				Set<String> sizes = new HashSet<String>();
				Elements sizeList = detailDoc.select("#size-list").select("li");
				for (int i = 0; i < sizeList.size(); i++) {
					sizes.add(sizeList.get(i).text());
				}
				Elements colorsDoc = doc.select("#color-list");
				Elements imgDoc = doc.select("#thumbs");
				Elements aList = imgDoc.select("a");
				Set<String> imageList = new HashSet<String>();
				for (int i = 0; i < aList.size(); i++) {
					Element aLink = aList.get(i);
					String imageUrl = baseUrl + aLink.attr("data-zoom-image");
					imageList.add(imageUrl);
				}
				String color = "";
				Elements colorAList = colorsDoc.select("a");
				for (int i = 0; i < colorAList.size(); i++) {
					Element aLink = colorAList.get(i);
					if (url.endsWith(aLink.attr("href"))) {
						color = aLink.text().trim();
					} else {
						if (FetchProduct.doneUrlSet.contains(aLink.text().trim())) {
							continue;
						} else {
							// 发起新颜色任务；
							AppendSKUInfoTask task = new AppendSKUInfoTask(aLink.attr("href"), gender, productFetchService);
							FetchProduct.infoManager.delayTask(new DelayTask(task, new Date(System.currentTimeMillis() + 30000L)));
						}
					}
				}
				spu.setId(UUIDGenerator.getUUID());
				spu.setSpuId(spuId);
				spu.setBrandName(brandName);
				spu.setCategoryName(topCategory);
				spu.setSubCategoryName(category);
				spu.setSpuName(description);
				spu.setMaterial(composition);
				// 商品所属性别字段；
				spu.setCategoryGender(gender);
				spu.setProductOrigin(productOrigin);

				FetchProduct.doneUrlSet.add(url);
				FetchProduct.spuMap.put(spu.getSpuId(), spu);

				for (String size : sizes) {
					SkuDTO sku = new SkuDTO();
					sku.setId(UUIDGenerator.getUUID());
					sku.setSpuId(spuId);
					sku.setSkuId(spuId + "_" + size);
					sku.setProductSize(size);
					sku.setMarketPrice(price);
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
				System.out.println("Error on fetch sku at=" + DateUtil.formatDate(new Date(startTime), "yyyy-MM-dd HH:mm:ss") + "  url="
						+ targetUrl + " tid=" + Thread.currentThread().getId());
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
