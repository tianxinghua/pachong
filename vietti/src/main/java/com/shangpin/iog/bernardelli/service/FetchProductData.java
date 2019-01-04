package com.shangpin.iog.bernardelli.service;

import com.google.gson.Gson;
import com.shangpin.iog.bernardelli.dto.Product;
import com.shangpin.iog.bernardelli.dto.ResponseDTO;
import com.shangpin.iog.bernardelli.dto.org.apache.commons.httpclient.auth.CsvDTO;
import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;

import com.shangpin.product.AbsSaveProduct;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;
import com.shangpin.iog.bernardelli.service.OAuth;

import java.util.*;

@Component("gg")
public class FetchProductData extends AbsSaveProduct {



    @Override
    public Map<String, Object> fetchProductAndSave() {
        List<Product> catalog_datas =null;

        try {
        String  data = OAuth.getData ("GET","https://www.viettishop.com/api/rest/marketplaces/catalog_feed/json","de924d99f10d11f0f881ee77cbdf254e","85f2923d3f8fdc376cda2633f10ee204","6637e3905e90dedb3cc177f930eba2dc","1.0","HMAC-SHA1");
        Gson gson = new Gson();
        ResponseDTO responseDTO = gson.fromJson(data, ResponseDTO.class);
        catalog_datas = responseDTO.getCatalog_data();
        int size = catalog_datas.size();
        for (int i = 0; i <size ; i++) {
            Product    product = catalog_datas.get(i);
            String json = gson.toJson(product);
            supp.setData(json);
            pushMessage(null);

        }
             }catch (Exception exception){
            exception.printStackTrace();
        }
        if(flag=false){
            return getProductMap(catalog_datas);
        }
        return null;
    }

    public  Map<String,Object> getProductMap(List<Product> csvLists){
        Map<String, Object> returnMap = new HashMap<String, Object>();
        List<SkuDTO> skuList = new ArrayList<SkuDTO>();
        List<SpuDTO> spuList = new ArrayList<SpuDTO>();
        Map<String, List<String>> imageMap = new HashMap<String, List<String>>();
        for (Product product : csvLists) {

            SkuDTO skuDTO = new SkuDTO();
            skuDTO.setId(UUIDGenerator.getUUID());
            skuDTO.setSkuId(product.getSku());
            skuDTO.setSpuId(product.getSku());
            skuDTO.setSupplierId("2017101301985");
            skuDTO.setProductName(product.getName());
            skuDTO.setSalePrice(product.getPrice());
            skuDTO.setMarketPrice(product.getSpecial_price());
            skuDTO.setSupplierPrice(product.getPrice());
            skuDTO.setColor(product.getColor());
            skuDTO.setProductCode(product.getBarcode());
            skuDTO.setSaleCurrency("EURO");
            skuDTO.setProductSize(product.getSize());
            skuDTO.setStock("10");
            skuDTO.setProductDescription(product.getDescription());
            skuDTO.setBarcode(product.getBarcode());
            skuList.add(skuDTO);

            //保存图片
            ProductPictureDTO pictureDTO = new ProductPictureDTO();
            pictureDTO.setSupplierId("2017101301985");
            pictureDTO.setSkuId(product.getSku());
         /*   String[] picUrl = new String[]{product.getImage_link()};
            imageMap.put(skuDTO.getSkuId() + ";" + skuDTO.getProductCode(), Arrays.asList(picUrl));*/

            //保存spu
            SpuDTO spu = new SpuDTO();
            spu.setId(UUIDGenerator.getUUID());
          //  spu.setSpuId(product.getId());
            spu.setSupplierId("2017101301985");
            spu.setBrandName(product.getName());
            spu.setCategoryName(product.getName());
          //  spu.setCategoryGender(product.g);
            spu.setMaterial(product.getModel());
        }
        returnMap.put("sku", skuList);
        returnMap.put("spu", spuList);
        returnMap.put("image", imageMap);
        return returnMap;
    }


}
