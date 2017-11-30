/**
 * ExtraInfoSearchResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package eu.monnalisa.pf;

public class ExtraInfoSearchResult  implements java.io.Serializable {
    private java.lang.String items;

    private java.lang.String output_type;

    public ExtraInfoSearchResult() {
    }

    public ExtraInfoSearchResult(
           java.lang.String items,
           java.lang.String output_type) {
           this.items = items;
           this.output_type = output_type;
    }


    /**
     * Gets the items value for this ExtraInfoSearchResult.
     * 
     * @return items
     */
    public java.lang.String getItems() {
        return items;
    }


    /**
     * Sets the items value for this ExtraInfoSearchResult.
     * 
     * @param items
     */
    public void setItems(java.lang.String items) {
        this.items = items;
    }


    /**
     * Gets the output_type value for this ExtraInfoSearchResult.
     * 
     * @return output_type
     */
    public java.lang.String getOutput_type() {
        return output_type;
    }


    /**
     * Sets the output_type value for this ExtraInfoSearchResult.
     * 
     * @param output_type
     */
    public void setOutput_type(java.lang.String output_type) {
        this.output_type = output_type;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ExtraInfoSearchResult)) return false;
        ExtraInfoSearchResult other = (ExtraInfoSearchResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.items==null && other.getItems()==null) || 
             (this.items!=null &&
              this.items.equals(other.getItems()))) &&
            ((this.output_type==null && other.getOutput_type()==null) || 
             (this.output_type!=null &&
              this.output_type.equals(other.getOutput_type())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getItems() != null) {
            _hashCode += getItems().hashCode();
        }
        if (getOutput_type() != null) {
            _hashCode += getOutput_type().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ExtraInfoSearchResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://pf.monnalisa.eu/", "extraInfoSearchResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("items");
        elemField.setXmlName(new javax.xml.namespace.QName("", "items"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("output_type");
        elemField.setXmlName(new javax.xml.namespace.QName("", "output_type"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
