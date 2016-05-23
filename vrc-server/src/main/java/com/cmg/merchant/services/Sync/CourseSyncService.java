package com.cmg.merchant.services.Sync;

import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.merchant.dao.course.CDAO;
import com.cmg.merchant.dao.teacher.TCHDAO;
import com.cmg.merchant.data.jdo.TeacherCourseHistory;
import com.cmg.merchant.services.generateSqlite.SqliteService;
import com.cmg.vrc.common.Constant;
import com.cmg.vrc.data.dao.impl.DatabaseVersionDAO;
import com.cmg.vrc.data.jdo.DatabaseVersion;
import com.cmg.vrc.util.AWSHelper;
import com.cmg.vrc.util.StringUtil;

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
            TeacherCourseHistory cmg = courseCMG();
            if(cmg!=null){
                list.add(cmg);
            }
            ArrayList<TeacherCourseHistory> listTmp = dao.getListCourseByStudent(username);
            if(listTmp!=null && listTmp.size() > 0){
                AWSHelper awsHelper = new AWSHelper();
                for(int i= 0 ; i < listTmp.size(); i++){
                    TeacherCourseHistory tmp  = dao.getLatestFile(listTmp.get(i).getIdCourse());
                    if(tmp!=null){
                        listTmp.get(i).setUrlDownload(awsHelper.generatePresignedUrl(Constant.FOLDER_DATABASE
                                + "/" + tmp.getPathAws()));
                        listTmp.get(i).setVersion(tmp.getVersion());
                    }
                }
                list.addAll(listTmp);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }


    public TeacherCourseHistory courseCMG(){
        return courseCMG(false);
    }
    /**
     *
     * @return
     */
    public TeacherCourseHistory courseCMG(boolean forceGenerate){
        TCHDAO dao = new TCHDAO();
        AWSHelper awsHelper = new AWSHelper();
        CDAO cDao = new CDAO();
        try {
            Course course = cDao.getByName(com.cmg.merchant.common.Constant.CMG_COURSE_DEMO);
            if(course != null){
                TeacherCourseHistory tmp  = dao.getLatestFile(course.getId());
                if (tmp == null || forceGenerate) {
                    SqliteService sqliteService = new SqliteService(course.getId());
                    sqliteService.clearData();
                    tmp = dao.getLatestFile(course.getId());
                }
                if(tmp != null){
                    tmp.setUrlDownload(awsHelper.generatePresignedUrl(Constant.FOLDER_DATABASE
                            + "/" + tmp.getPathAws()));
                    tmp.setVersion(tmp.getVersion());
                    tmp.setCmgCourse(true);
                    tmp.setName("demo");
                }
//                DatabaseVersion db = databaseVersionDAO.getSelectedVersion();
//                TeacherCourseHistory tmp = new TeacherCourseHistory();
//                int version = db.getVersion();
//                String url = awsHelper.generatePresignedUrl(Constant.FOLDER_DATABASE
//                        + "/"
//                        + db.getFileName());
//                tmp.setVersion(version);
//                tmp.setUrlDownload(url);

//                tmp.setIdCourse(course.getId());
                return tmp;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
