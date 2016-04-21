package com.shangpin.iog.product.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;

@Mapper
public interface EPRuleMapper extends IBaseDao<String>{

	/**
	 * 
	 * @param flag FLAG为1表示供货商，2表示品牌，3表示品类，4表示尺码，5表示季节，6表示性别
	 * @param yn YN（是否需要） 1表示是，0表示否
	 * @return
	 */
	public List<String> findAll(@Param("flag")Integer flag,@Param("yn")Integer yn);
	
}
