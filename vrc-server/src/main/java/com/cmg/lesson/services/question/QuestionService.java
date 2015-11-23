package com.cmg.lesson.services.question;

import com.cmg.lesson.common.DateSearchParse;
import com.cmg.lesson.dao.lessons.LessonMappingQuestionDAO;
import com.cmg.lesson.dao.question.QuestionDAO;
import com.cmg.lesson.dao.question.WeightForPhonemeDAO;
import com.cmg.lesson.data.dto.question.QuestionDTO;
import com.cmg.lesson.data.jdo.lessons.LessonMappingQuestion;
import com.cmg.lesson.data.jdo.question.Question;
import com.cmg.vrc.util.StringUtil;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by lantb on 2015-10-07.
 */
public class QuestionService {
    private static final Logger logger = Logger.getLogger(QuestionService.class
            .getName());

    private String SUCCESS = "success";
    private String ERROR = "error";

    /**
     *  use for get max version
     * @return max version in table
     */
    public int getMaxVersion(){
        int version = 0;
        QuestionDAO dao = new QuestionDAO();
        try {
            version = dao.getLatestVersion();
        }catch (Exception e){
            logger.info("can not get max version in table because : " + e.getMessage());
        }
        return version +1;
    }

    /**
     *
     * @param questionName
     * @return true if question was added to table.
     */
    public QuestionDTO addQuestionToDB(String questionName, String description){
        QuestionDAO dao = new QuestionDAO();
        QuestionDTO dto = new QuestionDTO();
        String message;
        try {
            if(!isExistQuestionName(questionName)) {
                Question q = new Question();
                q.setName(questionName);
                q.setDescription(description);
                q.setVersion(getMaxVersion());
                q.setTimeCreated(new Date(System.currentTimeMillis()));
                q.setIsDeleted(false);
                dao.create(q);
                message = SUCCESS;
            }else{
                message = ERROR + ":" + "question name is existed";
            }
        }catch(Exception e){
            message = ERROR + ":" + e.getMessage();
            logger.error("can not add question : " + questionName + " because : " + e.getMessage());
        }
        dto.setMessage(message);
        return dto;
    }

    /**
     *
     * @param id
     * @param questionName
     * @return
     */
    public QuestionDTO updateQuestionToDB(String id, String questionName, String description){
        QuestionDTO dto = new QuestionDTO();
        QuestionDAO dao = new QuestionDAO();
        String message;
        try{
            String oldName = (String)StringUtil.isNull(dao.getById(id).getName(),"");
            if(oldName.equalsIgnoreCase(questionName)){
                dao.updateQuestion(id, questionName,description);
                message = SUCCESS;
                dto.setMessage(message);
                return dto;
            }
            if(!isExistQuestionName(questionName)) {
                boolean isUpdate = dao.updateQuestion(id, questionName,description);
                if (isUpdate) {
                    message = SUCCESS;
                } else {
                    message = ERROR + ":" + "an error has been occurred in server!";
                }
            }else{
                message = ERROR + ":" + "question name is existed";
            }
        }catch(Exception e){
            message = ERROR + ": "+ e.getMessage();
            logger.error("can not update question : " + questionName + " because : " + e.getMessage());
        }
        dto.setMessage(message);
        return dto;
    }

    /**
     *
     * @param id
     * @return
     */
    public QuestionDTO deleteQuestionToDB(String id){
        QuestionDTO dto = new QuestionDTO();
        QuestionDAO dao = new QuestionDAO();
        String message;
        try{
            boolean isDelete=dao.deteleQuestion(id);
            if (isDelete){
                message = SUCCESS;
                //make sure you also delete mapping
                deleteMapping(id);
            }else{
                message = ERROR + ": " + "an error has been occurred in server!";
            }
        }catch(Exception e){
            message = ERROR + ": "+ e.getMessage();
            logger.error("can not delete question id: " + id + " because : " + e.getMessage());
        }
        dto.setMessage(message);
        return dto;
    }

