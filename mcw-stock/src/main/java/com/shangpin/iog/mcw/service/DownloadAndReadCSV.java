
package com.shangpin.iog.mcw.service;

import com.csvreader.CsvReader;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.record.crypto.Biff8EncryptionKey;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;




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
<<<<<<< Updated upstream
		System.out.println("开始现在文件");
		boolean b = false;
		try {
			//实例化url
			URL url = new URL("http://accuratime.com/feeds/mcw.xls");
			//载入文件到输入流
			BufferedInputStream bis = new BufferedInputStream(url.openStream());
			//实例化存储字节数组
			byte[] bytes = new byte[100];
			OutputStream bos = new FileOutputStream(new File("/usr/local/appstock/aa.xls"));
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
			b = false;
		}
		String buffer = "";
		String fileName = "/usr/local/appstock/aa.xls";
=======
>>>>>>> Stashed changes


		File file = new File(fileName);
		POIFSFileSystem fs=new POIFSFileSystem(new FileInputStream(file));
		Biff8EncryptionKey.setCurrentUserPassword("MASTERWH123");
		HSSFWorkbook wb = new HSSFWorkbook(fs);
		HSSFSheet sheet = wb.getSheetAt(0);
		StringBuffer fuffer;
		String savePath = "/usr/local/appstock/mcw.csv";
		File saveCSV = new File(savePath);
		if(!saveCSV.exists()){
			saveCSV.createNewFile();
		}
		BufferedWriter writer = new BufferedWriter(new FileWriter(saveCSV));
		for (int i=0;i<sheet.getLastRowNum()+1;i++){

			for(int j = 0;j < 25+1;j++){

				Cell cell = sheet.getRow(i).getCell(j);


				if (cell==null){
					continue;
				}
					buffer += cell.toString().replaceAll("\n","")+",";
			}
			buffer=buffer+("\r\n");


		}
		writer.write(buffer);
		writer.close();
		System.out.println("读取文件开始");
   		String realPaths="/usr/local/appstock/mcw.csv";
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
			if (StringUtils.isNotBlank(rowString)) {
				split = rowString.split(",");
				colValueList = Arrays.asList(split);
				T t = fillDTO(clazz.newInstance(),colValueList);
				dtoList.add(t);
			}
		}
        return dtoList;
    }
}

