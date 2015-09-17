package com.shangpin.iog.gilt;

import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.gilt.order.OrderServiceImpl;
import com.shangpin.iog.gilt.schedule.AppContext;
import com.shangpin.iog.gilt.service.FetchProduct;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by sunny on 2015/8/5.
 */
public class Startup {

    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");

    private static final String YYYY_MMDD_HH = "yyyy-MM-dd HH:mm:ss";
    private static String  startDate=null,endDate=null;
    private static ResourceBundle bdl=null;
    private static String supplierId;

    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
    }
    private static ApplicationContext factory;
    private static void loadSpringContext()
    {

        factory = new AnnotationConfigApplicationContext(AppContext.class);
    }

    public static void main(String[] args)
    {
        //加载spring
        loadSpringContext();
        //初始化时间
        initDate();

        //拉取数据
        OrderServiceImpl orderService =(OrderServiceImpl)factory.getBean("giltOrder");
        System.out.println("-------gilt start---------");
        try {
            List<Integer> status = new ArrayList<>();
            status.add(1);
            orderService.purOrder(supplierId,startDate,endDate,status);
            System.out.println("成功插入数据库");
            System.out.println("-------gilt end---------");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private static void initDate() {
        Date tempDate = new Date();

        endDate = DateTimeUtil.convertFormat(tempDate, YYYY_MMDD_HH);

        String lastDate=getLastGrapDate();
        startDate= StringUtils.isNotEmpty(lastDate) ? lastDate: DateTimeUtil.convertFormat(DateUtils.addDays(tempDate, -180), YYYY_MMDD_HH);



        writeGrapDate(endDate);


    }

    private static File getConfFile() throws IOException {
        String realPath = Startup.class.getClassLoader().getResource("").getFile();
        realPath= URLDecoder.decode(realPath, "utf-8");
        File df = new File(realPath+"date.ini");
        if(!df.exists()){
            df.createNewFile();
        }
        return df;
    }
    private static String getLastGrapDate(){
        File df;
        String dstr=null;
        try {
            df = getConfFile();
            try(BufferedReader br = new BufferedReader(new FileReader(df))){
                dstr=br.readLine();
            }
        } catch (IOException e) {
            logger.error("读取日期配置文件错误");
        }
        return dstr;
    }

    private static void writeGrapDate(String date){
        File df;
        try {
            df = getConfFile();

            try(BufferedWriter bw = new BufferedWriter(new FileWriter(df))){
                bw.write(date);
            }
        } catch (IOException e) {
            logger.error("写入日期配置文件错误");
        }
    }

}
