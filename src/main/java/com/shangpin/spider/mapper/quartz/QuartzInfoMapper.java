package com.shangpin.spider.mapper.quartz;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shangpin.spider.entity.quartz.QuartzInfo;

/** 
 * @author  njt 
 * @date 创建时间：2018年1月2日 下午2:06:57 
 * @version 1.0 
 * @parameter  
 */

public interface QuartzInfoMapper {
	/**
	 * 查询quartz的信息
	 */
	List<QuartzInfo> getQuartzList(@Param("index")int index, @Param("rowInt")int rowInt);
	/**
	 * 查询quartz的总条数
	 * @return
	 */
	Long getCount();
}
