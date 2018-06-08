
package com.shangpin.ep.order.module.orderapiservice.impl.dto.thestyleside;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ListinoVO complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="ListinoVO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO}GenericTabledEntityVO">
 *       &lt;sequence>
 *         &lt;element name="Prezzo" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *         &lt;element name="VAT" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="symbol" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ListinoVO", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", propOrder = {
    "prezzo",
    "vat",
    "symbol"
})
public class ListinoVO
    extends GenericTabledEntityVO
{

    @XmlElement(name = "Prezzo")
    protected Float prezzo;
    @XmlElement(name = "VAT")
    protected Boolean vat;
    @XmlElementRef(name = "symbol", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", type = JAXBElement.class, required = false)
    protected JAXBElement<String> symbol;

    /**
     * ��ȡprezzo���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getPrezzo() {
        return prezzo;
    }

    /**
     * ����prezzo���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setPrezzo(Float value) {
        this.prezzo = value;
    }

    /**
     * ��ȡvat���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isVAT() {
        return vat;
    }

    /**
     * ����vat���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setVAT(Boolean value) {
        this.vat = value;
    }

    /**
     * ��ȡsymbol���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getSymbol() {
        return symbol;
    }

    /**
     * ����symbol���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setSymbol(JAXBElement<String> value) {
        this.symbol = value;
    }

}
