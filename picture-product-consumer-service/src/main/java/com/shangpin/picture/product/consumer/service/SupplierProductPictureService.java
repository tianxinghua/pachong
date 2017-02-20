package com.shangpin.picture.product.consumer.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import com.shangpin.commons.redis.IShangpinRedis;
import com.shangpin.ephub.client.data.mysql.enumeration.CommonHandleState;
import com.shangpin.ephub.client.data.mysql.enumeration.DataState;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.picture.dto.HubSpuPendingPicDto;
import com.shangpin.ephub.client.fdfs.dto.UploadPicDto;
import com.shangpin.picture.product.consumer.bean.AuthenticationInformation;
import com.shangpin.picture.product.consumer.conf.certificate.CertificateConf;
import com.shangpin.picture.product.consumer.conf.stream.source.message.RetryPicture;
import com.shangpin.picture.product.consumer.conf.stream.source.sender.RetryPictureProductStreamSender;
import com.shangpin.picture.product.consumer.e.PicHandleState;
import com.shangpin.picture.product.consumer.manager.SpuPicStatusServiceManager;
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
@EnableConfigurationProperties(CertificateConf.class)
public class SupplierProductPictureService {

	private static final String PASSWORD = "password";

	private static final String USERNAME = "username";

	private static final int TIMEOUT = 45*60*1000;
	
	@Autowired
	private SupplierProductPictureManager supplierProductPictureManager;

	@Autowired
	private SpuPicStatusServiceManager spuPicStatusServiceManager;
	
	@Autowired
	private IShangpinRedis shangpinRedis;
	
