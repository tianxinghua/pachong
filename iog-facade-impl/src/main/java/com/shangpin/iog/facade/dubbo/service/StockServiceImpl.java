package com.shangpin.iog.facade.dubbo.service;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by loyalty on 15/7/7.
 */
@Path("stock")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
@Service("stockFacadeServiceImpl")
public class StockServiceImpl implements StockService{


    static Map<String,String> supplieryMap = new HashMap<String,String>(){
        { put("","");

        }
    };


    @Override
    @GET
    @Path("{supplierId}/{skuId}/{supplierSkuId}")
    public int getStockForProduct(@PathParam("supplierId") String supplierId,
                                  @PathParam("skuId")  String skuId,
                                  @PathParam("supplierSkuId") String supplierSkuId) {


        return 0;
    }
}
