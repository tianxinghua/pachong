package com.shangpin.iog.price.service;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsPriceService;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.dto.PriceCountDetailDTO;
import com.shangpin.iog.service.PriceCountDetailService;
import com.shangpin.iog.service.SkuPriceService;
@Component("updatePriceService")
public class UpdatePriceService extends AbsPriceService{

	
	@Autowired
	PriceCountDetailService priceCountDetailService;
	@Autowired
	SkuPriceService skuPriceService;
	
	Map<String, Map<String, String>> newSkuPrice = null;
	private String supplierId;
	
	private static ApplicationContext factory;
	private static void loadSpringContext()

	{
        factory = new AnnotationConfigApplicationContext(AppContext.class);
	}
	/**
	 * @param skuNo 线上需要更新的sku
	 * @return Map  最新Map<skuno,marketPrice|supplierPrice>
	 */
	@Override
	public Map<String, String> getPriceOfSupplier(Collection<String> skuNo)
			throws ServiceException, Exception {
		Map<String, String> returnMap = new HashMap<String,String>();
	
			Map<String, String> skuIdPrice = newSkuPrice.get(supplierId);
			//放到map中
			for(String skuId : skuNo){
				if (skuIdPrice.get(skuId)!=null) {
					returnMap.put(skuId, skuIdPrice.get(skuId));
				}
			}
		
		return returnMap;
	}
	
	public void  kkk() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		//可用的供应商
		try {
			List<PriceCountDetailDTO> list = priceCountDetailService.findAllByFlag("1");
			
			
			for (PriceCountDetailDTO priceCountDetailDTO : list) {
				//得到数据库中的最新价格
				newSkuPrice = skuPriceService.getNewSkuPrice(priceCountDetailDTO.getSupplierId());
				supplierId = priceCountDetailDTO.getSupplierId();
				int failCount = this.updatePirce(supplierId, sdf.format(new Date(100000)), sdf.format(new Date()));
				System.err.println(failCount);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public static void main(String[] args) {
		loadSpringContext();
		UpdatePriceService updatePriceService =(UpdatePriceService)factory.getBean("updatePriceService");
		System.out.println("开始");
		updatePriceService.kkk();
		System.out.println("结束");
		
	}

}
