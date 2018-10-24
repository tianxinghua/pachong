package com.shangpin.spider.service.gather.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.spider.entity.gather.CrawlResult;
import com.shangpin.spider.mapper.gather.CrawlResultMapper;
import com.shangpin.spider.service.gather.CrawlService;
import com.shangpin.spider.utils.common.CreateTable;

@Service
public class CrawlServiceImpl implements CrawlService{
	private static final Logger LOG = LoggerFactory.getLogger(CrawlServiceImpl.class);
	
	@Autowired
	private CrawlResultMapper crawlResultMapper;
	
	@Override
	public Long insert(CrawlResult crawlResult) {
		String tableName = crawlResult.getGender()+"_"+crawlResult.getBrand();
//		+"_"+crawlResult.getCategory()
		tableName = CreateTable.createTable(tableName);
		LOG.info("----存入的表名为：{}",tableName);
		crawlResultMapper.createTable(tableName);
		Long insertFlag = 0L;
		try {
			insertFlag = crawlResultMapper.insertByTableName(crawlResult,tableName);
		} catch (Exception e) {
			LOG.error("入库失败：链接{}，编号{}，尺寸{}",crawlResult.getDetailLink(),crawlResult.getSpu(),crawlResult.getSize());
			LOG.error("错误信息：{}",e.getMessage());
		}
		return insertFlag;
		
	}

}
