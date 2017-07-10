package com.shangpin.iog.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.shangpin.iog.app.AppContext;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.service.OpenapiService;
import com.shangpin.iog.service.SopService;

public class StartUp {

	private static org.apache.log4j.Logger loggerInfo = org.apache.log4j.Logger
			.getLogger("info");
	private static org.apache.log4j.Logger loggerError = org.apache.log4j.Logger.getLogger("error");
	private static final String YYYY_MMDD_HH = "yyyy-MM-dd HH:mm";
	private static ResourceBundle bdl = null;
	private static String suppliers = null;
	private static String isHK = null;
	private static String  startDate=null,endDate=null;
	private static String  theStartDate=null,theEndDate=null;
	private static String hostUrl=null;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		suppliers = bdl.getString("suppliers");	
		isHK = bdl.getString("isHK");
		theStartDate = bdl.getString("theStartDate");
		theEndDate = bdl.getString("theEndDate");
		hostUrl = bdl.getString("hostUrl");
	}
	private static ApplicationContext factory;
	private static void loadSpringContext()

	{

        factory = new AnnotationConfigApplicationContext(AppContext.class);
	}
	public static void main(String[] args) {
		try {
			loadSpringContext();
			loggerInfo.info("初始化成功，开始同步");
			initDate("dateAPI.ini");			
			loggerInfo.info("========开始时间========="+startDate+"=========结束时间=========="+endDate);
			System.out.println("========开始时间========="+startDate+"=========结束时间=========="+endDate); 
			if("1".equals(isHK)){
				SopService sopService = (SopService)factory.getBean("sopService");
				sopService.dotheJob(suppliers,startDate,endDate); 
			}else{
				OpenapiService openapiService = (OpenapiService)factory.getBean("openapiService");
				openapiService.dotheJob(hostUrl,suppliers,startDate,endDate);
			}
			loggerInfo.info("===========同步完成========"); 
		} catch (Exception e) {
			e.printStackTrace();
		}		
		System.exit(0); 
	}
	
	/**
	 * 初始化时间

	 * @param fileName 保存时间的文件名
	 */
	public static void initDate(String  fileName) {
        Date tempDate = new Date();

        endDate = com.shangpin.iog.common.utils.DateTimeUtil.convertFormat(tempDate, YYYY_MMDD_HH);

        String lastDate=getLastGrapDate(fileName);
        startDate= org.apache.commons.lang.StringUtils.isNotEmpty(lastDate) ? lastDate: com.shangpin.iog.common.utils.DateTimeUtil.convertFormat(DateUtils.addDays(tempDate, -180), YYYY_MMDD_HH);

        Date tmpDate =  DateTimeUtil.getAppointDayFromSpecifiedDay(DateTimeUtil.convertFormat(startDate,YYYY_MMDD_HH),-10,"S");
        startDate = DateTimeUtil.convertFormat(tmpDate,YYYY_MMDD_HH) ;
        
        if(StringUtils.isNotBlank(theStartDate)){
        	startDate =theStartDate;
        }
        
        if(StringUtils.isNotBlank(theEndDate)){
        	endDate = theEndDate;
        }else{
        	writeGrapDate(endDate,fileName);
        }
    }

    private static File getConfFile(String fileName) throws IOException {
        String realPath = StartUp.class.getClassLoader().getResource("").getFile();
        realPath= URLDecoder.decode(realPath, "utf-8");
        File df = new File(realPath+fileName);//"date.ini"
        if(!df.exists()){
            df.createNewFile();
        }
        return df;
    }
    private static String getLastGrapDate(String fileName){
        File df;
        String dstr=null;
        try {
            df = getConfFile(fileName);
            try(BufferedReader br = new BufferedReader(new FileReader(df))){
                dstr=br.readLine();
            }
        } catch (IOException e) {
        	loggerError.error("读取日期配置文件错误");
        }
        return dstr;
    }

    private static void writeGrapDate(String date,String fileName){
    	int i=0;
    	boolean boo = true;
    	while(boo && i<100){
    		File df;
            try {
                df = getConfFile(fileName);
                try(BufferedWriter bw = new BufferedWriter(new FileWriter(df))){
                    bw.write(date);
                }
                boo = false;
            } catch (IOException e) {
            	loggerError.error("写入日期配置文件错误 "+i); 
                boo = true;
                try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
            }finally{
            	i++;
            }
    	}
        
    }
}
