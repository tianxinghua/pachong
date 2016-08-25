package com.shangpin.iog.picture.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.shangpin.iog.common.utils.DowmImage;
import com.shangpin.iog.common.utils.queue.PicQueue;
import com.shangpin.iog.picture.dto.Supplier;
import com.shangpin.iog.picture.dto.Suppliers;
import com.shangpin.iog.picture.utils.ftp.FTPUtils;
import com.shangpin.iog.service.ProductReportService;

/**
 * Created by lizhongren on 2016/8/22.
 */
@Service("ftpDownPicService")
public class FtpDownPicService {
	
	private static org.apache.log4j.Logger loggerInfo = org.apache.log4j.Logger
            .getLogger("info");
    private static org.apache.log4j.Logger loggerError = org.apache.log4j.Logger
            .getLogger("error");

    private static ResourceBundle bdl = null;
 
    private static String smtpHost = null;
    private static String from = null;
    private static String fromUserPassword = null;
    private static String to = null;
    private static String subject = null;
    private static String messageType = null;
   
    private static String downloadPath = null;
    private static String jsonFilepath = null;
   
    @Autowired
    ProductReportService productReportService;

    static {
        if (null == bdl) {
            bdl = ResourceBundle.getBundle("ftp");
        }
        smtpHost  = bdl.getString("smtpHost");
        from = bdl.getString("from");
        fromUserPassword = bdl.getString("fromUserPassword");
        to = bdl.getString("to");
        subject = bdl.getString("subject");
        messageType = bdl.getString("messageType");

        downloadPath = bdl.getString("downloadPath");
        jsonFilepath = bdl.getString("jsonFilepath");

    }

	public void downPic(){
		
        try {
        	File jsonFile = new File(jsonFilepath);
        	BufferedReader buffReader = null;//
        	String jsonString = "";
            try {
                buffReader = new BufferedReader(new InputStreamReader(new FileInputStream(jsonFile), "UTF-8"));                
                String str = "";
                while ((str = buffReader.readLine()) != null){
                	jsonString += str;
                }
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                if (buffReader != null){
                    try {
                        buffReader.close();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
            Suppliers suppliers = new Gson().fromJson(jsonString, Suppliers.class);
            if(null == suppliers || null == suppliers.getSuppliers() || suppliers.getSuppliers().size()<=0){
            	loggerError.error("json文件为空或者解析失败"); 
            	return;
            }
            loggerInfo.info("解析json文件成功，共有"+suppliers.getSuppliers().size()+"个供应商");  
            ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 200, 300, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(100),new ThreadPoolExecutor.CallerRunsPolicy());
            for(Supplier supplier : suppliers.getSuppliers()){
            	executor.execute(new FtpDownPic(supplier)); 
            }
                        
        }catch(Exception e){
        	e.printStackTrace();
			loggerError.error(e.toString()); 
        }
	}
	
	class FtpDownPic implements Runnable {
		
		private Supplier supplier;
		
		public FtpDownPic(Supplier supplier){
			this.supplier = supplier;
		}
		@Override
		public void run() {
			try {
				loggerInfo.info(supplier.getSupplierId()+"开始下载图片========"); 
				//下载的文件名<-->图片链接
				Map<String,String> toBeDownPicMap = new HashMap<String,String>();
				Map<String,String> downLoadFailedMap = new HashMap<String,String>();
				Map<String,String> picMap = new HashMap<>();
		        Map<String,String> supplierDateMap = null;
		        String supplier_id = supplier.getSupplierId();
	    		String startDate = supplier.getStartDate();
	    		String endDate = supplier.getEndDate();
	    		
	    		if("".equals(supplier_id)) supplier_id=null;
	            supplierDateMap = productReportService.findPicture(picMap,supplier_id,startDate,endDate,null);
	            if(null!=supplierDateMap&&supplierDateMap.size()>0){
	                //获取日期
	                String key = "",supplierId = "",date= "",spukeyValue = "";                
	                for(Map.Entry<String,String> supplierDate:supplierDateMap.entrySet()){
	                     key = supplierDate.getKey();
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
	                                    if(img.contains("/")){
	                                    	img = img.substring(img.lastIndexOf("/")+1);
	            						}
	                                    toBeDownPicMap.put(dirPath+"/"+spu+" ("+i+").jpg", img);
	                                } catch (Exception e) {
	                                    e.printStackTrace();
	                                }
	                            }
	                        }
	                    }
	                }
	            }
	            //去ftp下载图片        
	            loggerInfo.info("需要下载的map的大小==============="+toBeDownPicMap.size());
	            if(null != toBeDownPicMap && toBeDownPicMap.size()>0){
	            	int port = 21;
	            	if(StringUtils.isNotBlank(supplier.getPort())){
	            		port = Integer.parseInt(supplier.getPort());
	            	}	            	 
	            	FTPUtils.downFile(supplier.getFtpUrl(), port, supplier.getUserName(), supplier.getPassword(), supplier.getRemote(), toBeDownPicMap, downLoadFailedMap);
	            	//下载失败的再下载一遍
	            	if(null != downLoadFailedMap && downLoadFailedMap.size()>0){
	            		Map<String,String> failedMap = new HashMap<String,String>();
	            		FTPUtils.downFile(supplier.getFtpUrl(), port, supplier.getUserName(), supplier.getPassword(), supplier.getRemote(), downLoadFailedMap, failedMap);
	            		//还失败的记录日志
	            		if(null != failedMap && failedMap.size()>0){
	            			loggerError.error("第二次下载失败的链接有======"+failedMap.toString());
	            		}            		
	            	}
	            	loggerInfo.info(supplier_id+"下载完成==============");
	            }
			} catch (Exception e) {
				e.printStackTrace();
				loggerError.error(e.toString()); 
			}
		}
	}
}
