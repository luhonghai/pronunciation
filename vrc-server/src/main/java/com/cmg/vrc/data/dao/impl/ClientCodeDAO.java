package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.ClientCode;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CMGT400 on 8/7/2015.
 */
public class ClientCodeDAO  extends DataAccess<ClientCode> {
    public ClientCodeDAO(){
        super(ClientCode.class);
    }

    public ClientCode getUserByEmailPassword(String email, String password) throws Exception{
        List<ClientCode> userList = list("WHERE userName == :1 && password == :2", email, password);
        if (userList != null && userList.size() > 0)
            return userList.get(0);
        return null;
    }
    public ClientCode getUserName(String name) throws Exception {
        List<ClientCode> userList = list("WHERE companyName == :1", name);
        if (userList != null && userList.size() > 0)
            return userList.get(0);
        return null;
    }

    public List<ClientCode> listAll(int start, int length,String search,int column,String order,String company,String contact,String emails) throws Exception {

        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT FROM " + ClientCode.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a="((companyName.toLowerCase().indexOf(search.toLowerCase()) != -1)||(contactName.toLowerCase().indexOf(search.toLowerCase()) != -1)||(email.toLowerCase().indexOf(search.toLowerCase()) != -1))";
        String b="((companyName == null || companyName.toLowerCase().indexOf(search.toLowerCase()) != -1)||(contactName == null || contactName.toLowerCase().indexOf(search.toLowerCase()) != -1)||(email == null || email.toLowerCase().indexOf(search.toLowerCase()) != -1))";

        if(company.length()>0){
            string.append("(companyName.toLowerCase().indexOf(company.toLowerCase()) != -1) &&");
        }
        if(contact.length()>0){
            string.append("(contactName.toLowerCase().indexOf(contact.toLowerCase()) != -1) &&");
        }
        string.append("(isDeleted==false) &&");
        if(emails.length()>0){
            string.append("(email.toLowerCase().indexOf(emails.toLowerCase()) != -1) &&");
        }

        if(search.length()>0){
            string.append(a);
        }
        if(search.length()==0){
            string.append(b);
        }
        q.setFilter(string.toString());
        q.declareParameters("String search, String company, String contact, String emails");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
        params.put("company", company);
        params.put("contact", contact);
        params.put("emails", emails);

        if (column==0 && order.equals("asc")) {
            q.setOrdering("companyName asc");
        }else if(column==0 && order.equals("desc")) {
            q.setOrdering("companyName desc");
        }
        if (column==1 && order.equals("asc")) {
            q.setOrdering("contactName asc");
        }else if(column==1 && order.equals("desc")) {
            q.setOrdering("contactName desc");
        }
        if (column==2 && order.equals("asc")) {
            q.setOrdering("email asc");
        }else if(column==2 && order.equals("desc")) {
            q.setOrdering("email desc");
        }
        q.setRange(start, start + length);

        try {
            List<ClientCode> tmp = (List<ClientCode>)q.executeWithMap(params);
            pm.detachCopyAll(tmp);
            return tmp;
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }

    public double getCountSearch(String search,String company,String contact,String emails) throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Transaction tx = pm.currentTransaction();
        Long count;
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + ClientCode.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a="((companyName.toLowerCase().indexOf(search.toLowerCase()) != -1)||(contactName.toLowerCase().indexOf(search.toLowerCase()) != -1)||(email.toLowerCase().indexOf(search.toLowerCase()) != -1))";
        String b="((companyName == null || companyName.toLowerCase().indexOf(search.toLowerCase()) != -1)||(contactName == null || contactName.toLowerCase().indexOf(search.toLowerCase()) != -1)||(email == null || email.toLowerCase().indexOf(search.toLowerCase()) != -1))";

        if(company.length()>0){
            string.append("(companyName.toLowerCase().indexOf(company.toLowerCase()) != -1) &&");
        }
        if(contact.length()>0){
            string.append("(contactName.toLowerCase().indexOf(contact.toLowerCase()) != -1) &&");
        }
        string.append("(isDeleted==false) &&");
        if(emails.length()>0){
            string.append("(email.toLowerCase().indexOf(emails.toLowerCase()) != -1) &&");
        }
        if(search.length()>0){
            string.append(a);
        }
        if(search.length()==0){
            string.append(b);
        }
        q.setFilter(string.toString());
        q.declareParameters("String search, String company, String contact, String emails");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
        params.put("company", company);
        params.put("contact", contact);
        params.put("emails", emails);
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

    public List<ClientCode> listAll() throws Exception {
        return list("WHERE isDeleted == false");
    }

    public double getCount() throws Exception {
        return getCount("WHERE isDeleted == false");
    }

}


