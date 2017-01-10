package com.shangpin.picture.product.consumer.service;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDto;
import com.shangpin.ephub.client.fdfs.dto.UploadPicDto;
import com.shangpin.picture.product.consumer.e.PicHandleState;
import com.shangpin.picture.product.consumer.manager.SupplierProductPictureManager;

import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Encoder;

/**
 * <p>Title:SupplierProductPictureService.java </p>
 * <p>Description: </p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2017年1月2日 下午12:45:06
 */
@Service
@Slf4j
public class SupplierProductPictureService {

	private static final int TIMEOUT = 10*60*1000;
	@Autowired
	private SupplierProductPictureManager supplierProductPictureManager;
	/**
	 * 处理供应商商品图片
	 * @param picDtos
	 */
	public void processProductPicture(List<HubSpuPendingPicDto> picDtos) {
		if (CollectionUtils.isNotEmpty(picDtos)) {
			for (HubSpuPendingPicDto picDto : picDtos) {
				String picUrl = picDto.getPicUrl();
				if(supplierProductPictureManager.exists(picUrl)){
					continue;
				}
				Long spuPendingPicId = supplierProductPictureManager.save(picDto);//保存初始化数据
				HubSpuPendingPicDto updateDto = new HubSpuPendingPicDto();
				updateDto.setSpuPendingPicId(spuPendingPicId);
				try {
					URL url = new URL(picUrl);
					URLConnection openConnection = url.openConnection();
					openConnection.setConnectTimeout(TIMEOUT);
					openConnection.setReadTimeout(TIMEOUT);
					InputStream inputStream = openConnection.getInputStream();
					String base64 = new BASE64Encoder().encode(IOUtils.toByteArray(inputStream));
					UploadPicDto uploadPicDto = new UploadPicDto();
					uploadPicDto.setBase64(base64);
					uploadPicDto.setExtension(getExtension(picUrl));
					String spPicUrl = supplierProductPictureManager.uploadPic(uploadPicDto);
					updateDto.setSpPicUrl(spPicUrl);
					updateDto.setPicHandleState(PicHandleState.HANDLED.getIndex());
					updateDto.setMemo("图片拉取成功");
				} catch (Throwable e) {
					log.error("系统拉取图片时发生异常",e);
					e.printStackTrace();
					updateDto.setPicHandleState(PicHandleState.HANDLE_ERROR.getIndex());
					updateDto.setMemo("图片拉取失败");
				}
				updateDto.setUpdateTime(new Date());
				supplierProductPictureManager.updateSelective(updateDto);
			}
		}
	}
	
	private String getExtension(String url){
		if (StringUtils.isNoneBlank(url)) {
			 return url.substring(url.lastIndexOf(".")+1);
		} else {
			return null;
		}
	}
}
