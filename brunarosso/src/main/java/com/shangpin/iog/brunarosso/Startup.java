package com.shangpin.iog.brunarosso;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.brunarosso.service.FetchProduct;
import com.shangpin.iog.brunarosso.utils.XmlReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * Created by sunny on 2015/7/10.
 */
public class Startup {
    private static ApplicationContext factory;
    public static final String PROPERTIES_FILE_NAME = "param";
    static ResourceBundle bundle = ResourceBundle.getBundle(PROPERTIES_FILE_NAME) ;
    private static String path = bundle.getString("path");
    private static void loadSpringContext()
    {

        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
    private static List<File> read() {
        File f = new File(path);
        List<File>list=getFileList(f);
        return list;
    }
    private static List<File>readPicPath(){
        File f = new File(path);
        List<File>list = getPicList(f);
        return list;
    }
    private static List<File>getPicList(File f){
        File[] filePaths = f.listFiles();
        List<File> filePathsList = new ArrayList<File>();
        for (File s : filePaths) {
            if (s.isDirectory()) {
                getFileList(s);
            } else {
                if (-1 != s.getName().lastIndexOf(".xml")&&-1!=s.getName().indexOf("Riferimenti")) {
                    filePathsList.add(s);
                }
            }
        }
        return filePathsList;
    }
    private static List<File> getFileList(File f) {
        File[] filePaths = f.listFiles();
        List<File> filePathsList = new ArrayList<File>();
        String latestPro=XmlReader.readTxt("E:\\latestProXml.txt");
        String updatePro="";
        for (File s : filePaths) {
            if (s.isDirectory()) {
                getFileList(s);
            } else {
                if (s.getName().equals("Prodotti.xml")||s.getName().compareTo(latestPro)>0) {
                    filePathsList.add(s);
                    updatePro=s.getName();
                }
            }
        }
        try {
            XmlReader.deleteTxtContent(latestPro);
            XmlReader.saveAsFileWriter("E:\\latestProXml.txt", updatePro);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePathsList;
    }
    public static void main(String[] args)
    {
        //加载spring

        loadSpringContext();
        //拉取数据
        FetchProduct fetchService = (FetchProduct)factory.getBean("brunarosso");
        System.out.println("-------brunarosso start---------");
       /* String latest=XmlReader.readTxt("E:\\latestXml.txt");
        String latestPro=XmlReader.readTxt("E:\\latestProXml.txt");*/
        List<File> list=readPicPath();
        for (int i = 0; i < list.size(); i++) {
            System.out.println(i);
            File file = list.get(i);
            try {
                System.out.println("正在读取的文件: " + file.getName());
                fetchService.savePic(path + file.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //fetchService.savePic("E:\\brunarosso"+"Riferimenti.xml");
        Map<String,List<String>> map = XmlReader.getSizeByPath("");
        try {
            List<File> list1=read();

            for (int i = 0; i < list1.size(); i++) {
                File file=list1.get(i);
                try {
                    System.out.println("正在读取的文件: "+file.getName());
                    fetchService.fetchProductAndSave(map,path+file.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("成功插入数据库");
       /* System.out.println(map.get("77007").get(1));*/
        System.out.println("-------brunarosso end---------");
    }
}
