package com.shangpin.iog.mengotti;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.csvreader.CsvReader;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;

@Component("mengotti")
public class FechProduct {

	private static Logger logInfo = Logger.getLogger("info");
	private static Logger logError = Logger.getLogger("error");
	private static Logger logMongoDB = Logger.getLogger("MongoDB");
	private static OutTimeConfig outTimeConf = new OutTimeConfig(1000 * 5,
			1000 * 60 * 5, 1000 * 60 * 5);

	private static ResourceBundle bdl = null;
	private static String supplierId = "";
	private static int day;
	private static String uri;

	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("param");
		supplierId = bdl.getString("supplierId");
		day = Integer.valueOf(bdl.getString("day"));
		uri = bdl.getString("uri");
	}

	@Autowired
	private ProductFetchService productFetchService;
	@Autowired
	private ProductSearchService productSearchService;

	public void fechAndSave() {
		try {

			Date startDate, endDate = new Date();
			startDate = DateTimeUtil.getAppointDayFromSpecifiedDay(endDate, day
					* -1, "D");
			// 获取原有的SKU 仅仅包含价格和库存
			Map<String, SkuDTO> skuDTOMap = new HashedMap();
			try {
				skuDTOMap = productSearchService
						.findStockAndPriceOfSkuObjectMap(supplierId, startDate,
								endDate);
			} catch (ServiceException e) {
				e.printStackTrace();
			}

			List<Item> items = readLocalCSV(Item.class, ';');
			List<String> pics = new ArrayList<String>();
			if (items.size() > 0) {
				for (Item item : items) {
					try{
						
						SkuDTO sku = new SkuDTO();
						sku.setSkuId(item.getSupplierSkuNo());
						sku.setSpuId(item.getSopProductName());
						sku.setId(UUIDGenerator.getUUID());
						sku.setSupplierId(supplierId);
						sku.setNewMarketPrice(item.getNewMarkerPrice());
						sku.setNewSalePrice(item.getNewSallPrice());
						sku.setNewSupplierPrice(item.getNewSupplierPrice());
						sku.setMarketPrice(item.getMarkerPrice());
						sku.setSalePrice(item.getSallPrice());
						sku.setSupplierPrice(item.getSupplierPrice());
						sku.setProductCode(item.getBarCode());
						sku.setColor(item.getProductColor());
						sku.setProductDescription(item.getPcDesc());
						sku.setProductSize(item.getProductSize());
						sku.setStock(item.getStock());
						sku.setSaleCurrency(item.getCurrency());
						
						// sku入库
						try {
							if (skuDTOMap.containsKey(sku.getSkuId())) {
								skuDTOMap.remove(sku.getSkuId());
							}
							productFetchService.saveSKU(sku);

						} catch (ServiceException e) {
							try {
								if (e.getMessage().equals("数据插入失败键重复")) {
									// 更新价格和库存
									productFetchService.updatePriceAndStock(sku);
								} else {
									e.printStackTrace();
								}
							} catch (ServiceException e1) {
								logError.error(e1.getMessage());
								e1.printStackTrace();
							}
						}

						pics.add(item.getProductUrl1());
						pics.add(item.getProductUrl2());
						pics.add(item.getProductUrl3());
						pics.add(item.getProductUrl4());
						pics.add(item.getProductUrl5());
						pics.add(item.getProductUrl6());
						pics.add(item.getProductUrl7());
						pics.add(item.getProductUrl8());
						productFetchService.savePicture(supplierId,
								null, sku.getSkuId(), pics);

						SpuDTO spu = new SpuDTO();
						spu.setId(UUIDGenerator.getUUID());
						spu.setSpuId(item.getSopProductName());
						spu.setSupplierId(supplierId);
						spu.setCategoryGender(item.getXingbie());
						spu.setCategoryName(item.getCategoryName());
						spu.setBrandName(item.getBrandName());					
						spu.setMaterial(item.getMaterial());
						spu.setSeasonName(item.getSession());
						spu.setProductOrigin(item.getProductOrigin());					
						// spu 入库
						try {
							productFetchService.saveSPU(spu);
						} catch (ServiceException e) {
							logError.error(e.getMessage());
							try {
								productFetchService.updateMaterial(spu);
							} catch (ServiceException ex) {
								logError.error(ex.getMessage());
								ex.printStackTrace();
							}

							e.printStackTrace();
						}
						
					}catch(Exception ex){
						ex.printStackTrace();
						logError.error(ex);
					}
					
				}

			}

			// 更新网站不再给信息的老数据
			for (Iterator<Map.Entry<String, SkuDTO>> itor = skuDTOMap
					.entrySet().iterator(); itor.hasNext();) {
				Map.Entry<String, SkuDTO> entry = itor.next();
				if (!"0".equals(entry.getValue().getStock())) {// 更新不为0的数据
																// 使其库存为0
					entry.getValue().setStock("0");
					try {
						productFetchService.updatePriceAndStock(entry
								.getValue());
					} catch (ServiceException e) {
						e.printStackTrace();
					}
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

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

	
	
//	public static void main(String[] args){
//		FechProduct ff = new FechProduct();
//		ff.fechAndSave();
//	}
	
}