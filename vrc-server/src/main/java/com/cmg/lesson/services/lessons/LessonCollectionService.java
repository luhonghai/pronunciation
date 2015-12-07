package com.cmg.lesson.services.lessons;

import com.cmg.lesson.common.DateSearchParse;
import com.cmg.lesson.dao.lessons.LessonCollectionDAO;
import com.cmg.lesson.dao.objectives.ObjectiveMappingDAO;
import com.cmg.lesson.dao.test.TestMappingDAO;
import com.cmg.lesson.data.dto.lessons.LessonCollectionDTO;
import com.cmg.lesson.data.jdo.lessons.LessonCollection;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

/**
 * Created by CMG Dev156 on 10/14/2015.
 */
public class LessonCollectionService {
    private static final Logger logger = Logger.getLogger(LessonCollectionService.class.getName());
    private String SUCCESS = "success";
    private String ERROR = "error";

    /**
     *  use for get max version
     * @return max version in table
     */
    public int getMaxVersion(){
        int version = 0;
        LessonCollectionDAO dao = new LessonCollectionDAO();
        try {
            version = dao.getLatestVersion();
        }catch (Exception e){
            logger.info("Can not get max version in table because : " + e.getMessage());
        }
        return version +1;
    }

    /**
     *
     * @param id
     * @param name
     * @param description
     * @return
     */
    public LessonCollectionDTO updateLesson(String id, String name,String title, String shortDescription,String description, boolean isUpdateLessonName){
        LessonCollectionDTO dto = new LessonCollectionDTO();
        LessonCollectionDAO dao = new LessonCollectionDAO();
        String message;
        try{
            LessonCollection lessonCollection=dao.getById(id);
            if(lessonCollection!=null) {
                if (isUpdateLessonName) {
                    if (!isExistLessonName(name)) {
                        boolean isUpdate = dao.updateLesson(id, name, title, shortDescription, description);
                        if (isUpdate) {
                            message = SUCCESS;
                        } else {
                            message = ERROR + ":" + "An error has been occurred in server!";
                        }
                    } else {
                        message = ERROR + ":" + "Lesson name is existed";
                    }
                } else {
                    boolean isUpdate = dao.updateDescription(id, title, shortDescription, description);
                    if (isUpdate) {
                        message = SUCCESS;
                    } else {
                        message = ERROR + ":" + "An error has been occurred in server!";
                    }
                }
            }else {
                message="deleted";
            }
        }catch(Exception e){
            message = ERROR + ": "+ e.getMessage();
            logger.error("Can not update LessionCollection : " + name + " because : " + e.getMessage());
        }
        dto.setMessage(message);
        return dto;
    }

    /**
     *
     * @param name
     * @return true if question was added to table.
     */
    public LessonCollectionDTO addLesson( String name,String title,String shortDescription,String longDescription){
        LessonCollectionDAO dao = new LessonCollectionDAO();
        LessonCollectionDTO dto = new LessonCollectionDTO();
        String message;
        try {
            if(!isExistLessonName(name)) {
                LessonCollection l = new LessonCollection();
                l.setNameUnique(name);
                l.setTitle(title);
                l.setName(shortDescription);
                l.setDescription(longDescription);
                l.setVersion(getMaxVersion());
                l.setDateCreated(new Date(System.currentTimeMillis()));
                l.setIsDeleted(false);
                dao.create(l);
                message = SUCCESS;
            }else{
                message = ERROR + ":" + "LessonCollection name is existed";
            }
        }catch(Exception e){
            message = ERROR + ":" + e.getMessage();
            logger.error("Can not add LessonCollection : " + name + " because : " + e.getMessage());
        }
        dto.setMessage(message);
        return dto;
    }

