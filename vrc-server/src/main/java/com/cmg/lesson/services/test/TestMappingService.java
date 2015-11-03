package com.cmg.lesson.services.test;

import com.cmg.lesson.dao.lessons.LessonCollectionDAO;
import com.cmg.lesson.dao.test.TestMappingDAO;
import com.cmg.lesson.data.dto.lessons.LessonCollectionDTO;
import com.cmg.lesson.data.dto.test.TestMappingDTO;
import com.cmg.lesson.data.jdo.lessons.LessonCollection;
import com.cmg.lesson.data.jdo.test.Test;
import com.cmg.lesson.data.jdo.test.TestMapping;
import com.cmg.lesson.services.lessons.LessonCollectionService;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CMG Dev156 on 10/16/2015.
 */
public class TestMappingService {
    private static final Logger logger = Logger.getLogger(TestMappingService.class.getName());
    private String SUCCESS = "success";
    private String ERROR = "error";

    /**
     *  use for get max version
     * @return max version in table
     */
    public int getMaxVersion(){
        int version = 0;
        TestMappingDAO dao = new TestMappingDAO();
        try {
            version = dao.getLatestVersion();
        }catch (Exception e){
            logger.info("Can not get max version in table because : " + e.getMessage());
        }
        return version +1;
    }

    /**
     *
     * @param idLessonCollection
     * @param idObjective
     * @return
     * @throws Exception
     */
    public boolean checkExist(String idLessonCollection, String idObjective) throws Exception{
        boolean isExist = false;
        TestMappingDAO dao = new TestMappingDAO();
        isExist = dao.checkExist(idLessonCollection, idObjective);
        return isExist;
    }

    /**
     *
     * @param idLessonCollection
     * @return
     */
    /*public ObjectiveMappingDTO getAllByIDLesson(String idLessonCollection){
        ObjectiveMappingDTO dto = new ObjectiveMappingDTO();
        ObjectiveMappingDAO dao = new ObjectiveMappingDAO();
        try{
            List<LessonMappingQuestion> list = dao.getAllByIDLesson(idLessonCollection);
            if(list!=null && list.size() > 0){
                dto.setData(list);
                dto.setMessage(SUCCESS);
            }else{
                dto.setMessage(ERROR + ":can not get question for this lesson");
            }
        }catch (Exception e){
            dto.setMessage(ERROR + ":can not get question for this lesson");
            logger.error("can not get list question on idLesson : " + idLessonCollection + " because : " + e.getMessage());
        }
        return dto;
    }*/

    /**
     *
     * @param idLesson
     * @return list question
     */
    /*public QuestionDTO listQuestionByIdLesson(String idLesson, String word,String order, int start, int length, int draw){
   // public QuestionDTO listQuestionByIdLesson(String idLesson, String word){
        QuestionDTO questionDTO = new QuestionDTO();
        ObjectiveMappingDAO ObjectiveMappingDAO = new ObjectiveMappingDAO();
        QuestionDAO questionDAO = new QuestionDAO();
        List<String> lstId = new ArrayList<String>();
        try {
            List<LessonMappingQuestion> listLessonMappingQuestions = ObjectiveMappingDAO.getAllByIDLesson(idLesson);
            if(listLessonMappingQuestions!=null && listLessonMappingQuestions.size()>0){
                for(LessonMappingQuestion lmq : listLessonMappingQuestions){
                    lstId.add(lmq.getIdQuestion());
                }
                List<Question> listQuestions = questionDAO.listIn(lstId, word, order, start, length);
                //List<Question> listQuestions = questionDAO.listIn(lstId, word);
                int count = questionDAO.getCountListIn(lstId, word, order, start, length);
                questionDTO.setData(listQuestions);
                questionDTO.setMessage(SUCCESS);
                questionDTO.setDraw(draw);
                questionDTO.setRecordsFiltered((double) count);
                questionDTO.setRecordsTotal((double) count);
            }else{
                questionDTO.setRecordsFiltered(0.0);
                questionDTO.setRecordsTotal(0.0);
                questionDTO.setData(new ArrayList<Question>());
                questionDTO.setDraw(draw);
                questionDTO.setMessage(ERROR + ":can not get question for this lesson");
            }
        } catch (Exception e) {
            questionDTO.setMessage(ERROR + "List question by id lesson : " + idLesson + " false because : " + e.getMessage());
            logger.error("List question by id lesson : " + idLesson + " false because : " + e.getMessage());
            e.printStackTrace();
        }
        return questionDTO;
    }*/

