package com.cmg.lesson.services.objectives;

import com.cmg.lesson.dao.course.CourseMappingDetailDAO;
import com.cmg.lesson.dao.lessons.LessonCollectionDAO;
import com.cmg.lesson.dao.objectives.ObjectiveDAO;
import com.cmg.lesson.dao.objectives.ObjectiveMappingDAO;
import com.cmg.lesson.dao.question.QuestionDAO;
import com.cmg.lesson.data.dto.lessons.LessonCollectionDTO;
import com.cmg.lesson.data.dto.objectives.IndexLesson;
import com.cmg.lesson.data.dto.objectives.ObjectiveMappingDTO;
import com.cmg.lesson.data.dto.question.QuestionDTO;
import com.cmg.lesson.data.jdo.course.CourseMappingDetail;
import com.cmg.lesson.data.jdo.lessons.LessonCollection;
import com.cmg.lesson.data.jdo.lessons.LessonMappingQuestion;
import com.cmg.lesson.data.jdo.objectives.Objective;
import com.cmg.lesson.data.jdo.objectives.ObjectiveMapping;
import com.cmg.lesson.data.jdo.question.Question;

import com.cmg.lesson.services.lessons.LessonCollectionService;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CMG Dev156 on 10/16/2015.
 */
public class ObjectiveMappingService {
    private static final Logger logger = Logger.getLogger(ObjectiveMappingService.class.getName());
    private String SUCCESS = "success";
    private String ERROR = "error";

