
package com.shangpin.ep.order.module.orderapiservice.impl.dto.thestyleside;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>TipoPagamentoVO complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="TipoPagamentoVO">
 *   &lt;complexContent>
 *     &lt;extension base="{http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO}GenericTabledEntityVO">
 *       &lt;sequence>
 *         &lt;element name="IsCash" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="IsCredit" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="Prezzo" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TipoPagamentoVO", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", propOrder = {
    "isCash",
    "isCredit",
    "prezzo"
})
public class TipoPagamentoVO
    extends GenericTabledEntityVO
{

    @XmlElement(name = "IsCash")
    protected Boolean isCash;
    @XmlElement(name = "IsCredit")
    protected Boolean isCredit;
    @XmlElement(name = "Prezzo")
    protected Float prezzo;

    /**
     * 获取isCash属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsCash() {
        return isCash;
    }

    /**
     * 设置isCash属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsCash(Boolean value) {
        this.isCash = value;
    }

    /**
     * 获取isCredit属性的值。
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsCredit() {
        return isCredit;
    }

    /**
     * 设置isCredit属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsCredit(Boolean value) {
        this.isCredit = value;
    }

    /**
     * 获取prezzo属性的值。
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
     * 设置prezzo属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setPrezzo(Float value) {
        this.prezzo = value;
    }

}
