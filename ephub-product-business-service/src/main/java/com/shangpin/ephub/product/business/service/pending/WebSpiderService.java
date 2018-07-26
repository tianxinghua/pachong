package com.shangpin.ephub.product.business.service.pending;

import com.shangpin.ephub.client.data.mysql.enumeration.DataState;
import com.shangpin.ephub.client.data.mysql.enumeration.PicState;
import com.shangpin.ephub.client.data.mysql.enumeration.SourceFromEnum;
import com.shangpin.ephub.client.data.mysql.enumeration.SpuState;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.product.business.hubpending.spu.result.HubPendingSpuCheckResult;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.common.enumeration.SpuStatus;
import com.shangpin.ephub.product.business.service.check.HubCheckService;
import com.shangpin.ephub.product.business.rest.hubpending.spu.service.HubPendingSpuCheckService;
import com.shangpin.ephub.product.business.rest.model.result.BrandModelResult;
import com.shangpin.ephub.product.business.service.hub.HubSpuCommonService;
import com.shangpin.ephub.product.business.service.model.BrandModelRuleBSService;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProductDto;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingSkuUpdatedVo;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingUpdatedVo;
import com.shangpin.ephub.response.HubResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class WebSpiderService {

    @Autowired
    HubSpuPendingGateWay spuPendingGateWay;

    @Autowired
    HubSpuCommonService hubSpuCommonService;



    @Autowired
    HubCheckService hubCheckService;

    @Autowired
    BrandModelRuleBSService brandModelRuleBSService;

    @Autowired
    private HubPendingSpuCheckService hubPendingSpuCheckService;

    @Autowired
    private CheckService checkService;

    @Autowired
    private PendingCommonService pendingCommonService;

    @Autowired
    HubSpuPendingGateWay hubSpuPendingGateWay;





    public HubSpuPendingDto getWebSpiderHandedSpu(String brandNo,String spuModel){
        HubSpuPendingCriteriaDto criteria = new HubSpuPendingCriteriaDto();
        criteria.createCriteria().andHubBrandNoEqualTo(brandNo).andSpuModelEqualTo(spuModel).andSpuStateEqualTo(SpuStatus.SPU_HANDLED.getIndex().byteValue())
                .andSourceFromEqualTo(SourceFromEnum.TYPE_WEBSPIDER.getIndex().byteValue()).andDataStateEqualTo(DataState.NOT_DELETED.getIndex())
        ;
        List<HubSpuPendingDto> hubSpuPendingDtos = spuPendingGateWay.selectByCriteria(criteria);
        if(null!=hubSpuPendingDtos&&hubSpuPendingDtos.size()>0){
            return  hubSpuPendingDtos.get(0);
        }
        return null;
    }

    public HubSpuPendingDto getWebSpiderHandedSpuWithHavePic(String brandNo,String spuModel){
        HubSpuPendingCriteriaDto criteria = new HubSpuPendingCriteriaDto();
        criteria.createCriteria().andHubBrandNoEqualTo(brandNo).andSpuModelEqualTo(spuModel).andSpuStateEqualTo(SpuStatus.SPU_HANDLED.getIndex().byteValue())
                .andSourceFromEqualTo(SourceFromEnum.TYPE_WEBSPIDER.getIndex().byteValue())
                .andPicStateEqualTo(PicState.HANDLED.getIndex())
        ;
        List<HubSpuPendingDto> hubSpuPendingDtos = spuPendingGateWay.selectByCriteria(criteria);
        if(null!=hubSpuPendingDtos&&hubSpuPendingDtos.size()>0){
            return  hubSpuPendingDtos.get(0);
        }
        return null;
    }


    public HubResponse<PendingUpdatedVo> saveWebSpider(PendingProductDto pendingProductDto) {
        log.info("接收到的保存待选品的数据：{}"+JsonUtil.serialize(pendingProductDto));
        HubResponse<PendingUpdatedVo> response = new HubResponse<PendingUpdatedVo>();
        response.setCode("0"); //初始设置为成功
        PendingUpdatedVo updatedVo = null;
        boolean pass = true; //全局用来判断整条数据是否校验通过

        HubSpuDto hubSpuDto = null;
        try {
            if(null != pendingProductDto){

                List<HubSpuDto>  hubSpuDtos = hubSpuCommonService.selectHubSpu(pendingProductDto.getSpuModel(),pendingProductDto.getHubBrandNo());
                if(null!=hubSpuDtos&&hubSpuDtos.size()>0){
                    hubSpuDto = hubSpuDtos.get(0);
                    pendingCommonService.convertHubSpuDtoToPendingSpu(hubSpuDto,pendingProductDto);
                    if(!hubCheckService.checkHubSeason(pendingProductDto.getHubSeason())){
                        pendingProductDto.setHubSeason(hubSpuDto.getMarketTime()+"_"+hubSpuDto.getSeason());
                    }
                    pendingCommonService.setSpuPendingStateWhenHaveHubSpu(pendingProductDto);

                }else{

                }
                /**
                 * 校验货号
                 */
                BrandModelResult brandModelResult = brandModelRuleBSService.verifyProductModle(pendingProductDto);

                boolean isHaveHubSpu = false;
                //返回信息中的SKU的信息状态
                List<PendingSkuUpdatedVo> skuMsgReturn = new ArrayList<PendingSkuUpdatedVo>();

                //如果货号校验通过  则先查询是否与HUB_SPU 如果有 直接赋值 并自动过滤尺码
                if(null!=brandModelResult&&brandModelResult.isPassing()){

                  if(null!=hubSpuDto){
                      pendingCommonService.setSpuPendingStateWhenHaveHubSpu(pendingProductDto);
                        //不处理尺码  直接设置
                        isHaveHubSpu = true;

                    }
                    //
                }
                if(!isHaveHubSpu){//无HubSpu的处理逻辑
                    /**
                     * 校验品类和性别是否一致
                     */
                    boolean checkCategoryAndGender = checkService.checkCategoryAndGender(pendingProductDto.getHubGender(), pendingProductDto.getHubCategoryNo());
                    if(brandModelResult.isPassing()){
                        if(checkCategoryAndGender) {

                            //	hubSpuDto = findAndUpdatedFromHubSpu(brandModelResult.getBrandMode(),pendingProductDto);
                            if (null == hubSpuDto) {
                                HubPendingSpuCheckResult spuResult = hubPendingSpuCheckService.checkHubPendingSpu(pendingProductDto);
                                if (spuResult.isPassing()) {
                                    pendingCommonService.setSpuPendingStateWhenHaveHubSpu(pendingProductDto);

                                } else {
                                    pendingCommonService.checkSpuState(pendingProductDto, spuResult);
                                    pass = false;
                                    log.info("pending spu校验失败，不更新：" + spuResult.getResult());
                                    updatedVo = pendingCommonService.setErrorMsg(response, pendingProductDto.getSpuPendingId(), spuResult.getResult());
                                }
                            }
                        }else{
                            pass = false ;
                            log.info("pending spu校验失败，不更新：性别与品类不符。");
                            updatedVo = pendingCommonService.setErrorMsg(response,pendingProductDto.getSpuPendingId(),"性别与品类不符");
                        }
                    }else{
                        pass = false ;
                        log.info("pending spu校验失败，不更新：货号校验不通过。");
                        updatedVo = pendingCommonService.setErrorMsg(response,pendingProductDto.getSpuPendingId(),"货号校验不通过");
                    }

                    if(null == updatedVo){
                        updatedVo = new PendingUpdatedVo();
                        updatedVo.setSpuResult("");
                        updatedVo.setSpuPendingId(pendingProductDto.getSpuPendingId());
                    }
                    updatedVo.setSkus(skuMsgReturn);
                    response.setErrorMsg(updatedVo);

                }
            }
            if(0==pendingProductDto.getSupplierSpuId()){
                pendingProductDto.setSupplierSpuId(null);
            }

            if(pass ){
                pendingProductDto.setSpuState(SpuState.HANDLED.getIndex());
            }
            pendingProductDto.setCreateTime(null);
            pendingProductDto.setPicState(null);
            log.info("更新参数："+JsonUtil.serialize(pendingProductDto));



            hubSpuPendingGateWay.updateByPrimaryKeySelective(pendingProductDto);



        } catch (Exception e) {
            log.error("供应商："+pendingProductDto.getSupplierNo()+"产品："+pendingProductDto.getSpuPendingId()+"更新时发生异常："+e.getMessage());
            pendingCommonService.setErrorMsg(response,pendingProductDto.getSpuPendingId(),"服务器错误");
        }
        log.info("返回的校验结果：+"+JsonUtil.serialize(response));
        return response;
    }




}
