package com.shangpin.asynchronous.task.consumer.productimport.pending.spu.service;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.shangpin.asynchronous.task.consumer.productexport.template.TaskImportTemplate2;
import com.shangpin.asynchronous.task.consumer.productimport.common.service.DataHandleService;
import com.shangpin.asynchronous.task.consumer.productimport.common.service.TaskImportService;
import com.shangpin.asynchronous.task.consumer.productimport.pending.spu.dao.HubColorImportDTO;
import com.shangpin.asynchronous.task.consumer.productimport.pending.spu.dao.HubPendingSpuImportDTO;
import com.shangpin.ephub.client.data.mysql.color.dto.HubColorDicCriteriaDto;
import com.shangpin.ephub.client.data.mysql.color.dto.HubColorDicDto;
import com.shangpin.ephub.client.data.mysql.color.dto.HubColorDicItemDto;
import com.shangpin.ephub.client.data.mysql.color.gateway.HubColorDicGateWay;
import com.shangpin.ephub.client.data.mysql.color.gateway.HubColorDicItemGateWay;
import com.shangpin.ephub.client.data.mysql.enumeration.MsgMissHandleState;
import com.shangpin.ephub.client.data.mysql.sku.dto.HubSkuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSpuPendingWithCriteriaDto;
import com.shangpin.ephub.client.data.mysql.spu.gateway.HubSpuPendingGateWay;
import com.shangpin.ephub.client.message.task.product.body.Task;
import com.shangpin.ephub.client.product.business.hubpending.sku.result.HubPendingSkuCheckResult;
import com.shangpin.ephub.client.product.business.hubpending.spu.dto.NohandleReason;
import com.shangpin.ephub.client.product.business.hubpending.spu.gateway.HubNohandleReasonGateWay;
import com.shangpin.ephub.client.product.business.hubpending.spu.gateway.HubPendingHandleGateWay;
import com.shangpin.ephub.client.product.business.mapp.dto.HubSupplierColorDicRequestDto;
import com.shangpin.ephub.client.product.business.mapp.gateway.DicRefreshGateWay;
import com.shangpin.ephub.client.product.business.size.result.MatchSizeResult;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.ephub.client.util.TaskImportTemplate;
import jdk.nashorn.internal.ir.ContinueNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * Title:SupplierOrderService.java
 * </p>
 * <p>
 * Description: 任务队列消费
 * </p>
 * <p>
 * Company: www.shangpin.com
 * </p>
 * 
 * @author zhaogenchun
 * @date 2016年11月23日 下午4:06:52
 */
@SuppressWarnings("rawtypes")
@Service
@Slf4j
public class PendingColorImportService {


	@Autowired
	HubSpuPendingGateWay hubSpuPendingGateWay;
	@Autowired
	DataHandleService dataHandleService;
	@Autowired
	TaskImportService taskService;
	@Autowired
	HubNohandleReasonGateWay nohandleGateWay;
	@Autowired
	HubPendingHandleGateWay pendingHandleGateWay;
	@Autowired
	HubColorDicItemGateWay hubColorDicItemGateWay;
	@Autowired
	HubColorDicGateWay hubColorDicGateWay;
	@Autowired
	DicRefreshGateWay dicRefreshGateWay;

	private static String[] pendingColorValueTemplate = null;

	static {
		pendingColorValueTemplate = TaskImportTemplate2.getColorValueTemplate();
	}

	public String handMessage(Task task) throws Exception {

		//从ftp下载文件
		JSONObject json = JSONObject.parseObject(task.getData());
		String filePath = json.get("taskFtpFilePath").toString();
		String createUser = json.get("createUser").toString();
		task.setData(filePath);

		InputStream in = taskService.downFileFromFtp(task);

		//excel转对象
		List<HubColorImportDTO> listHubProductImport = null;
		String fileFormat = task.getData().split("\\.")[1];
		if ("xls".equals(fileFormat)) {
			listHubProductImport = handlePendingColorXls(in, task, "color");
		} else if ("xlsx".equals(fileFormat)) {
			listHubProductImport = handlePendingColorXlsx(in, task, "color");
		}

		//校验数据并把校验结果写入excel
		return checkAndsaveHubPendingProduct(task.getTaskNo(), listHubProductImport, createUser);
	}

