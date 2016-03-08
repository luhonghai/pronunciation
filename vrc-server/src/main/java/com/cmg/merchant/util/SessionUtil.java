package com.cmg.merchant.util;

import com.amazonaws.http.HttpRequest;
import com.cmg.vrc.util.StringUtil;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by lantb on 2016-02-03.
 */
public class SessionUtil {
    private static final Logger logger = Logger.getLogger(SessionUtil.class
            .getName());
    public static String ATT_CPID = "companyId";
    public static String ATT_TID = "teacherId";
    public static String ATT_CPNAME = "companyName";
    private static String INVALID = "invalid";


    /**
     *
     * @param request
     * @return true if session contain the cpid and tid
     */
    public boolean checkSessionValid(HttpServletRequest request){
        boolean check = false;
        try {
            HttpSession session = request.getSession();
            String cpId = (String) StringUtil.isNull(session.getAttribute(ATT_CPID),"invalid");
            String tId =  (String) StringUtil.isNull(session.getAttribute(ATT_TID),"invalid");
            if(cpId!= INVALID && tId!=INVALID){
                check = true;
            }
        }catch (Exception e){
            logger.error("can not get cpid and tid in session attribute");
        }
        return check;
    }

    /**
     *
     * @param request
     * @return company name
     */
    public String getCompanyName(HttpServletRequest request){
        String companyName = (String) StringUtil.isNull(request.getSession().getAttribute(ATT_CPNAME),"");
        return companyName;
    }

    /**
     *
     * @param request
     * @return cpid
     */
    public String getCpId(HttpServletRequest request){
        String cpId = (String) StringUtil.isNull(request.getSession().getAttribute(ATT_CPID),"");
        return cpId;
    }

    /**
     *
     * @param request
     * @return tid
     */
    public String getTid(HttpServletRequest request){
        String tId = (String) StringUtil.isNull(request.getSession().getAttribute(ATT_TID),"");
        return tId;
    }
}
