package com.shangpin.iog.smets.util;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;

import com.csvreader.CsvReader;
import com.shangpin.iog.smets.dto.TxtDTO;

public class TXTUtil {
	
	private static Logger log = Logger.getLogger("info");
	/**
	 * 解析csv文件，将其转换为对象
     * @param remoteFileName  远端文件名称 shangpin.csv
	 * @param clazz DTO类
	 * @param sep 分隔符
	 * @return
	 * @throws Exception
	 */
	public static <T> List<T> readLocalCSV(Class<T> clazz, String sep,InputStream in)
			throws Exception {
		List<T> dtoList = new ArrayList<T>();
		if(in == null){
			System.out.println("FTP下载失败！！！！！！！！！！");
			log.error("FTP下载失败！！！！！！！！！！");
		}
		String rowString = null;
		String[] split = null;
		List<String> colValueList = null;
		CsvReader cr = null;
		// 解析csv文件
		//cr = new CsvReader(result);
		
		cr = new CsvReader(in,Charset.forName("unicode"));
		System.out.println("创建cr对象成功");
		// 得到列名集合
//		cr.readRecord();
		int a = 0;
		while (cr.readRecord()) {
			a++;
			rowString = cr.getRawRecord();   
			if (StringUtils.isNotBlank(rowString)) {
				split = rowString.split(sep);
				colValueList = Arrays.asList(split);
				T t = fillDTO(clazz.newInstance(), colValueList);
				dtoList.add(t);
			}
			System.out.println(a);
		}
		in.close();
		return dtoList;
	}
	
	public static <T> T fillDTO(T t,List<String> data){
    	try {
			Field[] fields = t.getClass().getDeclaredFields();
			for (int i = 0; i < data.size(); i++) {
				fields[i].setAccessible(true);
				fields[i].set(t,data.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return t;
    }
	
	
	public static  <T> List<T>  downloadFTP(Class<T> clazz, String sep){
		List<T> dtoList = null;
		FTPClient ftpClient = null;
		InputStream in = null;
        try {  
            ftpClient = new FTPClient();  
            ftpClient.setConnectTimeout(1000*60*30);
            System.out.println("开始连接");
            log.info("开始连接");
            ftpClient.connect("194.154.193.146");// 连接FTP服务器  
            boolean login = ftpClient.login("shangpin", "SmetsXShangpin");
            System.out.println("连接"+login);
            log.info("连接"+login);
			ftpClient.enterLocalPassiveMode();
			ftpClient.changeWorkingDirectory("/Connexion CEGID");
			String[] names = ftpClient.listNames("/Connexion CEGID");
//			String[] names = ftpClient.listNames();
			int wait = 0;
			while(null == names && wait < 20){
				try {					
					names = ftpClient.listNames();					
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					wait ++;
				}
			}
			log.info("获取文件名列表的次数是=============="+wait); 
			String filename = getFileName(names,new Date());
			log.info("本次获取的文件名称是==========="+filename); 
            System.out.println("读取txt");
            ftpClient.setControlEncoding("UTF-8");
            ftpClient.setDataTimeout(1000*60*30);
			in = ftpClient.retrieveFileStream(filename);
			int i = 0;
			while(null == in && i<10){
				in = ftpClient.retrieveFileStream(filename);
				i++;
			}
			log.info("================"+i+"==================="); 
			if(in != null){
				dtoList = readLocalCSV(clazz, sep, in);
			}			
            
        }catch(Exception ex){
        	log.error(ex.toString());
        	ex.printStackTrace();
        }finally{
        	try {
        		if(null != in){
            		in.close();
            	}
        		ftpClient.logout();
        		
			} catch (Exception e) {
				e.printStackTrace();
			}        	
        }
        return dtoList;
	}
	private static String getFileName(String[] names,Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH");
		String dateStr = sdf.format(date);
		System.out.println(dateStr); 
        String file = "";
        for (int i = names.length-1; i>=0; i--) {
        	if (names[i].contains(dateStr)) {
				file = names[i];
				break;
			}
		}
        if (StringUtils.isBlank(file)) {
			file = getFileName(names, new Date(date.getTime()-3600000));
		}
		return file;
	}
	public static void main(String[] args) {
		
////		String aaa= "3000003587604,3000003587611,3000003587628,3000003587635,3000003587642,3000003587659,3000003587666";
//		try {
//			List<TxtDTO> list = TXTUtil.downloadFTP(TxtDTO.class, ";");
//			Short[] needColsNo = new Short[]{0};
//			File file = new File("F://smets.xlsx");
//			List<TxtDTO> dto = new Excel2DTO().toDTO(file, 0, needColsNo, TxtDTO.class);
//			Map<String,String> mmm = new HashMap<String, String>();
//			System.out.println("判断");
//			for (TxtDTO txtDTO : dto) {
//				mmm.put(txtDTO.getId(), "");
//			}
//			
//			for (TxtDTO txtDTO : list) {
//				if (mmm.containsKey(txtDTO.getId())) {
//					System.out.println(txtDTO.getId());
//				}
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
}
