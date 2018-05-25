package com.shangpin.ephub.product.business.rest.hub.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuGateWay;
import com.shangpin.ephub.product.business.conf.rpc.ApiAddressProperties;
import com.shangpin.ephub.product.business.rest.hub.dto.SpuDescVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.data.mysql.enumeration.PriceHandleType;
import com.shangpin.ephub.client.data.mysql.enumeration.SupplierSelectState;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSkuSupplierMappingWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSkuSupplierMappingGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuGateWay;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSkuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.sku.gateway.HubSupplierSkuGateWay;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSupplierSpuGateWay;
import com.shangpin.ephub.client.product.business.gms.result.HubResponseDto;
import com.shangpin.ephub.client.product.business.gms.result.SopSkuDto;
import com.shangpin.ephub.product.business.rest.gms.service.SopSkuService;
import com.shangpin.ephub.product.business.rest.hubpending.pendingproduct.dto.SpSkuNoDto;
import com.shangpin.ephub.product.business.rest.price.service.PriceService;
import com.shangpin.ephub.product.business.service.ServiceConstant;
import com.shangpin.ephub.product.business.service.hub.dto.SopSkuQueryDto;
import com.shangpin.ephub.response.HubResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

/**
 *
 * 外部调用 非页面
 */
@RestController
@RequestMapping(value = "/pendingproduct")
@Slf4j
public class HubPendingProductController {
	
    @Autowired
	HubSkuPendingGateWay skuPendingGateWay;
    
    @Autowired
    HubSupplierSkuGateWay supplierSkuGateWay;
    @Autowired
    HubSupplierSpuGateWay supplierSpuGateWay;

    @Autowired
	HubSpuGateWay spuGateWay;

    @Autowired
	HubSpuPendingGateWay spuPendingGateWay;

	@Autowired
	HubSkuSupplierMappingGateWay skuSupplierMappingGateWay;

	@Autowired
	HubSkuGateWay hubSkuGateWay;

	@Autowired
	SopSkuService sopSkuService;
	@Autowired
	PriceService priceService;

	@Autowired
	private TaskExecutor executor;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	ApiAddressProperties apiAddressProperties;

	ObjectMapper mapper = new ObjectMapper();
	
	@RequestMapping(value = "/setspskuno")
	public HubResponse<?> checkSku(@RequestBody SpSkuNoDto dto){
		log.info("receive parameters :" + dto.toString());
		try {
			this.updatePendingSku(dto);

		} catch (Exception e) {
			log.error("update spsku error. reason :"+ e.getMessage(),e);
			return HubResponse.errorResp(e.getMessage());
		}
		return HubResponse.successResp(true);
	}

	private void updatePendingSku(SpSkuNoDto dto) throws Exception{
		//如果是因为SOP已存在的错误 需要调用接口获取到信息
//		log.info("getExistSpSkuNo start");
		if(dto.getSign()==0){
			getExistSpSkuNo(dto);
		}
//		log.info("getExistSpSkuNo end");
		//写入尚品的SKUno
		HubSkuPendingDto searchSkuPending =null;
//		log.info("searchSkuPending start");
		searchSkuPending = updateSkuPendingSpSkuNo(dto);
//		log.info("searchSkuPending end ");
		//更新SKUSUPPLIERMAPPING 的状态
//		log.info("updateSkuSupplierMapping start");
		updateSkuSupplierMapping(dto);
//		log.info("updateSkuSupplierMapping end");
		//修改hub_sku中的商品sku编号
//		log.info("updateHubSkuSpSkuNo start");
		if(null!=searchSkuPending){
			updateHubSkuSpSkuNo(dto, searchSkuPending);
		}
//		log.info("updateHubSkuSpSkuNo end");
		//更新尚品SKU到供货商原始SKU中
//		log.info("updateSkuSupplierSpSkuNo start");
		updateSkuSupplierSpSkuNo(dto);
//		log.info("updateSkuSupplierSpSkuNo end");
		// 获取hubspu 信息里的描述信息  ，若为空 取对应供货商里的信息
		if(null!=searchSkuPending) {
			log.info("push desc to sop ");
			PushProductDesc pushProductDesc = new PushProductDesc(spuPendingGateWay,spuGateWay,searchSkuPending,restTemplate,apiAddressProperties,mapper);
			executor.execute(pushProductDesc);
		}else{
			log.info("searchSkuPending is null, don't  need to   push desc to sop ");
		}

	}

