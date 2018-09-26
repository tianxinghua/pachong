package com.shangpin.supplier.product.consumer.supplier.gebnegozio;

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
import com.shangpin.supplier.product.consumer.supplier.frmoda.dto.ProductDTO;
import com.shangpin.supplier.product.consumer.supplier.gebnegozio.dto.*;
import com.shangpin.supplier.product.consumer.supplier.gebnegozio.util.HttpClientUtil;
import com.shangpin.supplier.product.consumer.supplier.gebnegozio.util.HttpRequestMethedEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

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
    Gson gson = new Gson();
    @Autowired
    private SupplierProductSaveAndSendToPending supplierProductSaveAndSendToPending;

    @Autowired
    private PictureHandler pictureHandler;

    @Autowired
    private SupplierProductMongoService mongoService;

    ObjectMapper mapper = new ObjectMapper();

    @Override
    public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
        String token = selToken();
        if ( null != token && !token.equals("") ){

        try {
            if (!StringUtils.isBlank(message.getData())){
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
                supplierPicture = pictureHandler.initSupplierPicture( message, hubSpu,
                        converImage( org.apache.commons.lang.StringUtils.isNotBlank(
                                selProductAttribute( gebnegozioDTO , token , "small_image")) ? selProductAttribute( gebnegozioDTO , token , "small_image") : selProductAttribute( gebnegozioDTO , token , "image")
                        )
                );

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
            if( null == seasonName ){
                seasonName = "27S";//gebnegozio供应商的简单产品没有季节，先写死
            }
            //处理Meterial 和 Origin
            String origin = "Italy";
            String meterial = "Cotton100 %";
            String meterial1 = "";
            String meterial2 = "";
            String meterialAndOrigin = selProductAttribute( item , token , "details");
            if( null != meterialAndOrigin && !meterialAndOrigin.equals("")){
                String  str = meterialAndOrigin.replaceAll("\\s*|\r|\n", "").replaceAll("</?[^>]+>", ";");

                if(null !=str && !str.equals("")){
                    List<String> detailLists = Arrays.asList(str.split(";"));
                    for ( String detailList : detailLists ) {
                        if( detailList.startsWith("Made in ") ){
                            int start = detailList.lastIndexOf("in ");
                            origin = detailList.substring(start);
                        }else if( detailList.startsWith("Exterior:") ){
                            int start = detailList.lastIndexOf(":");
                            meterial1 = detailList.substring(start);
                        }else if( detailList.startsWith("Details:") ){
                            int start = detailList.lastIndexOf(":");
                            meterial2 = detailList.substring(start);
                        }
                    }
                    meterial = meterial1 +" "+ meterial2;
                 }
            }

            hubSpu.setSupplierId(supplierId);
            hubSpu.setSupplierSpuNo(item.getId());
            hubSpu.setSupplierSpuColor( selProductAttribute( item , token , "color") );
            hubSpu.setSupplierSpuModel( selProductAttribute( item , token , "modello") );
            hubSpu.setSupplierSpuName(item.getName());
            hubSpu.setSupplierGender( selProductAttribute( item , token , "gender") );
            hubSpu.setSupplierCategoryname( selProductAttribute( item , token , "category_ids") );
            hubSpu.setSupplierBrandname( selProductAttribute( item , token , "designer") );
            hubSpu.setSupplierSeasonname( seasonName );
            hubSpu.setSupplierMaterial(meterial);
            hubSpu.setSupplierOrigin( origin );
            hubSpu.setSupplierSpuDesc( selProductAttribute( item , token , "description") );
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
            String price = selProductAttribute( item , token , "cost");
            String supplierSkuNo = item.getSku();
            hubSku.setSupplierSkuNo(supplierSkuNo);
            hubSku.setSupplierSkuName(item.getName());
            hubSku.setSupplierBarcode(supplierSkuNo);
            hubSku.setMarketPrice( new BigDecimal(StringUtil.verifyPrice(price)) );//市场价
            hubSku.setSalesPrice( new BigDecimal(StringUtil.verifyPrice(price)) );//售价
            hubSku.setSupplyPrice( new BigDecimal(StringUtil.verifyPrice(price)) );//供价
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
     * @param imgUrl
     * @return
     */
    private List<Image> converImage(String imgUrl){
        List<Image> images = new ArrayList<Image>();
        if(org.apache.commons.lang.StringUtils.isNotBlank(imgUrl)){
            String[] imageSpuUrlArray = imgUrl.split("\\|\\|");
            if(null!=imageSpuUrlArray&&imageSpuUrlArray.length>0){

                for(String url : imageSpuUrlArray){
                    Image image = new Image();
                    image.setUrl(url.trim());
                    images.add(image);
                }
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
                    attributeValue = customAttributes.getValue();//大类查询结果是字符串，需要测试能否正常拿到
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
        }else if ( attributeCode.equals("category_ids") ){
            List<String> cateNames = new ArrayList<String>();
            if(null != attributeValue && !attributeValue.isEmpty()){
                List<String> categoryIds = Arrays.asList( attributeValue.split(",") );
                for ( String categoryId : categoryIds ) {
                    String cateUrl = CATEGORY_URL + categoryId;
                    String colorJson = selMessage(token , cateUrl);
                    if (null != colorJson && !colorJson.equals("")){
                        CategorieDTO categorieDTO = gson.fromJson( colorJson, CategorieDTO.class );
                        String cateName = categorieDTO.getName();//取到大类名称
                        cateNames.add( cateName );
                    }
                }
                finalAttribute = cateNames.get(0);//先只取第一个大类名称
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
                List<Values> sizeValues = gebnegozioDetailDTO.getExtension_attributes().getConfigurableProductOptions().getValues();
                for ( Values values : sizeValues ) {
                    String sizeLable = selProductAttributeDetil( "size" , token , values.getValueIndex());
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
        String qty = null;
        if ( null != sku && !sku.equals("") ){
            try {
                String urlStr = URLEncoder.encode( sku , "UTF-8");
                String url = STOCK_URL + urlStr;
                String stockJson = selMessage(token , url);
                if ( null != stockJson && !stockJson.equals("") ){
                    StockDTO stockDTO = gson.fromJson( stockJson , StockDTO.class);
                    qty = stockDTO.getQty();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return  qty;
    }
}
