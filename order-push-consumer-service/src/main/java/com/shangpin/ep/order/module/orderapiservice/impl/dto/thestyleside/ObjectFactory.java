
package com.shangpin.ep.order.module.orderapiservice.impl.dto.thestyleside;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.shangpin.ep.order.module.orderapiservice.impl.dto.thestyleside package. 
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

    private final static QName _ArrayOfArticoloFlatVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ArrayOfArticoloFlatVO");
    private final static QName _MgDispo_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "MgDispo");
    private final static QName _CategoriaVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "CategoriaVO");
    private final static QName _ArrayOfSottoCategoriaVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ArrayOfSottoCategoriaVO");
    private final static QName _ArrayOfSottoGruppoVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ArrayOfSottoGruppoVO");
    private final static QName _TagliaVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "TagliaVO");
    private final static QName _SottoSottoGruppoVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "SottoSottoGruppoVO");
    private final static QName _LineaVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "LineaVO");
    private final static QName _TrackingStatus_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", "TrackingStatus");
    private final static QName _Duration_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "duration");
    private final static QName _DocumentoShortVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "DocumentoShortVO");
    private final static QName _ArrayOfDocumentoRigaVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ArrayOfDocumentoRigaVO");
    private final static QName _ArticoloLocaleVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ArticoloLocaleVO");
    private final static QName _ArrayOfstatusDetails_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", "ArrayOfstatusDetails");
    private final static QName _ArrayOfstring_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/Arrays", "ArrayOfstring");
    private final static QName _ArrayOfSottoSottoCategoriaVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ArrayOfSottoSottoCategoriaVO");
    private final static QName _Long_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "long");
    private final static QName _MgDispoArrayElement_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "MgDispoArrayElement");
    private final static QName _DateTime_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "dateTime");
    private final static QName _String_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "string");
    private final static QName _ListinoVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ListinoVO");
    private final static QName _ArrayOfColoreVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ArrayOfColoreVO");
    private final static QName _GruppoVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "GruppoVO");
    private final static QName _ArrayOfSottoSottoGruppoVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ArrayOfSottoSottoGruppoVO");
    private final static QName _UnsignedInt_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedInt");
    private final static QName _Char_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "char");
    private final static QName _Short_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "short");
    private final static QName _ClasseVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ClasseVO");
    private final static QName _ArrayOfArticoloFlatExtVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ArrayOfArticoloFlatExtVO");
    private final static QName _ArticoloFlatVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ArticoloFlatVO");
    private final static QName _Boolean_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "boolean");
    private final static QName _ArrayOfDocumentoShortVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ArrayOfDocumentoShortVO");
    private final static QName _DocumentoRigaVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "DocumentoRigaVO");
    private final static QName _RELSClassiSSCLassiVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "RELSClassiSSCLassiVO");
    private final static QName _RelTipiArticoliTaglieVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "RelTipiArticoliTaglieVO");
    private final static QName _ArrayOfAnagraficaVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ArrayOfAnagraficaVO");
    private final static QName _StatusDetails_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", "statusDetails");
    private final static QName _ReceiverDetails_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", "receiverDetails");
    private final static QName _ArrayOfTipoPagamentoVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ArrayOfTipoPagamentoVO");
    private final static QName _ArticoloFlatExtLocaleVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ArticoloFlatExtLocaleVO");
    private final static QName _Int_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "int");
    private final static QName _RelClassiSClassiVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "RelClassiSClassiVO");
    private final static QName _SottoSottoCategoriaVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "SottoSottoCategoriaVO");
    private final static QName _AnagraficaVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "AnagraficaVO");
    private final static QName _ArrayOfClasseVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ArrayOfClasseVO");
    private final static QName _DocumentoEvasioneVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "DocumentoEvasioneVO");
    private final static QName _QName_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "QName");
    private final static QName _ArrayOfArticoloFlatExtLocaleVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ArrayOfArticoloFlatExtLocaleVO");
    private final static QName _ArrayOfRelLineeClassiVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ArrayOfRelLineeClassiVO");
    private final static QName _UnsignedLong_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedLong");
    private final static QName _ArrayOfCategoriaVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ArrayOfCategoriaVO");
    private final static QName _UnsignedByte_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedByte");
    private final static QName _ArrayOfRelTipiArticoliTaglieVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ArrayOfRelTipiArticoliTaglieVO");
    private final static QName _UnsignedShort_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "unsignedShort");
    private final static QName _ArticoloFlatExtVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ArticoloFlatExtVO");
    private final static QName _SottoGruppoVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "SottoGruppoVO");
    private final static QName _ArrayOfRelAgentiAnagraficheVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ArrayOfRelAgentiAnagraficheVO");
    private final static QName _ArrayOfListinoVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ArrayOfListinoVO");
    private final static QName _TrackingInfo_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", "TrackingInfo");
    private final static QName _DocumentoVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "DocumentoVO");
    private final static QName _Float_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "float");
    private final static QName _DataPageVOOfAnagraficaVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "DataPageVOOfAnagraficaVO");
    private final static QName _ArrayOfTagliaVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ArrayOfTagliaVO");
    private final static QName _ArrayOfRELSClassiSSCLassiVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ArrayOfRELSClassiSSCLassiVO");
    private final static QName _AnyType_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "anyType");
    private final static QName _GenericTabledEntityVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "GenericTabledEntityVO");
    private final static QName _Guid_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "guid");
    private final static QName _SottoCategoriaVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "SottoCategoriaVO");
    private final static QName _SenderDetails_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", "senderDetails");
    private final static QName _ArrayOfGruppoVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ArrayOfGruppoVO");
    private final static QName _Decimal_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "decimal");
    private final static QName _ColoreVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ColoreVO");
    private final static QName _ArrayOfLineaVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ArrayOfLineaVO");
    private final static QName _ConsigmentDetails_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", "consigmentDetails");
    private final static QName _ArrayOfArticoloLocaleVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ArrayOfArticoloLocaleVO");
    private final static QName _Base64Binary_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "base64Binary");
    private final static QName _RelAgentiAnagraficheVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "RelAgentiAnagraficheVO");
    private final static QName _TipoPagamentoVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "TipoPagamentoVO");
    private final static QName _AnyURI_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "anyURI");
    private final static QName _ArrayOfMgDispoArrayElement_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ArrayOfMgDispoArrayElement");
    private final static QName _ArrayOfTrackingInfo_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", "ArrayOfTrackingInfo");
    private final static QName _ArrayOfNazioneVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ArrayOfNazioneVO");
    private final static QName _ArrayOfRelClassiSClassiVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ArrayOfRelClassiSClassiVO");
    private final static QName _Byte_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "byte");
    private final static QName _Double_QNAME = new QName("http://schemas.microsoft.com/2003/10/Serialization/", "double");
    private final static QName _RelLineeClassiVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "RelLineeClassiVO");
    private final static QName _NazioneVO_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "NazioneVO");
    private final static QName _GetArticoliFlatUsr_QNAME = new QName("http://tempuri.org/", "usr");
    private final static QName _GetArticoliFlatPwd_QNAME = new QName("http://tempuri.org/", "pwd");
    private final static QName _GenericTabledEntityVOId_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Id");
    private final static QName _GenericTabledEntityVODescrizione_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Descrizione");
    private final static QName _TrackingInfoConsigmentNumber_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", "ConsigmentNumber");
    private final static QName _TrackingInfoCollectionName_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", "CollectionName");
    private final static QName _TrackingInfoConsigmentDetails_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", "ConsigmentDetails");
    private final static QName _TrackingInfoSenderDetails_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", "SenderDetails");
    private final static QName _TrackingInfoOriginDepot_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", "OriginDepot");
    private final static QName _TrackingInfoDeliveryDate_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", "DeliveryDate");
    private final static QName _TrackingInfoReceiverDetails_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", "ReceiverDetails");
    private final static QName _TrackingInfoStatusDetails_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", "StatusDetails");
    private final static QName _ConsigmentDetailsSenderReference_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", "SenderReference");
    private final static QName _ConsigmentDetailsItemNumber_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", "ItemNumber");
    private final static QName _GetModelsByQueryResponseGetModelsByQueryResult_QNAME = new QName("http://tempuri.org/", "GetModelsByQueryResult");
    private final static QName _GetCustomerId_QNAME = new QName("http://tempuri.org/", "id");
    private final static QName _GetCustomerNome_QNAME = new QName("http://tempuri.org/", "nome");
    private final static QName _GetTaglieResponseGetTaglieResult_QNAME = new QName("http://tempuri.org/", "GetTaglieResult");
    private final static QName _GetRelationsLineeClassiResponseGetRelationsLineeClassiResult_QNAME = new QName("http://tempuri.org/", "GetRelationsLineeClassiResult");
    private final static QName _InsertOrderResponseInsertOrderResult_QNAME = new QName("http://tempuri.org/", "InsertOrderResult");
    private final static QName _ReceiverDetailsDestinationTown_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", "DestinationTown");
    private final static QName _ReceiverDetailsDestinationCountry_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", "DestinationCountry");
    private final static QName _ReceiverDetailsDestinationDepot_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", "DestinationDepot");
    private final static QName _GetDispoByModelCodeModelCode_QNAME = new QName("http://tempuri.org/", "ModelCode");
    private final static QName _GetDispoByModelCodeCdMagazzino_QNAME = new QName("http://tempuri.org/", "cdMagazzino");
    private final static QName _GetArticoliLocaleResponseGetArticoliLocaleResult_QNAME = new QName("http://tempuri.org/", "GetArticoliLocaleResult");
    private final static QName _GetNazioniResponseGetNazioniResult_QNAME = new QName("http://tempuri.org/", "GetNazioniResult");
    private final static QName _ArticoloFlatExtVOExtendedNote1_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ExtendedNote1");
    private final static QName _ArticoloFlatExtVOItemClass_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ItemClass");
    private final static QName _ArticoloFlatExtVOItemLine_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ItemLine");
    private final static QName _ArticoloFlatExtVOQuantita_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Quantita");
    private final static QName _ArticoloFlatExtVOExtendedNote_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ExtendedNote");
    private final static QName _ArticoloFlatExtVOListini_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Listini");
    private final static QName _ArticoloFlatExtVOImageURL_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ImageURL");
    private final static QName _AnagraficaVOVATNumber_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "VATNumber");
    private final static QName _AnagraficaVOAddress_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Address");
    private final static QName _AnagraficaVOTelephone_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Telephone");
    private final static QName _AnagraficaVOZIPCode_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ZIPCode");
    private final static QName _AnagraficaVOCity_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "City");
    private final static QName _AnagraficaVODeliveryArea_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "DeliveryArea");
    private final static QName _AnagraficaVODeliveryAddress_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "DeliveryAddress");
    private final static QName _AnagraficaVOName_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Name");
    private final static QName _AnagraficaVOIDState_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "IDState");
    private final static QName _AnagraficaVOArea_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Area");
    private final static QName _AnagraficaVODeliveryIDState_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "DeliveryIDState");
    private final static QName _AnagraficaVODeliveryName_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "DeliveryName");
    private final static QName _AnagraficaVOCFnumber_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "CFnumber");
    private final static QName _AnagraficaVONote_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Note");
    private final static QName _AnagraficaVODeliveryCity_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "DeliveryCity");
    private final static QName _AnagraficaVOID_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ID");
    private final static QName _AnagraficaVODeliveryZIPCode_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "DeliveryZIPCode");
    private final static QName _AnagraficaVOIDListinoCliente_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "IDListinoCliente");
    private final static QName _AnagraficaVOEmail_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "email");
    private final static QName _AnagraficaVOFax_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Fax");
    private final static QName _GetArticoliFlatExtLocaleByDateResponseGetArticoliFlatExtLocaleByDateResult_QNAME = new QName("http://tempuri.org/", "GetArticoliFlatExtLocaleByDateResult");
    private final static QName _GetGruppoVOResponseGetGruppoVOResult_QNAME = new QName("http://tempuri.org/", "GetGruppoVOResult");
    private final static QName _GetItemClassesResponseGetItemClassesResult_QNAME = new QName("http://tempuri.org/", "GetItemClassesResult");
    private final static QName _DocumentoVODiscount_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Discount");
    private final static QName _DocumentoVOTracking_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Tracking");
    private final static QName _DocumentoVOIDDocumentoRif_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "IDDocumentoRif");
    private final static QName _DocumentoVOIdTipoDocumento_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "IdTipoDocumento");
    private final static QName _DocumentoVOCustomer_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Customer");
    private final static QName _DocumentoVOOrderNumber_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "OrderNumber");
    private final static QName _DocumentoVOIDLetteraVettura_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "IDLetteraVettura");
    private final static QName _DocumentoVOTipiPagamento_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "TipiPagamento");
    private final static QName _DocumentoVONumber_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Number");
    private final static QName _DocumentoVORighe_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Righe");
    private final static QName _DocumentoVOTipoPagamento_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "TipoPagamento");
    private final static QName _DocumentoVOIDUser_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "IDUser");
    private final static QName _DocumentoVOPriceListCode_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "PriceListCode");
    private final static QName _RelTipiArticoliTaglieVOIDTipoArticolo_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "IDTipoArticolo");
    private final static QName _RelTipiArticoliTaglieVOIDTaglia_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "IDTaglia");
    private final static QName _GetModelsByQueryView_QNAME = new QName("http://tempuri.org/", "view");
    private final static QName _GetSottoSottoGruppoResponseGetSottoSottoGruppoResult_QNAME = new QName("http://tempuri.org/", "GetSottoSottoGruppoResult");
    private final static QName _GetRelationsSClassiSSClassiResponseGetRelationsSClassiSSClassiResult_QNAME = new QName("http://tempuri.org/", "GetRelationsSClassiSSClassiResult");
    private final static QName _GetColoriResponseGetColoriResult_QNAME = new QName("http://tempuri.org/", "GetColoriResult");
    private final static QName _GetPagedCustomerResponseGetPagedCustomerResult_QNAME = new QName("http://tempuri.org/", "GetPagedCustomerResult");
    private final static QName _GetNextDocumentNumberDocumentType_QNAME = new QName("http://tempuri.org/", "documentType");
    private final static QName _GetTrackingResponseGetTrackingResult_QNAME = new QName("http://tempuri.org/", "GetTrackingResult");
    private final static QName _GetArticoliFlatExtByDateResponseGetArticoliFlatExtByDateResult_QNAME = new QName("http://tempuri.org/", "GetArticoliFlatExtByDateResult");
    private final static QName _RelAgentiAnagraficheVOIDAnagrafica_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "IDAnagrafica");
    private final static QName _RelAgentiAnagraficheVOIDAgente_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "IDAgente");
    private final static QName _GetNextDocumentNumberResponseGetNextDocumentNumberResult_QNAME = new QName("http://tempuri.org/", "GetNextDocumentNumberResult");
    private final static QName _GetDispoByBarcodeResponseGetDispoByBarcodeResult_QNAME = new QName("http://tempuri.org/", "GetDispoByBarcodeResult");
    private final static QName _GetTrackingIdDocumento_QNAME = new QName("http://tempuri.org/", "IdDocumento");
    private final static QName _InsertOrderOrder_QNAME = new QName("http://tempuri.org/", "order");
    private final static QName _RelLineeClassiVOIDLineaArticolo_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "IDLineaArticolo");
    private final static QName _RelLineeClassiVOIDClasseArticolo_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "IDClasseArticolo");
    private final static QName _RELSClassiSSCLassiVOIDSSClasseArticolo_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "IDSSClasseArticolo");
    private final static QName _RELSClassiSSCLassiVOIDSClasseArticolo_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "IDSClasseArticolo");
    private final static QName _ColoreVOColourPicker_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ColourPicker");
    private final static QName _MgDispoArrayElementBarCode_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "BarCode");
    private final static QName _GetOrdersResponseGetOrdersResult_QNAME = new QName("http://tempuri.org/", "GetOrdersResult");
    private final static QName _GetCategorieResponseGetCategorieResult_QNAME = new QName("http://tempuri.org/", "GetCategorieResult");
    private final static QName _GetItemLinesResponseGetItemLinesResult_QNAME = new QName("http://tempuri.org/", "GetItemLinesResult");
    private final static QName _GetRelationsClassiSClassiResponseGetRelationsClassiSClassiResult_QNAME = new QName("http://tempuri.org/", "GetRelationsClassiSClassiResult");
    private final static QName _InsertCustomerResponseInsertCustomerResult_QNAME = new QName("http://tempuri.org/", "InsertCustomerResult");
    private final static QName _GetOrdersIdAnagrafica_QNAME = new QName("http://tempuri.org/", "idAnagrafica");
    private final static QName _GetTassoCambioValuta_QNAME = new QName("http://tempuri.org/", "valuta");
    private final static QName _StatusDetailsCarrierStatusCode_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", "CarrierStatusCode");
    private final static QName _StatusDetailsStatusDescription_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", "StatusDescription");
    private final static QName _StatusDetailsStatusDepot_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", "StatusDepot");
    private final static QName _StatusDetailsStatusDate_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", "StatusDate");
    private final static QName _GetSottoCategorieResponseGetSottoCategorieResult_QNAME = new QName("http://tempuri.org/", "GetSottoCategorieResult");
    private final static QName _GetSottoGruppoResponseGetSottoGruppoResult_QNAME = new QName("http://tempuri.org/", "GetSottoGruppoResult");
    private final static QName _GetSottoSottoCategorieResponseGetSottoSottoCategorieResult_QNAME = new QName("http://tempuri.org/", "GetSottoSottoCategorieResult");
    private final static QName _GetArticoliFlatExtByDateFilter_QNAME = new QName("http://tempuri.org/", "filter");
    private final static QName _ArticoloLocaleVODescription_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Description");
    private final static QName _ArticoloLocaleVODescrizioneAgg_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "DescrizioneAgg");
    private final static QName _ArticoloLocaleVOColor_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "color");
    private final static QName _ArticoloLocaleVOVisibility_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "visibility");
    private final static QName _ArticoloLocaleVOIdLocale_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "IdLocale");
    private final static QName _ArticoloLocaleVOSubdFabric_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "subd_fabric");
    private final static QName _ArticoloLocaleVOUrlKey_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "url_key");
    private final static QName _ArticoloLocaleVOSKU_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "SKU");
    private final static QName _ListinoVOSymbol_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "symbol");
    private final static QName _DocumentoRigaVOIDDocumentoContatore_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "IDDocumentoContatore");
    private final static QName _DocumentoRigaVOIDDocumento_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "IDDocumento");
    private final static QName _DocumentoRigaVOIDDocumentoContatoreRif_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "IDDocumentoContatoreRif");
    private final static QName _ArticoloFlatVOGroupName_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "GroupName");
    private final static QName _ArticoloFlatVOSize_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Size");
    private final static QName _ArticoloFlatVOModelCode_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ModelCode");
    private final static QName _ArticoloFlatVOShortDescription_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ShortDescription");
    private final static QName _ArticoloFlatVOSubSubCategory_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "SubSubCategory");
    private final static QName _ArticoloFlatVOColourWebID_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ColourWebID");
    private final static QName _ArticoloFlatVOLibero7Name_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Libero7Name");
    private final static QName _ArticoloFlatVOExtendedDescription_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ExtendedDescription");
    private final static QName _ArticoloFlatVOTextureID_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "TextureID");
    private final static QName _ArticoloFlatVOLibero8Name_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Libero8Name");
    private final static QName _ArticoloFlatVOSubSubGroup_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "SubSubGroup");
    private final static QName _ArticoloFlatVOEcommerce_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Ecommerce");
    private final static QName _ArticoloFlatVOLibero5Name_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Libero5Name");
    private final static QName _ArticoloFlatVOSubGroup_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "SubGroup");
    private final static QName _ArticoloFlatVOSubGroupName_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "SubGroupName");
    private final static QName _ArticoloFlatVOBrand_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Brand");
    private final static QName _ArticoloFlatVOSubCategoryName_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "SubCategoryName");
    private final static QName _ArticoloFlatVOLibero6Name_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Libero6Name");
    private final static QName _ArticoloFlatVOCategory_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Category");
    private final static QName _ArticoloFlatVOLibero2Name_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Libero2Name");
    private final static QName _ArticoloFlatVOGroup_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Group");
    private final static QName _ArticoloFlatVOLibero10Name_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Libero10Name");
    private final static QName _ArticoloFlatVOLibero1Name_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Libero1Name");
    private final static QName _ArticoloFlatVOColour_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Colour");
    private final static QName _ArticoloFlatVOTexture_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Texture");
    private final static QName _ArticoloFlatVOLibero3Name_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Libero3Name");
    private final static QName _ArticoloFlatVOSubSubCategoryName_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "SubSubCategoryName");
    private final static QName _ArticoloFlatVOSupplier_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Supplier");
    private final static QName _ArticoloFlatVOLibero4Name_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Libero4Name");
    private final static QName _ArticoloFlatVOLibero5_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Libero5");
    private final static QName _ArticoloFlatVOLibero6_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Libero6");
    private final static QName _ArticoloFlatVOLibero7_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Libero7");
    private final static QName _ArticoloFlatVOLibero9Name_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Libero9Name");
    private final static QName _ArticoloFlatVOLibero8_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Libero8");
    private final static QName _ArticoloFlatVOColourID_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ColourID");
    private final static QName _ArticoloFlatVOLibero9_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Libero9");
    private final static QName _ArticoloFlatVOSubSubGroupName_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "SubSubGroupName");
    private final static QName _ArticoloFlatVOLibero1_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Libero1");
    private final static QName _ArticoloFlatVOLibero2_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Libero2");
    private final static QName _ArticoloFlatVOLibero3_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Libero3");
    private final static QName _ArticoloFlatVOLibero4_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Libero4");
    private final static QName _ArticoloFlatVOColourWeb_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "ColourWeb");
    private final static QName _ArticoloFlatVOLibero10_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Libero10");
    private final static QName _ArticoloFlatVOCategoryName_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "CategoryName");
    private final static QName _ArticoloFlatVOSubCategory_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "SubCategory");
    private final static QName _ArticoloFlatVOSizeiD_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "SizeiD");
    private final static QName _GetRelTipiArticoliTaglieResponseGetRelTipiArticoliTaglieResult_QNAME = new QName("http://tempuri.org/", "GetRelTipiArticoliTaglieResult");
    private final static QName _GetArticoliFlatExtLocaleByDateLanguage_QNAME = new QName("http://tempuri.org/", "language");
    private final static QName _GetRelAgentiAnagraficheResponseGetRelAgentiAnagraficheResult_QNAME = new QName("http://tempuri.org/", "GetRelAgentiAnagraficheResult");
    private final static QName _GetCustomerResponseGetCustomerResult_QNAME = new QName("http://tempuri.org/", "GetCustomerResult");
    private final static QName _GetDispoByBarcodeBarCode_QNAME = new QName("http://tempuri.org/", "barCode");
    private final static QName _DocumentoShortVONumero_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Numero");
    private final static QName _DocumentoShortVOStatus_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Status");
    private final static QName _DocumentoShortVORagione_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Ragione");
    private final static QName _InsertCustomerCustomer_QNAME = new QName("http://tempuri.org/", "customer");
    private final static QName _GetDispoByModelCodeResponseGetDispoByModelCodeResult_QNAME = new QName("http://tempuri.org/", "GetDispoByModelCodeResult");
    private final static QName _GetArticoliFlatResponseGetArticoliFlatResult_QNAME = new QName("http://tempuri.org/", "GetArticoliFlatResult");
    private final static QName _SenderDetailsSendAccNo_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", "SendAccNo");
    private final static QName _SenderDetailsSendName_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", "SendName");
    private final static QName _ArticoloFlatExtLocaleVOLocales_QNAME = new QName("http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", "Locales");
    private final static QName _InsertOrderItalistResponseInsertOrderItalistResult_QNAME = new QName("http://tempuri.org/", "InsertOrderItalistResult");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.shangpin.ep.order.module.orderapiservice.impl.dto.thestyleside
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
     * Create an instance of {@link ArrayOfSottoSottoCategoriaVO }
     * 
     */
    public ArrayOfSottoSottoCategoriaVO createArrayOfSottoSottoCategoriaVO() {
        return new ArrayOfSottoSottoCategoriaVO();
    }

    /**
     * Create an instance of {@link GetSottoSottoGruppoResponse }
     * 
     */
    public GetSottoSottoGruppoResponse createGetSottoSottoGruppoResponse() {
        return new GetSottoSottoGruppoResponse();
    }

    /**
     * Create an instance of {@link ArrayOfSottoSottoGruppoVO }
     * 
     */
    public ArrayOfSottoSottoGruppoVO createArrayOfSottoSottoGruppoVO() {
        return new ArrayOfSottoSottoGruppoVO();
    }

    /**
     * Create an instance of {@link GetArticoliFlatExtLocaleByDateResponse }
     * 
     */
    public GetArticoliFlatExtLocaleByDateResponse createGetArticoliFlatExtLocaleByDateResponse() {
        return new GetArticoliFlatExtLocaleByDateResponse();
    }

    /**
     * Create an instance of {@link ArrayOfArticoloFlatExtLocaleVO }
     * 
     */
    public ArrayOfArticoloFlatExtLocaleVO createArrayOfArticoloFlatExtLocaleVO() {
        return new ArrayOfArticoloFlatExtLocaleVO();
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
     * Create an instance of {@link ArrayOfRelClassiSClassiVO }
     * 
     */
    public ArrayOfRelClassiSClassiVO createArrayOfRelClassiSClassiVO() {
        return new ArrayOfRelClassiSClassiVO();
    }

    /**
     * Create an instance of {@link GetRelAgentiAnagrafiche }
     * 
     */
    public GetRelAgentiAnagrafiche createGetRelAgentiAnagrafiche() {
        return new GetRelAgentiAnagrafiche();
    }

    /**
     * Create an instance of {@link GetArticoliFlatExtByDateResponse }
     * 
     */
    public GetArticoliFlatExtByDateResponse createGetArticoliFlatExtByDateResponse() {
        return new GetArticoliFlatExtByDateResponse();
    }

    /**
     * Create an instance of {@link ArrayOfArticoloFlatExtVO }
     * 
     */
    public ArrayOfArticoloFlatExtVO createArrayOfArticoloFlatExtVO() {
        return new ArrayOfArticoloFlatExtVO();
    }

    /**
     * Create an instance of {@link InsertCustomer }
     * 
     */
    public InsertCustomer createInsertCustomer() {
        return new InsertCustomer();
    }

    /**
     * Create an instance of {@link AnagraficaVO }
     * 
     */
    public AnagraficaVO createAnagraficaVO() {
        return new AnagraficaVO();
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
     * Create an instance of {@link DocumentoVO }
     * 
     */
    public DocumentoVO createDocumentoVO() {
        return new DocumentoVO();
    }

    /**
     * Create an instance of {@link GetDispoByModelCode }
     * 
     */
    public GetDispoByModelCode createGetDispoByModelCode() {
        return new GetDispoByModelCode();
    }

    /**
     * Create an instance of {@link GetSottoGruppo }
     * 
     */
    public GetSottoGruppo createGetSottoGruppo() {
        return new GetSottoGruppo();
    }

    /**
     * Create an instance of {@link GetModelsByQuery }
     * 
     */
    public GetModelsByQuery createGetModelsByQuery() {
        return new GetModelsByQuery();
    }

    /**
     * Create an instance of {@link GetItemLines }
     * 
     */
    public GetItemLines createGetItemLines() {
        return new GetItemLines();
    }

    /**
     * Create an instance of {@link GetOrdersResponse }
     * 
     */
    public GetOrdersResponse createGetOrdersResponse() {
        return new GetOrdersResponse();
    }

    /**
     * Create an instance of {@link ArrayOfDocumentoShortVO }
     * 
     */
    public ArrayOfDocumentoShortVO createArrayOfDocumentoShortVO() {
        return new ArrayOfDocumentoShortVO();
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
     * Create an instance of {@link ArrayOfCategoriaVO }
     * 
     */
    public ArrayOfCategoriaVO createArrayOfCategoriaVO() {
        return new ArrayOfCategoriaVO();
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
     * Create an instance of {@link ArrayOfGruppoVO }
     * 
     */
    public ArrayOfGruppoVO createArrayOfGruppoVO() {
        return new ArrayOfGruppoVO();
    }

    /**
     * Create an instance of {@link GetDispoByBarcode }
     * 
     */
    public GetDispoByBarcode createGetDispoByBarcode() {
        return new GetDispoByBarcode();
    }

    /**
     * Create an instance of {@link GetNextDocumentNumber }
     * 
     */
    public GetNextDocumentNumber createGetNextDocumentNumber() {
        return new GetNextDocumentNumber();
    }

    /**
     * Create an instance of {@link GetTrackingResponse }
     * 
     */
    public GetTrackingResponse createGetTrackingResponse() {
        return new GetTrackingResponse();
    }

    /**
     * Create an instance of {@link ArrayOfTrackingInfo }
     * 
     */
    public ArrayOfTrackingInfo createArrayOfTrackingInfo() {
        return new ArrayOfTrackingInfo();
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
     * Create an instance of {@link ArrayOfArticoloLocaleVO }
     * 
     */
    public ArrayOfArticoloLocaleVO createArrayOfArticoloLocaleVO() {
        return new ArrayOfArticoloLocaleVO();
    }

    /**
     * Create an instance of {@link GetDispoByModelCodeResponse }
     * 
     */
    public GetDispoByModelCodeResponse createGetDispoByModelCodeResponse() {
        return new GetDispoByModelCodeResponse();
    }

    /**
     * Create an instance of {@link ArrayOfMgDispoArrayElement }
     * 
     */
    public ArrayOfMgDispoArrayElement createArrayOfMgDispoArrayElement() {
        return new ArrayOfMgDispoArrayElement();
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
     * Create an instance of {@link ArrayOfAnagraficaVO }
     * 
     */
    public ArrayOfAnagraficaVO createArrayOfAnagraficaVO() {
        return new ArrayOfAnagraficaVO();
    }

    /**
     * Create an instance of {@link GetRelTipiArticoliTaglieResponse }
     * 
     */
    public GetRelTipiArticoliTaglieResponse createGetRelTipiArticoliTaglieResponse() {
        return new GetRelTipiArticoliTaglieResponse();
    }

    /**
     * Create an instance of {@link ArrayOfRelTipiArticoliTaglieVO }
     * 
     */
    public ArrayOfRelTipiArticoliTaglieVO createArrayOfRelTipiArticoliTaglieVO() {
        return new ArrayOfRelTipiArticoliTaglieVO();
    }

    /**
     * Create an instance of {@link GetTaglieResponse }
     * 
     */
    public GetTaglieResponse createGetTaglieResponse() {
        return new GetTaglieResponse();
    }

    /**
     * Create an instance of {@link ArrayOfTagliaVO }
     * 
     */
    public ArrayOfTagliaVO createArrayOfTagliaVO() {
        return new ArrayOfTagliaVO();
    }

    /**
     * Create an instance of {@link GetModelsByQueryResponse }
     * 
     */
    public GetModelsByQueryResponse createGetModelsByQueryResponse() {
        return new GetModelsByQueryResponse();
    }

    /**
     * Create an instance of {@link ArrayOfstring }
     * 
     */
    public ArrayOfstring createArrayOfstring() {
        return new ArrayOfstring();
    }

    /**
     * Create an instance of {@link InsertOrderItalistResponse }
     * 
     */
    public InsertOrderItalistResponse createInsertOrderItalistResponse() {
        return new InsertOrderItalistResponse();
    }

    /**
     * Create an instance of {@link GetNextDocumentNumberResponse }
     * 
     */
    public GetNextDocumentNumberResponse createGetNextDocumentNumberResponse() {
        return new GetNextDocumentNumberResponse();
    }

    /**
     * Create an instance of {@link GetRelationsLineeClassiResponse }
     * 
     */
    public GetRelationsLineeClassiResponse createGetRelationsLineeClassiResponse() {
        return new GetRelationsLineeClassiResponse();
    }

    /**
     * Create an instance of {@link ArrayOfRelLineeClassiVO }
     * 
     */
    public ArrayOfRelLineeClassiVO createArrayOfRelLineeClassiVO() {
        return new ArrayOfRelLineeClassiVO();
    }

    /**
     * Create an instance of {@link GetNazioniResponse }
     * 
     */
    public GetNazioniResponse createGetNazioniResponse() {
        return new GetNazioniResponse();
    }

    /**
     * Create an instance of {@link ArrayOfNazioneVO }
     * 
     */
    public ArrayOfNazioneVO createArrayOfNazioneVO() {
        return new ArrayOfNazioneVO();
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
     * Create an instance of {@link GetOrders }
     * 
     */
    public GetOrders createGetOrders() {
        return new GetOrders();
    }

    /**
     * Create an instance of {@link GetItemClassesResponse }
     * 
     */
    public GetItemClassesResponse createGetItemClassesResponse() {
        return new GetItemClassesResponse();
    }

    /**
     * Create an instance of {@link ArrayOfLineaVO }
     * 
     */
    public ArrayOfLineaVO createArrayOfLineaVO() {
        return new ArrayOfLineaVO();
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
     * Create an instance of {@link MgDispo }
     * 
     */
    public MgDispo createMgDispo() {
        return new MgDispo();
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
     * Create an instance of {@link ArrayOfRELSClassiSSCLassiVO }
     * 
     */
    public ArrayOfRELSClassiSSCLassiVO createArrayOfRELSClassiSSCLassiVO() {
        return new ArrayOfRELSClassiSSCLassiVO();
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
     * Create an instance of {@link DataPageVOOfAnagraficaVO }
     * 
     */
    public DataPageVOOfAnagraficaVO createDataPageVOOfAnagraficaVO() {
        return new DataPageVOOfAnagraficaVO();
    }

    /**
     * Create an instance of {@link GetRelAgentiAnagraficheResponse }
     * 
     */
    public GetRelAgentiAnagraficheResponse createGetRelAgentiAnagraficheResponse() {
        return new GetRelAgentiAnagraficheResponse();
    }

    /**
     * Create an instance of {@link ArrayOfRelAgentiAnagraficheVO }
     * 
     */
    public ArrayOfRelAgentiAnagraficheVO createArrayOfRelAgentiAnagraficheVO() {
        return new ArrayOfRelAgentiAnagraficheVO();
    }

    /**
     * Create an instance of {@link GetRelTipiArticoliTaglie }
     * 
     */
    public GetRelTipiArticoliTaglie createGetRelTipiArticoliTaglie() {
        return new GetRelTipiArticoliTaglie();
    }

    /**
     * Create an instance of {@link GetSottoGruppoResponse }
     * 
     */
    public GetSottoGruppoResponse createGetSottoGruppoResponse() {
        return new GetSottoGruppoResponse();
    }

    /**
     * Create an instance of {@link ArrayOfSottoGruppoVO }
     * 
     */
    public ArrayOfSottoGruppoVO createArrayOfSottoGruppoVO() {
        return new ArrayOfSottoGruppoVO();
    }

    /**
     * Create an instance of {@link GetTracking }
     * 
     */
    public GetTracking createGetTracking() {
        return new GetTracking();
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
     * Create an instance of {@link ArrayOfSottoCategoriaVO }
     * 
     */
    public ArrayOfSottoCategoriaVO createArrayOfSottoCategoriaVO() {
        return new ArrayOfSottoCategoriaVO();
    }

    /**
     * Create an instance of {@link GetItemLinesResponse }
     * 
     */
    public GetItemLinesResponse createGetItemLinesResponse() {
        return new GetItemLinesResponse();
    }

    /**
     * Create an instance of {@link ArrayOfClasseVO }
     * 
     */
    public ArrayOfClasseVO createArrayOfClasseVO() {
        return new ArrayOfClasseVO();
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
     * Create an instance of {@link ArrayOfArticoloFlatVO }
     * 
     */
    public ArrayOfArticoloFlatVO createArrayOfArticoloFlatVO() {
        return new ArrayOfArticoloFlatVO();
    }

    /**
     * Create an instance of {@link GetColoriResponse }
     * 
     */
    public GetColoriResponse createGetColoriResponse() {
        return new GetColoriResponse();
    }

    /**
     * Create an instance of {@link ArrayOfColoreVO }
     * 
     */
    public ArrayOfColoreVO createArrayOfColoreVO() {
        return new ArrayOfColoreVO();
    }

    /**
     * Create an instance of {@link LineaVO }
     * 
     */
    public LineaVO createLineaVO() {
        return new LineaVO();
    }

    /**
     * Create an instance of {@link TagliaVO }
     * 
     */
    public TagliaVO createTagliaVO() {
        return new TagliaVO();
    }

    /**
     * Create an instance of {@link SottoSottoGruppoVO }
     * 
     */
    public SottoSottoGruppoVO createSottoSottoGruppoVO() {
        return new SottoSottoGruppoVO();
    }

    /**
     * Create an instance of {@link CategoriaVO }
     * 
     */
    public CategoriaVO createCategoriaVO() {
        return new CategoriaVO();
    }

    /**
     * Create an instance of {@link DocumentoShortVO }
     * 
     */
    public DocumentoShortVO createDocumentoShortVO() {
        return new DocumentoShortVO();
    }

    /**
     * Create an instance of {@link ArticoloLocaleVO }
     * 
     */
    public ArticoloLocaleVO createArticoloLocaleVO() {
        return new ArticoloLocaleVO();
    }

    /**
     * Create an instance of {@link ArrayOfDocumentoRigaVO }
     * 
     */
    public ArrayOfDocumentoRigaVO createArrayOfDocumentoRigaVO() {
        return new ArrayOfDocumentoRigaVO();
    }

    /**
     * Create an instance of {@link MgDispoArrayElement }
     * 
     */
    public MgDispoArrayElement createMgDispoArrayElement() {
        return new MgDispoArrayElement();
    }

    /**
     * Create an instance of {@link GruppoVO }
     * 
     */
    public GruppoVO createGruppoVO() {
        return new GruppoVO();
    }

    /**
     * Create an instance of {@link ListinoVO }
     * 
     */
    public ListinoVO createListinoVO() {
        return new ListinoVO();
    }

    /**
     * Create an instance of {@link ClasseVO }
     * 
     */
    public ClasseVO createClasseVO() {
        return new ClasseVO();
    }

    /**
     * Create an instance of {@link ArticoloFlatVO }
     * 
     */
    public ArticoloFlatVO createArticoloFlatVO() {
        return new ArticoloFlatVO();
    }

    /**
     * Create an instance of {@link RELSClassiSSCLassiVO }
     * 
     */
    public RELSClassiSSCLassiVO createRELSClassiSSCLassiVO() {
        return new RELSClassiSSCLassiVO();
    }

    /**
     * Create an instance of {@link RelTipiArticoliTaglieVO }
     * 
     */
    public RelTipiArticoliTaglieVO createRelTipiArticoliTaglieVO() {
        return new RelTipiArticoliTaglieVO();
    }

    /**
     * Create an instance of {@link DocumentoRigaVO }
     * 
     */
    public DocumentoRigaVO createDocumentoRigaVO() {
        return new DocumentoRigaVO();
    }

    /**
     * Create an instance of {@link ArrayOfTipoPagamentoVO }
     * 
     */
    public ArrayOfTipoPagamentoVO createArrayOfTipoPagamentoVO() {
        return new ArrayOfTipoPagamentoVO();
    }

    /**
     * Create an instance of {@link RelClassiSClassiVO }
     * 
     */
    public RelClassiSClassiVO createRelClassiSClassiVO() {
        return new RelClassiSClassiVO();
    }

    /**
     * Create an instance of {@link ArticoloFlatExtLocaleVO }
     * 
     */
    public ArticoloFlatExtLocaleVO createArticoloFlatExtLocaleVO() {
        return new ArticoloFlatExtLocaleVO();
    }

    /**
     * Create an instance of {@link SottoSottoCategoriaVO }
     * 
     */
    public SottoSottoCategoriaVO createSottoSottoCategoriaVO() {
        return new SottoSottoCategoriaVO();
    }

    /**
     * Create an instance of {@link DocumentoEvasioneVO }
     * 
     */
    public DocumentoEvasioneVO createDocumentoEvasioneVO() {
        return new DocumentoEvasioneVO();
    }

    /**
     * Create an instance of {@link ArticoloFlatExtVO }
     * 
     */
    public ArticoloFlatExtVO createArticoloFlatExtVO() {
        return new ArticoloFlatExtVO();
    }

    /**
     * Create an instance of {@link SottoGruppoVO }
     * 
     */
    public SottoGruppoVO createSottoGruppoVO() {
        return new SottoGruppoVO();
    }

    /**
     * Create an instance of {@link ArrayOfListinoVO }
     * 
     */
    public ArrayOfListinoVO createArrayOfListinoVO() {
        return new ArrayOfListinoVO();
    }

    /**
     * Create an instance of {@link GenericTabledEntityVO }
     * 
     */
    public GenericTabledEntityVO createGenericTabledEntityVO() {
        return new GenericTabledEntityVO();
    }

    /**
     * Create an instance of {@link ColoreVO }
     * 
     */
    public ColoreVO createColoreVO() {
        return new ColoreVO();
    }

    /**
     * Create an instance of {@link SottoCategoriaVO }
     * 
     */
    public SottoCategoriaVO createSottoCategoriaVO() {
        return new SottoCategoriaVO();
    }

    /**
     * Create an instance of {@link TipoPagamentoVO }
     * 
     */
    public TipoPagamentoVO createTipoPagamentoVO() {
        return new TipoPagamentoVO();
    }

    /**
     * Create an instance of {@link RelAgentiAnagraficheVO }
     * 
     */
    public RelAgentiAnagraficheVO createRelAgentiAnagraficheVO() {
        return new RelAgentiAnagraficheVO();
    }

    /**
     * Create an instance of {@link NazioneVO }
     * 
     */
    public NazioneVO createNazioneVO() {
        return new NazioneVO();
    }

    /**
     * Create an instance of {@link RelLineeClassiVO }
     * 
     */
    public RelLineeClassiVO createRelLineeClassiVO() {
        return new RelLineeClassiVO();
    }

    /**
     * Create an instance of {@link SenderDetails }
     * 
     */
    public SenderDetails createSenderDetails() {
        return new SenderDetails();
    }

    /**
     * Create an instance of {@link ReceiverDetails }
     * 
     */
    public ReceiverDetails createReceiverDetails() {
        return new ReceiverDetails();
    }

    /**
     * Create an instance of {@link TrackingInfo }
     * 
     */
    public TrackingInfo createTrackingInfo() {
        return new TrackingInfo();
    }

    /**
     * Create an instance of {@link StatusDetails }
     * 
     */
    public StatusDetails createStatusDetails() {
        return new StatusDetails();
    }

    /**
     * Create an instance of {@link ArrayOfstatusDetails }
     * 
     */
    public ArrayOfstatusDetails createArrayOfstatusDetails() {
        return new ArrayOfstatusDetails();
    }

    /**
     * Create an instance of {@link ConsigmentDetails }
     * 
     */
    public ConsigmentDetails createConsigmentDetails() {
        return new ConsigmentDetails();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfArticoloFlatVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ArrayOfArticoloFlatVO")
    public JAXBElement<ArrayOfArticoloFlatVO> createArrayOfArticoloFlatVO(ArrayOfArticoloFlatVO value) {
        return new JAXBElement<ArrayOfArticoloFlatVO>(_ArrayOfArticoloFlatVO_QNAME, ArrayOfArticoloFlatVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MgDispo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "MgDispo")
    public JAXBElement<MgDispo> createMgDispo(MgDispo value) {
        return new JAXBElement<MgDispo>(_MgDispo_QNAME, MgDispo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CategoriaVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "CategoriaVO")
    public JAXBElement<CategoriaVO> createCategoriaVO(CategoriaVO value) {
        return new JAXBElement<CategoriaVO>(_CategoriaVO_QNAME, CategoriaVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfSottoCategoriaVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ArrayOfSottoCategoriaVO")
    public JAXBElement<ArrayOfSottoCategoriaVO> createArrayOfSottoCategoriaVO(ArrayOfSottoCategoriaVO value) {
        return new JAXBElement<ArrayOfSottoCategoriaVO>(_ArrayOfSottoCategoriaVO_QNAME, ArrayOfSottoCategoriaVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfSottoGruppoVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ArrayOfSottoGruppoVO")
    public JAXBElement<ArrayOfSottoGruppoVO> createArrayOfSottoGruppoVO(ArrayOfSottoGruppoVO value) {
        return new JAXBElement<ArrayOfSottoGruppoVO>(_ArrayOfSottoGruppoVO_QNAME, ArrayOfSottoGruppoVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TagliaVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "TagliaVO")
    public JAXBElement<TagliaVO> createTagliaVO(TagliaVO value) {
        return new JAXBElement<TagliaVO>(_TagliaVO_QNAME, TagliaVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SottoSottoGruppoVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "SottoSottoGruppoVO")
    public JAXBElement<SottoSottoGruppoVO> createSottoSottoGruppoVO(SottoSottoGruppoVO value) {
        return new JAXBElement<SottoSottoGruppoVO>(_SottoSottoGruppoVO_QNAME, SottoSottoGruppoVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LineaVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "LineaVO")
    public JAXBElement<LineaVO> createLineaVO(LineaVO value) {
        return new JAXBElement<LineaVO>(_LineaVO_QNAME, LineaVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TrackingStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", name = "TrackingStatus")
    public JAXBElement<TrackingStatus> createTrackingStatus(TrackingStatus value) {
        return new JAXBElement<TrackingStatus>(_TrackingStatus_QNAME, TrackingStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Duration }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "duration")
    public JAXBElement<Duration> createDuration(Duration value) {
        return new JAXBElement<Duration>(_Duration_QNAME, Duration.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DocumentoShortVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "DocumentoShortVO")
    public JAXBElement<DocumentoShortVO> createDocumentoShortVO(DocumentoShortVO value) {
        return new JAXBElement<DocumentoShortVO>(_DocumentoShortVO_QNAME, DocumentoShortVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfDocumentoRigaVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ArrayOfDocumentoRigaVO")
    public JAXBElement<ArrayOfDocumentoRigaVO> createArrayOfDocumentoRigaVO(ArrayOfDocumentoRigaVO value) {
        return new JAXBElement<ArrayOfDocumentoRigaVO>(_ArrayOfDocumentoRigaVO_QNAME, ArrayOfDocumentoRigaVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArticoloLocaleVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ArticoloLocaleVO")
    public JAXBElement<ArticoloLocaleVO> createArticoloLocaleVO(ArticoloLocaleVO value) {
        return new JAXBElement<ArticoloLocaleVO>(_ArticoloLocaleVO_QNAME, ArticoloLocaleVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfstatusDetails }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", name = "ArrayOfstatusDetails")
    public JAXBElement<ArrayOfstatusDetails> createArrayOfstatusDetails(ArrayOfstatusDetails value) {
        return new JAXBElement<ArrayOfstatusDetails>(_ArrayOfstatusDetails_QNAME, ArrayOfstatusDetails.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfstring }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/Arrays", name = "ArrayOfstring")
    public JAXBElement<ArrayOfstring> createArrayOfstring(ArrayOfstring value) {
        return new JAXBElement<ArrayOfstring>(_ArrayOfstring_QNAME, ArrayOfstring.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfSottoSottoCategoriaVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ArrayOfSottoSottoCategoriaVO")
    public JAXBElement<ArrayOfSottoSottoCategoriaVO> createArrayOfSottoSottoCategoriaVO(ArrayOfSottoSottoCategoriaVO value) {
        return new JAXBElement<ArrayOfSottoSottoCategoriaVO>(_ArrayOfSottoSottoCategoriaVO_QNAME, ArrayOfSottoSottoCategoriaVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "long")
    public JAXBElement<Long> createLong(Long value) {
        return new JAXBElement<Long>(_Long_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MgDispoArrayElement }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "MgDispoArrayElement")
    public JAXBElement<MgDispoArrayElement> createMgDispoArrayElement(MgDispoArrayElement value) {
        return new JAXBElement<MgDispoArrayElement>(_MgDispoArrayElement_QNAME, MgDispoArrayElement.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "dateTime")
    public JAXBElement<XMLGregorianCalendar> createDateTime(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_DateTime_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "string")
    public JAXBElement<String> createString(String value) {
        return new JAXBElement<String>(_String_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListinoVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ListinoVO")
    public JAXBElement<ListinoVO> createListinoVO(ListinoVO value) {
        return new JAXBElement<ListinoVO>(_ListinoVO_QNAME, ListinoVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfColoreVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ArrayOfColoreVO")
    public JAXBElement<ArrayOfColoreVO> createArrayOfColoreVO(ArrayOfColoreVO value) {
        return new JAXBElement<ArrayOfColoreVO>(_ArrayOfColoreVO_QNAME, ArrayOfColoreVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GruppoVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "GruppoVO")
    public JAXBElement<GruppoVO> createGruppoVO(GruppoVO value) {
        return new JAXBElement<GruppoVO>(_GruppoVO_QNAME, GruppoVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfSottoSottoGruppoVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ArrayOfSottoSottoGruppoVO")
    public JAXBElement<ArrayOfSottoSottoGruppoVO> createArrayOfSottoSottoGruppoVO(ArrayOfSottoSottoGruppoVO value) {
        return new JAXBElement<ArrayOfSottoSottoGruppoVO>(_ArrayOfSottoSottoGruppoVO_QNAME, ArrayOfSottoSottoGruppoVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Long }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedInt")
    public JAXBElement<Long> createUnsignedInt(Long value) {
        return new JAXBElement<Long>(_UnsignedInt_QNAME, Long.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "char")
    public JAXBElement<Integer> createChar(Integer value) {
        return new JAXBElement<Integer>(_Char_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Short }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "short")
    public JAXBElement<Short> createShort(Short value) {
        return new JAXBElement<Short>(_Short_QNAME, Short.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ClasseVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ClasseVO")
    public JAXBElement<ClasseVO> createClasseVO(ClasseVO value) {
        return new JAXBElement<ClasseVO>(_ClasseVO_QNAME, ClasseVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfArticoloFlatExtVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ArrayOfArticoloFlatExtVO")
    public JAXBElement<ArrayOfArticoloFlatExtVO> createArrayOfArticoloFlatExtVO(ArrayOfArticoloFlatExtVO value) {
        return new JAXBElement<ArrayOfArticoloFlatExtVO>(_ArrayOfArticoloFlatExtVO_QNAME, ArrayOfArticoloFlatExtVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArticoloFlatVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ArticoloFlatVO")
    public JAXBElement<ArticoloFlatVO> createArticoloFlatVO(ArticoloFlatVO value) {
        return new JAXBElement<ArticoloFlatVO>(_ArticoloFlatVO_QNAME, ArticoloFlatVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "boolean")
    public JAXBElement<Boolean> createBoolean(Boolean value) {
        return new JAXBElement<Boolean>(_Boolean_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfDocumentoShortVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ArrayOfDocumentoShortVO")
    public JAXBElement<ArrayOfDocumentoShortVO> createArrayOfDocumentoShortVO(ArrayOfDocumentoShortVO value) {
        return new JAXBElement<ArrayOfDocumentoShortVO>(_ArrayOfDocumentoShortVO_QNAME, ArrayOfDocumentoShortVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DocumentoRigaVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "DocumentoRigaVO")
    public JAXBElement<DocumentoRigaVO> createDocumentoRigaVO(DocumentoRigaVO value) {
        return new JAXBElement<DocumentoRigaVO>(_DocumentoRigaVO_QNAME, DocumentoRigaVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RELSClassiSSCLassiVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "RELSClassiSSCLassiVO")
    public JAXBElement<RELSClassiSSCLassiVO> createRELSClassiSSCLassiVO(RELSClassiSSCLassiVO value) {
        return new JAXBElement<RELSClassiSSCLassiVO>(_RELSClassiSSCLassiVO_QNAME, RELSClassiSSCLassiVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RelTipiArticoliTaglieVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "RelTipiArticoliTaglieVO")
    public JAXBElement<RelTipiArticoliTaglieVO> createRelTipiArticoliTaglieVO(RelTipiArticoliTaglieVO value) {
        return new JAXBElement<RelTipiArticoliTaglieVO>(_RelTipiArticoliTaglieVO_QNAME, RelTipiArticoliTaglieVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfAnagraficaVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ArrayOfAnagraficaVO")
    public JAXBElement<ArrayOfAnagraficaVO> createArrayOfAnagraficaVO(ArrayOfAnagraficaVO value) {
        return new JAXBElement<ArrayOfAnagraficaVO>(_ArrayOfAnagraficaVO_QNAME, ArrayOfAnagraficaVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StatusDetails }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", name = "statusDetails")
    public JAXBElement<StatusDetails> createStatusDetails(StatusDetails value) {
        return new JAXBElement<StatusDetails>(_StatusDetails_QNAME, StatusDetails.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReceiverDetails }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", name = "receiverDetails")
    public JAXBElement<ReceiverDetails> createReceiverDetails(ReceiverDetails value) {
        return new JAXBElement<ReceiverDetails>(_ReceiverDetails_QNAME, ReceiverDetails.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfTipoPagamentoVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ArrayOfTipoPagamentoVO")
    public JAXBElement<ArrayOfTipoPagamentoVO> createArrayOfTipoPagamentoVO(ArrayOfTipoPagamentoVO value) {
        return new JAXBElement<ArrayOfTipoPagamentoVO>(_ArrayOfTipoPagamentoVO_QNAME, ArrayOfTipoPagamentoVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArticoloFlatExtLocaleVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ArticoloFlatExtLocaleVO")
    public JAXBElement<ArticoloFlatExtLocaleVO> createArticoloFlatExtLocaleVO(ArticoloFlatExtLocaleVO value) {
        return new JAXBElement<ArticoloFlatExtLocaleVO>(_ArticoloFlatExtLocaleVO_QNAME, ArticoloFlatExtLocaleVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "int")
    public JAXBElement<Integer> createInt(Integer value) {
        return new JAXBElement<Integer>(_Int_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RelClassiSClassiVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "RelClassiSClassiVO")
    public JAXBElement<RelClassiSClassiVO> createRelClassiSClassiVO(RelClassiSClassiVO value) {
        return new JAXBElement<RelClassiSClassiVO>(_RelClassiSClassiVO_QNAME, RelClassiSClassiVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SottoSottoCategoriaVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "SottoSottoCategoriaVO")
    public JAXBElement<SottoSottoCategoriaVO> createSottoSottoCategoriaVO(SottoSottoCategoriaVO value) {
        return new JAXBElement<SottoSottoCategoriaVO>(_SottoSottoCategoriaVO_QNAME, SottoSottoCategoriaVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AnagraficaVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "AnagraficaVO")
    public JAXBElement<AnagraficaVO> createAnagraficaVO(AnagraficaVO value) {
        return new JAXBElement<AnagraficaVO>(_AnagraficaVO_QNAME, AnagraficaVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfClasseVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ArrayOfClasseVO")
    public JAXBElement<ArrayOfClasseVO> createArrayOfClasseVO(ArrayOfClasseVO value) {
        return new JAXBElement<ArrayOfClasseVO>(_ArrayOfClasseVO_QNAME, ArrayOfClasseVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DocumentoEvasioneVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "DocumentoEvasioneVO")
    public JAXBElement<DocumentoEvasioneVO> createDocumentoEvasioneVO(DocumentoEvasioneVO value) {
        return new JAXBElement<DocumentoEvasioneVO>(_DocumentoEvasioneVO_QNAME, DocumentoEvasioneVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link QName }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "QName")
    public JAXBElement<QName> createQName(QName value) {
        return new JAXBElement<QName>(_QName_QNAME, QName.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfArticoloFlatExtLocaleVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ArrayOfArticoloFlatExtLocaleVO")
    public JAXBElement<ArrayOfArticoloFlatExtLocaleVO> createArrayOfArticoloFlatExtLocaleVO(ArrayOfArticoloFlatExtLocaleVO value) {
        return new JAXBElement<ArrayOfArticoloFlatExtLocaleVO>(_ArrayOfArticoloFlatExtLocaleVO_QNAME, ArrayOfArticoloFlatExtLocaleVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRelLineeClassiVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ArrayOfRelLineeClassiVO")
    public JAXBElement<ArrayOfRelLineeClassiVO> createArrayOfRelLineeClassiVO(ArrayOfRelLineeClassiVO value) {
        return new JAXBElement<ArrayOfRelLineeClassiVO>(_ArrayOfRelLineeClassiVO_QNAME, ArrayOfRelLineeClassiVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedLong")
    public JAXBElement<BigInteger> createUnsignedLong(BigInteger value) {
        return new JAXBElement<BigInteger>(_UnsignedLong_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfCategoriaVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ArrayOfCategoriaVO")
    public JAXBElement<ArrayOfCategoriaVO> createArrayOfCategoriaVO(ArrayOfCategoriaVO value) {
        return new JAXBElement<ArrayOfCategoriaVO>(_ArrayOfCategoriaVO_QNAME, ArrayOfCategoriaVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Short }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedByte")
    public JAXBElement<Short> createUnsignedByte(Short value) {
        return new JAXBElement<Short>(_UnsignedByte_QNAME, Short.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRelTipiArticoliTaglieVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ArrayOfRelTipiArticoliTaglieVO")
    public JAXBElement<ArrayOfRelTipiArticoliTaglieVO> createArrayOfRelTipiArticoliTaglieVO(ArrayOfRelTipiArticoliTaglieVO value) {
        return new JAXBElement<ArrayOfRelTipiArticoliTaglieVO>(_ArrayOfRelTipiArticoliTaglieVO_QNAME, ArrayOfRelTipiArticoliTaglieVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "unsignedShort")
    public JAXBElement<Integer> createUnsignedShort(Integer value) {
        return new JAXBElement<Integer>(_UnsignedShort_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArticoloFlatExtVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ArticoloFlatExtVO")
    public JAXBElement<ArticoloFlatExtVO> createArticoloFlatExtVO(ArticoloFlatExtVO value) {
        return new JAXBElement<ArticoloFlatExtVO>(_ArticoloFlatExtVO_QNAME, ArticoloFlatExtVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SottoGruppoVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "SottoGruppoVO")
    public JAXBElement<SottoGruppoVO> createSottoGruppoVO(SottoGruppoVO value) {
        return new JAXBElement<SottoGruppoVO>(_SottoGruppoVO_QNAME, SottoGruppoVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRelAgentiAnagraficheVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ArrayOfRelAgentiAnagraficheVO")
    public JAXBElement<ArrayOfRelAgentiAnagraficheVO> createArrayOfRelAgentiAnagraficheVO(ArrayOfRelAgentiAnagraficheVO value) {
        return new JAXBElement<ArrayOfRelAgentiAnagraficheVO>(_ArrayOfRelAgentiAnagraficheVO_QNAME, ArrayOfRelAgentiAnagraficheVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfListinoVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ArrayOfListinoVO")
    public JAXBElement<ArrayOfListinoVO> createArrayOfListinoVO(ArrayOfListinoVO value) {
        return new JAXBElement<ArrayOfListinoVO>(_ArrayOfListinoVO_QNAME, ArrayOfListinoVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TrackingInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", name = "TrackingInfo")
    public JAXBElement<TrackingInfo> createTrackingInfo(TrackingInfo value) {
        return new JAXBElement<TrackingInfo>(_TrackingInfo_QNAME, TrackingInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DocumentoVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "DocumentoVO")
    public JAXBElement<DocumentoVO> createDocumentoVO(DocumentoVO value) {
        return new JAXBElement<DocumentoVO>(_DocumentoVO_QNAME, DocumentoVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Float }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "float")
    public JAXBElement<Float> createFloat(Float value) {
        return new JAXBElement<Float>(_Float_QNAME, Float.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DataPageVOOfAnagraficaVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "DataPageVOOfAnagraficaVO")
    public JAXBElement<DataPageVOOfAnagraficaVO> createDataPageVOOfAnagraficaVO(DataPageVOOfAnagraficaVO value) {
        return new JAXBElement<DataPageVOOfAnagraficaVO>(_DataPageVOOfAnagraficaVO_QNAME, DataPageVOOfAnagraficaVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfTagliaVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ArrayOfTagliaVO")
    public JAXBElement<ArrayOfTagliaVO> createArrayOfTagliaVO(ArrayOfTagliaVO value) {
        return new JAXBElement<ArrayOfTagliaVO>(_ArrayOfTagliaVO_QNAME, ArrayOfTagliaVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRELSClassiSSCLassiVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ArrayOfRELSClassiSSCLassiVO")
    public JAXBElement<ArrayOfRELSClassiSSCLassiVO> createArrayOfRELSClassiSSCLassiVO(ArrayOfRELSClassiSSCLassiVO value) {
        return new JAXBElement<ArrayOfRELSClassiSSCLassiVO>(_ArrayOfRELSClassiSSCLassiVO_QNAME, ArrayOfRELSClassiSSCLassiVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Object }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "anyType")
    public JAXBElement<Object> createAnyType(Object value) {
        return new JAXBElement<Object>(_AnyType_QNAME, Object.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenericTabledEntityVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "GenericTabledEntityVO")
    public JAXBElement<GenericTabledEntityVO> createGenericTabledEntityVO(GenericTabledEntityVO value) {
        return new JAXBElement<GenericTabledEntityVO>(_GenericTabledEntityVO_QNAME, GenericTabledEntityVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "guid")
    public JAXBElement<String> createGuid(String value) {
        return new JAXBElement<String>(_Guid_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SottoCategoriaVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "SottoCategoriaVO")
    public JAXBElement<SottoCategoriaVO> createSottoCategoriaVO(SottoCategoriaVO value) {
        return new JAXBElement<SottoCategoriaVO>(_SottoCategoriaVO_QNAME, SottoCategoriaVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SenderDetails }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", name = "senderDetails")
    public JAXBElement<SenderDetails> createSenderDetails(SenderDetails value) {
        return new JAXBElement<SenderDetails>(_SenderDetails_QNAME, SenderDetails.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfGruppoVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ArrayOfGruppoVO")
    public JAXBElement<ArrayOfGruppoVO> createArrayOfGruppoVO(ArrayOfGruppoVO value) {
        return new JAXBElement<ArrayOfGruppoVO>(_ArrayOfGruppoVO_QNAME, ArrayOfGruppoVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigDecimal }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "decimal")
    public JAXBElement<BigDecimal> createDecimal(BigDecimal value) {
        return new JAXBElement<BigDecimal>(_Decimal_QNAME, BigDecimal.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ColoreVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ColoreVO")
    public JAXBElement<ColoreVO> createColoreVO(ColoreVO value) {
        return new JAXBElement<ColoreVO>(_ColoreVO_QNAME, ColoreVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfLineaVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ArrayOfLineaVO")
    public JAXBElement<ArrayOfLineaVO> createArrayOfLineaVO(ArrayOfLineaVO value) {
        return new JAXBElement<ArrayOfLineaVO>(_ArrayOfLineaVO_QNAME, ArrayOfLineaVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsigmentDetails }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", name = "consigmentDetails")
    public JAXBElement<ConsigmentDetails> createConsigmentDetails(ConsigmentDetails value) {
        return new JAXBElement<ConsigmentDetails>(_ConsigmentDetails_QNAME, ConsigmentDetails.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfArticoloLocaleVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ArrayOfArticoloLocaleVO")
    public JAXBElement<ArrayOfArticoloLocaleVO> createArrayOfArticoloLocaleVO(ArrayOfArticoloLocaleVO value) {
        return new JAXBElement<ArrayOfArticoloLocaleVO>(_ArrayOfArticoloLocaleVO_QNAME, ArrayOfArticoloLocaleVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "base64Binary")
    public JAXBElement<byte[]> createBase64Binary(byte[] value) {
        return new JAXBElement<byte[]>(_Base64Binary_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RelAgentiAnagraficheVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "RelAgentiAnagraficheVO")
    public JAXBElement<RelAgentiAnagraficheVO> createRelAgentiAnagraficheVO(RelAgentiAnagraficheVO value) {
        return new JAXBElement<RelAgentiAnagraficheVO>(_RelAgentiAnagraficheVO_QNAME, RelAgentiAnagraficheVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link TipoPagamentoVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "TipoPagamentoVO")
    public JAXBElement<TipoPagamentoVO> createTipoPagamentoVO(TipoPagamentoVO value) {
        return new JAXBElement<TipoPagamentoVO>(_TipoPagamentoVO_QNAME, TipoPagamentoVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "anyURI")
    public JAXBElement<String> createAnyURI(String value) {
        return new JAXBElement<String>(_AnyURI_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfMgDispoArrayElement }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ArrayOfMgDispoArrayElement")
    public JAXBElement<ArrayOfMgDispoArrayElement> createArrayOfMgDispoArrayElement(ArrayOfMgDispoArrayElement value) {
        return new JAXBElement<ArrayOfMgDispoArrayElement>(_ArrayOfMgDispoArrayElement_QNAME, ArrayOfMgDispoArrayElement.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfTrackingInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", name = "ArrayOfTrackingInfo")
    public JAXBElement<ArrayOfTrackingInfo> createArrayOfTrackingInfo(ArrayOfTrackingInfo value) {
        return new JAXBElement<ArrayOfTrackingInfo>(_ArrayOfTrackingInfo_QNAME, ArrayOfTrackingInfo.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfNazioneVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ArrayOfNazioneVO")
    public JAXBElement<ArrayOfNazioneVO> createArrayOfNazioneVO(ArrayOfNazioneVO value) {
        return new JAXBElement<ArrayOfNazioneVO>(_ArrayOfNazioneVO_QNAME, ArrayOfNazioneVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRelClassiSClassiVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ArrayOfRelClassiSClassiVO")
    public JAXBElement<ArrayOfRelClassiSClassiVO> createArrayOfRelClassiSClassiVO(ArrayOfRelClassiSClassiVO value) {
        return new JAXBElement<ArrayOfRelClassiSClassiVO>(_ArrayOfRelClassiSClassiVO_QNAME, ArrayOfRelClassiSClassiVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Byte }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "byte")
    public JAXBElement<Byte> createByte(Byte value) {
        return new JAXBElement<Byte>(_Byte_QNAME, Byte.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Double }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.microsoft.com/2003/10/Serialization/", name = "double")
    public JAXBElement<Double> createDouble(Double value) {
        return new JAXBElement<Double>(_Double_QNAME, Double.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RelLineeClassiVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "RelLineeClassiVO")
    public JAXBElement<RelLineeClassiVO> createRelLineeClassiVO(RelLineeClassiVO value) {
        return new JAXBElement<RelLineeClassiVO>(_RelLineeClassiVO_QNAME, RelLineeClassiVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NazioneVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "NazioneVO")
    public JAXBElement<NazioneVO> createNazioneVO(NazioneVO value) {
        return new JAXBElement<NazioneVO>(_NazioneVO_QNAME, NazioneVO.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetArticoliFlat.class)
    public JAXBElement<String> createGetArticoliFlatUsr(String value) {
        return new JAXBElement<String>(_GetArticoliFlatUsr_QNAME, String.class, GetArticoliFlat.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetArticoliFlat.class)
    public JAXBElement<String> createGetArticoliFlatPwd(String value) {
        return new JAXBElement<String>(_GetArticoliFlatPwd_QNAME, String.class, GetArticoliFlat.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Id", scope = GenericTabledEntityVO.class)
    public JAXBElement<String> createGenericTabledEntityVOId(String value) {
        return new JAXBElement<String>(_GenericTabledEntityVOId_QNAME, String.class, GenericTabledEntityVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Descrizione", scope = GenericTabledEntityVO.class)
    public JAXBElement<String> createGenericTabledEntityVODescrizione(String value) {
        return new JAXBElement<String>(_GenericTabledEntityVODescrizione_QNAME, String.class, GenericTabledEntityVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetNazioni.class)
    public JAXBElement<String> createGetNazioniUsr(String value) {
        return new JAXBElement<String>(_GetArticoliFlatUsr_QNAME, String.class, GetNazioni.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetNazioni.class)
    public JAXBElement<String> createGetNazioniPwd(String value) {
        return new JAXBElement<String>(_GetArticoliFlatPwd_QNAME, String.class, GetNazioni.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", name = "ConsigmentNumber", scope = TrackingInfo.class)
    public JAXBElement<String> createTrackingInfoConsigmentNumber(String value) {
        return new JAXBElement<String>(_TrackingInfoConsigmentNumber_QNAME, String.class, TrackingInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", name = "CollectionName", scope = TrackingInfo.class)
    public JAXBElement<String> createTrackingInfoCollectionName(String value) {
        return new JAXBElement<String>(_TrackingInfoCollectionName_QNAME, String.class, TrackingInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ConsigmentDetails }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", name = "ConsigmentDetails", scope = TrackingInfo.class)
    public JAXBElement<ConsigmentDetails> createTrackingInfoConsigmentDetails(ConsigmentDetails value) {
        return new JAXBElement<ConsigmentDetails>(_TrackingInfoConsigmentDetails_QNAME, ConsigmentDetails.class, TrackingInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SenderDetails }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", name = "SenderDetails", scope = TrackingInfo.class)
    public JAXBElement<SenderDetails> createTrackingInfoSenderDetails(SenderDetails value) {
        return new JAXBElement<SenderDetails>(_TrackingInfoSenderDetails_QNAME, SenderDetails.class, TrackingInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", name = "OriginDepot", scope = TrackingInfo.class)
    public JAXBElement<String> createTrackingInfoOriginDepot(String value) {
        return new JAXBElement<String>(_TrackingInfoOriginDepot_QNAME, String.class, TrackingInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", name = "DeliveryDate", scope = TrackingInfo.class)
    public JAXBElement<String> createTrackingInfoDeliveryDate(String value) {
        return new JAXBElement<String>(_TrackingInfoDeliveryDate_QNAME, String.class, TrackingInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ReceiverDetails }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", name = "ReceiverDetails", scope = TrackingInfo.class)
    public JAXBElement<ReceiverDetails> createTrackingInfoReceiverDetails(ReceiverDetails value) {
        return new JAXBElement<ReceiverDetails>(_TrackingInfoReceiverDetails_QNAME, ReceiverDetails.class, TrackingInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfstatusDetails }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", name = "StatusDetails", scope = TrackingInfo.class)
    public JAXBElement<ArrayOfstatusDetails> createTrackingInfoStatusDetails(ArrayOfstatusDetails value) {
        return new JAXBElement<ArrayOfstatusDetails>(_TrackingInfoStatusDetails_QNAME, ArrayOfstatusDetails.class, TrackingInfo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", name = "SenderReference", scope = ConsigmentDetails.class)
    public JAXBElement<String> createConsigmentDetailsSenderReference(String value) {
        return new JAXBElement<String>(_ConsigmentDetailsSenderReference_QNAME, String.class, ConsigmentDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", name = "ItemNumber", scope = ConsigmentDetails.class)
    public JAXBElement<String> createConsigmentDetailsItemNumber(String value) {
        return new JAXBElement<String>(_ConsigmentDetailsItemNumber_QNAME, String.class, ConsigmentDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfstring }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetModelsByQueryResult", scope = GetModelsByQueryResponse.class)
    public JAXBElement<ArrayOfstring> createGetModelsByQueryResponseGetModelsByQueryResult(ArrayOfstring value) {
        return new JAXBElement<ArrayOfstring>(_GetModelsByQueryResponseGetModelsByQueryResult_QNAME, ArrayOfstring.class, GetModelsByQueryResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetCustomer.class)
    public JAXBElement<String> createGetCustomerUsr(String value) {
        return new JAXBElement<String>(_GetArticoliFlatUsr_QNAME, String.class, GetCustomer.class, value);
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
        return new JAXBElement<String>(_GetArticoliFlatPwd_QNAME, String.class, GetCustomer.class, value);
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
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetRelTipiArticoliTaglie.class)
    public JAXBElement<String> createGetRelTipiArticoliTaglieUsr(String value) {
        return new JAXBElement<String>(_GetArticoliFlatUsr_QNAME, String.class, GetRelTipiArticoliTaglie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetRelTipiArticoliTaglie.class)
    public JAXBElement<String> createGetRelTipiArticoliTagliePwd(String value) {
        return new JAXBElement<String>(_GetArticoliFlatPwd_QNAME, String.class, GetRelTipiArticoliTaglie.class, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", name = "DestinationTown", scope = ReceiverDetails.class)
    public JAXBElement<String> createReceiverDetailsDestinationTown(String value) {
        return new JAXBElement<String>(_ReceiverDetailsDestinationTown_QNAME, String.class, ReceiverDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", name = "DestinationCountry", scope = ReceiverDetails.class)
    public JAXBElement<String> createReceiverDetailsDestinationCountry(String value) {
        return new JAXBElement<String>(_ReceiverDetailsDestinationCountry_QNAME, String.class, ReceiverDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", name = "DestinationDepot", scope = ReceiverDetails.class)
    public JAXBElement<String> createReceiverDetailsDestinationDepot(String value) {
        return new JAXBElement<String>(_ReceiverDetailsDestinationDepot_QNAME, String.class, ReceiverDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetSottoSottoCategorie.class)
    public JAXBElement<String> createGetSottoSottoCategorieUsr(String value) {
        return new JAXBElement<String>(_GetArticoliFlatUsr_QNAME, String.class, GetSottoSottoCategorie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetSottoSottoCategorie.class)
    public JAXBElement<String> createGetSottoSottoCategoriePwd(String value) {
        return new JAXBElement<String>(_GetArticoliFlatPwd_QNAME, String.class, GetSottoSottoCategorie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetDispoByModelCode.class)
    public JAXBElement<String> createGetDispoByModelCodeUsr(String value) {
        return new JAXBElement<String>(_GetArticoliFlatUsr_QNAME, String.class, GetDispoByModelCode.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "ModelCode", scope = GetDispoByModelCode.class)
    public JAXBElement<String> createGetDispoByModelCodeModelCode(String value) {
        return new JAXBElement<String>(_GetDispoByModelCodeModelCode_QNAME, String.class, GetDispoByModelCode.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "cdMagazzino", scope = GetDispoByModelCode.class)
    public JAXBElement<String> createGetDispoByModelCodeCdMagazzino(String value) {
        return new JAXBElement<String>(_GetDispoByModelCodeCdMagazzino_QNAME, String.class, GetDispoByModelCode.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetDispoByModelCode.class)
    public JAXBElement<String> createGetDispoByModelCodePwd(String value) {
        return new JAXBElement<String>(_GetArticoliFlatPwd_QNAME, String.class, GetDispoByModelCode.class, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfNazioneVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetNazioniResult", scope = GetNazioniResponse.class)
    public JAXBElement<ArrayOfNazioneVO> createGetNazioniResponseGetNazioniResult(ArrayOfNazioneVO value) {
        return new JAXBElement<ArrayOfNazioneVO>(_GetNazioniResponseGetNazioniResult_QNAME, ArrayOfNazioneVO.class, GetNazioniResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ExtendedNote1", scope = ArticoloFlatExtVO.class)
    public JAXBElement<String> createArticoloFlatExtVOExtendedNote1(String value) {
        return new JAXBElement<String>(_ArticoloFlatExtVOExtendedNote1_QNAME, String.class, ArticoloFlatExtVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ItemClass", scope = ArticoloFlatExtVO.class)
    public JAXBElement<String> createArticoloFlatExtVOItemClass(String value) {
        return new JAXBElement<String>(_ArticoloFlatExtVOItemClass_QNAME, String.class, ArticoloFlatExtVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ItemLine", scope = ArticoloFlatExtVO.class)
    public JAXBElement<String> createArticoloFlatExtVOItemLine(String value) {
        return new JAXBElement<String>(_ArticoloFlatExtVOItemLine_QNAME, String.class, ArticoloFlatExtVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MgDispo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Quantita", scope = ArticoloFlatExtVO.class)
    public JAXBElement<MgDispo> createArticoloFlatExtVOQuantita(MgDispo value) {
        return new JAXBElement<MgDispo>(_ArticoloFlatExtVOQuantita_QNAME, MgDispo.class, ArticoloFlatExtVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ExtendedNote", scope = ArticoloFlatExtVO.class)
    public JAXBElement<String> createArticoloFlatExtVOExtendedNote(String value) {
        return new JAXBElement<String>(_ArticoloFlatExtVOExtendedNote_QNAME, String.class, ArticoloFlatExtVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfListinoVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Listini", scope = ArticoloFlatExtVO.class)
    public JAXBElement<ArrayOfListinoVO> createArticoloFlatExtVOListini(ArrayOfListinoVO value) {
        return new JAXBElement<ArrayOfListinoVO>(_ArticoloFlatExtVOListini_QNAME, ArrayOfListinoVO.class, ArticoloFlatExtVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ImageURL", scope = ArticoloFlatExtVO.class)
    public JAXBElement<String> createArticoloFlatExtVOImageURL(String value) {
        return new JAXBElement<String>(_ArticoloFlatExtVOImageURL_QNAME, String.class, ArticoloFlatExtVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "VATNumber", scope = AnagraficaVO.class)
    public JAXBElement<String> createAnagraficaVOVATNumber(String value) {
        return new JAXBElement<String>(_AnagraficaVOVATNumber_QNAME, String.class, AnagraficaVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Address", scope = AnagraficaVO.class)
    public JAXBElement<String> createAnagraficaVOAddress(String value) {
        return new JAXBElement<String>(_AnagraficaVOAddress_QNAME, String.class, AnagraficaVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Telephone", scope = AnagraficaVO.class)
    public JAXBElement<String> createAnagraficaVOTelephone(String value) {
        return new JAXBElement<String>(_AnagraficaVOTelephone_QNAME, String.class, AnagraficaVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ZIPCode", scope = AnagraficaVO.class)
    public JAXBElement<String> createAnagraficaVOZIPCode(String value) {
        return new JAXBElement<String>(_AnagraficaVOZIPCode_QNAME, String.class, AnagraficaVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "City", scope = AnagraficaVO.class)
    public JAXBElement<String> createAnagraficaVOCity(String value) {
        return new JAXBElement<String>(_AnagraficaVOCity_QNAME, String.class, AnagraficaVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "DeliveryArea", scope = AnagraficaVO.class)
    public JAXBElement<String> createAnagraficaVODeliveryArea(String value) {
        return new JAXBElement<String>(_AnagraficaVODeliveryArea_QNAME, String.class, AnagraficaVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "DeliveryAddress", scope = AnagraficaVO.class)
    public JAXBElement<String> createAnagraficaVODeliveryAddress(String value) {
        return new JAXBElement<String>(_AnagraficaVODeliveryAddress_QNAME, String.class, AnagraficaVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Name", scope = AnagraficaVO.class)
    public JAXBElement<String> createAnagraficaVOName(String value) {
        return new JAXBElement<String>(_AnagraficaVOName_QNAME, String.class, AnagraficaVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "IDState", scope = AnagraficaVO.class)
    public JAXBElement<String> createAnagraficaVOIDState(String value) {
        return new JAXBElement<String>(_AnagraficaVOIDState_QNAME, String.class, AnagraficaVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Area", scope = AnagraficaVO.class)
    public JAXBElement<String> createAnagraficaVOArea(String value) {
        return new JAXBElement<String>(_AnagraficaVOArea_QNAME, String.class, AnagraficaVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "DeliveryIDState", scope = AnagraficaVO.class)
    public JAXBElement<String> createAnagraficaVODeliveryIDState(String value) {
        return new JAXBElement<String>(_AnagraficaVODeliveryIDState_QNAME, String.class, AnagraficaVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "DeliveryName", scope = AnagraficaVO.class)
    public JAXBElement<String> createAnagraficaVODeliveryName(String value) {
        return new JAXBElement<String>(_AnagraficaVODeliveryName_QNAME, String.class, AnagraficaVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "CFnumber", scope = AnagraficaVO.class)
    public JAXBElement<String> createAnagraficaVOCFnumber(String value) {
        return new JAXBElement<String>(_AnagraficaVOCFnumber_QNAME, String.class, AnagraficaVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Note", scope = AnagraficaVO.class)
    public JAXBElement<String> createAnagraficaVONote(String value) {
        return new JAXBElement<String>(_AnagraficaVONote_QNAME, String.class, AnagraficaVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "DeliveryCity", scope = AnagraficaVO.class)
    public JAXBElement<String> createAnagraficaVODeliveryCity(String value) {
        return new JAXBElement<String>(_AnagraficaVODeliveryCity_QNAME, String.class, AnagraficaVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ID", scope = AnagraficaVO.class)
    public JAXBElement<String> createAnagraficaVOID(String value) {
        return new JAXBElement<String>(_AnagraficaVOID_QNAME, String.class, AnagraficaVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "DeliveryZIPCode", scope = AnagraficaVO.class)
    public JAXBElement<String> createAnagraficaVODeliveryZIPCode(String value) {
        return new JAXBElement<String>(_AnagraficaVODeliveryZIPCode_QNAME, String.class, AnagraficaVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "IDListinoCliente", scope = AnagraficaVO.class)
    public JAXBElement<String> createAnagraficaVOIDListinoCliente(String value) {
        return new JAXBElement<String>(_AnagraficaVOIDListinoCliente_QNAME, String.class, AnagraficaVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "email", scope = AnagraficaVO.class)
    public JAXBElement<String> createAnagraficaVOEmail(String value) {
        return new JAXBElement<String>(_AnagraficaVOEmail_QNAME, String.class, AnagraficaVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Fax", scope = AnagraficaVO.class)
    public JAXBElement<String> createAnagraficaVOFax(String value) {
        return new JAXBElement<String>(_AnagraficaVOFax_QNAME, String.class, AnagraficaVO.class, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfGruppoVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetGruppoVOResult", scope = GetGruppoVOResponse.class)
    public JAXBElement<ArrayOfGruppoVO> createGetGruppoVOResponseGetGruppoVOResult(ArrayOfGruppoVO value) {
        return new JAXBElement<ArrayOfGruppoVO>(_GetGruppoVOResponseGetGruppoVOResult_QNAME, ArrayOfGruppoVO.class, GetGruppoVOResponse.class, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link Float }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Discount", scope = DocumentoVO.class)
    public JAXBElement<Float> createDocumentoVODiscount(Float value) {
        return new JAXBElement<Float>(_DocumentoVODiscount_QNAME, Float.class, DocumentoVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Tracking", scope = DocumentoVO.class)
    public JAXBElement<String> createDocumentoVOTracking(String value) {
        return new JAXBElement<String>(_DocumentoVOTracking_QNAME, String.class, DocumentoVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "IDDocumentoRif", scope = DocumentoVO.class)
    public JAXBElement<String> createDocumentoVOIDDocumentoRif(String value) {
        return new JAXBElement<String>(_DocumentoVOIDDocumentoRif_QNAME, String.class, DocumentoVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Short }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "IdTipoDocumento", scope = DocumentoVO.class)
    public JAXBElement<Short> createDocumentoVOIdTipoDocumento(Short value) {
        return new JAXBElement<Short>(_DocumentoVOIdTipoDocumento_QNAME, Short.class, DocumentoVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AnagraficaVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Customer", scope = DocumentoVO.class)
    public JAXBElement<AnagraficaVO> createDocumentoVOCustomer(AnagraficaVO value) {
        return new JAXBElement<AnagraficaVO>(_DocumentoVOCustomer_QNAME, AnagraficaVO.class, DocumentoVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "OrderNumber", scope = DocumentoVO.class)
    public JAXBElement<String> createDocumentoVOOrderNumber(String value) {
        return new JAXBElement<String>(_DocumentoVOOrderNumber_QNAME, String.class, DocumentoVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "IDLetteraVettura", scope = DocumentoVO.class)
    public JAXBElement<String> createDocumentoVOIDLetteraVettura(String value) {
        return new JAXBElement<String>(_DocumentoVOIDLetteraVettura_QNAME, String.class, DocumentoVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfTipoPagamentoVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "TipiPagamento", scope = DocumentoVO.class)
    public JAXBElement<ArrayOfTipoPagamentoVO> createDocumentoVOTipiPagamento(ArrayOfTipoPagamentoVO value) {
        return new JAXBElement<ArrayOfTipoPagamentoVO>(_DocumentoVOTipiPagamento_QNAME, ArrayOfTipoPagamentoVO.class, DocumentoVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Number", scope = DocumentoVO.class)
    public JAXBElement<String> createDocumentoVONumber(String value) {
        return new JAXBElement<String>(_DocumentoVONumber_QNAME, String.class, DocumentoVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ID", scope = DocumentoVO.class)
    public JAXBElement<String> createDocumentoVOID(String value) {
        return new JAXBElement<String>(_AnagraficaVOID_QNAME, String.class, DocumentoVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfDocumentoRigaVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Righe", scope = DocumentoVO.class)
    public JAXBElement<ArrayOfDocumentoRigaVO> createDocumentoVORighe(ArrayOfDocumentoRigaVO value) {
        return new JAXBElement<ArrayOfDocumentoRigaVO>(_DocumentoVORighe_QNAME, ArrayOfDocumentoRigaVO.class, DocumentoVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "TipoPagamento", scope = DocumentoVO.class)
    public JAXBElement<String> createDocumentoVOTipoPagamento(String value) {
        return new JAXBElement<String>(_DocumentoVOTipoPagamento_QNAME, String.class, DocumentoVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "IDUser", scope = DocumentoVO.class)
    public JAXBElement<String> createDocumentoVOIDUser(String value) {
        return new JAXBElement<String>(_DocumentoVOIDUser_QNAME, String.class, DocumentoVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "PriceListCode", scope = DocumentoVO.class)
    public JAXBElement<String> createDocumentoVOPriceListCode(String value) {
        return new JAXBElement<String>(_DocumentoVOPriceListCode_QNAME, String.class, DocumentoVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "IDTipoArticolo", scope = RelTipiArticoliTaglieVO.class)
    public JAXBElement<String> createRelTipiArticoliTaglieVOIDTipoArticolo(String value) {
        return new JAXBElement<String>(_RelTipiArticoliTaglieVOIDTipoArticolo_QNAME, String.class, RelTipiArticoliTaglieVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "IDTaglia", scope = RelTipiArticoliTaglieVO.class)
    public JAXBElement<String> createRelTipiArticoliTaglieVOIDTaglia(String value) {
        return new JAXBElement<String>(_RelTipiArticoliTaglieVOIDTaglia_QNAME, String.class, RelTipiArticoliTaglieVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "view", scope = GetModelsByQuery.class)
    public JAXBElement<String> createGetModelsByQueryView(String value) {
        return new JAXBElement<String>(_GetModelsByQueryView_QNAME, String.class, GetModelsByQuery.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetModelsByQuery.class)
    public JAXBElement<String> createGetModelsByQueryUsr(String value) {
        return new JAXBElement<String>(_GetArticoliFlatUsr_QNAME, String.class, GetModelsByQuery.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetModelsByQuery.class)
    public JAXBElement<String> createGetModelsByQueryPwd(String value) {
        return new JAXBElement<String>(_GetArticoliFlatPwd_QNAME, String.class, GetModelsByQuery.class, value);
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
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetPagedCustomer.class)
    public JAXBElement<String> createGetPagedCustomerUsr(String value) {
        return new JAXBElement<String>(_GetArticoliFlatUsr_QNAME, String.class, GetPagedCustomer.class, value);
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
        return new JAXBElement<String>(_GetArticoliFlatPwd_QNAME, String.class, GetPagedCustomer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetRelationsLineeClassi.class)
    public JAXBElement<String> createGetRelationsLineeClassiUsr(String value) {
        return new JAXBElement<String>(_GetArticoliFlatUsr_QNAME, String.class, GetRelationsLineeClassi.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetRelationsLineeClassi.class)
    public JAXBElement<String> createGetRelationsLineeClassiPwd(String value) {
        return new JAXBElement<String>(_GetArticoliFlatPwd_QNAME, String.class, GetRelationsLineeClassi.class, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetSottoGruppo.class)
    public JAXBElement<String> createGetSottoGruppoUsr(String value) {
        return new JAXBElement<String>(_GetArticoliFlatUsr_QNAME, String.class, GetSottoGruppo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetSottoGruppo.class)
    public JAXBElement<String> createGetSottoGruppoPwd(String value) {
        return new JAXBElement<String>(_GetArticoliFlatPwd_QNAME, String.class, GetSottoGruppo.class, value);
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
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetRelationsSClassiSSClassi.class)
    public JAXBElement<String> createGetRelationsSClassiSSClassiUsr(String value) {
        return new JAXBElement<String>(_GetArticoliFlatUsr_QNAME, String.class, GetRelationsSClassiSSClassi.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetRelationsSClassiSSClassi.class)
    public JAXBElement<String> createGetRelationsSClassiSSClassiPwd(String value) {
        return new JAXBElement<String>(_GetArticoliFlatPwd_QNAME, String.class, GetRelationsSClassiSSClassi.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "documentType", scope = GetNextDocumentNumber.class)
    public JAXBElement<String> createGetNextDocumentNumberDocumentType(String value) {
        return new JAXBElement<String>(_GetNextDocumentNumberDocumentType_QNAME, String.class, GetNextDocumentNumber.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfTrackingInfo }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetTrackingResult", scope = GetTrackingResponse.class)
    public JAXBElement<ArrayOfTrackingInfo> createGetTrackingResponseGetTrackingResult(ArrayOfTrackingInfo value) {
        return new JAXBElement<ArrayOfTrackingInfo>(_GetTrackingResponseGetTrackingResult_QNAME, ArrayOfTrackingInfo.class, GetTrackingResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetSottoCategorie.class)
    public JAXBElement<String> createGetSottoCategorieUsr(String value) {
        return new JAXBElement<String>(_GetArticoliFlatUsr_QNAME, String.class, GetSottoCategorie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetSottoCategorie.class)
    public JAXBElement<String> createGetSottoCategoriePwd(String value) {
        return new JAXBElement<String>(_GetArticoliFlatPwd_QNAME, String.class, GetSottoCategorie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetItemClasses.class)
    public JAXBElement<String> createGetItemClassesUsr(String value) {
        return new JAXBElement<String>(_GetArticoliFlatUsr_QNAME, String.class, GetItemClasses.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetItemClasses.class)
    public JAXBElement<String> createGetItemClassesPwd(String value) {
        return new JAXBElement<String>(_GetArticoliFlatPwd_QNAME, String.class, GetItemClasses.class, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "IDAnagrafica", scope = RelAgentiAnagraficheVO.class)
    public JAXBElement<String> createRelAgentiAnagraficheVOIDAnagrafica(String value) {
        return new JAXBElement<String>(_RelAgentiAnagraficheVOIDAnagrafica_QNAME, String.class, RelAgentiAnagraficheVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "IDAgente", scope = RelAgentiAnagraficheVO.class)
    public JAXBElement<String> createRelAgentiAnagraficheVOIDAgente(String value) {
        return new JAXBElement<String>(_RelAgentiAnagraficheVOIDAgente_QNAME, String.class, RelAgentiAnagraficheVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetNextDocumentNumberResult", scope = GetNextDocumentNumberResponse.class)
    public JAXBElement<String> createGetNextDocumentNumberResponseGetNextDocumentNumberResult(String value) {
        return new JAXBElement<String>(_GetNextDocumentNumberResponseGetNextDocumentNumberResult_QNAME, String.class, GetNextDocumentNumberResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetRelationsClassiSClassi.class)
    public JAXBElement<String> createGetRelationsClassiSClassiUsr(String value) {
        return new JAXBElement<String>(_GetArticoliFlatUsr_QNAME, String.class, GetRelationsClassiSClassi.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetRelationsClassiSClassi.class)
    public JAXBElement<String> createGetRelationsClassiSClassiPwd(String value) {
        return new JAXBElement<String>(_GetArticoliFlatPwd_QNAME, String.class, GetRelationsClassiSClassi.class, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetTracking.class)
    public JAXBElement<String> createGetTrackingUsr(String value) {
        return new JAXBElement<String>(_GetArticoliFlatUsr_QNAME, String.class, GetTracking.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "IdDocumento", scope = GetTracking.class)
    public JAXBElement<String> createGetTrackingIdDocumento(String value) {
        return new JAXBElement<String>(_GetTrackingIdDocumento_QNAME, String.class, GetTracking.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetTracking.class)
    public JAXBElement<String> createGetTrackingPwd(String value) {
        return new JAXBElement<String>(_GetArticoliFlatPwd_QNAME, String.class, GetTracking.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = InsertOrder.class)
    public JAXBElement<String> createInsertOrderUsr(String value) {
        return new JAXBElement<String>(_GetArticoliFlatUsr_QNAME, String.class, InsertOrder.class, value);
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
        return new JAXBElement<String>(_GetArticoliFlatPwd_QNAME, String.class, InsertOrder.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "IDLineaArticolo", scope = RelLineeClassiVO.class)
    public JAXBElement<String> createRelLineeClassiVOIDLineaArticolo(String value) {
        return new JAXBElement<String>(_RelLineeClassiVOIDLineaArticolo_QNAME, String.class, RelLineeClassiVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "IDClasseArticolo", scope = RelLineeClassiVO.class)
    public JAXBElement<String> createRelLineeClassiVOIDClasseArticolo(String value) {
        return new JAXBElement<String>(_RelLineeClassiVOIDClasseArticolo_QNAME, String.class, RelLineeClassiVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "IDSSClasseArticolo", scope = RELSClassiSSCLassiVO.class)
    public JAXBElement<String> createRELSClassiSSCLassiVOIDSSClasseArticolo(String value) {
        return new JAXBElement<String>(_RELSClassiSSCLassiVOIDSSClasseArticolo_QNAME, String.class, RELSClassiSSCLassiVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "IDSClasseArticolo", scope = RELSClassiSSCLassiVO.class)
    public JAXBElement<String> createRELSClassiSSCLassiVOIDSClasseArticolo(String value) {
        return new JAXBElement<String>(_RELSClassiSSCLassiVOIDSClasseArticolo_QNAME, String.class, RELSClassiSSCLassiVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ID", scope = TagliaVO.class)
    public JAXBElement<String> createTagliaVOID(String value) {
        return new JAXBElement<String>(_AnagraficaVOID_QNAME, String.class, TagliaVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Descrizione", scope = TagliaVO.class)
    public JAXBElement<String> createTagliaVODescrizione(String value) {
        return new JAXBElement<String>(_GenericTabledEntityVODescrizione_QNAME, String.class, TagliaVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetSottoSottoGruppo.class)
    public JAXBElement<String> createGetSottoSottoGruppoUsr(String value) {
        return new JAXBElement<String>(_GetArticoliFlatUsr_QNAME, String.class, GetSottoSottoGruppo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetSottoSottoGruppo.class)
    public JAXBElement<String> createGetSottoSottoGruppoPwd(String value) {
        return new JAXBElement<String>(_GetArticoliFlatPwd_QNAME, String.class, GetSottoSottoGruppo.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ColourPicker", scope = ColoreVO.class)
    public JAXBElement<String> createColoreVOColourPicker(String value) {
        return new JAXBElement<String>(_ColoreVOColourPicker_QNAME, String.class, ColoreVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "BarCode", scope = MgDispoArrayElement.class)
    public JAXBElement<String> createMgDispoArrayElementBarCode(String value) {
        return new JAXBElement<String>(_MgDispoArrayElementBarCode_QNAME, String.class, MgDispoArrayElement.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetColori.class)
    public JAXBElement<String> createGetColoriUsr(String value) {
        return new JAXBElement<String>(_GetArticoliFlatUsr_QNAME, String.class, GetColori.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetColori.class)
    public JAXBElement<String> createGetColoriPwd(String value) {
        return new JAXBElement<String>(_GetArticoliFlatPwd_QNAME, String.class, GetColori.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetGruppoVO.class)
    public JAXBElement<String> createGetGruppoVOUsr(String value) {
        return new JAXBElement<String>(_GetArticoliFlatUsr_QNAME, String.class, GetGruppoVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetGruppoVO.class)
    public JAXBElement<String> createGetGruppoVOPwd(String value) {
        return new JAXBElement<String>(_GetArticoliFlatPwd_QNAME, String.class, GetGruppoVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfDocumentoShortVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetOrdersResult", scope = GetOrdersResponse.class)
    public JAXBElement<ArrayOfDocumentoShortVO> createGetOrdersResponseGetOrdersResult(ArrayOfDocumentoShortVO value) {
        return new JAXBElement<ArrayOfDocumentoShortVO>(_GetOrdersResponseGetOrdersResult_QNAME, ArrayOfDocumentoShortVO.class, GetOrdersResponse.class, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfClasseVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetItemLinesResult", scope = GetItemLinesResponse.class)
    public JAXBElement<ArrayOfClasseVO> createGetItemLinesResponseGetItemLinesResult(ArrayOfClasseVO value) {
        return new JAXBElement<ArrayOfClasseVO>(_GetItemLinesResponseGetItemLinesResult_QNAME, ArrayOfClasseVO.class, GetItemLinesResponse.class, value);
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
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "InsertCustomerResult", scope = InsertCustomerResponse.class)
    public JAXBElement<String> createInsertCustomerResponseInsertCustomerResult(String value) {
        return new JAXBElement<String>(_InsertCustomerResponseInsertCustomerResult_QNAME, String.class, InsertCustomerResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetOrders.class)
    public JAXBElement<String> createGetOrdersUsr(String value) {
        return new JAXBElement<String>(_GetArticoliFlatUsr_QNAME, String.class, GetOrders.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "idAnagrafica", scope = GetOrders.class)
    public JAXBElement<String> createGetOrdersIdAnagrafica(String value) {
        return new JAXBElement<String>(_GetOrdersIdAnagrafica_QNAME, String.class, GetOrders.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetOrders.class)
    public JAXBElement<String> createGetOrdersPwd(String value) {
        return new JAXBElement<String>(_GetArticoliFlatPwd_QNAME, String.class, GetOrders.class, value);
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
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", name = "CarrierStatusCode", scope = StatusDetails.class)
    public JAXBElement<String> createStatusDetailsCarrierStatusCode(String value) {
        return new JAXBElement<String>(_StatusDetailsCarrierStatusCode_QNAME, String.class, StatusDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", name = "StatusDescription", scope = StatusDetails.class)
    public JAXBElement<String> createStatusDetailsStatusDescription(String value) {
        return new JAXBElement<String>(_StatusDetailsStatusDescription_QNAME, String.class, StatusDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", name = "StatusDepot", scope = StatusDetails.class)
    public JAXBElement<String> createStatusDetailsStatusDepot(String value) {
        return new JAXBElement<String>(_StatusDetailsStatusDepot_QNAME, String.class, StatusDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", name = "StatusDate", scope = StatusDetails.class)
    public JAXBElement<String> createStatusDetailsStatusDate(String value) {
        return new JAXBElement<String>(_StatusDetailsStatusDate_QNAME, String.class, StatusDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "IDSClasseArticolo", scope = RelClassiSClassiVO.class)
    public JAXBElement<String> createRelClassiSClassiVOIDSClasseArticolo(String value) {
        return new JAXBElement<String>(_RELSClassiSSCLassiVOIDSClasseArticolo_QNAME, String.class, RelClassiSClassiVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "IDClasseArticolo", scope = RelClassiSClassiVO.class)
    public JAXBElement<String> createRelClassiSClassiVOIDClasseArticolo(String value) {
        return new JAXBElement<String>(_RelLineeClassiVOIDClasseArticolo_QNAME, String.class, RelClassiSClassiVO.class, value);
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
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = InsertOrderItalist.class)
    public JAXBElement<String> createInsertOrderItalistUsr(String value) {
        return new JAXBElement<String>(_GetArticoliFlatUsr_QNAME, String.class, InsertOrderItalist.class, value);
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
        return new JAXBElement<String>(_GetArticoliFlatPwd_QNAME, String.class, InsertOrderItalist.class, value);
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
        return new JAXBElement<String>(_GetArticoliFlatUsr_QNAME, String.class, GetArticoliFlatExtByDate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetArticoliFlatExtByDate.class)
    public JAXBElement<String> createGetArticoliFlatExtByDatePwd(String value) {
        return new JAXBElement<String>(_GetArticoliFlatPwd_QNAME, String.class, GetArticoliFlatExtByDate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Description", scope = ArticoloLocaleVO.class)
    public JAXBElement<String> createArticoloLocaleVODescription(String value) {
        return new JAXBElement<String>(_ArticoloLocaleVODescription_QNAME, String.class, ArticoloLocaleVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "DescrizioneAgg", scope = ArticoloLocaleVO.class)
    public JAXBElement<String> createArticoloLocaleVODescrizioneAgg(String value) {
        return new JAXBElement<String>(_ArticoloLocaleVODescrizioneAgg_QNAME, String.class, ArticoloLocaleVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "color", scope = ArticoloLocaleVO.class)
    public JAXBElement<String> createArticoloLocaleVOColor(String value) {
        return new JAXBElement<String>(_ArticoloLocaleVOColor_QNAME, String.class, ArticoloLocaleVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "visibility", scope = ArticoloLocaleVO.class)
    public JAXBElement<String> createArticoloLocaleVOVisibility(String value) {
        return new JAXBElement<String>(_ArticoloLocaleVOVisibility_QNAME, String.class, ArticoloLocaleVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "IdLocale", scope = ArticoloLocaleVO.class)
    public JAXBElement<String> createArticoloLocaleVOIdLocale(String value) {
        return new JAXBElement<String>(_ArticoloLocaleVOIdLocale_QNAME, String.class, ArticoloLocaleVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "subd_fabric", scope = ArticoloLocaleVO.class)
    public JAXBElement<String> createArticoloLocaleVOSubdFabric(String value) {
        return new JAXBElement<String>(_ArticoloLocaleVOSubdFabric_QNAME, String.class, ArticoloLocaleVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Name", scope = ArticoloLocaleVO.class)
    public JAXBElement<String> createArticoloLocaleVOName(String value) {
        return new JAXBElement<String>(_AnagraficaVOName_QNAME, String.class, ArticoloLocaleVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "url_key", scope = ArticoloLocaleVO.class)
    public JAXBElement<String> createArticoloLocaleVOUrlKey(String value) {
        return new JAXBElement<String>(_ArticoloLocaleVOUrlKey_QNAME, String.class, ArticoloLocaleVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "SKU", scope = ArticoloLocaleVO.class)
    public JAXBElement<String> createArticoloLocaleVOSKU(String value) {
        return new JAXBElement<String>(_ArticoloLocaleVOSKU_QNAME, String.class, ArticoloLocaleVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "symbol", scope = ListinoVO.class)
    public JAXBElement<String> createListinoVOSymbol(String value) {
        return new JAXBElement<String>(_ListinoVOSymbol_QNAME, String.class, ListinoVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "IDDocumentoContatore", scope = DocumentoRigaVO.class)
    public JAXBElement<String> createDocumentoRigaVOIDDocumentoContatore(String value) {
        return new JAXBElement<String>(_DocumentoRigaVOIDDocumentoContatore_QNAME, String.class, DocumentoRigaVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "IDDocumento", scope = DocumentoRigaVO.class)
    public JAXBElement<String> createDocumentoRigaVOIDDocumento(String value) {
        return new JAXBElement<String>(_DocumentoRigaVOIDDocumento_QNAME, String.class, DocumentoRigaVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "IDDocumentoContatoreRif", scope = DocumentoRigaVO.class)
    public JAXBElement<String> createDocumentoRigaVOIDDocumentoContatoreRif(String value) {
        return new JAXBElement<String>(_DocumentoRigaVOIDDocumentoContatoreRif_QNAME, String.class, DocumentoRigaVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "BarCode", scope = DocumentoRigaVO.class)
    public JAXBElement<String> createDocumentoRigaVOBarCode(String value) {
        return new JAXBElement<String>(_MgDispoArrayElementBarCode_QNAME, String.class, DocumentoRigaVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Descrizione", scope = DocumentoRigaVO.class)
    public JAXBElement<String> createDocumentoRigaVODescrizione(String value) {
        return new JAXBElement<String>(_GenericTabledEntityVODescrizione_QNAME, String.class, DocumentoRigaVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "GroupName", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOGroupName(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOGroupName_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Size", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOSize(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOSize_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ModelCode", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOModelCode(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOModelCode_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ShortDescription", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOShortDescription(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOShortDescription_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "SubSubCategory", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOSubSubCategory(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOSubSubCategory_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ColourWebID", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOColourWebID(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOColourWebID_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Libero7Name", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOLibero7Name(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOLibero7Name_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ExtendedDescription", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOExtendedDescription(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOExtendedDescription_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "TextureID", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOTextureID(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOTextureID_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Libero8Name", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOLibero8Name(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOLibero8Name_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "SubSubGroup", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOSubSubGroup(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOSubSubGroup_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Ecommerce", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOEcommerce(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOEcommerce_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Libero5Name", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOLibero5Name(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOLibero5Name_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "SubGroup", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOSubGroup(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOSubGroup_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "SubGroupName", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOSubGroupName(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOSubGroupName_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Brand", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOBrand(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOBrand_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "SubCategoryName", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOSubCategoryName(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOSubCategoryName_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Libero6Name", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOLibero6Name(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOLibero6Name_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Category", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOCategory(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOCategory_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Description", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVODescription(String value) {
        return new JAXBElement<String>(_ArticoloLocaleVODescription_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Libero2Name", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOLibero2Name(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOLibero2Name_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Group", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOGroup(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOGroup_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Libero10Name", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOLibero10Name(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOLibero10Name_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "BarCode", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOBarCode(String value) {
        return new JAXBElement<String>(_MgDispoArrayElementBarCode_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Libero1Name", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOLibero1Name(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOLibero1Name_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Colour", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOColour(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOColour_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Texture", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOTexture(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOTexture_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Libero3Name", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOLibero3Name(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOLibero3Name_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "SubSubCategoryName", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOSubSubCategoryName(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOSubSubCategoryName_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Supplier", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOSupplier(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOSupplier_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Libero4Name", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOLibero4Name(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOLibero4Name_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Libero5", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOLibero5(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOLibero5_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Libero6", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOLibero6(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOLibero6_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Libero7", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOLibero7(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOLibero7_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Libero9Name", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOLibero9Name(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOLibero9Name_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Libero8", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOLibero8(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOLibero8_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ColourID", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOColourID(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOColourID_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Libero9", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOLibero9(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOLibero9_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "SubSubGroupName", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOSubSubGroupName(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOSubSubGroupName_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "IDTipoArticolo", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOIDTipoArticolo(String value) {
        return new JAXBElement<String>(_RelTipiArticoliTaglieVOIDTipoArticolo_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Libero1", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOLibero1(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOLibero1_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Libero2", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOLibero2(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOLibero2_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Libero3", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOLibero3(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOLibero3_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Libero4", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOLibero4(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOLibero4_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "ColourWeb", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOColourWeb(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOColourWeb_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Libero10", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOLibero10(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOLibero10_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "CategoryName", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOCategoryName(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOCategoryName_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "SubCategory", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOSubCategory(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOSubCategory_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "SizeiD", scope = ArticoloFlatVO.class)
    public JAXBElement<String> createArticoloFlatVOSizeiD(String value) {
        return new JAXBElement<String>(_ArticoloFlatVOSizeiD_QNAME, String.class, ArticoloFlatVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetTaglie.class)
    public JAXBElement<String> createGetTaglieUsr(String value) {
        return new JAXBElement<String>(_GetArticoliFlatUsr_QNAME, String.class, GetTaglie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetTaglie.class)
    public JAXBElement<String> createGetTagliePwd(String value) {
        return new JAXBElement<String>(_GetArticoliFlatPwd_QNAME, String.class, GetTaglie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRelTipiArticoliTaglieVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetRelTipiArticoliTaglieResult", scope = GetRelTipiArticoliTaglieResponse.class)
    public JAXBElement<ArrayOfRelTipiArticoliTaglieVO> createGetRelTipiArticoliTaglieResponseGetRelTipiArticoliTaglieResult(ArrayOfRelTipiArticoliTaglieVO value) {
        return new JAXBElement<ArrayOfRelTipiArticoliTaglieVO>(_GetRelTipiArticoliTaglieResponseGetRelTipiArticoliTaglieResult_QNAME, ArrayOfRelTipiArticoliTaglieVO.class, GetRelTipiArticoliTaglieResponse.class, value);
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
        return new JAXBElement<String>(_GetArticoliFlatUsr_QNAME, String.class, GetArticoliFlatExtLocaleByDate.class, value);
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
        return new JAXBElement<String>(_GetArticoliFlatPwd_QNAME, String.class, GetArticoliFlatExtLocaleByDate.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfRelAgentiAnagraficheVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetRelAgentiAnagraficheResult", scope = GetRelAgentiAnagraficheResponse.class)
    public JAXBElement<ArrayOfRelAgentiAnagraficheVO> createGetRelAgentiAnagraficheResponseGetRelAgentiAnagraficheResult(ArrayOfRelAgentiAnagraficheVO value) {
        return new JAXBElement<ArrayOfRelAgentiAnagraficheVO>(_GetRelAgentiAnagraficheResponseGetRelAgentiAnagraficheResult_QNAME, ArrayOfRelAgentiAnagraficheVO.class, GetRelAgentiAnagraficheResponse.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetItemLines.class)
    public JAXBElement<String> createGetItemLinesUsr(String value) {
        return new JAXBElement<String>(_GetArticoliFlatUsr_QNAME, String.class, GetItemLines.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetItemLines.class)
    public JAXBElement<String> createGetItemLinesPwd(String value) {
        return new JAXBElement<String>(_GetArticoliFlatPwd_QNAME, String.class, GetItemLines.class, value);
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
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetDispoByBarcode.class)
    public JAXBElement<String> createGetDispoByBarcodeUsr(String value) {
        return new JAXBElement<String>(_GetArticoliFlatUsr_QNAME, String.class, GetDispoByBarcode.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "cdMagazzino", scope = GetDispoByBarcode.class)
    public JAXBElement<String> createGetDispoByBarcodeCdMagazzino(String value) {
        return new JAXBElement<String>(_GetDispoByModelCodeCdMagazzino_QNAME, String.class, GetDispoByBarcode.class, value);
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
        return new JAXBElement<String>(_GetArticoliFlatPwd_QNAME, String.class, GetDispoByBarcode.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Numero", scope = DocumentoShortVO.class)
    public JAXBElement<String> createDocumentoShortVONumero(String value) {
        return new JAXBElement<String>(_DocumentoShortVONumero_QNAME, String.class, DocumentoShortVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DocumentoEvasioneVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Status", scope = DocumentoShortVO.class)
    public JAXBElement<DocumentoEvasioneVO> createDocumentoShortVOStatus(DocumentoEvasioneVO value) {
        return new JAXBElement<DocumentoEvasioneVO>(_DocumentoShortVOStatus_QNAME, DocumentoEvasioneVO.class, DocumentoShortVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Ragione", scope = DocumentoShortVO.class)
    public JAXBElement<String> createDocumentoShortVORagione(String value) {
        return new JAXBElement<String>(_DocumentoShortVORagione_QNAME, String.class, DocumentoShortVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetCategorie.class)
    public JAXBElement<String> createGetCategorieUsr(String value) {
        return new JAXBElement<String>(_GetArticoliFlatUsr_QNAME, String.class, GetCategorie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetCategorie.class)
    public JAXBElement<String> createGetCategoriePwd(String value) {
        return new JAXBElement<String>(_GetArticoliFlatPwd_QNAME, String.class, GetCategorie.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = InsertCustomer.class)
    public JAXBElement<String> createInsertCustomerUsr(String value) {
        return new JAXBElement<String>(_GetArticoliFlatUsr_QNAME, String.class, InsertCustomer.class, value);
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
        return new JAXBElement<String>(_GetArticoliFlatPwd_QNAME, String.class, InsertCustomer.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "usr", scope = GetRelAgentiAnagrafiche.class)
    public JAXBElement<String> createGetRelAgentiAnagraficheUsr(String value) {
        return new JAXBElement<String>(_GetArticoliFlatUsr_QNAME, String.class, GetRelAgentiAnagrafiche.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "pwd", scope = GetRelAgentiAnagrafiche.class)
    public JAXBElement<String> createGetRelAgentiAnagrafichePwd(String value) {
        return new JAXBElement<String>(_GetArticoliFlatPwd_QNAME, String.class, GetRelAgentiAnagrafiche.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfMgDispoArrayElement }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "GetDispoByModelCodeResult", scope = GetDispoByModelCodeResponse.class)
    public JAXBElement<ArrayOfMgDispoArrayElement> createGetDispoByModelCodeResponseGetDispoByModelCodeResult(ArrayOfMgDispoArrayElement value) {
        return new JAXBElement<ArrayOfMgDispoArrayElement>(_GetDispoByModelCodeResponseGetDispoByModelCodeResult_QNAME, ArrayOfMgDispoArrayElement.class, GetDispoByModelCodeResponse.class, value);
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
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", name = "SendAccNo", scope = SenderDetails.class)
    public JAXBElement<String> createSenderDetailsSendAccNo(String value) {
        return new JAXBElement<String>(_SenderDetailsSendAccNo_QNAME, String.class, SenderDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common", name = "SendName", scope = SenderDetails.class)
    public JAXBElement<String> createSenderDetailsSendName(String value) {
        return new JAXBElement<String>(_SenderDetailsSendName_QNAME, String.class, SenderDetails.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ArrayOfArticoloLocaleVO }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", name = "Locales", scope = ArticoloFlatExtLocaleVO.class)
    public JAXBElement<ArrayOfArticoloLocaleVO> createArticoloFlatExtLocaleVOLocales(ArrayOfArticoloLocaleVO value) {
        return new JAXBElement<ArrayOfArticoloLocaleVO>(_ArticoloFlatExtLocaleVOLocales_QNAME, ArrayOfArticoloLocaleVO.class, ArticoloFlatExtLocaleVO.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://tempuri.org/", name = "InsertOrderItalistResult", scope = InsertOrderItalistResponse.class)
    public JAXBElement<String> createInsertOrderItalistResponseInsertOrderItalistResult(String value) {
        return new JAXBElement<String>(_InsertOrderItalistResponseInsertOrderItalistResult_QNAME, String.class, InsertOrderItalistResponse.class, value);
    }

}
