package com.shangpin.iog.styleside.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import org.apache.log4j.Logger;
import org.datacontract.schemas._2004._07.maximag_connector_vidra_service.ArrayOfArticoloFlatExtLocaleVO;
import org.datacontract.schemas._2004._07.maximag_connector_vidra_service.ArticoloFlatExtLocaleVO;
import org.datacontract.schemas._2004._07.maximag_connector_vidra_service.MgDispo;
import org.springframework.stereotype.Component;
import org.tempuri.IVidraSvcOfArticoloFlatExtVOArticoloFlatVO;
import org.tempuri.VidraSvc;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wangchao on 2017/10/26.
 */
@Component
public class ProductFetchUtil {
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId = "",usr="",pwd="",recordCount="",language="";
	static {
		if (null == bdl){
			bdl = ResourceBundle.getBundle("conf");
		}
		supplierId = bdl.getString("supplierId");

		usr = bdl.getString("usr");
		pwd = bdl.getString("pwd");
		recordCount = bdl.getString("recordCount");
		language = bdl.getString("language");
		supplierId = bdl.getString("supplierId");
	}
	ObjectMapper mapper = new ObjectMapper();
	OutTimeConfig timeConfig = new OutTimeConfig(1000*60*30,1000*60*30,1000*60*30);

	/**
	 * 程序配置：
	 * 拉取的时间、用户名、用户密码、fiter、每页拉取数、语言为配置文件控制
	 * 时间配置 获取当前时间
	 * 页码为程序中循环增加，
	 * 程序的循环结束（while循环）标志：
	 * 拉取的信息的 list size()为 0 的时候结束
	 */
	public  Map<String,String> getProductStock(Collection<String> skuNos) {
		//定义供应商 skuNo （key） Quantita(value) Map集合
		logger.info("===============Collection<String> skuNos size()================"+skuNos.size());
		Map<String, String> spStockMap = new HashMap<>(skuNos.size());
		int page=0;
		boolean loop= true;
		//获取当期那时间
		Calendar cal = Calendar.getInstance();
		IVidraSvcOfArticoloFlatExtVOArticoloFlatVO http = new VidraSvc().getHTTP();
		XMLGregorianCalendarImpl xmlGregorianCalendar = new XMLGregorianCalendarImpl();
		xmlGregorianCalendar.setYear(cal.get(Calendar.YEAR));
		xmlGregorianCalendar.setMonth(cal.get(Calendar.MONTH));
		xmlGregorianCalendar.setDay(cal.get(Calendar.DATE));
		xmlGregorianCalendar.setHour(cal.get(Calendar.HOUR));
		xmlGregorianCalendar.setMinute(cal.get(Calendar.MINUTE));
		xmlGregorianCalendar.setSecond(cal.get(Calendar.SECOND));
		xmlGregorianCalendar.setMillisecond(cal.get(Calendar.MILLISECOND));
		try {
			while(loop){
				page++;
				ArrayOfArticoloFlatExtLocaleVO articoliFlatExtLocaleByDate = http.getArticoliFlatExtLocaleByDate(usr, pwd, "", xmlGregorianCalendar, Integer.parseInt(recordCount), page, language);
				List<ArticoloFlatExtLocaleVO> articoloFlatExtLocaleVO = articoliFlatExtLocaleByDate.getArticoloFlatExtLocaleVO();

				logger.info("thestyleside成功获取第"+page+"页库存数据，当前页有"+articoloFlatExtLocaleVO.size()+"条数据");
				if(null==articoloFlatExtLocaleVO||articoloFlatExtLocaleVO.size()<Integer.parseInt(recordCount)){
					loop = false;
				}
				if(null!=articoloFlatExtLocaleVO&&articoloFlatExtLocaleVO.size()>0){
					for (ArticoloFlatExtLocaleVO vo :articoloFlatExtLocaleVO) {
						/**
						 * modelCode 对应商品的spu , modelCode-size 对应商品的sku
						 */
						String modelCode = vo.getModelCode().getValue();
						String size = vo.getSize().getValue();
						String skuNo = modelCode +"-"+ size;
						logger.info("============skuNo=============>:"+skuNo);
						/**
						 * collect 中包含有该 skuNO 则放入到spStockMap中，没有的话不存放，
						 * 供应商接口数据中可能没有提供全 skuNos 所包含的所有商品库存信息，该spStockMap中则不存放该key value，方法外处理置0
						 */
						if(skuNos.contains(skuNo)){
							MgDispo mgDispo = vo.getQuantita().getValue();
							BigDecimal quantitaDimm = mgDispo.getQuantitaDimm();
							String stockNum = quantitaDimm+"";
							logger.info("=====skuNO:stockNum-========"+skuNo+":"+stockNum);
							spStockMap.put(skuNo,stockNum);
						}
					}
				}
			}
			logger.info("  thestyleside  循环获取库存数据结束==");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("  ==thestyleside 获取第"+page+"页库存数据出现错误,错误如下："+e.getMessage());
		}
		logger.info("成功获取到的map大小  spStockMap.size======"+spStockMap.size());
		return spStockMap;
	}

}
