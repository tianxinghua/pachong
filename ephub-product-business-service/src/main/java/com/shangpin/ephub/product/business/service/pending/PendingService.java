package com.shangpin.ephub.product.business.service.pending;

import com.shangpin.ephub.product.business.ui.pending.vo.SpuModelMsgVO;
import com.shangpin.ephub.product.business.ui.pending.vo.SpuPendingAuditQueryVO;
import com.shangpin.ephub.product.business.ui.pending.vo.SpuPendingAuditVO;
import com.shangpin.ephub.product.business.ui.pending.vo.SpuPendingVO;

import java.util.List;

/**
 * Created by loyalty on 16/12/27.
 */
public interface PendingService {

    /**
     * 根据查询条件 获取货号信息
     * @param queryVO
     * @return
     */
    public SpuModelMsgVO getSpuModel(SpuPendingAuditQueryVO queryVO);

    /**
     * 根据货号和品牌 ，获取所有的待审核的数据
     * @param brandNo
     * @param spuModel
     * @return
     */
    public List<SpuPendingVO> getSpuPendingByBrandNoAndSpuModel(String brandNo, String spuModel);

    /**
     * 审核数据
     * @param auditVO
     * @return
     * @throws Exception
     */
    public boolean audit(SpuPendingAuditVO auditVO) throws Exception;
}
