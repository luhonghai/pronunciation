package com.cmg.lesson.services.ipa;

import com.cmg.lesson.dao.ipa.IpaMapArpabetDAO;
import com.cmg.lesson.data.dto.ipa.IpaMapDTO;
import com.cmg.lesson.data.jdo.ipa.IpaMapArpabet;
import com.cmg.vrc.data.jdo.UserVoiceModel;
import com.cmg.vrc.sphinx.SphinxResult;
import org.apache.log4j.Logger;

/**
 * Created by lantb on 2015-10-26.
 */
public class IpaMapArpabetService {

    private static final Logger logger = Logger.getLogger(IpaMapArpabetService.class
            .getName());

    private String SUCCESS = "success";
    private String ERROR = "error";

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


    /**
     *
     * @param map
     * @return
     */
    public IpaMapDTO addMapping(IpaMapArpabet map){
        IpaMapArpabetDAO dao = new IpaMapArpabetDAO();
        IpaMapDTO dto =  new IpaMapDTO();
        String message = "";
        try {
            dao.create(map);
            message = SUCCESS;
        }catch (Exception e){
            logger.error("can not add mapping between ipa with arpabet because : " + e);
            message = ERROR +": can not add mapping between ipa with arpabet because " +e.getMessage();
        }
        dto.setMessage(message);
        return dto;
    }

    /**
     *
     * @param id
     * @return
     */
    public IpaMapArpabet getById(String id){
        IpaMapArpabetDAO dao = new IpaMapArpabetDAO();
        try {
            return dao.getById(id);
        }catch (Exception e){
            logger.info("can not get ipa map arpabet with id : " + id + " because : " + e);
        }
        return null;
    }

    /**
     *
     * @param map
     * @return
     */
    public IpaMapDTO update(IpaMapArpabet map){
        IpaMapArpabetDAO dao = new IpaMapArpabetDAO();
        IpaMapDTO dto = new IpaMapDTO();
        try {
            dao.updateMap(map.getId(),map.getArpabet(),map.getDescription(),map.getColor(),
                    map.getTip(),map.getType(),map.getIndexingType(),map.getWords());
            dto.setMessage(SUCCESS);
        }catch (Exception e){
            logger.error("can not update mapping between ipa : " + map.getIpa() + " with arpabet : " + map.getArpabet());
        }
        return dto;
    }





}
