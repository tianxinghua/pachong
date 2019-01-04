package com.shangpin.api.airshop.product.m;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.shangpin.api.airshop.util.HttpClientUtil;
import com.shangpin.common.utils.FastJsonUtil;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shangpin.api.airshop.config.ApiServiceUrlConfig;
import com.shangpin.api.airshop.product.d.PageDTO;
import com.shangpin.api.airshop.product.d.PageProductDTO;
import com.shangpin.api.airshop.product.d.ProductDTO;
import com.shangpin.api.airshop.product.d.ResponseDTO;
import com.shangpin.api.airshop.product.d.SpuProductDTO;
import com.shangpin.api.airshop.product.d.hub.HubPageDTO;
import com.shangpin.api.airshop.product.d.hub.HubProductDTO;
import com.shangpin.api.airshop.product.v.ProductVO;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.multipart.MultipartFile;

@Component
@Slf4j
public class ProductManager {

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * 保存或者提交商品信息
	 * 
	 * @param supplierId
	 * @param addOrUpdate
	 * @param dto
	 * @return
	 */
	public ResponseDTO hubSaveOrSubmmit(String supplierId, SpuProductDTO productDTO, String addOrUpdate) {
		log.info("========>>>" + productDTO.toString());
		ResponseDTO req = null;
		try {
			String uri = null;

			if (addOrUpdate != null && "add".equals(addOrUpdate)) {
				uri = ApiServiceUrlConfig.addHubProduct();
			} else if (addOrUpdate != null && "update".equals(addOrUpdate)) {
				uri = ApiServiceUrlConfig.updateHubProduct();
			}
			req = restTemplate.postForObject(uri +"/"+ supplierId, productDTO,
					ResponseDTO.class);
		} catch (RestClientException e) {
			e.printStackTrace();
			throw new RuntimeException("When call the iog URL : /producthandle/saveProduct occur exception !", e);
		}
		return req;
	}
	
	
	/**
	 * 保存或者提交商品信息
	 * 
	 * @param supplierId
	 * @param addOrUpdate
	 * @param dto
	 * @return
	 */
	public ResponseDTO saveOrSubmmit(String supplierId, SpuProductDTO productDTO, String addOrUpdate) {
		log.info("========>>>" + productDTO.toString());
		ResponseDTO req = null;
		try {
			String uri = null;

			if (addOrUpdate != null && "add".equals(addOrUpdate)) {
				uri = ApiServiceUrlConfig.getSaveProduct();
			} else if (addOrUpdate != null && "update".equals(addOrUpdate)) {
				uri = ApiServiceUrlConfig.getUpdateProduct();
			}
			req = restTemplate.postForObject(ApiServiceUrlConfig.getProductHost() + uri + supplierId, productDTO,
					ResponseDTO.class);
		} catch (RestClientException e) {
			e.printStackTrace();
			throw new RuntimeException("When call the iog URL : /producthandle/saveProduct occur exception !", e);
		}
		return req;
	}

	/**
	 * 编辑商品信息
	 * 
	 * @param supplierId
	 * @param dto
	 * @return
	 */
	public ResponseDTO edit(String supplierId, List<ProductDTO> dtoes) {
		return restTemplate.postForObject(
				ApiServiceUrlConfig.getProductHost() + ApiServiceUrlConfig.getUpdateProduct() + supplierId, dtoes,
				ResponseDTO.class);
	}

	/**
	 * 更新商品信息
	 * 
	 * @param supplierId
	 * @param dtoes
	 * @return
	 */
	public ResponseDTO updateProduct(String supplierId, List<ProductDTO> dtoes) {
		return restTemplate.postForObject(
				ApiServiceUrlConfig.getProductHost() + ApiServiceUrlConfig.getUpdateProduct() + supplierId, dtoes,
				ResponseDTO.class);
	}

