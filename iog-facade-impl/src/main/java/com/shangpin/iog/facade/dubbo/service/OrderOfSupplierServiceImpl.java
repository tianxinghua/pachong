package com.shangpin.iog.facade.dubbo.service;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import org.springframework.stereotype.Service;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by lizhongren on 2015/11/9.
 */
@Path("order")
@Consumes({MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces({ContentType.APPLICATION_JSON_UTF_8, ContentType.TEXT_XML_UTF_8})
@Service("orderOfSupplierFacadeServiceImpl")
public class OrderOfSupplierServiceImpl implements OrderOfSupplierService{
    @Override

    @GET
    @Path("{supplierNo}")
    public String createOrder(@PathParam("supplierNo") String supplierNo,String orderMessage) {

        return null;
    }
}
