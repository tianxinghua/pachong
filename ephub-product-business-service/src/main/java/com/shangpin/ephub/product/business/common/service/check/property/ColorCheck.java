package com.shangpin.ephub.product.business.common.service.check.property;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ephub.client.data.mysql.enumeration.InfoState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuColorState;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.product.business.common.hubDic.color.dto.ColorDTO;
import com.shangpin.ephub.product.business.common.hubDic.color.service.HubColorDicService;
import com.shangpin.ephub.product.business.common.service.check.CommonCheckBase;
import com.shangpin.ephub.product.business.common.service.check.HubCheckService;
/**
 * Created by lizhongren on 2017/3/3.
 * 单个具体的实现类
 */
@Component
public class ColorCheck extends CommonCheckBase {

    static Map<String, String> colorStaticMap = null;
    static Map<String, String> hubColorStaticMap = null;
	@Autowired
	HubColorDicService hubColorDicService;
	@Autowired
	HubCheckService hubCheckService;

	@Override
	protected String checkValue(HubSpuPendingDto hubSpuPendingIsExist,HubSpuPendingDto spuPendingDto) throws Exception {
		if(hubSpuPendingIsExist!=null&&hubSpuPendingIsExist.getSpuColorState()!=null&&hubSpuPendingIsExist.getSpuColorState()==SpuColorState.HANDLED.getIndex()){
    		return null;
    	}
		hubSpuPendingIsExist.setHubColor(spuPendingDto.getHubColor());
		if(checkHubColor(spuPendingDto.getHubColor())){
			hubSpuPendingIsExist.setSpuColorState(SpuColorState.HANDLED.getIndex());
		}else{
			hubSpuPendingIsExist.setSpuColorState(SpuColorState.UNHANDLED.getIndex());
    		return "颜色校验失败";			
		}
		return null;
	}

	@Override
	protected boolean convertValue(HubSpuPendingDto hubSpuPendingIsExist,HubSpuPendingDto spuPendingDto) throws Exception{
		if(hubSpuPendingIsExist!=null&&hubSpuPendingIsExist.getSpuColorState()!=null&&hubSpuPendingIsExist.getSpuColorState()==SpuColorState.HANDLED.getIndex()){
    		return true;
    	}
		return setColorMapping(hubSpuPendingIsExist,spuPendingDto);
	}
    
    /**
	 * 校验颜色
	 * @param color
	 * @return
	 */
	public boolean checkHubColor(String color){
		
		if(hubCheckService.checkHubColor(color)){
			return true;
		}else{
			return false;
		}
	}
	 protected Map<String, String> getColorMap() {
	        if (null == colorStaticMap) {
	            colorStaticMap = new HashMap<>();
	            hubColorStaticMap = new HashMap<>();
	            List<ColorDTO> colorDTOS = hubColorDicService.getColorDTO();
	            for (ColorDTO dto : colorDTOS) {
	                colorStaticMap.put(dto.getSupplierColor().toUpperCase(), dto.getHubColorName());
	                hubColorStaticMap.put(dto.getHubColorName(), "");
	            }

	        } else {
	            if (isNeedHandle()) {
	                for (ColorDTO dto : hubColorDicService.getColorDTO()) {
	                    colorStaticMap.put(dto.getSupplierColor().toUpperCase(), dto.getHubColorName());
	                    hubColorStaticMap.put(dto.getHubColorName(), "");
	                }

	                // 无用的内容 暂时不考虑

	            }
	        }
	        return colorStaticMap;
	    }


	 protected boolean setColorMapping(HubSpuPendingDto hubSpuPendingIsExist,HubSpuPendingDto hubSpuPending) throws Exception {
	        boolean result = true;
	        Map<String, String> colorMap = this.getColorMap();
	        if (colorMap.containsKey(hubSpuPending.getHubColor().toUpperCase()) & !StringUtils.isEmpty(colorMap.get(hubSpuPending.getHubColor().toUpperCase()))) {
	            // 包含时转化赋值
	        	hubSpuPendingIsExist.setHubColor(colorMap.get(hubSpuPending.getHubColor().toUpperCase()));
	            hubSpuPendingIsExist.setSpuColorState(InfoState.PERFECT.getIndex());

	        } else {
	            result = false;
	            hubSpuPendingIsExist.setSpuColorState(InfoState.IMPERFECT.getIndex());
	            hubSpuPendingIsExist.setHubColor(hubSpuPending.getHubColor());
	            hubColorDicService.saveColorItem(hubSpuPending.getHubColor());

	        }
	        return result;
	    }
}
