package com.shangpin.ephub.product.business.ui.purchase.service.impl;


import com.shangpin.ephub.client.data.mysql.product.dto.PurchaseProductDto;
import com.shangpin.ephub.client.data.mysql.product.dto.PurchaseProductRecordDto;
import com.shangpin.ephub.client.data.mysql.product.gateway.PurchaseProductGateWay;
import com.shangpin.ephub.product.business.ui.purchase.service.IProductService;
import com.shangpin.ephub.product.business.ui.purchase.vo.Product;
import com.shangpin.ephub.product.business.ui.purchase.vo.QueryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/6.
 */
@Service
@Slf4j
public class ProductServiceImpl implements IProductService {

    @Autowired
    PurchaseProductGateWay purchaseProductGateWay;
    @Override
    public List<Product> getProductList(QueryDto queryDto){

        try {
        	log.info("airshop查询产品信息参数：{}",queryDto);
            PurchaseProductDto dto = new PurchaseProductDto();
            dto.setSupplierId(queryDto.getSupplierId());
            dto.setSpSkuIds(queryDto.getSkuId());
            List<PurchaseProductRecordDto>  resultMapper = purchaseProductGateWay.getProductWithPurchase(dto);

            List<Product> lists = new ArrayList<Product>();
            if(null != resultMapper && resultMapper.size() > 0){
                for(PurchaseProductRecordDto result : resultMapper){
                    Product item = new Product();
                    item.setSkuId(result.getSkuId());
                    item.setSpuId(result.getSpuId());
                    item.setProductName(result.getProductName());
                    item.setColor(result.getColor());
                    item.setSize(result.getSize());
                    item.setSupplierId(result.getSupplierId());
                    lists.add(item);
                }
            }

            return  lists;
        }
        catch (Exception ex){
            log.error("获取产品时异常："+ex.getMessage(),ex);
        }
        return  null;
    }

}
