package com.shangpin.iog.smets.service;

import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.shangpin.iog.dto.ProductDTO;
import com.shangpin.iog.product.dao.ProductsMapper;
import com.shangpin.product.AbsSaveProduct;

@Component("searchAndSend")
public class SearchAndSend extends AbsSaveProduct {
	
	private static Gson gson =  new Gson();
	private static Logger logger = Logger.getLogger("info");
	
	private static ResourceBundle bdl = null;
	private static String push_all = "";
	private static String supplierId = "";
	
	static{
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		push_all = bdl.getString("push_all");
		supplierId = bdl.getString("supplierId");
	}
	
	@Autowired
	private ProductsMapper productsDao;

	@Override
	public Map<String, Object> fetchProductAndSave() {
		List<ProductDTO> products = null;
		if("yes".equals(push_all)){
			products = productsDao.findAllOfProducts(supplierId, null, null);
		}else{
			products = productsDao.findAllProductWithNoSpSkuNo(supplierId, null, null);
		}
		logger.info("查找到："+products.size()+"条记录。");  
		if(CollectionUtils.isNotEmpty(products)){
			for(ProductDTO dto : products){
				supp.setData(gson.toJson(dto));
				pushMessage(null);
			}
		}
		return null;
	}

}
