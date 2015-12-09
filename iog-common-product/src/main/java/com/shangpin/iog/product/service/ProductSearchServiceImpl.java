package com.shangpin.iog.product.service;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.framework.page.Page;
import com.shangpin.iog.common.utils.InVoke;
import com.shangpin.iog.dto.*;
import com.shangpin.iog.mongodao.PictureDAO;
import com.shangpin.iog.mongodomain.ProductPicture;
import com.shangpin.iog.product.dao.*;
import com.shangpin.iog.service.ProductSearchService;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;

/**
 * Created by loyalty on 15/5/20.
 */
@Service
public class ProductSearchServiceImpl implements ProductSearchService {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	SkuMapper skuDAO;

	@Autowired
	SpuMapper spuDAO;

	@Autowired
	ProductPictureMapper picDAO;

	@Autowired
	PictureDAO pictureDAO;

	@Autowired
	ProductsMapper productDAO;

	@Autowired
	BrandSpMapper brandSpDAO;

	@Autowired
	ColorContrastMapper colorContrastDAO;

	@Autowired
	MaterialContrastMapper materialContrastDAO;

	@Autowired
	SupplierMapper supplierDAO;

	private static Map<String, String> spBrandMap = new HashMap<>();
	private static Map<String, String> colorContrastMap = new HashMap<>();

	private static Map<String, String> materialContrastMap = new HashMap<>();

	String splitSign = ",";

	// key 均为小写 以便匹配
	private static Map<String, String> cityMap = new HashMap<String, String>() {
		{
			put("italia", "意大利");
			put("stati Uniti d", "美国");
			put("gran Bretagna", "英国");
			put("canada", "加拿大");
			put("brazil", "巴西");
			put("argentina", "阿根廷");
			put("mexico", "墨西哥");
			put("germany", "德国");
			put("francia", "法国");
			put("russia", "俄罗斯");
			put("giappone", "日本");
			put("australia", "澳大利亚");
			put("corea", "韩国");
			put("cina", "中国");
			put("finland", "芬兰");
			put("svizzera", "瑞士");
			put("sweden", "瑞典");
			put("singapore", "新加坡");
			put("tailandia", "泰国");
			put("new zealand", "新西兰");
			put("ireland", "爱尔兰");
			put("arabia Saudita", "沙特阿拉伯");
			put("armenia", "亚美尼亚");
			put("belgio", "比利时");
			put("bosnia-Erzegovina", "波斯尼亚-黑塞哥维那");
			put("bulgaria", "保加利亚");
			put("cambogia", "柬埔寨");
			put("croazia", "克罗地亚");
			put("filippine", "菲律宾");
			put("georgia", "格鲁吉亚");
			put("hongkong", "香港");
			put("india", "印度");
			put("indonesia", "印度尼西亚");
			put("kenya", "肯尼亚");
			put("lituania", "立陶宛");
			put("madagascar", "马达加斯加");
			put("marocco", "摩洛哥");
			put("mauritius", "毛里求斯");
			put("moldavia", "摩尔多瓦共和国");
			put("myanmar(Unione)", "缅甸");
			put("polonia", "波兰");
			put("portogallo", "葡萄牙");
			put("romania", "罗马尼亚");
			put("serbia", "塞尔维亚");
			put("slovacca", "斯洛伐克");
			put("spagna", "西班牙");
			put("sri Lanka", "斯里兰卡");
			put("tunisia", "突尼斯");
			put("turchia", "土耳其");
			put("ucraina", "乌克兰");
			put("ungheria", "匈牙利");
			put("vietnam", "越南");
		}
	};

