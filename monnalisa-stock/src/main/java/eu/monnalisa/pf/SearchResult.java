/**
 * SearchResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package eu.monnalisa.pf;

public class SearchResult  implements java.io.Serializable {
    private java.lang.String colors;

    private java.lang.String products;

    private java.lang.String sizes;

    public SearchResult() {
    }

    public SearchResult(
           java.lang.String colors,
           java.lang.String products,
           java.lang.String sizes) {
           this.colors = colors;
           this.products = products;
           this.sizes = sizes;
    }


    /**
     * Gets the colors value for this SearchResult.
     * 
     * @return colors
     */
    public java.lang.String getColors() {
        return colors;
    }


    /**
     * Sets the colors value for this SearchResult.
     * 
     * @param colors
     */
    public void setColors(java.lang.String colors) {
        this.colors = colors;
    }


    /**
     * Gets the products value for this SearchResult.
     * 
     * @return products
     */
    public java.lang.String getProducts() {
        return products;
    }


    /**
     * Sets the products value for this SearchResult.
     * 
     * @param products
     */
    public void setProducts(java.lang.String products) {
        this.products = products;
    }


    /**
     * Gets the sizes value for this SearchResult.
     * 
     * @return sizes
     */
    public java.lang.String getSizes() {
        return sizes;
    }


    /**
     * Sets the sizes value for this SearchResult.
     * 
     * @param sizes
     */
    public void setSizes(java.lang.String sizes) {
        this.sizes = sizes;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SearchResult)) return false;
        SearchResult other = (SearchResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.colors==null && other.getColors()==null) || 
             (this.colors!=null &&
              this.colors.equals(other.getColors()))) &&
            ((this.products==null && other.getProducts()==null) || 
             (this.products!=null &&
              this.products.equals(other.getProducts()))) &&
            ((this.sizes==null && other.getSizes()==null) || 
             (this.sizes!=null &&
              this.sizes.equals(other.getSizes())));
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
        if (getColors() != null) {
            _hashCode += getColors().hashCode();
        }
        if (getProducts() != null) {
            _hashCode += getProducts().hashCode();
        }
        if (getSizes() != null) {
            _hashCode += getSizes().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SearchResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://pf.monnalisa.eu/", "searchResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("colors");
        elemField.setXmlName(new javax.xml.namespace.QName("", "colors"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("products");
        elemField.setXmlName(new javax.xml.namespace.QName("", "products"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sizes");
        elemField.setXmlName(new javax.xml.namespace.QName("", "sizes"));
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
