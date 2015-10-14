package com.cmg.lesson.services.question;

import com.cmg.lesson.dao.question.WeightForPhonemeDAO;
import com.cmg.lesson.data.dto.question.QuestionDTO;
import com.cmg.lesson.data.dto.question.WeightDTO;
import com.cmg.lesson.data.dto.question.WeightPhonemesDTO;
import com.cmg.lesson.data.dto.word.WordDTO;
import com.cmg.lesson.data.jdo.question.WeightForPhoneme;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lantb on 2015-10-08.
 */
public class WeightForPhonemeService {
    private static final Logger logger = Logger.getLogger(WeightForPhonemeService.class
            .getName());

    private String SUCCESS = "success";
    private String ERROR = "error";

    /**
     *
     * @return
     */
    public int getMaxVersion(){
        int version = 0;
        WeightForPhonemeDAO dao = new WeightForPhonemeDAO();
        try {
            version = dao.getLatestVersion();
        }catch (Exception e){
            logger.warn("can not get latest version : " + e.getMessage());
        }
        return version+1;
    }

    /**
     *
     * @param idQuestion
     * @param idWord
     * @return
     */
    public QuestionDTO listAll(String idQuestion, String idWord){
        WeightForPhonemeDAO dao = new WeightForPhonemeDAO();
        QuestionDTO dto = new QuestionDTO();
        try {
            List<WeightForPhoneme> list = dao.listBy(idQuestion,idWord);
            if(list!=null && list.size() > 0){
                dto.setListWeightPhoneme(list);
                dto.setMessage(SUCCESS);
            }else{
                dto.setMessage(ERROR + ":can not get phoneme and weight for this word");
            }
        }catch (Exception e){
            dto.setMessage(ERROR + ": can not get phoneme and weight for this word");
            logger.error("can not get list phoneme weight base on idQuestion : " + idQuestion + " and idWord : " + idWord + " because : " + e.getMessage());
        }
        return dto;
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
        }catch (Exception e){
            logger.debug("can not update delete in database because : " + e.getMessage());
        }
        return check;
    }


    /**
     *
     * @param dto
     * @return
     */
    public boolean addMapping(WeightPhonemesDTO dto){
        WeightForPhonemeDAO dao = new WeightForPhonemeDAO();
        boolean message=false;
        try {
            if(dto.getData()!=null && dto.getData().size() > 0){
                updateDeleted(dto.getIdQuestion(),dto.getIdWord());
                List<WeightForPhoneme> list = new ArrayList<WeightForPhoneme>();
                int version = getMaxVersion();
                for(WeightDTO w : dto.getData()){
                    WeightForPhoneme wfp = new WeightForPhoneme();
                    wfp.setIdQuestion(dto.getIdQuestion());
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



}
