package com.shangpin.iog.brunarosso.utils;
import java.io.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.w3c.dom.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * Created by sunny on 2015/7/15.
 */
public class XmlReader {
    public static final String PROPERTIES_FILE_NAME = "param";
    static ResourceBundle bundle = ResourceBundle.getBundle(PROPERTIES_FILE_NAME) ;
    private static String path = bundle.getString("path");
    static String testFilePath = bundle.getString("testFilePath");
    public static List<org.jdom2.Element> getProductElement(String path){
       // long lasting = System.currentTimeMillis();
        List allChildren = new ArrayList();
        try {
            SAXBuilder builder = new SAXBuilder();
            org.jdom2.Document doc = builder.build(new File(path));
            org.jdom2.Element foo =doc.getRootElement();
            allChildren = foo.getChildren();
            /*for (int i = 0; i < allChildren.size(); i++) {
                org.jdom2.Element element = (org.jdom2.Element) allChildren.get(i);
                String text=element.getChildText("BRAND");
                String spu = element.getChildText("CODICE_MODELLO");
                System.out.println("品牌名称:"+text);
                System.out.println("SPU："+text);
               *//*System.out.println("品牌名称：" + ((org.jdom2.Element) allChildren.get(i)).getAttribute("BRAND"));
                System.out.println("SPU："+ ((org.jdom2.Element) allChildren.get(i)).getAttribute("CODICE_MODELLO"));*//*
                }*/
            } catch (Exception e) {
            e.printStackTrace();
            }
        return allChildren;
    }
    public static List<org.jdom2.Element> getPictureElement(String path){
        List allChildren = new ArrayList();
        try {
            SAXBuilder builder = new SAXBuilder();
            org.jdom2.Document doc = builder.build(new File(path));
            org.jdom2.Element foo =doc.getRootElement();
            allChildren = foo.getChildren();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allChildren;
    }
    public static void getProductSize(String path,Map<String,List<String>>map){
        List<org.jdom2.Element> allChildren = new ArrayList();
        try {
            SAXBuilder builder = new SAXBuilder();
            org.jdom2.Document doc = builder.build(new File(path));//"E:/MailDoc/firma/Disponibilita.xml"
            org.jdom2.Element foo =doc.getRootElement();
            allChildren = foo.getChildren();
            for (org.jdom2.Element element:allChildren){
                if (map.containsKey(element.getChildText("ID_ARTICOLO"))){
                    map.get(element.getChildText("ID_ARTICOLO")).add(element.getChildText("MM_TAGLIA")+","+element.getChildText("BARCODEEAN")+","+element.getChildText("ESI"));
                }else{
                    List<String> valueList = new ArrayList();
                    valueList.add(element.getChildText("MM_TAGLIA")+","+element.getChildText("BARCODEEAN")+","+element.getChildText("ESI"));
                    map.put(element.getChildText("ID_ARTICOLO"),valueList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       // return allChildren;
    }
    private static List<String> read() {
        File f = new File(testFilePath);
        List<String>list=getFileList(f);
        return list;
    }
    private static List<String> getFileList(File f) {
        File[] filePaths = f.listFiles();
        List<String> filePathsList = new ArrayList<String>();
        for (File s : filePaths) {
            if (s.isDirectory()) {
                getFileList(s);
            } else {
                if (-1 != s.getName().lastIndexOf(".xml")&&-1!=s.getName().indexOf("Disponibilita")) {
                    filePathsList.add(s.getName());
                }
            }
        }
        return filePathsList;
    }
    public static Map<String,List<String>> getSizeByPath(String url){
        Map<String,List<String>>map = new HashMap();
        List<String> list=read();
        Set<org.jdom2.Element> set = new HashSet();
        for (int i = 0; i < list.size(); i++) {
            try {
                System.out.println("正在读取的尺寸文件: " + list.get(i));
                getProductSize(testFilePath +list.get(i),map);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        return pzFile.toString();
    }
    public static void deleteTxtContent(String file)throws IOException{
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
    public static void main(String args[]){

    }
}
