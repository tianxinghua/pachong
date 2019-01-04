/**
 * API_STUDIO69CallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:17:49 BST)
 */
package com.shangpin.iog.studio69.util;


/**
 *  API_STUDIO69CallbackHandler Callback class, Users can extend this class and implement
 *  their own receiveResult and receiveError methods.
 */
public abstract class API_STUDIO69CallbackHandler {
    protected Object clientData;

    /**
     * User can pass in any object that needs to be accessed once the NonBlocking
     * Web service call is finished and appropriate method of this CallBack is called.
     * @param clientData Object mechanism by which the user can pass in user data
     * that will be avilable at the time this callback is called.
     */
    public API_STUDIO69CallbackHandler(Object clientData) {
        this.clientData = clientData;
    }

    /**
     * Please use this constructor if you don't want to set any clientData
     */
    public API_STUDIO69CallbackHandler() {
        this.clientData = null;
    }

    /**
     * Get the client data
     */
    public Object getClientData() {
        return clientData;
    }

    /**
     * auto generated Axis2 call back method for getOrderDetails method
     * override this method for handling normal response from getOrderDetails operation
     */
    public void receiveResultgetOrderDetails(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.GetOrderDetailsResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getOrderDetails operation
     */
    public void receiveErrorgetOrderDetails(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for putStatusOrder method
     * override this method for handling normal response from putStatusOrder operation
     */
    public void receiveResultputStatusOrder(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.PutStatusOrderResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from putStatusOrder operation
     */
    public void receiveErrorputStatusOrder(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getGoodsListByCategoryIDByTS method
     * override this method for handling normal response from getGoodsListByCategoryIDByTS operation
     */
    public void receiveResultgetGoodsListByCategoryIDByTS(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.GetGoodsListByCategoryIDByTSResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getGoodsListByCategoryIDByTS operation
     */
    public void receiveErrorgetGoodsListByCategoryIDByTS(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for cancelOrder method
     * override this method for handling normal response from cancelOrder operation
     */
    public void receiveResultcancelOrder(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.CancelOrderResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from cancelOrder operation
     */
    public void receiveErrorcancelOrder(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getGoodsStockByGoodsID method
     * override this method for handling normal response from getGoodsStockByGoodsID operation
     */
    public void receiveResultgetGoodsStockByGoodsID(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.GetGoodsStockByGoodsIDResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getGoodsStockByGoodsID operation
     */
    public void receiveErrorgetGoodsStockByGoodsID(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getSplitOrder method
     * override this method for handling normal response from getSplitOrder operation
     */
    public void receiveResultgetSplitOrder(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.GetSplitOrderResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getSplitOrder operation
     */
    public void receiveErrorgetSplitOrder(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for updateOrderBuyerInfo method
     * override this method for handling normal response from updateOrderBuyerInfo operation
     */
    public void receiveResultupdateOrderBuyerInfo(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.UpdateOrderBuyerInfoResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from updateOrderBuyerInfo operation
     */
    public void receiveErrorupdateOrderBuyerInfo(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getGoodsDetailListByTs method
     * override this method for handling normal response from getGoodsDetailListByTs operation
     */
    public void receiveResultgetGoodsDetailListByTs(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.GetGoodsDetailListByTsResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getGoodsDetailListByTs operation
     */
    public void receiveErrorgetGoodsDetailListByTs(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for checkWebService method
     * override this method for handling normal response from checkWebService operation
     */
    public void receiveResultcheckWebService(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.CheckWebServiceResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from checkWebService operation
     */
    public void receiveErrorcheckWebService(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for putOrderStatus method
     * override this method for handling normal response from putOrderStatus operation
     */
    public void receiveResultputOrderStatus(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.PutOrderStatusResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from putOrderStatus operation
     */
    public void receiveErrorputOrderStatus(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for setProduct method
     * override this method for handling normal response from setProduct operation
     */
    public void receiveResultsetProduct(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.SetProductResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from setProduct operation
     */
    public void receiveErrorsetProduct(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for setStock method
     * override this method for handling normal response from setStock operation
     */
    public void receiveResultsetStock(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.SetStockResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from setStock operation
     */
    public void receiveErrorsetStock(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getGoodsListByPage method
     * override this method for handling normal response from getGoodsListByPage operation
     */
    public void receiveResultgetGoodsListByPage(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.GetGoodsListByPageResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getGoodsListByPage operation
     */
    public void receiveErrorgetGoodsListByPage(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getSupplierList method
     * override this method for handling normal response from getSupplierList operation
     */
    public void receiveResultgetSupplierList(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.GetSupplierListResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getSupplierList operation
     */
    public void receiveErrorgetSupplierList(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getGoodsCategory method
     * override this method for handling normal response from getGoodsCategory operation
     */
    public void receiveResultgetGoodsCategory(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.GetGoodsCategoryResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getGoodsCategory operation
     */
    public void receiveErrorgetGoodsCategory(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getGoodsListByCategoryID method
     * override this method for handling normal response from getGoodsListByCategoryID operation
     */
    public void receiveResultgetGoodsListByCategoryID(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.GetGoodsListByCategoryIDResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getGoodsListByCategoryID operation
     */
    public void receiveErrorgetGoodsListByCategoryID(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getGoodsList method
     * override this method for handling normal response from getGoodsList operation
     */
    public void receiveResultgetGoodsList(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.GetGoodsListResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getGoodsList operation
     */
    public void receiveErrorgetGoodsList(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getGoodsDetailByGoodsID method
     * override this method for handling normal response from getGoodsDetailByGoodsID operation
     */
    public void receiveResultgetGoodsDetailByGoodsID(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.GetGoodsDetailByGoodsIDResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getGoodsDetailByGoodsID operation
     */
    public void receiveErrorgetGoodsDetailByGoodsID(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for createBookingOrder method
     * override this method for handling normal response from createBookingOrder operation
     */
    public void receiveResultcreateBookingOrder(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.CreateBookingOrderResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from createBookingOrder operation
     */
    public void receiveErrorcreateBookingOrder(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getOrderStatusByOrderIDList method
     * override this method for handling normal response from getOrderStatusByOrderIDList operation
     */
    public void receiveResultgetOrderStatusByOrderIDList(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.GetOrderStatusByOrderIDListResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getOrderStatusByOrderIDList operation
     */
    public void receiveErrorgetOrderStatusByOrderIDList(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for shippingOrder method
     * override this method for handling normal response from shippingOrder operation
     */
    public void receiveResultshippingOrder(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.ShippingOrderResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from shippingOrder operation
     */
    public void receiveErrorshippingOrder(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getGoodsType method
     * override this method for handling normal response from getGoodsType operation
     */
    public void receiveResultgetGoodsType(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.GetGoodsTypeResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getGoodsType operation
     */
    public void receiveErrorgetGoodsType(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getGoodsDetailListByPage method
     * override this method for handling normal response from getGoodsDetailListByPage operation
     */
    public void receiveResultgetGoodsDetailListByPage(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.GetGoodsDetailListByPageResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getGoodsDetailListByPage operation
     */
    public void receiveErrorgetGoodsDetailListByPage(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for updateOrderStatus method
     * override this method for handling normal response from updateOrderStatus operation
     */
    public void receiveResultupdateOrderStatus(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.UpdateOrderStatusResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from updateOrderStatus operation
     */
    public void receiveErrorupdateOrderStatus(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getSupplierBankInfo method
     * override this method for handling normal response from getSupplierBankInfo operation
     */
    public void receiveResultgetSupplierBankInfo(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.GetSupplierBankInfoResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getSupplierBankInfo operation
     */
    public void receiveErrorgetSupplierBankInfo(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getOrderShippingInfo method
     * override this method for handling normal response from getOrderShippingInfo operation
     */
    public void receiveResultgetOrderShippingInfo(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.GetOrderShippingInfoResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getOrderShippingInfo operation
     */
    public void receiveErrorgetOrderShippingInfo(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getGoodsDetailListByCategoryID method
     * override this method for handling normal response from getGoodsDetailListByCategoryID operation
     */
    public void receiveResultgetGoodsDetailListByCategoryID(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.GetGoodsDetailListByCategoryIDResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getGoodsDetailListByCategoryID operation
     */
    public void receiveErrorgetGoodsDetailListByCategoryID(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for dictBrand method
     * override this method for handling normal response from dictBrand operation
     */
    public void receiveResultdictBrand(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.DictBrandResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from dictBrand operation
     */
    public void receiveErrordictBrand(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getOrderStatusByOrderID method
     * override this method for handling normal response from getOrderStatusByOrderID operation
     */
    public void receiveResultgetOrderStatusByOrderID(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.GetOrderStatusByOrderIDResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getOrderStatusByOrderID operation
     */
    public void receiveErrorgetOrderStatusByOrderID(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for dictOrderStatus method
     * override this method for handling normal response from dictOrderStatus operation
     */
    public void receiveResultdictOrderStatus(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.DictOrderStatusResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from dictOrderStatus operation
     */
    public void receiveErrordictOrderStatus(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getGoodsStockListByCategoryID method
     * override this method for handling normal response from getGoodsStockListByCategoryID operation
     */
    public void receiveResultgetGoodsStockListByCategoryID(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.GetGoodsStockListByCategoryIDResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getGoodsStockListByCategoryID operation
     */
    public void receiveErrorgetGoodsStockListByCategoryID(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getGoodsListTS method
     * override this method for handling normal response from getGoodsListTS operation
     */
    public void receiveResultgetGoodsListTS(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.GetGoodsListTSResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getGoodsListTS operation
     */
    public void receiveErrorgetGoodsListTS(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for putOrder method
     * override this method for handling normal response from putOrder operation
     */
    public void receiveResultputOrder(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.PutOrderResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from putOrder operation
     */
    public void receiveErrorputOrder(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getConfirmedGoodsByOrderID method
     * override this method for handling normal response from getConfirmedGoodsByOrderID operation
     */
    public void receiveResultgetConfirmedGoodsByOrderID(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.GetConfirmedGoodsByOrderIDResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getConfirmedGoodsByOrderID operation
     */
    public void receiveErrorgetConfirmedGoodsByOrderID(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getGoodsDetailListByCategoryIDByTs method
     * override this method for handling normal response from getGoodsDetailListByCategoryIDByTs operation
     */
    public void receiveResultgetGoodsDetailListByCategoryIDByTs(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.GetGoodsDetailListByCategoryIDByTsResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getGoodsDetailListByCategoryIDByTs operation
     */
    public void receiveErrorgetGoodsDetailListByCategoryIDByTs(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getGoodsCatg method
     * override this method for handling normal response from getGoodsCatg operation
     */
    public void receiveResultgetGoodsCatg(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.GetGoodsCatgResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getGoodsCatg operation
     */
    public void receiveErrorgetGoodsCatg(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getGoodsListByCategoryIDByPage method
     * override this method for handling normal response from getGoodsListByCategoryIDByPage operation
     */
    public void receiveResultgetGoodsListByCategoryIDByPage(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.GetGoodsListByCategoryIDByPageResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getGoodsListByCategoryIDByPage operation
     */
    public void receiveErrorgetGoodsListByCategoryIDByPage(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for dictSeason method
     * override this method for handling normal response from dictSeason operation
     */
    public void receiveResultdictSeason(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.DictSeasonResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from dictSeason operation
     */
    public void receiveErrordictSeason(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getGoodsDetailListByCategoryIDByPage method
     * override this method for handling normal response from getGoodsDetailListByCategoryIDByPage operation
     */
    public void receiveResultgetGoodsDetailListByCategoryIDByPage(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.GetGoodsDetailListByCategoryIDByPageResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getGoodsDetailListByCategoryIDByPage operation
     */
    public void receiveErrorgetGoodsDetailListByCategoryIDByPage(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getGoodsDetailList method
     * override this method for handling normal response from getGoodsDetailList operation
     */
    public void receiveResultgetGoodsDetailList(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.GetGoodsDetailListResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getGoodsDetailList operation
     */
    public void receiveErrorgetGoodsDetailList(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getGoodsStockList method
     * override this method for handling normal response from getGoodsStockList operation
     */
    public void receiveResultgetGoodsStockList(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.GetGoodsStockListResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getGoodsStockList operation
     */
    public void receiveErrorgetGoodsStockList(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for createNewOrder method
     * override this method for handling normal response from createNewOrder operation
     */
    public void receiveResultcreateNewOrder(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.CreateNewOrderResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from createNewOrder operation
     */
    public void receiveErrorcreateNewOrder(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getOrderHeaders method
     * override this method for handling normal response from getOrderHeaders operation
     */
    public void receiveResultgetOrderHeaders(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.GetOrderHeadersResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getOrderHeaders operation
     */
    public void receiveErrorgetOrderHeaders(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for getOrderAttachedFile method
     * override this method for handling normal response from getOrderAttachedFile operation
     */
    public void receiveResultgetOrderAttachedFile(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.GetOrderAttachedFileResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from getOrderAttachedFile operation
     */
    public void receiveErrorgetOrderAttachedFile(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for dictColors method
     * override this method for handling normal response from dictColors operation
     */
    public void receiveResultdictColors(
        com.shangpin.iog.studio69.util.API_STUDIO69Stub.DictColorsResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from dictColors operation
     */
    public void receiveErrordictColors(java.lang.Exception e) {
    }
}
