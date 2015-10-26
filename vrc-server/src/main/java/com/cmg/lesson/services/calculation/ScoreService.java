package com.cmg.lesson.services.calculation;

import com.cmg.lesson.dao.history.PhonemeLessonScoreDAO;
import com.cmg.lesson.dao.history.SessionScoreDAO;
import com.cmg.lesson.dao.history.UserLessonHistoryDAO;
import com.cmg.lesson.dao.ipa.IpaMapArpabetDAO;
import com.cmg.lesson.dao.question.WeightForPhonemeDAO;
import com.cmg.lesson.data.jdo.history.PhonemeLessonScore;
import com.cmg.lesson.data.jdo.history.SessionScore;
import com.cmg.lesson.data.jdo.history.UserLessonHistory;
import com.cmg.lesson.data.jdo.question.WeightForPhoneme;
import com.cmg.vrc.sphinx.SphinxResult;
import com.cmg.vrc.util.StringUtil;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lantb on 2015-10-16.
 */
public class ScoreService {

    private static final Logger logger = Logger.getLogger(ScoreService.class
            .getName());

    /**
     *
     * @param user
     * @return re calculate base on the weight admin set to each phonemes of the word in the question
     */
    public UserLessonHistory reCalculateBaseOnWeight(UserLessonHistory user){
        logger.info("=====Start calculate result of word : " + user.getWord() + " with weight form admin =======");
        if(user.getResult().getPhonemeScores() != null && user.getResult().getPhonemeScores().size() > 0){
            String idWord = user.getIdWord();
            String idQuestion = user.getIdQuestion();
            WeightForPhonemeDAO weightDao = new WeightForPhonemeDAO();
            IpaMapArpabetDAO ipaDao = new IpaMapArpabetDAO();
            try {
                List<WeightForPhoneme> weight = weightDao.listBy(idQuestion,idWord);
                int totalWeight = 0;
                double totalscore = user.getScore();
                logger.info("total score base on sphinx : "  + totalscore);
                List<SphinxResult.PhonemeScore> scores = user.getResult().getPhonemeScores();
                for(SphinxResult.PhonemeScore ph : scores){
                    float scorePhoneme = ph.getTotalScore();
                    String phoneme = ph.getName();
                    logger.info("phoneme : " + phoneme + " with score base on sphinx : " + scorePhoneme);
                    for(WeightForPhoneme w : weight){
                        if(w.getPhoneme().equalsIgnoreCase(phoneme.toLowerCase())){
                            int tempWeight = w.getWeight();
                            totalWeight = totalWeight + tempWeight;
                            totalscore = totalscore + (scorePhoneme*tempWeight);
                            ph.setTotalScore(scorePhoneme*tempWeight);
                            //set ipa to client;
                            ph.setIpa((String)StringUtil.isNull(ipaDao.getByArpabet(ph.getName()),""));
                            logger.info("phoneme : " + phoneme + " with score base on sphinx with added weight : " + (scorePhoneme *tempWeight));
                            break;
                        }
                    }
                }
                logger.info("total score base on weight : "  + totalscore);
                user.setScore(totalscore/totalWeight);
            }catch (Exception e){
                logger.error("can not get weight of each phoneme for this word : " + user.getWord());
            }
        }else{
            logger.info("===== Admin have not set weight for this word : " +user.getWord() + "=======");
        }
        logger.info("=====done=======");
        return user;
    }

    /**
     *
     * @param model
     */
    public void addUserLessonHistory(UserLessonHistory model){
        UserLessonHistoryDAO dao = new UserLessonHistoryDAO();
        try {
            dao.create(model);
        }catch (Exception e){
            logger.error("there are something wrong with function add user lesson history : " + e);
        }

    }

    /**
     *
     * @param model
     */
    public void addSessionScore(UserLessonHistory model){
        SessionScoreDAO dao = new SessionScoreDAO();
        try {
            SessionScore s = new SessionScore();
            s.setIdUserLessonHistory(model.getId());
            s.setIdCountry(model.getIdCountry());
            s.setSessionID(model.getSessionID());
            s.setIdQuestion(model.getIdQuestion());
            s.setIdLessonCollection(model.getIdLessonCollection());
            dao.create(s);
        }catch (Exception e){
            logger.error("there are something wrong with function add session score : " + e);
        }
    }

    /**
     *
     * @param model
     * @return
     */
    public void addPhonemeScore(UserLessonHistory model){
        PhonemeLessonScoreDAO dao = new PhonemeLessonScoreDAO();
        List<PhonemeLessonScore> list = null;
        try {
            List<SphinxResult.PhonemeScore> scores = model.getResult().getPhonemeScores();
            if(scores!=null && scores.size() > 0){
                list = new ArrayList<PhonemeLessonScore>();
                for(SphinxResult.PhonemeScore ph : scores){
                    PhonemeLessonScore pls = new PhonemeLessonScore();
                    pls.setIdCountry(model.getIdCountry());
                    pls.setIdUserLessonHistory(model.getId());
                    pls.setPhoneme(ph.getName());
                    pls.setIndex(ph.getIndex());
                    pls.setTotalScore(ph.getTotalScore());
                    list.add(pls);
                }
            }
            if(list!=null && list.size() > 0 ){
                dao.create(list);
            }
        }catch (Exception e){
            logger.error("there are something wrong with function add phoneme score for lesson : " + e);
        }
    }
}
