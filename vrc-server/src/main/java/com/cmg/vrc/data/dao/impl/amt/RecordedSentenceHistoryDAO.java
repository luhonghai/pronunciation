package com.cmg.vrc.data.dao.impl.amt;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.amt.RecordedSentence;
import com.cmg.vrc.data.jdo.amt.RecordedSentenceHistory;
import com.cmg.vrc.data.jdo.amt.RecordedSentenceHistoryJDO;
import com.cmg.vrc.data.jdo.amt.RecordedSentenceJDO;

/**
 * Created by cmg on 03/07/15.
 */
public class RecordedSentenceHistoryDAO extends DataAccess<RecordedSentenceHistoryJDO, RecordedSentenceHistory> {

    public RecordedSentenceHistoryDAO() {
        super(RecordedSentenceHistoryJDO.class, RecordedSentenceHistory.class);
    }
}
