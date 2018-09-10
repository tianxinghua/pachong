package com.shangpin.spider.mapper.gather;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.shangpin.spider.entity.gather.SpiderWhiteInfo;

public interface SpiderWhiteInfoMapper {
    int insert(SpiderWhiteInfo record);

    int insertSelective(SpiderWhiteInfo record);

    SpiderWhiteInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SpiderWhiteInfo record);

    int updateByPrimaryKey(SpiderWhiteInfo record);


    /**
     * 根据ID获取源信息
     * @param whiteId
     * @return
     */
	SpiderWhiteInfo getWhiteById(@Param("whiteId")Long whiteId);
	/**
	 * 获取数量
	 * @return
	 */
	Long getCount();
	/**
	 * 网站列表
	 * @param index
	 * @param rowInt
	 * @return
	 */
	List<SpiderWhiteInfo> getWebSiteList(@Param("index")int index, @Param("rowInt")int rowInt);
	/**
	 * 保存网站规则
	 * @param whiteInfoId
	 * @param spiderRuleId1
	 */
	void saveRuleId(@Param("whiteInfoId")Long whiteInfoId, @Param("spiderRuleId")Long spiderRuleId1);
	/**
	 * 查询网站是否有规则
	 * @param id
	 * @return
	 */
	Long queryRuleId(@Param("id")Long id);
	/**
	 * 获取所有网站信息
	 * @return
	 */
	List<SpiderWhiteInfo> getwhiteInfo();
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
}