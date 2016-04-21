package com.shangpin.iog.levelgroup.purchase.service;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import com.shangpin.iog.product.service.OrderServiceImpl;
import com.shangpin.iog.service.ProductSearchService;

/**
 * Created by Administrator on 2015/11/20.
 */
@Component
public class OrderService extends AbsOrderService {
	
	static Logger log = LoggerFactory.getLogger(OrderService.class);

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("info");

    private static ResourceBundle bdl = null;
    private static  String supplierId = null;
    private static  String supplierId2 = null;
    private static String supplierNo = null;
    private static String localFile = null;
    private static String localFile2 = null;
//    private static String startTime = null;
//    private static String endTime = null;

    static {
        if(null==bdl){
            bdl=ResourceBundle.getBundle("conf");
        }
        supplierId = bdl.getString("supplierId");
        supplierId2 = bdl.getString("supplierId2");
        supplierNo = bdl.getString("supplierNo");
        localFile = bdl.getString("localFile");
        localFile2 = bdl.getString("localFile2");
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

     // 创建订单信息并上传到ftp
    public void saveAndUpLoadOrder(){
    	logger.info("生成订单并上传");
        saveOrder();
        new MyFtpUtil().upLoad();
    }
    
    //创建并上传取消的订单
    public void saveAndUpLoadCancelOrder(){
    	saveCancelOrder();
    	new MyFtpUtil().uploadCancel();
    }
    /**
     * 
     * @throws ServiceException
     */
    private void saveOrder(){
        //List<OrderDTO> list = new ArrayList<>();
        List<OrderDTO> list1 = null;
        List<OrderDTO> list2 = null;
        Date startTime = new Date();
        Date endTime = new Date();
        startTime =DateTimeUtil.convertFormat(
        		DateTimeUtil.shortFmt(DateTimeUtil.getAppointDayFromSpecifiedDay(startTime, -1, "D"))+" 00:00:00", "yyyy-MM-dd HH:mm:ss");
        endTime =DateTimeUtil.convertFormat(DateTimeUtil.shortFmt(endTime)+" 00:00:00", "yyyy-MM-dd HH:mm:ss");
        try {
           list1 = orderService.getOrderBySupplierIdAndOrderStatusAndUpdateTime(supplierId,OrderStatus.CONFIRMED,DateTimeUtil.convertFormat(startTime, "yyyy-MM-dd HH:mm:ss"),
        		  DateTimeUtil.convertFormat(endTime,"yyyy-MM-dd HH:mm:ss"));
           list2 = orderService.getOrderBySupplierIdAndOrderStatusAndUpdateTime(supplierId2,OrderStatus.CONFIRMED,DateTimeUtil.convertFormat(startTime, "yyyy-MM-dd HH:mm:ss"),
         		  DateTimeUtil.convertFormat(endTime,"yyyy-MM-dd HH:mm:ss"));
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        
        StringBuffer ftpFile = new StringBuffer();
        ftpFile.append("ORDER CODE;ITEM CODE;SIZE;SKU;ORDER;PRICE;BRAND;STATUS");
        ftpFile.append("\n");
//        System.out.println("list1长度为:"+list1.size());
//        System.out.println("list2长度为:"+list2.size());
        
        for (OrderDTO orderDTO:list1){
        	try {
				ProductDTO product = productSearchService.findProductForOrder(supplierId,orderDTO.getDetail().split(":")[0]);
				ftpFile.append(orderDTO.getSpPurchaseNo());
	            if(product!=null){
	            	ftpFile.append(";").append(product.getProductCode());
	            	ftpFile.append(";").append(product.getSize());
	            	if(orderDTO.getDetail().split(":")[0].length()<15){
	            		ftpFile.append(";").append("09").append(orderDTO.getDetail().split(":")[0]);
	            	}else{
	            		ftpFile.append(";").append(orderDTO.getDetail().split(":")[0]);
	            	}
		            ftpFile.append(";").append(orderDTO.getDetail().split(":")[1]);
		            
		            if(orderDTO.getPurchasePriceDetail()!=null){
	            		BigDecimal priceInt = new BigDecimal(orderDTO.getPurchasePriceDetail());
						String price = priceInt.divide(new BigDecimal(1.05),5).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
						ftpFile.append(";").append(price.trim());
		            }else{
	            		String price = "";
	            		ftpFile.append(";").append(price);
	            	}
		            ftpFile.append(";").append(product.getBrandName());
		            ftpFile.append(";").append(orderDTO.getStatus());
		            
		            logger.info("SkuID="+orderDTO.getDetail().split(":")[0]+"采购单号:"+orderDTO.getSpPurchaseNo());
		           // System.out.println("订单1采购单号="+orderDTO.getSpPurchaseNo());
	            }else{
	            	ftpFile.append(";").append("");
	            	ftpFile.append(";").append("");
	            	if(orderDTO.getDetail().split(":")[0].length()<15){
	            		ftpFile.append(";").append("09").append(orderDTO.getDetail().split(":")[0]);
	            	}else{
	            		ftpFile.append(";").append(orderDTO.getDetail().split(":")[0]);
	            	}
		            ftpFile.append(";").append(orderDTO.getDetail().split(":")[1]);
		            ftpFile.append(";").append(0);
		            ftpFile.append(";").append("");
		            ftpFile.append(";").append("pre-sale");
	            }
	            
	            ftpFile.append("\n");
        	} catch (ServiceException e) {
				e.printStackTrace();
			}
        }
        
        for (OrderDTO orderDTO2:list2){
        	try {
				ProductDTO product2 = productSearchService.findProductForOrder(supplierId2,orderDTO2.getDetail().split(":")[0]);
				ftpFile.append(orderDTO2.getSpPurchaseNo());
	            if(product2!=null){
	            	ftpFile.append(";").append(product2.getProductCode());
	            	ftpFile.append(";").append(product2.getSize());
	            	if(orderDTO2.getDetail().split(":")[0].length()<15){
	            		ftpFile.append(";").append("09").append(orderDTO2.getDetail().split(":")[0]);
	            	}else{
	            		ftpFile.append(";").append(orderDTO2.getDetail().split(":")[0]);
	            	}
		            ftpFile.append(";").append(orderDTO2.getDetail().split(":")[1]);
		            
		            if(orderDTO2.getPurchasePriceDetail()!=null){
//		            	System.out.println("数据2="+orderDTO.getPurchasePriceDetail());
//		            	System.out.println("skuId="+orderDTO.getDetail().split(":")[0]);
//	            		BigDecimal priceInt = new BigDecimal(orderDTO.getPurchasePriceDetail());
//						String price = priceInt.divide(new BigDecimal(1.05),5).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
//						ftpFile.append(";").append(price.trim());
						ftpFile.append(";").append(orderDTO2.getPurchasePriceDetail());
		            }else{
	            		String price = "";
	            		ftpFile.append(";").append(price);
	            	}
		            ftpFile.append(";").append(product2.getBrandName());
		            ftpFile.append(";").append(orderDTO2.getStatus());
		            logger.info("SkuID="+orderDTO2.getDetail().split(":")[0]+"采购单号:"+orderDTO2.getSpPurchaseNo());
		            //System.out.println("订单2采购单号="+orderDTO2.getSpPurchaseNo());
	            }else{
	            	ftpFile.append(";").append("");
	            	ftpFile.append(";").append("");
	            	if(orderDTO2.getDetail().split(":")[0].length()<15){
	            		ftpFile.append(";").append("09").append(orderDTO2.getDetail().split(":")[0]);
	            	}else{
	            		ftpFile.append(";").append(orderDTO2.getDetail().split(":")[0]);
	            	}
		            ftpFile.append(";").append(orderDTO2.getDetail().split(":")[1]);
		            ftpFile.append(";").append(0);
		            ftpFile.append(";").append("");
		            ftpFile.append(";").append("pre-sale");
	            }
	            
	            ftpFile.append("\n");
        	} catch (ServiceException e) {
				e.printStackTrace();
			}
        }
        
        Map<String, String> mongMap = new HashMap<>();
        mongMap.put("supplierId", supplierId);
        mongMap.put("supplierName", "LevelGroup");
        mongMap.put("result", ftpFile.toString());
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
    
    
    private void saveCancelOrder(){
        List<OrderDTO> list = null;
        Date startTime = new Date();
        Date endTime = new Date();
        startTime =DateTimeUtil.convertFormat(
        		DateTimeUtil.shortFmt(DateTimeUtil.getAppointDayFromSpecifiedDay(startTime, -1, "D"))+" 00:00:00", "yyyy-MM-dd HH:mm:ss");
        endTime =DateTimeUtil.convertFormat(DateTimeUtil.shortFmt(endTime)+" 00:00:00", "yyyy-MM-dd HH:mm:ss");
        try {
           list = orderService.getOrderBySupplierIdAndOrderStatusAndUpdateTime(supplierId,OrderStatus.REFUNDED,DateTimeUtil.convertFormat(startTime, "yyyy-MM-dd HH:mm:ss"),
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
	            if(product!=null){
	            	ftpFile.append(";").append(product.getProductCode());
	            	ftpFile.append(";").append(product.getSize());
	            	if(orderDTO.getDetail().split(":")[0].length()<15){
	            		ftpFile.append(";").append("09").append(orderDTO.getDetail().split(":")[0]);
	            	}else{
	            		ftpFile.append(";").append(orderDTO.getDetail().split(":")[0]);
	            	}
		            ftpFile.append(";").append(orderDTO.getDetail().split(":")[1]);
		            
		            if(orderDTO.getPurchasePriceDetail()!=null){
	            		BigDecimal priceInt = new BigDecimal(orderDTO.getPurchasePriceDetail());
						String price = priceInt.divide(new BigDecimal(1.05),5).setScale(0, BigDecimal.ROUND_HALF_UP).toString();
						ftpFile.append(";").append(price.trim());
		            }else{
	            		String price = "";
	            		ftpFile.append(";").append(price);
	            	}
		            ftpFile.append(";").append(product.getBrandName());
		            ftpFile.append(";").append(orderDTO.getStatus());
		            
	            }else{
	            	ftpFile.append(";").append("");
	            	ftpFile.append(";").append("");
	            	if(orderDTO.getDetail().split(":")[0].length()<15){
	            		ftpFile.append(";").append("09").append(orderDTO.getDetail().split(":")[0]);
	            	}else{
	            		ftpFile.append(";").append(orderDTO.getDetail().split(":")[0]);
	            	}
		            ftpFile.append(";").append(orderDTO.getDetail().split(":")[1]);
		            ftpFile.append(";").append(0);
		            ftpFile.append(";").append("");
		            ftpFile.append(";").append("pre-sale");
	            }
	            
	            ftpFile.append("\n");
        	} catch (ServiceException e) {
				e.printStackTrace();
			}
        
        }
        Map<String, String> mongMap = new HashMap<>();
        mongMap.put("supplierId", supplierId);
        mongMap.put("supplierName", "LevelGroup");
        mongMap.put("result", ftpFile.toString());
        FileWriter fwriter = null;
        try {
            fwriter = new FileWriter(localFile2);
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
