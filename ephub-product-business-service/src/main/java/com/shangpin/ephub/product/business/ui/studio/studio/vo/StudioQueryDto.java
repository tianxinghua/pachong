package com.shangpin.ephub.product.business.ui.studio.studio.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.List;

/**
 * Created by Administrator on 2017/6/8.
 */
@Setter
@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudioQueryDto implements Serializable {
    private static final long serialVersionUID = 6030866700053745149L;

    private String supplierId;
    private String slotNo;
    private String spuNo;
    private String slotSSIds; //待拍照的SPU下挂供货商 表字段Id SlotSpuSupplierId多个，使用","分隔
    private Long slotSSId; //待拍照的SPU下挂供货商 表字段Id SlotSpuSupplierId
    private Long slotSSDId; //待拍照的SPU发货明细Id
    private String createUser;
    private Long studioSlotId;

    private String trackName; //物流公司
    private String trackingNo; //物流单号

    private String memo; //备注，主要是用来记录地址的
//    private Byte status;
//    private String type;

//    public Long getSupplierId(){
//        return  supplierId == null ? null : Long.parseLong(supplierId);
//    }
//    public Byte getStatus(){
//        return  status == null ? null : Byte.valueOf(status);
//    }
}
