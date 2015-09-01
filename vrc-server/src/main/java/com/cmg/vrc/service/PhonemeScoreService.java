package com.cmg.vrc.service;

import com.cmg.vrc.data.dao.impl.PhonemeScoreDAO;
import com.cmg.vrc.data.jdo.PhonemeScoreDB;
import com.cmg.vrc.sphinx.SphinxResult;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lantb on 2015-09-01.
 */
public class PhonemeScoreService {
    private static final Logger logger = Logger.getLogger(PhonemeScoreService.class
            .getName());

    /**
     * function add phoneme score
     * @param result
     * @param username
     * @param version
     */
    public void addPhonemeScore(SphinxResult result, String username, int version){
        PhonemeScoreDAO dao = new PhonemeScoreDAO();
        List<PhonemeScoreDB> temp = null;
        List<SphinxResult.PhonemeScore> list = result.getPhonemeScores();
        if(list.size() > 0){
            temp = new ArrayList<PhonemeScoreDB>();
            for(SphinxResult.PhonemeScore ps : list){
                PhonemeScoreDB score = new PhonemeScoreDB();
                score.setUsername(username);
                score.setVersion(version);
                score.setPhonemeWord(ps.getName());
                score.setTotalScore(ps.getTotalScore());
                temp.add(score);
            }
            try {
                dao.create(temp);
            }catch (Exception e){
                logger.warn("Can not insert list phonemeScore of username : " + username +" because exception : " + e.getMessage());
            }

        }
    }


    public List<PhonemeScoreDB> listByUsernameAndVersion(String username, int version){
        PhonemeScoreDAO dao = new PhonemeScoreDAO();
        List<PhonemeScoreDB> temp = null;
        try {
            temp = dao.getByUsernameAndVersion(username,version);
        }catch (Exception e){
            logger.warn("Can not get list phonemeScore of username : " + username + " with version : " + version + " because exception : " + e.getMessage());
        }
        return temp;
    }


}
