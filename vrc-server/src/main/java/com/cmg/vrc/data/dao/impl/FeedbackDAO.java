package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.Feedback;
import com.cmg.vrc.data.jdo.FeedbackJDO;

/**
 * Created by luhonghai on 5/8/15.
 */
public class FeedbackDAO extends DataAccess<FeedbackJDO, Feedback> {

    public FeedbackDAO() {
        super(FeedbackJDO.class, Feedback.class);
    }
}
