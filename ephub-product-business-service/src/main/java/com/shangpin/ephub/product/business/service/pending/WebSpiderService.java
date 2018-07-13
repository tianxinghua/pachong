package com.shangpin.ephub.product.business.service.pending;

import com.shangpin.ephub.client.data.mysql.enumeration.DataState;
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
import com.shangpin.ephub.product.business.rest.model.result.BrandModelResult;
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




    public HubResponse<PendingUpdatedVo> saveWebSpider(PendingProductDto pendingProductDto) {
        log.info("接收到的保存待选品的数据：{}"+JsonUtil.serialize(pendingProductDto));
        HubResponse<PendingUpdatedVo> response = new HubResponse<PendingUpdatedVo>();
        response.setCode("0"); //初始设置为成功
        PendingUpdatedVo updatedVo = null;
        boolean pass = true; //全局用来判断整条数据是否校验通过
        boolean isSkuPass = false;
        HubSpuDto hubSpuDto = null;
        try {
            if(null != pendingProductDto){

                List<HubSpuDto>  hubSpuDtos = selectHubSpu(pendingProductDto.getSpuModel(),pendingProductDto.getHubBrandNo());
                if(null!=hubSpuDtos&&hubSpuDtos.size()>0){
                    hubSpuDto = hubSpuDtos.get(0);
                    convertHubSpuDtoToPendingSpu(hubSpuDto,pendingProductDto);
                    if(!hubCheckService.checkHubSeason(pendingProductDto.getHubSeason())){
                        pendingProductDto.setHubSeason(hubSpuDto.getMarketTime()+"_"+hubSpuDto.getSeason());
                    }
                    setSpuState(pendingProductDto);

                }
                /**
                 * 校验货号
                 */
                BrandModelResult brandModelResult = verifyProductModle(pendingProductDto);

                boolean isHaveHubSpu = false;
                //返回信息中的SKU的信息状态
                List<PendingSkuUpdatedVo> skuMsgReturn = new ArrayList<PendingSkuUpdatedVo>();

                //如果货号校验通过  则先查询是否与HUB_SPU 如果有 直接赋值 并自动过滤尺码
                if(null!=brandModelResult&&brandModelResult.isPassing()){
                    //检验品牌是否
                    if(null==hubSpuDto) hubSpuDto = findAndUpdatedFromHubSpu(brandModelResult.getBrandMode(),pendingProductDto);
                    if(null!=hubSpuDto){
                        setSpuState(pendingProductDto);
                        //尺码处理  ,从页面上获取的
                        isHaveHubSpu = true;
                        String  skuHandleReuslt = skuPendingService.setWaitHandleSkuPendingSize(pendingProductDto.getSpuPendingId(),pendingProductDto.getHubBrandNo(),pendingProductDto.getHubCategoryNo(),pendingProductDto.getHubSkus(),skuMsgReturn);
                        if("0".equals(skuHandleReuslt)){//全部通过
                            isSkuPass = true;
                        }else if("1".equals(skuHandleReuslt)){//全部没有映射上
                            isSkuPass = false ;
                        }else if("2".equals(skuHandleReuslt)){//有无法处理的SKU，需要维护品牌品类尺码的对应关系
                            isSkuPass = false ;
                        }

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
                                    setSpuState(pendingProductDto);

                                } else {
                                    checkSpuState(pendingProductDto, spuResult);
                                    pass = false;
                                    log.info("pending spu校验失败，不更新：" + spuResult.getResult());
                                    updatedVo = setErrorMsg(response, pendingProductDto.getSpuPendingId(), spuResult.getResult());
                                }
                            }
                        }else{
                            pass = false ;
                            log.info("pending spu校验失败，不更新：性别与品类不符。");
                            updatedVo = setErrorMsg(response,pendingProductDto.getSpuPendingId(),"性别与品类不符");
                        }
                    }else{
                        pass = false ;
                        log.info("pending spu校验失败，不更新：货号校验不通过。");
                        updatedVo = setErrorMsg(response,pendingProductDto.getSpuPendingId(),"货号校验不通过");
                    }
                    //开始校验sku
                    List<HubSkuPendingDto> pengdingSkus = pendingProductDto.getHubSkus();
                    if(null == updatedVo){
                        updatedVo = new PendingUpdatedVo();
                        updatedVo.setSpuResult("");
                        updatedVo.setSpuPendingId(pendingProductDto.getSpuPendingId());
                    }


                    if(pengdingSkus!=null&&pengdingSkus.size()>0){

                        pass = updateSkuPendingValue(pendingProductDto, response, pass, hubSpuDto, pengdingSkus, skuMsgReturn);
                        //判断全局尺码是否都是排除
                        for(HubSkuPendingDto hubSkuPendingDto : pengdingSkus) {
                            String hubSkuSize = hubSkuPendingDto.getHubSkuSize();
                            hubSkuSize = StringUtils.isEmpty(hubSkuSize) ? "" : hubSkuSize;
                            if (hubSkuSize.startsWith("排除")) {

                            } else {
                                isSkuPass = true;
                            }
                        }

                        if(!isSkuPass){
                            updatedVo = setErrorMsg(response,pendingProductDto.getSpuPendingId(),"尺码都被排除");
                        }
                    }else{
                        updatedVo = setErrorMsg(response,pendingProductDto.getSpuPendingId(),"无sku信息");
                    }

                    updatedVo.setSkus(skuMsgReturn);
                    response.setErrorMsg(updatedVo);
                }
            }
            if(0==pendingProductDto.getSupplierSpuId()){
                pendingProductDto.setSupplierSpuId(null);
            }

            if(pass &&isSkuPass&& null != hubSpuDto){
                pendingProductDto.setSpuState(SpuState.INFO_IMPECCABLE.getIndex());
            }else if(pass&&isSkuPass){
                pendingProductDto.setSpuState(SpuState.INFO_IMPECCABLE.getIndex());
            }
            pendingProductDto.setCreateTime(null);
            pendingProductDto.setPicState(null);
            log.info("更新参数："+JsonUtil.serialize(pendingProductDto));



            hubSpuPendingGateWay.updateByPrimaryKeySelective(pendingProductDto);


            response.setCode("0");
        } catch (Exception e) {
            log.error("供应商："+pendingProductDto.getSupplierNo()+"产品："+pendingProductDto.getSpuPendingId()+"更新时发生异常："+e.getMessage());
            setErrorMsg(response,pendingProductDto.getSpuPendingId(),"服务器错误");
        }
        log.info("返回的校验结果：+"+JsonUtil.serialize(response));
        return response;
    }

}
