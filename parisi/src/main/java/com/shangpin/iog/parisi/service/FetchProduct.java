package com.shangpin.iog.parisi.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.parisi.dto.Product;
import com.shangpin.iog.parisi.dto.Products;
import com.shangpin.product.AbsSaveProduct;
import org.apache.log4j.Logger;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by lizhongren on 2017/2/22.
 */
@Component("parisi")
public class FetchProduct extends AbsSaveProduct {
    private static Logger logger = Logger.getLogger("info");
    private static Logger loggerError = Logger.getLogger("error");

    private static ResourceBundle bdl = null;
    private static String supplierId;


    static {
        if (null == bdl)
            bdl = ResourceBundle.getBundle("conf");
        supplierId = bdl.getString("supplierId");

    }
    @Autowired
    ProductFetchUtil  productFetchUtil;

    ObjectMapper objectMapper  = new ObjectMapper();

    @Override
    public Map<String, Object> fetchProductAndSave() {
        Map<String, Object> returnMap = new HashMap<String, Object>();
        List<SkuDTO> skuList = new ArrayList<SkuDTO>();
        List<SpuDTO> spuList = new ArrayList<SpuDTO>();
        Map<String,List<String>> imageMap = new HashMap<String, List<String>>();
        String productMsg = productFetchUtil.getProduct();
        try {
            Products productObj  =productFetchUtil.getList(productMsg);
            List<Product> products = productObj.getProducts();
            for(Product product:products){
                supp.setData(objectMapper.writeValueAsString(product));
                pushMessage(objectMapper.writeValueAsString(supp));

                SpuDTO spu = new SpuDTO();
                SkuDTO sku = new SkuDTO();

                sku.setId(UUIDGenerator.getUUID());
                sku.setSupplierId(supplierId);
                sku.setSkuId(product.getSku());
                sku.setSpuId(product.getProduct());
                sku.setProductName(product.getName());
                sku.setMarketPrice(product.getPrice());
                sku.setSalePrice("");
                sku.setSupplierPrice(product.getSupplierPrice());
                sku.setBarcode(product.getBarcode());
                sku.setProductCode(product.getProductCode());
                sku.setColor(product.getColor());
                sku.setProductDescription(product.getSpecification());
                sku.setSaleCurrency(product.getCurrency());
                sku.setProductSize(product.getSize());
                sku.setStock(product.getStock());
                skuList.add(sku);
//					System.out.println(sku.getSkuId());
                spu.setId(UUIDGenerator.getUUID());
                spu.setSupplierId(supplierId);
                spu.setSpuId(sku.getSpuId());
                spu.setCategoryGender(product.getGender());
                spu.setCategoryName(product.getCategory());
                spu.setBrandName(product.getBrand());
                spu.setSeasonId(product.getIdSeason());
                spu.setSeasonName(product.getSeason());
                spu.setMaterial(product.getComposition());
                spu.setProductOrigin(product.getCountry());
                spuList.add(spu);
//
                List<String> list1 = new ArrayList<String>();
                String imgUrl =product.getImages();
                String[] imageUrlArray = imgUrl.split("\\|\\|");
                if(null!=imageUrlArray&&imageUrlArray.length>0){
                    List<String> picUrlList = Arrays.asList(imageUrlArray);
                    for(String url :picUrlList){
                        if(url.startsWith("http")){
                            list1.add(url);
                        }
                    }
                }


                imageMap.put(spu.getSpuId()+";"+sku.getProductCode(), list1);



            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        returnMap.put("sku", skuList);
        returnMap.put("spu", spuList);
        returnMap.put("image", imageMap);
        return returnMap;
    }
}
