package Magento;

import org.springframework.stereotype.Service;

@Service
public class PortTypeProxy implements Magento.PortType {
  private String _endpoint = null;
  private Magento.PortType portType = null;
  
  public PortTypeProxy() {
    _initPortTypeProxy();
  }
  
  public PortTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initPortTypeProxy();
  }
  
  private void _initPortTypeProxy() {
    try {
      portType = (new Magento.MagentoServiceLocator()).getPort();
      if (portType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)portType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)portType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (portType != null)
      ((javax.xml.rpc.Stub)portType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public Magento.PortType getPortType() {
    if (portType == null)
      _initPortTypeProxy();
    return portType;
  }
  
  public java.lang.String login(java.lang.String username, java.lang.String apiKey) throws java.rmi.RemoteException{
    if (portType == null)
      _initPortTypeProxy();
    return portType.login(username, apiKey);
  }
  
  public boolean salesOrderAddComment(java.lang.String sessionId, java.lang.String orderIncrementId, java.lang.String status, java.lang.String comment, java.lang.String notify) throws java.rmi.RemoteException{
    if (portType == null)
      _initPortTypeProxy();
    return portType.salesOrderAddComment(sessionId, orderIncrementId, status, comment, notify);
  }
  
  public boolean salesOrderHold(java.lang.String sessionId, java.lang.String orderIncrementId) throws java.rmi.RemoteException{
    if (portType == null)
      _initPortTypeProxy();
    return portType.salesOrderHold(sessionId, orderIncrementId);
  }
  
  public boolean salesOrderUnhold(java.lang.String sessionId, java.lang.String orderIncrementId) throws java.rmi.RemoteException{
    if (portType == null)
      _initPortTypeProxy();
    return portType.salesOrderUnhold(sessionId, orderIncrementId);
  }
  
  public boolean salesOrderCancel(java.lang.String sessionId, java.lang.String orderIncrementId) throws java.rmi.RemoteException{
    if (portType == null)
      _initPortTypeProxy();
    return portType.salesOrderCancel(sessionId, orderIncrementId);
  }
 
}