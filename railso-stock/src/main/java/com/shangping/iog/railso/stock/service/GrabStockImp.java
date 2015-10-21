/**
 * railso更新库存
 */
package com.shangping.iog.railso.stock.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.theclutcher.utils.DownloadFileFromNet;
import com.shangpin.iog.theclutcher.utils.XMLUtil;
import com.shangping.iog.railso.stock.dto.Item;
import com.shangping.iog.railso.stock.dto.Rss;

import org.apache.log4j.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class GrabStockImp extends AbsUpdateProductStock {
	private static Logger logger = Logger.getLogger("info");
	private static Logger loggerError = Logger.getLogger("error");
	private static Logger logMongo = Logger.getLogger("mongodb");
	private static ResourceBundle bdl = null;
	private static String supplierId = "";
	private static String urlStr = "E:\\下载\\railso-com.xml.html";

	public Map<String, String> grabStock(Collection<String> skuNos)
			throws ServiceException {		

		Map<String, String> skuStock = new HashMap<>();
		Map<String, String> stockMap = new HashMap<>();
		try {
			
			// 读取文件
			String result = DownloadFileFromNet.file2Striing(new File(urlStr));
			Rss rss = XMLUtil.gsonXml2Obj(Rss.class, result);

			if (rss == null || rss.getChannel() == null) {
				return skuStock;
			}

			try {
				logger.info("拉取railso-stock数据开始");

				for (Item item : rss.getChannel().getItem()) {
					if (item == null) {
						continue;
					}
					for(Object size : item.getSize()){
						String skuId = item.getId() +"-"+ size.toString(); // 接口中g:id是spuId,对应不同尺码
						String stock = item.getAvailability();
						stockMap.put(skuId, stock);
						System.out.println(stockMap.toString());
					}
				}
				

				for (String skuNo : skuNos) {
					if (stockMap.containsKey(skuNo)) {
						skuStock.put(skuNo, stockMap.get(skuNo));
					} else {
						skuStock.put(skuNo, "0");
					}
				}
				
				logger.info("railso-stock赋值库存数据成功");
				logger.info("拉取railso-stock数据成功");
			} catch (Exception e) {
				e.printStackTrace();
				loggerError
						.error("拉取railso-stock数据失败---" + e.getMessage());
				throw new ServiceMessageException("拉取railso-stock数据失败");
			}

		} catch (Exception e) {
			loggerError.error(e.getMessage());
			e.printStackTrace();
		}

		return skuStock;
	}

	public static void main(String[] args) throws Exception {
		AbsUpdateProductStock grabStockImp = new GrabStockImp();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		logger.info("railso-stock更新数据库开始");
		grabStockImp.updateProductStock(supplierId, "2015-01-01 00:00",
				format.format(new Date()));
		logger.info("railso-stock更新数据库结束");
		System.exit(0);
//		GrabStockImp g = new GrabStockImp();
//		g.grabStock(null);
	}

}
