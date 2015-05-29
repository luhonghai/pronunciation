package com.cmg.android.bbcaccent.activity.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.data.SphinxResult;


/**
 * Created by luhonghai on 3/17/15.
 */
public class PhoneScoreItemAdapter extends ArrayAdapter<SphinxResult.PhonemeScoreUnit> {

    private static class ViewHolder {
        private TextView txtPhonemeScore;
    }

    private ViewHolder view;

    private final SphinxResult.PhonemeScoreUnit[] scores;


    public PhoneScoreItemAdapter(Context context, SphinxResult.PhonemeScoreUnit[] objects) {
        super(context, R.layout.list_phome_score_item, objects);
        this.scores = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflator = LayoutInflater.from(getContext());
        SphinxResult.PhonemeScoreUnit score = scores[position];

        float scoreVal = calculateScore(position);
        if (convertView == null) {
            view = new ViewHolder();
            convertView = inflator.inflate(R.layout.list_phome_score_item,
                    null);
            view.txtPhonemeScore = (TextView) convertView.findViewById(R.id.txtPhonemeScoreItem);
            convertView.setTag(view);
        } else {
            view = (ViewHolder) convertView.getTag();
        }

        view.txtPhonemeScore.setTag(score);

        view.txtPhonemeScore.setText(Math.round(scoreVal) + "% like " + score.getName());

        if (score.getType() == SphinxResult.PhonemeScoreUnit.MATCHED || score.getType() == SphinxResult.PhonemeScoreUnit.BEEP_PHONEME) {
            view.txtPhonemeScore.setTextColor(getContext().getResources().getColor(R.color.app_green));
        } else if (score.getType() == SphinxResult.PhonemeScoreUnit.NEIGHBOR) {
            view.txtPhonemeScore.setTextColor(getContext().getResources().getColor(R.color.app_orange));
        } else {
            view.txtPhonemeScore.setTextColor(getContext().getResources().getColor(R.color.app_red));
        }
        return convertView;
    }

    private float calculateScore(int pos) {
        int tokenCount = 0;
        for (SphinxResult.PhonemeScoreUnit scoreUnit : scores) {
            tokenCount += scoreUnit.getCount();
        }
        SphinxResult.PhonemeScoreUnit score = scores[pos];

        return tokenCount == 0 ? 0.0f : ((float) score.getCount() / tokenCount) * 100;
    }

}
