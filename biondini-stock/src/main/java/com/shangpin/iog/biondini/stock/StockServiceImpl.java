package com.shangpin.iog.biondini.stock;

import java.util.Collection;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.shangpin.framework.ServiceException;
import com.shangpin.ice.ice.AbsUpdateProductStock;
import com.shangpin.iog.biondini.axis2.LcvMagWSStub;
import com.shangpin.iog.biondini.axis2.LcvMagWSStub.LectureDuStock1;
import com.shangpin.iog.biondini.axis2.LcvMagWSStub.LectureDuStock1Response;
import com.shangpin.iog.biondini.axis2.LcvMagWSStub.TLectureDuStock1;

@Component
public class StockServiceImpl extends AbsUpdateProductStock{
	
	private static Logger logger = Logger.getLogger("info");
	private static ResourceBundle bdl = null;
	private static String user = null;
	private static String password = null;
	static {
		if (null == bdl)
			bdl = ResourceBundle.getBundle("conf");
		user = bdl.getString("user");
		password = bdl.getString("password");
	}

	@Override
	public Map<String, String> grabStock(Collection<String> skuNo)
			throws ServiceException, Exception {
		LcvMagWSStub stub = new LcvMagWSStub();
		LectureDuStock1 request = new LectureDuStock1();
		TLectureDuStock1 tl = new TLectureDuStock1();
		tl.setSUser(user);
		tl.setSMdp(password);
		tl.setBCData("");
		tl.setSArticle("");
		request.setLectureDuStock1(tl);
		LectureDuStock1Response response = stub.lectureDuStock1(request);
		String result = response.getLectureDuStock1Response().getLectureDuStock1Result();
		System.out.println(result);
		return null;
	}
	
	public static void main(String[] args) {
		StockServiceImpl s = new StockServiceImpl();
		try {
			s.grabStock(null);
		} catch (ServiceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
