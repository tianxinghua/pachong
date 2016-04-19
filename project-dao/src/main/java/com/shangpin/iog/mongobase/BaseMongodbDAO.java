package com.shangpin.iog.mongobase;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * Created by loyalty on 15/4/10.
 * mongodb
 *
 */

@NoRepositoryBean
public interface BaseMongodbDAO<T,ID extends Serializable> extends MongoRepository<T,ID>{


}
