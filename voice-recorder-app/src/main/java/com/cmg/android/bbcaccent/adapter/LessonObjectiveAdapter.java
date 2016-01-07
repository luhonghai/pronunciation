package com.cmg.android.bbcaccent.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.data.dto.lesson.objectives.Objective;
import com.cmg.android.bbcaccent.data.sqlite.lesson.LessonDBAdapterService;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.cmg.android.bbcaccent.view.cardview.CircleCardView;
import com.luhonghai.litedb.exception.LiteDatabaseException;

import java.util.Locale;

import butterknife.ButterKnife;

/**
 * Created by luhonghai on 22/10/2015.
 */
public class LessonObjectiveAdapter extends CursorRecyclerViewAdapter<LessonObjectiveAdapter.ViewHolder> {

    public interface OnSelectObjective {
        void onSelectObjective(Objective objective);
        Objective bindObjectiveData(Objective objective);
    }

    private final  OnSelectObjective onSelectObjective;

    public LessonObjectiveAdapter(Context context, Cursor cursor, OnSelectObjective onSelectObjective) {
        super(context, cursor);
        this.onSelectObjective = onSelectObjective;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lesson_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        try {
            Objective objective = onSelectObjective.bindObjectiveData(
                    LessonDBAdapterService.getInstance().toObject(cursor, Objective.class));
            holder.txtTitle.setText(objective.getName());
            int bgColor = R.color.app_light_aqua;
            int textColor = R.color.app_aqua;
            int scoreBgColor = R.color.app_gray;
            String scoreText = "";
            if (objective.getScore() >= 0) {
                scoreText = String.format(Locale.getDefault(), "%d", objective.getScore());
                if (objective.getScore() >= 80) {
                    scoreBgColor = R.color.app_green;
                } else if (objective.getScore() >= 45) {
                    scoreBgColor = R.color.app_orange;
                } else {
                    scoreBgColor = R.color.app_red;
                }
            }
            holder.txtScore.setText(scoreText);
            holder.cvItemContainer.setTag(objective);
            holder.cvScoreContainer.setCardBackgroundColor(MainApplication.getContext().getResources().getColor(scoreBgColor));
            holder.cvItemContainer.setCardBackgroundColor(MainApplication.getContext().getResources().getColor(bgColor));
            holder.txtTitle.setTextColor(MainApplication.getContext().getResources().getColor(textColor));
            holder.cvItemContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Objective l = (Objective) view.getTag();
                    onSelectObjective.onSelectObjective(l);
                }
            });
        } catch (LiteDatabaseException e) {
            SimpleAppLog.error("Could not parse objective data",e);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cvItemContainer;
        CircleCardView cvScoreContainer;
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
