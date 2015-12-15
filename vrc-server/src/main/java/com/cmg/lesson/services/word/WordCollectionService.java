package com.cmg.lesson.services.word;

import com.cmg.lesson.dao.question.WordOfQuestionDAO;
import com.cmg.lesson.dao.word.WordCollectionDAO;
import com.cmg.lesson.data.dto.word.ListWord;
import com.cmg.lesson.data.dto.word.WordDTO;
import com.cmg.lesson.data.jdo.word.WordCollection;
import com.cmg.lesson.data.jdo.word.WordMappingPhonemes;
import com.cmg.vrc.dictionary.OxfordDictionaryWalker;
import com.cmg.vrc.util.UUIDGenerator;
import edu.cmu.sphinx.linguist.dictionary.Word;
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

    private String SUCCESS = "success ";

    private String ERROR = "error: ";

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
    public boolean checkWordExist(String word) throws Exception{
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
     * @param definition
     * @param isDeleted
     * @return id if add success and error message if add false
     */
    public String addWordToDb(String word ,String arpabet, String pronunciation, String definition,String mp3Path, boolean isDeleted){
        WordCollectionDAO dao = new WordCollectionDAO();
        String messageError ="";
        try {
            if(!checkWordExist(word)){
                int version = getMaxVersion();
                WordCollection wc = new WordCollection(word,pronunciation,definition, mp3Path,isDeleted,version);
                String id = UUIDGenerator.generateUUID();
                wc.setArpabet(arpabet);
                wc.setId(id);
                dao.create(wc);
                return SUCCESS + ":"+ id;
            }else{
                return ERROR + "This word has already been added";
            }
        }catch (Exception e){
            logger.error("can not add word because error : " + e.getMessage());
            messageError = e.getMessage();
        }
        return ERROR + messageError;
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


    /**
     * filter by data table param
     * @return list word collection
     */
    public List<WordCollection> searchWord(int start, int length ,String search, String order){
        WordCollectionDAO dao = new WordCollectionDAO();
        try {
            return dao.search(search, order, length, start);
        }catch (Exception e){
            logger.error("can not search word start with : " + search);
        }
        return null;
    }

    /**
     *
     * @param start
     * @param length
     * @param search
     * @param order
     * @return count base on search
     */
    public double countSearch(int start, int length ,String search, String order){
        Double count = 0.0;
        WordCollectionDAO dao = new WordCollectionDAO();
        try {
            if(search!=null && search.trim().length() > 0){
                count = dao.countSearch(search,order,length,start);
            }else{
                count = dao.getCount();
            }
        }catch (Exception e){
            logger.error("can not get count search : " + search + " because  : " + e.getMessage(),e);
        }
        return count;
    }

    /**
     *
     * @param search
     * @param order
     * @param start
     * @param length
     * @param draw
     * @return object to client base on search
     */
    public ListWord searchWord(String search, String order, int start, int length, int draw){
        ListWord client = new ListWord();
        double count = countSearch(start, length, search, order);
        List<WordCollection> listWord = searchWord(start,length,search,order);
        //listWord = setPhonemeArpabet(listWord);
        client.setDraw(draw);
        client.setRecordsFiltered(count);
        client.setRecordsTotal(count);
        client.setData(listWord);
        return client;
    }

    /**
     * not used
     * @param temp
     * @return
     */
    public List<WordCollection> setPhonemeArpabet(List<WordCollection> temp){
        WordMappingPhonemesService service = new WordMappingPhonemesService();
        if(temp!=null && temp.size()>0){
            for(WordCollection w : temp){
                String arpabet = service.getPhonemesArpabet(w.getId());
                w.setArpabet(arpabet);
            }
        }
        return temp;
    }

    /**
     * this function use for add a word
     * @param word
     * @param pronunciation
     * @param definition
     * @param mp3Path
     * @param phonemes
     * @return worddto object with message
     */
    public WordDTO addWord(String word , String pronunciation, String definition,String mp3Path,List<String> phonemes){
        WordDTO dto = new WordDTO();
        String message = addWordToDb(word,parseFromList(phonemes), pronunciation, definition, mp3Path, false);
        if(message.startsWith(SUCCESS)) {
            String id = message.split(":")[1];
            WordMappingPhonemesService wpService = new WordMappingPhonemesService();
            int version = wpService.getMaxVersion();
            message = wpService.addMapping(id, phonemes, version, false);
        }
        dto.setMessage(message);
        return dto;
    }

    /**
     *
     * @param word
     * @param pronunciation
     * @param definition
     * @param mp3Path
     * @param phonemes
     * @return
     */
    public WordDTO addWordPhonemes(String word , String pronunciation, String definition,String mp3Path,List<WordMappingPhonemes> phonemes){
        WordDTO dto = new WordDTO();
        String message = addWordToDb(word, parseStringFromList(phonemes),pronunciation, definition, mp3Path, false);
        if(message.startsWith(SUCCESS)) {
            if(phonemes!=null && phonemes.size() > 0){
                String id = message.split(":")[1];
                WordMappingPhonemesService wpService = new WordMappingPhonemesService();
                int version = wpService.getMaxVersion();
                message = wpService.addMappingPhonemes(id, phonemes, version, false);
            }
        }
        dto.setMessage(message);
        return dto;
    }

    /**
     *  use for update information of word
     * @param wordID
     * @param definition
     * @param mp3Path
     * @return
     */
    public String updateWordInformation(String wordID, String definition, String mp3Path, String arpabet){
        String messageError = "";
        WordCollectionDAO dao = new WordCollectionDAO();
        try {
            WordCollection wordCollection=dao.getByID(wordID);
            if(wordCollection!=null) {
                boolean check = dao.updateWordInformation(wordID, definition, mp3Path, arpabet);
                if (check) {
                    return SUCCESS;
                } else {
                    return ERROR + "an error has been occurred";
                }
            }else {
                return ERROR + "deleted";
            }
        }catch (Exception e){
            messageError = e.getMessage();
            logger.error("can not update information of wordID : " + wordID);
        }
        return ERROR + messageError;
    }


    /**
     * this function use for update information of word
     * @param wordID
     * @param definition
     * @param mp3Path
     * @param phonemes
     * @return
     */
    public WordDTO updateWord(String wordID , String definition,String mp3Path,List<String> phonemes){
        WordDTO dto = new WordDTO();
        String message = updateWordInformation(wordID,definition,mp3Path,parseFromList(phonemes));
        if(message.startsWith(SUCCESS)){
            WordMappingPhonemesService wpService = new WordMappingPhonemesService();
            int version = wpService.getMaxVersion();
            message = wpService.addMapping(wordID, phonemes, version, false);
        }
        dto.setMessage(message);
        return dto;
    }

    /**
     *
     * @param wordID
     * @param definition
     * @param mp3Path
     * @param phonemes
     * @return
     */
    public WordDTO updateWordPhonemes(String wordID , String definition,String mp3Path,List<WordMappingPhonemes> phonemes){
        WordDTO dto = new WordDTO();
        String message = updateWordInformation(wordID,definition,mp3Path,parseStringFromList(phonemes));
        if(message.startsWith(SUCCESS)){
            if(phonemes!=null && phonemes.size() > 0) {
                WordMappingPhonemesService wpService = new WordMappingPhonemesService();
                int version = wpService.getMaxVersion();
                message = wpService.addMappingPhonemes(wordID, phonemes, version, false);
            }
        }
        dto.setMessage(message);
        return dto;
    }

    /**
     * use for update deleted in table word
     * @param wordID
     * @return success if delete word database ok
     */
    public String deleteWordDB(String wordID){
        WordCollectionDAO dao = new WordCollectionDAO();
        String messageError = "";
        try {
            WordCollection wordCollection=dao.getByID(wordID);
            if(wordCollection!=null) {
                dao.deleteWord(wordID);
                return SUCCESS;
            }else {
                return ERROR + "deleted";
            }
        }catch (Exception e){
            logger.error("delete word id : " + wordID + " false because : "+ e.getMessage());
            messageError = e.getMessage();
        }
        return ERROR + messageError;
    }

    /**
     * use for deleted word
     * @param wordID
     * @return dto object with message to client
     */
    public WordDTO deleteWord(String wordID){
        String message = "";
        WordDTO dto = new WordDTO();
        message = deleteWordDB(wordID);
        if(message.startsWith(SUCCESS)){
            WordMappingPhonemesService wpService = new WordMappingPhonemesService();
            message = wpService.updateDeleted(wordID,true);
            deleteMapping(wordID);
        }
        dto.setMessage(message);
        return dto;
    }

    /**
     *
     * @param wordID
     */
    public void deleteMapping(String wordID){
        try {
            WordOfQuestionDAO dao = new WordOfQuestionDAO();
            dao.deleteMappingWord(wordID);
        }catch (Exception e){
            logger.debug("can not delete mapping : " + e);
        }
    }

    /**
     *
     * @param wordID
     * @return
     */
    public WordCollection getById(String wordID){
        WordCollectionDAO dao = new WordCollectionDAO();
        try {
            return dao.getById(wordID);
        }catch (Exception e){
            logger.debug("can not get word by id : " + e.getMessage());
        }
        return null;
    }



    /**
     *  use for load all word from oxford dictionary
     * @param fileWords
     */
    public void loadWordToDataBase(File fileWords){
        try {
            OxfordDictionaryWalker walker = new OxfordDictionaryWalker(fileWords);
            walker.generateDictionary();
        }catch (Exception e){
            logger.error("can not load data to database : " + e.getMessage());
        }
    }

    public String parseStringFromList(List<WordMappingPhonemes> phonemes){
        String temp = "";
        if(phonemes!=null && phonemes.size()>0){
            for(int i = 0 ; i < phonemes.size(); i++){
                temp = temp + " " + phonemes.get(i).getPhoneme()+ " ";
            }
        }
        return temp;
    }

    public String parseFromList(List<String> phonemes){
        String temp = "";
        if(phonemes!=null && phonemes.size()>0){
            for(int i = 0 ; i < phonemes.size(); i++){
                temp = temp + " " + phonemes.get(i)+ " ";
            }
        }
        return temp;
    }

}
