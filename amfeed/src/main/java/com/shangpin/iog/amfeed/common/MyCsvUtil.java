package com.shangpin.iog.amfeed.common;

import com.shangpin.iog.amfeed.dto.Product;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

import com.csvreader.CsvReader;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Administrator on 2015/11/9.
 */
public class MyCsvUtil {
    private static ResourceBundle bdl = null;
    private static String httpurl;
    private static String localPath;
    static {
        if (bdl == null)
            bdl = ResourceBundle.getBundle("conf");
            httpurl = bdl.getString("url");
            localPath = bdl.getString("path");
    }
    /**
     * http下载csv文件到本地路径
     * @throws MalformedURLException
     */
    public static void csvDownload() throws MalformedURLException {
        int bytesum = 0;
        int byteread = 0;

        URL url = new URL(httpurl);
        String realPath=localPath;
        try {
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();
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
    }

    /**
     * http下载csv文件到本地路径
     * @throws MalformedURLException
     */
    public static List<Product> readCSVFile() throws Exception {
        //解析csv文件
        CsvReader cr = new CsvReader(new FileReader(localPath));
        System.out.println("创建cr对象成功");
        //得到列名集合
        cr.readRecord();
        String rowString = cr.getRawRecord();
        System.out.println(rowString);
        List<Product> dtoList = new ArrayList<Product>();
        Product product = null;
        while(cr.readRecord()) {
            product = new Product();
            rowString = cr.getRawRecord();
            Field[] fs = product.getClass().getDeclaredFields();
            for (int i = 0; i < fs.length; i++){
                String name = fs[i].getName(); // 获取属性的名字
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
                Method m = product.getClass().getMethod("set"+name,String.class);
                 m.invoke(product,rowString.split("\";\"")[i]);
            }
            dtoList.add(product);
        }
        return dtoList;
    }
/**
 * test
 * */
    public static void main(String[] args) {
        try {
            new MyCsvUtil().readCSVFile();
        } catch (Exception e) {
            e.printStackTrace();
        }


/*        String json = HttpUtil45.get(httpurl, new OutTimeConfig(1000 * 60 * 10, 10 * 1000 * 60, 10 * 1000 * 60), null);
        System.out.println(json);*/

    }
}
