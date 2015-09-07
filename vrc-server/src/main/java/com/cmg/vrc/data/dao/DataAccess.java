/**
 * Copyright (c) CMG Ltd All rights reserved.
 *
 * This software is the confidential and proprietary information of CMG
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with CMG.
 */

package com.cmg.vrc.data.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import com.cmg.vrc.data.Mirrorable;
import com.cmg.vrc.util.PersistenceManagerHelper;
import com.cmg.vrc.util.UUIDGenerator;


/**
 *  T is JDO class E is mirror class
 *  
 *  Query with format
        [WHERE <filter>]
        [VARIABLES <variable declarations>]
        [PARAMETERS <parameter declarations>]
        [<import declarations>]
        [GROUP BY <grouping>]
        [ORDER BY <ordering>]
        [RANGE <start>, <end>]
 * 
 * @Creator Hai Lu
 * @author $Author$
 * @version $Revision$
 * @Last changed: $LastChangedDate$
 */
public class DataAccess<T> implements InDataAccess<T> {
	private final Class<T> clazzT;

	/**
	 *  @param clazzT
     */
	public DataAccess(Class<T> clazzT) {
		this.clazzT = clazzT;
	}
	/**
	 * 
	 * @param obj
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 * @throws IOException
	 * @throws DataAccessException
	 */
	protected T from(final T obj) throws IOException,
			DataAccessException {
		verifyObject(obj);
		return obj;
	}
	/**
	 * 
	 * @param obj
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws JsonGenerationException
	 * @throws IOException
	 * @throws DataAccessException
	 */
	protected T to(final T obj) throws IOException, DataAccessException {
		return obj;
	}
	/**
	 * 
	 * @param obj
	 * @throws DataAccessException
	 */
	protected void verifyObject(final Object obj) throws DataAccessException {
		if (obj instanceof Mirrorable) {
			if (((Mirrorable) obj).getId() == null || ((Mirrorable) obj).getId().length() == 0) 
				((Mirrorable) obj).setId(UUIDGenerator.generateUUID());			
		} else {
			throw new DataAccessException(
					"The object must implement interface Mirrorable");
		}
	}
	