	//开始校验数据
	public String checkAndsaveHubPendingProduct(String taskNo, List<HubColorImportDTO> listHubProductImport, String createUser)
			throws Exception {

		if (listHubProductImport == null) {
			return null;
		}

		//记录单条数据的校验结果
		//Map<String, String> map = new HashMap<String, String>() ;

		//记录所有数据的校验结果集
		List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();

		for (HubColorImportDTO productImport : listHubProductImport) {
			if (productImport == null) {
				continue;
			}
			Map<String, String> map = new HashMap<String, String>();
			map.put("taskNo", taskNo);
			//对数据的添加，或修改 并 保存数据到数据库
			Map<String, String> map1 = filterColor(productImport, createUser, map);
			listMap.add(map1);
		}

		// 处理的结果以excel文件上传ftp，并更新任务表的任务状态和结果文件在ftp的路径
		return taskService.convertExcelColor(listMap,taskNo);

	}

	private Map<String, String> filterColor(HubColorImportDTO productImport, String createUser, Map<String, String> map) throws ParseException {
		HubColorDicItemDto hubColorDicItemDto = new HubColorDicItemDto();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if (productImport.getColorDicItemId()!=null && productImport.getColorDicId()!=null){
			HubSupplierColorDicRequestDto hubSupplierColorDicRequestDto=new HubSupplierColorDicRequestDto();

			HubColorDicItemDto hubColorDicItemDto1 = hubColorDicItemGateWay.selectByPrimaryKey(Long.parseLong(productImport.getColorDicItemId()));
			hubColorDicItemDto.setColorDicItemId(Long.parseLong(productImport.getColorDicItemId()));
			//map.put("colorDicItemId",productImport.getColorDicItemId());
			//  if (productImport.getColorItemName()==null)return null;
			  if (productImport.getColorItemName()!=null){
				  hubColorDicItemDto.setColorItemName(productImport.getColorItemName());
				  map.put("colorItemName",productImport.getColorItemName());

			  }else {
				  hubColorDicItemDto.setColorItemName("");
			  }


			    	//获取字典的全部颜色
			Map<String, Long> stringLongMap = queryHubColorDicCriteriaColor(productImport);
			for (Map.Entry<String, Long> entry : stringLongMap.entrySet()) {
				  String key = entry.getKey().toString();
				  //Long value = entry.getValue();
				  if (key.equals(productImport.getColorDicId())){
					  Long value = entry.getValue();
					  if (value==null)continue;
					  hubColorDicItemDto.setColorDicId(value);
					  map.put("hubcolor",value.toString());
				  }
			}
			       hubColorDicItemDto.setUpdateTime(new Date());
			int i = hubColorDicItemGateWay.updateByPrimaryKeySelective(hubColorDicItemDto);
			//int i= hubColorDicItemGateWay.updateByPrimaryKeySelective()
			if (i==1){
				map.put("task","校验成功");
			}else {
				map.put("task","校验失败");
			}


			hubSupplierColorDicRequestDto.setColorDicItemId(hubColorDicItemDto.getColorDicItemId());
			hubSupplierColorDicRequestDto.setColorDicId(hubColorDicItemDto.getColorDicId());
			hubSupplierColorDicRequestDto.setSupplierColor(productImport.getColorItemName());
			hubSupplierColorDicRequestDto.setHubColor(productImport.getColorDicId());
			//对比是否要刷新
			if (!hubColorDicItemDto1.getColorDicId().equals(hubSupplierColorDicRequestDto.getColorDicId())){
				dicRefreshGateWay.colorRefresh(hubSupplierColorDicRequestDto);
			}

			//刷新颜色
			return map;
			}
		else {
			HubSupplierColorDicRequestDto hubSupplierColorDicRequestDto=new HubSupplierColorDicRequestDto();

			hubColorDicItemDto.setColorItemName(productImport.getColorItemName());
			map.put("colorItemName",productImport.getColorItemName());
			hubColorDicItemDto.setUpdateTime(new Date());
		//	hubColorDicItemDto.setCreateTime(format.parse(productImport.getCreateTime()));
			hubColorDicItemDto.setColorDicId(Long.parseLong(productImport.getColorDicId()));
			map.put("hubcolor",productImport.getColorDicId());
			Long insert = hubColorDicItemGateWay.insert(hubColorDicItemDto);
			map.put("task","校验成功");

			hubSupplierColorDicRequestDto.setColorDicId(hubColorDicItemDto.getColorDicId());
			hubSupplierColorDicRequestDto.setSupplierColor(productImport.getColorItemName());
			hubSupplierColorDicRequestDto.setHubColor(productImport.getColorDicId());
			//刷新颜色
			dicRefreshGateWay.colorRefresh(hubSupplierColorDicRequestDto);
			return map;
		}
	}

