package com.shangpin.iog.picture.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.DowmImage;
import com.shangpin.iog.common.utils.queue.PicQueue;
import com.shangpin.iog.dto.SupplierDTO;
import com.shangpin.iog.service.ProductReportService;
import com.shangpin.iog.service.SupplierService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by lizhongren on 2016/8/20.
 */
@Service("pictureDownService")
public class PictureDownService {

    private static org.apache.log4j.Logger loggerInfo = org.apache.log4j.Logger
            .getLogger("info");
    private static org.apache.log4j.Logger loggerError = org.apache.log4j.Logger
            .getLogger("error");

    private static ResourceBundle bdl = null;
    private static String filepath = null;

    private static String smtpHost = null;
    private static String from = null;
    private static String fromUserPassword = null;
    private static String to = null;
    private static String subject = null;
    private static String messageType = null;
    private static String supplier_Id = null;
    private static String startDate = null;
    private static String endDate = null;
    private static String downloadPath = null;
    private static String userName = null;
    private static String password = null;
    private static String excludesupplierId = null;

    private static Map<String,String> supplierUserPassMap= new HashMap<String,String>(){
        {
             put("2016030701799","at98w-IIS,Polo2012");
        }
    };

    private static Map<String,String> supplierAuthType= new HashMap<String,String>(){
        {
            put("2016030701799","NT");
        }
    };

    @Autowired
    ProductReportService productReportService;

    @Autowired
    SupplierService supplierService;

