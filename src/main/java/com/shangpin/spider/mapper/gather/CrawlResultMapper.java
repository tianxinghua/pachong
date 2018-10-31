package com.shangpin.spider.mapper.gather;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.shangpin.spider.entity.gather.CrawlResult;

public interface CrawlResultMapper {
    int insert(CrawlResult record);

    int insertSelective(CrawlResult record);

    CrawlResult selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CrawlResult record);

    int updateByPrimaryKeyWithBLOBs(CrawlResult record);

    int updateByPrimaryKey(CrawlResult record);
    
	void createTable(@Param("tableName")String tableName);

	Long insertByTableName(@Param("crawlResult")CrawlResult crawlResult, @Param("tableName")String tableName);

	List<CrawlResult> selectAll(@Param("tableName")String tableName);
}