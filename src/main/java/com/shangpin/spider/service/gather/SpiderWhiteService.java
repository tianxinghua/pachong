package com.shangpin.spider.service.gather;

import java.util.List;

import com.shangpin.spider.entity.base.Result;
import com.shangpin.spider.entity.gather.SpiderWhiteInfo;

public interface SpiderWhiteService {

	/**
	 * 查询所有的源信息
	 * @return
	 */
	List<SpiderWhiteInfo> getwhiteInfo();
	/**
	 * 保存网站规则
	 * @param whiteInfoId
	 * @param spiderRuleId1
	 */
	void saveRuleId(Long whiteInfoId, Long spiderRuleId1);
	/**
	 * 查询网站是否有规则
	 * @param id
	 * @return
	 */
	Long queryRuleId(Long id);
	/**
	 * 添加源网站
	 * @param whiteInfo
	 */
	int addWhiteInfo(SpiderWhiteInfo whiteInfo);
	/**
	 * 修改源网站
	 * @param whiteInfo
	 */
	int updateWhiteInfo(SpiderWhiteInfo whiteInfo);
	/**
	 * 网站列表
	 * @param page
	 * @param rows
	 * @return
	 */
	Result<SpiderWhiteInfo> getWebSiteList(Integer page, Integer rows);

}
