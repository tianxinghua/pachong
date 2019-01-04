package com.shangpin.api.airshop.product.c;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.shangpin.api.airshop.config.ApiServiceUrlConfig;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.shangpin.api.airshop.config.ImageCDNConfig;
import com.shangpin.api.airshop.product.m.ProductManager;
import com.shangpin.api.airshop.product.o.UploadPicResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:ProductImageController </p>
 * <p>Description: 商品图片控制器</p>
 * <p>Company: shangpin</p> 
 * @author : yanxiaobin
 * @date :2016年4月22日 下午5:16:30
 */
@RestController
@RequestMapping("/product")
@Slf4j
public class ProductImageController{
	
	@Autowired
	private ProductManager pm;
	@Autowired
	private ImageCDNConfig cdnCfg;
	
	/**
	 * 上传图片控制器
	 * @throws Exception 
	 */
	@RequestMapping("/uploadMultiPic")
	public UploadPicResponse uploadMultiPic(HttpServletRequest req) throws Exception{
		UploadPicResponse upr = new UploadPicResponse();
		  // 判断提交的请求是否包含文件
        boolean isMultipart = ServletFileUpload.isMultipartContent(req);

        if (!isMultipart) {
        	upr.setCode(1);
        	upr.setMsg("This request is not Multipart !");;
            return upr;
        }
		try {
			MultipartHttpServletRequest mreq = (MultipartHttpServletRequest)req;
			Map<String, MultipartFile> map = mreq.getFileMap();
			 List<String> urls= null;
			if (map != null) {
				urls = new ArrayList<>();
				 Collection<MultipartFile> values = map.values();
				if (values != null) {
					 for (MultipartFile multipartFile : values) {
						 InputStream stream = multipartFile.getInputStream();
						 String originalFilename = multipartFile.getOriginalFilename();
						 String uploadPic = pm.uploadPic(stream,"airshop",originalFilename);
						 uploadPic=reparseImageUrl(uploadPic);
						 urls.add(uploadPic);
					} 
				}
			}
			upr.setCode(0);
			upr.setMsg("OK");
			upr.setPicUrls(urls);
		} catch (Exception e) {
			upr.setCode(2); 
			upr.setMsg(e.getMessage());
			e.printStackTrace();
			log.error("upload image occur exception", e);
		}
		 return upr;
	}

	@RequestMapping("/uploadMultiStudioPic")
	public UploadPicResponse uploadStudioPic(HttpServletRequest req)throws Exception{


        UploadPicResponse upr = new UploadPicResponse();
        // 判断提交的请求是否包含文件
        boolean isMultipart = ServletFileUpload.isMultipartContent(req);

        if (!isMultipart) {
            upr.setCode(1);
            upr.setMsg("This request is not Multipart !");;
            return upr;
        }
        try {
            MultipartHttpServletRequest mreq = (MultipartHttpServletRequest)req;
            Map<String, MultipartFile> map = mreq.getFileMap();
            List<String> urls= null;
            if (map != null) {
                urls = new ArrayList<>();
                Collection<MultipartFile> values = map.values();

                if (values != null) {
                    for (MultipartFile multipartFile : values) {
                        InputStream stream = multipartFile.getInputStream();
                        String originalFilename = multipartFile.getOriginalFilename();
                        String uploadPic = pm.uploadStudioPic(stream,"airshop",originalFilename);
                        uploadPic=reparseImageUrl(uploadPic);
                        urls.add(uploadPic);
                    }
                }
            }
            upr.setCode(0);
            upr.setMsg("OK");
            upr.setPicUrls(urls);
        } catch (Exception e) {
            upr.setCode(2);
            upr.setMsg(e.getMessage());
            e.printStackTrace();
            log.error("upload image occur exception", e);
        }
        return upr;
	}

	/**
	 * fck图片上传接口
	 */
	@RequestMapping(value = "/uploadPic")
	public void uploadPic(MultipartFile uploadPic,HttpServletRequest request,HttpServletResponse response){
		 response.setContentType("text/json;charset=UTF-8");
         response.setHeader("Cache-Control", "no-cache");
         try {
			PrintWriter out = response.getWriter();
			 InputStream stream = uploadPic.getInputStream();
			 String originalFilename = uploadPic.getOriginalFilename();
			 String picUrl = pm.uploadPic(stream,"airshop",originalFilename);
			 picUrl=reparseImageUrl(picUrl);
			 // 将上传的图片的url返回给ckeditor
			 String callback = request.getParameter("CKEditorFuncNum");
			 out.println("<script type=\"text/javascript\">");
			 out.println("window.parent.CKEDITOR.tools.callFunction("
			         + callback + ",'" + picUrl + "',''" + ")");
			 out.println("</script>");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String reparseImageUrl(String picUrl){
		/*cdnCfg = new ImageCDNConfig();
		cdnCfg.setCdnList(new ArrayList<String>());
		cdnCfg.getCdnList().add("pic.shangpn.com");*/
		int idxProtocol=picUrl.indexOf("///");
		String realUrl=picUrl;
		if(idxProtocol>0 && cdnCfg!=null && CollectionUtils.isNotEmpty(cdnCfg.getCdnList())){
			int rdmCdn=RandomUtils.nextInt(cdnCfg.getCdnList().size());
			String cndHost=cdnCfg.getCdnList().get(rdmCdn);
			realUrl=picUrl.substring(0,idxProtocol+2);
			realUrl+=cndHost+picUrl.substring(idxProtocol+2);
		}
		return realUrl;
	}
		
}
