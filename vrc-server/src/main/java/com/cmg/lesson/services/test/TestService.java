package com.cmg.lesson.services.test;

import com.cmg.lesson.dao.course.CourseMappingDetailDAO;
import com.cmg.lesson.dao.test.TestDAO;
import com.cmg.lesson.data.dto.level.LevelDTO;
import com.cmg.lesson.data.dto.test.TestDTO;
import com.cmg.lesson.data.dto.test.TestMappingDTO;
import com.cmg.lesson.data.jdo.course.CourseMappingDetail;
import com.cmg.lesson.data.jdo.test.Test;
import com.cmg.lesson.services.course.CourseMappingDetailService;
import com.cmg.vrc.util.UUIDGenerator;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by CMG Dev156 on 10/14/2015.
 */
public class TestService {
    private static final Logger logger = Logger.getLogger(TestService.class.getName());
    private String SUCCESS = "success";
    private String ERROR = "error";

    /**
     *  use for get max version
     * @return max version in table
     */
    public int getMaxVersion(){
        int version = 0;
        TestDAO dao = new TestDAO();
        try {
            version = dao.getLatestVersion();
        }catch (Exception e){
            logger.info("Can not get max version in table because : " + e.getMessage());
        }
        return version +1;
    }

    /**
     *
     * @param id
     * @param name
     * @param description
     * @return
     */
    public TestDTO updateTest(String id, String name, String description, double percentPass, boolean isUpdateLessonName){
        TestDTO dto = new TestDTO();
        TestDAO dao = new TestDAO();
        String message;
        try{
            if(isUpdateLessonName) {
                if (!isExistTestName(name)) {
                    boolean isUpdate = dao.updateTest(id, name, description, percentPass);
                    if (isUpdate) {
                        message = SUCCESS;
                    } else {
                        message = ERROR + ":" + "An error has been occurred in server!";
                    }
                } else {
                    message = ERROR + ":" + "Test name is existed";
                }
            }else {
                boolean isUpdate = dao.updateDescription(id, description);
                if (isUpdate) {
                    message = SUCCESS;
                } else {
                    message = ERROR + ":" + "An error has been occurred in server!";
                }
            }
        }catch(Exception e){
            message = ERROR + ": "+ e.getMessage();
            logger.error("Can not update Test : " + name + " because : " + e.getMessage());
        }
        dto.setMessage(message);
        return dto;
    }

    /**
     *
     * @param dto
     * @return
     */
    public TestMappingDTO updateTest(TestMappingDTO dto){
        String idTest = dto.getIdTest();
        TestDAO dao = new TestDAO();
        String message="";
        try {
            boolean isUpdate = dao.updateTest(idTest, dto.getNameTest(), dto.getDescriptionTest(), dto.getPercentPass());
            if (isUpdate){
                TestMappingService objMapSer = new TestMappingService();
                objMapSer.updateDeleted(idTest);
                message =  objMapSer.addTestMapLesson(dto.getIdLessons(),dto.getIdTest());
            }
        } catch (Exception e) {
            message = ERROR + ": "+ e.getMessage();
            logger.error("Can not update test : " +  dto.getNameTest() + " because : " + e.getMessage());
        }
        dto.setMessage(message);
        return dto;
    }

    /**
     *
     * @param name
     * @return true if question was added to table.
     */
    public String addTest(String id,String name, String description, double percentPass){
        TestDAO dao = new TestDAO();
        String message;
        try {
            //if(!isExistObjectiveName(name)) {
                Test obj = new Test();
                obj.setId(id);
                obj.setName(name);
                obj.setDescription(description);
                obj.setPercentPass(percentPass);
                obj.setVersion(getMaxVersion());
                obj.setDateCreated(new Date(System.currentTimeMillis()));
                obj.setIsDeleted(false);
                dao.create(obj);
                message = SUCCESS;
            //}else{
                //message = ERROR + ":" + "Objective name is existed";
            //}
        }catch(Exception e){
            message = ERROR + ":" + e.getMessage();
            logger.error("Can not add Test : " + name + " because : " + e.getMessage());
        }

        return message;
    }


    /**
     *
     * @param dto
     * @return
     */
    public TestMappingDTO addTest(TestMappingDTO dto){
        String idTest = UUIDGenerator.generateUUID();
        String message = addTest(idTest, dto.getNameTest(), dto.getDescriptionTest(), dto.getPercentPass());
        if(message.equalsIgnoreCase(SUCCESS)){
            dto.setIdTest(idTest);
            TestMappingService testMapSer = new TestMappingService();
            message =  testMapSer.addTestMapLesson(dto.getIdLessons(), dto.getIdTest());
            if(message.equalsIgnoreCase(SUCCESS)){
                CourseMappingDetailService cmdSer = new CourseMappingDetailService();
                message = cmdSer.addMappingDetail(dto.getIdCourse(),dto.getIdTest(),dto.getIdLevel(),true);
            }
        }
        dto.setMessage(message);
        return dto;
    }

