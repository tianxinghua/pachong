package com.shangpin.ephub.product.business.rest.studio.studio.controller;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.net.ftp.FTPFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shangpin.ephub.client.data.mysql.studio.pic.dto.HubSlotSpuPicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.studio.pic.dto.HubSlotSpuPicDto;
import com.shangpin.ephub.client.data.mysql.studio.pic.gateway.HubSlotSpuPicGateway;
import com.shangpin.ephub.client.data.mysql.studio.supplier.dto.HubSlotSpuSupplierDto;
import com.shangpin.ephub.client.data.studio.dic.dto.StudioDicCalendarDto;
import com.shangpin.ephub.client.data.studio.dic.dto.StudioDicSlotDto;
import com.shangpin.ephub.client.data.studio.slot.slot.dto.StudioSlotDto;
import com.shangpin.ephub.client.data.studio.studio.dto.StudioDto;
import com.shangpin.ephub.product.business.rest.studio.studio.dto.ResultObjList;
import com.shangpin.ephub.product.business.rest.studio.studio.dto.ResultResponseDto;
import com.shangpin.ephub.product.business.rest.studio.studio.service.StudioDicCalendarService;
import com.shangpin.ephub.product.business.rest.studio.studio.service.StudioDicSlotService;
import com.shangpin.ephub.product.business.rest.studio.studio.service.StudioService;
import com.shangpin.ephub.product.business.rest.studio.studio.service.StudioSlotService;
import com.shangpin.ephub.product.business.service.studio.hubslot.HubSlotSpuSupplierService;
import com.shangpin.ephub.product.business.ui.studio.common.pictrue.service.PictureService;
import com.shangpin.ephub.product.business.ui.task.common.util.FTPClientUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 创建studio批次 wangchao 2017-06-16
 * 
 */
@RestController
@RequestMapping(value = "/studio-slot")
@Slf4j
public class StudioSlotController {

	@Autowired
	StudioService studioService;
	@Autowired
	StudioDicCalendarService studioDicCalendarService;
	@Autowired
	StudioSlotService studioSlotService;
	@Autowired
	StudioDicSlotService studioDicSlotService;
	@Autowired
	private PictureService pictureService;
	@Autowired
	private HubSlotSpuSupplierService hubSlotSpuSupplierService;
	@Autowired
	private HubSlotSpuPicGateway hubSlotSpuPicGateway;
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat sdfomat = new SimpleDateFormat("yyyy/MM/dd");
	SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");

	@RequestMapping(value = "/create")
	public boolean createStudioSlot() {
		log.info("摄影棚批次创建----start");
		try {
			List<StudioDto> listStudioDto = studioService.getAllStudio();
			for (StudioDto studioDto : listStudioDto) {
				long studioId = studioDto.getStudioId();
				String studioNo = studioDto.getStudioNo();
				List<StudioDicCalendarDto> listStudioDicCalendarDto = studioDicCalendarService
						.getStudioDicCalendarByStudioId(studioId);
				if (listStudioDicCalendarDto == null || listStudioDicCalendarDto.size() == 0) {
					log.error("studioId:" + studioId + "studioNo:" + studioNo + "摄影棚日历不存在，请检查！");
					continue;
				}

				int period = studioDto.getPeriod();
				Calendar c = Calendar.getInstance();
				String from = sdf.format(c.getTime());
				String to;
				if (period - 1 < 0) {
					log.error("studioId:" + studioId + "studioNo:" + studioNo + "摄影棚批次周期设置有误，批次生成失败");
					continue;
				} else {
					c.add(Calendar.DAY_OF_MONTH, period - 1);
					to = sdf.format(c.getTime());
				}
				String calenderTemplateId = listStudioDicCalendarDto.get(0).getCalenderTemplateId();

				// 根据studioid 查询 studio_dic_slot表，确认每天生成几个批次
				List<StudioDicSlotDto> studioDicSlotList = studioDicSlotService.getStudioDicSlotByStudioId(studioId);
				if (studioDicSlotList == null || studioDicSlotList.size() == 0) {
					log.info("studioId:" + studioId + "studioNo:" + studioNo + "摄影棚批次基础表不存在数据,批次生成失败");
					continue;
				}
				// 调用api
				ResultResponseDto<ResultObjList> data = studioDicCalendarService.getStudioOffDayCalendarByApi(from, to,
						calenderTemplateId);
				if (!data.getSuccess()) {
					log.error("studioId:" + studioId + "studioNo:" + studioNo + "调用摄影日历API返回false！,批次生成失败");
					continue;
				}
				int num = data.getResultObjList().size();
				// 倒序判断是否需要生成批次
				for (int i = num - 1; i >= 0; i--) {
					// 根据当天时间查询批次管理表，当天是否有待拍摄批次
					ResultObjList resultObjList = data.getResultObjList().get(i);
					String DT = resultObjList.getDT();
					List<StudioSlotDto> studioSlotList = studioSlotService.getStudioSlotBySlotDate(sdfomat.parse(DT));
					if (studioSlotList == null || studioSlotList.size() == 0) {
						if (resultObjList.getIsOffDay() == 0) {
							int slot_efficiency = studioDicSlotList.get(0).getSlotEfficiency();
							for (int j = 1; j <= slot_efficiency; j++) {
								StudioSlotDto studioSlotDto = createStudioSlotDto(studioId, DT, studioNo, j);
								studioSlotService.insertStudioSlot(studioSlotDto);
							}
						}
					} else {
						break;
					}
				}
			}
			log.info("摄影棚批次创建-----end");
			return true;
		} catch (Exception e) {
			log.error("createStudioSlot处理发生异常：{}", e);
			e.printStackTrace();
		}
		return false;
	}
	
