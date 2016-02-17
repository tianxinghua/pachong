package com.shangpin.iog.stefaniamode.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import javax.xml.bind.JAXBException;

import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.service.ProductSearchService;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.stefaniamode.dto.Item;
import com.shangpin.iog.stefaniamode.dto.Items;
import com.shangpin.iog.stefaniamode.dto.Product;
import com.shangpin.iog.stefaniamode.dto.Products;

/**
 * Created by Jerry Wang on 2015/09/23.
 */
@Component("stefaniamode")
public class FetchProduct {
	final Logger logger = Logger.getLogger(this.getClass());
	private static Logger infoLogger = Logger.getLogger("info");

	private static ResourceBundle bdl = null;

	public static String supplierId;

	public static String zipUrl;

	public static int day;

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));
		zipUrl = bdl.getString("zipUrl");
	}

	@Autowired
	ProductFetchService productFetchService;
	@Autowired
	ProductSearchService productSearchService;
	public void fetchProductAndSave() {

		try {
			String origin = "";
			String xmlContent = "";
			Products products = null;
			List<Product> productList = new ArrayList<Product>();
			Date startDate,endDate= new Date();
			startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate,day*-1,"D");
			//获取原有的SKU 仅仅包含价格和库存
			Map<String,SkuDTO> skuDTOMap = new HashedMap();
			try {
				skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(supplierId,startDate,endDate);
			} catch (ServiceException e) {
				e.printStackTrace();
			}
			
			String[] urls = zipUrl.split(",");
			for (String url : urls) {
				xmlContent = FetchProduct.downLoadAndReadXml(url);
				products = ObjectXMLUtil.xml2Obj(Products.class,xmlContent);
				if (products.getProducts()!=null&&products.getProducts().size()>0) {
					productList.addAll(products.getProducts());
				}
			}
			for (Product product : productList) {
				SpuDTO spu = new SpuDTO();

				Items items = product.getItems();
				if (null == items) {// 无SKU
					continue;
				}

				List<Item> itemList = items.getItems();
				if (null == itemList)
					continue;
				String skuId = "";
				for (Item item : itemList) {
					SkuDTO sku = new SkuDTO();
					try {
						Integer stock = Integer.parseInt(item.getStock());
						if (stock == 0) {
							continue;
						}
						sku.setId(UUIDGenerator.getUUID());
						sku.setSupplierId(supplierId);

						sku.setSpuId(product.getProductId());
						skuId = item.getItem_id();
						if (skuId.indexOf("½") > 0) {
							skuId = skuId.replace("½", "+");
						}
						sku.setSkuId(skuId);
						sku.setProductSize(item.getItem_size());
						sku.setMarketPrice(item.getMarket_price());
						sku.setSalePrice(item.getSell_price());
						// sku.setSupplierPrice(item.getSupply_price());
						sku.setColor(item.getColor());
						sku.setProductDescription(item.getDescription());
						sku.setStock(item.getStock());
						sku.setProductCode(product.getProducer_id());
						sku.setSaleCurrency("EUR");
						sku.setBarcode(item.getBarcode());
						if(skuDTOMap.containsKey(sku.getSkuId())){
							skuDTOMap.remove(sku.getSkuId());
						}

						productFetchService.saveSKU(sku);

					} catch (ServiceException e) {
						try {
							if (e.getMessage().equals("数据插入失败键重复")) {
								// 更新价格和库存
								productFetchService.updatePriceAndStock(sku);
							} else {
								e.printStackTrace();
							}

						} catch (ServiceException e1) {
							e1.printStackTrace();
						}
					}
					
					if (StringUtils.isNotBlank(item.getPicture())) {
						String[] picArray = item.getPicture().split("\\|");
						productFetchService.savePicture(supplierId, null, item.getItem_id(), Arrays.asList(picArray));
					}
				}

				try {
					spu.setId(UUIDGenerator.getUUID());
					spu.setSupplierId(supplierId);
					spu.setSpuId(product.getProductId());
					spu.setBrandName(product.getProduct_brand());
					spu.setCategoryName(product.getCategory());
					spu.setSpuName(product.getProduct_name());
					spu.setSeasonId(product.getSeason_code());
					spu.setMaterial(product.getProduct_material());
					if (StringUtils.isNotBlank(product.getMade_in())) {
						origin = product.getMade_in();
					}
					spu.setProductOrigin(origin);
					// 商品所属性别字段；
					spu.setCategoryGender(product.getMain_category());
					productFetchService.saveSPU(spu);
				} catch (ServiceException e) {
					try {
						productFetchService.updateMaterial(spu);
					} catch (ServiceException e1) {
						e1.printStackTrace();
					}
					e.printStackTrace();
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
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	public static String readZipFile(String file) throws Exception {
		String content = "";
		ZipFile zf = new ZipFile(file);
		InputStream in = new BufferedInputStream(new FileInputStream(file));
		ZipInputStream zin = new ZipInputStream(in);
		ZipEntry ze;
		while ((ze = zin.getNextEntry()) != null) {
			if (!ze.isDirectory()) {
				System.out.println("file - " + ze.getName() + " : "
						+ ze.getSize() + " bytes");
				long size = ze.getSize();
				if (size > 0) {
					BufferedReader br = new BufferedReader(
							new InputStreamReader(zf.getInputStream(ze)));
					String line;
					while ((line = br.readLine()) != null) {
						content += line;
					}
					br.close();
				}
			}
		}
		zin.close();
		zf.close();
		return content;
	}

	public static String downLoadAndReadXml(String zipUrl) {
		int byteSum = 0;
		int byteRead = 0;
		String content = "";
		SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
		String folder = System.getProperty("java.io.tmpdir");
		String localFilePath = folder + "/stefaniamode_"
				+ sf1.format(new Date()) + ".zip";
		File zipFile = new File(localFilePath);
		if (zipFile.exists()) {
			// 测试时下载成功一次可以重复使用文件内容；
			// try {
			// return readZipFile(localFilePath);
			// } catch (Exception e) {
			// e.printStackTrace();
			// }
			zipFile.delete();
		}
		InputStream inStream = null;
		FileOutputStream fs = null;
		try {
			URL url = new URL(zipUrl);
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(3600000);
			conn.setReadTimeout(3600000);
			inStream = conn.getInputStream();
			fs = new FileOutputStream(zipFile);
			byte[] buffer = new byte[4096];
			while ((byteRead = inStream.read(buffer)) != -1) {
				byteSum += byteRead;
				fs.write(buffer, 0, byteRead);
			}
			fs.flush();
			content = readZipFile(localFilePath);
			// XML内容读取完毕应删除文件
			zipFile.delete();
			System.out.println("文件下载成功.....size=" + byteSum);
		} catch (Exception e) {
			System.out.println("下载异常" + e);
			return null;
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
		}

		return content;
	}

}
