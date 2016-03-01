import java.util.Iterator;
import java.util.concurrent.Executor;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;

import lcvmagws.LcvMagWS;
import lcvmagws.LcvMagWSSOAPPortType;


public class Test {

	public static void main(String[] args) {
//		
//		JYZXService se = new JYZXService();
//		
//		JYZXServicePortType ss = se.getJYZXServiceHttpport();
//		
//		System.out.println(ss.getZxInfo("1","urn:getZxInfo","getZxInfo"));
//		
//		
		
//		
		LcvMagWS ms = new LcvMagWS();
		Iterator<javax.xml.namespace.QName> name = ms.getPorts();
		 while(name.hasNext()){
			 QName str = (QName) name.next();
			  System.out.println(str.getLocalPart()+"---"+str.getNamespaceURI()+"--"+str.getPrefix());
			 }
//		ms.SERVICE;
		LcvMagWSSOAPPortType type = ms.getLcvMagWSSOAPPort();
		type.lecturePLA("", "BION456","INI123");
		System.out.println(type.lecturePLA("", "BION456","INI123"));

	}

}
