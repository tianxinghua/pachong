package com.shangpin.ephub.product.business.ui.studio.studio.vo;

import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/6/13.
 */
@Getter
@Setter
@ToString
public class SlotInfo extends StudioSlotDto implements Serializable {

    private static final long serialVersionUID = -9022748561440545730L;

    /*最大货品数据
    * */
    private int maxNum;

    /*最小货品数据
    * */
    private int minNum;

    /*当前商品数
    * */
    private long countNum;

    /**
     * 映射商品分类编号 1
     */
    private List<String> categoryFirst;

    /*
    * 映射商品分类编号 1
    * */
    private List<String> categorySecond;

    /**************************************/
    /**
     * studio联系人
     */
    private String contacts;
    /*
    * studio 联系方式
    * */
    private String contactInfo;

    private String telephone;
    private String email;

    private String country;
    private String address;
    /**************************************/
}
