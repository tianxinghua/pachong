
package lcvmagws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>tLectureDetailCdeGesComResponse complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="tLectureDetailCdeGesComResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="LectureDetailCdeGesComResult" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tLectureDetailCdeGesComResponse", propOrder = {
    "lectureDetailCdeGesComResult"
})
public class TLectureDetailCdeGesComResponse {

    @XmlElement(name = "LectureDetailCdeGesComResult", required = true)
    protected String lectureDetailCdeGesComResult;

    /**
     * ��ȡlectureDetailCdeGesComResult���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLectureDetailCdeGesComResult() {
        return lectureDetailCdeGesComResult;
    }

    /**
     * ����lectureDetailCdeGesComResult���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLectureDetailCdeGesComResult(String value) {
        this.lectureDetailCdeGesComResult = value;
    }

}
