package com.shangpin.iog.maje.service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.shangpin.iog.maje.dto.*;
import com.shangpin.iog.utils.HttpResponse;
import com.shangpin.iog.utils.HttpUtil45;
import com.shangpin.iog.utils.HttpUtils;
import com.shangpin.openapi.api.sdk.client.OutTimeConfig;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.Header;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wanner on 2018/6/27
 */
@Component("fetchStockImpl")
public class FetchStockImpl {

    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");

    private static ResourceBundle bdl = null;
    private static String supplierId = "",supplierNo = "",fetchSpProductInfosUrl ="",updateSpMarketPriceUrl="",pageSize="";

    private static OutputStreamWriter  out= null;
    private static OutputStreamWriter  priceOut= null;

    static String splitSign = ",";
    //库存csv 文件存放目录
    private static String filePath="";

    //MONCLER法国官网地址
    private static String uri="";

    //有库存
    private static final String IN_STOCK = "10";
    //无库存
    private static final String NO_STOCK = "0";

    // 请求失败的尚品 skuNo 集合
    private static List<SpSkuNoDTO> failedSpSkuNoList = null;

    static {
        if (null == bdl){
            bdl = ResourceBundle.getBundle("conf");
        }
        supplierId = bdl.getString("supplierId");

        supplierNo = bdl.getString("supplierNo");

        fetchSpProductInfosUrl = bdl.getString("fetchSpProductInfosUrl");

        updateSpMarketPriceUrl = bdl.getString("updateSpMarketPriceUrl");

        pageSize = bdl.getString("pageSize");

        filePath = bdl.getString("csvFilePath");

        uri = bdl.getString("uri");

    }

    private static OutTimeConfig timeConfig = new OutTimeConfig(1000*60*30,1000*60*30,1000*60*30);

