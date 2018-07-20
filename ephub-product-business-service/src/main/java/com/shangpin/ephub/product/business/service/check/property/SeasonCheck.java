package com.shangpin.ephub.product.business.service.check.property;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ephub.client.data.mysql.enumeration.InfoState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuSeasonState;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.product.business.common.enumeration.SeasonType;
import com.shangpin.ephub.product.business.common.hubDic.season.HubSeasonDicService;
import com.shangpin.ephub.product.business.service.check.CommonCheckBase;
import com.shangpin.ephub.product.business.service.check.HubCheckService;

/**
 * Created by lizhongren on 2017/3/3. 单个具体的实现类
 */
@Component
public class SeasonCheck extends CommonCheckBase {
	
	static Map<String, String> seasonStaticMap = null;
	static Map<String, String> hubSeasonStaticMap = null;
	@Autowired
	HubSeasonDicService hubSeasonDicService;
	@Autowired
	HubCheckService hubCheckService;
	@Override
	protected String checkValue(HubSpuPendingDto hubSpuPendingIsExist,HubSpuPendingDto spuPendingDto) {

		if(hubSpuPendingIsExist!=null&&hubSpuPendingIsExist.getSpuSeasonState()!=null&&hubSpuPendingIsExist.getSpuSeasonState()==SpuSeasonState.HANDLED.getIndex()){
    		return null;
    	}
		
		hubSpuPendingIsExist.setHubSeason(spuPendingDto.getHubSeason());
		if (checkHubSeason(spuPendingDto.getSupplierId(),spuPendingDto.getHubSeason())) {
			hubSpuPendingIsExist.setSpuSeasonState(SpuSeasonState.HANDLED.getIndex());
		} else {
			hubSpuPendingIsExist.setSpuSeasonState(SpuSeasonState.UNHANDLED.getIndex());
			return "季节校验失败";			
		}
		return null;
	}

	@Override
	protected boolean convertValue(HubSpuPendingDto hubSpuPendingIsExist,HubSpuPendingDto spuPendingDto) throws Exception {
		
		if(hubSpuPendingIsExist!=null&&hubSpuPendingIsExist.getSpuSeasonState()!=null&&hubSpuPendingIsExist.getSpuSeasonState()==SpuSeasonState.HANDLED.getIndex()){
    		return true;
    	}
		return setSeasonMapping(hubSpuPendingIsExist,spuPendingDto);
	}

	/**
	 * 校验季节
	 * 
	 * @param season
	 *            例如：2016_春夏
	 * @return
	 */
	public boolean checkHubSeason(String supplierId,String season) {
		if(hubCheckService.checkHubSeason(season)){
			return true;
		} else {
			return false;
		}
	}

	/**
	 * key :supplierId+"_"+supplierSeason value:hub_year+"_"+hub_season+"_"+memo
	 * meme = 1: current season 0: preview season
	 *
	 * @param supplierId
	 * @return
	 */
	protected Map<String, String> getSeasonMap(String supplierId) {
		if (null == seasonStaticMap) {
			seasonStaticMap = new HashMap<>();
			hubSeasonStaticMap = new HashMap<>();
			setSeasonStaticMap();
		} else {
			if (isNeedHandle()) {
				setSeasonStaticMap();
			}
		}
		return seasonStaticMap;
	}

	private void setSeasonStaticMap() {
		List<HubSeasonDicDto> hubSeasonDics = hubSeasonDicService.getHubSeasonDic();
		for (HubSeasonDicDto dicDto : hubSeasonDics) {
			if (StringUtils.isNotBlank(dicDto.getHubMarketTime()) && StringUtils.isNotBlank(dicDto.getHubSeason())
					&& StringUtils.isNotBlank(dicDto.getMemo())) {
				if(dicDto.getSupplierSeason()!=null){
					seasonStaticMap.put(dicDto.getSupplierid() + "_" + dicDto.getSupplierSeason().trim(),
							dicDto.getHubMarketTime().trim() + "_" + dicDto.getHubSeason().trim() + "|"
									+ dicDto.getMemo().trim());
				}
				hubSeasonStaticMap.put(dicDto.getHubMarketTime() + "_" + dicDto.getHubSeason(), "");
			}
		}
	}
	 protected boolean setSeasonMapping(HubSpuPendingDto hubSpuPendingIsExist,HubSpuPendingDto hubSpuPending) throws Exception {
	        Map<String, String> seasonMap = this.getSeasonMap(hubSpuPending.getSupplierId());
	        boolean result = true;
	        String spSeason = "", seasonSign = "";
	        if (StringUtils.isNotBlank(hubSpuPending.getHubSeason())) {
	        	hubSpuPendingIsExist.setHubSeason(hubSpuPending.getHubSeason());
	            if (seasonMap.containsKey(hubSpuPending.getSupplierId() + "_" + hubSpuPending.getHubSeason().trim())) {
	                // 包含时转化赋值
	                spSeason = seasonMap.get(hubSpuPending.getSupplierId() + "_" + hubSpuPending.getHubSeason().trim());
	                if (StringUtils.isNotBlank(spSeason)) {
	                    if (spSeason.indexOf("|") > 0) {
	                        seasonSign = spSeason.substring(spSeason.indexOf("|") + 1, spSeason.length());
	                        hubSpuPendingIsExist.setHubSeason(spSeason.substring(0, spSeason.indexOf("|")));
	                        if (SeasonType.SEASON_CURRENT.getIndex().toString().equals(seasonSign)) {
	                        	hubSpuPendingIsExist.setIsCurrentSeason(SeasonType.SEASON_CURRENT.getIndex().byteValue());
	                        } else {
	                        	hubSpuPendingIsExist.setIsCurrentSeason(SeasonType.SEASON_NOT_CURRENT.getIndex().byteValue());
	                        }
	                    }
	                    hubSpuPendingIsExist.setSpuSeasonState(InfoState.PERFECT.getIndex());
	                } else {
	                    result = false;
	                    hubSpuPendingIsExist.setSpuSeasonState(InfoState.IMPERFECT.getIndex());
	                }

	            } else {
	                result = false;
	                hubSpuPendingIsExist.setSpuSeasonState(InfoState.IMPERFECT.getIndex());
	                hubSeasonDicService.saveSeason(hubSpuPending.getSupplierId(), hubSpuPending.getHubSeason());
	            }

	        } else {//
	            result = false;
	            hubSpuPendingIsExist.setSpuSeasonState(InfoState.IMPERFECT.getIndex());
	        }
	        return result;
	    }
}
