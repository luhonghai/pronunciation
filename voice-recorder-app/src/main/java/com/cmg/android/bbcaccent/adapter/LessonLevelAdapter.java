package com.cmg.android.bbcaccent.adapter;

import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cmg.android.bbcaccent.MainActivity;
import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.broadcast.MainBroadcaster;
import com.cmg.android.bbcaccent.fragment.DetailFragment;
import com.cmg.android.bbcaccent.fragment.lesson.LessonObjectiveFragment;

import java.util.Locale;

import butterknife.ButterKnife;

/**
 * Created by luhonghai on 22/10/2015.
 */
public class LessonLevelAdapter extends RecyclerView.Adapter<LessonLevelAdapter.ViewHolder> {

    enum Level {
        DEMO("demo", 99, true),
        COUNTRY("country", 70, true),
        FOUNDATION("foundation", 23, true),
        LOWER_INTERMEDIATE("lower intermediate", 0, false),
        UPPER_INTERMEDIATE("upper intermediate", 0, false),
        ADVANCED("advanced", 0, false)
        ;
        String title;
        int score;
        boolean active;
        Level(){}
        Level(String title, int score, boolean active) {
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
        Level level = Level.values()[position];
        holder.txtTitle.setText(level.title);
        int bgColor = R.color.app_light_gray;
        int textColor = R.color.app_gray;
        int scoreBgColor = R.color.app_gray;
        if (level.active) {
            bgColor = R.color.app_light_aqua;
            textColor = R.color.app_aqua;
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
                Level l = (Level) view.getTag();
                if (l.active) {
                    MainActivity.SwitchFragmentParameter parameter
                            = new MainActivity.SwitchFragmentParameter(true, true, true);
                    parameter.setTitle(l.title);
                    MainBroadcaster.getInstance().getSender().sendSwitchFragment(
                            LessonObjectiveFragment.class,
                            new MainActivity.SwitchFragmentParameter(true, true, true),
                            null);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return Level.values().length;
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
