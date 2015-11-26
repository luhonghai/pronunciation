package com.cmg.android.bbcaccent.fragment.tab;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.broadcast.MainBroadcaster;
import com.cmg.android.bbcaccent.data.TipsContainer;
import com.cmg.android.bbcaccent.data.dto.UserVoiceModel;
import com.cmg.android.bbcaccent.utils.RandomHelper;
import com.cmg.android.bbcaccent.view.cardview.CircleCardView;
import com.google.gson.Gson;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by cmg on 2/12/15.
 */

public class TipFragment extends FragmentTab {

    private TipsContainer tipsContainer;

    private static final long MAX_UPDATE_TIMEOUT = 30000;

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

    private boolean isLoadedTip = false;

    private TipsContainer.PronunciationTip currentTip;

    private int currentTipIndex = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tip, container, false);
        ButterKnife.bind(this, v);
        tipsContainer = new TipsContainer(getActivity());
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
                model.setWord(currentTip.getWords().get(currentTipIndex));
                MainBroadcaster.getInstance().getSender().sendHistoryAction(gson.toJson(model),
                        null,
                        HistoryFragment.CLICK_RECORD_BUTTON);
                break;
        }
    }


    private Runnable mUpdateTipRunnable = new Runnable() {
        @Override
        public void run() {
            if (tipsContainer != null) {
                displayTip(tipsContainer.getTip(userVoiceModel));
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

    private void displayTip(TipsContainer.PronunciationTip tip) {
        if (tip != null) {
            currentTip = tip;
            txtText.setHtmlFromString(tip.getData(), true);
            List<String> words = currentTip.getWords();
            if (words != null && words.size() > 0) {
                currentTipIndex = RandomHelper.getRandomIndex(words.size());
                btnRecord.setVisibility(View.VISIBLE);
                displayWord();
            }
            isLoadedTip = true;
        } else {
            txtText.setText(getString(R.string.no_tip_found));
            txtWord.setText(getString(R.string.tap_to_reload));
            txtWord.setVisibility(View.VISIBLE);
            isLoadedTip = false;
        }
    }

    private void displayWord() {
        if (currentTip != null) {
            List<String> words = currentTip.getWords();
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
        if (tipsContainer == null)
            tipsContainer = new TipsContainer(getActivity());
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
