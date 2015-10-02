package com.shangpin.ice.ice;

import ShangPin.SOP.Entity.Api.Purchase.DeliveryOrderAdd;
import ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderDetail;
import ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderDetailPage;
import ShangPin.SOP.Entity.Where.OpenApi.Purchase.PurchaseOrderQueryDto;
import ShangPin.SOP.Servant.OpenApiServantPrx;
import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;
import com.shangpin.iog.common.utils.*;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.OrderDTO;
import com.shangpin.iog.dto.ReturnOrderDTO;
import com.shangpin.iog.ice.dto.ICEOrderDTO;
import com.shangpin.iog.ice.dto.ICEOrderDetailDTO;
import com.shangpin.iog.service.ReturnOrderService;
import com.shangpin.iog.service.SkuPriceService;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by loyalty on 15/9/9.
 */
public abstract class AbsOrderService {

    private static String  startDate=null,endDate=null;
    private static final String YYYY_MMDD_HH = "yyyy-MM-dd HH:mm:ss";

    static Logger log = LoggerFactory.getLogger(AbsOrderService.class);

    private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("info");
    private static org.apache.log4j.Logger loggerError = org.apache.log4j.Logger.getLogger("error");

    static String url="/purchase/createdeliveryorder";




    @Autowired
	public SkuPriceService skuPriceService;

    @Autowired
    com.shangpin.iog.service.OrderService productOrderService;

    @Autowired
    ReturnOrderService returnOrderService;


    /**
     * 处理供货商的信息
     * @param orderDTO
     * @throws ServiceException
     */
    abstract  public void handleSupplierOrder(OrderDTO orderDTO) throws ServiceException;

    /**
     * 取消订单
     * @throws ServiceException
     */
    abstract  public void handleCancelOrder(ReturnOrderDTO deleteOrder) throws ServiceException;

