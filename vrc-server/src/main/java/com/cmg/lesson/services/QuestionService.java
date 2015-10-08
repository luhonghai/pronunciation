package com.cmg.lesson.services;

import com.cmg.lesson.dao.QuestionDAO;
import com.cmg.lesson.data.dto.QuestionDTO;
import com.cmg.lesson.data.jdo.Question;
import org.apache.log4j.Logger;

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
    public QuestionDTO addQuestionToDB(String questionName){
        QuestionDAO dao = new QuestionDAO();
        QuestionDTO dto = new QuestionDTO();
        String message;
        try {
            if(!isExistQuestionName(questionName)) {
                Question q = new Question();
                q.setName(questionName);
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
    public QuestionDTO updateQuestionToDB(String id, String questionName){
        QuestionDTO dto = new QuestionDTO();
        QuestionDAO dao = new QuestionDAO();
        String message;
        try{
            boolean isUpdate=dao.updateQuestion(id, questionName);
            if (isUpdate){
                message = SUCCESS;
            }else{
                message = ERROR + ":" + "an error has been occurred in server!";
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
     * @param name
     * @return true is exits question name
     */
    public boolean isExistQuestionName(String name) throws Exception{
        boolean isExist = false;
        QuestionDAO dao = new QuestionDAO();
        isExist = dao.checkExist(name);
        return isExist;
    }

    public List<Question> listAll(int start, int length,String search,int column,String order,Date createDateFrom,Date createDateTo){
        QuestionDAO dao = new QuestionDAO();
        try{
            return dao.listAll(start, length, search,column, order, createDateFrom, createDateTo);
        }catch (Exception ex){
            logger.error("list all question error, because:" + ex.getMessage());
        }
        return null;
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
            if (search == null && createDateFrom == null && createDateTo == null){
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
    public QuestionDTO search(int start, int length,String search,int column,String order,Date createDateFrom,Date createDateTo, int draw){
        QuestionDTO dto = new QuestionDTO();
        double count = getCount(search,createDateFrom,createDateTo,length,start);
        List<Question> listQuestion = listAll(start,length,search,column,order,createDateFrom,createDateTo);
        dto.setDraw(draw);
        dto.setListQuestion(listQuestion);
        dto.setRecordsFiltered(count);
        dto.setRecordsTotal(count);
        return dto;
    }
}
