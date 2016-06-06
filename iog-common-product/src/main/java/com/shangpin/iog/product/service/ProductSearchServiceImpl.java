package com.shangpin.iog.product.service;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.framework.page.Page;
import com.shangpin.iog.common.utils.InVoke;
import com.shangpin.iog.dto.BrandSpDTO;
import com.shangpin.iog.dto.CategoryContrastDTO;
import com.shangpin.iog.dto.ColorContrastDTO;
import com.shangpin.iog.dto.MaterialContrastDTO;
import com.shangpin.iog.dto.ProductDTO;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.dto.SupplierDTO;
import com.shangpin.iog.mongodao.PictureDAO;
import com.shangpin.iog.mongodomain.ProductPicture;
import com.shangpin.iog.product.dao.BrandSpMapper;
import com.shangpin.iog.product.dao.CategoryContrastMapper;
import com.shangpin.iog.product.dao.ColorContrastMapper;
import com.shangpin.iog.product.dao.EPRuleMapper;
import com.shangpin.iog.product.dao.MaterialContrastMapper;
import com.shangpin.iog.product.dao.ProductPictureMapper;
import com.shangpin.iog.product.dao.ProductsMapper;
import com.shangpin.iog.product.dao.SkuMapper;
import com.shangpin.iog.product.dao.SpuMapper;
import com.shangpin.iog.product.dao.SupplierMapper;
import com.shangpin.iog.service.ProductSearchService;

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
	
	@Autowired
	EPRuleMapper ePRuleDAO;
	
	@Autowired
	CategoryContrastMapper categoryDAO;
	
	private static Map<String, String> spBrandMap = new HashMap<>();
	private static Map<String, String> colorContrastMap = new HashMap<>();
	private static Map<String, String> materialContrastMap = new HashMap<>();
	private static Map<String, String> smallMaterialContrastMap= new HashMap<>();
	private static Map<String, String> categoryContrastMap = new HashMap<>();
	

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
					
					
					
				}else if(flag.equals("ep_regular")){//根据ep规则查找product
					productList = productDAO.findListByEPRegularAndLastDate(
							supplier, startDate, endDate, new RowBounds(
							pageIndex, pageSize));
				}				
				else {
					productList = productDAO.findDiffListBySupplierAndLastDate(
							supplier, startDate, endDate, new RowBounds(
									pageIndex, pageSize));
				}

			} else {
				if (flag .equals("same")) {
					productList = productDAO.findListBySupplierAndLastDate(
							supplier, startDate, endDate);					
										
				}else if(flag.equals("ep_regular")){//根据ep规则查找product		
					
					productList = productDAO.findListByEPRegularAndLastDate(
							supplier, startDate, endDate);
					
				}
				else {
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
				+ "Category 品类翻译" + splitSign
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
				+ "Currency 币种" + splitSign + "新上市季节" + splitSign + "上市季节" + splitSign 
				+ "活动开始时间"+ splitSign + "活动结束时间"+ splitSign + "备注").append("\r\n");
		Page<ProductDTO> page = null;
		if (flag.equals("same")) {
			page = this.findProductPageBySupplierAndTime(supplier, startDate,
					endDate, pageIndex, pageSize, "same");

		}else if(flag.equals("ep_regular")){
			page = this.findProductPageBySupplierAndTime(supplier, startDate,
					endDate, pageIndex, pageSize, "ep_regular");
		}		
		else {
			page = this.findProductPageBySupplierAndTime(supplier, startDate,
					endDate, pageIndex, pageSize, "diff");
		}

		//品类map赋值
		this.setCategoryMap();
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
				
				//翻译
				String categoryCH = "";
				if(StringUtils.isNotBlank(categoryName)){
					if(categoryContrastMap.containsKey(categoryName.toLowerCase())){
						categoryCH = categoryContrastMap.get(categoryName.toLowerCase());
					}
				}
				
				categoryName = categoryName.replaceAll(splitSign, " ");
				buffer.append(categoryName).append(splitSign);
				
				//品类翻译
				buffer.append(categoryCH).append(splitSign);

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
				
				if(supplier== "2015081701437"){
					brandName = brandName.replaceAll("C?LINE", "CÈLINE");
				}
				
				buffer.append(brandName).append(splitSign);
				
				//品牌翻译
				
				
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
				color = dto.getColor()==null?"":dto.getColor().replace(",", " ").replaceAll("/", " "); 
				buffer.append(null == color ? "" : color.replace(",", " ")).append(splitSign);
				// 翻译中文
				String colorCh = "";
				if (StringUtils.isNotBlank(color)) {
					if(colorContrastMap.containsKey(color.toLowerCase())){
						colorCh = colorContrastMap.get(color.toLowerCase());
					}else{
						for(String co :color.split("\\s+")){
							if (colorContrastMap.containsKey(co.toLowerCase())) {
								colorCh += colorContrastMap.get(co.toLowerCase());
							}else{
								colorCh += co;
							}
						}
					}					
				}
				buffer.append(colorCh).append(splitSign);

				// 获取尺码
				productSize = dto.getSize();
				if (StringUtils.isNotBlank(productSize)) {
					productSize=productSize.replace(",", ".");
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
				if (StringUtils.isNotBlank(material)) {
					
					//先遍历带有空格的材质
					Set<Map.Entry<String, String>> materialSet = materialContrastMap
							.entrySet();
					for (Map.Entry<String, String> entry : materialSet) {

						material = material.toLowerCase().replaceAll(
								entry.getKey(), entry.getValue());
					}
					
					//再遍历单个材质
					Set<Map.Entry<String, String>> smallMaterialSet = smallMaterialContrastMap
							.entrySet();
					for (Map.Entry<String, String> entry : smallMaterialSet) {

						material = material.toLowerCase()
								.replaceAll(entry.getKey(),
										entry.getValue());
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
				if(supplier== "2015081701437"){//处理特殊供货商的特殊字符
					buffer.append(dto.getPicUrl().replaceAll("C?LINE", "CÈLINE")).append(splitSign);
					buffer.append(dto.getItemPictureUrl1().replaceAll("C?LINE", "CÈLINE")).append(splitSign)
							.append(dto.getItemPictureUrl2().replaceAll("C?LINE", "CÈLINE")).append(splitSign)
							.append(dto.getItemPictureUrl3().replaceAll("C?LINE", "CÈLINE")).append(splitSign)
							.append(dto.getItemPictureUrl4().replaceAll("C?LINE", "CÈLINE")).append(splitSign)
							.append(dto.getItemPictureUrl5().replaceAll("C?LINE", "CÈLINE")).append(splitSign)
							.append(dto.getItemPictureUrl6().replaceAll("C?LINE", "CÈLINE")).append(splitSign)
							.append(dto.getItemPictureUrl7().replaceAll("C?LINE", "CÈLINE")).append(splitSign)
							.append(dto.getItemPictureUrl8().replaceAll("C?LINE", "CÈLINE")).append(splitSign);
				}else{
					buffer.append(dto.getPicUrl()).append(splitSign);
					buffer.append(dto.getItemPictureUrl1()).append(splitSign)
							.append(dto.getItemPictureUrl2()).append(splitSign)
							.append(dto.getItemPictureUrl3()).append(splitSign)
							.append(dto.getItemPictureUrl4()).append(splitSign)
							.append(dto.getItemPictureUrl5()).append(splitSign)
							.append(dto.getItemPictureUrl6()).append(splitSign)
							.append(dto.getItemPictureUrl7()).append(splitSign)
							.append(dto.getItemPictureUrl8()).append(splitSign);
				}
				
				// 明细描述
				productDetail = dto.getProductDescription();
				if (StringUtils.isNotBlank(productDetail)) {					
					productDetail = productDetail.replaceAll(splitSign, "  ");
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
				
				//新季节
				buffer.append(
						null == dto.getNewseasonName() ? dto.getNewseasonId() : dto
								.getNewseasonName()).append(splitSign);
				
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
						.getEventEndTime()).append(splitSign);;
buffer.append(dto.getMemo());
				buffer.append("\r\n");
			} catch (Exception e) {
				logger.debug(dto.getSkuId() + "拉取失败" + e.getMessage());
				continue;
			}

		}
		return buffer;
	}
	
	/**
	 * 
	 */
	private void setCategoryMap(){
		
		int num = categoryDAO.findCount();
		if (categoryContrastMap.size() < num) {
			List<CategoryContrastDTO> cateList = null;
			try {
				cateList = categoryDAO.findAll();
			} catch (SQLException e) {
				e.printStackTrace();
				return;
			}
			for (CategoryContrastDTO dto : cateList) {
				categoryContrastMap.put(dto.getCategoryName().toLowerCase(),
						dto.getCategoryCH());
			}
		}
		
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
		Map<String, String> material = new HashMap<>(); 
		int num = materialContrastDAO.findCount();
		if (material.size() < num) {
			List<MaterialContrastDTO> materialContrastDTOList = null;

			try {
				materialContrastDTOList = materialContrastDAO.findAll();
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}

			for (MaterialContrastDTO dto : materialContrastDTOList) {
				if(dto.getMaterial().contains("\\s+")){
					materialContrastMap.put(dto.getMaterial().toLowerCase(),
							dto.getMaterialCh());
				}else{
					smallMaterialContrastMap.put(dto.getMaterial().toLowerCase(),
							dto.getMaterialCh());
				}
				
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
	
	public StringBuffer getDiffSeasonProducts(String suppliers,Date startDate,Date endDate,Integer pageIndex,Integer pageSize) throws ServiceException{
		
		Map<String, String> supMap = new HashMap<String, String>();
		List<SupplierDTO> supplierList = supplierDAO.findByState("1");
		for (SupplierDTO supplierDTO : supplierList) {
			supMap.put(supplierDTO.getSupplierId(),
					supplierDTO.getSupplierName());
		}
		StringBuffer buffer = new StringBuffer("供应商" + splitSign				
				+ "ProductModel 货号" + splitSign + "供货商skuid" + splitSign+ "新上市季节" + splitSign+ "上市季节").append("\r\n");
		for(String supplier : suppliers.split(",")){
			if(StringUtils.isNotBlank(supplier)){
				final List<ProductDTO> products = productDAO.findDiffSeasonProducts(supplier,startDate,endDate);
				if(null == products || products.size()==0){
					return null;
				}else{
					for(ProductDTO dto : products){
						// 供应商
						String supplierId = dto.getSupplierId();
						if (null == supMap.get(supplierId)) {
							buffer.append("	" + supplierId).append(splitSign);
						} else {
							buffer.append(supMap.get(supplierId)).append(splitSign);
						}
						buffer.append(
								null == dto.getProductCode() ? "" : dto
										.getProductCode().replaceAll(",", " "))
								.append(splitSign);
						//supplier skuid
						buffer.append(
								null == dto.getSkuId() ? "" : dto.getSkuId())
								.append(splitSign);
						//新季节
						buffer.append(
								null == dto.getNewseasonName() ? dto.getNewseasonId() : dto
										.getNewseasonName()).append(splitSign);
						
						// 季节

						buffer.append(
								null == dto.getSeasonName() ? dto.getSeasonId() : dto
										.getSeasonName());
						buffer.append("\r\n");
					}
				}
			}
		}
		return buffer;
	}
	
	public StringBuffer getDiffProduct(String supplier,Date startDate,Date endDate,Integer pageIndex,Integer pageSize,String flag) throws ServiceException{
		Map<String, String> supMap = new HashMap<String, String>();
		List<SupplierDTO> supplierList = supplierDAO.findByState("1");
		for (SupplierDTO supplierDTO : supplierList) {
			supMap.put(supplierDTO.getSupplierId(),
					supplierDTO.getSupplierName());
		}
		StringBuffer buffer = new StringBuffer("供应商" + splitSign				
				+ "ProductModel 货号" + splitSign + "供货商skuid" + splitSign+ "新市场价" + splitSign
				+ "新销售价" + splitSign + "新进货价" + splitSign + "市场价"
				+ splitSign + "销售价" + splitSign + "进货价").append("\r\n");
		
		final Page<ProductDTO> page = this.findProductPageBySupplierAndTime(supplier, startDate,
				endDate, pageIndex, pageSize, "diff");
		
		if(null==page || null== page.getItems()){
			return null;
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
					
					// 货号
					buffer.append(
							null == dto.getProductCode() ? "" : dto
									.getProductCode().replaceAll(",", " "))
							.append(splitSign);
					//supplier skuid
					buffer.append(
							null == dto.getSkuId() ? "" : dto.getSkuId())
							.append(splitSign);
					
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
					buffer.append(supplierPrice);

					buffer.append("\r\n");
				} catch (Exception e) {
					logger.debug(dto.getSkuId() + "拉取失败" + e.getMessage());
					continue;
				}
//			}
		}
		
		//更新价格
		Thread t = new Thread(	 new Runnable() {
			@Override
			public void run() {
				try {
					
					for (ProductDTO dto : page.getItems()) {
						try{
							
							SkuDTO skuDTO = new SkuDTO();
							skuDTO.setUpdateTime(new Date());
							if(!dto.getMarketPrice().equals(dto.getNewMarketPrice())){
								skuDTO.setNewMarketPrice(dto.getNewMarketPrice());
							}
							if(!dto.getSalePrice().equals(dto.getNewSalePrice())){
								skuDTO.setNewSalePrice(dto.getNewSalePrice());
							}
							if(!dto.getSupplierPrice().equals(dto.getNewSupplierPrice())){
								skuDTO.setNewSupplierPrice(dto.getNewSupplierPrice());
							}
							skuDAO.updatePrice(skuDTO); 
							
						}catch(Exception ex){
							ex.printStackTrace();
						}						
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
		
		return buffer;
	}

	@Override
	public ProductDTO findProductForOrder(String supplierId,String skuId)
			throws ServiceException {
		ProductDTO productOrderDTO = productDAO.findProductOrderDTOList(supplierId,skuId);
		return productOrderDTO;
	}

	@Override
	public ProductDTO findProductBySupplierIdAndSkuId(String supplierId, String skuId) throws ServiceException {
		return  productDAO.findProductBySupplierIdAndSkuId(supplierId,skuId);
	}

	@Override
	public StringBuffer dailyUpdatedProduct(String supplier, int day,
			Date now, Integer pageIndex, Integer pageSize, String flag)
			throws ServiceException {
		
		StringBuffer buffer = new StringBuffer("SupplierId 供货商名称" + splitSign
				+ "日期" + splitSign				
				+ "sku数量" + splitSign
				+ "缺少信息的sku数量").append("\r\n");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");		
		List<SupplierDTO> supplierList = supplierDAO.findByState("1");
		for(SupplierDTO supplierDTO:supplierList){
			System.out.println("======================="+supplierDTO.getSupplierId()+"=============================");
			for(int i=0;i<day;i++){
				Calendar calendar = Calendar.getInstance();	
				calendar.setTime(now);
			    calendar.set(Calendar.HOUR_OF_DAY, 0);
			    calendar.set(Calendar.MINUTE, 0);
			    calendar.set(Calendar.SECOND, 0);
				calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - i); 
				Date startDate = calendar.getTime();
				
				Calendar calendar1 = Calendar.getInstance();	
				calendar1.setTime(startDate);
				calendar1.set(Calendar.DATE, calendar1.get(Calendar.DATE) +1); 
				Date endDate = calendar1.getTime();
				System.out.println(startDate+"----------------------"+endDate);
				Page<ProductDTO> page = null;
				if (flag.equals("same")) {
					page = this.findProductPageBySupplierAndTime(supplierDTO.getSupplierId(), startDate,
							endDate, pageIndex, pageSize, "same");				
					if(null != page && null !=page.getItems() && page.getItems().size()>0){
						String supplierId="";						
						int goodSkuNo = 0;
						int badSkuNo = 0;
						for (ProductDTO dto : page.getItems()) {
							if(StringUtils.isNotBlank(dto.getSupplierName())){
								supplierId = dto.getSupplierName();
							}else{
								supplierId = dto.getSupplierId();
							}
							if(StringUtils.isNotBlank(dto.getCategoryName())&&StringUtils.isNotBlank(dto.getBrandName())
									&&StringUtils.isNotBlank(dto.getProductCode())&&StringUtils.isNotBlank(dto.getCategoryGender())
									&&StringUtils.isNotBlank(dto.getColor())&&StringUtils.isNotBlank(dto.getSize())
									&&StringUtils.isNotBlank(dto.getMaterial())
									&&StringUtils.isNotBlank(dto.getProductOrigin())
									&&(
									StringUtils.isNotBlank(dto.getPicUrl())||StringUtils.isNotBlank(dto.getItemPictureUrl1())||StringUtils.isNotBlank(dto.getItemPictureUrl2())||StringUtils.isNotBlank(dto.getItemPictureUrl3())||StringUtils.isNotBlank(dto.getItemPictureUrl4())||StringUtils.isNotBlank(dto.getItemPictureUrl5())||StringUtils.isNotBlank(dto.getItemPictureUrl6())
									||StringUtils.isNotBlank(dto.getItemPictureUrl7())||StringUtils.isNotBlank(dto.getItemPictureUrl8())
									)
									&&(StringUtils.isNotBlank(dto.getMarketPrice())||StringUtils.isNotBlank(dto.getSalePrice())||StringUtils.isNotBlank(dto.getSupplierPrice()))){
								goodSkuNo ++;
							}else{
								badSkuNo++;
							}
							
						}
						if(StringUtils.isNotBlank(supplierId)){
							buffer.append(supplierId).append(splitSign);
							buffer.append(sdf.format(startDate)).append(splitSign);
//							buffer.append(sdf.format(endDate)).append(splitSign); 
							buffer.append(goodSkuNo+badSkuNo).append(splitSign);
							buffer.append(badSkuNo);
							buffer.append("\r\n");
						}						
					}
				}
			}
		}	
		
		return buffer;
	}

	@Override
	public StringBuffer dailyGoodProducts(String[] suppliers, Date startDate,
			Date endDate, Integer pageIndex, Integer pageSize)
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
				+ "Currency 币种" + splitSign + "上市季节" + splitSign + "开始时间"
				+ splitSign + "结束时间").append("\r\n");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");	
		
		for (int i = 0; i < suppliers.length; i++) {

			try {

				String supplier = suppliers[i];
				Page<ProductDTO> page = null;
				page = this.findProductPageBySupplierAndTime(supplier,
						startDate, endDate, pageIndex, pageSize, "same");

				// 设置尚品网品牌
				this.setBrandMap();
				// 颜色Map赋值
				this.setColorContrastMap();
				// 材质Map 赋值
				this.setMaterialContrastMap();

				String productSize, season = "", productDetail = "", brandName = "", brandId = "", color = "", material = "", productOrigin = "";

				String supplierId = "", categoryId = "", categoryName = "", productName = "";
				for (ProductDTO dto : page.getItems()) {

					try {

						if (StringUtils.isNotBlank(dto.getCategoryName())
								&& StringUtils.isNotBlank(dto.getBrandName())
								&& StringUtils.isNotBlank(dto.getProductCode())
								&& StringUtils.isNotBlank(dto
										.getCategoryGender())
								&& StringUtils.isNotBlank(dto.getColor())
								&& StringUtils.isNotBlank(dto.getSize())
								&& StringUtils.isNotBlank(dto.getMaterial())
								&& StringUtils.isNotBlank(dto
										.getProductOrigin())
								&& (StringUtils.isNotBlank(dto.getPicUrl())
										|| StringUtils.isNotBlank(dto
												.getItemPictureUrl1())
									)
								&& (StringUtils
										.isNotBlank(dto.getMarketPrice())
										|| StringUtils.isNotBlank(dto
												.getSalePrice()) || StringUtils
											.isNotBlank(dto.getSupplierPrice()))) {

							// supplierId 供货商
							supplierId = dto.getSupplierName();
							if (StringUtils.isNotBlank(supplierId)) {
								buffer.append(supplierId).append(splitSign);
							} else {
								buffer.append(dto.getSupplierId()).append(
										splitSign);
							}

							// 品类名称
							categoryName = dto.getSubCategoryName();
							if (StringUtils.isBlank(categoryName)) {
								categoryName = StringUtils.isBlank(dto
										.getCategoryName()) ? "" : dto
										.getCategoryName();

							}
							categoryName = categoryName.replaceAll(splitSign,
									" ");
							buffer.append(categoryName).append(splitSign);

							buffer.append("尚品网品类编号").append(splitSign);

							brandName = dto.getBrandName();
							if (StringUtils.isNotBlank(brandName)) {
								if (spBrandMap.containsKey(brandName
										.toLowerCase())) {
									brandId = spBrandMap.get(brandName
											.toLowerCase());
								} else {
									brandId = "";
								}
							} else {
								brandId = "";
							}

							buffer.append(
									!"".equals(brandId) ? brandId : "尚品网品牌编号")
									.append(splitSign);
							buffer.append(brandName).append(splitSign);
							// 货号
							buffer.append(
									null == dto.getProductCode() ? "" : dto
											.getProductCode().replaceAll(",",
													" ")).append(splitSign);
							// 供应商SKUID

							buffer.append("\"\t" + dto.getSkuId() + "\"")
									.append(splitSign);
							// 欧洲习惯 第一个先看 男女
							buffer.append(
									null == dto.getCategoryGender() ? "" : dto
											.getCategoryGender().replaceAll(
													splitSign, " ")).append(
									splitSign);
							// 产品名称
							productName = dto.getProductName();
							if (StringUtils.isBlank(productName)) {
								productName = dto.getSpuName();
							}

							if (StringUtils.isNotBlank(productName)) {

								productName = productName
										.replaceAll(splitSign, " ")
										.replaceAll("\\r", "")
										.replaceAll("\\n", "");
							}

							buffer.append(productName).append(splitSign);

							buffer.append("\"\t" + dto.getBarcode() + "\"")
									.append(splitSign);

							// 获取颜色
							color = dto.getColor() == null ? "" : dto
									.getColor().replace(",", " ");
							buffer.append(
									null == color ? "" : color
											.replace(",", " ")).append(
									splitSign);
							// 翻译中文
							if (StringUtils.isNotBlank(color)) {
								if (colorContrastMap.containsKey(color
										.toLowerCase())) {
									color = colorContrastMap.get(color
											.toLowerCase());
								}
							} else {
								color = "";
							}

							buffer.append(color).append(splitSign);

							// 获取尺码
							productSize = dto.getSize();
							if (StringUtils.isNotBlank(productSize)) {
								productSize = productSize.replace(",", ".");
								if (productSize.indexOf("+") > 0) {
									productSize = productSize
											.replace("+", ".5");
								}
								productSize = productSize.replaceAll(splitSign,
										" ");

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
										.replaceAll("\\r", "")
										.replaceAll("\\n", "");
							}

							buffer.append(material).append(splitSign);
							// 材质 中文
							if (!"".equals(material)) {

								//先遍历带有空格的材质
								Set<Map.Entry<String, String>> materialSet = materialContrastMap
										.entrySet();
								for (Map.Entry<String, String> entry : materialSet) {

									material = material.toLowerCase()
											.replaceAll(entry.getKey(),
													entry.getValue());
								}
								//再遍历单个材质
								Set<Map.Entry<String, String>> smallMaterialSet = smallMaterialContrastMap
										.entrySet();
								for (Map.Entry<String, String> entry : smallMaterialSet) {

									material = material.toLowerCase()
											.replaceAll(entry.getKey(),
													entry.getValue());
								}
								
							}

							buffer.append(material).append(splitSign);

							// 获取产地
							productOrigin = dto.getProductOrigin();
							if (StringUtils.isNotBlank(productOrigin)) {
								if (cityMap.containsKey(productOrigin
										.toLowerCase())) {
									productOrigin = cityMap.get(productOrigin
											.toLowerCase());
								}
							} else {
								productOrigin = "";
							}

							buffer.append(productOrigin).append(splitSign);

							// 图片
							buffer.append(dto.getPicUrl()).append(splitSign);
							buffer.append(dto.getItemPictureUrl1())
									.append(splitSign)
									.append(dto.getItemPictureUrl2())
									.append(splitSign)
									.append(dto.getItemPictureUrl3())
									.append(splitSign)
									.append(dto.getItemPictureUrl4())
									.append(splitSign)
									.append(dto.getItemPictureUrl5())
									.append(splitSign)
									.append(dto.getItemPictureUrl6())
									.append(splitSign)
									.append(dto.getItemPictureUrl7())
									.append(splitSign)
									.append(dto.getItemPictureUrl8())
									.append(splitSign);
							// 明细描述
							productDetail = dto.getProductDescription();
							if (StringUtils.isNotBlank(productDetail)) {
								if (productDetail.indexOf(splitSign) > 0) {
									productDetail = productDetail.replace(
											splitSign, "  ");
								}

								productDetail = productDetail.replaceAll("\\r",
										"").replaceAll("\\n", "");
							}

							buffer.append(productDetail).append(splitSign);

							buffer.append(dto.getStock()).append(splitSign);
							// 新的价格
							String newMarketPrice = dto.getNewMarketPrice();
							String newSalePrice = dto.getNewSalePrice();
							String newSupplierPrice = dto.getNewSupplierPrice();
							if (StringUtils.isNotBlank(newMarketPrice)) {
								newMarketPrice = newMarketPrice.replace(",",
										".");
							} else {
								newMarketPrice = "";
							}
							if (StringUtils.isNotBlank(newSalePrice)) {
								newSalePrice = newSalePrice.replace(",", ".");
							} else {
								newSalePrice = "";
							}
							if (StringUtils.isNotBlank(newSupplierPrice)) {
								newSupplierPrice = newSupplierPrice.replace(
										",", ".");
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
							if (StringUtils.isNotBlank(marketPrice)) {
								marketPrice = marketPrice.replace(",", ".");
							} else {
								marketPrice = "";
							}
							if (StringUtils.isNotBlank(salePrice)) {
								salePrice = salePrice.replace(",", ".");
							} else {
								salePrice = "";
							}
							if (StringUtils.isNotBlank(supplierPrice)) {
								supplierPrice = supplierPrice.replace(",", ".");
							} else {
								supplierPrice = "";
							}
							buffer.append(marketPrice).append(splitSign);
							buffer.append(salePrice).append(splitSign);
							buffer.append(supplierPrice).append(splitSign);

							buffer.append(dto.getSaleCurrency()).append(
									splitSign);

							// 季节

							buffer.append(
									null == dto.getSeasonName() ? dto
											.getSeasonId() : dto
											.getSeasonName()).append(splitSign);
							// 活动开始时间
							buffer.append(sdf.format(startDate)).append(
									splitSign);
							// 活动结束时间
							buffer.append(sdf.format(endDate));

							buffer.append("\r\n");

						}

					} catch (Exception e) {
						logger.debug(dto.getSkuId() + "拉取失败" + e.getMessage());
						continue;
					}

				}

			} catch (Exception ex) {
				logger.debug(ex.getMessage());
				ex.printStackTrace();
			}
		}
		
		return buffer;
	}

	@Override
	public SpuDTO findPartSpuData(String supplierId, String spuId) {
		return spuDAO.findPartBySupAndSpuId(supplierId, spuId);
	}
	public List<SpuDTO> findpartSpuListBySupplier(String supplierId){
	    return spuDAO.findPartSPUListBySupplierId(supplierId);
	}

	@Override
	public List<SpuDTO> findPartBySupAndSpuId(String supplierId) {
		// TODO Auto-generated method stub
		return null;
	}
	public StringBuffer exportProductByEpRule(String supplier,Date startDate,Date endDate,Integer pageIndex,Integer pageSize) throws ServiceException{
		
		StringBuffer buffer = new StringBuffer("SupplierId 供货商名称" + splitSign
				+ "CategoryName 品类名称" + splitSign
				+ "Category 品类翻译" + splitSign
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
				+ "Currency 币种" + splitSign + "新上市季节" + splitSign+ "上市季节" + splitSign + "活动开始时间"
				+ splitSign + "活动结束时间"+ splitSign + "备注").append("\r\n");
		Page<ProductDTO> page = this.findProductPageBySupplierAndTime(supplier, startDate,
				endDate, pageIndex, pageSize, "same");
		//品牌
		List<String> brandList = new ArrayList<String>();
		for(String brand:ePRuleDAO.findAll(2, 1)){
			brandList.add(brand.toUpperCase());
		}
		//品类 排除
		List<String> categeryList = new ArrayList<String>();
		for(String cat:ePRuleDAO.findAll(3, 0)){
			categeryList.add(cat.toUpperCase());
		}
		//季节 排除
		List<String> seasonList = new ArrayList<String>();
		for(String season:ePRuleDAO.findAll(5, 0)){
			seasonList.add(season.toUpperCase());
		}
		//性别 排除
		List<String> genderList = new ArrayList<String>();
		for(String gender:ePRuleDAO.findAll(6, 0)){
			genderList.add(gender.toUpperCase());
		}
		
//		System.out.println("brandList.size======================="+brandList.size());
//		System.out.println("categeryList.size==================="+categeryList.size());
//		System.out.println("seasonList.size===================="+seasonList.size());
//		System.out.println("genderList.size======================"+genderList.size()); 
		
		//品类map赋值
		this.setCategoryMap();
		// 设置尚品网品牌
		this.setBrandMap();
		// 颜色Map赋值
		this.setColorContrastMap();
		// 材质Map 赋值
		this.setMaterialContrastMap();

		String productSize, productDetail = "", brandName = "", brandId = "", color = "", material = "", productOrigin = "";

		String supplierId="", categoryName = "", productName = "";	
		for (ProductDTO dto : page.getItems()) {
			try {
				
				if(!genderList.contains(null != dto.getCategoryGender()? dto.getCategoryGender().toUpperCase() : "")){
					if(!seasonList.contains(null != dto.getSeasonName()? dto.getSeasonName().toUpperCase() : "")){
						if(!categeryList.contains(null != dto.getCategoryName()? dto.getCategoryName().toUpperCase() : "")){
							if(null != dto.getBrandName() && (brandList.contains(dto.getBrandName().toUpperCase()) || dto.getBrandName().equals("Chloé") || dto.getBrandName().equals("Chloe'"))){
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
									
									//翻译
									String categoryCH = "";
									if(StringUtils.isNotBlank(categoryName)){
										if(categoryContrastMap.containsKey(categoryName.toLowerCase())){
											categoryCH = categoryContrastMap.get(categoryName.toLowerCase());
										}
									}
									
									categoryName = categoryName.replaceAll(splitSign, " ");
									buffer.append(categoryName).append(splitSign);
									
									//品类翻译
									buffer.append(categoryCH).append(splitSign);
									
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
									color = dto.getColor()==null?"":dto.getColor().replace(",", " ").replaceAll("/", " "); 
									buffer.append(null == color ? "" : color.replace(",", " ")).append(splitSign);
									// 翻译中文
									String colorCh = "";
									if (StringUtils.isNotBlank(color)) {
										if(colorContrastMap.containsKey(color.toLowerCase())){
											colorCh = colorContrastMap.get(color.toLowerCase());
										}else{
											for(String co :color.split("\\s+")){
												if (colorContrastMap.containsKey(co.toLowerCase())) {
													colorCh += colorContrastMap.get(co.toLowerCase());
												}else{
													colorCh += co;
												}
											}
										}	
										
									}
									buffer.append(colorCh).append(splitSign);
	
									// 获取尺码
									productSize = dto.getSize();
									if (StringUtils.isNotBlank(productSize)) {
										productSize=productSize.replace(",", ".");
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
									if (StringUtils.isNotBlank(material)) {
										
										//先遍历带有空格的材质
										Set<Map.Entry<String, String>> materialSet = materialContrastMap
												.entrySet();
										for (Map.Entry<String, String> entry : materialSet) {

											material = material.toLowerCase().replaceAll(
													entry.getKey(), entry.getValue());
										}
										
										//再遍历单个材质
										Set<Map.Entry<String, String>> smallMaterialSet = smallMaterialContrastMap
												.entrySet();
										for (Map.Entry<String, String> entry : smallMaterialSet) {

											material = material.toLowerCase()
													.replaceAll(entry.getKey(),
															entry.getValue());
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
										productDetail = productDetail.replaceAll(splitSign, "  ");
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
									//新季节
									buffer.append(
											null == dto.getNewseasonName() ? dto.getNewseasonId() : dto
													.getNewseasonName()).append(splitSign);
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
											.getEventEndTime()).append(splitSign);;
											buffer.append(dto.getMemo());
									buffer.append("\r\n");
								} catch (Exception e) {
									logger.debug(dto.getSkuId() + "拉取失败" + e.getMessage());
									continue;
								}
							}
						}
					}
				}
			} catch (Exception e) {
				logger.debug(dto.getSkuId() + "拉取失败" + e.getMessage());
				continue;
			}
		}
		
		
		return buffer;
				
	}
	@Override
	public List<ProductDTO> findPicName(String supplier,Date startDate, Date endDate, Integer pageIndex, Integer pageSize){
		List<ProductDTO> pList = null;
		if (null != pageIndex && null != pageSize) {
			pList = productDAO.findPicNameListByEPRegularAndLastDate(supplier, startDate, endDate, new RowBounds(pageIndex, pageSize));
		}else{
			pList = productDAO.findPicNameListByEPRegularAndLastDate(supplier, startDate, endDate);
		}
		return pList;
	}
	
	
	
}
