package com.cmg.lesson.services.ipa;

import com.cmg.lesson.dao.ipa.IpaMapArpabetDAO;
import com.cmg.vrc.data.jdo.UserVoiceModel;
import com.cmg.vrc.sphinx.SphinxResult;

/**
 * Created by lantb on 2015-10-26.
 */
public class IpaMapArpabetService {


    /**
     * use to get ipa for freestyle module
     * @param model
     * @return
     */
    public UserVoiceModel setIpa(UserVoiceModel model){
        IpaMapArpabetDAO dao = new IpaMapArpabetDAO();
        if(model.getResult() !=null){
            for(SphinxResult.PhonemeScore ph : model.getResult().getPhonemeScores()){
                try {
                    ph.setIpa(dao.getByArpabet(ph.getName()));
                }catch (Exception e){
                    ph.setIpa("");
                }
            }
        }
        return model;
    }






}
