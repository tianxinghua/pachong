package com.shangpin.iog.monnierfreres.utils;

import com.csvreader.CsvReader;
import com.shangpin.iog.monnierfreres.dto.Item;
import com.shangpin.iog.monnierfreres.dto.Product;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

/**
 * Created by monkey on 2015/9/28.
 */
public class DownloadAndReadCSV {
//    public static final String PROPERTIES_FILE_NAME = "param";
//    static ResourceBundle bundle = ResourceBundle.getBundle(PROPERTIES_FILE_NAME) ;
//    private static String path = bundle.getString("path");
//    private static String httpurl = bundle.getString("url");
//
//    /**
//     * http下载csv文件到本地路径
//     * @throws MalformedURLException
//     */
//    public static String downloadNet() throws MalformedURLException {
//        int bytesum = 0;
//        int byteread = 0;
//
//        URL url = new URL(httpurl);
//        String realPath="";
//        try {
//            URLConnection conn = url.openConnection();
//            InputStream inStream = conn.getInputStream();
//            realPath = getPath(path);
//            FileOutputStream fs = new FileOutputStream(realPath);
//
//            byte[] buffer = new byte[1204];
//            int length;
//            while ((byteread = inStream.read(buffer)) != -1) {
//                bytesum += byteread;
//                System.out.println(bytesum);
//                fs.write(buffer, 0, byteread);
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return  realPath;
//    }
//    public static List<Product> readLocalCSV() throws Exception {
//        
//    	String realPath=downloadNet();
//        String rowString = null;
//        List<Product> dtoList = new ArrayList<Product>();
//    	String[] split = null;
//    	List<String> colValueList = null;
//    	StringBuffer sb = new StringBuffer();
//        //解析csv文件
//        CsvReader cr = new CsvReader(new FileReader(realPath));
//        System.out.println("创建cr对象成功");
//        //得到列名集合
//        cr.readRecord();
//        rowString = cr.getRawRecord();
//        split = rowString.split("\"\\|\"");
//		List<String> colNameList = Arrays.asList(split);
//		//读取每一行，把对应列名的数据填充到FashionestaDTO对象中，放入list集合返回
//		//读取每一行信息解析，根据type判断是否是product(configable,simple),分别存入product和item,最后返回List<Product>
//		//两次判断
//		Product product = null;
//		int i=0;
//		while(cr.readRecord()){
//			System.out.println(i);
//			i++;
//			rowString = cr.getRawRecord();
//			split = rowString.split("\"\\|\"");
//			colValueList = Arrays.asList(split);
//			if(i==3746){
//				String d="s";
//			}
//			if(colValueList.size()==colNameList.size()){
//				
//				product = new Product();
//				product.setProductCode(colValueList.get(colNameList.indexOf("product_id")));
//				product.setProductName(colValueList.get(colNameList.indexOf("name")));
//				product.setBrand(colValueList.get(colNameList.indexOf("brand")));
//				product.setCategory(colValueList.get(colNameList.indexOf("type")));
//				product.setSubCategory(colValueList.get(colNameList.indexOf("subtype")));
//				product.setColor(colValueList.get(colNameList.indexOf("color")));
//				product.setDescription(colValueList.get(colNameList.indexOf("description")));
//				String[] img = new String[5];
//				if (StringUtils.isNotBlank(colValueList.get(colNameList.indexOf("image_url_1")))) {
//					img[0] = colValueList.get(colNameList.indexOf("image_url_1"));
//				}
//				if (StringUtils.isNotBlank(colValueList.get(colNameList.indexOf("image_url_2")))) {
//					img[1] = colValueList.get(colNameList.indexOf("image_url_2"));
//				}
//				if (StringUtils.isNotBlank(colValueList.get(colNameList.indexOf("image_url_3")))) {
//					img[2] = colValueList.get(colNameList.indexOf("image_url_3"));
//				}
//				if (StringUtils.isNotBlank(colValueList.get(colNameList.indexOf("image_url_4")))) {
//					img[3] = colValueList.get(colNameList.indexOf("image_url_4"));
//				}
//				if (StringUtils.isNotBlank(colValueList.get(colNameList.indexOf("image_url_5")))) {
//					img[4] = colValueList.get(colNameList.indexOf("image_url_5"));
//				}
//				product.setImage_url(img);
//				product.setMade(colValueList.get(colNameList.indexOf("manufacturer")));
//				product.setMaterial(colValueList.get(colNameList.indexOf("material")));
//				
//				Item item = new Item();
//				item.setItemCode(colValueList.get(colNameList.indexOf("\"sku")).replace("\"", ""));
//				item.setPrice(colValueList.get(colNameList.indexOf("price_before_discount")));
//				item.setSize(colValueList.get(colNameList.indexOf("size")));
//				item.setBarCode(colValueList.get(colNameList.indexOf("ean13")));
//				item.setStock(colValueList.get(colNameList.indexOf("qty")));
//				item.setCurrency(colValueList.get(colNameList.indexOf("price_type")));
//				product.getItems().add(item);
//				//是product
//				dtoList.add(product);
//			}
//		
//
//		}
//		cr.close();
//		File flie = new File(realPath);
//		boolean falg = flie.delete();
//		if(falg){
//			System.out.println("文件删除success");
//		}else{
//			System.out.println("文件删除fail");
//		}
//        return dtoList;
//    }
//	public static String getPath(String realpath){
//		   Date dt=new Date();
//	        SimpleDateFormat matter1=new SimpleDateFormat("yyyy-MM-ddHH");
//	        String date=matter1.format(dt).replaceAll("-","").trim();
//        realpath = realpath+"_"+date+".csv";
//        return realpath;
//    }
//	
}
