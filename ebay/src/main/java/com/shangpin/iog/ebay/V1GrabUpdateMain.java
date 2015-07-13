/**
 * 
 */
package com.shangpin.iog.ebay;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.sdk.SdkException;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;

/**
 * @description 
 * @author 陈小峰
 * <br/>2015年7月3日
 */
@Component
public class V1GrabUpdateMain extends AbsUpdateProductStock{
	@Autowired
	V1GrabService grabSrv;
	static Logger logger = LoggerFactory.getLogger(V1GrabUpdateMain.class);
	@Autowired
	public ProductFetchService fetchSrv;
	
	@Override
	public Map<String, Integer> grabStock(Collection<String> skuNo)
			throws ServiceException, Exception {
		Collection<String> itemIds=new HashSet<>(skuNo.size());
		for (Iterator<String> iterator = skuNo.iterator(); iterator.hasNext();) {
			String skuId = iterator.next();
			itemIds.add(skuId.split("#")[0]);
		}
		return grabSrv.getStock(itemIds);
	}
	/**
	 * 抓取供应商数据并保存
	 * @param sellerId ebay供应商卖家id
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void grabSaveProduct(String sellerId) {
		Date date = Calendar.getInstance().getTime();
		Date date2 = null;
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 4);c.add(Calendar.DATE, -2);
		date2 = c.getTime();
		Map<String, ? extends Collection> skuSpuAndPic = null;
		try {
			skuSpuAndPic = grabSrv.getSellerList(sellerId, date, date2);
		} catch (SdkException e1) {
			logger.error("抓取数据失败", e1);
			return;
		}
		Collection<SkuDTO> skus = skuSpuAndPic.get("sku");
		saveSku(skus);
		Collection<SpuDTO> spuDTOs = skuSpuAndPic.get("spu");
		saveSpu(spuDTOs);
		Collection<ProductPictureDTO> picUrls = skuSpuAndPic.get("pic");
		savePic(picUrls);
	}
	/**
	 * 通过find接口查询供应商店铺销售的item
	 * @param storeName 店铺名字
	 * @param brandName 品牌名字
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void grabSaveProduct4Find(String storeName,String brand){
	
		Map<String,? extends Collection> skuSpuAndPic=grabSrv.findStoreBrand(storeName,brand);
		Collection<SkuDTO> skus = skuSpuAndPic.get("sku");
		saveSku(skus);
		Collection<SpuDTO> spuDTOs = skuSpuAndPic.get("spu");
		saveSpu(spuDTOs);
		Collection<ProductPictureDTO> picUrls = skuSpuAndPic.get("pic");
		savePic(picUrls);
	}
	

	/**
	 * @param picUrls
	 */
	private void savePic(Collection<ProductPictureDTO> picUrls) {
		logger.info("pic数：{}", picUrls.size());
		int failCnt = 0;
		for (ProductPictureDTO picurl : picUrls) {
			try {
				fetchSrv.savePictureForMongo(picurl);
			} catch (ServiceException e) {
				logger.error("保存sku图片失败", e);
				failCnt++;
			}
		}
		logger.info("保存sku成功，失败数：{}", failCnt);
	}

	/**
	 * @param spuDTOs
	 */
	private void saveSpu(Collection<SpuDTO> spuDTOs) {
		logger.info("spu数：{}", spuDTOs.size());
		int failCnt = 0;
		for (SpuDTO spu : spuDTOs) {
			try {
				fetchSrv.saveSPU(spu);
			} catch (ServiceException e) {
				logger.error("保存spu失败失败", e);
				failCnt++;
			}
		}
		logger.info("保存sku成功，失败数：{}", failCnt);
	}

	/**
	 * @param skus
	 */
	private void saveSku(Collection<SkuDTO> skus) {
		logger.info("sku数：{}", skus.size());
		int failCnt = 0;
		for (SkuDTO sku : skus) {
			try {
				fetchSrv.saveSKU(sku);
			} catch (ServiceException e) {
				logger.error("保存sku失败", e);
				failCnt++;
			}
		}
		logger.info("保存sku成功，失败数：{}", failCnt);
	}
}
