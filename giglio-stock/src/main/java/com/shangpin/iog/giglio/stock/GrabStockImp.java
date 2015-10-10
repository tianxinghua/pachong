/**
 * GIGLIO更新库存
 * Created by Kelseo on 2015/9/23.
 */
package com.shangpin.iog.giglio.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.apache.log4j.Logger;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GrabStockImp extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");
    private static ResourceBundle bdl = null;
    private static String supplierId;

    static {
        if (bdl == null)
            bdl = ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
    }

    public Map<String, String> grabStock(Collection<String> skuNos) throws ServiceException {
        Map<String, String> skuStock = new HashMap<>(skuNos.size());
        Map<String, String> stockMap = new HashMap<>();

        try {
            logger.info("拉取GIGLIO数据开始");

//            Map<String, String> mongMap = new HashMap<>();


            OutTimeConfig timeConfig =new OutTimeConfig(1000*60*30,1000*60*30,1000*60*30);
            String result = HttpUtil45.get("http://www.giglio.com/feeds/shangpin.csv", timeConfig, null);
            HttpUtil45.closePool();

//            mongMap.put("supplierId", supplierId);
//            mongMap.put("supplierName", "giglio");
//            mongMap.put("result", "stream data.");
//            try {
//                logMongo.info(mongMap);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            CSVFormat csvFileFormat = CSVFormat.EXCEL.withHeader().withDelimiter(';');
            final Reader reader = new InputStreamReader(IOUtils.toInputStream(result, "UTF-8"), "UTF-8");

            int count = 0;
            Pattern pss = Pattern.compile("(.+)\\((\\d+)\\)");
            try (CSVParser parser = new CSVParser(reader, csvFileFormat)) {
                for (final CSVRecord record : parser) {
                    if (record.size() <= 1) {
                        continue;
                    }
                    System.out.println("count : " + ++count);
                    final String skuId = record.get("idSKU");
                    final String sizeStr = record.get("Taglie"); // 尺码(库存)

                    Matcher m = pss.matcher(sizeStr);
                    String stock = null;
                    while (m.find()) {
                        if (m.groupCount() > 1) {
                            stock = m.group(2);
                        }
                    }
                    stockMap.put(skuId, stock);
                    System.out.println("skuId : " + skuId + ", stock : " + stock);
                }
            } finally {
                reader.close();
            }

            for (String skuNo : skuNos) {
                if (stockMap.containsKey(skuNo)) {
                    skuStock.put(skuNo, stockMap.get(skuNo));
                } else {
                    skuStock.put(skuNo, "0");
                }
            }

            logger.info("GIGLIO赋值库存数据成功");
            logger.info("拉取GIGLIO数据成功");
        } catch (Exception e) {
            e.printStackTrace();
            loggerError.error("拉取GIGLIO数据失败---" + e.getMessage());
            throw new ServiceMessageException("拉取GIGLIO数据失败");
        }
        return skuStock;
    }

    public static void main(String[] args) throws Exception {
        AbsUpdateProductStock grabStockImp = new GrabStockImp();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("GIGLIO更新数据库开始");
        grabStockImp.updateProductStock(supplierId, "2015-01-01 00:00", format.format(new Date()));
        logger.info("GIGLIO更新数据库结束");
        System.exit(0);
    }

}
