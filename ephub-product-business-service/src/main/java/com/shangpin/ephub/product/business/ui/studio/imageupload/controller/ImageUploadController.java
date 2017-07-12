package com.shangpin.ephub.product.business.ui.studio.imageupload.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.data.mysql.studio.spu.dto.HubSlotSpuDto;
import com.shangpin.ephub.client.data.mysql.studio.supplier.dto.HubSlotSpuSupplierDto;
import com.shangpin.ephub.client.data.studio.enumeration.UploadPicSign;
import com.shangpin.ephub.client.data.studio.slot.spu.dto.StudioSlotSpuSendDetailDto;
import com.shangpin.ephub.client.util.JsonUtil;
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
		log.info("批次号："+slotNo);
		return HubResponse.successResp(imageUploadService.slotDetail(slotNo));
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public HubResponse<?> add(@RequestBody UploadQuery uploadQuery){
		log.info("添加图片参数："+JsonUtil.serialize(uploadQuery)); 
		if(StringUtils.isEmpty(uploadQuery.getSlotNoSpuId())){
			return HubResponse.errorResp("请先扫码");
		}
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
			StudioSlotSpuSendDetailDto detailDto = operationService.selectSlotSpuSendDetailOfRrrived(uploadQuery.getSlotNoSpuId());
			String slotSpuNo = detailDto.getSlotSpuNo();
			String supplierId = detailDto.getSupplierId();
			if(CollectionUtils.isEmpty(spPicUrls)){
				int result = imageUploadService.updateUploadPicSign(detailDto.getStudioSlotSpuSendDetailId(),UploadPicSign.NOT_YET_UPLOAD);
				log.info("更新uploadPicSign为0==========="+result); 
			}else{
				Map<String, String> picMap = imageUploadService.hasSlotSpuPic(spPicUrls);
				log.info("已存在的图片："+JsonUtil.serialize(picMap));  
				for(String spPicUrl : spPicUrls){
					if(!picMap.containsKey(spPicUrl)){
						if(null == map.get("hubSlotSpu")){
							HubSlotSpuDto spuDto =  operationService.findSlotSpu(slotSpuNo);
							map.put("hubSlotSpu", spuDto);
						}
						if(null == map.get("hubSlotSpuSupplier")){
							HubSlotSpuSupplierDto supplierDto = operationService.findSlotSpuSupplier(supplierId, slotSpuNo);
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
				int result = imageUploadService.updateUploadPicSign(detailDto.getStudioSlotSpuSendDetailId(),UploadPicSign.HAVE_UPLOADED);
				log.info("更新uploadPicSign结果==========="+result); 
			}
		} catch (Exception e) {
			log.error("上传图片页面异常："+e.getMessage(),e); 
		}
		log.info("上传失败的图片："+JsonUtil.serialize(list)); 
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
	
	@RequestMapping(value = "/confirm", method = RequestMethod.POST)
	public HubResponse<?> confirm(@RequestBody String slotNo){
		log.info("confirm=========="+slotNo);
		return imageUploadService.confirm(slotNo); 
	}
	
	@RequestMapping(value = "/modification", method = RequestMethod.POST)
	public HubResponse<?> modification(@RequestBody String barcode){
		List<String> list = imageUploadService.findPictures(barcode);
		if(null != list){
			return HubResponse.successResp(list);
		}else{
			return HubResponse.errorResp("modification error");
		}
		
	}

}
