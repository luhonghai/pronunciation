package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.LicenseCodeCompany;

import java.util.List;

/**
 * Created by cmg on 5/18/15.
 */
public class LicenseCodeCompanyDAO extends DataAccess<LicenseCodeCompany> {

    public LicenseCodeCompanyDAO() {
        super(LicenseCodeCompany.class);
    }


    public List<LicenseCodeCompany> listByCompany(String com) throws Exception {
        return list("WHERE company == :1", com);
    }

    public List<LicenseCodeCompany> listAll() throws Exception {
        return list("WHERE isDeleted==false");
    }
}

