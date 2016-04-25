package com.shangpin.iog.mongodao;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.shangpin.framework.ServiceException;
import com.shangpin.iog.mongodomain.ProductPicture;

/**
 * Created by lizhongren on 2016/4/21.
 */
@Repository
public class PictureDAOImpl {
    @Autowired
    MongoTemplate mongoTemplate;


    public void removePicture(String _id) throws ServiceException {
    	mongoTemplate.remove(query(where("id").is( _id )),ProductPicture.class);
//        mongoTemplate.updateFirst(query(where("name").is( _id )), update("age", 66),Test.class) ;
    };
}
