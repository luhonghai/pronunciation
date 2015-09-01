package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.DictionaryVersion;

/**
 * Created by cmg on 03/07/15.
 */
public class DictionaryModelVersionDAO extends DataAccess<DictionaryVersion> {

    public DictionaryModelVersionDAO() {
        super(DictionaryVersion.class);
    }
}
