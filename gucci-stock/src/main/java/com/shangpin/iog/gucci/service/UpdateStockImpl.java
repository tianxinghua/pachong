package com.shangpin.iog.gucci.service;

import com.shangpin.iog.gucci.dto.SpSkuNoDTO;
import com.shangpin.iog.ice.service.StockHandleService;
import com.shangpin.iog.utils.DownloadAndReadCSV;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wanner on 2018/6/27
 */
@Component("updateStockImpl")
public class UpdateStockImpl extends FetchStockImpl {


    /*@Component("stockHandleService")
    @Slf4j
    public class StockHandleService {*/
    @Autowired
    private StockHandleService stockHandleService;

    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");

    private static ResourceBundle bdl = null;
    private static String supplierId = "";

    static String splitSign = ",";
    //库存csv 文件存放目录
    private static String filePath="";

    static {
        if (null == bdl){
            bdl = ResourceBundle.getBundle("conf");
        }
        supplierId = bdl.getString("supplierId");

        filePath = bdl.getString("csvFilePath");
    }

    /**
     * 更新 意大利官网 商品库存数据
     */
    @Override
    public void fetchItlyProductStock(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startDateTime = format.format(new Date());
        System.out.println("============更新GUCCI库存数据库开始 "+startDateTime+"=========================");
        logger.info("==============更新GUCCI库存数据库开始 "+startDateTime+"=========================");

        //读取csv 数据信息
        long dayTime = 1000*3600*24l;
        Date yesterDate = new Date(new Date().getTime() - dayTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String yesterdayDateStr = simpleDateFormat.format(yesterDate);
        String csvFilePath = filePath +"gucci-qty-"+ yesterdayDateStr+".csv";
        try {
            List<SpSkuNoDTO> spSkuNoDTOS = DownloadAndReadCSV.readLocalCSV(csvFilePath, SpSkuNoDTO.class,splitSign);
            if(spSkuNoDTOS!=null&&spSkuNoDTOS.size()>0){
                batchUpdateSpSkuQtyBySpSkuNoDTOList(spSkuNoDTOS);
            }else{
                System.out.println("获取csv spSkuNO size:0 调取csv文件数据失败 ");
                loggerError.error("获取csv spSkuNO size:0 调取csv文件数据失败 ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String endtDateTime = format.format(new Date());
        logger.info("===================更新GUCCI库存数据库结束 "+endtDateTime+"=========================");
        System.out.println("=================更新GUCCI库存数据库结束 "+endtDateTime+"=========================");
    }

    public static void main(String[] args) {
        //读取csv 数据信息
        long dayTime = 1000*3600*24l;
        Date yesterDate = new Date(new Date().getTime() - dayTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String yesterdayDateStr = simpleDateFormat.format(yesterDate);
        String csvFilePath = filePath +"gucci-qty-"+ yesterdayDateStr+".csv";
        try {
            List<SpSkuNoDTO> spSkuNoDTOS = DownloadAndReadCSV.readLocalCSV(csvFilePath, SpSkuNoDTO.class,",");
            if(spSkuNoDTOS!=null&&spSkuNoDTOS.size()>0){
                new UpdateStockImpl().batchUpdateSpSkuQtyBySpSkuNoDTOList(spSkuNoDTOS);
            }else{
                System.out.println("获取csv spSkuNO size:0 调取csv文件数据失败 ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新 spSku 信息
     * @param spSkuNoDTO
     */
    private void updateSpSkuQtyByspSkuNoDTO(SpSkuNoDTO spSkuNoDTO) {
        if(spSkuNoDTO==null) return;
        String spSkuNo = spSkuNoDTO.getSpSkuNo();
        String qty = spSkuNoDTO.getQty();
        if(spSkuNo==null||qty==null||"".equals(spSkuNo)){
            System.out.println(" 更新库存信息 spSkuNo:"+spSkuNo +" qty:"+qty);
            loggerError.error(" 更新库存信息 spSkuNo:"+spSkuNo +" qty:"+qty);
            return;
        }
        Integer qtyNumber =null;
        try {
            qtyNumber = Integer.parseInt(qty);
        } catch (NumberFormatException e) {
            //手动处理为 0
            qtyNumber = 0;
            System.out.println(" string castto Integer failed ======== qty:"+qty);
            logger.info(" string castto Integer failed ======== qty:"+qty);
            e.printStackTrace();
        }

        //updateIceStock(String supplier, Map<String, Integer> iceStock)
        Map<String, Integer> iceStock = new HashMap<>();
        iceStock.put(spSkuNoDTO.getSpSkuNo(),qtyNumber);
        int updateFailedNum = updateIceSpSkuByMap(iceStock);
        System.out.println(" 本次更新尚品库存失败的个数："+updateFailedNum);
        loggerError.error("本次更新尚品库存失败的个数："+updateFailedNum);

    }

    /**
     * 批量更新 spSku 信息
     * @param spSkuNoDTOS
     */
    private  void batchUpdateSpSkuQtyBySpSkuNoDTOList(List<SpSkuNoDTO> spSkuNoDTOS) {
        //定义 需要更新下的spSkuMap key：spSkuNo  value:qty
        Map<String,Integer> spSkuMap = new HashMap<String,Integer>();

        if(spSkuNoDTOS==null){
            return;
        }
        int size = spSkuNoDTOS.size();
        for (int i = 0; i < size; i++) {

            String spSkuNo = spSkuNoDTOS.get(i).getSpSkuNo();
            String qty = spSkuNoDTOS.get(i).getQty();
            if(spSkuNo==null||"".equals(spSkuNo)){
                System.out.println(" -----------更新库存失败------- spSkuNo:"+spSkuNo +" qty:"+qty);
                loggerError.error(" ------------更新库存失败------ spSkuNo:"+spSkuNo +" qty:"+qty);
                return;
            }
            Integer qtyNumber =null;
            try {
                qtyNumber = Integer.parseInt(qty);
            } catch (NumberFormatException e) {
                //手动处理qty为 0
                qtyNumber = 0;
                System.out.println(" String cast to Integer failed ========spSkuNo:"+spSkuNo+" qty:"+qty);
                loggerError.error(" String cast to Integer failed ========spSkuNo:"+spSkuNo+" qty:"+qty);
                e.printStackTrace();
            }
            spSkuMap.put(spSkuNo,qtyNumber);
        }
        Integer updateFailedNum = updateIceSpSkuByMap(spSkuMap);
        System.out.println(" 本次更新尚品库存失败的个数："+updateFailedNum);
        loggerError.error("本次更新尚品库存失败的个数："+updateFailedNum);
    }


    /**
     * 批量更新 spSkuStock 信息
     * @param iceStockMap 尚品skuMap 集合  key:spSkuNO value:qty
     * @return
     */
    public Integer updateIceSpSkuByMap(Map<String, Integer> iceStockMap){
        Integer updateFailedNum = null;
        try {
            updateFailedNum = stockHandleService.updateIceStock(supplierId, iceStockMap);
            System.out.println(" updateFailedNum:"+updateFailedNum);
        } catch (Exception e) {
            System.out.println("----------更新库存 信息失败！！！ ");
            loggerError.error("-----------更新库存 信息失败！！！！ ");
            e.printStackTrace();
        }
        return updateFailedNum;
    }


}
