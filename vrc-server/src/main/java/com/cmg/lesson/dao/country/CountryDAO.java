package com.cmg.lesson.dao.country;

import com.cmg.lesson.data.jdo.country.Country;
import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

/**
 * Created by lantb on 2015-10-28.
 */
public class CountryDAO extends DataAccess<Country> {
    public CountryDAO(){super(Country.class);}


    /**
     *  use for get latest version in table
     * @return latest version
     * @throws Exception
     */
    public int getLatestVersion() throws Exception{
        int version = 0;
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT max(version) FROM " + Country.class.getCanonicalName());
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

}