    /**
     *
     * @param idTest
     * @return
     */
    public boolean updateDeleted(String idTest){
        boolean isDelete = false;
        TestMappingDAO dao = new TestMappingDAO();
        try{
            isDelete = dao.updateDeleted(idTest);
        }catch (Exception e){
            logger.debug("can not update delete in database because : " + e.getMessage());
        }
        return isDelete;
    }

    /**
     *
     * @param idLesson
     * @param idTest
     * @return
     */
    public TestMappingDTO updateDeleted(String idTest, String idLesson){
        TestMappingDTO dto = new TestMappingDTO();
        boolean isDelete = false;
        TestMappingDAO dao = new TestMappingDAO();
        try{
            isDelete = dao.updateDeleted(idTest, idLesson);
            if (isDelete){
                dto.setMessage(SUCCESS);
            }else {
                dto.setMessage(ERROR + ": can not delete in database");
            }
        }catch (Exception e){
            dto.setMessage(ERROR + ": can not delete in database because " + e.getMessage());
            logger.debug("can not delete in database because : " + e.getMessage());
        }
        return dto;
    }

    /**
     *
     * @param lessonId
     * @param idTest
     * @return
     */
    public TestMappingDTO addTestMappingLesson(String lessonId, String idTest){
        TestMappingDTO dto = new TestMappingDTO();
        TestMapping obj = new TestMapping();
        TestMappingDAO dao = new TestMappingDAO();
        obj.setIdLessonCollection(lessonId);
        obj.setIdTest(idTest);
        obj.setIsDeleted(false);
        obj.setVersion(getMaxVersion());

        boolean check = false;
        try {
            check = dao.checkExist(obj.getIdLessonCollection(), obj.getIdTest());
            if(check){
                dto.setMessage(ERROR + " : this lesson was already added to Test!");
            }else {
                dao.create(obj);
                dto.setMessage(SUCCESS);
            }
        }catch (Exception e){
            dto.setMessage(ERROR + " : " + e.getMessage());
            logger.error("can not add lesson to Test in db because : " + e.getMessage());
        }
        return dto;
    }


    /**
     *
     * @param idLessons
     * @param idTest
     * @return
     */
    public String addTestMapLesson(List<String> idLessons, String idTest){
        TestMappingDAO dao = new TestMappingDAO();
        String message = "";
        try {
            if(idLessons!=null && idLessons.size() > 0){
                List<TestMapping> temp = new ArrayList<>();
                for(String idL : idLessons){
                    TestMapping obj = new TestMapping();
                    obj.setIdLessonCollection(idL);
                    obj.setIdTest(idTest);
                    obj.setIsDeleted(false);
                    obj.setVersion(getMaxVersion());
                    temp.add(obj);
                }
                dao.create(temp);
                message = SUCCESS;
            }else{
                message = SUCCESS;
            }
        }catch (Exception e){
            message = ERROR + ": can not add Test mapping to lesson in db because " + e.getMessage();
            logger.error("can not add Test mapping to lesson in db because : " + e.getMessage());
        }
        return message;
    }