	/**
	 * 批量查询商品信息
	 * 
	 * @param qv2Dto
	 * @return
	 */
	public HubPageDTO queryHub(HubProductDTO qv2Dto, String supplierId, Integer pageSize, Integer pageIndex) {

		PageProductDTO qv2Dto1 = new PageProductDTO();
		qv2Dto1.setSkuId(qv2Dto.getSkuId());
		qv2Dto1.setMemo(qv2Dto.getMemo());
		qv2Dto1.setBarcode(qv2Dto.getBarcode());
		qv2Dto1.setBrandName(qv2Dto.getBrandName());
		qv2Dto1.setProductCode(qv2Dto.getProductCode());
		qv2Dto1.setProductName(qv2Dto.getProductName());
		qv2Dto1.setSpSkuId(qv2Dto.getSpSkuId());
		qv2Dto1.setColor(qv2Dto.getColor());
		qv2Dto1.setSize(qv2Dto.getSize());
		qv2Dto1.setCategoryName(qv2Dto.getCategoryName());
		qv2Dto1.setSizeClass(qv2Dto.getSizeClass());

		if (qv2Dto.getSpStatus().equals("") || qv2Dto.getSpStatus().equals("null")) {
			qv2Dto1.setSpStatus("0");
		} else {
			qv2Dto1.setSpStatus(qv2Dto.getSpStatus());
		}
		if (qv2Dto.getStatus().equals("") || qv2Dto.getStatus().equals("null")) {
			qv2Dto1.setStatus("0");
		} else {
			qv2Dto1.setStatus(qv2Dto.getStatus());
		}

		qv2Dto1.setSupplierId(supplierId);
		qv2Dto1.setPageIndex(pageIndex);
		qv2Dto1.setPageSize(pageSize);
		
//		String json = FastJsonUtil.serialize2StringEmpty(qv2Dto1);
//		String ss =  HttpClientUtil.doPostForJson(ApiServiceUrlConfig.getQueryHubProduct() , json);
//		System.out.println(ss);
		HubPageDTO pageDto = restTemplate.postForObject(ApiServiceUrlConfig.getQueryHubProduct(), qv2Dto1,
				HubPageDTO.class);
		return pageDto;

	}

	/**
	 * 批量查询商品信息
	 * 
	 * @param qv2Dto
	 * @return
	 */
	public PageDTO query(ProductDTO qv2Dto, String supplierId, Integer pageSize, Integer pageCode) {

		PageProductDTO qv2Dto1 = new PageProductDTO();
		qv2Dto1.setSkuId(qv2Dto.getSkuId());
		qv2Dto1.setMemo(qv2Dto.getMemo());
		qv2Dto1.setBarcode(qv2Dto.getBarcode());
		qv2Dto1.setBrandName(qv2Dto.getBrandName());
		qv2Dto1.setProductCode(qv2Dto.getProductCode());
		qv2Dto1.setProductName(qv2Dto.getProductName());
		qv2Dto1.setSpSkuId(qv2Dto.getSpSkuId());
		qv2Dto1.setColor(qv2Dto.getColor());
		qv2Dto1.setSize(qv2Dto.getSize());
		qv2Dto1.setCategoryName(qv2Dto.getCategoryName());
		qv2Dto1.setSizeClass(qv2Dto.getSizeClass());

		if (qv2Dto.getSpStatus().equals("") || qv2Dto.getSpStatus().equals("null")) {
			qv2Dto1.setSpStatus("0");
		} else {
			qv2Dto1.setSpStatus(qv2Dto.getSpStatus());
		}
		if (qv2Dto.getStatus().equals("") || qv2Dto.getStatus().equals("null")) {
			qv2Dto1.setStatus("0");
		} else {
			qv2Dto1.setStatus(qv2Dto.getStatus());
		}

		// qv2Dto1.setStatus("0");
		// qv2Dto1.setSpStatus("0");
		// String json =
		// restTemplate.postForObject(ApiServiceUrlConfig.getProductHost()+ApiServiceUrlConfig.getQueryProduct()+supplierId+"/"+pageSize+"/"+pageCode,
		// qv2Dto1, String.class);
		PageDTO pageDto1 = new PageDTO();
		PageDTO pageDto = restTemplate.postForObject(ApiServiceUrlConfig.getProductHost()
				+ ApiServiceUrlConfig.getQueryProduct() + supplierId + "/" + pageSize + "/" + pageCode, qv2Dto1,
				PageDTO.class);
		return pageDto;

	}

