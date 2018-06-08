
package com.shangpin.ep.order.module.orderapiservice.impl.dto.thestyleside;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ArrayOfArticoloFlatExtVO complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="ArrayOfArticoloFlatExtVO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ArticoloFlatExtVO" type="{http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO}ArticoloFlatExtVO" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfArticoloFlatExtVO", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", propOrder = {
    "articoloFlatExtVO"
})
public class ArrayOfArticoloFlatExtVO {

    @XmlElement(name = "ArticoloFlatExtVO", nillable = true)
    protected List<ArticoloFlatExtVO> articoloFlatExtVO;

    /**
     * Gets the value of the articoloFlatExtVO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the articoloFlatExtVO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getArticoloFlatExtVO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ArticoloFlatExtVO }
     * 
     * 
     */
    public List<ArticoloFlatExtVO> getArticoloFlatExtVO() {
        if (articoloFlatExtVO == null) {
            articoloFlatExtVO = new ArrayList<ArticoloFlatExtVO>();
        }
        return this.articoloFlatExtVO;
    }

}
