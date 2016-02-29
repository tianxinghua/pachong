
package lcvmagws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>tConnexionClient complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="tConnexionClient"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="sMailCli" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="sMdpCli" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
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
@XmlType(name = "tConnexionClient", propOrder = {
    "sMailCli",
    "sMdpCli",
    "sUser",
    "sMdp"
})
public class TConnexionClient {

    @XmlElement(required = true)
    protected String sMailCli;
    @XmlElement(required = true)
    protected String sMdpCli;
    @XmlElement(required = true)
    protected String sUser;
    @XmlElement(required = true)
    protected String sMdp;

    /**
     * ��ȡsMailCli���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSMailCli() {
        return sMailCli;
    }

    /**
     * ����sMailCli���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSMailCli(String value) {
        this.sMailCli = value;
    }

    /**
     * ��ȡsMdpCli���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSMdpCli() {
        return sMdpCli;
    }

    /**
     * ����sMdpCli���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSMdpCli(String value) {
        this.sMdpCli = value;
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
