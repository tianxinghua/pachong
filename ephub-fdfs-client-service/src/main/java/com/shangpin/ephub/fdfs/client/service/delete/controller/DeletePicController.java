package com.shangpin.ephub.fdfs.client.service.delete.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.fdfs.client.service.delete.dto.DeletePicDto;
import com.shangpin.ephub.fdfs.client.service.delete.service.DeletePicService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>Title:UploadController.java </p>
 * <p>Description: 图片删除控制器</p>
 * <p>Company: www.shangpin.com</p> 
 * @author yanxiaobin
 * @date 2017年1月2日 下午5:27:46
 */
@RestController
@RequestMapping(value = "/delete-pic")
@Slf4j
public class DeletePicController {
	
	@Autowired
	private DeletePicService deletePicService;
	/**
	 * 图片上传
	 * @param file 文件
	 * @param request 
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public Map<String,Integer> delete(@RequestBody DeletePicDto deletePicDto) {
		Map<String,Integer> result = null;
		List<String> urls = deletePicDto.getUrls();
		if (CollectionUtils.isNotEmpty(urls)) {
			result = new HashMap<>();
			for (String url : urls) {
				if (StringUtils.isNotBlank(url)) {
					try {
						deletePicService.deleteByUrl(url);
						result.put(url, 0);
					} catch (Throwable e) {
						result.put(url, 2);
						e.printStackTrace();
						log.error(e.getMessage(), e);
					}
				} else {
					result.put(url, 1);
				}
			}
		}
		return result;
    }
}
