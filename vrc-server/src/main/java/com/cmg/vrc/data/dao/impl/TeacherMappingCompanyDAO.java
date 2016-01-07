package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.AppDetail;
import com.cmg.vrc.data.jdo.TeacherMappingCompany;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.List;

/**
 * Created by luhonghai on 4/13/15.
 */
public class TeacherMappingCompanyDAO extends DataAccess<TeacherMappingCompany> {

    public TeacherMappingCompanyDAO() {
        super(TeacherMappingCompany.class);
    }

    public List<TeacherMappingCompany> listAll() throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT FROM " + TeacherMappingCompany.class.getCanonicalName());

        try {
            return detachCopyAllList(pm, q.execute());
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }
}
