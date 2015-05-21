/**
 * <pre>
 * Copyright:		Copyright(C) 2011-2012, feinno.com
 * Filename:		IBaseDao.java 
 * Date:			2014-1-13 
 * Author:			<a href="mailto:sundful@gmail.com">sundful</a>
 * Version          2.0.0
 * Description:		
 *
 * </pre>
 **/
package com.shangpin.iog.dao.base;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author
 * @version 2.0.0
 */
public interface IBaseDao<T> {

	T findById(Serializable id) throws SQLException;
	
	List<T> findAll() throws SQLException; 

	List<T> findAll(Map<String, Object> params) throws SQLException;

	List<T> findListByMap(Map<String, Object> params) throws SQLException;

	List<T> findListPage(Map<String, Object> params, int start, int end)throws SQLException;
	
	Long getCount(Map<String, Object> params) throws SQLException;

	Integer save(T o) throws SQLException;

	Integer saveMap(Map<String, Object> params) throws SQLException;

	Integer saveList(List<T> list) throws SQLException;

	Integer update(T o) throws SQLException;

	Integer updateByMap(Map<String, Object> params) throws SQLException;

	Integer updateDynamic(T o) throws SQLException;

	Integer delete(Serializable id) throws SQLException;

	Integer delete(T o) throws SQLException;
	
	Integer deleteList(List<T> list) throws SQLException;

}
