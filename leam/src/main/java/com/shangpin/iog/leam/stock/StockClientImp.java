package com.shangpin.iog.leam.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import org.apache.log4j.Logger;

import java.util.Collection;
import java.util.Map;

/**
 * Created by sunny on 2015/8/18.
 */
public class StockClientImp  extends AbsUpdateProductStock {
    private static Logger logger = Logger.getLogger("info");

    @Override
    public Map<String, Integer> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        return null;
    }
}