    /**
     *
     * @param id
     */
    public void deleteMapping(String id){
        try {
            LessonMappingQuestionDAO dao = new LessonMappingQuestionDAO();
            dao.updateDeletedByQuestion(id);
            WeightForPhonemeDAO weightForPhonemeDAO = new WeightForPhonemeDAO();
            weightForPhonemeDAO.updateDeletedByIdQuestion(id);
        }catch (Exception e){
            logger.debug("can not delete mapping : " + e);
        }
    }

    /**
     *
     * @param name
     * @return true is exits question name
     */
    public boolean isExistQuestionName(String name) throws Exception{
        boolean isExist = false;
        QuestionDAO dao = new QuestionDAO();
        isExist = dao.checkExist(name);
        return isExist;
    }

    /**
     *
     * @param start
     * @param length
     * @param search
     * @param column
     * @param order
     * @param createDateFrom
     * @param createDateTo
     * @return List<Question>
     */
    public List<Question> listAll(int start, int length,String search,int column,String order,Date createDateFrom,Date createDateTo){
        QuestionDAO dao = new QuestionDAO();
        try{
            return dao.listAll(start, length, search,column, order, createDateFrom, createDateTo);
        }catch (Exception ex){
            logger.error("list all question error, because:" + ex.getMessage());
        }
        return null;
    }


    public QuestionDTO searchName(String lessonId,String questionName){
        QuestionDTO dto = new QuestionDTO();
        QuestionDAO dao = new QuestionDAO();
        LessonMappingQuestionDAO lessonMappingQuestionDAO = new LessonMappingQuestionDAO();
        List<String> lstId = new ArrayList<String>();
        try{
            List<LessonMappingQuestion> listLessonMappingQuestions = lessonMappingQuestionDAO.getAllByIDLesson(lessonId);
            if(listLessonMappingQuestions!=null && listLessonMappingQuestions.size()>0) {
                for (LessonMappingQuestion lmq : listLessonMappingQuestions) {
                    lstId.add(lmq.getIdQuestion());
                }
            }
            List<Question> listQuestion = dao.searchName(lstId, questionName);
            if(listQuestion!=null && listQuestion.size()>0){
                dto.setData(listQuestion);
                dto.setMessage(SUCCESS);
            }else {
                dto.setMessage(ERROR + ":question not exists in database");
            }
        }catch (Exception e){
            dto.setMessage(ERROR + ": " + "search name question error, because:" + e.getMessage());
            logger.error("search name question error, because:" + e.getMessage());
        }
        return dto;
    }

    /**
     *
     * @param search
     * @param createDateFrom
     * @param createDateTo
     * @return total rows
     */
    public double getCount(String search,Date createDateFrom,Date createDateTo, int length, int start){
        QuestionDAO dao = new QuestionDAO();
        try {
            if (search == "" && createDateFrom == null && createDateTo == null){
                return dao.getCount();
            }else {
                    return dao.getCountSearch(search, createDateFrom, createDateTo,length,start);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    /**
     *
     * @param start
     * @param length
     * @param search
     * @param column
     * @param order
     * @param createDateFrom
     * @param createDateTo
     * @param draw
     * @return
     */
    public QuestionDTO search(int start, int length,String search,int column,String order,String createDateFrom,String createDateTo, int draw){
        QuestionDTO dto = new QuestionDTO();
        try{
            Date dateFrom =  DateSearchParse.parseDate(createDateFrom);
            Date dateTo =  DateSearchParse.parseDate(createDateTo, true);
            double count = getCount(search,dateFrom,dateTo,length,start);
            List<Question> listQuestion = listAll(start,length,search,column,order,dateFrom,dateTo);
            dto.setDraw(draw);
            dto.setRecordsFiltered(count);
            dto.setRecordsTotal(count);
            dto.setData(listQuestion);
        }catch (Exception e){
            dto.setMessage(ERROR + ": " + "search question error, because " + e.getMessage());
            logger.error("search question error, because:" + e.getMessage());
        }
        return dto;
    }

    /**
     * 
     * @param id
     * @return
     */
    public Question getById(String id){
        QuestionDAO dao = new QuestionDAO();
        try {
            return dao.getById(id);
        }catch (Exception e){
            logger.info("can not get question by id : " + id);
        }
        return null;
    }
}
