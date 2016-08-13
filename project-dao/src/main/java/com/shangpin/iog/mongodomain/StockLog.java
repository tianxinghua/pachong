package com.shangpin.iog.mongodomain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by lizhongren on 2016/7/28.
 */
@Getter
@Setter
@Document(collection="stock-log")
public class StockLog implements Serializable {
    private static final long serialVersionUID = -4890916950574974903L;
    @Id
    private String id;
    private String supplierId;
    private String supplierName;
    private String skuNo;
    private String supplierSkuNo;
    private Date  createTime;

}
