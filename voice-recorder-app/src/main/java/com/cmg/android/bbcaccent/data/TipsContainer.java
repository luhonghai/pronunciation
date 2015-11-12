package com.cmg.android.bbcaccent.data;

import android.content.Context;
import android.os.AsyncTask;

import com.cmg.android.bbcaccent.data.dto.SphinxResult;
import com.cmg.android.bbcaccent.data.dto.UserVoiceModel;
import com.cmg.android.bbcaccent.utils.FileHelper;
import com.cmg.android.bbcaccent.utils.RandomHelper;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.google.gson.Gson;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cmg on 2/13/15.
 */
public class TipsContainer {

    public static class PronunciationTip {

        public static final String TYPE_TEXT = "text";

        public static final String TYPE_VIDEO = "video";

        public static final String TYPE_IMAGE = "image";

        private String type;

        private String data;

        private String phoneme;

        private List<String> words;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getPhoneme() {
            return phoneme;
        }

        public void setPhoneme(String phoneme) {
            this.phoneme = phoneme;
        }

        public List<String> getWords() {
            return words;
        }

        public void setWords(List<String> words) {
            this.words = words;
        }
    }

    private static final int TIMEOUT = 5000;

    private final Context context;

    private PronunciationTip[] tips;

    private Map<String, List<Integer>> tipIndex;

    private boolean isLoaded;

    public TipsContainer(Context context) {
        this.context = context;
    }

    public void loadSync() {
        if (tips == null)
            tips = new PronunciationTip[0];
            File tipFile = FileHelper.getSavedTipFile(context);
            try {
                if (tipFile.exists()) {
                    FileUtils.forceDelete(tipFile);
                }
                FileUtils.copyInputStreamToFile(context.getAssets().open("tips.json"), tipFile);
            } catch (Exception ex) {
                SimpleAppLog.error("Could not copy tips.json",ex);
            }
            if (tipFile.exists()) {
                Gson gson = new Gson();
                try {
                    String source = FileUtils.readFileToString(tipFile, "UTF-8");
                    if (source != null && source.length() > 0)
                        tips = gson.fromJson(source, PronunciationTip[].class);
                } catch (Exception ex) {
                   SimpleAppLog.error("Could not read tips.json",ex);
                }
            }
            tipIndex = new HashMap<String, List<Integer>>();
            if (tips != null && tips.length > 0) {
                for (int i = 0; i < tips.length; i++) {
                    final PronunciationTip tip = tips[i];
                    String phoneme = tip.getPhoneme().toLowerCase();
                    if (tipIndex.containsKey(phoneme)) {
                        tipIndex.get(phoneme).add(i);
                    } else {
                        List<Integer> index = new ArrayList<Integer>();
                        index.add(i);
                        tipIndex.put(phoneme, index);
                    }
                }
            }
            isLoaded = true;
    }
    @Deprecated
    public void load() {
        isLoaded = false;
        AsyncTask<Void,Void,Void> asyncTask = new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... params) {
                loadSync();
                return null;
            }
            };
        asyncTask.execute();
    }

    @Deprecated
    public boolean isLoaded() {
        return isLoaded;
    }

    public PronunciationTip getTip(UserVoiceModel model) {
        loadSync();
        if (tips != null && tips.length > 0) {
            if (model != null) {
                SphinxResult result = model.getResult();
                int index = RandomHelper.getRandomIndex(tips.length);
                float lastScore = 1703.89f;
                if (result != null) {
                    List<SphinxResult.PhonemeScore> scores = result.getPhonemeScores();
                    if (scores != null && scores.size() > 0) {
                        for (SphinxResult.PhonemeScore score : scores) {
                            String phoneme = score.getName().toLowerCase();
                            if (score.getTotalScore() < lastScore && tipIndex.containsKey(phoneme)) {
                                List<Integer> indexT = tipIndex.get(phoneme);
                                index = indexT.get(RandomHelper.getRandomIndex(indexT.size()));
                                lastScore = score.getTotalScore();
                            }
                        }
                    }
                }
                return tips[index];
            } else {
                return tips[RandomHelper.getRandomIndex(tips.length)];
            }
        }
        return null;
    }
}
