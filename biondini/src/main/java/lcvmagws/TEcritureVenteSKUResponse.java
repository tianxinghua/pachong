
package lcvmagws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>tEcritureVenteSKUResponse complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="tEcritureVenteSKUResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="EcritureVenteSKUResult" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tEcritureVenteSKUResponse", propOrder = {
    "ecritureVenteSKUResult"
})
public class TEcritureVenteSKUResponse {

    @XmlElement(name = "EcritureVenteSKUResult", required = true)
    protected String ecritureVenteSKUResult;

    /**
     * 获取ecritureVenteSKUResult属性的值。
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEcritureVenteSKUResult() {
        return ecritureVenteSKUResult;
    }

    /**
     * 设置ecritureVenteSKUResult属性的值。
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEcritureVenteSKUResult(String value) {
        this.ecritureVenteSKUResult = value;
    }

}
