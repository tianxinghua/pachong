package com.shangpin.iog.dao.serialize;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

import java.io.IOException;

/**
 * java对象对象经过Jackson库转换成JSON字符串自定义类
 * 
 * @author sunweiwei
 * @date
 */
public class NullSerializer extends JsonSerializer<Object> {

	//null的JSON序列  
	 public void serialize(Object value, JsonGenerator jgen,  
             SerializerProvider provider) throws IOException,  
             JsonProcessingException {  
         jgen.writeString("");  
     }
}
