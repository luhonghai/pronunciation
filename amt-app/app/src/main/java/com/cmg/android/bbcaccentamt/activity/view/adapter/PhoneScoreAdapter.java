package com.cmg.android.bbcaccentamt.activity.view.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cmg.android.bbcaccentamt.R;
import com.cmg.android.bbcaccentamt.data.SphinxResult;


/**
 * Created by luhonghai on 3/17/15.
 */
public class PhoneScoreAdapter extends ArrayAdapter<SphinxResult.PhonemeScore> {

    private static class ViewHolder {
        private TextView txtPhonemeScore;
    }

    private ViewHolder view;

    private final SphinxResult.PhonemeScore[] scores;

    private final View.OnClickListener onPhonemeClickListener;

    public PhoneScoreAdapter(Context context, SphinxResult.PhonemeScore[] objects, View.OnClickListener onPhonemeClickListener) {
        super(context, R.layout.list_phome_item, objects);
        this.scores = objects;
        this.onPhonemeClickListener = onPhonemeClickListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflator = LayoutInflater.from(getContext());
        SphinxResult.PhonemeScore score = scores[position];
        float scoreVal = score.getTotalScore();
        if (convertView == null) {
            view = new ViewHolder();
            convertView = inflator.inflate(R.layout.list_phome_item,
                    null);

            view.txtPhonemeScore = (TextView) convertView.findViewById(R.id.txtPhonemeScore);
            view.txtPhonemeScore.setOnClickListener(onPhonemeClickListener);
            convertView.setTag(view);
        } else {
            view = (ViewHolder) convertView.getTag();
        }
        view.txtPhonemeScore.setTag(score);

        view.txtPhonemeScore.setText(score.getName());
        int sdk = android.os.Build.VERSION.SDK_INT;
        Drawable drawable;
        if (scoreVal >= 80.0f) {
            drawable = getContext().getResources().getDrawable(R.drawable.rounded_corner_color_green);
        } else if (scoreVal >= 45.0f) {
            drawable = getContext().getResources().getDrawable(R.drawable.rounded_corner_color_orange);
        } else {
            drawable = getContext().getResources().getDrawable(R.drawable.rounded_corner_color_red);
        }
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            view.txtPhonemeScore.setBackgroundDrawable(drawable);
        } else {
            view.txtPhonemeScore.setBackground(drawable);
        }
        return convertView;
    }

}
