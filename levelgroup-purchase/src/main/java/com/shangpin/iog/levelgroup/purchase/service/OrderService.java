package com.shangpin.iog.levelgroup.purchase.service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ProductDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.levelgroup.purchase.common.MyFtpUtil;
import com.shangpin.iog.levelgroup.purchase.common.OrderState;
import com.shangpin.iog.product.service.OrderServiceImpl;
import com.shangpin.iog.service.ProductSearchService;

/**
 * Created by Administrator on 2015/11/20.
 */
@Component
public class OrderService extends AbsOrderService {
	
	static Logger log = LoggerFactory.getLogger(OrderService.class);

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("info");
    private static org.apache.log4j.Logger loggerError = org.apache.log4j.Logger.getLogger("error");

    private static ResourceBundle bdl = null;
    private static  String supplierId = null;
    private static String supplierNo = null;
    private static String localFile = null;
//    private static String startTime = null;
//    private static String endTime = null;

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
 		logger.info("订单确认");
 		this.confirmOrder(supplierId);
 	}

    /**
     * 创建订单信息并上传到ftp
     * @throws ServiceException
     */
    public void saveAndUpLoadOrder(){
    	logger.info("生成订单并上传");
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
           list = orderService.getOrderBySupplierIdAndOrderStatusAndUpdateTime(supplierId,OrderStatus.CONFIRMED,DateTimeUtil.convertFormat(startTime, "yyyy-MM-dd HH:mm:ss"),
        		  DateTimeUtil.convertFormat(endTime,"yyyy-MM-dd HH:mm:ss"));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        
        StringBuffer ftpFile = new StringBuffer();
        ftpFile.append("ORDER CODE;ITEM CODE;SIZE;SKU;ORDER;PRICE;BRAND;STATUS");
        ftpFile.append("\n");
        for (OrderDTO orderDTO:list){
        	try {
				ProductDTO product = productSearchService.findProductForOrder(supplierId,orderDTO.getDetail().split(":")[0]);
				ftpFile.append(orderDTO.getSpPurchaseNo());
	            ftpFile.append(";").append(orderDTO.getSpPurchaseDetailNo());
	            if(product!=null){
	            	ftpFile.append(product.getSize());
	            	ftpFile.append(";").append(orderDTO.getDetail().split(":")[0]);
		            ftpFile.append(";").append(orderDTO.getDetail().split(":")[1]);
		            ftpFile.append(";").append(product.getNewSupplierPrice());
		            ftpFile.append(";").append(product.getBrandName());
		            ftpFile.append(";").append(orderDTO.getStatus());
	            }else{
	            	ftpFile.append(" ");
	            	ftpFile.append(";").append(orderDTO.getDetail().split(":")[0]);
		            ftpFile.append(";").append(orderDTO.getDetail().split(":")[1]);
		            ftpFile.append(";").append(0);
		            ftpFile.append(";").append(" ");
		            ftpFile.append(";").append(orderDTO.getStatus());
	            }
	            
	            ftpFile.append("\n");
        	} catch (ServiceException e) {
				e.printStackTrace();
			}
        
        }
        ///////////////////////////////////////////////////////////////////////////////////////
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
		logger.info("下单成功!");
		orderDTO.setStatus(OrderStatus.PAYED);
		
	}
	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		orderDTO.setExcState("0");
		logger.info("订单确认成功,订单状态为:"+orderDTO.getStatus());
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
