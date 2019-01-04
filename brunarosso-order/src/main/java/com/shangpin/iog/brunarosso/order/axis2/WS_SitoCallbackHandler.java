/**
 * WS_SitoCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:17:49 BST)
 */
package com.shangpin.iog.brunarosso.order.axis2;


/**
 *  WS_SitoCallbackHandler Callback class, Users can extend this class and implement
 *  their own receiveResult and receiveError methods.
 */
public abstract class WS_SitoCallbackHandler {
    protected Object clientData;

    /**
     * User can pass in any object that needs to be accessed once the NonBlocking
     * Web service call is finished and appropriate method of this CallBack is called.
     * @param clientData Object mechanism by which the user can pass in user data
     * that will be avilable at the time this callback is called.
     */
    public WS_SitoCallbackHandler(Object clientData) {
        this.clientData = clientData;
    }

    /**
     * Please use this constructor if you don't want to set any clientData
     */
    public WS_SitoCallbackHandler() {
        this.clientData = null;
    }

    /**
     * Get the client data
     */
    public Object getClientData() {
        return clientData;
    }

    /**
     * auto generated Axis2 call back method for impegnoConfermatoNegozioClienteListinoBox method
     * override this method for handling normal response from impegnoConfermatoNegozioClienteListinoBox operation
     */
    public void receiveResultimpegnoConfermatoNegozioClienteListinoBox(
        com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.ImpegnoConfermatoNegozioClienteListinoBoxResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from impegnoConfermatoNegozioClienteListinoBox operation
     */
    public void receiveErrorimpegnoConfermatoNegozioClienteListinoBox(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for ordineConfermatoClienteListino method
     * override this method for handling normal response from ordineConfermatoClienteListino operation
     */
    public void receiveResultordineConfermatoClienteListino(
        com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.OrdineConfermatoClienteListinoResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from ordineConfermatoClienteListino operation
     */
    public void receiveErrorordineConfermatoClienteListino(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for ordineConfermatoPrevenditaClienteData method
     * override this method for handling normal response from ordineConfermatoPrevenditaClienteData operation
     */
    public void receiveResultordineConfermatoPrevenditaClienteData(
        com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.OrdineConfermatoPrevenditaClienteDataResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from ordineConfermatoPrevenditaClienteData operation
     */
    public void receiveErrorordineConfermatoPrevenditaClienteData(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for ordineConfermato method
     * override this method for handling normal response from ordineConfermato operation
     */
    public void receiveResultordineConfermato(
        com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.OrdineConfermatoResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from ordineConfermato operation
     */
    public void receiveErrorordineConfermato(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for impegnoConfermatoClienteListinoBox method
     * override this method for handling normal response from impegnoConfermatoClienteListinoBox operation
     */
    public void receiveResultimpegnoConfermatoClienteListinoBox(
        com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.ImpegnoConfermatoClienteListinoBoxResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from impegnoConfermatoClienteListinoBox operation
     */
    public void receiveErrorimpegnoConfermatoClienteListinoBox(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for disponibilitaVarianteTaglia method
     * override this method for handling normal response from disponibilitaVarianteTaglia operation
     */
    public void receiveResultdisponibilitaVarianteTaglia(
        com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.DisponibilitaVarianteTagliaResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from disponibilitaVarianteTaglia operation
     */
    public void receiveErrordisponibilitaVarianteTaglia(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for ordineAssociaBarcode method
     * override this method for handling normal response from ordineAssociaBarcode operation
     */
    public void receiveResultordineAssociaBarcode(
        com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.OrdineAssociaBarcodeResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from ordineAssociaBarcode operation
     */
    public void receiveErrorordineAssociaBarcode(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for chiusuraOrdine method
     * override this method for handling normal response from chiusuraOrdine operation
     */
    public void receiveResultchiusuraOrdine(
        com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.ChiusuraOrdineResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from chiusuraOrdine operation
     */
    public void receiveErrorchiusuraOrdine(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for impegnoConfermatoClienteDestinazioneDiversaListino method
     * override this method for handling normal response from impegnoConfermatoClienteDestinazioneDiversaListino operation
     */
    public void receiveResultimpegnoConfermatoClienteDestinazioneDiversaListino(
        com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.ImpegnoConfermatoClienteDestinazioneDiversaListinoResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from impegnoConfermatoClienteDestinazioneDiversaListino operation
     */
    public void receiveErrorimpegnoConfermatoClienteDestinazioneDiversaListino(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for impegnoConfermatoClienteDestinazioneDiversaListinoBox method
     * override this method for handling normal response from impegnoConfermatoClienteDestinazioneDiversaListinoBox operation
     */
    public void receiveResultimpegnoConfermatoClienteDestinazioneDiversaListinoBox(
        com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.ImpegnoConfermatoClienteDestinazioneDiversaListinoBoxResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from impegnoConfermatoClienteDestinazioneDiversaListinoBox operation
     */
    public void receiveErrorimpegnoConfermatoClienteDestinazioneDiversaListinoBox(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for cancellaOrdine method
     * override this method for handling normal response from cancellaOrdine operation
     */
    public void receiveResultcancellaOrdine(
        com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.CancellaOrdineResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from cancellaOrdine operation
     */
    public void receiveErrorcancellaOrdine(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for ordineConfermatoPrevenditaClienteListino method
     * override this method for handling normal response from ordineConfermatoPrevenditaClienteListino operation
     */
    public void receiveResultordineConfermatoPrevenditaClienteListino(
        com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.OrdineConfermatoPrevenditaClienteListinoResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from ordineConfermatoPrevenditaClienteListino operation
     */
    public void receiveErrorordineConfermatoPrevenditaClienteListino(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for ordineConfermatoClienteData method
     * override this method for handling normal response from ordineConfermatoClienteData operation
     */
    public void receiveResultordineConfermatoClienteData(
        com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.OrdineConfermatoClienteDataResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from ordineConfermatoClienteData operation
     */
    public void receiveErrorordineConfermatoClienteData(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for ordineConfermatoPrevendita method
     * override this method for handling normal response from ordineConfermatoPrevendita operation
     */
    public void receiveResultordineConfermatoPrevendita(
        com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.OrdineConfermatoPrevenditaResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from ordineConfermatoPrevendita operation
     */
    public void receiveErrorordineConfermatoPrevendita(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for disponibilitaVarianteTagliaPrevendita method
     * override this method for handling normal response from disponibilitaVarianteTagliaPrevendita operation
     */
    public void receiveResultdisponibilitaVarianteTagliaPrevendita(
        com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.DisponibilitaVarianteTagliaPrevenditaResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from disponibilitaVarianteTagliaPrevendita operation
     */
    public void receiveErrordisponibilitaVarianteTagliaPrevendita(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for disponibilitaVarianteTagliaPrevenditaArrivo method
     * override this method for handling normal response from disponibilitaVarianteTagliaPrevenditaArrivo operation
     */
    public void receiveResultdisponibilitaVarianteTagliaPrevenditaArrivo(
        com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.DisponibilitaVarianteTagliaPrevenditaArrivoResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from disponibilitaVarianteTagliaPrevenditaArrivo operation
     */
    public void receiveErrordisponibilitaVarianteTagliaPrevenditaArrivo(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for ordineConfermatoCliente method
     * override this method for handling normal response from ordineConfermatoCliente operation
     */
    public void receiveResultordineConfermatoCliente(
        com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.OrdineConfermatoClienteResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from ordineConfermatoCliente operation
     */
    public void receiveErrorordineConfermatoCliente(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for associaGiftCard method
     * override this method for handling normal response from associaGiftCard operation
     */
    public void receiveResultassociaGiftCard(
        com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.AssociaGiftCardResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from associaGiftCard operation
     */
    public void receiveErrorassociaGiftCard(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for annullaOrdineConReso method
     * override this method for handling normal response from annullaOrdineConReso operation
     */
    public void receiveResultannullaOrdineConReso(
        com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.AnnullaOrdineConResoResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from annullaOrdineConReso operation
     */
    public void receiveErrorannullaOrdineConReso(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for chiusuraOrdineConPagamentoeDestinazione method
     * override this method for handling normal response from chiusuraOrdineConPagamentoeDestinazione operation
     */
    public void receiveResultchiusuraOrdineConPagamentoeDestinazione(
        com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.ChiusuraOrdineConPagamentoeDestinazioneResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from chiusuraOrdineConPagamentoeDestinazione operation
     */
    public void receiveErrorchiusuraOrdineConPagamentoeDestinazione(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for addebitaGiftCard method
     * override this method for handling normal response from addebitaGiftCard operation
     */
    public void receiveResultaddebitaGiftCard(
        com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.AddebitaGiftCardResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from addebitaGiftCard operation
     */
    public void receiveErroraddebitaGiftCard(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for ordineConfermatoClienteAnnulla method
     * override this method for handling normal response from ordineConfermatoClienteAnnulla operation
     */
    public void receiveResultordineConfermatoClienteAnnulla(
        com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.OrdineConfermatoClienteAnnullaResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from ordineConfermatoClienteAnnulla operation
     */
    public void receiveErrorordineConfermatoClienteAnnulla(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for chiusuraOrdineConPagamento method
     * override this method for handling normal response from chiusuraOrdineConPagamento operation
     */
    public void receiveResultchiusuraOrdineConPagamento(
        com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.ChiusuraOrdineConPagamentoResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from chiusuraOrdineConPagamento operation
     */
    public void receiveErrorchiusuraOrdineConPagamento(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for recuperaVA_ID method
     * override this method for handling normal response from recuperaVA_ID operation
     */
    public void receiveResultrecuperaVA_ID(
        com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.RecuperaVA_IDResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from recuperaVA_ID operation
     */
    public void receiveErrorrecuperaVA_ID(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for impegnoConfermatoNegozioClienteListino method
     * override this method for handling normal response from impegnoConfermatoNegozioClienteListino operation
     */
    public void receiveResultimpegnoConfermatoNegozioClienteListino(
        com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.ImpegnoConfermatoNegozioClienteListinoResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from impegnoConfermatoNegozioClienteListino operation
     */
    public void receiveErrorimpegnoConfermatoNegozioClienteListino(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for chiusuraImpegnoConPagamento method
     * override this method for handling normal response from chiusuraImpegnoConPagamento operation
     */
    public void receiveResultchiusuraImpegnoConPagamento(
        com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.ChiusuraImpegnoConPagamentoResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from chiusuraImpegnoConPagamento operation
     */
    public void receiveErrorchiusuraImpegnoConPagamento(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for ordineConfermatoClienteSpese method
     * override this method for handling normal response from ordineConfermatoClienteSpese operation
     */
    public void receiveResultordineConfermatoClienteSpese(
        com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.OrdineConfermatoClienteSpeseResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from ordineConfermatoClienteSpese operation
     */
    public void receiveErrorordineConfermatoClienteSpese(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for verificaGiftCard method
     * override this method for handling normal response from verificaGiftCard operation
     */
    public void receiveResultverificaGiftCard(
        com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.VerificaGiftCardResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from verificaGiftCard operation
     */
    public void receiveErrorverificaGiftCard(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for chiusuraOrdineConPagamentoeDestinazioneNoFattura method
     * override this method for handling normal response from chiusuraOrdineConPagamentoeDestinazioneNoFattura operation
     */
    public void receiveResultchiusuraOrdineConPagamentoeDestinazioneNoFattura(
        com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.ChiusuraOrdineConPagamentoeDestinazioneNoFatturaResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from chiusuraOrdineConPagamentoeDestinazioneNoFattura operation
     */
    public void receiveErrorchiusuraOrdineConPagamentoeDestinazioneNoFattura(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for impegnoConfermatoClienteListino method
     * override this method for handling normal response from impegnoConfermatoClienteListino operation
     */
    public void receiveResultimpegnoConfermatoClienteListino(
        com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.ImpegnoConfermatoClienteListinoResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from impegnoConfermatoClienteListino operation
     */
    public void receiveErrorimpegnoConfermatoClienteListino(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for chiusuraImpegno method
     * override this method for handling normal response from chiusuraImpegno operation
     */
    public void receiveResultchiusuraImpegno(
        com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.ChiusuraImpegnoResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from chiusuraImpegno operation
     */
    public void receiveErrorchiusuraImpegno(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for aggiornaCliente method
     * override this method for handling normal response from aggiornaCliente operation
     */
    public void receiveResultaggiornaCliente(
        com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.AggiornaClienteResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from aggiornaCliente operation
     */
    public void receiveErroraggiornaCliente(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for ordineConfermatoPrevenditaCliente method
     * override this method for handling normal response from ordineConfermatoPrevenditaCliente operation
     */
    public void receiveResultordineConfermatoPrevenditaCliente(
        com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.OrdineConfermatoPrevenditaClienteResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from ordineConfermatoPrevenditaCliente operation
     */
    public void receiveErrorordineConfermatoPrevenditaCliente(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for controllaWebService method
     * override this method for handling normal response from controllaWebService operation
     */
    public void receiveResultcontrollaWebService(
        com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.ControllaWebServiceResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from controllaWebService operation
     */
    public void receiveErrorcontrollaWebService(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for aggiornaClienteCompleta method
     * override this method for handling normal response from aggiornaClienteCompleta operation
     */
    public void receiveResultaggiornaClienteCompleta(
        com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.AggiornaClienteCompletaResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from aggiornaClienteCompleta operation
     */
    public void receiveErroraggiornaClienteCompleta(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for prodottoCancellato method
     * override this method for handling normal response from prodottoCancellato operation
     */
    public void receiveResultprodottoCancellato(
        com.shangpin.iog.brunarosso.order.axis2.WS_SitoStub.ProdottoCancellatoResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from prodottoCancellato operation
     */
    public void receiveErrorprodottoCancellato(java.lang.Exception e) {
    }
}
