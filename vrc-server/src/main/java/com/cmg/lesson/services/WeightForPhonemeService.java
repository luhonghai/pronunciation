package com.cmg.lesson.services;

import com.cmg.lesson.dao.WeightForPhonemeDAO;
import com.cmg.lesson.data.dto.QuestionDTO;
import com.cmg.lesson.data.jdo.WeightForPhoneme;
import org.apache.log4j.Logger;

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
            }
        }catch (Exception e){
            logger.error("can not get list phoneme weight base on idQuestion : " + idQuestion + " and idWord : " + idWord + " because : " + e.getMessage());
        }
        return dto;
    }


    /**
     *
     * @param list
     * @return
     */
    public QuestionDTO addMapping(List<WeightForPhoneme> list){
        WeightForPhonemeDAO dao = new WeightForPhonemeDAO();
        QuestionDTO dto = new QuestionDTO();
        String message="";
        try {
            dao.create(list);
            message = SUCCESS;
        }catch (Exception e){
            message = ERROR + ":" + e.getMessage();
            logger.error("can not add mapping list weight for phoneme : " + e.getMessage());
        }
        dto.setMessage(message);
        return dto;
    }



}
