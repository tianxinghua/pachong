package com.shangpin.iog.channeladvisor.stock;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
public class GrabStockImp extends AbsUpdateProductStock{

	private static Logger logInfo  = Logger.getLogger("info");
	private static Logger logError = Logger.getLogger("error");
	private static Logger logMongoDB = Logger.getLogger("MongoDB");
	
	private static ResourceBundle bdl = null;
	private static String supplierId = "";
	
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
	}
	
	@Override
	public Map<String, String> grabStock(Collection<String> skuNo)
			throws ServiceException, Exception {
		//通过refresh-token获取新的token
		OutTimeConfig timeConfig = new OutTimeConfig(1000*5, 1000*60 * 5, 1000*60 * 5);
        String access_token = getNewToken(timeConfig);
        //通过新的token获取库存信息
        Map<String, String> skustock = new HashMap<>();
		Map<String,String> stockMap = new HashMap<>();
		
		String url = "https://api.channeladvisor.com/v1/Products?access_token="+access_token+"&";
		while(StringUtils.isNotBlank(url) && !url.equals("null")){
			String content = HttpUtil45.get(url, timeConfig, null);
			JSONObject jsonObj = JSONObject.fromObject(content);
			if(jsonObj.containsKey("error")){
				String newToken = getNewToken(timeConfig);
				url = url.replaceFirst(url.substring(url.indexOf("=")+1, url.indexOf("&")),newToken);
				content = HttpUtil45.get(url, timeConfig, null);
				jsonObj = JSONObject.fromObject(content);
			}
			JSONArray array = jsonObj.getJSONArray("value");
			for(int i=0;i<array.size();i++){
				JSONObject obj = array.getJSONObject(i);
				String skuId = obj.getString("Sku");
				String stock = obj.getString("TotalAvailableQuantity");
				if(StringUtils.isNotBlank(skuId) && StringUtils.isNotBlank(stock) && !stock.equals("0")){
					stockMap.put(skuId, stock);
				}
			}
			url = jsonObj.getString("@odata.nextLink");
		}
		for (String skuno : skuNo) {
            if(stockMap.containsKey(skuno)){
                skustock.put(skuno, stockMap.get(skuno));
            } else{
                skustock.put(skuno, "0");
            }
        }
		
		//System.out.println(stockMap.toString());
		return skustock;
	}
	
	public String getNewToken(OutTimeConfig timeConfig){
		String application_id = "qwmmx12wu7ug39a97uter3dz29jbij3j";
        String shared_secret = "TqMSdN6-LkCFA0n7g7DWuQ";
        Map<String,String> map = new HashMap<>();
        map.put("grant_type","refresh_token");
        map.put("refresh_token", "6Rz4sozjjOFbdazaU_gjnnFwWvfG2VgG9L14kL9tB3w");
        map.put("redirect_uri","https://49.213.13.167:8443/iog/download/code");
        String kk = HttpUtil45.postAuth("https://api.channeladvisor.com/oauth2/token", map, timeConfig,application_id,shared_secret);
        System.out.println("kk = "  + kk);
        return JSONObject.fromObject(kk).getString("access_token");
	}
	
	public static void main(String[] args) {

		AbsUpdateProductStock grabStockImp = new GrabStockImp();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		logInfo.info("channeladvisor更新数据库开始");
		System.out.println("channeladvisor更新数据库开始");
		try {
			grabStockImp.updateProductStock(supplierId, "2015-01-01 00:00",
					format.format(new Date()));
		} catch (Exception e) {
			logError.error(e.getMessage());
			e.printStackTrace();
		}
		logInfo.info("channeladvisor更新数据库结束");
		System.out.println("channeladvisor更新数据库结束");
		System.exit(0);
		
	}
	
}