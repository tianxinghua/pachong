package com.shangpin.iog.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.page.Page;
import com.shangpin.iog.dto.SeasonRelationDTO;
import com.shangpin.iog.dto.SkuRelationDTO;

import java.util.Date;
import java.util.List;

/**
 * Created by loyalty on 15/10/2.
 * 处理供货商SKU和SOPSKU的关系
 */
public interface SeasonRelationService {


    /**
     * 获取supplierNo与尚品season对照关系列表
     * @param supplierNo 供货商门户No
     * @return
     */
    public  List<SeasonRelationDTO> findListBySupplierNo(String supplierId) throws ServiceException;

    /**
     * 根据尚品SKUID 获取对照关系
     * @param supplierId ：supplierId
     * @param currentSeason ：1代表当前季节 ，0代表非当前季节
     * @return
     * @throws ServiceException
     */
    public List<SeasonRelationDTO>  getSupplierCurrentSeasonBySupplierNo(String supplierId,String currentSeason)  throws  ServiceException;

    /**
     * 根据供应商和尚品季节 得到供应商对应的季节
     * @param spYear ：spYear
     * @param spSeason ：spSeason
     * @return
     * @throws ServiceException
     */
    public List<SeasonRelationDTO>  getSupplierSeasonBySupNoAndSpYearSeason(String supplierId,String spYear,String spSeason)  throws  ServiceException;

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

	public List<SeasonRelationDTO> findSpSeasonBySupplierSeason(String supplierId,
			String supplierSeason);




}
