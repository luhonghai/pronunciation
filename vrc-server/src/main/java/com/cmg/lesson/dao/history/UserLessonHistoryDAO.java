package com.cmg.lesson.dao.history;

import com.cmg.lesson.data.jdo.history.UserLessonHistory;
import com.cmg.lesson.data.jdo.lessons.LessonCollection;
import com.cmg.vrc.data.dao.DataAccess;

import java.util.List;

/**
 * Created by lantb on 2015-10-16.
 */
public class UserLessonHistoryDAO extends DataAccess<UserLessonHistory>{
    public UserLessonHistoryDAO() {
        super(UserLessonHistory.class);
    }
    public UserLessonHistory getUserByIdAndUsername(String id, String username) throws Exception{
        List<UserLessonHistory> userList = list("WHERE id == :1 && username == :2", id, username);
        if (userList != null && userList.size() > 0)
            return userList.get(0);
        return null;
    }
}
