package com.cmg.merchant.services;

import com.cmg.lesson.dao.objectives.ObjectiveDAO;
import com.cmg.lesson.dao.test.TestDAO;
import com.cmg.lesson.data.jdo.course.CourseMappingDetail;
import com.cmg.lesson.data.jdo.lessons.LessonCollection;
import com.cmg.lesson.data.jdo.objectives.Objective;
import com.cmg.lesson.data.jdo.question.Question;
import com.cmg.lesson.data.jdo.test.Test;
import com.cmg.lesson.data.jdo.test.TestMapping;
import com.cmg.merchant.dao.lessons.LDAO;
import com.cmg.merchant.dao.level.LVMTDAO;
import com.cmg.merchant.dao.objective.ODAO;
import com.cmg.merchant.dao.test.TDAO;
import com.cmg.merchant.dao.test.TMLDAO;
import com.cmg.vrc.util.UUIDGenerator;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by lantb on 2016-03-01.
 */
public class TestServices {
    private static final Logger logger = Logger.getLogger(TestServices.class.getName());
    private String ERROR = "error";
    private String SUCCESS = "success";
    /**
     *  use for get max version
     * @return max version in table
     */
    public int getMaxVersion(){
        int version = 0;
        ObjectiveDAO dao = new ObjectiveDAO();
        try {
            version = dao.getLatestVersion();
        }catch (Exception e){
            logger.info("Can not get max version in table because : " + e.getMessage());
        }
        return version +1;
    }

    /**
     *
     * @param idTest
     * @return
     */
    public String deleteTest(String idTest, String idLevel){
        LVMTDAO lvmtDAO = new LVMTDAO();
        TDAO tDAO = new TDAO();
        try {
            boolean check = lvmtDAO.removeMappingTestWithLevel(idTest, idLevel);
            if(!check){
                return ERROR + ": an error has been occurred in server!";
            }
            check = tDAO.deletedTest(idTest);
            if(!check){
                return ERROR + ": an error has been occurred in server!";
            }
        }catch (Exception e){
            return ERROR + ": an error has been occurred in server!";
        }
        return SUCCESS;
    }

    /**
     *
     * @param idTest
     * @param percentPass
     * @return
     */
     public String updateTest(String idTest, Double percentPass){
       TDAO tdao = new TDAO();
         try {
            boolean check =  tdao.updateTest(idTest, percentPass);
            if(!check){
                return ERROR + " : an error has been occurred in server!";
            }
         }catch (Exception e){
            logger.error("can not update test : " + e);
         }
         return SUCCESS;
     }


    /**
     *
     * @param percentPass
     * @return
     */
    public String addTest(String idLevel, Double percentPass){
        String idTest = UUIDGenerator.generateUUID().toString();
        String idLessons = UUIDGenerator.generateUUID().toString();
        String message = addTestToDb(idTest,percentPass);
        if(message.equalsIgnoreCase(ERROR)){
            return ERROR + ": an error has been occurred in server!";
        }
        message = addLessonsForTest(idLessons);
        if(message.equalsIgnoreCase(ERROR)){
            return ERROR + ": an error has been occurred in server!";
        }
        message = addTESTMappingLESSON(idTest, idLessons);
        if(message.equalsIgnoreCase(ERROR)){
            return ERROR + ": an error has been occurred in server!";
        }
        message = addTESTMappingLEVEL(idLevel,idTest);
        if(message.equalsIgnoreCase(ERROR)){
            return ERROR + ": an error has been occurred in server!";
        }
        return SUCCESS;
    }



    /**
     *
     * @param percentPass
     * @return true if test was added to db.
     */
    public String addTestToDb(String id, double percentPass){
        TDAO dao = new TDAO();
        String message;
        try {
            Test obj = new Test();
            obj.setId(id);
            obj.setName("Test");
            obj.setDescription("" + percentPass);
            obj.setPercentPass(percentPass);
            obj.setVersion(getMaxVersion());
            obj.setDateCreated(new Date(System.currentTimeMillis()));
            obj.setIsDeleted(false);
            dao.create(obj);
            message = SUCCESS;
        }catch(Exception e){
            message = ERROR ;
            logger.error("Can not add Test : "  + " because : " + e.getMessage());
        }

        return message;
    }

    /**
     *
     * @param idLessons
     * @return
     */
    public String addLessonsForTest(String idLessons){
        LDAO dao = new LDAO();
        String message;
        try {
            LessonCollection l = new LessonCollection();
            l.setId(idLessons);
            l.setNameUnique("");
            l.setTitle("");
            l.setName("");
            l.setDescription("");
            l.setVersion(getMaxVersion());
            l.setDateCreated(new Date(System.currentTimeMillis()));
            l.setIsDeleted(false);
            dao.create(l);
            message = SUCCESS;
        }catch(Exception e){
            message = ERROR ;
            logger.error("Can not add lessons : " + " because : " + e.getMessage());
        }

        return message;
    }

    /**
     *
     * @param idTest
     * @param idLessons
     * @return
     */
    public String addTESTMappingLESSON(String idTest, String idLessons){
        TMLDAO dao = new TMLDAO();
        String message;
        try {
            TestMapping tm = new TestMapping();
            tm.setId(UUIDGenerator.generateUUID().toString());
            tm.setIsDeleted(false);
            tm.setIdTest(idTest);
            tm.setIdLessonCollection(idLessons);
            tm.setVersion(getMaxVersion());
            dao.create(tm);
            message = SUCCESS;
        }catch(Exception e){
            message = ERROR ;
            logger.error("Can not add lessons : " + " because : " + e.getMessage());
        }

        return message;
    }

    /**
     *
     * @param idLevel
     * @param idTest
     * @return
     */
    public String addTESTMappingLEVEL(String idLevel, String idTest){
        LVMTDAO dao = new LVMTDAO();
        String message;
        try {
            CourseMappingDetail cmd = new CourseMappingDetail();
            cmd.setId(UUIDGenerator.generateUUID().toString());
            cmd.setIsTest(true);
            cmd.setIdLevel(idLevel);
            cmd.setIdChild(idTest);
            cmd.setIsDeleted(false);
            cmd.setVersion(getMaxVersion());
            cmd.setIndex(1);
            dao.create(cmd);
            message = SUCCESS;
        }catch(Exception e){
            message = ERROR ;
            logger.error("Can not add lessons : " + " because : " + e.getMessage());
        }

        return message;
    }


    /**
     *
     * @param idLevel
     * @return
     */
    public Test getTestByLevelId(String idLevel){
        TDAO dao = new TDAO();
        try {
            Test t = dao.getByIdLevel(idLevel);
            return t;
        }catch (Exception e){
        }
        return null;

    }

    /**
     *
     * @param idLevelMapping
     * @param idTestNeedDuplicated
     * @return
     */
    public String copyTest(String idLevelMapping, String idTestNeedDuplicated){
        TDAO testDAO = new TDAO();
        try {
            Test test = testDAO.getById(idTestNeedDuplicated);
            if(test!=null){
                Test tmp = new Test();
                String newId = UUIDGenerator.generateUUID().toString();
                tmp.setId(newId);
                tmp.setPercentPass(test.getPercentPass());
                tmp.setDateCreated(new Date(System.currentTimeMillis()));
                tmp.setIsDeleted(false);
                tmp.setName("Test");
                tmp.setDescription(""+test.getPercentPass());
                testDAO.create(tmp);
                addTESTMappingLEVEL(idLevelMapping,newId);
                return newId;
            }
        }catch (Exception e){
            return ERROR;
        }
        return ERROR;
    }


}
