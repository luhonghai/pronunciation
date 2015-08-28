package com.cmg.vrc.data.jdo;

import com.cmg.vrc.data.Mirrorable;

/**
 * Created by CMGT400 on 8/25/2015.
 */
public class LicenseCodeCompany implements Mirrorable {
    private String id;

    private String company;

    private String code;

    private boolean isDeleted;

    @Override
    public String getId() {
        return id;
    }
    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }


}
