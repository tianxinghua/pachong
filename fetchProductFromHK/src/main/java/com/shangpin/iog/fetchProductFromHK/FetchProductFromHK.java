package com.shangpin.iog.fetchProductFromHK;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.ProductDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SkuRelationDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;

@Component("fetchProductFromHK")
public class FetchProductFromHK {
	private static Logger logger = Logger.getLogger("info");
	private static Logger loggerError = Logger.getLogger("error");
	
	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static String relationFlag;
	public static String productFlag;
	public static String endDate;
	public static String startDate;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		relationFlag = bdl.getString("relationFlag");
		productFlag = bdl.getString("productFlag");
		startDate = bdl.getString("startDate");
		endDate = bdl.getString("endDate");
	}
	
	
	@Autowired
	private ProductFetchService productFetchService;
	private static ApplicationContext factory;

	private static void loadSpringContext() {

		factory = new AnnotationConfigApplicationContext(AppContext.class);
	}

	public static void main(String[] args) {

		loadSpringContext();
		FetchProductFromHK o = (FetchProductFromHK) factory
				.getBean("fetchProductFromHK");
		try {
			o.fetchRelationFromHK();
			o.fetchProductFromHK();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void fetchRelationFromHK() {
		//false 表示按每天拉取
		if ("false".equals(relationFlag)) {
			System.out.println("false");
			saveRelationDayFromHK();
		} else {
			//true 表示拉取所有的
			System.out.println("true");
			fetchAndsaveAllRelationFromHK();
//			writeGrapDate("false", "initRelation.ini");

		}
	}

	private void fetchAndsaveAllRelationFromHK() {
		List<SkuRelationDTO> list = null;
		try {
			list = productFetchService.selectAllRelation();
			logger.info("拉取所有的relation总数："+list.size());
			System.out.println("拉取所有的relation总数："+list.size());
			saveAllRelation(list);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

	private void saveRelationDayFromHK() {
		List<SkuRelationDTO> list = null;
		try {
			list = productFetchService.selectRelationDayFromHK();
			logger.info("今日拉取的relation总数："+list.size());
			System.out.println("今日拉取的relation总数："+list.size());
			saveAllRelation(list);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}
	private void saveAllRelation(List<SkuRelationDTO> list) {
		if (list != null) {
			for (SkuRelationDTO pro : list) {
				SkuRelationDTO sku = new SkuRelationDTO();
				sku.setSupplierId(pro.getSupplierId());
				sku.setSopNo(pro.getSopNo());
				sku.setSopSkuId(pro.getSopSkuId());
				sku.setSupplierSkuId(pro.getSupplierSkuId());
				try {
					productFetchService.saveSkuRelation(sku);
					i++;
				} catch (ServiceException e) {
					j++;
				}
			}
			logger.info("save success数量："+i);
			System.out.println("save success数量："+i);
			logger.info("重复的数量："+j);
			System.out.println("重复的数量："+j);
			i=0;
			j=0;
		}
	}

	public void fetchProductFromHK() {

		//按供应商拉取数据
		if(StringUtils.isNotBlank(supplierId)){
			String [] arraySupplierId = supplierId.split(",",-1);
			for(String supplier:arraySupplierId){
				saveProductFromHKBySupplierId(supplier);
			}
			
		}else if(StringUtils.isNotBlank(startDate)){
			saveProductFromHKByDate();
		}else{
			saveSkuDayFromHK();
			saveSpuDayFromHK();
		}
		//false 表示按每天拉取
//		if ("false".equals(supplierId)) {
//			System.out.println("false");
//			saveSkuDayFromHK();
//			saveSpuDayFromHK();
//		} else {
//			//true 表示拉取所有的
//			System.out.println("true");
//			saveAllSkuFromHK();
//			saveAllSpuFromHK();
//			writeGrapDate("false", "initSkuSpu.ini");
//
//		}
	}
	private void saveProductFromHKByDate() {
		List<ProductDTO> list = null;
		try {
			list = productFetchService.findProductByDate(startDate,endDate);
			logger.info("拉取的供应商数据总数："+list.size());
			System.out.println("今日拉取的供应商数据总数："+list.size());
			saveSku(list);
			saveSpu(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void saveProductFromHKBySupplierId(String supplier) {
		List<ProductDTO> list = null;
		try {
			list = productFetchService.findSkuBySupplierId(supplier);
			logger.info("拉取的供应商:"+supplier+"数据总数："+list.size());
			System.out.println("今日拉取的SKU总数："+list.size());
			saveSku(list);
			saveSpu(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	private void saveSkuDayFromHK() {

		List<ProductDTO> list = null;
		try {
			list = productFetchService.selectSkuByDay();
			logger.info("今日拉取的SKU总数："+list.size());
			System.out.println("今日拉取的SKU总数："+list.size());
			saveSku(list);
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
	}
	
	public void saveSpuDayFromHK() {

		List<ProductDTO> list = null;
		try {
			list = productFetchService.selectSpuByDay();
			logger.info("今日拉取的SPU总数："+list.size());
			System.out.println("今日拉取的SPU总数："+list.size());
			saveSpu(list);
		} catch (ServiceException e1) {
			e1.printStackTrace();
		}
	}

	public void saveAllSkuFromHK() {

		List<ProductDTO> list = null;
		try {
			list = productFetchService.selectAllSku();
			logger.info("拉取的所有SKU总数："+list.size());
			System.out.println("拉取的所有的SKU总数："+list.size());
			saveSku(list);
		} catch (ServiceException e1) {
			e1.printStackTrace();
		}
	}

	public void saveAllSpuFromHK() {

		List<ProductDTO> list = null;
		try {
			list = productFetchService.selectAllSpu();
			logger.info("拉取的所有SPU总数："+list.size());
			System.out.println("拉取的所有的SPU总数："+list.size());
			saveSpu(list);
		} catch (ServiceException e1) {
			e1.printStackTrace();
		}
	}
	int i,j=0;
	private void saveSku(List<ProductDTO> list){
		if (list != null) {
			for (ProductDTO pro : list) {
				SkuDTO sku = new SkuDTO();
				sku.setId(UUIDGenerator.getUUID());
				sku.setBarcode(pro.getBarcode());
				sku.setColor(pro.getColor());
				sku.setMarketPrice(pro.getMarketPrice());
				sku.setProductCode(pro.getProductCode());
				sku.setProductDescription(pro.getProductDescription());
				sku.setProductName(pro.getProductName());
				sku.setProductSize(pro.getSize());
				sku.setSaleCurrency(pro.getSaleCurrency());
				sku.setSalePrice(pro.getSalePrice());
				sku.setSkuId(pro.getSkuId());
				sku.setSpuId(pro.getSpuId());
				sku.setStock(pro.getStock());
				sku.setSupplierId(pro.getSupplierId());
				sku.setSupplierPrice(pro.getSupplierPrice());
				sku.setNewMarketPrice(pro.getNewMarketPrice());
				sku.setNewSalePrice(pro.getNewSalePrice());
				sku.setNewSupplierPrice(pro.getNewSupplierPrice());
				try {
					productFetchService.saveSKU(sku);
					i++;
				} catch (ServiceException e) {
					j++;
				}
			}
			logger.info("save success数量："+i);
			System.out.println("save success数量："+i);
			logger.info("重复的数量："+j);
			System.out.println("重复的数量："+j);
			i=0;
			j=0;
		}
	}
	int m,n=0;
	private void saveSpu(List<ProductDTO> list){
		if (list != null) {
			for (ProductDTO pro : list) {
				SpuDTO spu = new SpuDTO();
				spu.setBrandId(pro.getBrandId());
				spu.setId(UUIDGenerator.getUUID());
				spu.setBrandName(pro.getBrandName());
				spu.setCategoryGender(pro.getCategoryGender());
				spu.setCategoryId(pro.getCategoryId());
				spu.setCategoryName(pro.getCategoryName());
				spu.setMaterial(pro.getMaterial());
				spu.setProductOrigin(pro.getProductOrigin());
				spu.setSeasonId(pro.getSeasonId());
				spu.setSeasonName(pro.getSeasonName());
				spu.setSpuId(pro.getSpuId());
				spu.setSpuName(pro.getSpuName());
				spu.setSubCategoryId(pro.getSubCategoryId());
				spu.setSubCategoryName(pro.getSubCategoryName());
				spu.setSupplierId(pro.getSupplierId());
				try {
					productFetchService.saveSPU(spu);
					m++;
				} catch (Exception e) {
					n++;
				}
			}
			
			logger.info("save success数量："+m);
			System.out.println("save success数量："+m);
			logger.info("重复的数量："+n);
			System.out.println("重复的数量："+n);
			m=0;
			n=0;
		}
	}
//	private String readFile(String fileName) {
//
//		Scanner scanner = null;
//		StringBuilder buffer = new StringBuilder();
//		try {
//			File file = getConfFile(fileName);
//			scanner = new Scanner(file, "utf-8");
//			while (scanner.hasNextLine()) {
//				buffer.append(scanner.nextLine());
//			}
//		} catch (Exception e) {
//
//		} finally {
//			if (scanner != null) {
//				scanner.close();
//			}	
//		}
//		return buffer.toString();
//	}

	private static void writeGrapDate(String date, String fileName) {
		File df = null;
		try {
			df = getConfFile(fileName);

			try (BufferedWriter bw = new BufferedWriter(new FileWriter(df))) {
				bw.write(date);
			}
		} catch (IOException e) {
			// logger.error("写入日期配置文件错误");
		}
	}

	private static File getConfFile(String fileName) throws IOException {
		String realPath = FetchProductFromHK.class.getClassLoader()
				.getResource("").getFile();
		realPath = URLDecoder.decode(realPath, "utf-8");
		File df = new File(realPath + fileName);// "date.ini"
		if (!df.exists()) {
			df.createNewFile();
		}
		return df;
	}
}
