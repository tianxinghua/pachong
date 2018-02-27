/**
 * SearchResultMag.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package eu.monnalisa.pf;

public class SearchResultMag  extends eu.monnalisa.pf.SearchResult  implements java.io.Serializable {
    private java.lang.String extraData;

    private java.lang.String items;

    private java.lang.String magazzini;

    public SearchResultMag() {
    }

    public SearchResultMag(
           java.lang.String colors,
           java.lang.String products,
           java.lang.String sizes,
           java.lang.String extraData,
           java.lang.String items,
           java.lang.String magazzini) {
        super(
            colors,
            products,
            sizes);
        this.extraData = extraData;
        this.items = items;
        this.magazzini = magazzini;
    }


    /**
     * Gets the extraData value for this SearchResultMag.
     * 
     * @return extraData
     */
    public java.lang.String getExtraData() {
        return extraData;
    }


    /**
     * Sets the extraData value for this SearchResultMag.
     * 
     * @param extraData
     */
    public void setExtraData(java.lang.String extraData) {
        this.extraData = extraData;
    }


    /**
     * Gets the items value for this SearchResultMag.
     * 
     * @return items
     */
    public java.lang.String getItems() {
        return items;
    }


    /**
     * Sets the items value for this SearchResultMag.
     * 
     * @param items
     */
    public void setItems(java.lang.String items) {
        this.items = items;
    }


    /**
     * Gets the magazzini value for this SearchResultMag.
     * 
     * @return magazzini
     */
    public java.lang.String getMagazzini() {
        return magazzini;
    }


    /**
     * Sets the magazzini value for this SearchResultMag.
     * 
     * @param magazzini
     */
    public void setMagazzini(java.lang.String magazzini) {
        this.magazzini = magazzini;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof SearchResultMag)) return false;
        SearchResultMag other = (SearchResultMag) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.extraData==null && other.getExtraData()==null) || 
             (this.extraData!=null &&
              this.extraData.equals(other.getExtraData()))) &&
            ((this.items==null && other.getItems()==null) || 
             (this.items!=null &&
              this.items.equals(other.getItems()))) &&
            ((this.magazzini==null && other.getMagazzini()==null) || 
             (this.magazzini!=null &&
              this.magazzini.equals(other.getMagazzini())));
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
        if (getExtraData() != null) {
            _hashCode += getExtraData().hashCode();
        }
        if (getItems() != null) {
            _hashCode += getItems().hashCode();
        }
        if (getMagazzini() != null) {
            _hashCode += getMagazzini().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(SearchResultMag.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://pf.monnalisa.eu/", "searchResultMag"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("extraData");
        elemField.setXmlName(new javax.xml.namespace.QName("", "extraData"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("items");
        elemField.setXmlName(new javax.xml.namespace.QName("", "items"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("magazzini");
        elemField.setXmlName(new javax.xml.namespace.QName("", "magazzini"));
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
