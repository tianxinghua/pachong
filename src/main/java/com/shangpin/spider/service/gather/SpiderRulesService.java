package com.shangpin.spider.service.gather;

import com.alibaba.fastjson.JSONObject;
import com.shangpin.spider.entity.gather.SpiderRules;

public interface SpiderRulesService {

	int saveWebRule(SpiderRules spiderRuleInfo, Long whiteInfoId);

	JSONObject getRuleById(Long id);

	JSONObject queryRuleId(Long id);

}
