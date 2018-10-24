package com.shangpin.supplier.product.consumer.supplier.suitnegozi;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.message.picture.image.Image;
import com.shangpin.supplier.product.consumer.supplier.common.spinnaker.ISpinnakerHandler;
import com.shangpin.supplier.product.consumer.supplier.common.spinnaker.dto.Sku;
import com.shangpin.supplier.product.consumer.supplier.common.spinnaker.dto.Spu;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>Title:SpinnakerCommonHandler </p>
 * <p>Description: spinnaker供应商一般处理逻辑类 </p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月10日 下午2:59:57
 *
 */
@Component
public class SuitnegoziCommonHandler extends ISuitnegoziHandler {

	@Override
	public boolean convertSpu(String supplierId, Spu spu, Sku sku, HubSupplierSpuDto hubSpu,String data) {
		if(null != spu){
			
			String colorCode = "";
			String colorName = "";
			String color = sku.getColor();
			if(color.contains(" ")){
				String firstStr = color.substring(0, color.indexOf(" "));
				if(hasDigit(firstStr)){
					colorCode = firstStr;
					colorName = color.substring(color.indexOf(" ")).trim();
				}else{
					colorCode = color;
					colorName = color;
				}
			}else{
				colorCode = color;
				colorName = color;
			}
			
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(spu.getProduct_id()+"-"+colorCode);
			hubSpu.setSupplierSpuModel(spu.getProduct_name()+" "+colorCode);
			hubSpu.setSupplierSpuName(spu.getDescription());
			hubSpu.setSupplierSpuColor(colorName);
			hubSpu.setSupplierGender(spu.getType());
			hubSpu.setSupplierCategoryname(spu.getCategory());

		 	            //dolciTrame
			String[] dolciTrame={"ABITO","Camicia","GIACCA","Gonna","Jeans","MAGLIA","Pantaloni","Top","T-Shirt"};
            if(supplierId.equals("2017110801991")){
             for(int i=0;i<dolciTrame.length;i++){
             	if (hubSpu.getSupplierSpuName().toUpperCase().contains(dolciTrame[i].toUpperCase())){

					hubSpu.setSupplierCategoryname(dolciTrame[i]);

				}
			 }
			}

			//Monti

			String[] Monti={"ABITO", "Camicia", "CAPPOTTO", "Felpa", "GIACCA", "Gonna", "MAGLIA", "Pantaloni", "Polo", "Shorts", "Top", "T-Shirt"
			};
			if(supplierId.equals("2016032401822")){
				for(int i=0;i<Monti.length;i++){
					if (hubSpu.getSupplierSpuName().toUpperCase().contains(Monti[i].toUpperCase())){

						hubSpu.setSupplierCategoryname(Monti[i]);

					}
				}
			}
			//papini
			String[] papini={"ABITO", "Camicia", "CAPPOTTO", "Costume", "Felpa", "GIACCA", "Giubbotto", "Gonna", "Jeans", "MAGLIA", "Pantaloni", "Polo", "Shorts", "Top", "Trench", "T-Shirt"
			};
			if(supplierId.equals("2016030401797")){
				for(int i=0;i<papini.length;i++){
					if (hubSpu.getSupplierSpuName().toUpperCase().contains(papini[i].toUpperCase())){

						hubSpu.setSupplierCategoryname(papini[i]);

					}
				}
			}

			String[] portofino={"ABITO", "Body", "Camicia", "CAPPOTTO", "COMPLETO", "GIACCA", "Gonna", "Jeans", "MAGLIA", "Pantaloni", "Top", "T-Shirt"
			};
			if(supplierId.equals("2017060101976")){
				for(int i=0;i<portofino.length;i++){
					if (hubSpu.getSupplierSpuName().toUpperCase().contains(portofino[i].toUpperCase())){

						hubSpu.setSupplierCategoryname(portofino[i]);

					}
				}
			}

			//Sanremo
			String[] Sanremo={"ABITO", "Blazer", "Body", "Camicia", "CAPPOTTO", "COMPLETO", "Costume", "Felpa", "GIACCA", "Giubbotto", "Gonna", "Jeans", "MAGLIA", "Pantaloni", "Polo", "Shorts", "Top", "Tops", "T-Shirt"
			};
			if(supplierId.equals("2015082801463")){
				for(int i=0;i<Sanremo.length;i++){
					if (hubSpu.getSupplierSpuName().toUpperCase().contains(Sanremo[i].toUpperCase())){

						hubSpu.setSupplierCategoryname(Sanremo[i]);

					}
				}
			}

			//SPINNAKER
			String[] SPINNAKER={"ABITO", "Camicia", "CAPPOTTO", "Felpa", "GIACCA", "Gonna", "MAGLIA", "Pantaloni", "Top", "T-Shirt"
			};
			if(supplierId.equals("2015081701439")){
				for(int i=0;i<SPINNAKER.length;i++){
					if (hubSpu.getSupplierSpuName().toUpperCase().contains(SPINNAKER[i].toUpperCase())){

						hubSpu.setSupplierCategoryname(SPINNAKER[i]);

					}
				}
			}

            String[] SUITNEGOZI={"ABITO", "Camicia", "CAPPOTTO", "Felpa", "GIACCA", "Gonna", "MAGLIA", "Pantaloni", "Top", "T-Shirt"
            };
            if(supplierId.equals("201710261111")){
                for(int i=0;i<SPINNAKER.length;i++){
                    if (hubSpu.getSupplierSpuName().toUpperCase().contains(SUITNEGOZI[i].toUpperCase())){

                        hubSpu.setSupplierCategoryname(SPINNAKER[i]);

                    }
                }
            }









            hubSpu.setSupplierBrandname(spu.getProducer_id());
			hubSpu.setSupplierSeasonname(spu.getSeason());
			hubSpu.setSupplierMaterial(spu.getProduct_detail());
			hubSpu.setSupplierOrigin(spu.getProduct_MadeIn());
			hubSpu.setSupplierSpuDesc(spu.getProduct_detail()); 
			return true;
		}else{
			return false;
		}
	}

