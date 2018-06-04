package com.shangpin.ephub.client.product.business.mapp.gateway;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangpin.ephub.client.product.business.mapp.dto.HubSupplierBrandDicRequestDto;
import com.shangpin.ephub.client.product.business.mapp.dto.HubSupplierCategoryDicRequestDto;
import com.shangpin.ephub.client.product.business.mapp.dto.HubSupplierColorDicRequestDto;
import com.shangpin.ephub.client.product.business.mapp.dto.HubSupplierSeasonDicRequestDto;
import com.shangpin.ephub.client.product.business.mapp.dto.HubSupplierSizeDicRequestDto;

/**
 * <p>Title:DicRefreshGateWay.java </p>
 * <p>Description: HUB数据入库前校验rest服务接口</p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月23日 下午3:52:56
 */
@FeignClient("ephub-product-business-service")
public interface DicRefreshGateWay {
	
	/**
	 * 品类字典刷新
	 * @param dto 数据传输对象
	 * @return 
	 */
	@RequestMapping(value = "/hub-supplier-category-dic/refresh", method = RequestMethod.POST,consumes = "application/json")
	public void categoryRefresh(@RequestBody HubSupplierCategoryDicRequestDto dto);
	
	/**
	 * 品牌字典刷新
	 * @param dto 数据传输对象
	 * @return 
	 */
	@RequestMapping(value = "/hub-supplier-brand-dic/updateAndRefresh", method = RequestMethod.POST,consumes = "application/json")
	public void brandRefresh(@RequestBody HubSupplierBrandDicRequestDto dto);
	
	/**
	 * 颜色字典刷新
	 * @param dto 数据传输对象
	 * @return 
	 */
	@RequestMapping(value = "/hub-supplier-color-dic/updateAndRefresh", method = RequestMethod.POST,consumes = "application/json")
	public void colorRefresh(@RequestBody HubSupplierColorDicRequestDto dto);
	
	/**
	 * 产地字典刷新
	 * @param dto 数据传输对象
	 * @return 
	 */
	@RequestMapping(value = "/hub-supplier-origin-dic/updateAndRefresh", method = RequestMethod.POST,consumes = "application/json")
	public void originRefresh(@RequestBody HubSupplierSizeDicRequestDto dto);
	
	
	/**
	 * 尺码字典刷新
	 * @param dto 数据传输对象
	 * @return 
	 */
	@RequestMapping(value = "/hub-supplier-size-dic/refresh", method = RequestMethod.POST,consumes = "application/json")
	public void sizeRefresh(@RequestBody HubSupplierSizeDicRequestDto dto);
	
	/**
	 * 季节字典刷新
	 * @param dto 数据传输对象
	 * @return 
	 */
	@RequestMapping(value = "/hub-supplier-season-dic/updateAndRefresh", method = RequestMethod.POST,consumes = "application/json")
	public void seasonRefresh(@RequestBody HubSupplierSeasonDicRequestDto dto);
	
}
