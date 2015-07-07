package com.cmg.vrc.data.dao.impl.amt;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.amt.RecordedSentence;
import com.cmg.vrc.data.jdo.amt.RecordedSentenceJDO;
import com.cmg.vrc.data.jdo.amt.Transcription;
import com.cmg.vrc.data.jdo.amt.TranscriptionJDO;

/**
 * Created by cmg on 03/07/15.
 */
public class RecordedSentenceDAO extends DataAccess<RecordedSentenceJDO, RecordedSentence> {

    public RecordedSentenceDAO() {
        super(RecordedSentenceJDO.class, RecordedSentence.class);
    }
}
