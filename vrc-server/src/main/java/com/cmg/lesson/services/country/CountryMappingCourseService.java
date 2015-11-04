package com.cmg.lesson.services.country;

import com.cmg.lesson.dao.country.CountryMappingCourseDAO;
import com.cmg.lesson.data.dto.country.CountryMappingCourseDTO;
import com.cmg.lesson.data.jdo.country.CountryMappingCourse;
import org.apache.log4j.Logger;

/**
 * Created by CMG Dev156 on 11/2/2015.
 */
public class CountryMappingCourseService {
    private static final Logger logger = Logger.getLogger(CountryMappingCourseService.class.getName());
    private String SUCCESS = "success";
    private String ERROR = "error";

    /**
     *  use for get max version
     * @return max version in table
     */
    public int getMaxVersion(){
        int version = 0;
        CountryMappingCourseDAO  dao = new CountryMappingCourseDAO();
        try {
            version = dao.getLatestVersion();
        }catch (Exception e){
            logger.info("can not get max version in table because : " + e.getMessage());
        }
        return version +1;
    }

    /**
     *
     * @param idCountry
     * @param idCourse
     * @return
     */
    public boolean addMapping(String idCountry, String idCourse){
        CountryMappingCourseDAO dao = new CountryMappingCourseDAO();
        try {
            CountryMappingCourse map = new CountryMappingCourse();
            map.setIdCourse(idCourse);
            map.setIdCountry(idCountry);
            map.setIsDeleted(false);
            map.setVersion(getMaxVersion());
            return dao.create(map);
        }catch (Exception e){
            logger.error("can not add mapping country with course because : " + e);
        }
        return false;
    }

    public boolean updateIdCourseByIdCountry(String idCountry, String idCourse){
        CountryMappingCourseDAO dao = new CountryMappingCourseDAO();
        try {
            CountryMappingCourse map = new CountryMappingCourse();
            return dao.updateIdCourseByIdCountry(idCountry, idCourse);
        }catch (Exception e){
            logger.error("can not update mapping country with course because : " + e);
        }
        return false;
    }

    /**
     *
     * @param id
     * @return
     */
    public CountryMappingCourseDTO getById (String id){
        CountryMappingCourseDTO dto = new CountryMappingCourseDTO();
        CountryMappingCourseDAO dao = new CountryMappingCourseDAO();
        try{
            dto.setItem(dao.getById(id));
            dto.setMessage(SUCCESS);
        }catch (Exception e){
            dto.setMessage(ERROR + ": can not get CountryMappingCourse by id, because " + e.getMessage());
        }
        return dto;
    }

    /**
     *
     * @param idCountry
     * @param idCourse
     * @return
     */
    public CountryMappingCourseDTO getById (String idCountry, String idCourse){
        CountryMappingCourseDTO dto = new CountryMappingCourseDTO();
        CountryMappingCourseDAO dao = new CountryMappingCourseDAO();
        try{
            dto.setItem(dao.getById(idCountry, idCourse));
            dto.setMessage(SUCCESS);
        }catch (Exception e){
            dto.setMessage(ERROR + ": can not get CountryMappingCourse by id, because " + e.getMessage());
        }
        return dto;
    }

    /**
     *
     * @param idCountry
     * @return
     */
    public CountryMappingCourseDTO getListByIdCountry (String idCountry){
        CountryMappingCourseDTO dto = new CountryMappingCourseDTO();
        CountryMappingCourseDAO dao = new CountryMappingCourseDAO();
        try{
            dto.setData(dao.getlistByIdCountry(idCountry));
            dto.setMessage(SUCCESS);
        }catch (Exception e){
            dto.setMessage(ERROR + ": can not get list CountryMappingCourse by idCountry, because " + e.getMessage());
        }
        return dto;
    }

    public boolean updateDeletedByIdCountry(String IdCountry){
        CountryMappingCourseDAO dao = new CountryMappingCourseDAO();
        try {
            return dao.updateDeleted(IdCountry);
        }catch (Exception e){
            logger.info("delete country mapping course error : " + e);
        }
        return false;
    }
}
