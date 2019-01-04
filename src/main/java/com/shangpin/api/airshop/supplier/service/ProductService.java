package com.shangpin.api.airshop.supplier.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shangpin.api.airshop.config.ApiServiceUrlConfig;
import com.shangpin.api.airshop.dto.Product;
import com.shangpin.api.airshop.dto.PurchaseOrderDetail;
import com.shangpin.api.airshop.dto.PurchaseOrders;
import com.shangpin.api.airshop.util.DateFormat;
import com.shangpin.api.airshop.util.HttpClientUtil;
import com.shangpin.common.utils.FastJsonUtil;

/**
 * 获取海外商品信息
 * @author qinyingchun
 *
 */
@Service
public class ProductService{
	
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

	/**
	 * 查询供应商商品信息
	 * @param supplierNo 供应商编号
	 * @param supplierSkuNo 供应商sku编号，可以批量传入用，隔开（205401,202132）
	 * @return
	 */
	public String product(String supplierId, String supplierSkuNo){
//		String url=ApiServiceUrlConfig.getProductUri();
		//TODO:  因为studio上线是未测试，注销掉修改，恢复旧代码，以后再切换
		String url=ApiServiceUrlConfig.getHbProductUri();
		Map<String,String> request = new HashMap<>();
		request.put("supplierId",supplierId);
		request.put("skuId",supplierSkuNo);
		return HttpClientUtil.doPost(url, request);// String.class);
		/*Map<String, Object> params = new HashMap<String, Object>();
		params.put("sku", supplierNo);
		params.put("skuid", supplierSkuNo);
		
		String url = "";
		try {
			url = ApiServiceUrlConfig.getProductUri() + "/" + supplierNo + "/" + URLEncoder.encode(supplierSkuNo, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return HttpClientUtil.doGet(url);*/
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 获取商品信息列表
	 * @param supplierId 供应商编号
	 * @param supplierSkuNo,供应商sku编号,批量传入
	 * @return
	 */
	public List<Product> list(String supplierId, String supplierSkuNo){
		long start = System.currentTimeMillis();
		String json =product(supplierId, supplierSkuNo);
		logger.info(supplierSkuNo+"耗时："+(System.currentTimeMillis()-start)+",返回结果："+json);
		if(StringUtils.isEmpty(json) || "[]".equals(json)){
			return null;
		}
		List<Product> products = FastJsonUtil.deserializeString2ObjectList(json, Product.class);
		if(null == products){
			return null;
		}
		return products;
	}

	/**
	 * 获取商品信息列表
	 *
	 * @param supplierNo    供应商编号
	 * @param set 供应商sku编号
	 * @return Map<String,Product>
	 */
	public Map<String, Product> list(String supplierNo, Set<String> set) {
		String supplierSkuNo = StringUtils.collectionToDelimitedString(set, ",");
		List<Product> list = list(supplierNo, supplierSkuNo);
		if(list == null || list.isEmpty()){
			return null;
		}
		Map<String, Product> map = new HashMap<>();
		for (Product product : list) {
			map.put(product.getSkuId(), product);
		}
		return map;
	}

	/**
	 * 
	 * @param supplierNo 供应商编号
	 * @param supplierSkuNo 传入单一sku
	 * @return
	 */
	public Product detail(String supplierNo, String supplierSkuNo){
		List<Product> products = this.list(supplierNo, supplierSkuNo);
		if(null != products){
			return products.get(0);
		}
		return null;
	}
	
	/**
	 * 采购单列表中填充商品信息
	 * @param purchaseOrders 采购单列表
	 * @param sopUserNo 供应商编号
	 * @return
	 */
	public PurchaseOrders convert(PurchaseOrders purchaseOrders, String sopUserNo){
		List<PurchaseOrderDetail> purchaseOrderDetails = purchaseOrders.getPurchaseOrderDetails();
		if(null == purchaseOrderDetails || purchaseOrderDetails.size() == 0){
			return purchaseOrders;
		}
		String skus = "";
		Set<String> set = new HashSet<String>();
		for(PurchaseOrderDetail orderDetail : purchaseOrderDetails){
			String sku = orderDetail.getSupplierSkuNo();
			set.add(sku);
		}
		for(Iterator<String> iterator = set.iterator();iterator.hasNext();){  
            String sku = iterator.next();
            skus +=  "," + sku ;
        }
		List<Product> products = null;
		skus = skus.substring(1);
		logger.info("skus:"+skus);
			products = this.list(sopUserNo, skus);
		for(PurchaseOrderDetail detail : purchaseOrderDetails){
			detail.setCreateTime(DateFormat.TimeFormatChangeToString(detail.getCreateTime(), "yyyy-MM-dd HH:mm:ss", "dd-MM-yyyy HH:mm:ss"));
			detail.setDateArrival(DateFormat.TimeFormatChangeToString(detail.getDateArrival(), "yyyy-MM-dd HH:mm:ss", "dd-MM-yyyy HH:mm:ss"));
			detail.setDateCanceled(DateFormat.TimeFormatChangeToString(detail.getDateCanceled(), "yyyy-MM-dd HH:mm:ss", "dd-MM-yyyy HH:mm:ss"));
			detail.setProductModel(null);
			if(null == products){
				if(detail.getSize()==null){
					detail.setSize("");
				}
				if(detail.getProductName()==null){
					detail.setProductName("");
				}
				if(detail.getColor()==null){
					detail.setColor("");
				}
			}else {
				for(Product product : products){
					if(detail.getSupplierSkuNo().equals(product.getSkuId())){
						
						if(org.apache.commons.lang3.StringUtils.isNotBlank(product.getProductName())){
							detail.setProductName(product.getProductName());
						}
						
						if(org.apache.commons.lang3.StringUtils.isNotBlank(product.getSize())){
							detail.setSize(product.getSize());	
						}
						if(org.apache.commons.lang3.StringUtils.isNotBlank(product.getColor())){
							detail.setColor(product.getColor());
						}
						if(org.apache.commons.lang3.StringUtils.isNotBlank(product.getProductCode())){
							detail.setProductModel(product.getProductCode());
						}
						if(org.apache.commons.lang3.StringUtils.isNotBlank(product.getProductUrl())){
							detail.setProductUrl(product.getProductUrl());
						}
					}
				}
			}
		}
		return purchaseOrders;
	}
}
