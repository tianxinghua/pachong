package com.shangpin.supplier.product.consumer.supplier.vipgroup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.message.original.body.SupplierProduct;
import com.shangpin.ephub.client.message.picture.body.SupplierPicture;
import com.shangpin.ephub.client.message.picture.image.Image;
import com.shangpin.supplier.product.consumer.exception.EpHubSupplierProductConsumerRuntimeException;
import com.shangpin.supplier.product.consumer.service.SupplierProductMongoService;
import com.shangpin.supplier.product.consumer.service.SupplierProductSaveAndSendToPending;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;
import com.shangpin.supplier.product.consumer.supplier.common.picture.PictureHandler;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;
import com.shangpin.supplier.product.consumer.supplier.gebnegozio.dto.SupplierToken;
import com.shangpin.supplier.product.consumer.supplier.vipgroup.dto.PictureResp;
import com.shangpin.supplier.product.consumer.supplier.vipgroup.dto.ProductAttrResp;
import com.shangpin.supplier.product.consumer.supplier.vipgroup.dto.TokenResp;
import com.shangpin.supplier.product.consumer.supplier.vipgroup.util.*;
import com.shangpin.supplier.product.consumer.supplier.vipgroup.dto.Product;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by zhaowenjun on 2018/10/12.
 */
@Component("vipgroupHandler")
@Slf4j
public class VipgroupHandle implements ISupplierHandler {
    Gson gson = new Gson();
    @Autowired
    private SupplierProductSaveAndSendToPending supplierProductSaveAndSendToPending;

    @Autowired
    private PictureHandler pictureHandler;

    @Autowired
    private SupplierProductMongoService mongoService;

    ObjectMapper mapper = new ObjectMapper();

