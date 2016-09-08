package com.shangpin.iog.picture.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.DowmImage;
import com.shangpin.iog.common.utils.queue.PicQueue;
import com.shangpin.iog.service.ProductReportService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
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
    @Autowired
    ProductReportService productReportService;

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
        Map<String,String> picMap = new HashMap<>();
        Map<String,String> supplierDateMap = null;
        try {
            if("".equals(supplier_Id)) supplier_Id=null;
            supplierDateMap = productReportService.findPicture(picMap,supplier_Id,startDate,endDate,excludesupplierId);
            if(null!=supplierDateMap&&supplierDateMap.size()>0){
                //获取日期
                String key = "",supplierId = "",date= "",spukeyValue = "";
                ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 200, 300, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(100),new ThreadPoolExecutor.CallerRunsPolicy());
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

                        //下载保存图片


                            System.out.println("++++"+a+"++++++");
                            a++;
                            String[] ingArr = picMap.get(spu).split(",");
                            int i = 0;
                            for (String img : ingArr) {



                                if (org.apache.commons.lang.StringUtils.isNotBlank(img)) {
                                    try {
                                        img=this.changeUrl(supplierId,img) ;
                                        System.out.println("spu =" +spu + " 　img url  ="+ img);
                                        loggerInfo.info("spu =" +spu + " 　img url  ="+ img);
                                        i++;
                                        File f = new File(dirPath+"/"+spu+" ("+i+").jpg");
                                        if (f.exists()) {
                                            continue;
                                        }
                                        //某些供货商特殊处理
                                        if("2015092401528".equals(supplierId)){
                                            //2015092401528 stefania \
                                            // 2015101501608  tony    暂不需要
                                             DownloadPicTool.downImage(img.trim(),dirPath,spu+" ("+i+").jpg");
                                        }else{
                                            executor.execute(new DowmImage(img.trim(),spu+" ("+i+").jpg",dirPath,picQueue,null, null,userName,password));
                                        }


                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }




                    }
                }
                delay(executor);
                //重新下载失败的
                String failUrl = "";
                String[] split = null;
                Map<String,Integer> recordMap = new HashMap<String, Integer>();
                while(executor.getActiveCount()>0||!picQueue.unVisitedUrlsEmpty()){
                    if (picQueue.unVisitedUrlsEmpty()&&executor.getActiveCount()>=0) {
                        System.out.println("============================================都为空=======================================================");
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
                            loggerInfo.info("downloaderror="+ failUrl);
                            continue;
                        }
                        recordMap.put(failUrl, recordMap.get(failUrl)+1);
                    }else{
                        recordMap.put(failUrl, 1);
                    }
                    split = failUrl.split(";");
                    System.out.println("曾经下载失败的 spu =" +split[2] + " 　img url  ="+ split[0]);
                    loggerInfo.info("曾经下载失败的 spu =" +split[2] + " 　img url  ="+ split[0]);
                    executor.execute(new DowmImage(split[0],split[2],split[1],picQueue,null, null,userName,password));

                }
                delay(executor);
            }
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
                loggerInfo.error("线程活动数为0");
                break;
            }
            try {
                Thread.sleep(1000*30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