	/**
	 *
	 * 获取HubColorDicDto字典表中的颜色，
	 * ColorName为KEY
	 * ColorDicId 为VALUE
	 * @return  MAP
	 */
	private  Map<String ,Long> queryHubColorDicCriteriaColor(HubColorImportDTO productImport){
		HashMap<String, Long> map = new HashMap<>();
		HubColorDicCriteriaDto criteria=new HubColorDicCriteriaDto();
		if (productImport.getColorDicId()!=null){
			criteria.createCriteria().andColorNameEqualTo(productImport.getColorDicId());
		}
		List<HubColorDicDto> hubColorDicDtos = hubColorDicGateWay.selectByCriteria(criteria);
		for (HubColorDicDto hubColorDicDto : hubColorDicDtos) {
			map.put(hubColorDicDto.getColorName(),hubColorDicDto.getColorDicId());
		}
		return map;
	}


	private boolean insertNohandleReason(HubPendingSpuImportDTO product,String createUser){
		if(StringUtils.isNotBlank(product.getReason1()) || StringUtils.isNotBlank(product.getReason2()) || StringUtils.isNotBlank(product.getReason3()) || StringUtils.isNotBlank(product.getReason4())){
			NohandleReason nohandleReason = new NohandleReason();
			nohandleReason.setSupplierId(product.getSupplierId());
			nohandleReason.setSupplierSpuNo(product.getSupplierSpuNo());
			nohandleReason.setCreateUser(createUser);
			List<String> reasons = Lists.newArrayList();
			add(reasons,product.getReason1());
			add(reasons,product.getReason2());
			add(reasons,product.getReason3());
			add(reasons,product.getReason4());
			nohandleReason.setReasons(reasons);
			log.info("插入无法处理实体===="+JsonUtil.serialize(nohandleReason));
			return nohandleGateWay.insertNohandleReason(nohandleReason);
		}else{
			return false;
		}
	}

	private void add(List<String> reasons, String e){
		if(StringUtils.isNotBlank(e)){
			reasons.add(e);
		}
	}
	
	private void loopHandleSpuImportDto(Map<String, String> map, HubPendingSpuImportDTO productImport,String createUser) throws Exception{

		//excel数据转换为数据库对象
		HubSpuPendingDto conversionSpuPendingDto = convertHubPendingProduct2PendingSpu(productImport,createUser);
		//判断spuPending是否已存在
		List<HubSpuPendingDto> listSpu = dataHandleService.selectPendingSpu(conversionSpuPendingDto);
		HubSpuPendingDto isSpuPendingExist = null;
		if (listSpu != null && listSpu.size() > 0) {
			isSpuPendingExist = listSpu.get(0);
			//存在 看是否是需要重新处理SPU图片的 ，重新处理
			if("1".equals(productImport.getPicRetry())){
				if(null!=isSpuPendingExist.getSupplierSpuId()) {
					try {
						List<String> spAvailablePicList = dataHandleService.getSpAvailablePicList(isSpuPendingExist.getSupplierSpuId());
						if(null!=spAvailablePicList&&spAvailablePicList.size()>0){

							pendingHandleGateWay.retryPictures(spAvailablePicList);
						}
					} catch (Exception e) {
						log.error("spu pending id:" + isSpuPendingExist.getSpuPendingId() +" 图片处理失败. Reason : "+e.getMessage() );
					}
				}
			}
		}

		HubPendingSkuCheckResult checkSkuResult = selectAndcheckSku(productImport,isSpuPendingExist, map);
		taskService.checkPendingSpu(isSpuPendingExist, checkSkuResult, conversionSpuPendingDto, map, checkSkuResult.isPassing());
//		boolean isPassing = Boolean.parseBoolean(map.get("isPassing"));
//		if (isPassing) {
//			taskService.sendToHub(hubPendingSpuDto, map);
//		}
				
	}


