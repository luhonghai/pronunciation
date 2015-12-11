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
    private String ERROR = "error:";
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
    public List<Country> listAll(int start, int length,String search,int column,String order,String language, Date createDateFrom,Date createDateTo){
        CountryDAO dao = new CountryDAO();
        try{
            return dao.listAll(start, length, search, column, order,language,  createDateFrom, createDateTo);
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
    public double getCount(String search,String language, Date createDateFrom,Date createDateTo, int length, int start){
        CountryDAO dao = new CountryDAO();
        try {
            if (search == null && language==null && createDateFrom == null && createDateTo == null){
                return dao.getCount();
            }else {
                return dao.getCountSearch(search, language, createDateFrom, createDateTo,length,start);
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
    public CountryDTO search(int start, int length,String search,int column,String order, String language, String createDateFrom,String createDateTo, int draw){
        CountryDTO dto = new CountryDTO();
        try{
            Date dateFrom =  DateSearchParse.parseDate(createDateFrom);
            Date dateTo =  DateSearchParse.parseDate(createDateTo, true);
            double count = getCount(search,language,dateFrom,dateTo,length,start);
            List<Country> listCountry = listAll(start,length,search,column,order,language,dateFrom,dateTo);
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
    public CountryDTO add(String id,String name, String description, String linkImage, boolean isDefault){
        CountryDAO dao = new CountryDAO();
        CountryDTO dto = new CountryDTO();

        try {
            if(!checkExistedName(name)) {
                if ((!checkIsDefautExisted() && isDefault==true) || (checkIsDefautExisted() && isDefault==false)) {
                    Country country = new Country();
                    country.setId(id);
                    country.setName(name);
                    country.setDescription(description);
                    country.setImageURL(linkImage);
                    country.setTimeCreated(new Date(System.currentTimeMillis()));
                    country.setIsDeleted(false);
                    country.setVersion(getMaxVersion());
                    country.setIsDefault(isDefault);
                    dao.create(country);
                    dto.setMessage(SUCCESS);
                } else {
                    dto.setMessage(ERROR + ":" + " isDefaut is existed");
                }
            }else {
                dto.setMessage(ERROR + ":" + " Country name is existed");
            }

        }catch (Exception e){
            dto.setMessage ( ERROR + ":" + " Country name is existed or isDefaut is existed");
            logger.error(" Can not add country to database because : " + e);
        }
        return dto;
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
        dto = add(idCountry, name, description, linkS3, isDefault);
        if(dto.getMessage().equalsIgnoreCase("success")){
            CountryMappingCourseService service = new CountryMappingCourseService();
            boolean condition2 = service.addMapping(idCountry, idCourse);
            if(condition2){
                dto.setMessage(SUCCESS);
            }else{
                dto.setMessage(ERROR +" an error has been occurred in server!");
            }
        }
        return dto;
    }
    public boolean checkIsDefautExisted(){
        CountryDAO dao = new CountryDAO();
        boolean check = false;
        try {
            check =  dao.checkIsDefautExisted();
        }catch (Exception e){
            logger.info("there are some thing with find level demo in database");
        }
        return check;
    }


    /**
     *
     * @param id
     * @param name
     * @param description
     * @param linkS3
     * @param isDefault
     * @param isUpdateImg
     * @return
     */
    public CountryDTO updateCountry(String id, String name, String description, String linkS3, boolean isDefault, boolean isUpdateImg){
        CountryDTO dto = new CountryDTO();
        CountryDAO dao = new CountryDAO();
        try{
            Country country=dao.getById(id);

            if(country!=null) {
                    if ((!checkIsDefautExisted() && isDefault == true) || (checkIsDefautExisted() && isDefault == false) || (checkIsDefautExisted()&& isDefault == true && country.isDefault()==true)) {
                        if (isUpdateImg) {
                            dao.updateCountry(id, name, description, linkS3, isDefault);
                        } else {
                            dao.updateCountry(id, name, description, isDefault);
                        }
                        dto.setMessage(SUCCESS);
                    } else {
                        dto.setMessage(ERROR + ":" + " isDefaut is existed");
                    }
            }else {
                dto.setMessage("deleted");
            }
        }catch (Exception e){
            dto.setMessage ( ERROR + ":" + "can not update country, because " + e.getMessage());
            logger.error("can not update country to database because : " + e.getMessage());
        }
        return dto;
    }

    /**
     *
     * @param idCountry
     * @param name
     * @param description
     * @param idCourse
     * @param linkS3
     * @param isDefault
     * @param isUpdateImg
     * @return
     */
    public CountryDTO update(String idCountry, String name, String description, String idCourse, String linkS3, boolean isDefault, boolean isUpdateImg){
        CountryDTO dto = new CountryDTO();
        dto = updateCountry(idCountry, name, description, linkS3, isDefault, isUpdateImg);
        if(dto.getMessage().equalsIgnoreCase("success")){
            CountryMappingCourseService service = new CountryMappingCourseService();
            boolean condition2 = service.updateIdCourseByIdCountry(idCountry, idCourse);
            if(condition2){
                dto.setMessage(SUCCESS);
            }else{
                dto.setMessage(ERROR +": an error has been occurred in server!");
            }
        }
        return dto;
    }

    public CountryDTO detele(String idCountry){
        CountryDTO dto = new CountryDTO();
        CountryDAO dao = new CountryDAO();
        try {
            Country country=dao.getById(idCountry);
            if(country!=null) {
                Boolean isDelete = updateDeleted(idCountry);
                if (isDelete) {
                    CountryMappingCourseService service = new CountryMappingCourseService();
                    boolean isDeleteMapping = service.updateDeletedByIdCountry(idCountry);
                    if (isDeleteMapping) {
                        dto.setMessage(SUCCESS);
                    } else {
                        dto.setMessage(ERROR + ": an error has been occurred in server!");
                    }
                }
            }else {
                dto.setMessage("deleted");
            }
        }catch (Exception e){
            dto.setMessage ( ERROR + ":" + "can not delete country, because " + e.getMessage());
            logger.error("can not delete country to database because : " + e.getMessage());
        }

        return dto;
    }

    public boolean updateDeleted(String id){
        CountryDAO dao = new CountryDAO();
        try {
            return dao.updateDeleted(id);
        }catch (Exception e){
            logger.info("delete country error : " + e);
        }
        return false;
    }

}
