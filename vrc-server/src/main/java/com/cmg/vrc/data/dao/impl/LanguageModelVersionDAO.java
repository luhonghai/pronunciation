package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.LanguageModelVersion;

/**
 * Created by cmg on 03/07/15.
 */
public class LanguageModelVersionDAO extends DataAccess<LanguageModelVersion> {

    public LanguageModelVersionDAO() {
        super(LanguageModelVersion.class);
    }
}
