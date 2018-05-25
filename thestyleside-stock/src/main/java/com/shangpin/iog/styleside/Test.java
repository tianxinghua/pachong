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
            IVidraSvcOfArticoloFlatExtVOArticoloFlatVO http = new VidraSvc().getHTTP();
            XMLGregorianCalendarImpl xmlGregorianCalendar = new XMLGregorianCalendarImpl();
            xmlGregorianCalendar.setYear(cal.get(Calendar.YEAR));
            xmlGregorianCalendar.setMonth(cal.get(Calendar.MONTH));
            xmlGregorianCalendar.setDay(cal.get(Calendar.DATE));
            xmlGregorianCalendar.setHour(cal.get(Calendar.HOUR));
            xmlGregorianCalendar.setMinute(cal.get(Calendar.MINUTE));
            xmlGregorianCalendar.setSecond(cal.get(Calendar.SECOND));
            xmlGregorianCalendar.setMillisecond(cal.get(Calendar.MILLISECOND));
            while(loop){
                page++;
                ArrayOfArticoloFlatExtLocaleVO articoliFlatExtLocaleByDate = http.getArticoliFlatExtLocaleByDate("ECOMM", "vendiamotutto", "", xmlGregorianCalendar, 100, page, "eng");


                List<ArticoloFlatExtLocaleVO> articoloFlatExtLocaleVO = articoliFlatExtLocaleByDate.getArticoloFlatExtLocaleVO();

                System.out.println("thestyleside成功获取第"+page+"页库存数据，当前页有"+articoloFlatExtLocaleVO.size()+"条数据");
                if(null==articoloFlatExtLocaleVO||articoloFlatExtLocaleVO.size()<100){
                    loop = false;
                }
                if(null!=articoloFlatExtLocaleVO&&articoloFlatExtLocaleVO.size()>0){
                    for (ArticoloFlatExtLocaleVO vo :articoloFlatExtLocaleVO) {
                        /**
                         * modelCode 对应商品的spu , modelCode-size 对应商品的sku
                         */
                        String modelCode = vo.getModelCode().getValue();
                        String size = vo.getSize().getValue();
                        String skuNo = modelCode +"-"+ size;
                        System.out.println("============skuNo=============>:"+skuNo);

                        MgDispo mgDispo = vo.getQuantita().getValue();
                        BigDecimal quantitaDimm = mgDispo.getQuantitaDimm();
                        String stockNum = quantitaDimm+"";
                        System.out.println("=====skuNO:stockNum-========"+skuNo+":"+stockNum);

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
