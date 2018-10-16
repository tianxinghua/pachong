package com.shangpin.iog.hermes.service;

import com.shangpin.iog.hermes.dto.SpSkuNoDTO;
import com.shangpin.iog.hermes.dto.ApiResponseBody;
import com.shangpin.iog.hermes.dto.ZhiCaiSkuHttpDTO;
import com.shangpin.iog.hermes.dto.ZhiCaiSkuStock;
import com.shangpin.iog.utils.DownloadAndReadCSV;
import com.shangpin.iog.utils.HttpUtil45;
import com.shangpin.openapi.api.sdk.client.OutTimeConfig;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wanner on 2018/6/27
 */
@Component("updateStockImpl")
public class UpdateStockImpl extends FetchStockImpl {

    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");

    private static ResourceBundle bdl = null;
    private static String supplierId = "";
    //更新批量spSkuQty 信息 接口地址url
    private static String updateSpSkuUrl = "";

    static String splitSign = ",";
    //库存csv 文件存放目录
    private static String filePath="";

    private static Integer batchSize = 200;

    private static OutTimeConfig timeConfig = new OutTimeConfig(1000*60*30,1000*60*30,1000*60*30);

    static {
        if (null == bdl){
            bdl = ResourceBundle.getBundle("conf");
        }
        supplierId = bdl.getString("supplierId");
        updateSpSkuUrl = bdl.getString("updateSpSkuUrl");
        filePath = bdl.getString("csvFilePath");

        try{
            batchSize = Integer.parseInt(bdl.getString("batchSize"));
        }catch(Exception e) {
            batchSize = 200;
        }
    }

    /**
     * 更新 意大利官网 商品库存数据
     */
    @Override
    public void fetchItlyProductStock(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startDateTime = format.format(new Date());
        System.out.println("============更新LV库存数据库开始 "+startDateTime+"=========================");
        logger.info("==============更新LV库存数据库开始 "+startDateTime+"=========================");

        //读取csv 数据信息
        long dayTime = 1000*3600*24l;
        Date todayDate = new Date();
        Date yesterDate = new Date(new Date().getTime() - dayTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String todayDateStr = simpleDateFormat.format(todayDate);
        String yesterdayDateStr = simpleDateFormat.format(yesterDate);
        String csvFilePath = filePath +"lv-qty-"+ todayDateStr+"-2.csv";
        if(!new File(csvFilePath).exists()){
            csvFilePath = filePath +"lv-qty-"+ todayDateStr+"-1.csv";
            if(!new File(csvFilePath).exists()){
                csvFilePath = filePath +"lv-qty-"+ yesterdayDateStr+"-2.csv";
                if(!new File(csvFilePath).exists()){
                    csvFilePath = filePath +"lv-qty-"+ yesterdayDateStr+"-1.csv";
                }
            }
        }

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
        logger.info("===================更新LV库存数据库结束 "+endtDateTime+"=========================");
        System.out.println("=================更新LV库存数据库结束 "+endtDateTime+"=========================");
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
     * 批量更新 spSku 信息
     * @param spSkuNoDTOS
     */
    private  void batchUpdateSpSkuQtyBySpSkuNoDTOList(List<SpSkuNoDTO> spSkuNoDTOS) {

        if(spSkuNoDTOS==null){
            System.out.println("批量更新 spSku 信息 spSkuNoDTOS 为空");
            return;
        }
        //定义传送 直采数据httpDTO
        ZhiCaiSkuHttpDTO zhiCaiSkuHttpDTO = new ZhiCaiSkuHttpDTO();
        zhiCaiSkuHttpDTO.setSupplierId(supplierId);
        int size = spSkuNoDTOS.size();
        List<ZhiCaiSkuStock> skuStockList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if(skuStockList.size()==batchSize){
                //调用 spSku 更新接口
                zhiCaiSkuHttpDTO.setZhiCaiSkuStockList(skuStockList);
                updateZhiCaiSkuList(zhiCaiSkuHttpDTO);
                //清空需要更新spSkuList 信息
                skuStockList = new ArrayList<>();
            }

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
            //spSkuMap.put(spSkuNo,qtyNumber);
            skuStockList.add(new ZhiCaiSkuStock(spSkuNo,qtyNumber));

            //最后遍历到最后的时候将最后的spSku 更新 防止 当skuStockList 的size 大小小于batchSize
            if(i == size-1){
                zhiCaiSkuHttpDTO.setZhiCaiSkuStockList(skuStockList);
                updateZhiCaiSkuList(zhiCaiSkuHttpDTO);
            }
        }

        Integer updateFailedNum = spSkuNoDTOS.size();
        System.out.println(" 本次更新尚品库存个数："+updateFailedNum);
        logger.info("本次更新尚品库存失败的个数："+updateFailedNum);
    }

    /**
     * 调用接口更新 spSku qty 信息
     * @param zhiCaiSkuHttpDTO
     */
    private void updateZhiCaiSkuList(ZhiCaiSkuHttpDTO zhiCaiSkuHttpDTO) {
        JSONObject jsonObject =JSONObject.fromObject(zhiCaiSkuHttpDTO);
        String jsonStr = jsonObject.toString();
        try {
            String resultJsonStr = HttpUtil45.operateData("post","json",updateSpSkuUrl,timeConfig,null,jsonStr,null,null);
            JSONObject resultJsonObject = JSONObject.fromObject(resultJsonStr);
            Map<String,Class> keyMapConfig= new HashMap<>();
            keyMapConfig.put("content",String.class);
            ApiResponseBody apiResponseBody = (ApiResponseBody) JSONObject.toBean(resultJsonObject, ApiResponseBody.class, keyMapConfig);
            String code = apiResponseBody.getCode();
            if("0".equals(code)){
                logger.info("=============更新updateZhiCaiSkuList成功===============");
                System.out.println("=============更新updateZhiCaiSkuList成功===============");
            }else{
                loggerError.error("=============更新updateZhiCaiSkuList成功===============");
                System.err.println("=============更新updateZhiCaiSkuList成功===============");
            }

            System.out.println("更新updateZhiCaiSkuList resultJsonStr:"+resultJsonStr);
            logger.info("更新updateZhiCaiSkuList resultJsonStr:"+resultJsonStr);
        } catch (Exception e) {
            loggerError.error("更新updateSpMarketPrice 失败   zhiCaiSkuHttpDTO:"+zhiCaiSkuHttpDTO.toString());
            System.out.println("更新updateSpMarketPrice 失败   zhiCaiSkuHttpDTO:"+zhiCaiSkuHttpDTO.toString());
            e.printStackTrace();
        }
    }

}
