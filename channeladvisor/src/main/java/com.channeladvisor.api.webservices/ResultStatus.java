
package com.channeladvisor.api.webservices;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>ResultStatus的 Java 类。
 *
 * <p>以下模式片段指定包含在此类中的预期内容。
 * <p>
 * <pre>
 * &lt;simpleType name="ResultStatus"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="Success"/&gt;
 *     &lt;enumeration value="Failure"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 *
 */
@XmlType(name = "ResultStatus")
@XmlEnum
public enum ResultStatus {

    @XmlEnumValue("Success")
    SUCCESS("Success"),
    @XmlEnumValue("Failure")
    FAILURE("Failure");
    private final String value;

    ResultStatus(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ResultStatus fromValue(String v) {
        for (ResultStatus c: ResultStatus.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
