/**
 * MonnalisaWSServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package eu.monnalisa.pf;

public class MonnalisaWSServiceLocator extends org.apache.axis.client.Service implements eu.monnalisa.pf.MonnalisaWSService {

    public MonnalisaWSServiceLocator() {
    }


    public MonnalisaWSServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public MonnalisaWSServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for MonnalisaWSPort
    private java.lang.String MonnalisaWSPort_address = "http://139.196.218.154:8080/Monnalisa_SOA_EE_WEB/MonnalisaWS";

    public java.lang.String getMonnalisaWSPortAddress() {
        return MonnalisaWSPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String MonnalisaWSPortWSDDServiceName = "MonnalisaWSPort";

    public java.lang.String getMonnalisaWSPortWSDDServiceName() {
        return MonnalisaWSPortWSDDServiceName;
    }

    public void setMonnalisaWSPortWSDDServiceName(java.lang.String name) {
        MonnalisaWSPortWSDDServiceName = name;
    }

    public eu.monnalisa.pf.MonnalisaWS getMonnalisaWSPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(MonnalisaWSPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getMonnalisaWSPort(endpoint);
    }

    public eu.monnalisa.pf.MonnalisaWS getMonnalisaWSPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            eu.monnalisa.pf.MonnalisaWSServiceSoapBindingStub _stub = new eu.monnalisa.pf.MonnalisaWSServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getMonnalisaWSPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setMonnalisaWSPortEndpointAddress(java.lang.String address) {
        MonnalisaWSPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (eu.monnalisa.pf.MonnalisaWS.class.isAssignableFrom(serviceEndpointInterface)) {
                eu.monnalisa.pf.MonnalisaWSServiceSoapBindingStub _stub = new eu.monnalisa.pf.MonnalisaWSServiceSoapBindingStub(new java.net.URL(MonnalisaWSPort_address), this);
                _stub.setPortName(getMonnalisaWSPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("MonnalisaWSPort".equals(inputPortName)) {
            return getMonnalisaWSPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://pf.monnalisa.eu/", "MonnalisaWSService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://pf.monnalisa.eu/", "MonnalisaWSPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("MonnalisaWSPort".equals(portName)) {
            setMonnalisaWSPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
