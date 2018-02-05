package com.shangpin.ephub.product.business.service.hub.impl;

import com.shangpin.ephub.product.business.common.enumeration.ScmGenderType;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lizhongren on 2018/1/31.
 */
public class BuGroup {
    /**
     *
     *A11B02	女童鞋	BU1 儿童
     A11B04	男童鞋	BU1 儿童
     A11B07	女童装	BU1 儿童
     A11B08	男童装	BU1 儿童
     A11B11	女童配饰	BU1 儿童
     A11B12	男童配饰	BU1 儿童
     A01B01	女装	BU1 女装
     A05B10	女士服饰配件	BU1 女装
     A15B01	女士内衣	BU1 女装
     A02B01	女鞋	BU2 女鞋
     A03B02	男包	BU3 男包
     A02B02	男鞋	BU3 男鞋
     A01B02	男装	BU3 男装
     A05B11	男士服饰配件	BU3 男装
     A15B02	男士内衣	BU3 男装
     A03B01	女包	BU4 箱包
     A05B02	珠宝饰品	BU5 配饰
     A05B08	商务礼品	BU5 配饰
     A13B01	眼镜类	BU5 眼镜
     A01B11	女士户外运动	停止运营，无需匹配
     A01B12	男士户外运动	停止运营，无需匹配
     A05B09	运动配件	停止运营，无需匹配



     2E86D212-7139-47E3-84Ec-Acb84Cb3001B	礼品卡等
     5f173e90-400a-4f93-9cff-6f8c1e8e0719	BU3 男鞋
     91e28bd9-b544-4679-8afb-75d0dc6b08a3	BU3 男装
     c4fe8f65-4829-4591-a826-6118e602167b	BU2 女鞋
     e604268f-22a0-47e9-99cf-376589e1e86f	渠道运营组
     ceac2440-f874-4ac8-b62b-1367d2f287a1	BU1 女装
     db874447-211b-4a20-90fc-84712cea3a01	BU5 配饰
     4e78af0b-21fc-4b67-9fb6-61d49d7d6e99	BU5 家居
     eec3e0fd-4c10-4924-9903-b8f2445938ad	BU5 美妆
     e6e77d03-9d15-4e60-a001-9fbc93ffdf4d	BU5 手表
     c9d21148-cd6b-4cca-b581-36936daaf40d	BU5 眼镜
     50522ca8-c7bc-460c-93fe-e703969ad1fc	BU6 快时尚
     a56cec80-1ba8-41d2-b3e5-35cdc895b30a	BU4 女包
     889845d0-1650-48e8-acca-15813eebfbd0	尚品运营组
     29b88206-d7ef-4dd8-a895-d2fc1e4aa04d	海外招商部
     94ffeb84-6115-4654-9dab-464fc8994ed4	BU3 男包
     bab9023b-3e69-4f8f-929a-e59d074074d8	Tier2
     5f3b8b34-b919-4f52-a008-a97ea1f6764e	BU1 儿童




     *
     */
    public static Map<String,String> buMap = new HashMap<String,String>(){
        {
            put("A11B02","5f3b8b34-b919-4f52-a008-a97ea1f6764e");//
            put("A11B04","5f3b8b34-b919-4f52-a008-a97ea1f6764e");
            put("A11B07","5f3b8b34-b919-4f52-a008-a97ea1f6764e");
            put("A11B08","5f3b8b34-b919-4f52-a008-a97ea1f6764e");
            put("A11B11","5f3b8b34-b919-4f52-a008-a97ea1f6764e");
            put("A11B12","5f3b8b34-b919-4f52-a008-a97ea1f6764e");
            put("A01B01","ceac2440-f874-4ac8-b62b-1367d2f287a1");
            put("A05B10","ceac2440-f874-4ac8-b62b-1367d2f287a1");
            put("A15B01","ceac2440-f874-4ac8-b62b-1367d2f287a1");
            put("A02B01","c4fe8f65-4829-4591-a826-6118e602167b");
            put("A03B02","94ffeb84-6115-4654-9dab-464fc8994ed4");
            put("A02B02","91e28bd9-b544-4679-8afb-75d0dc6b08a3");
            put("A01B02","91e28bd9-b544-4679-8afb-75d0dc6b08a3");
            put("A15B02","91e28bd9-b544-4679-8afb-75d0dc6b08a3");
            put("A03B01","a56cec80-1ba8-41d2-b3e5-35cdc895b30a");
            put("A05B02","db874447-211b-4a20-90fc-84712cea3a01");
            put("A05B08","db874447-211b-4a20-90fc-84712cea3a01");
            put("A13B01","c9d21148-cd6b-4cca-b581-36936daaf40d");



        }
    };




    public static String getUserGroup(String categoryNo){
        if(StringUtils.isNotBlank(categoryNo)&&categoryNo.length()>=6){
            categoryNo = categoryNo.substring(0,6);
            if(buMap.containsKey(categoryNo)) return buMap.get(categoryNo);
        }
        return "";
    }
}