    /**
     *  use for get max version
     * @return max version in table
     */
    public int getMaxVersion(){
        int version = 0;
        ObjectiveMappingDAO dao = new ObjectiveMappingDAO();
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
        ObjectiveMappingDAO dao = new ObjectiveMappingDAO();
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
     * @param idObjective
     * @return
     */
    public boolean updateDeleted(String idObjective){
        boolean isDelete = false;
        ObjectiveMappingDAO dao = new ObjectiveMappingDAO();
        try{
            isDelete = dao.updateDeleted(idObjective);
        }catch (Exception e){
            logger.debug("can not update delete in database because : " + e.getMessage());
        }
        return isDelete;
    }

    /**
     *
     * @param idLesson
     * @param idObjective
     * @return
     */
    public ObjectiveMappingDTO updateDeleted(String idObjective, String idLesson){
        ObjectiveMappingDTO dto = new ObjectiveMappingDTO();
        boolean isDelete = false;
        ObjectiveMappingDAO dao = new ObjectiveMappingDAO();
        try{
            isDelete = dao.updateDeleted(idObjective, idLesson);
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
     * @param objectiveId
     * @return
     */
    public ObjectiveMappingDTO addObjectiveMappingLesson(String lessonId, String objectiveId){
        ObjectiveMappingDTO dto = new ObjectiveMappingDTO();
        ObjectiveMapping obj = new ObjectiveMapping();
        ObjectiveMappingDAO dao = new ObjectiveMappingDAO();
        obj.setIdLessonCollection(lessonId);
        obj.setIdObjective(objectiveId);
        obj.setIsDeleted(false);
        obj.setVersion(getMaxVersion());

        boolean check = false;
        try {
            check = dao.checkExist(obj.getIdLessonCollection(), obj.getIdObjective());
            if(check){
                dto.setMessage(ERROR + " : this question was already added to lesson!");
            }else {
                dao.create(obj);
                dto.setMessage(SUCCESS);
            }
        }catch (Exception e){
            dto.setMessage(ERROR + " : " + e.getMessage());
            logger.error("can not add question to lesson in db because : " + e.getMessage());
        }
        return dto;
    }


    /**
     *
     * @param idLessons
     * @param idObj
     * @return
     */
    public String addObjMapLesson(List<IndexLesson> idLessons, String idObj){
        ObjectiveMappingDAO dao = new ObjectiveMappingDAO();
        String message = "";
        try {
            if(idLessons!=null && idLessons.size() > 0){
                List<ObjectiveMapping> temp = new ArrayList<>();
                for(IndexLesson idL : idLessons){
                    ObjectiveMapping obj = new ObjectiveMapping();
                    obj.setIdLessonCollection(idL.getIdLesson());
                    obj.setIdObjective(idObj);
                    obj.setIsDeleted(false);
                    obj.setVersion(getMaxVersion());
                    obj.setIndex(idL.getIndexLesson());
                    temp.add(obj);
                }
                dao.create(temp);
                message = SUCCESS;
            }else{
                message = SUCCESS;
            }
        }catch (Exception e){
            message = ERROR + ": can not add object mapping to lesson in db because " + e.getMessage();
            logger.error("can not add object mapping to lesson in db because : " + e.getMessage());
        }
        return message;
    }

    /**
     *
     * @param idObjective
     * @return
     */
    public LessonCollectionDTO getAllLessonByObjective(String idObjective){
        LessonCollectionDTO lessonCollectionDTO = new LessonCollectionDTO();
        ObjectiveMappingDAO objectiveMappingDAO = new ObjectiveMappingDAO();
        LessonCollectionDAO lessonCollectionDAO = new LessonCollectionDAO();
        List<String> lstLessonId = new ArrayList<String>();
        try {
            List<ObjectiveMapping> listObjectiveMappings = objectiveMappingDAO.getAllByIdObjective(idObjective);
            if(listObjectiveMappings!=null && listObjectiveMappings.size()>0){
                for(ObjectiveMapping om : listObjectiveMappings){
                    lstLessonId.add(om.getIdLessonCollection());
                }
                List<LessonCollection> listObjectives = lessonCollectionDAO.listIn(idObjective);
                lessonCollectionDTO.setData(listObjectives);
                lessonCollectionDTO.setMessage(SUCCESS);
            }else{
                lessonCollectionDTO.setMessage(SUCCESS);
                lessonCollectionDTO.setData(new ArrayList<LessonCollection>());
            }
        } catch (Exception e) {
            lessonCollectionDTO.setMessage(ERROR + " : can not get list lesson by objective because " + e.getMessage());
            logger.error("Can not get list lesson by objective because : " + idObjective + " false because : " + e.getMessage());
            e.printStackTrace();
        }
        return lessonCollectionDTO;
    }

    /**
     *
     * @param idObjective
     * @return
     */
    public LessonCollectionDTO getAllLessonNotInObjective(String idObjective){
        LessonCollectionDTO lessonCollectionDTO = new LessonCollectionDTO();
        ObjectiveMappingDAO objectiveMappingDAO = new ObjectiveMappingDAO();
        LessonCollectionDAO lessonCollectionDAO = new LessonCollectionDAO();
        List<String> lstLessonId = new ArrayList<String>();
        try {
            List<ObjectiveMapping> listObjectiveMappings = objectiveMappingDAO.getAllByIdObjective(idObjective);
            if(listObjectiveMappings!=null && listObjectiveMappings.size()>0){
                for(ObjectiveMapping om : listObjectiveMappings){
                    lstLessonId.add(om.getIdLessonCollection());
                }
                List<LessonCollection> listObjectives = lessonCollectionDAO.listNotIn(lstLessonId);
                lessonCollectionDTO.setData(listObjectives);
                lessonCollectionDTO.setMessage(SUCCESS);
            }else{
                lessonCollectionDTO.setMessage(SUCCESS);
                LessonCollectionService lessonCollectionService = new LessonCollectionService();
                LessonCollectionDTO temp = lessonCollectionService.getAll();
                if(temp.getData()!=null && temp.getData().size() > 0 ){
                    lessonCollectionDTO.setData(temp.getData());
                }else{
                    lessonCollectionDTO.setData(new ArrayList<LessonCollection>());
                }

            }
        } catch (Exception e) {
            lessonCollectionDTO.setMessage(ERROR + " : can not get list lesson by objective because " + e.getMessage());
            logger.error("Can not get list lesson by objective because : " + idObjective + " false because : " + e.getMessage());
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
        lessonCollectionDTOIn = getAllLessonByObjective(idObjective);
        lessonCollectionDTONotIn = getAllLessonNotInObjective(idObjective);
        lessonCollectionDTOIn.getData().addAll(lessonCollectionDTONotIn.getData());
        return lessonCollectionDTOIn;
    }

    /**
     *
     * @param idObjective
     * @return
     */
    public ObjectiveMappingDTO getDataForUpdatePopup(String idObjective,String idLevel){
        ObjectiveMappingDTO objectiveMappingDTO = new ObjectiveMappingDTO();
        CourseMappingDetail courseMappingDetail=new CourseMappingDetail();
        CourseMappingDetailDAO courseMappingDetailDAO = new CourseMappingDetailDAO();
        ObjectiveService objectiveService = new ObjectiveService();
        Objective objective= objectiveService.getById(idObjective);
        try {
             courseMappingDetail=courseMappingDetailDAO.getByIdObjAndIdLevel(idObjective,idLevel);
        } catch (Exception e) {
            e.printStackTrace();
        }
        LessonCollectionDTO lessonCollectionDTO = getAllLessonSetChecked(idObjective);
        objectiveMappingDTO.setIdObjective(objective.getId());
        objectiveMappingDTO.setNameObj(objective.getName());
        objectiveMappingDTO.setIndex(courseMappingDetail.getIndex());
        objectiveMappingDTO.setDescriptionObj(objective.getDescription());
        objectiveMappingDTO.setData(lessonCollectionDTO.getData());
        objectiveMappingDTO.setMessage(lessonCollectionDTO.getMessage());
        return objectiveMappingDTO;
    }
}
