package com.shangpin.iog.stockMontiHK.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.iog.common.utils.SendMail;
import com.shangpin.iog.dto.StockUpdateDTO;
import com.shangpin.iog.dto.SupplierDTO;
import com.shangpin.iog.service.SkuPriceService;
import com.shangpin.iog.service.SupplierService;
import com.shangpin.iog.service.UpdateStockService;
import com.shangpin.openapi.api.sdk.client.SpClient;
import com.shangpin.openapi.api.sdk.model.ApiResponse;
import com.shangpin.openapi.api.sdk.model.SopProductSku;
import com.shangpin.openapi.api.sdk.model.SopProductSkuPage;
import com.shangpin.openapi.api.sdk.model.SopProductSkuPageQuery;
import com.shangpin.openapi.api.sdk.model.SopSku;
import com.shangpin.openapi.api.sdk.model.StockInfo;



@Component("stockMontiService")
public class StockMontiService {
	private static Logger logger = Logger.getLogger("info");
	@Autowired
	UpdateStockService updateStockService;
	private static ResourceBundle bdl=null;
    private static String supplierId;
    private static String smtpHost = null;
	private static String from = null;
	private static String fromUserPassword = null;
	private static String to = null;
	private static String hours = null;
	private static String messageType = null;
	private static String host = null;

    static {
        if(null==bdl)
         bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        
        smtpHost = bdl.getString("smtpHost");
		from = bdl.getString("from");
		fromUserPassword = bdl.getString("fromUserPassword");
		to = bdl.getString("to");
		hours = bdl.getString("hours");
		messageType = bdl.getString("messageType");
		host = bdl.getString("HOST");
    }
    
	@Autowired
	SkuPriceService skuPriceService;
	@Autowired
	SupplierService supplierService;
	
