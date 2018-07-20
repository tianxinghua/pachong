package com.shangpin.ephub.product.business.service.check.property;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ephub.client.data.mysql.enumeration.InfoState;
import com.shangpin.ephub.client.data.mysql.enumeration.OriginState;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.product.business.common.enumeration.SupplierValueMappingType;
import com.shangpin.ephub.product.business.common.hubDic.origin.service.HubOriginDicService;
import com.shangpin.ephub.product.business.service.check.CommonCheckBase;

/**
 * Created by lizhongren on 2017/3/3.
 * 单个具体的实现类
 */
@Component
public class OriginCheck extends CommonCheckBase {

    static Map<String, String> originStaticMap = null;
    static Map<String, String> hubOriginStaticMap = null;
	@Autowired
	HubOriginDicService hubOriginDicService;

    @Override
    protected String checkValue(HubSpuPendingDto hubSpuPendingIsExist,HubSpuPendingDto spuPendingDto) {
    	
    	if(hubSpuPendingIsExist!=null&&hubSpuPendingIsExist.getOriginState()!=null&&hubSpuPendingIsExist.getOriginState()==OriginState.HANDLED.getIndex()){
    		return null;
    	}
    	
    	hubSpuPendingIsExist.setHubOrigin(spuPendingDto.getHubOrigin());
    	if(checkHubOrigin(spuPendingDto.getHubOrigin())){
    		hubSpuPendingIsExist.setOriginState(OriginState.HANDLED.getIndex());
		}else{
			hubSpuPendingIsExist.setOriginState(OriginState.UNHANDLED.getIndex());
    		return "产地校验失败";			
		}
        return null;
    }
	@Override
	protected boolean convertValue(HubSpuPendingDto hubSpuPendingIsExist,HubSpuPendingDto spuPendingDto) throws Exception {
		if(hubSpuPendingIsExist!=null&&hubSpuPendingIsExist.getOriginState()!=null&&hubSpuPendingIsExist.getOriginState()==OriginState.HANDLED.getIndex()){
    		return true;
    	}
		return setOriginMapping(hubSpuPendingIsExist,spuPendingDto);
	}

    /**
     * 校验产地
     * @param hubOrigin
     * @return
     */
	public boolean checkHubOrigin(String hubOrigin) {
		
		if(hubOriginStaticMap==null){
			getOriginMap();
		}
		if(hubOriginStaticMap!=null&&hubOriginStaticMap.containsKey(hubOrigin)){
			return true;
		}
		return false;
	}
	
	  protected Map<String, String> getOriginMap() {
	        if (null == originStaticMap) {
	            originStaticMap = new HashMap<>();
	            hubOriginStaticMap = new HashMap<>();

	            setOriginStaticMap();

	        } else {
	            if (isNeedHandle()) {
	                setOriginStaticMap();
	            }
	        }
	        return originStaticMap;
	    }
	
	  protected boolean setOriginMapping(HubSpuPendingDto hubSpuPendingIsExist,HubSpuPendingDto hubSpuPending) throws Exception {
	        Map<String, String> originMap = this.getOriginMap();
	        if (StringUtils.isNotBlank(hubSpuPending.getHubOrigin())) {

	            if (originMap.containsKey(hubSpuPending.getHubOrigin().toUpperCase().trim())) {
	            	hubSpuPendingIsExist.setHubOrigin(originMap.get(hubSpuPending.getHubOrigin().toUpperCase().trim()));
	                hubSpuPendingIsExist.setOriginState(InfoState.PERFECT.getIndex());
	                return true;
	            } else {
	            	hubSpuPendingIsExist.setHubOrigin(hubSpuPending.getHubOrigin().trim());
	            	hubSpuPendingIsExist.setOriginState(InfoState.IMPERFECT.getIndex());
	                return false;
	            }
	        } else {
	        	hubSpuPendingIsExist.setOriginState(InfoState.IMPERFECT.getIndex());
	            return false;
	        }
	    }
	  private void setOriginStaticMap() {
	        List<HubSupplierValueMappingDto> valueMappingDtos = hubOriginDicService
	                .getHubSupplierValueMappingByType(SupplierValueMappingType.TYPE_ORIGIN.getIndex());
	        for (HubSupplierValueMappingDto dto : valueMappingDtos) {
	            originStaticMap.put(dto.getSupplierVal().toUpperCase().trim(), dto.getHubVal());
	            hubOriginStaticMap.put(dto.getHubVal(), dto.getSupplierVal());
	        }
	    }

	
}
