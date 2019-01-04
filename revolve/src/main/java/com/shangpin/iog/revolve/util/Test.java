package com.shangpin.iog.revolve.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.csvreader.CsvReader;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

public class Test {
	public static void main(String[] args) {
		try {
			SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			System.out.println(s.format(new Date()));
			String str = HttpUtil45.get("http://tms0.shangpin.com/Delivery/OrderManage/GetPrintDataBySupplierOrderNo?opType=0&supplierOrderNo=2016051208191", new OutTimeConfig(1000 * 60 * 10,1000 * 60 * 20, 1000 * 60 * 20), null);
			String jj = HttpUtil45.post("http://52.79.155.120/shangping/feed/fwrdproducts",new OutTimeConfig(1000*60*30,1000*60*30,1000*60*30));
			readLine(jj);
			InputStream in = new FileInputStream("F:\\head-revolveChinaMarketplace.txt");
			CsvReader csvReader = new CsvReader(in, Charset.forName("utf-8"));
//			CsvReader csvReader = new CsvReader("F:\\head-revolveChinaMarketplace.txt");
			while(csvReader.readRecord()){
				System.out.println(csvReader.getRawRecord());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private static void readLine(String content){
    	File file = new File("C://relvoe.json");
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
}
