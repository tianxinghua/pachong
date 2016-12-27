package com.shangpin.ephub.data.mysql.service.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by loyalty on 16/12/23.
 */
@Setter
@Getter
public class SpuPendingAuditVO  extends SpuPendingVO{

    /**
     * 0 :信息待完善 1 : 待复核 2 :已处理(审核通过) 3：无法处理 4:过滤不处理
     */
    private String  auditStatus;





}
