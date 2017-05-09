package com.shangpin.iog.giglio.stock;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.giglio.stock.schedule.AppContext;

@Component("grabStockImp")
public class GrabStockImp extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");

    public Map<String, String> grabStock(Collection<String> skuNos) throws ServiceException {
        Map<String, String> skuStock = new HashMap<>(skuNos.size());
        Map<String, String> stockMap = new HashMap<>();
        try {
            logger.info("拉取GIGLIO数据开始");
            OutTimeConfig timeConfig =new OutTimeConfig(1000*60*60,1000*60*60,1000*60*60);
            String result = HttpUtil45.get("http://www.giglio.com/feeds/shangpin.csv", timeConfig, null);
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
                    logger.info("skuId : " + skuId + ", stock : " + stock);
                    stockMap.put(skuId, stock);
                    System.out.println("skuId : " + skuId + ", stock : " + stock);
                }
            } finally {
                reader.close();
            }
            for (String skuNo : skuNos) {
                if (stockMap.containsKey(skuNo)) {
                    try {
                        skuStock.put(skuNo, stockMap.get(skuNo));
                    } catch (NumberFormatException e) {
                        skuStock.put(skuNo, "0");
                    }
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

    @SuppressWarnings("unused")
	private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
	
	public static void main(String[] args){
		loadSpringContext();		
	}

}
