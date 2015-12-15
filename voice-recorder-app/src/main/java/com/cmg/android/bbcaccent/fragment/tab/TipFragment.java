package com.cmg.android.bbcaccent.fragment.tab;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.broadcast.MainBroadcaster;
import com.cmg.android.bbcaccent.data.dto.UserVoiceModel;
import com.cmg.android.bbcaccent.data.dto.lesson.word.IPAMapArpabet;
import com.cmg.android.bbcaccent.data.sqlite.lesson.LessonDBAdapterService;
import com.cmg.android.bbcaccent.helper.PlayerHelper;
import com.cmg.android.bbcaccent.utils.FileHelper;
import com.cmg.android.bbcaccent.utils.RandomHelper;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.cmg.android.bbcaccent.view.cardview.CircleCardView;
import com.google.gson.Gson;
import com.luhonghai.litedb.exception.LiteDatabaseException;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.io.File;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by cmg on 2/12/15.
 */

public class TipFragment extends FragmentTab {


    private Handler mUpdateTipHandler = new Handler();

    private UserVoiceModel userVoiceModel;

    private long startTime;

    @Bind(R.id.btnPrevTip)
    ImageButton btnPrev;

    @Bind(R.id.btnNextTip)
    ImageButton btnNext;

    @Bind(R.id.btnRecordTip)
    CircleCardView btnRecord;

    @Bind(R.id.txtWordTip)
    TextView txtWord;

    @Bind(R.id.txtTextTip)
    HtmlTextView txtText;

    @Bind(R.id.imgTip)
    ImageView imgTip;

    @Bind(R.id.txtPhoneme)
    TextView txtPhoneme;

    @Bind(R.id.txtDefinition)
    TextView txtDefinition;

    @Bind(R.id.btnAudio)
    CircleCardView btnAudio;

    private boolean isLoadedTip = false;

    private IPAMapArpabet currentTip;

    private int currentTipIndex = 0;

