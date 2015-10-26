package theclutcher;

import com.shangpin.iog.common.utils.DateTimeUtil;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Test {

	public static void main(String[] args){
//		File file = new File("C:\\Users\\suny\\git\\iog\\theclutcher\\bin\\feedShanping.xml");
//		System.out.println("--------------"+ file.getPath());
//		String ss = "<item>"+      
//      "<g:brand>Cantarelli</g:brand>"+
//				"</item>";
//		 org.jdom.Document document=null;  
//         document=this.load();  
//           
//         Format format =Format.getPrettyFormat();      
//         format.setEncoding("UTF-8");//设置编码格式   
//           
//         StringWriter out=null; //输出对象  
//         String sReturn =""; //输出字符串  
//         XMLOutputter outputter =new XMLOutputter();   
//         out=new StringWriter();   
//         try {  
//            outputter.output(document,out);  
//         } catch (IOException e) {  
//            e.printStackTrace();  
//         }   
//         sReturn=out.toString();   
//         return sReturn;
	//	System.out.println("s =" +  new BigDecimal("1.0").intValue());

//		Calendar calendar = Calendar.getInstance();
//		calendar.setTime();
//		SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = DateTimeUtil.convertFormat("2015-10-22 17:12:00","yyyy-MM-dd HH:mm:ss" );
		Date date1 = DateTimeUtil.convertFormat("2015-10-22 20:12:03","yyyy-MM-dd HH:mm:ss" );
		System.out.println("111 ssss"+ DateTimeUtil.getTimeDifference(date, date1)/(120*1000*60)+"---");
		 ;
		
	}
}
