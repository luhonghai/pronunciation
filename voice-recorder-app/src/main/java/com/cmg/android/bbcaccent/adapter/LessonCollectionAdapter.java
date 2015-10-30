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
import com.cmg.android.bbcaccent.data.dto.lesson.lessons.LessonCollection;
import com.cmg.android.bbcaccent.data.sqlite.lesson.LessonDBAdapterService;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;
import com.luhonghai.litedb.exception.LiteDatabaseException;

import java.util.Locale;

import butterknife.ButterKnife;

/**
 * Created by luhonghai on 22/10/2015.
 */
public class LessonCollectionAdapter extends CursorRecyclerViewAdapter<LessonCollectionAdapter.ViewHolder> {

    public interface OnSelectLessonCollection {
        void onSelectLessonCollection(LessonCollection lessonCollection);
        LessonCollection bindLessonCollectionData(LessonCollection lessonCollection);
    }

    private final  OnSelectLessonCollection onSelectLessonCollection;

    public LessonCollectionAdapter(Context context, Cursor cursor, OnSelectLessonCollection onSelectLessonCollection) {
        super(context, cursor);
        this.onSelectLessonCollection = onSelectLessonCollection;
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
            LessonCollection lessonCollection = onSelectLessonCollection.bindLessonCollectionData(
                    LessonDBAdapterService.getInstance().toObject(cursor, LessonCollection.class));
            holder.txtTitle.setText(lessonCollection.getName());
            int bgColor = R.color.app_light_aqua;
            int textColor = R.color.app_aqua;
            int scoreBgColor = R.color.app_gray;
            if (lessonCollection.getScore() >= 0) {
                holder.txtScore.setText(String.format(Locale.getDefault(), "%d", lessonCollection.getScore()));
                if (lessonCollection.getScore() >= 80) {
                    scoreBgColor = R.color.app_green;
                } else if (lessonCollection.getScore() >= 45) {
                    scoreBgColor = R.color.app_orange;
                } else {
                    scoreBgColor = R.color.app_red;
                }
            }
            holder.cvItemContainer.setTag(lessonCollection);
            holder.cvScoreContainer.setCardBackgroundColor(MainApplication.getContext().getResources().getColor(scoreBgColor));
            holder.cvItemContainer.setCardBackgroundColor(MainApplication.getContext().getResources().getColor(bgColor));
            holder.txtTitle.setTextColor(MainApplication.getContext().getResources().getColor(textColor));
            holder.cvItemContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LessonCollection l = (LessonCollection) view.getTag();
                    onSelectLessonCollection.onSelectLessonCollection(l);
                }
            });
        } catch (LiteDatabaseException e) {
            SimpleAppLog.error("Could not parse lesson collection data",e);
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
