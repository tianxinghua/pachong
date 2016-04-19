package com.shangpin.iog.filippo.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.csvreader.CsvReader;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.filippo.dto.CsvDTO;

/**
 * Created by monkey on 2015/10/20.
 */
public class DownloadAndReadCSV {
    public static final String PROPERTIES_FILE_NAME = "param";
    static ResourceBundle bundle = ResourceBundle.getBundle(PROPERTIES_FILE_NAME) ;
    private static String path = bundle.getString("path");
    private static String httpurl = bundle.getString("url");
    private static Logger logger = Logger.getLogger("info");
    /**
     * http下载csv文件到本地路径
     * @throws MalformedURLException
     */
    public static List<String> downloadNet() {
        int num = 0;
        List<String> returnList = new ArrayList<String>();
        String[] urls = httpurl.split(",");
        OutTimeConfig outTimeConf = new OutTimeConfig(1000*60*10, 1000*60*120, 1000*60*120);
		for (String hurl : urls) {
			num++;
			System.out.println("开始下载数据"+hurl);
			String string = HttpUtil45.get(hurl, outTimeConf , null);
			String realPath="";
			realPath = getPath(path+num);
			logger.info("开始保存数据");
			System.out.println("开始保存数据");
			File file = new File(realPath);
			if (!file.exists()) {
				try {
					file.getParentFile().mkdirs();
					file.createNewFile();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			FileWriter fwriter = null;
			try {
				fwriter = new FileWriter(realPath);
				fwriter.write(string);
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
			returnList.add(realPath);
		}
        return  returnList;
    }
    public static <T> T fillDTO(T t,List<String> data){
    	CsvDTO product = new CsvDTO();
    	try {
			Field[] fields = t.getClass().getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				
				  String name = fields[i].getName(); // 获取属性的名字
	                name = name.substring(0, 1).toUpperCase() + name.substring(1);
	                Method m = product.getClass().getMethod("set"+name,String.class);
	                m.invoke(product,data.get(i));
				
				fields[i].setAccessible(true);
				fields[i].set(t, data.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
    }
    /**
     * 填充csvDTO
     * @param clazz 要填充的类类型 
     * @param sep csv分隔符
     * @return 填充好的集合
     * @throws Exception
     */
    public static <T> List<T> readLocalCSV(Class<T> clazz,String sep) {
        
    	List<String> realPaths=downloadNet();
        String rowString = null;
        List<T> dtoList = new ArrayList<T>();
        //Set<T> dtoSet = new HashSet<T>();
    	String[] split = null;
    	List<String> colValueList = null;
    	CsvReader cr = null;
    	for (String realPath : realPaths) {
    		//解析csv文件
    		try {
				cr = new CsvReader(new FileReader(realPath));
//    		CsvReader cr = new CsvReader(new FileReader("F:/filippo1.csv"));
				System.out.println("创建cr对象成功");
				cr.readRecord();
				while(cr.readRecord()){
					rowString = cr.getRawRecord();
					split = rowString.split(sep);
					colValueList = Arrays.asList(split);
					T t = fillDTO(clazz.newInstance(),colValueList);
					System.out.println(((CsvDTO)t).getCOMP());
					logger.info(((CsvDTO)t).getCOMP());
					//过滤重复的dto。。。sku,
					//dtoSet.add(t);
					dtoList.add(t);
				}
			} catch (FileNotFoundException e) {
				continue;
			} catch (IOException e) {
				continue;
			} catch (InstantiationException e) {
				logger.error("类型转化异常");
			} catch (IllegalAccessException e) {
				logger.error("类型转化异常");
			}
		}
        return dtoList;
    }
  
    public static String getPath(String realpath){
        //Date dt=new Date();
        SimpleDateFormat matter1=new SimpleDateFormat("yyyy-MM-ddHH");
        //String date=matter1.format(dt).replaceAll("-","").trim();
        //realpath = realpath+"_"+date+".csv";
        realpath = realpath+".csv";
        return realpath;
    }
  public static void main(String[] args) {
  }
}
