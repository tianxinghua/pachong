
package lcvmagws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>tEcritureTransfertArriveeTransitResponse complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="tEcritureTransfertArriveeTransitResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="EcritureTransfertArriveeTransitResult" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tEcritureTransfertArriveeTransitResponse", propOrder = {
    "ecritureTransfertArriveeTransitResult"
})
public class TEcritureTransfertArriveeTransitResponse {

    @XmlElement(name = "EcritureTransfertArriveeTransitResult", required = true)
    protected String ecritureTransfertArriveeTransitResult;

    /**
     * ��ȡecritureTransfertArriveeTransitResult���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEcritureTransfertArriveeTransitResult() {
        return ecritureTransfertArriveeTransitResult;
    }

    /**
     * ����ecritureTransfertArriveeTransitResult���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEcritureTransfertArriveeTransitResult(String value) {
        this.ecritureTransfertArriveeTransitResult = value;
    }

}
