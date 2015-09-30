package com.shangpin.iog.bagheera.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.bagheera.stock.dto.BagheeraDTO;
import com.shangpin.iog.bagheera.stock.utils.DownloadAndReadExcel;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by sunny on 2015/9/10.
 */
public class StockClientImp extends AbsUpdateProductStock{
    @Autowired
    DownloadAndReadExcel excelHelper;
    @Override
    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        Map<String, String> skustock = new HashMap<>(skuNo.size());
        List<BagheeraDTO> list=excelHelper.readLocalExcel();
        Iterator<String> it = skuNo.iterator();
        while (it.hasNext()) {
            String skuId = it.next();
            for(int i=0;i<list.size();i++){
                if(skuId.equals(list.get(i).getSUPPLIER_CODE()+"-"+list.get(i).getSIZE())){
                    skustock.put(skuId,list.get(i).getSTOCK()/*+"|"+list.get(i).getLIST_PRICE()*/);
                }
            }
        }
        return skustock;
    }
}
