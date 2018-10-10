package com.shangpin.picture.product.consumer.service;

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

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
import com.shangpin.picture.product.consumer.util.FtpUtil;

import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Encoder;
import sun.net.www.protocol.ftp.FtpURLConnection;

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

	private static final int TIMEOUT = 10*1000;

	private static final int CONNECT_TIMEOUT = 10*1000;
	
	@Autowired
	private SupplierProductPictureManager supplierProductPictureManager;

	@Autowired
	private SpuPicStatusServiceManager spuPicStatusServiceManager;

	@Autowired
	private SpuPendingService spuPendingService;
	
	
	@Autowired
	private CertificateConf certificate;

	@Autowired
	private ImageDownload download;

	/**
	 * 处理供应商商品图片
	 * @param picDtos
	 */
	public void processProductPicture(List<HubSpuPendingPicDto> picDtos) {
		if (CollectionUtils.isNotEmpty(picDtos)) {
			Long supplierSpuId = picDtos.get(0).getSupplierSpuId();
			for (HubSpuPendingPicDto picVO : picDtos) {
				String picUrl = picVO.getPicUrl();
				log.info("picUrl = " +picUrl);
				HubSpuPendingPicDto picDto = supplierProductPictureManager.getSpuPendingPicDtoBySupplierIdAndPicUrl(picVO.getSupplierId(), picUrl);
				if(null!=picDto){
					log.info("pic  DataState = " +picDto.getDataState());
					//如果连接存在 且状态为使用中 则不操作
					if(DataState.NOT_DELETED.getIndex()==picDto.getDataState()){
                         continue;
					}else{
						//更新图片状态
						HubSpuPendingPicDto updatePic = new HubSpuPendingPicDto();
						updatePic.setSpuPendingPicId(picDto.getSpuPendingPicId());
						updatePic.setDataState(DataState.NOT_DELETED.getIndex());
						updatePic.setMemo("");
						supplierProductPictureManager.updateSelective(updatePic);
						//更新spupending 图片状态
						spuPendingService.updateSpuPendingPicState(picDto.getSupplierId(),picDto.getSupplierSpuNo());

						continue;
					}
				}

				Long spuPendingPicId = supplierProductPictureManager.save(picVO);//保存初始化数据
				HubSpuPendingPicDto updateDto = new HubSpuPendingPicDto();
				updateDto.setSpuPendingPicId(spuPendingPicId);
				updateDto.setSupplierSpuId(picVO.getSupplierSpuId());
				AuthenticationInformation information = null;
				String supplierId = picVO.getSupplierId();
				if (StringUtils.isNotBlank(supplierId)) {
					information = getAuthentication(supplierId);
				}
				int code = 0;
				if(picUrl!=null){
					if(picUrl.toUpperCase().startsWith("HTTP")){
						code = pullPicAndPushToPicServer(picUrl, updateDto, information);
					}else if(picUrl.toUpperCase().startsWith("FTP")){
						if("2016110101955".equals(picVO.getSupplierId())){
							code = pullFtpPicByBrownAndPushToPicServer(picUrl, updateDto, information);
						}else{
							code = pullPicFromFtpAndPushToPicServer(picUrl, updateDto, information);
						}

					}	
				}
				
				
				if (code == 404 || code == 400) {
					supplierProductPictureManager.deleteById(spuPendingPicId);
				} else {
					log.info("id="+updateDto.getSpuPendingPicId()+"==第五步==>> 将数据保存至数据库，数据为"+updateDto);
					supplierProductPictureManager.updateSelective(updateDto);
					log.info("id="+updateDto.getSpuPendingPicId()+"==第六步==>> 图片拉取流程完毕");
				}
			}
			spuPicStatusServiceManager.judgeSpuPicState(supplierSpuId); 
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
	 * 拉取图片并上传图片服务器y
	 * @param picUrl 图片原始地址
	 * @param dto 数据传输对象
	 * @param authenticationInformation 认证信息
	 */
	private int pullPicAndPushToPicServer(String picUrl, HubSpuPendingPicDto dto, AuthenticationInformation authenticationInformation){
		if (picUrl.contains("_")){
			return download.downloadPicture(picUrl,dto,authenticationInformation);
		}else {
			InputStream inputStream = null;
			HttpURLConnection httpUrlConnection = null;
			int flag = 0;
			try {
				if (authenticationInformation != null) {//需要认证
					Authenticator.setDefault(new Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(authenticationInformation.getUsername(),
									new String(authenticationInformation.getPassword()).toCharArray());
						}
					});
				}

				URL url = new URL(picUrl.replaceAll(" +", "%20"));
				URLConnection openConnection = url.openConnection();
				httpUrlConnection  =  (HttpURLConnection) openConnection;
				httpUrlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");
				httpUrlConnection.setConnectTimeout(CONNECT_TIMEOUT);
				httpUrlConnection.setReadTimeout(TIMEOUT);
				httpUrlConnection.connect();
				flag = httpUrlConnection.getResponseCode();
				log.info("response code=" + flag);
				if (flag == 404 || flag == 400) {
					return flag;
				}else if(flag == 301 || flag == 302){
					log.info("id="+dto.getSpuPendingPicId()+"链接重定向，原始url="+picUrl);
					String newPicUrl = picUrl.replaceFirst("http", "https");
					return pullPicAndPushToPicServer(newPicUrl,dto,authenticationInformation);
				}
				inputStream = openConnection.getInputStream();
				byte[] byteArray = IOUtils.toByteArray(inputStream);
				if (byteArray == null || byteArray.length == 0) {
					throw new RuntimeException("读取到的图片字节为空,无法获取图片");
				}
				String base64 = new BASE64Encoder().encode(byteArray);
				log.info("id="+dto.getSpuPendingPicId()+"==第一步==>> "+"原始url="+picUrl+"， 上传图片前拉取的数据为"+base64.substring(0, 100)+"，长度 为 "+base64.length()+"， 下一步调用上传图片服务上传图片到图片服务器");
				UploadPicDto uploadPicDto = new UploadPicDto();
				uploadPicDto.setRequestId(String.valueOf(dto.getSpuPendingPicId()));
				uploadPicDto.setBase64(base64);
				uploadPicDto.setExtension(getExtension(picUrl));
				String fdfsURL = supplierProductPictureManager.uploadPic(uploadPicDto);
				log.info("id="+dto.getSpuPendingPicId()+"==第四步==>> 调用图片服务上传图片后返回的图片URL为"+fdfsURL+"， 下一步将更改数据库");
				dto.setSpPicUrl(fdfsURL);
				dto.setPicHandleState(PicHandleState.HANDLED.getIndex());
				dto.setMemo("图片拉取成功");
			}
			catch (Throwable e) {
					log.error("系统拉取图片时发生异常,url =" + picUrl, e);
					e.printStackTrace();
					dto.setPicHandleState(PicHandleState.HANDLE_ERROR.getIndex());
					dto.setMemo("图片拉取失败:" + flag);

			} finally {
				close(inputStream, httpUrlConnection);
			}

			dto.setUpdateTime(new Date());
			return flag;
		}
	}


	/**
	 *
	 * @param picUrl
	 * @param dto
	 * @param authenticationInformation
	 * @return
	 */
	private int pullFtpPicByBrownAndPushToPicServer(String picUrl,HubSpuPendingPicDto dto, AuthenticationInformation authenticationInformation){
		InputStream inputStream = null;
		FtpURLConnection httpUrlConnection = null;
		int flag = 0;
		try {
			if (authenticationInformation != null) {//需要认证
				Authenticator.setDefault(new Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(authenticationInformation.getUsername(),
								new String(authenticationInformation.getPassword()).toCharArray());
					}
				});
			}
			URL url = new URL(picUrl.replaceAll(" +", "%20"));
			URLConnection openConnection = url.openConnection();
			httpUrlConnection  =  (FtpURLConnection) openConnection;
			httpUrlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:50.0) Gecko/20100101 Firefox/50.0");
			httpUrlConnection.setConnectTimeout(CONNECT_TIMEOUT);
			httpUrlConnection.setReadTimeout(TIMEOUT);
			httpUrlConnection.connect();


			inputStream = openConnection.getInputStream();
			byte[] byteArray = IOUtils.toByteArray(inputStream);
			if (byteArray == null || byteArray.length == 0) {
				throw new RuntimeException("读取到的图片字节为空,无法获取图片");
			}
			String base64 = new BASE64Encoder().encode(byteArray);
			log.info("id="+dto.getSpuPendingPicId()+"==第一步==>> "+"原始url="+picUrl+"， 上传图片前拉取的数据为"+base64.substring(0, 100)+"，长度 为 "+base64.length()+"， 下一步调用上传图片服务上传图片到图片服务器");
			UploadPicDto uploadPicDto = new UploadPicDto();
			uploadPicDto.setRequestId(String.valueOf(dto.getSpuPendingPicId()));
			uploadPicDto.setBase64(base64);
			uploadPicDto.setExtension(getExtension(picUrl));
			String fdfsURL = supplierProductPictureManager.uploadPic(uploadPicDto);
			log.info("id="+dto.getSpuPendingPicId()+"==第四步==>> 调用图片服务上传图片后返回的图片URL为"+fdfsURL+"， 下一步将更改数据库");
			dto.setSpPicUrl(fdfsURL);
			dto.setPicHandleState(PicHandleState.HANDLED.getIndex());
			dto.setMemo("图片拉取成功");

		}catch (Throwable e) {
			log.error("系统拉取图片时发生异常,url ="+picUrl,e);
			e.printStackTrace();
			dto.setPicHandleState(PicHandleState.HANDLE_ERROR.getIndex());
			dto.setMemo("图片拉取失败:"+flag);
		} finally {
			closeFtpURL(inputStream, httpUrlConnection);
		}
		dto.setUpdateTime(new Date());
		return flag;
	}
	/**
	 * 从ftp下载图片并上传图片服务器
	 * @param picUrl 供应商原始链接，格式必须为：ftp://user:password@ip/urlpath
	 * @param dto
	 * @param authenticationInformation 必须将ftp的用户名密码配置到配置文件
	 * @return
	 */
	public int pullPicFromFtpAndPushToPicServer(String picUrl, HubSpuPendingPicDto dto, AuthenticationInformation authenticationInformation){
		FTPClient ftpClient = null;
		InputStream inputStream = null;
		int flag = 0;
		try {
			String url = picUrl.substring(picUrl.lastIndexOf("@")+1).trim();
			String ip = url.substring(0,url.indexOf("/"));
			String remotePath =  url.substring(url.indexOf("/"),url.lastIndexOf("/")); 
			String remoteFileName = picUrl.substring(picUrl.lastIndexOf("/")+1);
			int port = 21;
			if(ip.indexOf(":")>0){
				try {
					port = Integer.valueOf(ip.substring(ip.indexOf(":")+1,ip.length()).trim());
					ip=ip.substring(0,ip.indexOf(":"));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}
			inputStream = FtpUtil.getFtpUtil().downFile(ftpClient,authenticationInformation.getUsername(), authenticationInformation.getPassword(), ip, port, remotePath, remoteFileName);
			if(null == inputStream){
				return 404;
			}
			byte[] byteArray = IOUtils.toByteArray(inputStream);
			if (byteArray == null || byteArray.length == 0) {
				throw new RuntimeException("ftp读取到的图片字节为空,无法获取图片");
			}
			String base64 = new BASE64Encoder().encode(byteArray);
			log.info("id="+dto.getSpuPendingPicId()+"==第一步==>> "+"原始url="+picUrl+"， 上传图片前拉取的数据为"+base64.substring(0, 100)+"，长度 为 "+base64.length()+"， 下一步调用上传图片服务上传图片到图片服务器");
			UploadPicDto uploadPicDto = new UploadPicDto();
			uploadPicDto.setRequestId(String.valueOf(dto.getSpuPendingPicId()));
			uploadPicDto.setBase64(base64);
			uploadPicDto.setExtension(getExtension(picUrl));
			String fdfsURL = supplierProductPictureManager.uploadPic(uploadPicDto);
			log.info("id="+dto.getSpuPendingPicId()+"==第四步==>> 调用图片服务上传图片后返回的图片URL为"+fdfsURL+"， 下一步将更改数据库");
			dto.setSpPicUrl(fdfsURL);
			dto.setPicHandleState(PicHandleState.HANDLED.getIndex());
			dto.setMemo("图片拉取成功");
		
		} catch (Throwable e) {
			log.error("系统拉取ftp图片时发生异常,url ="+picUrl,e);
			e.printStackTrace();
			dto.setPicHandleState(PicHandleState.HANDLE_ERROR.getIndex());
			dto.setMemo("图片拉取失败:"+flag);
		}finally {
			closeFtp(inputStream,ftpClient);
		}
		dto.setUpdateTime(new Date());
		return flag;
	}
	/**
	 * 关闭链接以及资源
	 * @param inputStream
	 * @param httpUrlConnection
	 */
	private void close(InputStream inputStream, HttpURLConnection httpUrlConnection) {
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (Throwable e) {
				log.error("关闭资源流发生异常", e);
				e.printStackTrace();
				throw new RuntimeException("关闭资源流发生异常");
			}
		}
		if (httpUrlConnection != null) {
			try {
				httpUrlConnection.disconnect();
			} catch (Throwable e) {
				log.error("关闭链接发生异常", e);
				e.printStackTrace();
				throw new RuntimeException("关闭链接发生异常");
			}
		}
	}


	/**
	 * 关闭链接以及资源
	 * @param inputStream
	 * @param httpUrlConnection
	 */
	private void closeFtpURL(InputStream inputStream, FtpURLConnection httpUrlConnection) {
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (Throwable e) {
				log.error("关闭资源流发生异常", e);
				e.printStackTrace();
				throw new RuntimeException("关闭资源流发生异常");
			}
		}
		if (httpUrlConnection != null) {
			try {
				httpUrlConnection.close();
			} catch (Throwable e) {
				log.error("关闭链接发生异常", e);
				e.printStackTrace();
				throw new RuntimeException("关闭链接发生异常");
			}
		}
	}
	/**
	 * 关闭ftp资源流以及连接
	 * @param inputStream
	 * @param ftpClient
	 */
	private void closeFtp(InputStream inputStream,FTPClient ftpClient){
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (Throwable e) {
				log.error("关闭ftp资源流发生异常", e);
				e.printStackTrace();
				throw new RuntimeException("关闭ftp资源流发生异常");
			}
		}
		if(null != ftpClient){
			try {
				ftpClient.disconnect();
			} catch (Throwable e) {
				log.error("关闭ftp链接发生异常", e);
				e.printStackTrace();
				throw new RuntimeException("关闭ftp链接发生异常");
			}
		}
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
		criteria.createCriteria().andPicHandleStateEqualTo(PicHandleState.HANDLE_ERROR.getIndex()).andPicUrlIsNotNull().andDataStateEqualTo(DataState.NOT_DELETED.getIndex());
		for (int i = 1; i <= countTotalPage(supplierProductPictureManager.countFailedPictureTotal(criteria), PAGE_SIZE); i++) {
			HubSpuPendingPicCriteriaDto _criteria = new HubSpuPendingPicCriteriaDto();
			_criteria.setFields("spu_pending_pic_id");
			_criteria.setPageNo(i);
			_criteria.setPageSize(PAGE_SIZE);
			_criteria.createCriteria().andPicHandleStateEqualTo(PicHandleState.HANDLE_ERROR.getIndex()).andPicUrlIsNotNull().andDataStateEqualTo(DataState.NOT_DELETED.getIndex());
			List<HubSpuPendingPicDto> picDto = supplierProductPictureManager.queryByCriteria(_criteria);
			if (CollectionUtils.isNotEmpty(picDto)) {
				for (HubSpuPendingPicDto hubSpuPendingPicDto : picDto) {
					Long spuPendingPicId = hubSpuPendingPicDto.getSpuPendingPicId();//获取主键
						HubSpuPendingPicDto dto = supplierProductPictureManager.queryById(spuPendingPicId);
						if (dto != null && dto.getPicHandleState() != PicHandleState.HANDLED.getIndex()) {
							Integer retryCount = dto.getRetryCount();
							if (retryCount != null && retryCount > 6) {
								continue;
							}
							if("2017031301866 ".equals(dto.getSupplierId())){
								streamSender.brandPictureProductStream(new RetryPicture(spuPendingPicId) , null);
							}else{
								streamSender.supplierPictureProductStream(new RetryPicture(spuPendingPicId) , null);
							}
							
						}
				}
			}
		}
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
				deleteImage(hubSpuPendingPicDto);
				String picUrl = hubSpuPendingPicDto.getPicUrl();
				int code = 0;
				if(picUrl.toUpperCase().startsWith("HTTP")){
					code = pullPicAndPushToPicServer(picUrl, updateDto, information);
				}else if(picUrl.toUpperCase().startsWith("FTP")){

					if("2016110101955".equals(hubSpuPendingPicDto.getSupplierId())){
						code = pullFtpPicByBrownAndPushToPicServer(picUrl, updateDto, information);
					}else{
						code = pullPicFromFtpAndPushToPicServer(picUrl, updateDto, information);
					}

				}
				
				if (code == 404 || code == 400) {
					supplierProductPictureManager.deleteById(spuPendingPicId);
				} else { 
						count = retryCount == null ? 1 : retryCount + 1;
						updateDto.setRetryCount(count);
						supplierProductPictureManager.updateSelective(updateDto);
						spuPicStatusServiceManager.judgeSpuPicState(hubSpuPendingPicDto.getSupplierSpuId()); 
				}
			} 
		} catch (Throwable e) {
			log.error("重试拉取主键为"+spuPendingPicId+"的图片时发生异常，重试次数为"+count+"次",e);
		}
	}
	/**
	 * 删除图片：先备份图片地址到deleted表，再删除图片
	 * @param hubSpuPendingPicDto 图片
	 */
	private void deleteImage(HubSpuPendingPicDto hubSpuPendingPicDto) {
		if (StringUtils.isNotBlank(hubSpuPendingPicDto.getSpPicUrl())) {
			try {
				supplierProductPictureManager.backupHubSpuPendingPicDtoToDeleted(hubSpuPendingPicDto);
			} catch (Throwable e) {
				e.printStackTrace();
				log.error(e.getMessage(),e);
				throw new RuntimeException("备份图片信息【"+hubSpuPendingPicDto+"】到deleted表时发生异常，备份失败，系统停止删除图片并终止本次重试拉取图片", e);
			}
			supplierProductPictureManager.deleteImageAndSetNull(hubSpuPendingPicDto);
		}
	}

	public static void main(String[] args) {
		SupplierProductPictureService pictureService = new SupplierProductPictureService();
		HubSpuPendingPicDto picDto = new HubSpuPendingPicDto();
		AuthenticationInformation information = new AuthenticationInformation();
		pictureService.pullPicAndPushToPicServer("https://cache.mrporter.com/images/products/1082974/1082974_mrp_fr_l.jpg",picDto,information);

	}


}
