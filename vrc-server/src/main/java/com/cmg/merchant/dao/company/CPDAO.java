package com.cmg.merchant.dao.company;
import com.cmg.vrc.data.dao.DataAccess;
import com.cmg.vrc.data.jdo.ClientCode;

import java.util.List;

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
}
