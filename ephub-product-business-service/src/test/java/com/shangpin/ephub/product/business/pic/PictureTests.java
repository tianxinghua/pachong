package com.shangpin.ephub.product.business.pic;

import com.shangpin.ephub.product.business.rest.picture.service.ISelectHubSpuPicService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.ClientProtocolException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.List;

/**
 * Created by zhaowenjun on 2018/9/3.
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Slf4j
public class PictureTests {

   @Autowired
    private ISelectHubSpuPicService selectHubSpuPicService;

    @Test
    public void picSelectTest() throws ClientProtocolException, IOException{
        String spSpuNo="30389559";
        List<String> picUrl = selectHubSpuPicService.selectHubSpuPic(spSpuNo);
        log.info("============获取图片路径==============" + picUrl);
    }
}
