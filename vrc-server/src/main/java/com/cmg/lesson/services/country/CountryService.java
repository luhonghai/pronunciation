package com.cmg.lesson.services.country;

import com.cmg.lesson.common.DateSearchParse;
import com.cmg.lesson.dao.country.CountryDAO;
import com.cmg.lesson.dao.country.CountryMappingCourseDAO;
import com.cmg.lesson.dao.course.CourseDAO;
import com.cmg.lesson.data.dto.country.CountryDTO;
import com.cmg.lesson.data.dto.course.CourseDTO;
import com.cmg.lesson.data.jdo.country.Country;
import com.cmg.lesson.data.jdo.country.CountryMappingCourse;
import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.vrc.util.UUIDGenerator;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

/**
 * Created by lantb on 2015-10-28.
 */
public class CountryService {
    private static final Logger logger = Logger.getLogger(CountryService.class
            .getName());
    private String SUCCESS = "success";
    private String ERROR = "error";
    /**
     *  use for get max version
     * @return max version in table
     */
    public int getMaxVersion(){
        int version = 0;
        CountryDAO dao = new CountryDAO();
        try {
            version = dao.getLatestVersion();
        }catch (Exception e){
            logger.info("can not get max version in table because : " + e.getMessage());
        }
        return version +1;
    }

    /**
     *  use for get max version
     * @return max version in table
     */
    public int getMaxVersionMapping(){
        int version = 0;
        CountryMappingCourseDAO dao = new CountryMappingCourseDAO();
        try {
            version = dao.getLatestVersion();
        }catch (Exception e){
            logger.info("can not get max version in table mapping because : " + e.getMessage());
        }
        return version +1;
    }


    /**
     *
     * @param name
     * @return
     */
    public boolean checkExistedName(String name){
        CountryDAO dao = new CountryDAO();
        try {
            return dao.checkExisted(name);
        }catch (Exception e){
            logger.info("check existed name error : " + e);
        }
        return false;
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
    public List<Country> listAll(int start, int length,String search,int column,String order,Date createDateFrom,Date createDateTo){
        CountryDAO dao = new CountryDAO();
        try{
            return dao.listAll(start, length, search, column, order, createDateFrom, createDateTo);
        }catch (Exception ex){
            logger.error("list all course error, because:" + ex.getMessage());
        }
        return null;
    }

    /**
     *
     * @param search
     * @param createDateFrom
     * @param createDateTo
     * @return total rows
     */
    public double getCount(String search,Date createDateFrom,Date createDateTo, int length, int start){
        CountryDAO dao = new CountryDAO();
        try {
            if (search == null && createDateFrom == null && createDateTo == null){
                return dao.getCount();
            }else {
                return dao.getCountSearch(search, createDateFrom, createDateTo,length,start);
            }
        } catch (Exception e) {
            logger.error("list all course error, because:" + e.getMessage());
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
     * @param draw
     * @return
     */
    public CountryDTO search(int start, int length,String search,int column,String order,String createDateFrom,String createDateTo, int draw){
        CountryDTO dto = new CountryDTO();
        try{
            Date dateFrom =  DateSearchParse.parseDate(createDateFrom);
            Date dateTo =  DateSearchParse.parseDate(createDateTo, true);
            double count = getCount(search,dateFrom,dateTo,length,start);
            List<Country> listCountry = listAll(start,length,search,column,order,dateFrom,dateTo);
            dto.setDraw(draw);
            dto.setRecordsFiltered(count);
            dto.setRecordsTotal(count);
            dto.setData(listCountry);
        }catch (Exception e){
            dto.setMessage(ERROR + " : " + "search course error, because:" + e.getMessage());
            logger.error("search country error, because:" + e.getMessage());
        }
        return dto;
    }


    /**
     *
     * @param name
     * @param description
     * @param id
     * @param linkImage
     * @param isDefault
     * @return
     */
    public boolean add(String id,String name, String description, String linkImage, boolean isDefault){
        CountryDAO dao = new CountryDAO();
        try {
            Country country = new Country();
            country.setId(id);
            country.setName(name);
            country.setDescription(description);
            country.setImageURL(linkImage);
            country.setIsDeleted(false);
            country.setVersion(getMaxVersion());
            country.setIsDefault(isDefault);
            return dao.create(country);
        }catch (Exception e){
            logger.error("can not add country to database because : " + e);
        }
        return false;
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
            map.setVersion(getMaxVersionMapping());
            return dao.create(map);
        }catch (Exception e){
            logger.error("can not add mapping country with course because : "  + e);
        }
        return false;
    }

    /**
     *
     * @param name
     * @param description
     * @param idCourse
     * @param linkS3
     * @param isDefault
     * @return
     */
    public CountryDTO addCountry(String name, String description, String idCourse, String linkS3, boolean isDefault){
        CountryDTO dto = new CountryDTO();
        String idCountry = UUIDGenerator.generateUUID().toString();
        boolean condition1 = add(idCountry, name, description, linkS3, isDefault);
        boolean condition2 = addMapping(idCountry,idCourse);
        if(condition1 && condition2){
            dto.setMessage(SUCCESS);
        }else{
            dto.setMessage(ERROR +": an error has been occurred in server!");
        }
        return dto;
    }






}
