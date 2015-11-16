package com.cmg.android.bbcaccent.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.view.cardview.CircleCardView;

import butterknife.ButterKnife;

/**
 * Created by luhonghai on 05/11/2015.
 */
public class QuestionViewHolder extends RecyclerView.ViewHolder {

    public CircleCardView cardView;

    public TextView txtScore;

    public QuestionViewHolder(View itemView) {
        super(itemView);
        cardView = ButterKnife.findById(itemView, R.id.cvItemContainer);
        txtScore = (TextView) itemView.findViewById(R.id.txtScore);
    }
}
