package com.shangpin.iog.styleside;

import com.sun.org.apache.xerces.internal.jaxp.datatype.XMLGregorianCalendarImpl;
import org.datacontract.schemas._2004._07.maximag_connector_vidra_service.ArrayOfArticoloFlatExtLocaleVO;
import org.datacontract.schemas._2004._07.maximag_connector_vidra_service.ArticoloFlatExtLocaleVO;
import org.datacontract.schemas._2004._07.maximag_connector_vidra_service.MgDispo;
import org.tempuri.IVidraSvcOfArticoloFlatExtVOArticoloFlatVO;
import org.tempuri.VidraSvc;

import java.math.BigDecimal;
import java.util.List;

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
        IVidraSvcOfArticoloFlatExtVOArticoloFlatVO http = new VidraSvc().getHTTP();
        XMLGregorianCalendarImpl xmlGregorianCalendar = new XMLGregorianCalendarImpl();
        xmlGregorianCalendar.setYear(2017);
        xmlGregorianCalendar.setMonth(2);
        xmlGregorianCalendar.setDay(10);
        xmlGregorianCalendar.setHour(10);
        xmlGregorianCalendar.setMinute(58);
        xmlGregorianCalendar.setSecond(4);
        xmlGregorianCalendar.setMillisecond(445);
        ArrayOfArticoloFlatExtLocaleVO articoliFlatExtLocaleByDate = http.getArticoliFlatExtLocaleByDate("ECOMM", "vendiamotutto", "0000jj", xmlGregorianCalendar, 100, 1, "eng");
        List<ArticoloFlatExtLocaleVO> articoloFlatExtLocaleVO = articoliFlatExtLocaleByDate.getArticoloFlatExtLocaleVO();

        for (ArticoloFlatExtLocaleVO vo :articoloFlatExtLocaleVO) {
            String sku = vo.getModelCode().getValue();
            String size = vo.getSize().getValue();
            MgDispo mgDispo = vo.getQuantita().getValue();
            BigDecimal quantitaDimm = mgDispo.getQuantitaDimm();
            System.out.println("sku="+sku+"    quantitaDimm="+quantitaDimm);
            System.out.println("size=="+size);
            System.out.println();
            System.out.println();
        }
        return;
    }
}
