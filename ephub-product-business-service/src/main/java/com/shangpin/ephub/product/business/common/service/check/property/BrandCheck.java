package com.shangpin.ephub.product.business.common.service.check.property;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ephub.client.data.mysql.brand.dto.HubBrandDicDto;
import com.shangpin.ephub.client.data.mysql.brand.gateway.HubBrandDicGateway;
import com.shangpin.ephub.client.data.mysql.enumeration.InfoState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuBrandState;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.product.business.common.hubDic.brand.HubBrandDicService;
import com.shangpin.ephub.product.business.common.service.check.CommonCheckBase;

/**
 * Created by lizhongren on 2017/3/3. 单个具体的实现类
 */
@Component
public class BrandCheck extends CommonCheckBase {

	static Map<String, String> brandStaticMap = null;
    static Map<String, String> hubBrandStaticMap = null;
	@Autowired
	HubBrandDicGateway hubBrandDicGateway;
	@Autowired
	HubBrandDicService hubBrandDicService;

	@Override
	protected String checkValue(HubSpuPendingDto hubSpuPendingIsExist,HubSpuPendingDto spuPendingDto) throws Exception {
		
		if(hubSpuPendingIsExist!=null&&hubSpuPendingIsExist.getSpuBrandState()!=null&&hubSpuPendingIsExist.getSpuBrandState()==SpuBrandState.HANDLED.getIndex()){
			return null;
		}
		hubSpuPendingIsExist.setHubBrandNo(spuPendingDto.getHubBrandNo());
		if (checkHubBrand(spuPendingDto.getHubBrandNo())) {
			hubSpuPendingIsExist.setSpuBrandState(SpuBrandState.HANDLED.getIndex());
		} else {
			hubSpuPendingIsExist.setSpuBrandState(SpuBrandState.UNHANDLED.getIndex());
			return "品牌校验失败";
		}
		return null;
	}

	@Override
	protected boolean convertValue(HubSpuPendingDto hubSpuPendingIsExist,HubSpuPendingDto spuPendingDto) throws Exception{
		if(hubSpuPendingIsExist!=null&&hubSpuPendingIsExist.getSpuBrandState()!=null&&hubSpuPendingIsExist.getSpuBrandState()==SpuBrandState.HANDLED.getIndex()){
			return true;
		}
		return setBrandMapping(hubSpuPendingIsExist,spuPendingDto);
	}

	/**
	 * 校验品牌编号
	 * 
	 * @param brandNo
	 * @return
	 */
	public boolean checkHubBrand(String brandNo) throws Exception{
		
		if(hubBrandStaticMap == null){
			getBrandMap();
		}
		
		if(hubBrandStaticMap!=null&&hubBrandStaticMap.containsKey(brandNo)){
			return true;
		}else {
			return false;
		}
	}

	protected boolean setBrandMapping(HubSpuPendingDto hubSpuPendingIsExist,HubSpuPendingDto hubSpuPending) throws Exception {
		boolean result = true;
		Map<String, String> brandMap = this.getBrandMap();
		
		if (StringUtils.isNotBlank(hubSpuPending.getHubBrandNo())) {
			if (brandMap.containsKey(hubSpuPending.getHubBrandNo().trim().toUpperCase())) {
				// 包含时转化赋值
				hubSpuPendingIsExist.setSpuBrandState(InfoState.PERFECT.getIndex());
				hubSpuPendingIsExist.setHubBrandNo(brandMap.get(hubSpuPending.getHubBrandNo().trim().toUpperCase()));
			} else {
				result = false;
				hubSpuPendingIsExist.setSpuBrandState(InfoState.IMPERFECT.getIndex());
				hubSpuPendingIsExist.setHubBrandNo(hubSpuPending.getHubBrandNo());
				hubBrandDicService.saveSupplierBrand(hubSpuPending.getSupplierId(), hubSpuPending.getHubBrandNo().trim());
			}
		} else {
			result = false;
			hubSpuPendingIsExist.setHubBrandNo(hubSpuPending.getHubBrandNo());
			hubSpuPendingIsExist.setSpuBrandState(InfoState.IMPERFECT.getIndex());
		}
		return result;
	}
	
	/**
	 * key 供货商品牌名称 value 尚品的品牌编号
	 *
	 * @return
	 */
	protected Map<String, String> getBrandMap() throws Exception {

		if (null == brandStaticMap) {
			brandStaticMap = new HashMap<>();
			hubBrandStaticMap = new HashMap<>();
			List<HubBrandDicDto> brandDicDtos = hubBrandDicService.getBrand();
			for (HubBrandDicDto hubBrandDicDto : brandDicDtos) {
				brandStaticMap.put(hubBrandDicDto.getSupplierBrand().trim().toUpperCase(),
						hubBrandDicDto.getHubBrandNo());
				hubBrandStaticMap.put(hubBrandDicDto.getHubBrandNo().trim(), "");
			};

		} else {
			if (isNeedHandle()) {
				List<HubBrandDicDto> brandDicDtos = hubBrandDicService.getBrand();
				for (HubBrandDicDto hubBrandDicDto : brandDicDtos) {
					brandStaticMap.put(hubBrandDicDto.getSupplierBrand().trim().toUpperCase(),
							hubBrandDicDto.getHubBrandNo());
					hubBrandStaticMap.put(hubBrandDicDto.getHubBrandNo().trim(), "");
				};
			}
		}
		return brandStaticMap;
	}

}
