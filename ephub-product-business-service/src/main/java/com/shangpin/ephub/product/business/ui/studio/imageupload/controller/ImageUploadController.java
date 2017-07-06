package com.shangpin.ephub.product.business.ui.studio.imageupload.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.data.mysql.studio.spu.dto.HubSlotSpuDto;
import com.shangpin.ephub.client.data.mysql.studio.supplier.dto.HubSlotSpuSupplierDto;
import com.shangpin.ephub.product.business.ui.studio.common.operation.dto.OperationQuery;
import com.shangpin.ephub.product.business.ui.studio.common.operation.service.OperationService;
import com.shangpin.ephub.product.business.ui.studio.common.operation.vo.StudioSlotVo;
import com.shangpin.ephub.product.business.ui.studio.common.pictrue.dto.UploadQuery;
import com.shangpin.ephub.product.business.ui.studio.common.pictrue.service.PictureService;
import com.shangpin.ephub.product.business.ui.studio.imageupload.service.ImageUploadService;
import com.shangpin.ephub.response.HubResponse;

import lombok.extern.slf4j.Slf4j;
/**
 * <p>Title: ImageUploadController</p>
 * <p>Description: 图片上传页面所有的api </p>
 * <p>Company: </p> 
 * @author lubaijiang
 * @date 2017年6月27日 上午11:07:49
 *
 */
@RestController
@RequestMapping("/api/airstudio/image")
@Slf4j
public class ImageUploadController {
	
	@Autowired
	private ImageUploadService imageUploadService;
	@Autowired
	private OperationService operationService;
	@Autowired
	private PictureService pictureService;
	
	@RequestMapping(value = "/list" ,method = RequestMethod.POST)
	public HubResponse<?> slotList(@RequestBody OperationQuery operationQuery){
		List<StudioSlotVo> list = imageUploadService.list(operationQuery);
		if(null != list){
			return HubResponse.successResp(list);
		}else{
			return HubResponse.errorResp("error");
		}
	} 
	
	@RequestMapping(value = "/detail", method = RequestMethod.POST)
	public HubResponse<?> detail(@RequestBody String slotNo){
		return HubResponse.successResp(imageUploadService.slotDetail(slotNo));
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public HubResponse<?> add(@RequestBody UploadQuery uploadQuery){
		/**
		 * 记录上传失败的链接
		 */
		List<String> list = new ArrayList<String>();
		try {
			Map<String,Object> map = new HashMap<String,Object>(){/**
				 * 
				 */
				private static final long serialVersionUID = -3100796099597147465L;

			{put("hubSlotSpu",null);put("hubSlotSpuSupplier",null);}};
			List<String> spPicUrls = uploadQuery.getUrls();
			if(CollectionUtils.isNotEmpty(spPicUrls)){
//				String slotNoSpuId = uploadQuery.getSlotNoSpuId();
//				String slotNo = slotNoSpuId .substring(0, slotNoSpuId.indexOf("-"));
//				String slotSpuNo = slotNoSpuId.substring(slotNoSpuId.indexOf("-") + 1);
				String slotSpuNo = operationService.selectSlotSpuSendDetailOfRrrived(uploadQuery.getSlotNoSpuId()).getSlotSpuNo();
				Map<String, String> picMap = imageUploadService.hasSlotSpuPic(spPicUrls);
				for(String spPicUrl : spPicUrls){
					if(!picMap.containsKey(spPicUrl)){
						if(null == map.get("hubSlotSpu")){
							HubSlotSpuDto spuDto =  operationService.findSlotSpu(slotSpuNo);
							map.put("hubSlotSpu", spuDto);
						}
						if(null == map.get("hubSlotSpuSupplier")){
							HubSlotSpuSupplierDto supplierDto = operationService.findSlotSpuSupplier(uploadQuery.getSlotNo(), slotSpuNo);
							map.put("hubSlotSpuSupplier", supplierDto);
						}
						HubSlotSpuDto spuDto = (HubSlotSpuDto) map.get("hubSlotSpu");
						HubSlotSpuSupplierDto supplierDto = (HubSlotSpuSupplierDto) map.get("hubSlotSpuSupplier");
						String extension = pictureService.getExtension(spPicUrl);
						boolean bool = imageUploadService.insertSlotSpuPic(slotSpuNo, spPicUrl, spuDto, supplierDto, extension); 
						if(!bool){
							list.add(spPicUrl);
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("上传图片页面异常："+e.getMessage(),e); 
		}
		if(list.size() == 0){
			return HubResponse.successResp("全部上传成功。");
		}else{
			return HubResponse.errorResp(list);
		}
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public HubResponse<?> deleteSlotSpuPic(@RequestBody String spPicUrl){
		boolean bool = imageUploadService.deleteSlotSpuPic(spPicUrl);
		log.info("图片删除返回结果=========="+bool); 
		if(bool){
			return HubResponse.successResp("删除成功。");
		}else{
			return HubResponse.errorResp("删除失败。");
		}
	}
	
	

}
