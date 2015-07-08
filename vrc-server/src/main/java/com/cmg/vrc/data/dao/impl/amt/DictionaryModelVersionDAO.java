package com.cmg.vrc.data.dao.impl.amt;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.amt.DictionaryVersion;
import com.cmg.vrc.data.jdo.amt.DictionaryVersionJDO;
import com.cmg.vrc.data.jdo.amt.LanguageModelVersion;
import com.cmg.vrc.data.jdo.amt.LanguageModelVersionJDO;

/**
 * Created by cmg on 03/07/15.
 */
public class DictionaryModelVersionDAO extends DataAccess<DictionaryVersionJDO, DictionaryVersion> {

    public DictionaryModelVersionDAO() {
        super(DictionaryVersionJDO.class, DictionaryVersion.class);
    }
}