	@Override
	public Page<ProductDTO> findProductPageBySupplierAndTime(String supplier,
			Date startDate, Date endDate, Integer pageIndex, Integer pageSize,
			String flag) throws ServiceException {
		List<ProductDTO> productList = null;
		Page<ProductDTO> page = null;
		try {
			if (null != pageIndex && null != pageSize) {
				page = new Page<>(pageIndex, pageSize);
				if (flag .equals("same")) {
					if(StringUtils.isNotBlank(supplier)){
						productList = productDAO.findListBySupplierAndLastDate(
								supplier, startDate, endDate, new RowBounds(
										pageIndex, pageSize));
					}else{
						productList = productDAO.findListOfAllSupplier(startDate, endDate, new RowBounds(
										pageIndex, pageSize));
					}
					
					
					
				} else {
					productList = productDAO.findDiffListBySupplierAndLastDate(
							supplier, startDate, endDate, new RowBounds(
									pageIndex, pageSize));
				}

			} else {
				if (flag .equals("same")) {
					productList = productDAO.findListBySupplierAndLastDate(
							supplier, startDate, endDate);					
										
				} else {
					productList = productDAO.findDiffListBySupplierAndLastDate(
							supplier, startDate, endDate);
				}
				page = new Page<>(1, productList.size());
			}

			String supplierId = "", skuId = "";
			for (ProductDTO dto : productList) {

				if (null != dto.getSupplierId() && null != dto.getSkuId()) {
					supplierId = dto.getSupplierId();
					// List<ProductPicture> productPictureList = new
					// ArrayList<>();
					Set<ProductPicture> pictureSet = new HashSet<>();
					// 查询个性图片
					// 处理特殊供应商的SKUID
					skuId = dto.getSkuId();
					if ("new20150727".equals(supplierId)) {// brunarosso
						if (skuId.indexOf("-") > 0) {
							skuId = skuId.substring(0, skuId.indexOf("-"));
						}
					}
					List<ProductPicture> skuPictureList = pictureDAO
							.findDistinctProductPictureBySupplierIdAndSkuId(
									supplierId, skuId);
					if (!skuPictureList.isEmpty()) {
						// productPictureList.addAll(skuPictureList);
						pictureSet.addAll(skuPictureList);
					}
					// 查询公共的图片
					List<ProductPicture> spuPictureList = pictureDAO
							.findDistinctProductPictureBySupplierIdAndSpuIdAndSkuIdNull(
									supplierId, dto.getSpuId());
					if (!spuPictureList.isEmpty()) {
						// productPictureList.addAll(spuPictureList);
						pictureSet.addAll(spuPictureList);
					}

					List<ProductPictureDTO> picList = new ArrayList<>();
					for (ProductPicture productPicture : pictureSet) {
						ProductPictureDTO productPictureDTO = new ProductPictureDTO();
						InVoke.setValue(productPicture, productPictureDTO,
								null, null);
						picList.add(productPictureDTO);
					}

					// List<ProductPictureDTO> picList =
					// picDAO.findBySupplierAndSku(dto.getSupplierId(),
					// dto.getSkuId());
					this.setPic(dto, picList);
				}

			}
			page.setItems(productList);
		} catch (Exception e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
			throw new ServiceMessageException("数据导出失败");
		}

		return page;
	}

	@Override
	public List<ProductDTO> findProductListBySupplierAndTime(String supplier,
			Date startDate, Date endDate) throws ServiceException {

		List<ProductDTO> productList = productDAO
				.findListBySupplierAndLastDate(supplier, startDate, endDate);
		for (ProductDTO dto : productList) {

			if (null != dto.getSupplierId() && null != dto.getSkuId()) {
				List<ProductPictureDTO> picList = picDAO.findBySupplierAndSku(
						dto.getSupplierId(), dto.getSkuId());
				this.setPic(dto, picList);
			}

		}
		return productList;
	}

	@Override
	public Map<String, SkuDTO> findStockAndPriceOfSkuObjectMap(String supplier, Date startDate, Date endDate) throws ServiceException {
		List<SkuDTO> skuList = skuDAO.findListBySupplierAndLastDate(supplier, startDate, endDate);
		Map<String,SkuDTO> returnMap = new HashMap<>();
		for (SkuDTO dto : skuList) {

			  returnMap.put(dto.getSkuId(),dto);

		}
		return returnMap;
	}


