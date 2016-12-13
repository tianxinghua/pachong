package com.shangpin.ep.order.module.orderapiservice.impl;

import com.shangpin.ep.order.conf.supplier.SupplierCommon;
import com.shangpin.ep.order.conf.supplier.SupplierProperties;
import com.shangpin.ep.order.module.orderapiservice.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Created by lizhongren on 2016/10/26.
 */
@Component
public class OrderHandleSearch {

	@Autowired
	SupplierProperties supplierProperties;

	@Autowired
	@Qualifier("efashionOrderImpl")
	IOrderService efashionOrderImpl;

	@Autowired
	@Qualifier("inviqaOrderImpl")
	IOrderService inviqaOrderImpl;

	@Autowired
	@Qualifier("montiOrderService")
	IOrderService montiOrderService;

	@Autowired
	@Qualifier("papiniOrderService")
	IOrderService papiniOrderService;
	@Autowired
	@Qualifier("aladucaSreviceImpl")
	IOrderService aladucaSreviceImpl;

	@Autowired
	@Qualifier("brunarossoServiceImpl")
	IOrderService brunarossoServiceImpl;

	@Autowired
	@Qualifier("coltortiOrderServiceImpl")
	IOrderService coltortiOrderServiceImpl;
	@Autowired
	@Qualifier("creative99ServiceImpl")
	IOrderService creative99ServiceImpl;
	@Autowired
	@Qualifier("danielloServiceImpl")
	IOrderService danielloServiceImpl;
	@Autowired
	@Qualifier("genteromaServiceImpl")
	IOrderService genteromaServiceImpl;
	@Autowired
	@Qualifier("leamServiceImpl")
	IOrderService leamServiceImpl;
	@Autowired
	@Qualifier("lindeServiceImpl")
	IOrderService lindeServiceImpl;
	@Autowired
	@Qualifier("ostoreServiceImpl")
	IOrderService ostoreServiceImpl;
	@Autowired
	@Qualifier("pozzileiArteOrderService")
	IOrderService pozzileiArteOrderService;
	@Autowired
	@Qualifier("pozzileiOrderService")
	IOrderService pozzileiOrderService;
	@Autowired
	@Qualifier("pozzileiForteOrderService")
	IOrderService pozzileiForteOrderService;
	@Autowired
	@Qualifier("sanremoOrderService")
	IOrderService sanremoOrderService;
	@Autowired
	@Qualifier("spinnakerOrderService")
	IOrderService spinnakerOrderService;
	@Autowired
	@Qualifier("stefaniaService")
	IOrderService stefaniaService;
	@Autowired
	@Qualifier("tonyOrderImpl")
	IOrderService tonyOrderImpl;
	@Autowired
	@Qualifier("studio69Serviceimpl")
	IOrderService studio69Serviceimpl;
	@Autowired
	@Qualifier("wiseServiceImpl")
	IOrderService wiseServiceImpl;
	@Autowired
	@Qualifier("velaOrderService")
	IOrderService velaOrderService;
	@Autowired
	@Qualifier("tonySubOrderImpl")
	IOrderService tonySubOrderImpl;
	@Autowired
	@Qualifier("delibertiServiceImpl")
	IOrderService delibertiServiceImpl;
	@Autowired
	@Qualifier("divoServiceImpl")
	IOrderService divoServiceImpl;

	@Autowired
	@Qualifier("testBrunarossoServiceImpl")
	IOrderService testBrunarossoServiceImpl;

	@Autowired
	@Qualifier("testLeamServiceImpl")
	IOrderService testLeamServiceImpl;
	@Autowired
	@Qualifier("clutcherOrderImpl")
	IOrderService clutcherOrderImpl;

	@Autowired
	@Qualifier("russoCapriServiceImpl")
	IOrderService russoCapriServiceImpl;

	@Autowired
	@Qualifier("paoloServiceImpl")
	PaoloServiceImpl paoloServiceImpl;

	@Autowired
	@Qualifier("kixOrderImpl")
	IOrderService kixOrderImpl;

