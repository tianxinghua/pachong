package com.shangpin.iog.smets.util;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shangpin.iog.common.utils.UUIDGenerator;
import com.shangpin.iog.dto.SkuDTO;
import com.shangpin.iog.dto.SpuDTO;
import com.shangpin.iog.service.ProductFetchService;
import com.shangpin.iog.smets.dto.ExcelDTO;
@Component("qwerty")
public class Test {
	private static Logger logger = Logger.getLogger("info");
	@Autowired
	ProductFetchService productFetchService;
//	11282197
	public void test() {
		String url = "";
		List<ExcelDTO> dtoLists = new Excel2DTO().toDTO("F:\\has_id.xlsx", 0, new Short[]{0,1}, ExcelDTO.class);
		for (ExcelDTO excelDTO : dtoLists) {
			url = "http://www.farfetch.com/cn/shopping/"+excelDTO.getGender()+"/item-"+excelDTO.getId()+".aspx?q="+excelDTO.getId();
//			url = http://www.farfetch.com/cn/shopping/men/item-11282197.aspx?q=11282197;
			System.out.println(url);
			save(url);
		}
	}
	
	private void save(String url){
		HttpResponse response1 = null;
		try {
			response1 = HttpUtils.get(url);
			if(response1.getStatus()==200){
				String img = "";
				String matrial = "";
				String brandName = "";
				String description = "";
				String skuName = "";
				String gender = "";
				String skuId = "";
				String category = "";
				
				String htmlContentdet = response1.getResponse();
				Document docdet = Jsoup.parse(htmlContentdet);
				for(Element ele:docdet.select("a[class=relative js-video-thumb]")){
					img += ele.select("img").attr("src")+";";
				}
//			for(Element ele:doc.select("#detailSizeDropdown").select("#SizesInformation_SizeDesc")){
//				size += ele.attr("data-sizeid")+";";
//			}
				brandName = docdet.select("a[data-tstid=Label_ItemBrand]").get(0).ownText();
				category = docdet.select("span[data-tstid=Label_ItemDescription]").get(0).ownText();
				for(Element ele:docdet.select("div[data-tstid=Content_Composition&Care]").select("dd")){
					if (ele.ownText().contains(">")) {
						matrial +=ele.ownText().split(">")[1]+";";
					}
				}
				skuName = docdet.select("h1[class=detail-brand detail-name]").get(0).select("span").get(0).ownText();
				description = docdet.select("p[itemprop=description]").get(0).ownText();
				skuId = docdet.select("span[itemprop=sku]").get(0).ownText();
				gender = docdet.select("#divBreadCrumbInformation").select("a").get(1).ownText();
				//保存到数据库
				SkuDTO sku = new SkuDTO();
				SpuDTO spu = new SpuDTO();
				spu.setId(UUIDGenerator.getUUID());
				sku.setId(UUIDGenerator.getUUID());
				spu.setBrandName(brandName);
				spu.setSpuId(skuId);
				spu.setCategoryGender(gender);
				spu.setCategoryName(category);
				spu.setMaterial(matrial);
				spu.setSupplierId("201603111152");
				
//				201601291741   
				sku.setSupplierId("201603111152");
				sku.setSpuId(skuId);
				sku.setSkuId(skuId);
				sku.setSaleCurrency(url);
				sku.setProductName(skuName);
				sku.setProductDescription(description);
				sku.setStock("1");
				System.out.println("save sku");
				productFetchService.saveSKU(sku);
				System.out.println("save spu");
				productFetchService.saveSPU(spu);
				System.out.println("save spic");
				productFetchService.savePicture("201603111152", null, skuId, Arrays.asList(img.split(";")));
				//
				img = "";
				skuId = "";
				brandName = "";
				description = "";
				matrial = "";
				skuName = "";
				category = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
