package com.shangpin.api.airshop.product.c;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shangpin.api.airshop.config.ApiServiceUrlConfig;
import com.shangpin.api.airshop.dto.NotOnShelfDTO;
import com.shangpin.api.airshop.dto.UserInfo;
import com.shangpin.api.airshop.dto.base.ResponseContentList;
import com.shangpin.api.airshop.dto.base.ResponseContentOne;
import com.shangpin.api.airshop.dto.request.ProductListRequest;
import com.shangpin.api.airshop.product.d.PageDTO;
import com.shangpin.api.airshop.product.d.ProductDTO;
import com.shangpin.api.airshop.product.d.ResponseDTO;
import com.shangpin.api.airshop.product.d.SkuProductDTO;
import com.shangpin.api.airshop.product.d.SpuProductDTO;
import com.shangpin.api.airshop.product.d.hub.HubPageDTO;
import com.shangpin.api.airshop.product.d.hub.HubProductDTO;
import com.shangpin.api.airshop.product.m.ProductManager;
import com.shangpin.api.airshop.product.o.Page;
import com.shangpin.api.airshop.product.o.ProductResponse;
import com.shangpin.api.airshop.product.o.QueryResponse;
import com.shangpin.api.airshop.product.v.ProductVO;
import com.shangpin.api.airshop.product.v.QueryResult;
import com.shangpin.api.airshop.product.v.QueryVO;
import com.shangpin.api.airshop.product.v.Sku;
import com.shangpin.api.airshop.service.base.BaseService;
import com.shangpin.api.airshop.util.Constants;
import com.shangpin.api.airshop.util.ExportExcelUtils;
import com.shangpin.api.airshop.util.HttpClientUtil;
import com.shangpin.common.utils.FastJsonUtil;

/**
 * <p>
 * Title:ProductNewController
 * </p>
 * <p>
 * Description: 新增商品控制器
 * </p>
 * <p>
 * Company: shangpin
 * </p>
 * 
 * @author : yanxiaobin
 * @date :2016年4月22日 下午4:20:56
 */
@RestController
@Slf4j
@RequestMapping("/product")
@SessionAttributes(Constants.SESSION_USER)
public class ProductController {
	@Autowired
	BaseService baseService;
	@Autowired
	private ProductManager pm;

	/**
	 * 获取供应商未上架的商品以及未上架原因
	 * 
	 * @param spu
	 *            请求数据封装对象spu
	 * @return ProductBaseResponse 请求相应对象
	 */
	@RequestMapping(value = "/notOnShelfList", method = RequestMethod.POST)
	public String getNotOnShelfList(
			@RequestParam(value = "errorTypeCode") Integer errorTypeCode,
			@RequestParam(value = "errorDescCode", required = false) Integer errorDescCode,
			@RequestParam(value = "pageIndex") Integer pageIndex,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo) {

		JSONObject paramMap = new JSONObject();
		if (errorTypeCode != 0) {
			paramMap.put("errorType", errorTypeCode);
		}
		if (errorDescCode != 0) {
			paramMap.put("errorReason", errorDescCode);
		}
		paramMap.put("pageIndex", pageIndex);
		paramMap.put("pageSize", pageSize);
		paramMap.put("supplierId", userInfo.getSopUserNo());

		String requestJson = FastJsonUtil.serialize2String(paramMap);
		String returnJson = HttpClientUtil.doPostForJson(
				ApiServiceUrlConfig.getNotOnShelfList(), requestJson);

		if (StringUtils.isEmpty(returnJson)) {
			return null;
		}

		JSONObject jsonObject = JSON.parseObject(returnJson);
		String code = String.valueOf(jsonObject.get("code"));
		if (!"0".equals(code)) {
			return FastJsonUtil.serialize2String(ResponseContentOne
					.errorResp("Not Data"));
		}

		try {
			return FastJsonUtil.serialize2String(jsonObject.get("content"));
		} catch (Exception e) {

		}
		return FastJsonUtil.serialize2String(ResponseContentOne
				.errorResp("Not Data"));
	}

