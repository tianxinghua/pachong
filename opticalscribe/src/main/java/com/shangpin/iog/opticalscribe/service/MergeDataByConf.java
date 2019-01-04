package com.shangpin.iog.opticalscribe.service;

/**
 * Created by zhaogenchun on 2015/11/26.
 */

import com.shangpin.iog.opticalscribe.tools.CSVUtilsMergerData;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ResourceBundle;

/**
 * Created by wanner on 2018/8/17.
 *
 * conf.properties 配置如下：
 *

 * cnFilePath=D://test/gucci-cn.csv
 * #拉取数据存放路径
 * otherLanguageFilePath=D://test/gucci-it.csv
 * #去重数据存放路径
 * destFilePath=D://test/gucci-it-merged.csv
 */
//@Component("mergeDataByConf")
public class MergeDataByConf {

	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String cnFilePath;
	private static String otherLanguageFilePath;
	private static String destFilePath;

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		cnFilePath = bdl.getString("cnFilePath");
		otherLanguageFilePath = bdl.getString("otherLanguageFilePath");
		destFilePath = bdl.getString("destFilePath");
	}
	
	

	public  void getUrlList() throws Exception {
		logger.info("---开始合并中外文 数据----");
		CSVUtilsMergerData.mergeDataAndExport(cnFilePath,otherLanguageFilePath,destFilePath);
		logger.info("---合并中外文 数据 结束----");
	}

}