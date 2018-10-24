package com.shangpin.supplier.product.consumer.supplier.eraldo;

import com.google.gson.Gson;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.message.original.body.SupplierProduct;
import com.shangpin.ephub.client.message.picture.body.SupplierPicture;
import com.shangpin.ephub.client.message.picture.image.Image;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.supplier.product.consumer.service.SupplierProductMongoService;
import com.shangpin.supplier.product.consumer.service.SupplierProductSaveAndSendToPending;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;
import com.shangpin.supplier.product.consumer.supplier.common.picture.PictureHandler;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;
import com.shangpin.supplier.product.consumer.supplier.eraldo.dto.ItemInfo;
import com.shangpin.supplier.product.consumer.supplier.eraldo.dto.Items;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("eraldoHandler")
@Slf4j
public class eraldoHandler  implements ISupplierHandler {

    @Autowired
    private SupplierProductSaveAndSendToPending supplierProductSaveAndSendToPending;
    @Autowired
    private PictureHandler pictureHandler;
    @Autowired
    private SupplierProductMongoService mongoService;

    @Override
    public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers) {
        try {
            if(!StringUtils.isEmpty(message.getData())){
                 Gson gson =  new Gson();
                ItemInfo item = gson.fromJson(message.getData(), ItemInfo.class);
                        //  ItemInfo item = JsonUtil.deserialize(message.getData(), ItemInfo.class);
                String supplierId = message.getSupplierId();
                mongoService.save(supplierId, item.getProduct_sku(), item);

                HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
                boolean succ = convertSpu(supplierId,hubSpu,item);
                List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
                boolean success = convertSku(supplierId,hubSkus,item);
                if (success == false){
                    log.error("sku异常");
                    return;
                }
                SupplierPicture supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu, converImage(item));
                if(succ){
                    supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(),supplierId, message.getSupplierName(), hubSpu, hubSkus,supplierPicture);
                }
            }
        } catch (Exception e) {
            log.error("eraldo："+e.getMessage(),e);
        }


    }
    /**
     *
     * @param supplierId
     * @param hubSpu
     * @param item
     * @return
     */
    private boolean convertSpu(String supplierId,HubSupplierSpuDto hubSpu,ItemInfo item){
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-");
        if(null != item){
            //if("false".equals(item.getVariationInfo().getIsParent())){
            hubSpu.setSupplierId(supplierId);
            hubSpu.setSupplierSpuNo(item.getProduct_sku());
            hubSpu.setSupplierSpuModel(item.getProduct_design_code());
            hubSpu.setSupplierSpuName(item.getProduct_name());
            hubSpu.setSupplierSpuColor(item.getProduct_color());
            hubSpu.setSupplierGender(item.getProduct_gender());
            hubSpu.setSupplierCategoryno(item.getProduct_category_id());
            hubSpu.setSupplierCategoryname(item.getProduct_category());
            hubSpu.setSupplierBrandname(item.getProduct_brand());
            hubSpu.setSupplierBrandno(item.getProduct_brand_id());

            hubSpu.setSupplierSeasonname(item.getProduct_season());
            hubSpu.setSupplierMaterial(item.getProduct_material());
            hubSpu.setSupplierOrigin(item.getProduct_made_in());
            hubSpu.setSupplierSpuDesc(item.getProduct_description());


            return true;
        }

        return false;
    }
    /**
     *
     * @param supplierId
     * @param hubSkus
     * @param item
     * @return
     */
    private boolean convertSku(String supplierId,List<HubSupplierSkuDto> hubSkus, ItemInfo item){
        if(null != item){
            //if("false".equals(item.getVariationInfo().getIsParent())){
            List<Items> variants = item.getItems();
            if (null != variants && variants.size() > 0){
                variants.stream().forEach(t->{
                    HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
                    hubSku.setSupplierId(supplierId);
                    hubSku.setSupplierSkuNo(t.getItem_id());
                    hubSku.setSupplierSkuName(t.getItem_sku());
                   // hubSku.setSupplierBarcode(t.getCode());
                    hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(item.getProduct_market_price())));
                    hubSku.setSupplyPrice(new BigDecimal(StringUtil.verifyPrice(item.getProduct_purchase_price())));
                    hubSku.setSupplierSkuSize(t.getItem_size_value());
                    hubSku.setStock(StringUtil.verifyStock(t.getItem_stock()));
                    hubSkus.add(hubSku);
                });
                return true;
            }
        }
        //}
        return false;
    }

    private List<Image> converImage(ItemInfo item){
        List<Image> images = new ArrayList<Image>();
        List< String> imagesList = item.getProduct_picture();
        if(null != item && null != imagesList && CollectionUtils.isNotEmpty(imagesList)){
                for (String str : imagesList) {
                    Image image = new Image();
                    image.setUrl(str);
                    images.add(image);
                }
        }
        return images;
    }
}
