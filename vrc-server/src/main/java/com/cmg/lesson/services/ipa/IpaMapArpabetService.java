package com.cmg.lesson.services.ipa;

import com.cmg.lesson.common.DateSearchParse;
import com.cmg.lesson.dao.ipa.IpaMapArpabetDAO;
import com.cmg.lesson.dao.question.QuestionDAO;
import com.cmg.lesson.data.dto.ipa.IpaMapDTO;
import com.cmg.lesson.data.dto.question.QuestionDTO;
import com.cmg.lesson.data.jdo.ipa.IpaMapArpabet;
import com.cmg.lesson.data.jdo.question.Question;
import com.cmg.vrc.data.jdo.UserVoiceModel;
import com.cmg.vrc.sphinx.SphinxResult;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

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
                    String ipa = dao.getByArpabet(ph.getName());
                    ph.setIpa(ipa);
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
            map.setDateCreated(new Date(System.currentTimeMillis()));
            map.setIsDeleted(false);
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
            dao.updateMap(map.getId(),map.getArpabet(),map.getMp3Url(),map.getDescription(),map.getColor(),
                    map.getTip(),map.getType(),map.getIndexingType(),map.getWords());
            dto.setMessage(SUCCESS);
        }catch (Exception e){
            dto.setMessage(ERROR + " : can not update mapping because  " + e.getMessage());
            logger.error("can not update mapping between ipa : " + map.getIpa() + " with arpabet : " + map.getArpabet());
        }
        return dto;
    }

    /**
     *
     * @param id
     * @return
     */
    public IpaMapDTO delete(String id){
        IpaMapArpabetDAO dao = new IpaMapArpabetDAO();
        IpaMapDTO dto = new IpaMapDTO();
        try {
            dao.updateDeleted(id);
            dto.setMessage(SUCCESS);
        }catch (Exception e){
            logger.error("can not delete ipa map arpabet because : " + e);

        }
        return dto;
    }

    /**
     *
     * @param search
     * @param createDateFrom
     * @param createDateTo
     * @return total rows
     */
    public double getCount(String search,Date createDateFrom,Date createDateTo, int length, int start){
        IpaMapArpabetDAO dao = new IpaMapArpabetDAO();
        try {
            if (search == null && createDateFrom == null && createDateTo == null){
                return dao.getCount();
            }else {
                return dao.getCountSearch(search,createDateFrom,createDateTo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }




    /**
     *
     * @param start
     * @param length
     * @param search
     * @param column
     * @param order
     * @param createDateFrom
     * @param createDateTo
     * @return List<Question>
     */
    public List<IpaMapArpabet> listAll(int start, int length,String search,int column,String order,Date createDateFrom,Date createDateTo){
        IpaMapArpabetDAO dao = new IpaMapArpabetDAO();
        try{
            return dao.listAll(start, length, search,column, order, createDateFrom, createDateTo);
        }catch (Exception ex){
            logger.error("list all question error, because:" + ex.getMessage());
        }
        return null;
    }


    /**
     *
     * @param start
     * @param length
     * @param search
     * @param column
     * @param order
     * @param createDateFrom
     * @param createDateTo
     * @param draw
     * @return
     */
    public IpaMapDTO search(int start, int length,String search,int column,String order,String createDateFrom,String createDateTo, int draw){
        IpaMapDTO dto = new IpaMapDTO();
        try{
            Date dateFrom =  DateSearchParse.parseDate(createDateFrom);
            Date dateTo =  DateSearchParse.parseDate(createDateTo, true);
            double count = getCount(search,dateFrom,dateTo,length,start);
            List<IpaMapArpabet> listQuestion = listAll(start,length,search,column,order,dateFrom,dateTo);
            dto.setDraw(draw);
            dto.setRecordsFiltered(count);
            dto.setRecordsTotal(count);
            dto.setData(listQuestion);
        }catch (Exception e){
            dto.setMessage(ERROR + ": " + "search ipa mapping error, because " + e.getMessage());
            logger.error("search ipa mapping error, because:" + e.getMessage());
        }
        return dto;
    }

    /**
     *
     * @param arpabet
     * @return
     */
    public String getIpaByArpabet(String arpabet){
        IpaMapArpabetDAO dao = new IpaMapArpabetDAO();
        try {
            return dao.getByArpabet(arpabet);
        }catch (Exception e){
            logger.debug("get ipa by arpabet has trouble :" + e.getMessage());
        }
        return "";
    }




}