    static {
        if (null == bdl) {
            bdl = ResourceBundle.getBundle("conf");
        }
        smtpHost  = bdl.getString("smtpHost");
        from = bdl.getString("from");
        fromUserPassword = bdl.getString("fromUserPassword");
        to = bdl.getString("to");
        subject = bdl.getString("subject");
        messageType = bdl.getString("messageType");

        filepath = bdl.getString("filepath");

        supplier_Id = bdl.getString("supplierId");
        startDate = bdl.getString("startDate");
        endDate = bdl.getString("endDate");
        downloadPath = bdl.getString("downloadPath");
        userName = bdl.getString("userName");
        password = bdl.getString("password");
        excludesupplierId = bdl.getString("excludesupplierId");

    }
    public void downPic(){
        if("".equals(supplier_Id)) supplier_Id=null;
        //鐢ㄧ殑绾跨▼杩囧 httpclient鐨勮繛鎺ユ睜 涓嶅鐢�鐜版斁鎱㈤�搴�涓�釜渚涜揣鍟嗕竴涓緵璐у晢鐨勬潵鎿嶄綔
        Map excludeSupplierMap = new HashMap();
        if(StringUtils.isNotBlank(excludesupplierId)) {
            String[] supplierArray = excludesupplierId.split(",");
            if(null!=supplierArray){
                for(String supplierId:supplierArray){
                    excludeSupplierMap.put(supplierId,"");
                }
            }

        }
        if(null==supplier_Id) {
            try {

                List<SupplierDTO> supplierDTOs =  supplierService.findByState("1");
                for(SupplierDTO dto:supplierDTOs){
                    if(excludeSupplierMap.containsKey(dto.getSupplierId())) continue;//鎺掗櫎涓嶆媺鍙栫殑
                    downloadPic(dto.getSupplierId());
                }
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }else{
            String[] supplierArray = supplier_Id.split(",");
            for(String supplierId : supplierArray){
                if(excludeSupplierMap.containsKey(supplierId)) continue;//鎺掗櫎涓嶆媺鍙栫殑
                downloadPic(supplierId);
            }

        }
        System.exit(0);

    }

    private void downloadPic(String supplierIdPam) {
        try {
        	loggerInfo.info("供货商:" + supplierIdPam + " 开始下载");
            Map<String,String> picMap = new HashMap<>();
            Map<String,String> supplierDateMap = null;
            //濡傛灉寮�鏃ユ湡涓虹┖ 鍙媺鍙栧綋澶╃殑鏁版嵁
//            if(StringUtils.isBlank(startDate)){
//                startDate = DateTimeUtil.strForDateNew(new Date());
//            }

            supplierDateMap = productReportService.findPicture(picMap,supplierIdPam,startDate,endDate,excludesupplierId);
            if(null!=supplierDateMap&&supplierDateMap.size()>0){
                //鑾峰彇鏃ユ湡
                String key = "",supplierId = "",date= "",spukeyValue = "";
                ThreadPoolExecutor executor = new ThreadPoolExecutor(50, 150, 300, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(100),new ThreadPoolExecutor.CallerRunsPolicy());
                PicQueue picQueue = new PicQueue();
                for(Map.Entry<String,String> supplierDate:supplierDateMap.entrySet()){
                     key = supplierDate.getKey();

//    	String dirPath = "F:/usr/local/picturetem/"+new Date().getTime();
                    supplierId  = key.substring(0,key.indexOf("|"));
                    date = key.substring(key.indexOf("|")+1,key.length());
                    String dirPath =downloadPath  + supplierId +"/" + date;
                    File f1 = new File(dirPath);
                    if (!f1.exists()) {
                        f1.mkdirs();
                    }
                    spukeyValue=supplierDate.getValue();
                    String[] spuArray = spukeyValue.split("\\|\\|\\|\\|\\|");
                    int a = 0;
//                    loggerInfo.info("supplierId="+ supplierId);
                    for(String spu :spuArray){

                        //涓嬭浇淇濆瓨鍥剧墖


                            System.out.println("++++"+a+"++++++");
                            a++;
                            String[] ingArr = picMap.get(spu).split(",");
                            int i = 0;
                            for (String img : ingArr) {



                                if (StringUtils.isNotBlank(img)) {
                                    try {
                                        img=this.changeUrl(supplierId,img.trim()) ;
                                        System.out.println("spu =" + spu + " 　img url  =" + img);
                                        loggerInfo.info("spu =" + spu + " 　img url  =" + img);
                                        i++;
                                        File f = new File(dirPath+"/"+spu+" ("+i+").jpg");
                                        if (f.exists()) {
                                            continue;
                                        }
                                        //鏌愪簺渚涜揣鍟嗙壒娈婂鐞�
                                        if("2015092401528".equals(supplierId)){
                                            //2015092401528 stefania \
                                            // 2015101501608  tony    鏆備笉闇�
                                             DownloadPicTool.downImage(img.trim(),dirPath,spu+" ("+i+").jpg");
                                        }else  if("2016030701799".equals(supplierId)) {   //russocapri
                                        	Thread.sleep(500);
                                        	if(img.contains("82.185.221.164")){                                        		
                                                executor.execute(new DowmImage(img.trim(),spu+" ("+i+").jpg",dirPath,picQueue,null, null,"NT","at98w-IIS","Polo2012"));
                                        	}else{
                                        		executor.execute(new DowmImage(img.trim(),spu+" ("+i+").jpg",dirPath,picQueue,null, null,"","shangpin","Shang2016"));
                                        	}

                                            
                                        }else if("2016083001937".equals(supplierId)){
                                        	Thread.sleep(500);
                                        	executor.execute(new DowmImage(img.trim(),spu+" ("+i+").jpg",dirPath,picQueue,null, null,"","shangpin","Shang1808"));
                                        }else if("2016030901801".equals(supplierId)){
                                        	img = img.trim();
                                        	if(!img.startsWith("http")){
                                        		img = "http://"+img;
                                        	}
                                        	Thread.sleep(500);
                                            executor.execute(new DowmImage(img.trim(),spu+" ("+i+").jpg",dirPath,picQueue,null, null,"",userName,password));
                                        	
                                        }else if ("2016051201892".equals(supplierId)){
                                        	img = img.trim().replaceAll("\r", "").replaceAll("\n", "").trim();
                                        	if(img.contains("|")){
                                        		String[] urls = img.split("\\|");
                                        		for(int j=0;j<urls.length;j++){
                                        			executor.execute(new DowmImage(urls[j].trim(),spu+" ("+(j+1)+").jpg",dirPath,picQueue,null, null,"",userName,password));
                                        		}
                                        	}else{
                                        		executor.execute(new DowmImage(img.trim(),spu+" ("+i+").jpg",dirPath,picQueue,null, null,"",userName,password));
                                        	}
                                        	Thread.sleep(500);
                                        	
                                        }else{
                                            Thread.sleep(500);
                                            executor.execute(new DowmImage(img.trim(),spu+" ("+i+").jpg",dirPath,picQueue,null, null,"",userName,password));
                                        }


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }




                    }
                }
                delay(executor);
                //閲嶆柊涓嬭浇澶辫触鐨�
                String failUrl = "",path="",user="",pass="";
                String[] split = null;
                Map<String,Integer> recordMap = new HashMap<String, Integer>();
                while(executor.getActiveCount()>0||!picQueue.unVisitedUrlsEmpty()){
                    if (picQueue.unVisitedUrlsEmpty()&&executor.getActiveCount()>=0) {
                        System.out.println("============================================都为空======================================================");
                        try {
                            Thread.sleep(1000*10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                    failUrl = picQueue.unVisitEdUrlDeQueue();
                    if(StringUtils.isBlank(failUrl)){
                        continue;
                    }
                    if (recordMap.containsKey(failUrl)) {
                        if (recordMap.get(failUrl)>10) {
                            System.out.println("downloaderror="+failUrl);
//                            loggerInfo.info("downloaderror="+ failUrl);
                            loggerError.error("downloaderror="+ failUrl);
                            continue;
                        }
                        recordMap.put(failUrl, recordMap.get(failUrl)+1);
                    }else{
                        recordMap.put(failUrl, 1);
                    }
                    split = failUrl.split(";");
                    System.out.println("曾经下载失败的 spu =" + split[2] + " 　img url  =" + split[0]);
                    loggerInfo.info("曾经下载失败的 spu =" + split[2] + " 　img url  =" + split[0]);
                    path = split[1].substring(0,split[1].lastIndexOf("/"));
                    supplierId = path.substring(path.lastIndexOf("/")+1,path.length());
                    if(supplierUserPassMap.containsKey(supplierId)){
                        user =   (supplierUserPassMap.get(supplierId).split(","))[0];
                        pass =   (supplierUserPassMap.get(supplierId).split(","))[1];
                        executor.execute(new DowmImage(split[0],split[2],split[1],picQueue,null, null,supplierAuthType.get(supplierId),user,pass));
                    }else{
                        executor.execute(new DowmImage(split[0],split[2],split[1],picQueue,null, null,"",userName,password));
                    }


                }
                delay(executor);
            }
            loggerInfo.info("供货商:" + supplierIdPam + " 下载完毕");
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    private String changeUrl(String supplierId,String url){
        String resultRult = "";
        if(StringUtils.isNotBlank(supplierId)){
             if("2016032401823".equals(supplierId)){
                 url=url.replace("\\", "/");
             }else if("2016030901801".equals(supplierId)){   //deliberti
                 url="http://"+url;
             }
        }
        resultRult = this.replaceSpecialChar(url);

        return resultRult;
    }

    private String replaceSpecialChar(String url){
        return url.replace(" ", "%20");
    }
    private void delay(ThreadPoolExecutor executor){
        while(true){
            if(executor.getActiveCount()==0){
            	loggerInfo.info("线程活动数为0");
                break;
            }
            try {
                Thread.sleep(1000*10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
