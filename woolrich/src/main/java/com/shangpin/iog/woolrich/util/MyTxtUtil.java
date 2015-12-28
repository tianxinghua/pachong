package com.shangpin.iog.woolrich.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.json.JSONObject;

import com.csvreader.CsvReader;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.woolrich.dto.TxtDTO;

public class MyTxtUtil {
    private static ResourceBundle bdl = null;
    private static String localPath;
    private static String supplierId;
    private static String detailurls;
    static {
        if (bdl == null)
            bdl = ResourceBundle.getBundle("conf");
        localPath = bdl.getString("path");
        supplierId = bdl.getString("supplierId");
        detailurls = bdl.getString("detailurls");
    }
    //http异常
    private static String getHttpStr(String url){
    	String str = HttpUtil45.get(url, new OutTimeConfig(1000*60*10,1000*60*20,1000*60*20), null);
    	return str;
    }
    /**
     * http下载txtcsv文件到本地路径
     * @throws MalformedURLException
     */
    public static void txtDownload(String url) throws MalformedURLException {
        String csvFile =getHttpStr(url);
        if (csvFile.contains("发生错误异常")) {
        	try {
				Thread.currentThread().sleep(5000l);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        	csvFile =getHttpStr(url);
		}
        FileWriter fwriter = null;
        try {
        	File file = new File(localPath);
        	if (!file.exists()) {
				file.mkdirs();
			}
            fwriter = new FileWriter(getPath());
            fwriter.write(csvFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fwriter.flush();
                fwriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * http下载txt文件到本地路径
     * @throws MalformedURLException
     */
    public static List<TxtDTO> readTXTFile() throws Exception {
        //解析txt文件
        CsvReader cr = new CsvReader(new FileReader(getPath()));
        System.out.println("创建cr对象成功");
        //得到列名集合
        cr.readRecord();
        String rowString = cr.getRawRecord();
        List<TxtDTO> dtoList = new ArrayList<TxtDTO>();
        TxtDTO product = null;
        while(cr.readRecord()) {
            product = new TxtDTO();
            rowString = cr.getRawRecord();
            String[] from = rowString.split("[\t]");
            Field[] to = product.getClass().getDeclaredFields();
            for (int i = 0; i < to.length; i++){
                String name = to[i].getName(); // 获取属性的名字
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
                Method m = product.getClass().getMethod("set"+name,String.class);
                m.invoke(product,from[i]);
            }
            dtoList.add(product);
        }
        return dtoList;
    }
    private static String getPath(){
    	return localPath+"/woolrich.txt";
    }
    
    public static String getOrigin(String sku,String brand){
    	String url = "";
    	if (brand.equalsIgnoreCase("woolrich")) {
    		url = "http://www.woolrich.eu/dw/shop/v15_8/products/"+sku+"/availability?inventory_ids=07&client_id=8b29abea-8177-4fd9-ad79-2871a4b06658";
		}else if(brand.equalsIgnoreCase("aspesi")){
			url = "http://www.aspesi.com/dw/shop/v15_8/products/"+sku+"/availability?inventory_ids=02&client_id=8b29abea-8177-4fd9-ad79-2871a4b06658";
		}else if(brand.equalsIgnoreCase("casadei")){
			url="http://www.casadei.com/dw/shop/v15_8/products/"+sku+"/availability?inventory_ids=05&client_id=8b29abea-8177-4fd9-ad79-2871a4b06658";
		}
        OutTimeConfig timeConfig =new OutTimeConfig(1000*60,1000*60,1000*60);
        String jsonstr = HttpUtil45.get(url,timeConfig,null,null,null);
		if( jsonstr != null && jsonstr.length() >0){
			if (!jsonstr.contains("error")) {
				JSONObject json = JSONObject.fromObject(jsonstr);
				if (!json.isNullObject() && !json.containsKey("fault")) {
					return json.getString("c_madeIn");
				}
			}
		}
        return "";
    }

/**
 * test
 * */
    public static void main(String[] args) {

        List<TxtDTO> list = null;
        try {
            MyTxtUtil.txtDownload("");
            list = MyTxtUtil.readTXTFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(list.size());
        for (TxtDTO p:list){
            System.out.println(p.getGENDER());
        }
        for (TxtDTO p:list){
            System.out.println(p.getADVERTISERCATEGORY());
        }
        for (TxtDTO p:list){
            System.out.println(p.getBUYURL());
        }
        for (TxtDTO p:list){
            System.out.println(p.getMASTER_SKU());
        }
    }
}
