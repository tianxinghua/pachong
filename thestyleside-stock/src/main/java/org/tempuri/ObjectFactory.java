
package org.tempuri;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import org.datacontract.schemas._2004._07.maximag_connector_vidra_service.AnagraficaVO;
import org.datacontract.schemas._2004._07.maximag_connector_vidra_service.ArrayOfAnagraficaVO;
import org.datacontract.schemas._2004._07.maximag_connector_vidra_service.ArrayOfArticoloFlatExtLocaleVO;
import org.datacontract.schemas._2004._07.maximag_connector_vidra_service.ArrayOfArticoloFlatExtVO;
import org.datacontract.schemas._2004._07.maximag_connector_vidra_service.ArrayOfArticoloFlatVO;
import org.datacontract.schemas._2004._07.maximag_connector_vidra_service.ArrayOfArticoloLocaleVO;
import org.datacontract.schemas._2004._07.maximag_connector_vidra_service.ArrayOfCategoriaVO;
import org.datacontract.schemas._2004._07.maximag_connector_vidra_service.ArrayOfClasseVO;
import org.datacontract.schemas._2004._07.maximag_connector_vidra_service.ArrayOfColoreVO;
import org.datacontract.schemas._2004._07.maximag_connector_vidra_service.ArrayOfGruppoVO;
import org.datacontract.schemas._2004._07.maximag_connector_vidra_service.ArrayOfLineaVO;
import org.datacontract.schemas._2004._07.maximag_connector_vidra_service.ArrayOfNazioneVO;
import org.datacontract.schemas._2004._07.maximag_connector_vidra_service.ArrayOfRELSClassiSSCLassiVO;
import org.datacontract.schemas._2004._07.maximag_connector_vidra_service.ArrayOfRelClassiSClassiVO;
import org.datacontract.schemas._2004._07.maximag_connector_vidra_service.ArrayOfRelLineeClassiVO;
import org.datacontract.schemas._2004._07.maximag_connector_vidra_service.ArrayOfSottoCategoriaVO;
import org.datacontract.schemas._2004._07.maximag_connector_vidra_service.ArrayOfSottoGruppoVO;
import org.datacontract.schemas._2004._07.maximag_connector_vidra_service.ArrayOfSottoSottoCategoriaVO;
import org.datacontract.schemas._2004._07.maximag_connector_vidra_service.ArrayOfSottoSottoGruppoVO;
import org.datacontract.schemas._2004._07.maximag_connector_vidra_service.ArrayOfTagliaVO;
import org.datacontract.schemas._2004._07.maximag_connector_vidra_service.DataPageVOOfAnagraficaVO;
import org.datacontract.schemas._2004._07.maximag_connector_vidra_service.DocumentoVO;
import org.datacontract.schemas._2004._07.maximag_connector_vidra_service.MgDispo;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.tempuri package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetSottoCategorieUsr_QNAME = new QName("http://tempuri.org/", "usr");
    private final static QName _GetSottoCategoriePwd_QNAME = new QName("http://tempuri.org/", "pwd");
    private final static QName _GetPagedCustomerResponseGetPagedCustomerResult_QNAME = new QName("http://tempuri.org/", "GetPagedCustomerResult");
    private final static QName _InsertOrderOrder_QNAME = new QName("http://tempuri.org/", "order");
    private final static QName _GetArticoliFlatResponseGetArticoliFlatResult_QNAME = new QName("http://tempuri.org/", "GetArticoliFlatResult");
    private final static QName _GetArticoliFlatExtByDateResponseGetArticoliFlatExtByDateResult_QNAME = new QName("http://tempuri.org/", "GetArticoliFlatExtByDateResult");
    private final static QName _GetArticoliFlatExtLocaleByDateResponseGetArticoliFlatExtLocaleByDateResult_QNAME = new QName("http://tempuri.org/", "GetArticoliFlatExtLocaleByDateResult");
    private final static QName _GetSottoGruppoResponseGetSottoGruppoResult_QNAME = new QName("http://tempuri.org/", "GetSottoGruppoResult");
    private final static QName _GetRelationsSClassiSSClassiResponseGetRelationsSClassiSSClassiResult_QNAME = new QName("http://tempuri.org/", "GetRelationsSClassiSSClassiResult");
    private final static QName _GetCustomerResponseGetCustomerResult_QNAME = new QName("http://tempuri.org/", "GetCustomerResult");
    private final static QName _GetCustomerId_QNAME = new QName("http://tempuri.org/", "id");
    private final static QName _GetCustomerNome_QNAME = new QName("http://tempuri.org/", "nome");
    private final static QName _GetRelationsLineeClassiResponseGetRelationsLineeClassiResult_QNAME = new QName("http://tempuri.org/", "GetRelationsLineeClassiResult");
    private final static QName _GetArticoliFlatExtByDateFilter_QNAME = new QName("http://tempuri.org/", "filter");
    private final static QName _InsertOrderResponseInsertOrderResult_QNAME = new QName("http://tempuri.org/", "InsertOrderResult");
    private final static QName _GetSottoSottoGruppoResponseGetSottoSottoGruppoResult_QNAME = new QName("http://tempuri.org/", "GetSottoSottoGruppoResult");
    private final static QName _GetNazioniResponseGetNazioniResult_QNAME = new QName("http://tempuri.org/", "GetNazioniResult");
    private final static QName _GetRelationsClassiSClassiResponseGetRelationsClassiSClassiResult_QNAME = new QName("http://tempuri.org/", "GetRelationsClassiSClassiResult");
    private final static QName _InsertCustomerCustomer_QNAME = new QName("http://tempuri.org/", "customer");
    private final static QName _GetTassoCambioValuta_QNAME = new QName("http://tempuri.org/", "valuta");
    private final static QName _GetGruppoVOResponseGetGruppoVOResult_QNAME = new QName("http://tempuri.org/", "GetGruppoVOResult");
    private final static QName _GetDispoByBarcodeResponseGetDispoByBarcodeResult_QNAME = new QName("http://tempuri.org/", "GetDispoByBarcodeResult");
    private final static QName _GetSottoSottoCategorieResponseGetSottoSottoCategorieResult_QNAME = new QName("http://tempuri.org/", "GetSottoSottoCategorieResult");
    private final static QName _GetArticoliFlatExtLocaleByDateLanguage_QNAME = new QName("http://tempuri.org/", "language");
    private final static QName _GetSottoCategorieResponseGetSottoCategorieResult_QNAME = new QName("http://tempuri.org/", "GetSottoCategorieResult");
    private final static QName _GetArticoliLocaleResponseGetArticoliLocaleResult_QNAME = new QName("http://tempuri.org/", "GetArticoliLocaleResult");
    private final static QName _GetDispoByBarcodeCdMagazzino_QNAME = new QName("http://tempuri.org/", "cdMagazzino");
    private final static QName _GetDispoByBarcodeBarCode_QNAME = new QName("http://tempuri.org/", "barCode");
    private final static QName _GetItemClassesResponseGetItemClassesResult_QNAME = new QName("http://tempuri.org/", "GetItemClassesResult");
    private final static QName _GetItemLinesResponseGetItemLinesResult_QNAME = new QName("http://tempuri.org/", "GetItemLinesResult");
    private final static QName _InsertOrderItalistResponseInsertOrderItalistResult_QNAME = new QName("http://tempuri.org/", "InsertOrderItalistResult");
    private final static QName _GetTaglieResponseGetTaglieResult_QNAME = new QName("http://tempuri.org/", "GetTaglieResult");
    private final static QName _GetColoriResponseGetColoriResult_QNAME = new QName("http://tempuri.org/", "GetColoriResult");
    private final static QName _InsertCustomerResponseInsertCustomerResult_QNAME = new QName("http://tempuri.org/", "InsertCustomerResult");
    private final static QName _GetCategorieResponseGetCategorieResult_QNAME = new QName("http://tempuri.org/", "GetCategorieResult");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.tempuri
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetSottoSottoCategorieResponse }
     * 
     */
    public GetSottoSottoCategorieResponse createGetSottoSottoCategorieResponse() {
        return new GetSottoSottoCategorieResponse();
    }

    /**
     * Create an instance of {@link GetSottoSottoGruppoResponse }
     * 
     */
    public GetSottoSottoGruppoResponse createGetSottoSottoGruppoResponse() {
        return new GetSottoSottoGruppoResponse();
    }

    /**
     * Create an instance of {@link GetArticoliFlatExtLocaleByDateResponse }
     * 
     */
    public GetArticoliFlatExtLocaleByDateResponse createGetArticoliFlatExtLocaleByDateResponse() {
        return new GetArticoliFlatExtLocaleByDateResponse();
    }

    /**
     * Create an instance of {@link GetTaglie }
     * 
     */
    public GetTaglie createGetTaglie() {
        return new GetTaglie();
    }

    /**
     * Create an instance of {@link ExportItalistResponse }
     * 
     */
    public ExportItalistResponse createExportItalistResponse() {
        return new ExportItalistResponse();
    }

    /**
     * Create an instance of {@link GetNazioni }
     * 
     */
    public GetNazioni createGetNazioni() {
        return new GetNazioni();
    }

    /**
     * Create an instance of {@link GetRelationsClassiSClassiResponse }
     * 
     */
    public GetRelationsClassiSClassiResponse createGetRelationsClassiSClassiResponse() {
        return new GetRelationsClassiSClassiResponse();
    }

    /**
     * Create an instance of {@link GetArticoliFlatExtByDateResponse }
     * 
     */
    public GetArticoliFlatExtByDateResponse createGetArticoliFlatExtByDateResponse() {
        return new GetArticoliFlatExtByDateResponse();
    }

    /**
     * Create an instance of {@link InsertCustomer }
     * 
     */
    public InsertCustomer createInsertCustomer() {
        return new InsertCustomer();
    }

    /**
     * Create an instance of {@link GetRelationsLineeClassi }
     * 
     */
    public GetRelationsLineeClassi createGetRelationsLineeClassi() {
        return new GetRelationsLineeClassi();
    }

    /**
     * Create an instance of {@link InsertOrder }
     * 
     */
    public InsertOrder createInsertOrder() {
        return new InsertOrder();
    }

    /**
     * Create an instance of {@link GetSottoGruppo }
     * 
     */
    public GetSottoGruppo createGetSottoGruppo() {
        return new GetSottoGruppo();
    }

    /**
     * Create an instance of {@link GetItemLines }
     * 
     */
    public GetItemLines createGetItemLines() {
        return new GetItemLines();
    }

    /**
     * Create an instance of {@link GetArticoliFlat }
     * 
     */
    public GetArticoliFlat createGetArticoliFlat() {
        return new GetArticoliFlat();
    }

    /**
     * Create an instance of {@link GetCategorieResponse }
     * 
     */
    public GetCategorieResponse createGetCategorieResponse() {
        return new GetCategorieResponse();
    }

    /**
     * Create an instance of {@link GetRelationsSClassiSSClassi }
     * 
     */
    public GetRelationsSClassiSSClassi createGetRelationsSClassiSSClassi() {
        return new GetRelationsSClassiSSClassi();
    }

    /**
     * Create an instance of {@link GetGruppoVOResponse }
     * 
     */
    public GetGruppoVOResponse createGetGruppoVOResponse() {
        return new GetGruppoVOResponse();
    }

    /**
     * Create an instance of {@link GetDispoByBarcode }
     * 
     */
    public GetDispoByBarcode createGetDispoByBarcode() {
        return new GetDispoByBarcode();
    }

    /**
     * Create an instance of {@link GetCategorie }
     * 
     */
    public GetCategorie createGetCategorie() {
        return new GetCategorie();
    }

    /**
     * Create an instance of {@link GetArticoliLocaleResponse }
     * 
     */
    public GetArticoliLocaleResponse createGetArticoliLocaleResponse() {
        return new GetArticoliLocaleResponse();
    }

    /**
     * Create an instance of {@link InsertCustomerResponse }
     * 
     */
    public InsertCustomerResponse createInsertCustomerResponse() {
        return new InsertCustomerResponse();
    }

    /**
     * Create an instance of {@link GetSottoSottoCategorie }
     * 
     */
    public GetSottoSottoCategorie createGetSottoSottoCategorie() {
        return new GetSottoSottoCategorie();
    }

    /**
     * Create an instance of {@link GetSottoCategorie }
     * 
     */
    public GetSottoCategorie createGetSottoCategorie() {
        return new GetSottoCategorie();
    }

    /**
     * Create an instance of {@link GetRelationsClassiSClassi }
     * 
     */
    public GetRelationsClassiSClassi createGetRelationsClassiSClassi() {
        return new GetRelationsClassiSClassi();
    }

    /**
     * Create an instance of {@link GetCustomerResponse }
     * 
     */
    public GetCustomerResponse createGetCustomerResponse() {
        return new GetCustomerResponse();
    }

    /**
     * Create an instance of {@link GetTaglieResponse }
     * 
     */
    public GetTaglieResponse createGetTaglieResponse() {
        return new GetTaglieResponse();
    }

    /**
     * Create an instance of {@link InsertOrderItalistResponse }
     * 
     */
    public InsertOrderItalistResponse createInsertOrderItalistResponse() {
        return new InsertOrderItalistResponse();
    }

    /**
     * Create an instance of {@link GetRelationsLineeClassiResponse }
     * 
     */
    public GetRelationsLineeClassiResponse createGetRelationsLineeClassiResponse() {
        return new GetRelationsLineeClassiResponse();
    }

    /**
     * Create an instance of {@link GetNazioniResponse }
     * 
     */
    public GetNazioniResponse createGetNazioniResponse() {
        return new GetNazioniResponse();
    }

    /**
     * Create an instance of {@link GetLastRecordCount }
     * 
     */
    public GetLastRecordCount createGetLastRecordCount() {
        return new GetLastRecordCount();
    }

    /**
     * Create an instance of {@link GetTassoCambio }
     * 
     */
    public GetTassoCambio createGetTassoCambio() {
        return new GetTassoCambio();
    }

    /**
     * Create an instance of {@link GetLastRecordCountResponse }
     * 
     */
    public GetLastRecordCountResponse createGetLastRecordCountResponse() {
        return new GetLastRecordCountResponse();
    }

    /**
     * Create an instance of {@link GetCustomer }
     * 
     */
    public GetCustomer createGetCustomer() {
        return new GetCustomer();
    }

    /**
     * Create an instance of {@link GetItemClassesResponse }
     * 
     */
    public GetItemClassesResponse createGetItemClassesResponse() {
        return new GetItemClassesResponse();
    }

    /**
     * Create an instance of {@link GetItemClasses }
     * 
     */
    public GetItemClasses createGetItemClasses() {
        return new GetItemClasses();
    }

    /**
     * Create an instance of {@link GetArticoliLocale }
     * 
     */
    public GetArticoliLocale createGetArticoliLocale() {
        return new GetArticoliLocale();
    }

    /**
     * Create an instance of {@link GetArticoliFlatExtLocaleByDate }
     * 
     */
    public GetArticoliFlatExtLocaleByDate createGetArticoliFlatExtLocaleByDate() {
        return new GetArticoliFlatExtLocaleByDate();
    }

    /**
     * Create an instance of {@link GetDispoByBarcodeResponse }
     * 
     */
    public GetDispoByBarcodeResponse createGetDispoByBarcodeResponse() {
        return new GetDispoByBarcodeResponse();
    }

    /**
     * Create an instance of {@link ExportItalist }
     * 
     */
    public ExportItalist createExportItalist() {
        return new ExportItalist();
    }

    /**
     * Create an instance of {@link GetRelationsSClassiSSClassiResponse }
     * 
     */
    public GetRelationsSClassiSSClassiResponse createGetRelationsSClassiSSClassiResponse() {
        return new GetRelationsSClassiSSClassiResponse();
    }

    /**
     * Create an instance of {@link GetPagedCustomer }
     * 
     */
    public GetPagedCustomer createGetPagedCustomer() {
        return new GetPagedCustomer();
    }

    /**
     * Create an instance of {@link GetSottoSottoGruppo }
     * 
     */
    public GetSottoSottoGruppo createGetSottoSottoGruppo() {
        return new GetSottoSottoGruppo();
    }

    /**
     * Create an instance of {@link InsertOrderItalist }
     * 
     */
    public InsertOrderItalist createInsertOrderItalist() {
        return new InsertOrderItalist();
    }

    /**
     * Create an instance of {@link GetTassoCambioResponse }
     * 
     */
    public GetTassoCambioResponse createGetTassoCambioResponse() {
        return new GetTassoCambioResponse();
    }

    /**
     * Create an instance of {@link InsertOrderResponse }
     * 
     */
    public InsertOrderResponse createInsertOrderResponse() {
        return new InsertOrderResponse();
    }

    /**
     * Create an instance of {@link GetPagedCustomerResponse }
     * 
     */
    public GetPagedCustomerResponse createGetPagedCustomerResponse() {
        return new GetPagedCustomerResponse();
    }

    /**
     * Create an instance of {@link GetSottoGruppoResponse }
     * 
     */
    public GetSottoGruppoResponse createGetSottoGruppoResponse() {
        return new GetSottoGruppoResponse();
    }

    /**
     * Create an instance of {@link GetColori }
     * 
     */
    public GetColori createGetColori() {
        return new GetColori();
    }

    /**
     * Create an instance of {@link GetSottoCategorieResponse }
     * 
     */
    public GetSottoCategorieResponse createGetSottoCategorieResponse() {
        return new GetSottoCategorieResponse();
    }

    /**
     * Create an instance of {@link GetItemLinesResponse }
     * 
     */
    public GetItemLinesResponse createGetItemLinesResponse() {
        return new GetItemLinesResponse();
    }

    /**
     * Create an instance of {@link GetArticoliFlatExtByDate }
     * 
     */
    public GetArticoliFlatExtByDate createGetArticoliFlatExtByDate() {
        return new GetArticoliFlatExtByDate();
    }

    /**
     * Create an instance of {@link GetGruppoVO }
     * 
     */
    public GetGruppoVO createGetGruppoVO() {
        return new GetGruppoVO();
    }

    /**
     * Create an instance of {@link GetArticoliFlatResponse }
     * 
     */
    public GetArticoliFlatResponse createGetArticoliFlatResponse() {
        return new GetArticoliFlatResponse();
    }

    /**
     * Create an instance of {@link GetColoriResponse }
     * 
     */
    public GetColoriResponse createGetColoriResponse() {
        return new GetColoriResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetSottoCategorie.class)
    public JAXBElement<String> createGetSottoCategorieUsr(String value) {
        return new JAXBElement<String>(_GetSottoCategorieUsr_QNAME, String.class, GetSottoCategorie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetSottoCategorie.class)
    public JAXBElement<String> createGetSottoCategoriePwd(String value) {
        return new JAXBElement<String>(_GetSottoCategoriePwd_QNAME, String.class, GetSottoCategorie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DataPageVOOfAnagraficaVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetPagedCustomerResult", scope = GetPagedCustomerResponse.class)
    public JAXBElement<DataPageVOOfAnagraficaVO> createGetPagedCustomerResponseGetPagedCustomerResult(DataPageVOOfAnagraficaVO value) {
        return new JAXBElement<DataPageVOOfAnagraficaVO>(_GetPagedCustomerResponseGetPagedCustomerResult_QNAME, DataPageVOOfAnagraficaVO.class, GetPagedCustomerResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = InsertOrder.class)
    public JAXBElement<String> createInsertOrderUsr(String value) {
        return new JAXBElement<String>(_GetSottoCategorieUsr_QNAME, String.class, InsertOrder.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DocumentoVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "order", scope = InsertOrder.class)
    public JAXBElement<DocumentoVO> createInsertOrderOrder(DocumentoVO value) {
        return new JAXBElement<DocumentoVO>(_InsertOrderOrder_QNAME, DocumentoVO.class, InsertOrder.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = InsertOrder.class)
    public JAXBElement<String> createInsertOrderPwd(String value) {
        return new JAXBElement<String>(_GetSottoCategoriePwd_QNAME, String.class, InsertOrder.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetColori.class)
    public JAXBElement<String> createGetColoriUsr(String value) {
        return new JAXBElement<String>(_GetSottoCategorieUsr_QNAME, String.class, GetColori.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetColori.class)
    public JAXBElement<String> createGetColoriPwd(String value) {
        return new JAXBElement<String>(_GetSottoCategoriePwd_QNAME, String.class, GetColori.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfArticoloFlatVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetArticoliFlatResult", scope = GetArticoliFlatResponse.class)
    public JAXBElement<ArrayOfArticoloFlatVO> createGetArticoliFlatResponseGetArticoliFlatResult(ArrayOfArticoloFlatVO value) {
        return new JAXBElement<ArrayOfArticoloFlatVO>(_GetArticoliFlatResponseGetArticoliFlatResult_QNAME, ArrayOfArticoloFlatVO.class, GetArticoliFlatResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetSottoSottoGruppo.class)
    public JAXBElement<String> createGetSottoSottoGruppoUsr(String value) {
        return new JAXBElement<String>(_GetSottoCategorieUsr_QNAME, String.class, GetSottoSottoGruppo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetSottoSottoGruppo.class)
    public JAXBElement<String> createGetSottoSottoGruppoPwd(String value) {
        return new JAXBElement<String>(_GetSottoCategoriePwd_QNAME, String.class, GetSottoSottoGruppo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfArticoloFlatExtVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetArticoliFlatExtByDateResult", scope = GetArticoliFlatExtByDateResponse.class)
    public JAXBElement<ArrayOfArticoloFlatExtVO> createGetArticoliFlatExtByDateResponseGetArticoliFlatExtByDateResult(ArrayOfArticoloFlatExtVO value) {
        return new JAXBElement<ArrayOfArticoloFlatExtVO>(_GetArticoliFlatExtByDateResponseGetArticoliFlatExtByDateResult_QNAME, ArrayOfArticoloFlatExtVO.class, GetArticoliFlatExtByDateResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfArticoloFlatExtLocaleVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetArticoliFlatExtLocaleByDateResult", scope = GetArticoliFlatExtLocaleByDateResponse.class)
    public JAXBElement<ArrayOfArticoloFlatExtLocaleVO> createGetArticoliFlatExtLocaleByDateResponseGetArticoliFlatExtLocaleByDateResult(ArrayOfArticoloFlatExtLocaleVO value) {
        return new JAXBElement<ArrayOfArticoloFlatExtLocaleVO>(_GetArticoliFlatExtLocaleByDateResponseGetArticoliFlatExtLocaleByDateResult_QNAME, ArrayOfArticoloFlatExtLocaleVO.class, GetArticoliFlatExtLocaleByDateResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfSottoGruppoVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetSottoGruppoResult", scope = GetSottoGruppoResponse.class)
    public JAXBElement<ArrayOfSottoGruppoVO> createGetSottoGruppoResponseGetSottoGruppoResult(ArrayOfSottoGruppoVO value) {
        return new JAXBElement<ArrayOfSottoGruppoVO>(_GetSottoGruppoResponseGetSottoGruppoResult_QNAME, ArrayOfSottoGruppoVO.class, GetSottoGruppoResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRELSClassiSSCLassiVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetRelationsSClassiSSClassiResult", scope = GetRelationsSClassiSSClassiResponse.class)
    public JAXBElement<ArrayOfRELSClassiSSCLassiVO> createGetRelationsSClassiSSClassiResponseGetRelationsSClassiSSClassiResult(ArrayOfRELSClassiSSCLassiVO value) {
        return new JAXBElement<ArrayOfRELSClassiSSCLassiVO>(_GetRelationsSClassiSSClassiResponseGetRelationsSClassiSSClassiResult_QNAME, ArrayOfRELSClassiSSCLassiVO.class, GetRelationsSClassiSSClassiResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfAnagraficaVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetCustomerResult", scope = GetCustomerResponse.class)
    public JAXBElement<ArrayOfAnagraficaVO> createGetCustomerResponseGetCustomerResult(ArrayOfAnagraficaVO value) {
        return new JAXBElement<ArrayOfAnagraficaVO>(_GetCustomerResponseGetCustomerResult_QNAME, ArrayOfAnagraficaVO.class, GetCustomerResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetCustomer.class)
    public JAXBElement<String> createGetCustomerUsr(String value) {
        return new JAXBElement<String>(_GetSottoCategorieUsr_QNAME, String.class, GetCustomer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "id", scope = GetCustomer.class)
    public JAXBElement<String> createGetCustomerId(String value) {
        return new JAXBElement<String>(_GetCustomerId_QNAME, String.class, GetCustomer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "nome", scope = GetCustomer.class)
    public JAXBElement<String> createGetCustomerNome(String value) {
        return new JAXBElement<String>(_GetCustomerNome_QNAME, String.class, GetCustomer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetCustomer.class)
    public JAXBElement<String> createGetCustomerPwd(String value) {
        return new JAXBElement<String>(_GetSottoCategoriePwd_QNAME, String.class, GetCustomer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetTaglie.class)
    public JAXBElement<String> createGetTaglieUsr(String value) {
        return new JAXBElement<String>(_GetSottoCategorieUsr_QNAME, String.class, GetTaglie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetTaglie.class)
    public JAXBElement<String> createGetTagliePwd(String value) {
        return new JAXBElement<String>(_GetSottoCategoriePwd_QNAME, String.class, GetTaglie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRelLineeClassiVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetRelationsLineeClassiResult", scope = GetRelationsLineeClassiResponse.class)
    public JAXBElement<ArrayOfRelLineeClassiVO> createGetRelationsLineeClassiResponseGetRelationsLineeClassiResult(ArrayOfRelLineeClassiVO value) {
        return new JAXBElement<ArrayOfRelLineeClassiVO>(_GetRelationsLineeClassiResponseGetRelationsLineeClassiResult_QNAME, ArrayOfRelLineeClassiVO.class, GetRelationsLineeClassiResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "filter", scope = GetArticoliFlatExtByDate.class)
    public JAXBElement<String> createGetArticoliFlatExtByDateFilter(String value) {
        return new JAXBElement<String>(_GetArticoliFlatExtByDateFilter_QNAME, String.class, GetArticoliFlatExtByDate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetArticoliFlatExtByDate.class)
    public JAXBElement<String> createGetArticoliFlatExtByDateUsr(String value) {
        return new JAXBElement<String>(_GetSottoCategorieUsr_QNAME, String.class, GetArticoliFlatExtByDate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetArticoliFlatExtByDate.class)
    public JAXBElement<String> createGetArticoliFlatExtByDatePwd(String value) {
        return new JAXBElement<String>(_GetSottoCategoriePwd_QNAME, String.class, GetArticoliFlatExtByDate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetRelationsClassiSClassi.class)
    public JAXBElement<String> createGetRelationsClassiSClassiUsr(String value) {
        return new JAXBElement<String>(_GetSottoCategorieUsr_QNAME, String.class, GetRelationsClassiSClassi.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetRelationsClassiSClassi.class)
    public JAXBElement<String> createGetRelationsClassiSClassiPwd(String value) {
        return new JAXBElement<String>(_GetSottoCategoriePwd_QNAME, String.class, GetRelationsClassiSClassi.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetRelationsSClassiSSClassi.class)
    public JAXBElement<String> createGetRelationsSClassiSSClassiUsr(String value) {
        return new JAXBElement<String>(_GetSottoCategorieUsr_QNAME, String.class, GetRelationsSClassiSSClassi.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetRelationsSClassiSSClassi.class)
    public JAXBElement<String> createGetRelationsSClassiSSClassiPwd(String value) {
        return new JAXBElement<String>(_GetSottoCategoriePwd_QNAME, String.class, GetRelationsSClassiSSClassi.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetPagedCustomer.class)
    public JAXBElement<String> createGetPagedCustomerUsr(String value) {
        return new JAXBElement<String>(_GetSottoCategorieUsr_QNAME, String.class, GetPagedCustomer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "id", scope = GetPagedCustomer.class)
    public JAXBElement<String> createGetPagedCustomerId(String value) {
        return new JAXBElement<String>(_GetCustomerId_QNAME, String.class, GetPagedCustomer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "nome", scope = GetPagedCustomer.class)
    public JAXBElement<String> createGetPagedCustomerNome(String value) {
        return new JAXBElement<String>(_GetCustomerNome_QNAME, String.class, GetPagedCustomer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetPagedCustomer.class)
    public JAXBElement<String> createGetPagedCustomerPwd(String value) {
        return new JAXBElement<String>(_GetSottoCategoriePwd_QNAME, String.class, GetPagedCustomer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "InsertOrderResult", scope = InsertOrderResponse.class)
    public JAXBElement<String> createInsertOrderResponseInsertOrderResult(String value) {
        return new JAXBElement<String>(_InsertOrderResponseInsertOrderResult_QNAME, String.class, InsertOrderResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfSottoSottoGruppoVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetSottoSottoGruppoResult", scope = GetSottoSottoGruppoResponse.class)
    public JAXBElement<ArrayOfSottoSottoGruppoVO> createGetSottoSottoGruppoResponseGetSottoSottoGruppoResult(ArrayOfSottoSottoGruppoVO value) {
        return new JAXBElement<ArrayOfSottoSottoGruppoVO>(_GetSottoSottoGruppoResponseGetSottoSottoGruppoResult_QNAME, ArrayOfSottoSottoGruppoVO.class, GetSottoSottoGruppoResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetSottoSottoCategorie.class)
    public JAXBElement<String> createGetSottoSottoCategorieUsr(String value) {
        return new JAXBElement<String>(_GetSottoCategorieUsr_QNAME, String.class, GetSottoSottoCategorie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetSottoSottoCategorie.class)
    public JAXBElement<String> createGetSottoSottoCategoriePwd(String value) {
        return new JAXBElement<String>(_GetSottoCategoriePwd_QNAME, String.class, GetSottoSottoCategorie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfNazioneVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetNazioniResult", scope = GetNazioniResponse.class)
    public JAXBElement<ArrayOfNazioneVO> createGetNazioniResponseGetNazioniResult(ArrayOfNazioneVO value) {
        return new JAXBElement<ArrayOfNazioneVO>(_GetNazioniResponseGetNazioniResult_QNAME, ArrayOfNazioneVO.class, GetNazioniResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRelClassiSClassiVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetRelationsClassiSClassiResult", scope = GetRelationsClassiSClassiResponse.class)
    public JAXBElement<ArrayOfRelClassiSClassiVO> createGetRelationsClassiSClassiResponseGetRelationsClassiSClassiResult(ArrayOfRelClassiSClassiVO value) {
        return new JAXBElement<ArrayOfRelClassiSClassiVO>(_GetRelationsClassiSClassiResponseGetRelationsClassiSClassiResult_QNAME, ArrayOfRelClassiSClassiVO.class, GetRelationsClassiSClassiResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = InsertCustomer.class)
    public JAXBElement<String> createInsertCustomerUsr(String value) {
        return new JAXBElement<String>(_GetSottoCategorieUsr_QNAME, String.class, InsertCustomer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AnagraficaVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "customer", scope = InsertCustomer.class)
    public JAXBElement<AnagraficaVO> createInsertCustomerCustomer(AnagraficaVO value) {
        return new JAXBElement<AnagraficaVO>(_InsertCustomerCustomer_QNAME, AnagraficaVO.class, InsertCustomer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = InsertCustomer.class)
    public JAXBElement<String> createInsertCustomerPwd(String value) {
        return new JAXBElement<String>(_GetSottoCategoriePwd_QNAME, String.class, InsertCustomer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "valuta", scope = GetTassoCambio.class)
    public JAXBElement<String> createGetTassoCambioValuta(String value) {
        return new JAXBElement<String>(_GetTassoCambioValuta_QNAME, String.class, GetTassoCambio.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfGruppoVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetGruppoVOResult", scope = GetGruppoVOResponse.class)
    public JAXBElement<ArrayOfGruppoVO> createGetGruppoVOResponseGetGruppoVOResult(ArrayOfGruppoVO value) {
        return new JAXBElement<ArrayOfGruppoVO>(_GetGruppoVOResponseGetGruppoVOResult_QNAME, ArrayOfGruppoVO.class, GetGruppoVOResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MgDispo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetDispoByBarcodeResult", scope = GetDispoByBarcodeResponse.class)
    public JAXBElement<MgDispo> createGetDispoByBarcodeResponseGetDispoByBarcodeResult(MgDispo value) {
        return new JAXBElement<MgDispo>(_GetDispoByBarcodeResponseGetDispoByBarcodeResult_QNAME, MgDispo.class, GetDispoByBarcodeResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfSottoSottoCategoriaVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetSottoSottoCategorieResult", scope = GetSottoSottoCategorieResponse.class)
    public JAXBElement<ArrayOfSottoSottoCategoriaVO> createGetSottoSottoCategorieResponseGetSottoSottoCategorieResult(ArrayOfSottoSottoCategoriaVO value) {
        return new JAXBElement<ArrayOfSottoSottoCategoriaVO>(_GetSottoSottoCategorieResponseGetSottoSottoCategorieResult_QNAME, ArrayOfSottoSottoCategoriaVO.class, GetSottoSottoCategorieResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetArticoliFlat.class)
    public JAXBElement<String> createGetArticoliFlatUsr(String value) {
        return new JAXBElement<String>(_GetSottoCategorieUsr_QNAME, String.class, GetArticoliFlat.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetArticoliFlat.class)
    public JAXBElement<String> createGetArticoliFlatPwd(String value) {
        return new JAXBElement<String>(_GetSottoCategoriePwd_QNAME, String.class, GetArticoliFlat.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "filter", scope = GetArticoliFlatExtLocaleByDate.class)
    public JAXBElement<String> createGetArticoliFlatExtLocaleByDateFilter(String value) {
        return new JAXBElement<String>(_GetArticoliFlatExtByDateFilter_QNAME, String.class, GetArticoliFlatExtLocaleByDate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetArticoliFlatExtLocaleByDate.class)
    public JAXBElement<String> createGetArticoliFlatExtLocaleByDateUsr(String value) {
        return new JAXBElement<String>(_GetSottoCategorieUsr_QNAME, String.class, GetArticoliFlatExtLocaleByDate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "language", scope = GetArticoliFlatExtLocaleByDate.class)
    public JAXBElement<String> createGetArticoliFlatExtLocaleByDateLanguage(String value) {
        return new JAXBElement<String>(_GetArticoliFlatExtLocaleByDateLanguage_QNAME, String.class, GetArticoliFlatExtLocaleByDate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetArticoliFlatExtLocaleByDate.class)
    public JAXBElement<String> createGetArticoliFlatExtLocaleByDatePwd(String value) {
        return new JAXBElement<String>(_GetSottoCategoriePwd_QNAME, String.class, GetArticoliFlatExtLocaleByDate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetCategorie.class)
    public JAXBElement<String> createGetCategorieUsr(String value) {
        return new JAXBElement<String>(_GetSottoCategorieUsr_QNAME, String.class, GetCategorie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetCategorie.class)
    public JAXBElement<String> createGetCategoriePwd(String value) {
        return new JAXBElement<String>(_GetSottoCategoriePwd_QNAME, String.class, GetCategorie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = InsertOrderItalist.class)
    public JAXBElement<String> createInsertOrderItalistUsr(String value) {
        return new JAXBElement<String>(_GetSottoCategorieUsr_QNAME, String.class, InsertOrderItalist.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DocumentoVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "order", scope = InsertOrderItalist.class)
    public JAXBElement<DocumentoVO> createInsertOrderItalistOrder(DocumentoVO value) {
        return new JAXBElement<DocumentoVO>(_InsertOrderOrder_QNAME, DocumentoVO.class, InsertOrderItalist.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = InsertOrderItalist.class)
    public JAXBElement<String> createInsertOrderItalistPwd(String value) {
        return new JAXBElement<String>(_GetSottoCategoriePwd_QNAME, String.class, InsertOrderItalist.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfSottoCategoriaVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetSottoCategorieResult", scope = GetSottoCategorieResponse.class)
    public JAXBElement<ArrayOfSottoCategoriaVO> createGetSottoCategorieResponseGetSottoCategorieResult(ArrayOfSottoCategoriaVO value) {
        return new JAXBElement<ArrayOfSottoCategoriaVO>(_GetSottoCategorieResponseGetSottoCategorieResult_QNAME, ArrayOfSottoCategoriaVO.class, GetSottoCategorieResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetItemClasses.class)
    public JAXBElement<String> createGetItemClassesUsr(String value) {
        return new JAXBElement<String>(_GetSottoCategorieUsr_QNAME, String.class, GetItemClasses.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetItemClasses.class)
    public JAXBElement<String> createGetItemClassesPwd(String value) {
        return new JAXBElement<String>(_GetSottoCategoriePwd_QNAME, String.class, GetItemClasses.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfArticoloLocaleVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetArticoliLocaleResult", scope = GetArticoliLocaleResponse.class)
    public JAXBElement<ArrayOfArticoloLocaleVO> createGetArticoliLocaleResponseGetArticoliLocaleResult(ArrayOfArticoloLocaleVO value) {
        return new JAXBElement<ArrayOfArticoloLocaleVO>(_GetArticoliLocaleResponseGetArticoliLocaleResult_QNAME, ArrayOfArticoloLocaleVO.class, GetArticoliLocaleResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetDispoByBarcode.class)
    public JAXBElement<String> createGetDispoByBarcodeUsr(String value) {
        return new JAXBElement<String>(_GetSottoCategorieUsr_QNAME, String.class, GetDispoByBarcode.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "cdMagazzino", scope = GetDispoByBarcode.class)
    public JAXBElement<String> createGetDispoByBarcodeCdMagazzino(String value) {
        return new JAXBElement<String>(_GetDispoByBarcodeCdMagazzino_QNAME, String.class, GetDispoByBarcode.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "barCode", scope = GetDispoByBarcode.class)
    public JAXBElement<String> createGetDispoByBarcodeBarCode(String value) {
        return new JAXBElement<String>(_GetDispoByBarcodeBarCode_QNAME, String.class, GetDispoByBarcode.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetDispoByBarcode.class)
    public JAXBElement<String> createGetDispoByBarcodePwd(String value) {
        return new JAXBElement<String>(_GetSottoCategoriePwd_QNAME, String.class, GetDispoByBarcode.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetGruppoVO.class)
    public JAXBElement<String> createGetGruppoVOUsr(String value) {
        return new JAXBElement<String>(_GetSottoCategorieUsr_QNAME, String.class, GetGruppoVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetGruppoVO.class)
    public JAXBElement<String> createGetGruppoVOPwd(String value) {
        return new JAXBElement<String>(_GetSottoCategoriePwd_QNAME, String.class, GetGruppoVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetNazioni.class)
    public JAXBElement<String> createGetNazioniUsr(String value) {
        return new JAXBElement<String>(_GetSottoCategorieUsr_QNAME, String.class, GetNazioni.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetNazioni.class)
    public JAXBElement<String> createGetNazioniPwd(String value) {
        return new JAXBElement<String>(_GetSottoCategoriePwd_QNAME, String.class, GetNazioni.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfLineaVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetItemClassesResult", scope = GetItemClassesResponse.class)
    public JAXBElement<ArrayOfLineaVO> createGetItemClassesResponseGetItemClassesResult(ArrayOfLineaVO value) {
        return new JAXBElement<ArrayOfLineaVO>(_GetItemClassesResponseGetItemClassesResult_QNAME, ArrayOfLineaVO.class, GetItemClassesResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfClasseVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetItemLinesResult", scope = GetItemLinesResponse.class)
    public JAXBElement<ArrayOfClasseVO> createGetItemLinesResponseGetItemLinesResult(ArrayOfClasseVO value) {
        return new JAXBElement<ArrayOfClasseVO>(_GetItemLinesResponseGetItemLinesResult_QNAME, ArrayOfClasseVO.class, GetItemLinesResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "InsertOrderItalistResult", scope = InsertOrderItalistResponse.class)
    public JAXBElement<String> createInsertOrderItalistResponseInsertOrderItalistResult(String value) {
        return new JAXBElement<String>(_InsertOrderItalistResponseInsertOrderItalistResult_QNAME, String.class, InsertOrderItalistResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfTagliaVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetTaglieResult", scope = GetTaglieResponse.class)
    public JAXBElement<ArrayOfTagliaVO> createGetTaglieResponseGetTaglieResult(ArrayOfTagliaVO value) {
        return new JAXBElement<ArrayOfTagliaVO>(_GetTaglieResponseGetTaglieResult_QNAME, ArrayOfTagliaVO.class, GetTaglieResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfColoreVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetColoriResult", scope = GetColoriResponse.class)
    public JAXBElement<ArrayOfColoreVO> createGetColoriResponseGetColoriResult(ArrayOfColoreVO value) {
        return new JAXBElement<ArrayOfColoreVO>(_GetColoriResponseGetColoriResult_QNAME, ArrayOfColoreVO.class, GetColoriResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "InsertCustomerResult", scope = InsertCustomerResponse.class)
    public JAXBElement<String> createInsertCustomerResponseInsertCustomerResult(String value) {
        return new JAXBElement<String>(_InsertCustomerResponseInsertCustomerResult_QNAME, String.class, InsertCustomerResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetSottoGruppo.class)
    public JAXBElement<String> createGetSottoGruppoUsr(String value) {
        return new JAXBElement<String>(_GetSottoCategorieUsr_QNAME, String.class, GetSottoGruppo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetSottoGruppo.class)
    public JAXBElement<String> createGetSottoGruppoPwd(String value) {
        return new JAXBElement<String>(_GetSottoCategoriePwd_QNAME, String.class, GetSottoGruppo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfCategoriaVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetCategorieResult", scope = GetCategorieResponse.class)
    public JAXBElement<ArrayOfCategoriaVO> createGetCategorieResponseGetCategorieResult(ArrayOfCategoriaVO value) {
        return new JAXBElement<ArrayOfCategoriaVO>(_GetCategorieResponseGetCategorieResult_QNAME, ArrayOfCategoriaVO.class, GetCategorieResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetItemLines.class)
    public JAXBElement<String> createGetItemLinesUsr(String value) {
        return new JAXBElement<String>(_GetSottoCategorieUsr_QNAME, String.class, GetItemLines.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetItemLines.class)
    public JAXBElement<String> createGetItemLinesPwd(String value) {
        return new JAXBElement<String>(_GetSottoCategoriePwd_QNAME, String.class, GetItemLines.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetRelationsLineeClassi.class)
    public JAXBElement<String> createGetRelationsLineeClassiUsr(String value) {
        return new JAXBElement<String>(_GetSottoCategorieUsr_QNAME, String.class, GetRelationsLineeClassi.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetRelationsLineeClassi.class)
    public JAXBElement<String> createGetRelationsLineeClassiPwd(String value) {
        return new JAXBElement<String>(_GetSottoCategoriePwd_QNAME, String.class, GetRelationsLineeClassi.class, value);
    }

}
