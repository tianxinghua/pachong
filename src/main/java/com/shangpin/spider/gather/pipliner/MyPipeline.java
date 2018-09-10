package com.shangpin.spider.gather.pipliner;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.shangpin.spider.entity.gather.CrawlResult;
import com.shangpin.spider.service.gather.CrawlService;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/** 
 * @author  njt 
 * @date 创建时间：2017年11月23日 上午10:14:34 
 * @version 1.0 
 * @parameter  
 */
@Service
public class MyPipeline implements Pipeline{
	@Autowired
	private CrawlService crawlService;

	private static Logger LOG = LoggerFactory.getLogger(MyPipeline.class);

	@Override
	public void process(ResultItems resultItems, Task arg1) {
		for (Map.Entry<String, Object> entry : resultItems.getAll().entrySet()) {
//			String str = entry.getValue()+"";
			@SuppressWarnings("unchecked")
			Map<String,Object> map = (Map<String,Object>) entry.getValue();
			CrawlResult crawlResult = (CrawlResult)map.get("crawlResult");
			System.err.println("----抓取结果为："+crawlResult.toString());
			Long i = crawlService.insert(crawlResult);
			if(i>0) {
				LOG.info("-商品spu为{}-存库成功！",crawlResult.getSpu());
			}else {
				LOG.error("-商品spu为{}-存库失败！",crawlResult.getSpu());
			}
			
		}
	}


}
