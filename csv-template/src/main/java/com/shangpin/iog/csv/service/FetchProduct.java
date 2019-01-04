package com.shangpin.iog.csv.service;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import com.google.gson.Gson;
import com.shangpin.iog.csv.service.impl.ProductSaveService;
import com.shangpin.iog.csv.util.DownloadAndReadCSV;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.product.AbsSaveProduct;

@Component("csvFetchProduct")
public class FetchProduct extends AbsSaveProduct{

	private static ResourceBundle bdl = null;
	private static String supplierId;
	private static int day;
	private static  Gson gson;
	private static JSONArray list = null;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		supplierId = bdl.getString("supplierId");
		if(bdl.getString("flag")!=null){
			flag = Boolean.parseBoolean(bdl.getString("flag"));	
		}
		gson = new Gson();
	}
	
	@Autowired()
	@Qualifier("fratinardiServiceImpl")
	ProductSaveService productSaveService;
	
	@Autowired
	ProductSearchService productSearchService;
    public void sendAndSaveProduct(){
		handleData("spu", supplierId, day, null);
	}
	
    @Override
	public Map<String,Object> fetchProductAndSave() {
	
		Map<String,Object> returnMap = null;
		try {
			list = DownloadAndReadCSV.readLocalCSV();
			@SuppressWarnings("unchecked")
			Iterator<JSONObject> ite = list.iterator();
			while(ite.hasNext()){
				JSONObject product =ite.next();
				supp.setData(gson.toJson(product));
				pushMessage(null);
			}
			if(flag){
				returnMap = productSaveService.getProductMap(list);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnMap;
	}

}
