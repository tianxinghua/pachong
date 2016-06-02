package com.shangpin.ice.ice;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;

import ShangPin.SOP.Api.ApiException;
import com.shangpin.iog.dto.OrderDetailDTO;
import com.shangpin.iog.service.OrderDetailService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ShangPin.SOP.Entity.Api.Purchase.DeliveryOrderAdd;
import ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderDetail;
import ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderDetailPage;
import ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderDetailSpecial;
import ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderDetailSpecialPage;
import ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderEx;
import ShangPin.SOP.Entity.Where.OpenApi.Purchase.PurchaseOrderQueryDto;
import ShangPin.SOP.Servant.OpenApiServantPrx;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.SendMail;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.common.utils.logger.LoggerUtil;
import com.shangpin.iog.common.utils.redis.JedisClient;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.dto.SkuRelationDTO;
import com.shangpin.iog.ice.dto.ICEOrderDetailDTO;
import com.shangpin.iog.ice.dto.ICEWMSOrderDTO;
import com.shangpin.iog.ice.dto.ICEWMSOrderRequestDTO;
import com.shangpin.iog.ice.dto.OrderStatus;
import com.shangpin.iog.ice.dto.ResMessage;
import com.shangpin.iog.service.ReturnOrderService;
import com.shangpin.iog.service.SkuPriceService;
import com.shangpin.iog.service.SkuRelationService;
import org.springframework.core.annotation.Order;

/**
 * Created by loyalty on 15/9/9.
 */
public abstract class AbsOrderService {

    private static String  startDate=null,endDate=null;
    private static String  startDateOfWMS=null,endDateOfWMS=null;
    private static final String YYYY_MMDD_HH = "yyyy-MM-dd HH:mm:ss";
    private static final String YYYY_MMDD_HH_WMS = "yyyy-MM-dd 'T' HH:mm:ss";

    static Logger log = LoggerFactory.getLogger(AbsOrderService.class);

