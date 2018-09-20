package com.shangpin.ephub.product.business.ui.mapp.brand.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.commons.redis.IShangpinRedis;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubBrandDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubBrandDicDto;
import com.shangpin.ephub.client.data.mysql.brand.dto.HubSupplierBrandDicDto;
import com.shangpin.ephub.client.data.mysql.color.dto.HubColorDicItemDto;
import com.shangpin.ephub.client.data.mysql.enumeration.ConstantProperty;
import com.shangpin.ephub.client.data.mysql.enumeration.InfoState;
import com.shangpin.ephub.client.data.mysql.enumeration.TaskType;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.ephub.client.util.DateTimeUtil;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.common.enumeration.HubColorDic;
import com.shangpin.ephub.product.business.common.hubDic.brand.HubBrandDicService;
import com.shangpin.ephub.product.business.common.mapp.hubSupplierValueMapping.HubSupplierValueMappingService;
import com.shangpin.ephub.product.business.common.supplier.spu.HubSupplierSpuService;
import com.shangpin.ephub.product.business.rest.gms.dto.SupplierDTO;
import com.shangpin.ephub.product.business.rest.gms.service.SupplierService;
import com.shangpin.ephub.product.business.ui.mapp.brand.dto.HubSupplierBrandDicRequestDto;
import com.shangpin.ephub.product.business.ui.mapp.brand.dto.HubSupplierBrandDicResponseDto;
import com.shangpin.ephub.product.business.ui.mapp.brand.dto.HubSupplierBrandDicResponseWithPageDto;
import com.shangpin.ephub.product.business.ui.mapp.color.dto.HubSupplierColorDicResponseDto;
import com.shangpin.ephub.product.business.ui.task.common.service.TaskImportService;
import com.shangpin.ephub.response.HubResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * HubSpuImportTaskController
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Company: www.shangpin.com
 * </p>
 * @author zhaogenchun
 * @date 2016年12月21日 下午5:25:30
 */
@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/hub-supplier-brand-dic")
@Slf4j
public class HubSupplierBrandDicController {
	 @Autowired
	    HubBrandDicService hubBrandDicService;
	    @Autowired
	    HubSupplierSpuService hubSupplierSpuService;
	    @Autowired
	    HubSupplierValueMappingService hubSupplierValueMappingService;
	    @Autowired
	    SupplierService supplierService;
	    @Autowired
	    TaskImportService taskImportService;
	    @Autowired
	    IShangpinRedis shangpinRedis;
	    @RequestMapping(value = "/list", method = RequestMethod.POST)
	    public HubResponse selectHubSupplierBrandList(
	            @RequestBody HubSupplierBrandDicRequestDto hubSupplierBrandDicRequestDto) {

	        try {
	            log.info("===品牌映射list请求参数：{}",hubSupplierBrandDicRequestDto);
	            String supplierNo = hubSupplierBrandDicRequestDto.getSupplierNo();
	            String supplierId = null;
	            if(StringUtils.isNotBlank(supplierNo)){
	                SupplierDTO supplierDto = supplierService.getSupplier(supplierNo);
	                if(supplierDto==null){
	                    return HubResponse.errorResp("供应商为空");
	                }
	                supplierId = supplierDto.getSopUserNo();
	                hubSupplierBrandDicRequestDto.setSupplierId(supplierId);
	            }
	            byte type = hubSupplierBrandDicRequestDto.getType();
	            if(type==0){
	                //查询hubSupplierBrand表
	                return getHubSupplierBrandDic(hubSupplierBrandDicRequestDto);
	            }else if(type==1){
	                //查询hubBrandDic表
	                return getHubBrandDic(hubSupplierBrandDicRequestDto);
	            }
	            return HubResponse.errorResp("类型为空");
	        } catch (Exception e) {
	            log.error("获取列表失败：{}", e);
	            return HubResponse.errorResp("获取列表失败");
	        }
	    }

