package com.shangpin.ephub.data.mysql.sku.purchase.mapper;

import com.shangpin.ephub.data.mysql.sku.purchase.po.PurchaseProductCriteria;
import com.shangpin.ephub.data.mysql.sku.purchase.po.PurchaseProductRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Administrator on 2017/6/6.
 */
@Mapper
public interface PurchaseProductRecordMapper {

    List<PurchaseProductRecord> getProductWithPurchase(PurchaseProductCriteria searchItems);
}
