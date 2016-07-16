package machine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class MainClient {
	
	private static ResourceBundle bdl=null;
	private static String supplierId = null;
	private static String supplierNo = null;
	private static String supplierName = null;	
	private static String createdby = null;
	private static String profileName = "conf";
	
	static {
        if(null==bdl)
         bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        supplierName = bdl.getString("supplierName");        
        createdby = bdl.getString("createdby");  
        supplierNo = bdl.getString("supplierNo");
        
	}
	
	public static void main(String[] args) {		
		MainClient test = new MainClient();
		//订单
		test.generateService(createdby,profileName,supplierName);
		test.genFile(supplierName);
		//gradle
		test.genGradle(supplierName);
		//resoueces
		test.genResources(supplierName);
		test.genStart(supplierName);		
		test.genSchedule();
	}
	//生成service
	public void generateService(String createdby,String profileName,String supplierName){
		Configuration sfg = new Configuration();
		try {
			sfg.setDirectoryForTemplateLoading(new File("src/main/java/template"));
			Template template = sfg.getTemplate("OrderService.ftl");
			template.setEncoding("UTF-8");
			File f = new File("E://"+supplierName+"-order//src//main//java//com//shangpin//iog//"+supplierName+"//service//OrderService.java");
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
	public void genGradle(String supplierName){
		
		Configuration sfg = new Configuration();
		Map<String,String> rootMap = new HashMap<String, String>();
		rootMap.put("supplierName", supplierName);
		rootMap.put("tmp", "${file.name}");
		File f = null;
		try {					
			f = new File("E://"+supplierName+"-order"+"//build.gradle");				
			sfg.setDirectoryForTemplateLoading(new File("src/main/java/template"));
			Template template = sfg.getTemplate("build.ftl");
			template.setEncoding("UTF-8");
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
	//生成startUp
	public void genStart(String supplierName){
		Configuration sfg = new Configuration();
		try {
			sfg.setDirectoryForTemplateLoading(new File("src/main/java/template"));
			Template template = sfg.getTemplate("StartUp.ftl");
			template.setEncoding("UTF-8");
			File f = new File("E://"+supplierName+"-order//src//main//java/com/shangpin/iog/"+supplierName+"//StartUp.java");
			File f2 = new File("E://"+supplierName+"-order//src//main//java/com/shangpin/iog/"+supplierName+"//dto");
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
		
		Map<String,String> rootMap = new HashMap<String, String>();
		rootMap.put("supplierId", supplierId);
		rootMap.put("supplierNo", supplierNo);
		try {
			
			for (String name : fileName) {
				sfg.setDirectoryForTemplateLoading(new File("src/main/java/template"));
				template = sfg.getTemplate(name+".ftl");
				template.setEncoding("UTF-8");					
				f = new File("E://"+supplierName+"-order"+"//src//main//resources//"+name+".properties");
				rootMap.put("supplierName", supplierName);
				rootMap.put("stock", "-order");					
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
	//生成
	public void genFile(String supplierName){
		Configuration sfg = new Configuration();
		Template template = null;
		File f = null;		
		String[] fileName = new String[]{"classpath","gitignore","project"};
		Map<String,String> rootMap = new HashMap<String, String>();
		try {
			
			for (String name : fileName) {
				sfg.setDirectoryForTemplateLoading(new File("src/main/java/template"));
				template = sfg.getTemplate(name+".ftl");
				template.setEncoding("UTF-8");					
				f = new File("E://"+supplierName+"-order"+"//."+name);
				rootMap.put("supplierName", supplierName);
				rootMap.put("stock", "-order");					
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
		
	public void genSchedule(){
		String dir = "schedule";
		File f = new File("E://"+supplierName+"-order"+"//src//main//java/com/shangpin/iog/"+supplierName+"//"+dir);
		if (!f.exists()) {
			f.mkdirs();
		}
		
		Map<String,String> rootMap = new HashMap<String, String>();
		rootMap.put("supplierName", supplierName);
		rootMap.put("profileName", profileName);
		rootMap.put("supplierId", "${supplierId}");		
		rootMap.put("jobsSchedule", "${jobsSchedule}");
		rootMap.put("jobsScheduleConfirm", "${jobsScheduleConfirm}");
		
		Configuration sfg = new Configuration();
		try {
			sfg.setDirectoryForTemplateLoading(new File("src/main/java/template"));
			Template template = null;			
			template = sfg.getTemplate("Schedule.ftl");					
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
}