	    private HubResponse getHubBrandDic(HubSupplierBrandDicRequestDto hubSupplierBrandDicRequestDto) throws ParseException {

	        int total = hubBrandDicService.countHubBrand(hubSupplierBrandDicRequestDto.getSupplierBrand(),hubSupplierBrandDicRequestDto.getHubBrandNo(),hubSupplierBrandDicRequestDto.getStartTime(),hubSupplierBrandDicRequestDto.getEndTime());
	        log.info("返回个数："+total);
	        if(total>0){
	            List<HubBrandDicDto> list = hubBrandDicService.getHubBrand(hubSupplierBrandDicRequestDto.getSupplierBrand(),hubSupplierBrandDicRequestDto.getHubBrandNo(),hubSupplierBrandDicRequestDto.getPageNo(), hubSupplierBrandDicRequestDto.getPageSize(),hubSupplierBrandDicRequestDto.getStartTime(),hubSupplierBrandDicRequestDto.getEndTime());
	            if (list != null && list.size() > 0) {
	                List<HubSupplierBrandDicResponseDto> responseList = new ArrayList<HubSupplierBrandDicResponseDto>();
	                for (HubBrandDicDto dicDto : list) {
	                    HubSupplierBrandDicResponseDto dic = new HubSupplierBrandDicResponseDto();
	                    BeanUtils.copyProperties(dicDto, dic);
	                    dic.setCreateTime(DateTimeUtil.getTime(dicDto.getCreateTime()));
	                    if(dicDto.getUpdateTime()!=null){
	                        dic.setUpdateTime(DateTimeUtil.getTime(dicDto.getUpdateTime()));
	                    }
	                    responseList.add(dic);
	                }
	                HubSupplierBrandDicResponseWithPageDto response = new HubSupplierBrandDicResponseWithPageDto();
	                response.setTotal(total);
	                response.setList(responseList);
	                return HubResponse.successResp(response);
	            }
	        }
	        return HubResponse.errorResp("列表为空");
	    }

