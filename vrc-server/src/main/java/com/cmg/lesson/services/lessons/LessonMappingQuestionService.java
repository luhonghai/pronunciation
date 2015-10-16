package com.cmg.lesson.services.lessons;

import com.cmg.lesson.dao.lessons.LessonMappingQuestionDAO;
import org.apache.log4j.Logger;

/**
 * Created by CMG Dev156 on 10/16/2015.
 */
public class LessonMappingQuestionService {
    private static final Logger logger = Logger.getLogger(LessonCollectionService.class.getName());
    private String SUCCESS = "success";
    private String ERROR = "error";

    /**
     *  use for get max version
     * @return max version in table
     */
    public int getMaxVersion(){
        int version = 0;
        LessonMappingQuestionDAO dao = new LessonMappingQuestionDAO();
        try {
            version = dao.getLatestVersion();
        }catch (Exception e){
            logger.info("Can not get max version in table because : " + e.getMessage());
        }
        return version +1;
    }


}
