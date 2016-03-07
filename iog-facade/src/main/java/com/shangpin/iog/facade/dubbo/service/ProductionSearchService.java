package com.shangpin.iog.facade.dubbo.service;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import com.shangpin.iog.facade.dubbo.dto.ProductDTO;
import com.shangpin.iog.facade.dubbo.dto.ProductUpdateDTO;
import com.shangpin.iog.facade.dubbo.dto.ServiceException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by loyalty on
 * 产品查询展示业务
 *
 */
@Path("product")
public interface ProductionSearchService {

    @GET
    @Path("{supplierId}/{skuId}")
    @Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
    public ProductDTO findProductForOrder(@PathParam("supplierId") String supplierId,@PathParam("skuId")String skuId) ;

}
