/**
 * SingleSkuDetail.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package eu.monnalisa.pf;

public class SingleSkuDetail  implements java.io.Serializable {
    private eu.monnalisa.pf.TagliaColori[] taglie;

    private int qta;  // attribute

    private java.lang.String sku;  // attribute

    public SingleSkuDetail() {
    }

    public SingleSkuDetail(
           eu.monnalisa.pf.TagliaColori[] taglie,
           int qta,
           java.lang.String sku) {
           this.taglie = taglie;
           this.qta = qta;
           this.sku = sku;
    }


    /**
     * Gets the taglie value for this SingleSkuDetail.
     * 
     * @return taglie
     */
    public eu.monnalisa.pf.TagliaColori[] getTaglie() {
        return taglie;
    }


    /**
     * Sets the taglie value for this SingleSkuDetail.
     * 
     * @param taglie
     */
    public void setTaglie(eu.monnalisa.pf.TagliaColori[] taglie) {
        this.taglie = taglie;
    }


    /**
     * Gets the qta value for this SingleSkuDetail.
     * 
     * @return qta
     */
    public int getQta() {
        return qta;
    }


    /**
     * Sets the qta value for this SingleSkuDetail.
     * 
     * @param qta
     */
    public void setQta(int qta) {
        this.qta = qta;
    }


    /**
     * Gets the sku value for this SingleSkuDetail.
     * 
     * @return sku
     */
    public java.lang.String getSku() {
        return sku;
    }


    /**
     * Sets the sku value for this SingleSkuDetail.
     * 
     * @param sku
     */
    public void setSku(java.lang.String sku) {
        this.sku = sku;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SingleSkuDetail)) return false;
        SingleSkuDetail other = (SingleSkuDetail) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.taglie==null && other.getTaglie()==null) || 
             (this.taglie!=null &&
              java.util.Arrays.equals(this.taglie, other.getTaglie()))) &&
            this.qta == other.getQta() &&
            ((this.sku==null && other.getSku()==null) || 
             (this.sku!=null &&
              this.sku.equals(other.getSku())));
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
        if (getTaglie() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTaglie());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTaglie(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getQta();
        if (getSku() != null) {
            _hashCode += getSku().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SingleSkuDetail.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://pf.monnalisa.eu/", "singleSkuDetail"));
        org.apache.axis.description.AttributeDesc attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("qta");
        attrField.setXmlName(new javax.xml.namespace.QName("", "qta"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        typeDesc.addFieldDesc(attrField);
        attrField = new org.apache.axis.description.AttributeDesc();
        attrField.setFieldName("sku");
        attrField.setXmlName(new javax.xml.namespace.QName("", "sku"));
        attrField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        typeDesc.addFieldDesc(attrField);
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("taglie");
        elemField.setXmlName(new javax.xml.namespace.QName("", "taglie"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://pf.monnalisa.eu/", "taglia"));
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
