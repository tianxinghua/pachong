package com.shangpin.iog.giglio.service;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.json.JSONObject;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.product.AbsSaveProduct;
/**
 *
 */
@Component("giglio")
public class FetchProduct extends AbsSaveProduct{
	final Logger logger = Logger.getLogger("info");
	@Autowired
	ProductFetchService productFetchService;

	@Autowired
	ProductSearchService productSearchService;

	private static ResourceBundle bdl = null;
	private static String url;

	static {
		if (bdl == null)
			bdl = ResourceBundle.getBundle("conf");
		url = bdl.getString("url");
	}
	public void sendMessage() {
		
	}
	 private static void readLine(String content){
		 long time = System.currentTimeMillis();
	    	File file = new File("/usr/local/ephub/giglio/"+time+".csv");
	    	FileWriter fwriter = null;
	    	   try {
	    	    fwriter = new FileWriter(file);
	    	    fwriter.write(content);
	    	   } catch (Exception ex) {
	    	    ex.printStackTrace();
	    	   } finally {
	    	    try {
	    	     fwriter.flush();
	    	     fwriter.close();
	    	    } catch (Exception ex) {
	    	     ex.printStackTrace();
	    	    }
	    	   }
	    }
	@Override
	public Map<String, Object> fetchProductAndSave() {
		Reader reader = null;
		try{
			OutTimeConfig timeConfig = new OutTimeConfig(1000 * 60 * 60,
					1000 * 60 * 60, 1000 * 60 * 60);
			String result = HttpUtil45.get(url, timeConfig, null);
			readLine(result);
			if(!StringUtils.isEmpty(result) && !HttpUtil45.errorResult.equals(result)){
				CSVFormat csvFileFormat = CSVFormat.EXCEL.withHeader()
						.withDelimiter(';');
				reader = new InputStreamReader(IOUtils.toInputStream(result,"UTF-8"), "UTF-8");
				CSVParser parser = new CSVParser(reader, csvFileFormat);
				int i=0;
				for (final CSVRecord record : parser) {
					JSONObject obj = new JSONObject();
					Map<String,Integer> map = parser.getHeaderMap();
					for(Map.Entry<String, Integer> entry:map.entrySet()){    
					    String key = entry.getKey().trim();
						obj.put(key, record.get(key));
					}   
					supp.setData(obj.toString()); 
					pushMessage(null);
					i++;
					logger.info("当前行数："+i);
				}
			}else{
				logger.info("抓取供应商数据失败。。。。。。。。。。。。");
			}
			
		}catch(Exception ex){
			logger.error("拉去giglio异常："+ex.getMessage(),ex);
		}finally {
			HttpUtil45.closePool();
		}
		
		return null;
	}
	
	public static void main(String[] args) {
//		new FetchProduct().fetchProductAndSave();
		String ss = "idProdotto;idSKU;Codice Prodotto;Marca;Nome Modello;Colore ENG;Descrizione ENG;Materiale ENG;Sesso(unisex, uomo, donna);Categoria;Collezione / Anno;Prezzo Et (€);Prezzo Imp (€);Price to WS (€);Taglie;Foto";
		String[] sss = ss.split(";");
		for(int i = 0;i<sss.length;i++){
			System.out.println(i+"   "+sss[i].trim());
		}
	}

}
