package com.shangpin.iog.levelgroup.monobrand.purchase.service;

import java.util.Map;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsOrderService;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
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
    private static String supplierNo = null;

    static {
        if(null==bdl){
            bdl=ResourceBundle.getBundle("conf");
        }
        supplierId = bdl.getString("supplierId");
        supplierNo = bdl.getString("supplierNo");
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

	@Override
	public void handleSupplierOrder(OrderDTO orderDTO) {
		logger.info("下单成功!");
		orderDTO.setStatus(OrderStatus.PAYED);
		
	}
	@Override
	public void handleConfirmOrder(OrderDTO orderDTO) {
		orderDTO.setExcState("0");
		logger.info("订单确认成功,订单状态为:"+orderDTO.getStatus());
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