	    private HubResponse 	getHubSupplierBrandDic(HubSupplierBrandDicRequestDto hubSupplierBrandDicRequestDto) throws ParseException {

	        int total = hubBrandDicService.countSupplierBrandBySupplierIdAndType(hubSupplierBrandDicRequestDto.getSupplierId(),hubSupplierBrandDicRequestDto.getSupplierBrand(),hubSupplierBrandDicRequestDto.getHubBrandNo(),hubSupplierBrandDicRequestDto.getStartTime(),hubSupplierBrandDicRequestDto.getEndTime());

	        log.info("返回个数："+total);
	        if(total>0){
	            List<HubSupplierBrandDicDto> list = hubBrandDicService.getSupplierBrandBySupplierIdAndType(hubSupplierBrandDicRequestDto.getSupplierId(),
	                    hubSupplierBrandDicRequestDto.getSupplierBrand(),hubSupplierBrandDicRequestDto.getHubBrandNo(),hubSupplierBrandDicRequestDto.getPageNo(), hubSupplierBrandDicRequestDto.getPageSize(),hubSupplierBrandDicRequestDto.getStartTime(),hubSupplierBrandDicRequestDto.getEndTime());
	            if (list != null && list.size() > 0) {
	                List<HubSupplierBrandDicResponseDto> responseList = new ArrayList<HubSupplierBrandDicResponseDto>();
	                for (HubSupplierBrandDicDto dicDto : list) {
	                    HubSupplierBrandDicResponseDto dic = new HubSupplierBrandDicResponseDto();
	                    BeanUtils.copyProperties(dicDto, dic);
	                    if(dicDto.getCreateTime()!=null){
	                        dic.setCreateTime(DateTimeUtil.getTime(dicDto.getCreateTime()));
	                    }
	                    if(dicDto.getUpdateTime()!=null){
	                        dic.setUpdateTime(DateTimeUtil.getTime(dicDto.getUpdateTime()));
	                    }
	                    List<HubSupplierValueMappingDto> listMapp = hubSupplierValueMappingService.getHubSupplierValueMappingByTypeAndSupplierId((byte)5,dicDto.getSupplierId());
	                    if(listMapp!=null&&listMapp.size()>0){
	                        dic.setSupplierNo(listMapp.get(0).getHubValNo());
	                        dic.setSupplierName(listMapp.get(0).getHubVal());
	                    }
	                    responseList.add(dic);
	                }
	                HubSupplierBrandDicResponseWithPageDto response = new HubSupplierBrandDicResponseWithPageDto();
	                response.setTotal(total);
	                response.setList(responseList);
	                return HubResponse.successResp(response);
	            }
	        }
	        return HubResponse.errorResp("列表为空");
	    }

//	    @RequestMapping(value = "/detail/{id}", method = RequestMethod.POST)
//	    public HubResponse selectHubCateoryDetail(@PathVariable("id") Long id) {
//	        try {
//	            log.info("===hub品牌详情请求参数：{}",id);
//	            if (id!=null) {
//	                HubSupplierBrandDicResponseWithPageDto page = new HubSupplierBrandDicResponseWithPageDto();
//	                    HubBrandDicDto detail = hubBrandDicService.getHubBrandById(id);
//	                    if (detail != null) {
//	                        List<HubSupplierBrandDicResponseDto> responseList = new ArrayList<HubSupplierBrandDicResponseDto>();
//	                            HubSupplierBrandDicResponseDto dicResponse = new HubSupplierBrandDicResponseDto();
//	                            BeanUtils.copyProperties(detail, dicResponse);
//	                            responseList.add(dicResponse);
//	                        page.setTotal(1);
//	                        page.setList(responseList);
//	                        return HubResponse.successResp(page);
//	                    }
//	                return HubResponse.successResp(null);
//	            } else {
//	                return HubResponse.errorResp("传值为空");
//	            }
//	        } catch (Exception e) {
//	            log.error("获取列表失败：{}", e);
//	            return HubResponse.errorResp("获取列表失败");
//	        }
//	    }
//	    
//	    @RequestMapping(value = "/detail", method = RequestMethod.POST)
//	    public HubResponse selectHubSupplierCateoryDetail(@RequestBody HubSupplierBrandDicRequestDto hubSupplierBrandDicRequestDto) {
//	        try {
//	            log.info("===品牌详情请求参数：{}",hubSupplierBrandDicRequestDto);
//	            if (hubSupplierBrandDicRequestDto != null&&StringUtils.isNotBlank(hubSupplierBrandDicRequestDto.getHubBrandNo())) {
//	                HubSupplierBrandDicResponseWithPageDto page = new HubSupplierBrandDicResponseWithPageDto();
//	                int total = hubBrandDicService.countHubBrandByHubBrand(hubSupplierBrandDicRequestDto.getHubBrandNo());
//	                log.info("===品牌详情返回总数："+total);
//	                if(total>0){
//	                    List<HubBrandDicDto> detail = hubBrandDicService.getSupplierBrandByHubBrand(hubSupplierBrandDicRequestDto.getHubBrandNo(),hubSupplierBrandDicRequestDto.getPageNo(),hubSupplierBrandDicRequestDto.getPageSize());
//	                    if (detail != null) {
//	                        List<HubSupplierBrandDicResponseDto> responseList = new ArrayList<HubSupplierBrandDicResponseDto>();
//	                        for(HubBrandDicDto dic : detail){
//	                            HubSupplierBrandDicResponseDto dicResponse = new HubSupplierBrandDicResponseDto();
//	                            BeanUtils.copyProperties(dic, dicResponse);
//	                            responseList.add(dicResponse);
//	                        }
//	                        page.setTotal(total);
//	                        page.setList(responseList);
//	                        return HubResponse.successResp(page);
//	                    }
//	                }
//	                return HubResponse.successResp(null);
//	            } else {
//	                return HubResponse.errorResp("传值为空");
//	            }
//	        } catch (Exception e) {
//	            log.error("获取列表失败：{}", e);
//	            return HubResponse.errorResp("获取列表失败");
//	        }
//	    }
	    
