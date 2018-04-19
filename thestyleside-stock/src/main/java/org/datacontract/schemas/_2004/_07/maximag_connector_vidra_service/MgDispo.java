
package org.datacontract.schemas._2004._07.maximag_connector_vidra_service;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>MgDispo complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="MgDispo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
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
@XmlType(name = "MgDispo", propOrder = {
    "carQ",
    "impegnatoQ",
    "ordinatoQ",
    "quantita",
    "quantitaDimm",
    "quantitaDisp",
    "retQ",
    "scaQ"
})
public class MgDispo {

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
