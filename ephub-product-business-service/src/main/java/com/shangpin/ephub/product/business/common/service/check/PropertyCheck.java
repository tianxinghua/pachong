package com.shangpin.ephub.product.business.common.service.check;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by lizhongren on 2017/3/3. 所有的验证汇总 组合模式
 */
@Component
public class PropertyCheck extends CommonCheckBase {

	private List<CommonCheckBase> allPropertyCheck;

	public PropertyCheck() {
	}

	public PropertyCheck(List<CommonCheckBase> allPropertyCheck) {
		this.allPropertyCheck = allPropertyCheck;
	}

	public List<CommonCheckBase> getAllPropertyCheck() {
		return allPropertyCheck;
	}

	public void setAllPropertyCheck(List<CommonCheckBase> allPropertyCheck) {
		this.allPropertyCheck = allPropertyCheck;
	}

	@Override
	protected boolean convertValue(HubSpuPendingDto hubSpuPendingIsExist,HubSpuPendingDto spuPendingDto) throws Exception {

		boolean flag = true;
		for (CommonCheckBase base : allPropertyCheck) {
			if (!base.convertValue(hubSpuPendingIsExist,spuPendingDto)) {
				flag = false;
			}
		}
		return flag;
	}

	@Override
	protected String checkValue(HubSpuPendingDto hubSpuPendingIsExist,HubSpuPendingDto spuPendingDto) throws Exception {
		StringBuffer stringBuffer = new StringBuffer();
		for (CommonCheckBase base : allPropertyCheck) {
			String result = base.checkValue(hubSpuPendingIsExist,spuPendingDto);
			if(result!=null){
				stringBuffer.append(result).append(",");				
			}
		}
		if(StringUtils.isBlank(stringBuffer.toString())){
			return null;
		}
		return stringBuffer.toString();
	}

	public String handleconvertOrCheck(HubSpuPendingDto hubSpuPendingIsExist, HubSpuPendingDto hubSpuPendingDto) {
		// TODO Auto-generated method stub
		return null;
	}
}
