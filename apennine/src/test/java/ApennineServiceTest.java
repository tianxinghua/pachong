import com.shangpin.iog.apennine.utils.ApennineHttpUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;

/**
 * Created by sunny on 2015/6/5.
 */
/*@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
        "classpath:dataSource.xml"
})
@TransactionConfiguration(transactionManager="shopTransaction",defaultRollback=false)
@Transactional("shopTransaction")*/
public class ApennineServiceTest /*extends AbstractTransactionalJUnit4SpringContextTests*/ {



    @Autowired
    ApennineHttpUtil httpService;

   /* @Test
    public void settlementTest(){
        try {
            settlementService.createSettlement();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }*/

}
