package com.shangpin.iog.mongodao;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.mongodomain.ProductPicture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Update.update;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by lizhongren on 2016/4/21.
 */
@Repository
public class PictureDAOImpl {
    @Autowired
    MongoTemplate mongoTemplate;


    public void updatePicStatus(String _id) throws ServiceException {
        mongoTemplate.updateFirst(query(where("name").is("Joe")), update("age", 35),ProductPicture.class) ;
    };
}
