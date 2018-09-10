package com.shangpin.spider.service.gather.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.spider.common.Constants;
import com.shangpin.spider.entity.base.Result;
import com.shangpin.spider.entity.gather.SpiderWhiteInfo;
import com.shangpin.spider.gather.utils.PageUtil;
import com.shangpin.spider.mapper.gather.SpiderWhiteInfoMapper;
import com.shangpin.spider.service.gather.SpiderWhiteService;
@Service
public class SpiderWhiteServiceImpl implements SpiderWhiteService {

	private Logger log = LoggerFactory.getLogger(SpiderWhiteServiceImpl.class);
	@Autowired
	private SpiderWhiteInfoMapper spiderWhiteInfoMapper;
	@Override
	public List<SpiderWhiteInfo> getwhiteInfo() {
		return spiderWhiteInfoMapper.getwhiteInfo();
	}
	
	@Override
	public Result<SpiderWhiteInfo> getWebSiteList(Integer page, Integer rows) {
		log.info("----源网站列表");
		Result<SpiderWhiteInfo> result = new Result<SpiderWhiteInfo>();
		Long totalCount = spiderWhiteInfoMapper.getCount();
	    Long totalPages = PageUtil.getPage(totalCount, rows);
		
		List<SpiderWhiteInfo> list = spiderWhiteInfoMapper.getWebSiteList((page-1)*rows,rows);
		if(list!=null&&list.size()>0){
			result.setDataList(list);
			result.setCurrentPage(page);
			result.setTotalCount(totalCount);
			result.setTotalPages(totalPages);
			result.setStatus(Constants.SUCCESSCODE);
			result.setMsg(Constants.SUCCESS);
		}else{
			result.setDataList(null);
			result.setCurrentPage(1);
			result.setTotalCount(0L);
			result.setTotalPages(0L);
			result.setStatus(Constants.ERRORCODE);
			result.setMsg(Constants.FAIL);
		}
		/*obj.put("totalPages", totalPages);
		obj.put("currentPage", currentPage);
		obj.put("totalCount", totalCount);
		obj.put("dataList", array);*/
		
		return result;
	}
	
	@Override
	public int updateWhiteInfo(SpiderWhiteInfo whiteInfo) {
		spiderWhiteInfoMapper.updateWhiteInfo(whiteInfo);
		return 1;
	}
	
	@Override
	public int addWhiteInfo(SpiderWhiteInfo whiteInfo) {
		return spiderWhiteInfoMapper.addWhiteInfo(whiteInfo);
	}

	@Override
	public void saveRuleId(Long whiteInfoId, Long spiderRuleId1) {
		spiderWhiteInfoMapper.saveRuleId(whiteInfoId, spiderRuleId1);
	}

	@Override
	public Long queryRuleId(Long id) {
		return spiderWhiteInfoMapper.queryRuleId(id);
	}

}
