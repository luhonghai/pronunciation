package com.cmg.android.bbcaccent.adapter.viewholder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cmg.android.bbcaccent.R;

/**
 * Created by luhonghai on 05/11/2015.
 */
public class QuestionViewHolder extends RecyclerView.ViewHolder {

    public CardView cardView;

    public TextView txtScore;

    public QuestionViewHolder(View itemView) {
        super(itemView);
        cardView = (CardView) itemView.findViewById(R.id.cvItemContainer);
        txtScore = (TextView) itemView.findViewById(R.id.txtScore);
    }
}
