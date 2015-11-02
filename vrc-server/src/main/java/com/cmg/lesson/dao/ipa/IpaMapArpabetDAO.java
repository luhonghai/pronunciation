package com.cmg.lesson.dao.ipa;
import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.lesson.data.jdo.ipa.IpaMapArpabet;
import com.cmg.lesson.data.jdo.level.Level;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.Transcription;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.metadata.TypeMetadata;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lantb on 2015-10-26.
 */
public class IpaMapArpabetDAO extends DataAccess<IpaMapArpabet> {

    public IpaMapArpabetDAO(){super(IpaMapArpabet.class);}

    /**
     *
     * @param arpabet
     * @return
     * @throws Exception
     */
    public String getByArpabet(String arpabet) throws Exception{
        List<IpaMapArpabet> temp = list("WHERE arpabet == :1 && isDeleted == :2",arpabet,false);
        if(temp!=null && temp.size() > 0){
            return temp.get(0).getIpa();
        }
        return null;
    }


    /**
     *
     * @param id
     * @return true if update deleted success
     */
    public boolean updateDeleted(String id){
        boolean check = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(IpaMapArpabet.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET isDeleted= ? WHERE id=?");
        try {
            q.execute(true,id);
            check=true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }
        return check;
    }

    /**
     *
     * @param id
     * @param arphabet
     * @param description
     * @param color
     * @param tips
     * @param type
     * @param indexing
     * @return
     * @throws Exception
     */
    public boolean updateMap(String id, String arphabet, String description, String color, String tips, String type, int indexing, String words) throws Exception{
        boolean check = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(IpaMapArpabet.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() +
                " SET description= ? , tip=?,color=? ," +
                "arpabet='"+arphabet+"', type='"+type+"' , indexingType=" +indexing +
                " ,words='"+words+"' WHERE id='"+id+"'");
        try {
            q.execute(description,tips,color);
            check=true;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (q!= null)
                q.closeAll();
                pm.close();
        }
        return check;
    }

    /**
     *
     * @return total rows in table
     */
    public double getCount() throws  Exception{
        PersistenceManager pm = PersistenceManagerHelper.get();
        Long count;
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + IpaMapArpabet.class.getCanonicalName());
        q.setFilter("isDeleted=false");
        try {
            count = (Long) q.execute();
            return count.doubleValue();
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }
    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    public IpaMapArpabet getById(String id)throws Exception{
        List<IpaMapArpabet> list = list(" WHERE id==:1 && isDeleted==:2", id, false);
        if(list!=null && list.size()>0){
            return list.get(0);
        }
        return null;
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
     * @return
     * @throws Exception
     */
    public List<IpaMapArpabet> listAll(int start, int length,String search,int column,String order, Date createDateFrom,Date createDateTo) throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT FROM " + IpaMapArpabet.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a="(ipa.toLowerCase().indexOf(ipa.toLowerCase()) != -1)";
        String b="(ipa == null || ipa.toLowerCase().indexOf(search.toLowerCase()) != -1)";
        if(createDateFrom!=null&&createDateTo==null){
            string.append("(dateCreated >= createDateFrom) &&");
        }
        if(createDateFrom==null&&createDateTo!=null){
            string.append("(dateCreated <= createDateTo) &&");
        }
        if(createDateFrom!=null&&createDateTo!=null){
            string.append("(dateCreated >= createDateFrom && dateCreated <= createDateTo) &&");
        }

        string.append("(isDeleted==false) &&");

        if(search.length()>0){
            string.append(a);
        }
        if(search.length()==0){
            string.append(b);
        }
        q.setFilter(string.toString());
        q.declareParameters("String search, java.util.Date createDateFrom,java.util.Date createDateTo");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
        params.put("createDateFrom", createDateFrom);
        params.put("createDateTo", createDateTo);

        if (column==0 && order.equals("asc")) {
            q.setOrdering("type asc,indexingType asc");
        }else if(column==0 && order.equals("desc")) {
            q.setOrdering("type desc,indexingType asc");
        }
        if (column==2 && order.equals("asc")) {
            q.setOrdering("dateCreated asc");
        }else if(column==2 && order.equals("desc")) {
            q.setOrdering("dateCreated desc");
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

    /**
     *
     * @param search
     * @param createDateFrom
     * @param createDateTo
     * @return
     * @throws Exception
     */
    public double getCountSearch(String search,Date createDateFrom,Date createDateTo) throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Long count;
        Query q = pm.newQuery("SELECT COUNT(id) FROM " + IpaMapArpabet.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        String a="(ipa.toLowerCase().indexOf(search.toLowerCase()) != -1)";
        String b="(ipa == null || ipa.toLowerCase().indexOf(search.toLowerCase()) != -1)";

        if(createDateFrom!=null&&createDateTo==null){
            string.append("(dateCreated >= createDateFrom) &&");
        }
        if(createDateFrom==null&&createDateTo!=null){
            string.append("(dateCreated <= createDateTo) &&");
        }

        if(createDateFrom!=null&&createDateTo!=null){
            string.append("(dateCreated >= createDateFrom && dateCreated <= createDateTo) &&");
        }

        string.append("(isDeleted==false) &&");

        if(search.length()>0){
            string.append(a);
        }
        if(search.length()==0){
            string.append(b);
        }
        q.setFilter(string.toString());
        q.declareParameters("String search, java.util.Date createDateFrom,java.util.Date createDateTo");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("search", search);
        params.put("createDateFrom", createDateFrom);
        params.put("createDateTo", createDateTo);
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
