package com.shangpin.iog.fetchProductFromHK;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Scanner;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.service.ProductFetchService;
@Component("fetchProductFromHK")
public class FetchProductFromHK {
  private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    @Autowired
    private ProductFetchService productFetchService;
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {

        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

	public static void main(String[] args) {
		
		loadSpringContext();
		FetchProductFromHK o =(FetchProductFromHK)factory.getBean("fetchProductFromHK");
		try {
			o.fetchProductFromHK();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void fetchProductFromHK() throws Exception{
		
//		productFetchService.saveAllSkuFromHK();
		String falg = readFile();
		if("false".equals(falg)){
			System.out.println("false");
			productFetchService.saveSkuDayFromHK();
			productFetchService.saveSpuDayFromHK();
		}else{
			System.out.println("true");
			productFetchService.saveAllSkuFromHK();
			productFetchService.saveAllSpuFromHK();
			writeGrapDate("false","init.ini");

		}
		
   }

	 private String readFile() throws IOException {

		 File file = getConfFile("init.ini");
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
		return buffer.toString();
	}

	private static void writeGrapDate(String date,String fileName){
	        File df;
	        try {
	            df = getConfFile(fileName);

	            try(BufferedWriter bw = new BufferedWriter(new FileWriter(df))){
	                bw.write(date);
	            }
	        } catch (IOException e) {
//	            logger.error("写入日期配置文件错误");
	        }
	    }
	 private static File getConfFile(String fileName) throws IOException {
	        String realPath = FetchProductFromHK.class.getClassLoader().getResource("").getFile();
	        realPath= URLDecoder.decode(realPath, "utf-8");
	        File df = new File(realPath+fileName);//"date.ini"
	        if(!df.exists()){
	            df.createNewFile();
	        }
	        return df;
	    }
}
