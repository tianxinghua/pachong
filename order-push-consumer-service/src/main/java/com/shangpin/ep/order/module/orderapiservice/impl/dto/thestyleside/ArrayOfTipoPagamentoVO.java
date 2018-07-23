
package com.shangpin.ep.order.module.orderapiservice.impl.dto.thestyleside;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ArrayOfTipoPagamentoVO complex type的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * 
 * <pre>
 * &lt;complexType name="ArrayOfTipoPagamentoVO">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TipoPagamentoVO" type="{http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO}TipoPagamentoVO" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfTipoPagamentoVO", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Connector.Vidra.Service.VO", propOrder = {
    "tipoPagamentoVO"
})
public class ArrayOfTipoPagamentoVO {

    @XmlElement(name = "TipoPagamentoVO", nillable = true)
    protected List<TipoPagamentoVO> tipoPagamentoVO;

    /**
     * Gets the value of the tipoPagamentoVO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the tipoPagamentoVO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTipoPagamentoVO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link TipoPagamentoVO }
     * 
     * 
     */
    public List<TipoPagamentoVO> getTipoPagamentoVO() {
        if (tipoPagamentoVO == null) {
            tipoPagamentoVO = new ArrayList<TipoPagamentoVO>();
        }
        return this.tipoPagamentoVO;
    }

}
