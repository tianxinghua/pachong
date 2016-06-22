package com.shangpin.iog.generater.util.json;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

public class JsonTest {

	public static void main(String[] args) throws Exception {
		FileReader reader = new FileReader(new File("F://json.txt"));

		// JSONArray arry = JSONArray.fromObject(reader);
		// for (int j = 0; j < arry.size(); j++) {
		// JSONObject jsonObject = arry .getJSONObject(j);
		// Map<String, Object> map = new HashMap<String, Object>();
		// for (Iterator<?> iter = jsonObject.keys(); iter.hasNext();)
		// {
		// String key = (String) iter.next();
		//
		// String value = jsonObject.get(key).toString();
		// map.put(key, value);
		// System.out.println(key+"=="+value);
		// }
		// }

		// String url =
		// "https://c1e1d0fa10d3f3dc9ec8390e1aaeb007:0fc94701904077a2fb8eca2da6800522@kix-files.myshopify.com/admin/products.json?collection_id=213136323&limit=2&page=1";
		// String json = HttpUtil45.get(url, new OutTimeConfig(1000*60*10,
		// 1000*60*10, 1000*60*10), null);
		//
		//
		// JSONObject object = JSONObject.fromObject(json);
		// XMLSerializer xmlSerializer = new XMLSerializer();
		//
		// String string = xmlSerializer.write(object);
		// System.out.println(string);

		// HashMap<Object, Object> infoMap = gson.fromJson(reader, new
		// TypeToken<Map<Object, Object>>(){}.getType());
		// for (Entry<Object, Object> entry : infoMap.entrySet()) {
		// System.out.println(entry.getKey());
		// System.out.println(entry.getValue());
		//
		// }

		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(reader);
		new JsonTest().loop(rootNode);
		// JsonFactory jsonFactory = new JsonFactory();
		// JsonParser p = jsonFactory.createParser(new File("F://json.txt"));
		// while(!p.isClosed()){
		// p.nextToken();
		// System.out.println(p.getCurrentToken().name()+"=="+p.getCurrentName()+"::"+p.getText()+"::");
		// }

	}

	public void loop(JsonNode jsonNode) throws Exception {
		System.out.println("==="+jsonNode.toString()+"===");
		Iterator<JsonNode> iterator = jsonNode.iterator();
		while (iterator.hasNext()) {
			JsonNode node = iterator.next();
			if (node.getNodeType() == JsonNodeType.ARRAY) {
				loop(node);
			} else {
				if (node.getNodeType() == JsonNodeType.OBJECT) {
					loop(node);
				}else{
					System.out.println(node.toString());
				}
			}
		}
	}

}
