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
import java.util.*;

/**
 * Created by wangchao on 2017/10/26.
 */
@Component
public class ProductFetchUtil {
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String supplierId = "",year="",month="",days="",hour="",minute="",second="",millisecond="",usr="",pwd="",filter="",recordCount="",language="";
	static {
		if (null == bdl){
			bdl = ResourceBundle.getBundle("conf");
		}
		supplierId = bdl.getString("supplierId");
		year = bdl.getString("year");
		month = bdl.getString("month");
		days = bdl.getString("days");
		hour = bdl.getString("hour");
		minute = bdl.getString("minute");
		second = bdl.getString("second");
		millisecond = bdl.getString("millisecond");
		usr = bdl.getString("usr");
		pwd = bdl.getString("pwd");
		filter = bdl.getString("filter");
		recordCount = bdl.getString("recordCount");
		language = bdl.getString("language");
		supplierId = bdl.getString("supplierId");
	}
	ObjectMapper mapper = new ObjectMapper();
	OutTimeConfig timeConfig = new OutTimeConfig(1000*60*30,1000*60*30,1000*60*30);

	/**
	 * 程序配置：
	 * 拉取的时间、用户名、用户密码、fiter、每页拉取数、语言为配置文件控制
	 * 页码为程序中循环增加，
	 * 程序的循环结束（while循环）标志：
	 * 拉取的信息的 list size()为 0 的时候结束
	 */
	public  Map<String,String> getProductStock(Collection<String> skuNos) {
		//定义供应商 skuNo （key） Quantita(value) Map集合
		Map<String, String> spStockMap = new HashMap<>(skuNos.size());
		int page=0;
		boolean loop= true;
		try {
			while(loop){
				page++;
				IVidraSvcOfArticoloFlatExtVOArticoloFlatVO http = new VidraSvc().getHTTP();
				XMLGregorianCalendarImpl xmlGregorianCalendar = new XMLGregorianCalendarImpl();
				xmlGregorianCalendar.setYear(Integer.parseInt(year));
				xmlGregorianCalendar.setMonth(Integer.parseInt(month));
				xmlGregorianCalendar.setDay(Integer.parseInt(days));
				xmlGregorianCalendar.setHour(Integer.parseInt(hour));
				xmlGregorianCalendar.setMinute(Integer.parseInt(minute));
				xmlGregorianCalendar.setSecond(Integer.parseInt(second));
				xmlGregorianCalendar.setMillisecond(Integer.parseInt(millisecond));

				ArrayOfArticoloFlatExtLocaleVO articoliFlatExtLocaleByDate = http.getArticoliFlatExtLocaleByDate(usr, pwd, filter, xmlGregorianCalendar, Integer.parseInt(recordCount), page, language);

				List<ArticoloFlatExtLocaleVO> articoloFlatExtLocaleVO = articoliFlatExtLocaleByDate.getArticoloFlatExtLocaleVO();

				logger.info("thestyleside成功获取第"+page+"页库存数据，当前页有"+articoloFlatExtLocaleVO.size()+"条数据");
				if(null==articoloFlatExtLocaleVO||articoloFlatExtLocaleVO.size()<Integer.parseInt(recordCount)){
					loop = false;
				}
				if(null!=articoloFlatExtLocaleVO&&articoloFlatExtLocaleVO.size()>0){
					for (ArticoloFlatExtLocaleVO vo :articoloFlatExtLocaleVO) {
						/**
						 * modelCode 对应商品的spu , modelCode+size 对应商品的sku
						 */
						String modelCode = vo.getModelCode().getValue();
						String size = vo.getSize().getValue();
						String skuNo = modelCode + size;
						/**
						 * collect 中包含有该 skuNO 则放入到spStockMap中，没有的话不存放，
						 * 供应商接口数据中可能没有提供全 skuNos 所包含的所有商品库存信息，该spStockMap中则不存放该key value，方法外处理置0
						 */
						if(skuNos.contains(skuNo)){
							MgDispo mgDispo = vo.getQuantita().getValue();
							BigDecimal quantitaDimm = mgDispo.getQuantitaDimm();
							String stockNum = quantitaDimm+"";
							spStockMap.put(skuNo,quantitaDimm+stockNum);
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
