package com.cmg.lesson.services;

import com.cmg.lesson.dao.WordCollectionDAO;
import com.cmg.lesson.dao.WordOfQuestionDAO;
import com.cmg.lesson.data.dto.ListWord;
import com.cmg.lesson.data.dto.QuestionDTO;
import com.cmg.lesson.data.dto.WordDTO;
import com.cmg.lesson.data.jdo.WordCollection;
import com.cmg.lesson.data.jdo.WordOfQuestion;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lantb on 2015-10-07.
 */
public class WordOfQuestionService {
    private static final Logger logger = Logger.getLogger(WordOfQuestionService.class.getName());
    private String SUCCESS = "success";
    private String ERROR = "error";

    /**
     *
     * @return max version in table
     */
    public int getMaxVersion(){
        int version=0;
        WordOfQuestionDAO dao = new WordOfQuestionDAO();
        try {
            version = dao.getLatestVersion();
        } catch (Exception e) {
            logger.error("can not get max version in table because: " + e.getMessage());
        }
        return version + 1;
    }

    /**
     *
     * @param idQuestion
     * @param idWord
     * @return
     */
    public QuestionDTO deleteWordofQuestion(String idQuestion, String idWord){
        QuestionDTO dto = new QuestionDTO();
        WordOfQuestionDAO dao = new WordOfQuestionDAO();
        String message;
        try{
            boolean isDelete=dao.deleteWordofQuestion(idQuestion, idWord);
            if (isDelete){
                message = SUCCESS;
            }else{
                message = ERROR + ":" + "an error has been occurred in server!";
            }
        }catch(Exception e){
            message = ERROR + ": "+ e.getMessage();
            logger.error("can not delete word of question, id word and question is: " + idWord + ", " + idQuestion + " because : " + e.getMessage());
        }
        dto.setMessage(message);
        return dto;
    }

    /**
     *
     * @param idQuestion
     * @return
     */
    public QuestionDTO deleteQuestion(String idQuestion){
        QuestionDTO dto = new QuestionDTO();
        WordOfQuestionDAO dao = new WordOfQuestionDAO();
        String message;
        try{
            boolean isDelete=dao.deleteQuestion(idQuestion);
            if (isDelete){
                message = SUCCESS;
            }else{
                message = ERROR + ":" + "an error has been occurred in server!";
            }
        }catch(Exception e){
            message = ERROR + ": "+ e.getMessage();
            logger.error("can not delete question in table WordOfQuestion, id question is: " + idQuestion + " because : " + e.getMessage());
        }
        dto.setMessage(message);
        return dto;
    }

    /**
     *
     * @param idQuestion
     * @return list word collection
     */
    public ListWord listWordByIdQuestion(String idQuestion){
        ListWord listWord = new ListWord();
        WordOfQuestionDAO woqDAO = new WordOfQuestionDAO();
        WordCollectionDAO wcDAO = new WordCollectionDAO();
        List<String> lstId = new ArrayList<String>();
        try {
            List<WordOfQuestion> listWordOfQuestion = woqDAO.listByIdQuestion(idQuestion);
            if(listWordOfQuestion!=null && listWordOfQuestion.size()>0){
                for(WordOfQuestion woq : listWordOfQuestion){
                    lstId.add(woq.getIdWordCollection());
                }
                List<WordCollection> wordCollections = wcDAO.listIn(lstId);
                listWord.setData(wordCollections);
            }
        } catch (Exception e) {
            logger.error("list word by id question : "+ idQuestion + " false because : " +e.getMessage());
            e.printStackTrace();
        }
        return listWord;
    }


}
