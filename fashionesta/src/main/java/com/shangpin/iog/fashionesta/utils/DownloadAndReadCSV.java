package com.shangpin.iog.fashionesta.utils;

import com.csvreader.CsvReader;
import com.shangpin.iog.fashionesta.dto.Item;
import com.shangpin.iog.fashionesta.dto.Product;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Created by monkey on 2015/9/28.
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
    public static String downloadNet() {
        int bytesum = 0;
        int byteread = 0;

        String realPath="";
        try {
        	URL url = new URL(httpurl);
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            conn.setConnectTimeout(1000*60*30);
            conn.setReadTimeout(1000*60*120);
            realPath = getPath(path);
            File f = new File(realPath);
            if (!f.exists()) {
				f.createNewFile();
			}
            FileOutputStream fs = new FileOutputStream(f);

            byte[] buffer = new byte[1204];
            int length;
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                System.out.println(bytesum);
                fs.write(buffer, 0, byteread);
            }
        } catch (Exception e) {
        	e.printStackTrace();
        	logger.info("下载失败");
        	System.exit(0);
        }
        return  realPath;
    }
    public static List<Product> readLocalCSV() throws Exception {
        
    	String realPath=downloadNet();
        String rowString = null;
        List<Product> dtoList = new ArrayList<Product>();
    	String[] split = null;
    	List<String> colValueList = null;
    	StringBuffer sb = new StringBuffer();
        //解析csv文件
        CsvReader cr = new CsvReader(new FileReader(realPath));
        System.out.println("创建cr对象成功");
        //得到列名集合
        cr.readRecord();
        rowString = cr.getRawRecord();
        split = rowString.split("\";\"");
		List<String> colNameList = Arrays.asList(split);
		//读取每一行，把对应列名的数据填充到FashionestaDTO对象中，放入list集合返回
		//读取每一行信息解析，根据type判断是否是product(configable,simple),分别存入product和item,最后返回List<Product>
		//两次判断
		Product product = null;
		while(cr.readRecord()){
			rowString = cr.getRawRecord();
			split = rowString.split("\";\"");
			colValueList = Arrays.asList(split);
			//是product
			if (colValueList.get(colNameList.indexOf("type")).equals("configurable")/*&&StringUtils.isNotBlank(colValueList.get(colNameList.indexOf("material1")))*/) {
				product = new Product();
				product.setProductCode(colValueList.get(colNameList.indexOf("\"sku")).replace("\"", ""));
				product.setProductName(colValueList.get(colNameList.indexOf("name")));
				product.setBrand(colValueList.get(colNameList.indexOf("brand")));
				product.setCategory(colValueList.get(colNameList.indexOf("Im_type")));
				product.setColor(colValueList.get(colNameList.indexOf("color")));
				product.setDescription(colValueList.get(colNameList.indexOf("description")));
				product.setGender(colValueList.get(colNameList.indexOf("gender")));
				String[] img = new String[2];
				img[0] = colValueList.get(colNameList.indexOf("image"));
				img[1] = colValueList.get(colNameList.indexOf("small_image"));
				product.setImage_url(img);
				product.setMade(colValueList.get(colNameList.indexOf("country_of_origin")));
				sb.append(colValueList.get(colNameList.indexOf("material1")));
				if (StringUtils.isNotBlank(colValueList.get(colNameList.indexOf("material2")))) {
					sb.append(":").append(colValueList.get(colNameList.indexOf("material_1_percent")))
					  .append(",").append(colValueList.get(colNameList.indexOf("material2")))
					  .append(":").append(colValueList.get(colNameList.indexOf("material_2_percent")));
				}
				if (StringUtils.isNotBlank(colValueList.get(colNameList.indexOf("material3")))) {
					sb.append(",").append(colValueList.get(colNameList.indexOf("material3")))
					  .append(":").append(colValueList.get(colNameList.indexOf("material_3_percent")));
				}
				if (StringUtils.isNotBlank(colValueList.get(colNameList.indexOf("material4")))) {
					sb.append(",").append(colValueList.get(colNameList.indexOf("material4")))
					  .append(":").append(colValueList.get(colNameList.indexOf("material_4_percent")));
				}
				product.setMaterial(sb.toString());
				dtoList.add(product);
				sb.setLength(0);
			}else {
//				if (!colValueList.get(colNameList.indexOf("qty")).equals("0")) {
					Item item = new Item();
					item.setItemCode(colValueList.get(colNameList.indexOf("\"sku")).replace("\"", ""));
					item.setPrice(colValueList.get(colNameList.indexOf("price")));
					item.setSpecial_price(colValueList.get(colNameList.indexOf("special_price")));
					item.setSize(colValueList.get(colNameList.indexOf("size")));
					item.setStock(colValueList.get(colNameList.indexOf("qty")));
					product.getItems().add(item);
//				}
				
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
        try {
        	System.out.println("下载中");
            readLocalCSV();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
