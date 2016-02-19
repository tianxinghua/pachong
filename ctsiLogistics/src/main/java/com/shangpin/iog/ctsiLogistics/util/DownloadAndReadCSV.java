package com.shangpin.iog.ctsiLogistics.util;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import com.csvreader.CsvReader;
import com.shangpin.iog.ctsiLogistics.dao.Item;

/**
 * Created by monkey on 2015/10/29.
 */
public class DownloadAndReadCSV {
    static ResourceBundle bundle = ResourceBundle.getBundle("conf") ;
    private static String path = bundle.getString("localPath");

    /**
     * http下载csv文件到本地路径
     * @throws MalformedURLException
     */
    public static List<String> downloadNet() {
    	
    	 List<String> returnList = new ArrayList<String>();
        FTPClient ftpClient = new FTPClient();
        String hostName = "114.142.223.126";
        String userName = "qbcftp";
        String password = "QBC1@1@Ftp";
        String remoteDir = "/Output";
//        private static String HOST="114.142.223.126",PORT="21",USER="qbcftp",PASSWORD="QBC1@1@Ftp",FILE_PATH="/Output/";
        try {
          ftpClient.connect(hostName, 21);
          ftpClient.setControlEncoding("UTF-8");
          ftpClient.login(userName, password);
          ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
          FTPFile[] files = ftpClient.listFiles(remoteDir);
          for (int i = 0; i < files.length; i++) {
        	  String fileName = files[i].getName();
        	  returnList.add(fileName);
        	  System.out.println(files[i].getName());
        	  File file = new File(path+fileName);
              FileOutputStream fos = new FileOutputStream(file);
              ftpClient.retrieveFile(remoteDir + "/"+fileName, fos);
              fos.close();
          }
          
        } catch (SocketException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
        return returnList;
    }
    public static <T> T fillDTO(T t,List<String> data){
    	try {
			Field[] fields = t.getClass().getDeclaredFields();
			for (int i = 0; i < data.size(); i++) {
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
    public static <T> List<T> readLocalCSV(Class<T> clazz) throws Exception {
//    	   List<String> realPaths = new ArrayList<String>();
//    	   realPaths.add("D://t22222.txt");
    	   
    	List<String> realPaths=downloadNet();
        String rowString = null;
        List<T> dtoList = new ArrayList<T>();
        //Set<T> dtoSet = new HashSet<T>();
    	String[] split = null;
    	List<String> colValueList = null;
    	CsvReader cr = null;
    	for (String realPath : realPaths) {
    		//解析csv文件
        	cr = new CsvReader(new FileReader(path+realPath));
    		System.out.println("创建cr对象成功");
    		//得到列名集合
    		cr.readRecord();
    		int a = 0;
    		while(cr.readRecord()){
    			a++;
    			rowString = cr.getRawRecord();
    			if (StringUtils.isNotBlank(rowString)) {
    				split = rowString.split(",");
    				colValueList = Arrays.asList(split);
    				T t = fillDTO(clazz.newInstance(),colValueList);
    				//过滤重复的dto。。。sku,
    				//dtoSet.add(t);
    				dtoList.add(t);
				}
    			System.out.println(a);
    		}
		}
        return dtoList;
    }
  
    public static String getPath(String realpath){
        //Date dt=new Date();
        SimpleDateFormat matter1=new SimpleDateFormat("yyyy-MM-ddHH");
        //String date=matter1.format(dt).replaceAll("-","").trim();
        //realpath = realpath+"_"+date+".csv";
        realpath = realpath+".txt";
        return realpath;
    }
  public static void main(String[] args) {
        try {
        	System.out.println("下载中");
			List<Item> lists = readLocalCSV(Item.class);
            System.out.println("====");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
