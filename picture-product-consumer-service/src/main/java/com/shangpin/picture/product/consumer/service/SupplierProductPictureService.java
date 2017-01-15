package com.shangpin.picture.product.consumer.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;

import com.shangpin.ephub.client.data.mysql.enumeration.CommonHandleState;
import com.shangpin.ephub.client.data.mysql.enumeration.PicState;
import com.shangpin.picture.product.consumer.manager.SpuPicStatusServiceManager;
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

	@Autowired
	SpuPicStatusServiceManager spuPicStatusServiceManager;
	/**
	 * 处理供应商商品图片
	 * @param picDtos
	 */
	public void processProductPicture(List<HubSpuPendingPicDto> picDtos) {
		if (CollectionUtils.isNotEmpty(picDtos)) {

			Long supplierSpuId = null;
			for (HubSpuPendingPicDto picDto : picDtos) {
				String picUrl = picDto.getPicUrl();
				if(!supplierProductPictureManager.exists(picUrl)){
					continue;
				}
				Long spuPendingPicId = supplierProductPictureManager.save(picDto);//保存初始化数据
				HubSpuPendingPicDto updateDto = new HubSpuPendingPicDto();
				updateDto.setSpuPendingPicId(spuPendingPicId);
				InputStream inputStream = null;
				try {
					URL url = new URL(picUrl);
					URLConnection openConnection = url.openConnection();
					openConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");
					openConnection.setConnectTimeout(TIMEOUT);
					openConnection.setReadTimeout(TIMEOUT);
					inputStream = openConnection.getInputStream();
					String base64 = new BASE64Encoder().encode(IOUtils.toByteArray(inputStream));
					UploadPicDto uploadPicDto = new UploadPicDto();
					uploadPicDto.setBase64(base64);
					uploadPicDto.setExtension(getExtension(picUrl));
					String spPicUrl = supplierProductPictureManager.uploadPic(uploadPicDto);
					updateDto.setSpPicUrl(spPicUrl);
					updateDto.setPicHandleState(PicHandleState.HANDLED.getIndex());
					updateDto.setMemo("图片拉取成功");
					supplierSpuId = picDto.getSupplierSpuId();

				} catch (Throwable e) {
					log.error("系统拉取图片时发生异常",e);
					e.printStackTrace();
					updateDto.setPicHandleState(PicHandleState.HANDLE_ERROR.getIndex());
					updateDto.setMemo("图片拉取失败");
				} finally {
					if (inputStream != null) {
						try {
							inputStream.close();
						} catch (IOException e) {
							log.error("关闭资源流发生异常", e);
							e.printStackTrace();
							throw new RuntimeException("关闭资源流发生异常");
						}
					}
				}
				updateDto.setUpdateTime(new Date());
				supplierProductPictureManager.updateSelective(updateDto);
			}

			if(null!=supplierSpuId){
				spuPicStatusServiceManager.updatePicStatus(supplierSpuId, CommonHandleState.HANDLED.getIndex().byteValue());
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