    /**
     *
     * @param id
     * @return
     */
    public LessonCollectionDTO deleteLesson(String id){
        LessonCollectionDTO dto = new LessonCollectionDTO();
        LessonCollectionDAO dao = new LessonCollectionDAO();
        String message;
        try{
            LessonCollection lessonCollection=dao.getById(id);
            if(lessonCollection!=null) {
                boolean isDelete = dao.deletedLesson(id);
                if (isDelete) {
                    message = SUCCESS;
                    //function for  delete mapping lesson with objective and test
                    deleteMapping(id);
                } else {
                    message = ERROR + ": " + "An error has been occurred in server!";
                }
            }else {
                message ="deleted";
            }
        }catch(Exception e){
            message = ERROR + ": "+ e.getMessage();
            logger.error("Can not delete LessonCollection id: " + id + " because : " + e.getMessage());
        }
        dto.setMessage(message);
        return dto;
    }

    /**
     *
     * @param id
     */
    public void deleteMapping(String id){
        try {
            ObjectiveMappingDAO daoObj = new ObjectiveMappingDAO();
            daoObj.updateDeletedByLesson(id);
            TestMappingDAO daoTest = new TestMappingDAO();
            daoTest.updateDeletedByLesson(id);
        }catch (Exception e){
            logger.debug("can not delete mapping : " + e.getMessage());
        }
    }

    /**
     *
     * @param name
     * @return true is exits question name
     */
    public boolean isExistLessonName(String name) throws Exception{
        boolean isExist = false;
        LessonCollectionDAO dao = new LessonCollectionDAO();
        isExist = dao.checkExist(name);
        return isExist;
    }

    /**
     *
     * @param id
     * @return
     */
    public LessonCollection getById(String id){
        LessonCollectionDAO dao = new LessonCollectionDAO();
        try {
            return dao.getById(id);
        }catch (Exception e){
            logger.info("can not get lesson by id : " + id);
        }
        return null;
    }

    /**
     *
     * @return All lesson
     */
    public LessonCollectionDTO getAll(){
        LessonCollectionDAO dao = new LessonCollectionDAO();
        LessonCollectionDTO dto = new LessonCollectionDTO();
        try {
            dto.setData(dao.getAll());
            dto.setMessage(SUCCESS);
        }catch (Exception e){
            dto.setMessage(ERROR + ": can not get lesson " + e.getMessage());
            logger.info("can not get lesson" + e.getMessage());
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
    public double getCount(String search,String lesson,Date createDateFrom,Date createDateTo, int length, int start){
        LessonCollectionDAO dao = new LessonCollectionDAO();
        try {
            if (search == null && lesson==null && createDateFrom == null && createDateTo == null){
                return dao.getCount();
            }else {
                return dao.getCountSearch(search,lesson, createDateFrom, createDateTo,length,start);
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
    public List<LessonCollection> listAll(int start, int length,String search,int column,String order,String lesson, Date createDateFrom,Date createDateTo){
        LessonCollectionDAO dao = new LessonCollectionDAO();
        try{
            return dao.listAll(start, length, search,column, order,lesson, createDateFrom, createDateTo);
        }catch (Exception ex){
            logger.error("list all question error, because:" + ex.getMessage());
        }
        return null;
    }

    public LessonCollectionDTO search(int start, int length,String search,int column,String order,String lesson,String createDateFrom,String createDateTo, int draw){
        LessonCollectionDTO dto = new LessonCollectionDTO();
        try{
            Date dateFrom = DateSearchParse.parseDate(createDateFrom);
            Date dateTo = DateSearchParse.parseDate(createDateTo, true);
            double count = getCount(search,lesson,dateFrom,dateTo,length,start);
            List<LessonCollection> listLesson = listAll(start,length,search,column,order,lesson,dateFrom,dateTo);
            dto.setDraw(draw);
            dto.setRecordsFiltered(count);
            dto.setRecordsTotal(count);
            dto.setData(listLesson);
        }catch (Exception e){
            dto.setMessage(ERROR + ": " + "Search LessonCollection error, because:" + e.getMessage());
            logger.error("Search LessonCollection error, because:" + e.getMessage());
        }
        return dto;
    }

}