    private DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).build();

    private PlayerHelper player;

    @OnClick(R.id.btnAudio)
    public void clickAudio() {
        if (currentTip != null) {
            playUrl(currentTip.getMp3Url());
        }
    }

    private void play(File file) {
        if (!file.exists()) return;
        try {
            if (player != null) {
                try {
                    player.stop();
                } catch (Exception ex) {

                }
            }
            player = new PlayerHelper(file, new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                    try {
                        player.stop();
                    } catch (Exception ex) {

                    }
                }

            });
            player.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void playUrl(final String url) {
        if (url == null || url.length() == 0) return;
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                File file = new File(FileHelper.getCachedFilePath(url));
                if (file.exists()) {
                    play(file);
                } else {
                    SimpleAppLog.error("Could not found media to play " + url);
                }
                return null;
            }
        }.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tip, container, false);
        ButterKnife.bind(this, v);
        isLoadedView = true;
        Bundle bundle = getArguments();
        updateTip(bundle == null ? null : bundle.getString(MainBroadcaster.Filler.USER_VOICE_MODEL.toString()));
        return v;
    }

    @OnClick({R.id.btnPrevTip, R.id.btnNextTip, R.id.txtWordTip, R.id.btnRecordTip})
    public void onClick(View v) {
        sendShowcaseResetTimingRequest();
        switch (v.getId()) {
            case R.id.btnPrevTip:
                currentTipIndex--;
                displayWord();
                break;
            case R.id.btnNextTip:
                currentTipIndex++;
                displayWord();
                break;
            case R.id.txtWordTip:
                if (!isLoadedTip) {
                    updateModelTip(userVoiceModel);
                }
                break;
            case R.id.btnRecordTip:
                Gson gson = new Gson();
                UserVoiceModel model = new UserVoiceModel();
                model.setWord(currentTip.getWordList().get(currentTipIndex));
                MainBroadcaster.getInstance().getSender().sendHistoryAction(gson.toJson(model),
                        null,
                        HistoryFragment.CLICK_RECORD_BUTTON);
                break;
        }
    }


    private Runnable mUpdateTipRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                IPAMapArpabet ipaMapArpabet = LessonDBAdapterService.getInstance().getIPAArpabetTip(userVoiceModel);
                displayTip(ipaMapArpabet);
            } catch (LiteDatabaseException e) {
                SimpleAppLog.error("Could not load phoneme tip",e);
            }
        }
    };

    private void displayLoading() {
        txtText.setText(getString(R.string.loading));
        txtWord.setVisibility(View.INVISIBLE);
        btnPrev.setVisibility(View.INVISIBLE);
        btnNext.setVisibility(View.INVISIBLE);
        btnRecord.setVisibility(View.INVISIBLE);
    }

    private void displayTip(IPAMapArpabet ipaMapArpabet) {
        if (ipaMapArpabet != null) {
            currentTip = ipaMapArpabet;
            String tongueImage = ipaMapArpabet.getImgTongue();
            if (tongueImage != null && tongueImage.length() > 0) {
                imgTip.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(tongueImage, imgTip, displayImageOptions);
            } else {
                imgTip.setVisibility(View.GONE);
            }
            txtText.setHtmlFromString(ipaMapArpabet.getTip(), true);
            txtPhoneme.setText(ipaMapArpabet.getIpa());
            txtDefinition.setText( String.format(Locale.getDefault(), "<%s> %s",
                    ipaMapArpabet.getArpabet(),
                    ipaMapArpabet.getWords()));
            List<String> words = currentTip.getWordList();
            if (words != null && words.size() > 0) {
                currentTipIndex = RandomHelper.getRandomIndex(words.size());
                btnRecord.setVisibility(View.VISIBLE);
                displayWord();
            }
            isLoadedTip = true;
            btnAudio.setVisibility(View.VISIBLE);
        } else {
            txtText.setText(getString(R.string.no_tip_found));
            txtWord.setText(getString(R.string.tap_to_reload));
            txtDefinition.setText("");
            txtPhoneme.setText("");
            btnAudio.setVisibility(View.GONE);
            txtWord.setVisibility(View.VISIBLE);
            isLoadedTip = false;
        }
    }

    private void displayWord() {
        if (currentTip != null) {
            List<String> words = currentTip.getWordList();
            if (words != null && words.size() > 0) {
                if (words.size() == 1) {
                    btnPrev.setVisibility(View.INVISIBLE);
                    btnNext.setVisibility(View.INVISIBLE);
                } else if (currentTipIndex == 0) {
                    btnPrev.setVisibility(View.INVISIBLE);
                    btnNext.setVisibility(View.VISIBLE);
                } else if (currentTipIndex == words.size() - 1) {
                    btnPrev.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.INVISIBLE);
                } else {
                    btnPrev.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.VISIBLE);
                }
                txtWord.setText(words.get(currentTipIndex));
                txtWord.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDestroy() {
        try {
            mUpdateTipHandler.removeCallbacks(mUpdateTipRunnable);
        } catch (Exception ex ) {

        }
        super.onDestroy();
    }

    private void updateModelTip(UserVoiceModel model) {
        currentTip = null;
        isLoadedTip = false;
        displayLoading();
        userVoiceModel = model;
        startTime = System.currentTimeMillis();
        mUpdateTipHandler.post(mUpdateTipRunnable);
    }

    private void updateTip(String rawModel) {
        if (rawModel != null && rawModel.length() > 0) {
            Gson gson = new Gson();
            UserVoiceModel model = gson.fromJson(rawModel, UserVoiceModel.class);
            updateModelTip(model);
        } else {
            updateModelTip(null);
        }
    }

    @Override
    protected void onUpdateData(String data) {
        updateTip(data);
    }

    @Override
    protected void enableView(boolean enable) {
        txtText.setEnabled(enable);
        btnNext.setEnabled(enable);
        btnPrev.setEnabled(enable);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
