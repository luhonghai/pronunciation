package com.cmg.vrc.data.dao.impl.amt;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.amt.LanguageModelVersion;
import com.cmg.vrc.data.jdo.amt.LanguageModelVersionJDO;
import com.cmg.vrc.data.jdo.Transcription;
import com.cmg.vrc.data.jdo.TranscriptionJDO;

/**
 * Created by cmg on 03/07/15.
 */
public class LanguageModelVersionDAO extends DataAccess<LanguageModelVersionJDO, LanguageModelVersion> {

    public LanguageModelVersionDAO() {
        super(LanguageModelVersionJDO.class, LanguageModelVersion.class);
    }
}
