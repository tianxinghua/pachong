/**
 * LcvMagWSCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.3  Built on : Jun 27, 2015 (11:17:49 BST)
 */
package com.shangpin.iog.biondini.axis2;


/**
 *  LcvMagWSCallbackHandler Callback class, Users can extend this class and implement
 *  their own receiveResult and receiveError methods.
 */
public abstract class LcvMagWSCallbackHandler {
    protected Object clientData;

    /**
     * User can pass in any object that needs to be accessed once the NonBlocking
     * Web service call is finished and appropriate method of this CallBack is called.
     * @param clientData Object mechanism by which the user can pass in user data
     * that will be avilable at the time this callback is called.
     */
    public LcvMagWSCallbackHandler(Object clientData) {
        this.clientData = clientData;
    }

    /**
     * Please use this constructor if you don't want to set any clientData
     */
    public LcvMagWSCallbackHandler() {
        this.clientData = null;
    }

    /**
     * Get the client data
     */
    public Object getClientData() {
        return clientData;
    }

    /**
     * auto generated Axis2 call back method for lectureDesModelesAvecPrix method
     * override this method for handling normal response from lectureDesModelesAvecPrix operation
     */
    public void receiveResultlectureDesModelesAvecPrix(
        com.shangpin.iog.biondini.axis2.LcvMagWSStub.LectureDesModelesAvecPrixResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from lectureDesModelesAvecPrix operation
     */
    public void receiveErrorlectureDesModelesAvecPrix(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for lectureDetailCdeGesCom method
     * override this method for handling normal response from lectureDetailCdeGesCom operation
     */
    public void receiveResultlectureDetailCdeGesCom(
        com.shangpin.iog.biondini.axis2.LcvMagWSStub.LectureDetailCdeGesComResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from lectureDetailCdeGesCom operation
     */
    public void receiveErrorlectureDetailCdeGesCom(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for ecritureTransfertDepartTransit method
     * override this method for handling normal response from ecritureTransfertDepartTransit operation
     */
    public void receiveResultecritureTransfertDepartTransit(
        com.shangpin.iog.biondini.axis2.LcvMagWSStub.EcritureTransfertDepartTransitResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from ecritureTransfertDepartTransit operation
     */
    public void receiveErrorecritureTransfertDepartTransit(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for lectureTarifWeb method
     * override this method for handling normal response from lectureTarifWeb operation
     */
    public void receiveResultlectureTarifWeb(
        com.shangpin.iog.biondini.axis2.LcvMagWSStub.LectureTarifWebResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from lectureTarifWeb operation
     */
    public void receiveErrorlectureTarifWeb(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for ecritureVenteGesCom method
     * override this method for handling normal response from ecritureVenteGesCom operation
     */
    public void receiveResultecritureVenteGesCom(
        com.shangpin.iog.biondini.axis2.LcvMagWSStub.EcritureVenteGesComResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from ecritureVenteGesCom operation
     */
    public void receiveErrorecritureVenteGesCom(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for connexionClient method
     * override this method for handling normal response from connexionClient operation
     */
    public void receiveResultconnexionClient(
        com.shangpin.iog.biondini.axis2.LcvMagWSStub.ConnexionClientResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from connexionClient operation
     */
    public void receiveErrorconnexionClient(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for ecritureVenteSKU method
     * override this method for handling normal response from ecritureVenteSKU operation
     */
    public void receiveResultecritureVenteSKU(
        com.shangpin.iog.biondini.axis2.LcvMagWSStub.EcritureVenteSKUResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from ecritureVenteSKU operation
     */
    public void receiveErrorecritureVenteSKU(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for ecritureClient method
     * override this method for handling normal response from ecritureClient operation
     */
    public void receiveResultecritureClient(
        com.shangpin.iog.biondini.axis2.LcvMagWSStub.EcritureClientResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from ecritureClient operation
     */
    public void receiveErrorecritureClient(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for pE_Creer_Reception method
     * override this method for handling normal response from pE_Creer_Reception operation
     */
    public void receiveResultpE_Creer_Reception(
        com.shangpin.iog.biondini.axis2.LcvMagWSStub.PE_Creer_ReceptionResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from pE_Creer_Reception operation
     */
    public void receiveErrorpE_Creer_Reception(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for lectureStockParMag method
     * override this method for handling normal response from lectureStockParMag operation
     */
    public void receiveResultlectureStockParMag(
        com.shangpin.iog.biondini.axis2.LcvMagWSStub.LectureStockParMagResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from lectureStockParMag operation
     */
    public void receiveErrorlectureStockParMag(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for lectureDesModelesGarant method
     * override this method for handling normal response from lectureDesModelesGarant operation
     */
    public void receiveResultlectureDesModelesGarant(
        com.shangpin.iog.biondini.axis2.LcvMagWSStub.LectureDesModelesGarantResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from lectureDesModelesGarant operation
     */
    public void receiveErrorlectureDesModelesGarant(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for lectureStockParMagSurCde method
     * override this method for handling normal response from lectureStockParMagSurCde operation
     */
    public void receiveResultlectureStockParMagSurCde(
        com.shangpin.iog.biondini.axis2.LcvMagWSStub.LectureStockParMagSurCdeResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from lectureStockParMagSurCde operation
     */
    public void receiveErrorlectureStockParMagSurCde(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for ecritureTransfertArriveeTransit method
     * override this method for handling normal response from ecritureTransfertArriveeTransit operation
     */
    public void receiveResultecritureTransfertArriveeTransit(
        com.shangpin.iog.biondini.axis2.LcvMagWSStub.EcritureTransfertArriveeTransitResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from ecritureTransfertArriveeTransit operation
     */
    public void receiveErrorecritureTransfertArriveeTransit(
        java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for lectureCBLCV method
     * override this method for handling normal response from lectureCBLCV operation
     */
    public void receiveResultlectureCBLCV(
        com.shangpin.iog.biondini.axis2.LcvMagWSStub.LectureCBLCVResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from lectureCBLCV operation
     */
    public void receiveErrorlectureCBLCV(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for lectureCdeGesCom method
     * override this method for handling normal response from lectureCdeGesCom operation
     */
    public void receiveResultlectureCdeGesCom(
        com.shangpin.iog.biondini.axis2.LcvMagWSStub.LectureCdeGesComResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from lectureCdeGesCom operation
     */
    public void receiveErrorlectureCdeGesCom(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for sP_Recuperer_Client method
     * override this method for handling normal response from sP_Recuperer_Client operation
     */
    public void receiveResultsP_Recuperer_Client(
        com.shangpin.iog.biondini.axis2.LcvMagWSStub.SP_Recuperer_ClientResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from sP_Recuperer_Client operation
     */
    public void receiveErrorsP_Recuperer_Client(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for lecturePromo method
     * override this method for handling normal response from lecturePromo operation
     */
    public void receiveResultlecturePromo(
        com.shangpin.iog.biondini.axis2.LcvMagWSStub.LecturePromoResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from lecturePromo operation
     */
    public void receiveErrorlecturePromo(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for annulationTransfert method
     * override this method for handling normal response from annulationTransfert operation
     */
    public void receiveResultannulationTransfert(
        com.shangpin.iog.biondini.axis2.LcvMagWSStub.AnnulationTransfertResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from annulationTransfert operation
     */
    public void receiveErrorannulationTransfert(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for lectureDuStock method
     * override this method for handling normal response from lectureDuStock operation
     */
    public void receiveResultlectureDuStock(
        com.shangpin.iog.biondini.axis2.LcvMagWSStub.LectureDuStockResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from lectureDuStock operation
     */
    public void receiveErrorlectureDuStock(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for ecritureVente method
     * override this method for handling normal response from ecritureVente operation
     */
    public void receiveResultecritureVente(
        com.shangpin.iog.biondini.axis2.LcvMagWSStub.EcritureVenteResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from ecritureVente operation
     */
    public void receiveErrorecritureVente(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for lecturePLA method
     * override this method for handling normal response from lecturePLA operation
     */
    public void receiveResultlecturePLA(
        com.shangpin.iog.biondini.axis2.LcvMagWSStub.LecturePLAResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from lecturePLA operation
     */
    public void receiveErrorlecturePLA(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for motdePassePerdu method
     * override this method for handling normal response from motdePassePerdu operation
     */
    public void receiveResultmotdePassePerdu(
        com.shangpin.iog.biondini.axis2.LcvMagWSStub.MotdePassePerduResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from motdePassePerdu operation
     */
    public void receiveErrormotdePassePerdu(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for sP_Creer_Produit method
     * override this method for handling normal response from sP_Creer_Produit operation
     */
    public void receiveResultsP_Creer_Produit(
        com.shangpin.iog.biondini.axis2.LcvMagWSStub.SP_Creer_ProduitResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from sP_Creer_Produit operation
     */
    public void receiveErrorsP_Creer_Produit(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for sP_Recuperer_Mouvement method
     * override this method for handling normal response from sP_Recuperer_Mouvement operation
     */
    public void receiveResultsP_Recuperer_Mouvement(
        com.shangpin.iog.biondini.axis2.LcvMagWSStub.SP_Recuperer_MouvementResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from sP_Recuperer_Mouvement operation
     */
    public void receiveErrorsP_Recuperer_Mouvement(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for ecritureCommentaireGesCom method
     * override this method for handling normal response from ecritureCommentaireGesCom operation
     */
    public void receiveResultecritureCommentaireGesCom(
        com.shangpin.iog.biondini.axis2.LcvMagWSStub.EcritureCommentaireGesComResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from ecritureCommentaireGesCom operation
     */
    public void receiveErrorecritureCommentaireGesCom(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for lectureStockParMag1 method
     * override this method for handling normal response from lectureStockParMag1 operation
     */
    public void receiveResultlectureStockParMag1(
        com.shangpin.iog.biondini.axis2.LcvMagWSStub.LectureStockParMag1Response result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from lectureStockParMag1 operation
     */
    public void receiveErrorlectureStockParMag1(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for lectureDuStock1 method
     * override this method for handling normal response from lectureDuStock1 operation
     */
    public void receiveResultlectureDuStock1(
        com.shangpin.iog.biondini.axis2.LcvMagWSStub.LectureDuStock1Response result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from lectureDuStock1 operation
     */
    public void receiveErrorlectureDuStock1(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for ecritureTransfertDepartDirect method
     * override this method for handling normal response from ecritureTransfertDepartDirect operation
     */
    public void receiveResultecritureTransfertDepartDirect(
        com.shangpin.iog.biondini.axis2.LcvMagWSStub.EcritureTransfertDepartDirectResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from ecritureTransfertDepartDirect operation
     */
    public void receiveErrorecritureTransfertDepartDirect(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for sP_Creer_Client method
     * override this method for handling normal response from sP_Creer_Client operation
     */
    public void receiveResultsP_Creer_Client(
        com.shangpin.iog.biondini.axis2.LcvMagWSStub.SP_Creer_ClientResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from sP_Creer_Client operation
     */
    public void receiveErrorsP_Creer_Client(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for testEmailClient method
     * override this method for handling normal response from testEmailClient operation
     */
    public void receiveResulttestEmailClient(
        com.shangpin.iog.biondini.axis2.LcvMagWSStub.TestEmailClientResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from testEmailClient operation
     */
    public void receiveErrortestEmailClient(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for ecritureRetourSKU method
     * override this method for handling normal response from ecritureRetourSKU operation
     */
    public void receiveResultecritureRetourSKU(
        com.shangpin.iog.biondini.axis2.LcvMagWSStub.EcritureRetourSKUResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from ecritureRetourSKU operation
     */
    public void receiveErrorecritureRetourSKU(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for sP_Creer_Commande method
     * override this method for handling normal response from sP_Creer_Commande operation
     */
    public void receiveResultsP_Creer_Commande(
        com.shangpin.iog.biondini.axis2.LcvMagWSStub.SP_Creer_CommandeResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from sP_Creer_Commande operation
     */
    public void receiveErrorsP_Creer_Commande(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for lectureDesModeles method
     * override this method for handling normal response from lectureDesModeles operation
     */
    public void receiveResultlectureDesModeles(
        com.shangpin.iog.biondini.axis2.LcvMagWSStub.LectureDesModelesResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from lectureDesModeles operation
     */
    public void receiveErrorlectureDesModeles(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for lectureDesTables method
     * override this method for handling normal response from lectureDesTables operation
     */
    public void receiveResultlectureDesTables(
        com.shangpin.iog.biondini.axis2.LcvMagWSStub.LectureDesTablesResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from lectureDesTables operation
     */
    public void receiveErrorlectureDesTables(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for sP_Maj_Grille method
     * override this method for handling normal response from sP_Maj_Grille operation
     */
    public void receiveResultsP_Maj_Grille(
        com.shangpin.iog.biondini.axis2.LcvMagWSStub.SP_Maj_GrilleResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from sP_Maj_Grille operation
     */
    public void receiveErrorsP_Maj_Grille(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for lectureClient method
     * override this method for handling normal response from lectureClient operation
     */
    public void receiveResultlectureClient(
        com.shangpin.iog.biondini.axis2.LcvMagWSStub.LectureClientResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from lectureClient operation
     */
    public void receiveErrorlectureClient(java.lang.Exception e) {
    }
}
