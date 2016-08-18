package com.shangpin.iog.product.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.framework.page.Page;
import com.shangpin.iog.dto.ProductDTO;
import com.shangpin.iog.dto.SeasonRelationDTO;
import com.shangpin.iog.dto.SkuRelationDTO;
import com.shangpin.iog.product.dao.SeasonRelationMapper;
import com.shangpin.iog.product.dao.SkuRelationMapper;
import com.shangpin.iog.service.SeasonRelationService;
import com.shangpin.iog.service.SkuRelationService;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by loyalty on 15/10/2.
 */
@Service
public class SeasonRelationServiceImpl implements SeasonRelationService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    public static String  REPEAT_MESSAGE="数据插入失败键重复";

    @Autowired
    SeasonRelationMapper seasonRelationDAO;
//
//    @Override
//    public void saveSkuRelateion(SkuRelationDTO skuRelationDTO) throws ServiceException {
//
//        try {
//            if(null==skuRelationDTO) throw new ServiceMessageException("sku对应关系对象为空，无法保存。");
//            skuRelationDAO.save(skuRelationDTO);
//        } catch (Exception e) {
//            logger.error(skuRelationDTO.getSopSkuId()+"对应关系失败");
//            if(e instanceof DuplicateKeyException) {
//
//                throw new ServiceMessageException(REPEAT_MESSAGE);
//            }
//            throw new ServiceMessageException("数据插入失败"+e.getMessage());
//
//        }
//    }

	@Override
	public List<SeasonRelationDTO> findListBySupplierNo(String supplierId)
			throws ServiceException {
		// TODO Auto-generated method stub
		return seasonRelationDAO.findListBySupplierNo(supplierId);
	}

	@Override
	public List<SeasonRelationDTO> getSupplierCurrentSeasonBySupplierNo(
			String supplierId,String currentSeason) throws ServiceException {
		// TODO Auto-generated method stub
		return seasonRelationDAO.getSupplierCurrentSeasonBySupplierNo(supplierId,currentSeason);
	}

	@Override
	public List<SeasonRelationDTO> getSupplierSeasonBySupNoAndSpYearSeason(
			String supplierId, String spYear, String spSeason)
			throws ServiceException {
		// TODO Auto-generated method stub
		return seasonRelationDAO.getSupplierSeasonBySupNoAndSpYearSeason(supplierId,spYear,spSeason);
	}

	@Override
	public List<SeasonRelationDTO> getAllCurrentSeason()
			throws ServiceException {
		// TODO Auto-generated method stub
		return seasonRelationDAO.getAllCurrentSeason();
	}

	@Override
	public List<SeasonRelationDTO> getAllSeason() throws ServiceException {
		// TODO Auto-generated method stub
		return seasonRelationDAO.getAllSeason();
	}
	
	@Override
	public List<SeasonRelationDTO> findSpSeasonBySupplierSeason(String supplierId,
			String supplierSeason)  {
		return seasonRelationDAO.findSpSeasonBySupplierSeason(supplierId,supplierSeason);
	}

}
