package com.shangpin.ephub.product.business.service.model;

import com.shangpin.ephub.product.business.rest.model.dto.BrandModelDto;
import com.shangpin.ephub.product.business.rest.model.result.BrandModelResult;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProductDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *   品牌货号规则
 */
@Component
@Slf4j
public class BrandModelRuleBSService {

    @Autowired
    IHubBrandModelRuleService hubBrandModelRuleService;


    public  BrandModelResult verifyProductModle(PendingProductDto pendingProductDto) {
        BrandModelDto brandModelDto = new BrandModelDto();
        brandModelDto.setBrandMode(pendingProductDto.getSpuModel());
        brandModelDto.setHubBrandNo(pendingProductDto.getHubBrandNo());
        brandModelDto.setHubCategoryNo(pendingProductDto.getHubCategoryNo());
        BrandModelResult brandModelResult=  verifyWithCategory(brandModelDto);
        if(null!=brandModelResult&&!brandModelResult.isPassing()){
            //未通过替换 查找货号规则 若存在 替换代码
            pendingProductDto.setSpuModel(replaceSymbol(brandModelDto));

        }
        return brandModelResult;
    }



    /**
     * 校验品牌货号规则
     * @param dto
     * @return
     */
    public BrandModelResult verifyWithCategory(BrandModelDto dto){
        long start = System.currentTimeMillis();
        log.info("品牌校验规则（校验品牌并且校验品类）服务接收到的参数为:{}， 系统即将开始进行品牌型号规则验证!", dto.toString());
        BrandModelResult result = new BrandModelResult();
        //临时解决方案，眼镜货号不校验
        if(checkHubCategoryIsYanJing(dto,result)){
            return result;
        }
        String brandModel = hubBrandModelRuleService.regexVerifyWithCategory(dto.getHubBrandNo(), dto.getHubCategoryNo(), dto.getBrandMode());
        if (StringUtils.isNotBlank(brandModel)) {
            result.setPassing(true);
            result.setBrandMode(brandModel);
        } else {
            if("".equals(brandModel)){//有规则  但不符合

                result.setPassing(false);
            }else{
                String _brandModel = hubBrandModelRuleService.regexVerify(dto.getHubBrandNo(), dto.getHubCategoryNo(), dto.getBrandMode());
                if (StringUtils.isBlank(_brandModel)) {
                    result.setPassing(false);
                } else {
                    result.setPassing(true);
                    result.setBrandMode(_brandModel);
                }
            }

        }
        log.info("品牌校验规则（校验品牌并且校验品类）服务接收到的参数为:{}， 系统品牌型号规则验证结果为{}， 耗时{}milliseconds!", dto.toString(), result.toString(), System.currentTimeMillis() - start);
        return result;
    }


    public String  replaceSymbol(BrandModelDto dto){

        return  hubBrandModelRuleService.replaceSymbol(dto.getHubBrandNo(), dto.getHubCategoryNo(), dto.getBrandMode()," ");

    }

    private boolean checkHubCategoryIsYanJing(BrandModelDto dto,BrandModelResult result) {
        if(dto!=null&&dto.getHubCategoryNo()!=null&&dto.getHubCategoryNo().startsWith("A13")){
            if(dto.getBrandMode()!=null){
                result.setPassing(true);
            }else{
                result.setPassing(false);
            }
            result.setBrandMode(dto.getBrandMode());
            return true;
        }
        return false;
    }

}
