package com.shangpin.iog.inviqa.service;

/**
 * Created by wang on 2015/9/21.
 */

import java.io.File;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TimeZone;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.inviqa.dto.Product;
import com.shangpin.iog.inviqa.util.MyJsonUtil;
import com.shangpin.iog.service.EventProductService;
import com.shangpin.iog.service.ProductFetchService;

/**
 * Created by 赵根春 on 2015/12/25.
 */
@Component("inviqa")
public class FetchProduct {

	@Autowired
	ProductFetchService productFetchService;

	@Autowired
	EventProductService eventProductService;
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
	}

	/**
	 * fetch product and save into db
	 */
	public void fetchProductAndSave() {

		List<Product> list = MyJsonUtil.getProductList();
		messMappingAndSave(list);
	}
	/**
	 * message mapping and save into DB
	 */
	private void messMappingAndSave(List<Product> array) {

		if (array != null) {
			
			
			for (Product item : array) {
				SpuDTO spu = new SpuDTO();
				try {
					spu.setId(UUIDGenerator.getUUID());
					spu.setSupplierId(supplierId);
					spu.setSpuId(item.getSpuId());
					spu.setCategoryName(item.getCategoryName());
					spu.setSubCategoryName(item.getSubCategoryName());
					spu.setBrandName(item.getBrandName());
					spu.setSpuName(item.getProductName());
					if(!"NO DATA AVAILABLE".equals(item.getMaterial())){
						spu.setMaterial(item.getMaterial());
					}
					
					spu.setCategoryGender(item.getCategoryGender());
					productFetchService.saveSPU(spu);

				} catch (Exception e) {
					try {
						productFetchService.updateMaterial(spu);
					} catch (ServiceException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				String image = item.getImages();

				if (StringUtils.isNotBlank(image)) {
					String images[] = image.split("\\|\\|");
					productFetchService.savePicture(supplierId, null,
							item.getSkuId(), Arrays.asList(images));
				}

				SkuDTO sku = new SkuDTO();
				try {
					sku.setId(UUIDGenerator.getUUID());
					sku.setSupplierId(supplierId);
					sku.setSpuId(item.getSpuId());
					String proSize = item.getSize();
					if ("No".equals(proSize)) {
						sku.setProductSize("A");
					} else {
						sku.setProductSize(proSize);
					}
					sku.setSkuId(item.getSkuId());
					sku.setStock(item.getStock());
					sku.setSalePrice(item.getSalePrice());
					sku.setMarketPrice(item.getMarketPrice());
					sku.setSupplierPrice(item.getSupplierPrice());
					sku.setColor(item.getColor());
					sku.setProductName(item.getProductName());
					sku.setProductDescription(item.getProductDescription());
					sku.setProductCode(item.getProductCode());
					sku.setSaleCurrency(item.getSaleCurrency());
					productFetchService.saveSKU(sku);
				} catch (ServiceException e) {
					if (e.getMessage().equals("数据插入失败键重复")) {
						try {
							productFetchService.updatePriceAndStock(sku);
						} catch (ServiceException e1) {
							e1.printStackTrace();
						}
					} else {
						e.printStackTrace();
					}

				}
			}
		}
	}

	private static String getString(String ts) throws ParseException {
		ts = ts.replace("Z", " UTC");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
		Date dt = sdf.parse(ts);
		TimeZone tz = sdf.getTimeZone();
		Calendar c = sdf.getCalendar();
		StringBuffer result = new StringBuffer();
		result.append(c.get(Calendar.YEAR));
		result.append("-");
		result.append((c.get(Calendar.MONTH) + 1));
		result.append("-");
		result.append(c.get(Calendar.DAY_OF_MONTH));
		result.append(" ");
		result.append(c.get(Calendar.HOUR_OF_DAY));
		result.append(":");
		result.append(c.get(Calendar.MINUTE));
		result.append(":");
		result.append(c.get(Calendar.SECOND));
		return result.toString();
	}
}