	private void updateSkuSupplierSpSkuNo(SpSkuNoDto dto) throws Exception{
		HubSupplierSkuCriteriaDto criteria = new HubSupplierSkuCriteriaDto();
		criteria.createCriteria().andSupplierIdEqualTo(dto.getSupplierId()).andSupplierSkuNoEqualTo(dto.getSupplierSkuNo());
		List<HubSupplierSkuDto> hubSkuPendingList = supplierSkuGateWay.selectByCriteria(criteria);
		if(hubSkuPendingList!=null&&hubSkuPendingList.size()>0){
			HubSupplierSkuDto hubSkuPendingOrigion = hubSkuPendingList.get(0);
			if(hubSkuPendingOrigion.getSupplierSpuId()!=null){
				HubSupplierSkuDto hubSkuPending = new HubSupplierSkuDto();
				hubSkuPending.setSpSkuNo( dto.getSkuNo());
				hubSkuPending.setMemo(dto.getErrorReason());
				hubSkuPending.setSupplierSkuId(hubSkuPendingOrigion.getSupplierSkuId());
				supplierSkuGateWay.updateByPrimaryKeySelective(hubSkuPending);
				HubSupplierSpuDto hubSupplierSpuDto = supplierSpuGateWay.selectByPrimaryKey(hubSkuPendingOrigion.getSupplierSpuId());
				hubSkuPendingOrigion.setSpSkuNo(dto.getSkuNo());
				log.info("查询hubSupplierSpu:{}",hubSupplierSpuDto);
				try{
					if(!StringUtils.isEmpty(hubSkuPendingOrigion.getMarketPrice()) && !StringUtils.isEmpty(hubSkuPendingOrigion.getSupplyPrice())){
						priceService.savePriceRecordAndSendConsumer(hubSupplierSpuDto, dto.getSupplierNo(), hubSkuPendingOrigion, PriceHandleType.NEW_DEFAULT);
					}
				}catch(Exception e){
					log.error("推送价格队列失败",e);
				}
			}
		}
		
	}
	
