package com.shangpin.iog.facade.dubbo.service;

import com.alibaba.dubbo.rpc.protocol.rest.support.ContentType;
import org.springframework.stereotype.Service;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
    public String createOrder(String orderMessage) {
        return null;
    }
}