    public static final String PRODUCT_URL = "http://open.vipgroup.com.hk/api/";
//    String GET_TOKEN_URL=PRODUCT_URL + "token?_method=PUT";
    public static final String tokenUrl = "http://api.ephub.spidc1.com/supplier-in-hub/supplierToken";
    @Override
    public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
        String token = "";
        try {
            if (!StringUtils.isBlank(message.getData())) {
                System.out.println("转换数据：" + message.getData());
                Product product = mapper.readValue(message.getData(),Product.class);
                String supplierId = message.getSupplierId();
                mongoService.save(supplierId, product.getSpuId(), product);
                HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
                SupplierToken supplierToken = queryToken(supplierId);
                if (null != supplierToken){token = supplierToken.getAccessToken();}
                boolean spuSuccess = convertSpu(supplierId, product, hubSpu , token);

                ArrayList<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
                HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
                boolean skuSuccess = convertSku(supplierId,hubSpu.getSupplierSpuId(), product, hubSku , token);
                if(skuSuccess){
                    hubSkus.add(hubSku);
                }

                //处理图片
                SupplierPicture supplierPicture = null;
                supplierPicture = pictureHandler.initSupplierPicture( message, hubSpu, converImage( product , token , supplierId ) );

                if(spuSuccess){
                    supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(),supplierId, message.getSupplierName(), hubSpu, hubSkus, supplierPicture);
                }

            }
        }catch (Exception e){
            log.error("Vipgroup转换商品 异常："+e.getMessage(),e);
        }
    }
    /**
     * 将geb原始dto转换成hub spu
     * @param supplierId 供应商门户id
     * @param product 供应商原始dto
     * @param hubSpu hub spu表
     */
    public boolean convertSpu(String supplierId, Product product, HubSupplierSpuDto hubSpu, String token ) throws EpHubSupplierProductConsumerRuntimeException {
        if(null != product){
            hubSpu.setSupplierId(supplierId);
            hubSpu.setSupplierSpuNo( product.getSpuId() );
            hubSpu.setSupplierSpuColor( getAttr( token,product.getProductId() ).get("color") );
            hubSpu.setSupplierSpuModel( product.getSpuId() );
            hubSpu.setSupplierSpuName( product.getName() );
            hubSpu.setSupplierGender( queryGender(product) );//name中包含则取name里的性别，不包含则为男
            hubSpu.setSupplierCategoryname( product.getCat() );
            hubSpu.setSupplierBrandname( product.getBrand() );
            hubSpu.setSupplierSeasonname( "四季" );//不知赋值
            hubSpu.setSupplierMaterial( getAttr( token,product.getProductId() ).get("material") );
            hubSpu.setSupplierOrigin( "" );//不知赋值
            hubSpu.setSupplierSpuDesc( getAttr(token,product.getProductId()).get("description") );
            hubSpu.setSupplierMeasurement(getAttr( token,product.getProductId() ).get("size"));//新加
            return true;
        }else{
            return false;
        }
    }

    /**
     * 将geb原始dto转换成hub sku
     * @param supplierId
     * @param supplierSpuId hub spuid
     * @param product
     * @param hubSku
     * @return
     */
    public boolean convertSku(String supplierId, Long supplierSpuId, Product product, HubSupplierSkuDto hubSku, String token) throws EpHubSupplierProductConsumerRuntimeException{
        if(null != product){
            hubSku.setSupplierSpuId(supplierSpuId);
            hubSku.setSupplierId(supplierId);
            String supplierSkuNo = product.getSkuId();
            hubSku.setSupplierSkuNo(supplierSkuNo);
            hubSku.setSupplierSkuName(product.getName());
            hubSku.setSupplierBarcode("");//暂无
            hubSku.setMarketPrice( new BigDecimal(StringUtil.verifyPrice( product.getListPrice() )) );//市场价
            hubSku.setSalesPrice( new BigDecimal(StringUtil.verifyPrice( product.getSellPrice() )) );//售价
            hubSku.setSupplyPrice( new BigDecimal(StringUtil.verifyPrice( product.getWholesale() )));//供价
            hubSku.setSupplierSkuSize("U");
            hubSku.setStock(Integer.valueOf(product.getStock()));
            return true;
        }else{
            return false;
        }
    }

    /**
     * frmoda 处理图片
     * @param product,token
     * @return
     */
    private List<Image> converImage(Product product, String token , String supplierId){
        List<Image> imageList = new ArrayList<Image>();
        Image image = new Image();
        String productId = product.getProductId();
        if( null != productId && !productId.equals("") && null != token && !token.equals("")) {
            productId = URLEncoder.encode(productId);
            String picUrl = PRODUCT_URL + "image?uid=087&token=" + token + "&productId=" + productId;
            HashMap<String, String> picUrlResp = HttpClientUtil.sendHttp(HttpRequestMethedEnum.HttpGet, picUrl, null, null, null);
            String picRespValue = picUrlResp.get("resBody");
            if (null != picUrlResp && picUrlResp.size()>0 &&picUrlResp.get("code").equals("200")) {
                if (null != picRespValue && !picRespValue.equals("") && !picRespValue.equals("null")) {
                    PictureResp pictureResp = gson.fromJson(picRespValue, PictureResp.class);
                    List<String> pictureLists = pictureResp.getDetailPics();
                    if (null != pictureLists && pictureLists.size() > 0) {
                        for (String pic : pictureLists) {
                            Image image1Detial = new Image();
                            image1Detial.setUrl(pic);
                            imageList.add(image1Detial);
                        }
                        image.setUrl(pictureResp.getMainPic());
                        imageList.add(image);
                    }
                }else {
                    log.info("token过期，正在重查");
                    SupplierToken supplierToken = queryToken(supplierId);
                    if (null != supplierToken)
                        token = supplierToken.getAccessToken();
                    converImage( product,  token, supplierId);
                }
            }
        }
        return imageList;
    }
    /**
     *  获取token
     * @return
     */
    /*public String selAccessToken(){
        String token = "";
        String entity = "{\"uid\":\"087\",\"username\":\"shangpin\",\"password\":\"10122han9p1n2018\"}";
        HashMap<String,String> productsJSON = HttpClientUtil.sendHttp(HttpRequestMethedEnum.HttpPost ,GET_TOKEN_URL  ,null, null,entity);
        String proToken = productsJSON.get("resBody");
        if(null != productsJSON && productsJSON.size() > 0 &&productsJSON.get("code").equals("200")){
            if(null!=proToken && !proToken.equals("") && !proToken.equals("null")){
                TokenResp tokenResp = gson.fromJson( proToken, TokenResp.class );
                String errorCode = tokenResp.getErrorCode();
                if(errorCode.equals("0")){
                    token = tokenResp.getToken();
                }else if (errorCode.equals("2")){
                    token = tokenResp.getToken();
                    log.info("获取token错误消息" + tokenResp.getErrorMessage());
                }else {
                    token = selAccessToken();
                    log.info("获取token错误消息" + tokenResp.getErrorMessage());
                }
            }
        }else {
            log.info("获取token异常："+ productsJSON.get("message"));
        }

        return token;
    }*/
    /**
     *  查询颜色、尺码、材质
     * @param token
     * @param productId
     * @return
     */
    public Map<String,String> getAttr( String token , String productId ){
        Map<String,String> colorAndMaterial = new HashMap<String, String>();
        productId = URLEncoder.encode(productId);
        String url = PRODUCT_URL + "productInfo?uid=087&pageNum=1&lang=0&productId=" + productId + "&token=" + token;
        HashMap<String,String> productsJSON = HttpClientUtil.sendHttp(HttpRequestMethedEnum.HttpGet ,url  ,null, null,null);
        String produValue = productsJSON.get("resBody");
        if(null != productsJSON && productsJSON.size() > 0 && productsJSON.get("code").equals("200")){
            if(null != produValue && !produValue.equals("")&& !produValue.equals("null")){
                ProductAttrResp productAttrResp = gson.fromJson( produValue, ProductAttrResp.class );
                if(productAttrResp.getErrorCode().equals("0")){
                    List<String> attributes = productAttrResp.getData().get(0).getAttributes();
                    if (null != attributes && attributes.size() >0 ) {
                        String proColor = "";
                        String proMaterial = "";
                        String proSize = "U";
                        for (String attr : attributes) {
                            if (attr.contains("顏色")) {
                                proColor = attr;
                            }
                            if (attr.contains("材料") || attr.contains("材质")) {
                                proMaterial = attr;
                            }
                        }
                        colorAndMaterial.put("color", proColor);
                        colorAndMaterial.put("material", proMaterial);
                        colorAndMaterial.put("size", proSize);
                        colorAndMaterial.put("description",String.join("；", attributes));
                    }
                }else{
                        log.info("获取属性失败" + productAttrResp.getErrorMessage());
                 }
                }
        }
        return colorAndMaterial;
    }

    /**
     * 处理性别
     * @param product
     * @return
     */
    public String queryGender(Product product){
        String gender = "男";
        String name = product.getName();
        if ( null != name && !name.equals("") ){
            if ( name.contains("女") ){
                gender = "女";
            }
        }
        return gender;
    }
    /**
     *  根据 supplierId 查token
     * @param supplierId
     * @return
     */
    public SupplierToken queryToken(String supplierId){
        SupplierToken supplierTokenDTO = new SupplierToken();
        TokenResp tokenResp = new TokenResp();
        Map<String, String> param = new HashMap<String, String>();
        param.put("supplierId", supplierId);
        try {
            String result = HttpUtil45.operateData("get", "", tokenUrl, new OutTimeConfig(1000 * 60 * 3,
                    1000 * 60 * 30, 1000 * 60 * 30), param, "", "", "");
            System.out.println("根据 supplierId 查token：" + result);
            log.info("根据 supplierId 查token：" + result);
            tokenResp = gson.fromJson( result, TokenResp.class);
            String data = tokenResp.getData();
            if (null != tokenResp && !tokenResp.equals("") && tokenResp.getCode().equals("200")){
                if( null != data && !data.equals("") ){
                    supplierTokenDTO = gson.fromJson(data,SupplierToken.class);
                    log.info("数据库存在supplierId为 "+supplierId+" 的token：" + data);
                    System.out.println("数据库存在supplierId为 "+supplierId+" 的token：" + data);
                }else {
                    supplierTokenDTO = null;
                    log.info("数据库不存在supplierId为 "+supplierId+" 的token：" + data);
                    System.out.println("数据库不存在supplierId为 "+supplierId+" 的token：" + data);
                }
            }else {
                supplierTokenDTO = null;
                log.info("根据 supplierId 查token失败：" + tokenResp.getMessage());
                System.out.println("根据 supplierId 查token失败：" + tokenResp.getMessage());
            }
        } catch (ServiceException e) {
            log.error("根据 supplierId 查token异常：" + e.getMessage());
            e.printStackTrace();
        }
        return supplierTokenDTO;
    }
}
