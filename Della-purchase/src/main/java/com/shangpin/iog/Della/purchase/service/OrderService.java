package com.shangpin.iog.Della.purchase.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.Della.purchase.common.MyFtpUtil;
import com.shangpin.iog.Della.purchase.common.MyFtpUtil2;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ProductDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
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
    private static String startTime = null;
    private static String endTime = null;

    static {
        if(null==bdl){
            bdl=ResourceBundle.getBundle("conf");
        }
        supplierId = bdl.getString("supplierId");
        supplierNo = bdl.getString("supplierNo");
        localFile = bdl.getString("localFile");
    }

    @Autowired
    OrderServiceImpl orderService;
    
    @Autowired
    ProductSearchService productSearchService;
    
    // 下单处理
 	public void startSOP() {
 		this.checkoutOrderFromSOP(supplierId,supplierNo, true);
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
        Date startTime = new Date();
        Date endTime = new Date();
        startTime =DateTimeUtil.convertFormat(
        		DateTimeUtil.shortFmt(DateTimeUtil.getAppointDayFromSpecifiedDay(startTime, -1, "D"))+" 00:00:00", "yyyy-MM-dd HH:mm:ss");
        endTime =DateTimeUtil.convertFormat(DateTimeUtil.shortFmt(endTime)+" 00:00:00", "yyyy-MM-dd HH:mm:ss");
        try {
           list = orderService.getOrderBySupplierIdAndOrderStatusAndTime(supplierId,OrderStatus.CONFIRMED,DateTimeUtil.convertFormat(startTime, "yyyy-MM-dd HH:mm:ss"),
        		  DateTimeUtil.convertFormat(endTime,"yyyy-MM-dd HH:mm:ss"));
           //list = orderService.getOrderBySupplierIdAndOrderStatus(supplierId,"confirmed");
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        
        StringBuffer ftpFile = new StringBuffer();
        ftpFile.append("Purchasing number;PO Line;Item code;Description;Item supplier code;Price;Quantity;Size");
        ftpFile.append("\n");
        for (OrderDTO orderDTO:list){
        	try {
				ProductDTO product = productSearchService.findProductForOrder(supplierId,orderDTO.getDetail().split(":")[0]);
				ftpFile.append(orderDTO.getSpPurchaseNo());
				ftpFile.append(";").append("");
	            ftpFile.append(";").append(product.getProductCode());
	            ftpFile.append(";").append("");
	            ftpFile.append(";").append(orderDTO.getDetail().split(":")[0]);
	            ftpFile.append(";").append("");
	            ftpFile.append(";").append(orderDTO.getDetail().split(":")[1]);
	            ftpFile.append(";").append(product.getSize());
	            ftpFile.append("\n");
        	} catch (ServiceException e) {
				e.printStackTrace();
			}
        }
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
		orderDTO.setStatus(OrderStatus.PAYED);
		
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
