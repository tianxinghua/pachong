/**
 * Created by huxia on 2015/6/10.
 */
package com.shangpin.iog.brunarosso.stock;

import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPTransferType;
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
    private static String HOST="ftp.teenfashion.it",PORT="21",USER="1504604@aruba.it",PASSWORD="7efd422f35",FILE_PATH="/teenfashion.it/public/stockftp";
    static String localFilePath = bundle.getString("localFilePath");
    @Override
    public Map<String, Integer> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        //String url="E:\\brunarosso"+"Disponibilita.xml";
        Map<String,Integer>map=getSizeByPath("");
        Map<String,Integer>returnMap=new HashMap<>();
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
                    returnMap.put(id,map.get(id));
                }else {
                    String id = skuno.replace("½","+");
                    returnMap.put(id,0);
                }
            }
        }
        return returnMap;
    }
    private static List<File> read() {
        File f = new File(localFilePath);
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
                getMap(localFilePath + list.get(i),map);
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



            logger.error("file="+remoteFilePath+"/" +  remoteFileName);
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
                /** 如果文件夹不存在，则创建 */
                if (!attachments.exists())
                {
                    attachments.mkdir();
                }
                for (int i=0;i<files.length;i++) {
                    System.out.println("正在循环的文件"+files[i]);
                    if(files[i].equals("Disponibilita.xml")){
                        ftp.get(localFilePath+"/"+ subLocalfilePath +"/"+files[i].substring(files[i].lastIndexOf("/")+1),files[i]);

                    }
                    //ftp.get(localFilePath+"/"+ subLocalfilePath +"/"+files[i].substring(files[i].lastIndexOf("/")+1),files[i]);
                }
                System.out.println("下载完成");
            }


            logger.error("success");



        } catch (Exception e)
        {
            logger.error("Demo failed", e);

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

    public static void main(String[] args) throws Exception {
        StockClientImp impl = new StockClientImp();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("BRUNAROSSO更新数据库开始");
        downloadStock("","Disponibilita.xml",localFilePath,true);
        impl.updateProductStock("2015071701342", "2015-01-01 00:00", format.format(new Date()));
        logger.info("BRUNAROSSO更新数据库结束");
        System.exit(0);
    }
}
