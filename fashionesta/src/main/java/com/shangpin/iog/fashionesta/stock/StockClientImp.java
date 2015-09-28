package com.shangpin.iog.fashionesta.stock;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.fashionesta.dto.FashionestaDTO;
import com.shangpin.iog.fashionesta.utils.DownloadAndReadCSV;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by monkey on 2015/9/28.
 */
public class StockClientImp extends AbsUpdateProductStock{
    @Autowired
    DownloadAndReadCSV csvUtil;
    @Override
    public Map<String, String> grabStock(Collection<String> skuNo) throws ServiceException, Exception {
        Map<String, String> skustock = new HashMap<>(skuNo.size());
        String supplierId = "201509281616";
        List<FashionestaDTO> list=csvUtil.readLocalCSV();
        Iterator<String> it = skuNo.iterator();
        while (it.hasNext()) {
            String skuId = it.next();
            for(int i=0;i<list.size();i++){
                if(skuId.equals(list.get(i).getSUPPLIER_CODE())){
                    skustock.put(skuId,list.get(i).getSTOCK());
                }
            }
        }
        return skustock;
    }
}
