package com.cmg.merchant.services.Sync;

import com.cmg.merchant.dao.teacher.TCHDAO;
import com.cmg.merchant.data.jdo.TeacherCourseHistory;
import com.cmg.vrc.common.Constant;
import com.cmg.vrc.util.AWSHelper;

import java.util.ArrayList;

/**
 * Created by lantb on 2016-04-21.
 */
public class CourseSyncService {


    /**
     *
     * @param username
     * @return
     */
    public ArrayList<TeacherCourseHistory> listCourseByUser(String username){
        TCHDAO dao = new TCHDAO();
        ArrayList<TeacherCourseHistory> list = new ArrayList<>();
        try {
            list = dao.getListCourseByStudent(username);
            if(list!=null && list.size() > 0){
                AWSHelper awsHelper = new AWSHelper();
                for(int i= 0 ; i < list.size(); i++){
                    TeacherCourseHistory tmp  = dao.getLatestFile(list.get(i).getIdCourse());
                    if(tmp!=null){
                        list.get(i).setUrlDownload(awsHelper.generatePresignedUrl(tmp.getPathAws()));
                        list.get(i).setVersion(tmp.getVersion());
                    }
                }
            }
        }catch (Exception e){
        }
        return list;
    }
}
