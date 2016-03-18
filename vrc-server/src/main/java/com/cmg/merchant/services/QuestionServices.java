package com.cmg.merchant.services;

import com.cmg.lesson.dao.lessons.LessonMappingQuestionDAO;
import com.cmg.lesson.dao.question.QuestionDAO;
import com.cmg.lesson.dao.question.WeightForPhonemeDAO;
import com.cmg.lesson.dao.question.WordOfQuestionDAO;
import com.cmg.lesson.data.dto.question.WeightDTO;
import com.cmg.lesson.data.jdo.lessons.LessonMappingQuestion;
import com.cmg.lesson.data.jdo.question.Question;
import com.cmg.lesson.data.jdo.question.WeightForPhoneme;
import com.cmg.lesson.data.jdo.question.WordOfQuestion;
import com.cmg.merchant.dao.lessons.LMQDAO;
import com.cmg.merchant.dao.questions.QDAO;
import com.cmg.merchant.data.dto.ListWordAddQuestion;
import com.cmg.merchant.data.dto.WeightPhonemesDTO;
import com.cmg.vrc.util.UUIDGenerator;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by lantb on 2016-03-16.
 */
public class QuestionServices {
    private static final Logger logger = Logger.getLogger(LessonServices.class.getName());
    private String ERROR = "error";
    private String SUCCESS = "success";
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
    public int getMaxVersionQuestion(){
        int version = 0;
        QuestionDAO dao = new QuestionDAO();
        try {
            version = dao.getLatestVersion();
        }catch (Exception e){
            logger.info("Can not get max version in table because : " + e.getMessage());
        }
        return version +1;
    }
    public int getMaxVersionWordOfQuestion(){
        int version=0;
        WordOfQuestionDAO dao = new WordOfQuestionDAO();
        try {
            version = dao.getLatestVersion();
        } catch (Exception e) {
            logger.error("can not get max version in table because: " + e.getMessage());
        }
        return version + 1;
    }

    public String addQuestionToLesson(ListWordAddQuestion listWords){
        String message=null;
        List<WeightPhonemesDTO> list=listWords.getListWord();
        String idLesson=listWords.getIdLesson();
        String idQuestion = UUIDGenerator.generateUUID().toString();
        addQuestionToDB(idQuestion);
        addMappingQuestionToLesson(idQuestion,idLesson);
        if(list!=null){
            for(int i=0;i<list.size();i++){
                WordOfQuestion woq = new WordOfQuestion(idQuestion,list.get(i).getIdWord(),getMaxVersionWordOfQuestion(),false);
                boolean s=addMapping(list.get(i),idQuestion);
            }
        }

        //String message = addQuestionToDB(idLesson);
//        if(message.equalsIgnoreCase(ERROR)){
//            return ERROR + ": an error has been occurred in server!";
//        }
//        message = addMappingQuestionToLesson(idObj, idLesson);
        return message;
    }

    public String addQuestionToDB(String idQuestion){
        QDAO qdao=new QDAO();
        String message=null;
        try {
            Question question=new Question();
            question.setId(idQuestion);
            question.setIsDeleted(false);
            question.setVersion(getMaxVersionQuestion());
            question.setTimeCreated(new Date(System.currentTimeMillis()));
            qdao.create(question);
            message = idQuestion;
        }catch(Exception e){
            message = ERROR;
        }

        return message;
    }


    public String addMappingQuestionToLesson(String idQuestion, String idLesson ){
        LMQDAO lmqdao=new LMQDAO();
        try {
            LessonMappingQuestion lessonMappingQuestion=new LessonMappingQuestion();
            lessonMappingQuestion.setIsDeleted(false);
            lessonMappingQuestion.setIdLesson(idLesson);
            lessonMappingQuestion.setIdQuestion(idQuestion);
            lessonMappingQuestion.setVersion(getMaxVersion());
            lmqdao.create(lessonMappingQuestion);
        }catch (Exception e){e.printStackTrace();
            return ERROR + ": an error has been occurred in server!";
        }
        return SUCCESS;
    }

    public boolean addMapping(WeightPhonemesDTO dto,String IdQuestion){
        WeightForPhonemeDAO dao = new WeightForPhonemeDAO();
        boolean message=false;
        try {
            if(dto.getData()!=null && dto.getData().size() > 0){
                updateDeleted(IdQuestion,dto.getIdWord());
                List<WeightForPhoneme> list = new ArrayList<WeightForPhoneme>();
                int version = getMaxVersion();
                for(WeightDTO w : dto.getData()){
                    WeightForPhoneme wfp = new WeightForPhoneme();
                    wfp.setIdQuestion(IdQuestion);
                    wfp.setIdWordCollection(dto.getIdWord());
                    wfp.setPhoneme(w.getPhoneme());
                    wfp.setIndex(w.getIndex());
                    wfp.setWeight(w.getWeight());
                    wfp.setVersion(version);
                    wfp.setIsDeleted(false);
                    list.add(wfp);
                }
                dao.create(list);
                message = true;
            }else{
                message = true;
            }
        }catch (Exception e){
            logger.error("can not add mapping list weight for phoneme : " + e.getMessage());
        }
        return message;
    }
    public boolean updateDeleted(String idQuestion, String idWord){
        boolean check = false;
        WeightForPhonemeDAO dao = new WeightForPhonemeDAO();
        try {
            dao.updateDeletedBy(idQuestion,idWord);
            check = true;
        }catch (Exception e){
            logger.debug("can not update delete in database because : " + e.getMessage());
        }
        return check;
    }

}

