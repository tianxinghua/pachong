package com.shangpin.iog.Della.purchase.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.Della.purchase.common.MyFtpUtil;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.OrderDetailDTO;
import com.shangpin.iog.dto.ProductDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.service.OrderDetailService;
import com.shangpin.iog.service.ProductSearchService;

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
        supplierNo = bdl.getString("supplierNo");
        localFile = bdl.getString("localFile");
    }
    
    @Autowired
    ProductSearchService productSearchService;
    
    @Autowired
    OrderDetailService orderDetailService;
    
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
    public void saveAndUpLoadOrder(Date startTime,Date endTime){
        String orderFile = saveOrder(startTime,endTime);
        new MyFtpUtil().upload(orderFile);
    }
    /**
     * 
     * @throws ServiceException
     */
    private String saveOrder(Date startTime,Date endTime){
    	String fileName = null;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH");
		String dateStr = format.format(new Date());
		fileName = dateStr + ".csv";
    	List<OrderDetailDTO> orderDetails = null;
        logger.info("=========开始时间"+DateTimeUtil.convertFormat(startTime, "yyyy-MM-dd HH:mm:ss")+"到结束时间"+DateTimeUtil.convertFormat(endTime, "yyyy-MM-dd HH:mm:ss")+"的订单开始保存本地========="); 
        try {
        	orderDetails = orderDetailService.getOrderBySupplierIdAndOrderStatusAndUpdateTime(supplierId, OrderStatus.CONFIRMED, DateTimeUtil.convertFormat(startTime, "yyyy-MM-dd HH:mm:ss"), DateTimeUtil.convertFormat(endTime,"yyyy-MM-dd HH:mm:ss"));
        } catch (ServiceException e) {
        	loggerError.error(e); 
        }
        StringBuffer ftpFile = new StringBuffer();
        ftpFile.append("Purchasing number;PO Line;Item code;Description;Item supplier code;Price;Quantity;Size");
        ftpFile.append("\n");
        for (OrderDetailDTO orderDTO:orderDetails){
        	try {
				ProductDTO product = productSearchService.findProductForOrder(supplierId,orderDTO.getSupplierSku());
				ftpFile.append(orderDTO.getSpPurchaseNo());
				ftpFile.append(";").append("");
	            ftpFile.append(";").append(product.getProductCode());
	            ftpFile.append(";").append("");
	            ftpFile.append(";").append(orderDTO.getSupplierSku());
	            ftpFile.append(";").append("");
	            ftpFile.append(";").append(orderDTO.getQuantity());
	            ftpFile.append(";").append(product.getSize());
	            ftpFile.append("\n");
        	} catch (ServiceException e) {
        		loggerError.error(e); 
			}
        }
        logger.info("=========开始时间"+DateTimeUtil.convertFormat(startTime, "yyyy-MM-dd HH:mm:ss")+"的订单========\n"+ftpFile.toString());
        FileWriter fwriter = null;
        try {
        	File file = new File(localFile);
        	if(!file.exists()){
        		file.mkdirs();
        	}
            fwriter = new FileWriter(localFile+File.separator+fileName);
            fwriter.write(ftpFile.toString());
        } catch (IOException ex) {
        	loggerError.error(ex); 
        } finally {
            try {
                fwriter.flush();
                fwriter.close();
            } catch (IOException ex) {
            	loggerError.error(ex); 
            }
        }
        logger.info("========="+startTime+"到"+endTime+"的订单保存本地完毕 "+localFile+File.separator+fileName);
        return localFile+File.separator+fileName;
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
