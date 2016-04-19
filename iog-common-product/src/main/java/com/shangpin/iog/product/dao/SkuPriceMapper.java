package com.shangpin.iog.product.dao;


import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SkuPriceDTO;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SkuPriceMapper extends IBaseDao<SkuPriceDTO> {

    /**
     * 更新价格和库存
     * @param skuPriceDTO
     * @throws ServiceException
     */
    public void updatePrice(SkuPriceDTO skuPriceDTO) throws ServiceException;

    /**
     * 获取单个SKUPrice信息
     * @param supplierId ：供货商ID
     * @param skuId       : SKUID
     * @return
     * @throws ServiceException
     */
    public SkuPriceDTO getSkuPrice( @Param("supplierId") String supplierId, @Param("skuId") String skuId) throws  ServiceException;




}