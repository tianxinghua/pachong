package com.shangpin.pending.product.consumer.supplier.common;

import com.shangpin.commons.redis.IShangpinRedis;
import com.shangpin.ephub.client.data.mysql.categroy.dto.HubSupplierCategroyDicDto;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingDto;
import com.shangpin.pending.product.consumer.common.ConstantProperty;
import com.shangpin.pending.product.consumer.common.DateUtils;
import com.shangpin.pending.product.consumer.common.enumeration.SupplierValueMappingType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lizhongren on 2017/2/8.
 */
@Component
@Slf4j
public class PendingCommonHandler {
    @Autowired
    private DataServiceHandler dataServiceHandler;

    @Autowired
    private IShangpinRedis shangpinRedis;

    static Integer isCurrentMin  =   DateUtils.getCurrentMin();

    public  String  getSpCategoryValue(String supplierCategory) throws Exception {
        //先判断设置的时间是否有值  无值得话  品类重新处理
        String timeSign = shangpinRedis.get(ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_TIME_KEY);
        if(StringUtils.isBlank(timeSign)){
            shangpinRedis.hdel(ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_KEY);
            Map<String, String>  categoryMap = new HashMap<>() ;
            List<HubSupplierValueMappingDto> supplierValueMappingDtos = dataServiceHandler
                    .getHubSupplierValueMappingByType(SupplierValueMappingType.TYPE_CATEGORY.getIndex());
            if (null != supplierValueMappingDtos && supplierValueMappingDtos.size() > 0) {
                for (HubSupplierValueMappingDto dto : supplierValueMappingDtos) {
                    if (StringUtils.isBlank(dto.getSupplierVal()))    continue;
                    categoryMap.put( dto.getSupplierVal().trim().toUpperCase(),  dto.getHubVal());
                }
                shangpinRedis.hmset(ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_KEY,categoryMap);
                shangpinRedis.setex(ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_TIME_KEY,ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_TIME*1000,"1");
            }
        }
        return getSpCategoryValueFromRedis(supplierCategory);

    }

    /**
     * 如果内部Map的成员很多，那么涉及到遍历整个内部Map的操作，
     * 由于Redis单线程模型的缘故，这个遍历操作可能会比较耗时，而另其它客户端的请求完全不响应，这点需要格外注意
     * @return
     */
    @Deprecated
    private Map<String,String> getCategoryMapFromRedis(){
        Long start = System.currentTimeMillis();
        Map<String,String> map =  shangpinRedis.hgetAll(ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_KEY);
        log.debug("获取品类对照耗时：" + (System.currentTimeMillis() - start )+" 毫秒");
        return   map;
    }

    private String  getSpCategoryValueFromRedis(String supplierCategory){
        List<String> mapValue = shangpinRedis.hmget(ConstantProperty.REDIS_EPHUB_CATEGORY_COMMON_MAPPING_MAP_KEY,supplierCategory) ;
        if(null!=mapValue&&mapValue.size()>0){
            return mapValue.get(0);
        }else{
            return supplierCategory;
        }
    }

    /**
     * 在指定时间段 重新获取所有数据
     *
     * @return
     */
    public  boolean isNeedHandle() {
        int min = DateUtils.getCurrentMin();
        if(min-isCurrentMin>=5||min-isCurrentMin<0){
            isCurrentMin = min;
            return true;
        } else {
            return false;
        }

    }
}