	public void findSupplier(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		List<StockUpdateDTO> list = null;
		try {
			list = updateStockService.getAll();
			
			List<SupplierDTO> listSupp = supplierService.findByState("1");
			Map<String,String> map = new HashMap<String,String>();
			for(SupplierDTO supp:listSupp){
				map.put(supp.getAppKey(), supp.getAppSecret());	
			}
			for(StockUpdateDTO stockUpdateDTO:list){
				if(null !=stockUpdateDTO && null !=stockUpdateDTO.getUpdateTime()){
					if(stockUpdateDTO.getSupplierId().equals(supplierId)){
						continue;
					}
					if("1".equals(stockUpdateDTO.getStatus())){
						long diff = new Date().getTime()-stockUpdateDTO.getUpdateTime().getTime();
			    		long hour = diff / (1000 * 60 * 60);
			    		long maxHousr = Long.parseLong(hours);
			    		logger.info("供应商："+stockUpdateDTO.getSupplierId()+"未更新时间："+hour +"maxHousr:"+maxHousr);
			    		if(hour >= maxHousr){
			    			Thread t = new Thread(new Runnable() {
			    				@Override
			    				public void run() {
			    					try {
			    						SendMail.sendGroupMail(
			    								smtpHost,
			    								from,
			    								fromUserPassword,
			    								to,
			    								"【重要】库存更新异常",
			    								"供应商"+supplierId+"库存已超过"+hours
			    								+ "未更新,现已把库存全部更新为0",
			    								messageType);
			    					} catch (Exception e) {
			    						logger.error(e.getMessage());
			    					}
			    				}
			    			});
//			    			t.start();
			    			Map<String,String> stocks = new HashMap<String,String>();
			    			Collection<String> skuNo = grabProduct(stockUpdateDTO.getSupplierId(),map.get(stockUpdateDTO.getSupplierId()),"2015-01-01 00:00", format.format(new Date()), stocks);
			    			updateIceStock(stockUpdateDTO.getSupplierId(),map.get(stockUpdateDTO.getSupplierId()),skuNo,stocks);
			    		}
					}
				}
			}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 抓取主站商品SKU信息，等待更新库存<br/>
	 * 
	 * @param host
	 *            服务地址
	 * @param app_key
	 *            分配给供应商的KEY
	 * @param app_secret
	 *            分配给供应商的SECRET
	 * @param start
	 *            主站数据开始时间
	 * @param end
	 *            主站数据结束时间
	 * @param stocks
	 *            本地sku编号与ice的sku键值对
	 * @return 供应商skuNo
	 * @throws Exception
	 */
	private Collection<String> grabProduct(String app_key,
			String app_secret, String start, String end,
			Map<String, String> stocks) throws Exception {
		int pageIndex = 1, pageSize = 100;
		ApiResponse<SopProductSkuPage> result = null;
		SopProductSkuPageQuery request = null;
		boolean hasNext = true;
		logger.warn("获取sku 开始");

		Set<String> skuIds = new HashSet<String>();

		while (hasNext) {
			long startDate = System.currentTimeMillis();
			List<SopProductSku> skus = null;
			request = new SopProductSkuPageQuery();
			request.setPageIndex(pageIndex);
			request.setPageSize(pageSize);
			request.setStartTime(start);
			request.setEndTime(end);

			// 处理：通过openAPI 获取产品信息超时异常，如果异常次数超过5次就跳出
			for (int i = 0; i < 5; i++) {
				startDate = System.currentTimeMillis();
				Date timestamp = new Date(); //
				try {
					result = SpClient.FindCommodityByPage(host, app_key,
							app_secret, timestamp, request);
					SopProductSkuPage products = result.getResponse();
					skus = products.getSopProductSkuIces();
					logger.info("第" + (i + 1) + "次通过openAPI 获取第 "
							+ pageIndex + "页产品信息，信息耗时"
							+ (System.currentTimeMillis() - startDate));
					if (skus != null) {
						i = 5;
					}
				} catch (Exception e1) {
					e1.printStackTrace();
					logger.error("openAPI在异常中获取信息出错" + e1.getMessage(), e1);
				}

			}
			// 五次循环后 还有错误 不在处理 抛出异常
			if (null != skus) {
				for (SopProductSku sku : skus) {
					List<SopSku> skuIces = sku.getSopSkuIces();
					// loggerInfo.info("获取到的sku数量为:"+skuIces.size());
					for (SopSku ice : skuIces) {

						if (null != ice.getSkuNo()
								&& !"".equals(ice.getSkuNo())
								&& null != ice.getSupplierSkuNo()
								&& !"".equals(ice.getSupplierSkuNo())) {
							if (1 != ice.getIsDeleted()) {
								skuIds.add(ice.getSupplierSkuNo());
								stocks.put(ice.getSupplierSkuNo(),
										ice.getSkuNo());
							}
						}
					}
				}
			} else {
				logger.error("无法获取到产品信息");
				throw new Exception("无法获取到产品信息");
			}
			pageIndex++;
			hasNext = (pageSize == skus.size());

		}
		logger.warn("获取sku 结束");
		logger.info("获取SKU数量为：" + skuIds.size());
		return skuIds;
	}
	/**
	 * 更新ice的库存
	 * 
	 * @param host
	 *            服务地址
	 * @param app_key
	 *            提供给供应商的KEY
	 * @param app_secret
	 *            提供给供应商的SECRET
	 * @param iceStock
	 *            尚品skuNo与对应的库存关系
	 * @return 更新失败的数目
	 * @throws Exception
	 */
	private int updateIceStock(String app_key, String app_secret,Collection<String> skuNo,
			Map<String, String> iceStock) throws Exception {

		// 获取尚品库存
		int skuNum = 1;
		int failCount = 0;
		Map<String, Integer> toUpdateIce = new HashMap<>();
		
		for(String s : skuNo){
			toUpdateIce.put(iceStock.get(s), 0);
		}
		
		Iterator<Entry<String, Integer>> iter = toUpdateIce.entrySet()
				.iterator();

		logger.info("待更新的数据总和：--------" + toUpdateIce.size());
		ApiResponse<Boolean> result = null;
		StockInfo request_body = null;

		while (null != iter && iter.hasNext()) {

			Entry<String, Integer> entry = iter.next();
			request_body = new StockInfo();
			request_body.setSkuNo(entry.getKey());
			request_body.setInventoryQuantity(entry.getValue() < 0 ? 0 : entry
					.getValue());
			boolean success = true;
			for (int i = 0; i < 2; i++) {// 发生错误 允许再执行一次
				try {
					logger.info("待更新的数据：--------" + entry.getKey() + ":"
							+ entry.getValue());

					result = SpClient.UpdateStock(host, app_key, app_secret,
							new Date(), request_body);
					if (null != result && !result.getResponse()) {
						failCount++;
						success = false;
						logger.error(entry.getKey() + ":"
								+ entry.getValue() + "更新库存失败");
					}
				} catch (Exception e) {

					logger.error(
							"更新sku错误：" + entry.getKey() + ":"
									+ entry.getValue(), e);
					logger.error(
							"更新sku错误：" + entry.getKey() + ":"
									+ entry.getValue() + " " + e.getMessage(),
							e);
				}
				if (success) { // 成功直接跳出
					i = 2;
				}
			}

		}
		logger.info("更新库存结束");
		return failCount;
	}
}