	/**
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public boolean put(final T obj) throws Exception {
		verifyObject(obj);
		String id = ((Mirrorable) obj).getId();
		return update(obj);
	}
	/**
	 *
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public boolean create(T obj) throws Exception {
		verifyObject(obj);
		PersistenceManager pm = PersistenceManagerHelper.get();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			pm.makePersistent(obj);
			obj = pm.detachCopy(obj);
			tx.commit();
			return true;
		} catch (Exception e) {
			throw e;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 *
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public T createObj(T obj) throws Exception {
		verifyObject(obj);
		PersistenceManager pm = PersistenceManagerHelper.get();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			pm.makePersistent(obj);
			tx.commit();
			return pm.detachCopy(obj);
		} catch (Exception e) {
			throw e;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}

	/**
	 *
	 * @param objs
	 * @return
	 * @throws Exception
	 */
	public boolean create(List<T> objs) throws Exception {
		PersistenceManager pm = PersistenceManagerHelper.get();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			for (T obj : objs) {
				verifyObject(obj);
				pm.makePersistent(obj);
			}
			tx.commit();
			return true;
		} catch (Exception e) {
			throw e;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	/**
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public boolean delete(T obj) throws Exception {
		verifyObject(obj);
		return delete(((Mirrorable) obj).getId());
	}
	/**
	 *
	 *
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean delete(String id) throws Exception {
		if (!checkExistence(id)) {
			return false;
		}
		PersistenceManager pm = PersistenceManagerHelper.get();
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			T obj = pm.getObjectById(clazzT, id);
			pm.deletePersistent(obj);
			tx.commit();
			return true;
		} catch (Exception e) {
			throw e;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			pm.close();
		}
	}
	/**
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public boolean update(T obj) throws Exception {
		return create(obj);
	}
	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public T getById(String id) throws Exception {
		return getJDOById(id);
	}
	
	public List<T> listAll() throws Exception {
		PersistenceManager pm = PersistenceManagerHelper.get();
		Query q = pm.newQuery(clazzT);
		try {
			return detachCopyAllList (pm, q.execute());
		} catch (Exception e) {
			throw e;
		} finally {
			q.closeAll();
			pm.close();
		}
	}
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public boolean deleteAll() throws Exception {
		PersistenceManager pm = PersistenceManagerHelper.get();
		Transaction tx = pm.currentTransaction();
		Query query = pm.newQuery(clazzT);
		if (query == null)
			return false;
		try {
			tx.begin();
			Object obj = query.execute();
			if (obj != null) {
				List list = (List) obj;
				if (list != null && list.size() > 0) {
					pm.deletePersistentAll(list);
				}
			}
			tx.commit();
			return true;
		} catch (Exception e) {
			throw e;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
			}
			query.closeAll();
			pm.close();
		}
	}
	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	private T getJDOById(String id) throws Exception {
		PersistenceManager pm = PersistenceManagerHelper.get();
		try {
			try {
				T tmp = pm.getObjectById(clazzT, id);
				return (T) pm.detachCopy(tmp);
			} catch (JDOObjectNotFoundException jex) {
			}
		} catch (Exception e) {
			throw e;
		} finally {
			pm.close();
		}
		return null;
	}
	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public boolean checkExistence(String id) throws Exception {
		T obj = getJDOById(id);
		return obj != null;
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cmg.vrc.data.dao.InDataAccess#list(java.lang.String)
	 */
	@Override
	public List<T> list(String query, Object... parameters) throws Exception {
		PersistenceManager pm = PersistenceManagerHelper.get();
		Query q = pm.newQuery("SELECT FROM " + clazzT.getCanonicalName() + " " + query);
		try {
			return detachCopyAllList(pm,  q.execute(parameters));
		} catch (Exception e) {
			throw e;
		} finally {
			q.closeAll();
			pm.close();
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cmg.vrc.data.dao.InDataAccess#list(java.lang.String)
	 */
	@Override
	public List<T> list(String query, Object parameter) throws Exception {
		PersistenceManager pm = PersistenceManagerHelper.get();
		Query q = pm.newQuery("SELECT FROM " + clazzT.getCanonicalName() + " " + query);
		try {
			return detachCopyAllList(pm,  q.execute(parameter));
		} catch (Exception e) {
			throw e;
		} finally {
			q.closeAll();
			pm.close();
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cmg.vrc.data.dao.InDataAccess#list(java.lang.String)
	 */
	@Override
	public List<T> list(String query, Object para1, Object para2) throws Exception {
		PersistenceManager pm = PersistenceManagerHelper.get();
		Query q = pm.newQuery("SELECT FROM " + clazzT.getCanonicalName() + " " + query);
		try {
			return detachCopyAllList(pm, q.execute(para1, para2));
		} catch (Exception e) {
			throw e;
		} finally {
			q.closeAll();
			pm.close();
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cmg.vrc.data.dao.InDataAccess#list(java.lang.String)
	 */
	@Override
	public List<T> list(String query, Object para1, Object para2, Object para3) throws Exception {
		PersistenceManager pm = PersistenceManagerHelper.get();
		Query q = pm.newQuery("SELECT FROM " + clazzT.getCanonicalName() + " " + query);
		try {
			return detachCopyAllList(pm, q.execute(para1, para2, para3));
		} catch (Exception e) {
			throw e;
		} finally {
			q.closeAll();
			pm.close();
		}
	}
	
	/**
	 * (non-Javadoc)
	 * @see com.cmg.vrc.data.dao.InDataAccess#list(java.lang.String)
	 */
	@Override
	public List<T> list(String query) throws Exception {
		PersistenceManager pm = PersistenceManagerHelper.get();
		Query q = pm.newQuery("SELECT FROM " + clazzT.getCanonicalName() + " " + query);
		try {
			//tx.begin();
			return detachCopyAllList(pm,  q.execute());
		} catch (Exception e) {
			throw e;
		} finally {
			q.closeAll();
			pm.close();
		}
	}

	@Override
	public double getCount() throws Exception {
		return getCount("");
	}

	@Override
	public double getCount(String query, Object... parameters) throws Exception {
		PersistenceManager pm = PersistenceManagerHelper.get();
		Long count;
		Query q = pm.newQuery("SELECT COUNT(id) FROM " + clazzT.getCanonicalName() + " " + query);
		try {
			count = (Long) q.execute(parameters);
			return count.doubleValue();
		} catch (Exception e) {
			//throw e;
			return 0;
		} finally {
			q.closeAll();
			pm.close();
		}
	}

	public List<T> detachCopyAllList(PersistenceManager pm, Object obj) {
		return (List<T>) pm.detachCopyAll((List<T>) obj);
	}
}
