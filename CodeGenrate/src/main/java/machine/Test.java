package machine;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class Test {
	public static void main(String[] args) {
		String supplierName = "jianzhuangdexiaohuozi";
		Test test = new Test();
		test.generateService(supplierName);
		test.genFile(supplierName);
		test.genGradle(supplierName);
		test.genResources(supplierName);
		test.genStart(supplierName);
		test.genStock(supplierName);
	}
	//生成service
	public void generateService(String supplierName){
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
			template.process(rootMap, out);
		} catch (IOException | TemplateException e) {
			e.printStackTrace();
		}
	}
	//生成gradle
	public void genGradle(String supplierName){
		String[] ddd = new String[]{"","stock"};
		Configuration sfg = new Configuration();
		Map<String,String> rootMap = new HashMap<String, String>();
		rootMap.put("supplierName", supplierName);
		File f = null;
		try {
			for (String string : ddd) {
				if (string.equals("stock")) {
					rootMap.put("stock", "stock");
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
		String[] fileName = new String[]{"log4j","conf"};
		String[] stock = new String[]{"","stock"};
		Map<String,String> rootMap = new HashMap<String, String>();
		
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
		Configuration sfg = new Configuration();
		try {
			sfg.setDirectoryForTemplateLoading(new File("src/main/java/template"));
			Template template = sfg.getTemplate("StockImp.ftl");
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
}
