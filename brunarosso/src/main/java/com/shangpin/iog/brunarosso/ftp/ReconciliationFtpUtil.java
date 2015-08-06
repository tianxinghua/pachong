package com.shangpin.iog.brunarosso.ftp;

import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPTransferType;
import com.shangpin.iog.brunarosso.utils.XmlReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;


/**
 *
 * 简单的ftp下载工具
 * 一天用一次，不需要连接池
 * 14-4-29.
 */
public class ReconciliationFtpUtil {
    private static Log log = LogFactory.getLog(ReconciliationFtpUtil.class);
    //private static ResourceBundle bundle =ResourceBundle.getBundle("param", Locale.ENGLISH) ;
    private static String HOST="ftp.teenfashion.it",PORT="21",USER="1504604@aruba.it",PASSWORD="7efd422f35",FILE_PATH="/teenfashion.it/public/stockftp";
    public static final String PROPERTIES_FILE_NAME = "param";
    static ResourceBundle bundle = ResourceBundle.getBundle(PROPERTIES_FILE_NAME) ;
    static String latestProPath = bundle.getString("latestProPath");
    static String latestStockPath = bundle.getString("latestStockPath");
//    static {
//        ResourceBundle bundle= ResourceBundle.getBundle("reconciliationFtp");
//        HOST = bundle.getString("IP");
//        PORT = bundle.getString("PORT");
//        USER = bundle.getString("USER");
//        PASSWORD = bundle.getString("PASSWORD");
//        FILE_PATH = bundle.getString("PATH");
//    }

