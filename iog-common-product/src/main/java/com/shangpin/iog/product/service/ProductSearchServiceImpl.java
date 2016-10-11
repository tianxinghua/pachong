package com.shangpin.iog.product.service;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

import javax.imageio.ImageIO;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.ClientAnchor.AnchorType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;
import com.shangpin.framework.page.Page;
import com.shangpin.iog.common.utils.DateTimeUtil;
import com.shangpin.iog.common.utils.InVoke;
import com.shangpin.iog.common.utils.JavaUtils;
import com.shangpin.iog.dto.BrandSpDTO;
import com.shangpin.iog.dto.BuEpRuleDTO;
import com.shangpin.iog.dto.BuEpSpecialDTO;
import com.shangpin.iog.dto.CategoryContrastDTO;
import com.shangpin.iog.dto.ColorContrastDTO;
import com.shangpin.iog.dto.HubSupplierValueMappingDTO;
import com.shangpin.iog.dto.MaterialContrastDTO;
import com.shangpin.iog.dto.ProductDTO;
import com.shangpin.iog.dto.ProductOriginConstrastDTO;
import com.shangpin.iog.dto.ProductPictureDTO;
import com.shangpin.iog.dto.SeasonRelationDTO;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.dto.SupplierDTO;
import com.shangpin.iog.mongodao.PictureDAO;
import com.shangpin.iog.mongodomain.ProductPicture;
import com.shangpin.iog.product.dao.BrandSpMapper;
import com.shangpin.iog.product.dao.BuEpRuleMapper;
import com.shangpin.iog.product.dao.BuEpSpecialMapper;
import com.shangpin.iog.product.dao.BuEpValueMapper;
import com.shangpin.iog.product.dao.CategoryContrastMapper;
import com.shangpin.iog.product.dao.ColorContrastMapper;
import com.shangpin.iog.product.dao.EPRuleMapper;
import com.shangpin.iog.product.dao.HubSupplierValueMappingMapper;
import com.shangpin.iog.product.dao.MaterialContrastMapper;
import com.shangpin.iog.product.dao.ProductOriginConstrastMapper;
import com.shangpin.iog.product.dao.ProductPictureMapper;
import com.shangpin.iog.product.dao.ProductsMapper;
import com.shangpin.iog.product.dao.SkuMapper;
import com.shangpin.iog.product.dao.SpuMapper;
import com.shangpin.iog.product.dao.SupplierMapper;
import com.shangpin.iog.product.dto.BuParamDTO;
import com.shangpin.iog.product.special.Special;
import com.shangpin.iog.service.ProductOriginConstrastService;
import com.shangpin.iog.service.ProductSearchService;
import com.shangpin.iog.service.SeasonRelationService;

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
	@Autowired
	BuEpRuleMapper BuEpRuleDAO;
	@Autowired
	BuEpValueMapper BuEpValueDAO;
	@Autowired
	BuEpSpecialMapper BuEpSpecialDAO;
	@Autowired
	SeasonRelationService seasonRelationService;
	@Autowired
	HubSupplierValueMappingMapper hubSupplierValueMappingService;
	@Autowired
	ProductOriginConstrastMapper originConstrastDAO;

	private static Map<String, String> spBrandMap = new HashMap<>();
	private static Map<String, String> colorContrastMap = new HashMap<>();
	private static Map<String, String> materialContrastMap = new HashMap<>();
	private static Map<String, String> middleMaterialContrastMap = new HashMap<>();
	private static Map<String, String> middleMaterialContrastMap21 = new HashMap<>();
	private static Map<String, String> smallMaterialContrastMap= new HashMap<>();
	private static Map<String, String> smallMaterialContrastMap31= new HashMap<>();
	private static Map<String, String> categoryContrastMap = new HashMap<>();
	private static List<BuEpSpecialDTO> BuEpSpecialList = new ArrayList<BuEpSpecialDTO>();
	private static Map<String, String> hubBrandMap = new HashMap<>();
	
	private static int materialCount = 0;
	private static int madeInCount = 0;


	String splitSign = ",";

	// key 均为小写 以便匹配
	private static Map<String, String> cityMap = new HashMap<String, String>() ;
//			{
//		{
//			put("italia", "意大利");
//			put("stati Uniti d", "美国");
//			put("gran Bretagna", "英国");
//			put("canada", "加拿大");
//			put("brazil", "巴西");
//			put("argentina", "阿根廷");
//			put("mexico", "墨西哥");
//			put("germany", "德国");
//			put("francia", "法国");
//			put("russia", "俄罗斯");
//			put("giappone", "日本");
//			put("australia", "澳大利亚");
//			put("corea", "韩国");
//			put("cina", "中国");
//			put("finland", "芬兰");
//			put("svizzera", "瑞士");
//			put("sweden", "瑞典");
//			put("singapore", "新加坡");
//			put("tailandia", "泰国");
//			put("new zealand", "新西兰");
//			put("ireland", "爱尔兰");
//			put("arabia Saudita", "沙特阿拉伯");
//			put("armenia", "亚美尼亚");
//			put("belgio", "比利时");
//			put("bosnia-Erzegovina", "波斯尼亚-黑塞哥维那");
//			put("bulgaria", "保加利亚");
//			put("cambogia", "柬埔寨");
//			put("croazia", "克罗地亚");
//			put("filippine", "菲律宾");
//			put("georgia", "格鲁吉亚");
//			put("hongkong", "香港");
//			put("india", "印度");
//			put("indonesia", "印度尼西亚");
//			put("kenya", "肯尼亚");
//			put("lituania", "立陶宛");
//			put("madagascar", "马达加斯加");
//			put("marocco", "摩洛哥");
//			put("mauritius", "毛里求斯");
//			put("moldavia", "摩尔多瓦共和国");
//			put("myanmar(Unione)", "缅甸");
//			put("polonia", "波兰");
//			put("portogallo", "葡萄牙");
//			put("romania", "罗马尼亚");
//			put("serbia", "塞尔维亚");
//			put("slovacca", "斯洛伐克");
//			put("spagna", "西班牙");
//			put("sri Lanka", "斯里兰卡");
//			put("tunisia", "突尼斯");
//			put("turchia", "土耳其");
//			put("ucraina", "乌克兰");
//			put("ungheria", "匈牙利");
//			put("vietnam", "越南");
//			put("Made in China".toLowerCase(),"中国");
//			put("Made in Italy".toLowerCase(),"意大利");
//			put("Made in Romania".toLowerCase(),"罗马尼亚");
//			put("Bangladesh".toLowerCase(),"孟加拉国");
//			put("BD".toLowerCase(),"孟加拉国");
//			put("China".toLowerCase(),"中国");
//			put("CN".toLowerCase(),"中国");
//			put("Colombia".toLowerCase(),"哥伦比亚");
//			put("Dominican Republic".toLowerCase(),"多米尼加共和国");
//			put("France".toLowerCase(),"法国");
//			put("Gran Bretagna".toLowerCase(),"法国");
//			put("IN".toLowerCase(),"印度");
//			put("IT".toLowerCase(),"意大利");
//			put("Italy".toLowerCase(),"意大利");
//			put("JAPAN".toLowerCase(),"日本");
//			put("MADE IN BOSNIA E HERZEGOVINA".toLowerCase(),"波黑");
//			put("Made In Brasil".toLowerCase(),"巴西");
//			put("Made In CEE".toLowerCase(),"欧洲经济共同体");
//			put("MADE IN CHINA".toLowerCase(),"中国");
//			put("MADE IN EU".toLowerCase(),"欧盟");
//			put("MADE IN INDIA".toLowerCase(),"印度");
//			put("Made in Indonesia".toLowerCase(),"印度尼西亚");
//			put("made in italy".toLowerCase(),"意大利");
//			put("made in italyLI".toLowerCase(),"意大利");
//			put("made in madagascar".toLowerCase(),"马达加斯加岛");
//			put("Made in Messico".toLowerCase(),"墨西哥");
//			put("MADE IN PORTOGALLO".toLowerCase(),"波托加洛");
//			put("MADE IN PRC".toLowerCase(),"中国");
//			put("Made in Spain".toLowerCase(),"西班牙");
//			put("MADE IN THAILANDIA".toLowerCase(),"泰国");
//			put("MADE IN TURKEY".toLowerCase(),"土耳其");
//			put("MADE IN U.S.A.".toLowerCase(),"美国");
//			put("MADE IN UNITED KINGDOM".toLowerCase(),"英国");
//			put("MADE IN VIETNAM".toLowerCase(),"越南");
//			put("Philippines".toLowerCase(),"菲律宾");
//			put("Portugal".toLowerCase(),"葡萄牙");
//			put("RO".toLowerCase(),"罗马尼亚");
//			put("SPAIN".toLowerCase(),"西班牙");
//			put("Taiwan".toLowerCase(),"台湾");
//			put("TH".toLowerCase(),"泰国");
//			put("Thailand".toLowerCase(),"泰国");
//			put("UNITED KINGDOM".toLowerCase(),"英国");
//			put("US".toLowerCase(),"美国");
//			
//		}
//	};

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
				+ "SupplierSkuNo 供应商sku编号" + splitSign + "尚品sku编号" + splitSign
				+ " 性别 " + splitSign
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
				+ "活动开始时间"+ splitSign + "活动结束时间" + splitSign + "SupplierSpuNo 供应商spu编号" + splitSign + "供应商门户编号"+ splitSign + "SpuId" + splitSign + "备注").append("\r\n");
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
//		this.setBrandMap();
		this.setHubBrandMap(); 		
		// 颜色Map赋值
		this.setColorContrastMap();
		// 材质Map 赋值
		this.setMaterialContrastMap();
		//产地翻译
		this.setMadeInMap();

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
					
					if (hubBrandMap.containsKey(brandName.toLowerCase())) {						
						brandId = hubBrandMap.get(brandName.toLowerCase());						
					} else {
						brandId = "";
					}
				} else {
					brandId = "";
				}

				buffer.append(!"".equals(brandId.split(";")[0]) ? brandId.split(";")[0] : "尚品网品牌编号")
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
				//尚品sku编号
				buffer.append(StringUtils.isNotBlank(dto.getSpSkuId())? dto.getSpSkuId():"").append(splitSign);
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
				buffer.append(transforMaterial(material)).append(splitSign);
				// 获取产地
				productOrigin = dto.getProductOrigin();			

				buffer.append(transforMadeIn(productOrigin)).append(splitSign);

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
//				buffer.append(
//						null == dto.getNewseasonName() ? dto.getNewseasonId() : dto
//								.getNewseasonName()).append(splitSign);
				if(dto.getNewseasonName()!=null){
					if(dto.getNewseasonId()!=null){
						if(dto.getNewseasonName().trim().equals(dto.getNewseasonId().trim())){
							buffer.append(dto.getNewseasonName()).append(splitSign);
						}else{
							buffer.append(dto.getNewseasonId()).append(" ").append(dto.getNewseasonName()).append(splitSign);
						}
					}else{
						buffer.append(dto.getNewseasonName()).append(splitSign);
					}


				}else{
					buffer.append(dto.getNewseasonId()).append(splitSign);
				}

				if(dto.getSeasonName()!=null){
					if(dto.getSeasonId()!=null){
						if(dto.getSeasonName().trim().equals(dto.getSeasonId().trim())){
							buffer.append(dto.getSeasonName()).append(splitSign);
						}else{
							buffer.append(dto.getSeasonId()).append(" ").append(dto.getSeasonName()).append(splitSign);
						}
					}else{
						buffer.append(dto.getSeasonName()).append(splitSign);
					}


				}else{
					buffer.append(dto.getSeasonId()).append(splitSign);
				}

				// 季节

