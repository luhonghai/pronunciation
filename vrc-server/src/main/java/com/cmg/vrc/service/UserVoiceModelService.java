package com.cmg.vrc.service;

import com.cmg.vrc.data.dao.impl.UserVoiceModelDAO;
import com.cmg.vrc.data.jdo.UserVoiceModel;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by lantb on 2015-09-01.
 */
public class UserVoiceModelService {
    private static final Logger logger = Logger.getLogger(UserVoiceModelService.class
            .getName());

    public List<UserVoiceModel> getListByUsernameAndVersion(String username, int version){
        UserVoiceModelDAO dao = new UserVoiceModelDAO();
        List<UserVoiceModel> temp = null;
        try{
            temp = dao.getByUsernameAndVersion(username,version);
        }catch(Exception e){
            logger.warn("Can not get list user voice model with : "+username+ " and version : "+version+" because exception : " + e.getMessage());
        }
        return temp;
    }
}
