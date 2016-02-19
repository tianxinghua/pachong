package com.shangpin.iog.spinnaker.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by lizhongren on 2016/2/18.
 */

@Setter
@Getter
public class OrderInfoDTO {

    private Integer Id_b2b_order;
    private String Order_No;
    private String Purchase_No;
    private String Status;


    private String Date_Order;
    private String IT_Time_Order;
    private String Date_Confirmed;
    private String Date_Shipped;
    private String logistics_company;
    private String Trk_Number;
}
