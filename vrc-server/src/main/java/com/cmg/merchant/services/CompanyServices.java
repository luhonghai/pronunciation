package com.cmg.merchant.services;

import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.merchant.common.Constant;
import com.cmg.merchant.dao.company.CPDAO;
import com.cmg.merchant.dao.course.CDAO;
import com.cmg.merchant.dao.mapping.CMTDAO;
import com.cmg.merchant.data.dto.CourseDTO;
import com.cmg.vrc.data.jdo.ClientCode;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lantb on 2016-02-02.
 */
public class CompanyServices {

    private static final Logger logger = Logger.getLogger(CompanyServices.class
            .getName());

    public ArrayList<String> suggestionCompany(String company){
        CMTDAO dao = new CMTDAO();
        ArrayList<String> listSuggestion = new ArrayList<>();
        try {
            List<CourseDTO> cpList = dao.suggestCompany(Constant.STATUS_PUBLISH, company);
            if(cpList!=null && cpList.size() > 0){
                for(CourseDTO c : cpList){
                    listSuggestion.add(c.getCompanyName());
                }
            }
        }catch (Exception e){
            logger.error("can not retrieve data for suggestion company : " + e);
        }
        return listSuggestion;
    }
}
