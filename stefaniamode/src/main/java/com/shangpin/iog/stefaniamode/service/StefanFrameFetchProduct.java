package com.shangpin.iog.stefaniamode.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.ObjectXMLUtil;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.stefaniamode.dto.Item;
import com.shangpin.iog.stefaniamode.dto.Items;
import com.shangpin.iog.stefaniamode.dto.Product;
import com.shangpin.iog.stefaniamode.dto.Products;
import com.shangpin.product.AbsSaveProduct;

@Component("framestefaniamode")
public class StefanFrameFetchProduct extends AbsSaveProduct{
	private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
	final Logger logger = Logger.getLogger("info");

	private static ResourceBundle bdl = null;

	public static String supplierId;

	public static String url;
	public static String picpath;
	public static int day;

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));
		url = bdl.getString("url");
		picpath = bdl.getString("picpath");
	}
	//sku:List(skuDTO) spu:List(spuDTO) image: Map(id;picName,List) 
	@Override
	public Map<String, Object> fetchProductAndSave() {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<SkuDTO> skuList = new ArrayList<SkuDTO>();
		List<SpuDTO> spuList = new ArrayList<SpuDTO>();
		Map<String,List<String>> imageMap = new HashMap<String, List<String>>();
		
		String origin = "";
		Products products = null;
		List<Product> productList = new ArrayList<Product>();
		String xml = null;
		xml = HttpUtil45.get(url,new OutTimeConfig(1000 * 60*60, 1000 * 60*60, 1000 * 60*60),null);
		ByteArrayInputStream is = null;
		System.out.println(url);
		try {
			is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
			products = ObjectXMLUtil.xml2Obj(Products.class,is);
//			products = ObjectXMLUtil.xml2Obj(Products.class, new File("F://StefaniaMode.xml"));
			if (products.getProducts()!=null&&products.getProducts().size()>0) {
				productList.addAll(products.getProducts());
			}
		} catch (Exception e2) {
			e2.printStackTrace();
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
				sku.setSupplierPrice("");
				sku.setColor(item.getColor());
				sku.setProductDescription(item.getDescription());
				sku.setStock(item.getStock());
				sku.setProductCode(product.getProductId());
				sku.setSaleCurrency("EUR");
				sku.setBarcode(item.getBarcode());
				skuList.add(sku);
//				if (StringUtils.isNotBlank(item.getPicture())) {
//					String[] picArray = item.getPicture().split("\\|");
//					imageMap.put(sku.getSkuId()+";"+sku.getProductCode()+" "+sku.getColor(), Arrays.asList(picArray));
//				}
			}
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
			spuList.add(spu);
		}
		returnMap.put("sku", skuList);
		returnMap.put("spu", spuList);
		returnMap.put("image", imageMap);
		return returnMap;
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
	public static void main(String[] args) throws Exception {
	  	//加载spring
        loadSpringContext();
        StefanFrameFetchProduct stockImp =(StefanFrameFetchProduct)factory.getBean("framestefaniamode");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		stockImp.handleData("sku", supplierId, day, picpath);
	}
}
