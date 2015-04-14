package com.cmg.android.bbcaccent.activity.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.activity.BaseActivity;
import com.cmg.android.bbcaccent.data.ScoreDBAdapter;
import com.cmg.android.bbcaccent.utils.ColorHelper;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;

/**
 * Created by cmg on 2/12/15.
 */
public class HistoryFragment extends FragmentTab {
    public static final int CLICK_LIST_ITEM = 0;
    public static final int CLICK_PLAY_BUTTON = 1;
    public static final int CLICK_RECORD_BUTTON = 2;
    public static final String ON_HISTORY_LIST_CLICK = "com.cmg.android.bbcaccent.activity.fragment.HistoryFragment.ListView.click";

    private static class ViewHolder {
        private TextView txtWordItem;
        private TextView txtWordScore;
        private ImageButton btnPlayItem;
        private ImageButton btnRecordItem;
    }

    private class HistoryAdapter extends ArrayAdapter<ScoreDBAdapter.PronunciationScore> implements View.OnClickListener {

        private Context context;
        private ScoreDBAdapter.PronunciationScore[] scores;

        private ViewHolder view;
        private boolean isDetail;

        private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");

        public HistoryAdapter(Context context, ScoreDBAdapter.PronunciationScore[] objects, boolean isDetail) {
            super(context, R.layout.fragment_history_list_item, objects);
            this.context = context;
            this.scores = objects;
            this.isDetail = isDetail;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflator = LayoutInflater.from(context);
            ScoreDBAdapter.PronunciationScore score = scores[position];
            float scoreVal = score.getScore();
            if (convertView == null) {
                view = new ViewHolder();
                convertView = inflator.inflate(R.layout.fragment_history_list_item,
                        null);
                view.txtWordItem = (TextView) convertView.findViewById(R.id.txtWordItem);
                view.txtWordItem.setOnClickListener(this);
                view.txtWordScore = (TextView) convertView.findViewById(R.id.txtWordScore);
                view.txtWordScore.setOnClickListener(this);
                view.btnPlayItem = (ImageButton) convertView.findViewById(R.id.btnPlayItem);
                view.btnPlayItem.setOnClickListener(this);
                view.btnRecordItem = (ImageButton) convertView.findViewById(R.id.btnRecordItem);
                view.btnRecordItem.setOnClickListener(this);
                if (isDetail) {
                    view.btnPlayItem.setVisibility(View.GONE);
                    view.btnRecordItem.setVisibility(View.GONE);
                }
                convertView.setTag(view);
            } else {
                view = (ViewHolder) convertView.getTag();
            }
            view.txtWordItem.setTag(score);
            view.txtWordScore.setTag(score);
            view.btnPlayItem.setTag(score);
            view.btnRecordItem.setTag(score);
            if (isDetail) {
                view.txtWordItem.setText(simpleDateFormat.format(score.getTimestamp()));
            } else {
                view.txtWordItem.setText(score.getWord());
            }
            view.txtWordScore.setText(Integer.toString(Math.round(scoreVal)) + "%");

            if (scoreVal >= 80.0f) {
                view.txtWordItem.setTextColor(ColorHelper.COLOR_GREEN);
                view.txtWordScore.setTextColor(ColorHelper.COLOR_GREEN);
                view.btnPlayItem.setImageResource(R.drawable.p_audio_green);
                view.btnRecordItem.setImageResource(R.drawable.p_record_green);
            } else if (scoreVal >= 45.0f) {
                view.txtWordItem.setTextColor(ColorHelper.COLOR_ORANGE);
                view.txtWordScore.setTextColor(ColorHelper.COLOR_ORANGE);
                view.btnPlayItem.setImageResource(R.drawable.p_audio_orange);
                view.btnRecordItem.setImageResource(R.drawable.p_record_orange);
            } else {
                view.txtWordItem.setTextColor(ColorHelper.COLOR_RED);
                view.txtWordScore.setTextColor(ColorHelper.COLOR_RED);
                view.btnPlayItem.setImageResource(R.drawable.p_audio_red);
                view.btnRecordItem.setImageResource(R.drawable.p_record_red);
            }

            return convertView;
        }

        @Override
        public void onClick(View v) {
            ScoreDBAdapter.PronunciationScore score = (ScoreDBAdapter.PronunciationScore) v.getTag();
            switch (v.getId()) {
                case R.id.btnPlayItem:
                    try {
                        sendAction(score, CLICK_PLAY_BUTTON);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.btnRecordItem:
                    try {
                        sendAction(score, CLICK_RECORD_BUTTON);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case R.id.txtWordItem:
                case R.id.txtWordScore:
                    try {
                        sendAction(score, CLICK_LIST_ITEM);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    private HistoryAdapter historyAdapter;

    private ListView listView;

    private ScoreDBAdapter dbAdapter;

    private String word;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_history, container, false);
        listView = (ListView) v.findViewById(R.id.listHistory);
        isLoadedView = true;
        dbAdapter = new ScoreDBAdapter(getActivity());
        Bundle bundle =  getArguments();
        if (bundle != null)
            word = bundle.getString(ARG_WORD);
        loadScore();

        return v;
    }

    @Override
    protected void onUpdateData(String data) {
        loadScore();
    }

    @Override
    protected void enableView(boolean enable) {
        if (listView == null || historyAdapter == null || historyAdapter.isEmpty()) return;
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
        Collection<ScoreDBAdapter.PronunciationScore> scores = null;
        boolean isDetail = false;
        try {
            dbAdapter.open();
            if (word == null || word.length() == 0) {
                scores = dbAdapter.toCollection(dbAdapter.getAll());
            } else {
                isDetail = true;
                scores = dbAdapter.toCollection(dbAdapter.getByWord(word));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            try {
                dbAdapter.close();
            } catch (Exception ex) {

            }
        }
        if (scores != null && scores.size() > 0) {
            ScoreDBAdapter.PronunciationScore[]
                    listScores = new ScoreDBAdapter.PronunciationScore[scores.size()];
            scores.toArray(listScores);
            historyAdapter = new HistoryAdapter(getActivity(), listScores, isDetail);
            listView.setAdapter(historyAdapter);
            historyAdapter.notifyDataSetChanged();
        }
    }

    private void sendAction(ScoreDBAdapter.PronunciationScore score, int type) throws IOException {
        String modelSource = score.getUserVoiceModel(getActivity());
        Intent intent = new Intent(ON_HISTORY_LIST_CLICK);
        intent.putExtra(BaseActivity.USER_VOICE_MODEL, modelSource);
        intent.putExtra(FragmentTab.ARG_WORD, word);
        intent.putExtra(ACTION_TYPE, type);
        Log.i("HistoryAction", "Type: " + type + ". Send action: " + modelSource );
        getActivity().sendBroadcast(intent);
    }
}