	/**
	 * 获取供应商未上架的商品以及未上架原因
	 * 
	 * @param spu
	 *            请求数据封装对象spu
	 * @return ProductBaseResponse 请求相应对象
	 */
	@RequestMapping(value = "/exportNotOnShelfList")
	public String exportNotOnShelfList(
			@RequestParam(value = "errorTypeCode") Integer errorTypeCode,
			@RequestParam(value = "errorDescCode", required = false) Integer errorDescCode,
			@RequestParam(value = "pageIndex") Integer pageIndex,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			@ModelAttribute(Constants.SESSION_USER) UserInfo userInfo,
			HttpServletResponse response) {
		try {
			JSONObject paramMap = new JSONObject();
			paramMap.put("errorTypeCode", errorTypeCode);
			paramMap.put("errorDescCode", errorDescCode);
			paramMap.put("pageIndex", pageIndex);
			paramMap.put("pageSize", pageSize);
			paramMap.put("supplierId", userInfo.getSopUserNo());

			String requestJson = FastJsonUtil.serialize2String(paramMap);

			String json = HttpClientUtil.doPostForJson(
					ApiServiceUrlConfig.getNotOnShelfList(), requestJson);
			if (StringUtils.isEmpty(json)) {
				return null;
			}

			JSONObject jsonObject = JSON.parseObject(json);
			String code = String.valueOf(jsonObject.get("code"));
			if (!"0".equals(code)) {
				return FastJsonUtil.serialize2String(ResponseContentOne
						.errorResp("Not Data"));
			}
			String content = jsonObject.get("content").toString();
			Page li = FastJsonUtil.deserializeString2Obj(content, Page.class);

			Map<Integer, String> errorCodeDescmap = new HashMap<Integer, String>();
			errorCodeDescmap.put(11, "No Color Code");
			errorCodeDescmap.put(12, "No Material Code");
			errorCodeDescmap.put(13, "Wrong Code Rule");
			errorCodeDescmap.put(21, "Wrong Material Composition");
			errorCodeDescmap.put(22, "Wrong Material Percentage");
			errorCodeDescmap.put(23, "No Material Info");
			errorCodeDescmap.put(31, "Child-Adult Inversion");
			errorCodeDescmap.put(32, "Man-Woman Inversion");
			errorCodeDescmap.put(41, "No Origin Info");
			errorCodeDescmap.put(51, "Wrong Mapping of Code");
			errorCodeDescmap.put(52, "No Photo");
			errorCodeDescmap.put(61, "Different SPU to Same Item Code");
			errorCodeDescmap.put(71, "No Market Price 72,No Supplier Price");
			errorCodeDescmap.put(81, "Too Large / Small");
			errorCodeDescmap.put(82, "Wrong Size ");
			errorCodeDescmap.put(91, "Unprofitable Brand");

			List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
			for (NotOnShelfDTO dto : li.getList()) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("spuName", dto.getSpuName());
				map.put("brandName", dto.getBrandName());
				map.put("supplierSpuNo", dto.getSupplierSpuNo());
				map.put("spuModel", dto.getSpuModel());
				map.put("errorDesc", errorCodeDescmap.get(Integer.parseInt(dto
						.getErrorReason())));
				result.add(map);
			}
			response.setContentType("application/x-download");// 设置为下载application/x-download
			String fileName = "Stock Check (2st time)";
			fileName += "All";
			response.addHeader("Content-Disposition", "attachment;filename="
					+ new String(fileName.getBytes("gb2312"), "ISO8859-1")
					+ ".xls");
			OutputStream out = response.getOutputStream();
			if (result != null && result.size() > 0) {
				String[] headers = { "Item Name", "Brand", "Supplier Spu",
						"Item Code", "Error Descption" };
				String[] columns = { "spuName", "brandName", "supplierSpuNo",
						"spuModel", "errorDesc" };
				ExportExcelUtils.exportExcel(fileName, headers, columns,
						result, out, "");
				out.close();
				return null;
			} else {
				List<HashMap<String, Object>> result2 = new ArrayList<HashMap<String, Object>>();
				HashMap<String, Object> result1 = new HashMap<String, Object>();
				result1.put("oops", "Export failed. Please try again later.");
				result2.add(result1);
				String[] headers = { "Oops" };
				String[] columns = { "oops" };
				ExportExcelUtils.exportExcel(fileName, headers, columns,
						result2, out, "");
				out.close();
				return null;
			}
		} catch (Exception e) {

		}
		return FastJsonUtil.serialize2String(ResponseContentOne
				.errorResp("Not Data"));
	}
	/**
	 * 该请求处理器用于暂存供应商提供的商品信息，但并不提交处理该商品信息
	 * 
	 * @param spu
	 *            请求数据封装对象spu
	 * @return ProductBaseResponse 请求相应对象
	 */
	@RequestMapping(value = "/hubAddOrUpdate", method = RequestMethod.POST)
	public ProductResponse addHub(@RequestBody ProductVO spu, HttpSession session) {
		log.info("===image=>>>" + spu.toString());
		ProductResponse response = new ProductResponse();
		try {
			UserInfo user = (UserInfo) session
					.getAttribute(Constants.SESSION_USER);
			SpuProductDTO productDTO = vo2dto(spu);
			//addOrUpdate
			String type = spu.getType();
			String addOrupdate = spu.getAddOrupdate();
			if (type == null || addOrupdate == null) {
				response.setCode(2);
				response.setMsg("Please set type and addOrupdate");
			}
			if (type != null && !"1".equals(type) && !"2".equals(type)) {
				response.setCode(2);
				response.setMsg("type is only to be 1 or 2 !");
				return response;
			} else if (type != null && "1".equals(type)) {
				productDTO.setStatus("2");
			} else if (type != null && "2".equals(type)) {
				productDTO.setStatus("3");
			}
			ResponseDTO rd = pm.hubSaveOrSubmmit(user.getSopUserNo(), productDTO,
					addOrupdate.trim());
			if (rd != null && rd.getResponseCode() == 1) {
				response.setCode(0);
				response.setMsg("OK");
			} else {
				response.setCode(2);
			}
		} catch (Throwable e) {
			response.setCode(2);
			response.setMsg("Please try it later !");
			e.printStackTrace();
			log.info(
					"When save or submmit product occur exception (uri=/product/add)=============>>:",
					e);
		}
		return response;
	}
	/**
	 * 该请求处理器用于暂存供应商提供的商品信息，但并不提交处理该商品信息
	 * 
	 * @param spu
	 *            请求数据封装对象spu
	 * @return ProductBaseResponse 请求相应对象
	 */
	@RequestMapping(value = "/addOrUpdate", method = RequestMethod.POST)
	public ProductResponse add(@RequestBody ProductVO spu, HttpSession session) {
		log.info("===image=>>>" + spu.toString());
		ProductResponse response = new ProductResponse();
		try {
			UserInfo user = (UserInfo) session
					.getAttribute(Constants.SESSION_USER);
			SpuProductDTO productDTO = vo2dto(spu);
			String type = spu.getType();
			String addOrupdate = spu.getAddOrupdate();
			if (type == null || addOrupdate == null) {
				response.setCode(2);
				response.setMsg("Please set type and addOrupdate");
			}
			if (type != null && !"1".equals(type) && !"2".equals(type)) {
				response.setCode(2);
				response.setMsg("type is only to be 1 or 2 !");
				return response;
			} else if (type != null && "1".equals(type)) {
				productDTO.setStatus("2");
			} else if (type != null && "2".equals(type)) {
				productDTO.setStatus("3");
			}
			ResponseDTO rd = pm.saveOrSubmmit(user.getSopUserNo(), productDTO,
					addOrupdate.trim());
			if (rd != null && rd.getResponseCode() == 1) {
				response.setCode(0);
				response.setMsg("OK");
			} else {
				response.setCode(2);
			}
		} catch (Throwable e) {
			response.setCode(2);
			response.setMsg("Please try it later !");
			e.printStackTrace();
			log.info(
					"When save or submmit product occur exception (uri=/product/add)=============>>:",
					e);
		}
		return response;
	}
	private HubProductDTO qv2HubDto(QueryVO qv) {
		HubProductDTO pd = new HubProductDTO();

		String from = qv.getFrom();
		String to = qv.getTo();
		String memo = ",";

		if(!StringUtils.isEmpty(qv.getItemName())){
			pd.setProductName(qv.getItemName());	
		}
		
		pd.setProductCode(qv.getItemCode());
		pd.setSkuId(qv.getSupplierSku());
		pd.setBarcode(qv.getBarCode());
		pd.setSpSkuId(qv.getShangpinSKU());
		pd.setBrandName(qv.getBrand());

		if (!StringUtils.isEmpty(from)) {
			memo = from + memo;
		}
		if (!StringUtils.isEmpty(to)) {
			memo = memo + to;
		}

		pd.setMemo(memo);

		pd.setStatus(String.valueOf(qv.getEditStatus()));
		pd.setSpStatus(String.valueOf(qv.getShelveStatus()));
		// 颜色和尺码
		pd.setColor(qv.getColor());
		pd.setSize(qv.getSize());
		pd.setSizeClass(qv.getSizeClass());
		pd.setCategoryName(qv.getCategory());
		return pd;
	}
	private ProductDTO qv2Dto(QueryVO qv) {
		ProductDTO pd = new ProductDTO();

		String from = qv.getFrom();
		String to = qv.getTo();
		String memo = ",";

		pd.setProductName(qv.getItemName());
		pd.setProductCode(qv.getItemCode());
		pd.setSkuId(qv.getSupplierSku());
		pd.setBarcode(qv.getBarCode());
		pd.setSpSkuId(qv.getShangpinSKU());
		pd.setBrandName(qv.getBrand());

		if (!StringUtils.isEmpty(from)) {
			memo = from + memo;
		}
		if (!StringUtils.isEmpty(to)) {
			memo = memo + to;
		}

		pd.setMemo(memo);

		pd.setStatus(String.valueOf(qv.getEditStatus()));
		pd.setSpStatus(String.valueOf(qv.getShelveStatus()));
		// 颜色和尺码
		pd.setColor(qv.getColor());
		pd.setSize(qv.getSize());
		pd.setSizeClass(qv.getSizeClass());
		pd.setCategoryName(qv.getCategory());
		return pd;
	}

	/**
	 * 转换操作
	 * 
	 * @param spu
	 */
	private SpuProductDTO vo2dto(ProductVO spu) {
		SpuProductDTO dto = new SpuProductDTO();
		dto.setBrandName(spu.getBrand());
		dto.setCategoryGender(spu.getGender());
		dto.setCategoryName(spu.getCategory());
		dto.setColor(spu.getColour());
		dto.setMaterial(spu.getMaterial());
		dto.setProductCode(spu.getProductCode());
		dto.setProductOrigin(spu.getMadeIn());
		dto.setSeasonName(spu.getYear() + "-" + spu.getSeason());
		dto.setSaleCurrency(spu.getCurrency());
		dto.setProductDescription(spu.getDescription());
		dto.setMarketPrice(spu.getMarketPrice());
		List<String> images = spu.getImages();
		if (images != null) {
			StringBuffer sb = new StringBuffer();
			for (String image : images) {
				sb.append(image).append("|");
			}
			dto.setSpuPicture(sb.toString());
		}
		dto.setSupplierPrice(spu.getSupplyPrice());
		Set<Sku> skus = spu.getSkus();
		if (skus != null) {
			List<SkuProductDTO> list = new ArrayList<>();
			for (Sku sku : skus) {
				SkuProductDTO skuDto = new SkuProductDTO();
				skuDto.setSize(sku.getSizeClass() + "-" + sku.getSize());
				String skuId = sku.getSkuId();
				skuId = skuId == null ? null : skuId.trim();
				skuDto.setSkuId(skuId);
				skuDto.setStatus(sku.getStatus());
				String barcode = sku.getBarcode();
				barcode = barcode == null ? null : barcode.trim();
				skuDto.setBarcode(barcode);
				list.add(skuDto);
			}
			dto.setList(list);
		}
		dto.setProductName(spu.getSpuName());
		return dto;
	}

	
