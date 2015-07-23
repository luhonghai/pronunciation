package com.cmg.vrc.data.dao.impl.amt;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.amt.RecordedSentence;
import com.cmg.vrc.data.jdo.amt.RecordedSentenceJDO;
import com.cmg.vrc.data.jdo.amt.Transcription;
import com.cmg.vrc.data.jdo.amt.TranscriptionJDO;

import java.util.List;

/**
 * Created by cmg on 03/07/15.
 */
public class RecordedSentenceDAO extends DataAccess<RecordedSentenceJDO, RecordedSentence> {

    public RecordedSentenceDAO() {
        super(RecordedSentenceJDO.class, RecordedSentence.class);
    }

    public RecordedSentence getBySentenceIdAndAccount(String sentenceId, String account) throws Exception {
        List<RecordedSentence> list = list("WHERE sentenceId == :1 && account == :2", sentenceId, account);
        if (list != null && list.size() > 0)
            return list.get(0);
        return null;
    }
}
