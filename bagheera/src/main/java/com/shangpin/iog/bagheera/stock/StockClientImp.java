package com.shangpin.iog.bagheera.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.bagheera.utils.DownloadAndReadExcel;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by sunny on 2015/9/10.
 */
public class StockClientImp extends AbsUpdateProductStock{
    @Autowired
    DownloadAndReadExcel excelHelper;
    @Override
    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        Map<String, String> skustock = new HashMap<>(skuNo.size());
        Map<String,String> stockMap = new HashMap<>();
        String supplierId = "201509091712";
        Iterator<String> it = skuNo.iterator();
        while (it.hasNext()) {
            String skuId = it.next();
        }
        return null;
    }
}
