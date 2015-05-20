package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.LicenseCode;
import com.cmg.vrc.data.jdo.LicenseCodeJDO;

/**
 * Created by cmg on 5/18/15.
 */
public class LicenseCodeDAO extends DataAccess<LicenseCodeJDO, LicenseCode> {

    public LicenseCodeDAO() {
        super(LicenseCodeJDO.class, LicenseCode.class);
    }
}
