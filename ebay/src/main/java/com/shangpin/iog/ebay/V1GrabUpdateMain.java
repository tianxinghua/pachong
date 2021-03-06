/**
 * 
 */
package com.shangpin.iog.ebay;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ebay.sdk.SdkException;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.json.JsonUtil;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.ebay.page.PageGrabService;
import com.shangpin.iog.service.ProductFetchService;

/**
 * ebay数据抓取，库存更新服务主入口，数据保存入口
 * @description 
 * @author 陈小峰
 * @see PageGrabService 分页抓取保存 
 * <br/>2015年7月3日
 */
@Component
@Deprecated
public class V1GrabUpdateMain{
	@Autowired
	V1GrabService grabSrv;
	static Logger logger = LoggerFactory.getLogger(V1GrabUpdateMain.class);
	@Autowired
	public ProductFetchService fetchSrv;
	
	/*@Override
	public Map<String, Integer> grabStock(Collection<String> skuNo)
			throws ServiceException, Exception {
		Collection<String> itemIds=new HashSet<>(skuNo.size());
		for (Iterator<String> iterator = skuNo.iterator(); iterator.hasNext();) {
			String skuId = iterator.next();
			itemIds.add(skuId.split("#")[0]);
		}
		return grabSrv.getStock(itemIds);
	}*/
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
	public void grabSaveProduct4Find(String storeName,String brandName){
	
		Map<String,? extends Collection> skuSpuAndPic=grabSrv.findStoreBrand(storeName,brandName);
		String threadName = Thread.currentThread().getName();
		if(skuSpuAndPic==null ||skuSpuAndPic.size()==0) {
			logger.warn("线程{}没有抓取到数据",threadName);
			return ;
		}
		Collection<SkuDTO> skus = skuSpuAndPic.get("sku");
		logger.info("线程{}抓取到sku数{}",threadName,skus.size());
		saveSku(skus);
		Collection<SpuDTO> spuDTOs = skuSpuAndPic.get("spu");
		logger.info("线程{}抓取到spu数{}",threadName,spuDTOs.size());
		saveSpu(spuDTOs);
		Collection<ProductPictureDTO> picUrls = skuSpuAndPic.get("pic");
		logger.info("线程{}抓取到pic数{}",threadName,picUrls.size());
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
				logger.error("保存图片{}失败,error:{}", JsonUtil.getJsonString4JavaPOJO(picurl), e.getMessage());
				failCnt++;
			}
		}
		logger.info("保存pic数：{}成功，失败数：{}", picUrls.size(),failCnt);
	}

	/**
	 * @param spuDTOs
	 */
	private void saveSpu(Collection<SpuDTO> spuDTOs) {
		//logger.info("spu数：{}", spuDTOs.size());
		int failCnt = 0;
		for (SpuDTO spu : spuDTOs) {
			try {
				fetchSrv.saveSPU(spu);
			} catch (ServiceException e) {
				logger.error("保存spu:{}失败,error:{}",JsonUtil.getJsonString4JavaPOJO(spu), e.getMessage());
				failCnt++;
			}
		}
		logger.info("保存spu数：{}成功，失败数：{}", spuDTOs.size(),failCnt);
	}

	/**
	 * @param skus
	 */
	private void saveSku(Collection<SkuDTO> skus) {
		//logger.info("sku数：{}", skus.size());
		int failCnt = 0;
		for (SkuDTO sku : skus) {
			try {
				fetchSrv.saveSKU(sku);
			} catch (ServiceException e) {
				logger.error("保存sku:{}失败,error:{}", JsonUtil.getJsonString4JavaPOJO(sku),e.getMessage());
				failCnt++;
			}
		}
		logger.info("保存sku数：{}成功，失败数：{}", skus.size(),failCnt);
	}
}
