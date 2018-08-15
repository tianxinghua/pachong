package com.shangpin.supplier.product.consumer.supplier.common.atelier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.shangpin.ephub.client.data.mysql.sku.dto.HubSupplierSkuDto;
import com.shangpin.ephub.client.data.mysql.spu.dto.HubSupplierSpuDto;
import com.shangpin.ephub.client.message.original.body.SupplierProduct;
import com.shangpin.ephub.client.message.picture.body.SupplierPicture;
import com.shangpin.ephub.client.message.picture.image.Image;
import com.shangpin.ephub.client.util.JsonUtil;
import com.shangpin.supplier.product.consumer.service.SupplierProductMongoService;
import com.shangpin.supplier.product.consumer.service.SupplierProductSaveAndSendToPending;
import com.shangpin.supplier.product.consumer.supplier.ISupplierHandler;
import com.shangpin.supplier.product.consumer.supplier.common.atelier.dto.AtelierDate;
import com.shangpin.supplier.product.consumer.supplier.common.atelier.dto.AtelierPrice;
import com.shangpin.supplier.product.consumer.supplier.common.atelier.dto.AtelierSku;
import com.shangpin.supplier.product.consumer.supplier.common.atelier.dto.AtelierSpu;
import com.shangpin.supplier.product.consumer.supplier.common.picture.PictureHandler;
import com.shangpin.supplier.product.consumer.supplier.common.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * * 
 * <p>Title:IAtelierHandler </p>
 * <p>Description: 规范Atelier供应商对接的抽象类</p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2016年12月10日 上午11:41:03
 *
 */
@Component
@Slf4j
public abstract class IAtelierHandler implements ISupplierHandler{
	
	@Autowired
	private SupplierProductSaveAndSendToPending supplierProductSaveAndSendToPending;
	@Autowired
	private PictureHandler pictureHandler;
	@Autowired
	private SupplierProductMongoService mongoService;

	/**
	 * 处理spu行数据，返回一个spu对象
	 * @param spuColumn spu的一条数据
	 * @return
	 */
	public abstract AtelierSpu handleSpuData(String spuColumn);
	
	/**
	 * 处理sku行数据,返回一个sku对象
	 * @param skuColumn sku的一条数据
	 * @return
	 */
	public abstract AtelierSku handleSkuData(String skuColumn);
	
	/**
	 * 处理价格行数据,返回一个价格对象
	 * @param priceColumn
	 * @return
	 */
	public abstract AtelierPrice handlePriceData(String priceColumn);
	
	/**
	 * 从atelierSpu数据中，或者atelierSku数据中找到价格信息，并且给hubSku赋值
	 * @param hubSku hub被赋值的对象
	 * @param atelierSpu atelierSpu对象
	 * @param atelierPrice atelierSku对象
	 */
	public abstract void setProductPrice(HubSupplierSkuDto hubSku, AtelierSpu atelierSpu, AtelierPrice atelierPrice);
	/**
	 * Atelier处理图片
	 * @param atelierImags
	 * @return
	 */
	public abstract List<Image> converImage(List<String> atelierImags);
	
