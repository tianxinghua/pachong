package com.shangpin.ephub.product.business.ui.studio.studio.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shangpin.ephub.client.data.mysql.studio.supplier.dto.HubSlotSpuSupplierDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/8.
 */
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudioPendingProductVo extends HubSlotSpuSupplierDto implements Serializable {

    private static final long serialVersionUID = 9106346918684398530L;

//    private Long slotSpuId;
//    private String slotNo;
//    public String supplierId;

    public String brandNo;
    public String brandName;

    public String categoryNo;
    public String categoryName;

    public String supplierOrigin;

    public String productName;
}
