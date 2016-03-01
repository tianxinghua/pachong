
package lcvmagws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>tEcritureRetourSKU complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="tEcritureRetourSKU"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="sVente" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sEncaissement" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sTVA" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sMagasin" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sDate" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="scodeCli" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sUser" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sMdp" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tEcritureRetourSKU", propOrder = {
    "sVente",
    "sEncaissement",
    "stva",
    "sMagasin",
    "sDate",
    "scodeCli",
    "sUser",
    "sMdp"
})
public class TEcritureRetourSKU {

    @XmlElement(required = true)
    protected String sVente;
    @XmlElement(required = true)
    protected String sEncaissement;
    @XmlElement(name = "sTVA", required = true)
    protected String stva;
    @XmlElement(required = true)
    protected String sMagasin;
    @XmlElement(required = true)
    protected String sDate;
    @XmlElement(required = true)
    protected String scodeCli;
    @XmlElement(required = true)
    protected String sUser;
    @XmlElement(required = true)
    protected String sMdp;

    /**
     * ��ȡsVente���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSVente() {
        return sVente;
    }

    /**
     * ����sVente���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSVente(String value) {
        this.sVente = value;
    }

    /**
     * ��ȡsEncaissement���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSEncaissement() {
        return sEncaissement;
    }

    /**
     * ����sEncaissement���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSEncaissement(String value) {
        this.sEncaissement = value;
    }

    /**
     * ��ȡstva���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTVA() {
        return stva;
    }

    /**
     * ����stva���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTVA(String value) {
        this.stva = value;
    }

    /**
     * ��ȡsMagasin���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSMagasin() {
        return sMagasin;
    }

    /**
     * ����sMagasin���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSMagasin(String value) {
        this.sMagasin = value;
    }

    /**
     * ��ȡsDate���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSDate() {
        return sDate;
    }

    /**
     * ����sDate���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSDate(String value) {
        this.sDate = value;
    }

    /**
     * ��ȡscodeCli���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScodeCli() {
        return scodeCli;
    }

    /**
     * ����scodeCli���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScodeCli(String value) {
        this.scodeCli = value;
    }

    /**
     * ��ȡsUser���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSUser() {
        return sUser;
    }

    /**
     * ����sUser���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSUser(String value) {
        this.sUser = value;
    }

    /**
     * ��ȡsMdp���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSMdp() {
        return sMdp;
    }

    /**
     * ����sMdp���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSMdp(String value) {
        this.sMdp = value;
    }

}