	/**
	 * atelier通用处理主流程
	 * @param message
	 * @param headers
	 */
	public void handleOriginalProduct(SupplierProduct message, Map<String, Object> headers){
		try {
			if(!StringUtils.isEmpty(message.getData())){
				AtelierDate atelierDate = JsonUtil.deserialize(message.getData(),AtelierDate.class);
				String supplierId = message.getSupplierId();
				AtelierSpu atelierSpu = handleSpuData(atelierDate.getSpu());	
				
				mongoService.save(supplierId, atelierSpu.getSpuId(), atelierDate);
				
				HubSupplierSpuDto hubSpu =  new HubSupplierSpuDto();
				List<Image> images = converImage(atelierDate.getImage());
//				if(null == images){
//					hubSpu.setIsexistpic(Isexistpic.NO.getIndex());
//				}else{
//					hubSpu.setIsexistpic(Isexistpic.YES.getIndex()); 
//				}
				boolean success = convertSpu(supplierId,atelierSpu,hubSpu,message.getData());
				List<HubSupplierSkuDto> hubSkus = new ArrayList<HubSupplierSkuDto>();
				if(null != atelierDate.getSku()){				
					AtelierPrice atelierPrice = handlePriceData(atelierDate.getPrice());
					for(String skuColumn : atelierDate.getSku()){
						HubSupplierSkuDto hubSku = new HubSupplierSkuDto();
						AtelierSku atelierSku = handleSkuData(skuColumn);					
						boolean skuSucc = convertSku(supplierId,hubSpu.getSupplierSpuId(),atelierSpu,atelierSku,atelierPrice,hubSku);
						if(skuSucc){
							hubSkus.add(hubSku);
						}					
					}
				}
				//处理图片
				SupplierPicture supplierPicture = pictureHandler.initSupplierPicture(message, hubSpu, images);
				if(success){
					supplierProductSaveAndSendToPending.saveAndSendToPending(message.getSupplierNo(),supplierId, message.getSupplierName(), hubSpu, hubSkus,supplierPicture);
				}
			}
		} catch (Exception e) {
			log.error("Atelier系统供应商 "+message.getSupplierName()+"异常："+e.getMessage(),e); 
		}
		
		
	}
	
