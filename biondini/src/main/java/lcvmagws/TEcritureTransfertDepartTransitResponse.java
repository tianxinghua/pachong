
package lcvmagws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>tEcritureTransfertDepartTransitResponse complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
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
     * 获取ecritureTransfertDepartTransitResult属性的值。
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
     * 设置ecritureTransfertDepartTransitResult属性的值。
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
