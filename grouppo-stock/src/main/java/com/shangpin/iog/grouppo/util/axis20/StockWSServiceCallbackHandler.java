/**
 * StockWSServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:17:49 BST)
 */
package com.shangpin.iog.grouppo.util.axis20;


/**
 *  StockWSServiceCallbackHandler Callback class, Users can extend this class and implement
 *  their own receiveResult and receiveError methods.
 */
public abstract class StockWSServiceCallbackHandler {
    protected Object clientData;

    /**
     * User can pass in any object that needs to be accessed once the NonBlocking
     * Web service call is finished and appropriate method of this CallBack is called.
     * @param clientData Object mechanism by which the user can pass in user data
     * that will be avilable at the time this callback is called.
     */
    public StockWSServiceCallbackHandler(Object clientData) {
        this.clientData = clientData;
    }

    /**
     * Please use this constructor if you don't want to set any clientData
     */
    public StockWSServiceCallbackHandler() {
        this.clientData = null;
    }

    /**
     * Get the client data
     */
    public Object getClientData() {
        return clientData;
    }

    /**
     * auto generated Axis2 call back method for stock_Query method
     * override this method for handling normal response from stock_Query operation
     */
    public void receiveResultstock_Query(
        com.shangpin.iog.grouppo.util.axis20.StockWSServiceStub.Stock_QueryResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from stock_Query operation
     */
    public void receiveErrorstock_Query(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for stock_TabularQuery method
     * override this method for handling normal response from stock_TabularQuery operation
     */
    public void receiveResultstock_TabularQuery(
        com.shangpin.iog.grouppo.util.axis20.StockWSServiceStub.Stock_TabularQueryResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from stock_TabularQuery operation
     */
    public void receiveErrorstock_TabularQuery(java.lang.Exception e) {
    }
}
