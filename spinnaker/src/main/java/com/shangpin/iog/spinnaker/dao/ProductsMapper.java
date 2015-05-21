package com.shangpin.iog.spinnaker.dao;




import com.shangpin.iog.dao.base.IBaseDao;
import com.shangpin.iog.dao.base.Mapper;
import com.shangpin.iog.spinnaker.domain.Product;

import java.util.Date;
import java.util.List;

@Mapper
public interface ProductsMapper extends IBaseDao<Product> {


    /**
     * 根据品类和最后修改时间获取产品
     * @param categoryId 品类ID
     * @param startDate  开始时间
     * @param endDate    结束时间
     * @param pageIndex  页码
     * @param pageSize   每页大小
     * @return
     */
	List<Product> findListByCategoryAndLastDate(String categoryId,Date startDate,Date endDate,int pageIndex,int pageSize);
}