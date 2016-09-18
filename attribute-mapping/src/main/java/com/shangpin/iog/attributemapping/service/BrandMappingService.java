package com.shangpin.iog.attributemapping.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.BrandSpDTO;
import com.shangpin.iog.dto.HubSupplierValueMappingDTO;
import com.shangpin.iog.product.dao.BrandSpMapper;
import com.shangpin.iog.product.dao.HubSupplierValueMappingMapper;
import com.shangpin.iog.product.dao.SpuMapper;
import com.shangpin.iog.service.AttributeMappingService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * Created by lizhongren on 2016/9/13.
 */
@Component
public class BrandMappingService {

    private static org.apache.log4j.Logger loggerInfo = org.apache.log4j.Logger
            .getLogger("info");
    private static org.apache.log4j.Logger loggerError = org.apache.log4j.Logger
            .getLogger("error");

    @Autowired
    BrandSpMapper brandSpDAO;

    @Autowired
    SpuMapper spuMapper;

    @Autowired
    AttributeMappingService mappingService;

    @Autowired
    HubSupplierValueMappingMapper mappingMapperDAO;

    public void setBrandMapping(){
        try {
            //获取尚品的品牌
            List<BrandSpDTO> brandSpDTOs = brandSpDAO.findAll();
            //获取所有供货商的品牌 ，排重

            List<String>  brandList = spuMapper.findDistinctBrand();
           //供货商值与尚品品牌 第一个单词匹配 匹配上插入映射表中
            for(String brand:brandList){
                if(StringUtils.isBlank(brand)) continue;
                for(BrandSpDTO brandSpDTO:brandSpDTOs){
                    try {
                        if(brand.toLowerCase().startsWith(brandSpDTO.getBrandName().toLowerCase())) {
                            HubSupplierValueMappingDTO mappingDTO = new HubSupplierValueMappingDTO();
                            mappingDTO.setSpValueNo(brandSpDTO.getBrandId());
                            mappingDTO.setSpValue(brandSpDTO.getBrandName());
                            mappingDTO.setSupplierValue(brand);
                            mappingDTO.setCreateTime(new Date());
                            mappingDTO.setSpValueType(1);
                            mappingDTO.setMappingType(1);
                            mappingDTO.setCreateUser("system");
                            mappingService.saveBrand(mappingDTO) ;

                        }
                    } catch (Exception e) {
                        loggerError.error(e.getMessage(),e);

                    }
                }
            }


        } catch (Exception e) {
            loggerError.error(e.getMessage(),e);
        }
    }




}
