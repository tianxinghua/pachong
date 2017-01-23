package com.shangpin.supplier.product.consumer.supplier.coltorti.dto;


import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * <p>Title:ColtortiProduct </p>
 * <p>Description: 实体类 </p>
 * <p>Company: www.shangpin.com</p> 
 * @author lubaijiang
 * @date 2017年1月5日 下午6:34:03
 *
 */
@Getter
@Setter
@NoArgsConstructor
public class ColtortiProduct implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6274901727751290321L;
	private String skuId;//产品唯一id
	private String sizeKeyValue;
	private String productId;//产品相当spuid，列如iphone6的id，
	private Map<String,String> variant;//变种
	private String name;//名字
	private String description;//描述
	private Float price;//价格
    private Integer discountRate;//折扣
	private String[] alternativeIds;
	private Map<String,String> scalars; //
	private Map<String,String> brand;
	private Map<String,String> season;
	private List<String> images;
	private Map<String,String> family;//性别部分
	private Map<String,String> group; //分类部分 group(ms5Group)->ms5_category(subgroup)
	private Map<String,String> subgroup;
	private Map<String,String> ms5Group;
	@SuppressWarnings("unused")
	private Map<String,String> category;
	private Map<String,String> ms5Category;
	private Map<String,Map<String,Object>> attributes;
	private String updatedAt;
	
	private Integer stock;
	private Map<String,Integer> sizeStockMap;
	
	/**
	 * 获取类别分别是大、小类，以','号分隔，没有的用括号中的代替：<br/>
	 * group(ms5Group)
	 * @return
	 */
	public String getCategory(){
		String cat="";
		if(group!=null && group.size()>0){
			cat+=group.entrySet().iterator().next().getValue();
		}else{
			if(ms5Group!=null && ms5Group.size()>0){
				cat+=ms5Group.entrySet().iterator().next().getValue();
			}
		}
		return cat;
	}
	/**
	 * 取值顺序-subgroup(ms5Category)
	 * @return
	 */
	public String getSubCategory(){
		String cat="";
		if(subgroup!=null && subgroup.size()>0){
			cat+=subgroup.entrySet().iterator().next().getValue();
		}else{
			if(ms5Category!=null && ms5Category.size()>0){
				cat+=ms5Category.entrySet().iterator().next().getValue();
			}
		}
		return cat;
	}
	
	/**
	 * 先根据attribute的000001属性获取，得不到着到variant去获取。
	 * @return
	 */
	public String getColor(){
		boolean got=false;

		if(!got && variant!=null && variant.size()>0){
			Set<Entry<String, String>> set = this.getVariant().entrySet();
			return set.iterator().next().getValue();
		}


		if(attributes!=null && attributes.size()>0){
			String v=getAttributeValues("000001");
			if(v!=null)
				return v;
		}
		

		return "";
	}
	/**
	 * 先根据attribute的000001属性获取，得不到着到variant去获取。
	 * @return
	 */
	public String getColorCode(){
		boolean got=false;

		if(!got && variant!=null && variant.size()>0){
			Set<Entry<String, String>> set = this.getVariant().entrySet();
			return set.iterator().next().getKey();
		}


		if(attributes!=null && attributes.size()>0){
			String v=getAttributeValues("000001");
			if(v!=null)
				return v;
		}
		

		return "";
	}
	private String getAttributeValues(String attrKey){
		if(attributes!=null && attributes.size()>0){
			Map<String, Object> attr=attributes.get(attrKey);
			if(attr==null) return null;
			@SuppressWarnings("unchecked")
			Map<String,String> values=(Map<String, String>) attr.get("values");
			if(values!=null ){
//				return values.entrySet().iterator().next().getValue();
				String restult ="";
				for(Iterator<Entry<String,String>> itor = values.entrySet().iterator(); itor.hasNext(); ){
					restult =restult + itor.next().getValue() + " " ;
				}

				return  restult.trim();
			}
		}
		return null;
	}
	
	/**
	 * 获取产地 000004
	 * @return
	 */
	public String getMadIn() {
		return getAttributeValues("000004");
		/*if(attributes!=null && attributes.size()>0){
			Map<String,?> attr=attributes.get("000004");
			if(attr.get("values")!=null ){
				return ((Map<String,String>)attr.get("values")).entrySet().iterator().next().getValue();
			}
		}
		return null;*/
	}
	/**
	 * 获取材质优先使用000005（指明具体组成）然后使用 000003(指明质地) 
	 * @return
	 */
	public String getMaterial() {
		if(attributes!=null && attributes.size()>0){
			String mat=getAttributeValues("000005");
			if(mat!=null) return mat;
			mat=getAttributeValues("000003");
			return mat;
		}
		return null;
	}
	/**
	 * 拿货号
	 * @return
	 */
	public String getProductCode() {
		if(alternativeIds!=null && alternativeIds.length>0){
			for (String pcode : alternativeIds) {
				if(!pcode.equals(productId)){
					return pcode;
				}
			}
		}
		return null;
	}
}
