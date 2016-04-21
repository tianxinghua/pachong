
package com.shangpin.iog.common.utils;


import com.shangpin.framework.ServiceException;
import com.shangpin.framework.ServiceMessageException;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 赋值工具,通过反射把vo的值赋给bo

 * @author lzr_rzdy@163.com
 * 
 *
 */
public class InVoke<R,T> {

	/**
	 * 赋值
	 * @param source: 传入的有值对象
	 * @param target :需要被复制的对象
	 * @param rejectProperty target不赋值的属性
	 * @param nonEmptyProperty 非空的属性
	  * @throws
	 */
	@SuppressWarnings("rawtypes")
	public static void setValue(Object source,Object target,String[] rejectProperty,String[]  nonEmptyProperty) throws ServiceException {
	Class returnType=null;
    Class returnTypePo=null;
	Object[] arg = new Object[1];
	Object[] argArray = null;
	Object obj = null;
	Method[] targetMethod;
	Method[] sourceMethod;
	// getMethods 只能是公共的方法
    targetMethod =  target.getClass().getMethods();// po.getClass().getDeclaredMethods();
    sourceMethod = source.getClass().getMethods();//getDeclaredMethods();
	//把vo 的方法放入Map中，以便验证
	String sVoMethodName="";
	Map<String,Method> voMap = new HashMap<String,Method>();
    for(int j=0;j<sourceMethod.length;j++){
    	sVoMethodName="";
    	sVoMethodName=sourceMethod[j].getName();
    	voMap.put(sVoMethodName, sourceMethod[j]);
    	
    }
	int iLeng = targetMethod.length;
	String poMethodName="";
	
	kk:for (int i = 0; i < iLeng; i++) {
		poMethodName = targetMethod[i].getName();
	
		if (poMethodName.startsWith("set")||poMethodName.equals("getClass"))
			continue;
		try {			
			if (poMethodName.startsWith("get")) {
				if(null!=rejectProperty&&rejectProperty.length>0){//排除的属性
					for(int k=0;k<rejectProperty.length;k++){
						if(poMethodName.toLowerCase().equals(("get"+rejectProperty[k]).toLowerCase())){
						    continue kk;	
						}					
					}
				}
				if(voMap.containsKey(poMethodName)){//po 中包含  vo 同样的方法
//					System.out.println("methodName = " + poMethodName );
					obj = ((Method)voMap.get(poMethodName)).invoke(source, argArray);//获得值
					returnType = ((Method)voMap.get(poMethodName)).getReturnType();
                    returnTypePo =    targetMethod[i].getReturnType();

                    if(returnType.toString().equals(returnTypePo.toString())){ //返回类型相同
                        if (obj != null) {//有值
                            //非空判断,因为空值不算有值
                            judgeNonEmptyByValueAndType(poMethodName,nonEmptyProperty,obj,returnType);
                            arg[0] = obj;
                            poMethodName = "set" + poMethodName.substring(3);
//						System.out.println(" method = " + poMethodName );
                            Method method = target.getClass().getMethod(poMethodName,returnType);
                            method.invoke(target, arg);//赋值
                        }else{//没有值,判断是否在非空属性中
                            //非空判断
                            judageNonEmpty(poMethodName,nonEmptyProperty);
                        }
                    }


					
				}else{//vo 中没有此方法，不赋值
					
				}
					
			
			} else {//除了set 和 get 的其他方法
				continue;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new ServiceMessageException("-1","方法异常");
		}
	}



}
    public  void  setListObject(List<R> resourceList,List<T> targetList, Class<? extends  T> cls) throws ServiceException {


            try {
                for(R resource:resourceList) {
                    T  targetObj =  (T) Class.forName(cls.getName()).newInstance();

                    this.setValue(resource,targetObj,null);
                    targetList.add(targetObj);
                }

            } catch (IllegalAccessException  | InstantiationException |ClassNotFoundException  e) {
                throw new ServiceMessageException("对象赋值失败");
            }



    }

	
   /**
    * 获取非空属性
    * @param obj ：类实例
    * @param rejectProperty 排除的方法数组
    * @return ：非空属性的Map key:属性;value:属性对应的值
    */
	
	@SuppressWarnings("rawtypes")
	public static Map<String,Object> getNoNmptyPropertyAndValue(Object obj,String[] rejectProperty) throws ServiceException {
		
		Method[] methodArray;
		Class returnType=null;
		Object[] argArray =null;
		String rtType="";
		// getMethods :只能是公共的方法  getDeclaredMethods(): 不能包含父类  
		methodArray = obj.getClass().getMethods();
		int iLeng = methodArray.length;
		String methodName="",property="";
		Object objValue = null;
		Map<String,Object> map = new HashMap<String,Object>();
		kk:for (int i = 0; i < iLeng; i++) {
			methodName = methodArray[i].getName();		
			if (methodName.startsWith("set")||methodName.equals("getClass"))
				continue;
			try {			
				if (methodName.startsWith("get")) {	
					property = methodName.substring(3);
					property = property.substring(0,1).toLowerCase()+property.substring(1);
					if(null!=rejectProperty&&rejectProperty.length>0){//排除的属性
						for(int k=0;k<rejectProperty.length;k++){
							if(property.equals(rejectProperty[k])){
							    continue kk;	
							}					
						}
					}
					objValue = methodArray[i].invoke(obj, argArray);//获得值			
					returnType = methodArray[i].getReturnType();					
					if (objValue != null) {//有值	
						rtType = returnType.toString();
						if("class java.lang.String".equals(rtType)){
							if(!"".equals((String)objValue)){
								map.put(property, ((String)objValue).trim());
							}							
						}else{							
							
						}
						
					} 					
				
				} else {//除了set 和 get 的其他方法
					continue;
				}
			} catch (Exception ex) {
				ex.printStackTrace();
				throw new ServiceMessageException("Invoke-getNoNmptyPropertyAndValue","方法异常");
			}
		}	
		return map;
		
	}
	
	
	


	/**
	 * 判断方法是否在非空属性中，若在就抛出异常
	 * @param methodName 方法名
	 * @param nonEmptyProperty  非空属性数组
	 * @param obj  传入的值
	 * @param parameterType 传入的值对应的参数
	 * @throws com.shangpin.framework.ServiceMessageException
	 */

	private  static void judgeNonEmptyByValueAndType(String methodName,String[]  nonEmptyProperty,Object obj,@SuppressWarnings("rawtypes") Class parameterType) throws ServiceException {
		if(null!=nonEmptyProperty){
			if(null!=parameterType){
				if("java.lang.String".equals(parameterType.getName())||"java.lang.Character".equals(parameterType.getName())){
					if("".equals(obj)){
						judageNonEmpty(methodName,nonEmptyProperty);
					}
				}

			}
		}
	}
	/**
	 *  判断方法是否在非空属性中，若在就抛出异常
	 * @param methodName
	 * @param nonEmptyProperty
	 * @throws com.shangpin.framework.ServiceMessageException
	 */
	private static void judageNonEmpty(String methodName,String[]  nonEmptyProperty) throws ServiceException {
		if(null!=nonEmptyProperty){
			for(String property:nonEmptyProperty){
				if(methodName.toLowerCase().equals(("get"+property).toLowerCase())){
				    throw new ServiceMessageException("have null value","非空属性-" + property +"-为空");
				}
			}
		}
	}

    /**
     *  根据属性值获得值   */

	private static Object getValue(String property,Map<String,Method> objMap,Object obj) throws ServiceException {
        String method="";
		Object objValue  = null;
		Object[] argArray = null;
		method="get" + property.toLowerCase();
		if(objMap.containsKey(method)){
			try {
				objValue = objMap.get(method).invoke(obj, argArray);//获得值				
			} catch (Exception e) {
				e.printStackTrace();
			}				
		}		
		return objValue;		
	}
	
	
//	class A{
//		A(){};
//		private int i;
//		private Integer it;
//		private String s;
//		private Boolean b;
//		private boolean bb;
//		private Date d;
//		private Character c;
//		public int getI() {
//			return i;
//		}
//		public void setI(int i) {
//			this.i = i;
//		}
//		public String getS() {
//			return s;
//		}
//		public void setS(String s) {
//			this.s = s;
//		}
//		public Boolean getB() {
//			return b;
//		}
//		public void setB(Boolean b) {
//			this.b = b;
//		}
//		public Date getD() {
//			return d;
//		}
//		public void setD(Date d) {
//			this.d = d;
//		}
//		public Character getC() {
//			return c;
//		}
//		public void setC(Character c) {
//			this.c = c;
//		}
//		public boolean isBb() {
//			return bb;
//		}
//		public void setBb(boolean bb) {
//			this.bb = bb;
//		}
//		public Integer getIt() {
//			return it;
//		}
//		public void setIt(Integer it) {
//			this.it = it;
//		}
//		
//		
//	}
//	
//	public void gg(A a){
//		Method[] methodArray;
//		methodArray = a.getClass().getMethods();
//		int iLeng = methodArray.length;
//		String methodName="",property="";		
//		Object objValue = null;		
//		
//		Class returnType=null;
//		for (int i = 0; i < iLeng; i++) {
//			methodName = methodArray[i].getName();		
//			if (methodName.startsWith("set")||methodName.equals("getClass"))
//				continue;
//			try {			
//				if (methodName.startsWith("get")) {	
//					property = methodName.substring(3);
//					property = property.substring(0,1).toLowerCase()+property.substring(1);
//					objValue = methodArray[i].invoke(a, null);//获得值			
//					returnType = methodArray[i].getReturnType();
//					System.out.println("class =" + returnType.getName()+"-");
//				}
//			} catch (Exception ex) {
//					ex.printStackTrace();
//					throw new BOException("Invoke-getNoNmptyPropertyAndValue","方法异常");
//			}
//		}
//					
//	}
	public static void main(String[] arg){


	
    }
	/**
	 * 将sourc中的非null属性值copy到target属性上<br/>
	 * 复杂类型不做迭代复杂
	 * @param source 被复制的源数据
	 * @param target 赋值到的目标数据
	 * @throws Exception 
	 */
	public static void copyNonEmptyProperty(Object source,Object target) throws Exception {
		if(!source.getClass().equals(target.getClass())){
			throw new RuntimeException(" source class:"+source.getClass()+" not same as target class:"+target.getClass());
		}
		Field[] sFields = source.getClass().getDeclaredFields();
		try {
			for (int i = 0; i < sFields.length; i++) {
				String x=sFields[i].getName();
				x=x.substring(0,1).toUpperCase()+x.substring(1);
				Method mGet = source.getClass().getMethod("get"+x);
				Class<?> clz=sFields[i].getType();
				Method mSet=target.getClass().getMethod("set"+x, clz);
				Object value = mGet.invoke(source);//将s中的非null值copy到t对应的属性上
				if(value!=null){
					mSet.invoke(target, value);
				}
			}
		} catch (Exception e) {
			throw e;
		}
	}




    private  void setValue(Object source,Object target,String[] rejectProperty) throws ServiceException {
        Class returnType = null;
        Class returnTypePo = null;
        Object[] arg = new Object[1];
        Object[] argArray = null;
        Object obj = null;
        Method[] targetMethod;
        Method[] sourceMethod;
        // getMethods 只能是公共的方法
        targetMethod = target.getClass().getMethods();// po.getClass().getDeclaredMethods();
        sourceMethod = source.getClass().getMethods();//getDeclaredMethods();
        //把vo 的方法放入Map中，以便验证
        String sVoMethodName = "";
        Map<String, Method> voMap = new HashMap<String, Method>();
        for (int j = 0; j < sourceMethod.length; j++) {
            sVoMethodName = "";
            sVoMethodName = sourceMethod[j].getName();
            voMap.put(sVoMethodName, sourceMethod[j]);

        }
        int iLeng = targetMethod.length;
        String poMethodName = "";

        kk:
        for (int i = 0; i < iLeng; i++) {
            poMethodName = targetMethod[i].getName();

            if (poMethodName.startsWith("set") || poMethodName.equals("getClass"))
                continue;
            try {
                if (poMethodName.startsWith("get")) {
                    if (null != rejectProperty && rejectProperty.length > 0) {//排除的属性
                        for (int k = 0; k < rejectProperty.length; k++) {
                            if (poMethodName.toLowerCase().equals(("get" + rejectProperty[k]).toLowerCase())) {
                                continue kk;
                            }
                        }
                    }
                    if (voMap.containsKey(poMethodName)) {//po 中包含  vo 同样的方法
//					System.out.println("methodName = " + poMethodName );
                        obj = ((Method) voMap.get(poMethodName)).invoke(source, argArray);//获得值
                        returnType = ((Method) voMap.get(poMethodName)).getReturnType();
                        returnTypePo = targetMethod[i].getReturnType();

                        if (returnType.toString().equals(returnTypePo.toString())) { //返回类型相同
                            if (obj != null) {//有值

                                arg[0] = obj;
                                poMethodName = "set" + poMethodName.substring(3);
//						System.out.println(" method = " + poMethodName );
                                Method method = target.getClass().getMethod(poMethodName, returnType);
                                method.invoke(target, arg);//赋值
                            }
                        }


                    } else {//vo 中没有此方法，不赋值

                    }


                } else {//除了set 和 get 的其他方法
                    continue;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new ServiceMessageException("-1", "方法异常");
            }
        }
    }

}
