package com.cmg.vrc.data.dao.impl;

import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.NumberDate;

import java.util.List;

/**
 * Created by CMGT400 on 11/12/2015.
 */
public class NumberDateDAO extends DataAccess<NumberDate> {
    public NumberDateDAO() {
        super(NumberDate.class);
    }
    public NumberDate numberDate() throws Exception{
        List<NumberDate> userList = listAll();
        if (userList != null && userList.size() > 0)
            return userList.get(0);
        return new NumberDate();
    }
}
