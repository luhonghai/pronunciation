package com.cmg.lesson.services;

import com.cmg.lesson.dao.WordMappingPhonemesDAO;
import com.cmg.lesson.data.jdo.WordCollection;
import com.cmg.lesson.data.jdo.WordMappingPhonemes;
import com.cmg.vrc.sphinx.DictionaryHelper;
import org.apache.log4j.Logger;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by lantb on 2015-10-01.
 */
public class WordMappingPhonemesService {
    private static final Logger logger = Logger.getLogger(WordMappingPhonemesService.class
            .getName());

    /**
     *
     * @return max version
     */
    public int getMaxVersion(){
        int maxVersion = 0;
        WordMappingPhonemesDAO dao = new WordMappingPhonemesDAO();
        try {
            maxVersion = dao.getLatestVersion();
        }catch (Exception e){
            logger.info("max version return zero because : " + e.getMessage());
        }
        maxVersion = maxVersion +1;
        return maxVersion;
    }

    /**
     *
     * @param idWord
     * @return
     */
    public List<WordMappingPhonemes> getByWordID(String idWord){
        WordMappingPhonemesDAO dao = new WordMappingPhonemesDAO();
        try {
            return dao.getByWordID(idWord);
        }catch (Exception e){
            logger.error("get by word id did not work cause : " + e.getMessage());
        }
        return null;
    }

    /**
     *
     * @param wordID
     * @param phonemes
     * @param version
     * @param isDeleted
     * @return
     */
    public boolean addMapping(String wordID, List<String> phonemes,int version,boolean isDeleted){
        boolean check = false;
        if(checkExist(wordID)){
            updateDeleted(wordID,true);
        }
        System.out.println("word id : " + wordID);
        WordMappingPhonemesDAO dao = new WordMappingPhonemesDAO();
        ArrayList<WordMappingPhonemes> list = new ArrayList<WordMappingPhonemes>();
        try {
            for(int i = 0 ; i < phonemes.size(); i++){
                WordMappingPhonemes wp = new WordMappingPhonemes(wordID,phonemes.get(i),i,isDeleted,version);
                logger.info("add mapping phonemes " + wp.getPhoneme());
                list.add(wp);
            }
            if(list.size() > 0){
                dao.create(list);
                check = true;
            }
        }catch(Exception e ){
            logger.error("error when add mapping word with phoneme because : " + e.getMessage());
        }
        return check;
    }

    /**
     *
     * @param wordID
     * @return check existed
     */
    public boolean checkExist(String wordID){
        boolean check = false;
        WordMappingPhonemesDAO dao = new WordMappingPhonemesDAO();
        try {
            check = dao.checkExisted(wordID);
        }catch (Exception e){
            logger.warn("check exist warning : " + e.getMessage());
        }
        return check;
    }

    /**
     *
     * @param wordID
     * @param isDeleted
     */
    public void updateDeleted(String wordID, boolean isDeleted){
        WordMappingPhonemesDAO dao = new WordMappingPhonemesDAO();
        try {
             dao.updateDeleted(wordID,isDeleted);
        }catch (Exception e){
            logger.warn("check exist warning : " + e.getMessage());
        }
    }

    /**
     * update database mapping phonemes
     */
    public void updateDatabase(){
        WordCollectionService wcSer = new WordCollectionService();
        String word = null;
        try {
            List<WordCollection> list = wcSer.listAll(false);

            if(list == null || list.size() == 0){
                System.out.println("list equal null");
                return;
            }
            DictionaryHelper helper = new DictionaryHelper(DictionaryHelper.Type.BEEP);
            for(WordCollection wc : list){
                word = wc.getWord();
                System.out.println("check word : " + word);
                List<String> phonemes = helper.getCorrectPhonemes(wc.getWord());
                if (phonemes != null && phonemes.size() > 0) {
                    int version = getMaxVersion();
                    logger.info("add mapping word " + wc.getWord());
                    addMapping(wc.getId(), phonemes, version, false);
                    logger.info("==add success mapping word " + wc.getWord() +"====");
                }
            }
        }catch (Exception e){
            logger.error("can not check word :"+word+ " in beep cause : " + e.getMessage());
        }

    }

}
