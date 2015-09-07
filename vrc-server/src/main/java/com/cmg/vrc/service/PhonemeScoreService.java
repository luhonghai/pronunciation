package com.cmg.vrc.service;

import com.cmg.vrc.data.dao.impl.PhonemeScoreDAO;
import com.cmg.vrc.data.jdo.PhonemeScoreDB;
import com.cmg.vrc.data.jdo.UserVoiceModel;
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
     *
     * @param username
     * @return max version
     */
    public int getMaxVersion(String username){
        PhonemeScoreDAO dao = new PhonemeScoreDAO();
        int max = 0;
        try {
            max = dao.getMaxVersionByUsername(username);
        }catch (Exception e){
            logger.warn("can not get version in table phoneme score of username : " + username +" because : " + e.getMessage());
        }
        max = max + 1;
        return max;
    }

    /**
     *
     * @param model
     * return true if add phoneme score success
     */
    public boolean addPhonemeScore(UserVoiceModel model){
        PhonemeScoreDAO dao = new PhonemeScoreDAO();
        boolean check = false;
        String idUserVoice = model.getId();
        String username = model.getUsername();
        try {
            if(!dao.userVoiceIDExisted(idUserVoice)){
                int version = getMaxVersion(username);
                List<SphinxResult.PhonemeScore> list = model.getResult().getPhonemeScores();
                if(list.size() > 0){
                    List<PhonemeScoreDB> temp = new ArrayList<PhonemeScoreDB>();
                    for(SphinxResult.PhonemeScore ps : list){
                        PhonemeScoreDB score = new PhonemeScoreDB();
                        score.setUsername(username);
                        score.setVersion(version);
                        score.setPhonemeWord(ps.getName());
                        score.setTotalScore(ps.getTotalScore());
                        score.setTime(model.getServerTime());
                        score.setUserVoiceId(idUserVoice);
                        temp.add(score);
                    }
                    dao.create(temp);
                    check = true;
                }
            }
        }catch (Exception e){
            logger.warn("exception when add phoneme score to database because : " + e.getMessage());
            e.printStackTrace();
        }
        return check;
    }



    /**
     * function add phoneme score
     * @param result
     * @param username
     * @param version
     */
    public void addPhonemeScore(SphinxResult result, String username, int version,long time, String dataID){
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
                score.setUserVoiceId(dataID);
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

    /**
     *
     * @param temp
     * @return return list Phoneme Score from PhonemeScoreDB
     */
    public List<SphinxResult.PhonemeScore> swap(List<PhonemeScoreDB> temp){
        List<SphinxResult.PhonemeScore> list = new ArrayList<SphinxResult.PhonemeScore>();
        if(temp.size() > 0){
            for(PhonemeScoreDB db : temp){
                SphinxResult.PhonemeScore score = new SphinxResult.PhonemeScore();
                score.setName(db.getPhonemeWord());
                score.setTotalScore(db.getTotalScore());
                score.setUsername(db.getUsername());
                score.setVersion(db.getVersion());
                score.setUserVoiceId(db.getUserVoiceId());
                list.add(score);
            }
        }
        return list;
    }




}
