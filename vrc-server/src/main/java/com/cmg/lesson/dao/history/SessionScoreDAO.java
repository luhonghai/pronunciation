package com.cmg.lesson.dao.history;

import com.cmg.lesson.data.jdo.history.SessionScore;
import com.cmg.vrc.data.dao.DataAccess;

/**
 * Created by lantb on 2015-10-19.
 */
public class SessionScoreDAO extends DataAccess<SessionScore> {
    public SessionScoreDAO(){super(SessionScore.class);}
}
