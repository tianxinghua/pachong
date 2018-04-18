
package org.tempuri;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.9-b130926.1035
 * Generated source version: 2.2
 * 
 */
@WebServiceClient(name = "VidraSvc", targetNamespace = "http://tempuri.org/", wsdlLocation = "http://srv02.maximag.it:8007/VidraSvc?wsdl")
public class VidraSvc
    extends Service
{

    private final static URL VIDRASVC_WSDL_LOCATION;
    private final static WebServiceException VIDRASVC_EXCEPTION;
    private final static QName VIDRASVC_QNAME = new QName("http://tempuri.org/", "VidraSvc");

    static {
        URL url = null;
        WebServiceException e = null;
        try {
            url = new URL("http://srv02.maximag.it:8007/VidraSvc?wsdl");
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        VIDRASVC_WSDL_LOCATION = url;
        VIDRASVC_EXCEPTION = e;
    }

    public VidraSvc() {
        super(__getWsdlLocation(), VIDRASVC_QNAME);
    }

    public VidraSvc(WebServiceFeature... features) {
        super(__getWsdlLocation(), VIDRASVC_QNAME, features);
    }

    public VidraSvc(URL wsdlLocation) {
        super(wsdlLocation, VIDRASVC_QNAME);
    }

    public VidraSvc(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, VIDRASVC_QNAME, features);
    }

    public VidraSvc(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public VidraSvc(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     * 
     * @return
     *     returns IVidraSvcOfArticoloFlatExtVOArticoloFlatVO
     */
    @WebEndpoint(name = "HTTP")
    public IVidraSvcOfArticoloFlatExtVOArticoloFlatVO getHTTP() {
        return super.getPort(new QName("http://tempuri.org/", "HTTP"), IVidraSvcOfArticoloFlatExtVOArticoloFlatVO.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns IVidraSvcOfArticoloFlatExtVOArticoloFlatVO
     */
    @WebEndpoint(name = "HTTP")
    public IVidraSvcOfArticoloFlatExtVOArticoloFlatVO getHTTP(WebServiceFeature... features) {
        return super.getPort(new QName("http://tempuri.org/", "HTTP"), IVidraSvcOfArticoloFlatExtVOArticoloFlatVO.class, features);
    }

    private static URL __getWsdlLocation() {
        if (VIDRASVC_EXCEPTION!= null) {
            throw VIDRASVC_EXCEPTION;
        }
        return VIDRASVC_WSDL_LOCATION;
    }

}
