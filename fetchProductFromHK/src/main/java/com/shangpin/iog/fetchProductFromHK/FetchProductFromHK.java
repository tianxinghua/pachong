package com.shangpin.iog.fetchProductFromHK;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Scanner;
import java.util.logging.Logger;
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
		if("true".equals(falg)){
			productFetchService.saveAllSkuFromHK();
			productFetchService.saveAllSpuFromHK();
			writeGrapDate("false","date.ini");
		}else{
			productFetchService.saveSkuDayFromHK();
			productFetchService.saveSpuDayFromHK();
		}
		
   }

	 private String readFile() throws IOException {

			File file = getConfFile("date.ini");
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
	        String realPath = AbsOrderService.class.getClassLoader().getResource("").getFile();
	        realPath= URLDecoder.decode(realPath, "utf-8");
	        File df = new File(realPath+fileName);//"date.ini"
	        if(!df.exists()){
	            df.createNewFile();
	        }
	        return df;
	    }
}
