/**
 * Created by huxia on 2015/6/10.
 */
package com.shangpin.iog.brunarosso.stock;

import com.enterprisedt.net.ftp.FTPClient;
import com.enterprisedt.net.ftp.FTPConnectMode;
import com.enterprisedt.net.ftp.FTPException;
import com.enterprisedt.net.ftp.FTPTransferType;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.app.AppContext;
//import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.common.utils.SendMail;
import com.shangpin.sop.AbsUpdateProductStock;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import org.jdom2.input.SAXBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.io.*;


import java.text.SimpleDateFormat;
import java.util.*;
@Component("brunarossoStock")
public class StockClientImp extends AbsUpdateProductStock{
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    public static final String PROPERTIES_FILE_NAME = "param";
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {
        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }
    static ResourceBundle bundle = ResourceBundle.getBundle(PROPERTIES_FILE_NAME) ;
    private  static  ResourceBundle sopBundle = ResourceBundle.getBundle("sop");
    private static String HOST="ftp.backend.brunarosso.com",PORT="21",USER="backend.brunarosso.com_shang",PASSWORD="1Lt53Vf6",FILE_PATH="/public/stockftp";
    static String localFilePath = bundle.getString("localFilePath");
    static String email = bundle.getString("email");
    @Override
    public Map<String,Integer> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        //String url="E:\\brunarosso"+"Disponibilita.xml";
        Map<String,Integer>map=getSizeByPath("");
        Map<String,Integer>returnMap=new HashMap<>();

        logger.info("供货商库存数量 =" + map.size());

//        Set<String>set=map.keySet();
//        Iterator<String> iterator=set.iterator();
//        while (iterator.hasNext()){

//            String key = iterator.next();

        String id = "";
        for(String skuno:skuNo){
                if(skuno.indexOf("+")>0){
                    skuno=skuno.replace("+","½");
                }
                if(map.containsKey(skuno)){
                     id = skuno.replace("½","+");
                    returnMap.put(id,(map.get(skuno)<0?0:map.get(skuno)));
                }else {
                    id = skuno.replace("½","+");
                    returnMap.put(id,0);
                }
            }
//        }
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
                System.out.println("正在读取库存文件: " + list.get(i));
                getMap(list.get(i).getAbsolutePath(),map);
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
                logger.info("sku = " + element.getChildText("ID_ARTICOLO") + "-" + element.getChildText("MM_TAGLIA") + " quantity =" + Integer.parseInt(element.getChildText("ESI")  ));
                map.put(element.getChildText("ID_ARTICOLO") + "-" + element.getChildText("MM_TAGLIA"),Integer.parseInt(element.getChildText("ESI")));
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
    public static boolean downloadStock(String remoteFilePath,String remoteFileName,String localFilePath,Boolean isFile){
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
            ftp.setTimeout(60*60*1000);
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
//            ftp.chdir(remoteFilePath);

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
                logger.info("文件下载成功");
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
                        logger.info("文件下载成功");
                        break;

                    }
                    //ftp.get(localFilePath+"/"+ subLocalfilePath +"/"+files[i].substring(files[i].lastIndexOf("/")+1),files[i]);
                }
                System.out.println("下载完成");

            }


            logger.error("success");



        } catch (Exception e)
        {
            logger.error("文件下载失败", e);
            return false;

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
        return true;
    }

    //发邮件
    class MailThread implements  Runnable{



        @Override
        public void run() {
            try {
                SendMail.sendGroupMail("smtp.shangpin.com", "chengxu@shangpin.com",
                        "shangpin001", email, "brunarosso下载文件失败",
                        "brunarosso下载文件5次后仍然失败，请检查网络",
                        "text/html;charset=utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
    	//加载spring
        loadSpringContext();
        StockClientImp stockImp = (StockClientImp)factory.getBean("brunarossoStock");
    	String host = sopBundle.getString("HOST");
        String app_key = sopBundle.getString("APP_KEY");
        String app_secret= sopBundle.getString("APP_SECRET");
        if(StringUtils.isBlank(host)||StringUtils.isBlank(app_key)||StringUtils.isBlank(app_secret)){
            logger.error("参数错误，无法执行更新库存");
        }
        //AbsUpdateProductStock impl = new StockClientImp();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        logger.info("BRUNAROSSO更新库存开始");
        boolean bldown = false;
        for(int i=0;i<6;i++){
            if(downloadStock("","Disponibilita.xml",localFilePath,true)){
                   i=5;
                bldown = true;
            }else {
                  Thread.sleep(1000*60);
            }
        }
        if(!bldown){//下载失败
              //发邮件
            loggerError.error("下载文件失败");
            Thread t = new Thread(new StockClientImp().new MailThread());
            t.start();

            System.exit(0);
        }

        try {
        	stockImp.updateProductStock(host,app_key,app_secret,"2015-01-01 00:00",format.format(new Date()));
		} catch (Exception e) {
			loggerError.error("BRUNAROSSO更新库存失败");
			e.printStackTrace();
		}
        logger.info("BRUNAROSSO更新库存结束");
        System.exit(0);
    }
}