	private List<HubColorImportDTO> handlePendingColorXls(InputStream in, Task task, String type)
			throws Exception {
		HSSFSheet xssfSheet = taskService.checkXlsExcel(in, task, type);
		if (xssfSheet == null) {
			return null;
		}
		List<HubColorImportDTO> listHubProduct = new ArrayList<>();
		for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
			HSSFRow xssfRow = xssfSheet.getRow(rowNum);
			HubColorImportDTO hubColorImportDTO = convertCoDTO(xssfRow);
			if (hubColorImportDTO != null) {
				listHubProduct.add(hubColorImportDTO);
			}
		}
		return listHubProduct;
	}
	private List<HubColorImportDTO> handlePendingColorXlsx(InputStream in, Task task, String type)
			throws Exception {

		XSSFSheet xssfSheet = taskService.checkXlsxExcel(in, task, type);
		if (xssfSheet == null) {
			return null;
		}
		List<HubColorImportDTO> listHubProduct = new ArrayList<>();
		for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
			XSSFRow xssfRow = xssfSheet.getRow(rowNum);
			HubColorImportDTO product = convertColorXlsxDTO(xssfRow);
			if (product != null) {
				listHubProduct.add(product);
			}

		}
		return listHubProduct;
	}
	

	private HubPendingSkuCheckResult selectAndcheckSku(HubPendingSpuImportDTO productImport, HubSpuPendingDto isSpuPendingExist, Map<String, String> map)
			throws Exception {
	
		// 如果规格为尺码，则校验spu下所有的尺码
		HubPendingSkuCheckResult checkResult = new HubPendingSkuCheckResult();
		boolean flag = true;
		StringBuffer str = new StringBuffer();
		if (isSpuPendingExist != null) {
			boolean allFliter = true;
			boolean noSku = false;
			//查询出所有状态不是2(已完成)或5(审核中)的sku
			List<HubSkuPendingDto> listSku = dataHandleService.selectHubSkuPendingBySpuPendingId(isSpuPendingExist);
			if (listSku != null && listSku.size() > 0) {
				//判断sku是否都过滤或者都已处理
				for (HubSkuPendingDto hubSkuPendingDto : listSku) {
					HubPendingSkuCheckResult hubPendingSkuCheckResult = loopCheckHubSkuPending(hubSkuPendingDto,productImport,map);
					if(hubSkuPendingDto.getFilterFlag()==1){
						allFliter = false;
					}
					if(!hubPendingSkuCheckResult.isPassing()){
						if(!hubPendingSkuCheckResult.isFilter()){
							flag = false;	
						}
					}
					str.append(hubPendingSkuCheckResult.getMessage()).append(",");
				}
				map.put("allFilter",allFliter+"");
			} else {
				flag = false;
				noSku = true;
				map.put("noSku",noSku+"");
				str.append("无sku信息或者sku都已处理");
			}
		}else{
			flag = false;
		}
		checkResult.setPassing(flag);
		checkResult.setMessage(str.toString());
		return checkResult;
	}

	private HubPendingSkuCheckResult loopCheckHubSkuPending(HubSkuPendingDto hubSkuPendingDto,
			HubPendingSpuImportDTO pendingSpuImportDto,
			Map<String, String> map) throws Exception{
		
		boolean flag = false;
		String result = null;
		HubPendingSkuCheckResult hubPendingSkuCheckResult = new HubPendingSkuCheckResult();
		boolean isMultiSizeType = false;
		if ("尺码".equals(pendingSpuImportDto.getSpecificationType())|| 
				StringUtils.isBlank(pendingSpuImportDto.getSpecificationType())) {
	
			if (hubSkuPendingDto.getHubSkuSize() != null) {
				MatchSizeResult matchSizeResult = taskService.matchSize(pendingSpuImportDto.getHubBrandNo(),pendingSpuImportDto.getHubCategoryNo(),hubSkuPendingDto.getHubSkuSize());
				if(matchSizeResult!=null){
					hubPendingSkuCheckResult.setSizeValue(matchSizeResult.getSizeValue());
					if (matchSizeResult.isPassing()) {
						flag = true;
						hubPendingSkuCheckResult.setSizeId(matchSizeResult.getSizeId());
						hubPendingSkuCheckResult.setSizeType(matchSizeResult.getSizeType());
						result = matchSizeResult.getResult();
					}else if(matchSizeResult.isMultiSizeType()) {//多个匹配  失败 增加备注
						isMultiSizeType = matchSizeResult.isMultiSizeType();
						result = " " + hubSkuPendingDto.getHubSkuSize() + "多个匹配,失败";
					}else  if(matchSizeResult.isFilter()){//有模板没匹配上
//						hubPendingSkuCheckResult.setSizeId(matchSizeResult.getSizeId());
						hubPendingSkuCheckResult.setSizeType("排除");
						hubPendingSkuCheckResult.setFilter(true);
						result =" " + hubSkuPendingDto.getHubSkuSize() + "有模板没匹配上,排除";
					}else{
					   //不做处理
						result =" " + hubSkuPendingDto.getHubSkuSize() + "未找到品牌品类尺码，不做处理";
					}
				}else{
					result =  hubSkuPendingDto.getHubSkuSize() +" 返回结果为空，校验失败";
				}
			} else {
				result = hubSkuPendingDto.getSupplierSkuNo() + "尺码为空";
			}
		}else if("尺寸".equals(pendingSpuImportDto.getSpecificationType())){
			result = "校验通过：" + hubSkuPendingDto.getHubSkuSize();
			flag = true;
			hubPendingSkuCheckResult.setSizeType("尺寸");
			hubPendingSkuCheckResult.setSizeValue(hubSkuPendingDto.getHubSkuSize());
		}else{
			result = "校验失败,规格类型无效:" + pendingSpuImportDto.getSpecificationType();

		}
		
		hubPendingSkuCheckResult.setMessage(result);
		hubPendingSkuCheckResult.setPassing(flag);
		taskService.checkPendingSku(hubPendingSkuCheckResult, hubSkuPendingDto, map, isMultiSizeType);
		return hubPendingSkuCheckResult;
	}

	private HubSpuPendingDto convertHubPendingProduct2PendingSpu(HubPendingSpuImportDTO product,String createUser) {
		HubSpuPendingDto HubPendingSpuDto = new HubSpuPendingDto();
		BeanUtils.copyProperties(product, HubPendingSpuDto);
		if(StringUtils.isNotBlank(product.getSeasonYear())&&StringUtils.isNotBlank(product.getSeasonName())){
			HubPendingSpuDto.setHubSeason(product.getSeasonYear() + "_" + product.getSeasonName());
		}
		
		HubPendingSpuDto.setUpdateUser(createUser);
		return HubPendingSpuDto;
	}

	@SuppressWarnings("unchecked")

	private static HubColorImportDTO convertCoDTO(HSSFRow xssfRow) {
		HubColorImportDTO item=null;
		if (xssfRow != null) {
			try {
				item = new HubColorImportDTO();
				Class c = item.getClass();
				for (int i = 0; i< pendingColorValueTemplate.length; i++) {

					if (xssfRow.getCell(i) != null) {
						xssfRow.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
						String setMethodName = "set" + pendingColorValueTemplate[i].toUpperCase().charAt(0)
								+ pendingColorValueTemplate[i].substring(1);
						Method setMethod = c.getDeclaredMethod(setMethodName, String.class);

						setMethod.invoke(item, xssfRow.getCell(i).toString());
					}
				}
			} catch (Exception ex) {
				ex.getStackTrace();
			}
		}
		return item;
	}

	private static HubColorImportDTO convertColorXlsxDTO(XSSFRow xssfRow) {
		HubColorImportDTO item=null;
		if (xssfRow != null) {
			try {
				item = new HubColorImportDTO();
				Class c = item.getClass();
				for (int i = 0; i < pendingColorValueTemplate.length; i++) {
					if (xssfRow.getCell(i) != null) {
						xssfRow.getCell(i).setCellType(Cell.CELL_TYPE_STRING);
						String setMethodName = "set" + pendingColorValueTemplate[i].toUpperCase().charAt(0)
								+ pendingColorValueTemplate[i].substring(1);
						Method setMethod = c.getDeclaredMethod(setMethodName, String.class);
						setMethod.invoke(item, xssfRow.getCell(i).toString());
					}
				}
			} catch (Exception ex) {
				ex.getStackTrace();
			}
		}
		return item;
	}
}
