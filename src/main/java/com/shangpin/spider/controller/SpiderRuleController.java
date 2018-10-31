package com.shangpin.spider.controller;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shangpin.spider.common.Constants;
import com.shangpin.spider.entity.base.Result;
import com.shangpin.spider.entity.gather.CrawlResult;
import com.shangpin.spider.entity.gather.SpiderRules;
import com.shangpin.spider.entity.gather.SpiderWhiteInfo;
import com.shangpin.spider.service.gather.CrawlService;
import com.shangpin.spider.service.gather.SpiderRulesService;
import com.shangpin.spider.service.gather.SpiderWhiteService;
import com.shangpin.spider.utils.common.PoiExportUtil;
import com.shangpin.spider.utils.common.ReflectUtil;
import com.shangpin.spider.utils.shiro.ContentRole;


/** 
 * @author  njt 
 * @date 创建时间：2017年11月17日 上午9:49:21 
 * @version 1.0 
 * @parameter  
 */
@Controller
@RequestMapping("/spider")
public class SpiderRuleController {
	private Logger LOG = LoggerFactory.getLogger(SpiderRuleController.class);
	
	@Autowired
	private SpiderRulesService spiderRulesService;
	@Autowired
	private SpiderWhiteService spiderWhiteService;
	@Autowired
	private CrawlService crawlService;
	private final String formatDateStr = "yyyy-MM-dd HH:mm:ss";
	/*@Autowired
	private SpiderProxyInfoService spiderProxyService;*/
	
	/*@Autowired
	private CategoryInfoService categoryService;*/
	
	@RequestMapping("toWebSitePage")
	public String toWebSitePage(Model model){
		JSONArray roleArray = ContentRole.queryRole();
		JSONArray fieldArray = ReflectUtil.getAllField(CrawlResult.class.getName());
		model.addAttribute("roleArray", roleArray);
		model.addAttribute("fieldArray", fieldArray);
		return "spider/webSite";
	}
	
	@RequestMapping("getWebSiteList")
	@ResponseBody
	public Result<SpiderWhiteInfo> getWebSiteList(@RequestParam(defaultValue = "1")Integer page,@RequestParam(defaultValue = "10")Integer rows){
		Result<SpiderWhiteInfo> result = spiderWhiteService.getWebSiteList(page,rows);
		return result;
	}
	
	@RequestMapping("getRuleById")
	@ResponseBody
	public JSONObject getRuleById(Long id){
		JSONObject obj = spiderRulesService.getRuleById(id);
		/*JSONArray proxyArray = spiderProxyService.getProxyInfo();
		List<CategoryInfo> categoryArray = categoryService.selectAll();
		if(proxyArray!=null){
			obj.put("proxyArray", proxyArray);
		}else{
			obj.put("proxyArray", "");
		}
		if(categoryArray!=null){
			obj.put("categoryArray", categoryArray);
		}else{
			obj.put("categoryArray", "");
		}*/
		return obj;
	}
	
	@RequestMapping("saveWebRule")
	@ResponseBody
	public int saveWebRule(SpiderRules spiderRuleInfo,Long spiderRuleId){
		int result = spiderRulesService.saveWebRule(spiderRuleInfo,spiderRuleId);
		return result;
	}
	
	@RequestMapping("queryRuleId")
	@ResponseBody
	public JSONObject queryRuleId(Long id){
		JSONObject object = spiderRulesService.queryRuleId(id);
		return object;
	}
	
	@RequestMapping("editWebSite")
	@ResponseBody
	public JSONObject editWebSite(String oper,SpiderWhiteInfo whiteInfo){
		JSONObject resultObj = new JSONObject();
		
		if (Constants.EDIT.equals(oper)) {
		      try {
		    	  spiderWhiteService.updateWhiteInfo(whiteInfo);
		        resultObj.put("status", "success");
		      } catch (Exception e) {
		        resultObj.put("status", "error");
		        e.printStackTrace();
		        LOG.error("----修改源网站有误！");
		      }
		}
		
		if (Constants.ADD.equals(oper)) {
			try {
				resultObj.put("status", "success");
				spiderWhiteService.addWhiteInfo(whiteInfo);
			} catch (Exception e) {
				resultObj.put("status", "error");
				e.printStackTrace();
				LOG.error("----添加源网站有误！");
			}
		}

		return resultObj;

	}
	
