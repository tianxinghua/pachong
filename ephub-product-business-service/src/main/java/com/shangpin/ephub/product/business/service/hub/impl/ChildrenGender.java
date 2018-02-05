package com.shangpin.ephub.product.business.service.hub.impl;

import com.shangpin.ephub.product.business.common.enumeration.ScmGenderType;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lizhongren on 2018/1/31.
 */
public class ChildrenGender {



    /**
     * 女童鞋	A11B02	女童
     男童鞋	A11B04	男童
     婴童鞋	A11B05	女童
     女童装	A11B07	女童
     男童装	A11B08	男童
     婴童装	A11B09	女童
     女童配饰	A11B11	女童
     男童配饰	A11B12	男童

     */
    public static  Map<String,ScmGenderType> childGenderMap = new HashMap<String,ScmGenderType>(){
        {
            put("A11B02",ScmGenderType.WOMAN);
            put("A11B04",ScmGenderType.MAN);
            put("A11B05",ScmGenderType.WOMAN);
            put("A11B07",ScmGenderType.WOMAN);
            put("A11B08",ScmGenderType.MAN);
            put("A11B09",ScmGenderType.WOMAN);
            put("A11B11",ScmGenderType.WOMAN);
            put("A11B12",ScmGenderType.MAN);
        }
    };

    public static ScmGenderType getGender(String categoryNo){
        if(StringUtils.isNotBlank(categoryNo)&&categoryNo.length()>=6){
            categoryNo = categoryNo.substring(0,6);
            if(childGenderMap.containsKey(categoryNo)) return childGenderMap.get(categoryNo);
        }
        return null;
    }
}
