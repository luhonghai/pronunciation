package com.cmg.android.bbcaccent.fragment.lesson;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cmg.android.bbcaccent.MainApplication;
import com.cmg.android.bbcaccent.R;
import com.cmg.android.bbcaccent.adapter.LessonLevelAdapter;
import com.cmg.android.bbcaccent.adapter.LessonObjectiveAdapter;
import com.cmg.android.bbcaccent.fragment.BaseFragment;
import com.cmg.android.bbcaccent.view.RecordingView;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by luhonghai on 22/10/2015.
 */
public class LessonObjectiveFragment extends BaseFragment {

    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    @Bind(R.id.cvItemContainer)
    CardView cvItemContainer;

    @Bind(R.id.cvScoreContainer)
    CardView cvScoreContainer;

    @Bind(R.id.txtTitle)
    TextView txtTitle;

    @Bind(R.id.txtScore)
    TextView txtScore;

    @Bind(R.id.main_recording_view)
    RecordingView recordingView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_objective, null);
        ButterKnife.bind(this, root);
        recyclerView.setAdapter(new LessonObjectiveAdapter());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        showTestScore(79);
        return root;
    }

    private void showTestScore(final int score) {
        int scoreBgColor;
        if (score >= 80) {
            scoreBgColor = R.color.app_green;
        } else if (score >= 45) {
            scoreBgColor = R.color.app_orange;
        } else {
            scoreBgColor = R.color.app_red;
        }
        cvScoreContainer.setCardBackgroundColor(
                MainApplication.getContext().getResources().getColor(scoreBgColor));
        cvItemContainer.setCardBackgroundColor(
                MainApplication.getContext().getResources().getColor(R.color.app_light_purple));
        txtTitle.setTextColor(MainApplication.getContext().getResources().getColor(R.color.app_purple));
        txtTitle.setText("test");
        txtScore.setText(String.format(Locale.getDefault(), "%d", score));

        recordingView.postDelayed(new Runnable() {
            @Override
            public void run() {
                recordingView.recycleView();
                recordingView.startPingAnimation(getActivity(), 0, score, true, false);
            }
        }, 100);
        recordingView.setAnimationListener(new RecordingView.OnAnimationListener() {
            @Override
            public void onAnimationMax() {
                recordingView.stopPingAnimation();
            }

            @Override
            public void onAnimationMin() {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
