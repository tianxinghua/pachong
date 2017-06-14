package com.shangpin.ephub.product.business.ui.studio.studio.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;

import java.io.Serializable;
import java.security.PublicKey;

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
    private Long slotSSId; //待拍照的SPU下挂供货商 表字段Id SlotSpuSupplierId
    private String createUser;
//    private Byte status;
//    private String type;

//    public Long getSupplierId(){
//        return  supplierId == null ? null : Long.parseLong(supplierId);
//    }
//    public Byte getStatus(){
//        return  status == null ? null : Byte.valueOf(status);
//    }
}
