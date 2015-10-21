package com.cmg.lesson.services.lessons;

import com.cmg.lesson.dao.lessons.LessonMappingQuestionDAO;
import com.cmg.lesson.dao.question.QuestionDAO;
import com.cmg.lesson.data.dto.lessons.LessonMappingQuestionDTO;
import com.cmg.lesson.data.dto.question.QuestionDTO;
import com.cmg.lesson.data.jdo.lessons.LessonMappingQuestion;
import com.cmg.lesson.data.jdo.question.Question;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CMG Dev156 on 10/16/2015.
 */
public class LessonMappingQuestionService {
    private static final Logger logger = Logger.getLogger(LessonCollectionService.class.getName());
    private String SUCCESS = "success";
    private String ERROR = "error";

    /**
     *  use for get max version
     * @return max version in table
     */
    public int getMaxVersion(){
        int version = 0;
        LessonMappingQuestionDAO dao = new LessonMappingQuestionDAO();
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
     * @param idQuestion
     * @return
     * @throws Exception
     */
    public boolean checkExist(String idLessonCollection, String idQuestion) throws Exception{
        boolean isExist = false;
        LessonMappingQuestionDAO dao = new LessonMappingQuestionDAO();
        isExist = dao.checkExist(idLessonCollection, idQuestion);
        return isExist;
    }

    /**
     *
     * @param idLessonCollection
     * @return
     */
    public LessonMappingQuestionDTO getAllByIDLesson(String idLessonCollection){
        LessonMappingQuestionDTO dto = new LessonMappingQuestionDTO();
        LessonMappingQuestionDAO dao = new LessonMappingQuestionDAO();
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
    }

    /**
     *
     * @param idLesson
     * @return list question
     */
    public QuestionDTO listQuestionByIdLesson(String idLesson, String word,String order, int start, int length, int draw){
   // public QuestionDTO listQuestionByIdLesson(String idLesson, String word){
        QuestionDTO questionDTO = new QuestionDTO();
        LessonMappingQuestionDAO lessonMappingQuestionDAO = new LessonMappingQuestionDAO();
        QuestionDAO questionDAO = new QuestionDAO();
        List<String> lstId = new ArrayList<String>();
        try {
            List<LessonMappingQuestion> listLessonMappingQuestions = lessonMappingQuestionDAO.getAllByIDLesson(idLesson);
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
    }

    /**
     *
     * @param idLessonCollection
     * @return
     */
    public boolean updateDeleted(String idLessonCollection){
        boolean isDelete = false;
        LessonMappingQuestionDAO dao = new LessonMappingQuestionDAO();
        try{
            isDelete = dao.updateDeleted(idLessonCollection);
        }catch (Exception e){
            logger.debug("can not update delete in database because : " + e.getMessage());
        }
        return isDelete;
    }

    /**
     *
     * @param idLessonCollection
     * @param idQuestion
     * @return
     */
    public boolean updateDeleted(String idLessonCollection, String idQuestion){
        boolean isDelete = false;
        LessonMappingQuestionDAO dao = new LessonMappingQuestionDAO();
        try{
            isDelete = dao.updateDeleted(idLessonCollection,idQuestion);
        }catch (Exception e){
            logger.debug("can not update delete in database because : " + e.getMessage());
        }
        return isDelete;
    }

    public String addQuestionToLessonDB(LessonMappingQuestion obj){
        LessonMappingQuestionDAO dao = new LessonMappingQuestionDAO();
        boolean check = false;
        String message = "";
        try {
            check = dao.checkExist(obj.getIdLesson(), obj.getIdQuestion());
            if(check){
                message = ERROR + " : this question was already added to lesson!";
            }else {
                dao.create(obj);
                message = SUCCESS;
            }
        }catch (Exception e){
            message = ERROR + " : " + e.getMessage();
            logger.error("can not add question to lesson in db because : " + e.getMessage());
        }
        return message;
    }
}