    /**
     * 用户下单
     * @param supplierId
     */
    public  void  checkoutOrderFromSOP(String supplierId){

        //初始化时间
        initDate("date.ini");
        //获取订单数组
        List<Integer> status = new ArrayList<>();
        status.add(1);

        Map<String,List<PurchaseOrderDetail>> orderMap = null;
        try {
            orderMap = this.getPurchaseOrder(supplierId, startDate, endDate, status);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String uuid="";


        for(Iterator<Map.Entry<String,List<PurchaseOrderDetail>>> itor = orderMap.entrySet().iterator();itor.hasNext();){



            Map.Entry<String, List<PurchaseOrderDetail>> entry = itor.next();
            ICEOrderDTO orderDTO = new ICEOrderDTO();
            Map<String,Integer> stockMap = new HashMap<>();
            StringBuffer purchaseOrderDetailbuffer =new StringBuffer();
            //获取同一产品的数量

            for(PurchaseOrderDetail purchaseOrderDetail:entry.getValue()){

                if(stockMap.containsKey(purchaseOrderDetail.SupplierSkuNo)){
                    stockMap.put(purchaseOrderDetail.SupplierSkuNo, stockMap.get(purchaseOrderDetail.SupplierSkuNo)+1);
                }else{
                    stockMap.put(purchaseOrderDetail.SupplierSkuNo, 1);

                }

            }
            List<ICEOrderDetailDTO>list=new ArrayList<>();
            StringBuffer buffer = new StringBuffer();
            for(PurchaseOrderDetail purchaseOrderDetail:entry.getValue()){
                //记录采购单明细信息 以便发货
                purchaseOrderDetailbuffer.append(purchaseOrderDetail.SopPurchaseOrderDetailNo).append(";");
                //计算同一采购单的相同产品的数量
                if(stockMap.containsKey(purchaseOrderDetail.SupplierSkuNo)){
                    ICEOrderDetailDTO detailDTO = new ICEOrderDetailDTO();
                    detailDTO.setSku_id(Integer.valueOf(purchaseOrderDetail.SupplierSkuNo));
                    detailDTO.setQuantity(stockMap.get(purchaseOrderDetail.SupplierSkuNo));
                    buffer.append(detailDTO.getSku_id()).append(":").append(detailDTO.getQuantity()).append(",");
                    list.add(detailDTO);
                    stockMap.remove(purchaseOrderDetail.SupplierSkuNo);
                }

            }

            orderDTO.setOrder_items(list);
            uuid=UUID.randomUUID().toString();

            //存储
            OrderDTO spOrder =new OrderDTO();
            spOrder.setUuId(uuid);
            spOrder.setSupplierId(supplierId);
            spOrder.setStatus("WAITING");
            spOrder.setSpOrderId(entry.getKey());
            spOrder.setSpPurchaseDetailNo(purchaseOrderDetailbuffer.toString());
            spOrder.setDetail(buffer.toString());
            spOrder.setCreateTime(new Date());
            try {
                logger.info("采购单信息转化订单后信息："+spOrder.toString());
                System.out.println("采购单信息转化订单后信息："+spOrder.toString());
                productOrderService.saveOrder(spOrder);

                try {
                   //处理供货商订单
                    handleSupplierOrder(spOrder);
                } catch (Exception e) {
                    e.printStackTrace();

                    loggerError.error("采购单 ："+ spOrder.getSpOrderId() + "处理失败。失败信息 " + spOrder.toString()+" 原因 ：" + e.getMessage() );

                    Map<String,String> map = new HashMap<>();
                    map.put("excDesc",e.getMessage());

                    setErrorMsg(spOrder.getUuId(),map);



                }

            } catch (ServiceException e) {
                loggerError.error("采购单 ："+ spOrder.getSpOrderId() + "失败,失败信息 " + spOrder.toString()+" 原因 ：" + e.getMessage() );
                System.out.println("采购单 ："+ spOrder.getSpOrderId() + "失败,失败信息 " + spOrder.toString()+" 原因 ：" + e.getMessage());
                e.printStackTrace();
            } catch (Exception e){
                loggerError.error("下单错误 " + e.getMessage());
                e.printStackTrace();
            }


            logger.info("----gilt 订单存储完成----");
        }
    }



    public void cancelOrderFromSOP(String supplierId){

        try {
            //获取订单数组
            List<Integer> status = new ArrayList<>();
            status.add(5);
            Map<String,List<PurchaseOrderDetail>> orderMap =  this.getPurchaseOrder(supplierId, startDate, endDate, status);
            Gson gson = new Gson();
            OutTimeConfig timeConfig = new OutTimeConfig(1000*5,1000*5,1000*5);
            String orderDetail = "",operateTime="";
            for(Iterator<Map.Entry<String,List<PurchaseOrderDetail>>> itor = orderMap.entrySet().iterator();itor.hasNext();) {
                Map.Entry<String, List<PurchaseOrderDetail>> entry = itor.next();
                OrderDTO orderDTO = new OrderDTO();
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
                        buffer.append("'").append(Integer.valueOf(purchaseOrderDetail.SupplierSkuNo)).append("'").append(":").append(stockMap.get(purchaseOrderDetail.SupplierSkuNo)).append(",");
                        stockMap.remove(purchaseOrderDetail.SupplierSkuNo);
                    }
                }
                /**
                 * 根据sp_order_id查询UUID
                 */
                String uuid = productOrderService.getUuIdByspOrderId(entry.getKey());
                //存储
                if(org.apache.commons.lang.StringUtils.isBlank(uuid)){//采购单已到退款状态  未有已支付状态
                    continue;
                }

                ReturnOrderDTO deleteOrder =new ReturnOrderDTO();
                deleteOrder.setUuId(uuid);
                deleteOrder.setSupplierId(supplierId);
                deleteOrder.setStatus("WAITCANCEL");
                deleteOrder.setSpOrderId(entry.getKey());
                deleteOrder.setDetail(buffer.toString());
                deleteOrder.setCreateTime(new Date());
                try{
                    logger.info("采购单信息转化退单后信息："+deleteOrder.toString());
                    System.out.println("采购单信息转化退单后信息："+deleteOrder.toString());
                    returnOrderService.saveOrder(deleteOrder);

                    try {
                        //处理取消订单
                        handleCancelOrder(deleteOrder);
                    } catch (Exception e) {
                        e.printStackTrace();

                        loggerError.error("订单 ："+ deleteOrder.getUuId() + "处理退单失败。 原因 ：" + e.getMessage() );

                        Map<String,String> map = new HashMap<>();
                        map.put("excDesc",e.getMessage());

                        setErrorMsg(deleteOrder.getUuId(),map);



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
        OpenApiServantPrx servant = IcePrxHelper.getPrx(OpenApiServantPrx.class);
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
            productOrderService.updateExceptionMsg(map);
        } catch (ServiceException e) {
            loggerError.error("保存订单号：" + uuid + "，错误信息时失败");
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



        writeGrapDate(endDate,fileName);


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
