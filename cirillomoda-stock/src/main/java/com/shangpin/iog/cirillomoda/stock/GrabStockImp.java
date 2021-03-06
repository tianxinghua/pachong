/**
 * cirillomoda更新库存
 * Created by Kelseo on 2015/9/23.
 */
package com.shangpin.iog.cirillomoda.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.cirillomoda.stock.dto.Item;
import com.shangpin.iog.cirillomoda.stock.util.CVSUtil;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.*;

@Component("cirillomodaStock")
public class GrabStockImp extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

    private static ResourceBundle bdl = null;
    private static String supplierId;

    static {
        if (bdl == null)
            bdl = ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
    }

    public Map<String, String> grabStock(Collection<String> skuNos) throws ServiceException {
        Map<String, String> skuStock = new HashMap<>();
        Map<String, String> stockMap = new HashMap<>();

        try {
            logger.info("拉取cirillomoda数据开始");

//            Map<String, String> mongMap = new HashMap<>();
            OutTimeConfig timeConfig = new OutTimeConfig(1000*60*20, 1000*60*40,1000*60*40);
//            timeConfig.confRequestOutTime(600000);
//            timeConfig.confSocketOutTime(600000);
            String result = HttpUtil45.get("http://www.cirillomoda.com/maxpho/cirillo_maxpho.csv", timeConfig, null);
//            HttpUtil45.closePool();

//            mongMap.put("supplierId", supplierId);
//            mongMap.put("supplierName", "cirillomoda");
//            mongMap.put("result", "stream data.");
//            try {
//                logMongo.info(mongMap);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            CSVFormat csvFileFormat = CSVFormat.EXCEL.withHeader().withDelimiter(';');
            final Reader reader = new InputStreamReader(IOUtils.toInputStream(result, "UTF-8"), "UTF-8");

            int count = 0;
            String spuId = "";

            try (CSVParser parser = new CSVParser(reader, csvFileFormat)) {
                for (final CSVRecord record : parser) {
                    if (record.size() <= 1) {
                        continue;
                    }

                    String type = record.get("parent/child");

                    if ("parent".equals(type)) { //SPU
                        spuId = record.get("SKU");
                    } else if ("child".equals(type)) { //SKU
                        System.out.println("count : " + ++count);
                        String size = record.get("attribute_size");
                        String stock = record.get("attribute_size:quantity");
                        String skuId =  spuId + "-" + size;
                        stockMap.put(skuId, stock);
                    }
                }

            } finally {
                reader.close();
            }
            
//            List<Item> items = CVSUtil.readCSV(result, Item.class, ';');
//            for(Item item :items){
//            	try {
//					String spuId = "";
//            		String type = item.getParentChild();           		
//                    if ("parent".equals(type)) { //SPU
//                        spuId = item.getSKU();
//                    } else if ("child".equals(type)) { //SKU                       
//                        String size = item.getAttribute_size();
//                        String stock = item.getAttribute_size_quantity();
//                        String skuId =  spuId + "-" + size;
//                        stockMap.put(skuId, stock);
//                    }
//            		
//				} catch (Exception e) {
//					e.printStackTrace();
//					
//				}
//            }

            for (String skuNo : skuNos) {
                if (stockMap.containsKey(skuNo)) {
                    skuStock.put(skuNo, stockMap.get(skuNo));
                } else {
                    skuStock.put(skuNo, "0");
                }
            }            
            logger.info("拉取cirillomoda数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            loggerError.error("拉取cirillomoda数据失败---" + e.getMessage());
            return skuStock;            
        }
        return skuStock;
    }

    public static void main(String[] args) throws Exception {
    	
        GrabStockImp grabStockImp = new GrabStockImp();
        //AbsUpdateProductStock grabStockImp = new GrabStockImp();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("cirillomoda更新数据库开始");
        try {
//        	grabStockImp.updateProductStock(supplierId, "2015-01-01 00:00", format.format(new Date()));
        	
        	grabStockImp.grabStock(null);
		} catch (Exception e) {
			logger.info("cirillomoda更新数据库出错"+e.toString());
			e.printStackTrace();
		}
        logger.info("cirillomoda更新数据库结束");
        System.exit(0);
    }

}
