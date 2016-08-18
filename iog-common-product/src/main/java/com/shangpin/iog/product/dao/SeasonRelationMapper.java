package com.shangpin.iog.product.dao;


import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.dto.SeasonRelationDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SkuRelationDTO;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

@Mapper
public interface SeasonRelationMapper extends IBaseDao<SkuRelationDTO> {

    /**
     * 获取supplierNo与尚品season对照关系列表
     * @param supplierNo 供货商门户No
     * @return
     */
    public  List<SeasonRelationDTO> findListBySupplierNo(@Param("supplierId") String supplierId) throws ServiceException;

    /**
     * 根据尚品SKUID 获取对照关系
     * @param skuId ：尚品SKUID
     * @return
     * @throws ServiceException
     */
    public List<SeasonRelationDTO>  getSupplierCurrentSeasonBySupplierNo(@Param("supplierId") String supplierId,@Param("currentSeason") String currentSeason)  throws  ServiceException;

    /**
     * 根据供应商和尚品季节 得到供应商对应的季节
     * @param spYear ：spYear
     * @param spSeason ：spSeason
     * @return
     * @throws ServiceException
     */
    public List<SeasonRelationDTO>  getSupplierSeasonBySupNoAndSpYearSeason(@Param("supplierId") String supplierId,@Param("spYear") String spYear,@Param("spSeason") String spSeason)  throws  ServiceException;

    /**
     * 得到所有的当季新品
     * @return
     * @throws ServiceException
     */
    public List<SeasonRelationDTO>  getAllCurrentSeason()  throws  ServiceException;


    /**
     * 得到所有的季节
     * @return
     * @throws ServiceException
     */
    public List<SeasonRelationDTO>  getAllSeason()  throws  ServiceException;

	public List<SeasonRelationDTO> findSpSeasonBySupplierSeason(
			@Param("supplierId") String supplierId,@Param("supplierSeason") String supplierSeason);
	

}