	@Autowired
	private CertificateConf certificate;
	/**
	 * 处理供应商商品图片
	 * @param picDtos
	 */
	public void processProductPicture(List<HubSpuPendingPicDto> picDtos) {
		if (CollectionUtils.isNotEmpty(picDtos)) {
			for (HubSpuPendingPicDto picDto : picDtos) {
				String picUrl = picDto.getPicUrl();
				if(!supplierProductPictureManager.exists(picDto.getSupplierId(),picUrl)){
					continue;
				}
				Long spuPendingPicId = supplierProductPictureManager.save(picDto);//保存初始化数据
				HubSpuPendingPicDto updateDto = new HubSpuPendingPicDto();
				updateDto.setSpuPendingPicId(spuPendingPicId);
				updateDto.setSupplierSpuId(picDto.getSupplierSpuId());
				AuthenticationInformation information = null;
				String supplierId = picDto.getSupplierId();
				if (StringUtils.isNotBlank(supplierId)) {
					information = getAuthentication(supplierId);
				}
				pullPicAndPushToPicServer(picUrl, updateDto, information);
				supplierProductPictureManager.updateSelective(updateDto);
			}
		}
	}
	/**
	 * @param supplierId
	 * @return
	 */
	private AuthenticationInformation getAuthentication(String supplierId) {
		Map<String, Map<String, String>> usernameAndPassword = certificate.getUsernameAndPassword();
		Map<String, String> userAndPasswd = usernameAndPassword.get(supplierId);
		AuthenticationInformation authenticationInformation = null;
		if (MapUtils.isNotEmpty(userAndPasswd)) {
			authenticationInformation = new AuthenticationInformation(userAndPasswd.get(USERNAME), userAndPasswd.get(PASSWORD));
		}
		return authenticationInformation;
	}
	/**
	 * 拉取图片并上传图片服务器
	 * @param picUrl 图片原始地址
	 * @param dto 数据传输对象
	 * @param authenticationInformation 认证信息
	 */
	private void pullPicAndPushToPicServer(String picUrl, HubSpuPendingPicDto dto, AuthenticationInformation authenticationInformation){
		if (authenticationInformation != null) {//需要认证
			Authenticator.setDefault(new Authenticator() {
	            protected PasswordAuthentication getPasswordAuthentication() {
	                return new PasswordAuthentication(authenticationInformation.getUsername(),
	                        new String(authenticationInformation.getPassword()).toCharArray());
	            }
	        });
		}
		InputStream inputStream = null;
		try {
			
			URL url = new URL(picUrl.replaceAll(" +", "%20"));
			URLConnection openConnection = url.openConnection();
			openConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");
			openConnection.setConnectTimeout(TIMEOUT);
			openConnection.setReadTimeout(TIMEOUT);
			inputStream = openConnection.getInputStream();
			String base64 = new BASE64Encoder().encode(IOUtils.toByteArray(inputStream));
			if (StringUtils.isBlank(base64)) {
				throw new RuntimeException("读取到的图片内容为空,无法获取图片");
			}
			UploadPicDto uploadPicDto = new UploadPicDto();
			uploadPicDto.setBase64(base64);
			uploadPicDto.setExtension(getExtension(picUrl));
			dto.setSpPicUrl(supplierProductPictureManager.uploadPic(uploadPicDto));
			dto.setPicHandleState(PicHandleState.HANDLED.getIndex());
			dto.setMemo("图片拉取成功");
			if(dto.getSupplierSpuId() != null){
				spuPicStatusServiceManager.updatePicStatus(dto.getSupplierSpuId(), CommonHandleState.HANDLED.getIndex().byteValue());
			}
		} catch (Throwable e) {
			log.error("系统拉取图片时发生异常,url ="+picUrl,e);
			e.printStackTrace();
			dto.setPicHandleState(PicHandleState.HANDLE_ERROR.getIndex());
			dto.setMemo("图片拉取失败:"+e.getMessage());
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
		dto.setUpdateTime(new Date());
	}
	/**
	 * 获取图片扩张名
	 * @param url 图片地址
	 * @return 扩张名
	 */
	private String getExtension(String url){
		if (StringUtils.isNotBlank(url)) {
			 String suffix = url.substring(url.lastIndexOf(".")+1);
			 if (suffix == null) {
				 return "jpg";
			 }
			 if (suffix != null && suffix.length() > 5) {
				return "jpg";
			}
			return suffix;
		} else {
			return null;
		}
	}
	private static final Integer PAGE_SIZE = 100;
	@Autowired
	private RetryPictureProductStreamSender streamSender;
	/**
	 * 扫描拉取失败的图片
	 */
	public void scanFailedPictureToRetry() {
		HubSpuPendingPicCriteriaDto criteria = new HubSpuPendingPicCriteriaDto();
		criteria.createCriteria().andPicHandleStateNotEqualTo(PicHandleState.HANDLED.getIndex()).andPicUrlIsNotNull().andDataStateEqualTo(DataState.NOT_DELETED.getIndex());
		for (int i = 1; i <= countTotalPage(supplierProductPictureManager.countFailedPictureTotal(criteria), PAGE_SIZE); i++) {
			HubSpuPendingPicCriteriaDto _criteria = new HubSpuPendingPicCriteriaDto();
			_criteria.setFields("spu_pending_pic_id");
			_criteria.setPageNo(i);
			_criteria.setPageSize(PAGE_SIZE);
			_criteria.createCriteria().andPicHandleStateNotEqualTo(PicHandleState.HANDLED.getIndex()).andPicUrlIsNotNull().andDataStateEqualTo(DataState.NOT_DELETED.getIndex());
			List<HubSpuPendingPicDto> picDto = supplierProductPictureManager.queryByCriteria(_criteria);
			if (CollectionUtils.isNotEmpty(picDto)) {
				for (HubSpuPendingPicDto hubSpuPendingPicDto : picDto) {
					Long spuPendingPicId = hubSpuPendingPicDto.getSpuPendingPicId();//获取主键
					//if (StringUtils.isBlank(shangpinRedis.get(assemblyKey(spuPendingPicId)))) {//拿到锁
						HubSpuPendingPicDto dto = supplierProductPictureManager.queryById(spuPendingPicId);
						if (dto != null && dto.getPicHandleState() != PicHandleState.HANDLED.getIndex()) {
							Integer retryCount = dto.getRetryCount();
							if (retryCount != null && retryCount > 20) {
								continue;
							}
							//shangpinRedis.set(assemblyKey(spuPendingPicId), String.valueOf(spuPendingPicId));
							streamSender.supplierPictureProductStream(new RetryPicture(spuPendingPicId) , null);
						}
					//}
				}
			}
		}
	}
	/**
	 * 组装redis键
	 * @param spuPendingPicId 主键
	 * @return 缓存key
	 */
	private String assemblyKey(Long spuPendingPicId) {
		return "EP_HUB_SPU_PENDING_PIC_ID:"+spuPendingPicId;
	}
	/**
	 * 计算总页数
	 * @param count 总记录数
	 * @param pageSize 每页记录数
	 * @return 总页数
	 */
	public int countTotalPage(int count, int pageSize){
		if (count % pageSize == 0) {
			return count / pageSize;
		} else {
			return (count / pageSize) +1;
		}
	}
	/**
	 * 重试拉取图片
	 * @param spuPendingPicId 图片表主键
	 */
	public  void processRetryProductPicture(Long spuPendingPicId) {
		int count = 0;
		try {
			HubSpuPendingPicDto hubSpuPendingPicDto = supplierProductPictureManager.queryById(spuPendingPicId);
			if (hubSpuPendingPicDto != null && hubSpuPendingPicDto.getDataState() == DataState.NOT_DELETED.getIndex() && hubSpuPendingPicDto.getPicHandleState() != PicHandleState.HANDLED.getIndex()) {
				HubSpuPendingPicDto updateDto = new HubSpuPendingPicDto();
				updateDto.setSpuPendingPicId(hubSpuPendingPicDto.getSpuPendingPicId());
				updateDto.setSupplierSpuId(hubSpuPendingPicDto.getSupplierSpuId());
				Integer retryCount = hubSpuPendingPicDto.getRetryCount();
				AuthenticationInformation information = null;
				String supplierId = hubSpuPendingPicDto.getSupplierId();
				if (StringUtils.isNotBlank(supplierId)) {
						information = getAuthentication(supplierId);
				}
				pullPicAndPushToPicServer(hubSpuPendingPicDto.getPicUrl(), updateDto, information);
				count = retryCount == null ? 1 : retryCount + 1;
				updateDto.setRetryCount(count);
				supplierProductPictureManager.updateSelective(updateDto);
			} 
		} catch (Throwable e) {
			log.error("重试拉取主键为"+spuPendingPicId+"的图片时发生异常，重试次数为"+count+"次",e);
		} finally {
			shangpinRedis.del(assemblyKey(spuPendingPicId));
		}
	}
}
