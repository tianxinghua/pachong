package com.shangpin.iog.gilt.order;

import ShangPin.SOP.Entity.Api.Purchase.PurchaseOrderDetail;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.OrderService;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.gilt.dto.OrderDTO;
import com.shangpin.iog.gilt.dto.OrderDetailDTO;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URLDecoder;
import java.util.*;

/**
 * Created by loyalty on 15/9/9.
 */
@Component("giltOrder")
public class OrderServiceImpl  {

    private static String  startDate=null,endDate=null;
    private static final String YYYY_MMDD_HH = "yyyy-MM-dd HH:mm:ss";

    private static ResourceBundle bdl=null;
    private static String supplierId;

    static {
        if(null==bdl)
            bdl=ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");
    }

    @Autowired
    com.shangpin.iog.service.OrderService productOrderService;
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");
    private static Logger logMongo = Logger.getLogger("mongodb");
    private String key = "fb8ea6839b486dba8c5cabb374c03d9d";


    public void purchaseOrder(){

        //初始化时间
        initDate();
        OrderService iceOrderService = new OrderService();
        try {
            //获取订单数组
            List<Integer> status = new ArrayList<>();
            status.add(1);
            Map<String,List<PurchaseOrderDetail>> orderMap =  iceOrderService.geturchaseOrder(supplierId, startDate, endDate, status);
           //  下单
            String url = "https://api-sandbox.gilt.com/global/orders/";
            transData( url, supplierId,orderMap);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 传输库存
     * @param url
     * @param orderMap
     * @throws ServiceException
     */
    public void transData(String url,String supplierId, Map<String, List<PurchaseOrderDetail>> orderMap) throws ServiceException {
        Gson gson = new Gson();
        OutTimeConfig timeConfig = new OutTimeConfig(1000*5,1000*5,1000*5);
        for(Iterator<Map.Entry<String,List<PurchaseOrderDetail>>> itor = orderMap.entrySet().iterator();itor.hasNext();){
            Map.Entry<String,List<PurchaseOrderDetail>> entry = itor.next();
            OrderDTO orderDTO = new OrderDTO();
            Map<String,Integer> stockMap = new HashMap<>();
            List<OrderDetailDTO> detailDTOs = new ArrayList<>();
            //获取同一产品的数量

            for(PurchaseOrderDetail purchaseOrderDetail:entry.getValue()){

                if(stockMap.containsKey(purchaseOrderDetail.SupplierSkuNo)){
                    stockMap.put(purchaseOrderDetail.SupplierSkuNo,stockMap.get(purchaseOrderDetail.SupplierSkuNo)+1);
                }else{
                    stockMap.put(purchaseOrderDetail.SupplierSkuNo,1);
                }

            }
            List<OrderDetailDTO>list=new ArrayList<>();
            for(PurchaseOrderDetail purchaseOrderDetail:entry.getValue()){

               if(stockMap.containsKey(purchaseOrderDetail.SupplierSkuNo)){
                   OrderDetailDTO detailDTO = new OrderDetailDTO();
                   detailDTO.setSku_id(purchaseOrderDetail.SupplierSkuNo);
                   detailDTO.setQuantity(String.valueOf(stockMap.get(purchaseOrderDetail.SupplierSkuNo)));
                   list.add(detailDTO);
                   stockMap.remove(purchaseOrderDetail.SupplierSkuNo);
               }

            }
            orderDTO.setOrder_items(list);
            orderDTO.setId(UUIDGenerator.getUUID());
            orderDTO.setStatus("confirmed");
            String param = gson.toJson(orderDTO,new TypeToken<OrderDTO>(){}.getType());

            String result =  HttpUtil45.operateData("put", "json", url + UUIDGenerator.getUUID(), timeConfig, null, param, key, "");
            //TODO  存储
            /**
             * 日志存储，数据库存储
             */
            OrderDTO dto= getObjectByJsonString(result);
            com.shangpin.iog.dto.OrderDTO order=new com.shangpin.iog.dto.OrderDTO();
            /*order.setId();*/
            for(int i=0;i<dto.getOrder_items().size();i++){
                order.setUuId(dto.getId());
                order.setSupplierId(supplierId);
                order.setStatus(dto.getStatus());
                order.setSpOrderId(entry.getKey());
                order.setDetail(dto.getOrder_items().get(i).getSku_id()+"-"+dto.getOrder_items().get(i).getQuantity());
                order.setCreateTime(new Date());
                productOrderService.saveOrder(order);
            }
            logger.info("----gilt 订单存储完成----");
        }
    }
    private static List<OrderDTO> getObjectsByJsonString(String jsonStr){
        Gson gson = new Gson();
        List<OrderDTO> objs = new ArrayList<OrderDTO>();
        try {
            objs = gson.fromJson(jsonStr, new TypeToken<List<OrderDTO>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
            //logger.info("get List<ApennineProductDTO> fail :"+e);
        }
        return objs;
    }
    private static OrderDTO getObjectByJsonString(String jsonStr){
        OrderDTO obj=null;
        Gson gson = new Gson();
        try {
            obj=gson.fromJson(jsonStr, OrderDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    private  static void initDate() {
        Date tempDate = new Date();

        endDate = DateTimeUtil.convertFormat(tempDate, YYYY_MMDD_HH);

        String lastDate=getLastGrapDate();
        startDate= StringUtils.isNotEmpty(lastDate) ? lastDate: DateTimeUtil.convertFormat(DateUtils.addDays(tempDate, -180), YYYY_MMDD_HH);



        writeGrapDate(endDate);


    }

    private static File getConfFile() throws IOException {
        String realPath = OrderServiceImpl.class.getClassLoader().getResource("").getFile();
        realPath= URLDecoder.decode(realPath, "utf-8");
        File df = new File(realPath+"date.ini");
        if(!df.exists()){
            df.createNewFile();
        }
        return df;
    }
    private static String getLastGrapDate(){
        File df;
        String dstr=null;
        try {
            df = getConfFile();
            try(BufferedReader br = new BufferedReader(new FileReader(df))){
                dstr=br.readLine();
            }
        } catch (IOException e) {
            logger.error("读取日期配置文件错误");
        }
        return dstr;
    }

    private static void writeGrapDate(String date){
        File df;
        try {
            df = getConfFile();

            try(BufferedWriter bw = new BufferedWriter(new FileWriter(df))){
                bw.write(date);
            }
        } catch (IOException e) {
            logger.error("写入日期配置文件错误");
        }
    }


    public static void main(String[] args) throws  Exception{
        OrderServiceImpl  orderService = new OrderServiceImpl();

        Map<String,List<PurchaseOrderDetail>> orderMap =  new HashMap<>();
        List<PurchaseOrderDetail> purchaseOrderDetails = new ArrayList<>();
        PurchaseOrderDetail  purchaseOrderDetail = new PurchaseOrderDetail();
        purchaseOrderDetails.add(purchaseOrderDetail);
        orderMap.put("",purchaseOrderDetails);

        orderService.transData("https://api-sandbox.gilt.com/global/orders/","",orderMap);


    }
}
