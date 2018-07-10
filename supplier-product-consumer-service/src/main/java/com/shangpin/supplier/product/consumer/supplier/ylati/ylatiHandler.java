package com.shangpin.supplier.product.consumer.supplier.ylati;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.message.original.body.SupplierProduct;
import com.shangpin.ephub.client.message.picture.body.SupplierPicture;
import com.shangpin.ephub.client.message.picture.image.Image;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.supplier.product.consumer.exception.EpHubSupplierProductConsumerRuntimeException;
import com.shangpin.supplier.product.consumer.service.SupplierProductMongoService;
import com.shangpin.supplier.product.consumer.service.SupplierProductSaveAndSendToPending;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;
import com.shangpin.supplier.product.consumer.supplier.common.picture.PictureHandler;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;
import com.shangpin.supplier.product.consumer.supplier.ylati.dto.Product;
import com.shangpin.supplier.product.consumer.supplier.ylati.dto.SkuPro;
import com.shangpin.supplier.product.consumer.supplier.ylati.dto.products;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("ylatiHandler")
@Slf4j
public class ylatiHandler  implements ISupplierHandler {
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
                products pros = JsonUtil.deserialize(message.getData(), products.class);
                String supplierId = message.getSupplierId();
                List<com.shangpin.supplier.product.consumer.supplier.ylati.dto.Product> product = pros.getProduct();
                for (com.shangpin.supplier.product.consumer.supplier.ylati.dto.Product product1 : product) {

                    //保存原始数据
                    mongoService.save(supplierId, product1.getId(), product1);
                    HubSupplierSpuDto hubSpu = new HubSupplierSpuDto();
                   //转换SPU
                    boolean success = convertSpu(supplierId,product1,hubSpu);
                    if(success){
                        List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
                        List<SkuPro> variants = product1.getVariants();
                        for (SkuPro skuPro : variants) {
                            HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
                           //转换sku
                            boolean succSku =convertSku(supplierId,skuPro,hubSku);
                            if(succSku){
                                hubSkus.add(hubSku);
                            }
                        }

                        List<com.shangpin.supplier.product.consumer.supplier.ylati.dto.Image> images = product1.getImages();
                        for (com.shangpin.supplier.product.consumer.supplier.ylati.dto.Image image : images) {
                            if ( images.size()>0){
                                ArrayList<String> imgs = new ArrayList<>();
                                String imageSrc = image.getSrc();
                                imgs.add(imageSrc);
                                List<Image> im = converImage(imgs);
                                SupplierPicture supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu, im);
                                supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(),supplierId, message.getSupplierName(), hubSpu, hubSkus,supplierPicture);

                            }
                        }
                    }

                    }
                }

        } catch (Exception e) {
            log.error("ylati异常："+e.getMessage(),e);
        }

    }

    /**
     *
     * @param supplierId
     * @param proDto
     * @param hubSpu
     * @return
     * @throws EpHubSupplierProductConsumerRuntimeException
     */

    public boolean convertSpu(String supplierId,Product proDto, HubSupplierSpuDto hubSpu) throws EpHubSupplierProductConsumerRuntimeException {
        if(null == proDto){
            return false;
        }else{
            hubSpu.setSupplierId(supplierId);
            hubSpu.setSupplierSpuNo(proDto.getId());
            String tags = proDto.getTags();
            String[] tg = tags.split(",");
            for (String s : tg) {
                if (s.contains("SPC")){
                    String[] split1 = s.split("-");
                    hubSpu.setSupplierSpuColor(split1[1]);
                }
                if (s.contains("SPG")){
                    String[] split1 = s.split("-");
                    hubSpu.setSupplierGender(split1[1]);
                }
                if (s.contains("SPM")){
                    String[] split1 = s.split("-");
                    hubSpu.setSupplierMaterial(split1[1]);
                }
                if (s.contains("SPO")){
                    String[] split1 = s.split("-");
                    hubSpu.setSupplierOrigin(split1[1]);
                }
                if (s.contains("SPS")){
                    String[] split1 = s.split("-");
                    hubSpu.setSupplierSeasonname(split1[1]);
                }

                //不确定。。。.
                if (s.contains("SPP")){
                    String[] split1 = s.split("-");
                    hubSpu.setSupplierSpuModel(split1[1]);
                }
            }


            hubSpu.setSupplierSpuName(proDto.getVendor());
            hubSpu.setSupplierCategoryname(proDto.getProduct_type());
           //.....
            hubSpu.setSupplierBrandname("");
            hubSpu.setSupplierSpuDesc(proDto.getBody_html());
            return true;
        }
    }
    /**
     *
     * @param supplierId
     * @param
     * @param hubSku
     * @return
     */
    public boolean convertSku(String supplierId,SkuPro sk, HubSupplierSkuDto hubSku){
        hubSku.setSupplierId(supplierId);
        hubSku.setSupplierSkuNo(sk.getId());
        hubSku.setStock(Integer.parseInt(sk.getInventory_quantity()));
        hubSku.setSupplierSkuSize(sk.getTitle());

       //不确定
        if (sk.getCompare_at_price()!=null){

            hubSku.setMarketPrice(new BigDecimal(sk.getCompare_at_price()));

        }
        //不确定
        if (sk.getPrice()!=null){

            hubSku.setSupplyPrice(new BigDecimal(sk.getPrice()));

        }
        //不确定
        //hubSku.setSupplierBarcode();

        return true;
    }

    private List<Image> converImage(List<String> pictures){
        List<Image> images = new ArrayList<Image>();
        if(CollectionUtils.isNotEmpty(pictures)){
            for(String url : pictures){
                if(url!=null&&url.contains("jpg")){
                    Image image = new Image();
                    image.setUrl(url);
                    images.add(image);
                }
            }
        }
        return images;
    }




}
