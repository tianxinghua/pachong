package com.shangpin.iog.mengotti;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.csvreader.CsvReader;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.mengotti.schedule.AppContext;

@Component("mengottiStock")
public class GrapStock extends AbsUpdateProductStock{

	private static Logger logInfo  = Logger.getLogger("info");
	private static LoggerUtil logError = LoggerUtil.getLogger("error");
	private static Logger logMongoDB = Logger.getLogger("MongoDB");
	private static OutTimeConfig outTimeConf = new OutTimeConfig(1000*5, 1000*60 * 5, 1000*60 * 5);
	
	private static ResourceBundle bdl = null;
	private static String supplierId = "";
	private static String uri;
	
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("param");
		supplierId = bdl.getString("supplierId");
		uri = bdl.getString("uri");
	}
	
	@Override
	public Map<String, String> grabStock(Collection<String> skuNo)
			throws ServiceException, Exception {
		
		Map<String, String> skustock = new HashMap<String, String>();
		Map<String,String> stockMap = new HashMap<String, String>();
		List<Item> items = null;
		try{
			items = readLocalCSV(Item.class, ';');
			logInfo.info("items.size========="+items.size()); 
		}catch(Exception e){
			e.printStackTrace();
			logError.error(e);
			return skustock;
		}
		for (Item item : items) {
			try {
				stockMap.put(item.getSupplierSkuNo(), item.getStock());
			} catch (Exception e) {
				logError.error(e);
			}
			
//			System.out.println(stockMap.toString());
		}
		
		logInfo.info("供货商的map.size====="+stockMap.size()); 
		for (String skuno : skuNo) {
            if(stockMap.containsKey(skuno)){
                skustock.put(skuno, stockMap.get(skuno));
            } else{
                skustock.put(skuno, "0");
            }
        }
		logInfo.info("返回的map.size======="+skustock.size());
		return skustock;
	}
	
	/**
	 * 解析csv文件，将其转换为对象
	 * 
	 * @param clazz
	 *            DTO类
	 * @param sep
	 *            分隔符
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> readLocalCSV(Class<T> clazz, char sep)
			throws Exception {
		String result = null;
		URL url = new URL(uri);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置超时间为5分钟
		conn.setConnectTimeout(5 * 60 * 1000);
		// 防止屏蔽程序抓取而返回403错误
		conn.setRequestProperty("User-Agent",
				"Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		// 得到输入流
		InputStream in = conn.getInputStream();
		if (in == null) {
			System.out.println("下载失败！！！！！！！！！！");
			logError.error("下载失败！！！！！！！！！！");
			System.exit(0);
		}
//		BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));  
//        StringBuffer sb = new StringBuffer();  
//          
//        while (StringUtils.isNotBlank(result = reader.readLine()))  
//        {  
//            sb.append(result).append("\n");  
//        } 
//        String sss = sb.toString();
//        System.out.println("-----------------下载完毕------------------");
		String rowString = null;
		List<T> dtoList = new ArrayList<T>();
		List<String> colValueList = null;
		CsvReader cr = null;
		// 解析csv文件
//		cr = new CsvReader(sss);
		cr = new CsvReader(in, Charset.forName("UTF-8"));
		System.out.println("创建cr对象成功");
		// 得到列名集合
		cr.readRecord();
		int a = 0;
		while (cr.readRecord()) {
			a++;
			rowString = cr.getRawRecord();
			if (StringUtils.isNotBlank(rowString)) {
//				split = rowString.split(sep);
//				colValueList = Arrays.asList(split);
				colValueList = fromCSVLinetoArray(rowString,sep);
				T t = fillDTO(clazz.newInstance(), colValueList);
				// 过滤重复的dto。。。sku,
				// dtoSet.add(t);
				dtoList.add(t);
			}

		}

		return dtoList;
	}

	/**
	 * 把CSV文件的一行转换成字符串数组。不指定数组长度。
	 */
	public static ArrayList fromCSVLinetoArray(String source,char sep) {
		if (source == null || source.length() == 0) {
			return new ArrayList();
		}
		int currentPosition = 0;
		int maxPosition = source.length();
		int nextComma = 0;
		ArrayList rtnArray = new ArrayList();
		while (currentPosition < maxPosition) {
			nextComma = nextComma(source, currentPosition,sep);
			rtnArray.add(nextToken(source, currentPosition, nextComma));
			currentPosition = nextComma + 1;
			if (currentPosition == maxPosition) {
				rtnArray.add("");
			}
		}
		return rtnArray;
	}

	/**
	 * 查询下一个逗号的位置。
	 * 
	 * @param source
	 *            文字列
	 * @param st
	 *            检索开始位置
	 * @return 下一个逗号的位置。
	 */
	private static int nextComma(String source, int st,char sep) {
		int maxPosition = source.length();
		boolean inquote = false;
		while (st < maxPosition) {
			char ch = source.charAt(st);
			if (!inquote && ch == sep) {
				break;
			} else if ('"' == ch) {
				inquote = !inquote;
			}
			st++;
		}
		return st;
	}

	/**
	 * 取得下一个字符串
	 */
	private static String nextToken(String source, int st, int nextComma) {
		StringBuffer strb = new StringBuffer();
		int next = st;
		while (next < nextComma) {
			char ch = source.charAt(next++);
			if (ch == '"') {
				if ((st + 1 < next && next < nextComma)
						&& (source.charAt(next) == '"')) {
					strb.append(ch);
					next++;
				}
			} else {
				strb.append(ch);
			}
		}
		return strb.toString();
	}

	public static <T> T fillDTO(T t, List<String> data) {
		try {
			Field[] fields = t.getClass().getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				fields[i].setAccessible(true);
				fields[i].set(t, data.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
	}

	
	private static ApplicationContext factory;
    private static void loadSpringContext()
    {

        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
	
	public static void main(String[] args) {

		loadSpringContext();
//		GrapStock grabStockImp = (GrapStock)factory.getBean("mengottiStock");
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//		logInfo.info("glamestStock更新数据库开始");
//		System.out.println("glamestStock更新数据库开始");
//		try {
//			grabStockImp.updateProductStock(supplierId, "2015-01-01 00:00",
//					format.format(new Date()));
//		} catch (Exception e) {
//			logError.error(e.getMessage());
//			e.printStackTrace();
//		}
//		logInfo.info("glamestStock更新数据库结束");
//		System.out.println("glamestStock更新数据库结束");
//		System.exit(0);
//		try{
//			GrapStock grabStockImp = new GrapStock();
//			grabStockImp.grabStock(null);
//		}catch(Exception ex){
//			ex.printStackTrace();
//		}
		
	}

}
