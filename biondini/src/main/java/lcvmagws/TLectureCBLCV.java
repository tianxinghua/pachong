
package lcvmagws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>tLectureCBLCV complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="tLectureCBLCV"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="sCodeArticle" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sTaille" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
@XmlType(name = "tLectureCBLCV", propOrder = {
    "sCodeArticle",
    "sTaille",
    "sUser",
    "sMdp"
})
public class TLectureCBLCV {

    @XmlElement(required = true)
    protected String sCodeArticle;
    @XmlElement(required = true)
    protected String sTaille;
    @XmlElement(required = true)
    protected String sUser;
    @XmlElement(required = true)
    protected String sMdp;

    /**
     * ��ȡsCodeArticle���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSCodeArticle() {
        return sCodeArticle;
    }

    /**
     * ����sCodeArticle���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSCodeArticle(String value) {
        this.sCodeArticle = value;
    }

    /**
     * ��ȡsTaille���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSTaille() {
        return sTaille;
    }

    /**
     * ����sTaille���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSTaille(String value) {
        this.sTaille = value;
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
