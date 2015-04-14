package com.cmg.vrc.data.jdo;

import com.cmg.vrc.data.Mirrorable;

/**
 * Created by luhonghai on 4/13/15.
 */
public class AppDetail implements Mirrorable {

    private String id;

    private String noAccessMessage;

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
