
package lcvmagws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>tEcritureTransfertDepartTransitResponse complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="tEcritureTransfertDepartTransitResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="EcritureTransfertDepartTransitResult" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tEcritureTransfertDepartTransitResponse", propOrder = {
    "ecritureTransfertDepartTransitResult"
})
public class TEcritureTransfertDepartTransitResponse {

    @XmlElement(name = "EcritureTransfertDepartTransitResult", required = true)
    protected String ecritureTransfertDepartTransitResult;

    /**
     * ��ȡecritureTransfertDepartTransitResult���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEcritureTransfertDepartTransitResult() {
        return ecritureTransfertDepartTransitResult;
    }

    /**
     * ����ecritureTransfertDepartTransitResult���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEcritureTransfertDepartTransitResult(String value) {
        this.ecritureTransfertDepartTransitResult = value;
    }

}
