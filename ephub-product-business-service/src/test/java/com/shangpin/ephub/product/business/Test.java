package com.shangpin.ephub.product.business;



import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by lizhongren on 2017/9/7.
 */
public class Test {

    public static void main(String[] args){
        try {
//            List<SlotSpuSendDetailCheckDto> resCheckDtos = new ArrayList<>();
//            SlotSpuSendDetailCheckDto checkDto = new SlotSpuSendDetailCheckDto();
//            checkDto.setResultSign(false);
//            checkDto.setStudioSlotSpuSendDetailId(1L);
//            checkDto.setSlotSpuSupplierId(2L);
//            checkDto.setUserName("sdf");
//            checkDto.setSlotNo("slotno");
//            resCheckDtos.add(checkDto);
//            Map<Long, Long> errSlotSendDetailMap   = resCheckDtos.stream().collect(Collectors.toMap(SlotSpuSendDetailCheckDto::getStudioSlotSpuSendDetailId, SlotSpuSendDetailCheckDto::getSlotSpuSupplierId));
//            System.out.println("size = " + errSlotSendDetailMap.size());
            BigDecimal  a = new BigDecimal("12.24");
            BigDecimal  b = new BigDecimal(12.241).setScale(2,BigDecimal.ROUND_HALF_UP);
            if(!a.equals(b)){
                System.out.println(a);
                System.out.println(b);
                 System.out.println(true);
            }else{
                System.out.println(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
