package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.LicenseCode;
import com.cmg.vrc.data.jdo.LicenseCodeCompanyJDO;
import com.cmg.vrc.data.jdo.LicenseCodeJDO;
import com.cmg.vrc.util.PersistenceManagerHelper;
import com.google.gson.Gson;
import org.apache.poi.util.SystemOutLogger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.jdo.metadata.TypeMetadata;
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



    public List<LicenseCode> listAll(int start, int length,String search,int column,String order,String ac, String acti,Date dateFrom,Date dateTo, Date dateFrom2,Date dateTo2) throws Exception {

        PersistenceManager pm = PersistenceManagerHelper.get();
        Transaction tx = pm.currentTransaction();
        List<LicenseCode> list = new ArrayList<LicenseCode>();
        Query q = pm.newQuery("SELECT FROM " + LicenseCodeJDO.class.getCanonicalName() );
        StringBuffer string=new StringBuffer();
        String a="((account.toLowerCase().indexOf(search.toLowerCase()) != -1)||(code.toLowerCase().indexOf(search.toLowerCase()) != -1))";
        String b="((account == null || account.toLowerCase().indexOf(search.toLowerCase()) != -1)||(code == null || code.toLowerCase().indexOf(search.toLowerCase()) != -1))";

        if(ac.length()>0){
            string.append("(account.toLowerCase().indexOf(ac.toLowerCase()) != -1) &&");
        }
        string.append("(isDeleted==false) &&");
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

        if(dateFrom2!=null&&dateTo2==null){
            string.append("(createdDate >= dateFrom2) &&");
        }
        if(dateFrom2==null&&dateTo2!=null){
            string.append("(createdDate <= dateTo2) &&");
        }

        if(dateFrom2!=null&&dateTo2!=null){
            string.append("(createdDate >= dateFrom2 && createdDate <= dateTo2) &&");
        }


        if(search.length()>0){
            string.append(a);
        }
        if(search.length()==0){
            string.append(b);
        }
        q.setFilter(string.toString());
        q.declareParameters("String search, String ac, java.util.Date dateFrom, java.util.Date dateTo, java.util.Date dateFrom2, java.util.Date dateTo2");
        Map<String, Object> params = new HashMap<String, Object>();
            params.put("search", search);
            params.put("ac", ac);
            params.put("dateFrom", dateFrom);
            params.put("dateTo", dateTo);
            params.put("dateFrom2", dateFrom2);
            params.put("dateTo2", dateTo2);

        if (column==0 && order.equals("asc")) {
            q.setOrdering("account asc");
        }else if(column==0 && order.equals("desc")) {
            q.setOrdering("account desc");
        }
        if (column==3 && order.equals("asc")) {
            q.setOrdering("createdDate asc");
        }else if(column==3 && order.equals("desc")) {
            q.setOrdering("createdDate desc");
        }
        if (column==4 && order.equals("asc")) {
            q.setOrdering("activatedDate asc");
        }else if(column==4 && order.equals("desc")) {
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


    public List<LicenseCode> listAllByCompany(int start, int length,String search,String ac, String acti,Date dateFrom,Date dateTo, Date dateFrom2,Date dateTo2,String com) throws Exception {

        PersistenceManager pm = PersistenceManagerHelper.get();
        Transaction tx = pm.currentTransaction();
        List<LicenseCode> list = new ArrayList<LicenseCode>();
        StringBuffer query=new StringBuffer();
        TypeMetadata metaLicenseCode = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(LicenseCodeJDO.class.getCanonicalName());
        TypeMetadata metaLicenseCodeCompany = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(LicenseCodeCompanyJDO.class.getCanonicalName());
        String firstQuery = "select code.* from " +  metaLicenseCode.getTable()
                + " code join "  + metaLicenseCodeCompany.getTable()
                + " mapping on mapping.CODE=code.CODE where mapping.COMPANY='"+com+"'";
        query.append(firstQuery);
        if(ac.length()>0){
            query.append(" and code.account LIKE '%" + ac + "%'");
        }
        query.append(" and code.isDeleted=false");
        if (acti.equals("Yes")){
            query.append(" and code.isActivated=true");
        }
        if(acti.equals("No")){
            query.append(" and code.isActivated=false");
        }
        if(dateFrom!=null&&dateTo==null){
            query.append(" and code.activatedDate >= '" + dateFrom + "'");
        }
        if(dateFrom==null&&dateTo!=null){
            query.append(" and code.activatedDate <= '" + dateTo + "'");
        }

        if(dateFrom!=null&&dateTo!=null){
            query.append(" and code.activatedDate >= '" + dateFrom + "' and code.activatedDate <= '" + dateTo + "'");
        }
        if(dateFrom2!=null&&dateTo2==null){
            query.append(" and code.createdDate >= '" + dateFrom2 + "'");
        }
        if(dateFrom2==null&&dateTo2!=null){
            query.append(" and code.createdDate <= '" + dateTo2 + "'");
        }

        if(dateFrom2!=null&&dateTo2!=null){
            query.append(" and code.createdDate >= '" + dateFrom2 + "' and code.createdDate <= '" + dateTo2 + "'");
        }

        if(search.length()>0){
            query.append(" and code.account LIKE '%" + search + "%' and code.code LIKE '%" + search + "%' and code.imei LIKE '%" + search + "%'");
        }
        System.out.println(query);



        Query q = pm.newQuery("javax.jdo.query.SQL",query.toString());
        q.setRange(start, start + length);


        try {
            tx.begin();
            List<Object> tmp = (List<Object>)q.execute();

            for(Object obj : tmp){
                LicenseCode licenseCode=new LicenseCode();
                Object[] array =(Object[]) obj;
               System.out.println(array[6]);
               // array.get(1);
                if(array[0].toString().length()>0) {
                    licenseCode.setId(array[0].toString());
                }
                else{
                    licenseCode.setId(null);
                }
                if(array[1]!=null) {
                    licenseCode.setAccount(array[1].toString());
                }
                else{
                    licenseCode.setAccount(null);
                }
                if(array[2]!=null) {
                    licenseCode.setActivatedDate((Date) array[2]);
                }
                else {
                    licenseCode.setActivatedDate(null);
                }
                if(array[3]!=null) {
                    licenseCode.setCode(array[3].toString());
                }else{
                    licenseCode.setCode(null);
                }
                if(array[5]!=null) {
                    licenseCode.setActivated((boolean) array[5]);
                }else{
                    licenseCode.setActivated(false);
                }
                if(array[4]!=null) {
                    licenseCode.setImei(array[4].toString());
                }else {
                    licenseCode.setImei(null);
                }
                if(array[6]!=null) {
                    licenseCode.setIsDeleted((boolean) array[6]);
                }else {
                    licenseCode.setIsDeleted(false);
                }
                if(array[7]!=null) {
                    licenseCode.setCreatedDate((Date) array[7]);
                }
                else {
                    licenseCode.setCreatedDate(null);
                }


                list.add(licenseCode);

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
    public double getCountSearch(String search, String ac, String acti,Date dateFrom,Date dateTo, Date dateFrom2,Date dateTo2) throws Exception {
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
        string.append("(isDeleted==false) &&");
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

        if(dateFrom2!=null&&dateTo2==null){
            string.append("(createdDate >= dateFrom2) &&");
        }
        if(dateFrom2==null&&dateTo2!=null){
            string.append("(createdDate <= dateTo2) &&");
        }

        if(dateFrom2!=null&&dateTo2!=null){
            string.append("(createdDate >= dateFrom2 && createdDate <= dateTo2) &&");
        }


        if(search.length()>0){
            string.append(a);
        }
        if(search.length()==0){
            string.append(b);
        }
        q.setFilter(string.toString());
        q.declareParameters("String search, String ac, java.util.Date dateFrom, java.util.Date dateTo, java.util.Date dateFrom2, java.util.Date dateTo2");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
        params.put("ac", ac);
        params.put("dateFrom", dateFrom);
        params.put("dateTo", dateTo);
        params.put("dateFrom2", dateFrom2);
        params.put("dateTo2", dateTo2);
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
