package com.shangpin.iog.mongodao;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.mongodomain.StockLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by lizhongren on 2016/8/3.
 */
@Repository
public class StockDAOImpl {

    @Autowired
    MongoTemplate mongoTemplate;

    public List<StockLog> findListByMap(Map queryMap) throws ServiceException{
        Criteria criteria = new Criteria();
        if(queryMap.containsKey("supplierId")){
            criteria.where("supplierId").is(queryMap.get("supplierId"));
        }else if(queryMap.containsKey("skuNo")){
            criteria.andOperator(Criteria.where("skuNo").is(queryMap.get("skuNo")));
        }else if(queryMap.containsKey("supplierSkuNo")){
            criteria.andOperator(Criteria.where("supplierSkuNo").is(queryMap.get("supplierSkuNo")));
        }else if(queryMap.containsKey("startTime")){
            criteria.andOperator(Criteria.where("createTime").gt(queryMap.get("supplierSkuNo")));
        }else if(queryMap.containsKey("endTime")){
            criteria.andOperator(Criteria.where("endTime").lt(queryMap.get("endTime")));
        }
        return  mongoTemplate.find(new Query(criteria),StockLog.class);

    }
}
