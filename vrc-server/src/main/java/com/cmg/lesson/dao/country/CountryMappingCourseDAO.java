package com.cmg.lesson.dao.country;

import com.cmg.lesson.data.jdo.country.Country;
import com.cmg.lesson.data.jdo.country.CountryMappingCourse;
import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.lesson.data.jdo.level.Level;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.metadata.TypeMetadata;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lantb on 2015-10-28.
 */
public class CountryMappingCourseDAO extends DataAccess<CountryMappingCourse>{
    public CountryMappingCourseDAO(){super(CountryMappingCourse.class);}

    /**
     *  use for get latest version in table
     * @return latest version
     * @throws Exception
     */
    public int getLatestVersion() throws Exception{
        int version = 0;
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT max(version) FROM " + CountryMappingCourse.class.getCanonicalName());
        try {
            if (q != null) {
                version = (int) q.execute();
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }
        return version;
    }

    /**
     *
     * @param id
     * @return true if update deleted success
     */
    public boolean updateDeleted(String idCountry){
        boolean check = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(CountryMappingCourse.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET isDeleted= ? WHERE idCountry=?");
        try {
            q.execute(true,idCountry);
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
     * @param idCountry,idCourse
     * @return true if update deleted success
     */
    public boolean updateDeleted(String idCountry, String idCourse){
        boolean check = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(CountryMappingCourse.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET isDeleted= ? WHERE idCountry=? and idCourse=?");
        try {
            q.execute(true,idCountry,idCourse);
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
     * @param idCourse
     * @return
     * @throws Exception
     */
    public boolean updateDeletedBaseOnIdCourse(String idCourse) throws Exception{
        boolean check = false;
        PersistenceManager pm = PersistenceManagerHelper.get();
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(CountryMappingCourse.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "UPDATE " + metaRecorderSentence.getTable() + " SET isDeleted= ? WHERE idCourse=?");
        try {
            q.execute(true,idCourse);
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
     * @return
     */
    public List<Course> listCourseAvailable(){
        List<Course> listCourse = new ArrayList<Course>();
        PersistenceManager pm = PersistenceManagerHelper.get();
        String filter = " Where id not in (Select idCourse from CountryMappingCourse)";
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(Course.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "Select id,name,description from " + metaRecorderSentence.getTable() + filter);
        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size() > 0){
                for(Object obj : tmp){
                    Course c = new Course();
                    Object[] array = (Object[]) obj;
                    c.setId(array[0].toString());
                    if(array[1]!=null){
                        c.setName(array[1].toString());
                    }
                    if(array[2] != null) {
                        c.setDescription(array[2].toString());
                    }
                    listCourse.add(c);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }
        return listCourse;
    }

    /**
     *
     * @param idCountry
     * @return
     * @throws Exception
     */
    public Course getByIdCountry(String idCountry) throws Exception{
        List<Course> listCourse = new ArrayList<Course>();
        PersistenceManager pm = PersistenceManagerHelper.get();
        String filter = " Where id in (Select idCourse from CountryMappingCourse where idCountry='"+idCountry+"')";
        TypeMetadata metaRecorderSentence = PersistenceManagerHelper.getDefaultPersistenceManagerFactory().getMetadata(Course.class.getCanonicalName());
        Query q = pm.newQuery("javax.jdo.query.SQL", "Select id,name,description from " + metaRecorderSentence.getTable() + filter);
        try {
            List<Object> tmp = (List<Object>) q.execute();
            if(tmp!=null && tmp.size() > 0){
                for(Object obj : tmp){
                    Course c = new Course();
                    Object[] array = (Object[]) obj;
                    c.setId(array[0].toString());
                    if(array[1]!=null){
                        c.setName(array[1].toString());
                    }
                    if(array[2] != null) {
                        c.setDescription(array[2].toString());
                    }
                    return c;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (q!= null)
                q.closeAll();
            pm.close();
        }
        return null;
    }
}
