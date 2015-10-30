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
import com.cmg.android.bbcaccent.data.dto.lesson.level.LessonLevel;
import com.cmg.android.bbcaccent.data.sqlite.lesson.LessonDBAdapterService;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.luhonghai.litedb.exception.LiteDatabaseException;

import java.util.Locale;

import butterknife.ButterKnife;

/**
 * Created by luhonghai on 22/10/2015.
 */
public class LessonLevelAdapter extends CursorRecyclerViewAdapter<LessonLevelAdapter.ViewHolder> {

    public interface OnSelectLevel {
        void onSelectLevel(LessonLevel level);
        LessonLevel bindLevelData(LessonLevel level);
    }

    private final OnSelectLevel onSelectLevel;

    public LessonLevelAdapter(Context context, Cursor cursor, OnSelectLevel onSelectLevel) {
        super(context, cursor);
        this.onSelectLevel = onSelectLevel;
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
            LessonLevel level = onSelectLevel.bindLevelData(
                    LessonDBAdapterService.getInstance().toObject(cursor, LessonLevel.class));
            holder.txtTitle.setText(level.getName());
            int bgColor = R.color.app_light_gray;
            int textColor = R.color.app_gray;
            int scoreBgColor = R.color.app_gray;
            if (level.isActive() || level.isDemo()) {
                bgColor = R.color.app_light_aqua;
                textColor = R.color.app_aqua;
                if (level.getScore() >= 0) {
                    holder.txtScore.setText(String.format(Locale.getDefault(), "%d", level.getScore()));
                    if (level.getScore() >= 80) {
                        scoreBgColor = R.color.app_green;
                    } else if (level.getScore() >= 45) {
                        scoreBgColor = R.color.app_orange;
                    } else {
                        scoreBgColor = R.color.app_red;
                    }
                }
            }
            holder.cvItemContainer.setTag(level);
            holder.cvScoreContainer.setCardBackgroundColor(MainApplication.getContext().getResources().getColor(scoreBgColor));
            holder.cvItemContainer.setCardBackgroundColor(MainApplication.getContext().getResources().getColor(bgColor));
            holder.txtTitle.setTextColor(MainApplication.getContext().getResources().getColor(textColor));
            holder.cvItemContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LessonLevel l = (LessonLevel) view.getTag();
                    if (l.isActive() || l.isDemo()) {
                        onSelectLevel.onSelectLevel(l);
                    }
                }
            });
        } catch (LiteDatabaseException e) {
            SimpleAppLog.error("Could not get lesson level from database",e);
        }
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
