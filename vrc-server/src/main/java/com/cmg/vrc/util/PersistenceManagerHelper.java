package com.cmg.vrc.util;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

public class PersistenceManagerHelper {
	private static final String PERSISTENCE_UNIT = "VRC";
	private static PersistenceManagerFactory pmf;

	public static PersistenceManager get() {
		return getDefaultPersistenceManagerFactory().getPersistenceManager();
	}

	protected static PersistenceManagerFactory getDefaultPersistenceManagerFactory() {
		if (pmf == null) {
			pmf = JDOHelper.getPersistenceManagerFactory(PERSISTENCE_UNIT);
		}
		return pmf;
	}
}
