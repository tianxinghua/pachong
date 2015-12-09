package com.shangpin.iog.levelgroup.purchase.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.levelgroup.purchase.common.MyFtpUtil;
import com.shangpin.iog.product.service.OrderServiceImpl;
import com.shangpin.iog.service.ProductSearchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Created by Administrator on 2015/11/20.
 */
@Component
public class OrderService extends AbsOrderService {

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("info");
    private static org.apache.log4j.Logger loggerError = org.apache.log4j.Logger.getLogger("error");

    private static ResourceBundle bdl = null;
    private static  String supplierId = null;
    private static String supplierNo = null;
    private static String localFile = null;

    static {
        if(null==bdl){
            bdl=ResourceBundle.getBundle("conf");
        }
        supplierId = bdl.getString("supplierId");
        localFile = bdl.getString("localFile");
    }

    @Autowired
    OrderServiceImpl orderService;
    
    @Autowired
    ProductSearchService productSearchService;
    
    // 下单处理
 	public void startSOP() {
 		this.checkoutOrderFromSOP(supplierNo, supplierId, true);
 	}
 	
 	// 订单确认处理
 	public void confirmOrder() {
 		this.confirmOrder(supplierId);
 	}

    /**
     * 创建订单信息并上传到ftp
     * @throws ServiceException
     */
    public void saveAndUpLoadOrder(){
        saveOrder();
        new MyFtpUtil().upLoad();
    }
    /**
     * 
     * @throws ServiceException
     */
    private void saveOrder(){
        List<OrderDTO> list = null;
        try {
           list = orderService.getOrderBySupplierIdAndOrderStatus(supplierId,"confirmed");
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        StringBuffer ftpFile = new StringBuffer();
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
	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
		orderDTO.setStatus(OrderStatus.PLACED);
		
	}
	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		orderDTO.setExcState("0");
		//createOrder(OrderStatus.CONFIRMED, orderDTO);
		orderDTO.setStatus(OrderStatus.CONFIRMED);
		
	}
	@Override
	public void handleCancelOrder(ReturnOrderDTO deleteOrder) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void handleRefundlOrder(ReturnOrderDTO deleteOrder) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void handleEmail(OrderDTO orderDTO) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void getSupplierSkuId(Map<String, String> skuMap) throws ServiceException {
		// TODO Auto-generated method stub
		
	}
}
