
package lcvmagws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>tLectureDesModelesAvecPrixResponse complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="tLectureDesModelesAvecPrixResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="LectureDesModelesAvecPrixResult" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tLectureDesModelesAvecPrixResponse", propOrder = {
    "lectureDesModelesAvecPrixResult"
})
public class TLectureDesModelesAvecPrixResponse {

    @XmlElement(name = "LectureDesModelesAvecPrixResult", required = true)
    protected String lectureDesModelesAvecPrixResult;

    /**
     * ��ȡlectureDesModelesAvecPrixResult���Ե�ֵ��
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLectureDesModelesAvecPrixResult() {
        return lectureDesModelesAvecPrixResult;
    }

    /**
     * ����lectureDesModelesAvecPrixResult���Ե�ֵ��
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLectureDesModelesAvecPrixResult(String value) {
        this.lectureDesModelesAvecPrixResult = value;
    }

}