    /**
     *
     * @param idTest
     * @return
     */
    public LessonCollectionDTO getAllLessonByTest(String idTest){
        LessonCollectionDTO lessonCollectionDTO = new LessonCollectionDTO();
        TestMappingDAO testMappingDAO = new TestMappingDAO();
        LessonCollectionDAO lessonCollectionDAO = new LessonCollectionDAO();
        List<String> lstLessonId = new ArrayList<String>();
        try {
            List<TestMapping> listObjectiveMappings = testMappingDAO.getAllByIdTest(idTest);
            if(listObjectiveMappings!=null && listObjectiveMappings.size()>0){
                for(TestMapping om : listObjectiveMappings){
                    lstLessonId.add(om.getIdLessonCollection());
                }
                List<LessonCollection> listObjectives = lessonCollectionDAO.listIn(lstLessonId);
                lessonCollectionDTO.setData(listObjectives);
                lessonCollectionDTO.setMessage(SUCCESS);
            }else{
                lessonCollectionDTO.setMessage(SUCCESS);
                lessonCollectionDTO.setData(new ArrayList<LessonCollection>());
            }
        } catch (Exception e) {
            lessonCollectionDTO.setMessage(ERROR + " : can not get list lesson by test because " + e.getMessage());
            logger.error("Can not get list lesson by test because : " + idTest + " false because : " + e.getMessage());
            e.printStackTrace();
        }
        return lessonCollectionDTO;
    }

    /**
     *
     * @param idTest
     * @return
     */
    public LessonCollectionDTO getAllLessonNotInTest(String idTest){
        LessonCollectionDTO lessonCollectionDTO = new LessonCollectionDTO();
        TestMappingDAO testMappingDAO = new TestMappingDAO();
        LessonCollectionDAO lessonCollectionDAO = new LessonCollectionDAO();
        List<String> lstLessonId = new ArrayList<String>();
        try {
            List<TestMapping> listObjectiveMappings = testMappingDAO.getAllByIdTest(idTest);
            if(listObjectiveMappings!=null && listObjectiveMappings.size()>0){
                for(TestMapping om : listObjectiveMappings){
                    lstLessonId.add(om.getIdLessonCollection());
                }
                List<LessonCollection> listTest = lessonCollectionDAO.listNotIn(lstLessonId);
                lessonCollectionDTO.setData(listTest);
                lessonCollectionDTO.setMessage(SUCCESS);
            }else{
                lessonCollectionDTO.setMessage(SUCCESS);
                LessonCollectionService lessonCollectionService = new LessonCollectionService();
                LessonCollectionDTO temp = lessonCollectionService.getAll();
                if(temp.getData()!=null && temp.getData().size()>0){
                    lessonCollectionDTO.setData(lessonCollectionService.getAll().getData());
                }else{
                    lessonCollectionDTO.setData(new ArrayList<LessonCollection>());
                }

            }
        } catch (Exception e) {
            lessonCollectionDTO.setMessage(ERROR + " : can not get list lesson by test because " + e.getMessage());
            logger.error("Can not get list lesson by test because : " + idTest + " false because : " + e.getMessage());
            e.printStackTrace();
        }
        return lessonCollectionDTO;
    }

    /**
     *
     * @param idObjective
     * @return
     */
    public LessonCollectionDTO getAllLessonSetChecked(String idObjective){
        LessonCollectionDTO lessonCollectionDTOIn = new LessonCollectionDTO();
        LessonCollectionDTO lessonCollectionDTONotIn = new LessonCollectionDTO();
        lessonCollectionDTOIn = getAllLessonByTest(idObjective);
        lessonCollectionDTONotIn =getAllLessonNotInTest(idObjective);
        lessonCollectionDTOIn.getData().addAll(lessonCollectionDTONotIn.getData());
        return lessonCollectionDTOIn;
    }

    /**
     *
     * @param idTest
     * @return
     */
    public TestMappingDTO getDataForUpdatePopup(String idTest){
        TestMappingDTO testMappingDTO = new TestMappingDTO();
        TestService testService = new TestService();
        Test test= testService.getById(idTest);
        LessonCollectionDTO lessonCollectionDTO = getAllLessonSetChecked(idTest);
        testMappingDTO.setIdTest(test.getId());
        testMappingDTO.setNameTest(test.getName());
        testMappingDTO.setDescriptionTest(test.getDescription());
        testMappingDTO.setPercentPass(test.getPercentPass());
        testMappingDTO.setData(lessonCollectionDTO.getData());
        testMappingDTO.setMessage(lessonCollectionDTO.getMessage());
        return testMappingDTO;
    }
}
