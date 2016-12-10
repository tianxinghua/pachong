package com.shangpin.ep.order.module.sku.mapper;

import com.shangpin.ep.order.module.sku.bean.HubSkuOrg;
import com.shangpin.ep.order.module.sku.bean.HubSkuOrgCriteria;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;
/**
 * <p>Title:HubSkuOrgMapper.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2016年11月18日 下午2:43:37
 */
@Mapper
public interface HubSkuOrgMapper {
    int countByExample(HubSkuOrgCriteria example);

    int deleteByExample(HubSkuOrgCriteria example);

    int deleteByPrimaryKey(Long skuorgid);

    int insert(HubSkuOrg record);

    int insertSelective(HubSkuOrg record);

    List<HubSkuOrg> selectByExampleWithRowbounds(HubSkuOrgCriteria example, RowBounds rowBounds);

    List<HubSkuOrg> selectByExample(HubSkuOrgCriteria example);

    HubSkuOrg selectByPrimaryKey(Long skuorgid);

    int updateByExampleSelective(@Param("record") HubSkuOrg record, @Param("example") HubSkuOrgCriteria example);

    int updateByExample(@Param("record") HubSkuOrg record, @Param("example") HubSkuOrgCriteria example);

    int updateByPrimaryKeySelective(HubSkuOrg record);

    int updateByPrimaryKey(HubSkuOrg record);
    
    /**************************************以上部分为自动生产，如果需要自定义方法的话，请将自定义方法写在下方****************************************************************/
}