	@Override
	public StringBuffer exportProduct(String supplier, Date startDate,
			Date endDate, Integer pageIndex, Integer pageSize, String flag)
			throws ServiceException {

		StringBuffer buffer = new StringBuffer("SupplierId 供货商名称" + splitSign
				+ "CategoryName 品类名称" + splitSign
				+ "Category_No 品类编号" + splitSign + "BrandNo 品牌编号" + splitSign
				+ "BrandName 品牌" + splitSign + "ProductModel 货号" + splitSign
				+ "SupplierSkuNo 供应商SkuNo" + splitSign + " 性别 " + splitSign
				+ "SopProductName 商品名称" + splitSign + "BarCode 条形码" + splitSign
				+ "ProductColor 颜色" + splitSign + "color 中文" + splitSign
				+ "ProductSize 尺码" + splitSign + "material 材质" + splitSign
				+ "material 中文材质" + splitSign + "ProductOrigin 产地" + splitSign
				+ "productUrl1" + splitSign + "productUrl2" + splitSign
				+ "productUrl3" + splitSign + "productUrl4" + splitSign
				+ "productUrl5" + splitSign + "productUrl6" + splitSign
				+ "productUrl7" + splitSign + "productUrl8" + splitSign
				+ "productUrl9" + splitSign + "PcDesc 描述" + splitSign
				+ "Stock 库存" + splitSign + "新市场价" + splitSign + "新销售价"
				+ splitSign + "新进货价" + splitSign + "markerPrice" + splitSign
				+ "sallPrice" + splitSign + "supplier Price 进货价" + splitSign
				+ "Currency 币种" + splitSign + "上市季节" + splitSign + "活动开始时间"
				+ splitSign + "活动结束时间").append("\r\n");
		Page<ProductDTO> page = null;
		if (flag.equals("same")) {
			page = this.findProductPageBySupplierAndTime(supplier, startDate,
					endDate, pageIndex, pageSize, "same");

		} else {
			page = this.findProductPageBySupplierAndTime(supplier, startDate,
					endDate, pageIndex, pageSize, "diff");
		}

		// 设置尚品网品牌
		this.setBrandMap();
		// 颜色Map赋值
		this.setColorContrastMap();
		// 材质Map 赋值
		this.setMaterialContrastMap();

		String productSize, season = "", productDetail = "", brandName = "", brandId = "", color = "", material = "", productOrigin = "";

		String supplierId="", categoryId = "", categoryName = "", productName = "";
		for (ProductDTO dto : page.getItems()) {
			
			try {
				//supplierId 供货商
				supplierId = dto.getSupplierName();
				if(StringUtils.isNotBlank(supplierId)){
					buffer.append(supplierId).append(splitSign);
				}else{
					buffer.append(dto.getSupplierId()).append(splitSign);
				}
				
				// 品类名称
				categoryName = dto.getSubCategoryName();
				if (StringUtils.isBlank(categoryName)) {
					categoryName = StringUtils.isBlank(dto.getCategoryName()) ? ""
							: dto.getCategoryName();

				}
				categoryName = categoryName.replaceAll(splitSign, " ");
				buffer.append(categoryName).append(splitSign);

				buffer.append("尚品网品类编号").append(splitSign);

				brandName = dto.getBrandName();
				if (StringUtils.isNotBlank(brandName)) {
					if (spBrandMap.containsKey(brandName.toLowerCase())) {
						brandId = spBrandMap.get(brandName.toLowerCase());
					} else {
						brandId = "";
					}
				} else {
					brandId = "";
				}

				buffer.append(!"".equals(brandId) ? brandId : "尚品网品牌编号")
						.append(splitSign);
				buffer.append(brandName).append(splitSign);
				// 货号
				buffer.append(
						null == dto.getProductCode() ? "" : dto
								.getProductCode().replaceAll(",", " ")).append(
						splitSign);
				// 供应商SKUID

				buffer.append("\"\t" + dto.getSkuId() + "\"").append(splitSign);
				// 欧洲习惯 第一个先看 男女
				buffer.append(
						null == dto.getCategoryGender() ? "" : dto
								.getCategoryGender().replaceAll(splitSign, " "))
						.append(splitSign);
				// 产品名称
				productName = dto.getProductName();
				if (StringUtils.isBlank(productName)) {
					productName = dto.getSpuName();
				}

				if (StringUtils.isNotBlank(productName)) {

					productName = productName.replaceAll(splitSign, " ")
							.replaceAll("\\r", "").replaceAll("\\n", "");
				}

				buffer.append(productName).append(splitSign);

				buffer.append("\"\t" + dto.getBarcode() + "\"").append(
						splitSign);

				// 获取颜色
				color = dto.getColor();
				buffer.append(null == color ? "" : color).append(splitSign);
				// 翻译中文
				if (StringUtils.isNotBlank(color)) {
					if (colorContrastMap.containsKey(color.toLowerCase())) {
						color = colorContrastMap.get(color.toLowerCase());
					}
				} else {
					color = "";
				}

				buffer.append(color).append(splitSign);

				// 获取尺码
				productSize = dto.getSize();
				if (StringUtils.isNotBlank(productSize)) {

					if (productSize.indexOf("+") > 0) {
						productSize = productSize.replace("+", ".5");
					}
					productSize = productSize.replaceAll(splitSign, " ");

				} else {
					productSize = "";
				}

				buffer.append(productSize).append(splitSign);

				// 获取材质
				material = dto.getMaterial();
				if (StringUtils.isBlank(material)) {
					material = "";
				} else {

					material = material.replaceAll(splitSign, " ")
							.replaceAll("\\r", "").replaceAll("\\n", "");
				}

				buffer.append(material).append(splitSign);
				// 材质 中文
				if (!"".equals(material)) {

					Set<Map.Entry<String, String>> materialSet = materialContrastMap
							.entrySet();
					for (Map.Entry<String, String> entry : materialSet) {

						material = material.toLowerCase().replaceAll(
								entry.getKey(), entry.getValue());
					}
				}

				buffer.append(material).append(splitSign);

				// 获取产地
				productOrigin = dto.getProductOrigin();
				if (StringUtils.isNotBlank(productOrigin)) {
					if (cityMap.containsKey(productOrigin.toLowerCase())) {
						productOrigin = cityMap
								.get(productOrigin.toLowerCase());
					}
				} else {
					productOrigin = "";
				}

				buffer.append(productOrigin).append(splitSign);

				// 图片
				buffer.append(dto.getPicUrl()).append(splitSign);
				buffer.append(dto.getItemPictureUrl1()).append(splitSign)
						.append(dto.getItemPictureUrl2()).append(splitSign)
						.append(dto.getItemPictureUrl3()).append(splitSign)
						.append(dto.getItemPictureUrl4()).append(splitSign)
						.append(dto.getItemPictureUrl5()).append(splitSign)
						.append(dto.getItemPictureUrl6()).append(splitSign)
						.append(dto.getItemPictureUrl7()).append(splitSign)
						.append(dto.getItemPictureUrl8()).append(splitSign);
				// 明细描述
				productDetail = dto.getProductDescription();
				if (StringUtils.isNotBlank(productDetail)) {
					if (productDetail.indexOf(splitSign) > 0) {
						productDetail = productDetail.replace(splitSign, "  ");
					}

					productDetail = productDetail.replaceAll("\\r", "")
							.replaceAll("\\n", "");
				}

				buffer.append(productDetail).append(splitSign);

				buffer.append(dto.getStock()).append(splitSign);
				// 新的价格
				String newMarketPrice = dto.getNewMarketPrice();
				String newSalePrice = dto.getNewSalePrice();
				String newSupplierPrice = dto.getNewSupplierPrice();
				if (StringUtils.isNotBlank(newMarketPrice)) {
					newMarketPrice = newMarketPrice.replace(",", ".");
				} else {
					newMarketPrice = "";
				}
				if (StringUtils.isNotBlank(newSalePrice)) {
					newSalePrice = newSalePrice.replace(",", ".");
				} else {
					newSalePrice = "";
				}
				if (StringUtils.isNotBlank(newSupplierPrice)) {
					newSupplierPrice = newSupplierPrice.replace(",", ".");
				} else {
					newSupplierPrice = "";
				}
				buffer.append(newMarketPrice).append(splitSign);
				buffer.append(newSalePrice).append(splitSign);
				buffer.append(newSupplierPrice).append(splitSign);

				// 价格
				String marketPrice = dto.getMarketPrice();
				String salePrice = dto.getSalePrice();
				String supplierPrice = dto.getSupplierPrice();
				if (StringUtils.isNotBlank( marketPrice)) {
					marketPrice = marketPrice.replace(",", ".");
				} else {
					marketPrice = "";
				}
				if (StringUtils.isNotBlank(salePrice )) {
					salePrice = salePrice.replace(",", ".");
				} else {
					salePrice = "";
				}
				if (StringUtils.isNotBlank(supplierPrice )) {
					supplierPrice = supplierPrice.replace(",", ".");
				} else {
					supplierPrice = "";
				}
				buffer.append(marketPrice).append(splitSign);
				buffer.append(salePrice).append(splitSign);
				buffer.append(supplierPrice).append(splitSign);

				buffer.append(dto.getSaleCurrency()).append(splitSign);
				
				
				// 季节

				buffer.append(
						null == dto.getSeasonName() ? dto.getSeasonId() : dto
								.getSeasonName()).append(splitSign);
				// 活动开始时间
				buffer.append(
						null == dto.getEventStartTime() ? " " : dto
								.getEventStartTime()).append(splitSign);
				// 活动结束时间
				buffer.append(null == dto.getEventEndTime() ? " " : dto
						.getEventEndTime());

				buffer.append("\r\n");
			} catch (Exception e) {
				logger.debug(dto.getSkuId() + "拉取失败" + e.getMessage());
				continue;
			}

		}
		return buffer;
	}

