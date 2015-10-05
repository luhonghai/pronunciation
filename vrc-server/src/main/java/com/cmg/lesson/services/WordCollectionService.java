package com.cmg.lesson.services;

import com.cmg.lesson.dao.WordCollectionDAO;
import com.cmg.lesson.data.jdo.WordCollection;
import com.cmg.vrc.dictionary.OxfordDictionaryWalker;
import org.apache.log4j.Logger;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lantb on 2015-10-01.
 */
public class WordCollectionService {

    private static final Logger logger = Logger.getLogger(WordCollectionService.class
            .getName());

    /**
     *
     * @return Max version
     */
    public int getMaxVersion(){
        int maxVersion = 0;
        WordCollectionDAO dao = new WordCollectionDAO();
        try {
            maxVersion = dao.getLatestVersion();
        }catch (Exception e){
            logger.info("max version equal zero : " + e.getMessage());
        }
        maxVersion = maxVersion +1;
        return maxVersion;
    }

    /**
     *
     * @param word
     * @return true if word is exist
     */
    public boolean checkWordExist(String word){
        boolean check = false;
        WordCollectionDAO dao = new WordCollectionDAO();
        try {
            check = dao.checkWordExist(word);
        }catch(Exception e){
            logger.warn("word did not exist : " + e.getMessage());
        }
        return check;
    }

    /**
     *
     * @param word
     * @param phonemes
     * @param definition
     * @param isDeleted
     * @return true if add success and false if add false
     */
    public boolean addWord(String word , String pronunciation, String definition,String mp3Path, boolean isDeleted){
        boolean check = false;
        WordCollectionDAO dao = new WordCollectionDAO();
        try {
            if(!checkWordExist(word)){
                int version = getMaxVersion();
                WordCollection wc = new WordCollection(word,pronunciation,definition, mp3Path,isDeleted,version);
                dao.create(wc);
                check = true;
            }
        }catch (Exception e){
            logger.error("can not add word because error : " + e.getMessage());
        }
        return check;
    }

    /**
     *
     * @param collections
     * @return true if add success and false if add false
     */
    public boolean addListWord(ArrayList<WordCollection> collections){
        boolean check = false;
        WordCollectionDAO dao = new WordCollectionDAO();
        try {
            dao.create(collections);
            check = true;
        }catch (Exception e){
            logger.error("can not add list word because error : " + e.getMessage());
        }
        return check;
    }

    /**
     *  use for load all word from oxford dictionary
     * @param fileWords
     */
    public void loadWordToDataBase(File fileWords){
        try {
            OxfordDictionaryWalker.generateDictionary();
        }catch (Exception e){
            logger.error("can not load data to database : " + e.getMessage());
        }
    }

    /**
     *
     * @param isDeleted
     * @return list contains all words with filter column isDeleted
     */
    public List<WordCollection> listAll(boolean isDeleted){
        WordCollectionDAO dao = new WordCollectionDAO();
        try {
            return dao.listAll(isDeleted);
        }catch (Exception e){
            logger.error("can not get all word filter with column isDeleted value : " + isDeleted + " cause : " + e.getMessage());
        }
        return null;
    }


    public ArrayList<WordCollection> searchWord(String word){
        WordCollectionDAO dao = new WordCollectionDAO();
        try {

        }catch (Exception e){
            logger.error("can not get word in database like : " + word + " because : " + e.getMessage());
        }
        return null;
    }



}
