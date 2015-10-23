package com.cmg.android.bbcaccent.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.R;

import java.util.Locale;

import butterknife.ButterKnife;

/**
 * Created by luhonghai on 22/10/2015.
 */
public class LessonObjectiveAdapter extends RecyclerView.Adapter<LessonObjectiveAdapter.ViewHolder> {

    enum Objective {
        FINAL_CONSONANTS("final consonants", 19, true),
        VOWEL_SOUNDS("vowel sounds", 47, true),
        STRESS_PATTERNS("stress patterns", 87, true),
        INTONATION("intonation", 0, false),
        PACE_AND_SPEED("pace and speed", 47, true)
        ;
        String title;
        int score;
        boolean active;
        Objective(){}
        Objective(String title, int score, boolean active) {
            this.title = title;
            this.score = score;
            this.active = active;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lesson_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Objective level = Objective.values()[position];
        holder.txtTitle.setText(level.title);
        int bgColor = R.color.app_light_aqua;
        int textColor = R.color.app_aqua;
        int scoreBgColor = R.color.app_gray;
        if (level.active) {
            holder.txtScore.setText(String.format(Locale.getDefault(), "%d", level.score));
            if (level.score >= 80) {
                scoreBgColor = R.color.app_green;
            } else if (level.score >= 45) {
                scoreBgColor = R.color.app_orange;
            } else {
                scoreBgColor = R.color.app_red;
            }
        }
        holder.cvItemContainer.setTag(level);
        holder.cvScoreContainer.setCardBackgroundColor(MainApplication.getContext().getResources().getColor(scoreBgColor));
        holder.cvItemContainer.setCardBackgroundColor(MainApplication.getContext().getResources().getColor(bgColor));
        holder.txtTitle.setTextColor(MainApplication.getContext().getResources().getColor(textColor));
        holder.cvItemContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Objective l = (Objective) view.getTag();
                if (l.active) {

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return Objective.values().length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cvItemContainer;
        CardView cvScoreContainer;
        TextView txtTitle;
        TextView txtScore;
        public ViewHolder(View itemView) {
            super(itemView);
            cvItemContainer = ButterKnife.findById(itemView, R.id.cvItemContainer);
            cvScoreContainer = ButterKnife.findById(itemView, R.id.cvScoreContainer);
            txtScore = ButterKnife.findById(itemView, R.id.txtScore);
            txtTitle = ButterKnife.findById(itemView, R.id.txtTitle);
        }
    }
}