//	@RequestMapping(value = "/query")
//	public String hubQuery(QueryVO qv, HttpSession hs) {
//		String pageIndex = qv.getPageIndex().toString();
//		String pageSize = qv.getPageSize().toString();
//		if (StringUtils.isEmpty(pageIndex) || StringUtils.isEmpty(pageSize)) {
//			return FastJsonUtil.serialize2String(ResponseContentOne
//					.errorParam());
//		}
//
//		QueryResult qr = new QueryResult();
//
//		List<ProductListRequest> page = new ArrayList<ProductListRequest>();
//		int total = 0;
//		try {
//			UserInfo user = (UserInfo) hs.getAttribute(Constants.SESSION_USER);
//
//			HubPageDTO query = pm.queryHub(qv2HubDto(qv), user.getSopUserNo(),
//					Integer.parseInt(pageSize), Integer.parseInt(pageIndex));
//			total = query.getCount();
//			List<HubProductDTO> list = query.getList();
//			if (null != list && list.size() > 0) {
//				for (HubProductDTO productdto : list) {
//					ProductListRequest productListRequest = new ProductListRequest();
//					productListRequest.setItemName(productdto.getProductName());
//					productListRequest.setItemCode(productdto.getProductCode());
//					productListRequest.setShangpinSKU(productdto.getSpSkuId());
//					productListRequest.setBrand(productdto.getBrandName());
//					productListRequest.setEditStatus(productdto.getStatus());
//					productListRequest
//							.setShelveStatus(productdto.getSpStatus());
//
//					productListRequest.setColor(productdto.getColor());
//					productListRequest.setSize(productdto.getSize());
//					productListRequest.setSpuId(productdto.getSupplierSpuId());
//					productListRequest.setCategoryName(productdto
//							.getCategoryName());
//					productListRequest.setSupplierSKU(productdto.getSkuId());
//					if (null != productdto.getMarketPrice()) {
//						productListRequest.setPrice(productdto.getMarketPrice()
//								.toString());
//					} else {
//						productListRequest.setPrice("0");
//					}
//					productListRequest.setImage(productdto.getSpuPicture());
//
//					// 时间转换成年月日 时分
//					SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//					if (null != productdto.getLastTime()) {
//						productListRequest.setUpdateTime(df.format(productdto
//								.getLastTime()));
//					} else {
//						productListRequest.setUpdateTime("");
//					}
//					page.add(productListRequest);
//				}
//			}
//		} catch (Exception e) {
//			qr.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
//			e.printStackTrace();
//			log.error("query:", e);
//		}
//		ResponseContentList<ProductListRequest> result = ResponseContentList
//				.successResp(page);
//		result.setTotal(total);
//		return FastJsonUtil.serialize2StringEmpty(result);
//	}
	/**
	 * 根据指定条件动态查询sku信息
	 * 
	 * @param qv
	 * @return
	 */
	@RequestMapping(value = "/queryHub")
	public String queryHub(QueryVO qv, HttpSession hs) {

		String pageIndex = qv.getPageIndex().toString();
		String pageSize = qv.getPageSize().toString();
		if (StringUtils.isEmpty(pageIndex) || StringUtils.isEmpty(pageSize)) {
			return FastJsonUtil.serialize2String(ResponseContentOne
					.errorParam());
		}

		QueryResult qr = new QueryResult();

		List<ProductListRequest> page = new ArrayList<ProductListRequest>();
		int total = 0;
		try {
			UserInfo user = (UserInfo) hs.getAttribute(Constants.SESSION_USER);

			HubPageDTO query = pm.queryHub(qv2HubDto(qv), user.getSopUserNo(),
					Integer.parseInt(pageSize), Integer.parseInt(pageIndex));
			total = query.getCount();
			List<HubProductDTO> list = query.getList();
			if (null != list && list.size() > 0) {
				for (HubProductDTO productdto : list) {
					ProductListRequest productListRequest = new ProductListRequest();
					productListRequest.setItemName(productdto.getProductName());
					productListRequest.setItemCode(productdto.getProductCode());
					productListRequest.setShangpinSKU(productdto.getSpSkuId());
					productListRequest.setBrand(productdto.getBrandName());
					productListRequest.setEditStatus(productdto.getStatus());
					productListRequest
							.setShelveStatus(productdto.getSpStatus());

					productListRequest.setColor(productdto.getColor());
					productListRequest.setSize(productdto.getSizeClass()+"-"+productdto.getSize());
					productListRequest.setSpuId(String.valueOf(productdto.getSupplierSpuId()));
					productListRequest.setCategoryName(productdto
							.getCategoryName());
					productListRequest.setSupplierSKU(productdto.getSkuId());
					if (null != productdto.getMarketPrice()) {
						productListRequest.setPrice(productdto.getMarketPrice()
								.toString());
					} else {
						productListRequest.setPrice("0");
					}
					productListRequest.setImage(productdto.getSpuPicture());

					// 时间转换成年月日 时分
					SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
					if (null != productdto.getLastTime()) {
						productListRequest.setUpdateTime(df.format(productdto
								.getLastTime()));
					} else {
						productListRequest.setUpdateTime("");
					}
					page.add(productListRequest);
				}
			}
		} catch (Exception e) {
			qr.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
			log.error("query:", e);
		}
		ResponseContentList<ProductListRequest> result = ResponseContentList
				.successResp(page);
		result.setTotal(total);
		return FastJsonUtil.serialize2StringEmpty(result);
	}
	/**
	 * 根据指定条件动态查询sku信息
	 * 
	 * @param qv
	 * @return
	 */
	@RequestMapping(value = "/query")
	public String query(QueryVO qv, HttpSession hs) {

		String pageIndex = qv.getPageIndex().toString();
		String pageSize = qv.getPageSize().toString();
		if (StringUtils.isEmpty(pageIndex) || StringUtils.isEmpty(pageSize)) {
			return FastJsonUtil.serialize2String(ResponseContentOne
					.errorParam());
		}

		QueryResult qr = new QueryResult();

		List<ProductListRequest> page = new ArrayList<ProductListRequest>();
		int total = 0;
		try {
			UserInfo user = (UserInfo) hs.getAttribute(Constants.SESSION_USER);

			PageDTO query = pm.query(qv2Dto(qv), user.getSopUserNo(),
					Integer.parseInt(pageSize), Integer.parseInt(pageIndex));
			total = query.getCount();
			List<ProductDTO> list = query.getList();
			if (null != list && list.size() > 0) {
				for (ProductDTO productdto : list) {
					ProductListRequest productListRequest = new ProductListRequest();
					productListRequest.setItemName(productdto.getProductName());
					productListRequest.setItemCode(productdto.getProductCode());
					productListRequest.setShangpinSKU(productdto.getSpSkuId());
					productListRequest.setBrand(productdto.getBrandName());
					productListRequest.setEditStatus(productdto.getStatus());
					productListRequest
							.setShelveStatus(productdto.getSpStatus());

					productListRequest.setColor(productdto.getColor());
					productListRequest.setSize(productdto.getSize());
					productListRequest.setSpuId(productdto.getSpuId());
					productListRequest.setCategoryName(productdto
							.getCategoryName());
					productListRequest.setSupplierSKU(productdto.getSkuId());
					if (null != productdto.getMarketPrice()) {
						productListRequest.setPrice(productdto.getMarketPrice()
								.toString());
					} else {
						productListRequest.setPrice("0");
					}
					productListRequest.setImage(productdto.getSpuPicture());

					// 时间转换成年月日 时分
					SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
					if (null != productdto.getLastTime()) {
						productListRequest.setUpdateTime(df.format(productdto
								.getLastTime()));
					} else {
						productListRequest.setUpdateTime("");
					}
					page.add(productListRequest);
				}
			}
		} catch (Exception e) {
			qr.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
			log.error("query:", e);
		}
		ResponseContentList<ProductListRequest> result = ResponseContentList
				.successResp(page);
		result.setTotal(total);
		return FastJsonUtil.serialize2StringEmpty(result);
	}

	/**
	 * 编辑查询，根据供应商sku编号查询
	 */
	@RequestMapping(value = "/edit-query-hub/{itemCode}")
	public QueryResponse editQueryHub(
			@PathVariable(value = "itemCode") String itemCode,
			HttpSession session) {
		
		QueryResponse response = new QueryResponse();
		UserInfo user = (UserInfo) session.getAttribute(Constants.SESSION_USER);
		String sopUserNo = user.getSopUserNo();
		try {
			itemCode = URLEncoder.encode(itemCode, "UTF-8");
			ProductVO vo  = pm.editQueryHub(sopUserNo, itemCode);
			response.setCode(0);
			response.setMsg("OK");

			response.setContent(vo);
		} catch (Exception e) {
			response.setCode(2);
			response.setMsg("Please try it later thanks !");
			e.printStackTrace();
			log.error(
					"When call the url: /product/searchProductByMulCondition occur exception !",
					e);
		}

		return response;
	}
	
	
	/**
	 * 编辑查询，根据供应商sku编号查询
	 */
	@RequestMapping(value = "/edit-query/{itemCode}")
	public QueryResponse editQuery(
			@PathVariable(value = "itemCode") String itemCode,
			HttpSession session) {
		QueryResponse response = new QueryResponse();
		UserInfo user = (UserInfo) session.getAttribute(Constants.SESSION_USER);
		String sopUserNo = user.getSopUserNo();
		SpuProductDTO productDTO = null;
		try {
			itemCode = URLEncoder.encode(itemCode, "UTF-8");
			productDTO = pm.editQuery(sopUserNo, itemCode);
			response.setCode(0);
			response.setMsg("OK");
			if (productDTO != null) {
				ProductVO vo = dto2Vo(productDTO);

				response.setContent(vo);
			}
		} catch (Exception e) {
			response.setCode(2);
			response.setMsg("Please try it later thanks !");
			e.printStackTrace();
			log.error(
					"When call the url: /product/searchProductByMulCondition occur exception !",
					e);
		}

		return response;
	}

	private ProductVO dto2Vo(SpuProductDTO dto) {
		ProductVO vo = new ProductVO();
		vo.setBrand(dto.getBrandName());
		vo.setCategory(dto.getCategoryName());
		vo.setColour(dto.getColor());
		vo.setCurrency(dto.getSaleCurrency());
		vo.setDescription(dto.getProductDescription());
		vo.setGender(dto.getCategoryGender());
		String pics = dto.getSpuPicture();
		if (!org.apache.commons.lang3.StringUtils.isBlank(pics)) {
			String[] picArr = pics.split("\\|");
			List<String> images = new ArrayList<>();
			for (int i = 0; i < picArr.length; i++) {
				images.add(picArr[i]);
			}
			vo.setImages(images);
			log.info("==========>>" + images.toString());
		}

		vo.setMadeIn(dto.getProductOrigin());
		vo.setMarketPrice(dto.getMarketPrice());
		vo.setMaterial(dto.getMaterial());
		vo.setSpuId(dto.getProductCode());
		vo.setSpuName(dto.getProductName());
		vo.setSupplyPrice(dto.getSupplierPrice());
		String seasonName = dto.getSeasonName();
		if (!org.apache.commons.lang3.StringUtils.isBlank(seasonName)) {
			String[] picArr = seasonName.split("-");
			if (picArr != null && picArr.length == 2) {
				vo.setYear(picArr[0]);
				vo.setSeason(picArr[1]);
			}
		}
		List<SkuProductDTO> list = dto.getList();
		if (list != null) {
			Set<Sku> skus = new HashSet<>();
			for (SkuProductDTO d : list) {
				String size = d.getSize();
				if (!org.apache.commons.lang3.StringUtils.isBlank(size)) {
					Sku sku = new Sku();
					String[] sizes = size.split("-");
					if (sizes.length == 2) {
						sku.setSizeClass(sizes[0]);
						sku.setSize(sizes[1]);
					}
					sku.setSkuId(d.getSkuId());
					sku.setStatus(d.getStatus());
					sku.setShangpinSKU(d.getSpSkuId());
					sku.setBarcode(d.getBarcode());
					skus.add(sku);
				}
			}
			vo.setSkus(skus);
		}
		return vo;
	}
}
