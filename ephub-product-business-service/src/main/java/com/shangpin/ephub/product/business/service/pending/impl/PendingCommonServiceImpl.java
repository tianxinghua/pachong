package com.shangpin.ephub.product.business.service.pending.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.ephub.client.message.pending.body.spu.PendingSpu;
import com.shangpin.ephub.product.business.service.pending.PendingCommonService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by lizhongren on 2017/1/19.
 */
@Service
public class PendingCommonServiceImpl implements PendingCommonService{

    static Map<String, String> genderStaticMap = null;

    static Map<String, Map<String, String>> supplierCategoryMappingStaticMap = null;

    static Map<String, String> brandStaticMap = null;

    static Map<String, String> colorStaticMap = null;

    static Map<String, String> seasonStaticMap = null;

    static Map<String, String> materialStaticMap = null;

    static Map<String, String> originStaticMap = null;

    /**
     * 品牌货号映射表 key : hub的品牌编号 value：Map<String,String> key:正则表单规则 value ： 品类值
     */
    static Map<String, Map<String, String>> brandModelStaticMap = null;//

    static Map<String, String> hubGenderStaticMap = null;

    static Map<String, String> hubBrandStaticMap = null;

    static Map<String, String> hubColorStaticMap = null;

    static Map<String, String> hubSeasonStaticMap = null;

    /**
     * 存放 supplierId_supplierBrand，filterFlag 对应关系
     */
    static Map<String, Byte> hubSupplierBrandFlag = null;
    /**
     * 存放 supplierId_supplierSeason，filterFlag 对应关系
     */
    static Map<String, Byte> hubSeasonFlag = null;

    ObjectMapper mapper =new ObjectMapper();

    @Override
    public boolean handlePendingProduct(PendingSpu pendingSpu) throws Exception {
        return false;
    }
}
