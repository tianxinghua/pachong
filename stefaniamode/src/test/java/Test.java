import com.shangpin.iog.common.utils.httpclient.HttpUtil45;
import com.shangpin.iog.common.utils.httpclient.OutTimeConfig;

/**
 * Created by lizhongren on 2018/2/5.
 */
public class Test {

    public static void  main(String[] args){
        String xml = null;
        xml = HttpUtil45.get("https://www.stefaniamode.com/newfeeds/StefaniaMode.xml", new OutTimeConfig(1000 * 60 * 60,
                1000 * 60 * 60, 1000 * 60 * 60), null);

        System.out.println("xml="+xml);
    }
}
