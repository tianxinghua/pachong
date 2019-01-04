package com.shangpin.iog.mongodao;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.mongobase.BaseMongodbDAO;
import com.shangpin.iog.mongodomain.StockLog;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by lizhongren on 2016/7/28.
 * 库存更新日志
 */
@Repository
public interface StockDAO extends BaseMongodbDAO<StockLog,String> {

    public List<StockLog> findListByMap(Map queryMap) throws ServiceException;

}
