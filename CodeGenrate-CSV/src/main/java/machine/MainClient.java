package machine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class MainClient {
	
	private static ResourceBundle bdl=null;
	private static ResourceBundle csvbdl=null;
	private static String supplierId = null;
	private static String supplierName = null;
	private static String isHK = null;
	private static String createdby = null;
	private static String profileName = null;
	
	static {
        if(null==bdl)
         bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        supplierName = bdl.getString("supplierName");
        isHK = bdl.getString("isHK");
        createdby = bdl.getString("createdby");
        profileName = bdl.getString("profileName");
        if(null == csvbdl)
        	csvbdl = ResourceBundle.getBundle("csv");        
        
	}
	
	public static void main(String[] args) {	
//		List<Attr> listAttr = new ArrayList<Attr>();
		String[] attrs = new String[20];
		Enumeration<String> enums = csvbdl.getKeys();
		while(enums.hasMoreElements()){
			String key = enums.nextElement();
			if(null != csvbdl.getString(key) && !csvbdl.getString(key).equals("")){ 				
				attrs[Integer.parseInt(csvbdl.getString(key))-1] = key;				
			}			
		}
//		listAttr = Arrays.asList(attrs);
		MainClient test = new MainClient();
		//拉取产品
		test.generateService(createdby,profileName,supplierName);
		test.genFile(supplierName);
		//gradle
		test.genGradle(isHK,supplierName);
		//resoueces
		test.genResources(supplierName);
		test.genStart(supplierName);
		test.genStock(supplierName);
		test.genSchedule();		
		test.genCSV(attrs);
		
	}
	//生成service
	public void generateService(String createdby,String profileName,String supplierName){
		Configuration sfg = new Configuration();
		try {
			sfg.setDirectoryForTemplateLoading(new File("src/main/java/template"));
			Template template = sfg.getTemplate("FetchProduct.ftl");
			template.setEncoding("UTF-8");
			File f = new File("E://"+supplierName+"//src//main//java//com//shangpin//iog//"+supplierName+"//service//FetchProduct.java");
			if (!f.exists()) {
				f.getParentFile().mkdirs();
				f.createNewFile();
			}
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
			Map<String,String> rootMap = new HashMap<String, String>();
			rootMap.put("supplierName", supplierName);
			rootMap.put("profileName",profileName);
			rootMap.put("createdby", createdby);
			template.process(rootMap, out);
		} catch (IOException | TemplateException e) {
			e.printStackTrace();
		}
	}
	//生成gradle
	public void genGradle(String isHK,String supplierName){
		String[] ddd = new String[]{"","stock"};
		Configuration sfg = new Configuration();
		Map<String,String> rootMap = new HashMap<String, String>();
		rootMap.put("supplierName", supplierName);
		File f = null;
		try {
			for (String string : ddd) {
				if (string.equals("stock")) {
					rootMap.put("stock", "stock");
					rootMap.put("isHK", isHK);
					f = new File("E://"+supplierName+"-stock"+"//build.gradle");
				}else{
					rootMap.put("stock", "");
					f = new File("E://"+supplierName+"//build.gradle");
				}
				sfg.setDirectoryForTemplateLoading(new File("src/main/java/template"));
				Template template = sfg.getTemplate("build.ftl");
				template.setEncoding("UTF-8");
				if (!f.exists()) {
					f.getParentFile().mkdirs();
					f.createNewFile();
				}
				Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
				template.process(rootMap, out);
				
			}
		} catch (IOException | TemplateException e) {
			e.printStackTrace();
		}
	}
	//生成startUp
	public void genStart(String supplierName){
		Configuration sfg = new Configuration();
		try {
			sfg.setDirectoryForTemplateLoading(new File("src/main/java/template"));
			Template template = sfg.getTemplate("StartUp.ftl");
			template.setEncoding("UTF-8");
			File f = new File("E://"+supplierName+"//src//main//java/com/shangpin/iog/"+supplierName+"//StartUp.java");
			File f2 = new File("E://"+supplierName+"//src//main//java/com/shangpin/iog/"+supplierName+"//dto");
			if (!f2.exists()) {
				f2.mkdirs();
			}
			if (!f.exists()) {
				f.getParentFile().mkdirs();
				f.createNewFile();
			}
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
			Map<String,String> rootMap = new HashMap<String, String>();
			rootMap.put("supplierName", supplierName);
			rootMap.put("createdby", createdby);
			template.process(rootMap, out);
		} catch (IOException | TemplateException e) {
			e.printStackTrace();
		}
	}
	//生成
	public void genResources(String supplierName){
		Configuration sfg = new Configuration();
		Template template = null;
		File f = null;
		String[] fileName = new String[]{"log4j",profileName};
		String[] stock = new String[]{"","stock"};
		Map<String,String> rootMap = new HashMap<String, String>();
		rootMap.put("supplierId", supplierId);
		rootMap.put("isHK", isHK);
		try {
			for (String sss : stock) {
				for (String name : fileName) {
					sfg.setDirectoryForTemplateLoading(new File("src/main/java/template"));
					template = sfg.getTemplate(name+".ftl");
					template.setEncoding("UTF-8");
					if (sss.equals("stock")) {
						f = new File("E://"+supplierName+"-stock"+"//src//main//resources//"+name+".properties");
						rootMap.put("supplierName", supplierName);
						rootMap.put("stock", "-stock");
					}else {
						f = new File("E://"+supplierName+"//src//main//resources//"+name+".properties");
						rootMap.put("supplierName", supplierName);
						rootMap.put("stock", "");
					}
					if (!f.exists()) {
						f.getParentFile().mkdirs();
						f.createNewFile();
					}
					Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
					template.process(rootMap, out);
				}
				
			}
		} catch (IOException | TemplateException e) {
			e.printStackTrace();
		}
	}
	//生成
	public void genFile(String supplierName){
		Configuration sfg = new Configuration();
		Template template = null;
		File f = null;
		String[] stock = new String[]{"","stock"};
		String[] fileName = new String[]{"classpath","gitignore","project"};
		Map<String,String> rootMap = new HashMap<String, String>();
		try {
			for (String sss : stock) {
				for (String name : fileName) {
					sfg.setDirectoryForTemplateLoading(new File("src/main/java/template"));
					template = sfg.getTemplate(name+".ftl");
					template.setEncoding("UTF-8");
					if (sss.equals("stock")) {
						f = new File("E://"+supplierName+"-stock"+"//."+name);
						rootMap.put("supplierName", supplierName);
						rootMap.put("stock", "-stock");
					}else {
						f = new File("E://"+supplierName+"//."+name);
						rootMap.put("supplierName", supplierName);
						rootMap.put("stock", "");
					}
					if (!f.exists()) {
						f.getParentFile().mkdirs();
						f.createNewFile();
					}
					Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
					template.process(rootMap, out);
				}
				
			}
		} catch (IOException | TemplateException e) {
			e.printStackTrace();
		}
	}
	//生成stock
	public void genStock(String supplierName){
		String[] dirs = new String[]{"dto","stock","util"};
		for (String dir : dirs) {
			File f = new File("E://"+supplierName+"-stock"+"//src//main//java/com/shangpin/iog/"+supplierName+"//"+dir);
			if (!f.exists()) {
				f.mkdirs();
			}
		}
		Map<String,String> rootMap = new HashMap<String, String>();
		rootMap.put("supplierName", supplierName);
		rootMap.put("createdby", createdby);
		rootMap.put("profileName", profileName);
		
		Configuration sfg = new Configuration();
		try {
			sfg.setDirectoryForTemplateLoading(new File("src/main/java/template"));
			Template template = null;
			if(isHK.equals("1")){ 
				template = sfg.getTemplate("StockImpHK.ftl");
			}else{
				template = sfg.getTemplate("StockImp.ftl");
			}			
			template.setEncoding("UTF-8");
			File f = new File("E://"+supplierName+"-stock"+"//src//main//java/com/shangpin/iog/"+supplierName+"//stock//StockImp.java");
			if (!f.exists()) {
				f.getParentFile().mkdirs();
				f.createNewFile();
			} 
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
			template.process(rootMap, out);
		} catch (IOException | TemplateException e) {
			e.printStackTrace();
		}
	}
	
	public void genSchedule(){
		String dir = "schedule";
		File f = new File("E://"+supplierName+"-stock"+"//src//main//java/com/shangpin/iog/"+supplierName+"//"+dir);
		if (!f.exists()) {
			f.mkdirs();
		}
		
		Map<String,String> rootMap = new HashMap<String, String>();
		rootMap.put("supplierName", supplierName);
		rootMap.put("profileName", profileName);
		rootMap.put("supplierId", "${supplierId}");
		rootMap.put("time", "${time}");
		rootMap.put("jobsSchedule", "${jobsSchedule}");
		rootMap.put("HOST", "${HOST}");
		rootMap.put("APP_KEY", "${APP_KEY}");
		rootMap.put("APP_SECRET", "${APP_SECRET}");
		
		Configuration sfg = new Configuration();
		try {
			sfg.setDirectoryForTemplateLoading(new File("src/main/java/template"));
			Template template = null;
			if(isHK.equals("1")){
				template = sfg.getTemplate("ScheduleHK.ftl");
			}else{
				template = sfg.getTemplate("Schedule.ftl");
			}			
			template.setEncoding("UTF-8");
			File fSch = new File(f.getPath()+File.separator+"Schedule.java");
			if (!fSch.exists()) {
//				fSch.getParentFile().mkdirs();
				fSch.createNewFile();
			} 
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fSch)));
			template.process(rootMap, out);
			
			Template template2 = null;			
			template2 = sfg.getTemplate("AppContext.ftl");					
			template2.setEncoding("UTF-8");
			File fApp = new File(f.getPath()+File.separator+"AppContext.java");
			if (!fApp.exists()) {
//				fSch.getParentFile().mkdirs();
				fApp.createNewFile();
			} 
			Writer out1 = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fApp)));
			template2.process(rootMap, out1);
			
		} catch (IOException | TemplateException e) {
			e.printStackTrace();
		}
	}
	
	public void genCSV(String[] attrs){
		Configuration sfg = new Configuration();
		Template template = null;
		File f = null;
		String[] fileName = new String[]{"util//CSVUtil","util//DownLoad","dto//Item"};
		String[] stock = new String[]{"","stock"};
		Map<String,String> rootMap = new HashMap<String, String>();	
		
		for(int i=0;i<attrs.length;i++){
			if(StringUtils.isNotBlank(attrs[i])){ 				
				rootMap.put("s"+String.valueOf(i),attrs[i]); 
			}
			
		}
		
		rootMap.put("supplierName", supplierName);
		try {
			for (String sss : stock) {
				for (String name : fileName) {
					sfg.setDirectoryForTemplateLoading(new File("src/main/java/template"));
					String ftlName = name.substring(name.indexOf("//")+2);
					System.out.println("ftlName===="+ftlName); 
					template = sfg.getTemplate(ftlName+".ftl");
					template.setEncoding("UTF-8");
					if (sss.equals("stock")) {
						f = new File("E://"+supplierName+"-stock"+"//src//main//java/com/shangpin/iog/"+supplierName+"//"+name+".java");						
						rootMap.put("stock", "-stock");
					}else {
						f = new File("E://"+supplierName+"//src//main//java/com/shangpin/iog/"+supplierName+"//"+name+".java");
						rootMap.put("stock", "");
					}
					if (!f.exists()) {
						f.getParentFile().mkdirs();
						f.createNewFile();
					}
					Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
					template.process(rootMap, out);
				}
				
			}
		} catch (IOException | TemplateException e) {
			e.printStackTrace();
		}
	}
	
}