	/**
	 * 将Atelier对象转换成hub对象
	 * @param supplierId 供应商门户编号
	 * @param supplierSpuId hub spuid
	 * @param atelierSpu atelier spu对象
	 * @param atelierSku atelier sku对象
	 * @param atelierPrice atelier price对象
	 * @param hubSku hub sku对象
	 * @return
	 */
	public boolean convertSku(String supplierId,Long supplierSpuId, AtelierSpu atelierSpu,AtelierSku atelierSku,AtelierPrice atelierPrice, HubSupplierSkuDto hubSku){
		if(null != atelierSku){
			hubSku.setSupplierSpuId(supplierSpuId);
			hubSku.setSupplierId(supplierId);
			//!StringUtils.isEmpty(atelierSpu.getSizeType()) ? atelierSpu.getSizeType()+" "+atelierSku.getSize() : atelierSku.getSize()
			hubSku.setSupplierSkuSize(atelierSku.getSize());
			hubSku.setSupplierBarcode(atelierSku.getBarcode());
			hubSku.setSupplierSkuNo(atelierSku.getSpuId()+"-"+atelierSku.getBarcode());
			if (atelierSku.getSpuId().contains(">")){
			   String[] split = atelierSku.getSpuId().split(">");
			   String  s1=split[1];
			   hubSku.setSupplierSkuNo(s1+"-"+atelierSku.getBarcode());
		   }

			setProductPrice(hubSku,atelierSpu,atelierPrice);
			hubSku.setStock(StringUtil.verifyStock(atelierSku.getStock()));
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 将Atelier对象转换成hub对象
	 * @param supplierId 供应商门户编号
	 * @param atelierSpu atelier spu对象
	 * @param hubSpu hub spu对象
	 */
	public boolean convertSpu(String supplierId,AtelierSpu atelierSpu,HubSupplierSpuDto hubSpu,String data){
		if(null != atelierSpu){
			
			hubSpu.setSupplierId(supplierId);
			hubSpu.setSupplierSpuNo(atelierSpu.getSpuId());
			hubSpu.setSupplierSpuModel(atelierSpu.getStyleCode()+" "+atelierSpu.getColorCode());
			hubSpu.setSupplierSpuName(atelierSpu.getCategoryName()+" "+atelierSpu.getBrandName());
			hubSpu.setSupplierSpuColor(atelierSpu.getColorName());
			hubSpu.setSupplierGender(atelierSpu.getCategoryGender());
			hubSpu.setSupplierCategoryname(atelierSpu.getCategoryName());
			//Creative99
			String[] Creative99={"Body","Dress","Jacket","Jeans","Knitwear","Shirt","Short","Skirt","Sweatshirt","Trousers","T-Shirt"};
			if (supplierId.equals("2016030401795")){
            for (int i=0;i<Creative99.length;i++){
            	if (hubSpu.getSupplierSpuName().toUpperCase().contains(Creative99[i].toUpperCase())){
					hubSpu.setSupplierCategoryname(Creative99[i]);
				}
			}
			}
           //daniello
			String[] daniello={"ABITO","Bermuda","Blusa","Camicia","Canotta","CAPPOTTO","Cardigan","Costume","Felpa","GIACCA","Gilet","Giubbino","Gonna","Jeans","Leggins","MAGLIA","Pantalone","Polo","Short","Shorts","Spolverino","Top","Trench","T-Shirt","Tuta"};
			if (supplierId.equals("2016011201731")){
				for (int i=0;i<daniello.length;i++){
					if (hubSpu.getSupplierSpuName().toUpperCase().contains(daniello[i].toUpperCase())){
						hubSpu.setSupplierCategoryname(daniello[i]);
					}
				}
			}
			//DIVO
			String[] divo={"ABITO","Camicia","COMPLETO","COSTUME MARE UOMO","Felpa","GIACCA","Gonna","INTIMO DONNA","Jeans","MAGLIERIA KNITWEAR","Pantalone","Polo","Shorts","Top","T-Shirt","Tuta"};

			if (supplierId.equals("2016022401783")){
				for (int i=0;i<divo.length;i++){
					if (hubSpu.getSupplierSpuName().toUpperCase().contains(divo[i].toUpperCase())){
						hubSpu.setSupplierCategoryname(divo[i]);
					}
				}
			}

			//Bagheera
			String[] bagheera={"Abiti", "Bermuda", "Camicie", "CAPPOTTI", "Clothes", "Felpe", "GIACCA", "Giacche", "Giacche Casual", "GIACCHE DI PELLE", "Gonne", "Jacket", "Jeans", "Knitwear", "Leggings", "Maglieria", "Pantaloni", "Pants", "PELLICCE", "QUILTS", "TANK TOP", "Top", "T-Shirt", "Tute", "BERMUDA SHORTS"
			};
			if (supplierId.equals("2015100701573")){
				for (int i=0;i<bagheera.length;i++){
					if (hubSpu.getSupplierSpuName().toUpperCase().contains(bagheera[i].toUpperCase())){
						hubSpu.setSupplierCategoryname(bagheera[i]);
					}
				}
			}


			//Gaudenzi
			String[] gaudenzi={"ABITO","Bermuda","Blusa","Bomber","Camicia","CAPPA","CAPPOTTO","Cardigan","COMPLETO","Costume","Felpa","GIACCA","Gilet","Giubbotto","Gonna","Jeans","Kaban","Leggings","MAGLIA","Maglione","Pantalone","PARKA","Pelliccia","Polo","Pull","Shorts","SNEAKER","Spolverino","Top","Trench","T-Shirt","Tuta"};
			if (supplierId.equals("2017111401993")){
				for (int i=0;i<gaudenzi.length;i++){
					if (hubSpu.getSupplierSpuName().toUpperCase().contains(gaudenzi[i].toUpperCase())){
						hubSpu.setSupplierCategoryname(gaudenzi[i]);
					}
				}
			}

			//brunarosso
			String[] brunarosso={"Allacciata", "Bermuda", "BOOTS", "Caban", "Cardigan", "Ciabatta", "Coat", "Down Jacket", "Dress", "Espadrillas", "Espadrille", "Flat", "Flip Flop", "Fur", "Gilet", "Heavy Jacket", "Infradito", "Jacket", "Jeans", "Jumpsuit", "Lace Up", "Leather Jacket", "Loafer", "Loafer ", "MULE", "Pants", "Polo", "Pump", "Sabot", "SANDALS", "Shirt", "Shorts", "Skirt", "Slider", "Slingback", "SLIP ON", "SNEAKER", "Suit", "Sweater", "Sweatshirt", "Swimsuit", "tacco", "Top", "Trench", "T-Shirt", "Tunic", "Wedge"
			};
			if (supplierId.equals("2015091801507")){
				for (int i=0;i<brunarosso.length;i++){
					if (hubSpu.getSupplierSpuName().toUpperCase().contains(brunarosso[i].toUpperCase())){
						hubSpu.setSupplierCategoryname(brunarosso[i]);
					}
				}
			}


			//genteroma
			String[] genteroma={"Blousa","Jacket","Knitwear","Long dress","Mini dress","Shirt/Blouse","Top","Trousers","T-Shirt","Tuta"};
			if (supplierId.equals("2015111001656")){
				for (int i=0;i<genteroma.length;i++){
					if (hubSpu.getSupplierSpuName().toUpperCase().contains(genteroma[i].toUpperCase())){
						hubSpu.setSupplierCategoryname(genteroma[i]);
					}
				}
			}

			//julian fashion

			String[] julian ={"ALL-IN-ONE","BABY SUITS, BODIES", "BACKPACKS", "BEACHWEAR", "BLOUSES", "BOOTS", "BROGUES", "CAPES", "CASUAL JACKETS", "CASUAL JACKETS,PARKAS", "COATS,TRENCH COATS", "Dresses", "FLATS", "FURS", "HATS", "jackets", "Jeans", "KNITWEARS", "Leggings", "PARKAS", "Polo", "Pumps", "SANDALS", "SATCHEL", "SCARVES", "SHIRTS,BLOUSES", "SHOPPING BAGS", "Shorts", "SHOULDER BAGS", "Skirts", "SLIPPERS", "SNEAKERS", "sweaters", "Top", "Tote bags", "Trousers", "T-SHIRTS", "UNDERWEAR", "vests", "Wedges"};
			if (supplierId.equals("2015100501570")){
				for (int i=0;i<julian.length;i++){
					if (hubSpu.getSupplierSpuName().toUpperCase().contains(julian[i].toUpperCase())){
						hubSpu.setSupplierCategoryname(julian[i]);
					}
				}
			}

			//leam
			String[] leam={"Bermuda", "BODYSUIT", "Coats", "Down Jacket", "Dress", "Gilet", "Heavy Jacket", "Jacket", "Jeans", "Leggings", "Outerwear", "Polo Shirt", "Shirt", "Skirts", "Sweater", "Sweatshirt", "Swimsuit", "TANK TOP", "Topwear", "TRACKSUIT", "Trousers", "T-Shirt"
			};
			if (supplierId.equals("2015081701441")){
				for (int i=0;i<leam.length;i++){
					if (hubSpu.getSupplierSpuName().toUpperCase().contains(leam[i].toUpperCase())){
						hubSpu.setSupplierCategoryname(leam[i]);
					}
				}
			}
			//lindelepalais
			String[] lindelepalais={"Abiti", "BORSE GRANDI", "BORSE PICCOLE", "Camicie", "CAPPELLI", "CAPPOTTI", "Cardigan", "COPERTE", "COSTUMI", "Felpe", "Giacche", "GIACCHE DI PELLE", "Gilet", "GIUBBOTTI", "GONNE CORTE", "GONNE LUNGHE", "Jeans", "Leggings", "MAGLIONI", "PANTALONI CORTI", "PANTALONI LUNGHI", "PELLICCE", "PIUMINI", "SCIARPE", "SNEAKERS", "Tops", "T-Shirt", "VESTITI CORTI", "VESTITI LUNGHI", "ZAINI"
			};
			if (supplierId.equals("2016050401882")){
				for (int i=0;i<lindelepalais.length;i++){
					if (hubSpu.getSupplierSpuName().toUpperCase().contains(lindelepalais[i].toUpperCase())){
						hubSpu.setSupplierCategoryname(lindelepalais[i]);
					}
				}
			}

			//marino
			String[] marino={"Bag", "blouse", "Bomber", "Cap", "CAPPOTTO", "Cardigan", "Coat", "Coperta", "Dress", "Hat", "Hoodie", "Jacket", "Jeans", "Jumpsuit", "Leggings", "Lupetto", "Maxi t-shirt", "PIUMINO","Pochette", "Polo Shirt", "Pullover", "Romper", "Salopette", "Sciarpa", "Shirt", "Shoes", "Shorts", "Skirt", "Suit", "Sweatshirt", "Swimsuit", "TANK TOP", "Top", "Trousers", "T-Shirt", "VEST", "Zaino"
			};
			if (supplierId.equals("2017122101995")){
				for (int i=0;i<marino.length;i++){
					if (hubSpu.getSupplierSpuName().toUpperCase().contains(marino[i].toUpperCase())){
						hubSpu.setSupplierCategoryname(marino[i]);
					}
				}
			}

			//MengottiSnc
			String[] MengottiSnc={"Bermuda", "Felpa", "GIACCA", "Giaccone", "Gilet", "Giubbino gilet", "MAGLIA", "PIUMINO", "Polo", "T-Shirt"};
			if (supplierId.equals("2018042002008")){
				for (int i=0;i<MengottiSnc.length;i++){
					if (hubSpu.getSupplierSpuName().toUpperCase().contains(MengottiSnc[i].toUpperCase())){
						hubSpu.setSupplierCategoryname(MengottiSnc[i]);
					}
				}
			}

			//angeloMinetti-atelier

			String[] angeloMinetti={"Body", "Cardigan", "Coats", "Dresses", "jackets", "Jeans", "Knitwear", "Pants","PARKA", "Polo", "Shirts", "Shorts", "Skirts", "Suits", "Sweatshirts", "Tops", "Trousers", "T-SHIRTS"};
			if (supplierId.equals("2017111401992")){
				for (int i=0;i<angeloMinetti.length;i++){
					if (hubSpu.getSupplierSpuName().toUpperCase().contains(angeloMinetti[i].toUpperCase())){
						hubSpu.setSupplierCategoryname(angeloMinetti[i]);
					}
				}
			}
			//ostore
			String[] ostore={"Bag", "BAGS", "BEACHWEAR", "Camicia", "CAPPOTTO", "Coat", "Denim", "down coat", "Down Jacket", "DownJacket", "Dress", "fur coat", "GIACCA", "Gonna", "Hat", "Jacket", "Jeans", "Knitwear", "Leather Jacket", "Maglieria", "Pantalone", "Polo", "Scarf", "Scarpe", "sheepskin", "Shirt", "Shoes", "Short", "Shorts", "Skirt", "Suit", "Top", "Trench", "trenchcoat", "TROUSER", "Trousers", "Tshirt", "T-Shirt", "UNDERWEAR"
			};
			if (supplierId.equals("2015082701461")){
				for (int i=0;i<ostore.length;i++){
					if (hubSpu.getSupplierSpuName().toUpperCase().contains(ostore[i].toUpperCase())){
						hubSpu.setSupplierCategoryname(ostore[i]);
					}
				}
			}

			//paolofiorillo

			String[] paolofiorillo={"BERMUDA SHORTS", "Bodysuits", "Bomber Jacket", "Cardigan", "Coat", "Down Jacket", "Dress", "Gown", "Jacket", "Jeans", "Knitwear", "Pants", "Polo Shirt", "Pullover", "Roundneck Knitwear", "Shirt", "Sweater", "SWIMWEAR", "Top", "TRACKSUIT", "Trench Coat", "T-Shirt"
			};

			if (supplierId.equals("2016012101751")){
				for (int i=0;i<paolofiorillo.length;i++){
					if (hubSpu.getSupplierSpuName().toUpperCase().contains(paolofiorillo[i].toUpperCase())){
						hubSpu.setSupplierCategoryname(paolofiorillo[i]);
					}
				}
			}

			//Tessabit
			String[] tessabit={"ALL-IN-ONE", "BEACHWEAR", "Bomber Jacket", "BOXER", "CABAN JACKET", "Cardigan", "DENIM JACKET", "down coat", "DOWN WAISTCOAT", "Dress", "HIGHNECK SWEATER", "Jacket", "Jeans", "KAFTANO", "Leather Jacket", "Leggings", "Long dress", "PARKA", "Polo", "RAINCOAT", "Shirt", "SHORT COAT", "SHORT DRESS", "Shorts", "Skirt", "Suit", "Sweater", "Sweatshirt", "Top", "TROUSER", "T-Shirt", "Tunica", "SWEATER", "JACKET"
			};
			if (supplierId.equals("2015091701503")){
				for (int i=0;i<tessabit.length;i++){
					if (hubSpu.getSupplierSpuName().toUpperCase().contains(tessabit[i].toUpperCase())){
						hubSpu.setSupplierCategoryname(tessabit[i]);
					}
				}
			}
         //vietti
			String[] vietti={"Dresses", "jackets", "Jeans", "Knitwear", "Pants", "Shirts", "Shorts", "Skirts", "Sweatshirt", "SWIMWEAR", "T-shirt  Polo", "T-shirt  Top"
			};
			if (supplierId.equals("2016072601910")){
				for (int i=0;i<vietti.length;i++){
					if (hubSpu.getSupplierSpuName().toUpperCase().contains(vietti[i].toUpperCase())){
						hubSpu.setSupplierCategoryname(vietti[i]);
					}
				}
			}

			//WISE
			String[] wise={"BEACHWEAR", "Coats", "Denim", "DOWN JACKETS", "Dresses", "jackets", "Knitwear", "POLOS", "Shirts", "Shorts", "Skirts", "Sweatshirts", "Tops", "Trousers", "T-SHIRTS"
			};
			if (supplierId.equals("2016083001937")){
				for (int i=0;i<wise.length;i++){
					if (hubSpu.getSupplierSpuName().toUpperCase().contains(wise[i].toUpperCase())){
						hubSpu.setSupplierCategoryname(wise[i]);
					}
				}
			}
			//zitafabiani

			String[] zitafabiani={"ABITO", "Blusa", "Bomber", "Camicia", "CAMICIA TOP", "CAPPOTTO", "CARDIGAN di MAGLIA", "COMPLETO 2 PEZZI", "Costume", "Dresses", "Felpa", "GIACCA", "GIACCA JEANS", "GIACCONI", "Giubbino", "Gonna", "IMBOTTITI CORTI", "IMBOTTITI LUNGHI", "IMPERMEABILE/SOPRABITO", "Jeans", "Jumpsuit", "MAGLIA", "Pantalone", "PARKA", "Polo", "Short", "Skirts", "Top", "Tops", "T-Shirt"
			};
			if (supplierId.equals("2016032101816")){
				for (int i=0;i<zitafabiani.length;i++){
					if (hubSpu.getSupplierSpuName().toUpperCase().contains(zitafabiani[i].toUpperCase())){
						hubSpu.setSupplierCategoryname(zitafabiani[i]);
					}
				}
			}







			hubSpu.setSupplierBrandname(atelierSpu.getBrandName());
			hubSpu.setSupplierSeasonname(atelierSpu.getSeasonName());
			hubSpu.setSupplierMaterial(atelierSpu.getMaterial1()+" "+atelierSpu.getMaterial3());
			if(supplierId.equals("2016032101819"))
				hubSpu.setSupplierOrigin("");
			else
				hubSpu.setSupplierOrigin(atelierSpu.getProductOrigin());
			
			hubSpu.setSupplierSpuDesc(!StringUtils.isEmpty(atelierSpu.getSizeDetail()) ? atelierSpu.getSizeDetail()+";"+atelierSpu.getDescription() : atelierSpu.getDescription()); 
			return true;
		}else{
			return false;
		}
	}
}
