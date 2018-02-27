package eu.monnalisa.pf;

public class MonnalisaWSProxy implements eu.monnalisa.pf.MonnalisaWS {
  private String _endpoint = null;
  private eu.monnalisa.pf.MonnalisaWS monnalisaWS = null;
  
  public MonnalisaWSProxy() {
    _initMonnalisaWSProxy();
  }
  
  public MonnalisaWSProxy(String endpoint) {
    _endpoint = endpoint;
    _initMonnalisaWSProxy();
  }
  
  private void _initMonnalisaWSProxy() {
    try {
      monnalisaWS = (new eu.monnalisa.pf.MonnalisaWSServiceLocator()).getMonnalisaWSPort();
      if (monnalisaWS != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)monnalisaWS)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)monnalisaWS)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (monnalisaWS != null)
      ((javax.xml.rpc.Stub)monnalisaWS)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public eu.monnalisa.pf.MonnalisaWS getMonnalisaWS() {
    if (monnalisaWS == null)
      _initMonnalisaWSProxy();
    return monnalisaWS;
  }
  
  public int getQuantitaPrenotate(java.lang.String cod_collezione, java.lang.String cod_modello, java.lang.String cod_articolo, java.lang.String cod_colore, java.lang.String cod_taglia, java.lang.String cod_magazzino, java.lang.String cliente, java.lang.String userid, java.lang.String cart_id) throws java.rmi.RemoteException{
    if (monnalisaWS == null)
      _initMonnalisaWSProxy();
    return monnalisaWS.getQuantitaPrenotate(cod_collezione, cod_modello, cod_articolo, cod_colore, cod_taglia, cod_magazzino, cliente, userid, cart_id);
  }
  
  public int getDisponibilitaSkuParziale(java.lang.String cod_collezione, java.lang.String cod_modello, java.lang.String cod_articolo, java.lang.String cod_colore, java.lang.String cod_taglia, java.lang.String cod_magazzino, java.lang.String cliente, java.lang.String userid, java.lang.String cart_id) throws java.rmi.RemoteException{
    if (monnalisaWS == null)
      _initMonnalisaWSProxy();
    return monnalisaWS.getDisponibilitaSkuParziale(cod_collezione, cod_modello, cod_articolo, cod_colore, cod_taglia, cod_magazzino, cliente, userid, cart_id);
  }
  
  public java.lang.String getDisponibilitaListaCategoriaAuth(java.lang.String sku_string, java.lang.String cod_magazzino, java.lang.String userid) throws java.rmi.RemoteException{
    if (monnalisaWS == null)
      _initMonnalisaWSProxy();
    return monnalisaWS.getDisponibilitaListaCategoriaAuth(sku_string, cod_magazzino, userid);
  }
  
  public eu.monnalisa.pf.ExtendedSearchResult getDisponibilitaCollezione(java.lang.String cod_collezione, java.lang.String cod_magazzino, java.lang.String cliente, java.lang.String userid, java.lang.String cart_id) throws java.rmi.RemoteException{
    if (monnalisaWS == null)
      _initMonnalisaWSProxy();
    return monnalisaWS.getDisponibilitaCollezione(cod_collezione, cod_magazzino, cliente, userid, cart_id);
  }
  
  public int getDisponibilita(java.lang.String cod_collezione, java.lang.String cod_modello, java.lang.String cod_articolo, java.lang.String cod_colore, java.lang.String cod_taglia, java.lang.String cod_magazzino, java.lang.String cliente, java.lang.String userid, java.lang.String cart_id) throws java.rmi.RemoteException{
    if (monnalisaWS == null)
      _initMonnalisaWSProxy();
    return monnalisaWS.getDisponibilita(cod_collezione, cod_modello, cod_articolo, cod_colore, cod_taglia, cod_magazzino, cliente, userid, cart_id);
  }
  
  public eu.monnalisa.pf.MagazzinoResult[] getMagazzini(java.lang.String cliente, java.lang.String userid) throws java.rmi.RemoteException{
    if (monnalisaWS == null)
      _initMonnalisaWSProxy();
    return monnalisaWS.getMagazzini(cliente, userid);
  }
  
  public eu.monnalisa.pf.OrdinaResult cambiaCliente(java.lang.String cart_id, java.lang.String cliente_old, java.lang.String cliente_new, java.lang.String userid) throws java.rmi.RemoteException{
    if (monnalisaWS == null)
      _initMonnalisaWSProxy();
    return monnalisaWS.cambiaCliente(cart_id, cliente_old, cliente_new, userid);
  }
  
  public eu.monnalisa.pf.ExtraInfoSearchResult getExtraInfoDisponibilitaProdotti(java.lang.String cod_collezione, java.lang.String cod_tipo_settore, java.lang.String cod_tipo_modello, java.lang.String cod_tema, java.lang.String cod_colore, java.lang.String cod_magazzino, java.lang.String cliente, java.lang.String userid, java.lang.String cart_id) throws java.rmi.RemoteException{
    if (monnalisaWS == null)
      _initMonnalisaWSProxy();
    return monnalisaWS.getExtraInfoDisponibilitaProdotti(cod_collezione, cod_tipo_settore, cod_tipo_modello, cod_tema, cod_colore, cod_magazzino, cliente, userid, cart_id);
  }
  
  public eu.monnalisa.pf.SearchResultMag getDisponibilitaListaCategoriaTagliaColoreMagazzini(java.lang.String sku_string, java.lang.String taglia_string, java.lang.String colore_string, java.lang.String cod_magazzino, java.lang.String userid) throws java.rmi.RemoteException{
    if (monnalisaWS == null)
      _initMonnalisaWSProxy();
    return monnalisaWS.getDisponibilitaListaCategoriaTagliaColoreMagazzini(sku_string, taglia_string, colore_string, cod_magazzino, userid);
  }
  
  public eu.monnalisa.pf.SearchResult getSottoDisponibilitaTaglie(java.lang.String cod_collezione, java.lang.String cod_modello, java.lang.String cod_articolo, java.lang.String cod_colore, java.lang.String cod_magazzino, java.lang.String userid, java.lang.String cart_id) throws java.rmi.RemoteException{
    if (monnalisaWS == null)
      _initMonnalisaWSProxy();
    return monnalisaWS.getSottoDisponibilitaTaglie(cod_collezione, cod_modello, cod_articolo, cod_colore, cod_magazzino, userid, cart_id);
  }
  
  public eu.monnalisa.pf.ExtraInfoSearchResult getExtraInfoDisponibilitaColore(java.lang.String cod_collezione, java.lang.String cod_tipo_settore, java.lang.String cod_tipo_modello, java.lang.String cod_tema, java.lang.String cod_colore, java.lang.String cod_magazzino, java.lang.String cliente, java.lang.String userid, java.lang.String cart_id) throws java.rmi.RemoteException{
    if (monnalisaWS == null)
      _initMonnalisaWSProxy();
    return monnalisaWS.getExtraInfoDisponibilitaColore(cod_collezione, cod_tipo_settore, cod_tipo_modello, cod_tema, cod_colore, cod_magazzino, cliente, userid, cart_id);
  }
  
  public int notificaCambioStatoOrdine(java.lang.String stato, java.lang.String cod_magazzino, java.lang.String cliente, java.lang.String userid, java.lang.String cart_id) throws java.rmi.RemoteException{
    if (monnalisaWS == null)
      _initMonnalisaWSProxy();
    return monnalisaWS.notificaCambioStatoOrdine(stato, cod_magazzino, cliente, userid, cart_id);
  }
  
  public int notificaCambioStatoRigaOrdineSku(java.lang.String cod_collezione, java.lang.String cod_modello, java.lang.String cod_articolo, java.lang.String cod_colore, java.lang.String cod_taglia, java.lang.String cod_magazzino, java.lang.String cliente, java.lang.String userid, java.lang.String cart_id, java.lang.String stato, int qta) throws java.rmi.RemoteException{
    if (monnalisaWS == null)
      _initMonnalisaWSProxy();
    return monnalisaWS.notificaCambioStatoRigaOrdineSku(cod_collezione, cod_modello, cod_articolo, cod_colore, cod_taglia, cod_magazzino, cliente, userid, cart_id, stato, qta);
  }
  
  public java.lang.String getBarcodeFromSku(java.lang.String cod_collezione, java.lang.String cod_modello, java.lang.String cod_articolo, java.lang.String cod_colore, java.lang.String cod_taglia, java.lang.String cliente, java.lang.String userid, java.lang.String cart_id) throws java.rmi.RemoteException{
    if (monnalisaWS == null)
      _initMonnalisaWSProxy();
    return monnalisaWS.getBarcodeFromSku(cod_collezione, cod_modello, cod_articolo, cod_colore, cod_taglia, cliente, userid, cart_id);
  }
  
  public eu.monnalisa.pf.ExtraInfoSearchResult getDisponibilitaMagazziniByBarcode(java.lang.String barcode, java.lang.String cliente, java.lang.String userid, java.lang.String cart_id) throws java.rmi.RemoteException{
    if (monnalisaWS == null)
      _initMonnalisaWSProxy();
    return monnalisaWS.getDisponibilitaMagazziniByBarcode(barcode, cliente, userid, cart_id);
  }
  
  public eu.monnalisa.pf.OrdinaResult ordina(java.lang.String cart_id, java.lang.String cliente, java.lang.String userid, boolean inizioPagamento) throws java.rmi.RemoteException{
    if (monnalisaWS == null)
      _initMonnalisaWSProxy();
    return monnalisaWS.ordina(cart_id, cliente, userid, inizioPagamento);
  }
  
  public Magento.SalesOrderEntityArray magentoGetListaFatture(java.lang.String username, java.lang.String apiKey, java.lang.String from, java.lang.String to) throws java.rmi.RemoteException{
    if (monnalisaWS == null)
      _initMonnalisaWSProxy();
    return monnalisaWS.magentoGetListaFatture(username, apiKey, from, to);
  }
  
  public eu.monnalisa.pf.ExtraInfoSearchResult getExtraInfoDisponibilitaLinea(java.lang.String cod_collezione, java.lang.String cod_tipo_settore, java.lang.String cod_tipo_modello, java.lang.String cod_tema, java.lang.String cod_colore, java.lang.String cod_magazzino, java.lang.String cliente, java.lang.String userid, java.lang.String cart_id) throws java.rmi.RemoteException{
    if (monnalisaWS == null)
      _initMonnalisaWSProxy();
    return monnalisaWS.getExtraInfoDisponibilitaLinea(cod_collezione, cod_tipo_settore, cod_tipo_modello, cod_tema, cod_colore, cod_magazzino, cliente, userid, cart_id);
  }
  
  public eu.monnalisa.pf.CartResult getPrenotazioniCarrello(java.lang.String cliente, java.lang.String userid, java.lang.String cart_id) throws java.rmi.RemoteException{
    if (monnalisaWS == null)
      _initMonnalisaWSProxy();
    return monnalisaWS.getPrenotazioniCarrello(cliente, userid, cart_id);
  }
  
  public int cancellaPrenotazioneDaId(long cod_prenotazione, int qta, java.lang.String cod_magazzino, java.lang.String cliente, java.lang.String userid, java.lang.String cart_id) throws java.rmi.RemoteException{
    if (monnalisaWS == null)
      _initMonnalisaWSProxy();
    return monnalisaWS.cancellaPrenotazioneDaId(cod_prenotazione, qta, cod_magazzino, cliente, userid, cart_id);
  }
  
  public eu.monnalisa.pf.SearchResult getDisponibilitaListaCategoriaTagliaColore(java.lang.String sku_string, java.lang.String taglia_string, java.lang.String colore_string, java.lang.String cod_magazzino, java.lang.String userid) throws java.rmi.RemoteException{
    if (monnalisaWS == null)
      _initMonnalisaWSProxy();
    return monnalisaWS.getDisponibilitaListaCategoriaTagliaColore(sku_string, taglia_string, colore_string, cod_magazzino, userid);
  }
  
  public eu.monnalisa.pf.SearchResult getSottoDisponibilitaTaglieColori(java.lang.String cod_collezione, java.lang.String cod_modello, java.lang.String cod_articolo, java.lang.String cod_magazzino, java.lang.String userid, java.lang.String cart_id) throws java.rmi.RemoteException{
    if (monnalisaWS == null)
      _initMonnalisaWSProxy();
    return monnalisaWS.getSottoDisponibilitaTaglieColori(cod_collezione, cod_modello, cod_articolo, cod_magazzino, userid, cart_id);
  }
  
  public java.lang.String getDisponibilitaListaCategoria(java.lang.String sku_string) throws java.rmi.RemoteException{
    if (monnalisaWS == null)
      _initMonnalisaWSProxy();
    return monnalisaWS.getDisponibilitaListaCategoria(sku_string);
  }
  
  public eu.monnalisa.pf.PrenotazioneResult prenotaArticoloEsteso(java.lang.String cod_collezione, java.lang.String cod_modello, java.lang.String cod_articolo, java.lang.String cod_colore, java.lang.String cod_taglia, int qta, java.lang.String cod_magazzino, java.lang.String cliente, java.lang.String userid, java.lang.String cart_id, int secondsBeforeExpiry, java.lang.String note) throws java.rmi.RemoteException{
    if (monnalisaWS == null)
      _initMonnalisaWSProxy();
    return monnalisaWS.prenotaArticoloEsteso(cod_collezione, cod_modello, cod_articolo, cod_colore, cod_taglia, qta, cod_magazzino, cliente, userid, cart_id, secondsBeforeExpiry, note);
  }
  
  public eu.monnalisa.pf.ExtraInfoSearchResult getExtraInfoDisponibilitaTipologia(java.lang.String cod_collezione, java.lang.String cod_tipo_settore, java.lang.String cod_tipo_modello, java.lang.String cod_tema, java.lang.String cod_colore, java.lang.String cod_magazzino, java.lang.String cliente, java.lang.String userid, java.lang.String cart_id) throws java.rmi.RemoteException{
    if (monnalisaWS == null)
      _initMonnalisaWSProxy();
    return monnalisaWS.getExtraInfoDisponibilitaTipologia(cod_collezione, cod_tipo_settore, cod_tipo_modello, cod_tema, cod_colore, cod_magazzino, cliente, userid, cart_id);
  }
  
  public eu.monnalisa.pf.PrenotazioneResult notificaCambioStatoRigaOrdineSplitSku(java.lang.String cod_collezione, java.lang.String cod_modello, java.lang.String cod_articolo, java.lang.String cod_colore, java.lang.String cod_taglia, java.lang.String cod_magazzino, java.lang.String cliente, java.lang.String userid, java.lang.String cart_id, java.lang.String stato, int qta) throws java.rmi.RemoteException{
    if (monnalisaWS == null)
      _initMonnalisaWSProxy();
    return monnalisaWS.notificaCambioStatoRigaOrdineSplitSku(cod_collezione, cod_modello, cod_articolo, cod_colore, cod_taglia, cod_magazzino, cliente, userid, cart_id, stato, qta);
  }
  
  public eu.monnalisa.pf.GenericResult getDisponibilitaMagazzini(java.lang.String cod_collezione, java.lang.String cod_modello, java.lang.String cod_articolo, java.lang.String cod_colore, java.lang.String cod_taglia, java.lang.String cod_magazzino, java.lang.String cliente, java.lang.String userid, java.lang.String cart_id) throws java.rmi.RemoteException{
    if (monnalisaWS == null)
      _initMonnalisaWSProxy();
    return monnalisaWS.getDisponibilitaMagazzini(cod_collezione, cod_modello, cod_articolo, cod_colore, cod_taglia, cod_magazzino, cliente, userid, cart_id);
  }
  
  public java.lang.String magentoConfermaFattura(java.lang.String username, java.lang.String apiKey, java.lang.String codiceOrdine) throws java.rmi.RemoteException{
    if (monnalisaWS == null)
      _initMonnalisaWSProxy();
    return monnalisaWS.magentoConfermaFattura(username, apiKey, codiceOrdine);
  }
  
  public eu.monnalisa.pf.PrenotazioneResult prenotaArticolo(java.lang.String cod_collezione, java.lang.String cod_modello, java.lang.String cod_articolo, java.lang.String cod_colore, java.lang.String cod_taglia, int qta, java.lang.String cod_magazzino, java.lang.String cliente, java.lang.String userid, java.lang.String cart_id) throws java.rmi.RemoteException{
    if (monnalisaWS == null)
      _initMonnalisaWSProxy();
    return monnalisaWS.prenotaArticolo(cod_collezione, cod_modello, cod_articolo, cod_colore, cod_taglia, qta, cod_magazzino, cliente, userid, cart_id);
  }
  
  public int notificaCambioStatoRigaOrdine(java.lang.String stato, java.lang.String cod_magazzino, java.lang.String cliente, java.lang.String userid, long cod_prenotazione, int qta) throws java.rmi.RemoteException{
    if (monnalisaWS == null)
      _initMonnalisaWSProxy();
    return monnalisaWS.notificaCambioStatoRigaOrdine(stato, cod_magazzino, cliente, userid, cod_prenotazione, qta);
  }
  
  public int cancellaPrenotazione(java.lang.String cod_collezione, java.lang.String cod_modello, java.lang.String cod_articolo, java.lang.String cod_colore, java.lang.String cod_taglia, int qta, java.lang.String cod_magazzino, java.lang.String cliente, java.lang.String userid, java.lang.String cart_id) throws java.rmi.RemoteException{
    if (monnalisaWS == null)
      _initMonnalisaWSProxy();
    return monnalisaWS.cancellaPrenotazione(cod_collezione, cod_modello, cod_articolo, cod_colore, cod_taglia, qta, cod_magazzino, cliente, userid, cart_id);
  }
  
  public eu.monnalisa.pf.ExtendedSearchResult getSottoDisponibilitaTaglieColoriSkuList(java.lang.String sku_string, java.lang.String cod_magazzino, java.lang.String userid, java.lang.String cart_id) throws java.rmi.RemoteException{
    if (monnalisaWS == null)
      _initMonnalisaWSProxy();
    return monnalisaWS.getSottoDisponibilitaTaglieColoriSkuList(sku_string, cod_magazzino, userid, cart_id);
  }
  
  public eu.monnalisa.pf.ExtraInfoSearchResult getExtraInfoDisponibilitaTema(java.lang.String cod_collezione, java.lang.String cod_tipo_settore, java.lang.String cod_tipo_modello, java.lang.String cod_tema, java.lang.String cod_colore, java.lang.String cod_magazzino, java.lang.String cliente, java.lang.String userid, java.lang.String cart_id) throws java.rmi.RemoteException{
    if (monnalisaWS == null)
      _initMonnalisaWSProxy();
    return monnalisaWS.getExtraInfoDisponibilitaTema(cod_collezione, cod_tipo_settore, cod_tipo_modello, cod_tema, cod_colore, cod_magazzino, cliente, userid, cart_id);
  }
  
  
}