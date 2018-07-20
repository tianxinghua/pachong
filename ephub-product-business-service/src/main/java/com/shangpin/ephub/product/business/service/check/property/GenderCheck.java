package com.shangpin.ephub.product.business.service.check.property;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ephub.client.data.mysql.enumeration.InfoState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuGenderState;
import com.shangpin.ephub.client.data.mysql.gender.dto.HubGenderDicDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.product.business.common.hubDic.gender.service.HubGenderDicService;
import com.shangpin.ephub.product.business.service.check.CommonCheckBase;

/**
 * Created by lizhongren on 2017/3/3. 单个具体的实现类
 */
@Component
public class GenderCheck extends CommonCheckBase {

	static Map<String, String> genderStaticMap = null;
	static Map<String, String> hubGenderStaticMap = null;
	@Autowired
	HubGenderDicService hubGenderDicService;

	@Override
	protected String checkValue(HubSpuPendingDto hubSpuPendingIsExist,HubSpuPendingDto spuPendingDto) throws Exception {

		if(hubSpuPendingIsExist!=null&&hubSpuPendingIsExist.getSpuGenderState()!=null&&hubSpuPendingIsExist.getSpuGenderState()==SpuGenderState.HANDLED.getIndex()){
    		return null;
    	}
		
		hubSpuPendingIsExist.setHubGender(spuPendingDto.getHubGender());
		if (checkHubGender(spuPendingDto.getHubGender())) {
			hubSpuPendingIsExist.setSpuGenderState(SpuGenderState.HANDLED.getIndex());
		} else {
			hubSpuPendingIsExist.setSpuGenderState(SpuGenderState.UNHANDLED.getIndex());
			return "性别校验失败";
		}
		return null;
	}

	@Override
	protected boolean convertValue(HubSpuPendingDto hubSpuPendingIsExist,HubSpuPendingDto spuPendingDto) throws Exception {
		if(hubSpuPendingIsExist!=null&&hubSpuPendingIsExist.getSpuGenderState()!=null&&hubSpuPendingIsExist.getSpuGenderState()==SpuGenderState.HANDLED.getIndex()){
    		return true;
    	}
		return setGenderMapping(hubSpuPendingIsExist,spuPendingDto);
	}

	/**
	 * 校验性别
	 * 
	 * @param gender
	 * @return
	 */
	public boolean checkHubGender(String gender) {

		if (hubGenderStaticMap == null) {
			getGenderMap(null);
		}
		if (hubGenderStaticMap != null && hubGenderStaticMap.containsKey(gender)) {
			return true;
		} else {
			return false;
		}
	}

	protected boolean setGenderMapping(HubSpuPendingDto hubSpuPendingIsExist,HubSpuPendingDto hubSpuPending) throws Exception {
		boolean result = true;
		// 获取性别
		Map<String, String> genderMap = this.getGenderMap(null);

		if (null != hubSpuPending.getHubGender()
				&& genderMap.containsKey(hubSpuPending.getHubGender().trim().toUpperCase())) {
			// 包含时转化赋值
			hubSpuPendingIsExist.setHubGender(genderMap.get(hubSpuPending.getHubGender().toUpperCase()));
			hubSpuPendingIsExist.setSpuGenderState(InfoState.PERFECT.getIndex());
		} else {
			result = false;
			hubSpuPendingIsExist.setSpuGenderState(InfoState.IMPERFECT.getIndex());
			hubSpuPendingIsExist.setHubGender(hubSpuPending.getHubGender());
			hubGenderDicService.saveHubGender(null, hubSpuPending.getHubGender());
		}

		return result;
	}

	/**
	 * key : supplierId_supplierGender
	 *
	 * @param supplierId
	 * @return
	 */
	protected Map<String, String> getGenderMap(String supplierId) {
		if (null == genderStaticMap) {
			genderStaticMap = new HashMap<>();
			hubGenderStaticMap = new HashMap<>();
			setGenderValueToMap(supplierId);
		} else {
			if (isNeedHandle()) {
				setGenderValueToMap(supplierId);
			}
		}
		return genderStaticMap;
	}

	protected void setGenderValueToMap(String supplierId) {
		List<HubGenderDicDto> hubGenderDics = hubGenderDicService.getHubGenderDicBySupplierId(supplierId);
		if (null != hubGenderDics && hubGenderDics.size() > 0) {
			for (HubGenderDicDto dto : hubGenderDics) {
				genderStaticMap.put(dto.getSupplierGender().trim().toUpperCase(), dto.getHubGender().trim());
				hubGenderStaticMap.put(dto.getHubGender(), "");
			}
			// shangpinRedis.hset
		}
	}

}
