package com.shangpin.ephub.product.business.common.service.check.property;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ephub.client.data.mysql.enumeration.InfoState;
import com.shangpin.ephub.client.data.mysql.enumeration.OriginState;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuSeasonState;
import com.shangpin.ephub.client.data.mysql.season.dto.HubSeasonDicDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.product.business.common.enumeration.SeasonType;
import com.shangpin.ephub.product.business.common.hubDic.season.HubSeasonDicService;
import com.shangpin.ephub.product.business.common.service.check.CommonCheckBase;

/**
 * Created by lizhongren on 2017/3/3. 单个具体的实现类
 */
@Component
public class SeasonCheck extends CommonCheckBase {
	
	static Map<String, String> seasonStaticMap = null;
	static Map<String, String> hubSeasonStaticMap = null;
	@Autowired
	HubSeasonDicService hubSeasonDicService;

	@Override
	protected String checkValue(HubSpuPendingDto hubSpuPendingIsExist,HubSpuPendingDto spuPendingDto) {

		if(hubSpuPendingIsExist.getSpuSeasonState()==SpuSeasonState.HANDLED.getIndex()){
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
	protected boolean convertValue(HubSpuPendingDto spuPendingDto) throws Exception {
		return setSeasonMapping(spuPendingDto);
	}

	/**
	 * 校验季节
	 * 
	 * @param season
	 *            例如：2016_春夏
	 * @return
	 */
	public boolean checkHubSeason(String supplierId,String season) {

		
		if (season!=null&&season.split("_").length < 2) {
			return false;
		}
		
		if(hubSeasonStaticMap==null){
			getSeasonMap(supplierId);
		}
		
		if(hubSeasonStaticMap!=null&&hubSeasonStaticMap.containsKey(season)){
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

				seasonStaticMap.put(dicDto.getSupplierid() + "_" + dicDto.getSupplierSeason().trim(),
						dicDto.getHubMarketTime().trim() + "_" + dicDto.getHubSeason().trim() + "|"
								+ dicDto.getMemo().trim());
				hubSeasonStaticMap.put(dicDto.getHubMarketTime() + "_" + dicDto.getHubSeason(), "");
			}
		}
	}
	 protected boolean setSeasonMapping(HubSpuPendingDto hubSpuPending) throws Exception {
	        Map<String, String> seasonMap = this.getSeasonMap(hubSpuPending.getSupplierId());
	        boolean result = true;
	        String spSeason = "", seasonSign = "";
	        if (StringUtils.isNotBlank(hubSpuPending.getHubSeason())) {
	            if (seasonMap.containsKey(hubSpuPending.getSupplierId() + "_" + hubSpuPending.getHubSeason().trim())) {
	                // 包含时转化赋值
	                spSeason = seasonMap.get(hubSpuPending.getSupplierId() + "_" + hubSpuPending.getHubSeason().trim());
	                if (StringUtils.isNotBlank(spSeason)) {
	                    if (spSeason.indexOf("|") > 0) {
	                        seasonSign = spSeason.substring(spSeason.indexOf("|") + 1, spSeason.length());
	                        hubSpuPending.setHubSeason(spSeason.substring(0, spSeason.indexOf("|")));
	                        if (SeasonType.SEASON_CURRENT.getIndex().toString().equals(seasonSign)) {
	                            hubSpuPending.setIsCurrentSeason(SeasonType.SEASON_CURRENT.getIndex().byteValue());
	                        } else {
	                            hubSpuPending.setIsCurrentSeason(SeasonType.SEASON_NOT_CURRENT.getIndex().byteValue());
	                        }
	                    }
	                    hubSpuPending.setSpuSeasonState(InfoState.PERFECT.getIndex());
	                } else {
	                    result = false;
	                    hubSpuPending.setSpuSeasonState(InfoState.IMPERFECT.getIndex());
	                }

	            } else {
	                result = false;
	                hubSpuPending.setSpuSeasonState(InfoState.IMPERFECT.getIndex());
	                hubSeasonDicService.saveSeason(hubSpuPending.getSupplierId(), hubSpuPending.getHubSeason());
	            }

	        } else {//
	            result = false;
	            hubSpuPending.setSpuSeasonState(InfoState.IMPERFECT.getIndex());
	        }
	        return result;
	    }
}
