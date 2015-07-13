package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.LicenseCode;
import com.cmg.vrc.data.jdo.LicenseCodeJDO;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import java.util.*;

/**
 * Created by cmg on 5/18/15.
 */
public class LicenseCodeDAO extends DataAccess<LicenseCodeJDO, LicenseCode> {

    public LicenseCodeDAO() {
        super(LicenseCodeJDO.class, LicenseCode.class);
    }

    public void update(String id, boolean acti) throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Transaction tx = pm.currentTransaction();

        Query q = pm.newQuery("SELECT FROM " + LicenseCodeJDO.class.getCanonicalName());



        try {
            tx.begin();
            List<LicenseCodeJDO> tmp = (List<LicenseCodeJDO>) q.execute();
            Iterator<LicenseCodeJDO> iter = tmp.iterator();
            while (iter.hasNext()) {

            }
            tx.commit();

        } catch (Exception e) {
            throw e;
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            q.closeAll();
            pm.close();
        }
    }
    public List<LicenseCode> listAll(int start, int length,String search,int column,String order,String ac,String co, String acti,Date dateFrom,Date dateTo) throws Exception {

        PersistenceManager pm = PersistenceManagerHelper.get();
        Transaction tx = pm.currentTransaction();
        List<LicenseCode> list = new ArrayList<LicenseCode>();
        Query q = pm.newQuery("SELECT FROM " + LicenseCodeJDO.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a="((account.toLowerCase().indexOf(search.toLowerCase()) != -1)||(code.toLowerCase().indexOf(search.toLowerCase()) != -1))";
        String b="((account == null || account.toLowerCase().indexOf(search.toLowerCase()) != -1)||(code == null || code.toLowerCase().indexOf(search.toLowerCase()) != -1))";

        if(ac.length()>0){
            string.append("(account.toLowerCase().indexOf(ac.toLowerCase()) != -1) &&");
        }
        if(co.length()>0){
            string.append("(code.toLowerCase().indexOf(co.toLowerCase()) != -1) &&");
        }
        if(acti.equals("Yes")){
            string.append("isActivated==true &&");
        }
        if(acti.equals("No")){
            string.append("isActivated==false &&");
        }
        if(dateFrom!=null&&dateTo==null){
            string.append("(activatedDate >= dateFrom) &&");
        }
        if(dateFrom==null&&dateTo!=null){
            string.append("(activatedDate <= dateTo) &&");
        }

        if(dateFrom!=null&&dateTo!=null){
            string.append("(activatedDate >= dateFrom && activatedDate <= dateTo) &&");
        }
        if(search.length()>0){
            string.append(a);
        }
        if(search.length()==0){
            string.append(b);
        }
        q.setFilter(string.toString());
        q.declareParameters("String search, String ac, String co,java.util.Date dateFrom,java.util.Date dateTo");
        Map<String, Object> params = new HashMap<String, Object>();
            params.put("search", search);
            params.put("ac", ac);
            params.put("co", co);
            params.put("dateFrom", dateFrom);
            params.put("dateTo", dateTo);

        if (column==0 && order.equals("asc")) {
            q.setOrdering("account asc");
        }else if(column==0 && order.equals("desc")) {
            q.setOrdering("account desc");
        }
        if (column==1 && order.equals("asc")) {
            q.setOrdering("code asc");
        }else if(column==1 && order.equals("desc")) {
            q.setOrdering("code desc");
        }
        if (column==2 && order.equals("asc")) {
            q.setOrdering("activatedDate asc");
        }else if(column==2 && order.equals("desc")) {
            q.setOrdering("activatedDate desc");
        }

        q.setRange(start, start + length);

        try {
            tx.begin();
            List<LicenseCodeJDO> tmp = (List<LicenseCodeJDO>)q.executeWithMap(params);
            Iterator<LicenseCodeJDO> iter = tmp.iterator();
            while (iter.hasNext()) {
                list.add(to(iter.next()));
            }
            tx.commit();
            return list;
        } catch (Exception e) {
            throw e;
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            q.closeAll();
            pm.close();
        }
    }

    public LicenseCode getByCode(String code) throws Exception {
        List<LicenseCode> list = list("WHERE code == :1", code);
        if (list != null && list.size() > 0)
            return list.get(0);
        return null;
    }

    public LicenseCode getByEmail(String email) throws Exception {
        List<LicenseCode> list = listByEmail(email);
        if (list != null && list.size() > 0)
            return list.get(0);
        return null;
    }

    public List<LicenseCode> listByEmail(String email) throws Exception {
        return list("WHERE account == :1", email);
    }
    public double getCountSearch(String search, String ac,String co, String acti,Date dateFrom,Date dateTo) throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Transaction tx = pm.currentTransaction();
        Long count;
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + LicenseCodeJDO.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a="((account.toLowerCase().indexOf(search.toLowerCase()) != -1)||(code.toLowerCase().indexOf(search.toLowerCase()) != -1))";
        String b="((account == null || account.toLowerCase().indexOf(search.toLowerCase()) != -1)||(code == null || code.toLowerCase().indexOf(search.toLowerCase()) != -1))";

        if(ac.length()>0){
            string.append("(account.toLowerCase().indexOf(ac.toLowerCase()) != -1) &&");
        }
        if(co.length()>0){
            string.append("(code.toLowerCase().indexOf(co.toLowerCase()) != -1) &&");
        }
        if(acti.equals("Yes")){
            string.append("isActivated==true &&");
        }
        if(acti.equals("No")){
            string.append("isActivated==false &&");
        }
        if(dateFrom!=null&&dateTo==null){
            string.append("(activatedDate >= dateFrom) &&");
        }
        if(dateFrom==null&&dateTo!=null){
            string.append("(activatedDate <= dateTo) &&");
        }


        if(dateFrom!=null&&dateTo!=null){
            string.append("(activatedDate >= dateFrom && activatedDate <= dateTo) &&");
        }
        if(search.length()>0){
            string.append(a);
        }
        if(search.length()==0){
            string.append(b);
        }
        q.setFilter(string.toString());
        q.declareParameters("String search, String ac, String co,java.util.Date dateFrom,java.util.Date dateTo");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
        params.put("ac", ac);
        params.put("co", co);
        params.put("dateFrom", dateFrom);
        params.put("dateTo", dateTo);
        try {
            tx.begin();
            count = (Long) q.executeWithMap(params);
            tx.commit();
            return count.doubleValue();
        } catch (Exception e) {
            throw e;
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            q.closeAll();
            pm.close();
        }
    }
}
