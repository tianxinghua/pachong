package com.shangpin.ephub.price.consumer.processor;


import com.shangpin.ephub.price.consumer.conf.stream.source.message.ProductPriceDTO;
import com.shangpin.ephub.price.consumer.service.SupplierPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by lizhongren on 2017/3/24.
 * 价格处理
 */
@Component
@Slf4j
public class SupplierProductPriceProcessor {

    @Autowired
    SupplierPriceService supplierPriceService;

    public void processProductPrice(ProductPriceDTO message, Map<String, Object> headers) throws Exception {

        supplierPriceService.sendPriceMessageToScm(message,headers);

    }

    public void processRetryPrice(ProductPriceDTO message, Map<String, Object> headers) throws Exception {

        supplierPriceService.sendPriceMessageToScm(message,headers);

    }
}
