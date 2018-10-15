package com.shangpin.supplier.product.consumer.supplier.gebnegozio;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDto;
import com.shangpin.ephub.client.data.mysql.picture.gateway.HubSpuPendingPicGateWay;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSupplierSpuGateWay;
import com.shangpin.ephub.client.message.original.body.SupplierProduct;
import com.shangpin.ephub.client.message.picture.body.SupplierPicture;
import com.shangpin.ephub.client.message.picture.image.Image;
import com.shangpin.supplier.product.consumer.exception.EpHubSupplierProductConsumerRuntimeException;
import com.shangpin.supplier.product.consumer.service.SupplierProductMongoService;
import com.shangpin.supplier.product.consumer.service.SupplierProductSaveAndSendToPending;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;
import com.shangpin.supplier.product.consumer.supplier.common.picture.PictureHandler;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;
import com.shangpin.supplier.product.consumer.supplier.gebnegozio.dto.*;
import com.shangpin.supplier.product.consumer.supplier.gebnegozio.util.HttpClientUtil;
import com.shangpin.supplier.product.consumer.supplier.gebnegozio.util.HttpRequestMethedEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by zhaowenjun on 2018/9/7.
 */
@Component("gebnegozioHandler")
@Slf4j
public class GebnegozioHandle implements ISupplierHandler {
    public static final String POST_URL = "http://gebnegozio-qas.extranet.alpenite.com/rest/marketplace_shangpin/V1/integration/customer/token";
    public static final String ATTRIBUTE_URL = "http://gebnegozio-qas.extranet.alpenite.com/rest/marketplace_shangpin/V1/products/attributes/";
    public static final String STOCK_URL = "http://gebnegozio-qas.extranet.alpenite.com/rest/marketplace_shangpin/V1/stockStatuses/";
    public static final String CATEGORY_URL = "http://gebnegozio-qas.extranet.alpenite.com/rest/marketplace_shangpin/V1/categories/";
    public static final String PRODUCT_DETAIL_URL = "http://gebnegozio-qas.extranet.alpenite.com/rest/marketplace_shangpin/V1/products/";
    Gson gson = new Gson();
    @Autowired
    private SupplierProductSaveAndSendToPending supplierProductSaveAndSendToPending;

    @Autowired
    private PictureHandler pictureHandler;

    @Autowired
    private SupplierProductMongoService mongoService;

    @Autowired
    private HubSupplierSpuGateWay spuGateWay;

