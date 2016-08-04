package com.shangpin.iog.mongodao;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.mongodomain.StockLog;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by lizhongren on 2016/8/3.
 */
@Repository
public class StockDAOImpl {



    public List<StockLog> findListByMap(Map queryMap) throws ServiceException{
        return null;
    }
}
