package com.shangpin.iog.tony.service;

import com.google.gson.Gson;
import com.shangpin.framework.ServiceException;

import com.shangpin.framework.ServiceMessageException;
import com.shangpin.ice.ice.UpdateProductSock;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;
import com.shangpin.iog.dto.SkuRelationDTO;
import com.shangpin.iog.dto.SupplierStockDTO;
import com.shangpin.iog.service.SkuRelationService;
import com.shangpin.iog.service.SupplierStockService;
import com.shangpin.iog.tony.common.Constant;
import com.shangpin.iog.tony.common.MyJsonClient;

import com.shangpin.iog.tony.dto.EventDTO;
import com.shangpin.iog.tony.dto.ReturnDTO;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by wangyuzhi on 2015/9/14.
 */
@Service("tonyfetch")
public class TonyStockImp {
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");


    OutTimeConfig outTimeConfig =     new OutTimeConfig(1000 * 60 , 1000 * 60 , 1000 * 60);

    public static ResourceBundle bundle = ResourceBundle.getBundle("conf");
    public static String SUPPLIER_ID = bundle.getString("supplierId");
    @Autowired
    SupplierStockService supplierStockService;
    @Autowired
    SkuRelationService skuRelationService;
    public void  fetchStock() {

        Gson gson = new Gson();
        Map<String,Integer> stockMap = new HashMap<>();
        String json = Constant.EVENTS_INPUT;
        String rtnData ="";
        try {
            rtnData = HttpUtil45.operateData("post", "json", Constant.EVENTS_URL,outTimeConfig, null, json, "", "");
            logger.info("rtnData = " + rtnData);
            System.out.println("rtnData=="+rtnData);
        } catch (ServiceException e) {
            e.printStackTrace();
            loggerError.error(" reason : " + e.getMessage());
        }



        ReturnDTO returnDTO=gson.fromJson(rtnData, ReturnDTO.class );//new TypeToken<List<SaleDTO>>(){}.getType()

        if("ok".equals(returnDTO.getStatus())){
            List<EventDTO>  eventDTOs  = returnDTO.getData().getEvents();
            if(null!=eventDTOs){
                for(EventDTO eventDTO:eventDTOs){
                    if(0==eventDTO.getType()){//购买减少库存
                        //0: the quantity of the involved product is changed (you can find a sku attribute to identify the product and a qty attribute that contains the new quantity you have to display on your selling channel);
                        stockMap.put(eventDTO.getAdditional_info().getSku(),eventDTO.getAdditional_info().getQty());
                    }else if(1==eventDTO.getType()){
                        //1: the involved item was removed from the stock (you can find the sku attribute to identify the product and you have to put the quantity to 0)
                        stockMap.put(eventDTO.getAdditional_info().getSku(),0);
                    }else if(2==eventDTO.getType()){
                        //  2: the inventory was updated and you have to reload all the inventory using the proper web service (the getItemsList web service)
                    }else if(3==eventDTO.getType()){
                         //3: the item was upserted. It means that the involved item was added to the inventory or updated. You can find the item directly in the event and you have to refresh the info on your selling channel
                    }else if(4==eventDTO.getType()){
                        // 4 the item changed quantity or the stock price. You can find in the event the sku attribute of the involved product, the qty attribute (that you have to display on your selling channel), the qty_diff attribute (not important in this case) and the stock price
//                        if(0==eventDTO.getAdditional_info().getStock_price()){//停止销售
//                            stockMap.put(eventDTO.getAdditional_info().getSku(),0);
//                        }else{
                            stockMap.put(eventDTO.getAdditional_info().getSku(),eventDTO.getAdditional_info().getQty());
//                        }
                    }
                }
                for(Iterator<Map.Entry<String,Integer>> itor= stockMap.entrySet().iterator();itor.hasNext();){
                    Map.Entry<String,Integer> entry  = itor.next();
                    SupplierStockDTO supplierStockDTO = new SupplierStockDTO();
                    supplierStockDTO.setSupplierId(Constant.SUPPLIER_ID);
                    supplierStockDTO.setId(UUIDGenerator.getUUID());
                    supplierStockDTO.setSupplierSkuId(entry.getKey());
                    supplierStockDTO.setQuantity(entry.getValue());
                    supplierStockDTO.setOptTime(new Date());

                    try {
                        supplierStockService.saveStock(supplierStockDTO);

                    } catch (ServiceMessageException e) {
                        try {
                            if(e.getMessage().equals("数据插入失败键重复")){
                                //update
                                supplierStockService.updateStock(supplierStockDTO);
                                logger.info(supplierStockDTO.getSupplierSkuId() + " : 更新库存" + supplierStockDTO.getQuantity());
                                //

                            } else{
                                e.printStackTrace();
                            }

                        } catch (ServiceException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
                //实时更新库存
                if(stockMap.size()>0){
                    this.updateSOPInventory(stockMap);
                }

            }

        }
    }

    /**
     * 更新库存数量
     */
    private void updateSOPInventory(Map<String,Integer> stockMap){

        try {
            UpdateProductSock updateProductSock = new UpdateProductSock();
            Map<String,String> skuRelationMap = new HashMap<>();
            for(Iterator<String> itor = stockMap.keySet().iterator();itor.hasNext();){
                try {
                    SkuRelationDTO skuRelationDTO =  skuRelationService.getSkuRelationBySupplierIdAndSupplierSkuNo(SUPPLIER_ID,itor.next());
                    skuRelationMap.put(skuRelationDTO.getSupplierSkuId(),skuRelationDTO.getSopSkuId());
                } catch (ServiceException e) {
                    e.printStackTrace();
                }
            }

            updateProductSock.updateStock(stockMap,skuRelationMap,SUPPLIER_ID);
        } catch (Exception e) {
            loggerError.error("时时同步库存失败");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {


         long start = 0;//计时开始时间
         long end = 0;//计时结束时间
        start = System.currentTimeMillis();
//        String json = new MyJsonClient().getEvents();
        String json = "rtnData=={\"status\":\"ok\",\"data\":{\"events\":[{\"_id\":{\"$id\":\"561e6e2be4b0b71d53085f0f\"},\"shop_id\":{\"$id\":\"55f71cecb49dbbdf4ec6354d\"},\"type\":1,\"date\":{\"sec\":1444834859,\"usec\":487000},\"additional_info\":{\"qty_diff\":1,\"shop_from\":{\"$id\":\"561121e328499f880b0041a7\"},\"from\":\"shop\",\"sku\":\"356530S1533_1000-40\",\"order_id\":{\"$id\":\"561e6e21e4b0b71d53085f07\"}}},{\"_id\":{\"$id\":\"561e745fe4b0269c2a643f68\"},\"shop_id\":{\"$id\":\"55f71cecb49dbbdf4ec6354d\"},\"type\":0,\"date\":{\"sec\":1444836447,\"usec\":622000},\"additional_info\":{\"qty_diff\":1,\"shop_from\":{\"$id\":\"561121e328499f880b0041a7\"},\"qty\":2,\"from\":\"shop\",\"sku\":\"356530S1533_1500-40\",\"order_id\":{\"$id\":\"561e7455e4b0269c2a643f60\"}}},{\"_id\":{\"$id\":\"561e775ce4b0b5f049525a41\"},\"shop_id\":{\"$id\":\"55f71cecb49dbbdf4ec6354d\"},\"type\":0,\"date\":{\"sec\":1444837212,\"usec\":802000},\"additional_info\":{\"qty_diff\":1,\"shop_from\":{\"$id\":\"561121e328499f880b0041a7\"},\"qty\":1,\"from\":\"shop\",\"sku\":\"356530S1533_1500-40\",\"order_id\":{\"$id\":\"561e7752e4b0b5f049525a38\"}}},{\"_id\":{\"$id\":\"561e77a2e4b0b5f049525a4b\"},\"shop_id\":{\"$id\":\"55f71cecb49dbbdf4ec6354d\"},\"type\":0,\"date\":{\"sec\":1444837282,\"usec\":801000},\"additional_info\":{\"qty_diff\":-3,\"shop_from\":{\"$id\":\"561121e328499f880b0041a7\"},\"qty\":4,\"from\":\"shop\",\"sku\":\"356530S1533_1500-40\",\"order_id\":{\"$id\":\"561e7798e4b0b5f049525a42\"}}},{\"_id\":{\"$id\":\"561e7d36e4b0c01df26d77e9\"},\"shop_id\":{\"$id\":\"55f71cecb49dbbdf4ec6354d\"},\"type\":0,\"date\":{\"sec\":1444838710,\"usec\":853000},\"additional_info\":{\"qty_diff\":1,\"shop_from\":{\"$id\":\"55f71cecb49dbbdf4ec6354d\"},\"qty\":4,\"from\":\"shop\",\"sku\":\"356530S1533_1000-40\",\"order_id\":{\"$id\":\"561e7d2ce4b0c01df26d77e0\"}}}]}}";
        end = System.currentTimeMillis();


//        Items[] array = new Gson().fromJson(new StringUtil().getItemsArray(itemsJson),
//                new TypeToken<Items[]>() {}.getType());
        Gson gson = new Gson();
        ReturnDTO returnDTO=gson.fromJson(json, ReturnDTO.class );//new TypeToken<List<SaleDTO>>(){}.getType()
        Map<String,Integer> stockMap = new HashMap<>();
        if("ok".equals(returnDTO.getStatus())){
           List<EventDTO>  eventDTOs  = returnDTO.getData().getEvents();
            for(EventDTO eventDTO:eventDTOs){
                if(0==eventDTO.getType()){//购买减少库存
                   stockMap.put(eventDTO.getAdditional_info().getSku(),eventDTO.getAdditional_info().getQty());
                }else if(1==eventDTO.getType()){
                    stockMap.put(eventDTO.getAdditional_info().getSku(),0);
                }else if(2==eventDTO.getType()){
                    stockMap.put(eventDTO.getAdditional_info().getSku(),eventDTO.getAdditional_info().getQty());
                }else if(3==eventDTO.getType()){
                    stockMap.put(eventDTO.getAdditional_info().getSku(),eventDTO.getAdditional_info().getQty());
                }else if(4==eventDTO.getType()){
                    if(0==eventDTO.getAdditional_info().getStock_price()){//停止销售
                        stockMap.put(eventDTO.getAdditional_info().getSku(),0);
                    }else{
                        stockMap.put(eventDTO.getAdditional_info().getSku(),eventDTO.getAdditional_info().getQty());
                    }

                }

            }


        }


    }
}
