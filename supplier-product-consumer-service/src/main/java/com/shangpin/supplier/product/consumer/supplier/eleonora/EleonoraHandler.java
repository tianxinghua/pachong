package com.shangpin.supplier.product.consumer.supplier.eleonora;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.message.original.body.SupplierProduct;
import com.shangpin.ephub.client.message.picture.body.SupplierPicture;
import com.shangpin.ephub.client.message.picture.image.Image;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.supplier.product.consumer.exception.EpHubSupplierProductConsumerException;
import com.shangpin.supplier.product.consumer.service.SupplierProductSaveAndSendToPending;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;
import com.shangpin.supplier.product.consumer.supplier.common.picture.PictureHandler;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;
import com.shangpin.supplier.product.consumer.supplier.eleonora.dto.Item;
import com.shangpin.supplier.product.consumer.supplier.eleonora.dto.Items;
import com.shangpin.supplier.product.consumer.supplier.eleonora.dto.Product;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by lizhongren on 2017/1/22.
 */
@Component("eleonoraHandler")
@Slf4j
public class EleonoraHandler implements ISupplierHandler {

    @Autowired
    private SupplierProductSaveAndSendToPending supplierProductSaveAndSendToPending;
    @Autowired
    private PictureHandler pictureHandler;

    @Override
    public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
        try {
            if(!StringUtils.isEmpty(message.getData())){
            	Product product = JsonUtil.deserialize(message.getData(), Product.class);
                if(null!=product){
                        HubSupplierSpuDto supplierSpuDto = new HubSupplierSpuDto();
                        boolean success = false;
                        List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
                        Items itemObj = product.getItems();
                        if(null!=itemObj) {
                            List<Item> itemList = itemObj.getItems();
                            if(null!=itemList&&itemList.size()>0){
                                //处理SPU   颜色需要特殊处理
                                String color = itemList.get(0).getColor();
                                supplierSpuDto.setSupplierSpuColor(color);
                                success = convertSpu(message.getSupplierId(),product,supplierSpuDto);
                                //处理图片
                                String pic = itemList.get(0).getPicture();
                                for(Item item:itemList){
                                    HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
                                    boolean skuSuc = convertSku(message.getSupplierId(),supplierSpuDto.getSupplierSpuId(),item,hubSku);
                                    if(skuSuc){
                                        hubSkus.add(hubSku);
                                    }
                                }

                                if(success){
                                    SupplierPicture supplierPicture = null;
                                    if(StringUtils.isNotBlank(pic)){
                                        String[] picArray = pic.split("\\|");
                                        List<String> picUrlList = Arrays.asList(picArray);
                                        supplierPicture = pictureHandler.initSupplierPicture(message, supplierSpuDto, converImage(picUrlList));
                                    }
                                    supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(),message.getSupplierId(), message.getSupplierName(), supplierSpuDto, hubSkus,supplierPicture);
                                }
                            }else{
                                log.error("异常： spu : " + product.getProducer_id() +"没有sku");
                            }


                    }
                }
            }
        } catch (EpHubSupplierProductConsumerException e) {
            log.error("异常："+e.getMessage(),e);
        }

    }

    private boolean convertSku(String supplierId, Long supplierSpuId, Item item, HubSupplierSkuDto hubSku) {
        if(null != item){

            hubSku.setSupplierSpuId(supplierSpuId);
            hubSku.setSupplierId(supplierId);
            String size =  item.getItem_size();

            String supplierSkuNo = item.getItem_id();
            if (supplierSkuNo.indexOf("½") > 0) {
                supplierSkuNo = supplierSkuNo.replace("½", "+");
            }
            hubSku.setSupplierSkuNo(supplierSkuNo);
            hubSku.setSupplierSkuName("");
            if(item.getMarket_price() != null) {
                hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(item.getMarket_price().replaceAll(",", "."))));
            }
            if (item.getSupply_price() != null) {
                hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(item.getSupply_price().replaceAll(",", "."))));
            }
            hubSku.setMarketPriceCurrencyorg("");
            hubSku.setSupplyPriceCurrency("");
            hubSku.setSupplierSkuSize(size);
            hubSku.setStock(StringUtil.verifyStock((item.getStock())));
            return true;
        }else{
            return false;
        }
    }

    private boolean convertSpu(String supplierId, Product item, HubSupplierSpuDto hubSpu) {
        if(null != item){


            hubSpu.setSupplierId(supplierId);
            hubSpu.setSupplierSpuNo(item.getProductId());
            hubSpu.setSupplierSpuModel(item.getProducer_id());
            hubSpu.setSupplierSpuName(item.getProduct_name());

            hubSpu.setSupplierGender(item.getGender());
            hubSpu.setSupplierCategoryname(item.getCategory());
            hubSpu.setSupplierBrandname(item.getProduct_brand());
            hubSpu.setSupplierSeasonname(item.getSeason_code());

            hubSpu.setSupplierMaterial(item.getProduct_material());

            hubSpu.setSupplierOrigin(item.getProduct_MADEin());
            hubSpu.setSupplierSpuDesc(item.getDescription());
            return true;
        }else{
            return false;
        }
    }


    private List<Image> converImage(List<String> urlList){
        List<Image> images = new ArrayList<Image>();
        if(null != urlList){
            for(String url : urlList){
                Image image = new Image();
                image.setUrl(url);
                images.add(image);
            }
        }
        return images;
    }
}
