
package lcvmagws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>tEcritureTransfertArriveeTransit complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="tEcritureTransfertArriveeTransit"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="sNumTrf" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sLstArticle" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
@XmlType(name = "tEcritureTransfertArriveeTransit", propOrder = {
    "sNumTrf",
    "sLstArticle",
    "sUser",
    "sMdp"
})
public class TEcritureTransfertArriveeTransit {

    @XmlElement(required = true)
    protected String sNumTrf;
    @XmlElement(required = true)
    protected String sLstArticle;
    @XmlElement(required = true)
    protected String sUser;
    @XmlElement(required = true)
    protected String sMdp;

    /**
     * ��ȡsNumTrf���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSNumTrf() {
        return sNumTrf;
    }

    /**
     * ����sNumTrf���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSNumTrf(String value) {
        this.sNumTrf = value;
    }

    /**
     * ��ȡsLstArticle���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSLstArticle() {
        return sLstArticle;
    }

    /**
     * ����sLstArticle���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSLstArticle(String value) {
        this.sLstArticle = value;
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
