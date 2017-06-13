package com.shangpin.ephub.product.business.ui.studio.studio.vo;
import com.shangpin.ephub.client.data.mysql.studio.supplier.dto.HubSlotSpuSupplierDto;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/6/9.
 */
@Getter
@Setter
public class StudioPendingProduct extends HubSlotSpuSupplierDto implements Serializable {

    private static final long serialVersionUID = 4357243781682368686L;

    public String brandNo;
    public String brandName;

    public String categoryNo;
    public String categoryName;

    public String supplierOrigin;

    public String productName;

    public String productDesc;
}
