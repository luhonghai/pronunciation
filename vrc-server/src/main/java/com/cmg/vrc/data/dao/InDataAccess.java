/**
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */

package com.cmg.vrc.data.dao;

import java.util.List;

/** 
 * DOCME
 * 
 * @Creator Hai Lu
 * @author $Author$
 * @version $Revision$
 * @Last changed: $LastChangedDate$
 */

public interface InDataAccess<T> {
	
	public boolean deleteAll() throws Exception;
	
	public boolean put(T obj) throws Exception;
	
	public boolean create(T obj) throws Exception;
	
	public boolean delete(T obj) throws Exception;
	
	public boolean delete(String id) throws Exception;
	
	public boolean update(T obj) throws Exception;
	
	public T getById(String id) throws Exception;
	
	public List<T> listAll() throws Exception;
	
	public List<T> list(String query, Object parameter) throws Exception;
	
	public List<T> list(String query, Object para1, Object para2) throws Exception;

	public List<T> list(String query, Object para1, Object para2 , String order) throws Exception;
	
	public List<T> list(String query, Object para1, Object para2, Object para3) throws Exception;
	
	public List<T> list(String query, Object... parameters) throws Exception;
	
	public List<T> list(String query) throws Exception;

	public List<T> listFilter(String query , List<String> ids) throws Exception;
	
	public boolean checkExistence(String id) throws Exception;

	public double getCount() throws Exception;

	public double getCount(String query, Object... parameters) throws Exception;
}
