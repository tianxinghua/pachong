
package com.shangpin.iog.mcw.service;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jxl.*;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.apache.commons.lang.StringUtils;

import com.csvreader.CsvReader;




/**
 * Created by monkey on 2015/10/29.
 */

public class DownloadAndReadCSV {

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

    public static <T> List<T> readLocalCSV(Class<T> clazz,String sep) throws Exception {
		System.out.println("开始现在文件");
		boolean b = false;
		try {
			//实例化url
			URL url = new URL("http://accuratime.com/feeds/mcw.xls");
			//载入文件到输入流
			BufferedInputStream bis = new BufferedInputStream(url.openStream());
			//实例化存储字节数组
			byte[] bytes = new byte[100];
			OutputStream bos = new FileOutputStream(new File("D://aa.xls"));
			int len;
			while ((len = bis.read(bytes)) > 0) {
				bos.write(bytes, 0, len);
			}
			bis.close();
			bos.flush();
			bos.close();
			//关闭输出流
			b = true;
			System.out.println("文件下载成功");
		} catch (Exception e) {
			//如果图片未找到
			b = false;
		}
		String buffer = "";
		String fileName = "D://aa.xls";

		File file = new File(fileName);
		//设置文件编码
		WritableWorkbook wwb = Workbook.createWorkbook(new File(fileName));
		WritableSheet ws = wwb.createSheet("Test Sheet 1", 0);
		SheetSettings ss = ws.getSettings();
		ss.setPassword("MASTERWH123");
		ss.setProtected(true);


		for(int i=0;i<ws.getRows();i++){
			for(int j = 1;j < 21;j++){
				Cell cell = ws.getCell(j,i);
				buffer += cell.getContents().replaceAll("\n"," ")+",";
			}
			buffer = buffer.substring(0,buffer.lastIndexOf(",")).toString();
			buffer += "\n";
		}
		String savePath = "D://trans.csv";
		File saveCSV = new File(savePath);
		if(!saveCSV.exists()){
			saveCSV.createNewFile();
		}
		BufferedWriter writer = new BufferedWriter(new FileWriter(saveCSV));
		writer.write(buffer);
		writer.close();
		System.out.println("读取文件开始");
   		String realPaths="D://aa.csv";
   		//String realPaths="G:/test1.csv";
        String rowString = null;

        List<T> dtoList = new ArrayList<T>();
    	String[] split = null;
    	List<String> colValueList = null;
    	CsvReader cr = null;
		//解析csv文件
		InputStream is=new FileInputStream(realPaths);
		InputStreamReader reader = new InputStreamReader(is,"UTF-8");
		//cr=new CsvReader(is,Charset.forName("GBK"));
		cr=new CsvReader(reader);
		//得到列名集合
		cr.readRecord();
		int a = 0;
		while(cr.readRecord()){
			a++;
			rowString = cr.getRawRecord();
			System.out.println(rowString);
			if (StringUtils.isNotBlank(rowString)) {
				split = rowString.split(",");
				colValueList = Arrays.asList(split);
				T t = fillDTO(clazz.newInstance(),colValueList);
				dtoList.add(t);
			}
			System.out.println(a);
		}
        return dtoList;
    }
}

