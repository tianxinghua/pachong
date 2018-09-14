package com.shangpin.ephub.product.business.rest.picture.controller;

import com.shangpin.ephub.product.business.rest.picture.service.ISelectHubSpuPicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * Created by zhaowenjun on 2018/9/1.
 */
@RestController
@RequestMapping(value = "/picture")
@Slf4j
public class PictureRestController {

    @Autowired
    private ISelectHubSpuPicService selectHubSpuPicService;

    @RequestMapping(value = "/{spSpuNo}")
    public List<String> selectHubSpuPic(@PathVariable String spSpuNo){
            log.info("============获取图片路径==============");
            if(null != spSpuNo && !spSpuNo.equals("")){
               List<String> picUrl = selectHubSpuPicService.selectHubSpuPic(spSpuNo);
               return picUrl;
            }
            return null;
    }

}
