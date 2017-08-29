package com.shangpin.iog.clothing.service;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;

import Magento.PortType;
import Magento.PortTypeProxy;

/**
 * Created by lizhongren on 2017/2/22.
 */
@Component("clothing")
public class FetchStockImpl extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private PortType portType = new PortTypeProxy();
    private static ResourceBundle bdl = null;
    private static String username="",    Key="";
    static {
        if (null == bdl)
            bdl = ResourceBundle.getBundle("conf");
        username = bdl.getString("username");
        Key = bdl.getString("Key");
    }
    @Override
    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        Map<String, String> stock_map = new HashMap<String, String>();

        String stockXml="",stock;
        String[] skuArray = new String[0];
        for (String skuno : skuNo) {
            stock_map.put(skuno, "0");
            try {
            	String token = portType.login(username, Key);
            	skuArray[0] = skuno;
                Magento.CatalogInventoryStockItemEntity[] entity = portType.catalogInventoryStockItemList(token, skuArray);
                logger.debug("get skuNo:"+ skuno + " result :" + stockXml);
                if(entity!=null&&entity.length!=0){
                	String qty = entity[0].getQty();
                	stock = qty.substring(0,qty.indexOf("."));
                	logger.info("get skuNo:"+ skuno + " stock :" + stock);
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
    	PortType portType = new PortTypeProxy();
        try {
 		String ss = portType.login(username, Key);
 		System.out.println(ss);
 		String[] array = {"7272CP"};
 		Magento.CatalogInventoryStockItemEntity[] entitys = portType.catalogInventoryStockItemList(ss,array);
 		String qty = entitys[0].getQty();
 		System.out.println(qty.substring(0,qty.indexOf(".")));
        } catch (RemoteException e) {
 		e.printStackTrace();
 	}

     }
    
}
