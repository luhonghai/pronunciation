package com.cmg.lesson.services.question;

import com.cmg.lesson.dao.word.WordCollectionDAO;
import com.cmg.lesson.dao.question.WordOfQuestionDAO;
import com.cmg.lesson.data.dto.question.WeightPhonemesDTO;
import com.cmg.lesson.data.dto.word.ListWord;
import com.cmg.lesson.data.dto.question.QuestionDTO;
import com.cmg.lesson.data.jdo.word.WordCollection;
import com.cmg.lesson.data.jdo.question.WordOfQuestion;
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
    public QuestionDTO deleteWordOfQuestion(String idQuestion, String idWord){
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
    public ListWord listWordByIdQuestion(String idQuestion, String word,String order, int start, int length, int draw){
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
                List<WordCollection> wordCollections = wcDAO.listIn(lstId, word, order,start,length);
                int count = wcDAO.getCountListIn(lstId, word, order, start, length);
                listWord.setData(wordCollections);
                listWord.setDraw(draw);
                listWord.setRecordsFiltered((double) count);
                listWord.setRecordsTotal((double) count);
            }else{
                listWord.setRecordsFiltered(0.0);
                listWord.setRecordsTotal(0.0);
                listWord.setData(new ArrayList<WordCollection>());
                listWord.setDraw(draw);
            }
        } catch (Exception e) {
            logger.error("list word by id question : "+ idQuestion + " false because : " +e.getMessage());
            e.printStackTrace();
        }
        return listWord;
    }

    /**
     *
     * @param dto
     * @return
     */
    public QuestionDTO addWordToQuestion(WeightPhonemesDTO dto){
        QuestionDTO qDTO = new QuestionDTO();
        WordOfQuestionDAO woqDAO = new WordOfQuestionDAO();
        boolean check = false;
        try {
            WordOfQuestion woq = new WordOfQuestion(dto.getIdQuestion(),dto.getIdWord(),getMaxVersion(),false);
            check = addWordToQuestionDB(woq);
            if(check){
                WeightForPhonemeService wfpService = new WeightForPhonemeService();
                check = wfpService.addMapping(dto);
                if(check){
                    qDTO.setMessage(SUCCESS);
                }else{
                    deleteWordOfQuestion(dto.getIdQuestion(),dto.getIdWord());
                    qDTO.setMessage(ERROR +": can not add mapping weight for phonemes.An error has been occurred in server");
                }
            }else{
                qDTO.setMessage(ERROR +": can not add mapping word to question.An error has been occurred in server");
            }
        }catch (Exception e){
            logger.error("can not add Word to question because : " + e.getMessage());
        }
        return qDTO;
    }

    /**
     *
     * @param obj
     * @return
     */
    public boolean addWordToQuestionDB(WordOfQuestion obj){
        WordOfQuestionDAO woqDAO = new WordOfQuestionDAO();
        boolean check = false;
        try {
            woqDAO.create(obj);
            check = true;
        }catch (Exception e){
            logger.error("can not add word to question in db because : " + e.getMessage());
        }
        return check;
    }


}
