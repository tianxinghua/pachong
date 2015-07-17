package com.shangpin.iog.brunarosso.utils;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
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
    public static List<Node> getChildsNodes(Node node) {
        NodeList nodelist = node.getChildNodes();
        List<Node> result = new ArrayList<Node>();
        for (int i = 0; i < nodelist.getLength(); i++) {
            Node child = nodelist.item(i);
            if (child instanceof Text) {
                continue;
            }
            result.add(child);
        }
        return result;
    }
    public static List<org.jdom2.Element> getElement(String path){
       // long lasting = System.currentTimeMillis();
        List allChildren = new ArrayList();
        try {
            SAXBuilder builder = new SAXBuilder();
            org.jdom2.Document doc = builder.build(new File("E:/MailDoc/firma/Prodotti.xml"));
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
    public static void main(String args[]){
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
    }
}
