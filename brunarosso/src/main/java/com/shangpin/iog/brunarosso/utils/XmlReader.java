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
    /*public static void main(String args[]) {
        Element element = null;
        // 可以使用绝对路径
        File f = new File("E:/MailDoc/firma/Prodotti.xml");
        // documentBuilder为抽象不能直接实例化(将XML文件转换为DOM文件)
        DocumentBuilder db = null;
        DocumentBuilderFactory dbf = null;
        try {
            // 返回documentBuilderFactory对象
            dbf = DocumentBuilderFactory.newInstance();
            // 返回db对象用documentBuilderFatory对象获得返回documentBuildr对象
            db = dbf.newDocumentBuilder();
            // 得到一个DOM并返回给document对象
            Document dt = db.parse(f);
            // 得到一个elment根元素
            element = dt.getDocumentElement();
            // 获得根节点
            System.out.println("根元素：" + element.getNodeName());
            // 获得根元素下的子节点
            NodeList childNodes = element.getChildNodes();
            // 遍历这些子节点
            for (int i = 0; i < childNodes.getLength(); i++) {
                // 获得每个对应位置i的结点
                Node node1 = childNodes.item(i);
                if ("Prodotti".equals(node1.getNodeName())) {
                    // 如果节点的名称为"Prodotti"，则输出Prodotti元素属性type
                    //System.out.println("\r\n找到一篇账号. 所属区域: " + node1.getAttributes().getNamedItem("type").getNodeValue() + ". ");
                    // 获得<Prodotti>下的节点
                    NodeList nodeDetail = node1.getChildNodes();
                    //String resutl=nodeDetail.toString();
                    // 遍历<Prodotti>下的节点
                    for (int j = 0; j < nodeDetail.getLength(); j++) {
                        // 获得<Prodotti>元素每一个节点
                        Node detail = nodeDetail.item(j);
                        if ("ID_ARTICOLO".equals(detail.getNodeName())) // 输出sku
                            System.out.println("sku: " + detail.getTextContent());
                        else if ("SIGLA_STAGIONE".equals(detail.getNodeName())) // 输出SIGLA_STAGIONE
                            System.out.println("季节编码: " + detail.getTextContent());
                        else if ("BRAND".equals(detail.getNodeName())) // 输出BRAND
                            System.out.println("品牌: " + detail.getTextContent());
                        else if ("CODICE_MODELLO".equals(detail.getNodeName())) // 输出spu
                            System.out.println("spu: " + detail.getTextContent());
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }*/
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
                    map.get(element.getChildText("ID_ARTICOLO")).add(element.getChildText("MM_TAGLIA"));
                }else{
                    List<String> valueList = new ArrayList();
                    valueList.add(element.getChildText("MM_TAGLIA"));
                    map.put(element.getChildText("ID_ARTICOLO"),valueList);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
       // return allChildren;
    }
    private static List<String> read() {
        File f = new File(path);
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
            //File file=list.get(i);
            try {
                System.out.println("正在读取的尺寸文件: " + list.get(i));
                getProductSize(path +list.get(i),map);
                //set.addAll(list1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }
    /*public static void main(String args[]){
        List allChildren = new ArrayList();
        //String str = "HORO24KT abbigliamento Disponibile su BRUNAROSSO.COM. Sul nostro negozio on line puoi trovare numerosi altri modelli";
        try {
            SAXBuilder builder = new SAXBuilder();
            org.jdom2.Document doc = builder.build(new File("E:/MailDoc/firma/Prodotti.xml"));
            org.jdom2.Element foo =doc.getRootElement();
            allChildren = foo.getChildren();
            for (int i = 0; i < allChildren.size(); i++) {
                org.jdom2.Element element = (org.jdom2.Element) allChildren.get(i);
                String text = element.getChildText("BRAND");
                String spu = element.getChildText("CODICE_MODELLO");
                System.out.println("品牌名称:" + text);
                System.out.println("SPU：" + text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
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
      /* String url="E:/MailDoc/firma/Disponibilita.xml";
        Map<String,List<String>> map = getSizeByPath(url);
       System.out.println("map数据："+map.get("7731").get(1));*/

    }
}
