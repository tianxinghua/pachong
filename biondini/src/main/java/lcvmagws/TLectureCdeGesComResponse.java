
package lcvmagws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>tLectureCdeGesComResponse complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="tLectureCdeGesComResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="LectureCdeGesComResult" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tLectureCdeGesComResponse", propOrder = {
    "lectureCdeGesComResult"
})
public class TLectureCdeGesComResponse {

    @XmlElement(name = "LectureCdeGesComResult", required = true)
    protected String lectureCdeGesComResult;

    /**
     * ��ȡlectureCdeGesComResult���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLectureCdeGesComResult() {
        return lectureCdeGesComResult;
    }

    /**
     * ����lectureCdeGesComResult���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLectureCdeGesComResult(String value) {
        this.lectureCdeGesComResult = value;
    }

}