    /**
     * 拉取 法国官网 商品库存数据
     */
    public void fetchItlyProductStock(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String startDateTime = format.format(new Date());
        System.out.println("============拉取maje库存数据开始 "+startDateTime+"=========================");
        logger.info("==============拉取maje库存数据开始 "+startDateTime+"=========================");

        //1. 请求需要更新库存商品 信息接口
        failedSpSkuNoList = new ArrayList<>();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = simpleDateFormat.format(new Date());

        String temFilePath = filePath + "maje-qty-"+todayStr+".csv";
        String priceFilePath = filePath + "maje-price-"+todayStr+".csv";
        System.out.println("文件保存目录："+temFilePath);
        logger.info("文件保存目录："+temFilePath);
        try {
            out = new OutputStreamWriter(new FileOutputStream(temFilePath, true),"gb2312");
            priceOut = new OutputStreamWriter(new FileOutputStream(priceFilePath, true),"gb2312");

        } catch (IOException e) {
            e.printStackTrace();
        }

        StringBuffer buffer = new StringBuffer(
                "spSkuNO" + splitSign +
                        "qty" + splitSign
        ).append("\r\n");
        StringBuffer priceBuffer = new StringBuffer(
                "supplierId" + splitSign +
                        "spSkuNO" + splitSign +
                        "oldPrice" + splitSign +
                        "newPrice" + splitSign+
                        "productUrl" + splitSign
        ).append("\r\n");
        try {
            out.write(buffer.toString());
            priceOut.write(priceBuffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //定义所有需要的更新库存商品信息List
        List<ProductDTO> productDTOAllList =  new LinkedList<>();

        //获取第一页商品数据
        ShangPinPageContent selfridgesPageContent = getShangPinPageContentByParam(supplierId,"","fr.maje.com",1, Integer.parseInt(pageSize));
        productDTOAllList.addAll(selfridgesPageContent.getZhiCaiResultList());

        if(selfridgesPageContent == null) return;
        //总记录数
        Integer total = selfridgesPageContent.getTotal();
        Integer pageNumber = getPageNumber(total, 20);
        for (int i = 2; i <= pageNumber; i++) {
            ShangPinPageContent temselfridgesPageContent = getShangPinPageContentByParam(supplierId,"","fr.maje.com", i, Integer.parseInt(pageSize));
            if(temselfridgesPageContent!=null){
                productDTOAllList.addAll(temselfridgesPageContent.getZhiCaiResultList());
            }else{ //请求失败重新 再次请求
                temselfridgesPageContent = getShangPinPageContentByParam(supplierId,"","fr.maje.com", i, Integer.parseInt(pageSize));
                if(temselfridgesPageContent!=null){
                    productDTOAllList.addAll(temselfridgesPageContent.getZhiCaiResultList());
                }
            }
        }

        logger.info("=====需要更新maje spProduct Size:"+productDTOAllList.size());
        System.out.println("=====需要更新maje spProduct Size:"+productDTOAllList.size());
        //导出尚品库存数据
        exportQtyInfoForProductList(productDTOAllList);

        // 处理 请求失败的 尚品 skuNo 信息
        int failedSpSkuNoSize = failedSpSkuNoList.size();
        for (int i = 0; i < failedSpSkuNoSize; i++) {
            repeatSolveFailedSpSkuNo(failedSpSkuNoList.get(i));
        }
        try {
            out.close();
            priceOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String endtDateTime = format.format(new Date());
        logger.info("===================拉取maje库存数据结束 "+endtDateTime+"=========================");
        System.out.println("=================拉取maje库存数据结束 "+endtDateTime+"=========================");

    }

    /**
     * 获取商品页数
     * @param total 总记录数
     * @param pageSize 每页显示数
     * @return 页数
     */
    public static Integer getPageNumber(Integer total,Integer pageSize){
        Integer pageNumner = total/pageSize;
        //当余数大于0 的时候 页数加一
        if(total%pageSize>0){
            pageNumner++;
        }
        return pageNumner;
    }

    /**
     * 获取需要更新库存商品 分页数据
     * @param brandName 供应商名称
     * @param pageIndex 页码
     * @param pageSize 分页条数
     * @return
     */
    public static ShangPinPageContent getShangPinPageContentByParam(String supplierId,String brandName,String channel,Integer pageIndex,Integer pageSize){
        //String fetchSpProductInfosUrl = "http://192.168.20.176:8003/supplier-sku/get-product";
        //1. 请求需要更新库存商品 信息接口
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("supplierId",supplierId);
        jsonObject.put("brandName",brandName);
        jsonObject.put("pageIndex",pageIndex);
        jsonObject.put("pageSize",pageSize);
        jsonObject.put("channel",channel);
        String jsonStr = jsonObject.toString();

        ShangPinPageContent shangPinPageContent = null;
        try {
            String resultJsonStr = HttpUtil45.operateData("post","json",fetchSpProductInfosUrl,timeConfig,null,jsonStr,null,null);
            //System.out.println("=======resultJsonStr:"+resultJsonStr);
            //logger.info("=======resultJsonStr:"+resultJsonStr);
            JSONObject resultJsonObject = JSONObject.fromObject(resultJsonStr);
            Map<String,Class> keyMapConfig= new HashMap<>();
            keyMapConfig.put("zhiCaiResultList",ProductDTO.class);
            keyMapConfig.put("zhiCaiSkuResultList", SkuDTO.class);
            keyMapConfig.put("content",ShangPinPageContent.class);
            ApiResponseBody apiResponseBody = (ApiResponseBody) JSONObject.toBean(resultJsonObject, ApiResponseBody.class, keyMapConfig);

            if(apiResponseBody!=null){
                shangPinPageContent = (ShangPinPageContent) apiResponseBody.getContent();
            }
            System.out.println();
            System.out.println("获取第 "+pageIndex+"页成功 :"+resultJsonStr);
            logger.info("获取第 "+pageIndex+"页成功 :"+resultJsonStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return shangPinPageContent;
    }


    /**
     * 循环遍历 拉取商品库存信息
     * @param productDTOAllList 商品信息list
     */
    public static void exportQtyInfoForProductList(List<ProductDTO> productDTOAllList){
        for (ProductDTO productDTO:productDTOAllList) {
            boolean flag = solveProductQty(productDTO);
            if(!flag){
                repeatSolveFailProductQty(productDTO);
            }
        }
    }

    /**
     * 重复处理 spSkuNO qty 信息
     * @param spSkuNoDTO
     */
    public static void repeatSolveFailedSpSkuNo(SpSkuNoDTO spSkuNoDTO){
        int count = 0;
        while(count>4){
            count++;
            Map<String, String> checkProductInfo = getProductQtyInfo(spSkuNoDTO.getSpSkuNo(),spSkuNoDTO.getPcode());
            String temQty = checkProductInfo.get("qty");
            if (temQty != null) {
                exportSpSkunoAndQty(spSkuNoDTO.getSpSkuNo(),temQty);
                return;
            }
        }
        //重复请求 失败 做商品 库存置零处理
        exportSpSkunoAndQty(spSkuNoDTO.getSpSkuNo(),NO_STOCK);
    }

    /**
     * 重复请求 处理 商品库存信息 ，最后没有处理成功 直接将 库存置0 处理
     * @param productDTO
     */
    private static void repeatSolveFailProductQty(ProductDTO productDTO) {
        int count = 0;
        while(count<4){
            count++;
            boolean temFlag = solveProductQty(productDTO);
            if(temFlag){
                return;
            }
        }
        //重复请求 失败 做商品 库存置零处理
        setQtyZeroForProduct(productDTO);
    }

    /**
     * 将商品所有 spSkuNO 置零 并导出到 csv
     * @param productDTO
     */
    private static void setQtyZeroForProduct(ProductDTO productDTO) {
        if(productDTO!=null){
            List<SkuDTO> zhiCaiSkuResultList = productDTO.getZhiCaiSkuResultList();
            if(zhiCaiSkuResultList!=null&&zhiCaiSkuResultList.size()>0){
                int size = zhiCaiSkuResultList.size();
                for (int i = 0; i < size; i++) {
                    SkuDTO skuDTO = zhiCaiSkuResultList.get(i);
                    String spSkuNo = skuDTO.getSpSkuNo();
                    if(spSkuNo!=null){
                        exportSpSkunoAndQty(spSkuNo,NO_STOCK);
                    }
                }
            }
        }

    }

    /**
     * 处理单个商品库存信息
     * @param productDTO 商品信息
     */
    private static boolean solveProductQty(ProductDTO productDTO) {
        String productUrl = productDTO.getProductUrl();
        List<SkuDTO> zhiCaiSkuResultList = productDTO.getZhiCaiSkuResultList();
        int zhiCaiSkuResultListSize = zhiCaiSkuResultList.size();
        try {
            Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36)");
            Header[] headers = new Header[1];
            headers[0] = header;

            HttpResponse response = HttpUtils.get(productUrl,headers);
            //HttpResponse response = HttpUtils.get(productUrl);
            if (response.getStatus()==200) {
                String htmlContent = response.getResponse();
                Document doc = Jsoup.parse(htmlContent);


                /**
                 *   处理商品 的尺码 以及 库存信息
                 */
                //判断当前包页面有没有尺码信息  有分成多个 product 没有 尺码为均码
                Elements sizeElements=doc.select("#product-content > div.product-variations > ul > li.attribute.size > div > ul > li");
                if(sizeElements!=null&&sizeElements.size()>0){
                    int spSkuSize = zhiCaiSkuResultList.size();
                    int pageSize=sizeElements.size();
                    for (int j = 0; j <spSkuSize ; j++) {
                        SkuDTO skuDTO = zhiCaiSkuResultList.get(j);
                        for (int i = 0; i <pageSize ; i++) {
                            String sizeValue=sizeElements.get(i).select("a").attr("title").toString();

                            String temQty="";
                            try {

                                String jdbcSize=skuDTO.getSize();
                                if(!sizeValue.equals(jdbcSize)){
                                    continue;
                                }
                                temQty=sizeElements.attr("class").toString();
                                if (temQty.contains("unselectable")){
                                    temQty=NO_STOCK;
                                }
                                else{
                                    temQty=IN_STOCK;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                temQty=NO_STOCK;
                            }
                            //
                            String  price=doc.select("#pdpMain > div.wrapper-product-image-container.clearfix > div.product-col-2.product-detail > div.productPrices > div > span").attr("content").toString();

                            byte bytes[] = {(byte) 0xC2,(byte) 0xA0};
                            String UTFSpace = new String(bytes,"utf-8");
                            price = price.replaceAll(UTFSpace, "&nbsp;");
                            String marketPrice = skuDTO.getMarketPrice();
                            if(marketPrice!=null){
                                float temElementPrice = Float.parseFloat(price);
                                float spMarketPrice = Float.parseFloat(marketPrice);
                                if(temElementPrice!=spMarketPrice){ //价格发生改变
                                    updateSpSkuMarketPrice(skuDTO.getSupplierSkuNo(),price);
                                    exportSpSkunoAndPrice(supplierId,skuDTO.getSpSkuNo(),spMarketPrice,temElementPrice,productUrl);
                                    logger.info("推送 价格成功："+ skuDTO.getSupplierSkuNo()+" 原价："+marketPrice+" 新价:"+price);
                                    System.out.println("推送 价格成功："+ skuDTO.getSupplierSkuNo()+" 原价："+marketPrice+" 新价:"+price);
                                }
                            }else{
                                loggerError.error("getMarketPrice 为空 ProductDTO:"+productDTO.toString());
                            }
                            exportSpSkunoAndQty(skuDTO.getSpSkuNo(),temQty);
                            break;
                        }
                    }
                }else{
                    logger.error("===请求商品地址解析 商品尺码失败===========================================");
                    logger.error(productDTO.toString());
                    logger.error("===请求商品地址解析 商品尺码失败===========================================");
                    // 商品页面中没有获取到尺码信息重新请求
                    return false;
                }
            }else{
                logger.error("================请求商品地址失败===========================================");
                logger.error(productDTO.toString());
                logger.error("================请求商品地址失败===========================================");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        //每一款商品休息2s
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        return true;
    }
    /**
     * 更新尚品 spSkuMarketPrice
     * @param supplierSkuNo
     * @param marketPrice
     */
    private static void updateSpSkuMarketPrice(String supplierSkuNo, String marketPrice) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("supplierId",supplierId);
        jsonObject.put("supplierNo",supplierNo);
        jsonObject.put("supplierSkuNo",supplierSkuNo);
        jsonObject.put("marketPrice",marketPrice);
        String jsonStr = jsonObject.toString();
        System.out.println(" 推送价格入参json:"+jsonStr);
        logger.info(" 推送价格入参json:"+jsonStr);
        try {
            String resultJsonStr = HttpUtil45.operateData("post","json",updateSpMarketPriceUrl,timeConfig,null,jsonStr,null,null);
            JSONObject resultJsonObject = JSONObject.fromObject(resultJsonStr);
            Map<String,Class> keyMapConfig= new HashMap<>();
            keyMapConfig.put("content",String.class);
            ApiResponseBody apiResponseBody = (ApiResponseBody) JSONObject.toBean(resultJsonObject, ApiResponseBody.class, keyMapConfig);
            String code = apiResponseBody.getCode();
            if("0".equals(code)){
                logger.info("==更新updateSpSkuMarketPrice成功:"+supplierId+":"+supplierSkuNo+":"+marketPrice+"===============");
                System.out.println("==更新updateSpSkuMarketPrice成功:"+supplierId+":"+supplierSkuNo+":"+marketPrice+"===============");
            }else{
                loggerError.error("==更新updateSpSkuMarketPrice 失败=="+jsonStr);
                System.err.println("==更新updateSpSkuMarketPrice成功=="+jsonStr);
            }
            System.out.println("更新updateSpMarketPrice resultJsonStr:"+resultJsonStr);
            logger.info("更新updateSpMarketPrice resultJsonStr:"+resultJsonStr);
        } catch (Exception e) {
            loggerError.error("更新updateSpMarketPrice 失败   supplierSkuNo:"+supplierSkuNo+"marketPrice:"+marketPrice);
            System.out.println("更新updateSpMarketPrice 失败   supplierSkuNo:"+supplierSkuNo+"marketPrice:"+marketPrice);
            e.printStackTrace();
        }

    }

    /**
     * 获取尺码库存信息
     * @param spSkuNO 尚品尺码 skuNo
     * @param pcode 商品尺码 pcode 请求参数
     * @return
     */
    public static  Map<String,String> getProductQtyInfo(String spSkuNO, String pcode){
        //库存结果集
        HashMap<String, String> mapDate = new HashMap<>();
        //  /it/it/p/ajax/product-detail-shipping.ajax?pcode=808354516
        String checkProductQtyUrl = uri + "/it/it/p/ajax/product-detail-shipping.ajax?pcode="+pcode;

        try {
            getProductQtyUnit(checkProductQtyUrl,mapDate);
        } catch (Exception e) {
            logger.info("=========尝试再次获取=====");
            try {
                getProductQtyUnit(checkProductQtyUrl,mapDate);
            } catch (Exception ee) {
                logger.info("=========第二次：获取库存失败 ===spSkuNO: "+spSkuNO +"pcode:"+pcode);
                ee.printStackTrace();
                System.out.println();
                System.err.println("=========第二次：获取库存失败 ===spSkuNO: "+spSkuNO +"pcode:"+pcode);
            }
        }
        return mapDate;
    }

    /**
     * 获取商品库存信息
     * @param checkProductUrl
     * @throws Exception
     */
    public static void getProductQtyUnit(String checkProductUrl,Map<String,String> mapDate) throws Exception{
        Header header = new Header("User-Agent", "Mozilla/5.0 (Windows NT 6.1)AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.124 Safari/537.36)");
        Header[] headers = new Header[1];
        headers[0] = header;
        HttpResponse checkResponse = HttpUtils.get(checkProductUrl,headers);
        if (checkResponse.getStatus()==200) {
            /**
             <p class="title">DISPONIBILE	</p>
             <div class="subtitle">Il prodotto è disponibile per la spedizione. La consegna è stimata in 1-2 giorni lavorativi.</div>
             */
            String responseHtml = checkResponse.getResponse();
            if(responseHtml==null||"".equals(responseHtml)){
                //没有库存
                mapDate.put("qty",NO_STOCK);
                mapDate.put("qtyDesc","售罄");
                return;
            }
            Document doc = Jsoup.parse(responseHtml);

            String qtyStr = doc.select("p.title").first().text();
            // DISPONIBILE SOLO PER PRE-ORDINE | DISPONIBILE  | DISPONIBILE IN 1-2 SETTIMANE

            if(qtyStr!=null){
                if(qtyStr.contains("PRE-ORDINE")){ //预售
                    mapDate.put("qty",NO_STOCK);
                    mapDate.put("qtyDesc","预售");
                }else if(qtyStr.contains("DISPONIBILE")){
                    mapDate.put("qty",IN_STOCK);
                    mapDate.put("qtyDesc","有货");
                }
            }else{
                // 售罄
                mapDate.put("qty",NO_STOCK);
                mapDate.put("qtyDesc","售罄");
            }
        }else{
            mapDate.put("qty",NO_STOCK);
            mapDate.put("qtyDesc","售罄");
        }
    }

    /**
     * 导出 商品skuNo 和 qty 信息
     * @param spSkuNo 尚品skuNo
     * @param qty 库存信息
     */
    private static void exportSpSkunoAndQty(String spSkuNo, String qty) {
        //继续追加
        StringBuffer buffer = new StringBuffer();
        try {
            buffer.append(spSkuNo).append(splitSign);
            buffer.append(qty).append(splitSign);
            buffer.append("\r\n");
            out.write(buffer.toString());
            System.out.print("spSkuNo:"+spSkuNo+" qty:"+qty+"|");
            logger.info("spSkuNo:"+spSkuNo+" qty:"+qty+"|");
            logger.info(buffer.toString());
            out.flush();
        } catch (Exception e) {
        }
    }
    /**
     * 导出 商品skuNo 和 qty 信息
     * @param spSkuNo 尚品skuNo
     * @param oldPrice 库中价格
     *  @param newPrice doc价格
     */
    private static void exportSpSkunoAndPrice(String supplierId,String spSkuNo, Float oldPrice,Float newPrice,String productUrl) {
        //继续追加
        StringBuffer buffer = new StringBuffer();
        try {
            buffer.append(supplierId).append(splitSign);
            buffer.append(spSkuNo).append(splitSign);
            buffer.append(oldPrice).append(splitSign);
            buffer.append(newPrice).append(splitSign);
            buffer.append(productUrl).append(splitSign);
            buffer.append("\r\n");
            priceOut.write(buffer.toString());
            System.out.println("spSkuNo:"+spSkuNo+" newPrice:"+newPrice+"|");
            logger.info("spSkuNo:"+spSkuNo+" newPrice:"+newPrice+"|");
            logger.info(buffer.toString());
            priceOut.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    protected static void sendMail(){

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props);
        Message message = new MimeMessage(session);
        try
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String todayStr = simpleDateFormat.format(new Date());
            long dayTime = 1000*3600*24l;
            Date yesterDate = new Date(new Date().getTime() - dayTime);
            String yesterdayDateStr = simpleDateFormat.format(yesterDate);
            message.setSubject(yesterdayDateStr+"-"+bdl.getString("uri")+"修改了价格的商品信息");
            message.setFrom(new InternetAddress("jiangtao.nan@shangpin.com"));
            //发送给谁
            message.setRecipient(Message.RecipientType.TO, new InternetAddress("sophia.huo@shangpin.com"));
            //设置抄送人
            message.setRecipient(Message.RecipientType.CC, new InternetAddress("jiangtao.nan@shangpin.com"));
            Multipart multipart = new MimeMultipart();
            //实例化一个bodypart用于封装内容
            BodyPart bodyPart = new MimeBodyPart();
            bodyPart.setContent("<font color='red'>修改了价格的商品信息，见附件</font>","text/html;charset=utf8");
            //添加bodypart到multipart
            multipart.addBodyPart(bodyPart);
            //每一个部分实例化一个bodypart，故每个附件也需要实例化一个bodypart
            bodyPart = new MimeBodyPart();

            //实例化DataSource(来自jaf)，参数为文件的地址
            DataSource dataSource = new FileDataSource(bdl.getString("csvFilePath")+"maje-price-"+yesterdayDateStr+".csv");
            //使用datasource实例化datahandler
            DataHandler dataHandler = new DataHandler(dataSource);
            bodyPart.setDataHandler(dataHandler);
            //设置附件标题，使用MimeUtility进行名字转码，否则接收到的是乱码
            bodyPart.setFileName(javax.mail.internet.MimeUtility.encodeText(yesterdayDateStr+"修改了价格的商品信息"+".csv"));
            multipart.addBodyPart(bodyPart);
            message.setContent(multipart);
            Transport transport = session.getTransport("smtp");
            transport.connect("smtp.shangpin.com","jiangtao.nan@shangpin.com", "njt123456");
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            logger.info("===================发送邮件成功 =========================");
            System.out.println("===================发送邮件成功 =========================");
        }catch(Exception e) {
            e.printStackTrace();
            loggerError.error(" ===================发送邮件失败=========================");
            System.out.println("===================发送邮件失败 =========================");
        }
    }

    public static void main(String[] args) {
        FetchStockImpl o=new FetchStockImpl();
        o.getFileToEmail();
    }
    protected void getFileToEmail(){
        long dayTime = 1000*3600*24l;
        Date yesterDate = new Date(new Date().getTime() - dayTime);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String yesterdayDateStr = simpleDateFormat.format(yesterDate);
        String fileName=bdl.getString("csvFilePath")+"maje-price-"+yesterdayDateStr+".csv";
        File file=new File(bdl.getString("csvFilePath")+"maje-price-"+yesterdayDateStr+".csv");
        try {
            FileInputStream fis = new FileInputStream(file);
            System.out.println("文件的大小是："+fis.available()+"\n");
            if (fis.available()>50){
                sendMail();
                fis.close();
            }else {
                fis.close();
                deleteFile(fileName);
                logger.info("===================没有价格改变的商品不需邮箱发送 =========================");
                System.out.println("===================没有价格改变的商品不需邮箱发送 =========================");
            }
        } catch (IOException e) {
            e.printStackTrace();
            loggerError.error(" ===================寻找csv文件失败=========================");
            System.out.println("===================寻找csv文件失败 =========================");
        }
    }
    protected static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        System.gc();
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }
   /* public static void main(String[] args) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductUrl("https://fr.maje.com/fr/nouveautes/categories/pret-a-porter/ravira/H18RAVIRA.html?dwvar_H18RAVIRA_color=0102");
        List<SkuDTO> zhiCaiSkuResultList = new ArrayList<>();
        SkuDTO skuDTO = new SkuDTO();
        skuDTO.setSize("2");
        skuDTO.setMarketPrice("274");

        zhiCaiSkuResultList.add(skuDTO);
        productDTO.setZhiCaiSkuResultList(zhiCaiSkuResultList);
        solveProductQty(productDTO);

        //updateSpSkuMarketPrice("454070 A7M0T 5909-U","550");
    }*/

}
