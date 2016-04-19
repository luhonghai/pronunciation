package com.cmg.merchant.dao.company;
import com.cmg.lesson.data.jdo.course.Course;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.ClientCode;
import com.cmg.vrc.util.PersistenceManagerHelper;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lantb on 2016-01-26.
 */
public class CPDAO extends DataAccess<ClientCode> {
    public CPDAO(){
        super(ClientCode.class);
    }

    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    public ClientCode getById(String id) throws Exception{
        boolean isExist = false;
        List<ClientCode> list = list("WHERE id == :1 && isDeleted == :2 ", id, false);
        if(list!=null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    /**
     *
     * @param start
     * @param length
     * @param company
     * @return
     * @throws Exception
     */
    public List<ClientCode> suggestionCompany(int start, int length,String company) throws Exception {
        PersistenceManager pm = PersistenceManagerHelper.get();
        Query q = pm.newQuery("SELECT FROM " + ClientCode.class.getCanonicalName());
        StringBuffer string=new StringBuffer();
        string.append("(companyName.toLowerCase().indexOf(company.toLowerCase()) != -1) &&");
        string.append("(isDeleted==false)");
        q.setFilter(string.toString());
        q.declareParameters("String company");
        q.setOrdering("companyName asc");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("company", company);
        q.setRange(0, 3);
        try {
            return detachCopyAllList(pm, q.executeWithMap(params));
        } catch (Exception e) {
            throw e;
        } finally {
            q.closeAll();
            pm.close();
        }
    }
}