    /**
     *
     * @param id
     * @return
     */
    public TestDTO deleteTest(String id){
        TestDTO dto = new TestDTO();
        TestDAO dao = new TestDAO();
        String message;
        try{
            boolean isDelete=dao.deletedTest(id);
            if (isDelete){
                message = SUCCESS;
            }else{
                message = ERROR + ": " + "An error has been occurred in server!";
            }
        }catch(Exception e){
            message = ERROR + ": "+ e.getMessage();
            logger.error("Can not delete Test id: " + id + " because : " + e.getMessage());
        }
        dto.setMessage(message);
        return dto;
    }


    /**
     *
     * @param id
     * @return
     */
    public TestDTO deleteTestAndLesson(String id){
        TestDTO dto = deleteTest(id);
        if (dto.getMessage().equalsIgnoreCase("success")){
            CourseMappingDetailService courseMappingDetailService = new CourseMappingDetailService();
            String message = courseMappingDetailService.removeDetailByIdChild(id);
            if(message.equalsIgnoreCase("success")){
                TestMappingService testMappingService = new TestMappingService();
                boolean isDelete = testMappingService.updateDeleted(id);
                if (isDelete) {
                    dto.setMessage(SUCCESS);
                }else {
                    dto.setMessage(ERROR + ": can not delete lesson of test id, "+id);
                }
            }else {
                dto.setMessage(message);
            }
        }
        return dto;
    }

    /**
     *
     * @param name
     * @return true is exits question name
     */
    public boolean isExistTestName(String name) throws Exception{
        boolean isExist = false;
        TestDAO dao = new TestDAO();
        isExist = dao.checkExist(name);
        return isExist;
    }

    /**
     *
     * @param id
     * @return
     */
    public Test getById(String id){
        TestDAO dao = new TestDAO();
        try {
            return dao.getById(id);
        }catch (Exception e){
            logger.info("can not get test by id : " + id);
        }
        return null;
    }

    /**
     *
     * @return total rows
     */
    public double getCount(){
        TestDAO dao = new TestDAO();
        try{
            return dao.getCount();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }


    /*public LevelDTO getAllObjAndTest(String idCourse, String idLevel){
        LevelDTO levelDTO = new LevelDTO();
        CourseMappingDetailDAO courseMappingDetailDAO = new CourseMappingDetailDAO();
        ObjectiveDAO objectiveDAO = new ObjectiveDAO();
        List<String> lstObjId = new ArrayList<String>();
        List<String> lstTestId = new ArrayList<String>();
        try {
            List<CourseMappingDetail> listCourseMappingDetails = courseMappingDetailDAO.getAllByLevel(idCourse, idLevel);
            if(listCourseMappingDetails!=null && listCourseMappingDetails.size()>0){
                for(CourseMappingDetail cmp : listCourseMappingDetails){
                    if (cmp.isTest()){
                        lstTestId.add(cmp.getIdChild());
                    }else{
                        lstObjId.add(cmp.getIdChild());
                    }
                }
                List<Objective> listObjective = objectiveDAO.listIn(lstObjId);
                if(listObjective!=null && listObjective.size()>0){
                    List<ObjectiveMappingDTO> listObjectiveMappingDTO = new ArrayList<ObjectiveMappingDTO>();
                    ObjectiveMappingDTO objectiveMappingDTO;
                    for(Objective obj : listObjective){
                        objectiveMappingDTO = new  ObjectiveMappingDTO();
                        objectiveMappingDTO.setIdObjective(obj.getId());
                        objectiveMappingDTO.setIdLevel(idLevel);
                        objectiveMappingDTO.setIdCourse(idCourse);
                        objectiveMappingDTO.setNameObj(obj.getName());
                        objectiveMappingDTO.setDescriptionObj(obj.getDescription());
                        listObjectiveMappingDTO.add(objectiveMappingDTO);
                    }
                    levelDTO.setListObjMap(listObjectiveMappingDTO);
                    levelDTO.setMessage(SUCCESS);
                }
            }else{
                levelDTO.setMessage(SUCCESS);
                levelDTO.setListObjMap(new ArrayList<ObjectiveMappingDTO>());
            }
        } catch (Exception e) {
            levelDTO.setMessage(ERROR + " : can not get list objectives by level because " + e.getMessage());
            logger.error("List object by id level : " + idLevel + " false because : " + e.getMessage());
            e.printStackTrace();
        }
        return levelDTO;
    }*/

}
