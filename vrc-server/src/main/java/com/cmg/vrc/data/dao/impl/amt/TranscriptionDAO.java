package com.cmg.vrc.data.dao.impl.amt;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.amt.Transcription;
import com.cmg.vrc.data.jdo.amt.TranscriptionJDO;

import java.util.List;

/**
 * Created by cmg on 03/07/15.
 */
public class TranscriptionDAO extends DataAccess<TranscriptionJDO, Transcription> {

    public TranscriptionDAO() {
        super(TranscriptionJDO.class, Transcription.class);
    }

    public Transcription getBySentence(String sentence) throws Exception {
        List<Transcription> list = list("WHERE sentence == :1", sentence);
        if (list != null && list.size() > 0)
            return list.get(0);
        return null;
    }
}
