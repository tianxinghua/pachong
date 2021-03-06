package com.shangpin.iog.parisi.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by lizhongren on 2017/3/1.
 */
@XmlRootElement(name="Response")
@Getter
@Setter
@ToString
public class OrderDetail {
    private String key;
    private String order_id;
    private String order_no;
    private String message;
    private String status;
}
