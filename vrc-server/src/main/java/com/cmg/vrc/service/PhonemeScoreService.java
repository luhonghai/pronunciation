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


    public int getMaxVersion(String username){
        PhonemeScoreDAO dao = new PhonemeScoreDAO();
        int max = 0;
        try {
            max = dao.getMaxVersionByUsername(username);
            max = max + 1;
        }catch (Exception e){
            logger.warn("can not get version in table phoneme score of username : " + username +" because : " + e.getMessage());
            e.printStackTrace();
        }
        return max;
    }


    /**
     * function add phoneme score
     * @param result
     * @param username
     * @param version
     */
    public void addPhonemeScore(SphinxResult result, String username, int version,long time){
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
                score.setTime(time);
                temp.add(score);
            }
            try {
                dao.create(temp);
            }catch (Exception e){
                logger.warn("Can not insert list phonemeScore of username : " + username +" because exception : " + e.getMessage());
                e.printStackTrace();
            }

        }
    }

    /**
     *
     * @param username
     * @param version
     * @return list phoneme score filter by username and version
     */
    public List<PhonemeScoreDB> listByUsernameAndVersion(String username, int version){
        PhonemeScoreDAO dao = new PhonemeScoreDAO();
        List<PhonemeScoreDB> temp = null;
        try {
            temp = dao.getByUsernameAndVersion(username,version);
        }catch (Exception e){
            logger.warn("Can not get list phonemeScore of username : " + username + " with version : " + version + " because exception : " + e.getMessage());
            e.printStackTrace();
        }
        return temp;
    }


    public List<SphinxResult.PhonemeScore> swap(List<PhonemeScoreDB> temp){
        List<SphinxResult.PhonemeScore> list = new ArrayList<SphinxResult.PhonemeScore>();
        if(temp.size() > 0){
            for(PhonemeScoreDB db : temp){
                SphinxResult.PhonemeScore score = new SphinxResult.PhonemeScore();
                score.setName(db.getPhonemeWord());
                score.setTotalScore(db.getTotalScore());
                score.setUsername(db.getUsername());
                score.setVersion(db.getVersion());
                list.add(score);
            }
        }
        return list;
    }

}