	/**
	 * 设置map
	 */
	private void setBrandMap() {

		int num = brandSpDAO.findCount();
		if (spBrandMap.size() < num) {
			List<BrandSpDTO> brandSpDTOList = null;
			try {
				brandSpDTOList = brandSpDAO.findAll();
			} catch (SQLException e) {
				e.printStackTrace();
				return;
			}
			for (BrandSpDTO dto : brandSpDTOList) {
				spBrandMap.put(dto.getBrandName().toLowerCase(),
						dto.getBrandId());
			}
		}
	}

	/**
	 * 设置colorContrastMap
	 */
	private void setColorContrastMap() {
		int num = colorContrastDAO.findCount();
		if (colorContrastMap.size() < num) {
			List<ColorContrastDTO> colorContrastDTOList = null;

			try {
				colorContrastDTOList = colorContrastDAO.findAll();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}

			for (ColorContrastDTO dto : colorContrastDTOList) {
				colorContrastMap.put(dto.getColor().toLowerCase(),
						dto.getColorCh());
			}
		}
	}

	/**
	 * 设置materialContrastMap
	 */
	private void setMaterialContrastMap() {
		int num = materialContrastDAO.findCount();
		if (materialContrastMap.size() < num) {
			List<MaterialContrastDTO> materialContrastDTOList = null;

			try {
				materialContrastDTOList = materialContrastDAO.findAll();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}

			for (MaterialContrastDTO dto : materialContrastDTOList) {
				materialContrastMap.put(dto.getMaterial().toLowerCase(),
						dto.getMaterialCh());
			}
		}
	}

