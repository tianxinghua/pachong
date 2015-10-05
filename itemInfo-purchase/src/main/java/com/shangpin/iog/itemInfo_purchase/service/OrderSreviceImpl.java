package com.shangpin.iog.itemInfo_purchase.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderDetail;

import com.shangpin.ice.ice.OrderService;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.itemInfo_purchase.dto.OrderDetailDTO;

/**
 * 下订单类，并且完成在线验证
 * @author sunny
 *
 */
public class OrderSreviceImpl {
	
	private static String startDate = null,endDate = null;
	private static final String YYYY_MMDD_HH = "yyyy-MM-dd HH:mm:ss";
	
	private static ResourceBundle bdl=null;
    private static String supplierId;
    private static String key ;
    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
        key = bdl.getString("key");
    }
    
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");
	
	/**
	 * 
	 */
	public void purchaseOrder(){
		
		//初始化时间
		initDate("date.ini");
		//正常流程下单
		
	}
	
	public void tranDate(){
		OrderService iceOrderService = new OrderService();
		//获取订单集合
		Map<String,List<PurchaseOrderDetail>> orderMap = null;
		try{
		
			List<Integer> status = new ArrayList<>();
	        status.add(1);
	        orderMap = iceOrderService.getPurchaseOrder(supplierId, startDate, endDate, status);
	        //订单信息
	        Iterator<Entry<String, List<PurchaseOrderDetail>>> iterator = orderMap.entrySet().iterator();
	        Map<String,Integer> stockMap = new HashMap<>();//保存不同产品的数量
	        String uuid = "";//作为存库OrderDTO的id
	        
	        while(iterator.hasNext()){
	        	Entry<String, List<PurchaseOrderDetail>> entry = iterator.next();
	        	//获取同一个产品的数量
	        	for(PurchaseOrderDetail orderDetail : entry.getValue()){
	        		if(stockMap.containsKey(orderDetail.SupplierSkuNo)){
	        			stockMap.put(orderDetail.SupplierSkuNo, stockMap.get(orderDetail.SupplierSkuNo)+1);
	        		}else{
	        			stockMap.put(orderDetail.SupplierSkuNo,1);
	        		}
	        	}
	        	//处理采购单
	        	StringBuffer orderDetailBuffer = new StringBuffer();//用来合并采购单明细
	        	List<OrderDetailDTO> orderDetailDTOList = new ArrayList<OrderDetailDTO>();//留到后边用
	        	StringBuffer detailBuffer = new StringBuffer(); 
	        	for(PurchaseOrderDetail orderDetail : entry.getValue()){
	        		//记录采购单明细信息 以便发货
	        		orderDetailBuffer.append(orderDetail.SupplierSkuNo + ";");
	        		//计算同一采购单的相同产品的数量
	        		OrderDetailDTO detailDTO = new OrderDetailDTO();
	        		detailDTO.setSku_id(Integer.parseInt(orderDetail.SupplierSkuNo));
	        		detailDTO.setQuantity(stockMap.get(orderDetail.SupplierSkuNo));
	        		orderDetailDTOList.add(detailDTO);
	        		//订单的详细信息
	        		detailBuffer.append(detailDTO.getSku_id()).append(":").append(detailDTO.getQuantity()).append(",");
	        		
	        		stockMap.remove(orderDetail.SupplierSkuNo);
	        		
	        	}
	        	//存库
	        	com.shangpin.iog.dto.OrderDTO spOrder =new com.shangpin.iog.dto.OrderDTO();
	        	uuid=UUID.randomUUID().toString();
	        	spOrder.setUuId(uuid);
	            spOrder.setSupplierId(supplierId);
	            spOrder.setStatus("WAITING");
	            spOrder.setSpOrderId(entry.getKey());
	            spOrder.setSpPurchaseDetailNo(orderDetailBuffer.toString());
	            spOrder.setDetail(detailBuffer.toString());
	            spOrder.setCreateTime(new Date());
	        	
	        }
	        
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	private static void initDate(String fileName){
		Date tempDate = new Date();

        endDate = DateTimeUtil.convertFormat(tempDate, YYYY_MMDD_HH);

        String lastDate=getLastGrapDate(fileName);
        startDate= StringUtils.isNotEmpty(lastDate) ? lastDate: DateTimeUtil.convertFormat(DateUtils.addDays(tempDate, -180), YYYY_MMDD_HH);

        writeGrapDate(endDate,fileName);

	}
	
	private static String getLastGrapDate(String fileName){
        File df;
        String dstr=null;
        try {
            df = getConfFile(fileName);
            try(BufferedReader br = new BufferedReader(new FileReader(df))){
                dstr=br.readLine();
            }
        } catch (IOException e) {
            logger.error("读取日期配置文件错误");
        }
        return dstr;
    }
	
	private static void writeGrapDate(String date,String fileName){
        File df;
        try {
            df = getConfFile(fileName);

            try(BufferedWriter bw = new BufferedWriter(new FileWriter(df))){
                bw.write(date);
            }
        } catch (IOException e) {
            logger.error("写入日期配置文件错误");
        }
    }
	
	private static File getConfFile(String fileName) throws IOException {
        String realPath = OrderSreviceImpl.class.getClassLoader().getResource("").getFile();
        realPath= URLDecoder.decode(realPath, "utf-8");
        File df = new File(realPath+fileName);//"date.ini"
        if(!df.exists()){
            df.createNewFile();
        }
        return df;
    }

}