	public IOrderService getHander(String supplierId) {

		if ("2015102201625".equals(supplierId)) {// 预锁库存随机返回值
			return clutcherOrderImpl;
		} else if ("2016030701799".equals(supplierId)) {
			return russoCapriServiceImpl;
		} else if ("2015111001657".equals(supplierId)) {
			return efashionOrderImpl;
		} else if ("2015081701441".equals(supplierId)) {
			return leamServiceImpl;
		} else if ("2015081701439".equals(supplierId)) {
			return spinnakerOrderService;
		} else if ("2015091801507".equals(supplierId)) {
			return brunarossoServiceImpl;
		} else if ("2016030401795".equals(supplierId)) {
			return creative99ServiceImpl;
		} else if ("2016011201731".equals(supplierId)) {
			return danielloServiceImpl;
		} else if ("2016022401783".equals(supplierId)) {
			return divoServiceImpl;
		} else if ("2015111001656".equals(supplierId)) {
			return genteromaServiceImpl;
		} else if ("2016050401882".equals(supplierId)) {
			return lindeServiceImpl;
		} else if ("2015082701461".equals(supplierId)) {
			return ostoreServiceImpl;
		} else if ("2016012101751".equals(supplierId)) {
			return paoloServiceImpl;
		} else if ("2016042501870".equals(supplierId)) {
			return kixOrderImpl;
		} else if ("2015101501608".equals(supplierId)) {
			return tonyOrderImpl;
		} else if ("2015081701440".equals(supplierId)) {
			return coltortiOrderServiceImpl;
		}
		else if ("2015111001657".equals(supplierId)) {
			return papiniOrderService;
		}
		else if ("2015081701443".equals(supplierId)) {
			return velaOrderService;
		}
		else if ("2015101001588".equals(supplierId)) {
			return aladucaSreviceImpl;
		}
		else if ("2015092401528".equals(supplierId)) {
			return stefaniaService;
		}
		else if ("2015082801463".equals(supplierId)) {
			return sanremoOrderService;
		}
		else if ("2016030901801".equals(supplierId)) {
			return delibertiServiceImpl;
		}
		else if ("2016101401947".equals(supplierId)) {
			return pozzileiArteOrderService;
		}
		else if ("2015092801547".equals(supplierId)) {
			return pozzileiOrderService;
		}
		else if ("2016101401948".equals(supplierId)) {
			return pozzileiForteOrderService;
		}
		else if ("2015092201518".equals(supplierId)) {
			return tonySubOrderImpl;
		}
		else if ("2016032401822".equals(supplierId)) {
			return montiOrderService;
		}
		else if ("2016012801758".equals(supplierId)) {
			return inviqaOrderImpl;
		}
		else if ("2016080901915".equals(supplierId)) {
			return studio69Serviceimpl;
		} else {
			return null;
		}

		// if("2016022401783".equals(supplierId)){
		// return divoServiceImpl;
		// }
		// if("2016092801828".equals(supplierId)){
		// return efashionOrderImpl;
		// }

		//
		// if("2015101001588".equals(supplierId)){
		// return aladucaSreviceImpl;
		// }
		//
		//
		// if("2015091801507".equals(supplierId)){
		// return brunarossoServiceImpl;
		// }
		// if("2016030401795".equals(supplierId)){
		// return creative99ServiceImpl;
		// }
		//
		// IOrderService brunarossoServiceImpl;
		// if("2016011201731".equals(supplierId)){
		// return danielloServiceImpl;
		// }
		//
		//// if("2015112001671".equals(supplierId)){
		//// return dellaServiceimpl;
		//// }
		//
		// if("2015111001656".equals(supplierId)){
		// return genteromaServiceImpl;
		// }
		//
		// if("2015081701441".equals(supplierId)){
		// return leamServiceImpl;
		// }
		// if("2016050401882".equals(supplierId)){
		// return lindeServiceImpl;
		// }
		// if("2015082701461".equals(supplierId)){
		// return ostoreServiceImpl;
		// }

		// if("2015081701439".equals(supplierId)){
		// return spinnakerOrderService;
		// }
		//

		//
		//
		//
		//
		//
		// if("2016083001937".equals(supplierId)){
		// return wiseServiceImpl;
		// }
		// if("2015121801705".equals(supplierId)){
		// return woolrichServiceImpl;
		// }
		// return null;

		// if("2016102701834".equals(supplierId)){
		// return efashionOrderImpl;
		// }else{
		// return null;
		// }

		// if("2016102701830".equals(supplierId)){//无库
		// return testLeamServiceImpl;
		// }else if("2016102701831".equals(supplierId)) { //报错500
		// return testBrunarossoServiceImpl;
		// }else if("2016102701834".equals(supplierId)){ //非锁库 随机返回值
		// return efashionOrderImpl;
		// }else if("2016102701835".equals(supplierId)){//预锁库存随机返回值
		// return tonyOrderImpl;
		// } else if("2016102701836".equals(productDTO.getSupplierId())){
		// return
		// lindeSearchStock.getSearchStock(productDTO.getSupplierSkuNo());
		// }else if("2016102701837".equals(productDTO.getSupplierId())){
		// return
		// montiSearchStock.getSearchStock(productDTO.getSupplierSkuNo());
		// }else if("2016102701838".equals(productDTO.getSupplierId())){
		// return oSearchStock.getSearchStock(productDTO.getSupplierSkuNo());
		// }else if("2016102701839".equals(productDTO.getSupplierId())){
		// return
		// papiniSearchStock.getSearchStock(productDTO.getSupplierSkuNo());
		// }else if("2016102701840".equals(productDTO.getSupplierId())){
		// return
		// pozzileiSearchService.getSearchStock(productDTO.getSupplierSkuNo());
		// }else if("2016102701841".equals(productDTO.getSupplierId())){
		// return
		// sanremoSearchService.getSearchStock(productDTO.getSupplierSkuNo());
		// }else if("2016102701842".equals(productDTO.getSupplierId())){
		// return
		// spinnakerSearchService.getSearchStock(productDTO.getSupplierSkuNo());
		// }else if("2016102701843".equals(productDTO.getSupplierId())){
		// return
		// studioSearchService.getSearchStock(productDTO.getSupplierSkuNo());
		// }else if("2016102701844".equals(productDTO.getSupplierId())){
		// return
		// velaSearchService.getSearchStock(productDTO.getSupplierSkuNo());
		// }else if("2016102701845".equals(productDTO.getSupplierId())){
		// return
		// coltortiSearchService.getSearchStock(productDTO.getSupplierSkuNo());
		// }else if("2016102701846".equals(productDTO.getSupplierId())){
		// return wiseSearchStock.getSearchStock(productDTO.getSupplierSkuNo());
		// }else if("2016102701847".equals(productDTO.getSupplierId())){
		// return divoSearchStock.getSearchStock(productDTO.getSupplierSkuNo());
		// }
		// else if("2016101401947".equals(productDTO.getSupplierId())){
		// return
		// pozzileiArteSearchService.getSearchStock(productDTO.getSupplierSkuNo());
		// }else if("2016101401948".equals(productDTO.getSupplierId())){
		// return
		// pozzileiFortSearchService.getSearchStock(productDTO.getSupplierSkuNo());
		// }
	}

