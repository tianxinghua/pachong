package com.shangpin.iog.vinicco.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.Header;
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
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.vinicco.Startup;
import com.shangpin.iog.vinicco.utils.HttpResponse;
import com.shangpin.iog.vinicco.utils.HttpUtils;
import com.shangpin.iog.vinicco.utils.Md5;
import com.shangpin.iog.vinicco.utils.queue.DelayTask;
import com.shangpin.iog.vinicco.utils.queue.ITask;
import com.shangpin.iog.vinicco.utils.queue.QueueManager;

/**
 * Created by Jerry on 2015/09/23.
 */
@Component("vinicco")
public class FetchProduct {
	final Logger logger = Logger.getLogger(this.getClass());
	private static Logger infoLogger = Logger.getLogger("info");
	@Autowired
	ProductFetchService productFetchService;

	public static QueueManager listManager = new QueueManager("list_queue", 3, 1);

	public static QueueManager infoManager = new QueueManager("info_queue", 3, 1);

	public static QueueManager picManager = new QueueManager("pic_queue", 3, 1);

	private String doLogin(String name, String password) {
		String loginUrl = "http://185.37.44.155:10088/catalogo/app/controlloUtente.php";
		String sessionInfo = null;
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("login", name);
		paramMap.put("password", password);
		try {
			HttpResponse resp = HttpUtils.post(loginUrl, paramMap);
			if (resp.getStatus() == 302) {
				for (Header header : resp.getHeaders()) {
					if ("Set-Cookie".equals(header.getName())) {
						int index = header.getValue().indexOf(";");
						sessionInfo = header.getValue().substring(0, index);
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sessionInfo;
	}

	public void fetchProductAndSave(String supplierId, Map<String, String> brandMap, String[] brandArray, Map<String, String> categoryMap,
			String name, String password) {
		// 获取登录session
		String sessionInfo = doLogin(name, password);
		if (sessionInfo == null) {
			System.err.println("login fail");
			return;
		}
		System.out.println("login info=" + sessionInfo);
		int i = 0;
		for (String brandName : brandArray) {
			String upperBrandName = brandName.toUpperCase();
			if (brandMap.containsKey(upperBrandName)) {
				ListSPUTASK task = new ListSPUTASK(upperBrandName, brandMap.get(upperBrandName), sessionInfo, categoryMap,
						productFetchService);
				// 每个品牌间隔五分钟；
				listManager.delayTask(new DelayTask(task, new Date(System.currentTimeMillis() + 10000 * i)));
				i += 1;
			}
		}
		try {
			Thread.sleep(120000L);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		while (listManager.getDelayQueueLoad() != 0 || infoManager.getDelayQueueLoad() != 0 || picManager.getDelayQueueLoad() != 0) {
			System.out.println("at=" + DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss") + " list_queue_load="
					+ listManager.getDelayQueueLoad() + " info_queue_load=" + infoManager.getDelayQueueLoad() + " pic_queue_load="
					+ picManager.getDelayQueueLoad());
			try {
				Thread.sleep(60000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

/**
 * 
 codcliente:AW macro:4 continuos:ALL dariga:1 numerorecord:50
 * 
 * @author Administrator
 *
 */
class ListSPUTASK implements ITask {

	private int reTryCount = 0;

	private Map.Entry<String, String> reTryEntry;

	private int reTryStart = 0;

	private String brandName;

	private String brandId;

	private String sessionInfo;

	private Map<String, String> categoryMap;

	private ProductFetchService productFetchService;

	private Map<String, String> paramMap;

	public ListSPUTASK(String brandName, String brandId, String sessionInfo, Map<String, String> categoryMap,
			ProductFetchService productFetchService) {
		this.brandId = brandId;
		this.brandName = brandName;
		this.sessionInfo = sessionInfo;
		this.categoryMap = categoryMap;
		this.productFetchService = productFetchService;
	}

	private void fetchData(Map<String, String> paramMap, int start, int size, long startTime, Map.Entry<String, String> entry) {
		String listUrl = "http://185.37.44.155:10088/catalogo/login.php";
		do {
			paramMap.put("dariga", "" + start);
			Header[] headers = new Header[] { new Header("Cookie", sessionInfo) };
			try {
				HttpResponse resp = HttpUtils.post(listUrl, paramMap, headers);
				if (resp.getStatus() == 200) {
					String htmlContent = resp.getResponse();
					Document doc = Jsoup.parse(htmlContent);
					Elements trList = doc.select("tr");
					System.out.println("spu reTryCount=" + reTryCount + " at="
							+ DateUtil.formatDate(new Date(startTime), "yyyy-MM-dd HH:mm:ss") + "  brandId=" + brandId + " macro="
							+ entry.getValue() + " name=" + entry.getKey() + " start=" + start + " size=" + trList.size() + " tid="
							+ Thread.currentThread().getId());
					for (int i = 1; i < trList.size(); i++) {
						Element tr = trList.get(i);
						Elements tdList = tr.select("td");
						SpuDTO spu = new SpuDTO();
						spu.setId(UUIDGenerator.getUUID());
						spu.setSupplierId(Startup.supplierId);
						spu.setSpuId(tdList.get(1).select("a").get(0).attr("onclick").split("&")[1].substring(8).trim() + "_"
								+ tdList.get(2).text());
						spu.setBrandName(brandName);
						spu.setCategoryName(tdList.get(5).text());
						spu.setSpuName(tdList.get(3).text());
						spu.setSeasonId(tdList.get(4).text());
						spu.setMaterial("待补充");
						spu.setCategoryGender(tdList.get(6).text());
						String variant = tdList.get(2).text();
						try {
							productFetchService.saveSPU(spu);
						} catch (Exception e) {
							// e.printStackTrace();
						}
						SkuDTO sku = new SkuDTO();
						sku.setSupplierId(Startup.supplierId);
						sku.setSpuId(spu.getSpuId());
						// sku.setMarketPrice(tdList.get(9).text());
						sku.setSalePrice(tdList.get(9).text());
						// sku.setSupplierPrice(item.getSupply_price());
						sku.setColor(tdList.get(3).text());
						sku.setProductDescription(tdList.get(3).text());
						sku.setSaleCurrency("EUR");
						AppendSKUInfoTask task = new AppendSKUInfoTask(sessionInfo, sku, variant, entry.getValue(), productFetchService);
						FetchProduct.infoManager.delayTask(new DelayTask(task, new Date()));
					}
					if (trList.size() == size + 1) {
						start += size;
					} else {
						break;
					}
				}
			} catch (IOException e) {
				ListSPUTASK task = new ListSPUTASK(brandName, brandId, sessionInfo, null, productFetchService);
				task.setReTryCount(reTryCount + 1);
				task.setReTryStart(start);
				task.setReTryEntry(entry);
				if (task.getReTryCount() <= 3) {
					FetchProduct.listManager.delayTask(new DelayTask(task, new Date(System.currentTimeMillis() + 120000L)));
				} else {
					// 超过重试次数处理；
					System.out.println("ERROR on fetch  spu at=" + DateUtil.formatDate(new Date(startTime), "yyyy-MM-dd HH:mm:ss")
							+ "  brandId=" + brandId + " macro=" + entry.getValue() + " name=" + entry.getKey() + " start=" + start);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} while (true);
	}

	@Override
	public void doTask(long startTime) {
		int start = 1;
		int size = 50;

		if (this.paramMap != null) {
			fetchData(this.paramMap, this.reTryStart, size, startTime, this.reTryEntry);
		} else {
			for (Map.Entry<String, String> entry : categoryMap.entrySet()) {
				Map<String, String> paramMap = new HashMap<String, String>();
				paramMap.put("codcliente", brandId);
				paramMap.put("macro", entry.getValue());
				paramMap.put("continuos", "ALL");
				paramMap.put("numerorecord", "" + size);
				fetchData(paramMap, start, size, startTime, entry);
			}
		}
	}

	public int getReTryCount() {
		return reTryCount;
	}

	public void setReTryCount(int reTryCount) {
		this.reTryCount = reTryCount;
	}

	public void setParamMap(Map<String, String> paramMap) {
		this.paramMap = paramMap;
	}

	public void setReTryEntry(Map.Entry<String, String> reTryEntry) {
		this.reTryEntry = reTryEntry;
	}

	public void setReTryStart(int reTryStart) {
		this.reTryStart = reTryStart;
	}
}

/**
 * 
 * @author Jerry Wang. 补全商品的尺寸库存信息及图片信息；
 */
class AppendSKUInfoTask implements ITask {

	private int reTryCount = 0;

	private SkuDTO sku;

	private String variant;

	private String categoryId;

	private String sessionInfo;

	private ProductFetchService productFetchService;

	public AppendSKUInfoTask(String sessionInfo, SkuDTO sku, String variant, String categoryId, ProductFetchService productFetchService) {
		this.sessionInfo = sessionInfo;
		this.sku = sku;
		this.variant = variant;
		this.categoryId = categoryId;
		this.productFetchService = productFetchService;
	}

	@Override
	public void doTask(long startTime) {
		String encodeSpuId = "";
		try {
			encodeSpuId = URLEncoder.encode(sku.getSpuId().split("_")[0], "UTF-8");
		} catch (UnsupportedEncodingException e2) {
			e2.printStackTrace();
		}
		String stockUrl = "http://185.37.44.155:10088/catalogo/vediTaglie.php?category=" + categoryId + "&article=" + encodeSpuId
				+ "&variant=" + variant;
		Header[] headers = new Header[] { new Header("Cookie", sessionInfo) };
		System.out.println("sku reTryCount=" + reTryCount + " at=" + DateUtil.formatDate(new Date(startTime), "yyyy-MM-dd HH:mm:ss")
				+ "  categoryId=" + categoryId + " article=" + sku.getSpuId() + " variant=" + variant + " tid="
				+ Thread.currentThread().getId());
		try {
			HttpResponse resp = HttpUtils.get(stockUrl, headers);
			if (resp.getStatus() == 200) {
				String htmlContent = resp.getResponse();
				Document doc = Jsoup.parse(htmlContent);
				Elements trList = doc.select("tr");
				for (int i = 1; i < trList.size(); i++) {
					Element tr = trList.get(i);
					Elements tdList = tr.select("td");
					sku.setProductSize(tdList.get(0).text());
					Integer stock = Integer.parseInt(tdList.get(1).text());
					if (stock > 0) {
						sku.setId(UUIDGenerator.getUUID());
						String size = sku.getProductSize();
						// 解决尺码网页上的乱码，初步判断是半码；
						if (size.indexOf("�") > 0) {
							size = size.replace("�", ".5");
							sku.setProductSize(size);
						}
						sku.setSkuId(sku.getSpuId() + "_" + categoryId + "_" + size);
						sku.setStock(stock.toString());
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
				}
			}
		} catch (IOException e) {
			AppendSKUInfoTask task = new AppendSKUInfoTask(sessionInfo, sku, variant, categoryId, productFetchService);
			task.setReTryCount(reTryCount + 1);
			if (task.getReTryCount() <= 3) {
				FetchProduct.infoManager.delayTask(new DelayTask(task, new Date(System.currentTimeMillis() + 300000L)));
			} else {
				// 超过重试次数处理；
				System.out.println("Error on fetch sku at=" + DateUtil.formatDate(new Date(startTime), "yyyy-MM-dd HH:mm:ss")
						+ "  categoryId=" + categoryId + " article=" + sku.getSpuId() + " variant=" + variant);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("pic reTryCount=" + reTryCount + " at=" + DateUtil.formatDate(new Date(startTime), "yyyy-MM-dd HH:mm:ss")
				+ "  categoryId=" + categoryId + " article=" + sku.getSpuId() + " variant=" + variant + " tid="
				+ Thread.currentThread().getId());
		String imageUrl = "http://185.37.44.155:10088/catalogo/slideFoto.php?nome=" + encodeSpuId + "&variante=" + variant;
		try {
			HttpResponse resp = HttpUtils.get(imageUrl, headers);
			if (resp.getStatus() == 200) {
				String htmlContent = resp.getResponse();
				Document doc = Jsoup.parse(htmlContent);
				Elements imgList = doc.select("img");
				for (int i = 0; i < imgList.size(); i++) {
					ProductPictureDTO dto = new ProductPictureDTO();
					dto.setPicUrl("http://185.37.44.155:10088/catalogo/" + imgList.get(i).attr("src"));
					dto.setSupplierId(Startup.supplierId);
					dto.setId(UUIDGenerator.getUUID());
					dto.setSkuId(sku.getSkuId());
					DownloadPicTask task = new DownloadPicTask(sku, dto.getPicUrl(), sessionInfo);
					FetchProduct.picManager.delayTask(new DelayTask(task, new Date()));
				}
			}
		} catch (IOException e) {
			AppendSKUInfoTask task = new AppendSKUInfoTask(sessionInfo, sku, variant, categoryId, productFetchService);
			task.setReTryCount(reTryCount + 1);
			if (task.getReTryCount() <= 3) {
				FetchProduct.infoManager.delayTask(new DelayTask(task, new Date(System.currentTimeMillis() + 120000L)));
			} else {
				// 超过重试次数处理；
				System.out.println("Error on fetch sku at=" + DateUtil.formatDate(new Date(startTime), "yyyy-MM-dd HH:mm:ss")
						+ "  categoryId=" + categoryId + " article=" + sku.getSpuId() + " variant=" + variant);
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

class DownloadPicTask implements ITask {
	private int reTryCount = 0;

	private SkuDTO sku;

	private String imageUrl;

	private String sessionInfo;

	public DownloadPicTask(SkuDTO sku, String imageUrl, String sessionInfo) {
		this.sessionInfo = sessionInfo;
		this.imageUrl = imageUrl;
		this.sku = sku;
	}

	@Override
	public void doTask(long startTime) {
		if (imageUrl.equals("http://185.37.44.155:10088/catalogo/leggifoto.php?nome=")) {
			System.out.println("spuId=" + sku.getSpuId() + " does not have images.");
			return;
		}
		InputStream inStream = null;
		FileOutputStream fs = null;
		File imageFile = null;
		String folder = System.getProperty("java.io.tmpdir") + "/vinicco";
		if (Startup.storagePath != null && Startup.storagePath.length() > 2) {
			folder = Startup.storagePath + "/vinicco";
		}
		File parentFolder = new File(folder);
		if (!parentFolder.exists()) {
			parentFolder.mkdirs();
		}
		String spuId = sku.getSpuId();
		String filePath = folder + "/" + spuId + "_" + Md5.getMD5(imageUrl);
		String contentType = null;
		boolean error = false;
		try {
			URL url = new URL(imageUrl);
			URLConnection conn = url.openConnection();
			conn.setRequestProperty("Cookie", sessionInfo);
			conn.setConnectTimeout(60000);
			conn.setReadTimeout(300000);
			contentType = conn.getContentType();
			if (contentType == null) {
				return;
			}
			int index = contentType.indexOf("/");
			contentType = contentType.substring(index + 1);
			filePath += "." + contentType;
			int byteSum = 0;
			int byteRead = 0;
			imageFile = new File(filePath);
			if (imageFile.exists() && imageFile.length() > 1000) {
				return;
			}
			inStream = conn.getInputStream();
			fs = new FileOutputStream(imageFile);
			byte[] buffer = new byte[4096];
			while ((byteRead = inStream.read(buffer)) != -1) {
				byteSum += byteRead;
				fs.write(buffer, 0, byteRead);
			}
			fs.flush();
			System.out.println("文件下载成功.....size=" + byteSum);

		} catch (IOException e) {
			error = true;
			DownloadPicTask task = new DownloadPicTask(sku, imageUrl, sessionInfo);
			task.setReTryCount(reTryCount + 1);
			if (task.getReTryCount() <= 3) {
				FetchProduct.picManager.delayTask(new DelayTask(task, new Date(System.currentTimeMillis() + 300000L)));
			} else {
				// 超过重试次数处理；
				System.out.println("Error on fetch pic at=" + DateUtil.formatDate(new Date(startTime), "yyyy-MM-dd HH:mm:ss") + "  spuId="
						+ sku.getSpuId() + " uri=" + imageUrl);
			}

		} catch (Exception e) {
			error = true;
			System.out.println("contentType=" + contentType);
			e.printStackTrace();
			if (imageFile != null) {
				try {
					if (fs != null) {
						fs.close();
					}
				} catch (IOException e1) {
					fs = null;
				}
			}
		} finally {
			try {
				if (inStream != null) {
					inStream.close();
				}
			} catch (IOException e) {
				inStream = null;
			}
			try {
				if (fs != null) {
					fs.close();
				}
			} catch (IOException e) {
				fs = null;
			}
			if (error && imageFile != null && imageFile.exists()) {
				imageFile.delete();
			}
		}
	}

	public void setReTryCount(int reTryCount) {
		this.reTryCount = reTryCount;
	}

	public int getReTryCount() {
		return reTryCount;
	}
}