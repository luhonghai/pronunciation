package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.Feedback;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luhonghai on 5/8/15.
 */
public class FeedbackDAO extends DataAccess<Feedback> {

    public FeedbackDAO() {
        super(Feedback.class);
    }
    public List<Feedback> listAll(int start, int length,String search,int column,String order,String ac,String app, String os,String imei,Date dateFrom,Date dateTo) throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT FROM " + Feedback.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a="((account.toLowerCase().indexOf(search.toLowerCase()) != -1)||(appVersion.toLowerCase().indexOf(search.toLowerCase()) != -1)||(osVersion.toLowerCase().indexOf(search.toLowerCase()) != -1)||(imei.toLowerCase().indexOf(search.toLowerCase()) != -1))";
        String b="((account == null || account.toLowerCase().indexOf(search.toLowerCase()) != -1)||(appVersion == null || appVersion.toLowerCase().indexOf(search.toLowerCase()) != -1)||(osVersion == null || osVersion.toLowerCase().indexOf(search.toLowerCase()) != -1)||(imei == null || imei.toLowerCase().indexOf(search.toLowerCase()) != -1))";
        if(ac.length()>0){
            string.append("(account.toLowerCase().indexOf(ac.toLowerCase()) != -1) &&");
        }
        if(app.length()>0){
            string.append("(appVersion.toLowerCase().indexOf(app.toLowerCase()) != -1) &&");
        }
        if(os.length()>0){
            string.append("(osVersion.toLowerCase().indexOf(os.toLowerCase()) != -1) &&");
        }
        if(imei.length()>0){
            string.append("(imei.toLowerCase().indexOf(imei.toLowerCase()) != -1) &&");
        }
        if(dateFrom!=null&&dateTo==null){
            string.append("(createdDate >= dateFrom) &&");
        }
        if(dateFrom==null&&dateTo!=null){
            string.append("(createdDate <= dateTo) &&");
        }

        if(dateFrom!=null&&dateTo!=null){
            string.append("(createdDate >= dateFrom && createdDate <= dateTo) &&");
        }
        if(search.length()>0){
            string.append(a);
        }
        if(search.length()==0){
            string.append(b);
        }
        q.setFilter(string.toString());
        q.declareParameters("String search, String ac, String app,String os,String imei,java.util.Date dateFrom,java.util.Date dateTo");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
        params.put("ac", ac);
        params.put("app", app);
        params.put("os", os);
        params.put("imei", imei);
        params.put("dateFrom", dateFrom);
        params.put("dateTo", dateTo);

        if (column==0 && order.equals("asc")) {
            q.setOrdering("account asc");
        }else if(column==0 && order.equals("desc")) {
            q.setOrdering("account desc");
        }
        if (column==1 && order.equals("asc")) {
            q.setOrdering("imei asc");
        }else if(column==1 && order.equals("desc")) {
            q.setOrdering("imei desc");
        }
        if (column==2 && order.equals("asc")) {
            q.setOrdering("appVersion asc");
        }else if(column==2 && order.equals("desc")) {
            q.setOrdering("appVersion desc");
        }
        if (column==3 && order.equals("asc")) {
            q.setOrdering("osVersion asc");
        }else if(column==3 && order.equals("desc")) {
            q.setOrdering("osVersion desc");
        }
        if (column==4 && order.equals("asc")) {
            q.setOrdering("createdDate asc");
        }else if(column==4 && order.equals("desc")) {
            q.setOrdering("createdDate desc");
        }

        q.setRange(start, start + length);
        try {
            List<Feedback> tmp = (List<Feedback>)q.executeWithMap(params);
            pm.detachCopyAll(tmp);
            return tmp;
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }
    public double getCountSearch(String search, String ac,String app, String os,String imei,Date dateFrom,Date dateTo) throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Long count;
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + Feedback.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a="((account.toLowerCase().indexOf(search.toLowerCase()) != -1)||(appVersion.toLowerCase().indexOf(search.toLowerCase()) != -1)||(osVersion.toLowerCase().indexOf(search.toLowerCase()) != -1)||(imei.toLowerCase().indexOf(search.toLowerCase()) != -1))";
        String b="((account == null || account.toLowerCase().indexOf(search.toLowerCase()) != -1)||(appVersion == null || appVersion.toLowerCase().indexOf(search.toLowerCase()) != -1)||(osVersion == null || osVersion.toLowerCase().indexOf(search.toLowerCase()) != -1)||(imei == null || imei.toLowerCase().indexOf(search.toLowerCase()) != -1))";
        if(ac.length()>0){
            string.append("(account.toLowerCase().indexOf(ac.toLowerCase()) != -1) &&");
        }
        if(app.length()>0){
            string.append("(appVersion.toLowerCase().indexOf(app.toLowerCase()) != -1) &&");
        }
        if(os.length()>0){
            string.append("(osVersion.toLowerCase().indexOf(os.toLowerCase()) != -1) &&");
        }
        if(imei.length()>0){
            string.append("(imei.toLowerCase().indexOf(imei.toLowerCase()) != -1) &&");
        }
        if(dateFrom!=null&&dateTo==null){
            string.append("(createdDate >= dateFrom) &&");
        }
        if(dateFrom==null&&dateTo!=null){
            string.append("(createdDate <= dateTo) &&");
        }


        if(dateFrom!=null&&dateTo!=null){
            string.append("(createdDate >= dateFrom && createdDate <= dateTo) &&");
        }
        if(search.length()>0){
            string.append(a);
        }
        if(search.length()==0){
            string.append(b);
        }
        q.setFilter(string.toString());
        q.declareParameters("String search, String ac, String app,String os,String imei,java.util.Date dateFrom,java.util.Date dateTo");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
        params.put("ac", ac);
        params.put("app", app);
        params.put("os", os);
        params.put("imei", imei);
        params.put("dateFrom", dateFrom);
        params.put("dateTo", dateTo);

        try {
            count = (Long) q.executeWithMap(params);
            return count.doubleValue();
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }
}
