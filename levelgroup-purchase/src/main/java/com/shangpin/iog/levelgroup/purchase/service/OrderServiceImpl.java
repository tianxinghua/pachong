package com.shangpin.iog.levelgroup.purchase.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.dto.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by Administrator on 2015/11/20.
 */
public class OrderServiceImpl {

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("info");
    private static org.apache.log4j.Logger loggerError = org.apache.log4j.Logger.getLogger("error");

    private static ResourceBundle bdl = null;
    private static  String supplierId = null;
    private static String localFile = null;

    static {
        if(null==bdl){
            bdl=ResourceBundle.getBundle("conf");
        }
        supplierId = bdl.getString("supplierId");
        localFile = bdl.getString("localFile");
    }

    @Autowired
    com.shangpin.iog.product.service.OrderServiceImpl orderService;

    /**
     * 组装并保存采购单信息到本地
     * @throws ServiceException
     */
    public void saveOrder(){
        List<OrderDTO> list = null;
        try {
           list = orderService.getOrderBySupplierIdAndOrderStatus(supplierId,"confirmed");
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        StringBuffer ftpFile = new StringBuffer();
        //字段设值
        ftpFile.append("ORDER CODE;ITEM CODE;SIZE;SKU;ORDER;PRICE;BRAND");
        ftpFile.append("\\n\\t");
        for (OrderDTO orderDTO:list){
            ftpFile.append(orderDTO.getSpPurchaseNo());
            ftpFile.append(";").append(orderDTO.getSpPurchaseDetailNo());
            ftpFile.append(";").append("size");
            ftpFile.append(";").append(orderDTO.getMemo());
            ftpFile.append(";").append(orderDTO.getPurchasePriceDetail());
            ftpFile.append("\\n\\t");
        }
        //////////////////////////////////////////////////////////////////////////////////////////
        Map<String, String> mongMap = new HashMap<>();
        mongMap.put("supplierId", supplierId);
        mongMap.put("supplierName", "LevelGroup");
        mongMap.put("result", ftpFile.toString());
        //logMongo.info(mongMap);
        //System.out.println(csvFile);
        FileWriter fwriter = null;
        try {
            fwriter = new FileWriter(localFile);
            fwriter.write(ftpFile.toString());
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fwriter.flush();
                fwriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }


}
