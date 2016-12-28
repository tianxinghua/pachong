package com.shangpin.ephub.data.mysql.service;

import com.shangpin.ephub.data.mysql.service.vo.SpuModelVO;
import com.shangpin.ephub.data.mysql.service.vo.SpuPendingAuditVO;

/**
 * Created by loyalty on 16/12/27.
 */
public interface PengingToHubService {


    public boolean auditPending(SpuModelVO spuModelVO) throws Exception;


}
