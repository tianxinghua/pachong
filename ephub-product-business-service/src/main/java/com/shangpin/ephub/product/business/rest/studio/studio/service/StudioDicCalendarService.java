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
import com.shangpin.ephub.product.business.rest.studio.studio.dto.StudioRequestDto;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by loyalty on 17/6/8.
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
	public HubResponseDto<ResultObjList> getStudioOffDayCalendarByApi(String from,String to,String calenderTemplateId){
		StudioRequestDto requestDto = new StudioRequestDto();
		requestDto.setFrom(from);
		requestDto.setTo(to);
		requestDto.setCategoryNo(calenderTemplateId);
        
        log.info("摄影棚日历请求api参数：{},"+apiAddressProperties.getGetStudioOffDayCalendarUrl(),requestDto);
        HttpEntity<StudioRequestDto> requestEntity = new HttpEntity<StudioRequestDto>(requestDto);
        String getGetStudioOffDayCalendarUrl = apiAddressProperties.getGetStudioOffDayCalendarUrl();
		ResponseEntity<HubResponseDto<ResultObjList>> entity = restTemplate.exchange(getGetStudioOffDayCalendarUrl, HttpMethod.POST,
                requestEntity, new ParameterizedTypeReference<HubResponseDto<ResultObjList>>() {
                });
        log.info("摄影棚日历api返回结果：{}",entity.getBody());
        return entity.getBody();
	}
}
