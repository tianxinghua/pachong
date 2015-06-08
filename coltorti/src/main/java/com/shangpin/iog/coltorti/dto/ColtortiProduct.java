/**
 * 
 */
package com.shangpin.iog.coltorti.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * " product_id, variant, description, price, scalars, "
  + " ms5_group,ms5_subgroup,ms5_category, brand, season, images,"
  + " macro_category,group, subgroup, category, attributes, updated_at")
 * @description 
 * @author 陈小峰
 * <br/>2015年6月4日
 */
@Getter
@Setter
@NoArgsConstructor
public class ColtortiProduct implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6274901727751290321L;
	String skuId;//产品唯一id
	String productId;//产品相当spuid，列如iphone6的id，
	Map<String,String> variant;//变种
	String name;//名字
	String description;//描述
	Float price;//价格
	Map<String,String> scalars; //
	Map<String,String> brand;
	Map<String,String> season;
	List<List<String>> images;
	Map<String,String> family;//性别部分
	Map<String,String> group; //分类部分 group(ms5Group)->ms5_category(subgroup)
	Map<String,String> subgroup;
	Map<String,String> ms5Group;
	Map<String,String> category;
	Map<String,String> ms5Category;
	/*Map<String,String> conaiCategory;
	Map<String,String> macroCategory;*/
	Map<String,Map<String,Object>> attributes;
	Date updatedAt;
	
	Integer stock;
	
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
		if(attributes!=null && attributes.size()>0){
			String v=getAttributeValues("000001");
			if(v!=null)
				return v;
		}
		
		if(!got && variant!=null && variant.size()>0){
				Set<Entry<String, String>> set = this.getVariant().entrySet();
				return set.iterator().next().getValue();
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
				return values.entrySet().iterator().next().getValue();
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
}
