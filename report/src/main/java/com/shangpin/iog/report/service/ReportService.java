package com.shangpin.iog.report.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.service.ProductReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by lizhongren on 2016/8/18.
 */
@Component("reportService")
public class ReportService {
    private static org.apache.log4j.Logger loggerInfo = org.apache.log4j.Logger
            .getLogger("info");
    private static org.apache.log4j.Logger loggerError = org.apache.log4j.Logger
            .getLogger("error");

    @Autowired
    ProductReportService productReportService;

    public  void mail(){
        try {
            Map<String,Integer> map =  productReportService.findProductReport();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}
