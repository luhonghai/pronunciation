package com.cmg.vrc.data.jdo;

import com.cmg.vrc.data.Mirrorable;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

/**
 * Created by luhonghai on 4/13/15.
 */
@PersistenceCapable(table = "app_detail")
public class AppDetailJDO implements Mirrorable {

    @PrimaryKey
    private String id;

    @Persistent
    @Column(jdbcType="VARCHAR", length=Integer.MAX_VALUE)
    private String noAccessMessage;

    @Persistent
    private boolean registration = true;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    public String getNoAccessMessage() {
        return noAccessMessage;
    }

    public void setNoAccessMessage(String noAccessMessage) {
        this.noAccessMessage = noAccessMessage;
    }

    public boolean isRegistration() {
        return registration;
    }

    public void setRegistration(boolean registration) {
        this.registration = registration;
    }
}