	@Override
	public boolean convertSku(String supplierId, Long supplierSpuId, Sku sku, HubSupplierSkuDto hubSku) {
		if(null != sku){
			
			hubSku.setSupplierSpuId(supplierSpuId);
			hubSku.setSupplierId(supplierId);
			hubSku.setSupplierSkuNo(sku.getBarcode());
			hubSku.setMarketPrice(new BigDecimal(StringUtil.verifyPrice(sku.getSupply_price())));
			String marketPrice = StringUtil.verifyPrice(sku.getSupply_price());
			hubSku.setSupplyPrice(new BigDecimal(marketPrice).divide(new BigDecimal("1.2"),2, RoundingMode.HALF_UP)); 
			hubSku.setSupplierBarcode(sku.getBarcode());
			String size = "";
			if(sku.getItem_size().length()>4) {
				size = sku.getItem_size().substring(0,sku.getItem_size().length()-4);
            }else{
            	size = sku.getItem_size();
            }
			hubSku.setSupplierSkuSize(!StringUtils.isEmpty(sku.getCountry_size()) ? sku.getCountry_size()+" "+size : size);
			String stock = sku.getStock();
			if (StringUtils.isEmpty(stock)) {
				stock = "0";
			}else if (Integer.valueOf(stock) <= 0) {
				stock = "0";
			}
			hubSku.setStock(Integer.valueOf(stock)); 
			return true;
		}else{
			return false;
		}
	}

	@Override
	public List<Image> converImage(Sku sku) {
		if(null == sku || null == sku.getPictures() || sku.getPictures().size() == 0){
			return null;
		}else{
			List<Image> images = new ArrayList<Image>();
			for(String url : sku.getPictures()){
				Image image = new Image();
				image.setUrl(url);
				images.add(image);
			}
			return images;
		}
	}
	
	/**
	 * 判断字符串中是否含有数字
	 * @param content
	 * @return
	 */
	private boolean hasDigit(String content) {
		boolean flag = false;
		Pattern p = Pattern.compile(".*\\d+.*");
		Matcher m = p.matcher(content);
		if (m.matches())
			flag = true;
		return flag;
	}

}
