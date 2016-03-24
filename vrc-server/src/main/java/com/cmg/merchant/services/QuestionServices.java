package com.cmg.merchant.services;

import com.cmg.lesson.dao.lessons.LessonMappingQuestionDAO;
import com.cmg.lesson.dao.question.QuestionDAO;
import com.cmg.lesson.dao.question.WeightForPhonemeDAO;
import com.cmg.lesson.dao.question.WordOfQuestionDAO;
import com.cmg.lesson.dao.word.WordCollectionDAO;
import com.cmg.lesson.data.dto.question.QuestionDTO;
import com.cmg.lesson.data.jdo.lessons.LessonMappingQuestion;
import com.cmg.lesson.data.jdo.question.Question;
import com.cmg.lesson.data.jdo.question.WeightForPhoneme;
import com.cmg.lesson.data.jdo.question.WordOfQuestion;
import com.cmg.lesson.data.jdo.word.WordCollection;
import com.cmg.lesson.services.question.WeightForPhonemeService;
import com.cmg.merchant.dao.lessons.LMQDAO;
import com.cmg.merchant.dao.questions.QDAO;
import com.cmg.merchant.dao.questions.QMLDAO;
import com.cmg.merchant.dao.word.WDAO;
import com.cmg.merchant.dao.word.WFPDAO;
import com.cmg.merchant.dao.word.WMQDAO;
import com.cmg.merchant.data.dto.ListWordAddQuestion;
import com.cmg.merchant.data.dto.WeightPhonemesDTO;
import com.cmg.vrc.util.StringUtil;
import com.cmg.vrc.util.UUIDGenerator;
import org.apache.log4j.Logger;
import com.cmg.merchant.data.dto.WeightDTO;

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

    /**
     *
     * @return
     */
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

    /**
     *
     * @return
     */
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

    /**
     *
     * @param listWords
     * @param idLesson
     * @return
     */
    public String addQuestionToLesson(ListWordAddQuestion listWords,String idLesson){
        String message=null;
        List<WeightPhonemesDTO> list=listWords.getListWord();
        String idQuestion = UUIDGenerator.generateUUID().toString();

        if(addQuestionToDB(idQuestion,null, null).indexOf(ERROR) != -1){
            return ERROR;
        }
        if(addMappingQuestionToLesson(idQuestion,idLesson).indexOf(ERROR)!= -1){
            return ERROR;
        }
        if(list!=null){
            for(int i=0;i<list.size();i++){
                WordOfQuestion woq = new WordOfQuestion(idQuestion,list.get(i).getIdWord(),getMaxVersionWordOfQuestion(),false);
               if( addWordToQuestionDB(woq).indexOf(ERROR)!=-1){
                   return ERROR;
               }
                if(addMappingWeightForPhonemes(list.get(i), idQuestion).indexOf(ERROR)!=-1){
                    return ERROR;
                }
            }
        }
        message=SUCCESS;
        return message;
    }

    /**
     *
     * @param listWords
     * @param idLesson
     * @param type
     * @param description
     * @return
     */
    public String addQuestionToTest(ListWordAddQuestion listWords,String idLesson, String type, String description){
        String message=null;
        List<WeightPhonemesDTO> list=listWords.getListWord();
        String idQuestion = UUIDGenerator.generateUUID().toString();

        if(addQuestionToDB(idQuestion,type,description).indexOf(ERROR) != -1){
            return ERROR;
        }
        if(addMappingQuestionToLesson(idQuestion,idLesson).indexOf(ERROR)!= -1){
            return ERROR;
        }
        if(list!=null){
            for(int i=0;i<list.size();i++){
                WordOfQuestion woq = new WordOfQuestion(idQuestion,list.get(i).getIdWord(),getMaxVersionWordOfQuestion(),false);
                if( addWordToQuestionDB(woq).indexOf(ERROR)!=-1){
                    return ERROR;
                }
                if(addMappingWeightForPhonemes(list.get(i), idQuestion).indexOf(ERROR)!=-1){
                    return ERROR;
                }
            }
        }
        message=SUCCESS;
        return message;
    }

    /**
     *
     * @param idQuestion
     * @param type
     * @param description
     * @return
     */
    public String addQuestionToDB(String idQuestion,String type, String description){
        QDAO qdao=new QDAO();
        String message=null;
        try {
            Question question=new Question();
            if(type != null && description!=null){
                question.setName("Question");
                question.setDescription(description);
                question.setType(type);
            }else{
                question.setName("Question");
                question.setDescription("Question");
            }
            question.setId(idQuestion);
            question.setIsDeleted(false);
            question.setVersion(getMaxVersionQuestion());
            question.setTimeCreated(new Date(System.currentTimeMillis()));
            qdao.create(question);
            message=SUCCESS;
        }catch(Exception e){
            message = ERROR;
        }

        return message;
    }

    /**
     *
     * @param idQuestion
     * @param idLesson
     * @return
     */
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

    /**
     *
     * @param dto
     * @param IdQuestion
     * @return
     */
    public String addMappingWeightForPhonemes(WeightPhonemesDTO dto,String IdQuestion){
        WeightForPhonemeDAO dao = new WeightForPhonemeDAO();
        String message=null;
        try {
            if(dto.getListWeightPhoneme()!=null && dto.getListWeightPhoneme().size() > 0){
                updateDeleted(IdQuestion,dto.getIdWord());
                List<WeightForPhoneme> list = new ArrayList<WeightForPhoneme>();
                int version = getMaxVersion();
                for(WeightDTO w : dto.getListWeightPhoneme()){
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
                message = SUCCESS;
            }else{
                message = ERROR;
            }
        } catch (Exception e) {
            logger.error("can not add mapping list weight for phoneme : " + e.getMessage());
        }
        return message;
    }

    /**
     *
     * @param idQuestion
     * @param idWord
     * @return
     */
    public boolean updateDeleted(String idQuestion, String idWord){
        boolean check = false;
        WeightForPhonemeDAO dao = new WeightForPhonemeDAO();
        try {
            dao.updateDeletedBy(idQuestion,idWord);
            check = true;
        } catch (Exception e) {
            logger.debug("can not update delete in database because : " + e.getMessage());
        }
        return check;
    }

    /**
     *
     * @param obj
     * @return
     */
    public String addWordToQuestionDB(WordOfQuestion obj){
        WMQDAO woqDAO = new WMQDAO();
        boolean check = false;
        String message = "";
        try {
            check = woqDAO.checkExistedWord(obj.getIdQuestion(), obj.getIdWordCollection());
            if(check){
                message = ERROR + " : this word was already added to question!";
            }else {
                woqDAO.create(obj);
                message = SUCCESS;
            }
        }catch (Exception e){
            message = ERROR + " : " + e.getMessage();
            logger.error("can not add word to question in db because : " + e.getMessage());
        }
        return message;
    }

    /**
     *
     * @param idLesson
     * @return
     */
    public ArrayList<Question> getQuestionByIdLesson(String idLesson){
        QMLDAO dao = new QMLDAO();
        ArrayList<Question> list = new ArrayList<>();
        try {
            List<Question> temp = dao.getQuestionByIdLesson(idLesson);
            if(temp!=null && temp.size() > 0){
                for(Question q : temp){
                    list.add(q);
                }
            }
        }catch (Exception e){
            logger.error("can not get all question by id lesson : " + e.getMessage());
        }
        return list;
    }

    /**
     *
     * @param idQuestion
     * @return
     */
    public ArrayList<WordCollection> getWordsByIdQuestion(String idQuestion){
        WDAO dao = new WDAO();
        ArrayList<WordCollection> list = new ArrayList<>();
        try {
            List<WordCollection> temp = dao.getWordsByIdQuestion(idQuestion);
            if(temp!=null){
                for(WordCollection word : temp){
                    list.add(word);
                }
            }
        }catch (Exception e){
            logger.error("can not get all word by id question : " + e.getMessage());
        }
        return list;
    }

    /**
     *
     * @param idQuestion
     * @param idWord
     * @return
     */
    public ArrayList<WeightForPhoneme> getWeightById(String idQuestion, String idWord){
        WFPDAO dao =  new WFPDAO();
        try {
            ArrayList<WeightForPhoneme> list = (ArrayList<WeightForPhoneme>) dao.listBy(idQuestion,idWord);
            return list;
        }catch (Exception e){
            logger.error("can not get all word by id question : " + e.getMessage());
        }
        return null;
    }

    /**
     *
     * @param idLessonMapping
     * @param idQuestion
     * @return
     */
    public String copyQuestion(String idLessonMapping, String idQuestion){
        QDAO qDao = new QDAO();
        try {
            Question q = qDao.getById(idQuestion);
            if(q!=null){
                Question tmp = new Question();
                String newId = UUIDGenerator.generateUUID().toString();
                tmp.setId(newId);
                tmp.setName(q.getName());
                tmp.setDescription(q.getDescription());
                tmp.setType((String)StringUtil.isNull(q.getType(),""));
                tmp.setIsDeleted(false);
                tmp.setVersion(q.getVersion());
                tmp.setTimeCreated(new Date(System.currentTimeMillis()));
                qDao.create(tmp);
                addMappingQuestionToLesson(newId,idLessonMapping);
                return newId;
            }
        }catch (Exception e){
            return ERROR;
        }
        return ERROR;
    }

    /**
     *
     * @param idQuestionMapping
     * @param idWord
     * @return
     */
    public void copyWords(String idQuestionMapping, String idWord){
        WordOfQuestionDAO woqDao = new WordOfQuestionDAO();
        try {
            WordOfQuestion woq = new WordOfQuestion();
            woq.setIsDeleted(false);
            woq.setIdQuestion(idQuestionMapping);
            woq.setIdWordCollection(idWord);
            woq.setVersion(getMaxVersionWordOfQuestion());
            woqDao.create(woq);
        }catch (Exception e){
            logger.error("can not copy words");
        }
    }

    /**
     *
     * @param idQuestionMapping
     * @param idWord
     * @param idQuestionGetData
     */
    public void copyWeight(String idQuestionMapping, String idWord, String idQuestionGetData){
        WFPDAO dao = new WFPDAO();
        try {
            ArrayList<WeightForPhoneme> list = getWeightById(idQuestionGetData,idWord);
            if(list!=null && list.size() > 0){
                for(WeightForPhoneme wfp : list){
                    WeightForPhoneme temp = new WeightForPhoneme();
                    temp.setId(UUIDGenerator.generateUUID().toString());
                    temp.setIdQuestion(wfp.getIdQuestion());
                    temp.setIdWordCollection(wfp.getIdWordCollection());
                    temp.setIpa(wfp.getIpa());
                    temp.setPhoneme(wfp.getPhoneme());
                    temp.setWeight(wfp.getWeight());
                    temp.setIndex(wfp.getIndex());
                    temp.setIsDeleted(false);
                    temp.setVersion(wfp.getVersion());
                    dao.create(temp);
                }
            }
        }catch (Exception e){
            logger.error("can not copy weight for phoneme");
        }

    }

    /**
     *
     * @param listWords
     * @param idQuestion
     * @return
     */
    public String updateWordToQuestion(ListWordAddQuestion listWords,String idQuestion){
        WordOfQuestionDAO woqDAO = new WordOfQuestionDAO();
        String message=null;
        List<WeightPhonemesDTO> list=listWords.getListWord();
        try {
            for(int i=0;i<list.size();i++) {
                boolean check = woqDAO.checkExistedWord(idQuestion, list.get(i).getIdWord());
                if (check) {
                    if ( addMapping(list.get(i), idQuestion).indexOf(ERROR)!=-1) {
                        return  ERROR;
                    } else if(deleteWordOfQuestion(idQuestion, list.get(i).getIdWord()).indexOf(ERROR)!=-1) {
                        return ERROR;
                    }
                } else {
                    WordOfQuestion woq = new WordOfQuestion(idQuestion,list.get(i).getIdWord(),getMaxVersionWordOfQuestion(),false);

                    if( addWordToQuestionDB(woq).indexOf(ERROR)!=-1){
                        return ERROR;
                    }
                    if(addMappingWeightForPhonemes(list.get(i), idQuestion).indexOf(ERROR)!=-1){
                        return ERROR;
                    }
                }
            }
            message=SUCCESS;
        }catch (Exception e){
            logger.error("can not update Word to question because : " + e.getMessage());
        }
        return message;
    }


    /**
     *
     * @param dto
     * @param idQuestion
     * @return
     */
    public String addMapping(WeightPhonemesDTO dto,String idQuestion){
        WeightForPhonemeDAO dao = new WeightForPhonemeDAO();
        String message=null;
        try {
            if(dto.getListWeightPhoneme()!=null && dto.getListWeightPhoneme().size() > 0){
                updateDeleted(idQuestion,dto.getIdWord());
                List<WeightForPhoneme> list = new ArrayList<WeightForPhoneme>();
                int version = getMaxVersion();
                for(WeightDTO w : dto.getListWeightPhoneme()){
                    WeightForPhoneme wfp = new WeightForPhoneme();
                    wfp.setIdQuestion(idQuestion);
                    wfp.setIdWordCollection(dto.getIdWord());
                    wfp.setPhoneme(w.getPhoneme());
                    wfp.setIndex(w.getIndex());
                    wfp.setWeight(w.getWeight());
                    wfp.setVersion(version);
                    wfp.setIsDeleted(false);
                    list.add(wfp);
                }
                dao.create(list);
                message = SUCCESS;
            }else{
                message = ERROR;
            }
        }catch (Exception e){
            logger.error("can not add mapping list weight for phoneme : " + e.getMessage());
        }
        return message;
    }

    /**
     *
     * @param idQuestion
     * @param idWord
     * @return
     */
    public String deleteWordOfQuestion(String idQuestion, String idWord){
        WordOfQuestionDAO dao = new WordOfQuestionDAO();
        String message=null;
        try{
            boolean isDelete=dao.deleteWordofQuestion(idQuestion, idWord);
            if (isDelete){
                message = SUCCESS;
                updateDeleted(idQuestion, idWord);
            }else{
                message = ERROR + ":" + "an error has been occurred in server!";
            }
        }catch(Exception e){
            message = ERROR + ": "+ e.getMessage();
            logger.error("can not delete word of question, id word and question is: " + idWord + ", " + idQuestion + " because : " + e.getMessage());
        }
        return message;
    }

    /**
     *
     * @param idLesson
     * @param idQuestion
     * @return
     */
    public String deleteQuestion(String idLesson, String idQuestion ){
        QDAO dao = new QDAO();
        try {
            boolean check = dao.removeMappingQuestionWithLesson(idLesson, idQuestion);
            if(!check){
                return ERROR + ": an error has been occurred in server!";
            }
             check = dao.deletedQuestion(idQuestion);
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
     * @param word
     * @param idQuestion
     * @return
     */
    public String deleteWordMappingQuestion(String word, String idQuestion ){
        QDAO dao = new QDAO();
        WordCollectionDAO wordCollectionDAO=new WordCollectionDAO();
        try {
            WordCollection wordCollection=wordCollectionDAO.getByWord(word);
            boolean check = dao.updateDeletedWordOfQuestion(wordCollection.getId(), idQuestion);
            if(!check){
                return ERROR + ": an error has been occurred in server!";
            }

        }catch (Exception e){
            return ERROR + ": an error has been occurred in server!";
        }
        return SUCCESS;
    }
    public String updateWordToQuestionForTest(ListWordAddQuestion listWords,String idQuestion, String type, String description){
        WordOfQuestionDAO woqDAO = new WordOfQuestionDAO();
        QuestionDAO questionDAO=new QuestionDAO();
        String message=null;
        List<WeightPhonemesDTO> list=listWords.getListWord();
        try {
            Question question=questionDAO.getById(idQuestion);
            question.setDescription(description);
            question.setType(type);
            questionDAO.put(question);
            for(int i=0;i<list.size();i++) {
                boolean check = woqDAO.checkExistedWord(idQuestion, list.get(i).getIdWord());
                if (check) {
                    if ( addMapping(list.get(i), idQuestion).indexOf(ERROR)!=-1) {
                        return  ERROR;
                    } else if(deleteWordOfQuestion(idQuestion, list.get(i).getIdWord()).indexOf(ERROR)!=-1) {
                        return ERROR;
                    }

                } else {
                    WordOfQuestion woq = new WordOfQuestion(idQuestion,list.get(i).getIdWord(),getMaxVersionWordOfQuestion(),false);

                    if( addWordToQuestionDB(woq).indexOf(ERROR)!=-1){
                        return ERROR;
                    }
                    if(addMappingWeightForPhonemes(list.get(i), idQuestion).indexOf(ERROR)!=-1){
                        return ERROR;
                    }
                }
            }
            message=SUCCESS;
        }catch (Exception e){
            logger.error("can not update Word to question because : " + e.getMessage());
        }
        return message;
    }


}

