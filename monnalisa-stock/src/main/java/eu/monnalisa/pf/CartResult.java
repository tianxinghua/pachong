/**
 * CartResult.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package eu.monnalisa.pf;

public class CartResult  extends eu.monnalisa.pf.OrdinaResult  implements java.io.Serializable {
    private eu.monnalisa.pf.ArticleFull[] cartItems;

    public CartResult() {
    }

    public CartResult(
           java.lang.String message,
           boolean result,
           eu.monnalisa.pf.ArticleFull[] cartItems) {
        super(
            message,
            result);
        this.cartItems = cartItems;
    }


    /**
     * Gets the cartItems value for this CartResult.
     * 
     * @return cartItems
     */
    public eu.monnalisa.pf.ArticleFull[] getCartItems() {
        return cartItems;
    }


    /**
     * Sets the cartItems value for this CartResult.
     * 
     * @param cartItems
     */
    public void setCartItems(eu.monnalisa.pf.ArticleFull[] cartItems) {
        this.cartItems = cartItems;
    }

    public eu.monnalisa.pf.ArticleFull getCartItems(int i) {
        return this.cartItems[i];
    }

    public void setCartItems(int i, eu.monnalisa.pf.ArticleFull _value) {
        this.cartItems[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CartResult)) return false;
        CartResult other = (CartResult) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.cartItems==null && other.getCartItems()==null) || 
             (this.cartItems!=null &&
              java.util.Arrays.equals(this.cartItems, other.getCartItems())));
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
        if (getCartItems() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getCartItems());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getCartItems(), i);
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
        new org.apache.axis.description.TypeDesc(CartResult.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://pf.monnalisa.eu/", "cartResult"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cartItems");
        elemField.setXmlName(new javax.xml.namespace.QName("", "cartItems"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://pf.monnalisa.eu/", "articleFull"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
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