    public static boolean isDirExist(String dirname,String[] files)
    {
        for (int i=0;i<files.length;i++)
        {
            if (files[i].indexOf("<DIR>")>-1&&files[i].indexOf(dirname)>-1)
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 下载文件
     * 请注意未使用连接池  另外文件类型已知 要么文件夹要么文件  所以不做复杂判断
     * @param remoteFilePath 远端文件路径
     * @param remoteFileName  远端文件名称
     * @param localFilePath  本地文件存放路径
     */

    public static void download(String remoteFilePath,String remoteFileName,String localFilePath,Boolean isFile){

        /** 定义FTPClient便利 */
        FTPClient ftp = null;
        String subLocalfilePath = remoteFilePath;
        try
        {
            /** 创建FTPClient */
            ftp = new FTPClient();
            /** 连接服务器 */
            ftp.setRemoteHost(HOST);

            ftp.setRemotePort(Integer.parseInt(PORT));
            ftp.setTimeout(3600000);
            ftp.connect();

            /** 登陆 */
            ftp.login(USER, PASSWORD);

            /** 连接模式 */
            ftp.setConnectMode(FTPConnectMode.PASV);      //

            /** ASCII方式：只能传输一些如txt文本文件，
             * zip、jpg等文件需要使用BINARY方式
             * */
            //ftp.setType(FTPTransferType.ASCII);
            ftp.setType(FTPTransferType.BINARY);
            if("".equals(remoteFilePath)){
                remoteFilePath = FILE_PATH;
            }else{
                remoteFilePath = FILE_PATH+"/" + remoteFilePath;
            }


            String[]  files = null;
            ftp.chdir(remoteFilePath);

            /** 切换到主目录，并枚举主目录的所有文件及文件夹
             * 包括日期、文件大小等详细信息
             * files = ftp.dir(".")，则只有文件名
             */
//            String[] files = ftp.dir(".", true);



            log.error("file="+remoteFilePath+"/" +  remoteFileName);
            /** 下载文件 */

            if(isFile){//单个文件 zip
                File attachments = new File(localFilePath);
                /** 如果文件夹不存在，则创建 */
                if (!attachments.exists())
                {
                    attachments.mkdir();
                }
                ftp.get(localFilePath+"/"+remoteFileName,"/".equals(remoteFilePath)?remoteFilePath+  remoteFileName:remoteFilePath+ "/"+  remoteFileName);
            }else{//ftp上已解压后的目录
                try {
                    files = ftp.dir(remoteFilePath);
                }catch (Exception e){
                    e.printStackTrace();
                }

                File attachments = new File(localFilePath+"/"+ subLocalfilePath);
                String procontent = XmlReader.readTxt(latestProPath);
                String disContent = XmlReader.readTxt(latestStockPath);
                /** 如果文件夹不存在，则创建 */
                if (!attachments.exists())
                {
                    attachments.mkdir();
                }
                for (int i=0;i<files.length;i++) {
                    System.out.println("正在循环的文件"+files[i]);
                    if(files[i].indexOf("Prodotti")>=0&&files[i].indexOf(".txt")<0){
                        //content=XmlReader.readTxt("E:/latestProXml.txt");
                        try {
                            if(files[i].compareTo(procontent)>0||files[i].equals("Prodotti.xml")) {
                                ftp.get(localFilePath+"/"+ subLocalfilePath +"/"+files[i].substring(files[i].lastIndexOf("/")+1),files[i]);
                                if(files[i].compareTo(procontent)>0){
                                    procontent=files[i];
                                }
                                System.out.println("下载的产品文件"+files[i]);
                            }
                            else{
                                System.out.println("跳过的产品文件" + files[i]);
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }else if(files[i].indexOf("Disponibilita")>=0&&files[i].indexOf(".txt")<0){
                        try {
                            if(files[i].compareTo(disContent)>0||files[i].equals("Disponibilita.xml")){
                                ftp.get(localFilePath+"/"+ subLocalfilePath +"/"+files[i].substring(files[i].lastIndexOf("/")+1),files[i]);
                                if(files[i].compareTo(disContent)>0){
                                    disContent=files[i];
                                }
                                System.out.println("下载的库存文件"+files[i]);
                            }else{
                                System.out.println("跳过的库存文件"+files[i]);
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }else if(files[i].indexOf("Riferimenti")>=0&&files[i].indexOf(".txt")<0){
                        try {
                            if(files[i].equals("Riferimenti.xml")){
                                ftp.get(localFilePath+"/"+ subLocalfilePath +"/"+files[i].substring(files[i].lastIndexOf("/")+1),files[i]);
                                System.out.println("下载的图片文件"+files[i]);
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    //ftp.get(localFilePath+"/"+ subLocalfilePath +"/"+files[i].substring(files[i].lastIndexOf("/")+1),files[i]);
                }
                XmlReader.deleteTxtContent("E:/latestProXml.txt");
                XmlReader.saveAsFileWriter("E:/latestProXml.txt",procontent);
                XmlReader.deleteTxtContent("E:/latestXml.txt");
                XmlReader.saveAsFileWriter("E:/latestXml.txt",disContent);
            }


            log.error("success");



        } catch (Exception e)
        {
            log.error("Demo failed", e);

        }finally {
            /** 断开连接   */
            try {
                if(null!=ftp) ftp.quit();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (FTPException e) {
                e.printStackTrace();
            }
        }

    }
    public static void downloadStock(String remoteFilePath,String remoteFileName,String localFilePath,Boolean isFile){
        /** 定义FTPClient便利 */
        FTPClient ftp = null;
        String subLocalfilePath = remoteFilePath;
        try
        {
            /** 创建FTPClient */
            ftp = new FTPClient();
            /** 连接服务器 */
            ftp.setRemoteHost(HOST);

            ftp.setRemotePort(Integer.parseInt(PORT));
            ftp.setTimeout(3600000);
            ftp.connect();

            /** 登陆 */
            ftp.login(USER, PASSWORD);

            /** 连接模式 */
            ftp.setConnectMode(FTPConnectMode.PASV);      //

            /** ASCII方式：只能传输一些如txt文本文件，
             * zip、jpg等文件需要使用BINARY方式
             * */
            //ftp.setType(FTPTransferType.ASCII);
            ftp.setType(FTPTransferType.BINARY);
            if("".equals(remoteFilePath)){
                remoteFilePath = FILE_PATH;
            }else{
                remoteFilePath = FILE_PATH+"/" + remoteFilePath;
            }


            String[]  files = null;
            ftp.chdir(remoteFilePath);

            /** 切换到主目录，并枚举主目录的所有文件及文件夹
             * 包括日期、文件大小等详细信息
             * files = ftp.dir(".")，则只有文件名
             */
//            String[] files = ftp.dir(".", true);



            log.error("file="+remoteFilePath+"/" +  remoteFileName);
            /** 下载文件 */

            if(isFile){//单个文件 zip
                File attachments = new File(localFilePath);
                /** 如果文件夹不存在，则创建 */
                if (!attachments.exists())
                {
                    attachments.mkdir();
                }
                ftp.get(localFilePath+"/"+remoteFileName,"/".equals(remoteFilePath)?remoteFilePath+  remoteFileName:remoteFilePath+ "/"+  remoteFileName);
            }else{//ftp上已解压后的目录
                try {
                    files = ftp.dir(remoteFilePath);
                }catch (Exception e){
                    e.printStackTrace();
                }

                File attachments = new File(localFilePath+"/"+ subLocalfilePath);
                String procontent = XmlReader.readTxt(latestProPath);
                String disContent = XmlReader.readTxt(latestStockPath);
                /** 如果文件夹不存在，则创建 */
                if (!attachments.exists())
                {
                    attachments.mkdir();
                }
                for (int i=0;i<files.length;i++) {
                    System.out.println("正在循环的文件"+files[i]);
                    if(files[i].indexOf("Disponibilita")>=0&&files[i].indexOf(".txt")<0){
                        try {
                            if(files[i].compareTo(disContent)>0||files[i].equals("Disponibilita.xml")){
                                ftp.get(localFilePath+"/"+ subLocalfilePath +"/"+files[i].substring(files[i].lastIndexOf("/")+1),files[i]);
                                if(files[i].compareTo(disContent)>0){
                                    disContent=files[i];
                                }
                                System.out.println("下载的库存文件"+files[i]);
                            }else{
                                System.out.println("跳过的库存文件"+files[i]);
                            }
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    //ftp.get(localFilePath+"/"+ subLocalfilePath +"/"+files[i].substring(files[i].lastIndexOf("/")+1),files[i]);
                }
                System.out.println("下载完成");
                XmlReader.deleteTxtContent("E:/latestProXml.txt");
                XmlReader.saveAsFileWriter("E:/latestProXml.txt",procontent);
                XmlReader.deleteTxtContent("E:/latestXml.txt");
                XmlReader.saveAsFileWriter("E:/latestXml.txt",disContent);
            }


            log.error("success");



        } catch (Exception e)
        {
            log.error("Demo failed", e);

        }finally {
            /** 断开连接   */
            try {
                if(null!=ftp) ftp.quit();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (FTPException e) {
                e.printStackTrace();
            }
        }
    }
    public static void  main(String[] args) throws Exception{
        ReconciliationFtpUtil.download("", "", "e://tmp", false);
    }
}
