package com.shangpin.spider.service.gather.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.shangpin.spider.entity.gather.SpiderRules;
import com.shangpin.spider.mapper.gather.SpiderRulesMapper;
import com.shangpin.spider.service.gather.SpiderRulesService;
import com.shangpin.spider.service.gather.SpiderWhiteService;

@Service
public class SpiderRulesServiceImpl implements SpiderRulesService{
	private final Logger log = LoggerFactory.getLogger(SpiderRulesServiceImpl.class);
	
	@Autowired
	private SpiderWhiteService spiderWhiteService;
	@Autowired
	private SpiderRulesMapper spiderRulesMapper;
	
	@Override
	public int saveWebRule(SpiderRules spiderRuleInfo, Long spiderRuleId) {
		try {
			if(spiderRuleId==0){
				spiderRulesMapper.insertSelective(spiderRuleInfo);
				Long spiderRuleId1 = spiderRuleInfo.getId();
				spiderWhiteService.saveRuleId(spiderRuleInfo.getWhiteId(),spiderRuleId1);
			}else{
				spiderRuleInfo.setId(spiderRuleId);
				spiderRulesMapper.updateByPrimaryKeySelective(spiderRuleInfo);
			}
			return 1;
		} catch (Exception e) {
			log.error("添加网站规则出错"+e.getMessage());
			return 0;
		}
	}

	@Override
	public JSONObject getRuleById(Long id) {
		JSONObject obj = new JSONObject();
		SpiderRules spiderRuleInfo = spiderRulesMapper.getRuleById(id);
		obj.put("spiderRules", spiderRuleInfo);
		return obj;
	}

	@Override
	public JSONObject queryRuleId(Long id) {
		JSONObject object = new JSONObject();
		Long ruleId = spiderWhiteService.queryRuleId(id);
		object.put("ruleId", ruleId);
		Boolean status = true;
		if(ruleId!=0){
			SpiderRules spiderRuleInfo = spiderRulesMapper.getRuleById(ruleId);
			status = spiderRuleInfo.getStatus();
		}
		object.put("status", status);
		return object;
	}

}
