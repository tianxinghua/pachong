package com.shangpin.iog.biancabianca.util;

import com.csvreader.CsvReader;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.iog.biancabianca.dto.Product;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import org.apache.log4j.Logger;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.*;

/**
 * Created by Administrator on 2015/11/13.
 */
public class MyTxtUtil {
    private static Logger logMongo = Logger.getLogger("mongodb");
    private static ResourceBundle bdl = null;
    private static String httpurl;
    private static String localPath;
    private static String supplierId;
    static {
        if (bdl == null)
            bdl = ResourceBundle.getBundle("conf");
            httpurl = bdl.getString("url");
            localPath = bdl.getString("path");
            supplierId = bdl.getString("supplierId");
    }
    /**
     * http����txtcsv�ļ�������·��
     * @throws MalformedURLException
     */
    public static void txtDownload() throws MalformedURLException {
        String csvFile = HttpUtil45.get(httpurl, new OutTimeConfig(1000*60*10,1000*60*10,1000*60*10), null);
        //memo
        Map<String, String> mongMap = new HashMap<>();
        mongMap.put("supplierId", supplierId);
        mongMap.put("supplierName", "LevelGroup");
 		mongMap.put("result", csvFile);
        //logMongo.info(mongMap);
        //System.out.println(csvFile);
        FileWriter fwriter = null;
        try {
            fwriter = new FileWriter(localPath);
            fwriter.write(csvFile);
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
    }

    /**
     * http����txt�ļ�������·��
     * @throws MalformedURLException
     */
    public static List<Product> readTXTFile() throws Exception {
        //����txt�ļ�
        CsvReader cr = new CsvReader(new FileReader(localPath));
        System.out.println("����cr����ɹ�");
        //�õ���������
        cr.readRecord();
        String rowString = cr.getRawRecord();
        List<Product> dtoList = new ArrayList<Product>();
        Product product = null;
        while(cr.readRecord()) {
            product = new Product();
            rowString = cr.getRawRecord();
            //System.out.println("========================================");
            //System.out.println(rowString+"========================================");
            String[] from = rowString.split("[\t]");
            //System.out.println(from.length + "--1--");
            Field[] to = product.getClass().getDeclaredFields();
            //System.out.println(to.length + "--2--");
            for (int i = 0; i < to.length; i++){
                String name = to[i].getName(); // ��ȡ���Ե�����
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
                Method m = product.getClass().getMethod("set"+name,String.class);
                m.invoke(product,from[i]);
                //System.out.println(name + " : " + from[i]);
            }
            dtoList.add(product);
        }
        return dtoList;
    }


/**
 * test
 * */
    public static void main(String[] args) {

        List<Product> list = null;
        try {
//            MyTxtUtil.txtDownload();
            list = MyTxtUtil.readTXTFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(list.size());
        for (Product p:list){
            if("040827928307789".equals(p.getVARIANT_SKU())){
                System.out.println("spu name = "+ p.getNAME());
                System.out.println(p.getGENDER());
                System.out.println(p.getADVERTISERCATEGORY());
                System.out.println(p.getBUYURL());
                System.out.println(p.getMASTER_SKU());
            }

        }


/*        String json = HttpUtil45.get(httpurl, new OutTimeConfig(1000 * 60 * 10, 10 * 1000 * 60, 10 * 1000 * 60), null);
        System.out.println(json);*/

    }
}
