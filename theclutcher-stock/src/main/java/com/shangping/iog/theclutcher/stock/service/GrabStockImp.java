/**
 * GIGLIO更新库存
 * Created by Kelseo on 2015/9/23.
 */
package com.shangping.iog.theclutcher.stock.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.iog.theclutcher.Startup;
import com.shangpin.iog.theclutcher.dao.Item;
import com.shangpin.iog.theclutcher.dao.Rss;
import com.shangpin.iog.theclutcher.utils.DownloadFileFromNet;
import com.shangpin.iog.theclutcher.utils.UNZIPFile;
import com.shangpin.iog.theclutcher.utils.XMLUtil;
import com.shangpin.sop.AbsUpdateProductStock;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

public class GrabStockImp extends AbsUpdateProductStock {
	
	private  static  ResourceBundle bundle = ResourceBundle.getBundle("sop");
	
	private static Logger logger = Logger.getLogger("info");
	private static Logger loggerError = Logger.getLogger("error");
	private static Logger logMongo = Logger.getLogger("mongodb");
	private static ResourceBundle bdl = null;
	private static String supplierId = "";
	private static String fileName = "feedShangpin.zip";
	private static String urlStr ;

	private static String localPathDefault = ""; //

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("sop");
		supplierId = bdl.getString("supplierId");
		localPathDefault = bdl.getString("local.filePath");
		urlStr = bdl.getString("url");
	}

	public Map<String, Integer> grabStock(Collection<String> skuNos)
			throws ServiceException {		

		String localPath = "";// 存放下载的zip文件的本地目录
		try {
			localPath = URLDecoder.decode((Startup.class.getClassLoader()
					.getResource("").getFile()), "utf-8");
		} catch (UnsupportedEncodingException e) {
			localPath = localPathDefault;
			loggerError.info(e.getMessage());
			e.printStackTrace();
		}
		
		Map<String, Integer> skuStock = new HashMap<>();
		Map<String, Integer> stockMap = new HashMap<>();
		try {
			
			Rss rss = null;

			// 下载
			File zipFile = DownloadFileFromNet.downLoad(urlStr, fileName,
					localPath);
			// 解压
			File xmlFile = UNZIPFile.unZipFile(zipFile, localPath);
			// 读取文件
			String result = DownloadFileFromNet.file2Striing(xmlFile);
			rss= XMLUtil.gsonXml2Obj(Rss.class, result);
				

			
			if (rss == null || rss.getChannel() == null) {
				return  skuStock;
			}

			try {
				logger.info("拉取theclutcher-stock数据开始");

				for (Item item : rss.getChannel().getItem()) {
					if (item == null) {
						continue;
					}
					String size = item.getSize();
					if(StringUtils.isNotBlank(size)){
						if(size.indexOf("½")>0){
							size = size.substring(0, size.indexOf("½")-1)+".5";
						}
					}else{
						size="";
					}

					String skuId = item.getId() +"-"+ size; // 接口中g:id是spuId,对应不同尺码
					String stock = item.getAvailability();
					try {
						stockMap.put(skuId, Integer.parseInt(stock));
					} catch (NumberFormatException e) {
						stockMap.put(skuId, 0);
					}
					logger.info(" skuId =" + skuId + ",stock ="+stock );
					
				}

				for (String skuNo : skuNos) {
					if (stockMap.containsKey(skuNo)) {
						skuStock.put(skuNo, stockMap.get(skuNo));
					} else {
						skuStock.put(skuNo, 0);
					}
				}
				System.out.println(stockMap.toString());

				logger.info("theclutcher-stock赋值库存数据成功");
				logger.info("拉取theclutcher-stock数据成功");
			} catch (Exception e) {
				e.printStackTrace();
				loggerError
						.error("拉取theclutcher-stock数据失败---" + e.getMessage());
				throw new ServiceMessageException("拉取theclutcher-stock数据失败");
			}

		} catch (Exception e) {
			loggerError.error(e.getMessage());
			e.printStackTrace();
		}

		return skuStock;
	}

	public static void main(String[] args) throws Exception {
		String host = bundle.getString("HOST");
        String app_key = bundle.getString("APP_KEY");
        String app_secret= bundle.getString("APP_SECRET");
        if(StringUtils.isBlank(host)||StringUtils.isBlank(app_key)||StringUtils.isBlank(app_secret)){
            logger.error("参数错误，无法执行更新库存");
        }

        AbsUpdateProductStock theclutcher = new GrabStockImp();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		logger.info("theclutcher-stock更新数据库开始");
		theclutcher.updateProductStock(host,app_key,app_secret,"2015-01-01 00:00",format.format(new Date()));
		logger.info("theclutcher-stock更新数据库结束");
		System.exit(0);
//		GrabStockImp g = new GrabStockImp();
//		g.grabStock(null);
	}

}
