
package com.shangpin.ep.order.module.orderapiservice.impl.dto.thestyleside;

import java.math.BigDecimal;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>MgDispoArrayElement complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="MgDispoArrayElement">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="BarCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CarQ" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="ImpegnatoQ" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="OrdinatoQ" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="Quantita" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="QuantitaDimm" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="QuantitaDisp" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="RetQ" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *         &lt;element name="ScaQ" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MgDispoArrayElement", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", propOrder = {
    "barCode",
    "carQ",
    "impegnatoQ",
    "ordinatoQ",
    "quantita",
    "quantitaDimm",
    "quantitaDisp",
    "retQ",
    "scaQ"
})
public class MgDispoArrayElement {

    @XmlElementRef(name = "BarCode", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", type = JAXBElement.class, required = false)
    protected JAXBElement<String> barCode;
    @XmlElement(name = "CarQ")
    protected BigDecimal carQ;
    @XmlElement(name = "ImpegnatoQ")
    protected BigDecimal impegnatoQ;
    @XmlElement(name = "OrdinatoQ")
    protected BigDecimal ordinatoQ;
    @XmlElement(name = "Quantita")
    protected BigDecimal quantita;
    @XmlElement(name = "QuantitaDimm")
    protected BigDecimal quantitaDimm;
    @XmlElement(name = "QuantitaDisp")
    protected BigDecimal quantitaDisp;
    @XmlElement(name = "RetQ")
    protected BigDecimal retQ;
    @XmlElement(name = "ScaQ")
    protected BigDecimal scaQ;

    /**
     * ��ȡbarCode���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public JAXBElement<String> getBarCode() {
        return barCode;
    }

    /**
     * ����barCode���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link String }{@code >}
     *     
     */
    public void setBarCode(JAXBElement<String> value) {
        this.barCode = value;
    }

    /**
     * ��ȡcarQ���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getCarQ() {
        return carQ;
    }

    /**
     * ����carQ���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setCarQ(BigDecimal value) {
        this.carQ = value;
    }

    /**
     * ��ȡimpegnatoQ���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getImpegnatoQ() {
        return impegnatoQ;
    }

    /**
     * ����impegnatoQ���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setImpegnatoQ(BigDecimal value) {
        this.impegnatoQ = value;
    }

    /**
     * ��ȡordinatoQ���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getOrdinatoQ() {
        return ordinatoQ;
    }

    /**
     * ����ordinatoQ���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setOrdinatoQ(BigDecimal value) {
        this.ordinatoQ = value;
    }

    /**
     * ��ȡquantita���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getQuantita() {
        return quantita;
    }

    /**
     * ����quantita���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setQuantita(BigDecimal value) {
        this.quantita = value;
    }

    /**
     * ��ȡquantitaDimm���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getQuantitaDimm() {
        return quantitaDimm;
    }

    /**
     * ����quantitaDimm���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setQuantitaDimm(BigDecimal value) {
        this.quantitaDimm = value;
    }

    /**
     * ��ȡquantitaDisp���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getQuantitaDisp() {
        return quantitaDisp;
    }

    /**
     * ����quantitaDisp���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setQuantitaDisp(BigDecimal value) {
        this.quantitaDisp = value;
    }

    /**
     * ��ȡretQ���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRetQ() {
        return retQ;
    }

    /**
     * ����retQ���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRetQ(BigDecimal value) {
        this.retQ = value;
    }

    /**
     * ��ȡscaQ���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getScaQ() {
        return scaQ;
    }

    /**
     * ����scaQ���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setScaQ(BigDecimal value) {
        this.scaQ = value;
    }

}
