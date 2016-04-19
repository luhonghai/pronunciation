package com.cmg.merchant.dao.teacher;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.Admin;
import com.cmg.vrc.data.jdo.ClientCode;

import java.util.List;

/**
 * Created by lantb on 2016-01-26.
 */
public class TDAO extends DataAccess<Admin> {
    public TDAO(){
        super(Admin.class);
    }

    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    public Admin getById(String id) throws Exception{
        boolean isExist = false;
        List<Admin> list = list("WHERE id == :1 && isDeleted == :2 ", id, false);
        if(list!=null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }
}
