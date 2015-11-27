package com.cmg.android.bbcaccent.fragment.tab;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.data.sqlite.freestyle.ScoreDBAdapter;
import com.cmg.android.bbcaccent.data.dto.PronunciationScore;
import com.cmg.android.bbcaccent.data.dto.UserProfile;
import com.cmg.android.bbcaccent.broadcast.MainBroadcaster;
import com.cmg.android.bbcaccent.fragment.Preferences;
import com.cmg.android.bbcaccent.utils.AndroidHelper;
import com.cmg.android.bbcaccent.utils.ColorHelper;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.cmg.android.bbcaccent.view.AlwaysMarqueeTextView;
import com.cmg.android.bbcaccent.view.cardview.CircleCardView;
import com.luhonghai.litedb.exception.LiteDatabaseException;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by cmg on 2/12/15.
 */
public class HistoryFragment extends FragmentTab {
    public static final int CLICK_LIST_ITEM = 0;
    public static final int CLICK_PLAY_BUTTON = 1;
    public static final int CLICK_RECORD_BUTTON = 2;

    @Bind(R.id.listHistory)
    ListView listView;

    private ScoreDBAdapter dbAdapter;

    private String word;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, v);
        Bundle bundle =  getArguments();
        boolean isLesson = bundle != null && bundle.containsKey(MainBroadcaster.Filler.LESSON.toString());
        if (bundle != null)
            word = bundle.getString(MainBroadcaster.Filler.Key.WORD.toString());
        if (isLesson) {
            dbAdapter = new ScoreDBAdapter(MainApplication.getContext().getLessonHistoryDatabaseHelper());
        } else {
            dbAdapter = new ScoreDBAdapter();
        }
        HistoryAdapter historyAdapter = new HistoryAdapter(getActivity(), word != null && word.length() > 0);
        listView.setAdapter(historyAdapter);
        isLoadedView = true;
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadScore();
    }

    @Override
    protected void onUpdateData(String data) {
        loadScore();
    }

    @Override
    protected void enableView(boolean enable) {
        if (listView == null) return;
        enableItemView(R.id.btnPlayItem, enable);
        enableItemView(R.id.btnRecordItem, enable);
        enableItemView(R.id.txtWordItem, enable);
        enableItemView(R.id.txtWordScore, enable);
    }

    private void enableItemView(int id, boolean enable) {
        View v = listView.findViewById(id);
        if (v != null)
            v.setEnabled(enable);
    }

    private void loadScore() {
        UserProfile profile = Preferences.getCurrentProfile(getActivity());
        if (profile == null) return;
        try {
            dbAdapter.open();
            Cursor cursor;
            if (word == null || word.length() == 0) {
                cursor = dbAdapter.getAll(profile.getUsername());
            } else {
                cursor = dbAdapter.getByWord(word, profile.getUsername());
            }
            ((CursorAdapter)listView.getAdapter()).changeCursor(cursor);
        } catch (Exception e) {
            SimpleAppLog.error("Could not open database",e);
        } finally {
            try {
                dbAdapter.close();
            } catch (Exception ex) {

            }
        }
    }

    private void sendAction(PronunciationScore score, int type) {
        try {
            sendShowcaseResetTimingRequest();
            String modelSource = score.getUserVoiceModel(getActivity());
            MainBroadcaster.getInstance().getSender().sendHistoryAction(modelSource, word, type);
        } catch (Exception e) {
            SimpleAppLog.error("Could not send history action",e);
        }
    }

    private class HistoryAdapter extends CursorAdapter implements View.OnClickListener {

        private boolean isDetail;

        private LayoutInflater inflater;

        private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.getDefault());

        public HistoryAdapter(Context context, boolean isDetail) {
            super(context,null, FLAG_REGISTER_CONTENT_OBSERVER);
            this.isDetail = isDetail;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
            return inflater.inflate(R.layout.fragment_history_list_item,
                    null);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            final PronunciationScore score;
            try {
                score = dbAdapter.toObject(cursor);
                float scoreVal = score.getScore();
                RelativeLayout rlHistoryItem;
                AlwaysMarqueeTextView txtWordItem;
                TextView txtWordScore;
                CircleCardView btnPlayItem;
                CircleCardView btnRecordItem;

                txtWordItem = (AlwaysMarqueeTextView) view.findViewById(R.id.txtWordItem);
                txtWordItem.setOnClickListener(this);
                txtWordScore = (TextView) view.findViewById(R.id.txtWordScore);
                txtWordScore.setOnClickListener(this);
                btnPlayItem = ButterKnife.findById(view, R.id.btnPlayItem);
                btnPlayItem.setOnClickListener(this);
                btnRecordItem = ButterKnife.findById(view, R.id.btnRecordItem);
                btnRecordItem.setOnClickListener(this);
                if (isDetail) {
                    btnPlayItem.setVisibility(View.GONE);
                    btnRecordItem.setVisibility(View.GONE);
                }
                rlHistoryItem  = (RelativeLayout) view.findViewById(R.id.rlHistoryItem);
                rlHistoryItem.setOnClickListener(this);

                txtWordItem.setTag(score);
                txtWordScore.setTag(score);
                btnPlayItem.setTag(score);
                btnRecordItem.setTag(score);
                rlHistoryItem.setTag(score);
                if (isDetail) {
                    txtWordItem.setText(simpleDateFormat.format(score.getTimestamp()));
                } else {
                    txtWordItem.setText(score.getWord());
                    txtWordItem.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                        @Override
                        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                            AndroidHelper.updateMarqueeTextView((TextView)v, !AndroidHelper.isCorrectWidth((TextView) v, score.getWord()));
                            v.setSelected(true);
                        }
                    });

                }
                txtWordScore.setText(
                        String.format(Locale.getDefault(), "%d%%", Math.round(scoreVal)));
                int color;
                if (scoreVal >= 80.0f) {
                    color = ColorHelper.getColor(R.color.app_green);
                } else if (scoreVal >= 45.0f) {
                    color = ColorHelper.getColor(R.color.app_orange);
                } else {
                    color = ColorHelper.getColor(R.color.app_red);
                }
                txtWordItem.setTextColor(color);
                txtWordScore.setTextColor(color);
                btnPlayItem.setCardBackgroundColor(color);
                btnRecordItem.setCardBackgroundColor(color);
            } catch (LiteDatabaseException e) {
                SimpleAppLog.error("Could not cast cursor to object",e);
            }
        }

        @Override
        public void onClick(View v) {
            PronunciationScore score = (PronunciationScore) v.getTag();
            switch (v.getId()) {
                case R.id.btnPlayItem:
                    sendAction(score, CLICK_PLAY_BUTTON);
                    break;
                case R.id.btnRecordItem:
                    sendAction(score, CLICK_RECORD_BUTTON);
                    break;
                case R.id.rlHistoryItem:
                    sendAction(score, CLICK_LIST_ITEM);
                    break;
            }
        }
    }
}
