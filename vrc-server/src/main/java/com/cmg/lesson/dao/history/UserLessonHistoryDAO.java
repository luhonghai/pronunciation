package com.cmg.lesson.dao.history;

import com.cmg.lesson.data.jdo.history.UserLessonHistory;
import com.cmg.lesson.data.jdo.lessons.LessonCollection;
import com.cmg.vrc.data.dao.DataAccess;

/**
 * Created by lantb on 2015-10-16.
 */
public class UserLessonHistoryDAO extends DataAccess<UserLessonHistory>{
    public UserLessonHistoryDAO() {
        super(UserLessonHistory.class);
    }
}