	/**
	 * 图片赋值
	 * 
	 * @param dto
	 * @param picList
	 */
	private void setPic(ProductDTO dto, List<ProductPictureDTO> picList) {
		if (null != picList && !picList.isEmpty()) {
			Boolean isHavePic = true;
			// 如果原始无图片 则从picUrl开始赋值 赋值为picList的第一张图片
			if (StringUtils.isBlank(dto.getPicUrl())) {
				isHavePic = false;
			}
			for (int i = 0; i < picList.size(); i++) {
				if (null == picList.get(i).getPicUrl())
					continue;
				switch (i) {
				case 0:
					if (isHavePic) {
						dto.setItemPictureUrl1(picList.get(i).getPicUrl()
								.replaceAll(splitSign, "++++"));
					} else {
						dto.setPicUrl(picList.get(i).getPicUrl()
								.replaceAll(splitSign, "++++"));
					}
					break;
				case 1:

					if (isHavePic) {
						dto.setItemPictureUrl2(picList.get(i).getPicUrl()
								.replaceAll(splitSign, "++++"));
					} else {
						dto.setItemPictureUrl1(picList.get(i).getPicUrl()
								.replaceAll(splitSign, "++++"));
					}

					break;
				case 2:
					if (isHavePic) {
						dto.setItemPictureUrl3(picList.get(i).getPicUrl()
								.replaceAll(splitSign, "++++"));
					} else {
						dto.setItemPictureUrl2(picList.get(i).getPicUrl()
								.replaceAll(splitSign, "++++"));
					}
					break;
				case 3:
					if (isHavePic) {
						dto.setItemPictureUrl4(picList.get(i).getPicUrl()
								.replaceAll(splitSign, "++++"));
					} else {
						dto.setItemPictureUrl3(picList.get(i).getPicUrl()
								.replaceAll(splitSign, "++++"));
					}
					break;
				case 4:
					if (isHavePic) {
						dto.setItemPictureUrl5(picList.get(i).getPicUrl()
								.replaceAll(splitSign, "++++"));
					} else {
						dto.setItemPictureUrl4(picList.get(i).getPicUrl()
								.replaceAll(splitSign, "++++"));
					}
					break;
				case 5:
					if (isHavePic) {
						dto.setItemPictureUrl6(picList.get(i).getPicUrl()
								.replaceAll(splitSign, "++++"));
					} else {
						dto.setItemPictureUrl5(picList.get(i).getPicUrl()
								.replaceAll(splitSign, "++++"));
					}
					break;
				case 6:
					if (isHavePic) {
						dto.setItemPictureUrl7(picList.get(i).getPicUrl()
								.replaceAll(splitSign, "++++"));
					} else {
						dto.setItemPictureUrl6(picList.get(i).getPicUrl()
								.replaceAll(splitSign, "++++"));
					}
					break;
				case 7:
					if (isHavePic) {
						dto.setItemPictureUrl8(picList.get(i).getPicUrl()
								.replaceAll(splitSign, "++++"));
					} else {
						dto.setItemPictureUrl7(picList.get(i).getPicUrl()
								.replaceAll(splitSign, "++++"));
					}
					break;

				}
			}
		}

	}