    private static String toEmail;
    private static String fromEmail;
    private static String emailPass;

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("info");
    private static LoggerUtil loggerError = LoggerUtil.getLogger("error");

//    static String url="/purchase/createdeliveryorder";
    private static ResourceBundle bdl = null;
    private static ResourceBundle bd = null;
    private static String  startDateOfTemp=null,endDateOfTemp=null;
    private static  String url = null;
    private static  String redisUrl = null;  
    public static boolean SENDMAIL = false;
    private static JedisClient j = null;
	static {
        try {
            if(null==bdl){
                bdl=ResourceBundle.getBundle("openice");
            }
            if(null==bd){
                bd=ResourceBundle.getBundle("conf");
            }
            url = bdl.getString("wmsUrl");
            redisUrl = bdl.getString("redisUrl"); 
            toEmail = bdl.getString("email");
            fromEmail = bdl.getString("fromEmail");
            emailPass = bdl.getString("emailPass");
            startDateOfTemp=bd.getString("startDateOfTemp");
            endDateOfTemp = bd.getString("endDateOfTemp");
            j = JedisClient.getInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Autowired
	public SkuPriceService skuPriceService;

    @Autowired
    com.shangpin.iog.service.OrderService productOrderService;

    @Autowired
    OrderDetailService orderDetailService;

    @Autowired
    ReturnOrderService returnOrderService;

    @Autowired
    SkuRelationService skuRelationService;


    /**
     * 处理供货商订单信息
     * @param orderDTO
     * 订单状态需求参是 orderDTO 里的状态
     * @throws ServiceException
     */
    abstract  public void handleSupplierOrder(OrderDTO orderDTO) ;

    /**
     * 订单从下单到支付后的处理
     * @param orderDTO  订单信息
     */
    abstract public void handleConfirmOrder(OrderDTO orderDTO);

    /**
     * 取消订单 （未支付）
     * @throws ServiceException
     */
    abstract  public void handleCancelOrder(ReturnOrderDTO deleteOrder) ;

    /**
     * 退款
     * @param deleteOrder
     */
    abstract  public void handleRefundlOrder(ReturnOrderDTO deleteOrder) ;

    /**
     * 发送邮件
     * @param orderDTO
     */
    abstract public void handleEmail(OrderDTO orderDTO);

    /**
     * 获取真正的供货商编号
     * @param skuMap  key skuNo ,value supplerSkuNo
     * @return
     * @throws ServiceException
     */
    abstract public void getSupplierSkuId(Map<String,String> skuMap) throws ServiceException;

    /**
     *  通过采购单处理下单 包括下单和退单
     * @param supplierId 门户编号  201****
     *
     * @param supplierNo 供货商编号  S*****
     *
     * @param handleCancel 是否处理退款
     */
    public  void  checkoutOrderFromSOP(String supplierId,String supplierNo,boolean handleCancel){

        //初始化时间
        initDate("date.ini");
        
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MMDD_HH);
        String date = sdf.format(new Date());
        j.setValueOnAsync("iog_"+supplierId,date);
        //处理异常
        handlePurchaseOrderException(supplierId);

        handleCancelOrderException(supplierId);

        handleRefundPurchaseOrderException(supplierId);

        //处理订单
        handleOrderOfSOP(supplierId, supplierNo);
        //处理退款
        refundOrderFromSOP(supplierNo, supplierId, handleCancel);

        //时刻更新时间，以便监测程序是否运行
        updateOrderTime(supplierId);

    }

    private void updateOrderTime(String supplierId) {
		//先判断是否supplier已存在
//    	boolean flag = productOrderService.selectOrderUpdateBySupplier(supplierId);
//    	if(flag){
//    		productOrderService.updateSupplierOrderTime(supplierId);
//    	}else{
//    		productOrderService.saveSupplierOrderTime(supplierId);
//    	}
	}

	/**
     * 通过WMS下单 包换退单的处理
     * @param supplierNo 供货商编号    S******
     * @param supplierId 供货商门户编号 2015****
     */
    public  void  checkoutOrderFromWMS(String supplierNo,String supplierId,boolean handleCancel){
        //初始化时间
        initWMSDate("dateWMS.ini");
        
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MMDD_HH);
        String date = sdf.format(new Date());
        j.setValueOnAsync("iog_"+supplierId,date);

        //获取订单数组
        Gson gson = new Gson();
        ICEWMSOrderRequestDTO  dto = new ICEWMSOrderRequestDTO();
        logger.info("startDateOfWMS ="+startDateOfWMS);
        logger.info("endDateOfWMS="+endDateOfWMS);
        dto.setBeginTime(startDateOfWMS);
        dto.setEndTime(endDateOfWMS);
        dto.setSupplierNo(supplierNo);

        String jsonParameter= "="+ gson.toJson(dto);
        String result ="";
        result = getOrderInfoFromWMS(jsonParameter, result);
        List<ICEWMSOrderDTO> orderDTOList  = null;
        try {
            orderDTOList = gson.fromJson(result,new TypeToken<List<ICEWMSOrderDTO>>(){}.getType());
        } catch (JsonSyntaxException e) {
            loggerError.error("获取"+supplierNo +"供货商，wms订单转化异常,退出");
            Thread t = new Thread(new MailThread(supplierId,supplierId+" 线上订单发生错误","获取订单信息失败，无信息返回。"));
            t.start();
            try {
            	result = getOrderInfoFromWMS(jsonParameter, result);
            	orderDTOList = gson.fromJson(result,new TypeToken<List<ICEWMSOrderDTO>>(){}.getType());
			} catch (Exception e2) {
				loggerError.error("第二次获取"+supplierNo +"供货商，wms订单转化异常,退出");
	            Thread t1 = new Thread(new MailThread(supplierId,supplierId+" 线上订单发生错误","获取订单信息失败，无信息返回。"));
	            t1.start();
	            return;
			}           
            
        }
        String uuid="",skuNo="";
        //由于拉取时可能更改供货商的SKU的编号需要 继承者确认
        Map<String,String> skuMap = new HashMap<>();
        List<ICEWMSOrderDTO> orderList = new ArrayList<>();
        List<ICEWMSOrderDTO> refundList = new ArrayList<>();
        if(null!=orderDTOList) {
            for(ICEWMSOrderDTO icewmsOrderDTO:orderDTOList){
                SkuRelationDTO skuRelationDTO= null;
                if(icewmsOrderDTO.getChangeForOrderQuantity()<0){   //订单
                    orderList.add(icewmsOrderDTO);
                }else{
                    refundList.add(icewmsOrderDTO);
                }
                try {
//                    skuRelationDTO=  skuRelationService.getSkuRelationBySkuId(icewmsOrderDTO.getSkuNo());
                      skuRelationDTO = skuRelationService.getSkuRelationBySupplierIdAndSkuId(supplierId,icewmsOrderDTO.getSkuNo());
                    if(null!=skuRelationDTO){
                        skuMap.put(skuRelationDTO.getSopSkuId(), skuRelationDTO.getSupplierSkuId());
                    }else{   //获取供货商的SKU编号

                    }
                } catch (ServiceException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            //获取真正的供货商SKUID
            this.getSupplierSkuId(skuMap);
        } catch (ServiceException e) {
            e.printStackTrace();
        }

        // 检查订单是否已经被支付  若支付修改其状态
        checkPayed(supplierId);

        //处理推送异常
        handlePurchaseOrderException(supplierId);
        //处理取消异常
        handleCancelOrderException(supplierId);
       //处理退款异常
        handleRefundPurchaseOrderException(supplierId);

        //处理订单
        handleOrderOfWMS(supplierNo, supplierId, skuMap, orderList);

        //查找漏单的信息（重新采购、财务补单）
        String  startTime = "";
        Date tmpDate =  DateTimeUtil.getAppointDayFromSpecifiedDay(DateTimeUtil.convertFormat(startDateOfWMS,YYYY_MMDD_HH_WMS),-20,"m");
        startTime = DateTimeUtil.convertFormat(tmpDate,YYYY_MMDD_HH_WMS) ;
        handleOrderOfSOPForSpecial(supplierId,supplierNo,startTime,endDateOfWMS);

        //处理退单
        handleCancelOfWMS(supplierNo, supplierId, skuMap, refundList,handleCancel);

        //处理退款
        handleRefundOrderAndEmailOfWMS(supplierNo, supplierId);
    }

	private String getOrderInfoFromWMS(String jsonParameter, String result) {
		try {
        	
            result =  HttpUtil45.operateData("post","form",url+"/Api/StockQuery/SupplierInventoryLogQuery",new OutTimeConfig(1000*5,1000*30,1000*30),null,
                    jsonParameter,"","");
            logger.info("获取的订单信息为:" + result);
            System.out.println("kk = " + result);
            result =  result.substring(1,result.length()-1).replace("\\","");
        } catch (ServiceException e) {
            e.printStackTrace();
        }
		return result;
	}



    /**
     * 订单确认
     * @param supplierId
     */
    public void confirmOrder(String supplierId){
 
        List<OrderDetailDTO> detailDTOList = null;
        try {
        	detailDTOList  =orderDetailService.getOrderDetailBySupplierIdAndOrderStatus(supplierId,OrderStatus.PAYED);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        if(null!=detailDTOList){
            for(OrderDetailDTO detailDTO :detailDTOList){
            	com.shangpin.iog.dto.OrderDTO orderDTO = this.transOrderDatailToOrder(detailDTO);
                //订单支付后的处理
                handleConfirmOrder(orderDTO);
                updateOrderMsg(orderDTO);
            }

        }

    }

    /**
     * 采购异常 推送采购单下单异常
     * @param orderDTO 订单信息
     *                 @return -1:不做处理  1：成功  0：失败
     */
    public  String   setPurchaseOrderExc(OrderDTO orderDTO) {
        try {
            logger.info("采购单 " + orderDTO.getSpPurchaseNo() +" 推送异常订单状态 " +
                    ":"+ orderDTO.getStatus()+"----");
            if(!orderDTO.getStatus().equals(OrderStatus.PAYED)){
                return "-1";
            }

            List<Long> sopPurchaseOrderDetailNos = new ArrayList<>();

            if(null==orderDTO||StringUtils.isBlank(orderDTO.getSpPurchaseDetailNo())){
                loggerError.error("采购单明细为空，无法设置采购异常");
                return "0" ;

            }
            String[] purchaseOrderDetailArray = orderDTO.getSpPurchaseDetailNo().split(";");
            if(null!=purchaseOrderDetailArray){
                for(String purchaseDetailNo:purchaseOrderDetailArray){
                    if(org.apache.commons.lang.StringUtils.isNotBlank(purchaseDetailNo)){
                        try {
                            sopPurchaseOrderDetailNos.add(Long.valueOf(purchaseDetailNo));
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                            loggerError.error("采购单明细转化数据类型时失败。");
                            return "0" ;
                        }
                    }
                }
                OpenApiServantPrx servant = null;
                try {
                    servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                String excDesc = orderDTO.getExcDesc();
                if(excDesc!=null){
                	if(excDesc.length()>100){
                		excDesc = excDesc.substring(0,100);
                	}
                }
                logger.info("推送取消采购单发生的参数：sopPurchaseOrderDetailNos："+sopPurchaseOrderDetailNos+"excDesc:"+orderDTO.getExcDesc());
                PurchaseOrderEx purchaseOrderEx = new PurchaseOrderEx(sopPurchaseOrderDetailNos,sopPurchaseOrderDetailNos+":"+excDesc);
                String  result = servant.PurchaseDetailEx(purchaseOrderEx,orderDTO.getSupplierId()+"");
                Gson gson = new Gson();
                ResMessage message = gson.fromJson(result,ResMessage.class);
                if(null==message){
                    logger.error(orderDTO.getSpPurchaseNo()+"推送取消采购单失败，无信息返回。");
                    Thread t = new Thread(new MailThread(orderDTO.getSupplierId(),orderDTO.getSupplierId()+" 线上发生错误","推送取消采购单失败，无信息返回。"));
                    t.start();


                }else {
                    if (200 != message.getResCode()) {
                        logger.error(orderDTO.getSpPurchaseNo()+"推送取消采购单失败");
                        return "0";
                    }
                }

            }

        } catch (Exception e) {
            loggerError.error("供货商门户："+orderDTO.getSupplierId() + " 采购单： " +orderDTO.getSpPurchaseNo()+"推送取消采购单失败.原因："+e.getMessage());
            return "0";
        }
        return "1";

    }


    /**
     * 检查订单是否支付
     * @param supplierId
     */
    private  void checkPayed(String supplierId){


        List<OrderDTO>  orderDTOList= new ArrayList<>();
        List<OrderDetailDTO> orderDetailDTOList = null;
        try {
            //获取已下单的订单信息
        	String nowDate = DateTimeUtil.getDateTime();

            orderDetailDTOList = orderDetailService.getOrderDetailBySupplierIdAndOrderStatus(supplierId, OrderStatus.PLACED);

//            orderDTOList  =productOrderService.getOrderBySupplierIdAndOrderStatus(supplierId, OrderStatus.PLACED);

//            if(null!=orderDetailDTOList){
//                for(OrderDetailDTO detailDTO:orderDetailDTOList){
//                    orderDTOList.add(this.transOrderDatailToOrder(detailDTO));
//                }
//            }

            //判断12个小时还是未推送状态的 如果已经支付 就赋值成支付  待确认支付推送时 继承者handlerConfirm返回错误  使其赋值为采购异常
            List<OrderDetailDTO>  waitList = orderDetailService.getOrderBySupplierIdAndOrderStatus(supplierId, OrderStatus.WAITPLACED,nowDate);
//            if(null!=waitList){
//                for(OrderDetailDTO detailDTO:orderDetailDTOList){
//                    orderDTOList.add(this.transOrderDatailToOrder(detailDTO));
//                }
//            }
            if(null==orderDetailDTOList){
                orderDetailDTOList = new ArrayList<>();
            }
            orderDetailDTOList.addAll(waitList);
            
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        try {
            // 获取采购单    更新订单状态
            OpenApiServantPrx servant = null;
            try {
                servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String sopPurchaseOrderNo ="";
//
//            List<String> orderIdList =  new ArrayList<>();
//            int i = 1;
//            String orders="";
//            for(OrderDTO orderDTO:orderDTOList){
//                orders = orders + orderDTO.getSpOrderId() +",";
//                if(i%20==0) {
//                     orderIdList.add(orders.substring(0,orders.length()-1));
//                     i=1;
//                     orders = "";
//                    continue;
//
//                }
//                i++;
//            }
//            if(!orders.equals("")){
//                orderIdList.add(orders.substring(0,orders.length()-1));
//            }
//
//            for(String someOrder:orderIdList){
//                PurchaseOrderDetailSpecialPage  orderDetailSpecialPage = servant.FindPurchaseOrderDetailSpecial(supplierId,"",orderDTO.getSpOrderId());
//                if(null!=orderDetailSpecialPage&&null!=orderDetailSpecialPage.PurchaseOrderDetails&&orderDetailSpecialPage.PurchaseOrderDetails.size()>0){  //存在采购单 就代表已支付
//                    //更新其已支付状态
//
//                    for (PurchaseOrderDetailSpecial orderDetail : orderDetailSpecialPage.PurchaseOrderDetails) {
//                        sopPurchaseOrderNo  = orderDetail.SopPurchaseOrderNo;
//                        if(purchaseOrderMap.containsKey(sopPurchaseOrderNo)){
//                            //
//
//
//                        }else{
//                            orderDTO.setSpPurchaseNo(sopPurchaseOrderNo);
//                            purchaseOrderMap.put(sopPurchaseOrderNo,"");
//                            if(5!=orderDetail.DetailStatus){ //5 为退款  1=待处理，2=待发货，3=待收货，4=待补发，5=已取消，6=已完成
//                                orderDTO.setStatus(OrderStatus.PAYED);
//                            }else{
//                                orderDTO.setStatus(OrderStatus.CANCELLED);
//                            }
//                            productOrderService.update(orderDTO);
//                        }
//
//
//                    }
//
//
//                }
//            }

            String spOrderNo="",spSku="";
            boolean isNew = true;

            Map<String,String>  spMasterOrderNoMap = new HashMap<>();

            Map<String,List<PurchaseOrderDetailSpecial>>  purchaseOrderMap = new HashMap<>();

            start:
            for(OrderDetailDTO detailDTO:orderDetailDTOList) {

                if ( detailDTO.getEpMasterOrderNo().indexOf("-") > 0) {//重新采购 或者 财务确认重新生成的采购单
                    detailDTO.setStatus(OrderStatus.PAYED);//如果退款了 无所谓 临时保存为支付状态 后续有退款的处理
                    try {
                        orderDetailService.update(detailDTO);
                    } catch (ServiceException e1) {
                        loggerError.error("重新采购的采购单更新状态时失败  ");
                        e1.printStackTrace();
                    }

                } else {//订单推送

                    //获取真正的主订单号
                    spOrderNo = detailDTO.getSpMasterOrderNo();
//                    if(spOrderNo.indexOf("|")>0){
//                        spOrderNo = spOrderNo.substring(0,spOrderNo.indexOf("|"));
//                        spSku=orderDTO.getSpOrderId().substring(orderDTO.getSpOrderId().indexOf("|")+1,orderDTO.getSpOrderId().length());
//                        isNew = true;
//                    }else{
//                        spOrderNo = orderDTO.getSpOrderId();
//                        isNew = false;
//                    }
                    if (!spMasterOrderNoMap.containsKey(spOrderNo)) {
                        PurchaseOrderDetailSpecialPage orderDetailSpecialPage = null;
                        try {
                            orderDetailSpecialPage = servant.FindPurchaseOrderDetailSpecial(supplierId, "", spOrderNo);
                        } catch (ApiException e1) {
                            e1.printStackTrace();
                        }
                        logger.info("查询是否支付，订单号:" + spOrderNo);
                        if (null != orderDetailSpecialPage && null != orderDetailSpecialPage.PurchaseOrderDetails && orderDetailSpecialPage.PurchaseOrderDetails.size() > 0) {  //存在采购单 就代表已支付
                        	spMasterOrderNoMap.put(spOrderNo, "");
                            for (PurchaseOrderDetailSpecial orderDetail : orderDetailSpecialPage.PurchaseOrderDetails) {
                                sopPurchaseOrderNo = orderDetail.SopPurchaseOrderNo;
                                if (purchaseOrderMap.containsKey(sopPurchaseOrderNo)) {
                                    purchaseOrderMap.get(sopPurchaseOrderNo).add(orderDetail);
                                } else {
                                    List<PurchaseOrderDetailSpecial> orderList = new ArrayList<>();
                                    orderList.add(orderDetail);
                                    purchaseOrderMap.put(sopPurchaseOrderNo, orderList);
                                }
                            }
                        }
                    }

                    StringBuffer purchaseOrderDetailbuffer = new StringBuffer();
                    StringBuffer purchaseNobuffer = new StringBuffer();
                    for (Iterator<Map.Entry<String, List<PurchaseOrderDetailSpecial>>> itor = purchaseOrderMap.entrySet().iterator(); itor.hasNext(); ) {
                        Map.Entry<String, List<PurchaseOrderDetailSpecial>> entry = itor.next();
                        sopPurchaseOrderNo = entry.getKey();
                        List<PurchaseOrderDetailSpecial> purchaseOrderDetailSpecialList =  entry.getValue();
                        for (int i=0;i<purchaseOrderDetailSpecialList.size();i++) {
                            PurchaseOrderDetailSpecial purchaseOrderDetail = purchaseOrderDetailSpecialList.get(i);
                            if (detailDTO.getSpSku().equals(purchaseOrderDetail.SkuNo)) { //与ORDER 同一个商品
                            	
                            	//两种情况：1 新插入的,spPurchaseNo 为空 2 重新采购的 有值
                            	
                            	if (StringUtils.isEmpty(detailDTO.getSpPurchaseNo())) {
                            	     purchaseOrderDetailbuffer.append(purchaseOrderDetail.SopPurchaseOrderDetailNo).append(";");
                                     purchaseNobuffer.append(purchaseOrderDetail.SopPurchaseOrderNo).append(";");
                                     //赋值状态 海外商品每个采购单 只有一种商品
                                     detailDTO.setPurchasePriceDetail(purchaseOrderDetail.SkuPrice);
                                     detailDTO.setSpOrderDetailNo(purchaseOrderDetail.OrderDetailNo);
                                     detailDTO.setSpPurchaseNo(sopPurchaseOrderNo);
                                     detailDTO.setSpPurchaseDetailNo(purchaseOrderDetail.SopPurchaseOrderDetailNo);
								}else{
									if (!detailDTO.getSpPurchaseNo().equals(sopPurchaseOrderNo)) {
										continue;
									}
								}
 
                            	if (5 != purchaseOrderDetail.DetailStatus) { //5 为退款  1=待处理，2=待发货，3=待收货，4=待补发，5=已取消，6=已完成
                                    detailDTO.setStatus(OrderStatus.PAYED);
                                } else {
                                    detailDTO.setStatus(OrderStatus.REFUNDED);
                                }
//                                purchaseOrderDetailSpecialList.remove(i);
//                                i--;
                                purchaseOrderMap.remove(sopPurchaseOrderNo);
                                try {
                                    orderDetailService.update(detailDTO);
                                    continue start;
                                } catch (ServiceException e1) {
                                    e1.printStackTrace();
                                }

                            }
                        }
                    }
//                    if (isNew) {//新系统
//                        orderDTO.setSpPurchaseNo(purchaseNobuffer.toString().substring(0, purchaseNobuffer.toString().length() - 1));
//                    }
//
//                    orderDTO.setSpPurchaseDetailNo(purchaseOrderDetailbuffer.toString().substring(0, purchaseOrderDetailbuffer.toString().length() - 1));
//                    productOrderService.update(orderDTO);
                    

                }


            }



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 保留以前的逻辑 转化对象
     * @param detailDTO
     * @return
     */
    private OrderDTO transOrderDatailToOrder(OrderDetailDTO detailDTO){
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setSpOrderId(detailDTO.getOrderNo());//使用自定义的NO
        orderDTO.setDetail(detailDTO.getSupplierSku()+":"+detailDTO.getQuantity());
        orderDTO.setSpMasterOrderNo(detailDTO.getSpMasterOrderNo());
        orderDTO.setUuId(detailDTO.getUuid());
        orderDTO.setExcState(detailDTO.getExcState());
        orderDTO.setPurchasePriceDetail(detailDTO.getPurchasePriceDetail());
        orderDTO.setExcDesc(detailDTO.getExcDesc());
        orderDTO.setMemo(detailDTO.getSpSku()+":"+ detailDTO.getQuantity());
        orderDTO.setSpPurchaseNo(detailDTO.getSpPurchaseNo());
        orderDTO.setSpPurchaseDetailNo(detailDTO.getSpPurchaseDetailNo());


        return orderDTO;

    }

    /*
    处理发生错误的订单
     */
    private void handlePurchaseOrderException(String supplierId){
        //拉取采购单存入本地库
//        List<OrderDTO>  orderDTOList= null;
        List<OrderDetailDTO> detailDTOList =null;
        try {
        	String nowDate = DateTimeUtil.getDateTime(); 
//            orderDTOList  = productOrderService.getOrderBySupplierIdAndOrderStatusAndExceptionStatus(supplierId,OrderStatus.WAITPLACED,"1",nowDate,2);
            detailDTOList = orderDetailService.getOrderBySupplierIdAndOrderStatusAndExceptionStatus(supplierId,OrderStatus.WAITPLACED,"1",nowDate,2);


            if(null!=detailDTOList){

                for(OrderDetailDTO detailDTO:detailDTOList){
                    //兼容以前的程序
                    OrderDTO orderDTO = this.transOrderDatailToOrder(detailDTO);
                    try {
                        //处理供货商订单
                        handleSupplierOrder(orderDTO);
                        //更新海外购订单信息
                        updateOrderMsg(orderDTO);


                    } catch (Exception e) {
                        logger.error("订单处理失败。失败信息 " + orderDTO.toString()+" 原因 ：" + e.getMessage() );

                    }
                }
            }

        } catch (ServiceException e) {
            e.printStackTrace();
        }

    }

    /**
     * 处理尚未有取消锁库存操作记录的异常
     * @param supplierId
     */
    private void handleCancelOrderException(String supplierId){
        //拉取采购单存入本地库
        List<ReturnOrderDTO>  orderDTOList= null;
        try {
//            orderDTOList  =returnOrderService.getReturnOrderBySupplierIdAndOrderStatus(supplierId,OrderStatus.WAITCANCEL);
            orderDTOList  =returnOrderService.getReturnOrderBySupplierIdAndOrderStatusAndExcStatus(supplierId, OrderStatus.WAITCANCEL,"1");
            if(null!=orderDTOList){

                for(ReturnOrderDTO deleteOrder:orderDTOList){
                    //获取子订单
                    try {
                        //处理取消订单
                        handleCancelOrder(deleteOrder);
                        //更改退单状态无论成功或失败 还需要更改订单状态

                        updateCancelOrderMsg(deleteOrder);


                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("取消锁库存处理失败。失败信息 " + deleteOrder.toString()+" 原因 ：" + e.getMessage() );

                    }
                }
            }

        } catch (ServiceException e) {
            e.printStackTrace();
        }

    }
    /**
     * 处理尚未有退单操作记录的异常
     * @param supplierId
     */
    private void handleRefundPurchaseOrderException(String supplierId){
        //拉取采购单存入本地库
        List<ReturnOrderDTO>  orderDTOList= null;
        try {
//            orderDTOList  =returnOrderService.getReturnOrderBySupplierIdAndOrderStatus(supplierId,OrderStatus.WAITCANCEL);
            orderDTOList  =returnOrderService.getReturnOrderBySupplierIdAndOrderStatusAndExcStatus(supplierId, OrderStatus.WAITREFUND,"1");
            if(null!=orderDTOList){

                for(ReturnOrderDTO deleteOrder:orderDTOList){

                    try {
                        //处理退款订单
                        handleRefundlOrder(deleteOrder);
                        //更改退单状态无论成功或失败 还需要更改订单状态

                        updateRefundOrderMsg(deleteOrder);


                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("退款处理失败。失败信息 " + deleteOrder.toString()+" 原因 ：" + e.getMessage() );

                    }
                }
            }

        } catch (ServiceException e) {
            e.printStackTrace();
        }

    }


    private void handleOrderOfSOP(String supplierId, String supplierNo) {
        //获取订单数组
        List<Integer> status = new ArrayList<>();
        status.add(1);

        Map<String,List<PurchaseOrderDetailSpecial>> orderMap = null;
        try {
            orderMap = this.getPurchaseOrderSpecial(supplierId, startDate, endDate, status);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(Iterator<Map.Entry<String,List<PurchaseOrderDetailSpecial>>> itor = orderMap.entrySet().iterator();itor.hasNext();){
            Map.Entry<String, List<PurchaseOrderDetailSpecial>> entry = itor.next();
            Map<String,Integer> stockMap = new HashMap<>();
            StringBuffer purchaseOrderDetailbuffer =new StringBuffer();
            //获取同一产品的数量

            for(PurchaseOrderDetailSpecial purchaseOrderDetail:entry.getValue()){

                if(stockMap.containsKey(purchaseOrderDetail.SupplierSkuNo)){
                    stockMap.put(purchaseOrderDetail.SupplierSkuNo, stockMap.get(purchaseOrderDetail.SupplierSkuNo)+1);
                }else{
                    stockMap.put(purchaseOrderDetail.SupplierSkuNo, 1);

                }

            }
            List<ICEOrderDetailDTO>list=new ArrayList<>();
            StringBuffer buffer = new StringBuffer();
            StringBuffer sopbuffer= new StringBuffer();
            String purchasePrice="";
            for(PurchaseOrderDetailSpecial purchaseOrderDetail:entry.getValue()){
                //记录采购单明细信息 以便发货
                purchaseOrderDetailbuffer.append(purchaseOrderDetail.SopPurchaseOrderDetailNo).append(";");
                //计算同一采购单的相同产品的数量
                if(stockMap.containsKey(purchaseOrderDetail.SupplierSkuNo)){

                	logger.info("SopPurchaseOrderNo:"+purchaseOrderDetail.SopPurchaseOrderNo+"====="+"SopPurchaseOrderDetailNo:"+purchaseOrderDetail.SopPurchaseOrderDetailNo+"==="+"supplierId:"+supplierId);
                    purchasePrice =purchaseOrderDetail.SkuPrice;
                    ICEOrderDetailDTO detailDTO = new ICEOrderDetailDTO();
                    detailDTO.setSku_id(purchaseOrderDetail.SupplierSkuNo);
                    detailDTO.setQuantity(stockMap.get(purchaseOrderDetail.SupplierSkuNo));
                    buffer.append(detailDTO.getSku_id()).append(":").append(detailDTO.getQuantity()).append(",");
                    sopbuffer.append(purchaseOrderDetail.SkuNo).append(":").append(detailDTO.getQuantity()).append(",");
                    list.add(detailDTO);
                    stockMap.remove(purchaseOrderDetail.SupplierSkuNo);
                }

            }
            logger.info("商品采购价："+purchasePrice);

            //存储
            OrderDTO spOrder =new OrderDTO();
            spOrder.setUuId(UUID.randomUUID().toString());
            spOrder.setSupplierId(supplierId);
            spOrder.setSupplierNo(supplierNo);
            spOrder.setStatus(OrderStatus.WAITPLACED);
            spOrder.setSpOrderId(entry.getKey());
            spOrder.setSpPurchaseNo(entry.getKey());
            spOrder.setSpPurchaseDetailNo(purchaseOrderDetailbuffer.toString());
            spOrder.setDetail(buffer.toString().substring(0,buffer.toString().length()-1));
            spOrder.setMemo(sopbuffer.toString().substring(0,sopbuffer.toString().length()-1));
            spOrder.setPurchasePriceDetail(purchasePrice);
            spOrder.setCreateTime(new Date());
            spOrder.setSpMasterOrderNo(entry.getKey());
            try {
                logger.info("采购单信息转化订单后信息："+spOrder.toString());
                System.out.println("采购单信息转化订单后信息："+spOrder.toString());


                List<OrderDTO>  orderDTOs = null;

                try {
                    orderDTOs= productOrderService.saveOrderDetail(spOrder);
                    if(null!=orderDTOs) {
                        for (OrderDTO orderDTO : orderDTOs) {
                            try {
                                //处理供货商订单
                                handleSupplierOrder(orderDTO);
                                //更新海外购订单信息
                                updateOrderMsg(orderDTO);
                            } catch (Exception e) {
                                e.printStackTrace();
                                loggerError.error("供货商：" + spOrder.getSupplierId()+ "订单 ："+ spOrder.getSpOrderId() + "处理失败。失败信息 " + spOrder.toString()+" 原因 ：" + e.getMessage() );
                                Map<String, String> map = new HashMap<>();
                                map.put("excDesc", e.getMessage());
                                setErrorMsg(spOrder.getUuId(), map);
                            }
                        }
                        logger.info("----订单处理完成----");
                    }else{
                        loggerError.error("下单错误 无订单产生");

                    }


                } catch (Exception e) {
                    loggerError.error("下单错误 " + e.getMessage());
                    e.printStackTrace();

                }

//                if(productOrderService.saveOrderWithResult(spOrder)){
//                    try {
//                        //处理供货商订单
//                        handleSupplierOrder(spOrder);
//                        //更新海外购订单信息
//                        updateOrderMsg(spOrder);
//
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        loggerError.error("供货商:"+spOrder.getSupplierId()+" 采购单 ："+ spOrder.getSpPurchaseNo() + "处理失败。失败信息 " + spOrder.toString()+" 原因 ：" + e.getMessage() );
//                        Map<String,String> map = new HashMap<>();
//                        map.put("excDesc",e.getMessage());
//                        setErrorMsg(spOrder.getUuId(),map);
//
//                    }
//                }else{
//                    loggerError.error("供货商:"+spOrder.getSupplierId()+ "订单 ："+ spOrder.getSpOrderId() + "保存订单信息失败");
//                }


           } catch (Exception e){
                logger.error("下单错误 " + e.getMessage());
                e.printStackTrace();
            }


            logger.info("---- SOP订单操作完成----");
        }
    }


    /**
     * 查找漏掉的单子
     * 比如:采购异常的重新采购
     * @param supplierId
     * @param supplierNo
     * @param startDate
     * @param endDate
     */
    private void handleOrderOfSOPForSpecial(String supplierId, String supplierNo,String startDate,String endDate) {
        //获取订单数组
        List<Integer> status = new ArrayList<>();
        status.add(1);

        Map<String,List<PurchaseOrderDetailSpecial>> orderMap = null;
        try {
            orderMap = this.getPurchaseOrderSpecial(supplierId, startDate, endDate, status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        start:
        for(Iterator<Map.Entry<String,List<PurchaseOrderDetailSpecial>>> itor = orderMap.entrySet().iterator();itor.hasNext();){
            Map.Entry<String, List<PurchaseOrderDetailSpecial>> entry = itor.next();
            StringBuffer purchaseOrderDetailbuffer =new StringBuffer();

            StringBuffer buffer = new StringBuffer();
            StringBuffer sopbuffer= new StringBuffer();
            String purchsePrice = "",spMasterOrderNo="",spSku="",supplierSku="",spOrderDetailNo="";
            String purchaseNo,purchaseDetailNo="";
            purchaseNo = entry.getKey();
            for(PurchaseOrderDetailSpecial purchaseOrderDetail:entry.getValue()){  //每个采购单只有一个
                //记录采购单明细信息 以便发货
                purchaseOrderDetailbuffer.append(purchaseOrderDetail.SopPurchaseOrderDetailNo).append(";");
                purchsePrice = purchaseOrderDetail.SkuPrice;
                logger.info(entry.getValue()  + " 采购价 " + purchsePrice);

                spMasterOrderNo= purchaseOrderDetail.OrderNo;
                spSku =   purchaseOrderDetail.SkuNo;
                buffer.append(purchaseOrderDetail.SupplierSkuNo).append(":1");
                sopbuffer.append(purchaseOrderDetail.SkuNo).append(":1");
                spOrderDetailNo = purchaseOrderDetail.OrderDetailNo;
                purchaseDetailNo = purchaseOrderDetail.SopPurchaseOrderDetailNo;
                supplierSku = purchaseOrderDetail.SupplierSkuNo;
            }


            List<OrderDetailDTO> detailDTOList =null;
            try {
                 //根据订单子编号查找EP库中的采购单
            	detailDTOList = orderDetailService.getDetailDTOByEpMasterOrderNo(spMasterOrderNo+"|"+spSku);
//                detailDTOList = orderDetailService.getOrderDetailBySpOrderDetailNo(spOrderDetailNo);
            } catch (ServiceException e) {
                e.printStackTrace();
            }
            
            
            if(null!=detailDTOList&&detailDTOList.size()>0){
            	boolean f = true;
            	//原有数据 如果有采购单号且一致 为原有数据
            	for (OrderDetailDTO orderDetailDTO : detailDTOList) {
            		//是否是新的
            		if (!StringUtils.isEmpty(orderDetailDTO.getSpPurchaseNo())) {
						f = false;
					}
				}
            	if (f) {
            		continue start;
				}else{
					for (OrderDetailDTO orderDetailDTO : detailDTOList) {
						//原有数据不做处理
						 if(orderDetailDTO.getSpPurchaseNo().equals(purchaseNo)){
	                          continue start;
	                      }
					}
				}
            }


            //存储
//            OrderDTO spOrder =new OrderDTO();
//            spOrder.setUuId(UUID.randomUUID().toString());
//            spOrder.setSupplierId(supplierId);
//            spOrder.setSupplierNo(supplierNo);
//            spOrder.setStatus(OrderStatus.WAITPLACED);
//            spOrder.setSpPurchaseNo(entry.getKey());
//            spOrder.setSpOrderId(spOrderNo+"|"+spSku+"-"+String.valueOf((int)(Math.random() * 10))+String.valueOf((int)(Math.random() * 10)));
//            spOrder.setSpMasterOrderNo(spOrderNo);
//            spOrder.setSpPurchaseDetailNo(purchaseOrderDetailbuffer.toString());
//            spOrder.setDetail(buffer.toString().substring(0,buffer.toString().length()-1));
//            spOrder.setMemo(sopbuffer.toString().substring(0,sopbuffer.toString().length()-1));
//            spOrder.setPurchasePriceDetail(purchsePrice);
//            spOrder.setCreateTime(new Date());
            OrderDetailDTO detailDTO = new OrderDetailDTO();
            detailDTO.setUuid(UUID.randomUUID().toString());
            detailDTO.setSupplierId(supplierId);
            detailDTO.setSupplierNo(supplierNo);
            detailDTO.setStatus(OrderStatus.WAITPLACED);
            detailDTO.setOrderNo(spMasterOrderNo+"-"+String.valueOf((int)(Math.random() * 10))+String.valueOf((int)(Math.random() * 10)));
            detailDTO.setSpPurchaseNo(purchaseNo);
            detailDTO.setSpPurchaseDetailNo(purchaseDetailNo);
            detailDTO.setSpMasterOrderNo(spMasterOrderNo);
            detailDTO.setEpMasterOrderNo(spMasterOrderNo+"|"+spSku);
            detailDTO.setSpSku(spSku);
            detailDTO.setSpOrderDetailNo(spOrderDetailNo);
            detailDTO.setSupplierSku(supplierSku);
            detailDTO.setPurchasePriceDetail(purchsePrice);
            detailDTO.setQuantity("1");
            detailDTO.setCreateTime(new Date());
            try {
                logger.info("采购单信息转化订单后信息："+detailDTO.toString());
                System.out.println("采购单信息转化订单后信息："+detailDTO.toString());

                if(orderDetailService.saveOrderDetailWithResult(detailDTO)){
                    try {
                        OrderDTO spOrder = this.transOrderDatailToOrder(detailDTO);
                        //处理供货商订单
                        handleSupplierOrder(spOrder);
                        //更新海外购订单信息
                        updateOrderMsg(spOrder);


                    } catch (Exception e) {
                        e.printStackTrace();
                        loggerError.error("供货商：" + detailDTO.getSupplierId()+" 采购单 ："+ detailDTO.getSpPurchaseNo() + "处理失败。失败信息 " + detailDTO.toString()+" 原因 ：" + e.getMessage() );
                        Map<String,String> map = new HashMap<>();
                        map.put("excDesc",e.getMessage());
                        setErrorMsg(detailDTO.getUuid(),map);

                    }
                }else{
                    loggerError.error("供货商：" + detailDTO.getSupplierId()+ " 订单 ："+ detailDTO.getEpMasterOrderNo() + "保存订单信息失败");
                }


            } catch (ServiceException e) {
                loggerError.error("采购单 ："+ detailDTO.getSpPurchaseNo() + "失败,失败信息 " + detailDTO.toString()+" 原因 ：" + e.getMessage() );
                System.out.println("采购单 ："+ detailDTO.getSpPurchaseNo() + "失败,失败信息 " + detailDTO.toString()+" 原因 ：" + e.getMessage());
                e.printStackTrace();
            } catch (Exception e){
                loggerError.error("下单错误 " + e.getMessage());
                e.printStackTrace();
            }


            logger.info("---- SOP Special 订单操作完成----");
        }
    }


    /**
     * 处理订单
     * @param supplierNo
     * @param supplierId
     * @param skuMap
     * @param orderLit
     */
    private void handleOrderOfWMS(String supplierNo, String supplierId, Map<String, String> skuMap, List<ICEWMSOrderDTO> orderLit) {
        String uuid;
        Map<String,Integer> orderNoQuantityMap = new HashMap<>();

        Map<String,Integer> orderNoQuantityOriginMap = new HashMap<>();

        for(ICEWMSOrderDTO icewmsOrderDTO:orderLit){
            if(orderNoQuantityMap.containsKey(icewmsOrderDTO.getFormNo())){
                orderNoQuantityMap.put(icewmsOrderDTO.getFormNo(),orderNoQuantityMap.get(icewmsOrderDTO.getFormNo())+Math.abs(icewmsOrderDTO.getChangeForOrderQuantity()));
                orderNoQuantityOriginMap.put(icewmsOrderDTO.getFormNo(),orderNoQuantityOriginMap.get(icewmsOrderDTO.getFormNo())+Math.abs(icewmsOrderDTO.getChangeForOrderQuantity()));
            } else{
                orderNoQuantityMap.put(icewmsOrderDTO.getFormNo(),Math.abs(icewmsOrderDTO.getChangeForOrderQuantity()));
                orderNoQuantityOriginMap.put(icewmsOrderDTO.getFormNo(),Math.abs(icewmsOrderDTO.getChangeForOrderQuantity()));
            }
        }

        for(ICEWMSOrderDTO icewmsOrderDTO:orderLit){
            if(!skuMap.containsKey(icewmsOrderDTO.getSkuNo()))
                continue;

            uuid= UUID.randomUUID().toString();

            //存储
            OrderDTO spOrder =new OrderDTO();
            spOrder.setUuId(uuid);
            spOrder.setSupplierId(supplierId);
            spOrder.setSupplierNo(supplierNo);
            spOrder.setStatus(OrderStatus.WAITPLACED);
            spOrder.setSpOrderId(icewmsOrderDTO.getFormNo()+"|"+icewmsOrderDTO.getSkuNo());
            spOrder.setSpMasterOrderNo(icewmsOrderDTO.getFormNo());
            spOrder.setDetail(skuMap.get(icewmsOrderDTO.getSkuNo())+":"+Math.abs(icewmsOrderDTO.getChangeForOrderQuantity()));
            spOrder.setMemo(icewmsOrderDTO.getSkuNo()+":"+icewmsOrderDTO.getChangeForOrderQuantity());
            spOrder.setCreateTime(new Date());
            try {
                logger.info("订单信息："+spOrder.toString());
                System.out.println("订单信息："+spOrder.toString());
                boolean flag = false;
                flag = productOrderService.checkOrderByOrderIdSupplier(spOrder);
                if(!flag){ //查询订单是否存在
                    List<OrderDTO>  orderDTOs = null;

                    try {
                        orderDTOs= productOrderService.saveOrderDetail(spOrder,orderNoQuantityMap,orderNoQuantityOriginMap.get(spOrder.getSpMasterOrderNo()));
                        if(null!=orderDTOs) {
                            for (OrderDTO orderDTO : orderDTOs) {
                                try {
                                    //处理供货商订单
                                    handleSupplierOrder(orderDTO);
                                    //更新海外购订单信息
                                    updateOrderMsg(orderDTO);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    loggerError.error("供货商：" + spOrder.getSupplierId()+ "订单 ："+ spOrder.getSpOrderId() + "处理失败。失败信息 " + spOrder.toString()+" 原因 ：" + e.getMessage() );
                                    Map<String, String> map = new HashMap<>();
                                    map.put("excDesc", e.getMessage());
                                    setErrorMsg(spOrder.getUuId(), map);
                                }
                            }
                            logger.info("----订单处理完成----");
                        }else{
                            loggerError.error("下单错误 无订单产生");

                        }


                    } catch (Exception e) {
                        loggerError.error("下单错误 " + e.getMessage());
                        e.printStackTrace();

                    }

                }
            } catch (Exception e){
                loggerError.error("下单错误 " + e.getMessage());
                e.printStackTrace();
            }

        }
    }

    private void updateOrderMsg(OrderDTO spOrder) {
        //更新订单状态
        Map<String,String> map = new HashMap<>();

        map.put("uuid", spOrder.getUuId());
        map.put("supplierOrderNo",spOrder.getSupplierOrderNo());
        map.put("excState",spOrder.getExcState());
        map.put("excDesc",spOrder.getExcDesc());
        if(null!=spOrder.getExcState()&&"1".equals(spOrder.getExcState())){
            map.put("excTime", DateTimeUtil.convertFormat(new Date(), YYYY_MMDD_HH));
        }else{
            map.put("status",spOrder.getStatus());
            map.put("updateTime",DateTimeUtil.convertFormat(new Date(), YYYY_MMDD_HH));
        }
        try {
            //TODO 替换为明细表
//            productOrderService.updateOrderMsg(map);
            orderDetailService.updateDetailMsg(map);
        } catch (ServiceException e) {
            logger.error("订单："+spOrder.getSpOrderId()+" 下单成功。但更新订单状态失败");
            System.out.println("订单：" + spOrder.getSpOrderId() + " 下单成功。但更新订单状态失败");
        }
    }

    private void handleCancelOfWMS(String supplierNo,String supplierId,Map<String,String> skuMap,List<ICEWMSOrderDTO>  cancelList,boolean handleCancel) {
        String spOrderNo;

        Map<String,Integer> orderNoQuantityMap = new HashMap<>();
        Map<String,Integer> orderNoQuantityOriginMap = new HashMap<>();
        for(ICEWMSOrderDTO icewmsOrderDTO:cancelList){
            if(orderNoQuantityMap.containsKey(icewmsOrderDTO.getFormNo())){
                orderNoQuantityMap.put(icewmsOrderDTO.getFormNo(),orderNoQuantityMap.get(icewmsOrderDTO.getFormNo())+Math.abs(icewmsOrderDTO.getChangeForOrderQuantity()));
                orderNoQuantityOriginMap.put(icewmsOrderDTO.getFormNo(),orderNoQuantityOriginMap.get(icewmsOrderDTO.getFormNo())+Math.abs(icewmsOrderDTO.getChangeForOrderQuantity()));

            } else{
                orderNoQuantityMap.put(icewmsOrderDTO.getFormNo(),Math.abs(icewmsOrderDTO.getChangeForOrderQuantity()));
                orderNoQuantityOriginMap.put(icewmsOrderDTO.getFormNo(),Math.abs(icewmsOrderDTO.getChangeForOrderQuantity()));
            }
        }


        for(ICEWMSOrderDTO refundOrder:cancelList) {

            int quantity = Math.abs(refundOrder.getChangeForOrderQuantity());


            for(int i=0;i<quantity;i++){ //拆分子单到 退单表
//                if(1==quantity){
//                    spOrderNo = refundOrder.getFormNo();
//                }else{
//                    spOrderNo = refundOrder.getFormNo()+i;
//                }
                if(1==orderNoQuantityOriginMap.get(refundOrder.getFormNo())){
                    spOrderNo=refundOrder.getFormNo();
                }else{
                    spOrderNo = refundOrder.getFormNo()+  orderNoQuantityMap.get(refundOrder.getFormNo());
                    orderNoQuantityMap.put(refundOrder.getFormNo(),orderNoQuantityMap.get(refundOrder.getFormNo())-1);
                }

                String uuid = null;
                OrderDetailDTO detailDTO = null;
                try {
                    detailDTO = orderDetailService.getOrderDetailByOrderNo(spOrderNo);
                } catch (ServiceException e) {
                    e.printStackTrace();
                }

                if(null==detailDTO){//采购单已到退款状态  未有已支付状态
                    loggerError.error("退单信息：" + refundOrder.toString() + "  未发现订单，异常不做处理。");
                    continue;
                }
                ReturnOrderDTO deleteOrder =new ReturnOrderDTO();
                deleteOrder.setUuId(detailDTO.getUuid());
                deleteOrder.setSupplierId(supplierId);
                deleteOrder.setSupplierNo(supplierNo);
                deleteOrder.setSupplierOrderNo(detailDTO.getSupplierOrderNo());
                if(handleCancel) {
                    deleteOrder.setStatus(OrderStatus.WAITCANCEL);
                }else{
                    deleteOrder.setStatus(OrderStatus.NOHANDLE);
                }
                deleteOrder.setSpOrderId(detailDTO.getOrderNo());
                deleteOrder.setDetail(skuMap.get(refundOrder.getSkuNo())+":1");
                deleteOrder.setMemo(refundOrder.getSkuNo()+":1");
                deleteOrder.setCreateTime(new Date());
                try{
                    logger.info("退单信息："+deleteOrder.toString());
                    if(returnOrderService.saveOrderWithResult(deleteOrder)){
                        if(!handleCancel){
                            continue;
                        }
                        try {
                            //处理退单
                            handleCancelOrder(deleteOrder);
                            //更改退单状态无论成功或失败
                            updateCancelOrderMsg( deleteOrder);


                        } catch (Exception e) {
                            //下单失败
                            loggerError.error("供货商：" + deleteOrder.getSupplierId()+" 取消订单："+deleteOrder.getSpOrderId()+" 处理失败");
                            e.printStackTrace();
                        }
                    }else{
                        loggerError.error("供货商：" + deleteOrder.getSupplierId() +" 取消订单："+ deleteOrder.getSpOrderId() + "保存失败");
                    }


                }catch (Exception e){
                    logger.error("取消订单错误 " + e.getMessage());
                    e.printStackTrace();
                }



            }

        }
    }

    /**
     * 处理退款
     * @param supplierNo
     * @param supplierId
     */
    private void handleRefundOrderAndEmailOfWMS(String supplierNo,String supplierId){
        //获取订单数组
        List<Integer> status = new ArrayList<>();
        status.add(5);
        try {
            Map<String,List<PurchaseOrderDetail>> orderMap =  this.getPurchaseOrder(supplierId, startDateOfWMS, endDateOfWMS, status);
            if(null!=orderMap) {
                logger.info("获取退款数量： " + orderMap.size());
            }else {
                logger.info("获取退款数量：无 " );
            }
            for(Iterator<Map.Entry<String,List<PurchaseOrderDetail>>> itor = orderMap.entrySet().iterator();itor.hasNext();) {
                Map.Entry<String, List<PurchaseOrderDetail>> entry = itor.next();
                Map<String, Integer> stockMap = new HashMap<>();
                for (PurchaseOrderDetail purchaseOrderDetail : entry.getValue()) {
                    if (stockMap.containsKey(purchaseOrderDetail.SupplierSkuNo)) {
                        stockMap.put(purchaseOrderDetail.SupplierSkuNo, stockMap.get(purchaseOrderDetail.SupplierSkuNo) + 1);
                    } else {
                        stockMap.put(purchaseOrderDetail.SupplierSkuNo, 1);
                    }
                }
                List<ICEOrderDetailDTO> list = new ArrayList<>();
                StringBuffer buffer = new StringBuffer();
                for (PurchaseOrderDetail purchaseOrderDetail : entry.getValue()) {
                    if (stockMap.containsKey(purchaseOrderDetail.SupplierSkuNo)) {
                        buffer.append(purchaseOrderDetail.SupplierSkuNo).append(":").append(stockMap.get(purchaseOrderDetail.SupplierSkuNo)).append(",");
                        stockMap.remove(purchaseOrderDetail.SupplierSkuNo);
                    }
                }
                /**
                 * 根据sp_order_id查询UUID
                 */
                OrderDetailDTO detailDTO = null;
                try {
                    logger.info("退款操作 purchaseno =" + entry.getKey()+"---");
                    detailDTO= orderDetailService.getOrderByPurchaseNo(entry.getKey());
                } catch (ServiceException e) {
                    e.printStackTrace();
                }

                if (null==detailDTO) {//采购单已到退款状态  未有已支付状态 为下单 不做存储
                    logger.info("退款操作 purchaseno  = " + entry.getKey()+"未找到订单信息");
                    loggerError.error("退款操作 purchaseno  = " + entry.getKey()+"未找到订单信息");
                    continue;
                }

                ReturnOrderDTO deleteOrder = new ReturnOrderDTO();
                deleteOrder.setUuId(detailDTO.getUuid());
                deleteOrder.setSupplierId(supplierId);
                deleteOrder.setSupplierNo(supplierNo);
                deleteOrder.setSpPurchaseNo(detailDTO.getSpPurchaseNo());
                deleteOrder.setStatus(OrderStatus.WAITREFUND);
                deleteOrder.setSpOrderId(detailDTO.getOrderNo());
                deleteOrder.setDetail(buffer.toString());
                deleteOrder.setCreateTime(new Date());
                try {
                    logger.info("采购单转化退单后信息：" + deleteOrder.toString());
                    System.out.println("采购单转化退单后信息：" + deleteOrder.toString());
                    if(returnOrderService.saveOrderWithResult(deleteOrder)){
                        //处理退款
                        handleRefundlOrder(deleteOrder);
                        //更改退单状态无论成功或失败 还需要更改订单状态
                        updateRefundOrderMsg(deleteOrder);


                        if(SENDMAIL){
                            OrderDTO orderDTO = this.transOrderDatailToOrder(detailDTO);
                            handleEmail(orderDTO);
                        }
                    }else{
                        loggerError.error("退款："+ deleteOrder.getSpOrderId() + "保存失败");
                    }


                } catch (Exception e) {
                    loggerError.error("订单" + deleteOrder.getSpOrderId()  + "退款错误 " + e.getMessage());
                    e.printStackTrace();
                }

            }

        } catch (Exception e) {
            logger.error( "退款错误 " + e.getMessage());
            e.printStackTrace();
        }


    }


    /**
     * 更新退单信息 同时更新订单信息
     * @param deleteOrder
     */
    private void updateCancelOrderMsg( ReturnOrderDTO deleteOrder) {
        try {
            Map<String,String> map = new HashMap<>();
            map.put("status",deleteOrder.getStatus());
            map.put("uuid",deleteOrder.getUuId());
            map.put("excState",deleteOrder.getExcState());
            map.put("excDesc",deleteOrder.getExcDesc());
            if(null!=deleteOrder.getExcState()&&"1".equals(deleteOrder.getExcState())){
                map.put("excTime", DateTimeUtil.convertFormat(new Date(), YYYY_MMDD_HH));
            }else{
                map.put("status",OrderStatus.CANCELLED);
                map.put("updateTime",DateTimeUtil.convertFormat(new Date(), YYYY_MMDD_HH));
            }
            returnOrderService.updateReturnOrderMsg(map);
        } catch (ServiceException e) {
            loggerError.error("供货商"+ deleteOrder.getSupplierId()+" 取消订单："+deleteOrder.getSpPurchaseNo()+" 操作成功。但更新退单状态失败 原因:" +e.getMessage());
            e.printStackTrace();

        }
        /**
         * 退单成功时修改订单状态
         */
        if(null!=deleteOrder.getExcState()&&"1".equals(deleteOrder.getExcState())){//通知供货商退款异常

        }else{
            this.updateOrderMsgOnCancelOrder(deleteOrder.getUuId());
        }

    }




    /**
     * 更新退款信息 同时更新订单信息
     * @param deleteOrder
     */
    private void updateRefundOrderMsg( ReturnOrderDTO deleteOrder) {
        try {
            Map<String,String> map = new HashMap<>();
            map.put("status",deleteOrder.getStatus());
            map.put("uuid",deleteOrder.getUuId());
            map.put("excState",deleteOrder.getExcState());
            map.put("excDesc",deleteOrder.getExcDesc());
            if(null!=deleteOrder.getExcState()&&"1".equals(deleteOrder.getExcState())){
                map.put("excTime", DateTimeUtil.convertFormat(new Date(), YYYY_MMDD_HH));
            }else{
                map.put("status",OrderStatus.REFUNDED);
                map.put("updateTime",DateTimeUtil.convertFormat(new Date(), YYYY_MMDD_HH));
            }
            returnOrderService.updateReturnOrderMsg(map);
        } catch (ServiceException e) {
            loggerError.error("供货商"+ deleteOrder.getSupplierId()+" 取消订单："+deleteOrder.getSpPurchaseNo()+" 操作成功。但更新订单状态失败 原因:" +e.getMessage());
            e.printStackTrace();

        }
        /**
         * 退单成功时修改订单状态
         */
        if(null!=deleteOrder.getExcState()&&"1".equals(deleteOrder.getExcState())){//通知供货商退款异常

        }else{
            this.updateOrderMsgOnRefundOrder(deleteOrder.getUuId());
        }

    }

    /**
     * 当取消订单时 更改订单状态
     * @param uuId 订单编号
     */
    private void updateOrderMsgOnCancelOrder(String uuId){
//        OrderDTO order = null;

        try {
//            order = productOrderService.getOrderByUuId(uuId);
//            order.setStatus(OrderStatus.CANCELLED);
//
//            order.setUpdateTime(new Date());
//            productOrderService.update(order);
            Map<String,String> map = new HashMap<>();
            map.put("uuid",uuId);
            map.put("status",OrderStatus.CANCELLED);
            map.put("updateTime",DateTimeUtil.convertFormat(new Date(), YYYY_MMDD_HH));
            orderDetailService.updateDetailMsg(map);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


    /**
     * 当退款时 更改订单状态
     * @param uuId 订单编号
     */
    private void updateOrderMsgOnRefundOrder(String uuId){
//        OrderDTO order = null;
        try {
//            order = productOrderService.getOrderByUuId(uuId);
//            order.setStatus(OrderStatus.REFUNDED);
//            productOrderService.update(order);
            Map<String,String> map = new HashMap<>();
            map.put("uuid",uuId);
            map.put("status",OrderStatus.REFUNDED);
            map.put("updateTime",DateTimeUtil.convertFormat(new Date(), YYYY_MMDD_HH));
            orderDetailService.updateDetailMsg(map);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }


    private void refundOrderFromSOP(String supplierNo,String supplierId,boolean handleCancel){

        try {
            //获取订单数组
            List<Integer> status = new ArrayList<>();
            status.add(5);
            Map<String,List<PurchaseOrderDetailSpecial>> orderMap =  this.getPurchaseOrderSpecial(supplierId, startDate, endDate, status);

            for(Iterator<Map.Entry<String,List<PurchaseOrderDetailSpecial>>> itor = orderMap.entrySet().iterator();itor.hasNext();) {
                Map.Entry<String, List<PurchaseOrderDetailSpecial>> entry = itor.next();
                Map<String, Integer> stockMap = new HashMap<>();
                for (PurchaseOrderDetailSpecial purchaseOrderDetail : entry.getValue()) {
                    if (stockMap.containsKey(purchaseOrderDetail.SupplierSkuNo)) {
                        stockMap.put(purchaseOrderDetail.SupplierSkuNo, stockMap.get(purchaseOrderDetail.SupplierSkuNo) + 1);
                    } else {
                        stockMap.put(purchaseOrderDetail.SupplierSkuNo, 1);
                    }
                }
                List<ICEOrderDetailDTO> list = new ArrayList<>();
                StringBuffer buffer = new StringBuffer();
                for (PurchaseOrderDetailSpecial purchaseOrderDetail : entry.getValue()) {
                    if (stockMap.containsKey(purchaseOrderDetail.SupplierSkuNo)) {
                        buffer.append(purchaseOrderDetail.SupplierSkuNo).append(":").append(stockMap.get(purchaseOrderDetail.SupplierSkuNo)).append(",");
                        stockMap.remove(purchaseOrderDetail.SupplierSkuNo);
                    }
                }
                /**
                 * 根据sp_order_id查询UUID
                 */
                String uuid = productOrderService.getUuIdBySpOrderId(entry.getKey());

                if(org.apache.commons.lang.StringUtils.isBlank(uuid)){//采购单已到退款状态  未有已支付状态 为下单 不做存储
                    continue;
                }

                ReturnOrderDTO deleteOrder =new ReturnOrderDTO();
                deleteOrder.setUuId(uuid);
                deleteOrder.setSupplierId(supplierId);
                deleteOrder.setSupplierNo(supplierNo);
                if(handleCancel){
                    deleteOrder.setStatus(OrderStatus.WAITREFUND);
                }else{
                    deleteOrder.setStatus(OrderStatus.NOHANDLE);
                    deleteOrder.setMemo("退单不做处理只做记录");
                }

                deleteOrder.setSpOrderId(entry.getKey());
                deleteOrder.setDetail(buffer.toString());
                deleteOrder.setCreateTime(new Date());
                try{
                    logger.info("采购单信息转化退单后信息："+deleteOrder.toString());
                    System.out.println("采购单信息转化退单后信息："+deleteOrder.toString());
                    if(returnOrderService.saveOrderWithResult(deleteOrder)){
                        if(!handleCancel){
                            //不处理退单
                            continue;
                        }
                        try {
                            //处理取消订单
                            handleRefundlOrder(deleteOrder);
                            //更改退单状态无论成功或失败 还需要更改订单状态

                            updateRefundOrderMsg( deleteOrder);

                        } catch (Exception e) {
                            e.printStackTrace();

                            loggerError.error("供货商:"+deleteOrder.getSupplierId()+"订单 ："+ deleteOrder.getSpOrderId() + "处理退单失败。 原因 ：" + e.getMessage() );

                            Map<String,String> map = new HashMap<>();
                            map.put("excDesc",e.getMessage());

                            setErrorMsg(deleteOrder.getUuId(),map);

                        }
                    }else{
                        loggerError.error("供货商:"+deleteOrder.getSupplierId()+"退单："+ deleteOrder.getSpOrderId() + "保存失败");
                    }



                } catch (ServiceException e) {

                        e.printStackTrace();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 获取采购单
     * 需要注意
     * SOP处理采购单 一条记录代表一个库存  同样的产品 购买两件 生成两条记录
     *
     * @return
     */
    private Map<String,List<PurchaseOrderDetail>> getPurchaseOrder(String supplierId,String startTime ,String endTime,List<Integer> statusList) throws Exception{
        int pageIndex=1,pageSize=20;
        OpenApiServantPrx servant = null;
        try {
            servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
        } catch (Exception e) {
            loggerError.error("供货商:" + supplierId+ " ICE  IcePrxHelper 初始化异常");
            e.printStackTrace();
        }
        boolean hasNext=true;
        logger.warn("获取ice采购单 开始");
        Set<String> skuIds = new HashSet<String>();
        Map<String,List<PurchaseOrderDetail>>  purchaseOrderMap = new HashMap<>();
        String sopPurchaseOrderNo = "";

        while(hasNext){
            List<PurchaseOrderDetail> orderDetails = null;
            try {

                PurchaseOrderQueryDto  orderQueryDto = new PurchaseOrderQueryDto(startTime,endTime,statusList
                        ,pageIndex,pageSize);
                PurchaseOrderDetailPage orderDetailPage=
                servant.FindPurchaseOrderDetailPaged(supplierId, orderQueryDto);


                orderDetails = orderDetailPage.PurchaseOrderDetails;
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (PurchaseOrderDetail orderDetail : orderDetails) {
                sopPurchaseOrderNo  = orderDetail.SopPurchaseOrderNo;
                if(purchaseOrderMap.containsKey(sopPurchaseOrderNo)){
                    //

                    purchaseOrderMap.get(sopPurchaseOrderNo).add(orderDetail);
                }else{
                    List<PurchaseOrderDetail> orderList = new ArrayList<>();
                    orderList.add(orderDetail);
                    purchaseOrderMap.put(sopPurchaseOrderNo,orderList);
                }


            }
            pageIndex++;
            hasNext=(pageSize==orderDetails.size());

        }
//        for(Iterator<Map.Entry<String,List<PurchaseOrderDetail>>> itor = purchaseOrderMap.entrySet().iterator();itor.hasNext();){
//            Map.Entry<String,List<PurchaseOrderDetail>> entry = itor.next();
//            List<PurchaseOrderDetail> orderDetailList = entry.getValue();
//            gson.toJson(orderDetailList);
//
//        }
        logger.warn("获取ice采购单 结束");

        return purchaseOrderMap;

    }


    private Map<String,List<PurchaseOrderDetailSpecial>> getPurchaseOrderSpecial(String supplierId,String startTime ,String endTime,List<Integer> statusList) throws Exception{
        int pageIndex=1,pageSize=20;
        OpenApiServantPrx servant = null;
        try {
            servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
        } catch (Exception e) {
            loggerError.error("供货商:" + supplierId+" ICE  IcePrxHelper 初始化异常");
            e.printStackTrace();
        }
        boolean hasNext=true;
        logger.warn("获取ice采购单 开始");
        Set<String> skuIds = new HashSet<String>();
        Map<String,String>  purchaseTempMap = new HashMap<>();
        Map<String,List<PurchaseOrderDetailSpecial>>  purchaseOrderMap=  new HashMap<>();
        String sopPurchaseOrderNo = "";

        while(hasNext){
            List<PurchaseOrderDetail> orderDetails = null;
            try {

                PurchaseOrderQueryDto  orderQueryDto = new PurchaseOrderQueryDto(startTime,endTime,statusList
                        ,pageIndex,pageSize);
                PurchaseOrderDetailPage orderDetailPage=
                        servant.FindPurchaseOrderDetailPaged(supplierId, orderQueryDto);


                orderDetails = orderDetailPage.PurchaseOrderDetails;


            } catch (Exception e) {
                e.printStackTrace();
            }
            for (PurchaseOrderDetail orderDetail : orderDetails) {
                sopPurchaseOrderNo  = orderDetail.SopPurchaseOrderNo;
                if(purchaseTempMap.containsKey(sopPurchaseOrderNo)){
                   continue;
                }else{
                    purchaseTempMap.put(sopPurchaseOrderNo,"");
                    //转化为带订单号的采购单信息
                    PurchaseOrderDetailSpecialPage  orderDetailSpecialPage = servant.FindPurchaseOrderDetailSpecial(supplierId,sopPurchaseOrderNo,"");

                    if(null!=orderDetailSpecialPage&&null!=orderDetailSpecialPage.PurchaseOrderDetails&&orderDetailSpecialPage.PurchaseOrderDetails.size()>0) {  //存在采购单 就代表已支付

                        for (PurchaseOrderDetailSpecial purchaseOrderDetailSpecial : orderDetailSpecialPage.PurchaseOrderDetails) {
                            sopPurchaseOrderNo = purchaseOrderDetailSpecial.SopPurchaseOrderNo;
                            if (purchaseOrderMap.containsKey(sopPurchaseOrderNo)) {
                                purchaseOrderMap.get(sopPurchaseOrderNo).add(purchaseOrderDetailSpecial);
                            } else {
                                List<PurchaseOrderDetailSpecial> orderList = new ArrayList<>();
                                orderList.add(purchaseOrderDetailSpecial);
                                purchaseOrderMap.put(sopPurchaseOrderNo, orderList);

                            }
                        }
                    }

                }

            }
            pageIndex++;
            hasNext=(pageSize==orderDetails.size());

        }

        logger.warn("获取ice采购单 结束");

        return purchaseOrderMap;

    }


    //发邮件
    class MailThread implements  Runnable{

        String supplier = "";
        String content="";
        String title="";

        public MailThread(String  supplierId,String title,String content){
            this.supplier = supplierId;
            this.title = title;
            this.content = content;
        }

        @Override
        public void run() {
            try {
                SendMail.sendGroupMail("smtp.shangpin.com", fromEmail,
                        emailPass, toEmail, title,
                        content,
                        "text/html;charset=utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     *获取发货单编号
     * 首先推送发货单
     * @return
     * @throws Exception
     */
    public  String getPurchaseDeliveryOrderNo(String supplierId,String logisticsName, String logisticsOrderNo,
                                                    String dateDeliver, int estimateArrivedTime, String deliveryContacts,
                                                    String deliveryContactsPhone, String deliveryAddress,
                                                    String deliveryMemo, String warehouseNo, String warehouseName,
                                                    List<String> sopPurchaseOrderDetailNo, int printStatus) throws Exception{
        OpenApiServantPrx servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
        if(StringUtils.isEmpty(logisticsName)) logisticsName = "顺丰";
        if(StringUtils.isEmpty(logisticsOrderNo)) logisticsOrderNo = getRandomNum();
        if(StringUtils.isEmpty(dateDeliver)) dateDeliver = convertFormat(new Date(),YYYY_MMDD_HH);
        if(StringUtils.isEmpty(deliveryContacts)) deliveryContacts = "尼古拉斯";
        if(StringUtils.isEmpty(deliveryContactsPhone)) deliveryContactsPhone = "18547477474";
        if(StringUtils.isEmpty(deliveryAddress)) deliveryAddress = "北京市通州区马驹桥物流基地兴贸一街 11号华润物流园区5号库";
        if(StringUtils.isEmpty(deliveryMemo)) deliveryMemo = "贵重物品，轻拿轻放";
        if(StringUtils.isEmpty(warehouseNo)) warehouseNo = "B";
        if(StringUtils.isEmpty(warehouseName)) warehouseName = "北京代销实体仓";
        boolean hasNext=true;
        Set<String> skuIds = new HashSet<String>();
        Map<String,List<PurchaseOrderDetail>>  purchaseOrderMap = new HashMap<>();


        DeliveryOrderAdd deliveryOrderAdd= new DeliveryOrderAdd(logisticsName,logisticsOrderNo,dateDeliver,
                estimateArrivedTime,deliveryContacts
        ,deliveryContactsPhone,deliveryAddress,deliveryMemo,warehouseNo
                ,warehouseName, sopPurchaseOrderDetailNo,printStatus);
        String sopLogisticsOrderNo= servant.CreateDeliveryOrder(supplierId, deliveryOrderAdd);
        return sopLogisticsOrderNo;
    }





    private void setErrorMsg(String uuid, Map<String, String> map) {
        map.put("uuid",uuid);
        map.put("excState","1");

        map.put("excTime", DateTimeUtil.convertFormat(new Date(), YYYY_MMDD_HH));
        try {
//            productOrderService.updateExceptionMsg(map);
            orderDetailService.updateDetailMsg(map);
        } catch (ServiceException e) {
            logger.error("保存订单号：" + uuid + "，错误信息时失败");
            e.printStackTrace();
        }
    }


    public static String getRandomNum() {
        Random random = new Random();
        String num="";
        for (int i = 0; i <13; i++) {
            int a = random.nextInt(9);
            num = a+num;
        }
        return num;
    }
    public static void main(String[] args)
    {
        List<String> SopPurchaseOrderDetailNo=new ArrayList<>();
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static String convertFormat(Date date ,String format){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        return (sdf.format(date));

    }


    private  static void initDate(String  fileName) {
        Date tempDate = new Date();

        endDate = com.shangpin.iog.common.utils.DateTimeUtil.convertFormat(tempDate, YYYY_MMDD_HH);

        String lastDate=getLastGrapDate(fileName);
        startDate= org.apache.commons.lang.StringUtils.isNotEmpty(lastDate) ? lastDate: com.shangpin.iog.common.utils.DateTimeUtil.convertFormat(DateUtils.addDays(tempDate, -180), YYYY_MMDD_HH);

        Date tmpDate =  DateTimeUtil.getAppointDayFromSpecifiedDay(DateTimeUtil.convertFormat(startDate,YYYY_MMDD_HH),-10,"S");
        startDate = DateTimeUtil.convertFormat(tmpDate,YYYY_MMDD_HH) ;

        writeGrapDate(endDate,fileName);


    }


    private  static void initWMSDate(String  fileName) {
        Date tempDate = new Date();

        endDateOfWMS = com.shangpin.iog.common.utils.DateTimeUtil.convertFormat(tempDate, YYYY_MMDD_HH_WMS);

        String lastDate=getLastGrapDate(fileName);
        startDateOfWMS= org.apache.commons.lang.StringUtils.isNotEmpty(lastDate) ? lastDate: com.shangpin.iog.common.utils.DateTimeUtil.convertFormat(DateUtils.addDays(tempDate, -180), YYYY_MMDD_HH_WMS);

        Date tmpDate =  DateTimeUtil.getAppointDayFromSpecifiedDay(DateTimeUtil.convertFormat(startDateOfWMS,YYYY_MMDD_HH_WMS),-1,"m");
        startDateOfWMS = DateTimeUtil.convertFormat(tmpDate,YYYY_MMDD_HH_WMS) ;
        
        if(startDateOfTemp!=null&&!"".equals(startDateOfTemp)){
        	startDateOfWMS = startDateOfTemp;
        }
        if(endDateOfTemp!=null&&!"".equals(endDateOfTemp)){
        	endDateOfWMS = endDateOfTemp;
        }else{
        	writeGrapDate(endDateOfWMS, fileName);
        }
        System.out.println("startDateOfWMS："+startDateOfWMS + "，endDateOfWMS："+endDateOfWMS);
        logger.info("startDateOfWMS："+startDateOfWMS + "，endDateOfWMS："+endDateOfWMS);
    }

    private static File getConfFile(String fileName) throws IOException {
        String realPath = AbsOrderService.class.getClassLoader().getResource("").getFile();
        realPath= URLDecoder.decode(realPath, "utf-8");
        File df = new File(realPath+fileName);//"date.ini"
        if(!df.exists()){
            df.createNewFile();
        }
        return df;
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



}
