package com.cmg.vrc.service;

import com.cmg.lesson.data.jdo.history.UserLessonHistory;
import com.cmg.vrc.common.Constant;
import com.cmg.vrc.data.dao.impl.PhonemeScoreDAO;
import com.cmg.vrc.data.dao.impl.UserVoiceModelDAO;
import com.cmg.vrc.data.jdo.PhonemeScoreDB;
import com.cmg.vrc.data.jdo.UserVoiceModel;
import com.cmg.vrc.sphinx.SphinxResult;
import com.cmg.vrc.util.AWSHelper;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import java.io.File;
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
                if (model.getResult() == null) return false;
                List<SphinxResult.PhonemeScore> list = model.getResult().getPhonemeScores();
                if(list != null && list.size() > 0){
                    List<PhonemeScoreDB> temp = new ArrayList<PhonemeScoreDB>();
                    for(SphinxResult.PhonemeScore ps : list){
                        PhonemeScoreDB score = new PhonemeScoreDB();
                        score.setUsername(username);
                        score.setVersion(version);
                        score.setPhonemeWord(ps.getName());
                        score.setTotalScore(ps.getTotalScore());
                        score.setTime(model.getServerTime());
                        score.setUserVoiceId(idUserVoice);
                        score.setIndex(ps.getIndex());
                        temp.add(score);
                    }
                    temp = sortList(temp);
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
     * function add phoneme score ,not use
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
                score.setIndex(ps.getIndex());
                temp.add(score);
            }
            try {
                temp = sortList(temp);
                for(PhonemeScoreDB psDB : temp){
                    logger.info("add phoneword : " + psDB.getPhonemeWord() + " with index : " + psDB.getIndex());
                    dao.create(psDB);
                }

            }catch (Exception e){
                logger.warn("Can not insert list phonemeScore of username : " + username +" because exception : " + e.getMessage());
                e.printStackTrace();
            }

        }
    }


    /**
     *
     * @param list
     * @return sort this list
     */
    public List<PhonemeScoreDB> sortList(List<PhonemeScoreDB> list){
        PhonemeScoreDB temp;
        for (int x=0; x<list.size(); x++)
        {
            for (int i=0; i < list.size()-x-1; i++) {
                if (list.get(i).getIndex() > list.get(i+1).getIndex())
                {
                    temp = list.get(i);
                    list.set(i,list.get(i+1));
                    list.set(i+1, temp);
                }
            }
        }
        return list;

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
                score.setIndex(db.getIndex());
                score.setUserVoiceId(db.getUserVoiceId());
                list.add(score);
            }
        }
        return list;
    }

    public static void main(String[] args) {
        UserVoiceModelDAO dao = new UserVoiceModelDAO();
        PhonemeScoreService phonemeScoreService = new PhonemeScoreService();
        try {
            List<UserVoiceModel> userVoiceModels = dao.listAll();
            AWSHelper awsHelper = new AWSHelper();
            Gson gson = new Gson();
            File tmpRootDir = new File(FileUtils.getTempDirectory(), Constant.FOLDER_RECORDED_VOICES);
            if (!tmpRootDir.exists())
                tmpRootDir.mkdirs();
            if (userVoiceModels != null && userVoiceModels.size() > 0) {
                for (UserVoiceModel model : userVoiceModels) {
                    model.setVersion(1);
                    System.out.println("=====================================================");
                    System.out.println(gson.toJson(model));
                    File tmpDir = new File(tmpRootDir, model.getUsername());
                    if (!tmpDir.exists())
                        tmpDir.mkdirs();
                    String fileName = model.getRecordFile().substring(0, model.getRecordFile().length() - "_raw.wav".length());
                    System.out.println("Check file name: " + fileName);
                    File tmpFile = new File(tmpDir, fileName + ".json");
                    if (!tmpFile.exists()) {
                        awsHelper.download(Constant.FOLDER_RECORDED_VOICES + "/" + model.getUsername() + "/" + fileName + ".json", tmpFile);
                    }
                    if (tmpFile.exists()) {
                        String data = FileUtils.readFileToString(tmpFile, "UTF-8");
                        System.out.println("Found json data: " + data);
                        UserVoiceModel jsonModel = gson.fromJson(data, UserVoiceModel.class);
                        System.out.println("Try to add all phoneme score to database");
                        phonemeScoreService.addPhonemeScore(jsonModel);
                        dao.update(model);
                    } else {
                        System.out.println("No file found!");
                    }
                }
            } else {
                System.out.println("No user voice model found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
