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
	@Qualifier("gaudenziServiceImpl")
	IOrderService gaudenziServiceImpl;

	@Autowired
	@Qualifier("coltortiOrderServiceImpl")
	IOrderService coltortiOrderServiceImpl;

	@Autowired
	@Qualifier("creative99ServiceImpl")
	IOrderService creative99ServiceImpl;

	@Autowired
	@Qualifier("viettiServiceImpl")
	IOrderService viettiServiceImpl;

//	@Autowired
//	@Qualifier("parisiServiceImpl")
//	IOrderService parisiServiceImpl;

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
	@Qualifier("spinnakerPortofinoOrderService")
	IOrderService spinnakerPortofinoOrderService;

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

	@Autowired
	@Qualifier("palomaBarceloOrderImpl")
	IOrderService palomaBarceloOrderImpl;
	@Autowired
	@Qualifier("lamborghiniOrderImpl")
	IOrderService lamborghiniOrderImpl;
	@Autowired
	@Qualifier("tufanoOrderImpl")
	IOrderService tufanoOrderImpl;
	@Autowired
	@Qualifier("lungolivignoOrderService")
	IOrderService lungolivignoOrderService;
	@Autowired
	@Qualifier("carofiglioOrderImpl")
	IOrderService carofiglioOrderImpl;

	@Autowired
	@Qualifier("baseBluServiceImpl")
	IOrderService baseBluServiceImpl;
	@Autowired
	@Qualifier("tessabitServiceImpl")
	IOrderService tessabitServiceImpl;
	@Autowired
	@Qualifier("giglioServiceImpl") 
	IOrderService giglioServiceImpl;
	@Autowired
	@Qualifier("antonacciOrderImpl") 
	IOrderService antonacciOrderImpl;
	@Autowired
	@Qualifier("dlrboutiqueServiceImpl") 
	IOrderService dlrboutiqueServiceImpl;

	@Autowired
	@Qualifier("coccolebimbiOrderImpl") 
	IOrderService coccolebimbiOrderImpl;
	@Autowired
	@Qualifier("zitafabianiServiceImpl") 
	IOrderService zitafabianiServiceImpl;

	@Autowired
	@Qualifier("ylatiServiceImpl")
	IOrderService ylatiServiceImpl;

	@Autowired
	@Qualifier("parisiMailServiceImpl")
	IOrderService parisiMailServiceImpl;

	@Autowired
	@Qualifier("baseBluOrderImpl") 
	IOrderService baseBluOrderImpl;
	
	@Autowired
	@Qualifier("bagheeraOrderService") 
	IOrderService bagheeraOrderService;
	
	@Autowired
	@Qualifier("julianFashionHandler") 
	IOrderService julianFashionHandler;


	@Autowired
	@Qualifier("cocoroseMailService")
	IOrderService cocoroseServiceImpl;
	
	@Autowired
	@Qualifier("dolciTrameOrderService")
	IOrderService dolciTrameOrderService;
	
	@Autowired
	@Qualifier("angeloMinettiServiceImpl")
	IOrderService angeloMinettiServiceImpl;
	
	public IOrderService getHander(String supplierId) {
		//tonySub暂停
//		if ("2015092201518".equals(supplierId)) {
//			return tonySubOrderImpl;
//		}else
		if("2017110901992".equals(supplierId)){
			return angeloMinettiServiceImpl;
		}else if("2017110801991".equals(supplierId)){
			return dolciTrameOrderService;
		}else if ("2017070501977".equals(supplierId)) {
			return baseBluOrderImpl;
		}else if("2017072601979".equals(supplierId)){
			return coccolebimbiOrderImpl;
		}else if ("2015102201625".equals(supplierId)) {// 预锁库存随机返回值
			return clutcherOrderImpl;
		} else if ("2016102401951".equals(supplierId)) {
			return carofiglioOrderImpl;
		} else if ("2016110101955".equals(supplierId)) {
			return lungolivignoOrderService;
		} else if ("2016090601940".equals(supplierId)) {
			return tufanoOrderImpl;
		} else if ("2016030701799".equals(supplierId)) {
			return russoCapriServiceImpl;
		} else if ("2015111001657".equals(supplierId)) {
			return efashionOrderImpl;
		} else if ("2015081701441".equals(supplierId)) {
			return leamServiceImpl;
		} else if ("2016072601910".equals(supplierId)) {
			return viettiServiceImpl;
		} else if("2017060101976".equals(supplierId)){
			return spinnakerPortofinoOrderService;
		}else if ("2015081701439".equals(supplierId)) {
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
		else if ("2016030401797".equals(supplierId)) {
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
		}else if ("2016032401822".equals(supplierId)) {
			return montiOrderService;
		}else if ("2016012801758".equals(supplierId)) {
			return inviqaOrderImpl;
		}else if ("2016080901915".equals(supplierId)) {
			return studio69Serviceimpl;
		}else if ("2016080301912".equals(supplierId)) {
			return lamborghiniOrderImpl;
		}else if ("2016080301913".equals(supplierId)) {
			return palomaBarceloOrderImpl;
		}else if ("2016080801914".equals(supplierId)) {
			return baseBluServiceImpl;
		}else if("2015091701503".equals(supplierId)){
			return tessabitServiceImpl;
		}else if("2016083001937".equals(supplierId)){ 
			return wiseServiceImpl;
		}else if("2015091801508".equals(supplierId)){ 
			return giglioServiceImpl;
		}else if("2016022301781".equals(supplierId)){ 
			return antonacciOrderImpl;
		}else if("2017042601969".equals(supplierId)){
			return dlrboutiqueServiceImpl;
		}else if("2016032101816".equals(supplierId)){
			return zitafabianiServiceImpl;
		}else if("2017071701978".equals(supplierId)){
			return ylatiServiceImpl;
		}else if("2015101201596".equals(supplierId)){
			return parisiMailServiceImpl;
		}else if("2015100701573".equals(supplierId)){
			return bagheeraOrderService;
		}else if ("2015091801501".equals(supplierId)) {
			return gaudenziServiceImpl;
		}else if("2015100501570".equals(supplierId)){
			return julianFashionHandler;
		}else if("2017101101983".equals(supplierId)){
			return cocoroseServiceImpl;
		}else {
			return null;
		}
	}

	public SupplierCommon getSupplierProperty(String supplierId) {
		
		if("2017070501977".equals(supplierId)){
			return supplierProperties.getBaseBluEdsConf();
		}else if("2017110901992".equals(supplierId)){
			return supplierProperties.getAngeloMinetti();
		}else if("2017110801991".equals(supplierId)){
			return supplierProperties.getDolciTrameParam();
		}else if("2017072601979".equals(supplierId)){
			return supplierProperties.getCoccolebimbiParam();
		}else if("2017060101976".equals(supplierId)){
			return supplierProperties.getSpinnakerPortofinoParam();
		}else if ("2015102201625".equals(supplierId)) {
			return supplierProperties.getClutcher();
		}  else if ("2016102401951".equals(supplierId)) {
			return supplierProperties.getCarofiglio();
		} else if ("2016110101955".equals(supplierId)) {
			return supplierProperties.getLungolivigno();
		} else if ("2016090601940".equals(supplierId)) {
			return supplierProperties.getTufano();
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
		}else if ("2016030401797".equals(supplierId)) {
			return supplierProperties.getPapiniConf();
		}else if ("2015081701443".equals(supplierId)) {
			return supplierProperties.getVelaParam();
		}else if ("2015101001588".equals(supplierId)) {
			return supplierProperties.getAladuca();
		}else if ("2015092401528".equals(supplierId)) {
			return supplierProperties.getStefania();
		}else if ("2015082801463".equals(supplierId)) {
			return supplierProperties.getSanremoParam();
		}else if ("2016030901801".equals(supplierId)) {
			return supplierProperties.getDeliberti();
		}else if ("2016101401947".equals(supplierId)) {
			return supplierProperties.getPozzileiArte();
		}else if ("2015092801547".equals(supplierId)) {
			return supplierProperties.getPozzileiParam();
		}else if ("2016101401948".equals(supplierId)) {
			return supplierProperties.getPozzileiForte();
		}else if ("2015092201518".equals(supplierId)) {
			return supplierProperties.getTonySub();
		}else if ("2016032401822".equals(supplierId)) {
			return supplierProperties.getMontiParam();
		}else if ("2016012801758".equals(supplierId)) {
			return supplierProperties.getInviqaConf();
		}else if ("2016080901915".equals(supplierId)) {
			return supplierProperties.getStudio69();
		}else if ("2016080301912".equals(supplierId)) {
			return supplierProperties.getLamborghiniConf();
		}else if ("2016080301913".equals(supplierId)) {
			return supplierProperties.getPalomaBarceloConf();
		}else if ("2016080801914".equals(supplierId)) {
			return supplierProperties.getBaseBlu();
		}else if("2015091701503".equals(supplierId)){
			return supplierProperties.getTessabit();
		}else if("2016022301781".equals(supplierId)){
			return supplierProperties.getAntonacci();
		}else if("2017042601969".equals(supplierId)){
			return supplierProperties.getDlrboutique();
		}else if("2016032101816".equals(supplierId)){
			return supplierProperties.getZitafabiani();
		}else if ("2016072601910".equals(supplierId)) {
			return supplierProperties.getVietti();
		}else if("2015101201596".equals(supplierId)){
			return supplierProperties.getParisi();
		}else if("2017071701978".equals(supplierId)){
			return supplierProperties.getYlati();
		}else if("2015100701573".equals(supplierId)){
			return supplierProperties.getBagheera();
		}else if ("2015091801501".equals(supplierId)) {
			return supplierProperties.getGaudenzi();
		}else if("2015100501570".equals(supplierId)){
			return supplierProperties.getJulianFashion();
		}else if("2017101101983".equals(supplierId)){
			return supplierProperties.getCocorose();
		}
		else {
			return null;
		}
	}
}
