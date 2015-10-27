package com.cmg.android.bbcaccent.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.data.dto.UserVoiceModel;
import com.cmg.android.bbcaccent.extra.SwitchFragmentParameter;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.google.gson.Gson;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by luhonghai on 13/10/2015.
 */

public class MainBroadcaster {

    public enum Filler {
        NULL(""),
        USER_VOICE_MODEL("user_voice_model"),
        HISTORY_ACTION("history_action"),
        WORD("word"),
        DATA_UPDATE("data_update"),
        FEEDBACK("feedback"),
        SEARCH_WORD("search_word"),
        SWITCH_FRAGMENT("switch_fragment"),
        POP_BACK_STACK_FRAGMENT("pop_back_stack_fragment")
        ;
        String name;
        Filler(String name) {
            this.name = name;
        }

        static Filler fromName(String name) {
            for (Filler key : values()) {
                if (key.name.equals(name)) return key;
            }
            return NULL;
        }

        @Override
        public String toString() {
            return name;
        }

        public enum Key {
            NULL(""),
            WORD("word"),
            TYPE("type"),
            DATA("data"),
            CLASS_NAME("class_name"),
            SWITCH_FRAGMENT_PARAMETER("switch_fragment_parameter"),
            DICTIONARY_ITEM("dictionary_item"),
            VIEW_STATE("view_state")
            ;
            String name;
            Key(String name) {this.name = name;}

            @Override
            public String toString() {
                return name;
            }

            static Key fromName(String name) {
                for (Key key : values()) {
                    if (key.name.equals(name)) return key;
                }
                return NULL;
            }
        }
    }

    public static abstract class ReceiverListener {

        public void onUserModelFetched(UserVoiceModel model) {}

        public void onHistoryAction(UserVoiceModel model, String word, int type) {}

        public void onDataUpdate(String data, int type) {}

        public void onSearchWord(String word) {}

        public void onReceiveMessage(Filler filler, Bundle bundle) {}
    }

    private static int currentId;

    private final Map<Integer, ReceiverListener> listeners = new ConcurrentHashMap<Integer, ReceiverListener>();

    private final Context context;

    private static final MainBroadcaster instance = new MainBroadcaster(MainApplication.getContext());

    public static MainBroadcaster getInstance() {
        return instance;
    }

    private final Sender sender;

    private final Gson gson = new Gson();

    private MainBroadcaster(Context context) {
        this.context = context;
        this.sender = new Sender();
    }

    public Sender getSender() {
        return sender;
    }

    public void init() {
        this.context.registerReceiver(mBroadcastReceiver, new IntentFilter(MainBroadcaster.class.getName()));
    }

    public void destroy() {
        try {
            this.context.unregisterReceiver(mBroadcastReceiver);
        } catch (Exception e) {SimpleAppLog.error("Could not unregister all receiver",e);}
    }

    public int register(ReceiverListener listener) {
        int id = currentId++;
        listeners.put(id, listener);
        SimpleAppLog.info("Register new listener id " + id + ". Current size: " + listeners.size());
        return id;
    }

    public void unregister(int id) {
        listeners.remove(id);
        SimpleAppLog.info("Unregister listener id " + id + ". Current size: " + listeners.size());
    }

    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            Filler filler = Filler.fromName(bundle.getString(Filler.class.getName()));
            for (ReceiverListener listener : listeners.values()) {
                listener.onReceiveMessage(filler, bundle);
            }
            int type;
            String data, word;
            UserVoiceModel model;
            switch (filler) {
                case USER_VOICE_MODEL:
                    data = bundle.getString(Filler.USER_VOICE_MODEL.toString());
                    model = null;
                    try {
                        model = gson.fromJson(data, UserVoiceModel.class);
                    } catch (Exception ex) {
                        SimpleAppLog.error("Could not fetch user voice model from server",ex);
                    }
                    for (ReceiverListener listener : listeners.values()) {
                        listener.onUserModelFetched(model);
                    }
                    break;
                case HISTORY_ACTION:
                    data = bundle.getString(Filler.USER_VOICE_MODEL.toString());
                    word = bundle.getString(Filler.Key.WORD.toString());
                    type = bundle.getInt(Filler.HISTORY_ACTION.toString());
                    Gson gson = new Gson();
                    model = gson.fromJson(data, UserVoiceModel.class);
                    if (model != null) {
                        for (ReceiverListener listener : listeners.values()) {
                            listener.onHistoryAction(model, word, type);
                        }
                    }
                    break;
                case DATA_UPDATE:
                    data = bundle.getString(Filler.Key.DATA.toString());
                    type = bundle.getInt(Filler.Key.TYPE.toString());
                    for (ReceiverListener listener : listeners.values()) {
                        listener.onDataUpdate(data, type);
                    }
                    break;
                case SEARCH_WORD:
                    word = bundle.getString(Filler.Key.WORD.toString());
                    for (ReceiverListener listener : listeners.values()) {
                        listener.onSearchWord(word);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public class Sender {

        public void sendSearchWord(String word) {
            Bundle bundle = new Bundle();
            bundle.putString(Filler.Key.WORD.toString(), word);
            sendMessage(Filler.SEARCH_WORD, bundle);
        }

        public void sendOnUserVoiceModelFound(String modelJson) {
            Bundle bundle = new Bundle();
            bundle.putString(Filler.USER_VOICE_MODEL.toString(), modelJson);
            sendMessage(Filler.USER_VOICE_MODEL, bundle);
        }

        public void sendHistoryAction(String modelJson, String word, int type) {
            Bundle bundle = new Bundle();
            bundle.putString(Filler.USER_VOICE_MODEL.toString(), modelJson);
            bundle.putInt(Filler.HISTORY_ACTION.toString(), type);
            bundle.putString(Filler.Key.WORD.toString(), word);
            sendMessage(Filler.HISTORY_ACTION, bundle);
        }

        public void sendUpdateData(String data, int type) {
            Bundle bundle = new Bundle();
            bundle.putString(Filler.Key.DATA.toString(), data);
            bundle.putInt(Filler.Key.TYPE.toString(), type);
            sendMessage(Filler.DATA_UPDATE, bundle);
        }

        public void sendSwitchFragment(Class<?> fragmentClass, SwitchFragmentParameter parameter, Bundle bundle) {
            if (bundle == null) bundle = new Bundle();
            Gson gson = new Gson();
            bundle.putString(Filler.Key.CLASS_NAME.toString(), fragmentClass.getName());
            bundle.putString(Filler.Key.SWITCH_FRAGMENT_PARAMETER.toString(), gson.toJson(parameter));
            sendMessage(Filler.SWITCH_FRAGMENT, bundle);
        }

        public void sendPopBackStackFragment() {
            sendMessage(Filler.POP_BACK_STACK_FRAGMENT, null);
        }

        public void sendMessage(Filler key, Bundle bundle) {
            Intent intent = new Intent(MainBroadcaster.class.getName());
            intent.putExtra(Filler.class.getName(), key.toString());
            if (bundle != null)
                intent.putExtras(bundle);
            SimpleAppLog.debug("Send broadcast message key " + key.toString());
            context.sendBroadcast(intent);
        }
    }
}
