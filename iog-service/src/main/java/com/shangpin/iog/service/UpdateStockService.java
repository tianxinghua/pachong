package com.shangpin.iog.service;

import java.sql.SQLException;
import java.util.List;

import com.shangpin.iog.dto.StockUpdateDTO;


/**
 * @author monkey
 *
 */
public interface UpdateStockService {
	/**
	 * 更新updateStock的时间
	 */
	public void updateTime(String supplierId) throws SQLException;
	/**
	 * 更新updateStock的状态
	 */
	public void updateStatus(StockUpdateDTO stockUpdateDTO) throws SQLException;
	
	/**
	 * 获取所有的库存更新状态
	 */
	public List<StockUpdateDTO> getAll() throws SQLException;
	/**
	 * 保存updateStock
	 */
	public void saveDTO(StockUpdateDTO stockUpdateDTO) throws SQLException;
	
	/**
	 * 保存updateStock 但是保存之前会判断有没有，若有则不保存
	 * @param stockUpdateDTO
	 * @throws SQLException
	 */
	public void saveStockUpdateDTO(StockUpdateDTO stockUpdateDTO) throws SQLException;
	
	/**
	 * 保存或更新
	 */
	public void saveOrUpdateDTO(StockUpdateDTO stockUpdateDTO) throws SQLException;
	
	/**
	 * 根据supplierId查询表
	 * @param supplierId
	 * @return
	 * @throws SQLException
	 */
	public StockUpdateDTO findStockUpdateBySUpplierId(String supplierId) throws SQLException;
	
}
