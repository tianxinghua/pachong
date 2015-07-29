/**
 * Created by huxia on 2015/6/10.
 */
package com.shangpin.iog.brunarosso.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import org.apache.log4j.Logger;

import org.jdom2.input.SAXBuilder;

import java.io.*;


import java.text.SimpleDateFormat;
import java.util.*;

public class StockClientImp extends AbsUpdateProductStock{
    private static Logger logger = Logger.getLogger("info");
    public static final String PROPERTIES_FILE_NAME = "param";
    static ResourceBundle bundle = ResourceBundle.getBundle(PROPERTIES_FILE_NAME) ;
    private static String path = bundle.getString("path");
    static String testFilePath = bundle.getString("testFilePath");
    @Override
    public Map<String, Integer> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        //String url="E:\\brunarosso"+"Disponibilita.xml";
        Map<String,Integer>map=getSizeByPath("");
        Set<String>set=map.keySet();
        Iterator<String> iterator=set.iterator();
        while (iterator.hasNext()){
            String key = iterator.next();
            for(String skuno:skuNo){
                if(skuno.indexOf("+")>0){
                    skuno=skuno.replace("+","½");
                }
                if(key.equals(skuno)){
                    String id = skuno.replace("½","+");
                    map.put(id,map.get(id));
                }
            }
        }
        return map;
    }
    private static List<File> read() {
        File f = new File(path);
        List<File>list=getFileList(f);
        return list;
    }
    private static List<File> getFileList(File f) {
        File[] filePaths = f.listFiles();
        List<File> filePathsList = new ArrayList<File>();
       /* String latest=readTxt("E:\\latestXml.txt");
        //String latestPro = readTxt("E:\\latestProXml.txt");
        String updateStock="";*/
        for (File s : filePaths) {
            if (s.isDirectory()) {
                getFileList(s);
            } else {
                if (-1 != s.getName().lastIndexOf(".xml")&&-1!=s.getName().indexOf("Disponibilita")) {//Disponibilita
                        filePathsList.add(s);
                        /*updateStock=s.getName();*/
                }
            }
        }
        /*try {
            *//*deleteTxtContent(latest);
            saveAsFileWriter("E:\\latestXml.txt",updateStock);*//*
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        return filePathsList;
    }
    public static Map<String,Integer> getSizeByPath(String url){
        Map<String,Integer>map = new HashMap();
        List<File> list=read();
        for (int i = 0; i < list.size(); i++) {
            try {
                System.out.println("正在读取的尺寸文件: " + list.get(i));
                getMap(path + list.get(i),map);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }
    public static Map<String,Integer> getMap(String url, Map<String, Integer> map){
        List<org.jdom2.Element> allChildren = new ArrayList();
        try {
            SAXBuilder builder = new SAXBuilder();
            org.jdom2.Document doc = builder.build(new File(url));//"E:/MailDoc/firma/Disponibilita.xml"
            org.jdom2.Element foo =doc.getRootElement();
            allChildren = foo.getChildren();
            for (org.jdom2.Element element:allChildren){
                map.put(element.getChildText("ID_ARTICOLO")+"-"+element.getChildText("MM_TAGLIA"),Integer.parseInt(element.getChildText("ESI")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
    public static String  readTxt(String path){
        String endcode = "GBK";
        File file = new File(path);
        InputStreamReader in = null;
        StringBuffer pzFile = new StringBuffer();
        String lineText = null;
        try{
            if(file.isFile() && file.exists()){//判断是否有文件
                in = new InputStreamReader(new FileInputStream(file) , endcode);
                BufferedReader buffer = new BufferedReader(in);
                while((lineText = buffer.readLine()) != null){
                    pzFile.append(lineText);
                }
                System.out.println(pzFile);
            }else{
                System.out.println("抱歉哦，没有找到txt文件");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return lineText;
    }
    public static void deleteTxtContent(String file)throws IOException {
        //String file="E:/latestXml.txt";
        FileReader read = new FileReader(file);
        BufferedReader br = new BufferedReader(read);
        StringBuilder con = new StringBuilder();

        while(br.ready() != false){
            con.append(br.readLine());
            con.append("\r\n");
        }
        System.out.println(con.toString());
        con.delete(0, con.length());
        System.out.println("当前内容："+con.toString());
        br.close();
        read.close();
        FileOutputStream fs = new FileOutputStream(file);
        fs.write(con.toString().getBytes());
        fs.close();
    }
    public static void saveAsFileWriter(String file,String content) throws IOException {
        //String file="E:/latestXml.txt";
        FileWriter fwriter = null;
        try {
            fwriter = new FileWriter(file);
            deleteTxtContent(file);
            fwriter.write(content);
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
    public static void main(String[] args) throws Exception {
        StockClientImp impl = new StockClientImp();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("ACANFORA更新数据库开始");
        impl.updateProductStock("2015050800242","2015-01-01 00:00",format.format(new Date()));
        logger.info("ACANFORA更新数据库结束");
        System.exit(0);
    }
}
