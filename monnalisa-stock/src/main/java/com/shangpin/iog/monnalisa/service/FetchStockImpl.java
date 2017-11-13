package com.shangpin.iog.monnalisa.service;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;

import eu.monnalisa.pf.MonnalisaWSProxy;

/**
 * Created by lizhongren on 2017/2/22.
 */
@Component("monnalisa")
public class FetchStockImpl extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");

    @Override
    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        Map<String, String> stock_map = new HashMap<String, String>();

        String stock;
        for (String skuno : skuNo) {
            stock_map.put(skuno, "0");
            try {
            	logger.info("monnalisa sku:"+skuno);
            	MonnalisaWSProxy proxy = new MonnalisaWSProxy();
            	String[] array = skuno.split("-");
        		eu.monnalisa.pf.GenericResult result= proxy.getDisponibilitaMagazzini(array[0], array[1], array[2], array[3], array[4], null, null, "ecommerce", null);
        		stock = String.valueOf(result.getQuantity());
                logger.info("get skuNo:"+ skuno + " stock :" + stock);
                if(!stock.equals("-1")) {
                	stock_map.put(skuno,stock);
                }
            } catch (Exception e) {
                loggerError.error("拉取失败 "+e.getMessage());
                e.printStackTrace();
                continue;
            }
        }

        logger.info("返回的map大小  stock_map.size======"+stock_map.size());
        return stock_map;
    }
    
    public static void main(String[] args){
    	MonnalisaWSProxy proxy = new MonnalisaWSProxy();
    	try {
    		String sku = "72-110411-0625-6667-12";
    		String[] array = sku.split("-");
    		eu.monnalisa.pf.GenericResult result= proxy.getDisponibilitaMagazzini(array[0], array[1], array[2], array[3], array[4], null, null, "ecommerce", null);
		    System.out.println(result.getQuantity());
    	} catch (RemoteException e) {
			e.printStackTrace();
		}

    }
}