	public SupplierCommon getSupplierProperty(String supplierId) {

		// if("2016102701830".equals(supplierId)){
		// return supplierProperties.getLeam();
		// }else if("2016102701831".equals(supplierId)){
		// return supplierProperties.getBrunarosso();
		// }else if("2016102701834".equals(supplierId)){
		// return supplierProperties.getEfashionConf();
		// }else if("2016102701835".equals(supplierId)){
		// return supplierProperties.getTonyConf();
		// }else{
		// return null;
		// }

		if ("2015102201625".equals(supplierId)) {
			return supplierProperties.getClutcher();
		} else if ("2016030701799".equals(supplierId)) {
			return supplierProperties.getRussoCapri();
		} else if ("2015111001657".equals(supplierId)) {
			return supplierProperties.getEfashionConf();
		} else if ("2015081701441".equals(supplierId)) {
			return supplierProperties.getLeam();
		} else if ("2015081701439".equals(supplierId)) {
			return supplierProperties.getSpinnakerParam();
		} else if ("2015091801507".equals(supplierId)) {
			return supplierProperties.getBrunarosso();
		} else if ("2016030401795".equals(supplierId)) {
			return supplierProperties.getCreative99();
		} else if ("2016011201731".equals(supplierId)) {
			return supplierProperties.getDaniello();
		} else if ("2016022401783".equals(supplierId)) {
			return supplierProperties.getDivo();
		} else if ("2015111001656".equals(supplierId)) {
			return supplierProperties.getGenteroma();
		} else if ("2016050401882".equals(supplierId)) {
			return supplierProperties.getLinde();
		} else if ("2015082701461".equals(supplierId)) {
			return supplierProperties.getOstore();
		} else if ("2016012101751".equals(supplierId)) {
			return supplierProperties.getPaolo();
		} else if ("2016042501870".equals(supplierId)) {
			return supplierProperties.getKix();
		} else if ("2015101501608".equals(supplierId)) {
			return supplierProperties.getTonySub();
		} else if ("2015081701440".equals(supplierId)) {
			return supplierProperties.getColtorti();
		}
		else if ("2015111001657".equals(supplierId)) {
			return supplierProperties.getPapiniConf();
		}
		else if ("2015081701443".equals(supplierId)) {
			return supplierProperties.getVelaParam();
		}
		else if ("2015101001588".equals(supplierId)) {
			return supplierProperties.getAladuca();
		}
		else if ("2015092401528".equals(supplierId)) {
			return supplierProperties.getStefania();
		}
		else if ("2015082801463".equals(supplierId)) {
			return supplierProperties.getSanremoParam();
		}
		else if ("2016030901801".equals(supplierId)) {
			return supplierProperties.getDeliberti();
		}
		else if ("2016101401947".equals(supplierId)) {
			return supplierProperties.getPozzileiArte();
		}
		else if ("2015092801547".equals(supplierId)) {
			return supplierProperties.getPozzileiParam();
		}
		else if ("2016101401948".equals(supplierId)) {
			return supplierProperties.getPozzileiForte();
		}
		else if ("2015092201518".equals(supplierId)) {
			return supplierProperties.getTonySub();
		}
		else if ("2016032401822".equals(supplierId)) {
			return supplierProperties.getMontiParam();
		}
		else if ("2016012801758".equals(supplierId)) {
			return supplierProperties.getInviqaConf();
		}
		else if ("2016080901915".equals(supplierId)) {
			return supplierProperties.getStudio69();
		} else {
			return null;
		}

		// if("2016022401783".equals(supplierId)){
		// return divoServiceImpl;
		// }
		// if("2016092801828".equals(supplierId)){
		// return efashionOrderImpl;
		// }
		// if("2016030901801".equals(supplierId)){
		// return delibertiServiceImpl;
		// }
		// if("2016012801758".equals(supplierId)){
		// return inviqaOrderImpl;
		// }
		// if("2016032401822".equals(supplierId)){
		// return montiOrderService;
		// }
		// if("2015111001657".equals(supplierId)){
		// return papiniOrderService;
		// }

		//
		//
		// if("2015091801507".equals(supplierId)){
		// return brunarossoServiceImpl;
		// }
		//
		//
		//
		// if("2015081701440".equals(supplierId)){
		// return coltortiOrderServiceImpl;
		// }
		//
		// if("2016030401795".equals(supplierId)){
		// return creative99ServiceImpl;
		// }
		//
		// IOrderService brunarossoServiceImpl;
		// if("2016011201731".equals(supplierId)){
		// return danielloServiceImpl;
		// }
		//
		//// if("2015112001671".equals(supplierId)){
		//// return dellaServiceimpl;
		//// }
		//
		// if("2015111001656".equals(supplierId)){
		// return genteromaServiceImpl;
		// }
		//
		// if("2015081701441".equals(supplierId)){
		// return leamServiceImpl;
		// }
		// if("2016050401882".equals(supplierId)){
		// return lindeServiceImpl;
		// }
		// if("2015082701461".equals(supplierId)){
		// return ostoreServiceImpl;
		// }
		// if("2016101401947".equals(supplierId)){
		// return pozzileiArteOrderService;
		// }
		// if("2015092801547".equals(supplierId)){
		// return pozzileiOrderService;
		// }
		// if("2016101401948".equals(supplierId)){
		// return pozzileiForteOrderService;
		// }
		// if("2015082801463".equals(supplierId)){
		// return sanremoOrderService;
		// }
		// if("2015081701439".equals(supplierId)){
		// return spinnakerOrderService;
		// }
		// if("2015092401528".equals(supplierId)){
		// return stefaniaService;
		// }
		// if("2016080901915".equals(supplierId)){
		// return studio69Serviceimpl;
		// }
		// if("2015101501608".equals(supplierId)){
		// return tonyOrderImpl;
		// }
		//
		//
		// if("2015092201518".equals(supplierId)){
		// return tonySubOrderImpl;
		// }
		// if("2015081701443".equals(supplierId)){
		// return velaOrderService;
		// }
		// if("2016083001937".equals(supplierId)){
		// return wiseServiceImpl;
		// }
		// if("2015121801705".equals(supplierId)){
		// return woolrichServiceImpl;
		// }
		// return null;
	}
}
