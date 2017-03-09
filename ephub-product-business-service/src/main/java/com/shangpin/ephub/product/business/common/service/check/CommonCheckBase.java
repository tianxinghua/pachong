package com.shangpin.ephub.product.business.common.service.check;

import org.apache.commons.lang.StringUtils;

import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.product.business.common.util.DateTimeUtil;

/**
 * Created by lizhongren on 2017/3/3. 抽象类 总控的入口
 */
public abstract class CommonCheckBase {

	static Integer isCurrentMin = DateTimeUtil.getCurrentMin();

	private boolean isNeedConvert(HubSpuPendingDto hubSpuPendingIsExist,HubSpuPendingDto spuPendingDto) throws Exception {
		
		if(hubSpuPendingIsExist!=null&&StringUtils.isNotBlank(hubSpuPendingIsExist.getUpdateUser())){
			return false;
		}
		if (spuPendingDto!=null&&StringUtils.isNotBlank(spuPendingDto.getUpdateUser())) {
			return false;
		} 
		return true;
	}

	/**
	 * pending数据校验 总入口
	 * @param hubSpuPendingIsExist
	 * @param spuPendingDto
	 * @return
	 * @throws Exception
	 */
	public String handleconvertOrCheck(HubSpuPendingDto hubSpuPendingIsExist,HubSpuPendingDto spuPendingDto) throws Exception {

		if (isNeedConvert(hubSpuPendingIsExist,spuPendingDto)) {
			 convertValue(hubSpuPendingIsExist,spuPendingDto);
			 return null;
		}else{
			 return checkValue(hubSpuPendingIsExist,spuPendingDto);
		}
	}

	/**
	 * 在指定时间段 重新获取所有数据
	 *
	 * @return
	 */
	public boolean isNeedHandle() {
		int min = DateTimeUtil.getCurrentMin();
		if (min - isCurrentMin >= 5 || min - isCurrentMin < 0) {
			isCurrentMin = min;
			return true;
		} else {
			return false;
		}
	}
	
	/**
     * 具体的映射
     * @param spuPendingDto
     * @return 映射成功返回true，反之false
     */
    protected abstract boolean convertValue(HubSpuPendingDto hubSpuPendingIsExist,HubSpuPendingDto spuPendingDto)  throws Exception;

	/**
     * 具体的校验
     * @param spuPendingDto
     * @return 若失败返回失败原因 ,反之返回null
     */
    protected abstract String checkValue(HubSpuPendingDto hubSpuPendingIsExist,HubSpuPendingDto spuPendingDto)  throws Exception;

}
