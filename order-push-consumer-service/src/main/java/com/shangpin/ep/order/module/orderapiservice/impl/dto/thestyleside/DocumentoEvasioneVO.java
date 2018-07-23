
package com.shangpin.ep.order.module.orderapiservice.impl.dto.thestyleside;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>DocumentoEvasioneVO complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="DocumentoEvasioneVO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Annullati" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Aperte" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Evasi" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="InPreparazione" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="Ordinati" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DocumentoEvasioneVO", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", propOrder = {
    "annullati",
    "aperte",
    "evasi",
    "inPreparazione",
    "ordinati"
})
public class DocumentoEvasioneVO {

    @XmlElement(name = "Annullati")
    protected Integer annullati;
    @XmlElement(name = "Aperte")
    protected Integer aperte;
    @XmlElement(name = "Evasi")
    protected Integer evasi;
    @XmlElement(name = "InPreparazione")
    protected Integer inPreparazione;
    @XmlElement(name = "Ordinati")
    protected Integer ordinati;

    /**
     * ��ȡannullati���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAnnullati() {
        return annullati;
    }

    /**
     * ����annullati���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAnnullati(Integer value) {
        this.annullati = value;
    }

    /**
     * ��ȡaperte���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAperte() {
        return aperte;
    }

    /**
     * ����aperte���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAperte(Integer value) {
        this.aperte = value;
    }

    /**
     * ��ȡevasi���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getEvasi() {
        return evasi;
    }

    /**
     * ����evasi���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setEvasi(Integer value) {
        this.evasi = value;
    }

    /**
     * ��ȡinPreparazione���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getInPreparazione() {
        return inPreparazione;
    }

    /**
     * ����inPreparazione���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setInPreparazione(Integer value) {
        this.inPreparazione = value;
    }

    /**
     * ��ȡordinati���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getOrdinati() {
        return ordinati;
    }

    /**
     * ����ordinati���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setOrdinati(Integer value) {
        this.ordinati = value;
    }

}