    @Autowired
    private HubSpuPendingPicGateWay picGateWay;

    ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
        String token = selToken();
        if ( null != token && !token.equals("") ){
        try {
            if (!StringUtils.isBlank(message.getData())){
                System.out.println("看转换数据："+message.getData());
                GebnegozioDTO gebnegozioDTO = mapper.readValue(message.getData(),GebnegozioDTO.class);
                String supplierId = message.getSupplierId();
                gebnegozioDTO.setSpu(gebnegozioDTO.getId());
                mongoService.save(supplierId, gebnegozioDTO.getSpu(), gebnegozioDTO);

                HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
                boolean spuSuccess = convertSpu(supplierId, gebnegozioDTO, hubSpu , token);

                ArrayList<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
                HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
                boolean skuSuccess = convertSku(supplierId,hubSpu.getSupplierSpuId(), gebnegozioDTO, hubSku , token);
                if(skuSuccess){
                    hubSkus.add(hubSku);
                }

                //处理图片
                SupplierPicture supplierPicture = null;
                supplierPicture = pictureHandler.initSupplierPicture( message, hubSpu, converImage(supplierId, gebnegozioDTO , token ) );

                if(spuSuccess){
                    supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(),supplierId, message.getSupplierName(), hubSpu, hubSkus, supplierPicture);
                }

            }
        }catch (Exception e){
            log.error("Gebnegozio转换商品 异常："+e.getMessage(),e);
        }
        }
    }

    /**
     * 将geb原始dto转换成hub spu
     * @param supplierId 供应商门户id
     * @param item 供应商原始dto
     * @param hubSpu hub spu表
     */
    public boolean convertSpu(String supplierId, GebnegozioDTO item, HubSupplierSpuDto hubSpu, String token ) throws EpHubSupplierProductConsumerRuntimeException {
        if(null != item){
            String seasonName = selProductAttribute( item , token , "season");
            String supplierSpuNo =  selProductAttribute( item , token , "modello");
            if(StringUtils.isBlank(supplierSpuNo)) return false;
            HubSupplierSpuDto spuDb= null;
            if( null == seasonName ){
                HubSupplierSpuCriteriaDto spuCriteria = new HubSupplierSpuCriteriaDto();
                spuCriteria.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierSpuNoEqualTo(supplierSpuNo.trim());
                List<HubSupplierSpuDto> hubSupplierSpuDtos = spuGateWay.selectByCriteria(spuCriteria);
                if(null!=hubSupplierSpuDtos&&hubSupplierSpuDtos.size()>0){
                    spuDb =  hubSupplierSpuDtos.get(0);
                }else{
                    seasonName = "";
                }
            }
            //处理Meterial 和 Origin
            List<String> meterialLists = new ArrayList<String>();
            String origin = "";//Italy
            String meterial = "";//Cotton100 %
            String meterialAndOrigin = selProductAttribute( item , token , "details");
            if( null != meterialAndOrigin && !meterialAndOrigin.equals("")){
                String  productDetail = meterialAndOrigin.replaceAll("\r\n", ";");
                String productMaterial = productDetail.replaceAll("</?[^>]+>", "");
                System.out.println("产品详情里面的信息，包括产地和材质：" + productDetail);
                System.out.println("产品的材质：" + productMaterial);
                log.info("产品详情里面的信息，包括产地和材质：" + productDetail);
                log.info("产品的材质：" + productMaterial);
                if(null !=productMaterial && !productMaterial.equals("")){
                    meterialLists = Arrays.asList(productMaterial.split(";"));
                    final CopyOnWriteArrayList<String> cowList = new CopyOnWriteArrayList<String>(meterialLists);
                    for( String val : cowList ){
                        if( val.startsWith("Made in ") ){
                            int start = val.lastIndexOf("in ");
                            origin = val.substring( start + 3 );
                            cowList.remove(val);
                            break;
                        }

                    }
                    meterial = String.join("，", cowList);
                }
            }

            hubSpu.setSupplierId(supplierId);
            hubSpu.setSupplierSpuNo( supplierSpuNo.trim() );
            hubSpu.setSupplierSpuColor( selProductAttribute( item , token , "color") );
            hubSpu.setSupplierSpuModel( selProductAttribute( item , token , "modello") );
            hubSpu.setSupplierSpuName(item.getName());
            hubSpu.setSupplierGender( selProductAttribute( item , token , "gender") );
            hubSpu.setSupplierCategoryname( selCate(item.getSku() , token) );
            hubSpu.setSupplierBrandname( selProductAttribute( item , token , "designer") );
            hubSpu.setSupplierSpuDesc( selProductAttribute( item , token , "description") );
            hubSpu.setSupplierOrigin( origin );
            hubSpu.setSupplierMaterial(meterial);
            hubSpu.setSupplierSeasonname( seasonName );
            if(null!=spuDb){
                hubSpu.setSupplierOrigin( spuDb.getSupplierOrigin() );
                hubSpu.setSupplierMaterial(spuDb.getSupplierMaterial());
                hubSpu.setSupplierSeasonname( spuDb.getSupplierSeasonname() );
                hubSpu.setSupplierCategoryname(spuDb.getSupplierCategoryname());
            }
            return true;
        }else{
            return false;
        }
    }

    /**
     * 将geb原始dto转换成hub sku
     * @param supplierId
     * @param supplierSpuId hub spuid
     * @param item
     * @param hubSku
     * @return
     */
    public boolean convertSku(String supplierId, Long supplierSpuId, GebnegozioDTO item, HubSupplierSkuDto hubSku, String token) throws EpHubSupplierProductConsumerRuntimeException{
        if(null != item){
            hubSku.setSupplierSpuId(supplierSpuId);
            hubSku.setSupplierId(supplierId);
            String size = null;
            size = selProductAttribute( item , token , "size");
            if(size==null){
                size = "A";
            }
            //String price = selProductAttribute( item , token , "cost");
            String supplierSkuNo = item.getSku();
            hubSku.setSupplierSkuNo(supplierSkuNo);
            hubSku.setSupplierSkuName(item.getName());
            hubSku.setSupplierBarcode(item.getBarcode());
            hubSku.setMarketPrice( item.getFinal_price() );//市场价
            hubSku.setSalesPrice( item.getFinal_price() );//售价
            hubSku.setSupplyPrice( item.getFinal_price() );//供价
            /*
            hubSku.setMarketPrice( item.getFinal_price() );
            hubSku.setSupplyPrice( item.getPrice() );
            */
            hubSku.setSupplierSkuSize(size);
            hubSku.setStock(StringUtil.verifyStock(( selStock( item.getSku(), token ).toString() )));
            return true;
        }else{
            return false;
        }
    }

    /**
     * frmoda 处理图片
     * @param gebnegozioDTO,token
     * @return
     */
    private List<Image> converImage(String  supplierId,GebnegozioDTO gebnegozioDTO, String token){
        String picUrl = "https://www.gebnegozionline.com/media/catalog/product";
        List<Image> images = new ArrayList<Image>();
        //如果为简单对象 需要从图片表中查询复杂对象图片
        if("simple".equals(gebnegozioDTO.getType_id())){
            String supplierSpuNo =  selProductAttribute( gebnegozioDTO , token , "modello");
            HubSpuPendingPicCriteriaDto criteria = new HubSpuPendingPicCriteriaDto();
            criteria.createCriteria().andSupplierIdEqualTo(supplierId).andSupplierSpuNoEqualTo(supplierSpuNo).andDataStateEqualTo((byte)1);
            List<HubSpuPendingPicDto> picDtoList = picGateWay.selectByCriteria(criteria);
            if(null!=picDtoList&&picDtoList.size()>0){
                picDtoList.forEach(pic ->{
                    Image tmpImg = new Image();
                    tmpImg.setUrl(pic.getPicUrl());
                    images.add(tmpImg);
                } );
            }

        }else{
            List<PictRes> pictResList = gebnegozioDTO.getMedia_gallery_entries();
            if(null!=pictResList&&pictResList.size()>0) {
                pictResList.forEach(pictRes -> {
                    Image cofImg = new Image();
                    cofImg.setUrl(picUrl + pictRes.getFile());
                    images.add(cofImg);
                });
            }
        }
        return images;
    }
    /**
     *  获取token
     */
    public String selToken(){
        // 存储相关的header值
        Map<String,String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");

        // 请求正文内容
        String json = "{\"username\":\"ming.liu@shangpin.com\",\"password\":\"Ex7n4AQ5\"}";

        //返回值是token
        String response = HttpClientUtil.sendHttp(HttpRequestMethedEnum.HttpPost ,POST_URL, null, header, json);
        String token = response.substring( 1, response.length()-1 );
        return token;
    }

    /**
     * 获取产品
     */
    public String selMessage(String token , String url){
        // 存储相关的header值
        Map<String,String> header = new HashMap<String, String>();
        header.put("Content-Type", "application/json");
        header.put("Authorization", "Bearer " + token);

        String response = HttpClientUtil.sendHttp(HttpRequestMethedEnum.HttpGet ,url  ,null, header,null);
        return response;
    }
    /**
     * 查具体属性值：color 、 size 、description 、 gender
     *  {
     *      "attribute_code": "color",
     *      "value": "7650"
     *  }
     */
    public String selProductAttribute(GebnegozioDTO gebnegozioDTO , String token , String attributeCode){
        List<CustomAttributes> customAttributesList = gebnegozioDTO.getCustom_attributes();
        String attributeValue = null;
        String finalAttribute = null;
        //先根据 attribute_code 获取 value 值
        if (null != customAttributesList && !customAttributesList.isEmpty()){
            for (CustomAttributes customAttributes : customAttributesList) {
                if (customAttributes.getAttribute_code().equals(attributeCode)){
                    attributeValue = String.valueOf(customAttributes.getValue());//大类查询结果是字符串，需要测试能否正常拿到
                    break;
                }
            }
            //如果是 color 、size 、description 、 gender ，还要根据value 值去取真正的值；如果是 description ，直接返回 value 值
            finalAttribute = selProductAttributeDetil( attributeCode , token , attributeValue);
        }
        return finalAttribute;
    }

    /**
     *  //如果是 color 、size 、description 、 gender ，还要根据value 值去取真正的颜色；如果是 description ，直接返回 value 值
     */
    public String selProductAttributeDetil(String attributeCode , String token , String attributeValue){
        List<String> getValueAttr = new ArrayList<String>();
        getValueAttr.add("color");
        getValueAttr.add("size");
        getValueAttr.add("designer");
        getValueAttr.add("gender");
        getValueAttr.add("season");

        List<String> returnValueAttr = new ArrayList<String>();
        returnValueAttr.add("description");
        returnValueAttr.add("image");
        returnValueAttr.add("modello");

        String finalAttribute = null;
        String url = ATTRIBUTE_URL + attributeCode;
        if( getValueAttr.contains(attributeCode) ){
            String colorJson = selMessage(token , url);
            if (null != colorJson && !colorJson.equals("")){
                ColorDTO colorDTO = gson.fromJson(colorJson , ColorDTO.class);
                List<ColorOptions> colorOptionsList = colorDTO.getOptions();
                if (null != colorOptionsList && !colorOptionsList.isEmpty()){
                    for (ColorOptions colorOptions : colorOptionsList) {
                        if (colorOptions.getValue().equals(attributeValue)){
                            finalAttribute = colorOptions.getLabel();
                            break;
                        }
                    }
                }
            }
        }else/* if( returnValueAttr.contains(attributeCode) )*/{
            finalAttribute = attributeValue;
        }
        return finalAttribute;
    }
    /**
     * 查产品尺码-----针对可配置产品的 size
     * @return
     */
    public String selConfiSize(String sku , String token) throws UnsupportedEncodingException {
        String size = null;
        List<String> sizeList = null;
        if(null != sku && !sku.equals("") && null != token && !token.equals("")){
            String urlStr = URLEncoder.encode(sku , "UTF-8");
            String urlOri = "http://gebnegozio-qas.extranet.alpenite.com/rest/marketplace_shangpin/V1/products/";
            String url = urlOri + urlStr;

            String sizeJson = selMessage(token , url);
            if (null != sizeJson && !sizeJson.equals("")){
                GebnegozioDetailDTO gebnegozioDetailDTO = gson.fromJson(sizeJson , GebnegozioDetailDTO.class );
                List<Values> sizeValues = gebnegozioDetailDTO.getExtension_attributes().getConfigurable_product_options().getValues();
                for ( Values values : sizeValues ) {
                    String sizeLable = selProductAttributeDetil( "size" , token , values.getValue_index());
                    sizeList.add(sizeLable);
                }
            }
        }

        return sizeList.get(0);
    }
    /**
     * 查库存
     * @return
     */
    public String selStock( String sku , String token ){
        String qty = "";
        if ( null != sku && !sku.equals("") ){
            try {
                String urlStr = URLEncoder.encode( sku , "UTF-8");
                String url = STOCK_URL + urlStr;
                String stockJson = selMessage(token , url);
                if ( null != stockJson && !stockJson.equals("") ){
                    StockDTO stockDTO = gson.fromJson( stockJson , StockDTO.class);
                    qty = stockDTO.getStock_item().getQty();
                    if(null == qty && qty.equals("")){
                        qty = "0";
                    }
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return  qty;
    }

    /**
     *  查大类
     * @param sku
     * @param token
     * @return
     */
    public String selCate( String sku , String token ){
        List<String> cateNames = new ArrayList<String>();
        String categoryNames = null;
        if ( null != sku && !sku.equals("") ) {
            try {
                String urlStr = URLEncoder.encode(sku, "UTF-8");
                String url = PRODUCT_DETAIL_URL + urlStr;
                String productDetailJson = selMessage(token, url);
                List<String> cateLists = new ArrayList<String>();

                if (null != productDetailJson && !productDetailJson.equals("")) {
                    Map<String, Object> jsonMap = (Map<String, Object>)JSON.parseObject(productDetailJson, Map.class);
                    for (Map.Entry<String, Object> entryGeb : jsonMap.entrySet()) {
                        if (entryGeb.getKey().equals("custom_attributes")) {
                            List<Map<String, Object>> customLists = (List<Map<String, Object>>) entryGeb.getValue();
                            for (Map<String, Object> customMap : customLists) {
                                if (customMap.get("value") instanceof JSONArray && customMap.get("attribute_code").equals("category_ids")){
                                    JSONArray jsonArray = (JSONArray)customMap.get("value");
                                    String str = JSONObject.toJSONString(jsonArray);
                                    cateLists = JSONObject.parseArray(str,  String.class);
                                    log.info("获取的大类Value：" + cateLists);
                                    System.out.println("test:" + cateLists);
                                    break;
                                }
                            }
                            break;
                        }
                    }
                    for (String listValue : cateLists) {
                        String cateUrl = CATEGORY_URL + listValue;
                        String cateJson = selMessage(token, cateUrl);
                        if (null != cateJson && !cateJson.equals("")) {
                            CategorieDTO categorieDTO = gson.fromJson(cateJson, CategorieDTO.class);
                            String cateName = categorieDTO.getName();//取到大类名称
                            cateNames.add(cateName);
                        }
                    }
                    categoryNames = String.join(",", cateNames);
                }
                    } catch(Exception e){
                        e.printStackTrace();
                    }
                }
                return categoryNames;
            }
}
