package com.shangpin.ephub.product.business.ui.task.dic.DicExportDto;

import com.shangpin.ephub.client.data.mysql.enumeration.TaskType;
import com.shangpin.ephub.client.data.mysql.mapping.dto.HubSupplierValueMappingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.mapping.gateway.HubSupplierValueMappingGateWay;
import com.shangpin.ephub.client.data.mysql.studio.spusupplierunion.dto.SpuSupplierQueryDto;
import com.shangpin.ephub.client.data.mysql.task.dto.HubSpuImportTaskDto;
import com.shangpin.ephub.product.business.service.studio.hubslot.HubSlotSpuService;
import com.shangpin.ephub.product.business.ui.pending.service.IPendingProductService;
import com.shangpin.ephub.product.business.ui.task.pending.export.service.ExportService;
import com.shangpin.ephub.response.HubResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

public class MaterialRequestDto {
    protected Integer pageNo = 1;
    protected Integer startRow;
    protected Integer pageSize = 10;
    protected String supplierMaterial;
    protected String  hubMaterial;
    protected String createUser;
    protected String mappingLevel;

    public String getMappingLevel() {
        return mappingLevel;
    }

    public void setMappingLevel(String mappingLevel) {
        this.mappingLevel = mappingLevel;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getStartRow() {
        return startRow;
    }

    public void setStartRow(Integer startRow) {
        this.startRow = startRow;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getSupplierMaterial() {
        return supplierMaterial;
    }

    public void setSupplierMaterial(String supplierMaterial) {
        this.supplierMaterial = supplierMaterial;
    }

    public String getHubMaterial() {
        return hubMaterial;
    }

    public void setHubMaterial(String hubMaterial) {
        this.hubMaterial = hubMaterial;
    }

    /**
     * <p>Title: ExportController</p>
     * <p>Description: 待处理页（待拍照）的导出 </p>
     * <p>Company: </p>
     * @author lubaijiang
     * @date 2017年7月5日 上午11:21:42
     *
     */

    @RestController
    @RequestMapping("/made-export")
    @Slf4j
    public static class MadeExportController {

        @Autowired
        private ExportService exportService;
        @Autowired
        private IPendingProductService pendingProductService;
        @Autowired
        private HubSlotSpuService slotSpuService;
        @Autowired
        HubSupplierValueMappingGateWay hubSupplierValueMappingGateWay;

        /**
         * 待拍照导出
         * @param
         * @return
         */
        @RequestMapping(value="/made-dic-export",method= RequestMethod.POST)
        public HubResponse<?> exportWaitToShoot(@RequestBody HubSupplierMadeMappingDto madeMappingDtoDto){
            try {
                String remotePath = "pending_export";
                //第一步创建任务
                HubSpuImportTaskDto task = exportService.createAndSaveTaskIntoMysql(madeMappingDtoDto.getCreateUser(), remotePath , TaskType.EXPORT_ORIGIN);
                //第二步发送队列
                HubSupplierValueMappingCriteriaDto hubSupplierValueMappingCriteriaDto = new HubSupplierValueMappingCriteriaDto();
                hubSupplierValueMappingCriteriaDto.createCriteria().andHubValTypeEqualTo(madeMappingDtoDto.getType());
                //获取总条数
                int total = hubSupplierValueMappingGateWay.countByCriteria(hubSupplierValueMappingCriteriaDto);
                madeMappingDtoDto.setPageSize(total);
                boolean bool = exportService.sendTaskToQueue(task.getTaskNo(), TaskType.EXPORT_ORIGIN, madeMappingDtoDto);
                if(bool){
                    return HubResponse.successResp(task.getTaskNo()+":"+task.getSysFileName());
                }
            } catch (Exception e) {
                MadeExportController.log.error("产地导出异常："+e.getMessage(),e);
            }
            return HubResponse.errorResp("产地导出异常");
        }

        /**
         * 已提交导出
         * @param quryDto
         * @return
         */
        @RequestMapping(value="/commited",method=RequestMethod.POST)
        public HubResponse<?> exportCommited(@RequestBody SpuSupplierQueryDto quryDto){
            try {
                String remotePath = "pending_export";
                //第一步创建任务
                HubSpuImportTaskDto task = exportService.createAndSaveTaskIntoMysql(quryDto.getCreateUser(), remotePath , TaskType.EXPORT_COMMITED);
                //第二步发送队列
                quryDto.setPageSize(slotSpuService.countSlotSpu(quryDto));
                boolean bool = exportService.sendTaskToQueue(task.getTaskNo(), TaskType.EXPORT_COMMITED, quryDto);
                if(bool){
                    return HubResponse.successResp(task.getTaskNo()+":"+task.getSysFileName());
                }
            } catch (Exception e) {
                MadeExportController.log.error("已提交页面导出异常："+e.getMessage(),e);
            }
            return HubResponse.errorResp("已提交页面导出异常");
        }
    }
}
