package com.shangpin.ephub.data.mysql.hub.waitselect.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.shangpin.ephub.data.mysql.hub.waitselect.bean.HubWaitSelectDetailRequest;
import com.shangpin.ephub.data.mysql.hub.waitselect.bean.HubWaitSelectRequest;
import com.shangpin.ephub.data.mysql.hub.waitselect.bean.HubWaitSelectRequestWithPage;
import com.shangpin.ephub.data.mysql.hub.waitselect.po.HubWaitSelectResponse;
/**
 * <p>HubWaitSelectGateWay.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月28日 下午16:52:02
 */
@Mapper
public interface HubWaitSelectMapper {
    Long count(HubWaitSelectRequest hubWaitSelect);
    List<HubWaitSelectResponse> selectList(HubWaitSelectRequestWithPage hubWaitSelectWithPage);
	List<HubWaitSelectResponse> selectDetail(HubWaitSelectDetailRequest criteriaWithRowBounds);
}