	@RequestMapping(value = "export",method = RequestMethod.GET)
	public void export(@RequestParam(name = "whiteName")String whiteName,
			HttpServletResponse response){
		LOG.info("-----导出数据--网站（表名）："+whiteName);
		SimpleDateFormat format = new SimpleDateFormat(formatDateStr);
		String nowDate = format.format(new Date());
		// 定义表的标题
        String title = whiteName+"_DATA_"+nowDate;
        //定义表的列名
        String[] rowsName = new String[] { "gender", "brand", "category", "spu", "product_model",
                "season", "material", "color", "size", "proName", "国外市场价", "国内市场价", "salePrice"
                , "qty", "made", "desc", "pics", "detailLink", "measurement", "supplierId", "supplierNo"
                , "supplierSkuNo", "channel"};
        //定义表的内容
        List<Object[]> dataList = new ArrayList<Object[]>();
        Object[] objs = null;
        List<CrawlResult> list = crawlService.selectAll(whiteName);
        
//      List<GreatBenefitsInfo> list = loanInvestorRecordService.getGreatBenefitsRecord(columnStr,searchText,startTime,endTime);
        for (int i = 0; i < list.size(); i++) {
        	CrawlResult rec = list.get(i);
//        	Date lastInvestTime = rec.getLastInvestTime();
//        	String lastInvestTimeStr = format.format(lastInvestTime);
            objs = new Object[rowsName.length];
            objs[0] = rec.getGender()==""?"无":rec.getGender();
            objs[1] = rec.getBrand()==""?"无":rec.getBrand();
            objs[2] = rec.getCategory()==""?"无":rec.getCategory();
            objs[3] = rec.getSpu()==""?"无":rec.getSpu();
            objs[4] = rec.getProductModel()==""?"无":rec.getProductModel();
            objs[5] = rec.getSeason()==""?"无":rec.getSeason();
            objs[6] = rec.getMaterial()==""?"无":rec.getMaterial();
            objs[7] = rec.getColor()==""?"无":rec.getColor();
            objs[8] = rec.getSize()==""?"无":rec.getSize();
            objs[9] = rec.getProName()==""?"无":rec.getProName();
            objs[10] = rec.getForeignPrice()==BigDecimal.ZERO?"0":rec.getForeignPrice();
            objs[11] = rec.getDomesticPrice()==BigDecimal.ZERO?"0":rec.getDomesticPrice();
            objs[12] = rec.getSalePrice()==BigDecimal.ZERO?"0":rec.getSalePrice();
            objs[13] = rec.getQty()==""?"无":rec.getQty();
            objs[14] = rec.getMade()==""?"无":rec.getMade();
            objs[15] = rec.getDescription()==null?"无":rec.getDescription();
            objs[16] = rec.getPics()==null?"无":rec.getPics();
            objs[17] = rec.getDetailLink()==""?"无":rec.getDetailLink();
            objs[18] = rec.getMeasurement()==""?"无":rec.getMeasurement();
            objs[19] = rec.getSupplierId()==""?"无":rec.getSupplierId();
            objs[20] = rec.getSupplierNo()==""?"无":rec.getSupplierNo();
            objs[21] = rec.getSupplierSkuNo()==""?"无":rec.getSupplierSkuNo();
            objs[22] = rec.getChannel()==""?"无":rec.getChannel();
            dataList.add(objs);
        }
        
        // 创建ExportExcel对象
        PoiExportUtil ex = new PoiExportUtil(title, rowsName, dataList);
        
        // 输出Excel文件
        try {
            OutputStream output = response.getOutputStream();
            title = new String(title.getBytes("gbk"),"iso8859-1");
            response.reset();
            response.setHeader("Content-disposition",
                    "attachment; filename="+title+".xls");
            response.setContentType("application/msexcel");
            ex.export(output);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

	}
	
	/*@RequestMapping("toIpProxy")
	public String toIpProxy(){
		return "spider/ipProxy";
	}
	
	@RequestMapping("ipProxyList")
	@ResponseBody
	public Result<SpiderProxyInfo> ipProxyList(@RequestParam(defaultValue = "1")Integer page,@RequestParam(defaultValue = "10")Integer rows){
		Result<SpiderProxyInfo> result = spiderProxyService.ipProxyList(page,rows);
		return result;
	}
	
	@RequestMapping("editIpProxy")
	@ResponseBody
	public JSONObject editIpProxy(String oper, SpiderProxyInfo proxyInfo){
		JSONObject resultObj = new JSONObject();
	    if (Constants.EDIT.equals(oper)) {
	      try {
	    	  spiderProxyService.updateIpProxy(proxyInfo);
	        resultObj.put("status", "success");
	      } catch (Exception e) {
	        resultObj.put("status", "error");
	        e.printStackTrace();
	        LOG.error("----修改IP代理有误！");
	      }
	    }

	    if (Constants.ADD.equals(oper)) {
	      try {
	    	  spiderProxyService.addIpProxy(proxyInfo);
	    	  resultObj.put("status", "success");
	      } catch (Exception e) {
	        resultObj.put("status", "error");
	        e.printStackTrace();
	        LOG.error("----新增IP代理有误！");
	      }
	    }
	    return resultObj;
	}
	*/
}
