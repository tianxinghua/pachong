
package org.datacontract.schemas._2004._07.maximag_connector_vidra_service;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ArrayOfArticoloFlatVO complex type�� Java �ࡣ
 * 
 * <p>����ģʽƬ��ָ�������ڴ����е�Ԥ�����ݡ�
 * 
 * <pre>
 * &lt;complexType name="ArrayOfArticoloFlatVO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ArticoloFlatVO" type="{http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO}ArticoloFlatVO" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfArticoloFlatVO", propOrder = {
    "articoloFlatVO"
})
public class ArrayOfArticoloFlatVO {

    @XmlElement(name = "ArticoloFlatVO", nillable = true)
    protected List<ArticoloFlatVO> articoloFlatVO;

    /**
     * Gets the value of the articoloFlatVO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the articoloFlatVO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getArticoloFlatVO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArticoloFlatVO }
     * 
     * 
     */
    public List<ArticoloFlatVO> getArticoloFlatVO() {
        if (articoloFlatVO == null) {
            articoloFlatVO = new ArrayList<ArticoloFlatVO>();
        }
        return this.articoloFlatVO;
    }

}
