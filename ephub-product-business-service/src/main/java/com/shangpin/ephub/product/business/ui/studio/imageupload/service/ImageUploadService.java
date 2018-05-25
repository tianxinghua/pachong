package com.shangpin.ephub.product.business.ui.studio.imageupload.service;

import java.util.List;
import java.util.Map;

import com.shangpin.ephub.client.data.mysql.studio.spu.dto.HubSlotSpuDto;
import com.shangpin.ephub.client.data.mysql.studio.supplier.dto.HubSlotSpuSupplierDto;
import com.shangpin.ephub.client.data.studio.enumeration.UploadPicSign;
import com.shangpin.ephub.product.business.ui.studio.common.operation.dto.OperationQuery;
import com.shangpin.ephub.product.business.ui.studio.common.operation.vo.StudioSlotVo;
import com.shangpin.ephub.product.business.ui.studio.common.operation.vo.detail.StudioSlotSpuSendDetailVo;
import com.shangpin.ephub.response.HubResponse;
/**
 * <p>Title: ImageUploadService</p>
 * <p>Description: 图片上传页面所有的业务逻辑 </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月27日 上午11:07:22
 *
 */
public interface ImageUploadService {

	/**
	 * 图片上传页面的列表
	 * @param operationQuery
	 * @return
	 */
	public List<StudioSlotVo> list(OperationQuery operationQuery);
	
	/**
	 * 根据批次号查询该批次下的所有产品详情
	 * @param slotNo
	 * @return
	 */
	public List<StudioSlotSpuSendDetailVo> slotDetail(String slotNo);
	/**
	 * 判断spPics是否已存在数据库
	 * @param spPics
	 * @return
	 */
	public Map<String,String> hasSlotSpuPic(List<String> spPics);
	/**
	 * 插入hub_slot_spu_pic
	 * @param slotSpuNo slotspu编号
	 * @param spPicUrl 尚品图片编号
	 * @param spuDto 
	 * @param supplierDto
	 * @param extension 扩展名
	 * @return
	 */
	public boolean insertSlotSpuPic(String slotSpuNo, String spPicUrl, HubSlotSpuDto spuDto,
			HubSlotSpuSupplierDto supplierDto, String extension);
	/**
	 * 删除图片
	 * @param spPicUrl
	 * @return
	 */
	public boolean deleteSlotSpuPic(String spPicUrl);
	/**
	 * 更新上传图片标志
	 * @param studioSlotSpuSendDetailId
	 * @return
	 */
	public int updateUploadPicSign(Long studioSlotSpuSendDetailId,UploadPicSign uploadPicSign);
	/**
	 * 更新上传图标标志
	 * @param slotSpuNo
	 * @param uploadPicSign
	 * @return
	 */
	public int updateHubSlotSpuPicSign(String slotSpuNo, UploadPicSign uploadPicSign);



	public void updateHubSupplierSpuPicStateAndHubSlotSpuSupplierPicState(Long spuPendingId, Long slotSpuSupplierId,Long supplierSpuId,UploadPicSign uploadPicSign);





	/**
	 * 确认该批次已拍摄完毕
	 * @param slotNo
	 * @return
	 */
	public HubResponse<?> confirm(String slotNo);
	/**
	 * 根据条码查找该产品的所有图片
	 * @param barcode
	 * @return
	 */
	public List<String> findPictures(String barcode);
	
	/**
	 * 上传图片
	 * @param barcode
	 * @param spPicUrls
	 * @return 成功或失败
	 */
	public boolean add(String barcode, List<String> spPicUrls);
	
}