	@Override
	public String exportSkuId(String supplier, Date startDate, Date endDate)
			throws ServiceException {
		List<ProductDTO> productList = productDAO.findSkuIdbySupplier(supplier,
				startDate, endDate);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < productList.size(); i++) {
			if (i != productList.size() - 1) {
				sb.append(productList.get(i).getSkuId()).append(",");
			} else {
				sb.append(productList.get(i).getSkuId());
			}
		}

		return sb.toString();
	}

	@Override
	public StringBuffer exportDiffProduct(String supplier, Date startDate,
			Date endDate, Integer pageIndex, Integer pageSize, String flag)
			throws ServiceException {

		Map<String, String> supMap = new HashMap<String, String>();
		List<SupplierDTO> supplierList = supplierDAO.findByState("1");
		for (SupplierDTO supplierDTO : supplierList) {
			supMap.put(supplierDTO.getSupplierId(),
					supplierDTO.getSupplierName());
		}
		StringBuffer buffer = new StringBuffer("供应商" + splitSign
				+ "CategoryName 品类名称" + splitSign + "品牌" + splitSign
				+ "ProductModel 货号" + splitSign + "SupplierSkuNo 供应商SkuNo"
				+ splitSign + "Stock 库存" + splitSign + "新市场价" + splitSign
				+ "新销售价" + splitSign + "新进货价" + splitSign + "markerPrice"
				+ splitSign + "sallPrice" + splitSign + "supplier Price 进货价"
				+ splitSign + "Currency 币种").append("\r\n");
		Page<ProductDTO> page = null;
		if (flag.equals("same")) {
			page = this.findProductPageBySupplierAndTime(supplier, startDate,
					endDate, pageIndex, pageSize, "same");

		} else {
			page = this.findProductPageBySupplierAndTime(supplier, startDate,
					endDate, pageIndex, pageSize, "diff");
		}
		String categoryName = "";
		String supplierId = "";
		for (ProductDTO dto : page.getItems()) {
//			if (!(dto.getNewSalePrice()==null?"-":dto.getNewSalePrice()).equals(dto.getSalePrice()==null?"-":dto.getSalePrice())
//					|| !(dto.getNewMarketPrice()==null?"-":dto.getNewMarketPrice()).equals(dto.getMarketPrice()==null?"-":dto.getMarketPrice())
//					|| !(dto.getNewSupplierPrice()==null?"-":dto.getNewSupplierPrice()).equals(dto.getSupplierPrice()==null?"-":dto.getSupplierPrice())) {
				try {
					// 供应商
					supplierId = dto.getSupplierId();
					if (null == supMap.get(supplierId)) {
						buffer.append("	" + supplierId).append(splitSign);
					} else {
						buffer.append(supMap.get(supplierId)).append(splitSign);
					}
					// 品类名称
					categoryName = dto.getSubCategoryName();
					if (StringUtils.isBlank(categoryName)) {
						categoryName = StringUtils.isBlank(dto
								.getCategoryName()) ? "" : dto
								.getCategoryName();

					}
					categoryName.replaceAll(splitSign, " ");
					buffer.append(categoryName).append(splitSign);
					buffer.append(dto.getBrandName()).append(splitSign);

					// 货号
					buffer.append(
							null == dto.getProductCode() ? "" : dto
									.getProductCode().replaceAll(",", " "))
							.append(splitSign);
					// 供应商SKUID

					buffer.append("\"\t" + dto.getSkuId() + "\"").append(
							splitSign);

					buffer.append(dto.getStock()).append(splitSign);
					// 新的价格
					String newMarketPrice = dto.getNewMarketPrice();
					String newSalePrice = dto.getNewSalePrice();
					String newSupplierPrice = dto.getNewSupplierPrice();
					if (StringUtils.isNotBlank(newMarketPrice)) {
						newMarketPrice = newMarketPrice.replace(",", ".");
					} else {
						newMarketPrice = "";
					}
					if (StringUtils.isNotBlank(newSalePrice)) {
						newSalePrice = newSalePrice.replace(",", ".");
					} else {
						newSalePrice = "";
					}
					if (StringUtils.isNotBlank(newSupplierPrice)) {
						newSupplierPrice = newSupplierPrice.replace(",", ".");
					} else {
						newSupplierPrice = "";
					}
					buffer.append(newMarketPrice).append(splitSign);
					buffer.append(newSalePrice).append(splitSign);
					buffer.append(newSupplierPrice).append(splitSign);
					// 价格
					String marketPrice = dto.getMarketPrice();
					String salePrice = dto.getSalePrice();
					String supplierPrice = dto.getSupplierPrice();
					if (StringUtils.isNotBlank( marketPrice)) {
						marketPrice = marketPrice.replace(",", ".");
					} else {
						marketPrice = "";
					}
					if (StringUtils.isNotBlank(salePrice )) {
						salePrice = salePrice.replace(",", ".");
					} else {
						salePrice = "";
					}
					if (StringUtils.isNotBlank(supplierPrice )) {
						supplierPrice = supplierPrice.replace(",", ".");
					} else {
						supplierPrice = "";
					}
					buffer.append(marketPrice).append(splitSign);
					buffer.append(salePrice).append(splitSign);
					buffer.append(supplierPrice).append(splitSign);
					buffer.append(dto.getSaleCurrency());

					buffer.append("\r\n");
				} catch (Exception e) {
					logger.debug(dto.getSkuId() + "拉取失败" + e.getMessage());
					continue;
				}
//			}
		}
		return buffer;
	}

	@Override
	public ProductDTO findProductForOrder(String supplierId,String skuId)
			throws ServiceException {
		ProductDTO productOrderDTO = productDAO.findProductOrderDTOList(supplierId,skuId);
		return productOrderDTO;
	}

}
