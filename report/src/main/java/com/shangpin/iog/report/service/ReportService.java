package com.shangpin.iog.report.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.SendMail;
import com.shangpin.iog.service.ProductReportService;
import com.shangpin.iog.service.SeasonRelationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by lizhongren on 2016/8/18.
 */
@Component("reportService")
public class ReportService {
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
    private static String startDate = null;
    private static String endDate = null;


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

        startDate = bdl.getString("startDate");
        endDate = bdl.getString("endDate");
    }

    @Autowired
    ProductReportService productReportService;



    public  void mail(){
        try {
            Map<String,Integer> map =  productReportService.findProductReport(startDate,endDate);
            String supplierName="",shangpinSeasonName="";
            StringBuffer buffer = new StringBuffer();
            if(null==map.keySet()||map.keySet().size()==0) return;

            for(String key :map.keySet()){
                String[] keyArray = key.split("\\|\\|");
                buffer.append(keyArray[0]).append(",").append(keyArray[1]).append(",").append(map.get(key));
                buffer.append("\r\n");

            }

            try {
                if(StringUtils.isNotBlank(to)){


                    String messageText  = buffer.toString();
                    if(StringUtils.isNotBlank(messageText)){
                        try {
                            BufferedInputStream in = null;
                            File file = null;
                            FileOutputStream out = null;
                            try{
                                in = new BufferedInputStream(new ByteArrayInputStream(messageText.getBytes("gb2312")));
                                file = new File(filepath);
                                out = new FileOutputStream(file);
                                byte[] data = new byte[1024];
                                int len = 0;
                                while (-1 != (len=in.read(data, 0, data.length))) {
                                    out.write(data, 0, len);
                                }
                            }catch(Exception e){
                                e.printStackTrace();
                            }finally {
                                if (in != null) {
                                    in.close();
                                }
                                if (out != null) {
                                    out.close();
                                }
                            }
                            if(null != file){
                                SendMail.sendGroupMailWithFile(smtpHost, from, fromUserPassword, to, subject,"请查看附件", messageType,file);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                            loggerError.error(e.getMessage());
                        }
                    }

                }

            } catch (Exception e) {
                loggerError.error(e.getMessage());
                e.printStackTrace();
            }
        } catch (ServiceException e) {
            loggerError.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