	public byte[] readInputStream(InputStream inputStream) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			outputStream.write(buffer, 0, len);
		}
		outputStream.close();
		inputStream.close();
		return outputStream.toByteArray();
	}

	@Autowired
	private HttpClient client;

	/**
	 * 上传图片接口IPC
	 * 
	 * @param airshop
	 * @param fileName
	 * @param stream
	 * @throws UnsupportedEncodingException
	 */
	public String uploadPic(InputStream stream, String airshop, String fileName) throws Exception {

		Base64 base64 = new Base64();
		BufferedImage image = ImageIO.read(stream);
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		ImageOutputStream imageOut = ImageIO.createImageOutputStream(bs);
		ImageIO.write(image, "jpg", imageOut);
		byte[] bt = bs.toByteArray();
		String content = new String(base64.encode(bt));
		Map<String, String> params = new HashMap<String, String>();
		params.put("extension", fileName.substring(fileName.lastIndexOf(".")));
		params.put("picturetype", "airshop");
		params.put("picFileContent", content);
		HttpPost post = new HttpPost("http://mobile.apiv2.shangpin.com" + ApiServiceUrlConfig.getSavePicture());
		List<NameValuePair> p = new ArrayList<>();
		NameValuePair e1 = new BasicNameValuePair("extension", fileName.substring(fileName.lastIndexOf(".")));
		NameValuePair e2 = new BasicNameValuePair("picturetype", "airshop");
		NameValuePair e3 = new BasicNameValuePair("picFileContent", content);
		p.add(e1);
		p.add(e2);
		p.add(e3);
		log.info("上传图片："+post.getURI());
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(p);
		post.setEntity(entity);
		HttpResponse response = client.execute(post);
		ObjectMapper mapper = new ObjectMapper();
		
//		InputStream reader = response.getEntity().getContent();
//		  StringBuffer   out   =   new   StringBuffer(); 
//	        byte[]   b   =   new   byte[4096]; 
//	        for   (int   n;   (n   =   reader.read(b))   !=   -1;)   { 
//	                out.append(new   String(b,   0,   n)); 
//	        } 
//	           log.info("上传图片返回结果："+ out.toString());
//	           reader.close();
//		log.info("上传图片返回结果："+response.getEntity().getContent().toString());
		Map<String, Map<String, Object>> productMap = mapper.readValue(response.getEntity().getContent(), Map.class);
		Map<String, Object> innerMap = productMap.get("content");
		String picUrl = (String) innerMap.get("success");
		return picUrl;
	}

    /**
     * studio 上传图片
     * @param stream
     * @param airshop
     * @param fileName
     * @return
     * @throws Exception
     */
    public String uploadStudioPic(InputStream stream, String airshop, String fileName) throws Exception {
        Base64 base64 = new Base64();
        BufferedImage image = ImageIO.read(stream);
        ByteArrayOutputStream bs = new ByteArrayOutputStream();
        ImageOutputStream imageOut = ImageIO.createImageOutputStream(bs);
        ImageIO.write(image, "jpg", imageOut);
        byte[] bt = bs.toByteArray();
        String content = new String(base64.encode(bt));

        Map<String,String> param = new HashMap<>();
        param.put("content", content);
        param.put("extension", fileName.substring(fileName.lastIndexOf(".")));

        ResponseEntity<String> response  = restTemplate.postForEntity(ApiServiceUrlConfig.getSavePictureForStudio(), param, String.class);
        return response.getStatusCode() == HttpStatus.OK ? response.getBody():null;
    }


	public SpuProductDTO editQuery(String sopUserNo, String itemCode) {
		SpuProductDTO spuDto = null;
		try {
			spuDto = restTemplate.getForObject(ApiServiceUrlConfig.getProductHost()
					+ ApiServiceUrlConfig.getFindProductById() + sopUserNo + "/" + itemCode, SpuProductDTO.class);
		} catch (RestClientException e) {
			e.printStackTrace();
			throw new RuntimeException("When call the iog URL : /product/searchProductBySupplierIdAndSpuId/ occur exception !",
					e);
		}
		return spuDto;
	}
	public ProductVO editQueryHub(String sopUserNo, String itemCode) {
		ProductVO spuDto = null;
		try {
			spuDto = restTemplate.getForObject(ApiServiceUrlConfig.getQueryHubProductDetail() + "/" + itemCode, ProductVO.class);
		} catch (RestClientException e) {
			e.printStackTrace();
			throw new RuntimeException("When call the iog URL : /product/searchProductBySupplierIdAndSpuId/ occur exception !",
					e);
		}
		return spuDto;
	}
	public ResponseDTO batchSaveProduct(List<ProductDTO> dtoes, String supplierId) {
		int size = dtoes.size();// 总记录数
		int buffsize=100;
		List<ProductDTO> buffer = new ArrayList<>(buffsize);
		ResponseDTO dto = null;
		long time=0;
		for (int i = 0; i < size; i++) {

			buffer.add(dtoes.get(i));
			if ((i != 0 && i % buffsize == 0) || i == size - 1) {
				time=System.currentTimeMillis();
				dto = restTemplate.postForObject(
						ApiServiceUrlConfig.getProductHost() + ApiServiceUrlConfig.getBatchSaveProduct() + supplierId,
						buffer, ResponseDTO.class);
				buffer.clear();
				time=System.currentTimeMillis()-time;
		//	try {Thread.sleep(1000);} catch (InterruptedException e) {}// TODO
				log.info("{} per time ,times:{},use time:{}s+{}ms",buffsize,i,time/1000,time%1000);
			}
		}
		return dto;
	}
	public ResponseDTO batchSaveProductHub(List<ProductDTO> dtoes, String supplierId) {
		int size = dtoes.size();// 总记录数
		int buffsize=100;
		List<ProductDTO> buffer = new ArrayList<>(buffsize);
		ResponseDTO dto = null;
		long time=0;
		for (int i = 0; i < size; i++) {

			buffer.add(dtoes.get(i));
			if ((i != 0 && i % buffsize == 0) || i == size - 1) {
				time=System.currentTimeMillis();
				dto = restTemplate.postForObject(ApiServiceUrlConfig.getHubBatchSaveProduct() +"/"+supplierId,
						buffer, ResponseDTO.class);
				buffer.clear();
				time=System.currentTimeMillis()-time;
		//	try {Thread.sleep(1000);} catch (InterruptedException e) {}// TODO
				log.info("{} per time ,times:{},use time:{}s+{}ms",buffsize,i,time/1000,time%1000);
			}
		}
		return dto;
	}
	/*public ResponseDTO batchSaveProduct(final List<ProductDTO> dtoes,final String supplierId) {
		ResponseDTO dto = new ResponseDTO();
		final int size = dtoes.size();// 总记录数
		//duo xian cheng
		new Thread(new Runnable() {
			@Override
			public void run() {
				
			 final int buffsize=100;
			 List<ProductDTO> buffer = new ArrayList<>();
			 long time=0;
				for (int i = 0; i < size; i++) {
					
						buffer.add(dtoes.get(i));
					    if ((i != 0 && i % buffsize == 0) || i == size - 1) {
						time=System.currentTimeMillis();
						restTemplate.postForObject(
								ApiServiceUrlConfig.getProductHost() + ApiServiceUrlConfig.getBatchSaveProduct() + supplierId,
								buffer, ResponseDTO.class);
						buffer.clear();
						time=System.currentTimeMillis()-time;
						log.info("{} per time ,times:{},use time:{}s+{}ms",buffsize,i,time/1000,time%1000);
					}
					
				}
			}
		}).start();
		dto.setResponseCode(1);
		dto.setResponseMsg("Please wait five minutes , the data is being processed");
		return dto;
	}*/
	
	
	
	
	
	/*
	 * public static void main(String[] args) throws Exception{ Base64 base64 =
	 * new Base64(); InputStream input = new FileInputStream(new
	 * File("C:/yan.jpg")); BufferedImage image = ImageIO.read(input);
	 * ByteArrayOutputStream bs = new ByteArrayOutputStream(); ImageOutputStream
	 * imageOut = ImageIO .createImageOutputStream(bs); ImageIO.write(image,
	 * "jpg", imageOut); byte[] bt = bs.toByteArray(); String str = new
	 * String(base64.encode(bt)); System.out.println(str); Map<String,String>
	 * param = new HashMap<>(); param.put("extension", ".jpg");
	 * param.put("picturetype", "user"); param.put("picFileContent", str);
	 * MultiValueMap<String, String> params = new LinkedMultiValueMap<String,
	 * String>(); params.setAll(param); String
	 * url="http://qa.mobilev2.shangpin.com/shangpin/SaveMobilePicNew";
	 * RestTemplate rest = new RestTemplate(); String rs=rest.postForObject(url,
	 * params, String.class); System.out.println(rs); }
	 */

}
