package com.cmg.android.bbcaccent.auth;

import android.content.Context;
import android.os.AsyncTask;

import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.data.UserProfile;
import com.cmg.android.bbcaccent.http.HttpContacter;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by luhonghai on 4/13/15.
 */
public class AccountManager {

    public static interface AuthListener {

        public void onError(String message, Throwable e);

        public void onSuccess();
    }

    private GoogleApiClient mGoogleApiClient;

    private final Context context;

    public AccountManager(Context context) {
        this.context = context;
        init();
    }

    private void init() {
        FacebookSdk.sdkInitialize(context.getApplicationContext());
        mGoogleApiClient = new GoogleApiClient.Builder(context).addApi(Plus.API, Plus.PlusOptions.builder().build()).addScope(Plus.SCOPE_PLUS_LOGIN).build();
    }

    public void start()  {
        mGoogleApiClient.connect();
    }

    public void stop() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public void auth(final UserProfile profile, final AuthListener authListener) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Map<String, String> data = new HashMap<String, String>();
                Gson gson = new Gson();
                data.put("profile", gson.toJson(profile));
                try {
                    HttpContacter contacter = new HttpContacter(context);
                    String message = contacter.post(data, context.getResources().getString(R.string.auth_url));
                    if (message.equalsIgnoreCase("success")) {
                        authListener.onSuccess();
                    } else {
                        if (message.toLowerCase().contains("<html>")) {
                            authListener.onError("Could not connect to server. Please contact support@accenteasy.com", null);
                        } else {
                            authListener.onError(message, null);
                        }
                    }
                } catch (Exception e) {
                    authListener.onError("Could not connect to server. Please contact support@accenteasy.com", e);
                }
                return null;
            }
        }.execute();
    }

    public void logout() {
        LoginManager.getInstance().logOut();
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        }
    }
}
