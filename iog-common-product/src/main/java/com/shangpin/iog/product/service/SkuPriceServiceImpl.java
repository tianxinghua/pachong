package com.shangpin.iog.product.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.iog.dto.NewPriceDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SkuPriceDTO;
import com.shangpin.iog.product.dao.SkuMapper;
import com.shangpin.iog.product.dao.SkuPriceMapper;
import com.shangpin.iog.service.SkuPriceService;

import org.apache.commons.lang.StringUtils;
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
    @Autowired
    SkuMapper skuDAO;
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
    public Map<String,Map<String,String>> getNewSkuPrice(String supplierId) throws ServiceException {
    	StringBuffer sb = new StringBuffer();
    	Map<String,Map<String,String>> supMap = new HashMap<String,Map<String,String>>();
    	Map<String,String> skuMap = new HashMap<String, String>();
    	List<NewPriceDTO> skuList = null;
		try {
			skuList = skuDAO.findNewPrice(supplierId);
		} catch (Exception e) {
            logger.error("获取失败 "+e.getMessage());
			e.printStackTrace();
		}
    	for (NewPriceDTO newPriceDTO : skuList) {
    		if (null!=newPriceDTO.getNewMarketPrice()) {
				sb.append(newPriceDTO.getNewMarketPrice()).append("|");
			}else {
				sb.append(newPriceDTO.getMarketPrice()==null?"-1":newPriceDTO.getMarketPrice()).append("|");
			}
    		if (null!=newPriceDTO.getNewSupplierPrice()) {
    			sb.append(newPriceDTO.getNewSupplierPrice());
    		}else {
    			sb.append(newPriceDTO.getSupplierPrice()==null?"-1":newPriceDTO.getSupplierPrice());
    		}
			skuMap.put(newPriceDTO.getSkuId(), sb.toString());
			sb.setLength(0);
		}
    	supMap.put(supplierId, skuMap);
    	return supMap;
    }
    @Override
    public Map<String, Map<String, String>> getNewSkuPriceBySku(
    		String supplierId, String skuId) throws ServiceException {
    	StringBuffer sb = new StringBuffer();
    	Map<String,Map<String,String>> supMap = new HashMap<String,Map<String,String>>();
    	Map<String,String> skuMap = new HashMap<String, String>();
    	NewPriceDTO newPriceDTO = null;
		try {
			System.out.println("supplierId:"+supplierId + ",skuId:"+skuId);
			newPriceDTO = skuDAO.findNewPriceBySku(supplierId, skuId);
		} catch (Exception e) {
            logger.error("获取失败 "+e.getMessage());
			e.printStackTrace();
		}
		if(newPriceDTO!=null){
			if (null!=newPriceDTO.getNewMarketPrice()) {
				sb.append(newPriceDTO.getNewMarketPrice()).append("|");
			}else {
				sb.append(newPriceDTO.getMarketPrice()==null?"-1":newPriceDTO.getMarketPrice()).append("|");
			}
			if (null!=newPriceDTO.getNewSupplierPrice()) {
				sb.append(newPriceDTO.getNewSupplierPrice());
			}else {
				sb.append(newPriceDTO.getSupplierPrice()==null?"-1":newPriceDTO.getSupplierPrice());
			}
			System.out.println("sb"+sb.toString());
			skuMap.put(newPriceDTO.getSkuId(), sb.toString());
	    	supMap.put(supplierId, skuMap);
		}
	
    	return supMap;
    }

	@Override
	public SkuDTO findSupplierPrice(String supplierId, String skuId)
			throws ServiceException {
		
		SkuDTO sku = null;
		try {
			sku = skuDAO.findSupplierPrice(supplierId, skuId);
		} catch (Exception e) {
            logger.error("获取失败 "+e.getMessage());
			e.printStackTrace();
		}
		return sku;
	}

	@Override
	public void synchPrice(SkuDTO skuDTO) throws ServiceException{
        if(null==skuDTO.getUpdateTime()) skuDTO.setUpdateTime(new Date());
        try {
			skuDAO.updatePrice(skuDTO);
		} catch (Exception e) {
			logger.error("同步新旧价格失败 "+e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public NewPriceDTO getNewPriceDTO(String supplierId, String skuId)
			throws ServiceException {
		NewPriceDTO newPriceDTO = null;
		try {
			newPriceDTO = skuDAO.findNewPriceBySku(supplierId, skuId);
		} catch (Exception e) {
			logger.error("获取newpricedto失败 "+e.getMessage());
			e.printStackTrace();
		}
		return newPriceDTO;
	}

	@Override
    public List<NewPriceDTO> getNewSkuPriceList(String supplierId){
      List<NewPriceDTO> skuList = null;
      try{
        skuList = skuDAO.findNewPrice(supplierId);
      }
      catch (ServiceException e){
        e.printStackTrace();
      }
      return skuList;
    }
}
