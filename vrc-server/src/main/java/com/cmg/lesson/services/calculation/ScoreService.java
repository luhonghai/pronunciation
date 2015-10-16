package com.cmg.lesson.services.calculation;

import com.cmg.lesson.dao.question.WeightForPhonemeDAO;
import com.cmg.lesson.data.jdo.history.UserLessonHistory;
import com.cmg.lesson.data.jdo.question.WeightForPhoneme;
import com.cmg.vrc.sphinx.SphinxResult;
import org.apache.log4j.Logger;

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
}
