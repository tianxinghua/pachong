package com.shangpin.ephub.data.mysql.hub.filter.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.shangpin.ephub.data.mysql.hub.filter.bean.HubFilterRequest;
import com.shangpin.ephub.data.mysql.hub.filter.po.HubFilterResponse;
/**
 * <p>HubWaitSelectGateWay.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author zhaogenchun
 * @date 2016年12月28日 下午16:52:02
 */
@Mapper
public interface HubFilterMapper {
	
	public  List<HubFilterResponse> selectHubBrandByHubCategory(HubFilterRequest request);
}