//				buffer.append(
//						null == dto.getSeasonName() ? dto.getSeasonId() : dto
//								.getSeasonName()).append(splitSign);
				// 活动开始时间
				buffer.append(
						null == dto.getEventStartTime() ? " " : dto
								.getEventStartTime()).append(splitSign);
				// 活动结束时间
				buffer.append(null == dto.getEventEndTime() ? " " : dto
						.getEventEndTime()).append(splitSign);
				//供应商spuid
				buffer.append(null == dto.getSpuId() ? " " : dto
						.getSpuId()).append(splitSign);
				//供应商门户编号
				buffer.append(null == dto.getSupplierId() ? " " : dto
						.getSupplierId()).append(splitSign);

				buffer.append(null == dto.getSpuId() ? " " : "SPID"+dto.getSupplierId()+"-"+JavaUtils.getBASE64(dto.getSpuId())+"-"+JavaUtils.getBASE64(dto.getColor())).append(splitSign);

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
	 * 新的设置尚品品牌方法
	 */
	private void setHubBrandMap(){
		try {
			int num = hubSupplierValueMappingService.findCountOfSpvalueType(1);
			if(hubBrandMap.size() != num){		    	
	    		List<HubSupplierValueMappingDTO> list = hubSupplierValueMappingService.findListBySpvalueType(1);
	    		for(HubSupplierValueMappingDTO dto : list){
	    			hubBrandMap.put(dto.getSupplierValue().toLowerCase(), dto.getSpValueNo()+";"+dto.getSpValue());
	    		}
		    }
		} catch (Exception e) {
			e.printStackTrace();
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
		if (materialCount != num) {
			materialCount = num;
			try {
				//先查找1级
				List<MaterialContrastDTO> materialContrastDTOList = materialContrastDAO.findByRank(1);
				if(materialContrastDTOList != null && materialContrastDTOList.size()>0){
					for (MaterialContrastDTO dto : materialContrastDTOList) {					
						materialContrastMap.put(dto.getMaterial().trim().toLowerCase(),
								dto.getMaterialCh());
					}
				}
				//再查找2级中的长词组
				List<MaterialContrastDTO> materialContrastDTOList21 = materialContrastDAO.findByRank(21);
				if(materialContrastDTOList21 != null && materialContrastDTOList21.size()>0){
					for (MaterialContrastDTO dto : materialContrastDTOList21) {					
						middleMaterialContrastMap21.put(dto.getMaterial().trim().toLowerCase(),
								dto.getMaterialCh());
					}
				}
				//再查找2级
				List<MaterialContrastDTO> materialContrastDTOList2 = materialContrastDAO.findByRank(2);
				if(materialContrastDTOList2 != null && materialContrastDTOList2.size()>0){
					for (MaterialContrastDTO dto : materialContrastDTOList2) {					
						middleMaterialContrastMap.put(dto.getMaterial().trim().toLowerCase(),
								dto.getMaterialCh());
					}
				}
				//再查找3级中的长单词
				List<MaterialContrastDTO> materialContrastDTOList31 = materialContrastDAO.findByRank(31);
				if(materialContrastDTOList31 != null && materialContrastDTOList31.size()>0){
					for (MaterialContrastDTO dto : materialContrastDTOList31) {					
						smallMaterialContrastMap31.put(dto.getMaterial().trim().toLowerCase(),
								dto.getMaterialCh());
					}
				}
				//再查找3级
				List<MaterialContrastDTO> materialContrastDTOList3 = materialContrastDAO.findByRank(3);
				if(materialContrastDTOList3 != null && materialContrastDTOList3.size()>0){
					for (MaterialContrastDTO dto : materialContrastDTOList3) {					
						smallMaterialContrastMap.put(dto.getMaterial().trim().toLowerCase(),
								dto.getMaterialCh());
					}
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}
	
	/**
	 * 翻译材质
	 * @param materialOrg
	 * @return
	 */
	private String transforMaterial(String materialOrg){
		try {
			
			if(StringUtils.isBlank(materialOrg)){
				return "";
			}
			if(materialContrastMap.containsKey(materialOrg.toLowerCase())){
				return materialContrastMap.get(materialOrg.toLowerCase());
				
			}else{
				if(middleMaterialContrastMap21.size()>0){
					//先遍历2级中的长词组
					Set<Map.Entry<String, String>> materialSet = middleMaterialContrastMap21
							.entrySet();
					for (Map.Entry<String, String> entry : materialSet) {

						materialOrg = materialOrg.toLowerCase().replaceAll(
								entry.getKey(), entry.getValue());
					}
				}
				if(middleMaterialContrastMap.size()>0){
					//再遍历2级词组
					Set<Map.Entry<String, String>> materialSet = middleMaterialContrastMap
							.entrySet();
					for (Map.Entry<String, String> entry : materialSet) {

						materialOrg = materialOrg.toLowerCase().replaceAll(
								entry.getKey(), entry.getValue());
					}
				}
				if(smallMaterialContrastMap31.size()>0){
					//再遍历3级单词中的长单词
					Set<Map.Entry<String, String>> smallMaterialSet = smallMaterialContrastMap31
							.entrySet();
					for (Map.Entry<String, String> entry : smallMaterialSet) {

						materialOrg = materialOrg.toLowerCase()
								.replaceAll(entry.getKey(),
										entry.getValue());
					}
				}
				if(smallMaterialContrastMap.size()>0){
					//再遍历单个材质
					Set<Map.Entry<String, String>> smallMaterialSet = smallMaterialContrastMap
							.entrySet();
					for (Map.Entry<String, String> entry : smallMaterialSet) {

						materialOrg = materialOrg.toLowerCase()
								.replaceAll(entry.getKey(),
										entry.getValue());
					}
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return materialOrg;
	}
	
	private void setMadeInMap(){
		try {
			int num = originConstrastDAO.findCount();
			if(madeInCount != num){
				madeInCount = num;
				List<ProductOriginConstrastDTO> lists = originConstrastDAO.findByRank(1);
				if(null != lists && lists.size()>0){
					for(ProductOriginConstrastDTO product : lists){
						cityMap.put(product.getProductOrigin().trim().toLowerCase(), product.getProductOriginCH().trim());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private String transforMadeIn(String madeinOrg){
		if (StringUtils.isNotBlank(madeinOrg)) {
			if (cityMap.containsKey(madeinOrg.trim().toLowerCase())) {
				return cityMap.get(madeinOrg.trim().toLowerCase());
			}else{
				return madeinOrg.trim();
			}
		} else {
			return "";
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
		List<ProductDTO> toupdateProducts = new ArrayList<ProductDTO>();
//		Map<String, String> supMap = new HashMap<String, String>();
		List<SupplierDTO> supplierList = supplierDAO.findByState("1");
//		for (SupplierDTO supplierDTO : supplierList) {
//			supMap.put(supplierDTO.getSupplierId(),
//					supplierDTO.getSupplierName());
//		}
		System.out.println("supplierList.size===="+supplierList.size());
		StringBuffer buffer = new StringBuffer("供应商" + splitSign
				+ "ProductModel 货号" + splitSign + "供货商skuid" + splitSign+ "新上市季节" + splitSign+ "上市季节").append("\r\n");
		for(SupplierDTO supplier : supplierList){
			if(StringUtils.isNotBlank(supplier.getSupplierId())){
				final List<ProductDTO> products = productDAO.findDiffSeasonProducts(supplier.getSupplierId(),startDate,endDate);
				System.out.println("products.size====="+products.size());
				if(null == products || products.size()==0){
//					return null;
				}else{
					for(ProductDTO dto : products){
						toupdateProducts.add(dto);
						// 供应商
//						String supplierId = dto.getSupplierId();
//						if (null == supMap.get(supplierId)) {
//							buffer.append("	" + supplierId).append(splitSign);
//						} else {
//							buffer.append(supMap.get(supplierId)).append(splitSign);
//						}
						buffer.append(StringUtils.isNotBlank(supplier.getSupplierName())? supplier.getSupplierName():supplier.getSupplierId()).append(splitSign);
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

		System.out.println("toupdateProducts.size======="+toupdateProducts.size());
		if(toupdateProducts.size()> 0){
			try {

				for(ProductDTO dto :toupdateProducts){
					try {
						SpuDTO spu = new SpuDTO();
						spu.setSupplierId(dto.getSupplierId());
						spu.setSpuId(dto.getSpuId());
						spu.setSeasonName(dto.getNewseasonName());
						spu.setSeasonId(dto.getNewseasonId());
						spuDAO.updateSeason(spu);
						System.out.println(spu.getSupplierId()+"----"+spu.getSpuId()+"更新成功");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return buffer;
	}

	public StringBuffer getDiffProduct(Date startDate,Date endDate,Integer pageIndex,Integer pageSize,String flag) throws ServiceException{
//		Map<String, String> supMap = new HashMap<String, String>();
		StringBuffer buffer = new StringBuffer("供应商" + splitSign
				+ "ProductModel 货号" + splitSign + "供货商skuid" + splitSign+ "新市场价" + splitSign
				+ "新销售价" + splitSign + "新进货价" + splitSign + "市场价"
				+ splitSign + "销售价" + splitSign + "进货价").append("\r\n");

		List<ProductDTO> toUpdateProducts = new ArrayList<ProductDTO>();

		try {

			List<SupplierDTO> supplierList = supplierDAO.findByState("1");
			for(SupplierDTO supplierDTO : supplierList){
				try {

					List<ProductDTO> products = productDAO.findDiffPriceProducts(supplierDTO.getSupplierId(), startDate, endDate);

					for (ProductDTO dto : products) {
						try {
							toUpdateProducts.add(dto);
							// 供应商
							buffer.append(supplierDTO.getSupplierName()).append(splitSign);
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
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		//更新价格

		try {
			for (ProductDTO dto : toUpdateProducts) {
				try{

					SkuDTO skuDTO = new SkuDTO();
					skuDTO.setUpdateTime(new Date());
					skuDTO.setSupplierId(dto.getSupplierId());
					skuDTO.setSkuId(dto.getSkuId());
					if(StringUtils.isNotBlank(dto.getNewMarketPrice()) && !dto.getNewMarketPrice().equals(dto.getMarketPrice())){
						skuDTO.setMarketPrice(dto.getNewMarketPrice());
					}
					if(StringUtils.isNotBlank(dto.getNewSalePrice()) && !dto.getNewSalePrice().equals(dto.getSalePrice())){
						skuDTO.setSalePrice(dto.getNewSalePrice());
					}
					if(StringUtils.isNotBlank(dto.getNewSupplierPrice()) && !dto.getNewSupplierPrice().equals(dto.getSupplierPrice())){
						skuDTO.setSupplierPrice(dto.getNewSupplierPrice());
					}
					skuDAO.updatePrice(skuDTO);

				}catch(Exception ex){
					ex.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

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

	public List<ProductDTO> findReportProduct(String supplier,Date startDate,Date endDate,Integer pageIndex,Integer pageSize) throws ServiceException{

		List<ProductDTO> products = new ArrayList<ProductDTO>();

		Page<ProductDTO> page = this.findProductPageBySupplierAndTime(supplier, startDate,
				endDate, pageIndex, pageSize, "same");
		//品牌
//		List<String> brandList = new ArrayList<String>();
//		for(String brand:ePRuleDAO.findAll(2, 1)){
//			brandList.add(brand.toUpperCase());
//		}
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
		//需要的季节
		List<String> supplierSeasonList = new ArrayList<String>();
		List<SeasonRelationDTO> currentSeasonList  =   seasonRelationService.getAllCurrentSeason();
		for(SeasonRelationDTO dto:currentSeasonList){
			supplierSeasonList.add(dto.getSupplierId()+"-"+(null==dto.getSupplierSeason()?"":dto.getSupplierSeason()));
		}
		//品类map赋值
		this.setCategoryMap();
		// 设置尚品网品牌
//		this.setBrandMap();
		this.setHubBrandMap();
		// 颜色Map赋值
		this.setColorContrastMap();
		// 材质Map 赋值
		this.setMaterialContrastMap();
		//产地翻译
		this.setMadeInMap();
		for (ProductDTO dto : page.getItems()) {
			try {
				if(supplierSeasonList.contains(dto.getSupplierId()+"-"+(null==dto.getSeasonId()?"":dto.getSeasonId()))){

				}else if(supplierSeasonList.contains(dto.getSupplierId()+"-"+(null==dto.getSeasonName()?"":dto.getSeasonName()))){

				}else{
					continue;
				}
				if(StringUtils.isBlank(dto.getSpSkuId()) && StringUtils.isNotBlank(dto.getColor()) && StringUtils.isNotBlank(dto.getSize()) && StringUtils.isNotBlank(dto.getMaterial()) && (StringUtils.isNotBlank(dto.getPicUrl()) || StringUtils.isNotBlank(dto.getItemPictureUrl1()))){
					if(StringUtils.isNotBlank(dto.getCategoryGender()) && !genderList.contains(dto.getCategoryGender().toUpperCase())){
						if((StringUtils.isNotBlank(dto.getSeasonId()) && !seasonList.contains(dto.getSeasonId().toUpperCase())) || (StringUtils.isNotBlank(dto.getSeasonName()) && !seasonList.contains(dto.getSeasonName().toUpperCase()))){
							if((StringUtils.isNotBlank(dto.getCategoryName()) && !categeryList.contains(dto.getCategoryName().toUpperCase())) || (StringUtils.isNotBlank(dto.getSubCategoryName()) && !categeryList.contains(dto.getSubCategoryName().toUpperCase()))){
								if(null != dto.getBrandName() && (hubBrandMap.containsKey(dto.getBrandName().toLowerCase()) || dto.getBrandName().equals("Chloé") || dto.getBrandName().equals("Chloe'"))){
									products.add(dto);
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

		return products;
	}

	public HSSFWorkbook reportProductToExcel(List<ProductDTO> products,String picPath) throws ServiceException{
		//第一步创建workbook
		HSSFWorkbook wb = new HSSFWorkbook();
		//第二步创建sheet
		HSSFSheet sheet = wb.createSheet("产品信息");
		//第三步创建行row:添加表头0行
		HSSFRow row = sheet.createRow(0);
		HSSFCellStyle  style = wb.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);  //居中
		//第四步创建单元格
		String[] row0 = {"图片","SupplierId 供货商名称","CategoryName 品类名称","Category 品类翻译","Category_No 品类编号","BrandNo 品牌编号","BrandName 品牌","ProductModel 货号",
				"SupplierSkuNo 供应商SkuNo","尚品sku编号"," 性别 ","SopProductName 商品名称","BarCode 条形码","ProductColor 颜色","color 中文","ProductSize 尺码","material 材质",
				"material 中文材质","ProductOrigin 产地","productUrl1","productUrl2","productUrl3","productUrl4","productUrl5","productUrl6","productUrl7","productUrl8","productUrl9",
				"PcDesc 描述","Stock 库存","新市场价","新销售价","新进货价","markerPrice","sallPrice","supplier Price 进货价","Currency 币种","新上市季节","上市季节","活动开始时间",
				"活动结束时间","SupplierSpuNo 供应商spu编号","供应商门户编号","SpuId","备注"};
		for(int i= 0;i<row0.length;i++){
			HSSFCell cell = row.createCell(i);         //第一个单元格
			cell.setCellValue(row0[i]);                  //设定值
			cell.setCellStyle(style);
		}
		if(null != products && products.size()>0){
			String productSize, productDetail = "", brandName = "", brandId = "", color = "", material = "", productOrigin = "";
			String supplierId="", categoryName = "", productName = "";
			Map<String,String> allMap = new HashMap<String,String>();
			BufferedImage bufferImg = null;
			int j = 0;
			for(ProductDTO dto : products){
				try {
					if(allMap.containsKey(dto.getSpuId()+dto.getColor())){
						continue;
					}
					j++;
					row = sheet.createRow(j);
					row.setHeight((short) 1500);
					sheet.setColumnWidth((short)0, (short)  (36*150));
					//第一个单元格：图片
					String fileName = picPath+File.separator+dto.getSupplierId()+File.separator+DateTimeUtil.convertFormat(dto.getLastTime(),"yyyy-MM-dd")+ File.separator +"SPID"+dto.getSupplierId()+"-"+JavaUtils.getBASE64(dto.getSpuId())+"-"+JavaUtils.getBASE64(dto.getColor())+" (1).jpg";
//					System.out.println(fileName);
					File file = new File(fileName);
					if(file.exists()){
						try {
							bufferImg = ImageIO.read(file);
							ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();							
							ImageIO.write(bufferImg, "jpg", byteArrayOut);
							//画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
							HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
							//anchor主要用于设置图片的属性
							HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 500, 255,(short) 0, j, (short) 0, j);
							anchor.setAnchorType(AnchorType.MOVE_AND_RESIZE);
							//插入图片
							patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
						} catch (Exception e) {
							e.printStackTrace();
							bufferImg = null;
							row.createCell(0).setCellValue("图片错误");
							
						}
						
					}else{
						row.createCell(0).setCellValue("无图片");
					}

					//supplierId 供货商
					supplierId = dto.getSupplierName();
					if(StringUtils.isNotBlank(supplierId)){
						row.createCell(1).setCellValue(supplierId);
					}else{
						row.createCell(1).setCellValue(dto.getSupplierId());
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
					row.createCell(2).setCellValue(categoryName);

					//品类翻译
					row.createCell(3).setCellValue(categoryCH);

					row.createCell(4).setCellValue("尚品网品类编号");

					brandName = dto.getBrandName();
					if (StringUtils.isNotBlank(brandName)) {
						
						if (hubBrandMap.containsKey(brandName.toLowerCase())) {						
							brandId = hubBrandMap.get(brandName.toLowerCase());						
						} else {
							brandId = "";
						}
					} else {
						brandId = "";
					}
					row.createCell(5).setCellValue(!"".equals(brandId.split(";")[0]) ? brandId.split(";")[0] : "尚品网品牌编号");
					row.createCell(6).setCellValue(brandName);
					// 货号
					row.createCell(7).setCellValue(
							null == dto.getProductCode() ? "" : dto
									.getProductCode().replaceAll(",", " "));
					// 供应商SKUID

					row.createCell(8).setCellValue("\"\t" + dto.getSkuId() + "\"");
					//尚品sku编号
					row.createCell(9).setCellValue(StringUtils.isNotBlank(dto.getSpSkuId())? dto.getSpSkuId():"");

					// 欧洲习惯 第一个先看 男女
					row.createCell(10).setCellValue(
							null == dto.getCategoryGender() ? "" : dto
									.getCategoryGender().replaceAll(splitSign, " "));
					// 产品名称
					productName = dto.getProductName();
					if (StringUtils.isBlank(productName)) {
						productName = dto.getSpuName();
					}

					if (StringUtils.isNotBlank(productName)) {

						productName = productName.replaceAll(splitSign, " ")
								.replaceAll("\\r", "").replaceAll("\\n", "");
					}

					row.createCell(11).setCellValue(productName);

					row.createCell(12).setCellValue("\"\t" + dto.getBarcode() + "\"");

					// 获取颜色
					color = dto.getColor()==null?"":dto.getColor().replace(",", " ").replaceAll("/", " ");
					row.createCell(13).setCellValue(null == color ? "" : color.replace(",", " "));
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
					row.createCell(14).setCellValue(colorCh);

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
					row.createCell(15).setCellValue(productSize);

					// 获取材质
					material = dto.getMaterial();
					if (StringUtils.isBlank(material)) {
						material = "";
					} else {

						material = material.replaceAll(splitSign, " ")
								.replaceAll("\\r", "").replaceAll("\\n", "");
					}

					row.createCell(16).setCellValue(material);
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

					row.createCell(17).setCellValue(material);
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

					row.createCell(18).setCellValue(productOrigin);

					// 图片
					row.createCell(19).setCellValue(dto.getPicUrl());
					row.createCell(20).setCellValue(dto.getItemPictureUrl1());
					row.createCell(21).setCellValue(dto.getItemPictureUrl2());
					row.createCell(22).setCellValue(dto.getItemPictureUrl3());
					row.createCell(23).setCellValue(dto.getItemPictureUrl4());
					row.createCell(24).setCellValue(dto.getItemPictureUrl5());
					row.createCell(25).setCellValue(dto.getItemPictureUrl6());
					row.createCell(26).setCellValue(dto.getItemPictureUrl7());
					row.createCell(27).setCellValue(dto.getItemPictureUrl8());
					// 明细描述
					productDetail = dto.getProductDescription();
					if (StringUtils.isNotBlank(productDetail)) {
						productDetail = productDetail.replaceAll(splitSign, "  ");
						productDetail = productDetail.replaceAll("\\r", "")
								.replaceAll("\\n", "");
					}

					row.createCell(28).setCellValue(productDetail);

					row.createCell(29).setCellValue(dto.getStock());
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
					row.createCell(30).setCellValue(newMarketPrice);
					row.createCell(31).setCellValue(newSalePrice);
					row.createCell(32).setCellValue(newSupplierPrice);
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
					row.createCell(33).setCellValue(marketPrice);
					row.createCell(34).setCellValue(salePrice);
					row.createCell(35).setCellValue(supplierPrice);
					row.createCell(36).setCellValue(dto.getSaleCurrency());
					//新季节
					row.createCell(37).setCellValue(
							null == dto.getNewseasonName() ? dto.getNewseasonId() : dto
									.getNewseasonName());
					// 季节
					row.createCell(38).setCellValue(
							null == dto.getSeasonName() ? dto.getSeasonId() : dto
									.getSeasonName());
					// 活动开始时间
					row.createCell(39).setCellValue(
							null == dto.getEventStartTime() ? " " : DateTimeUtil.convertFormat(dto.getEventStartTime(), "yyyy-MM-dd HH:mm:ss"));
					// 活动结束时间
					row.createCell(40).setCellValue(null == dto.getEventEndTime() ? " " : DateTimeUtil.convertFormat(dto.getEventEndTime(),"yyyy-MM-dd HH:mm:ss"));
					//供应商spuid
					row.createCell(41).setCellValue(null == dto.getSpuId() ? " " : dto
							.getSpuId());
					//供应商门户编号
					row.createCell(42).setCellValue(null == dto.getSupplierId() ? " " : dto
							.getSupplierId());
					//spuid
					row.createCell(43).setCellValue(null == dto.getSpuId() ? " " : "SPID"+dto.getSupplierId()+"-"+JavaUtils.getBASE64(dto.getSpuId())+"-"+JavaUtils.getBASE64(dto.getColor()));
					row.createCell(44).setCellValue(dto.getMemo());

					allMap.put(dto.getSpuId()+dto.getColor(), null);
				} catch (Exception e) {
					j--;
					e.printStackTrace();
					logger.debug(dto.getSkuId() + "拉取失败" + e.getMessage());
					continue;
				}
			}
		}

		return wb;

	}

	public StringBuffer reportProductToBuffer(List<ProductDTO> products) throws ServiceException {

		StringBuffer buffer = new StringBuffer("SupplierId 供货商名称" + splitSign
				+ "CategoryName 品类名称" + splitSign
				+ "Category 品类翻译" + splitSign
				+ "Category_No 品类编号" + splitSign + "BrandNo 品牌编号" + splitSign
				+ "BrandName 品牌" + splitSign + "ProductModel 货号" + splitSign
				+ "SupplierSkuNo 供应商SkuNo" + splitSign + " 尚品sku编号 " + splitSign
				+ " 性别 " + splitSign
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
				+ splitSign + "活动结束时间"+ splitSign + "SupplierSpuNo 供应商spu编号" + splitSign +"供应商门户编号"+ splitSign + "SpuId" + splitSign + "备注").append("\r\n");

		if(null != products && products.size()>0){
			String productSize, productDetail = "", brandName = "", brandId = "", color = "", material = "", productOrigin = "";
			String supplierId="", categoryName = "", productName = "";
			for(ProductDTO dto : products){
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
						
						if (hubBrandMap.containsKey(brandName.toLowerCase())) {						
							brandId = hubBrandMap.get(brandName.toLowerCase());						
						} else {
							brandId = "";
						}
					} else {
						brandId = "";
					}

					buffer.append(!"".equals(brandId.split(";")[0]) ? brandId.split(";")[0] : "尚品网品牌编号")
							.append(splitSign);
					buffer.append(brandName).append(splitSign);
					// 货号
					buffer.append(
							null == dto.getProductCode() ? "" : dto
									.getProductCode().replaceAll(",", " ")).append(
							splitSign);
					// 供应商SKUID

					buffer.append("\"\t" + dto.getSkuId() + "\"").append(splitSign);
					//尚品sku编号
					buffer.append(StringUtils.isNotBlank(dto.getSpSkuId())? dto.getSpSkuId():"").append(splitSign);

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
					buffer.append(transforMaterial(material)).append(splitSign);
					// 获取产地
					productOrigin = dto.getProductOrigin();					
					buffer.append(transforMadeIn(productOrigin)).append(splitSign);

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
							.getEventEndTime()).append(splitSign);
					//供应商spuid
					buffer.append(null == dto.getSpuId() ? " " : dto
							.getSpuId()).append(splitSign);
					//供应商门户编号
					buffer.append(null == dto.getSupplierId() ? " " : dto
							.getSupplierId()).append(splitSign);
					//spuid
					buffer.append(null == dto.getSpuId() ? " " : "SPID"+dto.getSupplierId()+"-"+JavaUtils.getBASE64(dto.getSpuId())+"-"+JavaUtils.getBASE64(dto.getColor())).append(splitSign);
					buffer.append(dto.getMemo());
					buffer.append("\r\n");

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return buffer;

	}

	public StringBuffer exportReportProduct(String supplier,Date startDate,Date endDate,Integer pageIndex,Integer pageSize) throws ServiceException{

		List<ProductDTO> products = findReportProduct(supplier, startDate, endDate, pageIndex, pageSize);

		return reportProductToBuffer(products);
	}

	public StringBuffer exportProductByEpRule(String supplier,Date startDate,Date endDate,Integer pageIndex,Integer pageSize) throws ServiceException{

		StringBuffer buffer = new StringBuffer("SupplierId 供货商名称" + splitSign
				+ "CategoryName 品类名称" + splitSign
				+ "Category 品类翻译" + splitSign
				+ "Category_No 品类编号" + splitSign + "BrandNo 品牌编号" + splitSign
				+ "BrandName 品牌" + splitSign + "ProductModel 货号" + splitSign
				+ "SupplierSkuNo 供应商SkuNo" + splitSign + " 尚品sku编号 " + splitSign
				+ " 性别 " + splitSign
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
				+ splitSign + "活动结束时间"+ splitSign + "供应商门户编号"+ splitSign + "SupplierSpuNo 供应商spu编号" + splitSign + "SpuId" + splitSign + "备注").append("\r\n");
		Page<ProductDTO> page = this.findProductPageBySupplierAndTime(supplier, startDate,
				endDate, pageIndex, pageSize, "same");
		//品牌
//		List<String> brandList = new ArrayList<String>();
//		for(String brand:ePRuleDAO.findAll(2, 1)){
//			brandList.add(brand.toUpperCase());
//		}
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
//		this.setBrandMap();
		this.setHubBrandMap(); 		
		// 颜色Map赋值
		this.setColorContrastMap();
		// 材质Map 赋值
		this.setMaterialContrastMap();
		//产地翻译
		this.setMadeInMap();

		String productSize, productDetail = "", brandName = "", brandId = "", color = "", material = "", productOrigin = "";

		String supplierId="", categoryName = "", productName = "";
		for (ProductDTO dto : page.getItems()) {
			try {

				if(StringUtils.isBlank(dto.getSpSkuId()) && StringUtils.isNotBlank(dto.getColor()) && StringUtils.isNotBlank(dto.getSize()) && StringUtils.isNotBlank(dto.getMaterial()) && (StringUtils.isNotBlank(dto.getPicUrl()) || StringUtils.isNotBlank(dto.getItemPictureUrl1()))){
					if(StringUtils.isNotBlank(dto.getCategoryGender()) && !genderList.contains(dto.getCategoryGender().toUpperCase())){
						if((StringUtils.isNotBlank(dto.getSeasonId()) && !seasonList.contains(dto.getSeasonId().toUpperCase())) || (StringUtils.isNotBlank(dto.getSeasonName()) && !seasonList.contains(dto.getSeasonName().toUpperCase()))){
							if((StringUtils.isNotBlank(dto.getCategoryName()) && !categeryList.contains(dto.getCategoryName().toUpperCase())) || (StringUtils.isNotBlank(dto.getSubCategoryName()) && !categeryList.contains(dto.getSubCategoryName().toUpperCase()))){
								if(null != dto.getBrandName() && (hubBrandMap.containsKey(dto.getBrandName().toLowerCase()) || dto.getBrandName().equals("Chloé") || dto.getBrandName().equals("Chloe'"))){
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
											
											if (hubBrandMap.containsKey(brandName.toLowerCase())) {						
												brandId = hubBrandMap.get(brandName.toLowerCase());						
											} else {
												brandId = "";
											}
										} else {
											brandId = "";
										}

										buffer.append(!"".equals(brandId.split(";")[0]) ? brandId.split(";")[0] : "尚品网品牌编号")
												.append(splitSign);
										buffer.append(brandName).append(splitSign);
										// 货号
										buffer.append(
												null == dto.getProductCode() ? "" : dto
														.getProductCode().replaceAll(",", " ")).append(
												splitSign);
										// 供应商SKUID

										buffer.append("\"\t" + dto.getSkuId() + "\"").append(splitSign);
										//尚品sku编号
										buffer.append(StringUtils.isNotBlank(dto.getSpSkuId())? dto.getSpSkuId():"").append(splitSign);

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
										buffer.append(transforMaterial(material)).append(splitSign);
										// 获取产地
										productOrigin = dto.getProductOrigin();										
										buffer.append(transforMadeIn(productOrigin)).append(splitSign);

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
												.getEventEndTime()).append(splitSign);
										//供应商spuid
										buffer.append(null == dto.getSpuId() ? " " : dto
												.getSpuId()).append(splitSign);
										//供应商门户编号
										buffer.append(null == dto.getSupplierId() ? " " : dto
												.getSupplierId()).append(splitSign);
										buffer.append(null == dto.getSpuId() ? " " : "SPID"+dto.getSupplierId()+"-"+JavaUtils.getBASE64(dto.getSpuId())+"-"+JavaUtils.getBASE64(dto.getColor())).append(splitSign);
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
		List<ProductDTO> pList = new ArrayList<>();
//		if (null != pageIndex && null != pageSize) {
//			pList = productDAO.findPicNameListByEPRegularAndLastDate(supplier, startDate, endDate, new RowBounds(pageIndex, pageSize));
//		}else{
//			pList = productDAO.findPicNameListByEPRegularAndLastDate(supplier, startDate, endDate);
//		}
		Page<ProductDTO> page = null;
		try {
			page = this.findProductPageBySupplierAndTime(supplier, startDate,
					endDate, pageIndex, pageSize, "same");
		} catch (ServiceException e) {
			e.printStackTrace();
		}
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



		for (ProductDTO dto : page.getItems()) {
			try {

				if(StringUtils.isBlank(dto.getSpSkuId()) && StringUtils.isNotBlank(dto.getColor()) && StringUtils.isNotBlank(dto.getSize()) && StringUtils.isNotBlank(dto.getMaterial()) && StringUtils.isNotBlank(dto.getItemPictureUrl1())){
					if(StringUtils.isNotBlank(dto.getCategoryGender()) && !genderList.contains(dto.getCategoryGender().toUpperCase())){
						if((StringUtils.isNotBlank(dto.getSeasonId()) && !seasonList.contains(dto.getSeasonId().toUpperCase())) || (StringUtils.isNotBlank(dto.getSeasonName()) && !seasonList.contains(dto.getSeasonName().toUpperCase()))){
							if((StringUtils.isNotBlank(dto.getCategoryName()) && !categeryList.contains(dto.getCategoryName().toUpperCase())) || (StringUtils.isNotBlank(dto.getSubCategoryName()) && !categeryList.contains(dto.getSubCategoryName().toUpperCase()))){
								if(null != dto.getBrandName() && (brandList.contains(dto.getBrandName().toUpperCase()) || dto.getBrandName().equals("Chloé") || dto.getBrandName().equals("Chloe'"))){
									try {
										pList.add(dto);
									} catch (Exception e) {
										logger.debug(dto.getSkuId() + "拉取失败" + e.getMessage());
										continue;
									}
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
		return pList;
	}

	/**
	 * BU根据新的ep规则导出产品
	 * @param bu 选择哪个bu
	 * @param supplier
	 * @param startDate
	 * @param endDate
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	@Override
	public StringBuffer buExportProduct(String bu,String supplier, Date startDate,
										Date endDate, Integer pageIndex, Integer pageSize) throws ServiceException{

		StringBuffer buffer = new StringBuffer("SupplierId 供货商名称" + splitSign
				+ "CategoryName 品类名称" + splitSign
				+ "Category 品类翻译" + splitSign
				+ "Category_No 品类编号" + splitSign + "BrandNo 品牌编号" + splitSign
				+ "BrandName 品牌" + splitSign + "ProductModel 货号" + splitSign
				+ "SupplierSkuNo 供应商sku编号" + splitSign + "尚品sku编号" + splitSign
				+ " 性别 " + splitSign
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

		if(StringUtils.isBlank(supplier) || "-1".equals(supplier)){
			//查出该bu所有的供应商
			List<String> suppliers = BuEpRuleDAO.findAllSuppliersByBu(bu);
			if(null != suppliers && suppliers.size()>0){
				for(String supplierId : suppliers){
					buExportProductBySupplierId(bu, supplierId, startDate, endDate,
							pageIndex, pageSize, buffer);
				}
			}

		}else{
			buExportProductBySupplierId(bu, supplier, startDate, endDate,
					pageIndex, pageSize, buffer);
		}


		return buffer;
	}



	/**
	 * BU根据新的ep规则导出产品
	 * @param bu 选择哪个bu
	 * @param supplier
	 * @param startDate
	 * @param endDate
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */

	public StringBuffer shoeExportProduct(String bu,String supplier, Date startDate,
										  Date endDate, Integer pageIndex, Integer pageSize) throws ServiceException{

		StringBuffer buffer = new StringBuffer("SupplierId 供货商名称" + splitSign
				+ "CategoryName 品类名称" + splitSign
				+ "Category 品类翻译" + splitSign
				+ "Category_No 品类编号" + splitSign + "BrandNo 品牌编号" + splitSign
				+ "BrandName 品牌" + splitSign + "ProductModel 货号" + splitSign
				+ "SupplierSkuNo 供应商sku编号" + splitSign + "尚品sku编号" + splitSign
				+ " 性别 " + splitSign
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

		List<String> suppliers = new ArrayList<String>();
		if(StringUtils.isBlank(supplier) || "-1".equals(supplier)){
			List<SupplierDTO> supplierList = supplierDAO.findByState("1");
			for(SupplierDTO supplierDTO : supplierList){
				suppliers.add(supplierDTO.getSupplierId());
			}
		}else{
			logger.error("所选供应商为==============="+supplier);
			suppliers.add(supplier);
		}
		logger.error("suppliers的大小是================="+suppliers.size());
		List<BuEpRuleDTO> bus = BuEpRuleDAO.findBuEpRuleListBySupplierId(bu, null);
		logger.error("bus的大小是=========="+bus.size());
		List<ProductDTO> productList = null;
		List<String> categories = null;
		List<String> brands = null;
		List<String> genders = null;
		String isExport_cat = null;
		String isExport_brand = null;
		String isExport_gender = null;
		String ispecial = "0";
		if(null != bus && bus.size()>0){
			for(BuEpRuleDTO buEp : bus){
				if("1" == buEp.getIspecial()){
					ispecial = buEp.getIspecial();
				}
				if("CATEGORY".equals(buEp.getEpField().toUpperCase())){
					isExport_cat = buEp.getIsexport();
					categories = BuEpValueDAO.findValueByBuIdAndIsExport(buEp.getBuId(), isExport_cat);
				}else if("BRAND".equals(buEp.getEpField().toUpperCase())){
					isExport_brand = buEp.getIsexport();
					brands = BuEpValueDAO.findValueByBuIdAndIsExport(buEp.getBuId(), isExport_brand);
				}else if("GENDER".equals(buEp.getEpField().toUpperCase())){
					isExport_gender = buEp.getIsexport();
					genders = BuEpValueDAO.findValueByBuIdAndIsExport(buEp.getBuId(), isExport_gender);
				}
			}
			for(String supplierId : suppliers){
				productList = this.findListOfProducts(bu, categories, brands, genders, supplierId, startDate, endDate, pageIndex, pageSize,isExport_cat,isExport_brand,isExport_gender);
				logger.error("productList的大小是================="+productList.size());
				if(null != productList && productList.size()>0){
					//品类map赋值
					this.setCategoryMap();
					// 设置尚品网品牌
					this.setBrandMap();
					// 颜色Map赋值
					this.setColorContrastMap();
					// 材质Map 赋值
					this.setMaterialContrastMap();
					//处理
					addStringBuffer(buffer,productList);
				}
			}
		}
		return buffer;
	}

	/**
	 * 根据bu和供应商编号查找产品
	 * @param bu
	 * @param supplier
	 * @param startDate
	 * @param endDate
	 * @param pageIndex
	 * @param pageSize
	 * @param buffer
	 * @throws ServiceException
	 */
	private void buExportProductBySupplierId(String bu, String supplier,
											 Date startDate, Date endDate, Integer pageIndex, Integer pageSize,
											 StringBuffer buffer) throws ServiceException {

		List<BuEpRuleDTO> bus = BuEpRuleDAO.findBuEpRuleListBySupplierId(bu, supplier);

		List<ProductDTO> productList = null;
		List<String> categories = null;
		List<String> brands = null;
		List<String> genders = null;
		String isExport_cat = null;
		String isExport_brand = null;
		String isExport_gender = null;
		String ispecial = "0";
		if(null != bus && bus.size()>0){
			for(BuEpRuleDTO buEp : bus){
				if("1" == buEp.getIspecial()){
					ispecial = "1";
				}
				if("CATEGORY".equals(buEp.getEpField().toUpperCase())){
					isExport_cat = buEp.getIsexport();
					categories = BuEpValueDAO.findValueByBuIdAndIsExport(buEp.getBuId(), isExport_cat);
				}else if("BRAND".equals(buEp.getEpField().toUpperCase())){
					isExport_brand = buEp.getIsexport();
					brands = BuEpValueDAO.findValueByBuIdAndIsExport(buEp.getBuId(), isExport_brand);
				}else if("GENDER".equals(buEp.getEpField().toUpperCase())){
					isExport_gender = buEp.getIsexport();
					genders = BuEpValueDAO.findValueByBuIdAndIsExport(buEp.getBuId(), isExport_gender);
				}
			}

			productList = this.findListOfProducts(bu, categories, brands, genders, supplier, startDate, endDate, pageIndex, pageSize,isExport_cat,isExport_brand,isExport_gender);

			if(null != productList && productList.size()>0){
				//TODO 需要特殊处理的
				if("1".equals(ispecial)){
					this.screenProducts(productList);
				}
//				if("1".equals(buEp.getIspecial()) && StringUtils.isNotBlank(buEp.getSpeClass())){
//					try {
//						Special Special = (Special)Class.forName(buEp.getSpeClass()).newInstance();
//						Special.screen(productList);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}
				//品类map赋值
				this.setCategoryMap();
				// 设置尚品网品牌
				this.setBrandMap();
				// 颜色Map赋值
				this.setColorContrastMap();
				// 材质Map 赋值
				this.setMaterialContrastMap();
				//处理
				addStringBuffer(buffer,productList);
			}

		}
	}

	/**
	 * 根据指定条件查询产品列表
	 * @param categories
	 * @param brands
	 * @param supplier
	 * @param startDate
	 * @param endDate
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 * @throws ServiceException
	 */
	private List<ProductDTO> findListOfProducts(String bu ,List<String> categories, List<String> brands, List<String> genders, String supplier,	Date startDate, Date endDate, Integer pageIndex, Integer pageSize, String isExport_cat, String isExport_brand,String isExport_gender) throws ServiceException{

		List<ProductDTO> productList = null;
		Map<String, Object> params = new HashMap<String, Object>();
		BuParamDTO buParamDTO = new BuParamDTO();
		buParamDTO.setCategories(categories);
		buParamDTO.setIsExport_cat(isExport_cat);
		buParamDTO.setBrands(brands);
		buParamDTO.setIsExport_brand(isExport_brand);
		buParamDTO.setGenders(genders);
		buParamDTO.setIsExport_gender(isExport_gender);
		params.put("BU", bu);
		params.put("supplier", supplier);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("BuParamDTO", buParamDTO);

		if (null != pageIndex && null != pageSize && -1 != pageIndex && -1 != pageSize){
			productList = productDAO.findListInTheCategory(params, new RowBounds(pageIndex, pageSize));
		}else{
			productList = productDAO.findListInTheCategory(params);
		}
		String supplierId = "", skuId = "";
		for (ProductDTO dto : productList) {
			if (null != dto.getSupplierId() && null != dto.getSkuId()) {
				supplierId = dto.getSupplierId();
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
					pictureSet.addAll(skuPictureList);
				}
				// 查询公共的图片
				List<ProductPicture> spuPictureList = pictureDAO
						.findDistinctProductPictureBySupplierIdAndSpuIdAndSkuIdNull(
								supplierId, dto.getSpuId());
				if (!spuPictureList.isEmpty()) {
					pictureSet.addAll(spuPictureList);
				}

				List<ProductPictureDTO> picList = new ArrayList<>();
				for (ProductPicture productPicture : pictureSet) {
					ProductPictureDTO productPictureDTO = new ProductPictureDTO();
					InVoke.setValue(productPicture, productPictureDTO,
							null, null);
					picList.add(productPictureDTO);
				}
				this.setPic(dto, picList);
			}

		}
		return productList;
	}

	/**
	 * 动态添加
	 * @param buffer
	 * @param productList
	 */
	public void addStringBuffer(StringBuffer buffer,List<ProductDTO> productList){
		String productSize, productDetail = "", brandName = "", brandId = "", color = "", material = "", productOrigin = "";
		String supplierId="", categoryName = "", productName = "";
		for(ProductDTO dto : productList){
			try{
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
				//尚品sku编号
				buffer.append(StringUtils.isNotBlank(dto.getSpSkuId())? dto.getSpSkuId():"").append(splitSign);
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

	/**
	 * 查找所有BU
	 * @return
	 */
	public List<String> findAllBus() throws ServiceException{
		return BuEpRuleDAO.findAllBus();
	}

	/**
	 * 特殊处理的，过滤
	 * @param productList
	 */
	private void screenProducts(List<ProductDTO> productList){
		try {
			int count = BuEpSpecialDAO.findCount();
			if(count != BuEpSpecialList.size()){
				BuEpSpecialList = BuEpSpecialDAO.findAll();
			}
			for(ProductDTO product : productList){
				try {
					for(BuEpSpecialDTO special : BuEpSpecialList){
						if(special.getBrand().equals(product.getBrandName()) && (special.getCategory().equals(product.getSubCategoryName()) || special.getCategory().equals(product.getCategoryName()))){
							if((StringUtils.isNotBlank(special.getExcludedSize()) && special.getExcludedSize().equals(product.getSize()))
									|| (StringUtils.isNotBlank(special.getMinSupplierPrice()) && StringUtils.isNotBlank(product.getNewSupplierPrice()) && Double.parseDouble(product.getNewSupplierPrice()) < Double.parseDouble(special.getMinSupplierPrice()))
									|| (StringUtils.isNotBlank(special.getMaxSupplierPrice()) && StringUtils.isNotBlank(product.getNewSupplierPrice()) && Double.parseDouble(product.getNewSupplierPrice()) > Double.parseDouble(special.getMaxSupplierPrice()))
									|| (StringUtils.isNotBlank(special.getMinSalePrice()) && StringUtils.isNotBlank(product.getNewSalePrice()) && Double.parseDouble(product.getNewSalePrice()) < Double.parseDouble(special.getMinSalePrice()))
									|| (StringUtils.isNotBlank(special.getMaxSalePrice()) && StringUtils.isNotBlank(product.getNewSalePrice()) && Double.parseDouble(product.getNewSalePrice()) > Double.parseDouble(special.getMaxSalePrice()))
									|| (StringUtils.isNotBlank(special.getMinMarketPrice()) && StringUtils.isNotBlank(product.getNewMarketPrice()) && Double.parseDouble(product.getNewMarketPrice()) < Double.parseDouble(special.getMinMarketPrice()))
									|| (StringUtils.isNotBlank(special.getMaxMarketPrice()) && StringUtils.isNotBlank(product.getNewMarketPrice()) && Double.parseDouble(product.getNewMarketPrice()) > Double.parseDouble(special.getMaxMarketPrice()))){

								productList.remove(product);
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	@Override
	public StringBuffer tempExport(String supplier, Date startDate,
								   Date endDate, Integer pageIndex, Integer pageSize)
			throws ServiceException {

		StringBuffer buffer = new StringBuffer("SupplierId 供货商名称" + splitSign
				+ "CategoryName 品类名称" + splitSign + "Category 品类翻译" + splitSign
				+ "Category_No 品类编号" + splitSign + "BrandNo 品牌编号" + splitSign
				+ "BrandName 品牌" + splitSign + "ProductModel 货号" + splitSign
				+ "SupplierSkuNo 供应商SkuNo" + splitSign + " 尚品sku编号 "
				+ splitSign + " 性别 " + splitSign + "SopProductName 商品名称"
				+ splitSign + "BarCode 条形码" + splitSign + "ProductColor 颜色"
				+ splitSign + "color 中文" + splitSign + "ProductSize 尺码"
				+ splitSign + "material 材质" + splitSign + "material 中文材质"
				+ splitSign + "ProductOrigin 产地" + splitSign + "productUrl1"
				+ splitSign + "productUrl2" + splitSign + "productUrl3"
				+ splitSign + "productUrl4" + splitSign + "productUrl5"
				+ splitSign + "productUrl6" + splitSign + "productUrl7"
				+ splitSign + "productUrl8" + splitSign + "productUrl9"
				+ splitSign + "PcDesc 描述" + splitSign + "Stock 库存" + splitSign
				+ "新市场价" + splitSign + "新销售价" + splitSign + "新进货价" + splitSign
				+ "markerPrice" + splitSign + "sallPrice" + splitSign
				+ "supplier Price 进货价" + splitSign + "Currency 币种" + splitSign
				+ "新上市季节" + splitSign + "上市季节" + splitSign + "活动开始时间"
				+ splitSign + "活动结束时间" + splitSign + "SupplierSpuNo 供应商spu编号"
				+ splitSign + "供应商门户编号" + splitSign + "SpuId" + splitSign
				+ "备注").append("\r\n");
		Page<ProductDTO> page = null;
		page = this.tempFindPageBySupplierAndTime(supplier, startDate, endDate,
				pageIndex, pageSize);
		// 品类map赋值
		this.setCategoryMap();
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
				// supplierId 供货商
				supplierId = dto.getSupplierName();
				if (StringUtils.isNotBlank(supplierId)) {
					buffer.append(supplierId).append(splitSign);
				} else {
					buffer.append(dto.getSupplierId()).append(splitSign);
				}

				// 品类名称
				categoryName = dto.getSubCategoryName();
				if (StringUtils.isBlank(categoryName)) {
					categoryName = StringUtils.isBlank(dto.getCategoryName()) ? ""
							: dto.getCategoryName();

				}

				// 翻译
				String categoryCH = "";
				if (StringUtils.isNotBlank(categoryName)) {
					if (categoryContrastMap.containsKey(categoryName
							.toLowerCase())) {
						categoryCH = categoryContrastMap.get(categoryName
								.toLowerCase());
					}
				}

				categoryName = categoryName.replaceAll(splitSign, " ");
				buffer.append(categoryName).append(splitSign);

				// 品类翻译
				buffer.append(categoryCH).append(splitSign);

				buffer.append(StringUtils.isNotBlank(dto.getSpCategory()) ? dto.getSpCategory() : "").append(splitSign);

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

				buffer.append(StringUtils.isNotBlank(dto.getSpBrand()) ? dto.getSpBrand() : "")
						.append(splitSign);

				if (supplier == "2015081701437") {
					brandName = brandName.replaceAll("C?LINE", "CÈLINE");
				}

				buffer.append(brandName).append(splitSign);

				// 品牌翻译

				// 货号
				buffer.append(
						null == dto.getProductCode() ? "" : dto
								.getProductCode().replaceAll(",", " ")).append(
						splitSign);
				// 供应商SKUID

				buffer.append("\"\t" + dto.getSkuId() + "\"").append(splitSign);
				// 尚品sku编号
				buffer.append(
						StringUtils.isNotBlank(dto.getSpSkuId()) ? dto
								.getSpSkuId() : "").append(splitSign);
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
				color = dto.getColor() == null ? "" : dto.getColor()
						.replace(",", " ").replaceAll("/", " ");
				buffer.append(null == color ? "" : color.replace(",", " "))
						.append(splitSign);
				// 翻译中文
				String colorCh = "";
				if (StringUtils.isNotBlank(color)) {
					if (colorContrastMap.containsKey(color.toLowerCase())) {
						colorCh = colorContrastMap.get(color.toLowerCase());
					} else {
						for (String co : color.split("\\s+")) {
							if (colorContrastMap.containsKey(co.toLowerCase())) {
								colorCh += colorContrastMap.get(co
										.toLowerCase());
							} else {
								colorCh += co;
							}
						}
					}
				}
				buffer.append(colorCh).append(splitSign);

				// 获取尺码
				productSize = dto.getSize();
				if (StringUtils.isNotBlank(productSize)) {
					productSize = productSize.replace(",", ".");
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

					// 先遍历带有空格的材质
					Set<Map.Entry<String, String>> materialSet = materialContrastMap
							.entrySet();
					for (Map.Entry<String, String> entry : materialSet) {

						material = material.toLowerCase().replaceAll(
								entry.getKey(), entry.getValue());
					}

					// 再遍历单个材质
					Set<Map.Entry<String, String>> smallMaterialSet = smallMaterialContrastMap
							.entrySet();
					for (Map.Entry<String, String> entry : smallMaterialSet) {

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
				if (supplier == "2015081701437") {// 处理特殊供货商的特殊字符
					buffer.append(
							dto.getPicUrl().replaceAll("C?LINE", "CÈLINE"))
							.append(splitSign);
					buffer.append(
							dto.getItemPictureUrl1().replaceAll("C?LINE",
									"CÈLINE"))
							.append(splitSign)
							.append(dto.getItemPictureUrl2().replaceAll(
									"C?LINE", "CÈLINE"))
							.append(splitSign)
							.append(dto.getItemPictureUrl3().replaceAll(
									"C?LINE", "CÈLINE"))
							.append(splitSign)
							.append(dto.getItemPictureUrl4().replaceAll(
									"C?LINE", "CÈLINE"))
							.append(splitSign)
							.append(dto.getItemPictureUrl5().replaceAll(
									"C?LINE", "CÈLINE"))
							.append(splitSign)
							.append(dto.getItemPictureUrl6().replaceAll(
									"C?LINE", "CÈLINE"))
							.append(splitSign)
							.append(dto.getItemPictureUrl7().replaceAll(
									"C?LINE", "CÈLINE"))
							.append(splitSign)
							.append(dto.getItemPictureUrl8().replaceAll(
									"C?LINE", "CÈLINE")).append(splitSign);
				} else {
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

				buffer.append(dto.getSaleCurrency()).append(splitSign);

				// 新季节
				// buffer.append(
				// null == dto.getNewseasonName() ? dto.getNewseasonId() : dto
				// .getNewseasonName()).append(splitSign);
				if (dto.getNewseasonName() != null) {
					if (dto.getNewseasonId() != null) {
						if (dto.getNewseasonName().trim()
								.equals(dto.getNewseasonId().trim())) {
							buffer.append(dto.getNewseasonName()).append(
									splitSign);
						} else {
							buffer.append(dto.getNewseasonId()).append(" ")
									.append(dto.getNewseasonName())
									.append(splitSign);
						}
					} else {
						buffer.append(dto.getNewseasonName()).append(splitSign);
					}

				} else {
					buffer.append(dto.getNewseasonId()).append(splitSign);
				}

				if (dto.getSeasonName() != null) {
					if (dto.getSeasonId() != null) {
						if (dto.getSeasonName().trim()
								.equals(dto.getSeasonId().trim())) {
							buffer.append(dto.getSeasonName())
									.append(splitSign);
						} else {
							buffer.append(dto.getSeasonId()).append(" ")
									.append(dto.getSeasonName())
									.append(splitSign);
						}
					} else {
						buffer.append(dto.getSeasonName()).append(splitSign);
					}

				} else {
					buffer.append(dto.getSeasonId()).append(splitSign);
				}

				// 季节

				// buffer.append(
				// null == dto.getSeasonName() ? dto.getSeasonId() : dto
				// .getSeasonName()).append(splitSign);
				// 活动开始时间
				buffer.append(
						null == dto.getEventStartTime() ? " " : dto
								.getEventStartTime()).append(splitSign);
				// 活动结束时间
				buffer.append(
						null == dto.getEventEndTime() ? " " : dto
								.getEventEndTime()).append(splitSign);
				// 供应商spuid
				buffer.append(null == dto.getSpuId() ? " " : dto.getSpuId())
						.append(splitSign);
				// 供应商门户编号
				buffer.append(
						null == dto.getSupplierId() ? " " : dto.getSupplierId())
						.append(splitSign);

				buffer.append(
						null == dto.getSpuId() ? " " : "SPID"
								+ dto.getSupplierId() + "-"
								+ JavaUtils.getBASE64(dto.getSpuId()) + "-"
								+ JavaUtils.getBASE64(dto.getColor())).append(splitSign);

				buffer.append(dto.getMemo());
				buffer.append("\r\n");
			} catch (Exception e) {
				logger.debug(dto.getSkuId() + "拉取失败" + e.getMessage());
				continue;
			}

		}
		return buffer;
	}

	public Page<ProductDTO> tempFindPageBySupplierAndTime(String supplier,
														  Date startDate, Date endDate, Integer pageIndex, Integer pageSize) throws ServiceException {
		List<ProductDTO> productList = null;
		Page<ProductDTO> page = null;
		try {
			if (null != pageIndex && null != pageSize) {
				page = new Page<>(pageIndex, pageSize);

				productList = productDAO.tempFindListBySupplierAndLastDate(
						supplier, startDate, endDate, new RowBounds(
								pageIndex, pageSize));
			}else{
				System.out.println(supplier+"=================================");
				productList = productDAO.tempFindListBySupplierAndLastDate(
						supplier, startDate, endDate);
			}
			System.out.println(supplier+"=================================");
			System.out.println("======================================="+productList.size());

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

	public static void main(String[] args) {
		FileOutputStream fileOut = null;
		BufferedImage bufferImg = null;
		//先把读进来的图片放到一个ByteArrayOutputStream中，以便产生ByteArray
//       try { 
//    	   HSSFWorkbook wb = new HSSFWorkbook();     
//           HSSFSheet sheet = wb.createSheet("test picture");    
//           //画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）  
//           HSSFPatriarch patriarch = sheet.createDrawingPatriarch();    
//    	   
//    	   InputStream is = new FileInputStream(new File("F:\\testpics\\2016030401797\\2016-09-05\\SPID2016030401797-NDEwMA==-MTAwMCBORVJP (1).JPG"));  
//			byte[] bytes = IOUtils.toByteArray(is);
////			int pictureIdx = wb.addPicture(bytes, wb.PICTURE_TYPE_JPEG); 
//			int pictureIdx = wb.addPicture(bytes, wb.PICTURE_TYPE_JPEG);  
//			is.close();
//			CreationHelper helper = wb.getCreationHelper();  
//			Drawing drawing = sheet.createDrawingPatriarch();  
//			ClientAnchor anchor = helper.createClientAnchor();
//			// 图片插入坐标  
//			anchor.setCol1(0);  
//			anchor.setRow1(1);  
//			// 插入图片  
//			Picture pict = drawing.createPicture(anchor, pictureIdx);  
//			pict.resize(); 
//    	   
//    	   
////           ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();     
////           bufferImg = ImageIO.read(new File("F:\\testpics\\2016030401797\\2016-09-05\\SPID2016030401797-NDEwMA==-MTAwMCBORVJP (1).JPG"));     
////           ImageIO.write(bufferImg, "jpg", byteArrayOut);  
//             
//            
//           //anchor主要用于设置图片的属性  
////           HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 255, 255,(short) 1, 1, (short) 5, 8);     
////           anchor.setAnchorType(AnchorType.MOVE_AND_RESIZE);     
////           //插入图片    
////           patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));   
//           fileOut = new FileOutputStream("D:/测试Excel.xls");     
//           // 写入excel文件     
//            wb.write(fileOut);     
//            System.out.println("----Excle文件已生成------");  
//       } catch (Exception e) {  
//           e.printStackTrace();  
//       }finally{  
//           if(fileOut != null){  
//                try {  
//                   fileOut.close();  
//               } catch (IOException e) {  
//                   e.printStackTrace();  
//               }  
//           }  
//       }  


		//先把读进来的图片放到一个ByteArrayOutputStream中，以便产生ByteArray
		try {
			ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
			bufferImg = ImageIO.read(new File("F:\\testpics\\2016030401797\\2016-09-05\\SPID2016030401797-NDEwMA==-MTAwMCBORVJP (1).JPG"));
			ImageIO.write(bufferImg, "jpg", byteArrayOut);

			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet1 = wb.createSheet("test picture");
			//画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
			HSSFPatriarch patriarch = sheet1.createDrawingPatriarch();
			//anchor主要用于设置图片的属性
			HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 255, 255,(short) 0, 0, (short) 0, 0);
			anchor.setAnchorType(AnchorType.MOVE_AND_RESIZE);
			//插入图片
			patriarch.createPicture(anchor, wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));
			fileOut = new FileOutputStream("D:/测试Excel.xls");
			// 写入excel文件
			wb.write(fileOut);
			System.out.println("----Excle文件已生成------");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(fileOut != null){
				try {
					fileOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
