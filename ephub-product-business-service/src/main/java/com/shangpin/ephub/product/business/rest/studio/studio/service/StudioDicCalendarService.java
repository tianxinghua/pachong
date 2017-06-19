package com.shangpin.ephub.product.business.rest.studio.studio.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.shangpin.ephub.client.data.studio.dic.dto.StudioDicCalendarCriteriaDto;
import com.shangpin.ephub.client.data.studio.dic.dto.StudioDicCalendarDto;
import com.shangpin.ephub.client.data.studio.dic.gateway.StudioDicCalendarGateWay;
import com.shangpin.ephub.client.product.business.gms.result.HubResponseDto;
import com.shangpin.ephub.product.business.conf.rpc.ApiAddressProperties;
import com.shangpin.ephub.product.business.rest.studio.studio.dto.ResultObjList;
import com.shangpin.ephub.product.business.rest.studio.studio.dto.ResultResponseDto;
import com.shangpin.ephub.product.business.rest.studio.studio.dto.StudioRequestDto;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by wangchao on 2017/06/19.
 */
@Service
@Slf4j
public class StudioDicCalendarService {
	@Autowired
	private StudioDicCalendarGateWay studioDicCalendarGateWay;
	@Autowired
    private ApiAddressProperties apiAddressProperties;
	@Autowired
    private RestTemplate restTemplate;
	public List<StudioDicCalendarDto> getStudioDicCalendarByStudioId(long studioId){
		log.info("查询摄影棚日历信息----start");
		StudioDicCalendarCriteriaDto dto = new StudioDicCalendarCriteriaDto();
		dto.createCriteria().andStudioIdEqualTo(studioId);

		List<StudioDicCalendarDto> studioDicCalendarDto = studioDicCalendarGateWay.selectByCriteria(dto);
		log.info("查询摄影棚日历信息----end");
		return studioDicCalendarDto;
	}
	public ResultResponseDto<ResultObjList> getStudioOffDayCalendarByApi(String from,String to,String calenderTemplateId){
        log.info("摄影棚日历请求api参数：{},"+apiAddressProperties.getGetStudioOffDayCalendarUrl(),"from:"+from+"to:"+to+"calendarNo:"+calenderTemplateId);
        String getGetStudioOffDayCalendarUrl = apiAddressProperties.getGetStudioOffDayCalendarUrl();
		ResponseEntity<ResultResponseDto<ResultObjList>> entity = restTemplate.exchange(getGetStudioOffDayCalendarUrl+"?From="+from+"&To="+to+"&CalendarNo="+calenderTemplateId, HttpMethod.POST,
				null, new ParameterizedTypeReference<ResultResponseDto<ResultObjList>>() {
                });
        log.info("摄影棚日历api返回结果：{}",entity.getBody());
        return entity.getBody();
	}
}
