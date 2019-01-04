package com.shangpin.iog.webcontainer.front.strategy;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.shangpin.iog.dto.ProductDTO;
/**
 * 图片命名策略接口
 * @author Administrator
 */
public interface IStrategy {
	public Map<String,List<File>> generateName(List<ProductDTO> pList);
}
