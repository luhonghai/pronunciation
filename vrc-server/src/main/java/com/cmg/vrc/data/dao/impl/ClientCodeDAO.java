package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.ClientCode;
import com.cmg.vrc.data.jdo.TeacherMappingCompany;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.jdo.metadata.TypeMetadata;
import java.util.ArrayList;
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
            return detachCopyAllList(pm, q.executeWithMap(params));
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }

    public double getCountSearch(String search,String company,String contact,String emails) throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
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
            count = (Long) q.executeWithMap(params);
            return count.doubleValue();
        } catch (Exception e) {
            throw e;
        } finally {
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

    public List<ClientCode> getCompanyByTeacherName(String teacherName){
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaTeacherMappingCompany = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(TeacherMappingCompany.class.getCanonicalName());
        TypeMetadata metaClientCode = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(ClientCode.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL","SELECT id, companyName, email FROM " + metaClientCode.getTable() + " WHERE companyName not IN (select company FROM " + metaTeacherMappingCompany.getTable() + " WHERE teacherName='"+teacherName+"' and isDeleted = false) and isDeleted = false");
        try {
            List<ClientCode> clientCodes = new ArrayList<>();
            List<Object> objects = (List<Object>) q.execute();
            for (Object object : objects) {
                Object[] data = (Object[]) object;
                ClientCode clientCode = new ClientCode();
                clientCode.setId(data[0].toString());
                clientCode.setCompanyName(data[1].toString());
                clientCode.setEmail(data[2].toString());
                clientCodes.add(clientCode);
            }
            return clientCodes;
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }
    public List<ClientCode> getCompanyByStaff(String staffName){
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaStaffMappingCompany = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(TeacherMappingCompany.class.getCanonicalName());
        TypeMetadata metaClientCode = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(ClientCode.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL","SELECT id, companyName, email FROM " + metaClientCode.getTable() + " WHERE companyName not IN (select company FROM " + metaStaffMappingCompany.getTable() + " WHERE StaffName='"+staffName+"' and isDeleted = false) and isDeleted = false");
        try {
            List<ClientCode> clientCodes = new ArrayList<>();
            List<Object> objects = (List<Object>) q.execute();
            for (Object object : objects) {
                Object[] data = (Object[]) object;
                ClientCode clientCode = new ClientCode();
                clientCode.setId(data[0].toString());
                clientCode.setCompanyName(data[1].toString());
                clientCode.setEmail(data[2].toString());
                clientCodes.add(clientCode);
            }
            return clientCodes;
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }
    public List<ClientCode> CompanyStaff(String staffName){
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaStaffMappingCompany = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(TeacherMappingCompany.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL","SELECT id, idCompany, company FROM " + metaStaffMappingCompany.getTable() + " WHERE StaffName='"+staffName+"' and isDeleted = false");
        try {
            List<ClientCode> clientCodes = new ArrayList<>();
            List<Object> objects = (List<Object>) q.execute();
            for (Object object : objects) {
                Object[] data = (Object[]) object;
                ClientCode clientCode = new ClientCode();
                clientCode.setId(data[1].toString());
                clientCode.setCompanyName(data[2].toString());
                clientCodes.add(clientCode);
            }
            return clientCodes;
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }
    public List<ClientCode> CompanyTeacher(String teacherName){
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaTeacherMappingCompany = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(TeacherMappingCompany.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL","SELECT id, idCompany, company FROM " + metaTeacherMappingCompany.getTable() + " WHERE teacherName='"+teacherName+"' and isDeleted = false");
        try {
            List<ClientCode> clientCodes = new ArrayList<>();
            List<Object> objects = (List<Object>) q.execute();
            for (Object object : objects) {
                Object[] data = (Object[]) object;
                ClientCode clientCode = new ClientCode();
                clientCode.setId(data[1].toString());
                clientCode.setCompanyName(data[2].toString());
                clientCodes.add(clientCode);
            }
            return clientCodes;
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }

}


