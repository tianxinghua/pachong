package com.shangpin.iog.spinnaker.stock;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.spinnaker.stock.dto.ResponseDTO;
import com.shangpin.iog.spinnaker.stock.dto.Seasoncode;
import com.shangpin.iog.spinnaker.stock.dto.SeasoncodeList;
import com.shangpin.iog.spinnaker.stock.dto.Stock;

@Component("spinnaker")
public class SpinnakerStockImp extends AbsUpdateProductStock {
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String uri = null;
	private static String DBContext = null;
	private static String key = null;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		uri = bdl.getString("uri");
		DBContext = bdl.getString("DBContext");
		key = bdl.getString("key");
	}

	@Override
	public Map<String, String> grabStock(Collection<String> skuNo)
			throws Exception {
		Map<String, String> stock_map = new HashMap<String, String>();
		Map<String, String> map = getAllStock();
		for (String skuno : skuNo) {
			if (map.containsKey(skuno))
				stock_map.put(skuno, map.get(skuno));
			else {
				stock_map.put(skuno, "0");
			}
		}
		logger.info("返回的map大小  stock_map.size======" + stock_map.size());
		return stock_map;
	}

	private static Map<String, String> getAllStock() throws Exception {
		
		Gson gson = new Gson();
		Map<String, String> map = new HashMap<String, String>();
		String season_json = HttpUtil45.get(uri+"/Myapi/Productslist/GetAllSeasonCode?DBContext="+DBContext+"&key="+key,new OutTimeConfig(120000, 120000, 120000), null);

		SeasoncodeList season_list = gson.fromJson(
				season_json, new TypeToken<SeasoncodeList>() {
				}.getType());

		if ((season_list != null) && (season_list.getResult() != null)) {
			logger.info("获取季节码返回结果：" + season_json);
			return null;
		}
		for (Seasoncode obj : season_list.getSeasonCode()) {
			String code = URLEncoder.encode(obj.getSeasonCode(), "UTF-8");
			String url = uri+"/Myapi/Productslist/GetAllStockForSync?DBContext="+DBContext+"&SeasonCode="+ code + "&typeSync=web&key="+key;
			String stockData = HttpUtil45.get(url, new OutTimeConfig(1200000,
					1200000, 1200000), null);
			if (HttpUtil45.errorResult.equals(stockData)) {
				continue;
			}
			ResponseDTO res = gson.fromJson(stockData,
					new TypeToken<ResponseDTO>() {
					}.getType());
			if (res != null&&!"0".equals(res.getTotalSku())) {
				List<Stock> list = res.getListStockData();
				for (Stock stock : list) {
					if ((stock != null) && (stock.getBarcode() != null)) {
						map.put(stock.getBarcode(), stock.getQty());
					}
				}
			}
		}
		return map;
	}

}