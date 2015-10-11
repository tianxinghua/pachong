package com.shangpin.iog.product.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.iog.dto.SkuPriceDTO;
import com.shangpin.iog.product.dao.SkuPriceMapper;
import com.shangpin.iog.service.SkuPriceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lizhongren on 2015/8/29.
 */
@Service
public class SkuPriceServiceImpl implements SkuPriceService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SkuPriceMapper skuPriceDAO;

    private String noPirce="-1";

    @Override
    public Map<String, String> getSkuPriceMap(String supplierId) throws ServiceException {
        Map<String,Object> searchMap = new HashMap<>();
        searchMap.put("supplierId",supplierId);
        Map<String,String> resultMap = new HashMap<>();
        try {
            List<SkuPriceDTO> list = skuPriceDAO.findListByMap(searchMap);
            if(null!=list){
                getResultMap(resultMap, list);
            }
        } catch (Exception e) {
            logger.error("获取失败 "+e.getMessage());
            e.printStackTrace();
        }
        return resultMap;
    }

    private void getResultMap(Map<String, String> resultMap, List<SkuPriceDTO> list) {
        for(SkuPriceDTO skuPriceDTO:list){
            resultMap.put(skuPriceDTO.getSkuId(),null==skuPriceDTO.getMarketPrice()?noPirce:skuPriceDTO.getMarketPrice()+"|"+null==skuPriceDTO.getSupplierPrice()?noPirce:skuPriceDTO.getSupplierPrice()) ;
//            resultMap.put(skuPriceDTO.getSkuId(),skuPriceDTO.getMarketPrice()) ;
        }
    }

    @Override
    public String getSkuPrice(String supplierId, String skuId) throws ServiceException {
        String reslut ="";
        try {
            SkuPriceDTO skuPriceDTO = skuPriceDAO.getSkuPrice(supplierId,skuId);
            if(null!=skuPriceDTO){
                reslut = null==skuPriceDTO.getMarketPrice()?noPirce:skuPriceDTO.getMarketPrice()+"|"+null==skuPriceDTO.getSupplierPrice()?noPirce:skuPriceDTO.getSupplierPrice();
            }
        } catch (Exception e) {
            logger.error("获取失败 "+e.getMessage());
            e.printStackTrace();
        }
        return reslut;
    }

    @Override
    public void updatePrice(SkuPriceDTO skuPriceDTO) throws ServiceException {
        try {
            if(null==skuPriceDTO.getUpdateTime()) skuPriceDTO.setUpdateTime(new Date());
            skuPriceDAO.updatePrice(skuPriceDTO);
        } catch (ServiceException e) {
            logger.error("更新失败 "+e.getMessage());
            throw new ServiceMessageException("更新失败");
        }
    }


}
