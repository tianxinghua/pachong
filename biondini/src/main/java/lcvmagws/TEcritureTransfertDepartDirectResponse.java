
package lcvmagws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>tEcritureTransfertDepartDirectResponse complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="tEcritureTransfertDepartDirectResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="EcritureTransfertDepartDirectResult" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tEcritureTransfertDepartDirectResponse", propOrder = {
    "ecritureTransfertDepartDirectResult"
})
public class TEcritureTransfertDepartDirectResponse {

    @XmlElement(name = "EcritureTransfertDepartDirectResult", required = true)
    protected String ecritureTransfertDepartDirectResult;

    /**
     * ��ȡecritureTransfertDepartDirectResult���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEcritureTransfertDepartDirectResult() {
        return ecritureTransfertDepartDirectResult;
    }

    /**
     * ����ecritureTransfertDepartDirectResult���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEcritureTransfertDepartDirectResult(String value) {
        this.ecritureTransfertDepartDirectResult = value;
    }

}
