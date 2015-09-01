package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.RecordedSentenceHistory;

/**
 * Created by cmg on 03/07/15.
 */
public class RecordedSentenceHistoryDAO extends DataAccess<RecordedSentenceHistory> {

    public RecordedSentenceHistoryDAO() {
        super(RecordedSentenceHistory.class);
    }
}