	private void getExistSpSkuNo(SpSkuNoDto dto) {
		if(ServiceConstant.HUB_SEND_TO_SCM_EXIST_SCM_ERROR.equals(dto.getErrorReason())){
            //如果是已存在的错误，调用接口  组装
            SopSkuQueryDto queryDto = new SopSkuQueryDto();
            queryDto.setSopUserNo(dto.getSupplierId());
            List<String> supplierSkuNoList = new ArrayList<>();
             supplierSkuNoList.add(dto.getSupplierSkuNo());
            queryDto.setLstSupplierSkuNo(supplierSkuNoList);
            HubResponseDto<SopSkuDto> sopSkuResponseDto = null;
            try {
                if(supplierSkuNoList.size()>0){
                    sopSkuResponseDto = sopSkuService.querySpSkuNoFromScm(queryDto);
					if(null!=sopSkuResponseDto&&sopSkuResponseDto.getIsSuccess()){
						List<SopSkuDto> sopSkuDtos =  sopSkuResponseDto.getResDatas();
						if(null!=sopSkuDtos&&sopSkuDtos.size()>0){
							 SopSkuDto  sopSkuDto =  sopSkuDtos.get(0);
							dto.setSkuNo(sopSkuDto.getSkuNo());
						}
					}
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}

	private void updateHubSkuSpSkuNo(SpSkuNoDto dto, HubSkuPendingDto searchSkuPending) {
		if(null!=searchSkuPending&&searchSkuPending.getHubSkuNo()!=null){
//			HubSkuCriteriaDto criteriaSku = new HubSkuCriteriaDto();
			HubSkuDto hubSku = new HubSkuDto();
			hubSku.setSpSkuNo(dto.getSkuNo());
			hubSku.setUpdateTime(new Date());
			HubSkuCriteriaDto skuCriteria = new HubSkuCriteriaDto();
			skuCriteria.createCriteria().andSkuNoEqualTo(searchSkuPending.getHubSkuNo());
			HubSkuWithCriteriaDto criteriaWithSku = new HubSkuWithCriteriaDto(hubSku,skuCriteria);
			hubSkuGateWay.updateByCriteriaSelective(criteriaWithSku);
		}
	}

	private void updateSkuSupplierMapping(SpSkuNoDto dto) {
		HubSkuSupplierMappingCriteriaDto criteriaDto = new HubSkuSupplierMappingCriteriaDto();
		criteriaDto.createCriteria().andSupplierNoEqualTo(dto.getSupplierNo()).andSupplierSkuNoEqualTo(dto.getSupplierSkuNo());

		HubSkuSupplierMappingDto hubSkuSupplierMapping = new HubSkuSupplierMappingDto();
		if(dto.getSign()==1){
			if(StringUtils.isEmpty(dto.getSkuNo())){
				hubSkuSupplierMapping.setSupplierSelectState(Integer.valueOf(SupplierSelectState.SELECTE_FAIL.getIndex()).byteValue());
				hubSkuSupplierMapping.setMemo("尚品SKU未生成");
			}else{
				hubSkuSupplierMapping.setSupplierSelectState(Integer.valueOf(SupplierSelectState.SELECTED.getIndex()).byteValue());
			}

		}else if(dto.getSign()==2){
			hubSkuSupplierMapping.setSupplierSelectState(Integer.valueOf(SupplierSelectState.SELECTED.getIndex()).byteValue());
			hubSkuSupplierMapping.setMemo("品牌品类协议尚未维护");
		}else{
			if(ServiceConstant.HUB_SEND_TO_SCM_EXIST_SCM_ERROR.equals(dto.getErrorReason())){
				hubSkuSupplierMapping.setSupplierSelectState(Integer.valueOf(SupplierSelectState.EXIST.getIndex()).byteValue());
				hubSkuSupplierMapping.setMemo(ServiceConstant.HUB_SEND_TO_SCM_EXIST);

			}else{
				hubSkuSupplierMapping.setSupplierSelectState(Integer.valueOf(SupplierSelectState.SELECTE_FAIL.getIndex()).byteValue());
				hubSkuSupplierMapping.setMemo(dto.getErrorReason());
			}
		}
		hubSkuSupplierMapping.setUpdateTime(new Date());
		HubSkuSupplierMappingWithCriteriaDto skumappingCritria = new HubSkuSupplierMappingWithCriteriaDto(hubSkuSupplierMapping,criteriaDto);
		skuSupplierMappingGateWay.updateByCriteriaSelective(skumappingCritria);
	}

	private HubSkuPendingDto updateSkuPendingSpSkuNo(SpSkuNoDto dto) {
		HubSkuPendingDto hubSkuPending = new HubSkuPendingDto();
		hubSkuPending.setSpSkuNo( dto.getSkuNo());
		hubSkuPending.setMemo(dto.getErrorReason());
		hubSkuPending.setUpdateTime(new Date());
		HubSkuPendingCriteriaDto criteria = new HubSkuPendingCriteriaDto();
		criteria.createCriteria().andSupplierNoEqualTo(dto.getSupplierNo())
				.andSupplierSkuNoEqualTo(dto.getSupplierSkuNo());


		HubSkuPendingWithCriteriaDto skuCritria = new HubSkuPendingWithCriteriaDto(hubSkuPending,criteria);
		skuPendingGateWay.updateByCriteriaSelective(skuCritria);

		List<HubSkuPendingDto> hubSkuPendingDtos = null;
		if(1==dto.getSign()||2==dto.getSign()){
			////更新成功的才处理
			hubSkuPendingDtos = skuPendingGateWay.selectByCriteria(criteria);
		}else{
			//如果返回错误 但错误信息是SOP已存在的  则调用接口反写到返回值内 需要调用 以便更新HUB_sku的对应关系
			if(ServiceConstant.HUB_SEND_TO_SCM_EXIST_SCM_ERROR.equals(dto.getErrorReason())){

				hubSkuPendingDtos = skuPendingGateWay.selectByCriteria(criteria);
			}
		}
		HubSkuPendingDto  searchSkuPending  = null;

		if(null!=hubSkuPendingDtos&&hubSkuPendingDtos.size()>0){
			searchSkuPending = hubSkuPendingDtos.get(0);
		}
		return searchSkuPending;
	}



	@SuppressWarnings("unused")
	private void updateExistSkuSupplierMapping(SpSkuNoDto dto) {
		HubSkuSupplierMappingCriteriaDto criteriaDto = new HubSkuSupplierMappingCriteriaDto();
		criteriaDto.createCriteria().andSupplierNoEqualTo(dto.getSupplierNo()).andSupplierSkuNoEqualTo(dto.getSupplierSkuNo());

		HubSkuSupplierMappingDto hubSkuSupplierMapping = new HubSkuSupplierMappingDto();

		hubSkuSupplierMapping.setSupplierSelectState(Integer.valueOf(SupplierSelectState.EXIST.getIndex()).byteValue());
		hubSkuSupplierMapping.setMemo(dto.getErrorReason());

		hubSkuSupplierMapping.setUpdateTime(new Date());
		HubSkuSupplierMappingWithCriteriaDto skumappingCritria = new HubSkuSupplierMappingWithCriteriaDto(hubSkuSupplierMapping,criteriaDto);
		skuSupplierMappingGateWay.updateByCriteriaSelective(skumappingCritria);
	}

	class PushProductDesc implements Runnable {

		HubSpuGateWay spuGateWay;
		HubSpuPendingGateWay spuPendingGateWay;
		HubSkuPendingDto skuPending;

		RestTemplate restTemplate;
		ApiAddressProperties apiAddressProperties;
		ObjectMapper  mapper;

		public PushProductDesc(HubSpuPendingGateWay spuPendingGateWay,HubSpuGateWay spuGateWay,HubSkuPendingDto skuPending,
							   RestTemplate restTemplate,ApiAddressProperties apiAddressProperties,ObjectMapper  mapper){
			this.spuPendingGateWay = spuPendingGateWay;
			this.spuGateWay = spuGateWay;
			this.skuPending= skuPending;
			this.restTemplate = restTemplate;
			this.apiAddressProperties = apiAddressProperties;
			this.mapper = mapper;

		}
		@Override
		public void run() {
			//获取spu属性
			String spuNo = null;
			if(StringUtils.isEmpty(skuPending.getHubSkuNo())){
				log.error("推送商品描述到SOP时失败。error:skuno无值");
				return;
			}
			try {

				spuNo = skuPending.getHubSkuNo().substring(0,skuPending.getHubSkuNo().length()-3);
			} catch (Exception e) {
				log.error("推送商品描述到SOP时失败,无法获取spuNo。error:"+e.getMessage(),e);

				e.printStackTrace();
				return;
			}
			HubSpuCriteriaDto criteria = new HubSpuCriteriaDto();
			criteria.createCriteria().andSpuNoEqualTo(spuNo);
			List<HubSpuDto> hubSpuDtos = spuGateWay.selectByCriteria(criteria);
			SpuDescVo productDto =null;
			if(null!=hubSpuDtos&&hubSpuDtos.size()>0){
				HubSpuDto spuDto = hubSpuDtos.get(0);
				if(!StringUtils.isEmpty(spuDto.getSpuDesc())){
					try {
						productDto = getSpuDescVo(skuPending.getSupplierNo(),spuDto.getSpuDesc(),skuPending.getSpSkuNo());
						sendToSop(productDto);
					} catch (Exception e) {
						try {
							//失败多调用一次
							sendToSop(productDto);
						} catch (Exception e1) {
							log.error("推送产品描述到SOP失败.reason:" + e.getMessage(),e1);
						}
					}
					return ;
				}
			}
            //获取hubspupending 中的描述
			HubSpuPendingDto hubSpuPendingDto = spuPendingGateWay.selectByPrimaryKey(skuPending.getSpuPendingId());
			if(null!=hubSpuPendingDto&&!StringUtils.isEmpty(hubSpuPendingDto.getSpuDesc())){
				try {
					productDto = getSpuDescVo(skuPending.getSupplierNo(),hubSpuPendingDto.getSpuDesc(),skuPending.getSpSkuNo());
					log.info("推送SOP信息:"+ productDto.toString());
					sendToSop(productDto);
				} catch (Exception e) {
					try {
						//失败多调用一次
						sendToSop(productDto);
					} catch (Exception e1) {
						log.error("推送产品描述到SOP失败.reason:" + e.getMessage(),e1);
					}

				}
			}else{
				log.info("spuNo:"+spuNo + "，无法获取描述信息");
			}

		}




		private String sendToSop(SpuDescVo productDto) throws Exception {
			HttpEntity<SpuDescVo> requestEntity = new HttpEntity<SpuDescVo>(productDto);

			ResponseEntity<String> entity = restTemplate.exchange(apiAddressProperties.getSopDescUrl(), HttpMethod.POST,
					requestEntity, new ParameterizedTypeReference<String>() {
					});
			return entity.getBody();
		}

		private  SpuDescVo getSpuDescVo(String supplierNo,String desc,String spSkuNo){
			SpuDescVo productDto = new SpuDescVo();
			productDto.setDescHtml(desc);
			productDto.setSupplierNo(supplierNo);
			productDto.setSpskuNo(spSkuNo);
			productDto.setUpdateUser("Product-Business");
			return productDto;
		}
	}

}