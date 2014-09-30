package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.UserVoiceModel;
import com.cmg.vrc.data.jdo.UserVoiceModelJDO;

/**
 * Created by luhonghai on 9/30/14.
 */
public class UserVoiceModelDAO extends DataAccess<UserVoiceModelJDO, UserVoiceModel> {

    public UserVoiceModelDAO() {
        super(UserVoiceModelJDO.class, UserVoiceModel.class);
    }
}
