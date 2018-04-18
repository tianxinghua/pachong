package com.shangpin.iog.styleside;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import org.datacontract.schemas._2004._07.maximag_connector_vidra_service.ArrayOfArticoloFlatExtLocaleVO;
import org.datacontract.schemas._2004._07.maximag_connector_vidra_service.ArticoloFlatExtLocaleVO;
import org.datacontract.schemas._2004._07.maximag_connector_vidra_service.MgDispo;
import org.tempuri.IVidraSvcOfArticoloFlatExtVOArticoloFlatVO;
import org.tempuri.VidraSvc;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lizhongren on 2018/4/13.
 */
public class Test {

    public static void main(String[] args){

        /**
         * 程序配置：
         * 拉取的时间、用户名、用户密码、fiter、每页拉取数、语言为配置文件控制
         * 页码为程序中循环增加，
         * 程序的循环结束（while循环）标志：
         * 拉取的信息的 list size()为 0 的时候结束
         */
        int page=0;
        boolean loop= true;
        try {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);//获取年份
            int month=cal.get(Calendar.MONTH);//获取月份
            int day=cal.get(Calendar.DATE);//获取日
            int hour=cal.get(Calendar.HOUR);//小时
            int minute=cal.get(Calendar.MINUTE);//分
            int second=cal.get(Calendar.SECOND);//秒
            int millisecond = cal.get(Calendar.MILLISECOND);
            IVidraSvcOfArticoloFlatExtVOArticoloFlatVO http = new VidraSvc().getHTTP();
            XMLGregorianCalendarImpl xmlGregorianCalendar = new XMLGregorianCalendarImpl();
            while(loop){
                page++;
                xmlGregorianCalendar.setYear(year);
                xmlGregorianCalendar.setMonth(month);
                xmlGregorianCalendar.setDay(day);
                xmlGregorianCalendar.setHour(hour);
                xmlGregorianCalendar.setMinute(minute);
                xmlGregorianCalendar.setSecond(second);
                xmlGregorianCalendar.setMillisecond(millisecond);

                ArrayOfArticoloFlatExtLocaleVO articoliFlatExtLocaleByDate = http.getArticoliFlatExtLocaleByDate("ECOMM", "vendiamotutto", "", xmlGregorianCalendar, 100, 1, "eng");
                List<ArticoloFlatExtLocaleVO> articoloFlatExtLocaleVO = articoliFlatExtLocaleByDate.getArticoloFlatExtLocaleVO();

                if(null==articoloFlatExtLocaleVO||articoloFlatExtLocaleVO.size()<100){
                    loop = false;
                }
                if(null!=articoloFlatExtLocaleVO&&articoloFlatExtLocaleVO.size()>0){
                    for (ArticoloFlatExtLocaleVO vo :articoloFlatExtLocaleVO) {
                        String modelCode = vo.getModelCode().getValue();
                        String size = vo.getSize().getValue();
                        String skuNo = modelCode + size;
                        MgDispo mgDispo = vo.getQuantita().getValue();
                        BigDecimal quantitaDimm = mgDispo.getQuantitaDimm();
                        System.out.println(skuNo+"="+quantitaDimm);

                    }
                    System.out.println("==thestyleside 获取第"+page+"页结束==");
                    System.out.println();
                    System.out.println();
                    System.out.println();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
