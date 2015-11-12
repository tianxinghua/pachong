package com.shangpin.iog.product.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.framework.page.Page;
import com.shangpin.iog.dto.ProductDTO;
import com.shangpin.iog.dto.SkuRelationDTO;
import com.shangpin.iog.product.dao.SkuRelationMapper;
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
public class SkuRelationServiceImpl implements SkuRelationService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    public static String  REPEAT_MESSAGE="数据插入失败键重复";

    @Autowired
    SkuRelationMapper skuRelationDAO;

    @Override
    public Page<SkuRelationDTO> findSkuRelateionPageBySupplierId(String supplier, Integer pageIndex, Integer pageSize) throws ServiceException {
        List<SkuRelationDTO> skuRelationDTOList = null;
        Page<SkuRelationDTO> page  =  null;

        if(null!=pageIndex&&null!=pageSize){
            page = new Page<>(pageIndex,pageSize);
            skuRelationDTOList = skuRelationDAO.getSkuRelationListBySupplierId(supplier, new RowBounds(pageIndex, pageSize));


        }else{
            skuRelationDTOList = skuRelationDAO.getSkuRelationListBySupplierId(supplier);
            page = new Page<>(1,skuRelationDTOList.size());
        }

        page.setItems(skuRelationDTOList);
        return page;
    }

    @Override
    public List<SkuRelationDTO> findListBySupplierId(String supplier) throws ServiceException {
        return skuRelationDAO.getSkuRelationListBySupplierId(supplier);
    }

    @Override
    public SkuRelationDTO getSkuRelationBySkuId(String skuId) throws ServiceException {
        return skuRelationDAO.getSkuRelationBySopSkuId(skuId);
    }

    @Override
    public SkuRelationDTO getSkuRelationBySupplierIdAndSupplierSkuNo(String supplier, String supplierSkuNo) throws ServiceException {
        return skuRelationDAO.getSkuRelationBySupplierSkuId(supplier,supplierSkuNo);
    }

    @Override
    public void saveSkuRelateion(SkuRelationDTO skuRelationDTO) throws ServiceException {

        try {
            if(null==skuRelationDTO) throw new ServiceMessageException("sku对应关系对象为空，无法保存。");
            skuRelationDAO.save(skuRelationDTO);
        } catch (Exception e) {
            logger.error(skuRelationDTO.getSopSkuId()+"对应关系失败");
            if(e instanceof DuplicateKeyException) {

                throw new ServiceMessageException(REPEAT_MESSAGE);
            }
            throw new ServiceMessageException("数据插入失败"+e.getMessage());

        }
    }
}
