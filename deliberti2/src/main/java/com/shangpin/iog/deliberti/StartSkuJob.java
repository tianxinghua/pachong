package com.shangpin.iog.deliberti;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.google.gson.Gson;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

/**
 * Created by zhaogenchun on 2015/9/25.
 */
public class StartSkuJob {

    private static Logger log = Logger.getLogger("info");

    private static ApplicationContext factory;
    private static void loadSpringContext() {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
    private static String getJson() {

    	
		String fullFileName = "C:\\Users\\Administrator\\Desktop\\采购异常\\geb.xlsx";

		File file = new File(fullFileName);
		Scanner scanner = null;
		StringBuilder buffer = new StringBuilder();
		try {
			scanner = new Scanner(file, "utf-8");
			while (scanner.hasNextLine()) {
				buffer.append(scanner.nextLine());
			}
		} catch (Exception e) {

		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
		System.out.println(buffer.toString());
		return buffer.toString();
	}

    public static void main(String[] args) throws IOException{
    	String ss = "410100 CWC1G 5812";
    	System.out.println("410100 CWC1G 5812".equals(ss));
    	Map<String,String> map = new HashMap();
    	map.put("dto","hub/20161222144109505.xlsx");
    	
//    	String j = HttpUtil45.post("http://192.168.3.189:9094/hub-task/import-spu", map,new OutTimeConfig(1000*60*20,1000*60*20,1000*60*20));
//    	Gson gson = new Gson();
//    	System.out.println(j);
//    	
//    	HubBaseResponse p = gson.fromJson(j,HubBaseResponse.class);
//    	
//    	byte[] ss =p.getContent();
//    	
//    	 BufferedOutputStream boss = null;  
//         FileOutputStream fos = null;  
//             File dir = new File("C://sssa.xlsx");  
//             fos = new FileOutputStream(dir);  
//             boss = new BufferedOutputStream(fos);  
//             boss.write(ss);  
//             boss.flush();
//             boss.close();
//    	File file = new File("C:\\Users\\Administrator\\Desktop\\采购异常\\GIVENCHYSKU01.xlsx");
    	File file = new File("C:\\Users\\Administrator\\Desktop\\采购异常\\geb.xlsx");
    	 byte[] buffer = null;  
         try  
         {  
             FileInputStream fis = new FileInputStream(file);  
             ByteArrayOutputStream bos = new ByteArrayOutputStream();  
             byte[] b = new byte[1024];  
             int n;  
             while ((n = fis.read(b)) != -1)  
             {  
                 bos.write(b, 0, n);  
             }  
             fis.close();  
             bos.close();  
             buffer = bos.toByteArray();  
         }  
         catch (Exception e)  
         {  
             e.printStackTrace();  
         }  
    	HubImportTaskRequestDto s = new HubImportTaskRequestDto();
    	s.setCreateUser("商品");
    	s.setFileName("商品批量导入模板更新20161209.xlsx");
    	s.setUploadfile(buffer);
    	
    	try {
    		String json1 = HttpUtil45.operateData("post","json","http://127.0.0.1:8003/pending-task/import-sku",new OutTimeConfig(1000*60*20,1000*60*20,1000*60*20),null,
					new Gson().toJson(s),null,null);
//			String json1 = HttpUtil45.operateData("post","json","http://127.0.0.1:8003/hub-task/import-spu",new OutTimeConfig(1000*60*20,1000*60*20,1000*60*20),null,
//					new Gson().toJson(s),null,null);
			System.out.println(json1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
}
