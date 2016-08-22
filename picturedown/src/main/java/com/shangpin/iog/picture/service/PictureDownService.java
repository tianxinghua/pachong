package com.shangpin.iog.picture.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.DowmImage;
import com.shangpin.iog.common.utils.queue.PicQueue;
import com.shangpin.iog.service.ProductReportService;
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
    private static String supplierId = null;
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

        supplierId = bdl.getString("supplierId");
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
            if("".equals(supplierId)) supplierId=null;
            supplierDateMap = productReportService.findPicture(picMap,supplierId,startDate,endDate,excludesupplierId);
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
                    for(String spu :spuArray){
                        int a = 0;
                        //下载保存图片


                            System.out.println("++++"+a+"++++++");
                            a++;
                            String[] ingArr = picMap.get(spu).split(",");
                            int i = 0;
                            for (String img : ingArr) {
                                if (org.apache.commons.lang.StringUtils.isNotBlank(img)) {
                                    try {
                                        i++;
                                        File f = new File(dirPath+"/"+spu+" ("+i+").jpg");
                                        if (f.exists()) {
                                            continue;
                                        }
                                        executor.execute(new DowmImage(img.trim(),spu+" ("+i+").jpg",dirPath,picQueue,null, null,userName,password));
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
                            Thread.sleep(1000*15);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                    failUrl = picQueue.unVisitEdUrlDeQueue();
                    if (recordMap.containsKey(failUrl)) {
                        if (recordMap.get(failUrl)>10) {
                            continue;
                        }
                        recordMap.put(failUrl, recordMap.get(failUrl)+1);
                    }else{
                        recordMap.put(failUrl, 1);
                    }
                    split = failUrl.split(";");
                    executor.execute(new DowmImage(split[0],split[2],split[1],picQueue,null, null,userName,password));

                }
                delay(executor);
            }
        } catch (ServiceException e) {
            e.printStackTrace();
        }
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
