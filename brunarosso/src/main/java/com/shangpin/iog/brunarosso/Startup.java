package com.shangpin.iog.brunarosso;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.brunarosso.ftp.ReconciliationFtpUtil;
import com.shangpin.iog.brunarosso.service.FetchProduct;
import com.shangpin.iog.brunarosso.utils.XmlReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by sunny on 2015/7/10.
 */
public class Startup {
    private static ApplicationContext factory;
    public static final String PROPERTIES_FILE_NAME = "param";
    static ResourceBundle bundle = ResourceBundle.getBundle(PROPERTIES_FILE_NAME) ;
    private static String path = bundle.getString("path");
    static String localFilePath = bundle.getString("localFilePath");
    static String testFilePath = bundle.getString("testFilePath");
    private static void loadSpringContext()
    {

        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
    private static List<File> read() {
        File f = new File(testFilePath);
        List<File>list=getFileList(f);
        return list;
    }
    private static List<File>readPicPath(){
        File f = new File(testFilePath);
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
        /*String latestPro=XmlReader.readTxt("E:\\latestProXml.txt");
        String updatePro="";*/
        for (File s : filePaths) {
            if (s.isDirectory()) {
                getFileList(s);
            } else {
                if (-1 != s.getName().lastIndexOf(".xml")&&-1!=s.getName().indexOf("Prodotti")) {//Prodotti
                    filePathsList.add(s);
                    //updatePro=s.getName();
                }
            }
        }
        /*try {
            //XmlReader.deleteTxtContent(latestPro);
           // XmlReader.saveAsFileWriter("E:\\latestProXml.txt", updatePro);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return filePathsList;
    }
    private static List<File> getAllFileList(File f) {
        File[] filePaths = f.listFiles();
        List<File> filePathsList = new ArrayList<File>();
        /*String latestPro=XmlReader.readTxt("E:\\latestProXml.txt");
        String updatePro="";*/
        for (File s : filePaths) {
            if (s.isDirectory()) {
                getFileList(s);
            } else {
                if (-1 != s.getName().lastIndexOf(".xml")) {
                    filePathsList.add(s);
                    //updatePro=s.getName();
                }
            }
        }
        return filePathsList;
    }
    private static boolean deleteFile(){
       boolean flag = false;
       File f = new File(testFilePath);
        List<File>list=getAllFileList(f);
        // 路径为文件且不为空则进行删除
        /*if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;*/
        for(int i=0;i<list.size();i++){
            File file = list.get(i);
            try{
                file.delete();
                flag=true;
            }catch (Exception e){
                e.printStackTrace();
                flag=false;
            }
        }
        return flag;
    }
    public static void main(String[] args)
    {
        //加载spring

        loadSpringContext();
        //拉取数据
        boolean flag = true;
        FetchProduct fetchService = (FetchProduct)factory.getBean("brunarosso");
        System.out.println("-------brunarosso start---------");
        ReconciliationFtpUtil.download("","",testFilePath,false);
        Map<String,List<String>> map = XmlReader.getSizeByPath("");
        Map<String,String>returnMap=new HashMap<>();
        try {
            List<File> list1=read();
            for (int i = 0; i < list1.size(); i++) {
                File file=list1.get(i);
                try {
                    //System.out.println("正在读取的文件: "+file.getName());
                    returnMap=fetchService.fetchProductAndSave(map,testFilePath+file.getName());
                } catch (Exception e) {
                    e.printStackTrace();
                    flag=false;
                }

            }
        }catch (Exception e){
            e.printStackTrace();
            flag=false;
        }
        List<File> list=readPicPath();
        for (int i = 0; i < list.size(); i++) {
            System.out.println(i);
            File file = list.get(i);
            try {
                //System.out.println("正在读取的文件: " + file.getName());
                fetchService.savePic(testFilePath + file.getName(),returnMap);
            } catch (Exception e) {
                e.printStackTrace();
                flag=false;
            }
        }
        if(flag){
            flag=deleteFile();
        }
        System.out.println("执行成功："+flag);
        System.out.println("成功插入数据库");

        System.out.println("-------brunarosso end---------");
    }
}
