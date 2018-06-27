
package com.shangpin.ep.order.module.orderapiservice.impl.dto.thestyleside;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>TrackingStatus的 Java 类。
 * 
 * <p>以下模式片段指定包含在此类中的预期内容。
 * <p>
 * <pre>
 * &lt;simpleType name="TrackingStatus">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="InCarico"/>
 *     &lt;enumeration value="Ritirata"/>
 *     &lt;enumeration value="Partita"/>
 *     &lt;enumeration value="InTransito"/>
 *     &lt;enumeration value="InConsegna"/>
 *     &lt;enumeration value="Consegnata"/>
 *     &lt;enumeration value="InGiacenza"/>
 *     &lt;enumeration value="Altro"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TrackingStatus", namespace = "http://schemas.datacontract.org/2004/07/MaxiMag.Spedizioni.Common")
@XmlEnum
public enum TrackingStatus {

    @XmlEnumValue("InCarico")
    IN_CARICO("InCarico"),
    @XmlEnumValue("Ritirata")
    RITIRATA("Ritirata"),
    @XmlEnumValue("Partita")
    PARTITA("Partita"),
    @XmlEnumValue("InTransito")
    IN_TRANSITO("InTransito"),
    @XmlEnumValue("InConsegna")
    IN_CONSEGNA("InConsegna"),
    @XmlEnumValue("Consegnata")
    CONSEGNATA("Consegnata"),
    @XmlEnumValue("InGiacenza")
    IN_GIACENZA("InGiacenza"),
    @XmlEnumValue("Altro")
    ALTRO("Altro");
    private final String value;

    TrackingStatus(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static TrackingStatus fromValue(String v) {
        for (TrackingStatus c: TrackingStatus.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
