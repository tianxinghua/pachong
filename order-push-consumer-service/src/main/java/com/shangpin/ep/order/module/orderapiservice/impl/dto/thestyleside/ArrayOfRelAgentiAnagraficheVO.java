
package com.shangpin.ep.order.module.orderapiservice.impl.dto.thestyleside;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ArrayOfRelAgentiAnagraficheVO complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="ArrayOfRelAgentiAnagraficheVO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="RelAgentiAnagraficheVO" type="{http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO}RelAgentiAnagraficheVO" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfRelAgentiAnagraficheVO", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", propOrder = {
    "relAgentiAnagraficheVO"
})
public class ArrayOfRelAgentiAnagraficheVO {

    @XmlElement(name = "RelAgentiAnagraficheVO", nillable = true)
    protected List<RelAgentiAnagraficheVO> relAgentiAnagraficheVO;

    /**
     * Gets the value of the relAgentiAnagraficheVO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the relAgentiAnagraficheVO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRelAgentiAnagraficheVO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RelAgentiAnagraficheVO }
     * 
     * 
     */
    public List<RelAgentiAnagraficheVO> getRelAgentiAnagraficheVO() {
        if (relAgentiAnagraficheVO == null) {
            relAgentiAnagraficheVO = new ArrayList<RelAgentiAnagraficheVO>();
        }
        return this.relAgentiAnagraficheVO;
    }

}
