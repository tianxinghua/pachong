package com.shangpin.iog.amfeed.stock.util;

import com.csvreader.CsvReader;
import com.shangpin.iog.amfeed.stock.dto.Product;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public static boolean csvDownload() throws MalformedURLException {
        boolean flag = true;
        String csvFile = HttpUtil45.get(httpurl, new OutTimeConfig(1000*60*10,1000*60*30,1000*60*30), null);
        //System.out.println(csvFile);
        FileWriter fwriter = null;
        try {
            fwriter = new FileWriter(localPath);
            fwriter.write(csvFile);
        } catch (IOException ex) {
            flag = false;
            ex.printStackTrace();
            return flag;
        } finally {
            try {
                fwriter.flush();
                fwriter.close();
            } catch (IOException ex) {
                flag = false;
                ex.printStackTrace();
                return flag;
            }
        }
        return flag;
    }

    /**
     * http下载csv文件到本地路径
     * @throws MalformedURLException
     */
    public static List<Product> readCSVFile2() throws Exception {
        //解析csv文件
        CsvReader cr = new CsvReader(new FileReader(localPath));
        System.out.println("创建cr对象成功");
        //得到列名集合
        cr.readRecord();
        String rowString = cr.getRawRecord();
        //System.out.println(rowString);
        List<Product> dtoList = new ArrayList<Product>();
        Product product = null;
        //Pattern pCells = Pattern.compile("(\"[^\"]*(\"{2})*[^\"]*\")*[^,]*,");
        while(cr.readRecord()) {
            product = new Product();
            rowString = cr.getRawRecord();
            String[] from = rowString.split(",");
            //System.out.println(from.length + "--1--");
            Field[] to = product.getClass().getDeclaredFields();
            //System.out.println(to.length + "--2--");
            for (int i = 0; i < to.length; i++){
                String name = to[i].getName(); // 获取属性的名字
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
                Method m = product.getClass().getMethod("set"+name,String.class);
                String value = "";
                if(i<2){
                    value = from[i];
                    rowString.replace(from[i],"");
                }else if (i<4){
                    value = rowString.substring(0,rowString.indexOf("http:"));
                    rowString.replace(value,"");
                    value = value.substring(value.length()/2);
                }else {
                    //System.out.println("=============");
                    //System.out.println(rowString+"=============");
                    String[] from2 = rowString.substring(rowString.indexOf("http:")).split(",");
                    value = from2[i-4];
                }
                m.invoke(product,value);
                //System.out.println(name + " : " + value);
            }
            dtoList.add(product);
        }
        return dtoList;
    }

    /**
     * 解析csv文件 到一个list中 每个单元个为一个String类型记录，每一行为一个Product。 再将所有的行放到一个总list中
     */
    public static List<Product>
    readCSVFile() throws IOException {
        InputStreamReader fr = new InputStreamReader(new FileInputStream(localPath));
        BufferedReader br = null;

        br = new BufferedReader(fr);
        br.readLine();
        String rec = null;// 一行
        String str;// 一个单元格
        List<List<String>> listFile = new ArrayList<List<String>>();

        List<Product> dtoList = new ArrayList<Product>();
        Product product = null;
        try {
            // 读取一行
            while ((rec = br.readLine()) != null) {
                product = new Product();
                Field[] copyTo = product.getClass().getDeclaredFields();
                Pattern pCells = Pattern.compile("(\"[^\"]*(\"{2})*[^\"]*\")*[^,]*,");
                Matcher mCells = pCells.matcher(rec);
                List<String> cells = new ArrayList<String>();// 每行记录一个list
                int i = 0;
                // 读取每个单元格
                while (mCells.find()) {
                    try {
                        str = mCells.group();
                        str = str.replaceAll("(?sm)\"?([^\"]*(\"{2})*[^\"]*)\"?.*,", "$1");
                        str = str.replaceAll("(?sm)(\"(\"))", "$2");
                        str = deleLastComma(str);
                        str = str.replaceAll("\\+", "");
                        //System.out.println(")(" + str + ")(");
                        String name = copyTo[i++].getName(); // 获取属性的名字
                        name = name.substring(0, 1).toUpperCase() + name.substring(1);
                        Method m = product.getClass().getMethod("set"+name,String.class);
                        m.invoke(product,str);
                        //System.out.println(name+" : "+str);
                        cells.add(str);
                    } catch (Exception e) {
                        e.printStackTrace();
                        continue;

                    }
                }
                listFile.add(cells);
                //手动设值
                product.setCategrory(getLastValue(rec.split(",")));
                dtoList.add(product);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fr != null) {
                fr.close();
            }
            if (br != null) {
                br.close();
            }
        }
        return dtoList;
    }

    private static String getLastValue(String[] strArr){
        return strArr[strArr.length-1].replace("\\r","");
    }
    private static String deleLastComma(String str){
        int lenth = str.length();
        if(lenth>0){
            if (",".equals(str.substring(lenth-1))){
                return str.substring(0,lenth-1);
            }
        }

        return  str;
    }
    /**
     * test
     * */
    public static void main(String[] args) {
        List<Product> list = null;
        try {
            //MyCsvUtil.csvDownload();
            list = MyCsvUtil.readCSVFile();
            System.out.println("asd");
        } catch (Exception e) {
            e.printStackTrace();
        }
/*        System.out.println(list.size());
        for (Product p:list){
            System.out.println(p.getImage1());
        }
        for (Product p:list){
            System.out.println(p.getSku());
        }
        for (Product p:list){
            System.out.println(p.getPrice());
        }
        for (Product p:list){
            System.out.println(p.getCategrory());
        }*/

/*        String json = HttpUtil45.get(httpurl, new OutTimeConfig(1000 * 60 * 10, 10 * 1000 * 60, 10 * 1000 * 60), null);
        System.out.println(json);*/

    }
}
