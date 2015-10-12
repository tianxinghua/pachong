package com.shangpin.iog.monnierfreres.stock;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.monnierfreres.dto.Item;
import com.shangpin.iog.monnierfreres.utils.DownloadAndReadCSV;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by 赵根春 on 2015/10/12.
 */
public class StockImp extends AbsUpdateProductStock {

    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");

    public static void main(String[] args) throws Exception {
//    	DownloadAndReadCSV.readLocalCSV();
    }
    @Override
    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        //定义三方
    	Map<String,String> stockMap = new HashMap<String,String>();
    	Map<String,String> map = DownloadAndReadCSV.readLocalCSV();
    	for(String skuId:skuNo){
    		if(skuId.equals(map.get("skuId"))){
    			stockMap.put(skuId,map.get("qty"));
    		}
    	}
        return stockMap;
    }


}
