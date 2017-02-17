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
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.SendMail;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.ProductDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SkuRelationDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.dto.SupplierDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.SupplierService;

@Component("fetchProductFromHK")
public class FetchProductFromHK {
	private static Logger logger = Logger.getLogger("info");
	
	private static ResourceBundle bdl = null;
	private static String supplierId;
	public static String endDate;
	public static String startDate;
	private static String smtpHost = null;
	private static String from = null;
	private static String fromUserPassword = null;
	private static String to = null;
	private static String subject = null;
	private static String messageType = null;

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		startDate = bdl.getString("startDate");
		endDate = bdl.getString("endDate");

		smtpHost = bdl.getString("smtpHost");
		from = bdl.getString("from");
		fromUserPassword = bdl.getString("fromUserPassword");
		to = bdl.getString("to");
		subject = bdl.getString("subject");
		messageType = bdl.getString("messageType");
	}


	@Autowired
	SupplierService supplierService;
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
			o.fetchProductFromHK();
		} catch (final Exception e) {

			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						SendMail.sendMessage(
								smtpHost,
								from,
								fromUserPassword,
								to,
								subject,
								"从香港同步到本地数据出错，请检查原因:"+e.getMessage(),
								messageType);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			t.start();
		}
	}

	private void saveRelationFromHKBySupplierId(String supplier) {
		List<SkuRelationDTO> list = null;
			list = productFetchService.selectRelationFromHKBySupplierId(supplier);
			System.out.println("供应商"+supplier+"的relation总数："+list.size());
			saveAllRelation(list);
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

	public void fetchProductFromHK() throws Exception{
			//按供应商拉取数据
			if(StringUtils.isNotBlank(supplierId)){
				String [] arraySupplierId = supplierId.split(",",-1);
				for(String supplier:arraySupplierId){
					saveProductFromHKBySupplierId(supplier);
					saveRelationFromHKBySupplierId(supplier);
				}
			}else if(StringUtils.isNotBlank(startDate)){
				saveProductFromHKByDate();
			}else{
				List<SupplierDTO> list = null;
					list = supplierService.findByState("1");
					System.out.println("拉取到待更新的供应商总数："+list.size());
					for(SupplierDTO supp:list){
						saveProductFromHKBySupplierId(supp.getSupplierId());
						saveRelationFromHKBySupplierId(supp.getSupplierId());
					}
				} 
	}
	private void saveProductFromHKByDate() {
		List<ProductDTO> list = null;
		try {
			list = productFetchService.findProductByDate(startDate,endDate);
//			logger.info("拉取的供应商数据总数："+list.size());
			System.out.println("今日拉取的供应商数据总数："+list.size());
			saveSku(list);
			saveSpu(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void saveProductFromHKBySupplierId(String supplier) throws Exception{
		List<ProductDTO> list = null;
		list = productFetchService.findSkuBySupplierId(supplier);
		System.out.println("供应商:"+supplier+"的商品Product总数："+list.size());
		saveSku(list);
		saveSpu(list);
		
	}

	public void saveAllSkuFromHK() throws Exception{

		List<ProductDTO> list = null;
			list = productFetchService.selectAllSku();
			logger.info("拉取的所有SKU总数："+list.size());
			System.out.println("拉取的所有的SKU总数："+list.size());
			saveSku(list);
	}

	public void saveAllSpuFromHK() throws Exception{

		List<ProductDTO> list = null;
			list = productFetchService.selectAllSpu();
			logger.info("拉取的所有SPU总数："+list.size());
			System.out.println("拉取的所有的SPU总数："+list.size());
			saveSpu(list);
	}
	int i,j=0;
	private void saveSku(List<ProductDTO> list) throws Exception{
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
				sku.setSpSkuId(pro.getSpSkuId());
				try {
					productFetchService.saveSKU(sku);
					i++;
				} catch (ServiceException e) {
					if("数据插入失败键重复".equals(e.getMessage())){
						
					}else{
						throw e;
					}
					j++;
				}
			}
			System.out.println("save sku success数量："+i);
			System.out.println("重复的数量："+j);
			logger.info("save sku success数量："+i);
			logger.info("重复的数量："+j);
			i=0;
			j=0;
		}
	}
	int m,n=0;
	private void saveSpu(List<ProductDTO> list) throws Exception{
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
					if("数据插入失败键重复".equals(e.getMessage())){
						
					}else{
						throw e;
					}
					n++;
				}
			}
			
			logger.info("save spu success数量："+m);
			System.out.println("save spu success数量："+m);
			logger.info("重复的数量："+n);
			System.out.println("重复的数量："+n);
			m=0;
			n=0;
		}
	}
}