	    //待处理的更新操作
	    @RequestMapping(value = "/updateAndRefresh", method = { RequestMethod.POST, RequestMethod.GET })
	    public HubResponse update(@RequestBody HubSupplierBrandDicRequestDto hubSupplierBrandDicRequestDto) {
	        try {
	            log.info("修改参数：{}",hubSupplierBrandDicRequestDto);
	          
	           
	            if(hubSupplierBrandDicRequestDto.getBrandDicId()!=null){
	            	HubBrandDicDto hubBrandDicDto = new HubBrandDicDto();
	            	BeanUtils.copyProperties(hubSupplierBrandDicRequestDto, hubBrandDicDto);
	            	hubBrandDicDto.setUpdateTime(new Date());
	            	hubBrandDicService.updateHubBrandById(hubBrandDicDto);
	            }else if(hubSupplierBrandDicRequestDto.getSupplierBrandDicId()!=null){
	            	  HubSupplierBrandDicDto dicDto = new HubSupplierBrandDicDto();
	  	            BeanUtils.copyProperties(hubSupplierBrandDicRequestDto, dicDto);
	  	            dicDto.setUpdateTime(new Date());
	  	            dicDto.setPushState((byte)1);
	            	 hubBrandDicService.updateHubSupplierBrandDicById(dicDto);
	 	            hubBrandDicService.saveHubBrand(hubSupplierBrandDicRequestDto.getHubBrandNo(), hubSupplierBrandDicRequestDto.getSupplierBrand(),dicDto.getUpdateUser());
	            }
	            refreshBrand(hubSupplierBrandDicRequestDto);
 	            return HubResponse.successResp(null);
	        } catch (Exception e) {
	            log.error("刷新失败：{}", e);
	        }
	        return HubResponse.errorResp("刷新异常");
	    }
	    private void refreshBrand(HubSupplierBrandDicRequestDto dto) throws Exception{
			Date date = new Date();
			String taskNo = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date);
			taskImportService.saveTask(taskNo, "品牌映射:"+dto.getSupplierBrand()+"=>"+dto.getHubBrandNo(), dto.getUpdateUser(), TaskType.REFRESH_DIC.getIndex());
			dto.setRefreshDicType(InfoState.RefreshBrand.getIndex());
			taskImportService.sendTaskMessage(taskNo,TaskType.REFRESH_DIC.getIndex(),JsonUtil.serialize(dto));
			shangpinRedis.del(ConstantProperty.REDIS_EPHUB_SUPPLIER_BRAND_MAPPING_MAP_KEY);
	    }
	    /**
	     * 导出查询商品
	     * @param
	     * @return
	     */
	    @RequestMapping(value = "/save", method = { RequestMethod.POST, RequestMethod.GET })
	    public HubResponse save(@RequestBody HubSupplierBrandDicRequestDto hubSupplierBrandDicRequestDto) {

	        try {
	            log.info("保存参数：{}",hubSupplierBrandDicRequestDto);
	            HubSupplierBrandDicDto dicDto = new HubSupplierBrandDicDto();
	            BeanUtils.copyProperties(hubSupplierBrandDicRequestDto, dicDto);
	            if(StringUtils.isNotBlank(hubSupplierBrandDicRequestDto.getSupplierNo())){
	                //待处理保存更新
	                hubBrandDicService.updateHubSupplierBrandDicById(dicDto);
	                hubBrandDicService.saveHubBrand(hubSupplierBrandDicRequestDto.getHubBrandNo(), hubSupplierBrandDicRequestDto.getSupplierBrand(),hubSupplierBrandDicRequestDto.getUpdateUser());
	            }else{
	                //已处理保存
	                hubBrandDicService.saveHubBrand(hubSupplierBrandDicRequestDto.getHubBrandNo(), hubSupplierBrandDicRequestDto.getSupplierBrand(),hubSupplierBrandDicRequestDto.getUpdateUser());
	            }
	            refreshBrand(hubSupplierBrandDicRequestDto);
	            return HubResponse.successResp(null);
	        } catch (Exception e) {
	            log.error("保存失败：{}", e);
	        }
	        return HubResponse.errorResp("保存异常");
	    }

	    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	    public HubResponse deleteHubSupplierBrandDetail(@PathVariable("id") Long id) {
	        try {
	            if (id != null) {
	                hubBrandDicService.deleteHubBrandById(id);
	                return HubResponse.successResp(null);
	            } else {
	                return HubResponse.errorResp("传值为空");
	            }
	        } catch (Exception e) {
	            log.error("获取列表失败：{}", e);
	            return HubResponse.errorResp("获取列表失败");
	        }
	    }

}