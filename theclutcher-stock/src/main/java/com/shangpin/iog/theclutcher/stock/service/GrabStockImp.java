/**
 * GIGLIO更新库存
 * Created by Kelseo on 2015/9/23.
 */
package com.shangpin.iog.theclutcher.stock.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.sop.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.theclutcher.stock.dto.Item;
import com.shangpin.iog.theclutcher.stock.dto.Rss;
import com.shangpin.iog.theclutcher.utils.DownloadFileFromNet;
import com.shangpin.iog.theclutcher.utils.UNZIPFile;
import com.shangpin.iog.theclutcher.utils.XMLUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.*;


@Component("theclutcherStock")
public class GrabStockImp extends AbsUpdateProductStock {
	
	private static Logger logger = Logger.getLogger("info");
//	private static Logger loggerError = Logger.getLogger("error");
	private static LoggerUtil error = LoggerUtil.getLogger("error");
//	private static Logger logMongo = Logger.getLogger("mongodb");
	private static ResourceBundle bdl = null;
//	private static String supplierId = "";
	private static String fileName = "feedShangpin.zip";
	private static String urlStr ;

	private static String localPathDefault = ""; //

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("sop");
//		supplierId = bdl.getString("supplierId");
		localPathDefault = bdl.getString("local.filePath");
		urlStr = bdl.getString("url");
	}

	public Map<String, Integer> grabStock(Collection<String> skuNos)
			throws ServiceException {		

		String localPath = "";// 存放下载的zip文件的本地目录
		try {
			localPath = URLDecoder.decode((GrabStockImp.class.getClassLoader()
					.getResource("").getFile()), "utf-8");
		} catch (UnsupportedEncodingException e) {
			localPath = localPathDefault;
			error.error(e.getMessage());
			e.printStackTrace();
		}
		logger.info("zip存放地址+"+localPath); 
		Map<String, Integer> skuStock = new HashMap<>();
		Map<String, Integer> stockMap = new HashMap<>();
		try {
			
			Rss rss = null;

			// 下载
			File zipFile = null;
			try{
				logger.info("====================开始下载zip文件=================");
				zipFile = DownloadFileFromNet.downLoad(urlStr, fileName,
						localPath);
				logger.info("====================下载zip文件结束=================");
			}catch(IOException e){
				error.error(e);
				e.printStackTrace();
				return skuStock;
			}
					
			// 解压
			File xmlFile = null;
			try{
				logger.info("====================解压文件开始=================");
				xmlFile = UNZIPFile.unZipFile(zipFile, localPath);
				logger.info("====================解压文件结束=================");
			}catch(Exception e){
				error.error(e);
				e.printStackTrace();
				return skuStock;
			}
					
			// 读取文件
			try{
				String result = DownloadFileFromNet.file2Striing(xmlFile);
				logger.info("================读取成功=======================");
				rss= XMLUtil.gsonXml2Obj(Rss.class, result);
				logger.info("items.size======="+rss.getChannel().getItem().size()); 
				logger.info("====================转化对象成功=================");
			}catch(Exception e){
				error.error(e);
				return skuStock;
			}
			
				

			
			if (rss == null || rss.getChannel() == null) {
				logger.info("====================rss为空=================");
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
//					logger.info(" skuId =" + skuId + ",stock ="+stock );
//					System.out.println(stockMap.toString());
				}

				for (String skuNo : skuNos) {
					if (stockMap.containsKey(skuNo)) {
						skuStock.put(skuNo, stockMap.get(skuNo));
					} else {
						skuStock.put(skuNo, 0);
					}
				}
				

				logger.info("theclutcher-stock赋值库存数据成功");
				logger.info("拉取theclutcher-stock数据成功");
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("拉取theclutcher-stock数据失败---" + e.getMessage());
				throw new ServiceMessageException("拉取theclutcher-stock数据失败");
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}

		return skuStock;
	}
//	public static void main(String[] args) {
//		GrabStockImp g = new GrabStockImp();
//		try {
//			g.grabStock(null);
//		} catch (ServiceException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	
}
