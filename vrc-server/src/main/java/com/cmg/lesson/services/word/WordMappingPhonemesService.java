package com.cmg.lesson.services.word;

import com.cmg.lesson.dao.word.WordCollectionDAO;
import com.cmg.lesson.dao.word.WordMappingPhonemesDAO;
import com.cmg.lesson.data.dto.word.WordDTO;
import com.cmg.lesson.data.jdo.word.WordCollection;
import com.cmg.lesson.data.jdo.word.WordMappingPhonemes;
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


    private String SUCCESS = "success";
    private String ERROR = "error mapping phonemes";
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
     * @param word
     * @return list phonemes of word
     */
    public WordDTO getByWord(String word){
        WordCollectionDAO wcDao = new WordCollectionDAO();
        WordDTO dto = new WordDTO();
        WordMappingPhonemesDAO wmpDao = new WordMappingPhonemesDAO();
        try {
            WordCollection wc = wcDao.getByWord(word);
            if(wc!=null){
                List<WordMappingPhonemes> list = wmpDao.getByWordID(wc.getId());
                if(list!=null && list.size() > 0){
                    dto.setPhonemes(list);
                    dto.setMessage(SUCCESS);
                    dto.setId(wc.getId());
                }else{
                    dto.setMessage(ERROR + ": this word have not any phoneme in database");
                }

            }else{
                dto.setMessage(ERROR + ": this word does not exist in database" );
            }
        }catch (Exception e){
            dto.setMessage(ERROR + ": an error has been occurred in server");
            logger.error("can not get phoneme of this word : " + word  + " because : "+ e.getMessage());
        }
        return dto;
    }

    /**
     *
     * @param wordID
     * @param phonemes
     * @param version
     * @param isDeleted
     * @return
     */
    public String addMapping(String wordID, List<String> phonemes,int version,boolean isDeleted){
        String messageError = "";
        if(checkExist(wordID)){
            updateDeleted(wordID,true);
        }
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
                return SUCCESS;
            }
        }catch(Exception e ){
            logger.error("error when add mapping word with phoneme because : " + e.getMessage());
            messageError = e.getMessage();
        }
        return ERROR+messageError;
    }

    /**
     *
     * @param wordID
     * @param phonemes
     * @param version
     * @param isDeleted
     * @return
     */
    public String addMappingPhonemes(String wordID, List<WordMappingPhonemes> phonemes,int version,boolean isDeleted){
        String messageError = "";
        try {
            if(checkExist(wordID)){
                updateDeleted(wordID,true);
            }
            WordMappingPhonemesDAO dao = new WordMappingPhonemesDAO();
            ArrayList<WordMappingPhonemes> list = new ArrayList<WordMappingPhonemes>();
            for(int i = 0 ; i < phonemes.size(); i++){
                WordMappingPhonemes wp = new WordMappingPhonemes(wordID,phonemes.get(i).getPhoneme(),phonemes.get(i).getIndex(),isDeleted,version);
                logger.info("add mapping phonemes " + wp.getPhoneme());
                list.add(wp);
            }
            if(list.size() > 0){
                dao.create(list);
                return SUCCESS;
            }
        }catch(Exception e ){
            logger.error("error when add mapping word with phoneme because : " + e.getMessage());
            messageError = e.getMessage();
        }
        return ERROR+messageError;
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
    public String updateDeleted(String wordID, boolean isDeleted){
        WordMappingPhonemesDAO dao = new WordMappingPhonemesDAO();
        String messageError;
        try {
            dao.updateDeleted(wordID,isDeleted);
            return SUCCESS;
        }catch (Exception e){
            logger.warn("check exist warning : " + e.getMessage());
            messageError = e.getMessage();
        }
        return ERROR + messageError;
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
