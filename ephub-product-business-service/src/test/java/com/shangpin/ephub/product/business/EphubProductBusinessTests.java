package com.shangpin.ephub.product.business;


import com.alibaba.fastjson.JSONObject;
import com.shangpin.ephub.client.data.mysql.enumeration.DataState;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.data.mysql.studio.spusupplierunion.dto.SpuSupplierQueryDto;
import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuPicCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuPicDto;
import com.shangpin.ephub.client.data.studio.slot.defective.dto.StudioSlotDefectiveSpuPicWithCriteriaDto;
import com.shangpin.ephub.client.data.studio.slot.defective.gateway.StudioSlotDefectiveSpuPicGateWay;
import com.shangpin.ephub.client.fdfs.dto.UploadPicDto;
import com.shangpin.ephub.client.fdfs.gateway.UploadPicGateway;
import com.shangpin.ephub.client.product.business.gms.result.HubResponseDto;
import com.shangpin.ephub.client.product.business.size.result.MatchSizeResult;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.product.business.conf.mail.sender.ShangpinMailSender;
import com.shangpin.ephub.product.business.conf.rpc.ApiAddressProperties;
import com.shangpin.ephub.product.business.rest.gms.dto.CategoryScreenSizeDom;
import com.shangpin.ephub.product.business.rest.gms.dto.SizeRequestDto;
import com.shangpin.ephub.product.business.rest.gms.dto.SizeStandardItem;
import com.shangpin.ephub.product.business.service.studio.hubslot.HubSlotSpuService;
import com.shangpin.ephub.product.business.service.studio.hubslot.dto.SlotSpuDto;
import com.shangpin.ephub.product.business.ui.pending.dto.PendingQuryDto;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProductDto;
import com.shangpin.ephub.product.business.ui.pending.vo.PendingProducts;
import com.shangpin.ephub.product.business.ui.studio.common.pictrue.service.PictureService;
import com.shangpin.ephub.product.business.ui.studio.pending.service.StudioPendingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest
public class EphubProductBusinessTests {

	@Autowired
	private RestTemplate httpClient;
	@Autowired
	ApiAddressProperties ApiAddressProperties;





	@Autowired
	private ShangpinMailSender shangpinMailSender;





	@Autowired
	HubSlotSpuService hubSlotSpuService;

	@Autowired
	HubSpuPendingGateWay gateWay;

	@Test
	public void testAddSlotSpu(){

		PendingProductDto pendingProductDto = new PendingProductDto();
		HubSpuPendingCriteriaDto criteriaDto = new HubSpuPendingCriteriaDto();
		criteriaDto.createCriteria().andSpuPendingIdEqualTo(258248L);
		List<HubSpuPendingDto> spuPendingDtos   = gateWay.selectByCriteria(criteriaDto);
		if(null!=spuPendingDtos&&spuPendingDtos.size()>0){
			BeanUtils.copyProperties(spuPendingDtos.get(0),pendingProductDto);

//			pendingProductDto.setSpuModel("AC03WR17 404002");

			try {
				hubSlotSpuService.addSlotSpuAndSupplier(pendingProductDto);
//				hubSlotSpuService.updateSlotSpu(pendingProductDto);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}


	@Autowired
	private UploadPicGateway uploadPicGateway;

	@Test
	public void testUpload(){

		try {
			File file = new File("E:\\other\\1.jpg");
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1)
            {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            byte[] byteArray = bos.toByteArray();
            String base64 = new BASE64Encoder().encode(byteArray );
            UploadPicDto dto = new UploadPicDto();
            dto.setBase64(base64);
            dto.setExtension("jpg");
            String url = uploadPicGateway.upload(dto );
    		System.out.println("url======"+url);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Autowired
	private PictureService pictureService;
	@Test
	public void testDeletePic(){
		List<String> urls =  new ArrayList<String>();
		urls.add("http://192.168.9.71/group2/M00/01/58/wKgJR1lMmNWAD8KKAAEsQm-Q7jY392.jpg");
//		urls.add("11111.jpg");
		Map<String, Integer> map = pictureService.deletePics(urls);
		System.out.println(map);
	}
	@Autowired
	private StudioSlotDefectiveSpuPicGateWay defectiveSpuPicGateWay;

	@Test
	public void testDelete(){
		StudioSlotDefectiveSpuPicWithCriteriaDto withCriteria = new StudioSlotDefectiveSpuPicWithCriteriaDto();
		StudioSlotDefectiveSpuPicCriteriaDto criteria = new StudioSlotDefectiveSpuPicCriteriaDto();
		criteria.createCriteria().andSpPicUrlEqualTo("http://192.168.9.71:80/group2/M00/02/45/wKgJR1jSXnWAIbaAAAJt212IoZ8475.jpg");
		withCriteria.setCriteria(criteria );
		StudioSlotDefectiveSpuPicDto studioSlotDefectiveSpuPicDto = new StudioSlotDefectiveSpuPicDto();
		studioSlotDefectiveSpuPicDto.setDataState(DataState.DELETED.getIndex());
		withCriteria.setStudioSlotDefectiveSpuPic(studioSlotDefectiveSpuPicDto );
		int i = defectiveSpuPicGateWay.updateByCriteriaSelective(withCriteria );
		System.out.println(i);
	}


	@Autowired
	HubSlotSpuService slotSpuService;


	@Test
	public void testSloSupplierSearch(){

		SpuSupplierQueryDto quryDto = new SpuSupplierQueryDto();
//		quryDto.setSpuModel("ML07");
		quryDto.setSupplierNo("S0000766");
		List<SlotSpuDto> slotSpu = slotSpuService.findSlotSpu(quryDto);


        System.out.println("slot size = " + slotSpu.size());

	}

	@Autowired
	StudioPendingService studioPendingService;
	@Test
	public void testFindPendingProducts(){
		PendingQuryDto pendingQuryDto  = new PendingQuryDto();
		pendingQuryDto.setShoot(true);
		PendingProducts pendingProducts = studioPendingService.findPendingProducts(pendingQuryDto,false);
	}



}
