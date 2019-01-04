/**
 * ExtendedSearchResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package eu.monnalisa.pf;

public class ExtendedSearchResult  extends eu.monnalisa.pf.SearchResult  implements java.io.Serializable {
    private eu.monnalisa.pf.SingleSkuDetail[] articoli;

    public ExtendedSearchResult() {
    }

    public ExtendedSearchResult(
           java.lang.String colors,
           java.lang.String products,
           java.lang.String sizes,
           eu.monnalisa.pf.SingleSkuDetail[] articoli) {
        super(
            colors,
            products,
            sizes);
        this.articoli = articoli;
    }


    /**
     * Gets the articoli value for this ExtendedSearchResult.
     * 
     * @return articoli
     */
    public eu.monnalisa.pf.SingleSkuDetail[] getArticoli() {
        return articoli;
    }


    /**
     * Sets the articoli value for this ExtendedSearchResult.
     * 
     * @param articoli
     */
    public void setArticoli(eu.monnalisa.pf.SingleSkuDetail[] articoli) {
        this.articoli = articoli;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ExtendedSearchResult)) return false;
        ExtendedSearchResult other = (ExtendedSearchResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.articoli==null && other.getArticoli()==null) || 
             (this.articoli!=null &&
              java.util.Arrays.equals(this.articoli, other.getArticoli())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getArticoli() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getArticoli());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getArticoli(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ExtendedSearchResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://pf.monnalisa.eu/", "extendedSearchResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("articoli");
        elemField.setXmlName(new javax.xml.namespace.QName("", "articoli"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://pf.monnalisa.eu/", "articolo"));
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
