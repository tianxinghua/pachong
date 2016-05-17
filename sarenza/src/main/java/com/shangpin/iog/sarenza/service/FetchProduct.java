package com.shangpin.iog.sarenza.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.HttpUtils;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.sarenza.dto.Product;
import com.shangpin.iog.sarenza.util.FtpUtil;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by loyalty on 15/6/8.
 */
@Component("pozzilei")
public class FetchProduct {
	final Logger logger = Logger.getLogger(this.getClass());
	private static Logger logError = Logger.getLogger("error");
	private static ResourceBundle bdl = null;
	private static String supplierId = "";
	private static int day;

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));
	}

	@Autowired
	private ProductFetchService pfs;

	@Autowired
	ProductSearchService productSearchService;

	public void fetchProductAndSave() {

		Date startDate, endDate = new Date();
		startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate, day
				* -1, "D");
		// 获取原有的SKU 仅仅包含价格和库存
		Map<String, SkuDTO> skuDTOMap = new HashedMap();
//		try {
//			skuDTOMap = productSearchService.findStockAndPriceOfSkuObjectMap(
//					supplierId, startDate, endDate);
//		} catch (ServiceException e) {
//			e.printStackTrace();
//		}
		List<Product> list = null;
		try {
			list = FtpUtil.readLocalCSV(Product.class);

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (list != null) {
			for (Product spu : list) {
				List<String> picUrl = new ArrayList();
				if (StringUtils.isNotBlank(spu.getPicture1HD())) {
					picUrl.add(spu.getPicture1HD());
				}
				if (StringUtils.isNotBlank(spu.getPicture2HD())) {
					picUrl.add(spu.getPicture2HD());
				}
				if (StringUtils.isNotBlank(spu.getPicture3HD())) {
					picUrl.add(spu.getPicture3HD());
				}
				if (StringUtils.isNotBlank(spu.getPicture4HD())) {
					picUrl.add(spu.getPicture4HD());
				}
				if (StringUtils.isNotBlank(spu.getPicture5HD())) {
					picUrl.add(spu.getPicture5HD());
				}
				if (StringUtils.isNotBlank(spu.getPicture6HD())) {
					picUrl.add(spu.getPicture6HD());
				}
				if (StringUtils.isNotBlank(spu.getPicture7HD())) {
					picUrl.add(spu.getPicture7HD());
				}
				if (StringUtils.isNotBlank(spu.getPicture8HD())) {
					picUrl.add(spu.getPicture8HD());
				}
				if (StringUtils.isNotBlank(spu.getPicture9HD())) {
					picUrl.add(spu.getPicture9HD());
				}
				// String picUrl =
				String url = StringUtils.join(picUrl.toArray(), "|");
				// spu入库
				SpuDTO spudto = new SpuDTO();
				spudto.setBrandName(spu.getBrandLabel());
				spudto.setCategoryGender(spu.getGenderLabel());
				spudto.setCategoryName(spu.getSubTypeLabel1());
				spudto.setCreateTime(new Date());
				spudto.setSeasonId(spu.getSeasonLabel());
				spudto.setSeasonName(spu.getSeasonLabel());
				spudto.setSupplierId(supplierId);
				spudto.setSpuId(spu.getProductId()+"-"+spu.getPCID());
				spudto.setId(UUIDGenerator.getUUID());
				spudto.setMaterial(spu.getMaterialLabel());
				spudto.setProductOrigin(spu.getMadeIn());
				spudto.setPicUrl(url);
				spudto.setSpuName(spu.getTitle());
				try {
					pfs.saveSPU(spudto);
				} catch (ServiceException e) {
					logError.error(e.getMessage());
					try {
						pfs.updateMaterial(spudto);
					} catch (ServiceException ex) {
						logError.error(ex.getMessage());
						ex.printStackTrace();
					}
				}
				SkuDTO skudto = new SkuDTO();
				skudto.setCreateTime(new Date());
				// skudto.setBarcode(sku.getBarcode()+"|"+database);
				skudto.setColor(spu.getSupplierColorLabel());
				skudto.setId(UUIDGenerator.getUUID());
				skudto.setProductCode(spu.getProductCode());
				skudto.setProductDescription(spu.getDescription());
				skudto.setProductName(spu.getTitle());
				// skudto.setSaleCurrency("EUR");
				skudto.setProductSize(spu.getSupplierSize());
				skudto.setSkuId(spu.getProductId()+"-"+spu.getPCID()+ "-"
						+ spu.getSupplierSize());
				skudto.setMarketPrice(spu.getPrice());
				skudto.setSpuId(spu.getProductId()+"-"+spu.getPCID());
				skudto.setStock(spu.getStock());
				skudto.setSupplierId(supplierId);
				try {
					if (skuDTOMap.containsKey(skudto.getSkuId())) {
						skuDTOMap.remove(skudto.getSkuId());
					}
					pfs.saveSKU(skudto);
				} catch (ServiceException e) {
					try {
						if (e.getMessage().equals("数据插入失败键重复")) {
							pfs.updatePriceAndStock(skudto);
						} else {
							e.printStackTrace();
						}
					} catch (ServiceException e1) {
						e1.printStackTrace();
					}
				}
				// 保存图片
				pfs.savePicture(supplierId, null, skudto.getSkuId(), picUrl);
			}
		}
		// 更新网站不再给信息的老数据
		for (Iterator<Map.Entry<String, SkuDTO>> itor = skuDTOMap.entrySet()
				.iterator(); itor.hasNext();) {
			Map.Entry<String, SkuDTO> entry = itor.next();
			if (!"0".equals(entry.getValue().getStock())) {// 更新不为0的数据 使其库存为0
				entry.getValue().setStock("0");
				try {
					pfs.updatePriceAndStock(entry.getValue());
				} catch (ServiceException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
