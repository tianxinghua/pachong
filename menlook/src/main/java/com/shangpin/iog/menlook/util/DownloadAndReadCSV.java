package com.shangpin.iog.menlook.util;

import com.csvreader.CsvReader;
import com.shangpin.iog.menlook.dto.Item;

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

/**
 * Created by monkey on 2015/11/23.
 */
public class DownloadAndReadCSV {
    public static final String PROPERTIES_FILE_NAME = "conf";
    static ResourceBundle bundle = ResourceBundle.getBundle(PROPERTIES_FILE_NAME) ;
    private static String path = bundle.getString("path");
    private static String httpurl = bundle.getString("url");

    /**
     * http下载csv文件到本地路径
     * @throws MalformedURLException
     */
    public static String downloadNet() throws MalformedURLException {
        int bytesum = 0;
        int byteread = 0;

        URL url = new URL(httpurl);
        String realPath="";
        try {
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
            realPath = getPath(path);
            FileOutputStream fs = new FileOutputStream(realPath);

            byte[] buffer = new byte[1204];
            int length;
            while ((byteread = inStream.read(buffer)) != -1) {
                bytesum += byteread;
                System.out.println(bytesum);
                fs.write(buffer, 0, byteread);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  realPath;
    }
    public static List<Item> readLocalCSV() throws Exception {
        
//    	String realPath=downloadNet();
    	String realPath="F:/catalogueCompletLengow-menlook.csv";
        String rowString = null;
        List<Item> dtoList = new ArrayList<Item>();
    	String[] split = null;
    	List<String> colValueList = null;
    	StringBuffer sb = new StringBuffer();
        //解析csv文件
        CsvReader cr = new CsvReader(new FileReader(realPath));
        System.out.println("创建cr对象成功");
        //得到列名集合
        cr.readRecord();
        rowString = cr.getRawRecord();
        split = rowString.split(",");
		List<String> colNameList = Arrays.asList(split);
		//读取每一行，把对应列名的数据填充到FashionestaDTO对象中，放入list集合返回
		//读取每一行信息解析，根据type判断是否是product(configable,simple),分别存入product和item,最后返回List<Product>
		//两次判断
		Item item = null;
		while(cr.readRecord()){
			rowString = cr.getRawRecord();
			if (StringUtils.isNotBlank(rowString)) {
				String replaceAll = rowString.replaceAll(",", "^,");
				
				split = replaceAll.split(",");
				colValueList = Arrays.asList(split);
				item = new Item();
				item.setSpuId(colValueList.get(colNameList.indexOf("id_produit")));
				item.setSkuId(colValueList.get(colNameList.indexOf("ID_unique")));
				item.setProductName(colValueList.get(colNameList.indexOf("titre")));
				item.setProductCode(colValueList.get(colNameList.indexOf("refence_produit_fournisseur")));
				item.setBrand(colValueList.get(colNameList.indexOf("marque")));
				item.setMarketPrice(colValueList.get(colNameList.indexOf("prix_TTC")));
				item.setSupplierPrice(colValueList.get(colNameList.indexOf("sp_price")));
				item.setStock(colValueList.get(colNameList.indexOf("quantite_en_stock")));
				item.setSize(colValueList.get(colNameList.indexOf("taille")));
				item.setColor(colValueList.get(colNameList.indexOf("couleur")));
				item.setCategory(colValueList.get(colNameList.indexOf("nom_categorie_2")));
				item.setSeason(colValueList.get(colNameList.indexOf("collection")));
				item.setMaterial(colValueList.get(colNameList.indexOf("feature_matiere")));
				item.setDescription(colValueList.get(colNameList.indexOf("description_longue")));
				item.setGender("men");
				item.setCurrency("dollor");
				item.setImg(colValueList.get(colNameList.indexOf("image_fix_url")));
				dtoList.add(item);
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