	@RequestMapping(value = "/check")
	public boolean checkStudioSlot() {
		try {
			// 1.当日之前，未被申请的批次需要处理为已过期。
			studioSlotService.selectAndUpdateStudioSlotBeforeSlotDate();
			// 2. 查询摄影棚当天收货状态，对超过预定的收货时间的批次进行处理
			studioSlotService.selectAndUpdateStudioSlotAfterPlanArriveDate();
			// 3. 查询当天之前的拍摄状态，如果有未设置拍摄状态的批次進行处理
			studioSlotService.selectAndUpdateStudioSlotBeforeSlotDateByShotStatus();
			// 查询当天拍摄的批次进行处理。
			studioSlotService.selectAndUpdateStudioSlotByShootTime();
			return true;
		} catch (Exception e) {
			log.error("checkStudioSlot处理发生异常：{}", e);
			e.printStackTrace();
		}
		return false;
	}
	
	@RequestMapping(value = "/downloadImage")
	public boolean downLoadImageByFtp() {
		try {
			Calendar calendar = Calendar.getInstance();  
	        calendar.setTime(new Date());  
	        calendar.add(Calendar.DAY_OF_MONTH, -1);  
	        String newDate = sd.format(calendar.getTime()); 
			List<StudioSlotDto> studioDtoList = studioSlotService.selectStudioSlotByShootTime();
			for (StudioSlotDto studioSlotDto : studioDtoList) {
				String slotNo = studioSlotDto.getSlotNo();
				List<HubSlotSpuSupplierDto> hubSlotSpuSupplierDtoLists = hubSlotSpuSupplierService.getSlotSpuSupplierBySlotNo(slotNo);
				for(HubSlotSpuSupplierDto hubSlotSpuSupplierDto : hubSlotSpuSupplierDtoLists){
					long slotSpuId = hubSlotSpuSupplierDto.getSlotSpuId();
					long slotSpuSupplierId = hubSlotSpuSupplierDto.getSlotSpuSupplierId();
					String slotSpuNo = hubSlotSpuSupplierDto.getSlotSpuNo();
					String supplierNo = hubSlotSpuSupplierDto.getSupplierNo();
					String supplierId = hubSlotSpuSupplierDto.getSupplierId();
					
					String pathName = new String("/home/dev/"+newDate+"/"+slotNo+"/"+slotSpuNo+"/");
//					String pathName = new String("/home/dev/ftpLoad/");
					FTPFile[] files = FTPClientUtil.getFiles(pathName);
					for (FTPFile file : files) {
						try {
							String fileName = file.getName();
							String downLoadAddress = "ftpLoad/" + fileName;
							InputStream in = FTPClientUtil.downFile(downLoadAddress);

							ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
							byte[] buff = new byte[100]; // buff用于存放循环读取的临时数据
							int rc = 0;
							while ((rc = in.read(buff, 0, 100)) > 0) {
								swapStream.write(buff, 0, rc);
							}
							byte[] in_b = swapStream.toByteArray(); // in_b为转换之后的结果
							String extension = pictureService.getExtension(fileName);
							String fdfsURL = pictureService.uploadPic(in_b, extension);
							
							HubSlotSpuPicDto dto = createHubSlotSpuPicDto(slotSpuId, slotSpuSupplierId, slotSpuNo, supplierNo, supplierId, fdfsURL, extension);
							
							hubSlotSpuPicGateway.insertSelective(dto);
							
							in.close();
						} catch (Exception e) {
							log.error(file.getName() + "图片上传发生异常：{}", e);
							e.printStackTrace();
						}
					}
				}
				
			}
			return true;
		} catch (Exception e) {
			log.error("downImgByFtp处理发生异常：{}", e);
			e.printStackTrace();
		}
		return false;
	}

	private StudioSlotDto createStudioSlotDto(long studioId, String DT, String studioNo, int num) {
		StudioSlotDto dto = new StudioSlotDto();
		dto.setStudioId(studioId);
		String slotNo = DT.replaceAll("/", "") + studioNo + "00" + num;
		dto.setSlotNo(slotNo);
		dto.setSlotStatus((byte) 0);
		dto.setApplyStatus((byte) 0);
		dto.setCreateTime(new Date());
		dto.setUpdateTime(new Date());
		dto.setShotStatus((byte) 0);
		try {
			dto.setSlotDate(sdfomat.parse(DT));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dto;
	}
	private HubSlotSpuPicDto createHubSlotSpuPicDto(long slotSpuId, long slotSpuSupplierId, String slotSpuNo, String supplierNo,String supplierId,String picUrl,String extension) {
		HubSlotSpuPicDto dto = new HubSlotSpuPicDto();
		dto.setSlotSpuId(slotSpuId);
		dto.setSlotSpuNo(slotSpuNo);
		dto.setSlotSpuSupplierId(slotSpuSupplierId);
		dto.setSupplierNo(supplierNo);
		dto.setSupplierId(supplierId);
		dto.setCreateTime(new Date());
		dto.setUpdateTime(new Date());
		dto.setSpPicUrl(picUrl);
		dto.setPicExtension(extension);
		dto.setCreateUser("admin");
		return dto;
	}
	public static void main(String[] args){
		System.out.println("2017/06/19".replaceAll("/", ""));
		
	}
}
