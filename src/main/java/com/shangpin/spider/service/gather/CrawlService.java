package com.shangpin.spider.service.gather;

import java.util.List;

import com.shangpin.spider.entity.gather.CrawlResult;

public interface CrawlService {

	Long insert(CrawlResult crawlResult);
	
	/**
	 * 查询所有的源信息
	 * @return
	 */
	List<CrawlResult> selectAll(String tableName);

}
